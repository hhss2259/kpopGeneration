package kpop.kpopGeneration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.QPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;


import java.util.List;
import java.util.Optional;

import static kpop.kpopGeneration.entity.QMember.*;
import static kpop.kpopGeneration.entity.QPost.*;

@Slf4j
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager entityManager){
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public int findCnt() {
        int size = queryFactory
                .selectFrom(post)
                .fetch().size();
        return size;
    }

    @Override
    public Page<Post> findPostListByCategory(Category category, Pageable pageable) {
        List<Post> fetch = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.category.eq(category),
                        post.deletedTrue.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();


        int size = queryFactory
                .selectFrom(post)
                .where(post.category.eq(category),
                        post.deletedTrue.eq(false)
                )
                .fetch()
                .size();

        return new PageImpl<Post>(fetch,pageable, size);
    }

    @Override
    public Page<Post> findALLPostList(Pageable pageable) {
        List<Post> fetch = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.deletedTrue.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();


        int size = queryFactory
                .selectFrom(post)
                .where(
                        post.deletedTrue.eq(false)
                )
                .fetch()
                .size();

        return new PageImpl<Post>(fetch,pageable, size);
    }

    @Override
    public Optional<Post> findPostById(Long Id) {
        Post selectedPost = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.id.eq(Id),
                        post.deletedTrue.eq(false)
                )
                .fetchOne();
        return Optional.ofNullable(selectedPost);
    }
}
