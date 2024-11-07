package com.myaws.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.persistance.BoardMapper;

@Service  // 빼먹지 말자
public class BoardServiceImpl implements BoardService {

	private BoardMapper bm;

	/*
	 * @Autowired SqlSession sqlSession;  // 생성자에서 생성하지 않고 이렇게 생성할 수 있지만, 매번 sqlSession.getMapper(BoardMapper.class);를 써야하므로 중복 발생
	 */
	
	@Autowired  // 스프링아 객체를 찾아줘
	public BoardServiceImpl(SqlSession sqlSession) {
		this.bm = sqlSession.getMapper(BoardMapper.class);  // import를 안하면 BoardMapper.class를 com.myaws.myapp.persistance.BoardMapper.class라고 써야 함
	}
	
	@Override
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();  // HashMap은 ArrayList와 비슷하지만 "이름: 값"의 형식을 가지고 있다. mybatis에서 권장함
		hm.put("startPageNum", (scri.getPage() - 1) * scri.getPerPageNum());
		hm.put("perPageNum", scri.getPerPageNum());
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		
		ArrayList<BoardVo> blist = bm.boardSelectAll(hm);
		
		return blist;
	}

	@Override
	public int boardTotalCount(SearchCriteria scri) {
		
		int cnt = bm.boardTotalCount(scri);
		
		return cnt;
	}

	@Override
	// @Transactional
	public int boardInsert(BoardVo bv) {
		
		int value = bm.boardInsert(bv);
		int maxBidx = bv.getBidx();  // selectKey 결과값
		int value2 = bm.boardOriginbidxUpadte(maxBidx);
		
		return value + value2;
	}

	@Override
	public BoardVo boardSelectOne(int bidx) {
		
		BoardVo bv = bm.boardSelectOne(bidx);
		
		return bv;
	}
	
	@Override
	public int boardViewCntUpdate(BoardVo bv) {
				
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("bidx", bv.getBidx());
		hm.put("viewcntUpdate", bv.getViewcnt() + 1);
		
		int value = bm.boardViewCntUpdate(hm);
		
		return value;
	}
}
