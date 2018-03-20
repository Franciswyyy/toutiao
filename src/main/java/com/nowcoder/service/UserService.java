package com.nowcoder.service;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    //有许多框架，可以判断字符串为空
    //调用Service，要返回数据的合法性，信息比较多
    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(username);

        if(user != null){
            map.put("msgname", "用户名已经被注册");
        }

        user = new User();
        user.setName(username);
//        user.setPassword(password);  不能这样明文保存，还要加盐
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        
    }

    public User getUser(int id){
        return userDao.selectById(id);
    }
}
