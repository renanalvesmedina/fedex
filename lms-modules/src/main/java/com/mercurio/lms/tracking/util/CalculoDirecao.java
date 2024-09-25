package com.mercurio.lms.tracking.util;




public class CalculoDirecao {
	
	public enum Direcao {

		NORTE, SUL, LESTE, OESTE, NOROESTE, SUDESTE, NORDESTE, SUDOESTE;
	}

	/**
	 * Calcula a direção(NORTE, SUL, LESTE, OESTE, NOROESTE,
	 * SUDESTE, NORDESTE, SUDOESTE) a partir de pontos no mapa
	 * 
	 * @param lat1 - latitude atual
	 * @param long1 - longitude atual
	 * @param lat2 - latitude destino
	 * @param long2 - longitude destino 
	 * @return A Direcao.
	 */
	public synchronized static Direcao calculaDirecao(double lat1, double long1, double lat2, double long2){
		double graus = bearing(lat1, long1, lat2, long2);
		Direcao direcao = null;
		if((graus >= 337.5 && graus <= 360.0) || (graus <= 22.5 && graus >= 0.0)){
			direcao = Direcao.NORTE;
		}else if(graus > 22.5 && graus <= 67.65){
			direcao = Direcao.NORDESTE;
		}else if(graus > 67.65 && graus <= 112.5){
			direcao = Direcao.LESTE;
		}else if(graus > 112.5 && graus <= 157.5){
			direcao = Direcao.SUDESTE;
		}else if(graus > 157.5 && graus <= 202.5){
			direcao = Direcao.SUL;
		}else if(graus > 202.5 && graus <= 247.5){
			direcao = Direcao.SUDOESTE;
		}else if(graus > 247.5 && graus <= 292.5){
			direcao = Direcao.OESTE;
		}else if(graus > 292.5 && graus < 337.5){
			direcao = Direcao.NOROESTE;
		}
		
		return direcao;
	}
	
	/**
	 * Calcula o rolamento em graus a partir de pontos no mapa.
	 * 
	 * @param lat1 - latitude atual
	 * @param long1 - longitude atual
	 * @param lat2 - latitude destino
	 * @param long2 - longitude destino 
	 * @return rolamento em graus.
	 */
	private static double bearing(double lat1, double long1, double lat2, double long2){
			lat1 = DegreeToRadian(lat1);
			lat2 = DegreeToRadian(lat2);
			double dLon = DegreeToRadian(long2-long1);
			double y = Math.sin(dLon) * Math.cos(lat2);
			double x = Math.cos(lat1)*Math.sin(lat2) -
			Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
			return toBrng(Math.atan2(y, x));
	}
	
	/**
	 *  convert radians to degrees (as bearing: 0...360)
	 * @param radiano
	 * @return
	 */
	private static double toBrng(double radiano) {
		return (RadianToDegree(radiano)+360) % 360;
	}
	
	/**
	 * Converte de graus para radianos.
	 * @param angulo - angulo em graus.
	 * @return
	 */
	private static double DegreeToRadian(double angulo) {
		return Math.PI * angulo / 180.0;
	}
	
	/**
	 * Converte de radiano para graus.
	 * @param angulo - angulo em graus
	 * @return
	 */
	private static double RadianToDegree(double angulo) {
		return 180.0 * angulo / Math.PI;
	}
}
