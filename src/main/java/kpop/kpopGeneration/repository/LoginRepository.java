package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findMemberByUsername(String username);
}
