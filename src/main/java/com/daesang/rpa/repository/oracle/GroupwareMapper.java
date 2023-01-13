package com.daesang.rpa.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupwareMapper {

	List<Map<String, Object>> selectMembers(String baseDate);
}