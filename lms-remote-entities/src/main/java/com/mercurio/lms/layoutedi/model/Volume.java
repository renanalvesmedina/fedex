package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Volume  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1294207697148244894L;
	private String codigoVolume;
	
	public final String getCodigoVolume() {
		return codigoVolume;
	}
	public final void setCodigoVolume(String codigoVolume) {
		this.codigoVolume = codigoVolume;
	}
	
	public static Volume parseMap(HashMap<String, Object> mapa){
		Volume volume = new Volume();
		
		if (mapa.get("COD_VOLUME") != null)
			volume.setCodigoVolume((String)mapa.get("COD_VOLUME"));

		return volume;
	}	
}
