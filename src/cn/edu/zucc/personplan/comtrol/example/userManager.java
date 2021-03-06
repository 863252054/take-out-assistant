package cn.edu.zucc.personplan.comtrol.example;

import cn.edu.zucc.personplan.PersonPlanUtil;
import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanRider;
import cn.edu.zucc.personplan.model.BeanSearchProduct;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.model.BeanUserDetail;
import cn.edu.zucc.personplan.util.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userManager implements IUserManager {

	@Override
	public BeanUser reg(String userName, String userSex, String userPassword, String userPassword2, String userTelNumber,
						String userEmail, String userCity) throws BaseException{
		// TODO Auto-generated method stub
		if("".equals(userName)) throw new BusinessException("用户名不能为空");
		if("".equals(userTelNumber)) throw new BusinessException("手机号不能为空");
		if("".equals(userPassword)) throw new BusinessException("密码不能为空");
		if(!userPassword.equals(userPassword2)) throw new BusinessException("两次密码不一致");
		if(userTelNumber.length() != 11) throw new BusinessException("手机号长度应为11位");
		Connection conn = null;
		try {
			conn = DBUtil2.getConnection();
			String sql = "select * from tbl_user where user_name=?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			java.sql.ResultSet rs = pst.executeQuery();
			if (rs.next()) throw new BusinessException("此用户名已存在");
			rs.close();
			pst.close();
			sql = "insert into tbl_user(user_name,user_sex,user_password,user_telNumber,user_email,user_city,user_regDate,user_isVIP,user_vipEndDate) " +
					"values(?, ?, ?, ?, ?, ?, now(), null , null)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			pst.setString(2, userSex);
			pst.setString(3, userPassword);
			pst.setString(4, userTelNumber);
			pst.setString(5, userEmail);
			pst.setString(6, userCity);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}

	
	@Override
	public BeanUser login(String userName, String userPassword) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;
		try {
			conn= DBUtil2.getConnection();
			String sql="update tbl_user set user_isVIP=-1 where now()>user_vipEndDate";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.execute();
			sql="update tbl_user set user_isVIP=0 where user_vipEndDate is null";
			pst=conn.prepareStatement(sql);
			pst.execute();
			sql="update tbl_user set user_isVIP=1 where user_vipEndDate>now()";
			pst=conn.prepareStatement(sql);
			pst.execute();
			sql="select user_name,user_password,user_id,user_isVIP,user_vipEndDate from tbl_user where user_name=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1,userName);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("用户名不存在");
     		if(!rs.getString(2).equals(userPassword))throw new BusinessException("密码错误");
			BeanUser u=new BeanUser();
			u.setUser_name(rs.getString(1));
			u.setUser_id(rs.getInt(3));
			u.setUser_isVIP(rs.getInt(4));
			u.setUser_vipEndDate(rs.getTimestamp(5));
			rs.close();
			pst.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}


	public void changeName(BeanUser user, String name)throws BaseException{
		if(name==null||"".equals(name)) throw new BusinessException("用户名不能为空");
		Connection conn=null;
		try {
			conn=DBUtil2.getConnection();
			String sql="select * from tbl_user where user_name=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,name);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) throw new BusinessException("用户名已存在");
			sql="update tbl_user set user_name = ? where user_id = ?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, user.getUser_id());
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	@Override
	public void changePwd(BeanUser user, String oldPwd, String newPwd,
			String newPwd2) throws BaseException {
		// TODO Auto-generated method stub
		if(oldPwd==null) throw new BusinessException("密码不能为空");
		if(newPwd==null || "".equals(newPwd) || !newPwd.equals(newPwd2)) throw new BusinessException("两次密码不一致");
		Connection conn=null;
		try {
			conn=DBUtil2.getConnection();
			String sql="select user_password from tbl_user where user_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setInt(1,user.getUser_id());
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("搜索不到用户");
			if(!oldPwd.equals(rs.getString(1))) throw new BusinessException("原密码错误");
			sql="update tbl_user set user_password=? where user_id=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, newPwd);
			pst.setInt(2, user.getUser_id());
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	@Override
	public void systemUserLogin(String systemUserName,String systemUserPassword)throws BaseException{
		Connection conn=null;
		try {
			conn= DBUtil2.getConnection();
			String sql="select systemUser_password from tbl_systemuser where systemUser_name=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,systemUserName);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("账号名不存在");
			if(!rs.getString(1).equals(systemUserPassword)) throw new BusinessException("密码错误");
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void vipManager()throws BaseException{
		Connection conn=null;
		try {
			conn=DBUtil2.getConnection();
			String sql=null;
			if(BeanUser.currentLoginUser.getUser_isVIP()==0||BeanUser.currentLoginUser.getUser_isVIP()==-1){
				sql="update tbl_user set user_isVIP=1,user_vipEndDate=date_add(now(),interval 1 month) where user_id="+BeanUser.currentLoginUser.getUser_id();
			}else if(BeanUser.currentLoginUser.getUser_isVIP()==1){
				sql="update tbl_user set user_vipEndDate=date_add(user_vipEndDate,interval 1 month) where user_id="+BeanUser.currentLoginUser.getUser_id();
			}
			java.sql.Statement st = conn.createStatement();
			st.execute(sql);
			sql="select user_isVIP,user_vipEndDate from tbl_user where user_id="+BeanUser.currentLoginUser.getUser_id();
			java.sql.ResultSet rs =st.executeQuery(sql);
			if(rs.next()){
				BeanUser.currentLoginUser.setUser_isVIP(rs.getInt(1));
				BeanUser.currentLoginUser.setUser_vipEndDate(rs.getTimestamp(2));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public List<BeanUserDetail> loadUserDetails() throws BaseException{
		List<BeanUserDetail> result=new ArrayList<BeanUserDetail>();
		Connection conn = null;
		try {
			conn= DBUtil2.getConnection();
			String sql="select a.user_id,user_name,count(*),sum(b.finalPrice),sum(b.originalPrice-b.finalPrice) " +
					"from tbl_user a, tbl_productorder b " +
					"where a.user_id=b.user_id " +
					"GROUP BY b.user_id";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs=pst.executeQuery();
			while(rs.next()){
				BeanUserDetail bud = new BeanUserDetail();
				bud.setUserId(rs.getInt(1));
				bud.setUserName(rs.getString(2));
				bud.setOrderCount(rs.getInt(3));
				bud.setSumConsumption(rs.getFloat(4));
				bud.setSumDiscount(rs.getFloat(5));
				result.add(bud);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public List<BeanSearchProduct> loadSearchProduct(String key) throws BaseException{
		List<BeanSearchProduct> result=new ArrayList<BeanSearchProduct>();
		Connection conn = null;
		try {
			conn= DBUtil2.getConnection();
			String sql="select * from tbl_productDetails where product_name like '%"+key +"%'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs=pst.executeQuery();
			while(rs.next()){
				BeanSearchProduct bsp = new BeanSearchProduct();
				bsp.setProduct_id(rs.getInt(1));
				bsp.setMerchant_id(rs.getInt(2));
				bsp.setType_id(rs.getInt(3));
				bsp.setProduct_name(rs.getString(4));
				bsp.setProduct_price(rs.getFloat(5));
				bsp.setProduct_discountPrice(rs.getFloat(6));
				bsp.setProduct_count(rs.getInt(7));
				result.add(bsp);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public List<BeanSearchProduct> loadRecommend() throws BaseException{
		List<BeanSearchProduct> result=new ArrayList<BeanSearchProduct>();
		Connection conn = null;
		try {
			conn= DBUtil2.getConnection();
			String sql="select a.product_id,sum(a.product_quantity)from tbl_orderdetail a ,tbl_productorder b " +
					"where b.order_state = '已完成' and a.order_id=b.order_id and a.user_id = ? " +
					"GROUP BY a.product_id " +
					"ORDER BY sum(a.product_quantity) DESC " +
					"Limit 5";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1,BeanUser.currentLoginUser.getUser_id());
			java.sql.ResultSet rs=pst.executeQuery();
			List<Integer> tempProductId = new ArrayList<>();
			while(rs.next()){
				tempProductId.add(rs.getInt(1));
				}
			for(int i=0; i<tempProductId.size();i++){
				sql="select * from tbl_productDetails where product_id = ?";
				pst=conn.prepareStatement(sql);
				pst.setInt(1,tempProductId.get(i));
				rs=pst.executeQuery();
				while(rs.next()){
					BeanSearchProduct bsp = new BeanSearchProduct();
					bsp.setProduct_id(rs.getInt(1));
					bsp.setMerchant_id(rs.getInt(2));
					bsp.setType_id(rs.getInt(3));
					bsp.setProduct_name(rs.getString(4));
					bsp.setProduct_price(rs.getFloat(5));
					bsp.setProduct_discountPrice(rs.getFloat(6));
					bsp.setProduct_count(rs.getInt(7));
					result.add(bsp);
					System.out.println(tempProductId.get(i));
				}
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
