package com.sane.o2o.web;

import com.sane.o2o.entity.Area;
import com.sane.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FirstController {
    @Autowired
    private AreaService areaService;
@RequestMapping("/first")
@ResponseBody
    public  Object first(){
    List<Area> areaList=areaService.queryAreaList();
    return  areaList;
    }
}
