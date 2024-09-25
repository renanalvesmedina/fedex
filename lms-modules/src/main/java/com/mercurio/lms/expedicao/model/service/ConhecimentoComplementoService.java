package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.TipoCusto;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoComplementoService"
 */
public class ConhecimentoComplementoService extends AbstractConhecimentoService {
	private ConhecimentoService conhecimentoService;
	private DoctoServicoService doctoServicoService;
	private FaturaService faturaService;
	private ParcelaPrecoService parcelaPrecoService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private InscricaoEstadualService inscricaoEstadualService;

	/**
	 * Grava Complemento de CONHECIMENTO
	 * @param conhecimento
	 * @return
	 */
	public Serializable storeConhecimentoComplemento(Conhecimento conhecimento) {
		return conhecimentoService.store(conhecimento);
	}

	/**
	 * Valida Complemente de Frete/ICMS
	 * @param conhecimento
	 */
	public void validateCTRCComplemento(TypedFlatMap conhecimento) {
		String tpConhecimento = conhecimento.getString("tpConhecimento");
		String idConhecimento = conhecimento.getString("idConhecimento");
		if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(tpConhecimento)) {
			validateConhecimentoComplementoIcms(conhecimento, idConhecimento);
		} else if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(tpConhecimento)) {
			validateConhecimentoComplementoFrete(conhecimento, idConhecimento);
			}
		if(!"A".equals(conhecimento.get("densidade")) && StringUtils.isNotBlank(conhecimento.getString("psAforado"))) {
			throw new BusinessException("LMS-04066");
			}
			}

	private void validateConhecimentoComplementoFrete(
			TypedFlatMap conhecimento, String idConhecimento) {
			if(StringUtils.isBlank(idConhecimento) && StringUtils.isBlank(conhecimento.getString("nrFatura"))) {
				throw new BusinessException("LMS-04029");
			}
			if(ConstantesExpedicao.CALCULO_MANUAL.equals(conhecimento.getString("tpCalculo")) && StringUtils.isBlank(conhecimento.getString("valor"))) {
				throw new BusinessException("LMS-04189");
			}
		}

	private void validateConhecimentoComplementoIcms(TypedFlatMap conhecimento,
			String idConhecimento) {
		if(StringUtils.isBlank(idConhecimento)) {
			throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("conhecimento")});
		}
		if(StringUtils.isBlank(conhecimento.getString("valor"))) {
			throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("valor")});
	}
		if(StringUtils.isBlank(idConhecimento) && !"M".equals(conhecimento.getString("tpCalculo"))) {
			throw new BusinessException("LMS-04055");
		}
	}
	
	/**
	 * Calculo de Frete NORMAL
	 * @param conhecimentoNovo
	 * @param tpFrete
	 * @param idClienteBase
	 * @param blGeraReceita
	 */

	public void executeCalculoFreteComplemento(Conhecimento conhecimentoNovo, Long idFatura, BigDecimal vlFreteManual) {
		executeCalculoFreteComplemento(conhecimentoNovo, idFatura, vlFreteManual, null);
	}
	
	/**
	 * Calculo de Frete NORMAL
	 * @param conhecimentoNovo
	 * @param tpFrete
	 * @param idClienteBase
	 * @param blGeraReceita
	 */
	
	public void executeCalculoFreteComplemento(Conhecimento conhecimentoNovo, Long idFatura, BigDecimal vlFreteManual, Boolean blGeraReceita) {
		Long idDoctoServicoOriginal = conhecimentoNovo.getDoctoServicoOriginal().getIdDoctoServico();

		CalculoFrete calculoFrete = new CalculoFrete();
		EnderecoPessoa enderecoPessoaRemetente = null;
		EnderecoPessoa enderecoPessoaDestinatario = null;

		if(idDoctoServicoOriginal != null) {
			Conhecimento conhecimentoOriginal = conhecimentoService.findById(idDoctoServicoOriginal);

			conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
			conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
			conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
			conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
			conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());
			conhecimentoNovo.setFilialByIdFilialOrigem(conhecimentoOriginal.getFilialByIdFilialOrigem());
			conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
			conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
			conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepEntrega());
			conhecimentoNovo.setQtVolumes(conhecimentoOriginal.getQtVolumes());
			setClienteBaseCalculoFrete(calculoFrete, conhecimentoOriginal);

			enderecoPessoaRemetente = conhecimentoOriginal.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa();
			enderecoPessoaDestinatario = conhecimentoOriginal.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa();

			calculoFrete.setIdServico(conhecimentoOriginal.getServico().getIdServico());

			//Tabela DEVEDOR_DOC_SERV (Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original):
			ConhecimentoUtils.copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo);

			//Tabela DEVEDOR_DOC_SERV_DEV
			ConhecimentoUtils.copyDevedoresDoctoServicoFaturamento(conhecimentoOriginal, conhecimentoNovo, new DomainValue("P"));
			
		} else if(idFatura != null) {
			Fatura fatura = faturaService.findById(idFatura);

			Cliente cliente = fatura.getCliente();
			conhecimentoNovo.setClienteByIdClienteRemetente(cliente);
			conhecimentoNovo.setClienteByIdClienteDestinatario(cliente);
			conhecimentoNovo.setClienteByIdClienteConsignatario(cliente);
			conhecimentoNovo.setClienteByIdClienteRedespacho(cliente);
			conhecimentoNovo.setFilialByIdFilialDestino(fatura.getFilialByIdFilial());
			conhecimentoNovo.setFilialByIdFilialOrigem(fatura.getFilialByIdFilial());
			calculoFrete.setClienteBase(cliente);

			enderecoPessoaRemetente = cliente.getPessoa().getEnderecoPessoa();
			enderecoPessoaDestinatario = cliente.getPessoa().getEnderecoPessoa();

			calculoFrete.setIdServico(fatura.getServico().getIdServico());
		}

		if (calculoFrete.getDadosCliente() != null) {
			Municipio municipioRemetente = enderecoPessoaRemetente != null ? enderecoPessoaRemetente.getMunicipio() : null;
			Municipio municipioDestinatario = enderecoPessoaDestinatario != null ? enderecoPessoaDestinatario.getMunicipio() : null;
			calculoFrete.getDadosCliente().setIdMunicipioRemetente(municipioRemetente != null ? municipioRemetente.getIdMunicipio() : null);
			calculoFrete.getDadosCliente().setIdMunicipioDestinatario(municipioDestinatario != null ? municipioDestinatario.getIdMunicipio() : null);
		}

		calculoFrete.setNrCepColeta(enderecoPessoaRemetente != null ? enderecoPessoaRemetente.getNrCep() : null);
		calculoFrete.setNrCepEntrega(enderecoPessoaDestinatario != null ? enderecoPessoaDestinatario.getNrCep() : null);

		calculoFrete.setPsCubadoInformado(conhecimentoNovo.getPsAforado());
		calculoFrete.setPsRealInformado(conhecimentoNovo.getPsReal());
		calculoFrete.setVlMercadoria(conhecimentoNovo.getVlMercadoria());
		calculoFrete.setIdDoctoServico(idDoctoServicoOriginal);
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_RODOVIARIO);
		calculoFrete.setTpFrete(conhecimentoNovo.getTpFrete().getValue());
		calculoFrete.setTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		calculoFrete.setTpCalculo(conhecimentoNovo.getTpCalculoPreco().getValue());
		calculoFrete.setTpConhecimento(conhecimentoNovo.getTpConhecimento().getValue());
		calculoFrete.setBlGeraReceita(blGeraReceita);
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setQtVolumes(conhecimentoNovo.getQtVolumes());
		generateParcelaPesoConhecimentoComplemento(conhecimentoNovo, vlFreteManual, calculoFrete);
		
		executeCalculo(conhecimentoNovo, calculoFrete);
		
		// LMS-3179
		if ((ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(calculoFrete.getTpConhecimento()) 
				|| ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(calculoFrete.getTpConhecimento()))
				&& calculoFrete.isBlGeraReceita()
				&& (calculoFrete.getVlTotal() == null || Double.valueOf(0).equals(calculoFrete.getVlTotal().doubleValue()))) {
			throw new BusinessException("LMS-04080");
	}
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("vlFrete", conhecimentoNovo.getVlTotalDocServico());
		doctoServicoService.executeValidacaoLimiteValorFrete(map);

		map.put("vlMercadoria", conhecimentoNovo.getVlMercadoria());
		doctoServicoService.executeValidacaoPercentualValorMercadoria(map);
	}

	private void generateParcelaPesoConhecimentoComplemento(Conhecimento conhecimentoNovo, BigDecimal vlFreteManual, CalculoFrete calculoFrete) {
		if(ConstantesExpedicao.CALCULO_MANUAL.equals(calculoFrete.getTpCalculo())) {
			ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
			if (calculoFrete.gerarParcelaFretePesoCtrcCompleto()) {
			ParcelaServico parcelaServico = new ParcelaServico(parcelaPreco, vlFreteManual, vlFreteManual);
			if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(conhecimentoNovo.getTpConhecimento().getValue())){
				parcelaServico.setVlParcela(vlFreteManual);
			}
			calculoFrete.addParcelaGeral(parcelaServico);
			} else {
				ParcelaServico parcelaServico = new ParcelaServico(parcelaPreco, BigDecimal.ZERO, BigDecimal.ZERO);
				if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(conhecimentoNovo.getTpConhecimento().getValue())){
					calculoFrete.setVlComplementoIcms(vlFreteManual);
					conhecimentoNovo.setVlDesconto(null);
					conhecimentoNovo.setVlMercadoria(BigDecimal.ZERO); // verificar aqui se foi herdado o valor da nota
					
				}
				calculoFrete.addParcelaGeral(parcelaServico);
			}
			calculoFrete.setDoctoServico(conhecimentoNovo);
		}
	}

	/**
	 * Gera Complemento de CONHECIMENTO
	 * @param criteria
	 * @return
	 */
	public Serializable generateCTRCComplementoConhecimento(Conhecimento conhecimentoCalculo) {
		Conhecimento conhecimentoNovo = new Conhecimento();
		Filial filial = SessionUtils.getFilialSessao();

		Long idConhecimentoOriginal = conhecimentoCalculo.getDoctoServicoOriginal().getIdDoctoServico();
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimentoOriginal);

		conhecimentoNovo.setMoeda(conhecimentoOriginal.getMoeda());
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		conhecimentoNovo.setFilialByIdFilialOrigem(filial);
		conhecimentoNovo.setFilialOrigem(filial);

		conhecimentoNovo.setFluxoFilial(conhecimentoOriginal.getFluxoFilial());
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());		
		conhecimentoNovo.setFilialDestinoOperacional(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(conhecimentoOriginal.getRotaColetaEntregaByIdRotaColetaEntregaSugerid());
		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setDensidade(conhecimentoOriginal.getDensidade());
		conhecimentoNovo.setDsEspecieVolume(conhecimentoOriginal.getDsEspecieVolume());
		conhecimentoNovo.setDsCodigoColeta(conhecimentoOriginal.getDsCodigoColeta());
		conhecimentoNovo.setQtVolumes(conhecimentoOriginal.getQtVolumes());
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());

		conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepEntrega());
		conhecimentoNovo.setNrCepColeta(conhecimentoOriginal.getNrCepColeta());

		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimentoOriginal.getInscricaoEstadualRemetente());
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimentoOriginal.getInscricaoEstadualDestinatario());

		conhecimentoNovo.setDsLocalEntrega(conhecimentoOriginal.getDsLocalEntrega());
		conhecimentoNovo.setBlColetaEmergencia(conhecimentoOriginal.getBlColetaEmergencia());
		conhecimentoNovo.setBlEntregaEmergencia(conhecimentoOriginal.getBlEntregaEmergencia());
		conhecimentoNovo.setAeroportoByIdAeroportoOrigem(conhecimentoOriginal.getAeroportoByIdAeroportoOrigem());
		conhecimentoNovo.setAeroportoByIdAeroportoDestino(conhecimentoOriginal.getAeroportoByIdAeroportoDestino());
		conhecimentoNovo.setProdutoEspecifico(conhecimentoOriginal.getProdutoEspecifico());
		conhecimentoNovo.setDsBairroEntrega(conhecimentoOriginal.getDsBairroEntrega());
		conhecimentoNovo.setDsComplementoEntrega(conhecimentoOriginal.getDsComplementoEntrega());
		conhecimentoNovo.setPaisOrigem(conhecimentoOriginal.getPaisOrigem());
		conhecimentoNovo.setBlPesoAferido(conhecimentoOriginal.getBlPesoAferido());

		Object value = conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "INDICADOR CTE", false);
		String indicadorFilial = value == null ? StringUtils.EMPTY : String.valueOf(value);
		
		if("2".equals(indicadorFilial)){
				conhecimentoNovo.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
				conhecimentoNovo.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		}else{
			conhecimentoNovo.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
			conhecimentoNovo.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
		}
		
		if(conhecimentoCalculo.getTpCalculoPreco().getValue().equals(ConstantesExpedicao.CALCULO_MANUAL)){
			ConhecimentoUtils.copyLiberacaoDocServs(conhecimentoCalculo, conhecimentoNovo);
		}

		generateCTRCComplemento(conhecimentoNovo, conhecimentoCalculo);

		//Tabela DEVEDOR_DOC_SERV (Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original):
		ConhecimentoUtils.copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo);

		//Tabela DEVEDOR_DOC_SERV_DEV
		ConhecimentoUtils.copyDevedoresDoctoServicoFaturamento(conhecimentoOriginal, conhecimentoNovo, new DomainValue("P"));

		//Tabela VALOR_CUSTO
		TipoCusto tipoCusto = tipoCustoService.findByDsTipoCusto(ConstantesExpedicao.TIPO_ICMS);
		ConhecimentoUtils.copyValorCusto(conhecimentoNovo, tipoCusto);

		//Tabela DADO_COMPLEMENTO
		ConhecimentoUtils.copyDadosComplemento(conhecimentoOriginal, conhecimentoNovo);

		//Tabela DOCTO_SERVICO_SEGUROS
		ConhecimentoUtils.copyDoctoServicoSeguros(conhecimentoOriginal, conhecimentoNovo);

		//Tabela NOTA_FISCAL_CONHECIMENTO
		ConhecimentoUtils.copyNotasFiscaisConhecimento(conhecimentoOriginal, conhecimentoNovo); 
		
		//Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO.
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimentoOriginal.getDevedorDocServs().get(0)).getCliente();
		generateObsDoctoClienteByObsConhecimento(clienteResponsavel, conhecimentoNovo);

		setNrCfop(conhecimentoNovo);
		
		//Gerar monitoramento de descarga
		MonitoramentoDescarga monitoramentoDescarga = executeMonitoramentoDescarga(conhecimentoNovo);
		
		executeVolumeNotaFiscal(conhecimentoNovo, monitoramentoDescarga);

		return storeConhecimentoComplemento(conhecimentoNovo);
	}

	private void executeVolumeNotaFiscal(final Conhecimento conhecimentoNovo, final MonitoramentoDescarga monitoramentoDescarga) {
		NotaFiscalConhecimento nfc = conhecimentoNovo.getNotaFiscalConhecimentos().get(0);

		VolumeNotaFiscal vnf = new VolumeNotaFiscal();
		vnf.setNotaFiscalConhecimento(nfc);
		vnf.setNrSequencia(1);
		vnf.setNrVolumeColeta("0");
		vnf.setMonitoramentoDescarga(monitoramentoDescarga);
		vnf.setQtVolumes(1);
		vnf.setTpVolume("U");

		List<VolumeNotaFiscal> list = new ArrayList<VolumeNotaFiscal>();
		list.add(vnf);
		nfc.setVolumeNotaFiscais(list);
	}

	private MonitoramentoDescarga executeMonitoramentoDescarga(Conhecimento co){

		MonitoramentoDescarga md = new MonitoramentoDescarga();
		md.setFilial(co.getFilialByIdFilialOrigem());		
		md.setTpSituacaoDescarga(new DomainValue("M"));
		md.setQtVolumesTotal(LongUtils.getLong(co.getQtVolumes()));
		md.setDhChegadaVeiculo(co.getDtColeta());
		md.setNrFrota("COMP");
		md.setNrPlaca("COMP");
		md.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		monitoramentoDescargaService.store(md);

		return md;
	}

	/**
	 * Atribui os valores comuns ao CTRC de complemento de Conhecimento e Fatura
	 * @param conhecimentoNovo
	 * @param conhecimentoCalculo
	 */
	private void generateCTRCComplemento(Conhecimento conhecimentoNovo, Conhecimento conhecimentoCalculo) {
		conhecimentoNovo.setTabelaPreco(conhecimentoCalculo.getTabelaPreco());
		if (conhecimentoNovo.getBlIndicadorEdi() == null) {
			conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		}
		conhecimentoNovo.setBlIndicadorFretePercentual(Boolean.FALSE);
		conhecimentoNovo.setTpSituacaoConhecimento(new DomainValue("P"));
		conhecimentoNovo.setBlPermiteTransferencia(Boolean.TRUE);
		conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		conhecimentoNovo.setBlSeguroRr(Boolean.FALSE);
		conhecimentoNovo.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);

		conhecimentoNovo.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		conhecimentoNovo.setDhEmissao(null);
		conhecimentoNovo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		conhecimentoNovo.setBlBloqueado(Boolean.FALSE);

		conhecimentoNovo.setVlTotalParcelas(conhecimentoCalculo.getVlTotalParcelas());
		conhecimentoNovo.setVlTotalServicos(BigDecimalUtils.ZERO);
		conhecimentoNovo.setVlDesconto(conhecimentoCalculo.getVlDesconto());
		conhecimentoNovo.setVlTotalDocServico(conhecimentoCalculo.getVlTotalDocServico());
		conhecimentoNovo.setVlLiquido(conhecimentoCalculo.getVlLiquido());

		conhecimentoNovo.setVlImposto(conhecimentoCalculo.getVlImposto());
		conhecimentoNovo.setVlImpostoPesoDeclarado(conhecimentoCalculo.getVlImposto());
		conhecimentoNovo.setVlBaseCalcImposto(calculaBaseCalcImposto(conhecimentoCalculo));
		conhecimentoNovo.setPcAliquotaIcms(conhecimentoCalculo.getPcAliquotaIcms());
		conhecimentoNovo.setTipoTributacaoIcms(conhecimentoCalculo.getTipoTributacaoIcms());
		conhecimentoNovo.setVlIcmsSubstituicaoTributaria(conhecimentoCalculo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setVlIcmsSubstituicaoTributariaPesoDeclarado(conhecimentoCalculo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setBlIncideIcmsPedagio(conhecimentoCalculo.getBlIncideIcmsPedagio());

		conhecimentoNovo.setTpCalculoPreco(conhecimentoCalculo.getTpCalculoPreco());
		conhecimentoNovo.setPsReal(conhecimentoCalculo.getPsReal());
		conhecimentoNovo.setPsAforado(conhecimentoCalculo.getPsAforado());
		conhecimentoNovo.setPsReferenciaCalculo(conhecimentoCalculo.getPsReferenciaCalculo());
		conhecimentoNovo.setTpConhecimento(conhecimentoCalculo.getTpConhecimento());

		conhecimentoNovo.setTpFrete(conhecimentoCalculo.getTpFrete());
		conhecimentoNovo.setVlMercadoria(conhecimentoCalculo.getVlMercadoria());
		conhecimentoNovo.setTpMotivoLiberacao(conhecimentoCalculo.getTpMotivoLiberacao());

		//Observacao Geral
		String dsObservacao = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL);
		ConhecimentoUtils.copyObservacoesDoctoServico(conhecimentoCalculo, conhecimentoNovo, dsObservacao);

		//Parcelas
		ConhecimentoUtils.copyParcelasDoctoServico(conhecimentoCalculo, conhecimentoNovo);

	}

	private BigDecimal calculaBaseCalcImposto(Conhecimento conhecimentoCalculo) {
		//(100 * VL_TOTAL_DOC_SERVICO(Conforme campo Valor do Frete, retornado do cálculo)) / PC_ALIQUOTA_ICMS(resultante do cálculo do frete)
		
		if(conhecimentoCalculo.getTpConhecimento()!= null && 
				conhecimentoCalculo.getTpConhecimento().getValue().equals(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS)){
			return new BigDecimal(100).multiply(conhecimentoCalculo.getVlTotalDocServico(), MathContext.DECIMAL128).divide(conhecimentoCalculo.getPcAliquotaIcms(), MathContext.DECIMAL128);
		}
		else{
			return conhecimentoCalculo.getVlBaseCalcImposto();
		}
	}

	/**
	 * Executa Calculo do Complemento
	 */
	@Override
	protected void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateDadosCliente(conhecimento, calculoFrete);
		
		if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(calculoFrete.getTpConhecimento())) {
			documentoServicoFacade.executeCalculoConhecimentoNacionalComplementoICMS(calculoFrete);
		} else if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(calculoFrete.getTpConhecimento())) {
			documentoServicoFacade.executeCalculoConhecimentoNacionalComplementoFrete(calculoFrete);
		}
		
		CalculoFreteUtils.copyResult(conhecimento, calculoFrete);
	}

	@Override
	protected void validateCotacaoByCTRC(Conhecimento conhecimento,CalculoFrete calculoFrete) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Carrega Inscricao Estadual do Rementente/Destinatario
	 * @param criteria
	 */
	public void configureIEConhecimento(Map<String, Object> criteria) {
		Long idDoctoServico = MapUtils.getLong(criteria, "idDoctoServico");
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);

		if(doctoServico.getInscricaoEstadualRemetente() != null) {
			putInscricaoEstadual(criteria, doctoServico.getInscricaoEstadualRemetente().getIdInscricaoEstadual(), "remetente");
		}
		if(doctoServico.getInscricaoEstadualDestinatario() != null) {
			putInscricaoEstadual(criteria, doctoServico.getInscricaoEstadualDestinatario().getIdInscricaoEstadual(), "destinatario");
		}
	}

	private void putInscricaoEstadual(Map<String, Object> criteria, Long idInscricaoEstadual, String key) {
		Map<String, Object> pessoa = MapUtils.getMap(criteria, key);
		pessoa.put("nrInscricaoEstadual", inscricaoEstadualService.findById(idInscricaoEstadual).getNrInscricaoEstadual());
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
}
