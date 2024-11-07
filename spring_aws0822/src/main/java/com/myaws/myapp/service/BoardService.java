package com.myaws.myapp.service;

import java.util.ArrayList;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface BoardService {

	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri);
	
	public int boardTotalCount(SearchCriteria scri);
	
	public int boardInsert(BoardVo bv);

	public BoardVo boardSelectOne(int bidx);
	
	public int boardViewCntUpdate(BoardVo bv);
	
}
