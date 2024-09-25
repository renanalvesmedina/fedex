package com.mercurio.lms.rest.sim;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseDTO;

public class LocalizacaoMercadoriaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private VarcharI18n dsLocalizacaoMercadoria;

	public VarcharI18n getDsLocalizacaoMercadoria() {
		return dsLocalizacaoMercadoria;
	}

	public void setDsLocalizacaoMercadoria(VarcharI18n dsLocalizacaoMercadoria) {
		this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
	}
}