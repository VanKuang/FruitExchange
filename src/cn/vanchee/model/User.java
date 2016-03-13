package cn.vanchee.model;

import cn.vanchee.util.MyFactory;
import sun.misc.BASE64Encoder;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class User {

    private Integer id;
    private String name;
    private String password;
    private int[] resource;

    public transient static User admin;

    static {
        admin = new User();
        admin.setId(1);
        admin.setName("admin");
        admin.setPassword((new BASE64Encoder()).encodeBuffer(("admin123").getBytes()));
        admin.setResource(MyFactory.getResourceService().getAllResources());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int[] getResource() {
        return resource;
    }

    public void setResource(int[] resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "User--[id:" + this.id
                + ",username:" + this.name
                + "]";
    }
}
