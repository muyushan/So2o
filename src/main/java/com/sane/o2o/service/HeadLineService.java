package com.sane.o2o.service;

import com.sane.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

    public List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
