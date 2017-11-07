package com.baidu.agentseller.dal.dao;

import com.baidu.agentseller.dal.entity.User;

public interface UserDao {

    User getUserById(Integer userId, boolean lock);

    int updateById(User user);

    User getUserByJsessionId(String jsessionId);

    User getUserByOpenId(String openId, boolean lock);

}
