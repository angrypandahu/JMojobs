package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Experience.
 */
@Entity
@Table(name = "experience")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "experience")
public class Experience implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @NotNull
    @Size(max = 255)
    @Column(name = "school", length = 255, nullable = false)
    private String school;

    @NotNull
    @Size(max = 255)
    @Column(name = "grade", length = 255, nullable = false)
    private String grade;

    @NotNull
    @Size(max = 255)
    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "currently_work_here")
    private Boolean currentlyWorkHere;

    @NotNull
    @Size(max = 2000)
    @Column(name = "responsibility", length = 2000, nullable = false)
    private String responsibility;

    @ManyToOne
    private Resume resume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Experience title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public Experience school(String school) {
        this.school = school;
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public Experience grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLocation() {
        return location;
    }

    public Experience location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Experience fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public Experience toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean isCurrentlyWorkHere() {
        return currentlyWorkHere;
    }

    public Experience currentlyWorkHere(Boolean currentlyWorkHere) {
        this.currentlyWorkHere = currentlyWorkHere;
        return this;
    }

    public void setCurrentlyWorkHere(Boolean currentlyWorkHere) {
        this.currentlyWorkHere = currentlyWorkHere;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public Experience responsibility(String responsibility) {
        this.responsibility = responsibility;
        return this;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public Resume getResume() {
        return resume;
    }

    public Experience resume(Resume resume) {
        this.resume = resume;
        return this;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Experience experience = (Experience) o;
        if (experience.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experience.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Experience{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", school='" + getSchool() + "'" +
            ", grade='" + getGrade() + "'" +
            ", location='" + getLocation() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", currentlyWorkHere='" + isCurrentlyWorkHere() + "'" +
            ", responsibility='" + getResponsibility() + "'" +
            "}";
    }
}
