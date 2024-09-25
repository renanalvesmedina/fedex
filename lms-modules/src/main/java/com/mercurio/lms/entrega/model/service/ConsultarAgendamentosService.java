package com.mercurio.lms.entrega.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.dao.AgendamentoDoctoServicoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.consultarAgendamentosService"
 */
public class ConsultarAgendamentosService extends CrudService{
	public void setDAO(AgendamentoDoctoServicoDAO dao) {
		setDao( dao );
	}

}
