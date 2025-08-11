package graduation_service.graduation.api.exampleConst;

public class CoreSubjectListFindByYearExample {
    public static final String CORE_SUBJECT_LIST_FIND_BY_YEAR_SUCCESS =
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
                    		"curriculumYear": 2022,
                    		"coreType": "CORE_3",
                    		"courseId": 6
                    	  }
                    }
                    """;

}
