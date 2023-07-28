package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberRepositoryCustom {

    Optional<Member> findByUsername(String username);
}
