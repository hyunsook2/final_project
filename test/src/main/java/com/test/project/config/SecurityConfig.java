package com.test.project.config;

import com.test.project.member.dto.JoinDto;
import com.test.project.member.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
        http.authorizeRequests()
                .requestMatchers("/css/**", "/js/**", "/image/**").permitAll()
                .requestMatchers("/login/**", "/join/**", "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringRequestMatchers(
                        new AntPathRequestMatcher("/h2-console/**")
                ).and().headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                )).and().formLogin().loginPage("/login").usernameParameter("email").defaultSuccessUrl("/")
                .and().oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/login").successHandler(authenticationSuccessHandler())
                                .userInfoEndpoint(
                                        userInfoEndPoint -> userInfoEndPoint
                                                .userService(customOAuth2UserService)
                                )
                )
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            boolean hasRegisterUserAuthority = token.getAuthorities().stream()
                    .anyMatch(auth -> "REGISTER_USER".equals(auth.getAuthority()));
            if (hasRegisterUserAuthority) {
                OAuth2User oAuth2User = token.getPrincipal();
                Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
                Map<String, Object> naver_account = (Map<String, Object>) oAuth2User.getAttribute("response"); // 네이버에서 받은 데이터에서 프로필 정보가 담긴 response 값을 꺼냅니다.
                HttpSession session = request.getSession();
                if(kakao_account!=null) {
                    JoinDto joinDto = new JoinDto();
                    joinDto.setName((String)kakao_account.get("name"));
                    joinDto.setEmail((String) kakao_account.get("email"));
                    joinDto.setAgeRange((String)kakao_account.get("age_range"));
                    joinDto.setMobile((String)kakao_account.get("phone_number"));
                    joinDto.setGender((String)kakao_account.get("gender"));
                    session.setAttribute("joinDto",joinDto);
                }else{
                    JoinDto joinDto = new JoinDto();
                    joinDto.setName((String)naver_account.get("name"));
                    joinDto.setEmail((String) naver_account.get("email"));
                    String age =  (String)naver_account.get("age");
                    age = age.replace("-","~");
                    joinDto.setAgeRange(age);
                    joinDto.setMobile((String)naver_account.get("mobile"));
                    String gender = (String) naver_account.get("gender");
                    if(gender.equals("M")) gender="male";
                    else gender ="female";
                    joinDto.setGender(gender);
                    session.setAttribute("joinDto",joinDto);
                }
                // ... 다른 값들도 저장하세요
                response.sendRedirect("/join");
            } else {
                response.sendRedirect("/"); // Or whatever your default success URL is
            }
        };
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

