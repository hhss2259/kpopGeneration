package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Post, Long>, BoardRepositoryCustom {

}
