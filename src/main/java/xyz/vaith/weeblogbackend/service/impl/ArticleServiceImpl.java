package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.*;
import xyz.vaith.weeblogbackend.model.*;
import xyz.vaith.weeblogbackend.param.ArticleParam;
import xyz.vaith.weeblogbackend.redis.RedisCacheKeys;
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
    @CacheEvict(value = RedisCacheKeys.ARTICLE_LIST, allEntries = true)
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
    @Cacheable(value = RedisCacheKeys.ARTICLE_DETAIL, key = "#id")
    public Article getArticleByID(Integer id) throws Exception {
        return  articleMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(value = RedisCacheKeys.ARTICLE_LIST, key = "#page + '-' + #size")
    public Page<Article> getArticleList(Integer page, Integer size) throws Exception {
        List<Article> articles = articleMapper.selectArticleListBy(page * size, size);
        int total = articleMapper.selectCount();
        int totalPage = total % size == 0 ? total / size : total / size + 1;
        return Page.<Article>builder().data(articles).size(size).total(total).currentPage(page).totalPage(totalPage).build();
    }

    @Override
    @CacheEvict(value = {RedisCacheKeys.ARTICLE_LIST, RedisCacheKeys.ARTICLE_DETAIL}, allEntries = true)
    public Article updateArticle(ArticleParam param) throws Exception {
        Article article = articleMapper.selectByPrimaryKey(param.getId());
        if (article != null) {
            //category
            Category category = categoryMapper.selectByPrimaryKey(param.getCategoryID());
            if (category == null) {
                throw new BuzzException("分类不存在");
            }
            acMapper.deleteByArtcileId(article.getId());
            ArticleCategory ac = ArticleCategory.builder().articleId(article.getId()).categoryId(category.getId()).createDate(new Date()).updateDate(new Date()).build();
            acMapper.insert(ac);
            article.setCategory(category);

            //cover
            Image cover = imageMapper.selectByPrimaryKey(param.getCoverID());
            if (cover == null) {
                throw new BuzzException("图片不存在");
            }
            articleCoverMapper.deleteByArticleId(article.getId());
            ArticleCover acc = ArticleCover.builder().articleId(article.getId()).imageId(param.getCoverID()).createDate(new Date()).updateDate(new Date()).build();
            articleCoverMapper.insert(acc);
            article.setCover(cover);


            //tags
            atMapper.deleteByArticle(article.getId());
            List<Tag> tags = tagMapper.selectTagsByIDs(param.getTags());
            for (Tag tag : tags) {
                ArticleTag at = ArticleTag.builder().articleId(article.getId()).tagId(tag.getId()).createDate(new Date()).updateDate(new Date()).build();
                atMapper.insert(at);
            }
            article.setTags(tags);

            //content
            article.setContent(param.getContent());
            article.setTitle(param.getTitle());
            article.setUpdateDate(new Date());

            articleMapper.updateByPrimaryKey(article);

            return article;
        }
        throw new BuzzException("文章不存在");
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.ARTICLE_LIST, allEntries = true)
    public int removeArticle(int id) throws Exception {
        acMapper.deleteByArtcileId(id);
        atMapper.deleteByArticle(id);
        articleCoverMapper.deleteByArticleId(id);
        return articleMapper.deleteByPrimaryKey(id);
    }
}
