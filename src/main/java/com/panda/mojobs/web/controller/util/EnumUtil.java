package com.panda.mojobs.web.controller.util;

import com.panda.mojobs.domain.enumeration.Location;
import com.panda.mojobs.domain.enumeration.SchoolLevel;
import com.panda.mojobs.domain.enumeration.SchoolType;

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
}
