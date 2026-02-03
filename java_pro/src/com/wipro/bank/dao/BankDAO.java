package com.wipro.bank.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDAO {

	private Connection con = DBUtil.getConnection();

	public boolean validateAccount(String accountNumber) throws SQLException {
		String query = "SELECT Account_Number FROM account_tbl WHERE Account_Number = ?";
		try (PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, accountNumber);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public float findBalance(String accountNumber) throws SQLException {
		if (!validateAccount(accountNumber)) {
			return -1;
		}

		String query = "SELECT Balance FROM account_tbl WHERE Account_Number = ?";
		try (PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, accountNumber);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getFloat("Balance");
				}
			}
		}
		return -1;
	}

	public boolean transferMoney(TransferBean transferbean) {
		String query = "INSERT INTO transfer_tbl (Transaction_id, Account_Number, Beneficiary_acc_number, Transaction_Date, Transaction_Amount) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, generateSequenceNumber());
			ps.setString(2, transferbean.getFromAccountNumber());
			ps.setString(3, transferbean.getToAccountNumber());
			ps.setDate(4, new Date(transferbean.getDateOfTransaction().getTime()));
			ps.setFloat(5, transferbean.getAmount());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateBalance(String accountNumber, float newBalance) {
		try {
			if (!validateAccount(accountNumber)) {
				return false;
			}

			String query = "UPDATE account_tbl SET Balance = ? WHERE Account_Number = ?";
			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setFloat(1, newBalance);
				ps.setString(2, accountNumber);
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	public int generateSequenceNumber() {
		String query = "SELECT MAX(Transaction_id) FROM transfer_tbl";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			return 1;
		}
		return 1;
	}
}
