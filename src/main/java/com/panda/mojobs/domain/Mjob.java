package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.panda.mojobs.domain.enumeration.JobType;

/**
 * A Mjob.
 */
@Entity
@Table(name = "mjob")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mjob")
public class Mjob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Lob
    @Column(name = "job_description", nullable = false)
    private String jobDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private JobType type;

    @ManyToOne(optional = false)
    @NotNull
    private School school;

    @ManyToOne(optional = false)
    @NotNull
    private JobSubType subType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Mjob name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Mjob jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobType getType() {
        return type;
    }

    public Mjob type(JobType type) {
        this.type = type;
        return this;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public School getSchool() {
        return school;
    }

    public Mjob school(School school) {
        this.school = school;
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public JobSubType getSubType() {
        return subType;
    }

    public Mjob subType(JobSubType jobSubType) {
        this.subType = jobSubType;
        return this;
    }

    public void setSubType(JobSubType jobSubType) {
        this.subType = jobSubType;
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
        Mjob mjob = (Mjob) o;
        if (mjob.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mjob.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mjob{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
