package com.forurtravel.attraction.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forurtravel.attraction.model.dto.AttractionDto;
import com.forurtravel.attraction.model.dto.AttractionSearchDto;
import com.forurtravel.attraction.model.dto.ContentTypeDto;
import com.forurtravel.attraction.model.dto.SidoDto;
import com.forurtravel.attraction.model.mapper.AttractionMapper;

@Service
public class AttractionServiceImpl implements AttractionService {
	private AttractionMapper am;

	@Autowired
	public AttractionServiceImpl(AttractionMapper am) {
		super();
		this.am = am;
	}

	@Override
	public List<AttractionDto> getList(AttractionSearchDto attractionSearchDto) throws Exception {
		if(attractionSearchDto.getWord().equals("*")) {
			attractionSearchDto.setWord("");
		}
		return am.getList(attractionSearchDto);
	}

	@Override
	public List<ContentTypeDto> getContentTypeList() throws Exception {
		return am.getContentTypeList();
	}

	@Override
	public List<SidoDto> getSidoList() throws Exception {
		return am.getSidoList();
	}

	@Override
	public List<SidoDto> getGugunList() throws Exception {
		return am.getGugunList();
	}
	
}
