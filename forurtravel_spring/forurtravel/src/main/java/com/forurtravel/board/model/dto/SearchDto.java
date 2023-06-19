package com.forurtravel.board.model.dto;

public class SearchDto {
	private int boardType;
	private int key;
	private String word;
	private int listCount;
	private int curPage;
	public int getBoardType() {
		return boardType;
	}
	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getListCount() {
		return listCount;
	}
	public void setListCount(int listCount) {
		this.listCount = listCount;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	@Override
	public String toString() {
		return "SearchDto [boardType=" + boardType + ", key=" + key + ", word=" + word + ", listCount=" + listCount
				+ ", curPage=" + curPage + "]";
	}
	
	
}
