package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.security.entity.Resource;
import kpop.kpopGeneration.security.entity.ResourceRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRoleRepository extends JpaRepository<ResourceRole, Long> {
}
