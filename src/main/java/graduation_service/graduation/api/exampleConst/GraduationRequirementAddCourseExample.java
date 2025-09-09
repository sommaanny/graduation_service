package graduation_service.graduation.api.exampleConst;

public class GraduationRequirementAddCourseExample {
    public static final String GRADUATION_REQUIREMENT_ADD_COURSE_SUCCESS =
            """
            {
              "status": "SUCCESS",
              "message": "졸업요건 과목 등록 성공",
              "data": [
                  {
                    "grId": 1,
                    "year": 2022,
                    "courseId": 3,
                    "courseType": "MAJOR_REQUIRED"
                  }
              ]
            }
            """;
}
