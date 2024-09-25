package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.MunicipioGrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.dao.MunicipioGrupoRegiaoDAO;
import com.mercurio.lms.util.LongUtils;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.municipioGrupoRegiaoService"
 */
public class MunicipioGrupoRegiaoService extends CrudService<MunicipioGrupoRegiao, Long> {

	private GrupoRegiaoService grupoRegiaoService;
	
	@Override
	public MunicipioGrupoRegiao findById(java.lang.Long id) {
		return (MunicipioGrupoRegiao)super.findById(id);
	}
	
	@Override
	public Serializable store(MunicipioGrupoRegiao bean) {
			return super.store(bean);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {	
		return	super.findPaginated(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return super.getRowCount(criteria);
	}	
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	public void setMunicipioGrupoRegiaoDAO(MunicipioGrupoRegiaoDAO dao) {
		setDao(dao);
	}

	private MunicipioGrupoRegiaoDAO getMunicipioGrupoRegiaoDAO() {
		return (MunicipioGrupoRegiaoDAO) getDao();
	}	

	public void removeByIdGrupoRegiao(Long idGrupoRegiao) {		
		List<MunicipioGrupoRegiao> municipios = getMunicipioGrupoRegiaoDAO().findByIdGrupoRegiao(idGrupoRegiao);
		if(municipios != null) {
			List<Long> ids = new ArrayList<Long>();
			for(MunicipioGrupoRegiao municipio : municipios) {
				ids.add(municipio.getIdMunicipioGrupoRegiao());
			}
			this.removeByIds(ids);
		}		
	}
	
	public ResultSetPage<MunicipioGrupoRegiao> findPaginated(PaginatedQuery paginatedQuery) {
		return getMunicipioGrupoRegiaoDAO().findPaginated(paginatedQuery); 
		}

	public GrupoRegiao findGrupoRegiaoAtendimento(Long idTabelaPreco, Long idMunicipio){
		
		Long idGrupoRegiao = null;
		
		List list = getMunicipioGrupoRegiaoDAO().findMunicipioGrupoRegiao(idTabelaPreco,idMunicipio);
		if(CollectionUtils.isNotEmpty(list)){
			idGrupoRegiao = LongUtils.getLong(list.get(0));
		}else{
			list = grupoRegiaoService.findGrupoRegiao(idTabelaPreco, idMunicipio);
			if(CollectionUtils.isNotEmpty(list)){
				idGrupoRegiao = LongUtils.getLong(list.get(0));
	}
		}
		
		if(idGrupoRegiao != null){
			return grupoRegiaoService.findById(idGrupoRegiao);
	}	
	
		return null; 
	}	
	
	public GrupoRegiaoService getGrupoRegiaoService() {
		return grupoRegiaoService;
	}

	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}
	}
