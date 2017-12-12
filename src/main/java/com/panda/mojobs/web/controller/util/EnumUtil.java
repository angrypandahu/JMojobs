package com.panda.mojobs.web.controller.util;

import com.panda.mojobs.domain.enumeration.*;

import java.util.LinkedHashMap;

/**
 * Created by hupanpan on  2017/11/27.
 */
public class EnumUtil {
    public static LinkedHashMap<Location, String> getLocations() {
        LinkedHashMap<Location, String> locationStringLinkedHashMap = new LinkedHashMap<>();
        locationStringLinkedHashMap.put(Location.PUDONG, "Pudong,Shanghai");
        locationStringLinkedHashMap.put(Location.PUXI, "Puxi,Shanghai");
        locationStringLinkedHashMap.put(Location.JIANGSU, "Jiangsu");
        locationStringLinkedHashMap.put(Location.ZHEJIANG, "Zhejiang");
        return locationStringLinkedHashMap;
    }


    public static LinkedHashMap<SchoolType, String> getSchoolType() {
        LinkedHashMap<SchoolType, String> locationStringLinkedHashMap = new LinkedHashMap<>();
        locationStringLinkedHashMap.put(SchoolType.INTERNATIONAL_BILINGUAL_SCHOOL, "International / Bilingual School");
        locationStringLinkedHashMap.put(SchoolType.LANGUAGE_TRAINING, "Language Training");
        return locationStringLinkedHashMap;
    }

    public static LinkedHashMap<SchoolLevel, String> getSchoolLevel() {
        LinkedHashMap<SchoolLevel, String> locationStringLinkedHashMap = new LinkedHashMap<>();
        locationStringLinkedHashMap.put(SchoolLevel.KINDERGARTEN, "Kindergarten");
        locationStringLinkedHashMap.put(SchoolLevel.ELEMENTARY, "Elementary School");
        locationStringLinkedHashMap.put(SchoolLevel.MIDDLE_SCHOOL, "Middle School");
        locationStringLinkedHashMap.put(SchoolLevel.HIGH_SCHOOL, "High School");
        locationStringLinkedHashMap.put(SchoolLevel.UNIVERSITY, "University");
        return locationStringLinkedHashMap;
    }

    public static LinkedHashMap<Gender, String> getGender() {
        LinkedHashMap<Gender, String> genderStringLinkedHashMap = new LinkedHashMap<>();
        genderStringLinkedHashMap.put(Gender.MALE, "Male");
        genderStringLinkedHashMap.put(Gender.FEMALE, "Female");
        return genderStringLinkedHashMap;

    }

    public static LinkedHashMap<EducationLevel, String> getEducationLevel() {
        LinkedHashMap<EducationLevel, String> educationLevelStringLinkedHashMap = new LinkedHashMap<>();
        educationLevelStringLinkedHashMap.put(EducationLevel.ASSOCIATE, "Associate");
        educationLevelStringLinkedHashMap.put(EducationLevel.BACHELOR, "Bachelor");
        educationLevelStringLinkedHashMap.put(EducationLevel.MASTER, "Master");
        educationLevelStringLinkedHashMap.put(EducationLevel.DOCTORATE, "Doctorate and above");
        return educationLevelStringLinkedHashMap;

    }
}
