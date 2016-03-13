package cn.vanchee.service;

import cn.vanchee.model.InDetail;
import cn.vanchee.model.PaidVo;
import cn.vanchee.model.Resource;
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
public class InDetailService {

    private static Logger log = LoggerFactory.getLogger(InDetailService.class);

    private List<InDetail> inDetailList;
    private int id;

    public List<InDetail> getInDetailList() {
        return inDetailList;
    }

    public InDetailService() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.debug("start init in detail data");

        inDetailList = (List<InDetail>) DataUtil.readListFromFile(Constants.FILE_NAME_IN_DETAIL);

        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            inDetailList = queryInDetail(-1, -1, -1, -1, -1, -1, MyFactory.getCurrentUserId());
        }

        Collections.sort(inDetailList);
        id = inDetailList.size() + 1;

        long end = System.currentTimeMillis();
        log.debug("end init in detail data, use time:" + (end - start) + "ms" +
                (inDetailList != null ? ",data size:" + inDetailList.size() : ""));
    }

    public boolean create(InDetail inDetail) {
        checkData();
        inDetail.setId(id);
        inDetail.setUid(MyFactory.getCurrentUser().getId());

        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            inDetail.setCensored(Constants.CENSORED_PASS);
        }

        inDetailList.add(0, inDetail);
        id++;
        updateFile();

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + inDetail);
        return true;
    }

    public boolean delete(int id) {
        checkData();
        boolean flag = false;
        InDetail i = null;
        for (InDetail od : inDetailList) {
            if (id == od.getId()) {
                i = od;
                inDetailList.remove(od);
                flag = true;
                break;
            }
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + i);
            updateFile();
        }
        return flag;
    }

    public boolean update(InDetail inDetail) {
        checkData();
        int oid = inDetail.getId();
        int i = 0;
        boolean flag = false;
        InDetail id = null;
        for (InDetail od : inDetailList) {
            if (oid == od.getId()) {
                id = od;
                if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
                    inDetail.setCensored(Constants.CENSORED_PASS);
                } else {
                    inDetail.setCensored(Constants.CENSORED_ORIGINAL);
                }
                inDetailList.set(i, inDetail);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + id
                    + " to " + inDetail);
            updateFile();
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        checkData();
        int i = 0;
        boolean flag = false;
        InDetail inDetail = null;
        for (InDetail od : inDetailList) {
            if (id == od.getId()) {
                inDetail = od;
                inDetail.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
                inDetailList.set(i, inDetail);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + inDetail);
            updateFile();
        }
        return flag;
    }

    public InDetail getInDetailById(int id) {
        for (InDetail inDetail : inDetailList) {
            if (id == inDetail.getId()) {
                return inDetail;
            }
        }
        return null;
    }

    public List<InDetail> queryInDetail(int id, int owner, int fruit, int censored, long from, long to, int uid) {
        checkData();
        List<InDetail> result = new ArrayList<InDetail>();
        result.addAll(inDetailList);
        if (result == null) {
            return new ArrayList<InDetail>();
        }
        if (id != -1) {
            result = selectId(result, id);
        }
        if (owner != -1) {
            result = selectOwner(result, owner);
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
        if (uid != -1) {
            result = selectUser(result, uid);
        }
        return result;
    }

    public List<InDetail> selectCensoredReverse(List<InDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((InDetail) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    public List<PaidVo> getUnPaidOutDetail(int ownerId, long from, long to, int uid) {
        checkData();
        List<InDetail> result = new ArrayList<InDetail>();
        result.addAll(inDetailList);
        if (ownerId != -1) {
            result = selectOwner(result, ownerId);
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

        Collections.sort(result);
        Collections.reverse(result);
        Map<Integer, Double> shouldPaid = new HashMap<Integer, Double>();
        Map<Integer, Double> hadPaid = new HashMap<Integer, Double>();
        Map<Integer, Long> dateMap = new HashMap<Integer, Long>();
        Map<Integer, String> fruitMap = new HashMap<Integer, String>();
        Map<Integer, String> ownerMap = new HashMap<Integer, String>();
        for (InDetail inDetail : result) {
            int id = inDetail.getId();
            if (shouldPaid.get(id) != null) {
                shouldPaid.put(id, shouldPaid.get(id) + inDetail.getMoney());
            } else {
                shouldPaid.put(id, inDetail.getMoney());
                dateMap.put(id, inDetail.getDate());
                fruitMap.put(id, inDetail.getFruitName());
                ownerMap.put(id, inDetail.getOwnerName());
            }
            if (hadPaid.get(id) != null) {
                hadPaid.put(id, hadPaid.get(id) + inDetail.getPaidMoney());
            } else {
                hadPaid.put(id, inDetail.getPaidMoney());
            }
        }
        List<PaidVo> list = new ArrayList<PaidVo>();
        for (Map.Entry<Integer, Double> m : shouldPaid.entrySet()) {
            int key = m.getKey();
            if (hadPaid.get(key) - m.getValue() < 0) {
                PaidVo paidVo = new PaidVo();
                paidVo.setOid(key);
                paidVo.setFruitName(fruitMap.get(key));
                paidVo.setName(ownerMap.get(key));
                paidVo.setShouldPaid(m.getValue());
                paidVo.setHadPaid(hadPaid.get(key));
                paidVo.setDate(dateMap.get(key));
                list.add(paidVo);
            }
        }

        return list;
    }

    private void checkData() {
        if (inDetailList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_IN_DETAIL, inDetailList);
            }
        });
    }

    private List<InDetail> selectId(List<InDetail> list, int id) {
        List<InDetail> result = new ArrayList<InDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            InDetail inDetail = (InDetail) iterator.next();
            if (id == inDetail.getId()) {
                result.add(inDetail);
            }
        }
        return result;
    }

    private List<InDetail> selectOwner(List<InDetail> list, int owner) {
        List<InDetail> result = new ArrayList<InDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            InDetail inDetail = (InDetail) iterator.next();
            if (owner == inDetail.getOwner()) {
                result.add(inDetail);
            }
        }
        return result;
    }

    private List<InDetail> selectFruit(List<InDetail> list, int fruit) {
        List<InDetail> result = new ArrayList<InDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            InDetail inDetail = (InDetail) iterator.next();
            if (fruit == inDetail.getFruit()) {
                result.add(inDetail);
            }
        }
        return result;
    }

    private List<InDetail> selectCensored(List<InDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored != ((InDetail) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectDateFrom(List<InDetail> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((InDetail) iterator.next()).getDate() < from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectDateEnd(List<InDetail> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((InDetail) iterator.next()).getDate() > end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<InDetail> selectUser(List<InDetail> list, int uid) {
        List<InDetail> result = new ArrayList<InDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            InDetail inDetail = (InDetail) iterator.next();
            if (uid == inDetail.getUid()) {
                result.add(inDetail);
            }
        }
        return result;
    }

}
