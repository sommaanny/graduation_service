package graduation_service.graduation.serviceV1;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.requestDto.courseDto.CourseRequest;
import graduation_service.graduation.dto.requestDto.graduationRequirementDto.GraduationRequirementCreateRequest;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static graduation_service.graduation.domain.enums.CoreType.*;
import static graduation_service.graduation.domain.enums.CourseType.MAJOR_REQUIRED;
import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;
import static graduation_service.graduation.domain.enums.Department.COMPUTER_SCIENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class GraduationRequirementServiceV1Test {

    @Autowired
    GraduationRequirementServiceV1 graduationRequirementService;

    @Autowired
    CourseServiceV1 courseServiceV1;

    @Autowired
    EntityManager em;

    private CourseCreateRequest createRequest(String number, String title, int credits) {
        CourseCreateRequest request = new CourseCreateRequest();
        request.setCourseNumber(number);
        request.setCourseTitle(title);
        request.setCredits(credits);
        return request;
    }

    @Test
    void 졸업요건_저장() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        //when
        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        //then
        assertThat(response.getDepartment()).isEqualTo(COMPUTER_SCIENCE);
        assertThat(response.getGraduationRequirementsYear()).isEqualTo(22);
    }

    //졸업요건 중복 저장시 예외 발생해야함
    @Test
    void 졸업요건_중복저장() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        graduationRequirementService.addGR(request);

        //when then
        assertThatThrownBy(() -> graduationRequirementService.addGR(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 학과의 졸업요건입니다");
    }

    @Test
    void 졸업요건_과목_추가() {
        //given
        CourseCreateRequest request = createRequest("AIE-12234", "기계학습", 3);
        CourseResponse courseResponse = courseServiceV1.addCourse(request);

        em.flush();
        em.clear();

        GraduationRequirementCreateRequest grRequest = GraduationRequirementCreateRequest.builder()
                .department(AI_ENGINEERING)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        GraduationRequirementResponse grResponse = graduationRequirementService.addGR(grRequest);

        //when
        CourseRequest courseRequest = new CourseRequest(courseResponse.getId(), MAJOR_REQUIRED);
        graduationRequirementService.addCourseToGraduationRequirement(grResponse.getId(), courseRequest);

        //then
        GraduationRequirementResponse updated = graduationRequirementService.findGR(grResponse.getId());
        assertThat(updated.getDepartment()).isEqualTo(AI_ENGINEERING);
    }

    //id로 조회
    @Test
    void 졸업요건_조회() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        //when
        GraduationRequirementResponse find = graduationRequirementService.findGR(response.getId());

        //then
        assertThat(find.getId()).isEqualTo(response.getId());
    }

    //학과로 조회
    @Test
    void 졸업요건_학과로_조회() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        //when
        GraduationRequirementResponse findGR = graduationRequirementService.findByGRDepartment(COMPUTER_SCIENCE, 22);

        //then
        assertThat(findGR.getId()).isEqualTo(response.getId());
        assertThat(findGR.getDepartment()).isEqualTo(response.getDepartment());
    }

    //전체 조회
    @Test
    void 졸업요건_전체_조회() {
        //given
        GraduationRequirementCreateRequest request1 = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        GraduationRequirementCreateRequest request2 = GraduationRequirementCreateRequest.builder()
                .department(AI_ENGINEERING)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        graduationRequirementService.addGR(request1);
        graduationRequirementService.addGR(request2);

        //when
        List<GraduationRequirementResponse> allGR = graduationRequirementService.findAllGR(22);

        //then
        assertThat(allGR).extracting("department")
                .contains(COMPUTER_SCIENCE, AI_ENGINEERING);
    }

    //삭제
    @Test
    void 졸업요건_삭제() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();


        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        //when
        graduationRequirementService.deleteGR(response.getId());

        //then
        assertThatThrownBy(() -> graduationRequirementService.findGR(response.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("졸업 요건을 찾을 수 없습니다.");
    }

    //삭제 실패
    @Test
    void 졸업요건_삭제_실패() {
        assertThatThrownBy(() -> graduationRequirementService.deleteGR(9999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("삭제할 졸업 요건을 찾을 수 없습니다.");
    }

    //변경
    @Test
    void 졸업요건_변경() {
        //given
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        //when
        GraduationRequirementUpdateDto updateDto = GraduationRequirementUpdateDto.builder()
                .totalCreditsEarned(125)
                .majorCreditsEarned(70)
                .requiredMajorCredits(35)
                .generalEducationCreditsEarned(55)
                .requiredGeneralEducationCredits(30)
                .gpa(3.5F)
                .build();


        graduationRequirementService.updateGR(response.getId(), updateDto);

        //then
        GraduationRequirementResponse updated = graduationRequirementService.findGR(response.getId());

        assertThat(updated.getTotalCredits()).isEqualTo(125);
        assertThat(updated.getMajorCredits()).isEqualTo(70);
        assertThat(updated.getGeneralEducationCredits()).isEqualTo(55);
        assertThat(updated.getGpa()).isEqualTo(3.5F);
    }

    //졸업요건 핵심교양 삭제
    @Test
    void 졸업요건_핵심교양_삭제() {
        //졸업요건 요청
        GraduationRequirementCreateRequest request = GraduationRequirementCreateRequest.builder()
                .department(COMPUTER_SCIENCE)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(30)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.0F)
                .graduationRequirementsYear(22)
                .build();

        //졸업요건 저장
        GraduationRequirementResponse response = graduationRequirementService.addGR(request);

        Long grId = response.getId();

        //졸업요건 핵심교양 추가
        graduationRequirementService.addCoreSubjectTypes(grId, 22, CORE_1);

        GraduationRequirementResponse gr = graduationRequirementService.findGR(grId);

        //졸업요건에 핵심교양 추가 검증
        Assertions.assertThat(gr.getCoreTypes()).contains(CORE_1);

        //핵심교양 삭제
        graduationRequirementService.deleteCoreSubjectTypes(grId, CORE_1);

        //핵심교양 삭제 검증
        Assertions.assertThat(gr.getCoreTypes().size()).isEqualTo(0);
    }

}