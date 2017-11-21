package com.panda.mojobs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.panda.mojobs.domain.enumeration.Language;

import com.panda.mojobs.domain.enumeration.LanguageLevel;

/**
 * A MLanguage.
 */
@Entity
@Table(name = "m_language")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mlanguage")
public class MLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private Language name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_level", nullable = false)
    private LanguageLevel level;

    @ManyToOne
    private Resume resume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getName() {
        return name;
    }

    public MLanguage name(Language name) {
        this.name = name;
        return this;
    }

    public void setName(Language name) {
        this.name = name;
    }

    public LanguageLevel getLevel() {
        return level;
    }

    public MLanguage level(LanguageLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(LanguageLevel level) {
        this.level = level;
    }

    public Resume getResume() {
        return resume;
    }

    public MLanguage resume(Resume resume) {
        this.resume = resume;
        return this;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
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
        MLanguage mLanguage = (MLanguage) o;
        if (mLanguage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mLanguage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MLanguage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
