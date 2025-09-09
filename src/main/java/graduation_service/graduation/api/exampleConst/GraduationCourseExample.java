package graduation_service.graduation.api.exampleConst;

public class GraduationCourseExample {
    public static final String GRADUATION_COURSES_SUCCESS =
            """
            {
              "status": "SUCCESS",
              "message": "졸업요건 과목 조회 성공",
              "data": [
                  {
                    "grId": 1,
                    "year": 2022,
                    "department": "ai-engineering"
                    "course" : [
                    	{
                    	  "courseId": 1
                          "courseNumber": "AIE3001",
                    	  "courseTitle": "알고리즘",
                    	  "credits": 3
                    	  "courseType": "MAJOR_REQUIRED"
                    	},
                    	{
                    	  "courseId": 1
                          "courseNumber": "AIE3002",
                    	  "courseTitle": "자료구조",
                    	  "credits": 4
                    	  "courseType": "MAJOR_REQUIRED"
                    	}
                    ]
                  }
            }
            """;
}
