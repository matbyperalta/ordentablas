package com.macbein.coneccion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConeccion {
	String driverClassName = "oracle.jdbc.driver.OracleDriver";
	String connectionUrl = "jdbc:oracle:thin:@137.135.80.186:1521:registro";
	String dbUser = "adm_sdb";
	String dbPwd = "Osdb2018*";

	private static FabricaConeccion connectionFactory = null;

	private FabricaConeccion() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection(String ip, String puerto, String usuario, String clave, String bd) throws SQLException {
		Connection conn = null;
		connectionUrl = "jdbc:oracle:thin:@"+ip+":"+puerto+":"+bd+"";
		dbUser = usuario;
		dbPwd = clave;
		conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
		return conn;
	}

	public static FabricaConeccion getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new FabricaConeccion();
		}
		return connectionFactory;
	}
}
