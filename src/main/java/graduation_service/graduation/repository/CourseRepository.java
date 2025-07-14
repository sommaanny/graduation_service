package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.Course;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private final EntityManager em;

    public void save(Course course) {
        em.persist(course);
    }

    public Course findOne(Long id) {
        return em.find(Course.class, id);
    }

    public List<Course> findAll() {
        return em.createQuery("select c from Course c", Course.class).getResultList();
    }

    //과목 이름으로 조회
    public List<Course> findByCourseTitle(String courseTitle) {
        return  em.createQuery("select c from Course c where c.courseTitle = :courseTitle"
                        , Course.class)
                .setParameter("courseTitle", courseTitle)
                .getResultList();
    }

    // 학점으로 조회
    public List<Course> findByCredits(int credits) {
        return em.createQuery("select c from Course c where c.credits = :credits"
                        , Course.class)
                .setParameter("credits", credits)
                .getResultList();
    }


    //학수번호로 조회
    public Optional<Course> findByCourseNumber(String courseNumber) {
        return  em.createQuery("select c from Course c where c.courseNumber = :courseNumber"
                        , Course.class)
                .setParameter("courseNumber", courseNumber)
                .getResultList()
                .stream().findFirst();
    }

    public void delete(Course course) {
        em.remove(course);
    }

}
