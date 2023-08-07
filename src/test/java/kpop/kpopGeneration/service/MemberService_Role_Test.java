package kpop.kpopGeneration.service;


import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberService_Role_Test {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버의 기본 Role은 ROLE_USER")
    void saveMember() {

        //given
        Member member = new Member("aaaa", "1111", "member1");
        Long save = memberService.save(member);
        Member savedMember = memberRepository.findById(save).get();

        //when
        Optional<List<MemberRole>> optional = memberRoleRepository.findAllByMemberFetch(savedMember);

        //then
        assertEquals(1, optional.get().size());
        assertTrue("ROLE_USER".equals(optional.get().get(0).getRole().getName()));
    }


    List<String> namesList = Arrays.asList("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN");

    @Test
    @DisplayName("role 업데이트하기 ")
    void updateRoleTest(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Long save = memberService.save(member);
        Member savedMember = memberRepository.findById(save).get();

        //when
        List<String> names = new ArrayList<>();
        for (int i = 0; i < namesList.size(); i++) {
            names.add(namesList.get(i));
            memberService.updateMemberRole("aaaa", names);
            assertEquals(i+1, memberRoleRepository.findAllByMemberFetch(savedMember).get().size());
        }


    }
}
