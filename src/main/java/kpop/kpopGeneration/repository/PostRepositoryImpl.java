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
     * 카테고리별 포스트 리스트 조회하기
     */
    @Override
    public Page<Post> findPostListByCategory(Category category, Pageable pageable) {
        List<Post> fetch = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.category.eq(category), //카테고리 지정
                        post.deletedTrue.eq(false) // 삭제하지 않은 포스트만
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
     * 전체 포스트 리스트 조회하기 
     */
    @Override
    public Page<Post> findALLPostList(Pageable pageable) {
        List<Post> fetch = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.category.ne(Category.NEWS),
                        post.deletedTrue.eq(false) // 삭제하지 않은 포스트
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        int size = queryFactory
                .selectFrom(post)
                .where(
                        post.category.ne(Category.NEWS),
                        post.deletedTrue.eq(false)
                )
                .fetch()
                .size();

        return new PageImpl<Post>(fetch,pageable, size);
    }

    /**
     *  한 사용자가 최근에 작상한 포스트 5개 조회해오기
     */
    
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
                        post.member.eq(member), //같은 작성자
                        post.id.ne(postId), // 현재 포스트는 제외한 5개 포스트
                        post.deletedTrue.eq(false) //삭제하지 않은 포스트
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

    /**
     *  포스트의 id로 포스트 조회해오기
     */

    @Override
    public Optional<Post> findPostById(Long Id) {
        Post selectedPost = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(
                        post.id.eq(Id), //포스트 id
                        post.deletedTrue.eq(false) //삭제하지 않는 포스트
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
