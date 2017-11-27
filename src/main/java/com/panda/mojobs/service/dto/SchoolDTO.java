package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.panda.mojobs.domain.enumeration.SchoolLevel;
import com.panda.mojobs.domain.enumeration.SchoolType;

/**
 * A DTO for the School entity.
 */
public class SchoolDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private byte[] logo;
    private String logoContentType;

    @NotNull
    private SchoolLevel level;

    @NotNull
    private SchoolType schoolType;

    private Long addressId;

    private String addressName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public SchoolLevel getLevel() {
        return level;
    }

    public void setLevel(SchoolLevel level) {
        this.level = level;
    }

    public SchoolType getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SchoolDTO schoolDTO = (SchoolDTO) o;
        if(schoolDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schoolDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SchoolDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logo='" + getLogo() + "'" +
            ", level='" + getLevel() + "'" +
            ", schoolType='" + getSchoolType() + "'" +
            "}";
    }
}
