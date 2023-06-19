package com.forurtravel.travelPlan.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.travelPlan.model.dto.TravelPlanAttractionDto;
import com.forurtravel.travelPlan.model.dto.TravelPlanDto;
import com.forurtravel.travelPlan.model.dto.TravelPlanlistDto;

@Mapper
public interface TravelPlanMapper {

	void write(Map<String, Object> map) throws Exception;

	TravelPlanlistDto readPlanlist(int planlistNo) throws Exception;
	
	List<TravelPlanlistDto> readPlanlists(int userNo) throws Exception;

	List<TravelPlanDto> readPlan(int planlistNo) throws Exception;

	List<TravelPlanAttractionDto> readPlanAttraction(int planNo) throws Exception;
}
