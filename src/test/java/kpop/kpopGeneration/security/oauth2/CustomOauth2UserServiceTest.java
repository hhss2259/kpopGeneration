package kpop.kpopGeneration.security.oauth2;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import kpop.kpopGeneration.security.entity.repository.RoleRepository;
import kpop.kpopGeneration.security.exception.NotExistedRoleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CustomOauth2UserServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRoleRepository memberRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Test
    @DisplayName("이미 회원가입한 유저인 경우")
    void joinSns(){

        //given - 이미 회원가입한 회원
        String username = "aaaa";
        String password = "xxx";
        Member member1 = new Member(username, "1111", "member1");
        Member savedMember = memberRepository.save(member1);
        Role role_user = roleRepository.findByName("ROLE_USER").get();
        Role role_manager = roleRepository.findByName("ROLE_MANAGER").get();
        memberRoleRepository.save(new MemberRole(savedMember, role_user));
        memberRoleRepository.save(new MemberRole(savedMember, role_manager));

        // when - loadUser 동작
        Member member = null;
        List<GrantedAuthority> roles = new ArrayList<>();
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if(byUsername.isPresent()){ //이미 회원가입된 user입니다.
            member = byUsername.get();
            List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(member).get();
            memberRoles.forEach( memberRole -> {
                roles.add(new SimpleGrantedAuthority(memberRole.getRole().getName()));
            });
        }else{
            Member newMember = new Member(username, password, null);
            member = memberRepository.save(newMember);
            Role basicRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotExistedRoleException());
            memberRoleRepository.save(new MemberRole(member, basicRole));

            roles.add(new SimpleGrantedAuthority(basicRole.getName()));
        }

        //then
        Member originalMember = memberRepository.findByUsername(username).get();
        assertNotNull(originalMember.getNickName());
        assertEquals(2, roles.size());
    }

    @Test
    @DisplayName("아직 회원가입하지 않은 유저인 경우")
    void newSns(){

        String username = "aaaa";
        String password = "xxx";

        // when - loadUser 동작
        Member member = null;
        List<GrantedAuthority> roles = new ArrayList<>();
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if(byUsername.isPresent()){ //이미 회원가입된 user입니다.
            member = byUsername.get();
            List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(member).get();
            memberRoles.forEach( memberRole -> {
                roles.add(new SimpleGrantedAuthority(memberRole.getRole().getName()));
            });
        }else{
            Member newMember = new Member(username, password, null);
            member = memberRepository.save(newMember);
            Role basicRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotExistedRoleException());
            memberRoleRepository.save(new MemberRole(member, basicRole));

            roles.add(new SimpleGrantedAuthority(basicRole.getName()));
        }

        //then
        Member originalMember = memberRepository.findByUsername(username).get();
        assertNull(originalMember.getNickName());
        assertEquals(1, roles.size());
        List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(originalMember).get();
        assertEquals(1, memberRoles.size());

    }

}