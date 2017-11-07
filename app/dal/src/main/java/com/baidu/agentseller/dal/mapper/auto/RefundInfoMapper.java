package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.RefundInfo;
import com.baidu.agentseller.dal.entity.RefundInfoExample;
import com.baidu.agentseller.dal.entity.RefundInfoWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RefundInfoMapper {
    int countByExample(RefundInfoExample example);

    int deleteByExample(RefundInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RefundInfoWithBLOBs record);

    int insertSelective(RefundInfoWithBLOBs record);

    List<RefundInfoWithBLOBs> selectByExampleWithBLOBs(RefundInfoExample example);

    List<RefundInfo> selectByExample(RefundInfoExample example);

    RefundInfoWithBLOBs selectByPrimaryKey(Integer id);

    List<RefundInfoWithBLOBs> selectByExampleWithPaging(RefundInfoExample example);

    int updateByExampleSelective(@Param("record") RefundInfoWithBLOBs record, @Param("example") RefundInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") RefundInfoWithBLOBs record, @Param("example") RefundInfoExample example);

    int updateByExample(@Param("record") RefundInfo record, @Param("example") RefundInfoExample example);

    int updateByPrimaryKeySelective(RefundInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(RefundInfoWithBLOBs record);

    int updateByPrimaryKey(RefundInfo record);

    List<RefundInfoWithBLOBs> selectByExampleForUpdate(RefundInfoExample example);

    RefundInfoWithBLOBs selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<RefundInfo> record);
}