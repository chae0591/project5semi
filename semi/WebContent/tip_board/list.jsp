<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="beans.*" %>

<%

	TipBoardDao dao = new TipBoardDao();
	List<TipBoardDto> list; 
	list = dao.select();

%>   

<jsp:include page="/template/header.jsp"></jsp:include>
		
<div class="outbox" style="width:640px">
	<div class="row center">
		<h2>환영합니다!</h2>
		<%=list.size() %>
		<%for(TipBoardDto dto : list){ %>
			<%=dto.getBoard_no() %>
			<%=dto.getBoard_title() %>
		<%} %>
	</div>
</div>
			
<jsp:include page="/template/footer.jsp"></jsp:include>		
		