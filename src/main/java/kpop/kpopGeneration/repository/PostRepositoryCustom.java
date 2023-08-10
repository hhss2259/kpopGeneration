package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    int findCnt();

    Page<Post> findPostListByCategory(Category category, Pageable pageable);

    Page<Post> findALLPostList(Pageable pageable);

    Optional<Post> findPostById(Long Id);
}
