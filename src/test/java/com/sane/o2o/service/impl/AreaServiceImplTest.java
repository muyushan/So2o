package com.sane.o2o.service.impl;

import com.sane.o2o.BaseTest;
import com.sane.o2o.service.AreaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AreaServiceImplTest extends BaseTest {

    @Autowired
    private AreaService areaService;
    @Test
    public void queryAreaList() {
        assertEquals(4,areaService.queryAreaList().size());
    }
}