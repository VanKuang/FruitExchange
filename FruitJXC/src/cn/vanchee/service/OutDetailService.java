package cn.vanchee.service;

import cn.vanchee.model.*;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DataUtil;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class OutDetailService {

    private static Logger log = LoggerFactory.getLogger(OutDetailService.class);

    private List<OutDetail> outDetailList;
    private int id;

    public List<OutDetail> getOutDetailList() {
        return outDetailList;
    }


    public OutDetailService() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.info("start init out detail data");
        outDetailList = (List<OutDetail>) DataUtil.readListFromFile(Constants.FILE_NAME_OUT_DETAIL);
        Collections.sort(outDetailList);
        id = outDetailList.size() + 1;
        long end = System.currentTimeMillis();
        log.info("end init out detail data, use time:" + (end - start) + "ms" +
                (outDetailList != null ? ",data size:" + outDetailList.size() : ""));
    }

    public boolean create(OutDetail outDetail) {
        //需要减去相应的进货库存
        InDetailService inDetailService = MyFactory.getInDetailService();
        InDetail inDetail = inDetailService.getInDetailById(outDetail.getIid());
        if (inDetail != null) {
            inDetail.setSale(inDetail.getSale() + outDetail.getNum());
            inDetailService.update(inDetail);
        } else {
            return false;
        }

        checkData();
        outDetail.setId(id);
        outDetail.setUid(MyFactory.getUserService().getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
            outDetail.setCensored(Constants.CENSORED_PASS);
        }
        outDetailList.add(0, outDetail);

        id++;

        updateFile();

        log.info(MyFactory.getUserService().getCurrentUserName() + " create " + outDetail.toString());
        return true;
    }

    public boolean delete(int id) {

        // need add in detail sale number
        OutDetail old = getOutDetail(id);
        if (old != null) {
            InDetailService inDetailService = MyFactory.getInDetailService();
            InDetail inDetail = inDetailService.getInDetailById(old.getIid());
            if (inDetail != null) {
                inDetail.setSale(inDetail.getSale() - old.getNum());
                inDetailService.update(inDetail);
            } else {
                return false;
            }
        }

        checkData();
        boolean flag = false;
        OutDetail o = null;
        for (OutDetail od : outDetailList) {
            if (id == od.getId()) {
                o = od;
                outDetailList.remove(od);
                flag = true;
                break;
            }
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + o.toString());
            updateFile();
        }
        return flag;
    }

    public boolean update(OutDetail outDetail) {

        // first need find the old out detail
        // if sale number had change, need update in detail
        OutDetail old = getOutDetail(outDetail.getId());
        if (old.getNum() != outDetail.getNum()) {
            InDetailService inDetailService = MyFactory.getInDetailService();
            InDetail inDetail = inDetailService.getInDetailById(outDetail.getIid());
            if (inDetail != null) {
                inDetail.setSale(inDetail.getSale() - old.getNum() + outDetail.getNum());
                inDetailService.update(inDetail);
            } else {
                return false;
            }
        }

        checkData();
        int oid = outDetail.getId();
        int i = 0;
        boolean flag = false;
        OutDetail o = null;
        for (OutDetail od : outDetailList) {
            if (oid == od.getId()) {
                o = od;
                if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
                    outDetail.setCensored(Constants.CENSORED_PASS);
                } else {
                    outDetail.setCensored(Constants.CENSORED_ORIGINAL);
                }
                outDetailList.set(i, outDetail);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " update " + o.toString() +
                    " to " + outDetail.toString());
            updateFile();
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        checkData();
        int i = 0;
        boolean flag = false;
        OutDetail o = null;
        for (OutDetail od : outDetailList) {
            if (id == od.getId()) {
                o = od;
                o.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
                outDetailList.set(i, o);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censor " + o.toString());
            updateFile();
        }
        return flag;
    }

    public void paid(PaidDetail paidDetail) {
        OutDetail outDetail = getOutDetail(paidDetail.getOid());
        if (outDetail != null) {
            if (outDetail.getPaidMoneyIncludeDiscount() >= outDetail.getPrice() * outDetail.getNum()) {
                outDetail.setStatus(Constants.OUT_STATUS_PAID_ENOUGH);
                backup(outDetail);
            } else if (outDetail.getPaidMoneyIncludeDiscount() == 0) {
                outDetail.setStatus(Constants.OUT_STATUS_ORIGINAL);
                update(outDetail);
            } else {
                outDetail.setStatus(Constants.OUT_STATUS_PAID_NOT_ENOUGH);
                update(outDetail);
            }
        }
    }

    /**
     * @param id
     * @return
     */
    public OutDetail getOutDetail(int id) {
        checkData();
        List<OutDetail> result = new ArrayList<OutDetail>();
        result.addAll(outDetailList);
        for (OutDetail outDetail : result) {
            if (id == outDetail.getId()) {
                return outDetail;
            }
        }
        return null;
    }

    /**
     * @param owner
     * @param consumer
     * @param fruit
     * @param from
     * @param to
     * @param status
     * @return
     */
    public List<OutDetail> queryOutDetail(int id, int iid, int owner, int consumer, int fruit, int censored,
                                          long from, long to, int status, int uid) {
        checkData();
        List<OutDetail> result = new ArrayList<OutDetail>();
        result.addAll(outDetailList);
        if (id != -1) {
            result = selectOutDetail(result, id);
        }
        if (iid != -1) {
            result = selectInDetail(result, iid);
        }
        if (owner != -1) {
            result = selectOwner(result, owner);
        }
        if (consumer != -1) {
            result = selectConsumer(result, consumer);
        }
        if (fruit != -1) {
            result = selectFruit(result, fruit);
        }
        if (censored != -1) {
            result = selectCensored(result, censored);
        }
        if (from != -1) {
            result = selectDateFrom(result, from);
        }
        if (to != -1) {
            result = selectDateEnd(result, to);
        }
        if (status != -1) {
            result = selectStatus(result, status);
        }
        if (uid != -1) {
            result = selectUser(result, uid);
        }
        return result;
    }

    public List<OutDetail> selectCensoredReverse(List<OutDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored == ((OutDetail)iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    public List<PaidVo> getUnPaidOutDetail(int consumerId, long from, long to, int uid) {
        checkData();
        List<OutDetail> result = new ArrayList<OutDetail>();
        result.addAll(outDetailList);
        if (consumerId != -1) {
            result = selectConsumer(result, consumerId);
        }
        if (from != -1) {
            result = selectDateFrom(result, from);
        }
        if (to != -1) {
            result = selectDateEnd(result, to);
        }
        if (uid != -1) {
            result = selectUser(result, uid);
        }
        //result = selectReverseStatus(result, Constants.OUT_STATUS_PAID_ENOUGH);

        Collections.sort(result);
        Collections.reverse(result);
        Map<String, Double> shouldPaid = new HashMap<String, Double>();
        Map<String, Double> hadPaid = new HashMap<String, Double>();
        Map<String, Long> dateMap = new HashMap<String, Long>();
        Map<String, String> fruitMap = new HashMap<String, String>();
        Map<String, String> consumerMap = new HashMap<String, String>();
        for (OutDetail outDetail : result) {
            int cid = outDetail.getCid();
            int color = outDetail.getColor();
            String key = cid + "," + color;
            if (shouldPaid.get(key) != null) {
                shouldPaid.put(key, shouldPaid.get(key) + outDetail.getMoney());
            } else {
                shouldPaid.put(key, outDetail.getMoney());
                dateMap.put(key, outDetail.getDate());
                fruitMap.put(key, outDetail.getFruitName());
                consumerMap.put(key, outDetail.getConsumerName());
            }
            if (hadPaid.get(key) != null) {
                hadPaid.put(key, hadPaid.get(key) + outDetail.getPaidMoneyIncludeDiscount());
            } else {
                hadPaid.put(key, outDetail.getPaidMoneyIncludeDiscount());
            }
        }
        List<PaidVo> list = new ArrayList<PaidVo>();
        for (Map.Entry<String, Double> m : shouldPaid.entrySet()) {
            String key = m.getKey();
            String[] keys = m.getKey().split(",");
            int id = Integer.valueOf(keys[0]);
            int color = Integer.valueOf(keys[1]);
            if (hadPaid.get(key) - m.getValue() < 0) {
                PaidVo paidVo = new PaidVo();
                paidVo.setOid(id);
                paidVo.setColor(color);
                paidVo.setFruitName(fruitMap.get(key));
                paidVo.setName(consumerMap.get(key));
                paidVo.setShouldPaid(m.getValue());
                paidVo.setHadPaid(hadPaid.get(key));
                paidVo.setDate(dateMap.get(key));
                list.add(paidVo);
            }
        }

        return list;
    }

    private void checkData() {
        if (outDetailList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_OUT_DETAIL, outDetailList);
            }
        });
    }

    /**
     * @param outDetail
     */
    private synchronized void backup(final OutDetail outDetail) {
        //delete(outDetail.getId());

        //update
        update(outDetail);

        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<OutDetailHistory> backup = DataUtil.readListFromFile(Constants.OUT_DETAIL_BACKUP_FILE_NAME);
                OutDetailHistory history = new OutDetailHistory();
                history.setId(outDetail.getId());
                history.setDate(outDetail.getDate());
                history.setConsumer(outDetail.getCid());
                history.setNum(outDetail.getNum());
                history.setStatus(outDetail.getStatus());
                history.setBackupDate(new Date().getTime());
                backup.add(history);
                DataUtil.writeListToFile(Constants.OUT_DETAIL_BACKUP_FILE_NAME, backup);
            }
        });
    }

    private List<OutDetail> selectOutDetail(List<OutDetail> list, int oid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (oid != ((OutDetail)iterator.next()).getId()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectInDetail(List<OutDetail> list, int iid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (iid != ((OutDetail)iterator.next()).getIid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectOwner(List<OutDetail> list, int owner) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (owner !=((OutDetail)iterator.next()).getOwnerId()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectConsumer(List<OutDetail> list, int cid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (cid != ((OutDetail)iterator.next()).getCid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectFruit(List<OutDetail> list, int fruit) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (fruit != ((OutDetail)iterator.next()).getFruitId()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectCensored(List<OutDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored != ((OutDetail)iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectDateFrom(List<OutDetail> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((OutDetail)iterator.next()).getDate() < from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectDateEnd(List<OutDetail> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((OutDetail)iterator.next()).getDate() > end) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectStatus(List<OutDetail> list, int status) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((OutDetail)iterator.next()).getStatus() != status) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<OutDetail> selectReverseStatus(List<OutDetail> list, int status) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((OutDetail)iterator.next()).getStatus() == status) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<OutDetail> selectUser(List<OutDetail> list, int uid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (uid != ((OutDetail)iterator.next()).getUid()) {
                iterator.remove();
            }
        }
        return list;
    }
}
