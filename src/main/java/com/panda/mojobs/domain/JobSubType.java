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
 * A JobSubType.
 */
@Entity
@Table(name = "job_sub_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobsubtype")
public class JobSubType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "parent", nullable = false)
    private JobType parent;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobType getParent() {
        return parent;
    }

    public JobSubType parent(JobType parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(JobType parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public JobSubType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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
        JobSubType jobSubType = (JobSubType) o;
        if (jobSubType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobSubType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobSubType{" +
            "id=" + getId() +
            ", parent='" + getParent() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
