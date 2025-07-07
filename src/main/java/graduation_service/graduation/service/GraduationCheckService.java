package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.repository.CourseRepository;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GraduationCheckService {

    private final GraduationRequirementsRepository graduationRequirementsRepository;
    private final CourseRepository courseRepository;
    private final GraduationRequirementCoursesRepository graduationRequirementCoursesRepository;


    //졸업요건 비교
    @Transactional
    public boolean checkGraduation(Long graduationRequirementId) {
        Transcript transcript = inputTranscript(); //성적표 입력
        English english = inputEnglish(); //영어성적 입력

        //비교 로직
        GraduationRequirements findOne = graduationRequirementsRepository.findOne(graduationRequirementId);


        return true;
    }


    //성적표 입력(엑셀)
    public Transcript inputTranscript() {
        return null;
    }

    //영어성적 입력
    public English inputEnglish() {
        English english = new English();
        return null;
    }

}
