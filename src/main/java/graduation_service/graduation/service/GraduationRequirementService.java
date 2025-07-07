package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementService {

    private final GraduationRequirementsRepository graduationRequirementsRepository;

    //저장
    @Transactional
    public void saveGR(GraduationRequirements graduationRequirement) {
        graduationRequirementsRepository.save(graduationRequirement);
    }


    //조회
    public GraduationRequirements findOneGR(Long id) {
        return graduationRequirementsRepository.findOne(id);
    }

    //전체 조회
    public List<GraduationRequirements> findAllGR() {
        return graduationRequirementsRepository.findAll();
    }

    //삭제
    @Transactional
    public void deleteGR(Long id) {
        GraduationRequirements gr = findOneGR(id);
        if (gr == null) {
            throw new IllegalStateException("삭제할 졸업요건이 없습니다");
        }
        graduationRequirementsRepository.delete(gr);
    }

    //변경



}
