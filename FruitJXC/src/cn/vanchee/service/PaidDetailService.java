package cn.vanchee.service;

import cn.vanchee.model.PaidDetail;
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
public class PaidDetailService {

    private static Logger log = LoggerFactory.getLogger(PaidDetailService.class);

    private List<PaidDetail> paidDetailList;
    private int id;

    public List<PaidDetail> getPaidDetailList() {
        return paidDetailList;
    }

    public PaidDetailService() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.info("start init paid detail data");
        paidDetailList = (List<PaidDetail>) DataUtil.readListFromFile(Constants.FILE_NAME_PAID_DETAIL);
        Collections.sort(paidDetailList);
        id = paidDetailList.size() + 1;
        long end = System.currentTimeMillis();
        log.info("end init paid detail data, use time:" + (end - start) + "ms" +
                (paidDetailList != null ? ",data size:" + paidDetailList.size() : ""));
    }

    public boolean create(PaidDetail paidDetail) {
        checkData();
        paidDetail.setId(id);
        paidDetail.setUid(MyFactory.getUserService().getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
            paidDetail.setCensored(Constants.CENSORED_PASS);
        }
        paidDetailList.add(0, paidDetail);

        id++;

        updateFile();

        log.info(MyFactory.getUserService().getCurrentUserName() + " create " + paidDetail.toString());

        OutDetailService outDetailService = MyFactory.getOutDetailService();
        outDetailService.paid(paidDetail);

        return true;
    }

    public boolean delete(int id) {
        checkData();
        boolean flag = false;
        PaidDetail p = null;
        for (PaidDetail od : paidDetailList) {
            if (id == od.getId()) {
                p = od;
                paidDetailList.remove(od);
                flag = true;
                break;
            }
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + p.toString());
            updateFile();

            OutDetailService outDetailService = MyFactory.getOutDetailService();
            outDetailService.paid(p);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        checkData();
        int i = 0;
        boolean flag = false;
        PaidDetail p = null;
        for (PaidDetail od : paidDetailList) {
            if (id == od.getId()) {
                p = od;
                p.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
                paidDetailList.set(i, p);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + p.toString());

            updateFile();
        }
        return flag;
    }

    public boolean update(PaidDetail paidDetail) {
        checkData();
        int oid = paidDetail.getId();
        int i = 0;
        boolean flag = false;
        PaidDetail p = null;
        for (PaidDetail od : paidDetailList) {
            if (oid == od.getId()) {
                p = od;
                if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
                    paidDetail.setCensored(Constants.CENSORED_PASS);
                } else {
                    paidDetail.setCensored(Constants.CENSORED_ORIGINAL);
                }
                paidDetailList.set(i, paidDetail);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " update " + p.toString() +
                    " to " + paidDetail.toString());

            OutDetailService outDetailService = MyFactory.getOutDetailService();
            outDetailService.paid(paidDetail);

            updateFile();
        }
        return flag;
    }

    /**
     * @param iid    in detail id
     * @param oid    out detail id
     * @param ownerId owner id
     * @param cid    consumer id
     * @param fid    fruit id
     * @param from   date from
     * @param to     date to
     * @return
     */
    public List<PaidDetail> queryPaidDetail(int id, int iid, int oid, int ownerId, int cid, int fid, int censored,
                                            long from, long to, int uid) {
        checkData();
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        result.addAll(paidDetailList);
        if (id != -1) {
            result = selectPaid(result, id);
        }
        if (iid != -1) {
            result = selectInDetail(result, iid);
        }
        if (oid != -1) {
            result = selectOutDetail(result, oid);
        }
        if (ownerId != -1) {
            result = selectOwner(result, ownerId);
        }
        if (cid != -1) {
            result = selectConsumer(result, cid);
        }
        if (fid != -1) {
            result = selectFruit(result, fid);
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

    public List<PaidDetail> selectCensoredReverse(List<PaidDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored == ((PaidDetail)iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private void checkData() {
        if (paidDetailList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_PAID_DETAIL, paidDetailList);
            }
        });
    }

    private List<PaidDetail> selectPaid(List<PaidDetail> list, int id) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (id != ((PaidDetail)iterator.next()).getId()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectInDetail(List<PaidDetail> list, int iid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (iid != ((PaidDetail)iterator.next()).getIid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectOutDetail(List<PaidDetail> list, int oid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (oid != ((PaidDetail)iterator.next()).getOid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectOwner(List<PaidDetail> list, int ownerId) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (ownerId != ((PaidDetail)iterator.next()).getOwnerId()) {
                iterator.remove();
            }
        }
        return list;
    }


    private List<PaidDetail> selectConsumer(List<PaidDetail> list, int cid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (cid != ((PaidDetail)iterator.next()).getCid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectFruit(List<PaidDetail> list, int fid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (fid != ((PaidDetail)iterator.next()).getFid()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectCensored(List<PaidDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored != ((PaidDetail)iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectDateFrom(List<PaidDetail> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((PaidDetail)iterator.next()).getDate() <= from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectDateEnd(List<PaidDetail> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((PaidDetail)iterator.next()).getDate() >= end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<PaidDetail> selectUser(List<PaidDetail> list, int uid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (uid != ((PaidDetail)iterator.next()).getUid()) {
                iterator.remove();
            }
        }
        return list;
    }

}
