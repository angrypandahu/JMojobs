package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Image entity.
 */
public class ImageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 5000000)
    @Lob
    private byte[] img;
    private String imgContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgContentType() {
        return imgContentType;
    }

    public void setImgContentType(String imgContentType) {
        this.imgContentType = imgContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageDTO imageDTO = (ImageDTO) o;
        if(imageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), imageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + getId() +
            ", img='" + getImg() + "'" +
            "}";
    }
}
