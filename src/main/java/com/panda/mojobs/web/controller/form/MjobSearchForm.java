package com.panda.mojobs.web.controller.form;

import com.panda.mojobs.domain.enumeration.Location;

/**
 * Created by hupanpan on  2017/11/27.
 */
public class MjobSearchForm {
    private String name = "";
    private Location location = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
