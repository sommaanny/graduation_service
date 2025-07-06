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

    public Optional<Course> findByCourseTitle(String courseTitle) {
        List<Course> result = em.createQuery("select c from Course c where c.courseTitle = :courseTitle"
                        , Course.class)
                .setParameter("courseTitle", courseTitle)
                .getResultList();

        return result.stream().findFirst();
    }

    public Optional<Course> findByCredits(int credits) {
        List<Course> result = em.createQuery("select c from Course c where c.credits = :credits"
                        , Course.class)
                .setParameter("credits", credits)
                .getResultList();

        return result.stream().findFirst();
    }

}
