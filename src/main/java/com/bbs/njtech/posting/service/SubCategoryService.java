package com.bbs.njtech.posting.service;


import com.bbs.njtech.posting.repo.SubCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepo subCategoryRepo;

//    @Transactional(readOnly = true)//没实现
//    public List<SubCategoryVO> findSubCategory(String userId){
//
//
//
//
//
//    }


}
