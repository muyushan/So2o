package com.sane.o2o.web.superadmin;

import com.sane.o2o.entity.Area;
import com.sane.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
    private Logger logger= LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private AreaService areaService;
    @RequestMapping("listarea")
    @ResponseBody
    public Map<String,Object> listArea(){
        logger.info("dddd");
        Map<String,Object> modelMap=new HashMap<>();
        List<Area> list=new ArrayList<>();
        list=areaService.queryAreaList();
        modelMap.put("success",true);
        modelMap.put("rows",list);
        modelMap.put("total",list.size());
        return  modelMap;
    }
}
