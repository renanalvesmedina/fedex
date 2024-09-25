/**
 * 
 */
package com.mercurio.lms.expedicao.swt.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoDevolucaoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DigitarNotaService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.CotacaoService;

/**
 * Copia da action de igual nome utilizada na web, porém esta versão é para
 * ser utilizada com os mecanismos de tela do SWT.
 * 
 * @author Luis Carlos Poletto
 */
public class DigitarNotaAction extends CrudAction {
	protected ServicoService servicoService;
	protected ConfiguracoesFacade configuracoesFacade;
	protected NaturezaProdutoService naturezaProdutoService;
	protected CotacaoService cotacaoService;
	protected ProdutoEspecificoService produtoEspecificoService;
	protected AeroportoService aeroportoService;
	protected EmpresaService empresaService;
	protected FilialService filialService;
	protected PpeService ppeService;
	protected ConhecimentoService conhecimentoService;
	protected DigitarNotaService digitarNotaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private DoctoServicoService doctoServicoService;
	private ConhecimentoDevolucaoService conhecimentoDevolucaoService;

	public Map findServico() {
		List servicos = servicoService.findByTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		if (servicos != null && !servicos.isEmpty()) {
			for (int i = 0; i < servicos.size(); i++) {
				Map servico = (Map) servicos.get(i);
				Map modal = (Map) servico.remove("tpModal");
				Map abrangencia = (Map) servico.remove("tpAbrangencia");
				
				servico.put("dsServico", ((VarcharI18n) servico.remove("dsServico")).getValue());
				servico.put("tpModal", modal.get("value"));
				servico.put("tpAbrangencia", abrangencia.get("value"));
			}
		}
		Map map = new HashMap(2);
		map.put("servicos", servicos);
		map.put("idServicoPadrao", configuracoesFacade.getValorParametro(Servico.SERVICO_PADRAO));
		return map;
	}

	public void validateNrNotasFiscais(Integer nrNotas){
		if(nrNotas != null) {
			BigDecimal nrGeral = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.LIMITE_NFS_CTRC);
			if(nrNotas.intValue() > nrGeral.intValue())
				throw new BusinessException("LMS-04044");
		}
		
		Integer maxNotas = filialService.findMaxNotasCtrcOrigem(SessionUtils.getFilialSessao().getIdFilial());
		if (maxNotas > 0) {
			if (nrNotas > maxNotas) {
				throw new BusinessException("LMS-04382", new Object[] {maxNotas.toString()});
	}
		}
		
	}

	public List findNaturezaProduto() {
		return naturezaProdutoService.findAllAtivo();
	}

	public List findDensidade() {
		return digitarNotaService.findDensidade();
	}

	public List findCotacao(Long idCliente, String tpDocumento) {
		List result = cotacaoService.findCotacoes(idCliente, tpDocumento);
		for(Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			map.put("nrCotacao", map.remove("sgFilial") + "-" + map.get("nrCotacao") + "-" + map.get("nmClienteDestino"));
		}
		return result;
	}

	public List findProdutoEspecifico() {
		return produtoEspecificoService.findAllAtivo();
	}

	public List findEmpresaParceira() {
		List empresas = empresaService.findByTpEmpresaTpSituacao("P", "A");
		if (empresas != null && !empresas.isEmpty()) {
			for (int i = 0; i < empresas.size(); i++) {
				Map empresa = (Map) empresas.get(i);
				Map pessoa = (Map) empresa.remove("pessoa");
				empresa.put("nmPessoaEmpresaCooperada", pessoa.get("nmPessoa"));
				String nrIdentificacao = FormatUtils.formatIdentificacao("CNPJ", (String) pessoa.get("nrIdentificacao"));
				empresa.put("nrIdentificacaoEmpresaCooperada", nrIdentificacao);
			}
		}
		return empresas;
	}

	public List findDoctoServico(Map criteria) {
		Long nrConhecimento = LongUtils.getLong(criteria.get("nrConhecimento"));
		Long idFilialOrigem = (Long) criteria.get("idFilial");
		String tpConhecimento = (String) criteria.get("tpConhecimento");
		
		String tpDocumentoServico = (String) criteria.get("tpDocumentoServico");
		if(tpDocumentoServico == null){
			tpDocumentoServico = ConstantesExpedicao.CONHECIMENTO_NACIONAL;
		}
		
		/* CQPRO00028986 - Ajuste para pegar conhecimentos com status diferente de Cancelado e Pré-conhecimento */
		List<Map<String, Object>> result = conhecimentoService.findByNrConhecimentoIdFilialOrigem(nrConhecimento, idFilialOrigem, "", tpDocumentoServico);
		if(result.isEmpty() || result.size() > 1) {
			return null;
		} else {
			Map ctrc = result.get(0);		
			long idDoctoServico = (Long) ctrc.get("idDoctoServico");
			
			String tpSituacaoConhecimento = (String)((Map)ctrc.get("tpSituacaoConhecimento")).get("value"); 
			if(tpSituacaoConhecimento.equals("C") || tpSituacaoConhecimento.equals("P"))
				return null;						
			
			boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(idDoctoServico);
			ctrc.put("isEntregaParcial", isEntregaParcial);
			
	        if(ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(tpConhecimento) && isEntregaParcial){
	            DoctoServico doctoOriginal = doctoServicoService.findDoctoServicoById(idDoctoServico);
	            
	            if (!ConstantesExpedicao.CD_MERCADORIA_NO_TERMINAL.equals(doctoOriginal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())&&
                    !ConstantesExpedicao.CD_MERCADORIA_RETORNADA_NO_TERMINAL.equals(doctoOriginal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
                    throw new BusinessException("LMS-04373");
                }
	            
	            Map<String, Boolean> validacaoNF = conhecimentoDevolucaoService.validateExisteOcorrenciaEntregaNF(idDoctoServico);
	            StringBuilder msg = new StringBuilder();
	            msg.append(doctoOriginal.getTpDocumentoServico().getValue());
	            msg.append(" ");
	            msg.append(doctoOriginal.getFilialByIdFilialOrigem().getSgFilial());
	            msg.append(doctoOriginal.getNrDoctoServico());
	            
	            if (!validacaoNF.get("blExisteNFPendente").booleanValue()) {
	                throw new BusinessException("LMS-04575", new Object[]{ msg });
	            }

	            if (!validacaoNF.get("blExisteOcorrencia").booleanValue()) {
	                throw new BusinessException("LMS-04576", new Object[]{ msg });
	            }
	        }else{
	            /* LMS-1211 */
	            boolean blBloqueado = (Boolean) ctrc.get("blBloqueado");
	            if(blBloqueado){
	                Short[] cdOcorrencias = new Short[]{
	                        ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_REFATURADA,
	                        ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_REFATURADA_LEG
	                };
	                List<OcorrenciaDoctoServico> ocorrenciaDoctoList = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoByCdOcorrencia(idDoctoServico, cdOcorrencias, false);
	                if(ocorrenciaDoctoList.isEmpty()){
	                    throw new BusinessException("LMS-04373");
	                }
	            }else{
	                Short cdLocalizacaoMercadoria = (Short) ctrc.get("cdLocalizacaoMercadoria");
	                if(cdLocalizacaoMercadoria.shortValue() != ConstantesExpedicao.CD_MERCADORIA_REFATURADA.shortValue()){
	                    throw new BusinessException("LMS-04373");
	                }	
	            }
	        }
		}
		
		String nrFormatado = FormatUtils.formatDecimal("00000000", nrConhecimento);
		Map map = (Map) result.get(0);
			map.put("nrConhecimento", nrFormatado);
		
		return result;
	}

	public List findFilial(Map criteria) {
		String sgFilial = (String) criteria.get("sgFilial");
		Long idEmpresa = (Long) criteria.get("idEmpresa");
		List result = filialService.findFilialBySgEmpresaLookup(sgFilial, idEmpresa);
		if (result != null && !result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				Map filial = (Map) result.get(i);
				filial.remove("nmFantasia");
			}
		}
		return filialService.findFilialBySgEmpresaLookup(sgFilial, idEmpresa);
	}

	public List findFilialConhecimento(Map criteria) {
		return filialService.findLookupBySgFilial((String) criteria.get("sgFilial"), (String) criteria.get("tpAcesso"));
	}

	public Map findAeroportoOrigemByFilial() {
		return findAeroportoByFilial(SessionUtils.getFilialSessao().getIdFilial());
	}

	public Map findAeroportoDestinoByFilial(TypedFlatMap criteria) {
		Long idFilial = null;
		idFilial = ppeService.findFilialAtendimentoMunicipio(
			criteria.getLong("idMunicipio"),
			criteria.getLong("idServico"),
			Boolean.FALSE,
			JTDateTimeUtils.getDataAtual(),
			criteria.getString("nrCep"),
			criteria.getLong("idCliente"),
			null, null, null, null, null);

		return findAeroportoByFilial(idFilial);
	}

	protected Conhecimento createConhecimentoPersistente(Map parameters, DomainValue tpDocumentoServico) {
		return digitarNotaService.createConhecimentoPersistente(parameters, tpDocumentoServico);
	}

	public Map findParametroObservacaoGeral(){
		String observacao = String.valueOf(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL));
		Map retorno = new TypedFlatMap();
		retorno.put("dsObservacaoDoctoServico", observacao);
		return retorno;
	}

	public List findAeroporto(Map criteria, String type) {
		List aeroportos = aeroportoService.findLookupAeroporto(criteria);
		if (aeroportos != null && !aeroportos.isEmpty()) {
			for (int i = 0; i < aeroportos.size(); i++) {
				Map aeroporto = (Map) aeroportos.get(i);
				Map pessoa = (Map) aeroporto.remove("pessoa");
				aeroporto.put("nmPessoaAeroporto" + type, pessoa.get("nmPessoa"));
				aeroporto.put("sgAeroporto" + type, aeroporto.remove("sgAeroporto"));
				aeroporto.put("idAeroporto" + type, aeroporto.remove("idAeroporto"));
			}
		}
		return aeroportos;
	}
	
	public List findAeroportoOrigem(Map criteria) {
		return findAeroporto(criteria, "Origem");
	}

	public List findAeroportoDestino(Map criteria) {
		return findAeroporto(criteria, "Destino");
	}

	/*
	 * METODOS PRIVADOS
	 */
	private Map findAeroportoByFilial(Long idFilial) {
		if(idFilial == null) {
			return null;
		}
		return filialService.findAeroportoFilial(idFilial);
	}

	/*
	 * GETTERS E SETTERS
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setProdutoEspecificoService(ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}
    public void setEventoDocumentoServicoService(
            EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }
    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }
    public void setConhecimentoDevolucaoService(
            ConhecimentoDevolucaoService conhecimentoDevolucaoService) {
        this.conhecimentoDevolucaoService = conhecimentoDevolucaoService;
    }
    public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
}
}
