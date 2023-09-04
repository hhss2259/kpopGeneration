package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository  extends JpaRepository<PostImage, Long>,PostImageRepositoryCustom {

}
