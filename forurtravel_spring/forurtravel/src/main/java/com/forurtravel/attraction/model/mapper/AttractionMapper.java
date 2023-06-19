package com.forurtravel.attraction.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.attraction.model.dto.AttractionDto;
import com.forurtravel.attraction.model.dto.AttractionSearchDto;
import com.forurtravel.attraction.model.dto.ContentTypeDto;
import com.forurtravel.attraction.model.dto.SidoDto;

@Mapper
public interface AttractionMapper {

	List<AttractionDto> getList(AttractionSearchDto attractionSearchDto) throws Exception;

	List<ContentTypeDto> getContentTypeList() throws Exception;

	List<SidoDto> getSidoList() throws Exception;

	List<SidoDto> getGugunList() throws Exception;

}
