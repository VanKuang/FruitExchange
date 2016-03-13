package cn.vanchee.service;

import cn.vanchee.model.Consumption;
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
public class ConsumptionService {

    private static Logger log = LoggerFactory.getLogger(ConsumptionService.class);

    private List<Consumption> consumptionList;
    private int id;

    public List<Consumption> getConsumptionList() {
        return consumptionList;
    }

    public ConsumptionService() {
        init();
    }

    public void init() {
        long start = System.currentTimeMillis();
        log.debug("start init consumption data");

        consumptionList = (List<Consumption>) DataUtil.readListFromFile(Constants.FILE_NAME_CONSUMPTION);
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            consumptionList = queryConsumption(-1, -1, MyFactory.getCurrentUserId());
        }
        Collections.sort(consumptionList);
        id = consumptionList.size() + 1;

        long end = System.currentTimeMillis();
        log.debug("end init consumption data, use time:" + (end - start) + "ms" +
                (consumptionList != null ? ",data size:" + consumptionList.size() : ""));
    }

    public boolean create(Consumption consumption) {
        checkData();
        consumption.setId(id);
        consumption.setUid(MyFactory.getCurrentUserId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            consumption.setCensored(Constants.CENSORED_PASS);
        }
        consumptionList.add(0, consumption);
        id++;
        updateFile();

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + consumption.toString());
        return true;
    }

    public boolean delete(int id) {
        checkData();
        Consumption c = null;
        boolean flag = false;
        for (Consumption od : consumptionList) {
            if (id == od.getId()) {
                c = od;
                consumptionList.remove(od);
                flag = true;
                break;
            }
        }
        if (flag) {
            updateFile();
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + c.toString());
        }
        return flag;
    }

    public boolean update(Consumption consumption) {
        checkData();
        Consumption c = null;
        int oid = consumption.getId();
        int i = 0;
        boolean flag = false;
        for (Consumption od : consumptionList) {
            if (oid == od.getId()) {
                c = od;
                if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
                    consumption.setCensored(Constants.CENSORED_PASS);
                } else {
                    consumption.setCensored(Constants.CENSORED_ORIGINAL);
                }
                consumptionList.set(i, consumption);
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            updateFile();
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + c
                    + " to " + consumption);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        checkData();
        int i = 0;
        boolean flag = false;
        Consumption p = null;
        for (Consumption od : consumptionList) {
            if (id == od.getId()) {
                p = od;
                p.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
                consumptionList.set(i, p);
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

    public Consumption getConsumption(int id) {
        checkData();
        List<Consumption> result = new ArrayList<Consumption>();
        result.addAll(consumptionList);
        for (Consumption consumption : result) {
            if (id == consumption.getId()) {
                return consumption;
            }
        }
        return null;
    }

    public List<Consumption> queryConsumption(long from, long to, int uid) {
        checkData();
        List<Consumption> result = new ArrayList<Consumption>();
        result.addAll(consumptionList);
        if (result == null) {
            return new ArrayList<Consumption>();
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

    public List<Consumption> selectCensoredReverse(List<Consumption> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((Consumption) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

    private void checkData() {
        if (consumptionList == null) {
            init();
        }
    }


    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_CONSUMPTION, consumptionList);
            }
        });
    }

    private List<Consumption> selectDateFrom(List<Consumption> list, long from) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((Consumption) iterator.next()).getDate() < from) {
                iterator.remove();
            }
        }
        return list;
    }

    private List<Consumption> selectDateEnd(List<Consumption> list, long end) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (((Consumption) iterator.next()).getDate() > end) {
                iterator.remove();
            }
        }
        return list;
    }

    private static List<Consumption> selectUser(List<Consumption> list, int uid) {
        List<Consumption> result = new ArrayList<Consumption>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Consumption consumption = (Consumption) iterator.next();
            if (uid == consumption.getUid()) {
                result.add(consumption);
            }
        }
        return result;
    }

}
