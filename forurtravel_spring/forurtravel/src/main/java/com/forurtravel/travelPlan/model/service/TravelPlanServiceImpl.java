package com.forurtravel.travelPlan.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.forurtravel.travelPlan.model.dto.TravelPlanDto;
import com.forurtravel.travelPlan.model.dto.TravelPlanlistDto;
import com.forurtravel.travelPlan.model.mapper.TravelPlanMapper;

@Service
public class TravelPlanServiceImpl implements TravelPlanService {
	
	private TravelPlanMapper tpm;
	
	public TravelPlanServiceImpl(TravelPlanMapper tpm) {
		this.tpm = tpm;
	}

	@Override
	public void write(Map<String, Object> map) throws Exception {
		System.out.println(map);
		tpm.write(map);
	}

	@Override
	public TravelPlanlistDto readList(int planlistNo) throws Exception {
		TravelPlanlistDto tpd = tpm.readPlanlist(planlistNo);
		tpd.setPlan(tpm.readPlan(planlistNo));
		for(TravelPlanDto plan : tpd.getPlan()) {
			plan.setPlanAttraction(tpm.readPlanAttraction(plan.getPlanNo()));
		}
		return tpd;
	}

	@Override
	public List<TravelPlanlistDto> readListPreview(int userNo) throws Exception {
		return tpm.readPlanlists(userNo);
	}
}
