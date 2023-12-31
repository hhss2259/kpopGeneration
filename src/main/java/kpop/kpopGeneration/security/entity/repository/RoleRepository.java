package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {
    Optional<Role> findByName(String name);
}
