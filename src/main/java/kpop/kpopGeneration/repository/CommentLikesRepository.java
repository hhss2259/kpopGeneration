package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {

    Optional<CommentLikes> findByMemberAndComment(Member member, Comment comment);
}
