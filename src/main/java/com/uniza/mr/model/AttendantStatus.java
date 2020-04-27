package com.uniza.mr.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "ATTENDANT_STATUS")
public class AttendantStatus {

    @Id
    @NotNull(message="AttendantStatus id cannot be null.")
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "STATUS")
    @Size(min = 1, max = 20)
    @NotNull (message="AttendantStatus status cannot be null")
    private String status;

    @Column(name = "ICON")
    @NotNull (message="AttendantStatus icon cannot be null")
    @Size(min = 1, max = 20)
    private String icon;

    @Column(name = "DESCRIPTION")
    private String description;

    public AttendantStatus() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
