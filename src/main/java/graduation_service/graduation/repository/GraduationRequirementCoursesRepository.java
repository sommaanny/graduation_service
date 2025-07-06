package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GraduationRequirementCoursesRepository {

    private final EntityManager em;

    public void save(GraduationRequirementsCourses graduationCourses) {
        em.persist(graduationCourses);
    }

    public GraduationRequirementsCourses findOne(Long id) {
        return em.find(GraduationRequirementsCourses.class, id);
    }

    public List<GraduationRequirementsCourses> findAll() {
        return em.createQuery("select g from GraduationRequirementsCourses g", GraduationRequirementsCourses.class)
                .getResultList();
    }

    //과목 타입(전공, 교양 ..)으로 조회
    public List<GraduationRequirementsCourses> findByCourseType(CourseType courseType) {
        return em.createQuery("select g from GraduationRequirementsCourses g where g.courseType = :courseType"
                        , GraduationRequirementsCourses.class)
                .setParameter("courseType", courseType)
                .getResultList();
    }


}
