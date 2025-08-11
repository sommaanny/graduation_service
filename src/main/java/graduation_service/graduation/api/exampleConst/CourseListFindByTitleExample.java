package graduation_service.graduation.api.exampleConst;

public class CourseListFindByTitleExample {
    public static final String COURSE_LIST_FIND_BY_ID_SUCCESS =
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
                    		"courseNumber": "CSE1002",
                    		"courseTitle": "알고리즘",
                    		"credits": 3
                    	  },
                    	  {
                    		"id": 3,
                    		"courseNumber": "DSC4001",
                    		"courseTitle": "알고리즘",
                    		"credits": 3
                    	  }
                      ]
                    }
                    """;

}
