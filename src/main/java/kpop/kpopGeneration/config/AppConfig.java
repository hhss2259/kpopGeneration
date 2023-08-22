package kpop.kpopGeneration.config;

import kpop.kpopGeneration.security.entity.Resource;
import kpop.kpopGeneration.security.entity.ResourceRole;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.entity.repository.ResourceRepository;
import kpop.kpopGeneration.security.entity.repository.ResourceRoleRepository;
import kpop.kpopGeneration.security.entity.repository.RoleRepository;
import kpop.kpopGeneration.security.service.SecurityResourceService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceRoleRepository resourceRoleRepository;

    /**
     * 회원가입 시 기본 role을 저장한다
     * 세 가지 Role이 존재
     * 1. ROLE_USER
     * 2. ROLE_MANAGER
     * 3. ROLE_ADMIN
     */
    @PostConstruct
    public void saveBasicRole() {
        Role role_user = roleRepository.save(new Role("ROLE_USER"));
        Role role_manager = roleRepository.save(new Role("ROLE_MANAGER"));
        Role role_admin = roleRepository.save(new Role("ROLE_ADMIN"));

        Resource resource_member = resourceRepository.save(new Resource("/test/member"));
        Resource resource_manager = resourceRepository.save(new Resource("/test/manager"));
        Resource resource_admin = resourceRepository.save(new Resource("/test/admin"));

        resourceRoleRepository.save(new ResourceRole(resource_member, role_user));
        resourceRoleRepository.save(new ResourceRole(resource_member, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_member, role_admin));
        resourceRoleRepository.save(new ResourceRole(resource_manager, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_manager, role_admin));
        resourceRoleRepository.save(new ResourceRole(resource_admin, role_admin));


    }


}
