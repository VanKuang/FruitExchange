package cn.vanchee.service;

import cn.vanchee.model.PaidDetail;
import cn.vanchee.model.Resource;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DataUtil;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            paidDetailList = queryPaidDetail(-1, -1, -1, -1, -1, -1, -1, -1, -1, MyFactory.getCurrentUserId());
        }
        Collections.sort(paidDetailList);
        id = paidDetailList.size() + 1;

        long end = System.currentTimeMillis();
        log.info("end init paid detail data, use time:" + (end - start) + "ms" +
                (paidDetailList != null ? ",data size:" + paidDetailList.size() : ""));
    }

    public boolean create(PaidDetail paidDetail) {
        checkData();
        paidDetail.setId(id);
        paidDetail.setUid(MyFactory.getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            paidDetail.setCensored(Constants.CENSORED_PASS);
        }
        paidDetailList.add(0, paidDetail);

        id++;

        updateFile();

        log.info(MyFactory.getUserService().getCurrentUserName() + " create " + paidDetail);

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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + p);
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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + p);

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
                if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " update " + p + " to " + paidDetail);

            OutDetailService outDetailService = MyFactory.getOutDetailService();
            outDetailService.paid(paidDetail);

            updateFile();
        }
        return flag;
    }

    /**
     * @param iid     in detail id
     * @param oid     out detail id
     * @param ownerId owner id
     * @param cid     consumer id
     * @param fid     fruit id
     * @param from    date from
     * @param to      date to
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
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((PaidDetail) iterator.next()).getCensored()) {
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
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (id == paidDetail.getId()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

    private List<PaidDetail> selectInDetail(List<PaidDetail> list, int iid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (iid == paidDetail.getIid()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

    private List<PaidDetail> selectOutDetail(List<PaidDetail> list, int oid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (oid == paidDetail.getOid()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

    private List<PaidDetail> selectOwner(List<PaidDetail> list, int ownerId) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (ownerId == paidDetail.getOwnerId()) {
                result.add(paidDetail);
            }
        }
        return result;
    }


    private List<PaidDetail> selectConsumer(List<PaidDetail> list, int cid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (cid == paidDetail.getCid()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

    private List<PaidDetail> selectFruit(List<PaidDetail> list, int fid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (fid == paidDetail.getFid()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

    private List<PaidDetail> selectCensored(List<PaidDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored != ((PaidDetail) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectDateFrom(List<PaidDetail> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((PaidDetail) iterator.next()).getDate() <= from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<PaidDetail> selectDateEnd(List<PaidDetail> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((PaidDetail) iterator.next()).getDate() >= end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<PaidDetail> selectUser(List<PaidDetail> list, int uid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (uid == paidDetail.getUid()) {
                result.add(paidDetail);
            }
        }
        return result;
    }

}
