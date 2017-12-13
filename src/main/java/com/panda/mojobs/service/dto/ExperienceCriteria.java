package com.panda.mojobs.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Experience entity. This class is used in ExperienceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /experiences?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExperienceCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter school;

    private StringFilter grade;

    private StringFilter location;

    private LocalDateFilter fromDate;

    private LocalDateFilter toDate;

    private BooleanFilter currentlyWorkHere;

    private StringFilter responsibility;

    private LongFilter resumeId;

    public ExperienceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSchool() {
        return school;
    }

    public void setSchool(StringFilter school) {
        this.school = school;
    }

    public StringFilter getGrade() {
        return grade;
    }

    public void setGrade(StringFilter grade) {
        this.grade = grade;
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

    public BooleanFilter getCurrentlyWorkHere() {
        return currentlyWorkHere;
    }

    public void setCurrentlyWorkHere(BooleanFilter currentlyWorkHere) {
        this.currentlyWorkHere = currentlyWorkHere;
    }

    public StringFilter getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(StringFilter responsibility) {
        this.responsibility = responsibility;
    }

    public LongFilter getResumeId() {
        return resumeId;
    }

    public void setResumeId(LongFilter resumeId) {
        this.resumeId = resumeId;
    }

    @Override
    public String toString() {
        return "ExperienceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (school != null ? "school=" + school + ", " : "") +
                (grade != null ? "grade=" + grade + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (fromDate != null ? "fromDate=" + fromDate + ", " : "") +
                (toDate != null ? "toDate=" + toDate + ", " : "") +
                (currentlyWorkHere != null ? "currentlyWorkHere=" + currentlyWorkHere + ", " : "") +
                (responsibility != null ? "responsibility=" + responsibility + ", " : "") +
                (resumeId != null ? "resumeId=" + resumeId + ", " : "") +
            "}";
    }

}
