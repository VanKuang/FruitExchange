package cn.vanchee.test;

import cn.vanchee.service.UserService;
import cn.vanchee.util.MyFactory;

import java.io.IOException;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class UserServiceTest {

    public static void main(String[] args) throws IOException {
        UserService userService = MyFactory.getUserService();

        String name = "user_1";
        String password = "password_1";

        System.out.println("test user login:" + userService.login(name, password));

        System.out.println("test user login:" + !userService.login(name, password + "1"));
    }
}
