package com.forurtravel.board.model.service;

import java.util.List;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;

public interface BoardService {

	void write(BoardDto boardDto) throws Exception;

	List<BoardDto> getList(SearchDto searchDto) throws Exception;

	BoardDto getBoard(int boardNo) throws Exception;

	void update(BoardDto boardDto) throws Exception;

	void delete(int boardNo) throws Exception;

	void updateHit(int boardNo) throws Exception;

	int getTotalCount(SearchDto searchDto)throws Exception;

}
