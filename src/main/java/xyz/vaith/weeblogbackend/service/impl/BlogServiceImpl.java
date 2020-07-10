package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.mapper.ArticleMapper;
import xyz.vaith.weeblogbackend.mapper.CategoryMapper;
import xyz.vaith.weeblogbackend.mapper.HomeInfoMapper;
import xyz.vaith.weeblogbackend.mapper.TagMapper;
import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.model.Tag;
import xyz.vaith.weeblogbackend.redis.CacheExpire;
import xyz.vaith.weeblogbackend.redis.RedisCacheKeys;
import xyz.vaith.weeblogbackend.service.BlogService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Log4j2
public class BlogServiceImpl implements BlogService {

    @Resource
    HomeInfoMapper homeInfoMapper;

    @Resource
    ArticleMapper articleMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    TagMapper tagMapper;



    @Override
    @Cacheable(RedisCacheKeys.HOME_INFO)
    public Map<String, Object> homeInfo() throws Exception {
        log.info("获取首页信息");
        Map<String, Object> json = new HashMap<>();
        json.put("info", homeInfoMapper.selectLastHomeInfo());
        json.put("blog", articleMapper.selectLastArticleList());
        return json;
    }

    @Override
    public Article getArticleByID(int id) throws Exception {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> categoryList() throws Exception {
        return categoryMapper.selectByPageAndSize(0, 100);
    }

    @Override
    public List<Article> recentArticleList() throws Exception {
        List<Article> data =  articleMapper.selectLastArticleList();
        if (data.size() <= 3) {
            return data;
        } else  {
            return data.subList(0, 3);
        }
    }

    @Override
    public List<String> archiveList() throws Exception {
        return articleMapper.selectArchiveList();
    }

    @Override
    public List<Tag> tagList() throws Exception {
        return tagMapper.selectSortTagsList();
    }

    @Override
    public Map<String, Object> footnote() throws Exception {
        Map<String, Object> data = new HashMap<>();
        List<Article> articles =  articleMapper.selectLastArticleList();
        List<String> archives = articleMapper.selectArchiveList();
        List<Tag> tags = tagMapper.selectSortTagsList();
        data.put("articles", articles.size() > 3 ? articles.subList(0, 3): articles);
        data.put("archives", archives);
        data.put("tags", tags);
        return data;
    }
}
