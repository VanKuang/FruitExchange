package cn.vanchee.service;

import cn.vanchee.model.MyPaid;
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
public class MyPaidService {

    private static Logger log = LoggerFactory.getLogger(MyPaidService.class);

    private List<MyPaid> myPaidList;
    private int id;

    public MyPaidService() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.debug("start init my paid detail data");

        myPaidList = (List<MyPaid>) DataUtil.readListFromFile(Constants.FILE_NAME_MY_PAID);
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            myPaidList = queryMyPaid(-1, -1, -1, -1, -1, -1, -1, MyFactory.getCurrentUserId());
        }
        Collections.sort(myPaidList);
        id = myPaidList.size() + 1;

        long end = System.currentTimeMillis();
        log.debug("end init my paid detail data, use time:" + (end - start) + "ms" +
                (myPaidList != null ? ",data size:" + myPaidList.size() : ""));
    }

    public boolean create(MyPaid myPaid) {
        checkData();
        myPaid.setId(id);
        myPaid.setUid(MyFactory.getCurrentUser().getId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            myPaid.setCensored(Constants.CENSORED_PASS);
        }
        myPaidList.add(0, myPaid);

        id++;

        updateFile();

        log.info(MyFactory.getUserService().getCurrentUserName() + " create " + myPaid);
        return true;
    }

    public boolean delete(int id) {
        checkData();
        boolean flag = false;
        MyPaid p = null;
        for (MyPaid od : myPaidList) {
            if (id == od.getId()) {
                p = od;
                myPaidList.remove(od);
                flag = true;
                break;
            }
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + p);
            updateFile();
        }
        return flag;
    }

    public boolean update(MyPaid myPaid) {
        checkData();
        int oid = myPaid.getId();
        int i = 0;
        boolean flag = false;
        MyPaid p = null;
        for (MyPaid od : myPaidList) {
            if (oid == od.getId()) {
                p = od;
                if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
                    myPaid.setCensored(Constants.CENSORED_PASS);
                } else {
                    myPaid.setCensored(Constants.CENSORED_ORIGINAL);
                }
                myPaidList.set(i, myPaid);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + p
                    + " to " + myPaid);
            updateFile();
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        checkData();
        int i = 0;
        boolean flag = false;
        MyPaid p = null;
        for (MyPaid od : myPaidList) {
            if (id == od.getId()) {
                p = od;
                p.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
                myPaidList.set(i, p);
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

    public List<MyPaid> queryMyPaid(int id, int iid, int ownerId, int fid, int censored, long from, long to, int uid) {
        checkData();
        List<MyPaid> result = new ArrayList<MyPaid>();
        result.addAll(myPaidList);
        if (id != -1) {
            result = selectMyPaid(result, id);
        }
        if (iid != -1) {
            result = selectInDetail(result, iid);
        }
        if (ownerId != -1) {
            result = selectOwner(result, ownerId);
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

    public List<MyPaid> selectCensoredReverse(List<MyPaid> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((MyPaid) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private void checkData() {
        if (myPaidList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_MY_PAID, myPaidList);
            }
        });
    }

    private List<MyPaid> selectMyPaid(List<MyPaid> list, int id) {
        List<MyPaid> result = new ArrayList<MyPaid>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            MyPaid myPaid = (MyPaid) iterator.next();
            if (id == myPaid.getId()) {
                result.add(myPaid);
            }
        }
        return result;
    }

    private List<MyPaid> selectInDetail(List<MyPaid> list, int iid) {
        List<MyPaid> result = new ArrayList<MyPaid>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            MyPaid myPaid = (MyPaid) iterator.next();
            if (iid == myPaid.getIid()) {
                result.add(myPaid);
            }
        }
        return result;
    }

    private List<MyPaid> selectOwner(List<MyPaid> list, int cid) {
        List<MyPaid> result = new ArrayList<MyPaid>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            MyPaid myPaid = (MyPaid) iterator.next();
            if (cid == myPaid.getOwnerId()) {
                result.add(myPaid);
            }
        }
        return result;
    }

    private List<MyPaid> selectFruit(List<MyPaid> list, int fid) {
        List<MyPaid> result = new ArrayList<MyPaid>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            MyPaid myPaid = (MyPaid) iterator.next();
            if (fid == myPaid.getFid()) {
                result.add(myPaid);
            }
        }
        return result;
    }

    private List<MyPaid> selectCensored(List<MyPaid> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored != ((MyPaid) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<MyPaid> selectDateFrom(List<MyPaid> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((MyPaid) iterator.next()).getDate() <= from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<MyPaid> selectDateEnd(List<MyPaid> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((MyPaid) iterator.next()).getDate() >= end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<MyPaid> selectUser(List<MyPaid> list, int uid) {
        List<MyPaid> result = new ArrayList<MyPaid>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            MyPaid myPaid = (MyPaid) iterator.next();
            if (uid == myPaid.getUid()) {
                result.add(myPaid);
            }
        }
        return result;
    }

}
