package graduation_service.graduation.service.graduationComparisonService;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import graduation_service.graduation.service.GraduationRequirementCoursesService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;

@Service
@RequiredArgsConstructor
public class GraduationCheckServiceV0 {

    private final GraduationRequirementsRepository graduationRequirementsRepository;
    private final GraduationRequirementCoursesService grcService;


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
    @Transactional
    public Transcript inputTranscript(MultipartFile file, Department department) throws IOException {

        InputStream inputStream = file.getInputStream();
        PDDocument document = PDDocument.load(inputStream); // pdf 파일 읽기

        PDFTextStripper stripper = new PDFTextStripper(); //텍스트 추출 도구
        String text = stripper.getText(document); //텍스트 추출

        document.close(); //리소스 정리

        // 성적표 클래스에 매핑
        Transcript transcript = new Transcript(); //매핑할 성적표 객체 생성
        mappingTranscriptAndCheckCourses(text, transcript, department); // 성적표 매핑 & 이수과목 확인

        return transcript;
    }

    private void mappingTranscriptAndCheckCourses(String text, Transcript transcript ,Department department) {

        HashSet<String> courseNumberSet = new HashSet<>(); // 추출한 학수번호 저장소
        Pattern courseNumberPattern = Pattern.compile("[A-Z]{3}[0-9]{4}");//학수번호 패턴 ex) AIE2004 - 영어3개, 숫자4개

        String[] lines = text.split("\n"); //텍스트를 행 단위로 분리

        for (int i = 0; i < lines.length; i++) {
            //pdf에서 추출한 text 특성상 양 옆에 공백에 있을 가능성이 큼 따라서 trim으로 제거
            String line = lines[i].trim();

            // 이수 과목 세부내용 확인
            // 1.성적표 텍스트에서 학수번호 추출
            Matcher matcher = courseNumberPattern.matcher(line); //매칭을 위한 객체 생성
            if (matcher.find()) {
                String matchingText = matcher.group(); // 매칭부분
                courseNumberSet.add(matchingText); //매칭 부분 set에 저장
            }

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
            }

            //2. 추출한 학수번호를 졸업요건과목들과 비교하여 이수정도 파악
            List<GraduationRequirementsCourses> remainingCourses = checkCourse(courseNumberSet);
            break;
        }
    }

    //이수과목 세부확인
    private List<GraduationRequirementsCourses> checkCourse(Set<String> courseNumberSet) {
        //학과 졸업요건 과목 불러오기
        List<GraduationRequirementsCourses> allGrc = grcService.findAllGrc(AI_ENGINEERING);

        return allGrc.stream()
                .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber()))
                .toList();
    }

    //영어성적 입력
    public English inputEnglish() {
        English english = new English();
        return null;
    }

}
