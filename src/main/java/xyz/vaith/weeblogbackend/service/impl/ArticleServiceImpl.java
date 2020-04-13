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
    ArticleMapper articleMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    ArticleCategoryMapper acMapper;

    @Resource
    ArticleTagMapper atMapper;

    @Resource
    TagMapper tagMapper;

    @Resource
    ArticleCoverMapper articleCoverMapper;

    @Resource
    ImageMapper imageMapper;

    @Override
    public Article addArticle(ArticleParam param) throws Exception {
        Category category = categoryMapper.selectByPrimaryKey(param.getCategoryID());
        if (category == null) {
            throw new BuzzException("分类不存在");
        }
        Article article = Article.builder().title(param.getTitle()).content(param.getContent()).createDate(new Date()).updateDate(new Date()).build();
        int result = articleMapper.insert(article);
        if (result > 0) {
            ArticleCategory ac = ArticleCategory.builder().articleId(article.getId()).categoryId(category.getId()).createDate(new Date()).updateDate(new Date()).build();
            acMapper.insert(ac);
            article.setCategory(category);

            ArticleCover acc = ArticleCover.builder().articleId(article.getId()).imageId(param.getCoverID()).createDate(new Date()).updateDate(new Date()).build();
            articleCoverMapper.insert(acc);
        }

        Image cover = imageMapper.selectByPrimaryKey(param.getCoverID());
        article.setCover(cover);


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
        return  articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Article> getArticleList(Integer page, Integer size) throws Exception {
        List<Article> articles = articleMapper.selectArticleListBy(page * size, size);
        int total = articleMapper.selectCount();
        int totalPage = total % size == 0 ? total / size : total / size + 1;
        return Page.<Article>builder().data(articles).size(size).total(total).currentPage(page).totalPage(totalPage).build();
    }
}
