package cn.vanchee.model;

import java.io.Serializable;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.model
 * @verson v1.0.0
 */
public class Owner implements Serializable {

    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
