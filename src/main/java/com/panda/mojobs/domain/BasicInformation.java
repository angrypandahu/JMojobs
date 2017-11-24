package com.panda.mojobs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.panda.mojobs.domain.enumeration.Gender;

import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * A BasicInformation.
 */
@Entity
@Table(name = "basic_information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "basicinformation")
public class BasicInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "nationality", length = 255, nullable = false)
    private String nationality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "dateof_brith", nullable = false)
    private LocalDate dateofBrith;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "education_level", nullable = false)
    private EducationLevel educationLevel;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "skype", length = 255)
    private String skype;

    @Size(max = 255)
    @Column(name = "phone", length = 255)
    private String phone;

    @Size(max = 255)
    @Column(name = "wechat", length = 255)
    private String wechat;

    @Size(max = 5000000)
    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToOne(mappedBy = "basicInformation")
    @JsonIgnore
    private Resume resume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public BasicInformation lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public BasicInformation firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNationality() {
        return nationality;
    }

    public BasicInformation nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return gender;
    }

    public BasicInformation gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateofBrith() {
        return dateofBrith;
    }

    public BasicInformation dateofBrith(LocalDate dateofBrith) {
        this.dateofBrith = dateofBrith;
        return this;
    }

    public void setDateofBrith(LocalDate dateofBrith) {
        this.dateofBrith = dateofBrith;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public BasicInformation educationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
        return this;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getEmail() {
        return email;
    }

    public BasicInformation email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public BasicInformation skype(String skype) {
        this.skype = skype;
        return this;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getPhone() {
        return phone;
    }

    public BasicInformation phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public BasicInformation wechat(String wechat) {
        this.wechat = wechat;
        return this;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public BasicInformation photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public BasicInformation photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Resume getResume() {
        return resume;
    }

    public BasicInformation resume(Resume resume) {
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
        BasicInformation basicInformation = (BasicInformation) o;
        if (basicInformation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), basicInformation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BasicInformation{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", gender='" + getGender() + "'" +
            ", dateofBrith='" + getDateofBrith() + "'" +
            ", educationLevel='" + getEducationLevel() + "'" +
            ", email='" + getEmail() + "'" +
            ", skype='" + getSkype() + "'" +
            ", phone='" + getPhone() + "'" +
            ", wechat='" + getWechat() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + photoContentType + "'" +
            "}";
    }
}
