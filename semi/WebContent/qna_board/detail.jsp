<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="beans.*" %>
<%@ page import="java.util.*" %>

<!-- 
	상세보기 페이지
	이 페이지는 무조건 "어떤 항목을 볼 것인지에 대한 값"이 존재해야 한다
	일반적으로 primary key가 이 역할을 수행한다.
	
	즉, 이 페이지에서는 시작하자마자 전달되는 데이터를 받아서 데이터베이스에 존재하는 값을 불러온 뒤 출력해야 한다.
	게시판에서는 게시글번호(board_no)가 해당된다. 
-->
<%
	//번호 받기 - QnaBoardDao 연결
	int board_no = Integer.parseInt(request.getParameter("board_no"));

	//단일검색
	QnaBoardDao dao = new QnaBoardDao();
	QnaBoardDto dto = dao.find(board_no);
%>
<jsp:include page="/template/header.jsp"></jsp:include>

<script>

	$(function(){
		//수정 버튼 - edit.jsp로 번호를 첨부하여 전송
		$(".edit-btn").click(function(){
			location.href = "edit.jsp?board_no=<%=board_no%>";
		});
		
		//삭제 버튼 - delete.jsp로 번호를 첨부하여 전송
		$(".delete-btn").click(function(){
			if(confirm("정말 지우시겠습니까?")){
				location.href = "delete.jsp?board_no=<%=board_no%>";
			}
		});
	});


</script>

<div>

작성자 : <%=dto.getBoard_writer()%>
제목 : <%=dto.getBoard_title()%>
<br><br>
내용 : <%=dto.getBoard_content()%>
<br><br>
작성일 : <%=dto.getRegist_time()%>
<br><br>
좋아요 : <%=dto.getVote()%>

</div>

<button class="input edit-btn">수정</button>
<button class="input delete-btn">삭제</button>

<jsp:include page="/template/footer.jsp"></jsp:include>