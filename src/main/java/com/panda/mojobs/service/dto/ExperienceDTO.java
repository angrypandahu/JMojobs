package com.panda.mojobs.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Experience entity.
 */
public class ExperienceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String school;

    @NotNull
    @Size(max = 255)
    private String grade;

    @NotNull
    @Size(max = 255)
    private String location;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Boolean currentlyWorkHere;

    @NotNull
    @Size(max = 2000)
    private String responsibility;

    private Long resumeId;

    private String resumeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public Boolean isCurrentlyWorkHere() {
        return currentlyWorkHere;
    }

    public void setCurrentlyWorkHere(Boolean currentlyWorkHere) {
        this.currentlyWorkHere = currentlyWorkHere;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
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

        ExperienceDTO experienceDTO = (ExperienceDTO) o;
        if(experienceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experienceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExperienceDTO{" +
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
