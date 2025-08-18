package com.bbs.njtech.posting.vo;

import com.bbs.njtech.posting.domain.GeneralCategory;
import com.bbs.njtech.posting.domain.SubCategory;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CategoryVO {

    private Map<String,List<String>> Category = new HashMap<>();

    public static CategoryVO build(List<GeneralCategory> generalCategoryList,List<SubCategory> subCategoryList){
        CategoryVO vo = new CategoryVO();
        Map<String,List<String>> AllCategory = new HashMap<>();
        Map<String,String> Category = new HashMap<>();
        for(GeneralCategory  generalCategory:generalCategoryList ){
            for(SubCategory subCategory:subCategoryList){
                if(subCategory.getGeneralCategoryId().equals(generalCategory.getId())){//找子类的外键和大类的键一一对应
                    if(Category.containsKey(generalCategory.getName())){//已经存过一个进去了
                        Category.put(generalCategory.getName(),subCategory.getName());
                        List<String> subCategoryStr = AllCategory.get(generalCategory.getName());
                        subCategoryStr.add(subCategory.getName());
                        AllCategory.put(generalCategory.getName(),subCategoryStr);
                    }else{
                        Category.put(generalCategory.getName(),subCategory.getName());
                        List<String> subCategoryStr = new ArrayList<>();
                        subCategoryStr.add(subCategory.getName());
                        AllCategory.put(generalCategory.getName(),subCategoryStr);
                    }


                }
            }
        }

        vo.setCategory(AllCategory);
        return vo;
    }

}