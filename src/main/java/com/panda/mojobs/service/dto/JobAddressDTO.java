package com.panda.mojobs.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the JobAddress entity.
 */
public class JobAddressDTO implements Serializable {

    private Long id;

    private Long addressId;

    private String addressName;

    private Long jobId;

    private String jobName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long mjobId) {
        this.jobId = mjobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String mjobName) {
        this.jobName = mjobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobAddressDTO jobAddressDTO = (JobAddressDTO) o;
        if(jobAddressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobAddressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobAddressDTO{" +
            "id=" + getId() +
            "}";
    }
}
