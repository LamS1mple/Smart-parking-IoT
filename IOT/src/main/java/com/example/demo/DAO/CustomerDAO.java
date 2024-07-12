package com.example.demo.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.example.demo.Model.Customer;

public class CustomerDAO {
	public static Connection con;

	public CustomerDAO() {
		if (con == null) {
			String dbUrl = "jdbc:mysql://localhost:3306/iot_thay_duc";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbUrl, "root", "lam2002");
				System.out.println("Thành công");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public int addXe(Customer a) {
		String queryString = "Insert into customers (bienSoXe, the, raVao, trangThai) values(? , ?, ? , ?)";
		try {
			CallableStatement callableStatement = con.prepareCall(queryString);
			callableStatement.setString(1, a.getBienSoXe());
			callableStatement.setString(2, a.getThe());
			callableStatement.setInt(3, a.getRaVao() );
			callableStatement.setInt(4, a.getTrangThai());
			callableStatement.executeUpdate();
			ResultSet rs = callableStatement.getGeneratedKeys();
			int id = 0;
			if (rs.next())
				id = rs.getInt(1);
			System.out.println(id);
			return id;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return 0;
	}

	public void updateBuong(Customer a) {
		String queryString = "update customers set viTri = ? where id = ?";
		try {
			CallableStatement callableStatement = con.prepareCall(queryString);
			callableStatement.setInt(1, a.getViTri());
			callableStatement.setInt(2, a.getId());
			callableStatement.executeUpdate();


		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public  boolean out(Customer a) {
		String qurey = "Select * from customers where bienSoXe = ? and the = ? and trangThai = 1";
		String quString = "Update customers set trangThai=? where id = ?";
		try {
			CallableStatement callableStatement = con.prepareCall(qurey);
			callableStatement.setString(1,a.getBienSoXe());
			callableStatement.setString(2, a.getThe());
			ResultSet resultSet = callableStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println(12345);
				a.setId(resultSet.getInt("id"));
				a.setViTri(resultSet.getInt("viTri"));	
				a.setTrangThai(0);
				System.out.println(a);
				callableStatement = con.prepareCall(quString);
				callableStatement.setInt(1, 0);
				callableStatement.setInt(2, a.getId());
				callableStatement.executeUpdate();
				addXe(a);
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	public  int timKiemBangGiong(Customer a) {
		String qurey = "Select * from customers where bienSoXe = ? and trangThai = 1";
		try {
			CallableStatement callableStatement = con.prepareCall(qurey);
			callableStatement.setString(1,a.getBienSoXe());
			ResultSet resultSet = callableStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("viTri");
			}
			
		} catch (Exception e) {
		}
		return 0;
	}
	public int timKiemBangThe(Customer a) {
		String qurey = "Select * from customers where the = ? and trangThai = 1";
		try {
			CallableStatement callableStatement = con.prepareCall(qurey);
			callableStatement.setString(1,a.getThe());
			ResultSet resultSet = callableStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("viTri");
			}
			
		} catch (Exception e) {
		}
		return 0;
	}
}
