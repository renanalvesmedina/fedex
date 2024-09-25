package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.PrecoFreteDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.precoFreteService"
 */
public class PrecoFreteService extends CrudService<PrecoFrete, Long> {
	private TabelaPrecoService tabelaPrecoService;
	private RotaPrecoService rotaPrecoService;
	private TarifaPrecoService tarifaPrecoService;
	private ParcelaPrecoService parcelaPrecoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Recupera uma inst�ncia de <code>PrecoFrete</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public PrecoFrete findById(java.lang.Long id) {
		return (PrecoFrete)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	List ids = new ArrayList(1);
    	ids.add(id);
    	validateTabelasPrecoEfetivadas(ids);
    	validateHistoricoWorkflow(id);
        super.removeById(id);
    }

	private void validateHistoricoWorkflow(Long id) {
		PrecoFrete precoFrete = this.findById(id);
		tabelaPrecoService.validatePendenciaWorkflow(precoFrete.getTabelaPrecoParcela().getTabelaPreco());
	}
    
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	validateTabelasPrecoEfetivadas(ids);

		/*
		 * Verifica se a TabelaPreco relacionada com PrecoFrete possui Workflow
		 * pendente de aprova��o. Como a TabelaPreco � igual para todos os
		 * PrecoFrete, realiza a valida��o somente para o primeiro id de
		 * PrecoFrete.
		 */
    	validateHistoricoWorkflow((Long)ids.get(0));
        super.removeByIds(ids);
    }

    /**
     * Valida se a tabela preco esta efetiva para os ids preco frete passados
     * 
     * @param ids
     */
    public void validateTabelasPrecoEfetivadas(List ids){
    	Integer count = getPrecoFreteDAO().getCountTabelaPrecoEfetuada(ids);
    	if(CompareUtils.gt(count, IntegerUtils.ZERO)) throw new BusinessException("LMS-30042");
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PrecoFrete bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setPrecoFreteDAO(PrecoFreteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private PrecoFreteDAO getPrecoFreteDAO() {
        return (PrecoFreteDAO) getDao();
    }
    
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FilterResultSetPage filterRs = new FilterResultSetPage(getPrecoFreteDAO()
				.findPaginated(criteria, FindDefinition.createFindDefinition(criteria))) {
			public Map filterItem(Object item) {
				PrecoFrete pf = (PrecoFrete)item;
				TypedFlatMap retorno = new TypedFlatMap();
				retorno.put("idPrecoFrete", pf.getIdPrecoFrete());
				TarifaPreco tp = pf.getTarifaPreco();
				if(tp != null)
					retorno.put("tarifaPreco.cdTarifaPreco", tp.getCdTarifaPreco());
				RotaPreco rp = pf.getRotaPreco();
				if(rp != null) {
					retorno.put("rotaPreco.idRotaPreco", rp.getIdRotaPreco());
					retorno.put("rotaPreco.origemString", rp.getOrigemString());
					retorno.put("rotaPreco.destinoString", rp.getDestinoString());
				}
				retorno.put("vlPrecoFrete", pf.getVlPrecoFrete());
				return retorno;
			}
		};
		return (ResultSetPage)filterRs.doFilter();
	}    

	public PrecoFrete findPrecoFrete(Long idTabelaPreco, String cdParcelaPreco, Long idTarifaPreco) {
		return getPrecoFreteDAO().findPrecoFrete(idTabelaPreco, cdParcelaPreco, idTarifaPreco);
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public PrecoFrete findByIdTabelaPrecoParaMarkup(Long idTabelaPreco) {
		return getPrecoFreteDAO().findByIdTabelaPrecoParaMarkup(idTabelaPreco);
	}
	
	public PrecoFrete findPrecoFrete(Long idTabelaPreco, Long idParcelaPreco, Long idRotaPreco, Long idTarifaPreco) {
		PrecoFrete precoFrete = getPrecoFreteDAO().findPrecoFrete(idTabelaPreco, idParcelaPreco, idRotaPreco, idTarifaPreco);
		
		if(precoFrete == null){
			StringBuilder msg = new StringBuilder();
			
			List<String> params = new ArrayList<String>();
			ParcelaPreco parcelaPreco = parcelaPrecoService.findById(idParcelaPreco);
			params.add(parcelaPreco.getNmParcelaPreco().getValue());
			
			if(idRotaPreco != null){
				params.add(configuracoesFacade.getMensagem("rota"));
				
				RotaPreco rotaPreco = rotaPrecoService.findById(idRotaPreco);
				msg.append(rotaPreco.getOrigemString());
				msg.append(" > "); 
				msg.append(rotaPreco.getDestinoString());
				
			} else if(idTarifaPreco != null){
				params.add(configuracoesFacade.getMensagem("tarifa"));
				
				TarifaPreco tarifaPreco = tarifaPrecoService.findById(idTarifaPreco);
				msg.append(tarifaPreco.getCdTarifaPreco());
			}
			
			params.add(msg.toString());

			throw new BusinessException("LMS-30082", params.toArray());
		}
		
		return precoFrete;
	}

	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}

	public void setTarifaPrecoService(TarifaPrecoService tarifaPrecoService) {
		this.tarifaPrecoService = tarifaPrecoService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}