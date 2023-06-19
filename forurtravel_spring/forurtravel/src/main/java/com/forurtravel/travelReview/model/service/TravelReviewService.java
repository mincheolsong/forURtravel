package com.forurtravel.travelReview.model.service;

import java.util.List;

import com.forurtravel.travelReview.model.dto.HotplaceDto;
import com.forurtravel.travelReview.model.dto.TravelReviewDto;
import com.forurtravel.travelReview.model.dto.TravelReviewResultDto;
import com.forurtravel.travelReview.model.dto.TravelReviewWordsDto;

public interface TravelReviewService {

	void write(TravelReviewDto travelReviewDto) throws Exception;

	TravelReviewResultDto detail(int travelReviewNo) throws Exception;

	List<TravelReviewResultDto> listAttraction(int attractionNo) throws Exception;

	List<TravelReviewResultDto> listUser(int userNo) throws Exception ;

	List<HotplaceDto> hotplace() throws Exception ;

	List<TravelReviewResultDto> random() throws Exception ;

	List<TravelReviewWordsDto> wordcloud() throws Exception ;

}
