package com.forurtravel.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;

@Mapper
public interface BoardMapper {

	void write(BoardDto boardDto)throws Exception;

	List<BoardDto> getList(SearchDto searchDto)throws Exception;

	BoardDto getBoard(int boardNo)throws Exception;

	void update(BoardDto boardDto)throws Exception;

	void delete(int boardNo)throws Exception;

	void updateHit(int boardNo)throws Exception;

	int getTotalCount(SearchDto searchDto)throws Exception;
}