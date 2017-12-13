package com.panda.mojobs.service.dto;

import java.io.Serializable;
import com.panda.mojobs.domain.enumeration.JobType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Mjob entity. This class is used in MjobResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /mjobs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MjobCriteria implements Serializable {
    /**
     * Class for filtering JobType
     */
    public static class JobTypeFilter extends Filter<JobType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private JobTypeFilter type;

    private LongFilter addressId;

    private LongFilter schoolId;

    private LongFilter subTypeId;

    public MjobCriteria() {
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

    public JobTypeFilter getType() {
        return type;
    }

    public void setType(JobTypeFilter type) {
        this.type = type;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(LongFilter schoolId) {
        this.schoolId = schoolId;
    }

    public LongFilter getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(LongFilter subTypeId) {
        this.subTypeId = subTypeId;
    }

    @Override
    public String toString() {
        return "MjobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (schoolId != null ? "schoolId=" + schoolId + ", " : "") +
                (subTypeId != null ? "subTypeId=" + subTypeId + ", " : "") +
            "}";
    }

}
