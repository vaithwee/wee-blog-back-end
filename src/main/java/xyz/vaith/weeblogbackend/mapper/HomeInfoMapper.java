package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.HomeInfo;

public interface HomeInfoMapper {
    int deleteByPrimaryKey(Integer id);


    int deleteByImageId(Integer id);

    int insert(HomeInfo record);

    int insertSelective(HomeInfo record);

    HomeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HomeInfo record);

    int updateByPrimaryKey(HomeInfo record);

    HomeInfo selectLastHomeInfo();
}