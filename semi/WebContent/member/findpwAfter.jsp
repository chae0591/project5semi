<%@page import="beans.MemberDto"%>
<%@page import="beans.MemberDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!------ Include the above in your HEAD tag ---------->

<script src="https://cdn.jsdelivr.net/jquery.validation/1.15.1/jquery.validate.min.js"></script>
<link href="https://fonts.googleapis.com/css?family=Kaushan+Script" rel="stylesheet">
<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">


<style>
		body, html{
			width: 100%;
		    height: 100%;
		    margin: 0;
		    padding: 0;
		    display:table;
      
		background:rgba(0, 0, 0, 0. 100);
        }
        body {
		    display:table-cell;
		    vertical-align:middle;
		}
		form {
		    display:table;/* shrinks to fit content */
		    margin:auto;
		}
        a{
         text-decoration:none !important;
         }
         h1,h2,h3{
         font-family: 'Kaushan Script', cursive;
         }
          .myform{
		position: relative;
		display: -ms-flexbox;
		display: flex;
		padding: 1rem;
		-ms-flex-direction: column;
		flex-direction: column;
		width: 100%;
		pointer-events: auto;
		background-color: #fff;
		background-clip: padding-box;
		border: 1px solid #5edfdf;
		border-radius: 1.1rem;
		outline: 0;
		max-width: 500px;
		 }
         .tx-tfm{
         text-transform:uppercase;
         }
         .mybtn{
         border-radius:50px;
         }
        
         .login-or {
         position: relative;
         color: #aaa;
         margin-top: 10px;
         margin-bottom: 10px;
         padding-top: 10px;
         padding-bottom: 10px;
         }
         .span-or {
         display: block;
         position: absolute;
         left: 50%;
         top: -2px;
         margin-left: -25px;
         background-color: #fff;
         width: 50px;
         text-align: center;
         }
         .hr-or {
         height: 1px;
         margin-top: 0px !important;
         margin-bottom: 0px !important;
         }
          form .error {
         color: #ff0000;
         }
		         
		.login_div {
		    position: absolute;
		
		    width: 300px;
		    height: 300px;
		
		    /* Center form on page horizontally & vertically */
		    top: 30%;
		    left: 50%;
		    margin-top: -150px;
		    margin-left: -150px;
		}
		
		.login_form {
		    width: 300px;
		    height: 300px;
		    border-radius: 10px;
		    margin: 0;
		    padding: 0;
		}
</style>


<script language="JavaScript"> 
	
	function Timer() { 
	setTimeout("locateLogin()",5000); 
	 } 
	function locateLogin(){
	 location.replace("../member/login.jsp");
	}
	
	cnt = 5; // 카운트다운 시간 초단위로 표시
	function countdown() {
	 if(cnt == 0){
	        // 시간이 0일경우
	       locateLogin();
	 }else {
	       // 시간이 남았을 경우 카운트다운을 지속한다.
	      document.all.JunDiv.innerHTML = cnt + "초후에 로그인 페이지로 이동";
	      setTimeout("countdown()",1000);
		  cnt--;
	 }
	}
</script> 
 

</head>


<%
	int member_no = (int)session.getAttribute("check");
	String member_pw = (String)session.getAttribute("pw");

	MemberDao dao = new MemberDao();
	MemberDto dto = dao.find(member_no); 
	
	
%>

<body  onLoad="Timer()">

  <div class="login_div">
        <div class="form login_form" >
        <div id="JunDiv" class="text-center"></div>
		
		
			  <div >
			      <div class="myform">
                        <div>
                           <div class="col-md-12 text-center">
                              <h1>Here's Your Password!</h1>
                           </div>
                           
                           
							<div class="form-group"><br>
								<p class="text-center"><a>회원님의 비밀번호는 <%=dto.getMember_pw()%></a> </p>
								<script>countdown();</script>
						        
								
							</div>
							
						
                           
                        </div>
                      
                     </div>
			</div>
		</div>
      </div>   
</body>
</html>