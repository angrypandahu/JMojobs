package com.panda.mojobs.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ApplyJobResume entity.
 */
public class ApplyJobResumeDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate applyDate;

    @NotNull
    @Lob
    private String content;

    private Long resumeId;

    private String resumeName;

    private Long mjobId;

    private String mjobName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getMjobId() {
        return mjobId;
    }

    public void setMjobId(Long mjobId) {
        this.mjobId = mjobId;
    }

    public String getMjobName() {
        return mjobName;
    }

    public void setMjobName(String mjobName) {
        this.mjobName = mjobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplyJobResumeDTO applyJobResumeDTO = (ApplyJobResumeDTO) o;
        if(applyJobResumeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applyJobResumeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplyJobResumeDTO{" +
            "id=" + getId() +
            ", applyDate='" + getApplyDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
