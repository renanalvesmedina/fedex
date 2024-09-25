package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.ItemLigacao;
import com.mercurio.lms.contasreceber.model.LigacaoCobranca;
import com.mercurio.lms.contasreceber.model.dao.LigacaoCobrancaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.ligacaoCobrancaService"
 */
public class LigacaoCobrancaService extends CrudService<LigacaoCobranca, Long> {

	private ItemLigacaoService itemLigacaoService;
	
	private CobrancaInadimplenciaService cobrancaInadimplenciaService;
	
	private ItemCobrancaService itemCobrancaService;
	
	public void setCobrancaInadimplenciaService(CobrancaInadimplenciaService cobrancaInadimplenciaService) {
		this.cobrancaInadimplenciaService = cobrancaInadimplenciaService;
	}
	
	public void setItemCobrancaService(ItemCobrancaService itemCobrancaService) {
		this.itemCobrancaService = itemCobrancaService;
	}

	/**
	 * Recupera uma instância de <code>LigacaoCobranca</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public java.io.Serializable findByIdMap(Long id) {
        return getLigacaoCobrancaDAO().findByIdMap(id);
    }

    public LigacaoCobranca findById(Long id) {
        return (LigacaoCobranca) super.findById(id);
    }
    
    public ResultSetPage findPaginatedByLigacaoCobranca(TypedFlatMap tfm) {
    	return getLigacaoCobrancaDAO().findPaginatedByLigacaoCobranca(tfm);
    }
    
    public Integer getRowCountByLigacaoCobranca(TypedFlatMap tfm) {
		return getLigacaoCobrancaDAO().getRowCountByLigacaoCobranca(tfm);
	}

	public List findComboFaturasInadimplencia(TypedFlatMap idCobrancaInadimplencia) {
		return getLigacaoCobrancaDAO().findComboFaturasInadimplencia(idCobrancaInadimplencia);
	}
    
	public List findFaturasByCobrancaInadimplencia(Long idCobrancaInadimplencia) {
		return getLigacaoCobrancaDAO().findFaturasByCobrancaInadimplencia(idCobrancaInadimplencia);
	}
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	LigacaoCobranca ligacaoCobranca = (LigacaoCobranca)findById(id);
    	
    	/** Valida se a cobrancaInadimplencia já está encerrada */
		if(ligacaoCobranca.getCobrancaInadimplencia().getBlCobrancaEncerrada().equals(Boolean.TRUE)){
			throw new BusinessException("LMS-36138");
		}
		
		if(ligacaoCobranca.getItemLigacoes()!= null && !ligacaoCobranca.getItemLigacoes().isEmpty()){
			for (Iterator iter = ligacaoCobranca.getItemLigacoes().iterator();iter.hasNext();){
				ItemLigacao itemligacao = (ItemLigacao)iter.next();
				getItemLigacaoService().removeById(itemligacao.getIdItemLigacao());
			}
			ligacaoCobranca.getItemLigacoes().clear();
		}
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	for(int i=0; i< ids.size(); i++){
    		Long id = (Long)ids.get(i);
    		removeById(id);
    	}    	
        //super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param ligacaoCobranca entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TypedFlatMap tfm) {
    	
    	LigacaoCobranca ligacaoCobranca = new LigacaoCobranca();
    	CobrancaInadimplencia cobrancaInadimplencia = cobrancaInadimplenciaService.findById(tfm.getLong("cobrancaInadimplencia.idCobrancaInadimplencia"));
    	
	    	/** Valida se a cobrancaInadimplencia já está encerrada */
    	if(cobrancaInadimplencia.getBlCobrancaEncerrada().equals(Boolean.TRUE)){
				throw new BusinessException("LMS-36138");
			}
    	
    	if(tfm.getLong("idLigacaoCobranca") != null){
	    	ligacaoCobranca = (LigacaoCobranca)findById(tfm.getLong("idLigacaoCobranca"));
    	}
    	
		List itemLigacoes = null;

		// pega lista de faturas da pairedListbox na tela
		List lista = tfm.getList("itemLigacoes");
		
		ligacaoCobranca = new LigacaoCobranca();
		ligacaoCobranca.setIdLigacaoCobranca(tfm.getLong("idLigacaoCobranca"));

		if (lista != null) {
			
			itemLigacoes = new ArrayList();
		
			// itera a lista de faturas e adiciona em um objeto List que será setado no pojo LigacaoCobranca
			for (Iterator iter = lista.iterator(); iter.hasNext();) {
				Map map = (Map) iter.next();
				
				Long idItemCobranca = Long.valueOf(map.get("idItemCobranca").toString());
				
				ItemCobranca itemCobranca = itemCobrancaService.findById(idItemCobranca);
				
				ItemLigacao il = new ItemLigacao();
				il.setItemCobranca(itemCobranca);
				il.setLigacaoCobranca(ligacaoCobranca);
		
				// adiciona na lista
				itemLigacoes.add(il);
			}
		}
		
		TelefoneContato telefoneContato = new TelefoneContato();
		telefoneContato.setIdTelefoneContato(tfm.getLong("telefoneContato.idTelefoneContato"));
		
		ligacaoCobranca.setCobrancaInadimplencia(cobrancaInadimplencia); // pojo CobrancaInadimplencia
		ligacaoCobranca.setItemLigacoes(itemLigacoes); // List itemLigacoes -> pojo ItemLigacao 
		ligacaoCobranca.setTelefoneContato(telefoneContato); // pojo TelefoneContato
		ligacaoCobranca.setUsuario(SessionUtils.getUsuarioLogado()); // Seta o usuário que realizou a ligação (usuário da sessão).
		ligacaoCobranca.setDhLigacaoCobranca(tfm.getDateTime("dhLigacaoCobranca"));
		ligacaoCobranca.setDsLigacaoCobranca(tfm.getString("dsLigacaoCobranca"));
		ligacaoCobranca.setDsLigacaoCobranca(tfm.getString("dsLigacaoCobranca"));

    	final Long idLigacaoCobranca = ligacaoCobranca.getIdLigacaoCobranca();
		
    	LigacaoCobranca ligacaoCobrancaBanco = null;
    	
    	if(idLigacaoCobranca != null){
    		ligacaoCobrancaBanco = (LigacaoCobranca) findById(idLigacaoCobranca);
    		
    		ligacaoCobrancaBanco.setCobrancaInadimplencia(ligacaoCobranca.getCobrancaInadimplencia());
    		ligacaoCobrancaBanco.setTelefoneContato(ligacaoCobranca.getTelefoneContato());
    		
    		ligacaoCobrancaBanco.setDhLigacaoCobranca(ligacaoCobranca.getDhLigacaoCobranca());
    		ligacaoCobrancaBanco.setDsLigacaoCobranca(ligacaoCobranca.getDsLigacaoCobranca());
    		
    		ligacaoCobrancaBanco.setUsuario(ligacaoCobranca.getUsuario());
    		
			final List itemLigacoesBanco = ligacaoCobrancaBanco.getItemLigacoes();
			final List itemLigacoesTela = ligacaoCobranca.getItemLigacoes();
			
			// se a lista da tela estiver nula, limpa a lista carregada do banco
			if (itemLigacoesTela == null || itemLigacoesTela.isEmpty()) {
				itemLigacoesBanco.clear();
			} else {

				// se a lista nao estiver nula, percorremos e atualizamos os trechos excluidos na tela
				for (Iterator iterItemLigacoesBanco = itemLigacoesBanco.iterator(); iterItemLigacoesBanco.hasNext();) {

					ItemLigacao itemLigacaoBanco = (ItemLigacao) iterItemLigacoesBanco.next();
					Long idItemCobrancaBanco = itemLigacaoBanco.getItemCobranca().getIdItemCobranca();

					boolean excluirBanco = true;
					
					for (Iterator iterItemLigacoesTela = itemLigacoesTela.iterator(); iterItemLigacoesTela.hasNext();) {
						ItemLigacao itemLigacaoTela = (ItemLigacao)iterItemLigacoesTela.next();
						Long idItemCobrancaTela = itemLigacaoTela.getItemCobranca().getIdItemCobranca();
						
						if (idItemCobrancaTela.equals(idItemCobrancaBanco)) {
							// se o registro esta na lista da tela não remove do banco 
							excluirBanco = false;
							iterItemLigacoesTela.remove();
							break;
						}
					}
					
					if (excluirBanco) {
						iterItemLigacoesBanco.remove();
					}
				}
				
				// adiciona os registros de trechos novos na lista de trechos do banco
				// e seta o pai no trecho
				for (Iterator iterTela = itemLigacoesTela.iterator(); iterTela.hasNext();) {
					ItemLigacao novoItemLigacao = (ItemLigacao) iterTela.next();
					novoItemLigacao.setLigacaoCobranca(ligacaoCobrancaBanco);
					itemLigacoesBanco.add(novoItemLigacao);
				}
	
			}
		} else {
			// se o id for nulo salva o registro direto		
			List item = ligacaoCobranca.getItemLigacoes();
			
			if (item != null && !item.isEmpty()) {
				for (Iterator iterTela = item.iterator(); iterTela.hasNext();){
					ItemLigacao novoItemLigacao = (ItemLigacao)iterTela.next();
					novoItemLigacao.setLigacaoCobranca(ligacaoCobranca);
				}
			}
			ligacaoCobrancaBanco = ligacaoCobranca;
		}
		return super.store(ligacaoCobrancaBanco);
    }

	public ItemLigacaoService getItemLigacaoService() {
		return itemLigacaoService;
	}

	public void setItemLigacaoService(ItemLigacaoService itemLigacaoService) {
		this.itemLigacaoService = itemLigacaoService;
	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setLigacaoCobrancaDAO(LigacaoCobrancaDAO dao) {
        setDao( dao );
    }
    
    
    
    
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public LigacaoCobrancaDAO getLigacaoCobrancaDAO() {
        return (LigacaoCobrancaDAO) getDao();
    }
    
    /**
     * Busca os dados da última Ligação Cobrança de acordo com a cobrança inadimplência
     * @param tfm Map contendo o Identificador da Cobrança Inadimplência
     * @return A última ligação cobrança de acordo com a cobrança inadimplência
     */
    public LigacaoCobranca findDadosLigacaoCobrancaByCobrancaInadimplencia(TypedFlatMap tfm){
        return getLigacaoCobrancaDAO().findDadosLigacaoCobrancaByCobrancaInadimplencia(tfm);
    }
    
    /**
     * Montagem da listbox das faturas existentes p/ a cobranca inadimplencia
     * @param tfm Critérios de pesquisa : idCobrancaInadimplencia ou ligacaoCobranca.idLigacaoCobranca
     * @return Lista de faturas
     */
    public List findFaturasInadimplenciaByAgendaCobranca(TypedFlatMap tfm){
        return getLigacaoCobrancaDAO().findFaturasInadimplenciaByAgendaCobranca(tfm);
    }

}