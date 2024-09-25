package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.dao.TaxaClienteDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.taxaClienteService"
 */
public class TaxaClienteService extends CrudService<TaxaCliente, Long> {
	
	private SimulacaoService simulacaoService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;

	/**
	 * Método utilizado pela Integração - CQPRO00008642
	 * @author Andre Valadas
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public void removeByTabelaDivisaoCliente(List<Long> idsTabelaDivisaoCliente) {
		getTaxaClienteDAO().removeByTabelaDivisaoCliente(idsTabelaDivisaoCliente);
	}

	/**
	 * Recupera uma instância de <code>TaxaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Map findByIdMap(java.lang.Long id) {
    	return getTaxaClienteDAO().findTaxaClienteById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
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
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
    
    protected TaxaCliente beforeStore(TaxaCliente bean) {
    	TaxaCliente tc = (TaxaCliente)bean;
    	if(tc.getPsMinimo() == null)
    		tc.setPsMinimo(new BigDecimal(0));
    	if(tc.getVlExcedente() == null)
    		tc.setVlExcedente(new BigDecimal(0));
    	if(tc.getVlTaxa() == null)
    		tc.setVlTaxa(new BigDecimal(0));
    	
    	validaValoresTaxas(tc);
    	
    	return super.beforeStore(bean);
    }
    
    private void validaValoresTaxas(TaxaCliente taxaCliente) {
    	// 5.13 e 5.14
    	String[] indicadores = new String[] {"A", "V"};
    	String tpTaxaIndicador = taxaCliente.getTpTaxaIndicador().getValue();
    	BigDecimal vlTaxa = taxaCliente.getVlTaxa();
    	validaDescontoValor(indicadores, tpTaxaIndicador, vlTaxa, "LMS-01075");
    	// 5.15
    	BigDecimal psMinimo = taxaCliente.getPsMinimo();
		if (!CompareUtils.ge(psMinimo, BigDecimalUtils.ZERO)) {
			throw new BusinessException("LMS-01076");
		}
    	// 5.16
		BigDecimal vlExcedente = taxaCliente.getVlExcedente();
		if (!CompareUtils.ge(vlExcedente, BigDecimalUtils.ZERO)) {
			throw new BusinessException("LMS-01077");
		}
    }
    
    private void validaDescontoValor(String[] indicadores, String indicador, BigDecimal valor, String alerta) {
    	boolean maiorIgual = false;
    	for (int i = 0; i < indicadores.length; i++) {
    		if (indicador.equals(indicadores[i])) {
    			maiorIgual = true;
    			break;
    		}
    	}
    	if (maiorIgual) {
    		if (!CompareUtils.ge(valor, BigDecimalUtils.ZERO)) {
    			throw new BusinessException(alerta);
    		}
    	} else if (indicador.equals("D")) {
    		if (!CompareUtils.between(valor, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
    			throw new BusinessException(alerta);
    		}
    	}
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TaxaCliente bean) {
        return super.store(bean);
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	Long idParamentoCliente = criteria.getLong("parametroCliente.idParametroCliente");
    	if(idParamentoCliente != null)
    		return getTaxaClienteDAO().findPaginatedByParametroCliente(idParamentoCliente, fd);
    	return ResultSetPage.EMPTY_RESULTSET;
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	Long idParamentoCliente = criteria.getLong("parametroCliente.idParametroCliente");
    	if(idParamentoCliente != null)
    		return getTaxaClienteDAO().getRowCountByParametroCliente(idParamentoCliente);
    	return Integer.valueOf(0);
    }
    
    public ResultSetPage findPaginatedByParametroClienteProposta(TypedFlatMap criteria) {
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	Long idParamentoCliente = criteria.getLong("parametroCliente.idParametroCliente");
    	if(idParamentoCliente != null)
    		return getTaxaClienteDAO().findPaginatedByParametroClienteProposta(idParamentoCliente, fd);
    	return ResultSetPage.EMPTY_RESULTSET;
    }
    
    public Integer getRowCountByParametroClienteProposta(TypedFlatMap criteria) {
    	Long idParamentoCliente = criteria.getLong("parametroCliente.idParametroCliente");
    	if(idParamentoCliente != null)
    		return getTaxaClienteDAO().getRowCountByParametroClienteProposta(idParamentoCliente);
    	return Integer.valueOf(0);
    }
    
    public Simulacao storeProposta(TaxaCliente taxaCliente, Long idSimulacao) {
    	boolean atualizaPendencia = true;
    	if (taxaCliente.getIdTaxaCliente() != null) {
    		// e um update, devemos verificar se algum campo foi alterado
    		atualizaPendencia = needAtualizacaoPendencia(taxaCliente);
    	}
    	store(taxaCliente);
    	
    	Simulacao simulacao = getSimulacaoService().findById(idSimulacao);
    	
    	if (atualizaPendencia) {
    		getSimulacaoService().storePendenciaAprovacaoProposta(simulacao, false);
    	}
    	
    	return simulacao;
    }

    /**
     * Remove as taxas que estao associadas a simulacao porem nao podem
     * devido a alteração de tabela de preco.
     * 
     * @param idTabelaPrecoNova
     * @param idTabelaPrecoAntiga
     * @param idSimulacao
     */
    public void removeByTabelasPreco(Long idTabelaPrecoNova, Long idTabelaPrecoAntiga, Long idSimulacao) {
    	List parcelaPrecos = tabelaPrecoParcelaService.findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(idTabelaPrecoNova, idTabelaPrecoAntiga);
    	List taxas = getTaxaClienteDAO().findIdsByTabelasPrecoIdSimulacao(idSimulacao, parcelaPrecos);
    	if (taxas != null && !taxas.isEmpty()) {
    		removeByIds(taxas);
    	}
    }

    private boolean needAtualizacaoPendencia(TaxaCliente taxaCliente) {
    	boolean result = false;
    	TaxaCliente persistida = (TaxaCliente) super.findById(taxaCliente.getIdTaxaCliente());

    	if (!persistida.getParametroCliente().equals(taxaCliente.getParametroCliente()) ||
    		!persistida.getParcelaPreco().equals(taxaCliente.getParcelaPreco()) ||
    		!persistida.getTpTaxaIndicador().equals(taxaCliente.getTpTaxaIndicador()) ||
    		CompareUtils.ne(persistida.getVlTaxa(), taxaCliente.getVlTaxa()) ||
    		CompareUtils.ne(persistida.getPsMinimo(), taxaCliente.getPsMinimo()) ||
    		CompareUtils.ne(persistida.getVlExcedente(), taxaCliente.getVlExcedente())) {

    		result = true;
    	}

    	getTaxaClienteDAO().getSessionFactory().getCurrentSession().evict(persistida);
    	return result;
    }

    /*
     * Getters e setters
     */
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTaxaClienteDAO(TaxaClienteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TaxaClienteDAO getTaxaClienteDAO() {
        return (TaxaClienteDAO) getDao();
    }

	public List findByIdParametroCliente(Long idParametroCliente) {
		return getTaxaClienteDAO().findByIdParametroCliente(idParametroCliente);
	}

	/**
	 * @return Returns the simulacaoService.
	 */
	public SimulacaoService getSimulacaoService() {
		return simulacaoService;
	}

	/**
	 * @param simulacaoService The simulacaoService to set.
	 */
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}

	/**
	 * @param tabelaPrecoParcelaService The tabelaPrecoParcelaService to set.
	 */
	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
   }