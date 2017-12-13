package com.panda.mojobs.service.dto;

import java.io.Serializable;
import com.panda.mojobs.domain.enumeration.CanBeReadBy;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Resume entity. This class is used in ResumeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /resumes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ResumeCriteria implements Serializable {
    /**
     * Class for filtering CanBeReadBy
     */
    public static class CanBeReadByFilter extends Filter<CanBeReadBy> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private CanBeReadByFilter canBeReadBy;

    private StringFilter others;

    private LongFilter basicInformationId;

    private LongFilter experienciesId;

    private LongFilter educationBackgroundsId;

    private LongFilter professionalDevelopmentsId;

    private LongFilter languagesId;

    private LongFilter userId;

    public ResumeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public CanBeReadByFilter getCanBeReadBy() {
        return canBeReadBy;
    }

    public void setCanBeReadBy(CanBeReadByFilter canBeReadBy) {
        this.canBeReadBy = canBeReadBy;
    }

    public StringFilter getOthers() {
        return others;
    }

    public void setOthers(StringFilter others) {
        this.others = others;
    }

    public LongFilter getBasicInformationId() {
        return basicInformationId;
    }

    public void setBasicInformationId(LongFilter basicInformationId) {
        this.basicInformationId = basicInformationId;
    }

    public LongFilter getExperienciesId() {
        return experienciesId;
    }

    public void setExperienciesId(LongFilter experienciesId) {
        this.experienciesId = experienciesId;
    }

    public LongFilter getEducationBackgroundsId() {
        return educationBackgroundsId;
    }

    public void setEducationBackgroundsId(LongFilter educationBackgroundsId) {
        this.educationBackgroundsId = educationBackgroundsId;
    }

    public LongFilter getProfessionalDevelopmentsId() {
        return professionalDevelopmentsId;
    }

    public void setProfessionalDevelopmentsId(LongFilter professionalDevelopmentsId) {
        this.professionalDevelopmentsId = professionalDevelopmentsId;
    }

    public LongFilter getLanguagesId() {
        return languagesId;
    }

    public void setLanguagesId(LongFilter languagesId) {
        this.languagesId = languagesId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ResumeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (canBeReadBy != null ? "canBeReadBy=" + canBeReadBy + ", " : "") +
                (others != null ? "others=" + others + ", " : "") +
                (basicInformationId != null ? "basicInformationId=" + basicInformationId + ", " : "") +
                (experienciesId != null ? "experienciesId=" + experienciesId + ", " : "") +
                (educationBackgroundsId != null ? "educationBackgroundsId=" + educationBackgroundsId + ", " : "") +
                (professionalDevelopmentsId != null ? "professionalDevelopmentsId=" + professionalDevelopmentsId + ", " : "") +
                (languagesId != null ? "languagesId=" + languagesId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
