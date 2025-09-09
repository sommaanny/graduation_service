package graduation_service.graduation.api.exampleConst;

public class CourseListFindExample {
    public static final String COURSE_LIST_FIND_SUCCESS =
                    """
                    {
                    "status": "SUCCESS",
                      "message": "과목 조회 성공",
                      "data": [
                    	  {
                    		"id": 1,
                    		"courseNumber": "AIE3001",
                    		"courseTitle": "알고리즘",
                    		"credits": 3
                    	  },
                    	  {
                    		"id": 2,
                    		"courseNumber": "AIE3002",
                    		"courseTitle": "컴퓨터네트워크",
                    		"credits": 3
                    	  },
                    	  {
                    		"id": 3,
                    		"courseNumber": "AIE3003",
                    		"courseTitle": "데이터베이스",
                    		"credits": 3
                    	  }
                      ]
                    }
                    """;

}
