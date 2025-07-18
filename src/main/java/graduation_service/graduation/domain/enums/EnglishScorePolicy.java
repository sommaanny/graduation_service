package graduation_service.graduation.domain.enums;

public enum EnglishScorePolicy {
    TOEIC(700),
    TOEIC_SPEAKING(130),
    TOEFL_PBT(540),
    TOEFL_CBT(207),
    TOEFL_IBT(76),
    NEW_TEPS(327),
    OPIC(0),  // 특별 처리 필요
    IELTS(6.0f);

    //상수들이 가지는 필드 - 최소 점수
    private final float minimumScore;

    //상수들이 필드 값을 생성자로 설정
    EnglishScorePolicy(float minimumScore) {
        this.minimumScore = minimumScore;
    }

    public float getMinimumScore() {
        return minimumScore;
    }

    //최소 기준을 넘었는가? - 메서드 오버로딩으로 구현

    // 일반 시험 점수 판별
    public boolean isPassed(float inputScore) {
        if (this == OPIC) {
            throw new UnsupportedOperationException("OPIC은 등급 기반이므로 문자열 메서드를 사용하세요.");
        }
        return inputScore >= minimumScore;
    }

    // OPIC 전용 등급 판별
    public boolean isPassed(String grade) {
        if (this != OPIC) {
            throw new UnsupportedOperationException("문자열 판별은 OPIC에만 해당됩니다.");
        }

        return grade.equals("IM1") || grade.equals("IM2") || grade.equals("IH") || grade.equals("AL");
    }

}
