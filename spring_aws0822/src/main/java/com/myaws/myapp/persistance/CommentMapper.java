package com.myaws.myapp.persistance;

import java.util.ArrayList;

import com.myaws.myapp.domain.CommentVo;

public interface CommentMapper {

	public ArrayList<CommentVo> commentSelectAll(int bidx);
	
	public int commentInsert(CommentVo cv);

	public int commentDelete(CommentVo cv);
	
}
