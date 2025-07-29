package graduation_service.graduation.domain.enums;

import java.util.Arrays;

public enum Department {
    COMPUTER_SCIENCE("computer-science"),
    AI_ENGINEERING("ai_engineering"),
    DATA_SCIENCE("data_science");

    private final String urlName;

    Department(String urlName) {
        this.urlName = urlName;
    }

    public static Department fromUrl(String url) {
        return Arrays.stream(values()) //values() 모든 enum값을 배열로 반환
                .filter(d -> d.urlName.equals(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid department"));
    }

}
