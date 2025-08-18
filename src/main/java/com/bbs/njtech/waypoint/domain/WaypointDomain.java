package com.bbs.njtech.waypoint.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "waypoint")
public class WaypointDomain {
    @Id
    @Column(name = "id", length = 32, columnDefinition = "varchar(32)")
    private String id;
    @Column(name = "name", length = 32, columnDefinition = "varchar(32)")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "lat", columnDefinition = "double")
    private double lat;
    @Column(name = "lon", columnDefinition = "double")
    private double lon;
    @Column(name = "doc_ur", length = 512, columnDefinition = "varchar(512)")
    private String docUrl;
}
