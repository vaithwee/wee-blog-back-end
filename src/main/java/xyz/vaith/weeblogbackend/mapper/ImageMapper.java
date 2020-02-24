package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.Image;

import java.util.List;

public interface ImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);

    List<Image> selectImageList(int start, int size);

    Image selectByArticleId(Integer id);
}
