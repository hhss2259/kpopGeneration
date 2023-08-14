package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long>, PostLikesRepositoryCustom {

    Optional<PostLikes> findByMemberAndPost(Member member, Post post);
}
