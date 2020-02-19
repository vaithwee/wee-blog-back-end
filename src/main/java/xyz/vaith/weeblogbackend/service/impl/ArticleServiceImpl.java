package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.*;
import xyz.vaith.weeblogbackend.model.*;
import xyz.vaith.weeblogbackend.param.ArticleParam;
import xyz.vaith.weeblogbackend.service.ArticleService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Resource
    ArticleMapper mapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    ArticleCategoryMapper acMapper;

    @Resource
    ArticleTagMapper atMapper;

    @Resource
    TagMapper tagMapper;

    @Override
    public Article addArticle(ArticleParam param) throws Exception {
        Category category = categoryMapper.selectByPrimaryKey(param.getCategoryID());
        if (category == null) {
            throw new BuzzException("分类不存在");
        }
        Article article = Article.builder().title(param.getTitle()).content(param.getContent()).createDate(new Date()).updateDate(new Date()).build();
        int result = mapper.insert(article);
        if (result > 0) {
            ArticleCategory ac = ArticleCategory.builder().articleId(article.getId()).categoryId(category.getId()).createDate(new Date()).updateDate(new Date()).build();
            acMapper.insert(ac);
            article.setCategory(category);
        }



         List<Tag> tags = tagMapper.selectTagsByIDs(param.getTags());
        for (Tag tag : tags) {
            ArticleTag at = ArticleTag.builder().articleId(article.getId()).tagId(tag.getId()).createDate(new Date()).updateDate(new Date()).build();
            atMapper.insert(at);
        }
        log.info("tag select result:" + tags);
        article.setTags(tags);

        return article;
    }

    @Override
    public Article getArticleByID(Integer id) throws Exception {
        return  mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Article> getArticleList(Integer page, Integer size) throws Exception {
        return mapper.selectArticleListBy(page * size, size);
    }
}
