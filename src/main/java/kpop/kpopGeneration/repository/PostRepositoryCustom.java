package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.RecentPostByMemberDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    int findCnt();

    Page<Post> findPostListByCategory(Category category, Pageable pageable);

    Page<Post> findALLPostList(Pageable pageable);

    Page<RecentPostByMemberDto> findRecentPostListByMember(Member member, Pageable pageable, Long postId);

    Optional<Post> findPostById(Long Id);

    String findWriter(Long id);
}
