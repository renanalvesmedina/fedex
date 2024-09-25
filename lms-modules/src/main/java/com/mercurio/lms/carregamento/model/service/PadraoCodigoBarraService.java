package com.mercurio.lms.carregamento.model.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.lms.carregamento.model.PadraoCodigoBarra;
import com.mercurio.lms.carregamento.model.dao.PadraoCodigoBarraDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.PadraoCodigoBarra"
 */
public class PadraoCodigoBarraService extends CrudService<PadraoCodigoBarra, Long> {

	public PadraoCodigoBarra findById(java.lang.Long id) {
		return (PadraoCodigoBarra)super.findById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PadraoCodigoBarra bean) {
		return super.store(bean);
	}
	
	public List<PadraoCodigoBarra> findPadraoCodigoBarraByNrCaracter(String codigoBarra){
		return getPadraoCodigoBarraDAO().findPadraoCodigoBarraByNrCaracter(codigoBarra);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPadraoCodigoBarraDAO(PadraoCodigoBarraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PadraoCodigoBarraDAO getPadraoCodigoBarraDAO() {
		return (PadraoCodigoBarraDAO) getDao();
	}
	

	
	
}