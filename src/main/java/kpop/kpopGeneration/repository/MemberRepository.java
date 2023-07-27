package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberRepositoryCustom{
}
