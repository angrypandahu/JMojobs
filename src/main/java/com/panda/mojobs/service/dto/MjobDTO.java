package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.panda.mojobs.domain.enumeration.JobType;

/**
 * A DTO for the Mjob entity.
 */
public class MjobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Lob
    private String jobDescription;

    @NotNull
    private JobType type;

    private Long addressId;

    private String addressName;

    private Long schoolId;

    private String schoolName;

    private Long subTypeId;

    private String subTypeName;

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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Long jobSubTypeId) {
        this.subTypeId = jobSubTypeId;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String jobSubTypeName) {
        this.subTypeName = jobSubTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MjobDTO mjobDTO = (MjobDTO) o;
        if(mjobDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mjobDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MjobDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
