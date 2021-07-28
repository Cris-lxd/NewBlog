package com.lxd;

import com.lxd.po.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: Cris
 * Date: 2021/07/26
 * Time: 13:57
 * Project: blog
 * Description：
 **/
public class ReflectionTest {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
        User user = new User();
        Class<? extends User> aClass = user.getClass();
        Class[] p = {String.class};
        Method user1 = aClass.getDeclaredMethod("getuser", p);
        Field field = aClass.getDeclaredField("email");
        field.setAccessible(true);
        field.set("email", "1120620371@qq.com");
        String s = field.get("email").toString();
        System.out.println();

    }
}