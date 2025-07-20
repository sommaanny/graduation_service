package graduation_service.graduation.serviceV2;

import graduation_service.graduation.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    //회원가입


    //로그인


}
