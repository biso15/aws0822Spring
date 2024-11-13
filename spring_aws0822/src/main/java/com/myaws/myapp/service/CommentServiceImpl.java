package com.myaws.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myaws.myapp.domain.CommentVo;
import com.myaws.myapp.persistance.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentMapper cm;
	
	@Autowired  // Ω∫«¡∏µæ∆ ∞¥√º∏¶ √£æ∆¡‡
	public CommentServiceImpl(SqlSession sqlSession) {
		this.cm = sqlSession.getMapper(CommentMapper.class);
	}
	
	@Override
	public ArrayList<CommentVo> commentSelectAll(int bidx) {

		ArrayList<CommentVo> clist = cm.commentSelectAll(bidx);

		return clist;
	}

//	@Override
//	public int commentInsert(CommentVo cv) {
//			
//		int value = cm.commentInsert(cv);
//		
//		return value;
//	}
	
}
