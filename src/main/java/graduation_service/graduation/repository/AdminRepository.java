package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.Admin;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    public void save(Admin admin) {
        em.persist(admin);
    }

    public Optional<Admin> findByLoginId(String loginId) {
        return em.createQuery("select a from Admin a where a.loginId = :loginId", Admin.class)
                .setParameter("loginId", loginId)
                .getResultList()
                .stream()
                .findFirst();
    }

}
