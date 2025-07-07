package graduation_service.graduation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Course {

    @Id @GeneratedValue
    @Column(name = "course_id")
    private Long id;

    // 과목 이름
    private String courseTitle;

    // 학수번호
    private String courseNumber;

    // 학점(ex 3학점, 2학점 ..)
    private int credits;

    // 졸업요건과 course 사이에 있는 중간 테이블에서 전공인지 교양인지 구분하는 필드를 다루는 것이 더 효율적일 것 같아
    // 원래 계획에 있던 필드를 제외하였다.
    //private String courseType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GraduationRequirementsCourses> graduationRequirementsCourses;

    //연관관계 편의 메서드
    public void addGraduationRequirementsCourses(GraduationRequirementsCourses grc) {
        graduationRequirementsCourses.add(grc);
        grc.setCourse(this);
    }

}
