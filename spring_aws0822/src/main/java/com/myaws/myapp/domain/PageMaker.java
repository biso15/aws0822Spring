package com.myaws.myapp.domain;

import org.springframework.stereotype.Component;

@Component  // Bean처럼 주입 요청을 함
public class PageMaker {

	private int displayPageNum = 10;  // 페이지 목록번호 리스트  1 2 3 4 5 6 7 8 9 10
	private int startPage;  // 목록의 시작번호를 담는 변수
	private int endPage;  // 목록의 끝번호를 담는 변수
	private int totalCount;  // 총 게시물 수를 담는 변수
	
	private boolean prev;  // 이전버튼
	private boolean next;  // 다음 버튼
	
	private SearchCriteria scri;	

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {  // 총 게시물이 몇개인지 받는 메소드
		this.totalCount = totalCount;
		calcData();  // 페이지 목록 리스트 번호를 나타내주기 위한 계산식
		
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public SearchCriteria getScri() {
		return scri;
	}

	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}
	
	private void calcData() {
		
		// 1. 기본적으로 1에서부터 10까지 나타나게 설정한다  (페이지네비게이션에서)
		endPage = (int)(Math.ceil(scri.getPage() / (double)displayPageNum) * displayPageNum);  // 모두 올림처리 하는 메소드 ceil()
		// Math.ceil(scri.getPage() / (double)displayPageNum -> 현재 페이지를 한번에 보여주는 페이지 수로 나눠서 몇번째 페이지 네비게이션인지 확인한다.
		// Math.ceil(scri.getPage() / (double)displayPageNum) * displayPageNum -> 페이지 네비게이션에 한번에 보여주는 페이지 수를 곱해서 마지막 페이지를 구한다.
		// (int)(Math.ceil(scri.getPage() / (double)displayPageNum) * displayPageNum) -> int형으로 형변환
		
		// 2. endPage가 설정되었으면 시작페이지도 설정
		startPage = (endPage - displayPageNum) + 1;
		// endPage - displayPageNum -> 마지막 페이지에서 한번에 보여주는 페이지 수를 빼서 이전 페이지 네비게이션의 마지막 페이지를 확인한다.
		// (endPage - displayPageNum) + 1 -> 이전 페이지 네비게이션의 마지막 페이지에 1을 더해서 현재 페이지 네비게이션의 처음 페이지를 구한다.
		
		// 3. 실제 게시물수에 따라서 endPage를 구하겠다
		int tempEndPage = (int)(Math.ceil(totalCount / (double)scri.getPerPageNum()));
		// totalCount / (double)scri.getPerPageNum() -> 총 게시물수를 한 페이지에 보이는 게시물 수로 나눠서 전체 페이지수를 구한다.
		// Math.ceil(totalCount / (double)scri.getPerPageNum()) -> 남는 게시물 갯수(나머지)가 있을수 있으니 소수점 올림 한다.
		// (int)(Math.ceil(totalCount / (double)scri.getPerPageNum()))) -> int형으로 형변환

		// 4. 설정한 endPage와 실제 endPage를 비교해서 최종 endPage를 구한다
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		// endPage보다 실제endPage(tempEndPage)가 작으면 endPage를 실제endPage(tempEndPage)로 변경한다.
		
		// 5. 이전, 다음 버튼 만들기
		prev = (startPage == 1? false : true);  // 삼항연산자 사용
		// 시작페이지가 1이면 이전버튼을 없앤다.
		
		next = (endPage * scri.getPerPageNum() >= totalCount ? false : true);
		// 마지막 페이지에 한 페이지에 보이는 게시물 수를 곱한 값이 전체 게시물수보다 크거나 같으면 현재 페이지가 게시판의 마지막 페이지이므로
		// 다음버튼을 없앤다.
	}
	
}
