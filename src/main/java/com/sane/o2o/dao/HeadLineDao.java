package com.sane.o2o.dao;

import com.sane.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {

    public List<HeadLine> queryHeadLine(HeadLine headLineCondiction);
}
