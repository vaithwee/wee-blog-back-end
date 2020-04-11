package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.TagMapper;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.model.Page;
import xyz.vaith.weeblogbackend.model.Tag;
import xyz.vaith.weeblogbackend.param.TagParam;
import xyz.vaith.weeblogbackend.redis.RedisCacheKeys;
import xyz.vaith.weeblogbackend.service.TagService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Resource
    TagMapper tagMapper;

    @Override
    @CacheEvict(value = RedisCacheKeys.TAG_LIST, allEntries = true)
    public Tag addTag(TagParam param) throws Exception {
        Tag tag = Tag.builder().name(param.getName()).type(param.getType()).sort(param.getSort()).createDate(new Date()).updateDate(new Date()).build();
        int res =  tagMapper.insert(tag);
        return tag;
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.TAG_LIST, allEntries = true)
    public boolean removeTag(TagParam param) throws Exception {
        int result = tagMapper.deleteByPrimaryKey(param.getId());
        return result > 0;
    }

    @Override
    @Cacheable(value = RedisCacheKeys.TAG_LIST, key = "#page + '-' + #size")
    public Page<Tag> getTagList(int page, int size) throws Exception {
        List<Tag> tags = tagMapper.selectTagList(page * size, size);
        int total = tagMapper.selectCount();
        int totalPage = total % size == 0 ? total / size : total / size + 1;
        return Page.<Tag>builder().data(tags).size(size).currentPage(page).total(total).totalPage(totalPage).build();
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.TAG_LIST, allEntries = true)
    public Tag updateTTag(TagParam param) throws Exception {
        Tag tag = tagMapper.selectByPrimaryKey(param.getId());
        if (tag != null) {
            tag.setUpdateDate(new Date());
            tag.setName(param.getName());
            tag.setSort(param.getSort());
            tag.setType(param.getType());
            tagMapper.updateByPrimaryKey(tag);
            return tag;
        }
        throw new BuzzException("标签不存在");
    }
}
