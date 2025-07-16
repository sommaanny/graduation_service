package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.repository.CourseRepository;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GraduationCheckService {

    private final GraduationRequirementsRepository graduationRequirementsRepository;
    private final CourseRepository courseRepository;
    private final GraduationRequirementCoursesRepository graduationRequirementCoursesRepository;


    //졸업요건 비교
    @Transactional
    public boolean checkGraduation(Department department) {
//        Transcript transcript = inputTranscript(); //성적표 입력
        English english = inputEnglish(); //영어성적 입력

        //비교 로직
        Optional<GraduationRequirements> findGR = graduationRequirementsRepository.findByDepartment(department);



        return true;
    }


    //성적표 입력(pdf)
    public Transcript inputTranscript(MultipartFile file) throws IOException {

        InputStream inputStream = file.getInputStream();
        PDDocument document = PDDocument.load(inputStream); // pdf 파일 읽기

        PDFTextStripper stripper = new PDFTextStripper(); //텍스트 추출 도구
        String text = stripper.getText(document); //텍스트 추출

        document.close(); //리소스 정리

        // 성적표 클래스에 매핑
        Transcript transcript = new Transcript(); //매핑할 성적표 객체 생성
        String[] lines = text.split("\n"); //텍스트를 행 단위로 분리

        for (int i = 0; i < lines.length; i++) {
            //pdf에서 추출한 text 특성상 양 옆에 공백에 있을 가능성이 큼 따라서 trim으로 제거
            String line = lines[i].trim();

            //P(신청학점과 취득학점에는 포함되나, 평점평균 산출시 제외됨) 뒤에 순서대로 이수학점이 나열됨
            if (line.matches("^P\\(.*\\)$")) {
                for (int j = i + 1; j < i + 24; j++) {
                    String credit = lines[j].trim();
                    if (j == i + 1) {
                        //1. 필수교양
                        int input = Integer.parseInt(credit);
                        transcript.setRequiredGeneralEducationCredits(input);
                    } else if (j == i + 2) {
                        //2. 교양선택
                        int input = Integer.parseInt(credit);
                        transcript.setElectiveGeneralEducationCredits(input);
                    } else if (j == i + 3) {
                        //3. 기초전공
                        int input = Integer.parseInt(credit);
                        transcript.setBasicMajorCredits(input);
                    } else if (j == i + 4) {
                        //4. 전공필수
                        int input = Integer.parseInt(credit);
                        transcript.setRequiredMajorCredits(input);
                    } else if (j == i + 5) {
                        //5. 전공선택
                        int input = Integer.parseInt(credit);
                        transcript.setElectiveMajorCredits(input);
                    } else if (j == i + 17) {
                        //17. 기타학점
                        int input = Integer.parseInt(credit);
                        transcript.setOtherEarnedCredits(input);
                    } else if (j == i + 19) {
                        //19. 총 취득학점
                        int input = Integer.parseInt(credit);
                        transcript.setTotalCredits(input);
                    } else if (j == i + 22) {
                        //22. 종합 성적
                        float input = Float.parseFloat(credit);
                        transcript.setGpa(input);
                    } else if (j == i + 24) {
                        //24. 편입인정학점(전공/전체)
                        String[] result = credit.split("/");
                        int major = Integer.parseInt(result[0]); //전공
                        int total = Integer.parseInt(result[1]); //전체
                        transcript.setTransferredMajorCredits(major);
                        transcript.setTotalTransferredCredits(total);
                    }
                }
                break;
            }
        }

        // 이수 과목 세부내용 확인

        return transcript;
    }

    //영어성적 입력
    public English inputEnglish() {
        English english = new English();
        return null;
    }

}
