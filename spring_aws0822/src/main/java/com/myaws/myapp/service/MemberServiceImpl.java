package com.myaws.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.persistance.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	private MemberMapper mm;

	@Autowired  // 스프링아 객체를 찾아줘
	public MemberServiceImpl(SqlSession sqlSession) {
		this.mm = sqlSession.getMapper(MemberMapper.class);  // .class 파일이 있는지 확인하므로, 확장자를 붙여야 함
	}
	
	@Override
	public int memberInsert(MemberVo mv) {
		int value = mm.memberInsert(mv);
		return value;
	}

	@Override
	public int memberIdCheck(String memberId) {
		int value = mm.memberIdCheck(memberId);
		return value;
	}
	
	@Override
	public MemberVo memberLoginCheck(String memberId) {
		
		MemberVo mv = mm.memberLoginCheck(memberId);
		return mv;
	}

	@Override
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = mm.memberSelectAll();
		return alist;
	}
}
