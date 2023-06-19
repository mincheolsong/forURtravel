package com.forurtravel.attraction.model.service;

import java.util.List;

import com.forurtravel.attraction.model.dto.AttractionDto;
import com.forurtravel.attraction.model.dto.AttractionSearchDto;
import com.forurtravel.attraction.model.dto.ContentTypeDto;
import com.forurtravel.attraction.model.dto.SidoDto;

public interface AttractionService {

	List<AttractionDto> getList(AttractionSearchDto attractionSearchDto) throws Exception;

	List<ContentTypeDto> getContentTypeList() throws Exception;

	List<SidoDto> getSidoList() throws Exception;

	List<SidoDto> getGugunList() throws Exception;

}
