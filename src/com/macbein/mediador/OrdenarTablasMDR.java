package com.macbein.mediador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.macbein.coneccion.FabricaConeccion;
import com.macbein.dao.ConstraintsDAO;

public class OrdenarTablasMDR {

	private List<String> tablasOrdenadas = null;
	private List<String> tablasCompleta = null;
	private int nivel = 1;
	private ConstraintsDAO constraintsDAO = null;
	private Connection conn = null;
	private BufferedWriter bufferedWriter = null;
	
	public void inicar(String ip, String puerto, String usuario, String clave, String bd, String ruta) {
		
		try {

			tablasCompleta = new ArrayList<String>();
			tablasOrdenadas = new ArrayList<String>();
			conn = FabricaConeccion.getInstance().getConnection(ip, puerto, usuario, clave, bd);
			
			constraintsDAO = new ConstraintsDAO(conn);

			tablasCompleta = listarTablas();
			tablasOrdenadas.addAll(tablasSinHijos());
			while (tablasCompleta.size() > 0) {
//				for (String nombreTablas : tablasOrdenadas) {
//					System.out.println("tablasOrdenadasDebug: "+nombreTablas);
//				}
//				for (String nombreTablas : tablasCompleta) {
//					System.out.println("tablasCompletaDebug: "+nombreTablas);
//				}
				nivel++;
				List<String> lista = tablasSinNietos();
				//if(lista != null) {
					tablasOrdenadas.addAll(lista);
				//}
			}
			
			File file = new File(ruta);
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (String nombreTablas : tablasOrdenadas) {
//				System.out.println("tablasOrdenadas: "+nombreTablas);
				bufferedWriter.write(nombreTablas);
				bufferedWriter.newLine();
			}
			

		} catch (Exception e) {
			for(String dat : tablasCompleta) {
				System.out.println(dat);
			}
			e.printStackTrace();
		}finally {
			try {
				if (conn != null)
					conn.close();
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> listarTablas() {

		List<String> listaTablas = constraintsDAO.listarTablas();
		System.out.println("Cantidad de tablas: " + listaTablas.size());
		return listaTablas;
	}

	private List<String> tablasSinHijos() {

		List<String> tablasSinHijos = null;

		for (int i = 0; i < tablasCompleta.size(); i++) {
			List<String> tablasForeign = constraintsDAO.listarForeignTablas(tablasCompleta.get(i));
			if (tablasForeign == null) {
				if (tablasSinHijos == null) {
					tablasSinHijos = new ArrayList<String>();
				}
				tablasSinHijos.add(tablasCompleta.get(i));
				System.out.println("Nivel "+nivel+": " + tablasCompleta.get(i));
				tablasCompleta.remove(i);
				--i;
			}
		}

		return tablasSinHijos;
	}

	private List<String> tablasSinNietos() {

		List<String> tablasSinNietos = null;
		for (int i = 0; i < tablasCompleta.size(); i++) {
			if(tablasCompleta.get(i).equals("SDB_BNG_REGISTRO_ANOTACION_PROHIBICION") && nivel == 11) {
				System.out.println("debug");
			}
			//System.out.println("tabla Completa iteración i:"+tablasCompleta.get(i));
			List<String> tablasForeign = constraintsDAO.listarForeignTablas(tablasCompleta.get(i));
			if (tablasForeign != null) {
				boolean noTieneNietos = true;
				for (int j = 0; j < tablasForeign.size(); j++) {
					if(tablasCompleta.get(i).toString().equals(tablasForeign.get(j).toString())) {
						continue;
					}
					if (!tablasOrdenadas.contains(tablasForeign.get(j))) {
						noTieneNietos = false;
						break;
					}
				}
				if (noTieneNietos) {
					if (tablasSinNietos == null) {
						tablasSinNietos = new ArrayList<String>();
					}
					tablasSinNietos.add(tablasCompleta.get(i));
					System.out.println("Nivel "+nivel+": " + tablasCompleta.get(i));
					tablasCompleta.remove(i);
					--i;
				}
			}
		}
		return tablasSinNietos;
	}

}
