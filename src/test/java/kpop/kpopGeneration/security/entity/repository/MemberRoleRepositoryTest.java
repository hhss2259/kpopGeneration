package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.exception.NotExistedRoleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberRoleRepositoryTest {

    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    List<String> namesList = Arrays.asList("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN");


    @Test
    @DisplayName("멤버의 모든 MemberRole 조회하기")
    void findAllByMemberFetch(){
        //given
        Member member1 = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member1);

        // 아직 memberRole을 아무것도 저장하지 않은 상태
        Optional<List<MemberRole>> optionalNull = memberRoleRepository.findAllByMemberFetch(savedMember);
        assertEquals(0, optionalNull.get().size());

        // memberRole을 하나씩 저장하고, 저장할 때마다 모든 memberRole을 조회한다

        for (int i = 0; i < namesList.size(); i++) {
            Role role = roleRepository.findByName(namesList.get(i)).get();
            memberRoleRepository.save(new MemberRole(savedMember, role));
            
            List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(savedMember).get();
            assertEquals(i + 1, memberRoles.size());
            
            //출력 확인
            for (MemberRole memberRole : memberRoles) {
                System.out.print(memberRole.getRole().getName()+" ");
            }
            System.out.println();
        }
    }

    @Test
    @DisplayName("멤버의 모든 MemberRole 삭제하기")
    void deleteAllByMember(){
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);


        for (int i = 0; i < namesList.size(); i++) {
            Role role = roleRepository.findByName(namesList.get(i)).get();
            memberRoleRepository.save(new MemberRole(savedMember, role));

            memberRoleRepository.deleteAllByMember(savedMember);
            assertEquals(0, memberRoleRepository.findAllByMemberFetch(savedMember).get().size());
        }
    }


}