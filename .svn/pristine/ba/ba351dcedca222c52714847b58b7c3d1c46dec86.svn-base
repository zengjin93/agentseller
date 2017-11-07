package com.baidu.agentseller.dal.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baidu.agentseller.dal.dao.GoodsDao;
import com.baidu.agentseller.dal.entity.Goods;
import com.baidu.agentseller.dal.mapper.auto.GoodsMapper;

@Repository("goodsDao")
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Goods getGoodsInfoById(Integer id) {

        return goodsMapper.selectByPrimaryKey(id);
    }

}
