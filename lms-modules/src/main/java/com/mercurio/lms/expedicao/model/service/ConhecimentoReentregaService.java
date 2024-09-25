package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.TipoCusto;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoReentregaService"
 */
public class ConhecimentoReentregaService extends AbstractConhecimentoService {
	private DomainValueService domainValueService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private ConhecimentoService conhecimentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ConhecimentoDevolucaoService conhecimentoDevolucaoService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private DigitarNotaService digitarNotaService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private DevedorDocServService devedorDocServService;
	private NotaFiscalOperadaService notaFiscalOperadaService;

	public Serializable storeConhecimentoReentrega(final Long idDoctoServicoOriginal, final Conhecimento conhecimentoCalculo, final List<NotaFiscalConhecimento> notasFiscaisConhecimento, final Boolean isEntregaParcial) {
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idDoctoServicoOriginal);

		// Aplica regras de CTRC válido para reentrega
		Long idFilialOrigem = null;
		if(conhecimentoOriginal.getFilialByIdFilialOrigem() != null) {
			idFilialOrigem = conhecimentoOriginal.getFilialByIdFilialOrigem().getIdFilial();
		}
		Long idFilialDestino = null;
		if(conhecimentoOriginal.getFilialByIdFilialDestino() != null) {
			idFilialDestino = conhecimentoOriginal.getFilialByIdFilialDestino().getIdFilial();
		}
		
		String msgExcecao = conhecimentoOriginal.getTpDocumentoServico().getValue() + " " + conhecimentoOriginal.getFilialOrigem().getSgFilial() + conhecimentoOriginal.getNrDoctoServico();
		validateCtrcEntrega(idDoctoServicoOriginal, idFilialOrigem, idFilialDestino, msgExcecao);

		Filial filialOrigem = SessionUtils.getFilialSessao();

		Conhecimento conhecimentoNovo = new Conhecimento();
		conhecimentoNovo.setMoeda(conhecimentoOriginal.getMoeda());
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		conhecimentoNovo.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		conhecimentoNovo.setFilialByIdFilialOrigem(filialOrigem);
		conhecimentoNovo.setVlTotalParcelas(conhecimentoCalculo.getVlTotalParcelas());
		conhecimentoNovo.setVlTotalServicos(conhecimentoCalculo.getVlTotalServicos());
		conhecimentoNovo.setVlDesconto(conhecimentoCalculo.getVlDesconto());
		conhecimentoNovo.setVlTotalDocServico(conhecimentoCalculo.getVlTotalDocServico());
		conhecimentoNovo.setVlLiquido(conhecimentoCalculo.getVlLiquido());
		conhecimentoNovo.setPcAliquotaIcms(conhecimentoCalculo.getPcAliquotaIcms());
		conhecimentoNovo.setTipoTributacaoIcms(conhecimentoCalculo.getTipoTributacaoIcms());
		conhecimentoNovo.setVlImposto(conhecimentoCalculo.getVlImposto());
		conhecimentoNovo.setVlImpostoPesoDeclarado(conhecimentoCalculo.getVlImpostoPesoDeclarado());
		conhecimentoNovo.setVlBaseCalcImposto(conhecimentoCalculo.getVlBaseCalcImposto());
		conhecimentoNovo.setVlIcmsSubstituicaoTributaria(conhecimentoCalculo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setVlIcmsSubstituicaoTributariaPesoDeclarado(conhecimentoCalculo.getVlIcmsSubstituicaoTributariaPesoDeclarado());
		conhecimentoNovo.setBlIncideIcmsPedagio(conhecimentoCalculo.getBlIncideIcmsPedagio());
		conhecimentoNovo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		conhecimentoNovo.setBlBloqueado(Boolean.FALSE);
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setFilialDestinoOperacional(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(conhecimentoOriginal.getRotaColetaEntregaByIdRotaColetaEntregaSugerid());
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaReal(conhecimentoOriginal.getRotaColetaEntregaByIdRotaColetaEntregaReal());
		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setPsReferenciaCalculo(conhecimentoOriginal.getPsReferenciaCalculo());
		conhecimentoNovo.setTabelaPreco(conhecimentoOriginal.getTabelaPreco());
		conhecimentoNovo.setTarifaPreco(conhecimentoOriginal.getTarifaPreco());
		conhecimentoNovo.setTpCalculoPreco(conhecimentoOriginal.getTpCalculoPreco());
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setPaisOrigem(conhecimentoOriginal.getPaisOrigem());
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimentoOriginal.getInscricaoEstadualDestinatario());
		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimentoOriginal.getInscricaoEstadualRemetente());

		//Conhecimento
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_REENTREGA));
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setFilialOrigem(filialOrigem);
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio());
		conhecimentoNovo.setDensidade(conhecimentoOriginal.getDensidade());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setDsEspecieVolume(conhecimentoOriginal.getDsEspecieVolume());
		conhecimentoNovo.setTpFrete(conhecimentoOriginal.getTpFrete());
		if (conhecimentoNovo.getBlIndicadorEdi() == null) {
			conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		}
		conhecimentoNovo.setBlIndicadorFretePercentual(Boolean.FALSE);
		conhecimentoNovo.setTpSituacaoConhecimento(new DomainValue("P"));
		conhecimentoNovo.setBlPermiteTransferencia(Boolean.TRUE);
		conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		conhecimentoNovo.setBlSeguroRr(Boolean.FALSE);
		conhecimentoNovo.setProduto(conhecimentoOriginal.getProduto());
		conhecimentoNovo.setPsAforado(conhecimentoOriginal.getPsAforado());
		conhecimentoNovo.setPsReal(conhecimentoOriginal.getPsReal());
		conhecimentoNovo.setDsCodigoColeta(conhecimentoOriginal.getDsCodigoColeta());
		conhecimentoNovo.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);
		conhecimentoNovo.setNrCepColeta(conhecimentoOriginal.getNrCepColeta());
		conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepEntrega());
		conhecimentoNovo.setDsEnderecoEntrega(conhecimentoOriginal.getDsEnderecoEntrega());
		conhecimentoNovo.setDsEnderecoEntregaReal(conhecimentoOriginal.getDsEnderecoEntregaReal());
		conhecimentoNovo.setNrEntrega(conhecimentoOriginal.getNrEntrega());
		conhecimentoNovo.setDsComplementoEntrega(conhecimentoOriginal.getDsComplementoEntrega());
		conhecimentoNovo.setDsBairroEntrega(conhecimentoOriginal.getDsBairroEntrega());
		conhecimentoNovo.setDsLocalEntrega(conhecimentoOriginal.getDsLocalEntrega());
		conhecimentoNovo.setVlMercadoria(conhecimentoOriginal.getVlMercadoria());
		conhecimentoNovo.setQtVolumes(this.findQtdVolume(conhecimentoOriginal, notasFiscaisConhecimento, isEntregaParcial));
		conhecimentoNovo.setBlPesoAferido(Boolean.TRUE);
		conhecimentoNovo.setBlProdutoControlado(conhecimentoOriginal.getBlProdutoControlado());
		conhecimentoNovo.setBlProdutoPerigoso(conhecimentoOriginal.getBlProdutoPerigoso());
		conhecimentoNovo.setDadosCliente(conhecimentoCalculo.getDadosCliente());
		conhecimentoNovo.setClienteByIdClienteBaseCalculo(conhecimentoOriginal.getClienteByIdClienteBaseCalculo());

		//Tabela DEVEDOR_DOC_SERV (Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original)
		ConhecimentoUtils.copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo);

		//Tabela DEVEDOR_DOC_SERV_FAT(Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original)
		DomainValue tpSituacaoCobranca = new DomainValue("P");
		ConhecimentoUtils.copyDevedoresDoctoServicoFaturamento(conhecimentoOriginal, conhecimentoNovo, tpSituacaoCobranca);

		//Tabela VALOR_CUSTO
		TipoCusto tipoCusto = tipoCustoService.findByDsTipoCusto(ConstantesExpedicao.TIPO_ICMS);
		ConhecimentoUtils.copyValorCusto(conhecimentoNovo, tipoCusto);

		//Tabela PARCELA_DOCTO_SERVICO (Para cada uma das parcelas geradas no conhecimento da rotina de calculo)
		ConhecimentoUtils.copyParcelasDoctoServico(conhecimentoCalculo, conhecimentoNovo);
		
		//Tabela IMPOSTO_SERVICO
		ConhecimentoUtils.copyImpostoServico(conhecimentoCalculo, conhecimentoNovo);

		//Tabela OBSERVACAO_DOCTO_SERVICO (Para cada das obervacoes do conhecimento da rotina de calculo)
		String dsObservacaoGeral = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL);
		ConhecimentoUtils.copyObservacoesDoctoServico(conhecimentoCalculo, conhecimentoNovo, dsObservacaoGeral);
		String dsProdutoPerigoso = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_PERIGOSO);
		String dsProdutoControlado = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_CONTROLADO);
		conhecimentoNovo.getObservacaoDoctoServicos().addAll(ConhecimentoUtils.criaObservacoesDoctoServicoPerigosoControlado(conhecimentoNovo, dsProdutoPerigoso, dsProdutoControlado));

		//Tabela DADO_COMPLEMENTO (Deverá ser gerada um registro para cada registro existente para o CTRC original na tabela DADO_COMPLEMENTO):
		ConhecimentoUtils.copyDadosComplemento(conhecimentoOriginal, conhecimentoNovo);

		// NR_CFOP = Conteúdo do campo CFOP definido no processo de cálculo do frete (Joelson)
		setNrCfop(conhecimentoNovo);
		
		//Tabela NOTA_FISCAL_CONHECIMENTO (Deverá ser gerado um registro para cada registro existente para o CTRC original na tabela NOTA_FISCAL_CONHECIMENTO)
		ConhecimentoUtils.copyNotasFiscaisConhecimento(conhecimentoOriginal, conhecimentoNovo, notasFiscaisConhecimento, isEntregaParcial);

		/*Identifica o tipo de documento*/
		DomainValue tpDocumento = new DomainValue(digitarNotaService.findTpDocumento(conhecimentoNovo, filialOrigem)); 
		conhecimentoNovo.setTpDocumentoServico(tpDocumento);
		conhecimentoNovo.setTpDoctoServico(tpDocumento);		
		
		//Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO.
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimentoOriginal.getDevedorDocServs().get(0)).getCliente();
		generateObsDoctoClienteByObsConhecimento(clienteResponsavel, conhecimentoNovo);
		
		//Define o Fluxo de Carga
		setFluxoCarga(conhecimentoNovo, Boolean.FALSE);

		conhecimentoNovo.setTpCargaDocumento(conhecimentoOriginal.getTpCargaDocumento());
		
		//Gerar monitoramento de descarga
		MonitoramentoDescarga monitoramento = executeMonitoramentoDescarga(conhecimentoNovo);		
		
		//Cria um volumes vinculados ao monitoramento de descarga
		executeVinculoVolumes(conhecimentoNovo,monitoramento);

		Serializable rusult = conhecimentoService.store(conhecimentoNovo);
		
		if (isEntregaParcial) {
            this.storeNotaFiscalOperada(notasFiscaisConhecimento, conhecimentoNovo);
        }
		
		liberacaoNotaNaturaService.atualizaTerraNaturaReentregaDevolucaoDigitado(conhecimentoOriginal, conhecimentoNovo);
		
		return rusult;
	}
	
	private void storeNotaFiscalOperada(List<NotaFiscalConhecimento> notasFiscaisConhecimento, Conhecimento conhecimento) {
        List<NotaFiscalOperada> notasFiscaisOperadas = new ArrayList<NotaFiscalOperada>();
        
        if(CollectionUtils.isEmpty(notasFiscaisConhecimento)){
            throw new BusinessException("LMS-17038");
        }
        
        for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
            NotaFiscalOperada notaFiscalOperada = new NotaFiscalOperada();
            notaFiscalOperada.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimento);
            notaFiscalOperada.setTpSituacao(new DomainValue("RE"));
            notaFiscalOperada.setDoctoServico(conhecimento);
            notasFiscaisOperadas.add(notaFiscalOperada);
        }
        
        notaFiscalOperadaService.storeAll(notasFiscaisOperadas);
    }
	
	private Integer findQtdVolume(Conhecimento conhecimentoOriginal, List<NotaFiscalConhecimento> notasFiscaisConhecimento, Boolean isEntregaParcial) {
		if (!isEntregaParcial) {
			return conhecimentoOriginal.getQtVolumes();
		}

		Integer retorno = 0;
		List<NotaFiscalConhecimento> notasFiscaisConhecimentoOriginais = conhecimentoOriginal.getNotaFiscalConhecimentos();

		for (NotaFiscalConhecimento notaFiscalConhecimentoOriginal : notasFiscaisConhecimentoOriginais) {
			for(NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {

				if(notaFiscalConhecimentoOriginal.getIdNotaFiscalConhecimento().equals(notaFiscalConhecimento.getIdNotaFiscalConhecimento())){
					retorno += notaFiscalConhecimentoOriginal.getQtVolumes();
					break;
				}
			}
		}
		
		return retorno;
	}

	/**
	 * Efetua o vinculo necessário entre NOTA_FISCAL_CONHECIMENTO , VOLUME_NOTA_FISCAL E 
	 * MONITORAMENTO_DESCARGA . Este vínculo é necessário devido ao processo que é feito
	 * na emissão de CTRC Normal 
	 * 
	 * @param conhecimentoNovo
	 * @param monitoramento
	 */
	private void executeVinculoVolumes(Conhecimento conhecimentoNovo, MonitoramentoDescarga monitoramento){

		Integer nrSequencia = 1;
		if(conhecimentoNovo.getNotaFiscalConhecimentos() != null && !conhecimentoNovo.getNotaFiscalConhecimentos().isEmpty()){
			for(NotaFiscalConhecimento nfc : conhecimentoNovo.getNotaFiscalConhecimentos()){
				
				VolumeNotaFiscal vol = new VolumeNotaFiscal();
				vol.setMonitoramentoDescarga(monitoramento);
				vol.setNotaFiscalConhecimento(nfc);
				vol.setNrConhecimento(conhecimentoNovo.getNrConhecimento());
				vol.setNrSequencia(nrSequencia++);
				vol.setNrVolumeColeta("0");
				vol.setQtVolumes(1);
				vol.setTpVolume("U");				
				
				List<VolumeNotaFiscal> list = new ArrayList<VolumeNotaFiscal>();
				list.add(vol);
				nfc.setVolumeNotaFiscais(list);
				
			}
		}/*if*/
	}
	
	/**
	 * Grava monitoramento de descarga através de um conhecimento
	 * 
	 * @param conhecimento
	 */
	private MonitoramentoDescarga executeMonitoramentoDescarga(Conhecimento co){
		
		MonitoramentoDescarga md = new MonitoramentoDescarga();
		md.setFilial(co.getFilialByIdFilialOrigem());		
		md.setTpSituacaoDescarga(new DomainValue("M"));
		md.setQtVolumesTotal(LongUtils.getLong(co.getQtVolumes()));
		md.setDhChegadaVeiculo(co.getDtColeta());
		md.setNrFrota("REEN");
		md.setNrPlaca("REEN");
		md.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		monitoramentoDescargaService.store(md);
		
		return md;
	}	

	private void validateCtrcEntrega(Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino, String mensagemExcecao) {
		boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(idDoctoServico);
		
		if (isEntregaParcial) {
		    Conhecimento conhecimentoOriginal = conhecimentoService.findById(idDoctoServico);
		    if (!ConstantesExpedicao.CD_MERCADORIA_NO_TERMINAL.equals(conhecimentoOriginal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())&&
                !ConstantesExpedicao.CD_MERCADORIA_RETORNADA_NO_TERMINAL.equals(conhecimentoOriginal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
                throw new BusinessException("LMS-04057");
            }
		    
		    Map<String, Boolean> validacaoNf = conhecimentoDevolucaoService.validateExisteOcorrenciaEntregaNF(idDoctoServico);

			if (!validacaoNf.get("blExisteNFPendente").booleanValue()) {
				throw new BusinessException("LMS-04575", new Object[]{ mensagemExcecao });
			}

			if (!validacaoNf.get("blExisteOcorrencia").booleanValue()) {
				throw new BusinessException("LMS-04576", new Object[]{ mensagemExcecao });
			}
			
		} else {
		//Verificar se esta cadastrado em um manifesto de entrega com uma ocorrência de entrega que permita a emissão de reentrega
		Integer nrOcorrenciaEntrega = manifestoEntregaDocumentoService.getRowCountManifestoEntregaDoctoServicoReentrega(idDoctoServico);
		if(CompareUtils.eq(nrOcorrenciaEntrega, IntegerUtils.ZERO)) {
			//Verificar se existe um evento com código de ocorrência de entrega que permita a emissão de reentrega
			nrOcorrenciaEntrega = eventoDocumentoServicoService.getRowCountEventoDoctoServicoReentrega(idDoctoServico);
		}
		if(CompareUtils.eq(nrOcorrenciaEntrega, IntegerUtils.ZERO)) {
			throw new BusinessException("LMS-04057");
		}
		}

		//Verificar se o cliente devedor possui cobrança de Reentrega
		Cliente clienteDevedor = devedorDocServService.findByIdDoctoServico(idDoctoServico);
		Cliente cliente = clienteService.findById(clienteDevedor.getIdCliente());
		if (!cliente.getBlCobraReentrega()){
			throw new BusinessException("LMS-04089");
		}	

		Filial filialUsuariologado = SessionUtils.getFilialSessao();

		//Se tpLocalEmissaoConReent diferente de null Avalia as regras 4 e 5
		if (cliente.getTpLocalEmissaoConReent() != null) {
			// Regra 4
			if ("O".equals(cliente.getTpLocalEmissaoConReent().getValue())) {
				if (idFilialOrigem != null && !idFilialOrigem.equals(filialUsuariologado.getIdFilial()))
					throw new BusinessException("LMS-04090");
			}
			// Regra 5
			if ("D".equals(cliente.getTpLocalEmissaoConReent().getValue())) {
				if (idFilialDestino != null && !idFilialDestino.equals(filialUsuariologado.getIdFilial()))
					throw new BusinessException("LMS-04091");
			}
		}
	}

	public List<Map<String, Object>> findConhecimentoReentregaLookup(Long nrDoctoServico, Long idFilial, String tpDocumentoServico) {
		/* CQPRO00028986 - Ajuste para pegar conhecimentos com status diferente de Cancelado e Pré-conhecimento */
		List<Map<String, Object>> result = conhecimentoService.findByNrConhecimentoIdFilialOrigem(nrDoctoServico, idFilial, "", tpDocumentoServico);		
		if(result.isEmpty()) {
			return null;
		} else {
			if(result.size() > 1){
				return null;
			}
			String tpSituacaoConhecimento = (String)((Map)result.get(0).get("tpSituacaoConhecimento")).get("value"); 
			if(tpSituacaoConhecimento.equals("C") || tpSituacaoConhecimento.equals("P")){
				return null;						
		}
		}

		Map<String, Object> conhecimento = result.get(0);
		Long idDoctoServico = (Long)conhecimento.get("idDoctoServico");
		Long idFilialDestino = (Long)conhecimento.get("idFilialDestino");
		Long idFilialOrigem = (Long)conhecimento.get("idFilialOrigem");
		
		String mensagemExcecao = tpDocumentoServico + " " + conhecimento.get("sgFilialOrigem") + conhecimento.get("nrDoctoServico");
		validateCtrcEntrega(idDoctoServico, idFilialOrigem, idFilialDestino, mensagemExcecao);
		
		Map<String, Object> remetente = (Map<String, Object>) conhecimento.get("remetente");
		Long idClienteRemetente = (Long) remetente.get("idCliente");
		DateTime dhEmissao = (DateTime) conhecimento.get("dhEmissao");
		Boolean blPermiteCte = (Boolean) remetente.get("blPermiteCte");

		boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(idDoctoServico);
		conhecimento.put("isEntregaParcial", isEntregaParcial);
		
		List<Map<String, Object>> notasFiscaisConhecimento = notaFiscalConhecimentoService.findNFByIdDoctoServico((Long) conhecimento.get("idDoctoServico"));

		if (isEntregaParcial) {
			notasFiscaisConhecimento = this.extractNfDisponivel(
				notasFiscaisConhecimento, 
				notaFiscalConhecimentoService.findNfDisponivel(idDoctoServico)
			);
		}
		
		notasFiscaisConhecimento = populateTipoDocumentoDescricao(notasFiscaisConhecimento);
		conhecimento.put("notasFiscaisConhecimento", notasFiscaisConhecimento);
		
		Boolean isDocumentoEletronico = isDocumentoEletronico(idDoctoServico, blPermiteCte); 
		conhecimento.put("isDocumentoEletronico", isDocumentoEletronico);
		
		//LMS-448
		liberacaoNotaNaturaService.validateTerraNaturaCTRCReentregaDevolucao(idDoctoServico, idFilialOrigem, idClienteRemetente, dhEmissao, ConstantesExpedicao.NM_PARAMETRO_TP_SERVICO_REENTREGA_NATURA);
		
		conhecimento.put("LMS-04142", this.validateLiberacaoConhecimentoReentrega(idDoctoServico));
		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		retorno.add(conhecimento);
		return retorno;
	}
	
	private String validateLiberacaoConhecimentoReentrega(Long idDoctoServico){
		if(CollectionUtils.isNotEmpty(conhecimentoService.findConhecimentoToValidateLiberacaoConhecimentoReentrega(idDoctoServico))){
			return "LMS-04142";
		}
		return null;
	}

	private List<Map<String, Object>> extractNfDisponivel(List<Map<String, Object>> notasFiscaisConhecimento, List<Long> notasFiscaisConhecimentoNaoEntregue) {
		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
		
		for(Long idNotaFiscalConhecimentoEntregue : notasFiscaisConhecimentoNaoEntregue) {
			for (Map<String, Object> notaFiscalConhecimento : notasFiscaisConhecimento) {
				if (notaFiscalConhecimento.get("idNotaFiscalConhecimento").equals(idNotaFiscalConhecimentoEntregue)) {
					retorno.add(notaFiscalConhecimento);
				}
			}
		}
		
		return retorno;
	}
	
	private List<Map<String, Object>> populateTipoDocumentoDescricao(List<Map<String, Object>> notasFiscaisConhecimento){
		for (Map<String, Object> map : notasFiscaisConhecimento) {
			map.put("tpDocumentoDesc", map.get("tpDocumento") == null ? "" : domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_CARGA", map.get("tpDocumento").toString()).getDescriptionAsString());
		}
		return notasFiscaisConhecimento;
	}

	public Boolean isDocumentoEletronico(final Long idDoctoServico, final Boolean blPermiteCte) {
		Boolean isDocumentoEletronico = false;
		
		Object obj = conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "INDICADOR CTE", false);
		if(obj != null){
			String indicadorCte = String.valueOf(obj);
			if("2".equals(indicadorCte)){
				isDocumentoEletronico = true;
			} else if("1".equals(indicadorCte)){
				isDocumentoEletronico = blPermiteCte == null ? false : blPermiteCte;
			}
		}
		if(!isDocumentoEletronico){
			obj = conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "INDICADOR NFE", false);
			if(obj != null){
				String indicadorNfe = String.valueOf(obj);
				isDocumentoEletronico = "1".equals(indicadorNfe);
			}
		}
		return isDocumentoEletronico;
	}

	public Conhecimento executeCalcularFreteReentrega(Long idConhecimento) {
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimento);

		Conhecimento conhecimentoNovo = new Conhecimento();
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal); 
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setFilialByIdFilialOrigem(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_REENTREGA));
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		
		DoctoServicoDadosCliente dadosCliente = conhecimentoOriginal.getDadosCliente();
		dadosCliente.setIdUfRemetente(dadosCliente.getIdUfDestinatario());
		dadosCliente.setIdUfFilialRemetente(dadosCliente.getIdUfDestinatario());
		dadosCliente.setIdMunicipioRemetente(dadosCliente.getIdMunicipioDestinatario());
		
		conhecimentoNovo.setDadosCliente(dadosCliente);
		
		/*Devedor*/
		List<DevedorDocServ> list = new ArrayList<DevedorDocServ>();
		list.add(devedorDocServService.findDevedorByDoctoServico(idConhecimento));
		conhecimentoNovo.setDevedorDocServs(list);
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		
		String tpDocumento = digitarNotaService.findTpDocumento(conhecimentoNovo, SessionUtils.getFilialSessao());
		conhecimentoNovo.setTpDocumentoServico(new DomainValue(tpDocumento));
		
		CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimentoNovo.getTpDocumentoServico());
		
		if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(conhecimentoNovo.getTpDocumentoServico().getValue()) || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(conhecimentoNovo.getTpDocumentoServico().getValue())) {
			calculoFrete.setBlCalculaImpostoServico(Boolean.TRUE);
		}
		
		setClienteBaseCalculoFrete(calculoFrete, conhecimentoOriginal);
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_REENTREGA);
		calculoFrete.setIdDoctoServico(idConhecimento);
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_RODOVIARIO);
		calculoFrete.setTpFrete(conhecimentoOriginal.getTpFrete().getValue());
		calculoFrete.setTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_NORMAL);
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		
		executeCalculo(conhecimentoNovo, calculoFrete);

		//LMS-3715
		conhecimentoNovo.setVlIcmsSubstituicaoTributariaPesoDeclarado(conhecimentoNovo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setVlImpostoPesoDeclarado(conhecimentoNovo.getVlImposto());
		
		return conhecimentoNovo;
	}

	@Override
	protected void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateDadosCliente(conhecimento, calculoFrete);
		documentoServicoFacade.executeCalculoConhecimentoNacionalReentrega(calculoFrete);
		CalculoFreteUtils.copyResult(conhecimento, calculoFrete);
	}

	@Override
	protected void validateCotacaoByCTRC(Conhecimento conhecimento,CalculoFrete calculoFrete) {
		// TODO Auto-generated method stub
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setConhecimentoDevolucaoService(ConhecimentoDevolucaoService conhecimentoDevolucaoService) {
		this.conhecimentoDevolucaoService = conhecimentoDevolucaoService;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}

	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setNotaFiscalOperadaService(NotaFiscalOperadaService notaFiscalOperadaService) {
        this.notaFiscalOperadaService = notaFiscalOperadaService;
    }
}
