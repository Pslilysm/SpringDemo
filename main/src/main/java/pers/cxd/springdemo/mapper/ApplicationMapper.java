package pers.cxd.springdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ApplicationMapper {

    @Update("show tables")
    int ping();

}
