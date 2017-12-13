package com.panda.mojobs.service.dto;

import java.io.Serializable;
import com.panda.mojobs.domain.enumeration.Language;
import com.panda.mojobs.domain.enumeration.LanguageLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the MLanguage entity. This class is used in MLanguageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /m-languages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MLanguageCriteria implements Serializable {
    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {
    }

    /**
     * Class for filtering LanguageLevel
     */
    public static class LanguageLevelFilter extends Filter<LanguageLevel> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LanguageFilter name;

    private LanguageLevelFilter level;

    private LongFilter resumeId;

    public MLanguageCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LanguageFilter getName() {
        return name;
    }

    public void setName(LanguageFilter name) {
        this.name = name;
    }

    public LanguageLevelFilter getLevel() {
        return level;
    }

    public void setLevel(LanguageLevelFilter level) {
        this.level = level;
    }

    public LongFilter getResumeId() {
        return resumeId;
    }

    public void setResumeId(LongFilter resumeId) {
        this.resumeId = resumeId;
    }

    @Override
    public String toString() {
        return "MLanguageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (resumeId != null ? "resumeId=" + resumeId + ", " : "") +
            "}";
    }

}
