package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.UserAddress;
import com.baidu.agentseller.dal.entity.UserAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAddressMapper {
    int countByExample(UserAddressExample example);

    int deleteByExample(UserAddressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    List<UserAddress> selectByExample(UserAddressExample example);

    UserAddress selectByPrimaryKey(Integer id);

    List<UserAddress> selectByExampleWithPaging(UserAddressExample example);

    int updateByExampleSelective(@Param("record") UserAddress record, @Param("example") UserAddressExample example);

    int updateByExample(@Param("record") UserAddress record, @Param("example") UserAddressExample example);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

    List<UserAddress> selectByExampleForUpdate(UserAddressExample example);

    UserAddress selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<UserAddress> record);
}