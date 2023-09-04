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

        Resource resource_post_write = resourceRepository.save(new Resource("/post"));
        resourceRoleRepository.save(new ResourceRole(resource_post_write, role_user));
        resourceRoleRepository.save(new ResourceRole(resource_post_write, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_post_write, role_admin));

        Resource resource_news_write = resourceRepository.save(new Resource("/news"));
        resourceRoleRepository.save(new ResourceRole(resource_news_write, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_news_write, role_admin));

        Resource resource_logout = resourceRepository.save(new Resource("/logout"));
        resourceRoleRepository.save(new ResourceRole(resource_logout, role_user));
        resourceRoleRepository.save(new ResourceRole(resource_logout, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_logout, role_admin));

        Resource resource_api_comment_likes = resourceRepository.save(new Resource("/api/comment/likes/toggle"));
        resourceRoleRepository.save(new ResourceRole(resource_api_comment_likes, role_user));
        resourceRoleRepository.save(new ResourceRole(resource_api_comment_likes, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_api_comment_likes, role_admin));

        Resource resource_api_post_likes = resourceRepository.save(new Resource("/api/comment/likes/toggle"));
        resourceRoleRepository.save(new ResourceRole(resource_api_post_likes, role_user));
        resourceRoleRepository.save(new ResourceRole(resource_api_post_likes, role_manager));
        resourceRoleRepository.save(new ResourceRole(resource_api_post_likes, role_admin));



    }


}
