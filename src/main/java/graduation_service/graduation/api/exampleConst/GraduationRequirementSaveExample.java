package graduation_service.graduation.api.exampleConst;

public class GraduationRequirementSaveExample {
    public static final String GRADUATION_REQUIREMENT_SAVE_SUCCESS =
            """
            {
              "status": "SUCCESS",
              "message": "졸업요건 등록 성공",
              "data": [
                  {
                    "id": 1
                    "department": AI_ENGINEERING
                    "totalCredits": 130
                    "majorCredits": 65
                    "requiredMajorCredits": 30,
                    "generalEducationCredits": 65,
                    "requiredGeneralEducationCredits": 25,
                    "gpa": 3.0
                    "graduationRequirementsYear": 2022
                  }
              ]
            }
            """;
}
