package cn.vanchee.service;

import cn.vanchee.dao.FruitDao;
import cn.vanchee.model.Fruit;
import cn.vanchee.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vanchee
 * @date 13-5-6
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class FruitService {

    private List<Fruit> fruitList;
    private Map<Integer, String> fruitMap;

    private static FruitDao fruitDao = new FruitDao();

    public FruitService() {
        init();
        analyze();
    }

    public boolean createTable() {
        return fruitDao.createTable();
    }

    public String[] listNames() {
        checkData();
        String[] data = new String[fruitList.size()];
        int i = 0;
        for (Fruit fruit : fruitList) {
            data[i] = fruit.getName();
            i++;
        }
        return data;
    }

    public boolean isExist(String name) {
        return fruitDao.isExist(name);
    }

    public boolean create(String name) {
        if (isExist(name)) {
            return false;
        }
        Fruit fruit = new Fruit();
        fruit.setName(name);

        boolean flag = fruitDao.create(name);

        if (flag) {
            fruit = fruitDao.findByName(name);
            checkData();
            fruitList.add(0, fruit);
            updateMap(fruit, Constants.CREATE);
        }
        return flag;
    }

    public String getFruitName(int id) {
        checkData();
        return fruitMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name.trim())) {
            return -1;
        }
        for (Fruit o : fruitList) {
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
        fruitList = fruitDao.getList();
    }

    private void analyze() {
        if (fruitList == null) {
            init();
        }
        fruitMap = new HashMap<Integer, String>();
        for (Fruit fruit : fruitList) {
            fruitMap.put(fruit.getId(), fruit.getName());
        }
    }

    private void checkData() {
        if (fruitList == null) {
            init();
        }
        if (fruitMap == null) {
            analyze();
        }
    }

    private void updateMap(Fruit fruit, int type) {
        int oid = fruit.getId();
        String name = fruit.getName();
        if (type == Constants.CREATE) {
            fruitMap.put(oid, name);
        }
        if (type == Constants.DELETE) {
            fruitMap.remove(oid);
        }
        if (type == Constants.UPDATE) {
            fruitMap.put(oid, name);
        }
    }
}
