package com.panda.mojobs.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * A DTO for the EducationBackground entity.
 */
public class EducationBackgroundDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String school;

    @NotNull
    @Size(max = 255)
    private String major;

    @NotNull
    private EducationLevel degree;

    @NotNull
    @Size(max = 255)
    private String location;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private Long resumeId;

    private String resumeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public EducationLevel getDegree() {
        return degree;
    }

    public void setDegree(EducationLevel degree) {
        this.degree = degree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EducationBackgroundDTO educationBackgroundDTO = (EducationBackgroundDTO) o;
        if(educationBackgroundDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), educationBackgroundDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EducationBackgroundDTO{" +
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
