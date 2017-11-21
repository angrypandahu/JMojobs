package com.panda.mojobs.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the MojobImage entity.
 */
public class MojobImageDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String name;

    @NotNull
    @Lob
    private byte[] content;
    private String contentContentType;

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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MojobImageDTO mojobImageDTO = (MojobImageDTO) o;
        if(mojobImageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mojobImageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MojobImageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
