package graduation_service.graduation.domain.enums;

import java.util.Arrays;

public enum CoreType {
    CORE_1("core-1"), //핵심교양
    CORE_2("core-2"),
    CORE_3("core-3"),
    CORE_4("core-4"),
    CORE_5("core-5"),
    CORE_6("core-6"),
    CREATIVITY("creativity"), //창의
    SW_AI("sw-ai");

    private String urlName;

    CoreType(String urlName) {
        this.urlName = urlName;
    }

    public String getUrlName() {
        return urlName;
    }

    // URL에서 받은 문자열로 enum 찾기
    public static CoreType fromUrlName(String urlName) {
        return Arrays.stream(values())
                .filter(ct -> ct.urlName.equals(urlName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid course type"));
    }
}
