package cn.vanchee.service;

import cn.vanchee.model.Consumer;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DataUtil;
import cn.vanchee.util.MyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class ConsumerService {

    private List<Consumer> consumerList;
    private Map<Integer, String> consumerMap;
    private int id;

    public ConsumerService() {
        init();
        analyze();
    }

    public void init() {
        consumerList = (List<Consumer>) DataUtil.readListFromFile(Constants.FILE_NAME_CONSUMER);
        id = consumerList.size() + 1;
    }

    public void analyze() {
        checkData();
        consumerMap = new HashMap<Integer, String>();
        for (Consumer consumer : consumerList) {
            consumerMap.put(consumer.getId(), consumer.getName());
        }
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

    public boolean isExist(int id, String name) {
        checkData();
        for (Consumer consumer : consumerList) {
            if (id == consumer.getId() && name.equals(consumer.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExist(String name) {
        checkData();
        for (Consumer consumer : consumerList) {
            if (name.equals(consumer.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean create(String name) {
        checkData();
        if (isExist(name)) {
            return false;
        }
        Consumer consumer = new Consumer();
        consumer.setId(id);
        consumer.setName(name);
        consumerList.add(0, consumer);

        id++;

        updateFile();
        updateMap(consumer, Constants.CREATE);
        return true;
    }

    public boolean delete(int id) {
        checkData();
        for (Consumer consumer : consumerList) {
            if (id == consumer.getId()) {
                consumerList.remove(consumer);
                updateFile();
                updateMap(consumer, Constants.DELETE);
                return true;
            }
        }
        return true;
    }

    public boolean update(Consumer consumer) {
        checkData();
        int i = 0;
        for (Consumer o : consumerList) {
            if (consumer.getId() == o.getId()) {
                consumerList.set(i, consumer);
                updateFile();
                updateMap(consumer, Constants.UPDATE);
                return true;
            }
            i++;
        }
        return true;
    }

    public String getConsumerName(int id) {
        return consumerMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name)) {
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
            cid = id;
            //需要新增记录
            create(name);
        }
        return cid;
    }

    public List<Consumer> queryConsumerByName(String name) {
        List<Consumer> list = new ArrayList<Consumer>();
        for (Consumer o : consumerList) {
            if (o.getName().indexOf(name) != -1) {
                list.add(o);
            }
        }
        return list;
    }

    private void checkData() {
        if (consumerList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_CONSUMER, consumerList);
            }
        });
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
