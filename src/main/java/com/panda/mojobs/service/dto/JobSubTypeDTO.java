package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.panda.mojobs.domain.enumeration.JobType;

/**
 * A DTO for the JobSubType entity.
 */
public class JobSubTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private JobType parent;

    @Size(max = 255)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobType getParent() {
        return parent;
    }

    public void setParent(JobType parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobSubTypeDTO jobSubTypeDTO = (JobSubTypeDTO) o;
        if(jobSubTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobSubTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobSubTypeDTO{" +
            "id=" + getId() +
            ", parent='" + getParent() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
