package com.sane.o2o.service.impl;

import com.sane.o2o.dao.AreaDao;
import com.sane.o2o.entity.Area;
import com.sane.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Override
    @Cacheable(value = {"area1","area2"},key = "#root.targetClass.name")
    public List<Area> queryAreaList() {
        return areaDao.queryDao();
    }
}
