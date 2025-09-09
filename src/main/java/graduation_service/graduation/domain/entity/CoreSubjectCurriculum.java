package graduation_service.graduation.domain.entity;

import graduation_service.graduation.domain.enums.CoreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "core_subject_curriculum",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "curriculum_year"})
)
@NoArgsConstructor
@AllArgsConstructor
public class CoreSubjectCurriculum {

    @Id @GeneratedValue
    @Column(name = "core_subject_curriculum_id")
    private Long id;

    private int curriculumYear; //교양 교과과정표 연도

    @Enumerated(EnumType.STRING)
    CoreType coreType; //교양타입 (핵심교양, 창의 ..)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


    public void setCoreType(CoreType coreType) {
        this.coreType = coreType;
    }

    public void setCurriculumYear(int curriculumYear) {
        this.curriculumYear = curriculumYear;
    }

    public void assignCourse(Course course) {
        this.course = course;
        course.getCoreSubjectCurriculumList().add(this);
    }



}
