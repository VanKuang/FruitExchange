package cn.vanchee.service;

import cn.vanchee.dao.OwnerDao;
import cn.vanchee.model.Owner;
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
public class OwnerService {

    private List<Owner> ownerList;
    private Map<Integer, String> ownerMap;

    private static OwnerDao ownerDao;

    public OwnerService() {
        ownerDao = new OwnerDao();
        init();
        analyze();
    }

    public boolean createTable() {
        return ownerDao.createTable();
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

    public boolean isExist(String name) {
        return ownerDao.isExist(name);
    }

    public boolean create(String name) {
        if (isExist(name)) {
            return false;
        }
        Owner owner = new Owner();
        owner.setName(name);

        boolean flag = ownerDao.create(name);

        if (flag) {
            owner = ownerDao.findByName(name);
            checkData();
            ownerList.add(0, owner);
            updateMap(owner, Constants.CREATE);
        }
        return flag;
    }

    public String getOwnerName(int id) {
        checkData();
        return ownerMap.get(id);
    }

    public int getIdByName4Query(String name) {
        if (name == null || "".equals(name.trim())) {
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
        int cid = getIdByName4Query(name);
        if (cid == -2) {
            //需要新增记录
            create(name);
        }
        return cid;
    }

    private void init() {
        ownerList = ownerDao.getList();
    }

    private void analyze() {
        if (ownerList == null) {
            init();
        }
        ownerMap = new HashMap<Integer, String>();
        for (Owner owner : ownerList) {
            ownerMap.put(owner.getId(), owner.getName());
        }
    }

    private void checkData() {
        if (ownerList == null) {
            init();
        }
        if (ownerMap == null) {
            analyze();
        }
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
