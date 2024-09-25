package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.GrupoEconomico;
import com.mercurio.lms.vendas.model.dao.GrupoEconomicoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.grupoEconomicoService"
 */
public class GrupoEconomicoService extends CrudService<GrupoEconomico, Long> {
	
	private GrupoEconomicoClienteService grupoEconomicoClienteService;

	public GrupoEconomico findById(java.lang.Long id) {
		return (GrupoEconomico)super.findById(id);
	}

	public void removeById(java.lang.Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		List<Long> idsDependenciasRemover = grupoEconomicoClienteService.findIdsByIdsGrupoEconomico(ids);
		grupoEconomicoClienteService.removeByIds(idsDependenciasRemover);
		super.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		List<Long> idsDependenciasRemover = grupoEconomicoClienteService.findIdsByIdsGrupoEconomico(ids);
		grupoEconomicoClienteService.removeByIds(idsDependenciasRemover);
		super.removeByIds(ids);
	}

	public java.io.Serializable store(GrupoEconomico bean) {
		return super.store(bean);
	}

	public GrupoEconomico findGrupoEconomicoByIdGrupoIdCliente(Long idGrupoEconomico, Long idCliente){
		return getGrupoEconomicoDAO().findGrupoEconomicoByIdGrupoIdCliente(idGrupoEconomico, idCliente);
	}

	public void setGrupoEconomicoDAO(GrupoEconomicoDAO dao) {
		setDao( dao );
	}

	private GrupoEconomicoDAO getGrupoEconomicoDAO() {
		return (GrupoEconomicoDAO) getDao();
	}

	public List<GrupoEconomico> findByCodigoDescricaoAtivoDiferente(String codigo, String descricao, Long idGrupoEconomico) {
		return getGrupoEconomicoDAO().findByCodigoDescricaoAtivoDiferente(codigo, descricao, idGrupoEconomico);
	}
	
	@SuppressWarnings("unchecked")
	public List<GrupoEconomico> findGruposEconomicos(TypedFlatMap tfm) {
		Set<GrupoEconomico> retorno = new HashSet<GrupoEconomico>();
		retorno.addAll(getGrupoEconomicoDAO().findGruposEconomicos(tfm));
		
		if(tfm.getLong("clientePrincipal.idCliente") != null){
			retorno.addAll(grupoEconomicoClienteService.findGruposEconomicos(tfm.getLong("clientePrincipal.idCliente")));
		}
		return new ArrayList<GrupoEconomico>(retorno);
		
	}

	public void setGrupoEconomicoClienteService(GrupoEconomicoClienteService grupoEconomicoClienteService) {
		this.grupoEconomicoClienteService = grupoEconomicoClienteService;
	}

}
