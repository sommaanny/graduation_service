package graduation_service.graduation.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 2. CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 3. HTTP Basic 인증 비활성화 (사용하지 않으므로)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 4. Form 기반 로그인 비활성화 (REST API이므로 Form 페이지가 필요 없습니다)
                .formLogin(AbstractHttpConfigurer::disable)

                // 5. 경로별 인가(Authorization) 설정
                .authorizeHttpRequests(authorize -> authorize

                        // 다음 경로들은 인증 없이 접근 허용
                        .requestMatchers(
                                "/admin/login", "/transcript/**",
                                "/english-types/**", "/departments/**",
                                "/graduation-check").permitAll()

                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )

                //로그아웃 기능
                .logout(logout -> logout
                        .logoutUrl("/admin/logout") // 로그아웃 처리 URL
                        .logoutSuccessHandler(myLogoutSuccessHandler) //로그아웃 성공시 핸들러
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                );



        return http.build();
    }


}
