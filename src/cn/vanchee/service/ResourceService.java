package cn.vanchee.service;

import cn.vanchee.model.Resource;
import cn.vanchee.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author vanchee
 * @date 13-3-25
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class ResourceService {

    private Map<Integer, String> resourceMap;

    public ResourceService() {
        init();
    }

    public Map<Integer, String> getResourceMap() {
        if (resourceMap == null) {
            init();
        }
        return resourceMap;
    }

    public boolean hasRight(User user, int resource) {
        if (user == null) {
            return false;
        }
        int[] resources = user.getResource();
        for (int id : resources) {
            if (id == resource) {
                return true;
            }
        }
        return false;
    }

    public int[] getAllResources() {
        if (resourceMap == null) {
            init();
        }
        int size = resourceMap.size();
        int resources[] = new int[size];
        int i = 0;
        for (Map.Entry<Integer, String> m : resourceMap.entrySet()) {
            resources[i] = m.getKey();
            i++;
        }
        return resources;
    }

    public int queryResourceId(String displayName) {
        if (resourceMap == null) {
            init();
        }
        for (Map.Entry<Integer, String> m : resourceMap.entrySet()) {
            if (displayName.equals(m.getValue())) {
                return m.getKey();
            }
        }
        return 0;
    }

    public String queryDisplayName(int rid) {
        if (resourceMap == null) {
            init();
        }
        for (Map.Entry<Integer, String> m : resourceMap.entrySet()) {
            if (rid == m.getKey()) {
                return m.getValue();
            }
        }
        return null;
    }


    private void init() {
        resourceMap = new LinkedHashMap<Integer, String>();
        resourceMap.put(Resource.IN_R, "进货读");
        resourceMap.put(Resource.IN_W, "进货写");
        resourceMap.put(Resource.IN_O, "进货详细导出");

        resourceMap.put(Resource.OUT_R, "销售读");
        resourceMap.put(Resource.OUT_W, "销售写");
        resourceMap.put(Resource.OUT_O, "销售详细导出");

        resourceMap.put(Resource.MY_PAID_R, "我的还款读");
        resourceMap.put(Resource.MY_PAID_W, "我的还款写");

        resourceMap.put(Resource.PAID_R, "他人还款读");
        resourceMap.put(Resource.PAID_W, "他人还款写");

        resourceMap.put(Resource.CONSUMPTION_R, "其他消费读");
        resourceMap.put(Resource.CONSUMPTION_W, "其他消费写");

        resourceMap.put(Resource.MONEY_REPORT_R, "资金明细表读");
        resourceMap.put(Resource.MY_OWN_R, "我的欠款读");
        resourceMap.put(Resource.MY_PAID_EXPORT, "我的欠款导出");
        resourceMap.put(Resource.OTHER_OWN_R, "他人欠款读");
        resourceMap.put(Resource.PAID_EXPORT, "他人欠款导出");
        resourceMap.put(Resource.MONTH_REPORT_R, "月结表读");

        resourceMap.put(Resource.USER_MNG, "用户管理");
        resourceMap.put(Resource.BACKUP, "备份数据");
        resourceMap.put(Resource.DATA_INIT, "数据初始化");

        resourceMap.put(Resource.GET_OTHERS_DATA, "查看他人数据");
        resourceMap.put(Resource.CENSORED, "审核");
    }

}
