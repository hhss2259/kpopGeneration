package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    Optional<MemberRole> findByMember(Member member);
}
