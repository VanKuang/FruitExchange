package cn.vanchee.service;

import cn.vanchee.dao.ConsumptionDao;
import cn.vanchee.model.Consumption;
import cn.vanchee.model.Resource;
import cn.vanchee.util.Constants;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class ConsumptionService {

    private static Logger log = LoggerFactory.getLogger(ConsumptionService.class);

    private ConsumptionDao consumptionDao;

    public ConsumptionService() {
        consumptionDao = new ConsumptionDao();
    }

    public boolean createTable() {
        return consumptionDao.createTable();
    }

    public boolean create(Consumption consumption) {
        consumption.setUid(MyFactory.getCurrentUserId());
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            consumption.setCensored(Constants.CENSORED_PASS);
        }

        log.debug(MyFactory.getUserService().getCurrentUserName() + " create " + consumption.toString());
        return consumptionDao.create(consumption);
    }

    public boolean delete(int id) {
        Consumption old = consumptionDao.find(id);
        boolean flag = consumptionDao.delete(id);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + old.toString());
        }
        return flag;
    }

    public boolean update(Consumption consumption) {
        int id = consumption.getId();
        Consumption old = consumptionDao.find(id);
        if (MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            consumption.setCensored(Constants.CENSORED_PASS);
        } else {
            consumption.setCensored(Constants.CENSORED_ORIGINAL);
        }
        boolean flag = consumptionDao.update(consumption);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName()
                    + " update " + old
                    + " to " + consumption);
        }
        return flag;
    }

    public boolean censored(int id, boolean pass) {
        Consumption p = consumptionDao.find(id);
        p.setCensored(pass ? Constants.CENSORED_PASS : Constants.CENSORED_REFUSE);
        boolean flag = consumptionDao.update(p);
        if (flag) {
            log.debug(MyFactory.getUserService().getCurrentUserName() + " censored " + p);
        }
        return flag;
    }

    public Consumption getConsumption(int id) {
        return consumptionDao.find(id);
    }

    public List<Consumption> queryConsumption(Date from, Date to, int uid) {
        return consumptionDao.queryConsumption(from, to, uid);
    }

    public List<Consumption> selectCensoredReverse(List<Consumption> list, int censored) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            if (censored == ((Consumption) iterator.next()).getCensored()) {
                iterator.remove();
            }
        }
        return list;
    }

}
