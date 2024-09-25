package com.mercurio.lms.facade.radar.impl;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Imagem;
import com.mercurio.lms.configuracoes.model.service.ImagemService;
import com.mercurio.lms.facade.radar.ImagemFacade;

/**
 * @author Gilmar Costa
 * @spring.bean id="lms.radar.imagemFacade"
 */
@ServiceSecurity
public class ImagemFacadeImpl implements ImagemFacade {
	
	private ImagemService imagemService;

	@Override
	@MethodSecurity
	(
		processGroup = "radar.imagem",
		processName = "findImagemByChave", authenticationRequired=false
	)
	public TypedFlatMap findImagemByChave(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		String chave = criteria.getString("chave");
		Imagem imagem = imagemService.findByChave(chave);
		map.put("picture", imagem.getPicture());
		return map;
	}

	public void setImagemService(ImagemService imagemService) {
		this.imagemService = imagemService;
	}
}
