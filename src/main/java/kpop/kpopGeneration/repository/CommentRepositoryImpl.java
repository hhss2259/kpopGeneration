package kpop.kpopGeneration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
