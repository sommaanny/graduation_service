package graduation_service.graduation.api.exampleConst;

public class TranscriptExample {
    public static final String TRANSCRIPT_SUCCESS =
                """
                {
                    "status": "SUCCESS",
                    "message": "성적표 등록 완료",
                    "data": {
                    	  "totalCredits": 127,
                    	  "requiredMajorCredits": 32,
                    	  "electiveMajorCredits": 35,
                    	  "basicMajorCredits": 0,
                    	  "requiredGeneralEducationCredits": 30,
                    	  "electiveGeneralEducationCredits": 30,
                    	  "transferredMajorCredits": 0,
                    	  "totalTransferredCredits": 0,
                    	  "otherEarnedCredits": 0,
                    	  "pga": 3.7,
                    	  "completedCourseNumbers":
                    	  [
                    		"AIE2004",
                    	    "CSE1010",
                    	    "MAT1001"
                    	  ]
                    }
                }
                """;
}
