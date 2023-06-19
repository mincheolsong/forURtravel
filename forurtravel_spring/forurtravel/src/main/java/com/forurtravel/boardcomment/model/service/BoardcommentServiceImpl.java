package com.forurtravel.boardcomment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.boardcomment.model.dto.BoardcommentDto;
import com.forurtravel.boardcomment.model.mapper.BoardcommentMapper;

@Service
public class BoardcommentServiceImpl implements BoardcommentService {
	private BoardcommentMapper bm;

	public BoardcommentServiceImpl(BoardcommentMapper bm) {
		super();
		this.bm = bm;
	}

	@Override
	public void write(BoardcommentDto boardcommentDto) throws Exception {
		bm.write(boardcommentDto);
	}

	@Override
	public List<BoardDto> getList(int boardNo) throws Exception {
		return bm.getList(boardNo);
	}

	@Override
	public int getTotalCount(int boardNo) throws Exception {
		return bm.getTotalCount(boardNo);
	}

	@Override
	public void update(BoardcommentDto boardcommentDto) throws Exception {
		bm.update(boardcommentDto);
	}

	@Override
	public void delete(int commentNo) throws Exception {
		bm.delete(commentNo);
	}
	
}
