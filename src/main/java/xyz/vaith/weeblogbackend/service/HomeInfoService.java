package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.HomeInfo;
import xyz.vaith.weeblogbackend.param.HomeInfoParam;

import java.util.Map;

public interface HomeInfoService {
    HomeInfo addHomeInfo(HomeInfoParam param) throws Exception;
    HomeInfo updateHomeInfo(HomeInfoParam param) throws Exception;
    Map<String, Object> getHomeInfo() throws Exception;
}
