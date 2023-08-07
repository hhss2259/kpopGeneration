package kpop.kpopGeneration.security.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import kpop.kpopGeneration.security.exception.NotExistedRoleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRoleRepository memberRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Member 객체 조회
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        Member member = byUsername.orElseThrow(() -> new UsernameNotFoundException("username not found"));

        // Member의 권한 정보 조회
        Optional<List<MemberRole>> byMember = memberRoleRepository.findAllByMemberFetch(member);
        List<MemberRole> memberRoles = byMember.orElseThrow(() -> new NotExistedRoleException("ROLE이 반드시 존재해야 합니다"));

        // MemberRole 객체를 GrantedAuthority로 변환
        List<GrantedAuthority> roles = new ArrayList<>();
        memberRoles.forEach( memberRole -> {
            roles.add(new SimpleGrantedAuthority(memberRole.getRole().getName()));
        });

        MemberContext memberContext = new MemberContext(member, roles);
        return memberContext;
    }
}
