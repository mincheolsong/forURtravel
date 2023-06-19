package com.forurtravel.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forurtravel.board.model.dto.BoardDto;
import com.forurtravel.board.model.dto.SearchDto;
import com.forurtravel.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	
	private BoardMapper boardMapper;

	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	@Override
	public void write(BoardDto boardDto) throws Exception {
		boardMapper.write(boardDto);
	}

	@Override
	public List<BoardDto> getList(SearchDto searchDto) throws Exception {
		if(searchDto.getWord().equals("*")) { // 프론트 단에서 검색어를 입력하지 않았을 경우 default 로  '*'를 넘겨줌
			searchDto.setWord("");
		}
		return boardMapper.getList(searchDto);
	}

	@Override
	public BoardDto getBoard(int boardNo) throws Exception {
		return boardMapper.getBoard(boardNo);
	}

	@Override
	public void update(BoardDto boardDto)  throws Exception{
		boardMapper.update(boardDto);
	}

	@Override
	public void delete(int boardNo) throws Exception {
		boardMapper.delete(boardNo);
	}

	@Override
	public void updateHit(int boardNo) throws Exception {
		boardMapper.updateHit(boardNo);
	}

	@Override
	public int getTotalCount(SearchDto searchDto) throws Exception {
		if(searchDto.getWord().equals("*")) {
			searchDto.setWord("");
		}
		return boardMapper.getTotalCount(searchDto);
	}

}
