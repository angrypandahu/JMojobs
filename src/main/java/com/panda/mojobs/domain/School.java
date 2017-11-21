package com.panda.mojobs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.panda.mojobs.domain.enumeration.SchoolLevel;

import com.panda.mojobs.domain.enumeration.SchoolType;

/**
 * A School.
 */
@Entity
@Table(name = "school")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "school")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_level", nullable = false)
    private SchoolLevel level;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "school_type", nullable = false)
    private SchoolType schoolType;

    @NotNull
    @Size(max = 5000000)
    @Lob
    @Column(name = "logo", nullable = false)
    private byte[] logo;

    @Column(name = "logo_content_type", nullable = false)
    private String logoContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "school")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mjob> jobs = new HashSet<>();

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

    public School name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchoolLevel getLevel() {
        return level;
    }

    public School level(SchoolLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(SchoolLevel level) {
        this.level = level;
    }

    public SchoolType getSchoolType() {
        return schoolType;
    }

    public School schoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
        return this;
    }

    public void setSchoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
    }

    public byte[] getLogo() {
        return logo;
    }

    public School logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public School logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Address getAddress() {
        return address;
    }

    public School address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Mjob> getJobs() {
        return jobs;
    }

    public School jobs(Set<Mjob> mjobs) {
        this.jobs = mjobs;
        return this;
    }

    public School addJobs(Mjob mjob) {
        this.jobs.add(mjob);
        mjob.setSchool(this);
        return this;
    }

    public School removeJobs(Mjob mjob) {
        this.jobs.remove(mjob);
        mjob.setSchool(null);
        return this;
    }

    public void setJobs(Set<Mjob> mjobs) {
        this.jobs = mjobs;
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
        School school = (School) o;
        if (school.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), school.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "School{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level='" + getLevel() + "'" +
            ", schoolType='" + getSchoolType() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + logoContentType + "'" +
            "}";
    }
}
