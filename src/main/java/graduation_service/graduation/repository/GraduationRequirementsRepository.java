package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GraduationRequirementsRepository {

    private final EntityManager em;

    public void save(GraduationRequirements graduationRequirements) {
        em.persist(graduationRequirements);
    }

    public GraduationRequirements findOne(Long id) {
        return em.find(GraduationRequirements.class, id);
    }

    public List<GraduationRequirements> findAll() {
        return em.createQuery("select g from GraduationRequirements g", GraduationRequirements.class)
                .getResultList();
    }

    //학과별 졸업 요건은 하나이나 후에 졸업요건이 바뀌어 추가했을 경우 같은 학과의 졸업 요건이 두 개 이상 나올 수 있는
    // 예외 상황을 고려해 getSingleResult()가 아닌 getResultList()로 받아 첫 번째 값을 가져와 옵셔널로 감싸주어 반환하였다.
    public Optional<GraduationRequirements> findByDepartment(Department department) {
        List<GraduationRequirements> result = em.createQuery("select g from GraduationRequirements g where g.department = :department"
                        , GraduationRequirements.class)
                .setParameter("department", department)
                .getResultList();

        return result.stream().findFirst(); //결과가 없으면 Optional.empty() 반환
    }

    public void delete(GraduationRequirements graduationRequirements) {
        em.remove(graduationRequirements);
    }

}
