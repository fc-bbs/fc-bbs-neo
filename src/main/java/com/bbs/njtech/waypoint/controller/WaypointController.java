package com.bbs.njtech.waypoint.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import com.bbs.njtech.common.vo.Result;
import com.bbs.njtech.waypoint.domain.WaypointDomain;
import com.bbs.njtech.waypoint.repo.WaypointRepo;
import com.bbs.njtech.waypoint.vo.WaypointVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@SaIgnore
@RestController
@RequestMapping("/waypoint")
public class WaypointController {
    @Autowired
    private WaypointRepo waypointRepo;

    @SaIgnore
    @GetMapping("/all")
    public Result<WaypointVo> GetAllWaypoints() {
        List<WaypointDomain> waypoints = waypointRepo.findAll();
        return Result.success(
                WaypointVo.withWaypoints(waypoints)
        );
    }
}
