package graduation_service.graduation.configuration;


import graduation_service.graduation.filter.CsrfTokenDebugFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyLogoutSuccessHandler myLogoutSuccessHandler;
    private final CsrfTokenDebugFilter csrfTokenDebugFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //cors 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. CSRF 보호 쿠키 방식
                // 첫 GET 요청의 응답에 XSRF-TOKEN 쿠키를 실어보냄.
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // 해당 API 경로는 CSRF 검증에서 제외합니다.
                        .ignoringRequestMatchers(
                                "/admin/login", "/admin/logout",
                                "/transcript/**", "/english-types/**",
                                "/departments/**", "/graduation-check")
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()) // Xor 방식 제거
                )

                // 3. HTTP Basic 인증 비활성화 (사용하지 않으므로)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 4. Form 기반 로그인 비활성화 (REST API이므로 Form 페이지가 필요 없습니다)
                .formLogin(AbstractHttpConfigurer::disable)

                // 5. 경로별 인가(Authorization) 설정
                .authorizeHttpRequests(authorize -> authorize
                        //정적 리소스 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 다음 경로들은 인증 없이 접근 허용
                        .requestMatchers(
                                "/admin/login", "/transcript/**",
                                "/english-types/**", "/departments/**",
                                "/graduation-check", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()

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

        http.addFilterBefore(csrfTokenDebugFilter, CsrfFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://14.52.129.190:5173");
        config.addAllowedOrigin("https://suborbicularly-bushlike-rodolfo.ngrok-free.dev/"); // 허용할 오리진
        config.addAllowedMethod("*"); // 허용할 HTTP 메서드
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.setAllowCredentials(true); // 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 이 설정 적용
        return source;
    }

}
