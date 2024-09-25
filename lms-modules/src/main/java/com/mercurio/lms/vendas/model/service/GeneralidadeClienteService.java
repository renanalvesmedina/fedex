package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.GeneralidadeClienteDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.generalidadeClienteService"
 */
public class GeneralidadeClienteService extends CrudService<GeneralidadeCliente, Long> {

	private SimulacaoService simulacaoService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;

	/**
	 * M�todo utilizado pela Integra��o - CQPRO00008642
	 * @author Andre Valadas
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public void removeByTabelaDivisaoCliente(List<Long> idsTabelaDivisaoCliente) {
		getGeneralidadeClienteDAO().removeByTabelaDivisaoCliente(idsTabelaDivisaoCliente);
	}

	/**
	 * Recupera uma inst�ncia de <code>GeneralidadeCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Map findByIdMap(Long id) {
        return getGeneralidadeClienteDAO().findGeneralidadeClienteById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * 
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(GeneralidadeCliente bean) {
        return super.store(bean);
    }

    /* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	protected GeneralidadeCliente beforeStore(GeneralidadeCliente bean) {
		validaValoresGeneralidades((GeneralidadeCliente) bean);
		return super.beforeStore(bean);
	}
	
	private void validaValoresGeneralidades(GeneralidadeCliente generalidadeCliente) {
    	String[] indicadores = new String[] {"V", "A"};
    	String tpIndicador = generalidadeCliente.getTpIndicador().getValue();
    	BigDecimal vlGeneralidade = generalidadeCliente.getVlGeneralidade();
    	validaDescontoValor(indicadores, tpIndicador, vlGeneralidade, "LMS-01075");
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setGeneralidadeClienteDAO(GeneralidadeClienteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private GeneralidadeClienteDAO getGeneralidadeClienteDAO() {
        return (GeneralidadeClienteDAO) getDao();
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	String param = criteria.getString("parametroCliente.idParametroCliente");
    	if(StringUtils.isNotBlank(param)) {
    		Long idParamentoCliente = Long.valueOf(param);
    		return getGeneralidadeClienteDAO().findPaginatedByParametroCliente(idParamentoCliente, fd);
    	}
    	return ResultSetPage.EMPTY_RESULTSET;
    }
 
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	String param = criteria.getString("parametroCliente.idParametroCliente");
    	if(StringUtils.isNotBlank(param)) {
    		Long idParamentoCliente = Long.valueOf(param);
    		return getGeneralidadeClienteDAO().getRowCountByParametroCliente(idParamentoCliente);
    	}
    	return Integer.valueOf(0);
    }
    
    public ResultSetPage findPaginatedByParametroClienteProposta(TypedFlatMap criteria) {
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	String param = criteria.getString("parametroCliente.idParametroCliente");
    	if(StringUtils.isNotBlank(param)) {
    		Long idParamentoCliente = Long.valueOf(param);
    		return getGeneralidadeClienteDAO().findPaginatedByParametroClienteProposta(idParamentoCliente, fd);
    	}
    	return ResultSetPage.EMPTY_RESULTSET;
    }
 
    
    public Integer getRowCountByParametroClienteProposta(TypedFlatMap criteria) {
    	String param = criteria.getString("parametroCliente.idParametroCliente");
    	if(StringUtils.isNotBlank(param)) {
    		Long idParamentoCliente = Long.valueOf(param);
    		return getGeneralidadeClienteDAO().getRowCountByParametroClienteProposta(idParamentoCliente);
    	}
    	return Integer.valueOf(0);
    }

    public List findByIdParametroCliente(Long idParametroCliente) {
		return getGeneralidadeClienteDAO().findByIdParametroCliente(idParametroCliente);
	}
    
    public Simulacao storeProposta(GeneralidadeCliente generalidadeCliente, Long idSimulacao) {
    	boolean atualizaPendencia = true;
    	if (generalidadeCliente.getIdGeneralidadeCliente() != null) {
    		atualizaPendencia = needAtualizacaoPendencia(generalidadeCliente);
    	}
    	
    	store(generalidadeCliente);
    	Simulacao simulacao = getSimulacaoService().findById(idSimulacao);
    	
    	if (atualizaPendencia) {
    		getSimulacaoService().storePendenciaAprovacaoProposta(simulacao, false);
    	}
    	return simulacao;
    }
    
    private boolean needAtualizacaoPendencia(GeneralidadeCliente generalidadeCliente) {
    	boolean result = false;
    	GeneralidadeCliente persistida = (GeneralidadeCliente) super.findById(generalidadeCliente.getIdGeneralidadeCliente());
    	
    	if (!persistida.getParametroCliente().equals(generalidadeCliente.getParametroCliente()) ||
    		!persistida.getParcelaPreco().equals(generalidadeCliente.getParcelaPreco()) ||
    		!persistida.getTpIndicador().equals(generalidadeCliente.getTpIndicador()) ||
    		CompareUtils.ne(persistida.getVlGeneralidade(), generalidadeCliente.getVlGeneralidade())) {
    		result = true;
    	}
    	
    	getGeneralidadeClienteDAO().getSessionFactory().getCurrentSession().evict(persistida);
    	
    	return result;
    }
    
    /**
     * Remove as generalidades que estao associadas a simulacao porem nao podem
     * devido a altera��o de tabela de preco.
     * 
     * @param idTabelaPrecoNova
     * @param idTabelaPrecoAntiga
     * @param idSimulacao
     */
    public void removeByTabelasPreco(Long idTabelaPrecoNova, Long idTabelaPrecoAntiga, Long idSimulacao) {
    	List parcelaPrecos = tabelaPrecoParcelaService.findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(idTabelaPrecoNova, idTabelaPrecoAntiga);
    	List generalidades = getGeneralidadeClienteDAO().findIdsByTabelasPrecoIdSimulacao(idSimulacao, parcelaPrecos);
    	if (generalidades != null && !generalidades.isEmpty()) {
    		removeByIds(generalidades);
    	}
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
	
	public List findByIdParametroClienteProposta(Long idParametroCliente) {
		return getGeneralidadeClienteDAO().findByIdParametroClienteProposta(idParametroCliente);
	}

	/**
	 * @param tabelaPrecoParcelaService The tabelaPrecoParcelaService to set.
	 */
	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}

    
}