package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.dao.TarifaPrecoRotaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.tarifaPrecoRotaService"
 */
public class TarifaPrecoRotaService extends CrudService<TarifaPrecoRota, Long> {

	private TabelaPrecoService tabelaPrecoService;
	private RotaPrecoService rotaPrecoService;

	/**
	 * Recupera uma instância de <code>TarifaPrecoRota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    @Override
	public TarifaPrecoRota findById(java.lang.Long id) {
	  	return (TarifaPrecoRota)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
	public java.io.Serializable store(TarifaPrecoRota bean) {
        return super.store(bean);
    }

    
	public java.io.Serializable basicStore(TarifaPrecoRota bean) {
		getDao().store(bean);
		return getDao().getIdentifier(bean);
	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTarifaPrecoRotaDAO(TarifaPrecoRotaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TarifaPrecoRotaDAO getTarifaPrecoRotaDAO() {
        return (TarifaPrecoRotaDAO) getDao();
    }
    
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getTarifaPrecoRotaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		FilterResultSetPage filterRs = new FilterResultSetPage(rsp) {
			@Override
			public Map filterItem(Object item) {
				TarifaPrecoRota tpr = (TarifaPrecoRota)item;
				TypedFlatMap retorno = new TypedFlatMap();
				retorno.put("idTarifaPrecoRota", tpr.getIdTarifaPrecoRota());
				retorno.put("tabelaPreco.tabelaPrecoString", tpr.getTabelaPreco().getTabelaPrecoString());
				retorno.put("tarifaPreco.cdTarifaPreco", tpr.getTarifaPreco().getCdTarifaPreco());
				retorno.put("rotaPreco.origemString", tpr.getRotaPreco().getOrigemString());
				retorno.put("rotaPreco.destinoString", tpr.getRotaPreco().getDestinoString());
				return retorno;
			}
		};
		return (ResultSetPage)filterRs.doFilter();
	}
	
	public List findByIdTabelaPreco(Long idTabelaPreco) {
		return getTarifaPrecoRotaDAO().findByIdTabelaPreco(idTabelaPreco);
	}
	
	@Override
	protected TarifaPrecoRota beforeStore(TarifaPrecoRota bean) {
		validate(bean, null);
		
		return super.beforeStore(bean);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		/*
		 * Verifica se a TabelaPreco relacionada com TarifaPrecoRota possui
		 * Workflow pendente de aprovação. Como a TabelaPreco é igual para todos
		 * os TarifaPrecoRota, realiza a validação somente para o primeiro id de
		 * TarifaPrecoRota.
		 */
		beforeRemoveById((Long)ids.get(0));
		super.beforeRemoveByIds(ids);
	}
	
	@Override
	protected void beforeRemoveById(Long id) {
		TarifaPrecoRota tarifaPrecoRota = findById(id);
		//Verifica se a tabela de preço está efetivada, não podendo então excluir.
		getTabelaPrecoService().validateEfetivadas(tarifaPrecoRota.getTabelaPreco().getIdTabelaPreco());
		validateHistoricoWorkflow(id);
		super.beforeRemoveById(id);
	}
	
	public void validate(TarifaPrecoRota bean, Long idTabelaBase) {
		TarifaPrecoRota tarifaPrecoRota = bean;
		TypedFlatMap tabelaPreco = tabelaPrecoService.findByIdMap(tarifaPrecoRota.getTabelaPreco().getIdTabelaPreco());
		RotaPreco rotaPreco = rotaPrecoService.findById(tarifaPrecoRota.getRotaPreco().getIdRotaPreco());
		String tpSubtipoTabelaPreco = tabelaPreco.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco");
		if ("F".equals(tpSubtipoTabelaPreco)) {
			if (rotaPreco.getTipoLocalizacaoMunicipioComercialOrigem() == null) {
				throw new BusinessException("LMS-30055");
			}
		} else if (!"P".equals(tpSubtipoTabelaPreco) ){
			if (rotaPreco.getTipoLocalizacaoMunicipioComercialOrigem() != null) {
				throw new BusinessException("LMS-30056");
			}
		}
		
		/*Verifica grupo regiao*/
		GrupoRegiao origem  = rotaPreco.getGrupoRegiaoOrigem(); 
		GrupoRegiao destino = rotaPreco.getGrupoRegiaoDestino(); 
		if (idTabelaBase == null) {
			TabelaPreco tabelaInformada = tarifaPrecoRota.getTabelaPreco();
			idTabelaBase = tabelaInformada.getIdTabelaPreco();
		}
				
		if (origem != null) { 
			if (!idTabelaBase.equals(origem.getTabelaPreco().getIdTabelaPreco())) {
				throw new BusinessException("LMS-30064");
			}
		}		
		
		if (destino != null) {
			if (!idTabelaBase.equals(destino.getTabelaPreco().getIdTabelaPreco())) {
				throw new BusinessException("LMS-30064");
	}
		}
	}

	/**
	 * Obtem todas as tarifas da tabela preço através de informações de
	 * origem e destino
	 * 
	 * @param 	idTabelaPreco
	 * @param 	restricaoRotaOrigem
	 * @param 	restricaoRotaDestino
	 * @return	List<TarifaPrecoRota>
	 */
	public List<TarifaPrecoRota> findTarifaPrecoRota(Long idTabelaPreco, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		return getTarifaPrecoRotaDAO().findTarifaPrecoRota(idTabelaPreco, restricaoRotaOrigem, restricaoRotaDestino);
	}	
	
	private void validateHistoricoWorkflow(Long id) {
		TarifaPrecoRota tarifaPrecoRota = this.findById(id);
		tabelaPrecoService.validatePendenciaWorkflow(tarifaPrecoRota.getTabelaPreco());
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}

	public TarifaPrecoRota findByTarifaRota(Long idTabelaPreco,	Long idTarifaPreco, Long idRotaPreco) {
		return getTarifaPrecoRotaDAO().findByTarifaRota(idTabelaPreco, idTarifaPreco, idRotaPreco);
	}

	public List<TarifaPrecoRota> findByTabelaETarifa(Long idTabelaPreco, Long idTarifa) {
		return getTarifaPrecoRotaDAO().findByTabelaETarifa(idTabelaPreco, idTarifa);
	}

	public void removeByTabelaPreco(Long idTabelaPreco) {
		this.getTarifaPrecoRotaDAO().removeByIdTabelaPreco(idTabelaPreco);		
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}
}