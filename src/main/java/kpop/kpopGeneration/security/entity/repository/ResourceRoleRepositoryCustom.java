package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.security.entity.ResourceRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRoleRepositoryCustom {

    Optional<List<ResourceRole>> findAllResourceRole();
}
