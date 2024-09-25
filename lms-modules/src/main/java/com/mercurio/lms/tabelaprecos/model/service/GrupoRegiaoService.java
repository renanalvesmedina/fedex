package com.mercurio.lms.tabelaprecos.model.service;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.GrupoRegiaoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.MunicipioGrupoRegiaoDAO;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.grupoRegiaoService"
 */
public class GrupoRegiaoService extends CrudService<GrupoRegiao, Long> {

	private TabelaPrecoService tabelaPrecoService;
	private MunicipioGrupoRegiaoService municipioGrupoRegiaoService;
	private MunicipioGrupoRegiaoDAO municipioGrupoRegiaoDAO;

	@Override
	public GrupoRegiao findById(java.lang.Long id) {
		return getGrupoRegiaoDAO().findByIdGrupoRegiao(id);
	}	
	
	public GrupoRegiao findByIdCrudService(java.lang.Long id) {
		return (GrupoRegiao) super.findById(id);
	}
	
	/**
	 *  3.5
	 */
	@Override
	public Serializable store(GrupoRegiao bean) {
		return super.store(bean);
	}

	public void generateGruposRegioesAlterandoTabelaPreco(Long idOldTabelaPreco, Long idNewTabelaPreco) throws Throwable {
		TabelaPreco tabelaNova =  tabelaPrecoService.findByIdTabelaPreco(idNewTabelaPreco);
		validateNovaTabelaPreco(tabelaNova.isEfetivada(), tabelaNova.getIdTabelaPreco());

		getGrupoRegiaoDAO().insertCloneGrupoRegiao(idOldTabelaPreco, idNewTabelaPreco);
		municipioGrupoRegiaoDAO.insertCloneMunicipioGrupoRegiao(idNewTabelaPreco);
	}

	private void validateNovaTabelaPreco(boolean isEfetivada, Long idNewtabelapreco) {
		if (isEfetivada){
			throw new BusinessException("LMS-30062");
		}
		
		if (existeGrupoRegiaoUsandoTabela(idNewtabelapreco)){
			throw new BusinessException("LMS-30063");
		}
	}

	private boolean existeGrupoRegiaoUsandoTabela(Long idNewtabelapreco) {
		Collection grupos = getGrupoRegiaoDAO().findByTabelaPreco(idNewtabelapreco);
		return isNotEmpty(grupos);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return super.getRowCount(criteria);
	}	
	
	@Override
	public void removeById(Long id) {
		municipioGrupoRegiaoService.removeByIdGrupoRegiao(id);
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			municipioGrupoRegiaoService.removeByIdGrupoRegiao(id);
		}
		super.removeByIds(ids);
	}
	
	public List findGruposRegiao() {
		return getGrupoRegiaoDAO().findGruposRegiao();
	}
	
	/**
	 * Obtem os grupos regiões através do idTabelaPreco e idUnidadeFederativa
	 * 
	 * @param  idTabelaPreco
	 * @param  idUnidadeFederativa
	 * @return List 
	 */
	public List<GrupoRegiao> findGruposRegiao(Long idTabelaPreco, Long idUnidadeFederativa) {
		return getGrupoRegiaoDAO().findGruposRegiao(idTabelaPreco,idUnidadeFederativa);
	}	
	
	public ResultSetPage<GrupoRegiao> findPaginated(PaginatedQuery paginatedQuery) {
		return getGrupoRegiaoDAO().findPaginated(paginatedQuery); 
	}

	
	/**
	 * Busca o Gruporegiao sem vinculo com
	 * MunicipioGrupoRegiao 
	 * 
	 * @param idTabelaPreco
	 * @param idMunicipio
	 * @return
	 */
	public List<GrupoRegiao> findGrupoRegiao(Long idTabelaPreco, Long idMunicipio) {
		return getGrupoRegiaoDAO().findGrupoRegiao(idTabelaPreco,idMunicipio);
	}
	
	//LMS-3626
	public GrupoRegiao findByGrupoTabela(Long idGrupoRegiao, Long idTabelaPreco) {
		return getGrupoRegiaoDAO().findByGrupoTabela(idGrupoRegiao, idTabelaPreco);
	}
	
	public void setGrupoRegiaoDAO(GrupoRegiaoDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private GrupoRegiaoDAO getGrupoRegiaoDAO() {
		return (GrupoRegiaoDAO) getDao();
	}	

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
			
	public void setMunicipioGrupoRegiaoService(
			MunicipioGrupoRegiaoService municipioGrupoRegiaoService) {
		this.municipioGrupoRegiaoService = municipioGrupoRegiaoService;
	}
	
	public void setMunicipioGrupoRegiaoDAO(MunicipioGrupoRegiaoDAO municipioGrupoRegiaoDAO) {
		this.municipioGrupoRegiaoDAO = municipioGrupoRegiaoDAO;
	}
	
	public List findByUfAndTabela(Long idUnidadeFederativa, Long idTabela){
		return getGrupoRegiaoDAO().findByUfAndTabela(idUnidadeFederativa, idTabela);
	}
	
	public GrupoRegiao findByDsGrupoTabelaUf(String dsGrupoRegiao, Long idTabelaPreco, Long idUnidadeFederativa) {
		return getGrupoRegiaoDAO().findByDsGrupoTabelaUf(dsGrupoRegiao, idTabelaPreco, idUnidadeFederativa);
	}
	
}
