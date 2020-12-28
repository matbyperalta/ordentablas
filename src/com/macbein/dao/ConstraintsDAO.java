package com.macbein.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintsDAO {

	private Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;

	public ConstraintsDAO(Connection conn) {
		connection = conn;
	}

//	private Connection getConnection() throws SQLException {
//		Connection conn;
//		conn = FabricaConeccion.getInstance().getConnection();
//		return conn;
//	}

	public List<String> listarTablas() {
		List<String> listaTablas = null;
		try {

			String queryString = "SELECT table_name FROM user_tables ORDER BY table_name";
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {

				if (listaTablas == null) {
					listaTablas = new ArrayList<>();
				}

				String nombreTabla = resultSet.getString("table_name");

				listaTablas.add(nombreTabla);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return listaTablas;

	}
	
	public List<String> listarForeignTablas(String tabla){
		List<String> listaTablas = null;
		try {

			StringBuffer queryString = new StringBuffer();
			
			queryString.append("SELECT a.table_name, a.column_name, a.constraint_name, c.owner, c.r_owner, c_pk.table_name r_table_name, c_pk.constraint_name r_pk ");
			queryString.append("FROM all_cons_columns a ");
			queryString.append("JOIN all_constraints c ON a.owner = c.owner ");
			queryString.append("AND a.constraint_name = c.constraint_name ");
			queryString.append("JOIN all_constraints c_pk ON c.r_owner = c_pk.owner ");
			queryString.append("AND c.r_constraint_name = c_pk.constraint_name ");
			queryString.append("WHERE c.constraint_type = 'R' ");
			queryString.append("AND a.table_name = ? ");
			
			ptmt = connection.prepareStatement(queryString.toString());
			ptmt.setString(1, tabla);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {

				if (listaTablas == null) {
					listaTablas = new ArrayList<>();
				}

				String nombreTabla = resultSet.getString("r_table_name");

				listaTablas.add(nombreTabla);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return listaTablas;
	}

	public List<String> listarForeignDestinoTablas(String tabla){
		List<String> listaTablas = null;
		try {

			StringBuffer queryString = new StringBuffer();
			
			queryString.append("SELECT UC.TABLE_NAME, UC.R_CONSTRAINT_NAME, UCC.TABLE_NAME, UCC.COLUMN_NAME ");
			queryString.append("FROM USER_CONSTRAINTS  UC, USER_CONS_COLUMNS UCC ");
			queryString.append("WHERE UC.R_CONSTRAINT_NAME = UCC.CONSTRAINT_NAME ");
			queryString.append("AND UC.CONSTRAINT_TYPE = 'R' ");
			queryString.append("AND UCC.TABLE_NAME = ? ");
			
			ptmt = connection.prepareStatement(queryString.toString());
			ptmt.setString(1, tabla);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {

				if (listaTablas == null) {
					listaTablas = new ArrayList<>();
				}

				String nombreTabla = resultSet.getString("TABLE_NAME");

				listaTablas.add(nombreTabla);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return listaTablas;
	}
	
	
	
	
	
}
