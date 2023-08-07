package kpop.kpopGeneration.security.service;

import kpop.kpopGeneration.security.entity.ResourceRole;
import kpop.kpopGeneration.security.entity.repository.ResourceRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityResourceService {

    @Autowired
    private  ResourceRoleRepository resourceRoleRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceRoleList(){
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        Optional<List<ResourceRole>> optional = resourceRoleRepository.findAllResourceRole();
        List<ResourceRole> resourceRoles = optional.orElseGet(() -> null);

        // 만약 하나의 Resouce에 role_user, role_manager, role_admin 세 가지가 있다면
        // resource + role_user, resource+ role_user + role_manager, resouce+ role_user + role+manager+role_admin 세 개 생성
//        resourceRoles.forEach(r -> {
//            List<ConfigAttribute> configAttributeList = new ArrayList<>();
//
//            r.getRoleSet().forEach(role ->{
//                configAttributeList.add(new SecurityConfig(role.getRoleName()));
//                result.put(new AntPathRequestMatcher(resource.getResouceName(), configAttributeList));
//            });
//
//        });

        for (ResourceRole resourceRole : resourceRoles) {
            RequestMatcher key = new AntPathRequestMatcher(resourceRole.getResource().getUrl());

            List<ConfigAttribute> list;
            if(!result.containsKey(key)){
                list = new ArrayList<>();
            }else{
                list = result.get(key);
            }
            list.add(new SecurityConfig(resourceRole.getRole().getName()));
            result.put(key, list);
        }
        return result;
    }

}
