package cn.vanchee.service;

import cn.vanchee.dao.UserDao;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.List;

/**
 * @author vanchee
 * @date 13-5-5
 * @package cn.vanchee.service
 * @verson v1.0.0
 */
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static UserDao userDao = new UserDao();

    private User currentUser = null;

    public boolean createTable() {
        return userDao.createTable();
    }

    public boolean create(User user) {
        if (userDao.isExist(user.getName())) {
            return false;
        }
        return userDao.create(user);
    }

    public boolean update(User user) {
        return userDao.update(user);
    }

    public boolean delete(int id) {
        return userDao.delete(id);
    }

    public boolean login(String name, String password) {
        User user = userDao.findByName(name);
        if (user != null) {
            try {
                if (password.equals(new String((new BASE64Decoder()).decodeBuffer(user.getPassword())))) {
                    currentUser = user;
                    return true;
                }
            } catch (IOException e) {
                log.error("login error", e);
            }
        }
        return false;
    }

    public List<User> getUserList() {
        return userDao.getList();
    }

    public User queryUserByName(String name) {
        return userDao.findByName(name);
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

    public int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getId();
        }
        return 0;
    }
}
