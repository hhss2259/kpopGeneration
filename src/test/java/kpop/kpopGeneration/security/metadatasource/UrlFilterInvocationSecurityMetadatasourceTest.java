package kpop.kpopGeneration.security.metadatasource;

import kpop.kpopGeneration.security.SecurityConfig;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.service.SecurityResourceService;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.security.Security;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UrlFilterInvocationSecurityMetadatasourceTest {


    @Autowired
    SecurityResourceService securityResourceService;
    @Test
    @DisplayName("securityMetadatasource 테스트")
    void testMetadataSource(){
        //given
        /**
         * DB에 저장해놓은 권한 정보를 가지고 온다
         */
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = securityResourceService.getResourceRoleList();

        //when
        UrlFilterInvocationSecurityMetadatasource urlFilterInvocationSecurityMetadatasource = new UrlFilterInvocationSecurityMetadatasource(requestMap,securityResourceService);

        //then
        /**
         *  현재 "/test/member"에 접근할 수 있는 role 은 총 세 가지이다.
         *  1. ROLE_USER
         *  2. ROLE_MANAGER
         *  3. ROLE_ADMIN
         */
        Collection<ConfigAttribute> list1 = urlFilterInvocationSecurityMetadatasource.getAttributes(new FilterInvocation("/test/member", HttpMethod.GET.name()));
        assertEquals(3, list1.size());


        /**
         *  현재 "/test/manager"에 접근할 수 있는 role 은 총 두 가지이다.
         *  1. ROLE_MANAGER
         *  2. ROLE_ADMIN
         */
        Collection<ConfigAttribute> list2 = urlFilterInvocationSecurityMetadatasource.getAttributes(new FilterInvocation("/test/manager", HttpMethod.GET.name()));
        assertEquals(2, list2.size());

        /**
         *  현재 "/test/manager"에 접근할 수 있는 role 은 총 한 가지이다.
         *  1. ROLE_ADMIN
         */
        Collection<ConfigAttribute> list3 = urlFilterInvocationSecurityMetadatasource.getAttributes(new FilterInvocation("/test/admin", HttpMethod.GET.name()));
        assertEquals(1, list3.size());
    }
}