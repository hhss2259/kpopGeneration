package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryCustom{
    Optional<List<Role>> findAllByName(List<String> names);

}
