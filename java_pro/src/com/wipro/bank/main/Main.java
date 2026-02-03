package com.wipro.bank.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import com.wipro.bank.*;
import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.dao.BankDAO;
import com.wipro.bank.service.BankService;
import com.wipro.bank.util.DBUtil;

public class Main {
	public static void main(String[] args) throws Exception {
		Connection con = DBUtil.getConnection();
		System.out.println("Connection Successful");

		Statement smt = con.createStatement();

		String query = "select * from transfer_tbl";

		ResultSet res = smt.executeQuery(query);


		BankDAO bankdao = new BankDAO();

		BankService service = new BankService();

		

		TransferBean transferbean = new TransferBean(1, "1234567897", "1234567894", new Date(), 1000f);
		

		System.out.println(service.transfer(transferbean));
		System.out.println(service.checkBalance("1234567897"));

	}
}