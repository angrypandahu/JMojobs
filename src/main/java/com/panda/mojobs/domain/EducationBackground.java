package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * A EducationBackground.
 */
@Entity
@Table(name = "education_background")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "educationbackground")
public class EducationBackground implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "school", length = 255, nullable = false)
    private String school;

    @NotNull
    @Size(max = 255)
    @Column(name = "major", length = 255, nullable = false)
    private String major;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_degree", nullable = false)
    private EducationLevel degree;

    @NotNull
    @Size(max = 255)
    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @ManyToOne
    private Resume resume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public EducationBackground school(String school) {
        this.school = school;
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public EducationBackground major(String major) {
        this.major = major;
        return this;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public EducationLevel getDegree() {
        return degree;
    }

    public EducationBackground degree(EducationLevel degree) {
        this.degree = degree;
        return this;
    }

    public void setDegree(EducationLevel degree) {
        this.degree = degree;
    }

    public String getLocation() {
        return location;
    }

    public EducationBackground location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public EducationBackground fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public EducationBackground toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Resume getResume() {
        return resume;
    }

    public EducationBackground resume(Resume resume) {
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
        EducationBackground educationBackground = (EducationBackground) o;
        if (educationBackground.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), educationBackground.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EducationBackground{" +
            "id=" + getId() +
            ", school='" + getSchool() + "'" +
            ", major='" + getMajor() + "'" +
            ", degree='" + getDegree() + "'" +
            ", location='" + getLocation() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            "}";
    }
}
