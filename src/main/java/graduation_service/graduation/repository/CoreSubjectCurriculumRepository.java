package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.enums.CoreType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoreSubjectCurriculumRepository {

    private final EntityManager em;

    public void save(CoreSubjectCurriculum coreSubjectCurriculum) {
        em.persist(coreSubjectCurriculum);
    }

    public CoreSubjectCurriculum findOne(Long id) {
        return em.find(CoreSubjectCurriculum.class, id);
    }

    //과목으로 조회
    public Optional<CoreSubjectCurriculum> findByCourse(Course course, int curriculumYear) {
        return em.createQuery("select c from CoreSubjectCurriculum c join fetch c.course where c.course = :course " +
                        "and c.curriculumYear = :curriculumYear", CoreSubjectCurriculum.class)
                .setParameter("course", course)
                .setParameter("curriculumYear", curriculumYear)
                .setMaxResults(1) // 안전하게 첫 번째 결과만 가져오기
                .getResultList()
                .stream().findFirst();
    }

    //핵심교양 타입으로 조회
    public List<CoreSubjectCurriculum> findByCoreType(CoreType coreType) {
        return em.createQuery("select c from CoreSubjectCurriculum c join fetch c.course where c.coreType = :coreType", CoreSubjectCurriculum.class)
                .setParameter("coreType", coreType)
                .getResultList();
    }



    //전체 조회
    public List<CoreSubjectCurriculum> findAll() {
        return em.createQuery("select c from CoreSubjectCurriculum c join fetch c.course", CoreSubjectCurriculum.class)
                .getResultList();
    }

    //특정 년도 조회
    public List<CoreSubjectCurriculum> findByYear(int curriculumYear) {
        return em.createQuery("select c from CoreSubjectCurriculum c join fetch c.course where c.curriculumYear = :curriculumYear", CoreSubjectCurriculum.class)
                .setParameter("curriculumYear", curriculumYear)
                .getResultList();
    }



}
