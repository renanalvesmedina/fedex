package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.OcorrenciaEndereco;
import com.mercurio.lms.configuracoes.model.dao.OcorrenciaEnderecoDAO;


/**
 * Classe de servi�o para CRUD:   
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.OcorrenciaEnderecoService"
 */
public class OcorrenciaEnderecoService extends CrudService<OcorrenciaEndereco, Long> {

	public OcorrenciaEndereco findById(Long id) {
		return (OcorrenciaEndereco)super.findById(id);
	}

	public java.io.Serializable store(OcorrenciaEndereco bean) {
		return super.store(bean);
	}

	public OcorrenciaEnderecoDAO getOcorrenciaEnderecoDAO() {
		return (OcorrenciaEnderecoDAO) getDao();
	}

	public void setOcorrenciaEnderecoDAO(OcorrenciaEnderecoDAO ocorrenciaEnderecoDAO) {
		setDao( ocorrenciaEnderecoDAO );
	}

}