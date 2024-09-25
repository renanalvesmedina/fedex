package com.mercurio.lms.carregamento.action;

import java.util.List;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.MotivoParadaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.motivoParadaControleCargaAction"
 */

public class MotivoParadaControleCargaAction {
	
	private MotivoParadaService motivoParadaService;


	public void setMotivoParadaService(MotivoParadaService motivoParadaService) {
		this.motivoParadaService = motivoParadaService;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		List lista = motivoParadaService.findByIdPontoParadaTrecho(criteria.getLong("idPontoParadaTrecho"));
		return new ResultSetPage(Integer.valueOf(1), lista); 
	}
}