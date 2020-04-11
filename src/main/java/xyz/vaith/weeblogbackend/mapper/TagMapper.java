package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.Tag;

import java.util.List;

public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    List<Tag> selectTagList(int start, int size);

    List<Tag> selectTagsByIDs(List<Integer> ids);

    List<Tag> selectTagsByArticleID(Integer id);

    int selectCount();
}
