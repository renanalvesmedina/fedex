package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalClienteDestinatario;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.ServicoAdicionalClienteDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.vendas.servicoAdicionalClienteService"
 */
public class ServicoAdicionalClienteService extends CrudService<ServicoAdicionalCliente, Long> {
	private ConfiguracoesFacade configuracoesFacade;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private SimulacaoService simulacaoService;
	private ServicoAdicionalClienteDestinatarioService servAdicCliDestService;

	public NumberFormat getPercentFormat() {
		NumberFormat percentFormat = null;
		if(percentFormat == null) {
			percentFormat = NumberFormat.getPercentInstance(LocaleContextHolder.getLocale());
			percentFormat.setMinimumFractionDigits(2);
			percentFormat.setMaximumFractionDigits(4);
		}
		return percentFormat;
	}

	public NumberFormat getCurrencyFormat() {
		NumberFormat currencyFormat = null;
		if(currencyFormat == null) {
			currencyFormat = NumberFormat.getCurrencyInstance(LocaleContextHolder.getLocale());
		}
		return currencyFormat;
	}

	/**
	 * Recupera uma instância de <code>ServicoAdicionalCliente</code>
	 * a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ServicoAdicionalCliente findById(java.lang.Long id) {
		return (ServicoAdicionalCliente) super.findById(id);
	}

	public ResultSetPage findPaginated(Map criteria) {
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = ((ServicoAdicionalClienteDAO)getDao()).findCustomPaginated(criteria, fd);
		rsp.setList(formatPaginatedList(rsp.getList()));
		return rsp;
	}
	
	public ResultSetPage findPaginatedByProposta(TypedFlatMap criteria) {
		ResultSetPage rsp = getServicoAdicionalClienteDAO().findPaginatedByProposta(criteria);
		formatList(rsp.getList());
		return rsp;
	}

	public Integer getRowCountByProposta(TypedFlatMap criteria) {
		return getServicoAdicionalClienteDAO().getRowCountByProposta(criteria);
	}
	
	private void formatList(List<ServicoAdicionalCliente> list) {
		for (ServicoAdicionalCliente servicoAdicionalCliente : list) {
			String tpIndicador = servicoAdicionalCliente.getTpIndicador().getValue();
			if ("T".equals(tpIndicador)) {
				servicoAdicionalCliente.setVlValorFormatado("0,00");
			} else if ("V".equals(tpIndicador)) {
				servicoAdicionalCliente.setVlValorFormatado(servicoAdicionalCliente.getTabelaDivisaoCliente().getTabelaPreco().getMoeda().getSiglaSimbolo() + " " +new DecimalFormat("###,###,###,###,##0.00").format(servicoAdicionalCliente.getVlValor()));
			} else {
				servicoAdicionalCliente.setVlValorFormatado(this.getPercentFormat().format(servicoAdicionalCliente.getVlValor().divide(new BigDecimal(100), 4, BigDecimal.ROUND_FLOOR)));
			}
		}
	}
	
	private List<ServicoAdicionalCliente> formatPaginatedList(List<Map<String, Object>> list) {
		
		List<ServicoAdicionalCliente> servicosAdicionaisCliente = new ArrayList<ServicoAdicionalCliente>();
		
		for (Map<String, Object> mapServicoAdicionalCliente : list) {
			
			ServicoAdicionalCliente servicoAdicionalCliente = (ServicoAdicionalCliente)mapServicoAdicionalCliente.get("servicoAdicionalCliente");
			String tpIndicador = servicoAdicionalCliente.getTpIndicador().getValue();
			
			if ("T".equals(tpIndicador)) {
				servicoAdicionalCliente.setVlValorFormatado("0,00");
			} else if ("V".equals(tpIndicador)) {
				servicoAdicionalCliente.setVlValorFormatado(servicoAdicionalCliente.getTabelaDivisaoCliente().getTabelaPreco().getMoeda().getSiglaSimbolo() + " " +new DecimalFormat("###,###,###,###,##0.00").format(servicoAdicionalCliente.getVlValor()));
			} else {
				servicoAdicionalCliente.setVlValorFormatado(this.getPercentFormat().format(servicoAdicionalCliente.getVlValor().divide(new BigDecimal(100), 4, BigDecimal.ROUND_FLOOR)));
			}
			
			// NmParcelaPreco
			servicoAdicionalCliente.getParcelaPreco().setNmParcelaPreco((VarcharI18n)mapServicoAdicionalCliente.get("nmParcelaPreco"));
			
			// Adiciona o nº da proposta
			if(mapServicoAdicionalCliente.get("idProposta") != null) {
				servicoAdicionalCliente.setIdProposta((Long)mapServicoAdicionalCliente.get("idProposta"));
			}
			
			servicosAdicionaisCliente.add(servicoAdicionalCliente);
		}
		
		return servicosAdicionaisCliente;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	public void removeByIdCascade(Long id) {
		List<ServicoAdicionalClienteDestinatario> destinatarios = servAdicCliDestService.findByIdServicoAdicionalCliente(id);
		List<Long> idsToRemove = new ArrayList<Long>();
		
	    for (ServicoAdicionalClienteDestinatario destinatario : destinatarios) {
	    	if(destinatario.getIdServicoAdicionalClienteDestinatario() != null) {
	    		idsToRemove.add(destinatario.getIdServicoAdicionalClienteDestinatario());
	    	}	    	
	    }	    	   	    
				
		servAdicCliDestService.removeByIds(idsToRemove);
		removeById(id);
	}
	
	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public void removeByIdsCascade(List<Long> ids) {
		for(Long id : ids) {
			this.removeByIdCascade(id);
		}
	}

	/**
	 * Remove os Serviços Adicionais do Cliente que estao associadas a simulacao porem nao podem
	 * devido a alteração de tabela de preco.
	 * 
	 * @param idTabelaPrecoNova
	 * @param idTabelaPrecoAntiga
	 * @param idSimulacao
	 */
	public void removeByTabelasPreco(Long idTabelaPrecoNova, Long idTabelaPrecoAntiga, Long idSimulacao) {
		List<Long> parcelaPrecos = tabelaPrecoParcelaService.findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(idTabelaPrecoNova, idTabelaPrecoAntiga);
		List<Long> servicosAdicionais = getServicoAdicionalClienteDAO().findIdsByTabelasPrecoIdSimulacao(idSimulacao, parcelaPrecos);
		if (servicosAdicionais != null && !servicosAdicionais.isEmpty()) {
			removeByIds(servicosAdicionais);
		}
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeByIdProposta(java.lang.Long id) {
		validateRemoveByPropostas(id);
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsPropostas(List<Long> ids) {
		validateRemoveByPropostas(ids.get(0));
		super.removeByIds(ids);
	}

	private void validateRemoveByPropostas(Long idServicoAdicionalCliente) {
		ServicoAdicionalCliente servicoAdicionalCliente = findById(idServicoAdicionalCliente);
		validatePropostas(servicoAdicionalCliente.getSimulacao().getIdSimulacao());
	}

	private void validatePropostas(Long idSimulacao) {
		Simulacao simulacao = simulacaoService.findById(idSimulacao);
		/** Verificar se a filial da proposta é a mesma do usuário */
		if(!simulacao.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
			throw new BusinessException("LMS-30035");
		}
		/** Verificar se a proposta já foi efetivada */
		if(Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
			throw new BusinessException("LMS-30036");
		}
		/** Atualizar a pendência de aprovação da proposta */
		simulacaoService.storePendenciaAprovacaoProposta(simulacao,false);
	}

	protected ServicoAdicionalCliente beforeStore(ServicoAdicionalCliente bean) {
		ServicoAdicionalCliente servicoAdicionalCliente = (ServicoAdicionalCliente) bean;
		/** Valida Proposta Cliente */
		if(servicoAdicionalCliente.getSimulacao() != null) {
			validatePropostas(servicoAdicionalCliente.getSimulacao().getIdSimulacao());
		}

		/** Valida Indicador e Valores */
		String tpIndicador = servicoAdicionalCliente.getTpIndicador().getValue();
		if (!"T".equals(tpIndicador)) {
			double vlValor = servicoAdicionalCliente.getVlValor().doubleValue();
			if ("V".equals(tpIndicador) || "A".equals(tpIndicador)) {
				// indicador = Valor
				if (vlValor < 0) {
					throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem("valor")});
				}
			} else if ("D".equals(tpIndicador)) {
				// indicador = Desconto ou indicador = Acrescimo
				if ((vlValor < 0) || (vlValor > 100)) {
					throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem("valor")});
				}
			}

			if(servicoAdicionalCliente.getVlMinimo() == null) {
				servicoAdicionalCliente.setVlMinimo(BigDecimal.ZERO);
			}
			if (servicoAdicionalCliente.getVlMinimo().doubleValue() < 0) {
				throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem("valorMinimo")});
			}
		}

		/** Valida Quantidade Minima de Dias */
		if(servicoAdicionalCliente.getNrQuantidadeDias() != null) {
			int nrDias = servicoAdicionalCliente.getNrQuantidadeDias().intValue();
			if (nrDias < 0) {
				throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem("quantidadeDias")});
			}
		}

		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ServicoAdicionalCliente bean) {
		return super.store(bean);
	}

	public Serializable storeCascade(ServicoAdicionalCliente bean, 
			List<ServicoAdicionalClienteDestinatario> destinatariosToSave, 
			List<ServicoAdicionalClienteDestinatario> destinatariosToDelete) {
		
		Serializable s = this.store(bean);
		
		List<Long> idsToDelete = new ArrayList<Long>();
		
		for (ServicoAdicionalClienteDestinatario destinatario : destinatariosToDelete) {
			if(destinatario.getIdServicoAdicionalClienteDestinatario() != null) {
				idsToDelete.add(destinatario.getIdServicoAdicionalClienteDestinatario());				
			}
		}
		
		servAdicCliDestService.removeByIds(idsToDelete);
		
		for(ServicoAdicionalClienteDestinatario destinatario : destinatariosToSave) {
			destinatario.setServicoAdicionalCliente(bean);
		}
		
		servAdicCliDestService.storeAll(destinatariosToSave);
				
		return s;
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		return getServicoAdicionalClienteDAO().getRowCountByIdCotacao(idCotacao);
	}

	public List<Map<String, Object>> findServicosByIdCotacao(Long idCotacao) {
		return getServicoAdicionalClienteDAO().findByIdCotacao(idCotacao);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Claiton Grings
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param idParcelaPreco
	 * @return
	 */
	public ServicoAdicionalCliente findServicoAdicionalCliente(Long idTabelaDivisaoCliente, Long idParcelaPreco) {
		return getServicoAdicionalClienteDAO().findServicoAdicionalCliente(idTabelaDivisaoCliente, idParcelaPreco);
	}

	/**
	 * Método utilizado pela Integração - CQPRO00007501
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public void removeByTabelasDivisaoCliente(List<Long> idsTabelaDivisaoCliente) {
		getServicoAdicionalClienteDAO().removeByTabelaDivisaoCliente(idsTabelaDivisaoCliente);
	}

	public List<ServicoAdicionalCliente> findByTabelaDivisaoCliente(Long idTabelaPreco, Long idDivisaoCliente) {
		return findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, null);
	}
	
	public List<ServicoAdicionalCliente> findByTabelaDivisaoCliente(Long idTabelaPreco, Long idDivisaoCliente, String cdParcelaPreco) {
		return getServicoAdicionalClienteDAO().findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, cdParcelaPreco);
	}

	public List<ServicoAdicionalCliente> findByTabelaSimulacaoCliente(Long idSimulacao) {
		return getServicoAdicionalClienteDAO().findByTabelaSimulacaoCliente(idSimulacao);
	}
	
	public ServicoAdicionalCliente findUniqueByTabelaDivisaoCliente(Long idTabelaPreco, Long idDivisaoCliente, String cdParcelaPreco) {
		List<ServicoAdicionalCliente> listaServicos = getServicoAdicionalClienteDAO().
				findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, cdParcelaPreco);
		
		if(listaServicos != null && listaServicos.size() > 0) {
			return listaServicos.get(0);
		} else {
			return null;
		}		
	}
	
	public Map<String, Integer> findQtDiasCarenciaAndQtDiasDecursoByCdParcelaPreco(Long idTabelaPreco, Long idDivisaoCliente, String cdParcelaPreco) {
		List<ServicoAdicionalCliente> lista = findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, cdParcelaPreco);
		ServicoAdicionalCliente sac = null;
		
		if(lista == null || lista.size() == 0) {
			return null;
		}
		
		sac = lista.get(0);
		Map<String, Integer> retorno = new HashMap<String, Integer>();
		retorno.put("qtDiasCarencia", sac.getNrQuantidadeDias());
		retorno.put("qtDiasDecurso", sac.getNrDecursoPrazo());
		return retorno;		
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	/**
	 * Atribui o DAO responsável por tratar a persistência dos
	 * dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setServicoAdicionalClienteDAO(ServicoAdicionalClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private ServicoAdicionalClienteDAO getServicoAdicionalClienteDAO() {
		return (ServicoAdicionalClienteDAO) getDao();
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	public void setServAdicCliDestService(
			ServicoAdicionalClienteDestinatarioService servAdicCliDestService) {
		this.servAdicCliDestService = servAdicCliDestService;
	}

    public Boolean validateNegociacaoCliente(String cdParcela, DivisaoCliente divisaoCliente) {
        if(divisaoCliente!= null){
        	return getServicoAdicionalClienteDAO().hasNegociacaoServicoAdicionalCliente(cdParcela, divisaoCliente);
        }	
        return true;
    }
}