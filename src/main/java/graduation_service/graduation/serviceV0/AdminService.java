package graduation_service.graduation.serviceV0;

import graduation_service.graduation.domain.entity.Admin;
import graduation_service.graduation.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //로그인
    public Admin login(String loginId, String loginPw) {
        Admin admin = adminRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));

        if (!passwordEncoder.matches(loginPw, admin.getLoginPw())) {
            throw new IllegalArgumentException("PW가 잘못되었습니다.");
        }

        return admin;
    }

}
