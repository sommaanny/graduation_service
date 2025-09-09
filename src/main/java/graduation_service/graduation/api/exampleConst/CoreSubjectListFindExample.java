package graduation_service.graduation.api.exampleConst;

public class CoreSubjectListFindExample {
    public static final String CORE_SUBJECT_LIST_FIND_SUCCESS =
                    """
                    {
                    "status": "SUCCESS",
                      "message": "핵심교양 조회 성공",
                      "data":
                    	  {
                    		"curriculumYear": 2022,
                    		"coreType": "CORE_1",
                    		"courseId": 1
                    	  },
                    	  {
                    		"curriculumYear": 2022,
                    		"coreType": "CORE_2",
                    		"courseId": 4
                    	  },
                    	  {
                    		"curriculumYear": 2023,
                    		"coreType": "CORE_1",
                    		"courseId": 1
                    	  }
                    }
                    """;

}
