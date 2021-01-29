<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="beans.*" %>


<%
	request.setCharacterEncoding("UTF-8");
%>

<%
// 	페이지 분할 계산 코드를 작성
	int boardSize = 10;

// 	int p = Integer.parseInt(request.getParameter("p")) or 1;
	int p;
	try{
		p = Integer.parseInt(request.getParameter("p"));
		if(p <= 0) throw new Exception();//강제예외
	}
	catch(Exception e){
		p = 1;
	}
	
// 	p의 값에 따라 시작 row번호와 종료 row번호를 계산
	int endRow = p * boardSize;
	int startRow = endRow - boardSize + 1;
%>


<%
	int orderColumn;
	int orderType;
	try{
		orderColumn = Integer.parseInt(request.getParameter("orderColumn"));
	}
	catch(Exception e){
		orderColumn = 0;
	}
	try{
		orderType= Integer.parseInt(request.getParameter("orderType"));
	}
	catch(Exception e){
		orderType = 0;
	}
	// 	목록,검색을 위해 필요한 프로그래밍 코드
// 	type : 분류 , key : 검색어
	String type = request.getParameter("type");
	String key = request.getParameter("key");
	boolean isSearch = type != null && key != null;

	TipBoardDao dao = new TipBoardDao();
	List<TipBoardOpinionCountVO> list; 
	if(isSearch){
		list = dao.orderedPagingReplyCountList(orderColumn, orderType, type, key, startRow, endRow);  
	}
	else{
		list = dao.orderedPagingReplyCountList(orderColumn, orderType, startRow, endRow); 
	}

	//TipBoardDao dao = new TipBoardDao();
	//List<TipBoardDto> list; 
	//list = dao.select();
%>   

<%
// 	페이지 네비게이터 계산 코드를 작성
	
// 	블록 크기를 설정
	int blockSize = 10;

// 	페이지 번호에 따라 시작블록과 종료블럭을 계산
	int startBlock = (p-1) / blockSize * blockSize + 1;
	int endBlock = startBlock + blockSize - 1;
	
// 	endBlock이 마지막 페이지 번호보다 크면 안된다 = 데이터베이스에서 게시글 수를 구해와야 한다.
// 	int count = 목록개수 or 검색개수;
	int count;
	if(isSearch){
		count = dao.getCount(type, key); 
	}
	else{
		count = dao.getCount();
	}

// 	페이지 개수 = (게시글수 + 9) / 10 = (게시글수 + 페이지크기 - 1) / 페이지크기
	int pageSize = (count + boardSize - 1) / boardSize;
	
	if(endBlock > pageSize){
		endBlock = pageSize;
	}
%>


<style>

	.container-content {
		padding: 10px;
	}
	
	.gray {
		color: #999;
	}

	.border-gray-1 {
		border: 1px solid #999 !important;
  		border-radius: 10px;
	}
	div{ box-sizing: border-box; }

	.container-tip{
		display: grid;
		grid-template-columns: repeat(2,1fr);
		grid-auto-rows: minmax(1em, auto);
		grid-gap: 1rem;
		justify-items: start;
		align-items: start;		
		padding: 10px 0 0 ;
		margin-left: 1.2rem;
	}

	.item{
		padding: 1rem;
		width: 500px;
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
    	font-size: 23px;
    	font-weight: 700;
    	color: #454545;
    	border-bottom: 3px solid #454545;
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
    	border-radius: 10px;
    }
    
    .right-line{
    	float:right; 
    	margin-right: 1rem;
    }
    
    /* 글 제목 글자수 제한 */
    .title-line{
    	float:left; 
    	font-size:1.1em; 
    	font-weight:600;
    	display: inline-block;
    	width:280px;
    	margin-left: 0.5em;
    	white-space: nowrap;
    	overflow: hidden;
    	text-overflow: ellipsis;
    }
    
    .title-line:hover{
    	text-decoration: underline;
    }
    
    /* 글 내용 줄 제한 */
    .content-line{
    	font-size: 0.9em;
    	display: inline-block;
    	width:280px;
    	white-space: nowrap;
    	overflow: hidden;
    	text-overflow: ellipsis;
    	white-space: normal;
    	line-height: 1.5;
    	height: 3em;
    	word-wrap: break-word;
    	display: -webkit-box;
    	-webkit-line-clamp: 2;
    	-webkit-box-orient: vertical;
    }
    
    .content-line:hover{
    	text-decoration: underline;
    }
</style>
<jsp:include page="/template/header.jsp"></jsp:include>

<script>
	function setUrlParam(key, value){
		console.log(key, value);
		var urlParams = new URLSearchParams(window.location.search);
		urlParams.set(key, value);
		window.location.search = urlParams;
	}

	$(function(){
		//.write-btn을 누르면 글쓰기 페이지로 이동
		$(".write-btn").click(function(){
			//상대경로
			//location.href = "write.jsp";
			//$(location).attr("href", "write.jsp");
			
			//절대경로
			//location.href = "http://localhost:8888/home/board/write.jsp";
			//location.href = "/home/board/write.jsp";
			location.href = "<%=request.getContextPath()%>/tip_board/write.jsp";
		});
	});
</script>

<div class="contents left">
	<a href="<%=request.getContextPath()%>">전체</a>
	<span> &gt; </span> 
	<a href="<%=request.getContextPath()%>/tip_board">여행꿀팁</a>
</div>

<div class="bigTitle">여행꿀팁</div>
<div class="btn-box ">
	<a href="#" onclick="setUrlParam('orderColumn', '0'); return false;">최신순</a>
	 | 
	<a href="#" onclick="setUrlParam('orderColumn', '1'); return false;">댓글순</a>
</div>


<div class="container-tip">
  	<%for(TipBoardOpinionCountVO dto : list){ %>
	<div class="item border-gray-1">
  		<a href="<%=request.getContextPath()%>/tip_board/detail.jsp?board_no=<%=dto.getBoard_no()%>">
			<span style="float:left; color:blue;">Tip</span>
			<span class="title-line"><%=dto.getBoard_title() %>
			<%if(dto.getOpinion_count() > 0){ %>
				[<%=dto.getOpinion_count()%>]
			<%}%>
			</span>
			<span class="right-line"><%=dto.getRegist_time() %></span>
			<br><br>
			<span class="content-line" style="margin-left: 2rem"><%=dto.getBoard_content()%></span>
			<br>
			<span style="float:left; font-size:14px; margin-left: 2rem;">일정 <%=dto.getStart_date()%> ~ <%=dto.getEnd_date() %></span>
			<span class="right-line" style="color:#8C8C8C"><%=dto.getMember_nick() %> 여행작가</span>
		</a>
	</div>
	<%} %>
	
</div>

<div class="btn-box row right">
	<button class="write-btn input input-inline">글쓰기</button>
</div>

<div class="btn-box center">
	<div class="row">
		<ul class="pagination">
			<%if(isSearch){ %>
				<li><a href="list.jsp?p=<%=startBlock-1%>&type=<%=type%>&key=<%=key%>">&lt;</a></li>
			<%}else{ %>
				<li><a href="list.jsp?p=<%=startBlock-1%>">&lt;</a></li>
			<%} %>
			
			<%for(int i=startBlock; i<=endBlock; i++){ %>
				<%if(i == p){ %>
				<li class="active">
				<%}else{ %>
				<li>
				<%} %>
				<%if(isSearch){ %>
					<!-- 검색용 링크 -->
					<a href="list.jsp?p=<%=i%>&type=<%=type%>&key=<%=key%>"><%=i%></a>
				<%}else{ %>
					<!-- 목록용 링크 -->
					<a href="list.jsp?p=<%=i%>"><%=i%></a>
				<%} %>
				</li>
			<%} %>
			
			<%if(isSearch){ %>
				<li><a href="list.jsp?p=<%=endBlock+1%>&type=<%=type%>&key=<%=key%>">&gt;</a></li>
			<%}else{ %>
				<li><a href="list.jsp?p=<%=endBlock+1%>">&gt;</a></li>
			<%} %>
		</ul>
	</div>
</div>



			
<jsp:include page="/template/footer.jsp"></jsp:include>		
		