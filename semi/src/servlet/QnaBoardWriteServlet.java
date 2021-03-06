package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.*;

@WebServlet(urlPatterns = "/qna_board/write.do")
public class QnaBoardWriteServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			//준비 : board_writer, board_title, board_content
			//2개는 파라미터에서 가져온다(board_title, board_content)
			//1개는 세션의 정보를 이용하여 구한다(member_id -> board_writer)
			req.setCharacterEncoding("UTF-8");
			QnaBoardDto boardDto = new QnaBoardDto();
			boardDto.setBoard_title(req.getParameter("board_title"));
			boardDto.setBoard_content(req.getParameter("board_content"));
			
			//현재 로그인한 사용자 정보를 불러오는 코드
			int member_no = (int)req.getSession().getAttribute("check");
			MemberDao memberDao = new MemberDao();
			MemberDto memberDto = memberDao.find(member_no);
			
			//memberDto의 member_id를 boardDto의 board_writer에 설정
			boardDto.setBoard_writer(memberDto.getMember_id());
		
			//처리 : 게시글 등록(BoardDao를 사용)
			//1. 시퀀스 번호 생성 = .getSequence()
			//2. 등록 = .writeWithPrimaryKey()
			QnaBoardDao qnaboardDao = new QnaBoardDao();
			int board_no = qnaboardDao.getSequence();		//시퀀스 번호 생성
			boardDto.setBoard_no(board_no);					//생성된 번호를 DTO에 설정
			qnaboardDao.writeWithPrimaryKey(boardDto); 		//설정된 정보를 등록
			
			System.out.println(req.getParameter("file_no_list"));
			String s_file_no_list = req.getParameter("file_no_list");
			QnaTmpFileDao qnaTmpFileDao = new QnaTmpFileDao();
			if(s_file_no_list != null && s_file_no_list.length() > 0) {
				String[] file_no_list = s_file_no_list.split(",");
				for (String s : file_no_list) {
					int file_no = Integer.parseInt(s);
					qnaTmpFileDao.updateBoardNo(file_no, board_no);
				}
			}
			
			//출력 : 상세페이지로 이동
			resp.sendRedirect("detail.jsp?board_no="+board_no);
		}
		catch(Exception e){
			e.printStackTrace();
			resp.sendError(500);
		}
	}

}