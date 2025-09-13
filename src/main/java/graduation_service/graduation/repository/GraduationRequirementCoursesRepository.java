package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GraduationRequirementCoursesRepository {

    private final EntityManager em;

    //특정 학과의 졸업요건 과목 전체 조회
    public List<GraduationRequirementsCourses> findAll(Department department, int grYear) {
        return em.createQuery("select g from GraduationRequirementsCourses g join fetch g.course where g.graduationRequirements.department = :department " +
                                "and g.graduationRequirements.graduationRequirementsYear = :grYear"
                        , GraduationRequirementsCourses.class)
                .setParameter("department", department)
                .setParameter("grYear", grYear)
                .getResultList();
    }

    //특정 학과의 졸업요건 과목 중 과목 타입(전공, 교양 ..)으로 조회
    public List<GraduationRequirementsCourses> findByCourseType(Department department, CourseType courseType, int grYear) {
        return em.createQuery("select g from GraduationRequirementsCourses g join fetch g.course where g.graduationRequirements.department = :department" +
                                " and g.courseType = :courseType" +
                                " and g.graduationRequirements.graduationRequirementsYear = :grYear"
                        , GraduationRequirementsCourses.class)
                .setParameter("department", department)
                .setParameter("courseType", courseType)
                .setParameter("grYear", grYear)
                .getResultList();
    }

    //특정 학과의 졸업요건 과목 중 학수번호로 조회

    //특정 학과의 졸업요건 과목 중 과목 이름으로 조회

    //특정 학과 졸업요건 과목 중 학점(2학점, 3학점 ..)으로 조회

    //복합 검색

}
