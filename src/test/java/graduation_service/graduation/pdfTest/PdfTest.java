package graduation_service.graduation.pdfTest;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.service.CourseService;
import graduation_service.graduation.service.GraduationRequirementCoursesService;
import graduation_service.graduation.service.GraduationRequirementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;

@Slf4j
@Transactional
@SpringBootTest
public class PdfTest {

    @Autowired
    GraduationRequirementService grService;

    @Autowired
    GraduationRequirementCoursesService grcService;

    @Autowired
    CourseService courseService;

    @BeforeEach
    void setUp() {
        //과목추가
        Course course1 = new Course("AIE3001", "기계학습", 3);
        Course course2 = new Course("AIE1223", "알고리즘", 3);
        Course course3 = new Course("MTH1001", "일반수학 1", 3);
        Course course4 = new Course("AIE1227", "축제와 인간사회", 3);

        courseService.addCourse(course1);
        courseService.addCourse(course2);
        courseService.addCourse(course3);
        courseService.addCourse(course4);

        //졸업요건 추가
        GraduationRequirements gr = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F, 22);
        Long saveId = grService.addGR(gr, 22);


        //졸업 요건에 과목추가
        grService.addCourseToGraduationRequirement(saveId, 22, course1, CourseType.MAJOR_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course2, CourseType.MAJOR_ELECTIVE);
        grService.addCourseToGraduationRequirement(saveId, 22, course3, CourseType.GENERAL_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course4, CourseType.GENERAL_ELECTIVE);
    }

    @Test
    void pdf추출() {
        try {
            Transcript transcript = new Transcript(); //매칭할 성적표 객체 생성

            // 테스트용 PDF 파일 로드
            File file = new File("src/test/resources/sample-transcript.pdf"); // 테스트 PDF 경로
            FileInputStream fis = new FileInputStream(file); //파일을 byte 단위로 읽을 수 있게

            PDDocument document = PDDocument.load(fis); //pdf파일 읽기
            PDFTextStripper stripper = new PDFTextStripper(); //텍스트 추출 도구

            String text = stripper.getText(document); //텍스트 추출

            mappingTranscriptAndCheckCourses(text, transcript);

            log.info(transcript.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mappingTranscriptAndCheckCourses(String text, Transcript transcript) {
        Set<String> courseNumberSet = new HashSet<>(); //학수번호를 담아두기 위한 set
        Pattern courseNumberPattern = Pattern.compile("[A-Z]{3}[0-9]{4}"); //학수번호 패턴 ex) AIE2004 - 영어3개, 숫자4개

        String[] lines = text.split("\n"); //텍스트를 행 단위로 분리

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = line.trim(); //pdf에서 추출한 text 특성상 양 옆에 공백에 있을 가능성이 큼(있어도 상관은 없지만 일관성을 위해)

            log.info("line" + i + ": " + line);

            // 이수 과목 세부내역 확인
            // 과목 코드 파싱
            Matcher matcher = courseNumberPattern.matcher(line); //매 라인에서 패턴(학수번호)에 매칭되는지 체크해주는 객체
            // find() -> 매칭 되는게 있다면 True
            if (matcher.find()) {
                String courseNumber = matcher.group();
                log.info("학수번호 추출 : " + courseNumber);
                //set에 저장
                courseNumberSet.add(courseNumber);
            }

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
        }

        //졸업요건 과목과 비교
        List<GraduationRequirementsCourses> remainingCourses = checkCourse(courseNumberSet);
        for (GraduationRequirementsCourses remainingCourse : remainingCourses) {
            log.info("졸업 요건에서 남은과목 : " + remainingCourse.getCourse().getCourseTitle());
        }

    }

    //이수과목 세부확인
    private List<GraduationRequirementsCourses> checkCourse(Set<String> courseNumberSet) {
        //학과 졸업요건 과목 불러오기
        List<GraduationRequirementsCourses> allGrc = grcService.findAllGrc(AI_ENGINEERING, 22);

        return allGrc.stream()
                .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber()))
                .toList();
    }

}

