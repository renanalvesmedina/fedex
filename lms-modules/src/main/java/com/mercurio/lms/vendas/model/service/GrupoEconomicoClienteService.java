package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.GrupoEconomico;
import com.mercurio.lms.vendas.model.GrupoEconomicoCliente;
import com.mercurio.lms.vendas.model.dao.GrupoEconomicoClienteDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.grupoEconomicoClienteService"
 */
public class GrupoEconomicoClienteService extends CrudService<GrupoEconomicoCliente, Long> {

	public GrupoEconomicoCliente findById(java.lang.Long id) {
		return (GrupoEconomicoCliente)super.findById(id);
	}

	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(GrupoEconomicoCliente bean) {
		return super.store(bean);
	}

	public void setGrupoEconomicoClienteDAO(GrupoEconomicoClienteDAO dao) {
		setDao( dao );
	}

	private GrupoEconomicoClienteDAO getGrupoEconomicoClienteDAO() {
		return (GrupoEconomicoClienteDAO) getDao();
	}
	
	 /**
	 * Busca lista de <tt>GrupoEconomicoCliente</tt> pelo id do
	 * <tt>GrupoEconomico</tt> a que estão relacionadas, correspondente atributo
	 * <tt>grupoEconomico.idGrupoEconomico</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>grupoEconomico.idGrupoEconomico</tt>
	 * @return lista de <tt>GrupoEconomicoCliente</tt>
	 */
	public ResultSetPage<GrupoEconomicoCliente> findGrupoEconomicoClienteList(TypedFlatMap criteria) {
		return getGrupoEconomicoClienteDAO().findGrupoEconomicoClienteList(criteria);
	}
	
	/**
	 * Busca quantidade de <tt>GrupoEconomicoCliente</tt> relacionados a um
	 * <tt>GrupoEconomico</tt> com id correspondente atributo
	 * <tt>grupoEconomico.idGrupoEconomico</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>grupoEconomico.idGrupoEconomico</tt>
	 * @return quantidade de <tt>GrupoEconomicoCliente</tt>
	 */
	public Integer findSimulacaoAnexoRowCount(TypedFlatMap criteria) {
		Long idGrupoEconomico = criteria.getLong("grupoEconomico.idGrupoEconomico");
		return getGrupoEconomicoClienteDAO().findGrupoEconomicoClienteRowCount(idGrupoEconomico);
	}
	
	public List<Long> findIdsByIdsGrupoEconomico(List<Long> idGrupoEconomico){
		return getGrupoEconomicoClienteDAO().findIdsByIdsGrupoEconomico(idGrupoEconomico);
	}

	public List<GrupoEconomicoCliente> findByIdGrupoIdCliente(Long idGrupoEconomico, Long idCliente) {
		return getGrupoEconomicoClienteDAO().findByIdGrupoIdCliente(idGrupoEconomico, idCliente);
	}
	
	public List<GrupoEconomico> findGruposEconomicos(Long idCliente) {
		return getGrupoEconomicoClienteDAO().findGruposEconomicos(idCliente);
	}

}
