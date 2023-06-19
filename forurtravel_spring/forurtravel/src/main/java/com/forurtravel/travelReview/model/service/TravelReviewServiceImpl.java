package com.forurtravel.travelReview.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forurtravel.travelReview.model.dto.HotplaceDto;
import com.forurtravel.travelReview.model.dto.TravelReviewDto;
import com.forurtravel.travelReview.model.dto.TravelReviewResultDto;
import com.forurtravel.travelReview.model.dto.TravelReviewWordsDto;
import com.forurtravel.travelReview.model.mapper.TravelReviewMapper;

@Service
public class TravelReviewServiceImpl implements TravelReviewService {

	private TravelReviewMapper trm;
	
	public TravelReviewServiceImpl(TravelReviewMapper trm) {
		super();
		this.trm = trm;
	}

	@Override
	public void write(TravelReviewDto travelReviewDto) throws Exception {
		trm.write(travelReviewDto);
	}

	@Override
	public TravelReviewResultDto detail(int travelReviewNo) throws Exception {
		return trm.detail(travelReviewNo);
	}

	@Override
	public List<TravelReviewResultDto> listAttraction(int attractionNo) throws Exception {
		return trm.listAttraction(attractionNo);
	}

	@Override
	public List<TravelReviewResultDto> listUser(int userNo) throws Exception  {
		return trm.listUser(userNo);
	}

	@Override
	public List<HotplaceDto> hotplace() throws Exception {
		return trm.hotplace();
	}

	@Override
	public List<TravelReviewResultDto> random() throws Exception {
		return trm.random();
	}

	@Override
	public List<TravelReviewWordsDto> wordcloud() throws Exception {
		return trm.wordcloud();
	}

}
