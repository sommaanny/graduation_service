package graduation_service.graduation;

import graduation_service.graduation.domain.entity.Admin;
import graduation_service.graduation.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
//@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String adminId = System.getenv("ADMIN_INITIAL_ID");
        String rawPw = System.getenv("ADMIN_INITIAL_PW");

        if (adminId == null || rawPw == null) {
            throw new IllegalStateException("환경변수 ADMIN_INITIAL_ID 또는 ADMIN_INITIAL_PW가 설정되어 있지 않습니다.");
        }

        boolean exists = adminRepository.findByLoginId(adminId).isPresent();
        if (exists) {
            log.info("관리자 계정이 이미 존재합니다. 초기화 생략.");
            return;
        }

        String adminPw = passwordEncoder.encode(rawPw);
        Admin admin = new Admin(adminId, adminPw);
        adminRepository.save(admin);
        log.info("초기 관리자 계정을 생성했습니다.");
    }


}
