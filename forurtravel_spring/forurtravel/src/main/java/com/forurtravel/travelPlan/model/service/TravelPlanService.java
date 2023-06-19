package com.forurtravel.travelPlan.model.service;

import java.util.List;
import java.util.Map;

import com.forurtravel.travelPlan.model.dto.TravelPlanlistDto;

public interface TravelPlanService {

	void write(Map<String, Object> map) throws Exception;

	TravelPlanlistDto readList(int planlistNo) throws Exception;

	List<TravelPlanlistDto> readListPreview(int planlistNo) throws Exception;
}
