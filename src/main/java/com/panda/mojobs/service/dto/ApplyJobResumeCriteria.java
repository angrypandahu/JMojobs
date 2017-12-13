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
 * Criteria class for the ApplyJobResume entity. This class is used in ApplyJobResumeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /apply-job-resumes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplyJobResumeCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter applyDate;

    private LongFilter resumeId;

    private LongFilter mjobId;

    public ApplyJobResumeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateFilter applyDate) {
        this.applyDate = applyDate;
    }

    public LongFilter getResumeId() {
        return resumeId;
    }

    public void setResumeId(LongFilter resumeId) {
        this.resumeId = resumeId;
    }

    public LongFilter getMjobId() {
        return mjobId;
    }

    public void setMjobId(LongFilter mjobId) {
        this.mjobId = mjobId;
    }

    @Override
    public String toString() {
        return "ApplyJobResumeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (applyDate != null ? "applyDate=" + applyDate + ", " : "") +
                (resumeId != null ? "resumeId=" + resumeId + ", " : "") +
                (mjobId != null ? "mjobId=" + mjobId + ", " : "") +
            "}";
    }

}
