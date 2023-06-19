package com.forurtravel.travelReview.model.dto;

public class TravelReviewWordsDto {
	private String word;
	private int count;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "TravelReviewWordsDto [word=" + word + ", count=" + count + "]";
	}
	
}
