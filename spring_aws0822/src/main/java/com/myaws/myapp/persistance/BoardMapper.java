package com.myaws.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

// mybatis용 메서드
public interface BoardMapper {
		
	public ArrayList<BoardVo> boardSelectAll(HashMap<String,Object> hm);  // HashMap을 사용하는 Mybatis 등장. 이전까지는 Service와 유사했지만 이 메서드에서는 달라짐
	
	public int boardTotalCount(SearchCriteria scri);
	
	public int boardInsert(BoardVo bv);

	public int boardOriginbidxUpadte(int bidx);

	public BoardVo boardSelectOne(int bidx);
		
	public int boardViewCntUpdate(int bidx);
	
	public int boardRecomUpdate(BoardVo bv);
	
	public int boardDelete(HashMap<String,Object> hm);

	public int boardUpdate(BoardVo bv);

	public int boardReplyUpdate(BoardVo bv);
	
	public int boardReplyInsert(BoardVo bv);
}
