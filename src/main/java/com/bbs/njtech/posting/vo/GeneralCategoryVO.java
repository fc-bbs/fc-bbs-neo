package com.bbs.njtech.posting.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.bbs.njtech.posting.domain.GeneralCategory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class GeneralCategoryVO {

    private String id;

    private String name;

    public static List<GeneralCategoryVO> convertFor(List<GeneralCategory> pos){
        if (CollectionUtil.isEmpty(pos)){
            return new ArrayList<>();
        }

        List<GeneralCategoryVO> vos = new ArrayList<>();
        for (GeneralCategory po:pos){
            vos.add(convertFor(po));
        }
        return vos;


    }
    public static GeneralCategoryVO convertFor(GeneralCategory po){

        if (po==null){
            return null;
        }
        GeneralCategoryVO vo = new GeneralCategoryVO();
        BeanUtils.copyProperties(po,vo);
        return vo;

    }
}