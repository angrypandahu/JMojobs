package com.panda.mojobs.service.dto;

import java.io.Serializable;
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
 * Criteria class for the EducationBackground entity. This class is used in EducationBackgroundResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /education-backgrounds?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EducationBackgroundCriteria implements Serializable {
    /**
     * Class for filtering EducationLevel
     */
    public static class EducationLevelFilter extends Filter<EducationLevel> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter school;

    private StringFilter major;

    private EducationLevelFilter degree;

    private StringFilter location;

    private LocalDateFilter fromDate;

    private LocalDateFilter toDate;

    private LongFilter resumeId;

    public EducationBackgroundCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSchool() {
        return school;
    }

    public void setSchool(StringFilter school) {
        this.school = school;
    }

    public StringFilter getMajor() {
        return major;
    }

    public void setMajor(StringFilter major) {
        this.major = major;
    }

    public EducationLevelFilter getDegree() {
        return degree;
    }

    public void setDegree(EducationLevelFilter degree) {
        this.degree = degree;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateFilter toDate) {
        this.toDate = toDate;
    }

    public LongFilter getResumeId() {
        return resumeId;
    }

    public void setResumeId(LongFilter resumeId) {
        this.resumeId = resumeId;
    }

    @Override
    public String toString() {
        return "EducationBackgroundCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (school != null ? "school=" + school + ", " : "") +
                (major != null ? "major=" + major + ", " : "") +
                (degree != null ? "degree=" + degree + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (fromDate != null ? "fromDate=" + fromDate + ", " : "") +
                (toDate != null ? "toDate=" + toDate + ", " : "") +
                (resumeId != null ? "resumeId=" + resumeId + ", " : "") +
            "}";
    }

}
