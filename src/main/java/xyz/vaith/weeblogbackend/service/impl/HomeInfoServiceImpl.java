package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import xyz.vaith.weeblogbackend.mapper.ArticleMapper;
import xyz.vaith.weeblogbackend.mapper.HomeInfoMapper;
import xyz.vaith.weeblogbackend.mapper.ImageMapper;
import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.model.HomeInfo;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.param.HomeInfoParam;
import xyz.vaith.weeblogbackend.service.HomeInfoService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeInfoServiceImpl implements HomeInfoService {

    @Resource
    HomeInfoMapper homeInfoMapper;

    @Resource
    ImageMapper imageMapper;

    @Resource
    ArticleMapper articleMapper;

    @Override
    public HomeInfo addHomeInfo(HomeInfoParam param) throws Exception {
        HomeInfo homeInfo = HomeInfo.builder().coverId(param.getCoverID()).greeting(param.getGreeting()).createDate(new Date()).updateDate((new Date())).build();
        homeInfoMapper.insert(homeInfo);
        Image cover = imageMapper.selectByPrimaryKey(param.getCoverID());
        homeInfo.setCover(cover);
        return homeInfo;
    }

    @Override
    public Map<String, Object> getHomeInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        HomeInfo homeInfo = homeInfoMapper.selectLastHomeInfo();
        map.put("info",homeInfo);

        List<Article> articles = articleMapper.selectLastArticleList();
        map.put("articles", articles);
        return map;
    }

    @Override
    public HomeInfo updateHomeInfo(HomeInfoParam param) throws Exception {
        HomeInfo homeInfo = homeInfoMapper.selectByPrimaryKey(param.getId());
        homeInfo.setCoverId(param.getCoverID());
        homeInfo.setGreeting(param.getGreeting());
        homeInfoMapper.updateByPrimaryKey(homeInfo);
        return homeInfo;
    }
}
