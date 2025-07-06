package graduation_service.graduation.domain.pojo;

import graduation_service.graduation.domain.enums.TestType;
import lombok.Getter;

@Getter
public class English {

    private Long id;

    private TestType testType;

    private float grade;
}
