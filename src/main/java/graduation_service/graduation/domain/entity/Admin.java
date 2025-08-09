package graduation_service.graduation.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Admin {

    @Id @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    // login_id
    private String loginId;

    // login_pw
    private String loginPw;

    public Admin(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}
