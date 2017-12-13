package com.panda.mojobs.service.dto;

import java.io.Serializable;
import com.panda.mojobs.domain.enumeration.Gender;
import com.panda.mojobs.domain.enumeration.EducationLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the BasicInformation entity. This class is used in BasicInformationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /basic-informations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BasicInformationCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }

    /**
     * Class for filtering EducationLevel
     */
    public static class EducationLevelFilter extends Filter<EducationLevel> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter lastName;

    private StringFilter firstName;

    private StringFilter nationality;

    private GenderFilter gender;

    private LocalDateFilter dateofBrith;

    private EducationLevelFilter educationLevel;

    private StringFilter email;

    private StringFilter skype;

    private StringFilter phone;

    private StringFilter wechat;

    private LongFilter resumeId;

    private LongFilter imageId;

    public BasicInformationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public LocalDateFilter getDateofBrith() {
        return dateofBrith;
    }

    public void setDateofBrith(LocalDateFilter dateofBrith) {
        this.dateofBrith = dateofBrith;
    }

    public EducationLevelFilter getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevelFilter educationLevel) {
        this.educationLevel = educationLevel;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getSkype() {
        return skype;
    }

    public void setSkype(StringFilter skype) {
        this.skype = skype;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getWechat() {
        return wechat;
    }

    public void setWechat(StringFilter wechat) {
        this.wechat = wechat;
    }

    public LongFilter getResumeId() {
        return resumeId;
    }

    public void setResumeId(LongFilter resumeId) {
        this.resumeId = resumeId;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "BasicInformationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (nationality != null ? "nationality=" + nationality + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (dateofBrith != null ? "dateofBrith=" + dateofBrith + ", " : "") +
                (educationLevel != null ? "educationLevel=" + educationLevel + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (skype != null ? "skype=" + skype + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (wechat != null ? "wechat=" + wechat + ", " : "") +
                (resumeId != null ? "resumeId=" + resumeId + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
            "}";
    }

}
