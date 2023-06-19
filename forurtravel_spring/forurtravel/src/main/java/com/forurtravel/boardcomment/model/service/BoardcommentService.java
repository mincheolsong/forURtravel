package com.forurtravel.boardcomment.model.service;

import java.util.List;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.boardcomment.model.dto.BoardcommentDto;

public interface BoardcommentService {

	void write(BoardcommentDto boardcommentDto) throws Exception;

	List<BoardDto> getList(int boardNo) throws Exception;

	int getTotalCount(int boardNo) throws Exception;

	void update(BoardcommentDto boardcommentDto) throws Exception;

	void delete(int commentNo) throws Exception;

}
