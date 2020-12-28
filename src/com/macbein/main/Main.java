package com.macbein.main;

import com.macbein.mediador.OrdenarTablasMDR;

public class Main {

	public static void main(String[] args) {
		OrdenarTablasMDR ordenarTablasMDR = new OrdenarTablasMDR();
		String ip = "10.0.0.5";//args[0];
		String puerto = "1521";//args [1];
		String usuario = "adm_sdb";//args[2];
		String clave = "glenis2018";//args[3];
		String bd = "NSTAGE";//args[4];
		String rutaArchivo = "";//args[5];
		
//		String ip = "10.0.0.10";//args[0];
//		String puerto = "1521";//args [1];
//		String usuario = "adm_sdb";//args[2];
//		String clave = "Osdb2018*";//args[3];
//		String bd = "registro";//args[4];
//		String rutaArchivo = "";//args[5];
		
		ordenarTablasMDR.inicar(ip, puerto, usuario, clave, bd, rutaArchivo);

	}

}
