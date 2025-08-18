package com.bbs.njtech.setting.repo;


import com.bbs.njtech.setting.domain.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemSettingRepo
		extends JpaRepository<SystemSetting, String>, JpaSpecificationExecutor<SystemSetting> {

	SystemSetting findTopByOrderByLatelyUpdateTime();

}
