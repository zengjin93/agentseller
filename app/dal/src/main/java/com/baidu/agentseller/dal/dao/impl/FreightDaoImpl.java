package com.baidu.agentseller.dal.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.agentseller.dal.dao.FreightDao;
import com.baidu.agentseller.dal.entity.Freight;
import com.baidu.agentseller.dal.mapper.auto.FreightMapper;

@Service("freightDao")
public class FreightDaoImpl implements FreightDao {

    @Autowired
    private FreightMapper freightMapper;

    @Override
    public Freight getFreightById(Integer id, boolean lock) {

        return lock ? freightMapper.selectByPrimaryKeyForUpdate(id) : freightMapper.selectByPrimaryKey(id);
    }

    @Override
    public int createFreightOrder(Freight freight) {

        return freightMapper.insertSelective(freight);
    }

    @Override
    public int updateFreightOrder(Freight freight) {

        return freightMapper.updateByPrimaryKeySelective(freight);
    }

}
