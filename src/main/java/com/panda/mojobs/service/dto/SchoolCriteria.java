package com.panda.mojobs.service.dto;

import java.io.Serializable;
import com.panda.mojobs.domain.enumeration.SchoolLevel;
import com.panda.mojobs.domain.enumeration.SchoolType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the School entity. This class is used in SchoolResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /schools?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolCriteria implements Serializable {
    /**
     * Class for filtering SchoolLevel
     */
    public static class SchoolLevelFilter extends Filter<SchoolLevel> {
    }

    /**
     * Class for filtering SchoolType
     */
    public static class SchoolTypeFilter extends Filter<SchoolType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private SchoolLevelFilter level;

    private SchoolTypeFilter schoolType;

    private LongFilter addressId;

    private LongFilter imageId;

    private LongFilter jobsId;

    public SchoolCriteria() {
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

    public SchoolLevelFilter getLevel() {
        return level;
    }

    public void setLevel(SchoolLevelFilter level) {
        this.level = level;
    }

    public SchoolTypeFilter getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(SchoolTypeFilter schoolType) {
        this.schoolType = schoolType;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getJobsId() {
        return jobsId;
    }

    public void setJobsId(LongFilter jobsId) {
        this.jobsId = jobsId;
    }

    @Override
    public String toString() {
        return "SchoolCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (schoolType != null ? "schoolType=" + schoolType + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
                (jobsId != null ? "jobsId=" + jobsId + ", " : "") +
            "}";
    }

}
