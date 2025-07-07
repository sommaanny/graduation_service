package graduation_service.graduation.domain.pojo;

import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class UserInfo {

    // student_id
    private int studentId;

    //department
    private Department department;
}
