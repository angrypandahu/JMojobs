package com.panda.mojobs.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.panda.mojobs.domain.enumeration.Gender;
import com.panda.mojobs.domain.enumeration.EducationLevel;

/**
 * A DTO for the BasicInformation entity.
 */
public class BasicInformationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String nationality;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate dateofBrith;

    @NotNull
    private EducationLevel educationLevel;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String skype;

    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String wechat;

    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateofBrith() {
        return dateofBrith;
    }

    public void setDateofBrith(LocalDate dateofBrith) {
        this.dateofBrith = dateofBrith;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BasicInformationDTO basicInformationDTO = (BasicInformationDTO) o;
        if(basicInformationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), basicInformationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BasicInformationDTO{" +
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
            "}";
    }
}
