package cn.vanchee.service;

import cn.vanchee.dao.OutDetailDao;
import cn.vanchee.model.*;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class OutDetailService {

    private static final Logger log = LoggerFactory.getLogger(OutDetailService.class);

    private OutDetailDao outDetailDao;

    public OutDetailService() {
        outDetailDao = new OutDetailDao();
    }

    public boolean createTable() {
        return outDetailDao.createTable();
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

        outDetail.setUid(MyFactory.getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            outDetail.setCensored(Constants.CENSORED_PASS);
        }

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + outDetail);
        return outDetailDao.create(outDetail);
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

        boolean flag;
        OutDetail o = outDetailDao.find(id);
        flag = outDetailDao.delete(id);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + o);
        }
        return flag;
    }

    public boolean update(OutDetail outDetail) {

        // first need find the old out detail
        // if sale number had change, need update in detail
        int id = outDetail.getId();
        OutDetail old = outDetailDao.find(id);
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

        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            outDetail.setCensored(Constants.CENSORED_PASS);
        } else {
            outDetail.setCensored(Constants.CENSORED_ORIGINAL);
        }
        boolean flag = outDetailDao.update(outDetail);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + old + " to " + outDetail);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        boolean flag;
        OutDetail old = outDetailDao.find(id);
        old.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
        flag = outDetailDao.update(old);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censor " + old);
        }
        return flag;
    }

    public void paid(PaidDetail paidDetail) {
        OutDetail outDetail = getOutDetail(paidDetail.getOid());
        if (outDetail != null) {
            if (outDetail.getPaidMoneyIncludeDiscount() >= outDetail.getPrice() * outDetail.getNum()) {
                outDetail.setStatus(Constants.OUT_STATUS_PAID_ENOUGH);
            } else if (outDetail.getPaidMoneyIncludeDiscount() == 0) {
                outDetail.setStatus(Constants.OUT_STATUS_ORIGINAL);
            } else {
                outDetail.setStatus(Constants.OUT_STATUS_PAID_NOT_ENOUGH);
            }
        }
        backup(outDetail);
    }

    /**
     * @param id
     * @return
     */
    public OutDetail getOutDetail(int id) {
        return outDetailDao.find(id);
    }

    /**
     * @param oid
     * @param cid
     * @param fid
     * @param from
     * @param to
     * @param status
     * @return
     */
    public List<OutDetail> queryOutDetail(int id, int iid, int oid, int cid, int fid, int censored,
                                          Date from, Date to, int status, int uid) {
        List<OutDetail> result = outDetailDao.queryOutDetail(id, iid, cid, censored, from, to, status, uid);
        if (oid != -1) {
            result = selectOwner(result, oid);
        }
        if (fid != -1) {
            result = selectFruit(result, fid);
        }
        return result;
    }

    public List<OutDetail> selectCensoredReverse(List<OutDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((OutDetail) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    public List<PaidVo> getUnPaidOutDetail(int cid, Date from, Date to, int uid) {
        List<OutDetail> result = outDetailDao.queryOutDetail(-1, -1, cid, -1, from, to, -1, uid);
        //result = selectReverseStatus(result, Constants.OUT_STATUS_PAID_ENOUGH);

        //Collections.sort(result);
        Collections.reverse(result);
        Map<String, Double> shouldPaid = new HashMap<String, Double>();
        Map<String, Double> hadPaid = new HashMap<String, Double>();
        Map<String, String> dateMap = new HashMap<String, String>();
        Map<String, String> fruitMap = new HashMap<String, String>();
        Map<String, String> consumerMap = new HashMap<String, String>();
        for (OutDetail outDetail : result) {
            int cid1 = outDetail.getCid();
            int color = outDetail.getColor();
            String key = cid1 + "," + color;
            if (shouldPaid.get(key) != null) {
                shouldPaid.put(key, shouldPaid.get(key) + outDetail.getMoney());
            } else {
                shouldPaid.put(key, outDetail.getMoney());
                //dateMap.put(key, outDetail.getCreateAt());
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
                paidVo.setCreateAt(dateMap.get(key));
                list.add(paidVo);
            }
        }

        return list;
    }

    /**
     * @param outDetail
     */
    private synchronized void backup(final OutDetail outDetail) {
        //delete(outDetail.getId());

        //update
        update(outDetail);

        /*
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
        */
    }

    private List<OutDetail> selectOwner(List<OutDetail> list, int owner) {
        List<OutDetail> result = new ArrayList<OutDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            OutDetail outDetail = (OutDetail) iterator.next();
            if (owner == outDetail.getOwnerId()) {
                result.add(outDetail);
            }
        }
        return result;
    }

    private List<OutDetail> selectFruit(List<OutDetail> list, int fruit) {
        List<OutDetail> result = new ArrayList<OutDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            OutDetail outDetail = (OutDetail) iterator.next();
            if (fruit == outDetail.getFruitId()) {
                result.add(outDetail);
            }
        }
        return result;
    }

}
