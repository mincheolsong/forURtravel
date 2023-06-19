package com.forurtravel.boardcomment.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.boardcomment.model.dto.BoardcommentDto;

@Mapper
public interface BoardcommentMapper {

	void write(BoardcommentDto boardcommentDto) throws Exception;

	List<BoardDto> getList(int boardNo) throws Exception;

	int getTotalCount(int boardNo) throws Exception;

	void update(BoardcommentDto boardcommentDto) throws Exception;

	void delete(int commentNo) throws Exception;

}
