package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import xyz.vaith.weeblogbackend.mapper.TagMapper;
import xyz.vaith.weeblogbackend.model.Tag;
import xyz.vaith.weeblogbackend.param.TagParam;
import xyz.vaith.weeblogbackend.service.TagService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    TagMapper tagMapper;

    @Override
    public Tag addTag(TagParam param) throws Exception {
        Tag tag = Tag.builder().name(param.getName()).type(param.getType()).sort(param.getSort()).createDate(new Date()).updateDate(new Date()).build();
        int res =  tagMapper.insert(tag);
        return tag;
    }

    @Override
    public boolean removeTag(TagParam param) throws Exception {
        int result = tagMapper.deleteByPrimaryKey(param.getId());
        return result > 0;
    }

    @Override
    public List<Tag> getTagList(int page, int size) throws Exception {
        return tagMapper.selectTagList(page * size, size);
    }
}
