package graduation_service.graduation.serviceV0;

import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static graduation_service.graduation.domain.enums.CourseType.*;
import static graduation_service.graduation.domain.enums.Department.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class GraduationRequirementServiceTest {

    @Autowired GraduationRequirementService graduationRequirementService;

    @Autowired
    CourseService courseService;


    @Test
    void 졸업요건_저장() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();

        //when
        Long saveId = graduationRequirementService.addGR(graduationRequirements, 22);

        //then
        assertThat(saveId).isEqualTo(graduationRequirements.getId());
    }

    //졸업요건 중복 저장시 예외 발생해야함
    @Test
    void 졸업요건_중복저장() {
        //given
        GraduationRequirements graduationRequirements1
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        GraduationRequirements graduationRequirements2
                = new GraduationRequirements(COMPUTER_SCIENCE, 135, 70, 65, 3.0F, 22);


        graduationRequirements1.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements1.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements1.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements1.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements1.validateCreditsConsistency();

        graduationRequirements2.setRequiredMajorCreditsEarned(35); //전필 35학점
        graduationRequirements2.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements2.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements2.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements2.validateCreditsConsistency();

        Long saveId1 = graduationRequirementService.addGR(graduationRequirements1, 22);

        //when, then

        assertThatThrownBy(() -> graduationRequirementService.addGR(graduationRequirements2, 22))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 학과의 졸업요건입니다.");
    }


    @Test
    void 졸업요건_과목_추가() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();

        Long saveId = graduationRequirementService.addGR(graduationRequirements, 22);

        //Course DB에 있는 데이터라고 가정
        Course course = new Course("AIE-12234", "기계학습", 3);
        CourseType courseType = MAJOR_REQUIRED; //전공필수
        courseService.addCourse(course);

        //when
        graduationRequirementService.addCourseToGraduationRequirement(saveId, 22, course, courseType);

        //then
        GraduationRequirements findGR = graduationRequirementService.findGR(saveId);
        List<GraduationRequirementsCourses> graduationRequirementsCourses =
                findGR.getGraduationRequirementsCourses();

        for (GraduationRequirementsCourses graduationRequirementsCourse : graduationRequirementsCourses) {
            log.info("학수번호: " + graduationRequirementsCourse.getCourse().getCourseNumber());
            log.info("과목명: " + graduationRequirementsCourse.getCourse().getCourseTitle());
            log.info("학점: " + graduationRequirementsCourse.getCourse().getCredits());
            log.info("과목타입: " + graduationRequirementsCourse.getCourseType());
        }

        assertThat(graduationRequirementsCourses.size()).isEqualTo(1);

        GraduationRequirementsCourses added = graduationRequirementsCourses.get(0);

        //학수번호 검증
        assertThat(added.getCourse().getCourseNumber())
                .isEqualTo("AIE-12234");

        //과목타입 검증
        assertThat(added.getCourseType())
                .isEqualTo(MAJOR_REQUIRED);

    }

    //id로 조회
    @Test
    void 졸업요건_조회() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();


        Long saveId = graduationRequirementService.addGR(graduationRequirements, 22);

        //when
        GraduationRequirements findGR = graduationRequirementService.findGR(saveId);

        //then
        assertThat(findGR.getId()).isEqualTo(saveId);
        assertThat(findGR.getDepartment()).isEqualTo(graduationRequirements.getDepartment());
    }

    //학과로 조회
    @Test
    void 졸업요건_학과로_조회() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();


        Long saveId = graduationRequirementService.addGR(graduationRequirements, 22);

        //when
        Optional<GraduationRequirements> find = graduationRequirementService.findByGRDepartment(COMPUTER_SCIENCE, 22);
        GraduationRequirements findGR = find.orElseThrow(() -> new IllegalStateException("해당 학과의 졸업요건은 존재하지 않습니다."));

        //then
        assertThat(findGR.getId()).isEqualTo(saveId);
        assertThat(findGR.getDepartment()).isEqualTo(graduationRequirements.getDepartment());
    }

    //전체 조회
    @Test
    void 졸업요건_전체_조회() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);

        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();

        GraduationRequirements graduationRequirements2
                = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F, 22);



        graduationRequirements2.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements2.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements2.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements2.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements2.validateCreditsConsistency();


        graduationRequirementService.addGR(graduationRequirements, 22);
        graduationRequirementService.addGR(graduationRequirements2, 22);

        //when
        List<GraduationRequirements> allGR = graduationRequirementService.findAllGR(22);

        //then
        for (GraduationRequirements requirements : allGR) {
            log.info(requirements.getDepartment() + "의 졸업요건");
        }

        assertThat(allGR).extracting("department")
                .contains(COMPUTER_SCIENCE, AI_ENGINEERING);

    }

    //삭제
    @Test
    void 졸업요건_삭제() {
        //given
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);


        graduationRequirements.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements.validateCreditsConsistency();


        GraduationRequirements graduationRequirements2
                = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F, 22);


        graduationRequirements2.setRequiredMajorCreditsEarned(30); //전필 30학점
        graduationRequirements2.setElectiveMajorCreditsEarned(35); //전선 35학점
        graduationRequirements2.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        graduationRequirements2.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        graduationRequirements2.validateCreditsConsistency();


        Long saveId1 = graduationRequirementService.addGR(graduationRequirements, 22);
        Long saveId2 = graduationRequirementService.addGR(graduationRequirements2, 22);

        //when
        graduationRequirementService.deleteGR(saveId1);

        //then
        List<GraduationRequirements> allGR = graduationRequirementService.findAllGR(22);
        for (GraduationRequirements requirements : allGR) {
            log.info(requirements.getDepartment() + "의 졸업요건");
        }

    }

    //삭제 실패
    @Test
    void 졸업요건_삭제_실패() {
        assertThatThrownBy(() -> graduationRequirementService.deleteGR(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("졸업 요건을 찾을 수 없습니다.");
    }

    //변경
//    @Test
//    void 졸업요건_변경() {
//        //given
//        GraduationRequirements graduationRequirements
//                = new GraduationRequirements(COMPUTER_SCIENCE, 130, 65, 65, 3.0F, 22);
//
//        Long saveId = graduationRequirementService.addGR(graduationRequirements, 22);
//
//        //when
//        GraduationRequirementUpdateDto updateDto = new GraduationRequirementUpdateDto(125, 65, 60, 3.0F);
//
//        graduationRequirementService.updateGR(saveId, 22, updateDto);
//
//        //then
//        GraduationRequirements findGR2 = graduationRequirementService.findGR(saveId, 22);
//        log.info("학과: " + findGR2.getDepartment() + ", 총 학점: " + findGR2.getTotalCreditsEarned() + ", 전공 학점: "
//        + findGR2.getMajorCreditsEarned() + ", 교양 학점: " + findGR2.getGeneralEducationCreditsEarned());
//
//        assertThat(findGR2.getTotalCreditsEarned()).isEqualTo(125);
//        assertThat(findGR2.getGeneralEducationCreditsEarned()).isEqualTo(60);
//    }



}