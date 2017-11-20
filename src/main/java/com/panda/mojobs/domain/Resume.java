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

import com.panda.mojobs.domain.enumeration.CanBeReadBy;

/**
 * A Resume.
 */
@Entity
@Table(name = "resume")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "resume")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "can_be_read_by")
    private CanBeReadBy canBeReadBy;

    @Size(max = 2000)
    @Column(name = "others", length = 2000)
    private String others;

    @OneToOne
    @JoinColumn(unique = true)
    private BasicInformation basicInformation;

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Experience> experiencies = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EducationBackground> educationBackgrounds = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfessionalDevelopment> professionalDevelopments = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MLanguage> languages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

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

    public Resume name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CanBeReadBy getCanBeReadBy() {
        return canBeReadBy;
    }

    public Resume canBeReadBy(CanBeReadBy canBeReadBy) {
        this.canBeReadBy = canBeReadBy;
        return this;
    }

    public void setCanBeReadBy(CanBeReadBy canBeReadBy) {
        this.canBeReadBy = canBeReadBy;
    }

    public String getOthers() {
        return others;
    }

    public Resume others(String others) {
        this.others = others;
        return this;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public BasicInformation getBasicInformation() {
        return basicInformation;
    }

    public Resume basicInformation(BasicInformation basicInformation) {
        this.basicInformation = basicInformation;
        return this;
    }

    public void setBasicInformation(BasicInformation basicInformation) {
        this.basicInformation = basicInformation;
    }

    public Set<Experience> getExperiencies() {
        return experiencies;
    }

    public Resume experiencies(Set<Experience> experiences) {
        this.experiencies = experiences;
        return this;
    }

    public Resume addExperiencies(Experience experience) {
        this.experiencies.add(experience);
        experience.setResume(this);
        return this;
    }

    public Resume removeExperiencies(Experience experience) {
        this.experiencies.remove(experience);
        experience.setResume(null);
        return this;
    }

    public void setExperiencies(Set<Experience> experiences) {
        this.experiencies = experiences;
    }

    public Set<EducationBackground> getEducationBackgrounds() {
        return educationBackgrounds;
    }

    public Resume educationBackgrounds(Set<EducationBackground> educationBackgrounds) {
        this.educationBackgrounds = educationBackgrounds;
        return this;
    }

    public Resume addEducationBackgrounds(EducationBackground educationBackground) {
        this.educationBackgrounds.add(educationBackground);
        educationBackground.setResume(this);
        return this;
    }

    public Resume removeEducationBackgrounds(EducationBackground educationBackground) {
        this.educationBackgrounds.remove(educationBackground);
        educationBackground.setResume(null);
        return this;
    }

    public void setEducationBackgrounds(Set<EducationBackground> educationBackgrounds) {
        this.educationBackgrounds = educationBackgrounds;
    }

    public Set<ProfessionalDevelopment> getProfessionalDevelopments() {
        return professionalDevelopments;
    }

    public Resume professionalDevelopments(Set<ProfessionalDevelopment> professionalDevelopments) {
        this.professionalDevelopments = professionalDevelopments;
        return this;
    }

    public Resume addProfessionalDevelopments(ProfessionalDevelopment professionalDevelopment) {
        this.professionalDevelopments.add(professionalDevelopment);
        professionalDevelopment.setResume(this);
        return this;
    }

    public Resume removeProfessionalDevelopments(ProfessionalDevelopment professionalDevelopment) {
        this.professionalDevelopments.remove(professionalDevelopment);
        professionalDevelopment.setResume(null);
        return this;
    }

    public void setProfessionalDevelopments(Set<ProfessionalDevelopment> professionalDevelopments) {
        this.professionalDevelopments = professionalDevelopments;
    }

    public Set<MLanguage> getLanguages() {
        return languages;
    }

    public Resume languages(Set<MLanguage> mLanguages) {
        this.languages = mLanguages;
        return this;
    }

    public Resume addLanguages(MLanguage mLanguage) {
        this.languages.add(mLanguage);
        mLanguage.setResume(this);
        return this;
    }

    public Resume removeLanguages(MLanguage mLanguage) {
        this.languages.remove(mLanguage);
        mLanguage.setResume(null);
        return this;
    }

    public void setLanguages(Set<MLanguage> mLanguages) {
        this.languages = mLanguages;
    }

    public User getUser() {
        return user;
    }

    public Resume user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        Resume resume = (Resume) o;
        if (resume.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resume.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resume{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", canBeReadBy='" + getCanBeReadBy() + "'" +
            ", others='" + getOthers() + "'" +
            "}";
    }
}
