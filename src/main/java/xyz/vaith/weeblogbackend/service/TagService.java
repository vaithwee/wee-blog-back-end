package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Tag;
import xyz.vaith.weeblogbackend.param.TagParam;

import java.util.List;

public interface TagService {
    Tag addTag(TagParam param) throws Exception;
    boolean removeTag(TagParam param) throws Exception;
    List<Tag> getTagList(int page, int size) throws Exception;
}
