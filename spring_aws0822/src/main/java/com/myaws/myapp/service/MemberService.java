package com.myaws.myapp.service;

import java.util.ArrayList;

import com.myaws.myapp.domain.MemberVo;

// 스프링에서 (MEMBER 기능에서) 사용할 메서드를 선언하는 곳
public interface MemberService {
		
	public int memberInsert(MemberVo mv);

	public int memberIdCheck(String memberId);
	
	public MemberVo memberLoginCheck(String memberId);
	
	public ArrayList<MemberVo> memberSelectAll();
	
}
