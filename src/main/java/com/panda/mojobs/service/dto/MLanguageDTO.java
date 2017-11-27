package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.panda.mojobs.domain.enumeration.Language;
import com.panda.mojobs.domain.enumeration.LanguageLevel;

/**
 * A DTO for the MLanguage entity.
 */
public class MLanguageDTO implements Serializable {

    private Long id;

    @NotNull
    private Language name;

    @NotNull
    private LanguageLevel level;

    private Long resumeId;

    private String resumeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getName() {
        return name;
    }

    public void setName(Language name) {
        this.name = name;
    }

    public LanguageLevel getLevel() {
        return level;
    }

    public void setLevel(LanguageLevel level) {
        this.level = level;
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

        MLanguageDTO mLanguageDTO = (MLanguageDTO) o;
        if(mLanguageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mLanguageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MLanguageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
