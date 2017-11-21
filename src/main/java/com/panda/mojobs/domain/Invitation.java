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
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invitation")
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "subject", length = 255, nullable = false)
    private String subject;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Size(max = 2000)
    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @ManyToOne(optional = false)
    @NotNull
    private School school;

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

    public Invitation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public Invitation subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Invitation fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public String getContent() {
        return content;
    }

    public Invitation content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public School getSchool() {
        return school;
    }

    public Invitation school(School school) {
        this.school = school;
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public User getUser() {
        return user;
    }

    public Invitation user(User user) {
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
        Invitation invitation = (Invitation) o;
        if (invitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", subject='" + getSubject() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
