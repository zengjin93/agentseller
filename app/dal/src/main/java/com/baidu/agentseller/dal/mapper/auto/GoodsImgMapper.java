package com.baidu.agentseller.dal.mapper.auto;

import com.baidu.agentseller.dal.entity.GoodsImg;
import com.baidu.agentseller.dal.entity.GoodsImgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsImgMapper {
    int countByExample(GoodsImgExample example);

    int deleteByExample(GoodsImgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GoodsImg record);

    int insertSelective(GoodsImg record);

    List<GoodsImg> selectByExample(GoodsImgExample example);

    GoodsImg selectByPrimaryKey(Integer id);

    List<GoodsImg> selectByExampleWithPaging(GoodsImgExample example);

    int updateByExampleSelective(@Param("record") GoodsImg record, @Param("example") GoodsImgExample example);

    int updateByExample(@Param("record") GoodsImg record, @Param("example") GoodsImgExample example);

    int updateByPrimaryKeySelective(GoodsImg record);

    int updateByPrimaryKey(GoodsImg record);

    List<GoodsImg> selectByExampleForUpdate(GoodsImgExample example);

    GoodsImg selectByPrimaryKeyForUpdate(Integer id);

    int insertBatch(List<GoodsImg> record);
}