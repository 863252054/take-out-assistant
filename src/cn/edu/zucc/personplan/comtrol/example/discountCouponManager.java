package cn.edu.zucc.personplan.comtrol.example;

import cn.edu.zucc.personplan.itf.IDiscountCouponManager;
import cn.edu.zucc.personplan.itf.IFullReductionManager;
import cn.edu.zucc.personplan.model.BeanDiscountCoupon;
import cn.edu.zucc.personplan.model.BeanFullReduction;
import cn.edu.zucc.personplan.model.BeanMerchant;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.BusinessException;
import cn.edu.zucc.personplan.util.DBUtil2;
import cn.edu.zucc.personplan.util.DbException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class discountCouponManager implements IDiscountCouponManager {
    @Override
    public void addDiscountCoupon(BeanMerchant merchant, float money, Date startDate,
                                  Date endDate, int request, String isConflict) throws BaseException{
        Connection conn=null;
        try {
            conn= DBUtil2.getConnection();
            String sql="insert into tbl_discountCoupon (merchant_id,discountCoupon_money,discountCoupon_startDate" +
                    ",discountCoupon_endDate,discountCoupon_request,discountCoupon_isConflict) values (?,?,?,?,?,?)";
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.setInt(1,merchant.getMerchant_id());
            pst.setFloat(2,money);
            pst.setTimestamp(3,new java.sql.Timestamp(startDate.getTime()));
            pst.setTimestamp(4,new java.sql.Timestamp(endDate.getTime()));
            pst.setInt(5,request);
            pst.setString(6,isConflict);
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

    public void modifyDiscountCoupon(BeanDiscountCoupon discountCoupon, float money, Date startDate,
                                     Date endDate, int request, String isConflict) throws BaseException{
        Connection conn=null;
        try {
            conn= DBUtil2.getConnection();
            String sql="select * from tbl_discountcoupon " +
                    "where discountCoupon_id in(" +
                    "SELECT discountCoupon_id FROM tbl_discountcouponrequest " +
                    "UNION SELECT discountCoupon_id FROM tbl_userdiscountcoupon " +
                    "UNION SELECT discountCoupon_id FROM tbl_productorder) " +
                    "and discountCoupon_id=?";
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.setInt(1,discountCoupon.getDiscountCoupon_id());
            java.sql.ResultSet rs = pst.executeQuery();
            if(rs.next()) throw new BusinessException("无法修改，订单中已有该优惠券");
            sql="update tbl_discountCoupon set discountCoupon_money = ?,discountCoupon_startDate = ?" +
                    ",discountCoupon_endDate = ?,discountCoupon_request = ?,discountCoupon_isConflict = ? where discountCoupon_id = ?";
            pst=conn.prepareStatement(sql);
            pst.setFloat(1,money);
            pst.setTimestamp(2,new java.sql.Timestamp(startDate.getTime()));
            pst.setTimestamp(3,new java.sql.Timestamp(endDate.getTime()));
            pst.setInt(4,request);
            pst.setString(5,isConflict);
            pst.setInt(6,discountCoupon.getDiscountCoupon_id());
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

    public void deleteDiscountCoupon(BeanDiscountCoupon discountCoupon) throws BaseException{
        Connection conn=null;
        try {
            conn= DBUtil2.getConnection();
            String sql="select * from tbl_discountcoupon " +
                    "where discountCoupon_id in(" +
                    "SELECT discountCoupon_id FROM tbl_discountcouponrequest " +
                    "UNION SELECT discountCoupon_id FROM tbl_userdiscountcoupon " +
                    "UNION SELECT discountCoupon_id FROM tbl_productorder) " +
                    "and discountCoupon_id=?";
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.setInt(1,discountCoupon.getDiscountCoupon_id());
            java.sql.ResultSet rs = pst.executeQuery();
            if(rs.next()) throw new BusinessException("无法删除，已有该优惠券记录");
            sql="delete from tbl_discountCoupon where discountCoupon_id = "+discountCoupon.getDiscountCoupon_id();
            java.sql.Statement st = conn.createStatement();
            st.execute(sql);
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

    public List<BeanDiscountCoupon> loadDiscountCoupon(BeanMerchant beanMerchant) throws BaseException{
        List<BeanDiscountCoupon> result=new ArrayList<BeanDiscountCoupon>();
        Connection conn = null;
        try {
            conn= DBUtil2.getConnection();
            String sql = "select * from tbl_discountCoupon where merchant_id=? group by discountCoupon_id";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, beanMerchant.getMerchant_id());
            java.sql.ResultSet rs= pst.executeQuery();
            while(rs.next()){
                BeanDiscountCoupon bdc=new BeanDiscountCoupon();
                bdc.setDiscountCoupon_id(rs.getInt(1));
                bdc.setMerchant_id(rs.getInt(2));
                bdc.setDiscountCoupon_money(rs.getFloat(3));
                bdc.setDiscountCoupon_startDate(rs.getTimestamp(4));
                bdc.setDiscountCoupon_endDate(rs.getTimestamp(5));
                bdc.setDiscountCoupon_request(rs.getInt(6));
                bdc.setDiscountCoupon_isConflict(rs.getString(7));
                result.add(bdc);
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
