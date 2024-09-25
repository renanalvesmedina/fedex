package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Densidade;
import com.mercurio.lms.expedicao.model.dao.DensidadeDAO;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.densidadeService"
 */
public class DensidadeService extends CrudService<Densidade, Long> {

	/**
	 * Recupera uma inst�ncia de <code>Densidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public Densidade findById(Long id) {
		return (Densidade)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	public Densidade beforeStore(Densidade bean) {
		bean.setEmpresa(SessionUtils.getEmpresaSessao());
		return super.beforeStore(bean);	
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(Densidade bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDensidadeDAO(DensidadeDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DensidadeDAO getDensidadeDAO() {
		return (DensidadeDAO) getDao();
	}

	public List findAllAtivo() {
		return getDensidadeDAO().findAllAtivo();
	}

	public Densidade findByIdEmpresaTpDensidade(Long idEmpresa, String tpDensidade) {
		return getDensidadeDAO().findByIdEmpresaTpDensidade(idEmpresa,tpDensidade);
	}
	
	public Densidade findByTpDensidade(String tpDensidade){
		return getDensidadeDAO().findByTpDensidade(tpDensidade);
	}
}