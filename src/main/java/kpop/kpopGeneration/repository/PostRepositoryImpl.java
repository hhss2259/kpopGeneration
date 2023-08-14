package kpop.kpopGeneration.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.RecentPostByMemberDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
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
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager entityManager){
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public int findCnt() {
        int size = queryFactory
                .selectFrom(post)
                .fetch().size();
        return size;
    }

    /**
     * 포스트 목록 조회하기(카테고리별)
     */
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
    /**
     * 포스트 목록 조회하기(전체 포스트)
     */
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
    public Page<RecentPostByMemberDto> findRecentPostListByMember(Member member, Pageable pageable, Long postId) {
        List<RecentPostByMemberDto> fetch = queryFactory
                .select(
                    Projections.bean(
                        RecentPostByMemberDto.class,
                        post.id.as("id"),
                        post.title.as("title"),
                        post.createdTime.as("createdTime")
                        )
                )
                .from(post)
                .where(
                        post.member.eq(member),
                        post.id.ne(postId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdTime.desc())
                .fetch();

        int size = queryFactory
                .selectFrom(post)
                .where(post.member.eq(member))
                .fetch().size();

        return new PageImpl<RecentPostByMemberDto>(fetch, pageable, size);
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

    @Override
    public String findWriter(Long id) {
        String username = queryFactory
                .select(post.member.username)
                .from(post)
                .where(post.id.eq(id))
                .fetchOne();
        return username;
    }
}
