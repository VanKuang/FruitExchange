package cn.vanchee.service;

import cn.vanchee.model.Fruit;
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
public class FruitService {

    private List<Fruit> fruitList;
    private Map<Integer, String> fruitMap;
    private int id;

    public List<Fruit> getFruitList() {
        return fruitList;
    }

    public Map<Integer, String> getFruitMap() {
        return fruitMap;
    }

    public FruitService() {
        init();
        analyze();
    }

    public void init() {
        fruitList = (List<Fruit>) DataUtil.readListFromFile(Constants.FILE_NAME_FRUIT);
        id = fruitList.size() + 1;
    }

    public void analyze() {
        checkData();
        fruitMap = new HashMap<Integer, String>();
        for (Fruit fruit : fruitList) {
            fruitMap.put(fruit.getId(), fruit.getName());
        }
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

    public boolean isExist(int id, String name) {
        checkData();
        for (Fruit fruit : fruitList) {
            if (id == fruit.getId() && name.equals(fruit.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExist(String name) {
        checkData();
        for (Fruit fruit : fruitList) {
            if (name.equals(fruit.getName())) {
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
        Fruit fruit = new Fruit();
        fruit.setId(id);
        fruit.setName(name);
        fruitList.add(0, fruit);

        id++;

        updateFile();
        updateMap(fruit, Constants.CREATE);

        return true;
    }

    public boolean delete(int id) {
        checkData();
        for (Fruit fruit : fruitList) {
            if (id == fruit.getId()) {
                fruitList.remove(fruit);
                updateFile();
                updateMap(fruit, Constants.DELETE);
                return true;
            }
        }
        return true;
    }

    public boolean update(Fruit fruit) {
        checkData();
        int i = 0;
        for (Fruit o : fruitList) {
            if (fruit.getId() == o.getId()) {
                fruitList.set(i, fruit);
                updateFile();
                updateMap(fruit, Constants.UPDATE);
                return true;
            }
            i++;
        }
        return true;
    }

    public String getFruitName(int id) {
        return fruitMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name)) {
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
        int fid = getIdByName4Query(name);
        if (fid == -2) {
            fid = id;
            //需要新增记录
            create(name);
        }
        return fid;
    }

    public List<Fruit> queryFruitByName(String name) {
        List<Fruit> list = new ArrayList<Fruit>();
        for (Fruit o : fruitList) {
            if (o.getName().indexOf(name) != -1) {
                list.add(o);
            }
        }
        return list;
    }

    private void checkData() {
        if (fruitList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_FRUIT, fruitList);
            }
        });
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
