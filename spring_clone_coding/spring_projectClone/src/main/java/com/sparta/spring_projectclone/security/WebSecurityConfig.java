package com.sparta.spring_projectclone.security;

import com.sparta.spring_projectclone.jwt.JwtAuthenticationFilter;
import com.sparta.spring_projectclone.jwt.JwtAuthorizationFilter;
import com.sparta.spring_projectclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;

    @Override
    public void configure (WebSecurity web){
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web.ignoring().antMatchers("/h2-console/**");
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }



    @Bean
    public BCryptPasswordEncoder encodePassword() {
        // 패스워드 암호화
        return new BCryptPasswordEncoder();

    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        // 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
//        //http.csrf().ignoringAntMatchers("/user/**");
//
//        //POST 요청들이 문제없이 처리된다. csef 무시
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                //회원 관리 처리 API 전부를 login 없이 허용
//                .antMatchers("/user/**").permitAll()
//                // image 폴더를 login 없이 허용
//                .antMatchers("/images/**").permitAll()
//                // css 폴더를 login 없이 허용
//                .antMatchers("/css/**").permitAll()
//                //.antMatchers("/api/**").permitAll()
//                .antMatchers("/api/**").permitAll()

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 시큐리티 폼로그인기능 비활성화
                .formLogin().disable()
                // 로그인폼 화면으로 리다이렉트 비활성화
                .httpBasic().disable()
                // UsernamePasswordAuthenticationFilter 단계에서 json 로그인과 jwt 토큰을 만들어 response 반환
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // AuthenticationManager

                // BasicAuthenticationFilter 단계에서 jwt 토큰 검증
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))

                .authorizeRequests()

                // PreFlight 요청 모두 허가
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                // 게시글 인증
                //.antMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/posts/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()

                // 댓글 인증    로그인을 하지 않았을 경우, 예외처리가 아닌 403 error 발생
                //.antMatchers(HttpMethod.DELETE, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/comment/**").authenticated()

                // 그 외 요청 모두 허가
                .anyRequest().permitAll()
                .and().cors();
    }
}