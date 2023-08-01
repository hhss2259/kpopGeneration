package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> , CommentRepositoryCustom {
}
