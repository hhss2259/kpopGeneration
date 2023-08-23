package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PostImageRepositoryCustom {
    List<PostImage> findNotThumbNail(Post post);

    Optional<PostImage> findThumbNail(Post post);

    void deleteAllPostImage(Post post);

    List<PostImage> findAllPostImage(Post post);

    Optional<PostImage> findPostImageBySrc(String src, Post post);
    void deletePostImageBySrc(String src);

}
