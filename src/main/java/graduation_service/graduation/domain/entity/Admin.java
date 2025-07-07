package graduation_service.graduation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Admin {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    // login_id
    private String loginId;

    // login_pw
    private String loginPw;

}
