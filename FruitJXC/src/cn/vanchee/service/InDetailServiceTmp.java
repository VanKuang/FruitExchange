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
public class InDetailServiceTmp {

    private static Logger log = LoggerFactory.getLogger(InDetailServiceTmp.class);

    private List<InDetail> inDetailList;
    private int id;

    public List<InDetail> getInDetailList() {
        return inDetailList;
    }

    public InDetailServiceTmp() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.info("start init in detail data");
        inDetailList = (List<InDetail>) DataUtil.readListFromFile(Constants.FILE_NAME_IN_DETAIL);
        Collections.sort(inDetailList);
        id = inDetailList.size() + 1;
        long end = System.currentTimeMillis();
        log.info("end init in detail data, use time:" + (end - start) + "ms" +
                (inDetailList != null ? ",data size:" + inDetailList.size() : ""));
    }

    public boolean create(InDetail inDetail) {
        checkData();
        inDetail.setId(id);
        inDetail.setUid(MyFactory.getUserService().getCurrentUser().getId());

        if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
            inDetail.setCensored(Constants.CENSORED_PASS);
        }

        inDetailList.add(0, inDetail);
        id++;
        updateFile();

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + inDetail.toString());
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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + i.toString());
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
                if (MyFactory.getResourceService().hasRight(MyFactory.getUserService().getCurrentUser(), Resource.CENSORED)) {
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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " update " + id.toString() +
                    " to " + inDetail.toString());
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
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + inDetail.toString());
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
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored == ((InDetail)iterator.next()).getCensored()) {
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
        Map<String, Double> shouldPaid = new HashMap<String, Double>();
        Map<String, Double> hadPaid = new HashMap<String, Double>();
        Map<String, Long> dateMap = new HashMap<String, Long>();
        Map<String, String> fruitMap = new HashMap<String, String>();
        Map<String, String> ownerMap = new HashMap<String, String>();
        for (InDetail inDetail : result) {
            int cid = inDetail.getOwner();
            int color = inDetail.getColor();
            String key = cid + "," + color;
            if (shouldPaid.get(key) != null) {
                shouldPaid.put(key, shouldPaid.get(key) + inDetail.getMoney());
            } else {
                shouldPaid.put(key, inDetail.getMoney());
                dateMap.put(key, inDetail.getDate());
                fruitMap.put(key, inDetail.getFruitName());
                ownerMap.put(key, inDetail.getOwnerName());
            }
            if (hadPaid.get(key) != null) {
                hadPaid.put(key, hadPaid.get(key) + inDetail.getPaidMoney());
            } else {
                hadPaid.put(key, inDetail.getPaidMoney());
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
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (id !=((InDetail)iterator.next()).getId()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectOwner(List<InDetail> list, int owner) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (owner !=((InDetail)iterator.next()).getOwner()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectFruit(List<InDetail> list, int fruit) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (fruit != ((InDetail)iterator.next()).getFruit()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectCensored(List<InDetail> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (censored != ((InDetail)iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectDateFrom(List<InDetail> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((InDetail)iterator.next()).getDate() < from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<InDetail> selectDateEnd(List<InDetail> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (((InDetail)iterator.next()).getDate() > end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<InDetail> selectUser(List<InDetail> list, int uid) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            if (uid != ((InDetail)iterator.next()).getUid()) {
                iterator.remove();
            }
        }
        return list;
    }

}
