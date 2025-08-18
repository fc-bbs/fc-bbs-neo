package com.bbs.njtech.posting.service;


import com.bbs.njtech.posting.domain.GeneralCategory;
import com.bbs.njtech.posting.repo.GeneralCategoryRepo;
import com.bbs.njtech.posting.vo.GeneralCategoryVO;
import com.bbs.njtech.user.domain.College;
import com.bbs.njtech.user.domain.User;
import com.bbs.njtech.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class GeneralCategoryService {

    @Autowired
    private GeneralCategoryRepo generalCategoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional(readOnly = true)//用于根据collegeId找到所有的GeneralCategory
    public List<GeneralCategoryVO> findGeneralCategory(String userId){
        User user = userRepo.getReferenceById(userId);
        College college= user.getCollege();
        List<GeneralCategory> generalCategoryList = generalCategoryRepo.findByCollegeIdAndDeleteFlagFalse(college.getId());
        return GeneralCategoryVO.convertFor(generalCategoryList);
    }


}
