package graduation_service.graduation.api.exampleConst;

public class GraduationCheckExample {
    public static final String GRADUATION_CHECK_SUCCESS =
                    """
                    {
                      "status": "SUCCESS",
                      "message": "졸업요건 진단 완료",
                      "data": [
                    	  {
                    		"graduated": false, #졸업요건 충족 여부
                    		"englishPassed": false, #영어성적 충족 여부
                    		"creditStatus": #학점 충족 상태
                    			{
                    			  "creditPassed": true, #학점 충족 여부
                    			  "missingRequiredMajorCredits": 0, #모자란 전공 필수 학점
                    			  "missingElectiveMajorCredits": 0, #모자란 전공 선택 학점
                    			  "missingRequiredGeneralCredits": 0, #모자란 교양 필수 학점
                    			  "missingElectiveGeneralCredits": 3, #모자란 교양 선택 학점
                    			  "missingTotalCredits": 3 #모자란 총 학점
                    			},
                    			"remainingCourses": #이수하지 못한 과목들
                    			[
                    			  {
                    				"courseTitle": "과목이름",
                    				"courseNumber": "AIE3101",
                    				"credits": 3, #학점
                    				"courseType": GENERAL_ELECTIVE #교양선택
                    			  }
                    			]
                    		"remainingCoreType": #이수하지 못한 핵심교양 타입
                    		    [
                    		      {
                    			    CORE_1, #핵심교양1
                    			    CORE_5 #핵심교양5
                    		      }
                    		    ]
                          }
                      ]
                    }
                    """;
}
