package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.Goods;
import com.baidu.agentseller.dal.entity.GoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper {
    int countByExample(GoodsExample example);

    int deleteByExample(GoodsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    List<Goods> selectByExample(GoodsExample example);

    Goods selectByPrimaryKey(Integer id);

    List<Goods> selectByExampleWithPaging(GoodsExample example);

    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

    int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> selectByExampleForUpdate(GoodsExample example);

    Goods selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<Goods> record);
}