package com.myaws.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

// mybatis�� �޼���
public interface BoardMapper {
		
	public ArrayList<BoardVo> boardSelectAll(HashMap<String,Object> hm);  // HashMap�� ����ϴ� Mybatis ����. ���������� Service�� ���������� �� �޼��忡���� �޶���
	
	public int boardTotalCount(SearchCriteria scri);
	
	public int boardInsert(BoardVo bv);

	public int boardOriginbidxUpadte(int bidx);

	public BoardVo boardSelectOne(int bidx);
	
	public int boardViewCntUpdate(HashMap<String,Object> hm);
	
}