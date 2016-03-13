package cn.vanchee.service;

import cn.vanchee.model.Owner;
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
public class OwnerService {

    private List<Owner> ownerList;
    private Map<Integer, String> ownerMap;
    private int id;

    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public Map<Integer, String> getOwnerMap() {
        return ownerMap;
    }

    public OwnerService() {
        init();
        analyze();
    }

    public void init() {
        ownerList = (List<Owner>) DataUtil.readListFromFile(Constants.FILE_NAME_OWNER);
        id = ownerList.size() + 1;
    }

    public void analyze() {
        checkData();
        ownerMap = new HashMap<Integer, String>();
        for (Owner owner : ownerList) {
            ownerMap.put(owner.getId(), owner.getName());
        }
    }

    public String[] listNames() {
        checkData();
        String[] data = new String[ownerList.size()];
        int i = 0;
        for (Owner owner : ownerList) {
            data[i] = owner.getName();
            i++;
        }
        return data;
    }

    public boolean isExist(int id, String name) {
        checkData();
        for (Owner owner : ownerList) {
            if (id == owner.getId() && name.equals(owner.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExist(String name) {
        checkData();
        for (Owner owner : ownerList) {
            if (name.equals(owner.getName())) {
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
        Owner owner = new Owner();
        owner.setId(id);
        owner.setName(name);
        ownerList.add(0, owner);

        id++;

        updateFile();
        updateMap(owner, Constants.CREATE);

        return true;
    }

    public boolean delete(int id) {
        checkData();
        for (Owner owner : ownerList) {
            if (id == owner.getId()) {
                ownerList.remove(owner);
                updateFile();
                updateMap(owner, Constants.DELETE);
                return true;
            }
        }
        return true;
    }

    public boolean update(Owner owner) {
        checkData();
        int i = 0;
        for (Owner o : ownerList) {
            if (owner.getId() == o.getId()) {
                ownerList.set(i, owner);
                updateFile();
                updateMap(owner, Constants.UPDATE);
                return true;
            }
            i++;
        }
        return true;
    }

    public String getOwnerName(int id) {
        return ownerMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name)) {
            return -1;
        }
        for (Owner o : ownerList) {
            if (name.equals(o.getName())) {
                return o.getId();
            }
        }
        return -2;
    }

    public int getIdByName4Add(String name) {
        int oid = getIdByName4Query(name);
        if (oid == -2) {
            oid = id;
            //需要新增记录
            create(name);
        }
        return oid;
    }

    public List<Owner> queryOwnerByName(String name) {
        List<Owner> list = new ArrayList<Owner>();
        for (Owner o : ownerList) {
            if (o.getName().indexOf(name) != -1) {
                list.add(o);
            }
        }
        return list;
    }

    private void checkData() {
        if (ownerList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_OWNER, ownerList);
            }
        });
    }

    private void updateMap(Owner owner, int type) {
        int oid = owner.getId();
        String name = owner.getName();
        if (type == Constants.CREATE) {
            ownerMap.put(oid, name);
        }
        if (type == Constants.DELETE) {
            ownerMap.remove(oid);
        }
        if (type == Constants.UPDATE) {
            ownerMap.put(oid, name);
        }
    }

}
