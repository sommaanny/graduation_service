package graduation_service.graduation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Course {

    @Id @GeneratedValue
    @Column(name = "course_id")
    private Long id;

    public Course() {
    }

    public Course(String courseNumber, String courseTitle, int credits) {
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        this.credits = credits;
    }

    // 과목 이름
    private String courseTitle;

    // 학수번호
    // 학수번호는 중복되면 안되기에 유일성 제약 추가
    @Column(unique = true)
    private String courseNumber;

    // 학점(ex 3학점, 2학점 ..)
    private int credits;

    // 졸업요건과 course 사이에 있는 중간 테이블에서 전공인지 교양인지 구분하는 필드를 다루는 것이 더 효율적일 것 같아
    // 원래 계획에 있던 필드를 제외하였다.
    //private String courseType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GraduationRequirementsCourses> graduationRequirementsCourses = new ArrayList<>();

}
