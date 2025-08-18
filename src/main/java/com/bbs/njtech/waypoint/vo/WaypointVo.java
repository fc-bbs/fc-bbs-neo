package com.bbs.njtech.waypoint.vo;

import com.bbs.njtech.waypoint.domain.WaypointDomain;
import lombok.Data;

import java.util.List;

@Data
public class WaypointVo {
    private List<WaypointDomain> waypoints;
    WaypointVo(List<WaypointDomain> waypoints) {
        this.waypoints = waypoints;
    }
    public static WaypointVo withWaypoints(List<WaypointDomain> waypoints) {
        return new WaypointVo(waypoints);
    }
}
