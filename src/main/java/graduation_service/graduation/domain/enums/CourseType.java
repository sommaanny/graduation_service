package graduation_service.graduation.domain.enums;

import java.util.Arrays;

public enum CourseType {
    MAJOR_REQUIRED("major-required", "전공필수"),
    MAJOR_ELECTIVE("major-elective", "전공선택"),
    GENERAL_REQUIRED("general-required", "교양필수"),
    GENERAL_ELECTIVE("general-elective", "교양선택");

    private final String urlName;       // URL-friendly 이름
    private final String displayName;   // 사용자 표시용 이름

    CourseType(String urlName, String displayName) {
        this.urlName = urlName;
        this.displayName = displayName;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // URL에서 받은 문자열로 enum 찾기
    public static CourseType fromUrlName(String urlName) {
        return Arrays.stream(values())
                .filter(ct -> ct.urlName.equals(urlName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid course type"));
    }
}
