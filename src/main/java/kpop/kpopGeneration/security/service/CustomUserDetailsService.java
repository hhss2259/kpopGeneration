package kpop.kpopGeneration.security.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.NotExistedCommentException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
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
        Optional<Member> byUsername = memberRepository.findByUsername(username);



        Member member = byUsername.orElseThrow(() -> new UsernameNotFoundException("username not found"));
        Optional<MemberRole> byMember = memberRoleRepository.findByMember(member);
        MemberRole memberRole = byMember.orElseThrow(() -> new RuntimeException("ROLE이 반드시 존재해야 합니다"));



        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(memberRole.getRole().getName()));
        MemberContext memberContext = new MemberContext(member, roles);

        return memberContext;
    }
}
