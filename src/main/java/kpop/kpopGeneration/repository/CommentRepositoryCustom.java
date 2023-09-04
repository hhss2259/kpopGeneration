package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom  {
    Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable);

    Page<Comment> findPureCommentListByPost(Long postId, Pageable pageable);

    Boolean getIsCommentForComment(Long commentId);

    Optional<Comment> findCommentById(Long commentId);
}
