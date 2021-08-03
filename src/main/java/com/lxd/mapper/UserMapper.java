package com.lxd.mapper;/*
 *  create by 20224
 *  2020/9/8
 * */

import com.lxd.po.User;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {

    /**
     * 查询所有
     * @return
     */
    @MapKey("id")
    public List<Map<String, String>> findA();

    int addUser(User user);
}
