package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.vaith.weeblogbackend.cache.Cache;
import xyz.vaith.weeblogbackend.mapper.ArticleMapper;
import xyz.vaith.weeblogbackend.mapper.HomeInfoMapper;
import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.service.BlogService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Log4j2
public class BlogServiceImpl implements BlogService {

    @Resource
    HomeInfoMapper homeInfoMapper;

    @Resource
    ArticleMapper articleMapper;



    @Override
    @Cacheable(value = Cache.Key.HOME_INFO, key = "0")
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
}
