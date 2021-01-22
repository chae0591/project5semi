package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.JdbcUtil;

public class TmpFileDao {
	
	public static final String USERNAME = "project5";
	public static final String PASSWORD = "project5";

//	시퀀스 번호를 미리 생성하는 기능
	public int getSequence() throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "select tmp_file_seq.nextval from dual";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();//무조건 나옴
		int seq = rs.getInt(1);
//		int seq = rs.getInt("NEXTVAL");
		
		con.close();
		return seq;
	}
	
//	번호까지 함께 등록하는 기능
	public void writeWithPrimaryKey(TmpFileDto tmpFileDto) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "insert into tmp_file values(?, ?, ?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, tmpFileDto.getFile_no());
		ps.setString(2, tmpFileDto.getUpload_name());
		ps.setString(3, tmpFileDto.getSave_name());
		ps.setLong(4, tmpFileDto.getFile_size());
		ps.setString(5, tmpFileDto.getFile_type());
		ps.execute();
		
		con.close();
		
	}
	
//	등록 기능
	public void insert(TmpFileDto tmpFileDto) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "insert into tmp_file values(tmp_file_seq.nextval, ?, ?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, tmpFileDto.getUpload_name());
		ps.setString(2, tmpFileDto.getSave_name());
		ps.setLong(3, tmpFileDto.getFile_size());
		ps.setString(4, tmpFileDto.getFile_type());
		ps.execute();
		
		con.close();
	}
	
//	단일 조회 기능
	public TmpFileDto find(int file_no) throws Exception {
		Connection con = JdbcUtil.getConnection(USERNAME, PASSWORD);
		
		String sql = "select * from tmp_file where file_no = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, file_no);
		ResultSet rs = ps.executeQuery();//데이터 개수 : 없거나 한 개 있거나
		
		TmpFileDto tmpFileDto;
		if(rs.next()) {
			tmpFileDto = new TmpFileDto();
			tmpFileDto.setFile_no(rs.getInt("file_no"));
			tmpFileDto.setUpload_name(rs.getString("upload_name"));
			tmpFileDto.setSave_name(rs.getString("save_name"));
			tmpFileDto.setFile_size(rs.getLong("file_size"));
			tmpFileDto.setFile_type(rs.getString("file_type"));
		}
		else {
			tmpFileDto = null;
		}
		con.close();
		return tmpFileDto;
	}
	
}














