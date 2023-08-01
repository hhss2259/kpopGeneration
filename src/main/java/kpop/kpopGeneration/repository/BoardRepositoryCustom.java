package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface BoardRepositoryCustom {
    int findCnt();

    Page<Post> findPostListByCategory(Category category, Pageable pageable);

    Page<Post> findALLPostList(Pageable pageable);

    Optional<Post> findPostById(Long Id);
}
