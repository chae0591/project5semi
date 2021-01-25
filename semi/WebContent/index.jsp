<%@page import="beans.QnaBoardDto"%>
<%@page import="beans.QnaBoardDao"%>
<%@page import="java.util.List"%>
<%@page import="beans.TipBoardDto"%>
<%@page import="beans.TipBoardDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	request.setCharacterEncoding("UTF-8");

	//여행꿀팁
	TipBoardDao tipboardDao = new TipBoardDao();
	List<TipBoardDto> tipList = tipboardDao.selectMain();
	
	//여행qna
	QnaBoardDao qnaboardDao = new QnaBoardDao();
	List<QnaBoardDto> qnaList = qnaboardDao.selectMain();
%>

<style>
	div{ box-sizing: border-box; }

	.container-tip{
		display: grid;
		grid-template-columns: repeat(2,1fr);
		grid-auto-rows: minmax(1em, auto);
		grid-gap: 1rem;
		justify-items: start;
		align-items: start;		
		padding: 10px 0 0 ;
	}
	
	.container-qna{
		display: grid;
		grid-template-columns: repeat(2,1fr);
		grid-auto-rows: minmax(1em, auto);
		grid-gap: 0.5rem;
		justify-items: start;
		align-items: start;	
		padding: 10px 0 0 ;	
	}	

	.item{
		padding: 1rem;
		width: 440px;
	}
	
	.contents{
		min-height: 50px;
    	margin: 0 auto;
    	padding: 10px 0 0 ;
    	border-bottom: 1px solid #eeeeee;
    }
    
    .contents span{
    	float: left;
    	position: relative;
    	height: 40px;
    	line-height:40px;
    	color : #bbbbbb;
    	font-size: 14px;
    	font-weight: 500;
    }
    
    .contents a{
    	display: block;
    	float: left;
    	position: relative;
    	height: 40px;
    	line-height:40px;
    	color: #666666;
    	font-size: 14px;
    	font-weight: 500;
    }
    
    .bigTitle{
    	position: relative;
    	height: 60px;
    	line-height: 70px;
    	font-size: 25px;
    	font-weight: 700;
    	border-bottom: 3px solid #242424;
    }
    
    .btn-box{
    	margin-top: 15px;
    }
    
    .btn-more{
    	padding: 0 35px;
    	height: 35px;
    	line-height: 35px;
    	border: 1px solid #ddd;
    	border-radius: 2px;
    	font-size: 14px;
    	color: #333;
    }
</style>


<jsp:include page="/template/header.jsp"></jsp:include>


<div class="contents left">
	<a href="<%=request.getContextPath()%>">전체</a>
	<span> &gt; </span> 
	<a href="<%=request.getContextPath()%>">추천콘텐츠</a>
</div>

<div class="bigTitle">여행꿀팁</div>

<div class="container-tip">
  	<%for(TipBoardDto tipboardDto : tipList){ %>
	<div class="item">
  		<a href="<%=request.getContextPath()%>/tip_board/detail.jsp?board_no=<%=tipboardDto.getBoard_no()%>">
			<span style="float:left; color:blue;">Tip&nbsp;</span>
			<span style="float:left; font-size:1.1em; font-weight:600; width:250px;"><%=tipboardDto.getBoard_title() %></span>
			<span style="float:right"><%=tipboardDto.getRegist_time() %></span>
			<br><br>
			<span style="float:left; font-size:14px;">일정 <%=tipboardDto.getStart_date()%> ~ <%=tipboardDto.getEnd_date() %></span>
			<span style="float:right; color:#8C8C8C;"><%=tipboardDto.getBoard_writer() %> 여행작가</span>
		</a>
	</div>
	<%} %>
</div>

<div class="btn-box center">
	<a class="btn-more input" href="<%=request.getContextPath()%>/tip_board/list.jsp">여행꿀팁 더보기&gt;</a>
</div>

<div class="bigTitle">여행Q&amp;A</div>

<div class="container-qna">
  	<%for(QnaBoardDto qnaboardDto : qnaList){ %>
	<div class="item">
		<a href="<%=request.getContextPath()%>/qna_board/detail.jsp?board_no=<%=qnaboardDto.getBoard_no()%>">
			<span style="float:left; color:red;">Q&A&nbsp;</span>
			<span style="float:left; font-size:1.1em; font-weight:600; width:250px;"><%=qnaboardDto.getBoard_title() %></span>
			<span style="float:right"><%=qnaboardDto.getRegist_time() %></span>
			<br><br>
			<span style="color:#8C8C8C"><%=qnaboardDto.getBoard_writer() %> 님의 질문입니다</span>
		</a>
	</div>
	<%} %>
</div>

<div class="btn-box center">
	<a class="btn-more input" href="<%=request.getContextPath()%>/qna_board/list.jsp">여행Q&A 더보기&amp;</a>
</div>

			
<jsp:include page="/template/footer.jsp"></jsp:include>		
		