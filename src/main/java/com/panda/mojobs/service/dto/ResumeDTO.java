package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.panda.mojobs.domain.enumeration.CanBeReadBy;

/**
 * A DTO for the Resume entity.
 */
public class ResumeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private CanBeReadBy canBeReadBy;

    @Size(max = 2000)
    private String others;

    private Long basicInformationId;

    private String basicInformationFirstName;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CanBeReadBy getCanBeReadBy() {
        return canBeReadBy;
    }

    public void setCanBeReadBy(CanBeReadBy canBeReadBy) {
        this.canBeReadBy = canBeReadBy;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public Long getBasicInformationId() {
        return basicInformationId;
    }

    public void setBasicInformationId(Long basicInformationId) {
        this.basicInformationId = basicInformationId;
    }

    public String getBasicInformationFirstName() {
        return basicInformationFirstName;
    }

    public void setBasicInformationFirstName(String basicInformationFirstName) {
        this.basicInformationFirstName = basicInformationFirstName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResumeDTO resumeDTO = (ResumeDTO) o;
        if(resumeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resumeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResumeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", canBeReadBy='" + getCanBeReadBy() + "'" +
            ", others='" + getOthers() + "'" +
            "}";
    }
}
