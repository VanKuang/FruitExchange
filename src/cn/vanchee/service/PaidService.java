package cn.vanchee.service;

import cn.vanchee.dao.PaidDao;
import cn.vanchee.model.PaidDetail;
import cn.vanchee.model.Resource;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class PaidService {

    private static Logger log = LoggerFactory.getLogger(PaidService.class);

    private PaidDao paidDao;

    public PaidService() {
        paidDao = new PaidDao();
    }

    public boolean createTable() {
        return paidDao.createTable();
    }

    public boolean create(PaidDetail paidDetail) {
        paidDetail.setUid(MyFactory.getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            paidDetail.setCensored(Constants.CENSORED_PASS);
        }

        boolean flag = paidDao.create(paidDetail);
        if (flag) {
            log.info(MyFactory.getUserService().getCurrentUserName() + " create " + paidDetail);
            if (paidDetail.getDiscount() != -1) {
                OutDetailService outDetailService = MyFactory.getOutDetailService();
                outDetailService.paid(paidDetail);
            }
        }
        return flag;
    }

    public boolean delete(int id) {
        PaidDetail paidDetail = paidDao.find(id);
        boolean flag = paidDao.delete(id);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + paidDetail);
        }
        return flag;
    }

    public boolean update(PaidDetail paidDetail) {
        int id = paidDetail.getId();
        boolean flag = false;
        PaidDetail old = paidDao.find(id);
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            paidDetail.setCensored(Constants.CENSORED_PASS);
        } else {
            paidDetail.setCensored(Constants.CENSORED_ORIGINAL);
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + old
                    + " to " + paidDetail);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        boolean flag;
        PaidDetail paidDetail = paidDao.find(id);
        paidDetail.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
        flag = paidDao.update(paidDetail);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + paidDetail);
        }
        return flag;
    }

    /**
     * @param id
     * @param iid      进货ID
     * @param ownerId  货主
     * @param fid
     * @param censored
     * @param from
     * @param to
     * @param uid
     * @return
     */
    public List<PaidDetail> queryMyPaidDetail(int id, int iid, int ownerId, int fid, int censored,
                                              Date from, Date to, int uid) {
        List<PaidDetail> result = paidDao.queryMyPaidDetail(id, iid, censored, from, to, uid);
        if (ownerId != -1) {
            result = selectOwner(result, ownerId);
        }
        if (fid != -1) {
            result = selectFruit(result, fid);
        }
        return result;
    }

    /**
     * @param id
     * @param oid      销售ID
     * @param iid      进货ID
     * @param ownerId  货主
     * @param fid
     * @param censored
     * @param from
     * @param to
     * @param uid
     * @return
     */
    public List<PaidDetail> queryPaidDetail(int id, int iid, int oid, int ownerId, int fid, int censored,
                                            Date from, Date to, int uid) {
        List<PaidDetail> result = paidDao.queryPaidDetail(id, oid, censored, from, to, uid);
        if (iid != -1) {
            result = selectInDetail(result, iid);
        }
        if (ownerId != -1) {
            result = selectOwner(result, ownerId);
        }
        if (fid != -1) {
            result = selectFruit(result, fid);
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

    private List<PaidDetail> selectOwner(List<PaidDetail> list, int cid) {
        List<PaidDetail> result = new ArrayList<PaidDetail>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            PaidDetail paidDetail = (PaidDetail) iterator.next();
            if (cid == paidDetail.getOwnerId()) {
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

}
