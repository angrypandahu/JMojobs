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
 * Criteria class for the JobSubType entity. This class is used in JobSubTypeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /job-sub-types?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobSubTypeCriteria implements Serializable {
    /**
     * Class for filtering JobType
     */
    public static class JobTypeFilter extends Filter<JobType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private JobTypeFilter parent;

    private StringFilter name;

    public JobSubTypeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public JobTypeFilter getParent() {
        return parent;
    }

    public void setParent(JobTypeFilter parent) {
        this.parent = parent;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JobSubTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (parent != null ? "parent=" + parent + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
            "}";
    }

}
