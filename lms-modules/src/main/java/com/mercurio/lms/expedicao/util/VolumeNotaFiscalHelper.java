package com.mercurio.lms.expedicao.util;

import java.util.Map;

import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

public class VolumeNotaFiscalHelper {

	public static void setDimensoes(VolumeNotaFiscal bean, Map data) {
		Integer nrDimensao1Cm = (Integer) data.get("nrDimensao1Cm");
		Integer nrDimensao2Cm = (Integer) data.get("nrDimensao2Cm");
		Integer nrDimensao3Cm = (Integer) data.get("nrDimensao3Cm");
		Double nrCubagemM3 = (Double) data.get("nrCubagemM3");
		
		setDimensoes(bean, nrDimensao1Cm, nrDimensao2Cm, nrDimensao3Cm, nrCubagemM3);
	}

	public static void setDimensoes(VolumeNotaFiscal bean,
			Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, Double nrCubagemM3) {
		bean.setNrDimensao1Cm(nrDimensao1Cm);
		bean.setNrDimensao2Cm(nrDimensao2Cm);
		bean.setNrDimensao3Cm(nrDimensao3Cm);
		bean.setNrCubagemM3(nrCubagemM3);
	}

}
