package kpop.kpopGeneration.security;

import kpop.kpopGeneration.security.common.FormAuthenticationDetailSource;
import kpop.kpopGeneration.security.entity.repository.ResourceRoleRepository;
import kpop.kpopGeneration.security.factory.UrlResourcesMapFactoryBean;
import kpop.kpopGeneration.security.handler.CustomAccessDeniedHandler;
import kpop.kpopGeneration.security.handler.CustomAuthenticationFailureHandler;
import kpop.kpopGeneration.security.handler.CustomAuthenticationSuccessHandler;
import kpop.kpopGeneration.security.handler.CustomLogoutSuccessHandler;
import kpop.kpopGeneration.security.metadatasource.UrlFilterInvocationSecurityMetadatasource;
import kpop.kpopGeneration.security.provider.CustomAuthenticationProvider;
import kpop.kpopGeneration.security.service.CustomUserDetailsService;
import kpop.kpopGeneration.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     *  PasswordEncoder 등록 => DB에 PW를 저장 시 암호화 저장, PasswordEncode를 통해서 비밀번호 검사
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 로그인 인증 시도 시 DB에 접근해 해당하는 username을 가진 member를 조회해온다
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     *  AuthenticationProvider 등록 => 실제 인증을 담당하는 클래스 , UserDetailsService가 조회해온 Member의 Password를 인증한다
     *  인증 성공 시 AuthenticationToken을 만들며, 해당 토큰에는 member 객체와 member 객체의 role이 담겨 있다
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    /** 
     * CustomAuthenticationProvider를 AuthenticationManager에 등록하면, Manager가 해당 Provider를 직접 사용하여 인증을 수행한다
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    /**
     *  AuthenticationDetailsSource 인터페이스를 구현해서 빈으로 등록하면,
     *  인증 과정에서 HttpServletRequest에서 필요한 정보를 추출하여 인증 과정에서 사용하는 등, 인증에 사용되는 세부 정보 등을 사용할 수 있게 도와준다
     *  여기서 구현한 ForAuthenticationDetailsService는  HttpServletRequest로부터 secret_key라는 값을 조회해온다.
     *  secret_key가 정확하면 유효한 접근이라고 판단한다.
     */
    @Bean
    public AuthenticationDetailsSource formAuthenticationDetailSource() {
        return new FormAuthenticationDetailSource();
    }

    /**
     * 인증에 성공했을 때 사용할 Handler
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     * 인증에 실패했을 때 사용할 Handler
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /**
     * 최종적인 필터인 FilerSecurityInterceptor가 최종적으로 사용자의 권한을 평가할 때, 권한 검사에 실패했을 때 사용되는 핸들러
     *  FilterSecurityInterceptor는 SecurityMetaDatasource로부터 DB에 저장된 각 url의 권한 정보를 조회해오며,
     *  AccessDecisionHandler에게 이 url에게 설정된 권한 정보와 현재 접근자의 권한 정보를 비교하도록 요청함으로써 권한 평가를 수행한다
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();
        customAccessDeniedHandler.setErrorPage("/denied");
        return customAccessDeniedHandler;
    }

    /**
     *  정적 파일들에 대해서는 권한 검사를 하지 않는다.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        web
                .ignoring()
                .antMatchers("/resources/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();


        http
                .authorizeRequests()
                        .anyRequest().permitAll();

        /**
         * 인증 과정에서 form 로그인을 사용한다.
         */
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .authenticationDetailsSource(formAuthenticationDetailSource()) //인증 과정에서 HttpServletRequest로부터 접근에 대한 세부 정보를 가져온다
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll();

        /**
         * 직접 구혀한 CustomSecurityInterceptor를 스프링 시큐리티가 제공하는 기본 인터셉터 앞에 위치시킨다
         * 따라서 기본 인터셉터보다 먼저 실행되며,
         * 권한 평가에 성공했을 경우, 기본 인터셉터는 사용되지 않고 인증/인가 프로세스를 종료한다
         */
        http
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        /**
         * FilterSecurityInterceptor를 통한 권한 평가 시, 해당 접근자에게 권한이 없다면 해당 Handler가 작동한다
         */
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());

        /**
         * 로그아웃 처리에 대한 설정을 담당한다
         * 로그아웃 성공 시 Session과 Cookie를 무효화하며, SecurityContext를 clear 함으로써 Authentication 객체를 제거한다
         */
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler());


        /**
         * 브라우저에서 form 태그 사용 시 에러가 발생해서 추가해주었다
         */
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }




    /**
     * 세부 설정을 거쳐 새로운 FitlerSecurityInterceptor를 만들어 등록한다.
     * FilterSecurityInterceptor => 해당 인터셉터가 최종적으로 사용자의 권한 정보를 평가하고, 해당 요청을 허용할지 거부할지 결정한다
     *
     * FitlerSecurityInterceptor를 등록하기 위해서 세 가지 정보가 필요하다
     * 1. SecurityInterceptorMetadatasource : DB에 접근하여 url에 설정되어 있는 권한 정보를 조회해온다
     * 2. AuthenticationDecisionManager : FilterSecurityInterceptor는 AccessManager에게 실제 권한 평가의 수행을 위임한다
     *         AUthenticationDecisionManager는 Voter를 통해서 실제 권한 평가를 수행함으로 어떠한 Voter를 사용할 것인지 등록해주어야 한다
     * 3. AuthenticationManager : 인증 과정에서 사용되는 매니저이다
     */
    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadatasource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        return filterSecurityInterceptor;
    }

    /**
     * 현재 사용되는 AuthenticationManage를 반환한다
     * AuthenticationMaanger를 실제로 인증 처리를 담당하는 여러 Provider 중에서 해당 인증 요청을 처리할 수 있는 하나의 Provider를 찾아
     * 인증 요청을 위임하는 역할을 한다
     */
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 최종적으로 권한 평가를 수행함으로써 해당 접근을 허용할지 거부할지 결정하는 FitlerSecurityInterceptor에게
     * DB에 저장되어 있는 url 권한 설정 정보를 넘겨주는 역할을 한다.
     * 이 정보를 바탕으로 인터셉터는 현재 사용자의 권한 정보와 사용자가 접근하고자 하는 url에 설정되어 있는 권한 정보를 비교해
     * 이 접근을 허용할지 결정한다
     */
    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadatasource() throws Exception {
        return new UrlFilterInvocationSecurityMetadatasource(urlResourcesMapFactoryBean().getObject(), securityResourceService());
    }

    // SecurityMetadatasource가 사용하는 Service로 실제로 DB에 접근해 각 url 별 인가 권한을 조회해온다
    @Bean
    public SecurityResourceService securityResourceService(){
        SecurityResourceService securityResourceService = new SecurityResourceService();
        return securityResourceService;
    }

    /**
     * 어플리케이션 최초 동작 시 , DB에 저장되어 있는 url 권한 정보를 추출하여 제공한다
     */
    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(){
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean(securityResourceService());
        return urlResourcesMapFactoryBean;
    }

    /**
     *  AccessDecsionManager는 Voter를 통해 실제 권한 평가를 수행하는 역할을 맡는다
     */
    private AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    /**
     * 어떠한 Voter를 사용할 지 결정한다.
     */
    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }

}
