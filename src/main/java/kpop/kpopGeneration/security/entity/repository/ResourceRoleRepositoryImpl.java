package kpop.kpopGeneration.security.entity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.security.entity.QResource;
import kpop.kpopGeneration.security.entity.QResourceRole;
import kpop.kpopGeneration.security.entity.QRole;
import kpop.kpopGeneration.security.entity.ResourceRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static kpop.kpopGeneration.security.entity.QResource.*;
import static kpop.kpopGeneration.security.entity.QResourceRole.*;
import static kpop.kpopGeneration.security.entity.QRole.*;

public class ResourceRoleRepositoryImpl implements ResourceRoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ResourceRoleRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Optional<List<ResourceRole>> findAllResourceRole() {
        List<ResourceRole> fetch = queryFactory
                .select(resourceRole)
                .from(resourceRole)
                .join(resourceRole.resource, resource).fetchJoin()
                .join(resourceRole.role, role).fetchJoin()
                .orderBy(resourceRole.orderPriority.asc())
                .fetch();


        return Optional.ofNullable(fetch);
    }
}
