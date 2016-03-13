package cn.vanchee.service;

import cn.vanchee.dao.ConsumerDao;
import cn.vanchee.model.Consumer;
import cn.vanchee.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date 13-5-5
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class ConsumerService {

    private List<Consumer> consumerList;
    private Map<Integer, String> consumerMap;

    private static ConsumerDao consumerDao = new ConsumerDao();

    public ConsumerService() {
        init();
        analyze();
    }

    public boolean createTable() {
        return consumerDao.createTable();
    }

    public String[] listNames() {
        checkData();
        String[] data = new String[consumerList.size()];
        int i = 0;
        for (Consumer consumer : consumerList) {
            data[i] = consumer.getName();
            i++;
        }
        return data;
    }

    public boolean isExist(String name) {
        return consumerDao.isExist(name);
    }

    public boolean create(String name) {
        if (isExist(name)) {
            return false;
        }
        Consumer consumer = new Consumer();
        consumer.setName(name);

        boolean flag = consumerDao.create(name);

        if (flag) {
            consumer = consumerDao.findByName(name);
            checkData();
            consumerList.add(0, consumer);
            updateMap(consumer, Constants.CREATE);
        }
        return flag;
    }

    public String getConsumerName(int id) {
        checkData();
        return consumerMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name.trim())) {
            return -1;
        }
        for (Consumer o : consumerList) {
            if (name.equals(o.getName())) {
                return o.getId();
            }
        }
        return -2;
    }

    public int getIdByName4Add(String name) {
        int cid = getIdByName4Query(name);
        if (cid == -2) {
            //需要新增记录
            create(name);
        }
        return cid;
    }

    private void init() {
        consumerList = consumerDao.getList();
    }

    private void analyze() {
        if (consumerList == null) {
            init();
        }
        consumerMap = new HashMap<Integer, String>();
        for (Consumer consumer : consumerList) {
            consumerMap.put(consumer.getId(), consumer.getName());
        }
    }

    private void checkData() {
        if (consumerList == null) {
            init();
        }
        if (consumerMap == null) {
            analyze();
        }
    }

    private void updateMap(Consumer consumer, int type) {
        int oid = consumer.getId();
        String name = consumer.getName();
        if (type == Constants.CREATE) {
            consumerMap.put(oid, name);
        }
        if (type == Constants.DELETE) {
            consumerMap.remove(oid);
        }
        if (type == Constants.UPDATE) {
            consumerMap.put(oid, name);
        }
    }
}
