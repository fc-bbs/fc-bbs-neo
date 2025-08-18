package com.bbs.njtech.waypoint.repo;

import com.bbs.njtech.waypoint.domain.WaypointDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import reactor.util.annotation.NonNull;

import java.util.List;

public interface WaypointRepo extends JpaRepository<WaypointDomain, String>, JpaSpecificationExecutor<WaypointDomain> {
    @NonNull
    public List<WaypointDomain> findAll();
}
