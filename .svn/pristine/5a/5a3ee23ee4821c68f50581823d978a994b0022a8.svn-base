package com.baidu.agentseller.dal.dao.impl;

import com.baidu.agentseller.dal.dao.RefundInfoDao;
import com.baidu.agentseller.dal.entity.RefundInfo;
import com.baidu.agentseller.dal.entity.RefundInfoExample;
import com.baidu.agentseller.dal.entity.RefundInfoWithBLOBs;
import com.baidu.agentseller.dal.mapper.auto.RefundInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by v_zengjin on 2017/10/31 0031.
 */
@Service
public class RefundInfoDaoImpl implements RefundInfoDao {

    @Autowired
    private RefundInfoMapper refundInfoMapper;

    @Override
    public int createRefundInfo(RefundInfoWithBLOBs info) {

        return refundInfoMapper.insertSelective(info);

    }

    @Override
    public int updateRefundInfo(RefundInfoWithBLOBs update) {
        return refundInfoMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public RefundInfoWithBLOBs getRefundInfoByOrderId(Integer orderId) {
        RefundInfoExample example = new RefundInfoExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<RefundInfoWithBLOBs> list = refundInfoMapper.selectByExampleForUpdate(example);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
