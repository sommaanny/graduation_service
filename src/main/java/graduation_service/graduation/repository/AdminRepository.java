package graduation_service.graduation.repository;

import graduation_service.graduation.domain.entity.Admin;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    public void save(Admin user) {
        em.persist(user);
    }

    public Admin findOne(Long id) {
        return em.find(Admin.class, id);
    }

    public List<Admin> findAll() {
        return em.createQuery("select u from User u", Admin.class).getResultList();
    }

}
