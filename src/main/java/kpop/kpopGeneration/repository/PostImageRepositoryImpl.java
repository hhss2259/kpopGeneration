package kpop.kpopGeneration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostImage;
import kpop.kpopGeneration.entity.QPostImage;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static kpop.kpopGeneration.entity.QPostImage.*;

public class PostImageRepositoryImpl implements PostImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostImageRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 썸네일을 제외한 이미지들을 가지고 온다
     */
    @Override
    public List<PostImage> findNotThumbNail(Post post) {
        List<PostImage> fetch = queryFactory.selectFrom(postImage)
                .where(postImage.post.eq(post), postImage.thumbnail.eq( false)) // 썸네일이 아닌 이미지들을 가지고 온다
                .fetch();
        return fetch;
    }

    /**
     * 썸네일을 가지고 온다
     */
    @Override
    public Optional<PostImage> findThumbNail(Post post) {
        PostImage thumbnail = queryFactory.selectFrom(postImage)
                .where(postImage.post.eq(post), postImage.thumbnail.eq(true)) // 썸네일로 사용되는 이미지를 가지고 온다
                .fetchOne();
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public void deleteAllPostImage(Post post) {
        long execute = queryFactory
                .delete(postImage)
                .where(postImage.post.eq(post))
                .execute();
    }

    @Override
    public List<PostImage> findAllPostImage(Post post) {
        List<PostImage> list = queryFactory.selectFrom(postImage)
                .where(postImage.post.eq(post))
                .fetch();
        return list;
    }
}
