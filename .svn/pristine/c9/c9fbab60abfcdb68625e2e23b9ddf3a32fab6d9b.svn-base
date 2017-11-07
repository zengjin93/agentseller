package com.baidu.agentseller.dal.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baidu.agentseller.dal.dao.UserDao;
import com.baidu.agentseller.dal.entity.User;
import com.baidu.agentseller.dal.entity.UserExample;
import com.baidu.agentseller.dal.mapper.auto.UserMapper;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer userId, boolean lock) {
        if (lock) {
            return userMapper.selectByPrimaryKeyForUpdate(userId);
        } else {
            return userMapper.selectByPrimaryKey(userId);
        }
    }

    @Override
    public int updateById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User getUserByJsessionId(String jsessionId) {
        UserExample example = new UserExample();
        example.createCriteria().andJsessionIdEqualTo(jsessionId);
        List<User> users = userMapper.selectByExampleWithBLOBs(example);
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User getUserByOpenId(String openId, boolean lock) {
        UserExample example = new UserExample();
        example.createCriteria().andOpenIdEqualTo(openId);
        List<User> users = null;
        users = lock ? userMapper.selectByExampleForUpdate(example) : userMapper.selectByExample(example);
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

}
