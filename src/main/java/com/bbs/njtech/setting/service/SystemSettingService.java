package com.bbs.njtech.setting.service;

import com.bbs.njtech.setting.domain.SystemSetting;
import com.bbs.njtech.setting.repo.SystemSettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SystemSettingService {
    @Autowired
    SystemSettingRepo systemSettingRepo;
    @Transactional(readOnly = true)
    public String findAppContactUsImgUrl(){
        SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
        return systemSetting.getAppContactUsImg();
    }

    @Transactional(readOnly = true)
    public String findAppAvatarUrl(){//applogo
        SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
        return systemSetting.getAppAvatarUrl();
    }

    @Transactional(readOnly = true)
    public String findAppGroupImg(){//社群图片
        SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
        return systemSetting.getAppGroupImg();
    }

    @Transactional(readOnly = true)
    public String findAppName(){//社群图片
        SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
        return systemSetting.getAppName();
    }
    @Transactional(readOnly = true)
    public String findAppOfficialAccountImg(){//公众号图片
        SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
        return systemSetting.getAppOfficialAccountImg();
    }

}
