package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ApplyJobResume.
 */
@Entity
@Table(name = "apply_job_resume")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "applyjobresume")
public class ApplyJobResume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(optional = false)
    @NotNull
    private Resume resume;

    @ManyToOne(optional = false)
    @NotNull
    private Mjob mjob;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public ApplyJobResume applyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
        return this;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public String getContent() {
        return content;
    }

    public ApplyJobResume content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Resume getResume() {
        return resume;
    }

    public ApplyJobResume resume(Resume resume) {
        this.resume = resume;
        return this;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Mjob getMjob() {
        return mjob;
    }

    public ApplyJobResume mjob(Mjob mjob) {
        this.mjob = mjob;
        return this;
    }

    public void setMjob(Mjob mjob) {
        this.mjob = mjob;
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
        ApplyJobResume applyJobResume = (ApplyJobResume) o;
        if (applyJobResume.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applyJobResume.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplyJobResume{" +
            "id=" + getId() +
            ", applyDate='" + getApplyDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
