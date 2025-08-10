package graduation_service.graduation.api;

import graduation_service.graduation.domain.entity.Admin;
import graduation_service.graduation.dto.requestDto.loginDto.LoginRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.serviceV0.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Admin-Controller", description = "관리자 로그인 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin/login")
    public ApiResponse<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        String loginId = loginRequest.getLoginId();
        String loginPw = loginRequest.getLoginPw();

        Admin admin = adminService.login(loginId, loginPw);//로그인에 성공하면 예외가 발생하지 않음 따라서 세션 생성

        // 인증 객체 생성 (Authentication)
        // 앞으로 ADMIN 역할을 부여하기 위해 GrantedAuthority 리스트 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // "ROLE_" 접두사는 필수

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                admin, // Principal (사용자 정보)
                null,  // Credentials (비밀번호, 인증 후엔 보통 null 처리)
                authorities // Authorities (권한 목록)
        );

        // SecurityContext에 넣기
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        HttpSession session = request.getSession(); //세션 생성
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext); //세션 저장소에 로그인 회원 정보 보관

        return ApiResponse.success("관리자 로그인 성공", null);
    }

}
