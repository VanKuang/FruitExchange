package cn.vanchee.service;

import cn.vanchee.dao.InDetailDao;
import cn.vanchee.model.InDetail;
import cn.vanchee.model.PaidVo;
import cn.vanchee.model.Resource;
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
public class InDetailService {

    private static final Logger log = LoggerFactory.getLogger(InDetailService.class);

    private InDetailDao inDetailDao = null;

    public InDetailService() {
        inDetailDao = new InDetailDao();
    }

    public boolean createTable() {
        return inDetailDao.createTable();
    }

    public boolean create(InDetail inDetail) {
        inDetail.setUid(MyFactory.getCurrentUser().getId());

        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            inDetail.setCensored(Constants.CENSORED_PASS);
        }

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + inDetail);

        return inDetailDao.create(inDetail);
    }

    public boolean delete(int id) {
        InDetail inDetail = inDetailDao.find(id);
        boolean flag = inDetailDao.delete(id);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + inDetail);
        }
        return flag;
    }

    public boolean update(InDetail inDetail) {
        int id = inDetail.getId();
        boolean flag = false;
        InDetail old = inDetailDao.find(id);
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            inDetail.setCensored(Constants.CENSORED_PASS);
        } else {
            inDetail.setCensored(Constants.CENSORED_ORIGINAL);
        }
        flag = inDetailDao.update(inDetail);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + old
                    + " to " + inDetail);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        boolean flag;
        InDetail inDetail = inDetailDao.find(id);
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            inDetail.setCensored(Constants.CENSORED_PASS);
        } else {
            inDetail.setCensored(Constants.CENSORED_ORIGINAL);
        }
        flag = inDetailDao.update(inDetail);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + inDetail);
        }
        return flag;
    }

    public InDetail getInDetailById(int id) {
        return inDetailDao.find(id);
    }

    public List<InDetail> queryInDetail(int id, int oid, int fid, int censored, Date from, Date to, int uid) {
        return inDetailDao.queryInDetail(id, oid, fid, censored, from, to, uid);
    }

    public List<InDetail> selectCensoredReverse(List<InDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((InDetail) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    public List<PaidVo> getUnPaidOutDetail(int oid, Date from, Date to, int uid) {
        List<InDetail> result = inDetailDao.queryInDetail(-1, oid, -1, -1, from, to, uid);

        //Collections.sort(result);
        //Collections.reverse(result);
        Map<Integer, Double> shouldPaid = new HashMap<Integer, Double>();
        Map<Integer, Double> hadPaid = new HashMap<Integer, Double>();
        Map<Integer, String> dateMap = new HashMap<Integer, String>();
        Map<Integer, String> fruitMap = new HashMap<Integer, String>();
        Map<Integer, String> ownerMap = new HashMap<Integer, String>();
        for (InDetail inDetail : result) {
            int id = inDetail.getId();
            if (shouldPaid.get(id) != null) {
                shouldPaid.put(id, shouldPaid.get(id) + inDetail.getMoney());
            } else {
                shouldPaid.put(id, inDetail.getMoney());
                dateMap.put(id, inDetail.getCreateAt());
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
                paidVo.setCreateAt(dateMap.get(key));
                list.add(paidVo);
            }
        }

        return list;
    }

}
