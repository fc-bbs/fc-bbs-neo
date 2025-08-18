package com.bbs.njtech.user.service;


import com.bbs.njtech.user.repo.CollegeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class CollegeService {

    @Autowired
    private CollegeRepo collegeRepo;




}
