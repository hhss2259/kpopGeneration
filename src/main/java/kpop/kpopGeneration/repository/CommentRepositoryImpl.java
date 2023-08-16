package kpop.kpopGeneration.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.QComment;
import kpop.kpopGeneration.entity.QMember;
import kpop.kpopGeneration.entity.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static kpop.kpopGeneration.entity.QComment.*;
import static kpop.kpopGeneration.entity.QMember.*;
import static kpop.kpopGeneration.entity.QPost.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable) {
        QComment parentComment = new QComment("parentComment");
        List<CommentViewDto> fetch = queryFactory
                .select(
                        Projections.bean(
                                CommentViewDto.class,
                                comment.id.as("commentId"),
                                member.nickName.as("nickname"),
                                member.id.as("memberId"),
                                comment.textBody.as("textBody"),
                                comment.likes.as("likes"),
                                post.id.as("postId"),
                                parentComment.id.as("parentCommentId"),
                                comment.isCommentForComment.as("isCommentForComment"),
                                comment.lastModifiedTime.as("lastModifiedTime"),
                                comment.depth.as("depth")
                        )
                )
                .from(comment)
                .join(comment.parentPost, post)
                .join(comment.member, member)
                .leftJoin(comment.parentComment, parentComment)
                .where(
                        comment.parentPost.id.eq(postId),
                        comment.deletedTrue.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.id.asc())
                .fetch();
//        List<Comment> fetch = queryFactory
//                .selectFrom(comment)
//                .where(comment.parentPost.id.eq(postId))
//                .fetch();

        int size = queryFactory
                .selectFrom(comment)
                .where(
                        comment.parentPost.id.eq(postId),
                        comment.deletedTrue.eq(false)
                )
                .fetch().size();

        return new PageImpl<>(fetch, pageable, size);
    }

    @Override
    public Boolean getIsCommentForComment(Long commentId) {
        Boolean aBoolean = queryFactory
                .select(comment.isCommentForComment)
                .from(comment)
                .where(comment.id.eq(commentId))
                .fetchOne();
        return aBoolean;
    }
}
