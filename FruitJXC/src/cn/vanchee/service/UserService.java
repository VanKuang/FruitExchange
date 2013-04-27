package cn.vanchee.service;

import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.util.Constants;
import cn.vanchee.util.DataUtil;
import cn.vanchee.util.MyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private List<User> userList;
    private User currentUser;
    private int id;

    public List<User> getUserList() {
        return userList;
    }

    public UserService() {
        init();
    }

    public void init() {
        userList = (List<User>) DataUtil.readListFromFile(Constants.FILE_NAME_USER);
        id = userList.size() + 1;
    }


    public boolean isExist(int id, String name) {
        checkData();
        for (User user : userList) {
            if (name.equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String name, String password) {
        User user = queryUserByName(name);
        if (user != null) {
            try {
                if (password.equals(new String((new BASE64Decoder()).decodeBuffer(user.getPassword())))) {
                    currentUser = user;
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isExist(String name) {
        checkData();
        for (User user : userList) {
            if (name.equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean create(String name, String password) {
        checkData();
        if (isExist(name)) {
            return false;
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword((new BASE64Encoder()).encodeBuffer(password.getBytes()));
        user.setResource(new int[0]);
        userList.add(0, user);

        id++;
        updateFile();

        log.info(MyFactory.getUserService().getCurrentUserName() + " create " + user.toString());
        return true;
    }

    public boolean delete(String name) {
        checkData();
        for (User user : userList) {
            if (name == user.getName()) {
                userList.remove(user);
                updateFile();
                log.debug(MyFactory.getUserService().getCurrentUserName() + " delete " + user.toString());
                return true;
            }
        }
        return false;
    }

    public boolean update(User user) {
        checkData();
        int i = 0;
        for (User o : userList) {
            if (user.getName() == o.getName()) {
                userList.set(i, user);
                updateFile();
                log.debug(MyFactory.getUserService().getCurrentUserName() + " update " + user.toString());
                return true;
            }
            i++;
        }
        return true;
    }

    public User queryUserByName(String name) {
        for (User o : userList) {
            if (name.equals(o.getName())) {
                return o;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        if (MainApp.debug) {
            return User.admin;
        }
        return currentUser;
    }

    public String getCurrentUserName() {
        if (currentUser != null) {
            return currentUser.getName();
        }
        return "not login user";
    }

    private void checkData() {
        if (userList == null) {
            init();
        }
    }

    private synchronized void updateFile() {
        MyFactory.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DataUtil.writeListToFile(Constants.FILE_NAME_USER, userList);
            }
        });
    }

}
