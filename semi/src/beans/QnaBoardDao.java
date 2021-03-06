package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.JdbcUtil;

public class QnaBoardDao {
	
	public static final String USERNAME = "kh45";
	public static final String PASSWORD = "kh45";
	
	//목록 기능
	public List<QnaBoardDto> select() throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "select * from qna_board order by board_no desc";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		List<QnaBoardDto> list = new ArrayList<>();
		
		while(rs.next()) {
			QnaBoardDto dto = new QnaBoardDto();
			dto.setBoard_no(rs.getInt("board_no"));
			dto.setBoard_writer(rs.getString("board_writer"));
			dto.setBoard_title(rs.getString("board_title"));
			dto.setBoard_content(rs.getString("board_content"));
			dto.setRegist_time(rs.getDate("regist_time"));
			dto.setOpinion(rs.getInt("opinion"));
			list.add(dto);
		}
		con.close();
		
		return list;
	}
	
	/*
	 * //댓글순 목록 기능 public List<QnaBoardDto> select_opinion() throws Exception {
	 * Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
	 * 
	 * String sql = "select* from qna_board order by opinion desc;";
	 * 
	 * PreparedStatement ps = con.prepareStatement(sql); ResultSet rs =
	 * ps.executeQuery();
	 * 
	 * List<QnaBoardDto> list = new ArrayList<>();
	 * 
	 * while(rs.next()) { QnaBoardDto dto = new QnaBoardDto();
	 * dto.setBoard_no(rs.getInt("board_no"));
	 * dto.setBoard_writer(rs.getString("board_writer"));
	 * dto.setBoard_title(rs.getString("board_title"));
	 * dto.setBoard_content(rs.getString("board_content"));
	 * dto.setRegist_time(rs.getDate("regist_time"));
	 * dto.setOpinion(rs.getInt("opinion")); list.add(dto); } con.close();
	 * 
	 * return list; }
	 */
	
	//등록 기능 - QnaBoardWriteServlet
	public void write(QnaBoardDto qnadto) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);

		String sql = "insert into qna_board("
				+ "board_no, board_writer, board_title, board_content, regist_time, vote, opinion"
				+ ") values(qna_board_seq.nextval, ?, ?, ?, sysdate, 0, 0)";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, qnadto.getBoard_writer());
		ps.setString(2, qnadto.getBoard_title());
		ps.setString(3, qnadto.getBoard_content());
		ps.execute();
		
		con.close();
	}
	
	//시퀀스 번호를 미리 생성하는 기능 - .getSequence()
	public int getSequence() throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "select qna_board_seq.nextval from dual";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int seq = rs.getInt(1);
		
		con.close();
		return seq;
	}
	
	//번호까지 함께 등록하는 기능 - .writeWithPrimaryKey()
	public void writeWithPrimaryKey(QnaBoardDto dto) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "insert into qna_board values(?, ?, ?, ?, sysdate, 0, 0)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, dto.getBoard_no());
		ps.setString(2, dto.getBoard_writer());
		ps.setString(3, dto.getBoard_title());
		ps.setString(4, dto.getBoard_content());
		ps.execute();
		
		con.close();
	}
	
	//수정 기능 - QnaBoardEditServlet
	public boolean update(QnaBoardDto dto) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "update qna_board "
				+ "set board_title=?, board_content=?"
				+ "where board_no=?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, dto.getBoard_title());
		ps.setString(2, dto.getBoard_content());
		ps.setInt(3, dto.getBoard_no());
		int count = ps.executeUpdate();
		
		con.close();
		return count > 0;
	}
	
	//삭제 기능
	public boolean delete(int board_no) throws Exception {
		Connection con =  JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "delete qna_board where board_no=?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, board_no);
		int count = ps.executeUpdate();
		
		con.close();
		
		return count > 0;
	}
	
	//상세보기(단일조회)
	public QnaBoardDto find(int board_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
			
		String sql = "select * from qna_board where board_no=?";//결과가 절대로 여러개가 나올 수 없다
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, board_no);
		ResultSet rs = ps.executeQuery();
			
		QnaBoardDto dto;
		if(rs.next()) {//결과가 있다면 객체를 만들어 데이터베이스 값을 전부다 복사하겠다
			dto = new QnaBoardDto();
			dto.setBoard_no(rs.getInt("board_no"));
			dto.setBoard_writer(rs.getString("board_writer"));
			dto.setBoard_title(rs.getString("board_title"));
			dto.setBoard_content(rs.getString("board_content"));
			dto.setRegist_time(rs.getDate("regist_time"));
			dto.setVote(rs.getInt("vote"));
			dto.setOpinion(rs.getInt("opinion"));
		}
		else {//결과가 없다면 잘못된 번호니까 null이라는 값을 반환하겠다
			dto = null;
		}
			
			con.close();
			
			return dto;
		}
	
	//메인 선택글(최신순6개)
	public List<QnaSearchVO> selectMain() throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
			
		String sql = "select * from ("
						+ "select rownum rn, TMP.* from ("
						+ "select * from("
							+ "select Q.board_title, Q.board_content, Q.board_no, Q.regist_time, M.member_nick from "
							+ "qna_board Q inner join member M on M.member_id = Q.board_writer "
							+ "order by board_no desc)"
							+ ")TMP"
							+ ") where rn between 1 and 6";
				
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
			
		List<QnaSearchVO> list = new ArrayList<>();
		while(rs.next()) {
			QnaSearchVO qnasearchVO = new QnaSearchVO();
			qnasearchVO.setBoard_no(rs.getInt("board_no"));
			qnasearchVO.setBoard_title(rs.getString("board_title"));
			qnasearchVO.setBoard_content(rs.getString("board_content"));
			qnasearchVO.setRegist_time(rs.getDate("regist_time"));
			qnasearchVO.setMember_nick(rs.getString("member_nick"));
			list.add(qnasearchVO);
		}
		
		con.close();
		
		return list;
	}
	
	
	//댓글 개수 증가 기능
	public void plusOpinion(int board_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
	 
		String sql = "update qna_board set opinion=opinion+1 where board_no=?";
	
		PreparedStatement ps = con.prepareStatement(sql); ps.setInt(1, board_no);
		ps.execute();
	
		con.close();
	}
	
	//댓글 개수 감소(취소) 기능
	public void minusOpinion(int board_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
	 
		String sql = "update qna_board set opinion=opinion-1 where board_no=?";
	 
		PreparedStatement ps = con.prepareStatement(sql); ps.setInt(1, board_no);
		ps.execute();
	 
		con.close();
	}
	 
		
	//좋아요 증가 기능
	public void plusVote(int board_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "update qna_board set vote=vote+1 where board_no=?";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, board_no);
		ps.execute();
		
		con.close();
	}
	
	//좋아요 감소(취소) 기능
	public void minusVote(int board_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
			
		String sql = "update qna_board set vote=vote-1 where board_no=?";
			
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, board_no);
		ps.execute();
			
		con.close();
	}
	
	//검색 결과 DAO
	public List<QnaSearchVO> select(String keyword) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
		String sql = "select * from ("
						+ "select rownum rn, TMP.* from ("
						+ "select * from("
							+ "select Q.board_title, Q.board_content, Q.board_no, Q.regist_time, M.member_nick from "
							+ "qna_board Q inner join member M "
							+ "on M.member_id = Q.board_writer "
							+ "where instr(board_title, ?) > 0 order by board_no desc)"
						+ ")TMP"
						+ ") where rn between 1 and 6";
					
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, keyword);
		ResultSet rs = ps.executeQuery();
				
		List<QnaSearchVO> list = new ArrayList<>();
		while(rs.next()) {
			QnaSearchVO qnasearchVO = new QnaSearchVO();
			qnasearchVO.setBoard_no(rs.getInt("board_no"));
			qnasearchVO.setBoard_title(rs.getString("board_title"));
			qnasearchVO.setBoard_content(rs.getString("board_content"));
			qnasearchVO.setRegist_time(rs.getDate("regist_time"));
			qnasearchVO.setMember_nick(rs.getString("member_nick"));
			list.add(qnasearchVO);
		}
				
			con.close();
				
			return list;
		}
		
	//페이징을 위한 검색 
		public List<QnaBoardDto> pagingList(String type, String key, int startRow, int endRow) throws Exception {
			Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
			String sql = "select * from(" + 
								"select rownum rn, TMP.* from(" + 
									"select * from Qna_board "
									+ "where instr(#1, ?) > 0 "
									+ "order by board_no desc" + 
								")TMP" + 
							") where rn between ? and ?";
			sql = sql.replace("#1", type);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, key);
			ps.setInt(2, startRow);
			ps.setInt(3, endRow);
			ResultSet rs = ps.executeQuery();
				
			List<QnaBoardDto> list = new ArrayList<>();
			while(rs.next()) {
				QnaBoardDto dto = new QnaBoardDto();
				dto.setBoard_no(rs.getInt("board_no"));
				dto.setBoard_writer(rs.getString("board_writer"));
				dto.setBoard_title(rs.getString("board_title"));
				dto.setBoard_content(rs.getString("board_content"));
				dto.setRegist_time(rs.getDate("regist_time"));
				dto.setVote(rs.getInt("vote"));
				dto.setOpinion(rs.getInt("opinion"));
				list.add(dto);
			}
				
				con.close();
				
				return list;
				
			}
		
		//페이징을 이용한 검색
			public List<QnaSearchVO> searchPagingList(String keyword, int startRow, int endRow) throws Exception{
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
				String sql = "select * from ("
						+ "select rownum rn, TMP.* from ("
						+ "select * from("
							+ "select Q.board_title, Q.board_content, Q.regist_time, Q.board_no, M.member_nick from "
							+ "qna_board Q inner join member M "
							+ "on M.member_id = Q.board_writer "
							+ "where instr(board_title, ?) > 0 order by board_no desc)"
						+ ")TMP"
						+ ") where rn between ? and ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, keyword);
				ps.setInt(2, startRow);
				ps.setInt(3, endRow);
				ResultSet rs = ps.executeQuery();
				
				List<QnaSearchVO> list = new ArrayList<>();
				while(rs.next()) {
					QnaSearchVO qnasearchVO = new QnaSearchVO();
					qnasearchVO.setBoard_no(rs.getInt("board_no"));
					qnasearchVO.setBoard_title(rs.getString("board_title"));
					qnasearchVO.setBoard_content(rs.getString("board_content"));
					qnasearchVO.setRegist_time(rs.getDate("regist_time"));
					qnasearchVO.setMember_nick(rs.getString("member_nick"));
					list.add(qnasearchVO);
				}
					
					con.close();
					
					return list;
			}

		//검색 개수 구하는 메소드
			public int searchCount(String keyword) throws Exception{
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
				String sql = "select count(*) from qna_board where instr(board_title, ?) > 0";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, keyword);
				ResultSet rs = ps.executeQuery();
				
				rs.next();
				int count = rs.getInt(1);
				con.close();
				
				return count;
				
			}
			//페이징을 이용한 목록
			public List<QnaBoardDto> pagingList(int startRow, int endRow) throws Exception {
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
				String sql = 	"select * from(" + 
									"select rownum rn, TMP.* from(" + 
										"select * from qna_board order by board_no desc" + 
									")TMP" + 
								") where rn between ? and ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, startRow);
				ps.setInt(2, endRow);
				ResultSet rs = ps.executeQuery();
				
				List<QnaBoardDto> list = new ArrayList<>();
				while(rs.next()) {
					QnaBoardDto dto = new QnaBoardDto();
					dto.setBoard_no(rs.getInt("board_no"));
					dto.setBoard_writer(rs.getString("board_writer"));
					dto.setBoard_title(rs.getString("board_title"));
					dto.setBoard_content(rs.getString("board_content"));
					dto.setRegist_time(rs.getDate("regist_time"));
					dto.setVote(rs.getInt("vote"));
					dto.setOpinion(rs.getInt("opinion"));
					list.add(dto);

				}
				con.close();
				
				return list; 
			}

			//검색 개수를 구하는 메소드 
			public int getCount(String type, String key) throws Exception {
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				
				String sql = "select count(*) from qna_board where instr(#1,?) > 0";
				sql = sql.replace("#1", type);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, key);
				
				ResultSet rs = ps.executeQuery();
				rs.next();
				int count = rs.getInt(1);
				con.close();
				return count; 
				
			}


			//페이징을 이용한 목록
			public List<QnaBoardDto> pagingListByOpinion(String type, String key, int startRow, int endRow) throws Exception {
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				

				String sql = "select * from ("
								+ "select rownum rn, TMP.* from ("
									+ "select "
										+ "B.board_no, B.board_writer, B.board_title, B.board_content, "
										+ "B.regist_time, B.vote, "
										+ "count(R.opinion_no) opinion_count "
									+ "from "
									+ "qna_board B left outer join qna_opinion R on B.board_no = R.board_no "
									+ "where instr(#1, ?) > 0 "
									+ "group by "
										+ "B.board_no, B.board_writer, B.board_title, B.board_content, "
										+ "B.regist_time, B.vote "
									+ "order by count(R.opinion_no) desc "
								+ ")TMP "
							+ ") where rn between ? and ?";

				sql = sql.replace("#1", type);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, key);
				ps.setInt(2, startRow);
				ps.setInt(3, endRow);
				ResultSet rs = ps.executeQuery();
				
				List<QnaBoardDto> list = new ArrayList<>();
				while(rs.next()) {
					QnaBoardDto dto = new QnaBoardDto();
					dto.setBoard_no(rs.getInt("board_no"));
					dto.setBoard_writer(rs.getString("board_writer"));
					dto.setBoard_title(rs.getString("board_title"));
					dto.setBoard_content(rs.getString("board_content"));
					dto.setRegist_time(rs.getDate("regist_time"));
					dto.setVote(rs.getInt("vote"));
//					dto.setOpinion(rs.getInt("opinion"));
					dto.setOpinion(rs.getInt("opinion_count"));
					list.add(dto);
				}
				con.close();
				
				return list; 
			}

			//페이징을 이용한 목록
			public List<QnaBoardDto> pagingListByOpinion(int startRow, int endRow) throws Exception {
				Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
				

				String sql = "select * from ("
								+ "select rownum rn, TMP.* from ("
									+ "select "
										+ "B.board_no, B.board_writer, B.board_title, B.board_content, "
										+ "B.regist_time, B.vote, "
										+ "count(R.opinion_no) opinion_count "
									+ "from "
									+ "qna_board B left outer join qna_opinion R on B.board_no = R.board_no "
									+ "group by "
										+ "B.board_no, B.board_writer, B.board_title, B.board_content, "
										+ "B.regist_time, B.vote "
									+ "order by count(R.opinion_no) desc "
								+ ")TMP "
							+ ") where rn between ? and ?";

				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, startRow);
				ps.setInt(2, endRow);
				ResultSet rs = ps.executeQuery();
				
				List<QnaBoardDto> list = new ArrayList<>();
				while(rs.next()) {
					QnaBoardDto dto = new QnaBoardDto();
					dto.setBoard_no(rs.getInt("board_no"));
					dto.setBoard_writer(rs.getString("board_writer"));
					dto.setBoard_title(rs.getString("board_title"));
					dto.setBoard_content(rs.getString("board_content"));
					dto.setRegist_time(rs.getDate("regist_time"));
					dto.setVote(rs.getInt("vote"));
//					dto.setOpinion(rs.getInt("opinion"));
					dto.setOpinion(rs.getInt("opinion_count"));
					list.add(dto);
				}
				con.close();
				
				return list;
			}

		//목록개수를 구하는 메소드 
		public int getCount() throws Exception {
			Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
			
			String sql = "select count(*) from qna_board";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			con.close();
			return count; 
		}
	}
