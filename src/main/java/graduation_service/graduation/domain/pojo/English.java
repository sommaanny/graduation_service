package graduation_service.graduation.domain.pojo;

import graduation_service.graduation.domain.enums.EnglishScorePolicy;
import graduation_service.graduation.domain.enums.TestType;
import lombok.Getter;


@Getter
public class English {

    private TestType testType;

    private Float numericScore; //다른 시험 점수

    private String gradeScore; //OPIC 점수

    public boolean isPassed() {
        //name() - enum 상수를 문자열로 반환하는 함수
        //valueOf() - 문자열로 enum 상수를 가져올 수 있는 함수
        EnglishScorePolicy policy = EnglishScorePolicy.valueOf(testType.name());

        if (testType == TestType.OPIC) {
            return policy.isPassed(gradeScore);
        } else {
            return policy.isPassed(numericScore);
        }
    }

    public English() {
    }

    public English(TestType testType, float score) {
        this.testType = testType;
        this.numericScore = score;
    }

    public English(TestType testType, String grade) {
        this.testType = testType;
        this.gradeScore = grade;
    }

}
