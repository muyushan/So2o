package com.sane.o2o.dao;

import com.sane.o2o.BaseTest;
import com.sane.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;
    @Test
    public void queryDao() {
        List<Area> areaList=areaDao.queryDao();
        assertEquals(4,areaList.size());
    }
}