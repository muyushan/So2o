package com.sane.o2o.service.impl;

import com.sane.o2o.dao.HeadLineDao;
import com.sane.o2o.entity.HeadLine;
import com.sane.o2o.service.HeadLineService;
import com.sane.o2o.web.shopadmin.ProductCategoryManagmentController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class HeadLineServiceImpl implements HeadLineService {
    Logger logger= Logger.getLogger(HeadLineServiceImpl.class);
    @Autowired
    private HeadLineDao headLineDao;
    @Cacheable(value = "headline",key ="#root.methodName")
        @Override
        public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
            return headLineDao.queryHeadLine(headLineCondition);

    }
}
