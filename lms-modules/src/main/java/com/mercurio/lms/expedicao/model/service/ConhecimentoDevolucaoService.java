package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.service.EntregaNotaFiscalService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
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
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SegmentoMercado;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoDevolucaoService"
 */
public class ConhecimentoDevolucaoService extends AbstractConhecimentoNormalService {
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ReciboReembolsoService reciboReembolsoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private EntregaNotaFiscalService entregaNotaFiscalService;
	private FilialService filialService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private DigitarNotaService digitarNotaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private ConhecimentoReentregaService conhecimentoReentregaService;
	private ManifestoService manifestoService;
	private NotaFiscalOperadaService notaFiscalOperadaService;

	/**
	 * Calculo de Frete para Devolucao.
	 * @param conhecimentoNovo
	 * @param tpCalculo
	 * @param idCotacao
	 */
	public void executeCalculoFreteDevolucao(Conhecimento conhecimentoNovo, String tpCalculo, Long idCotacao) {
		if(ConstantesExpedicao.CALCULO_COTACAO.equals(tpCalculo) && (idCotacao == null) ) {
			throw new BusinessException("LMS-04095");
		}
		Long idDoctoServicoOriginal = conhecimentoNovo.getDoctoServicoOriginal().getIdDoctoServico();
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idDoctoServicoOriginal);

		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setTpFrete(new DomainValue(ConstantesExpedicao.TP_FRETE_FOB));
		
		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(conhecimentoOriginal.getTpDevedorFrete().getValue())
				|| (!ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(conhecimentoOriginal.getTpDevedorFrete().getValue()) && ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoOriginal.getTpFrete().getValue()))){
			conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		}else{
			conhecimentoNovo.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO));
		}

		//ID_FILIAL_ORIGEM = Filial do usuário logado (FILIAL.ID_FILIAL)
		Filial filial = SessionUtils.getFilialSessao();
		conhecimentoNovo.setFilialOrigem(filial);
		conhecimentoNovo.setFilialByIdFilialOrigem(filial);
		
		conhecimentoNovo.setVlLiquido(conhecimentoOriginal.getVlLiquido());
		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio());
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO));
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		
		copyDevedorDocServ(conhecimentoOriginal, conhecimentoNovo);

		String tpDocumento = digitarNotaService.findTpDocumento(conhecimentoNovo, SessionUtils.getFilialSessao());
		conhecimentoNovo.setTpDocumentoServico(new DomainValue(tpDocumento));
		
		CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimentoNovo.getTpDocumentoServico());
		
		if(calculoFrete.isCalculoNotaTransporte()) {
			calculoFrete.setBlCalculaImpostoServico(Boolean.TRUE);
		}
		
		calculoFrete.setDoctoServico(conhecimentoNovo);
		setClienteBaseCalculoFrete(calculoFrete, conhecimentoOriginal);

		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO);
		calculoFrete.setIdDoctoServico(idDoctoServicoOriginal);
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_RODOVIARIO);
		calculoFrete.setTpFrete(conhecimentoNovo.getTpFrete().getValue());
		calculoFrete.setTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		calculoFrete.setTpCalculo(tpCalculo);
		calculoFrete.setIdCotacao(idCotacao);

		/*Informa a situacao tributaria do responsavel*/
		DevedorDocServ devedorDoctoServico = (DevedorDocServ)conhecimentoOriginal.getDevedorDocServs().get(0);
		if(devedorDoctoServico != null){			
			conhecimentoNovo.getDadosCliente()
				.setTpSituacaoTributariaResponsavel(getTpSitTributariaResponsavel(devedorDoctoServico));
		}
				
		executeCalculo(conhecimentoNovo, calculoFrete);
	}

	public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoOriginal(Long idConhecimento) {
		List<NotaFiscalConhecimento> notas = new LinkedList<NotaFiscalConhecimento>();
		List<Map> list = notaFiscalConhecimentoService.findIdNrNotaByIdConhecimento(idConhecimento);
		if(list != null) {
			NotaFiscalConhecimento notaFiscalConhecimento = null;
			for (Map notaFiscal : list) {
				notaFiscalConhecimento = new NotaFiscalConhecimento();
				notaFiscalConhecimento.setIdNotaFiscalConhecimento((Long) notaFiscal.get("idNotaFiscalConhecimento"));
				notaFiscalConhecimento.setNrNotaFiscal((Integer) notaFiscal.get("nrNotaFiscal"));
				notas.add(notaFiscalConhecimento);
			}
		}
		return notas;
	}
	
	/**
	 * Calculo de Frete Manual.
	 * @param conhecimentoNovo
	 * @param parcelas
	 */
	public void executeCalculoFreteDevolucaoManual(Conhecimento conhecimentoNovo, List<Map<String, Object>> parcelas) {
		Long idDoctoServicoOriginal = conhecimentoNovo.getDoctoServicoOriginal().getIdDoctoServico();
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idDoctoServicoOriginal);

		//Seta Clientes
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());

		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialOrigem());

		//ID_FILIAL_ORIGEM = Filial do usuário logado (FILIAL.ID_FILIAL)
		Filial filial = SessionUtils.getFilialSessao();
		conhecimentoNovo.setFilialOrigem(filial);
		conhecimentoNovo.setFilialByIdFilialOrigem(filial);
		
		conhecimentoNovo.setVlLiquido(conhecimentoOriginal.getVlLiquido());
		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		
		conhecimentoNovo.setTpFrete(new DomainValue("F"));
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioColeta());
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO));
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		
		String tpDocumento = digitarNotaService.findTpDocumento(conhecimentoNovo, SessionUtils.getFilialSessao());
		conhecimentoNovo.setTpDocumentoServico(new DomainValue(tpDocumento));
		
		copyDevedorDocServ(conhecimentoOriginal, conhecimentoNovo);
		
		CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimentoNovo.getTpDocumentoServico());
		
		if(calculoFrete.isCalculoNotaTransporte()) {
			calculoFrete.setBlCalculaImpostoServico(Boolean.TRUE);
		}
		
		setClienteBaseCalculoFrete(calculoFrete, conhecimentoOriginal);

		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO);
		calculoFrete.setIdDoctoServico(idDoctoServicoOriginal);
		calculoFrete.setTpModal(ConstantesExpedicao.MODAL_RODOVIARIO);
		calculoFrete.setTpFrete(conhecimentoNovo.getTpFrete().getValue());
		calculoFrete.setTpAbrangencia(ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_MANUAL);

		if(parcelas != null && !parcelas.isEmpty()){
			Map<String, Object> parcela = parcelas.get(0);
			BigDecimal vlTotalFrete = MapUtilsPlus.getBigDecimal(parcela, "vlParcela");
			ParcelaPreco pp = new ParcelaPreco();
			pp.setIdParcelaPreco(MapUtilsPlus.getLong(parcela, "id"));
			pp.setNmParcelaPreco(new VarcharI18n((String)parcela.get("dsParcela")));
			pp.setDsParcelaPreco(new VarcharI18n((String)parcela.get("dsParcela")));			
			pp.setCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
			ParcelaServico fp = new ParcelaServico(pp, vlTotalFrete, vlTotalFrete);
			calculoFrete.addParcelaGeral(fp);
		}
		executeCalculo(conhecimentoNovo, calculoFrete);
	}

	@Override
	protected void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateDadosCliente(conhecimento, calculoFrete);

		documentoServicoFacade.executeCalculoConhecimentoNacionalDevolucao(calculoFrete);
		CalculoFreteUtils.copyResult(conhecimento, calculoFrete);
	}

	/**
	 * Grava Conhecimento de Devolucao.
	 * @param idConhecimentoOriginal
	 * @param conhecimentoCalculo
	 * @param notasFiscaisConhecimento2 
	 * @return
	 */
	public Serializable storeConhecimentoDevolucao(Long idConhecimentoOriginal, Conhecimento conhecimentoCalculo, List<Map<String, Object>> mapNotasFiscaisConhecimento, final List<NotaFiscalConhecimento> notasFiscaisConhecimento, Boolean blBloqueiaSubcontratada, final Boolean isEntregaParcial) {

		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimentoOriginal);
		
		FaseProcesso faseProcesso = ocorrenciaDoctoServicoService.getFaseProcessoService().findByIdDoctoServico(conhecimentoOriginal.getIdDoctoServico());
		
		Conhecimento conhecimentoNovo = new Conhecimento();
		Filial filial = SessionUtils.getFilialSessao();
		
		Integer qtdVolume = this.findQtdVolume(conhecimentoOriginal, notasFiscaisConhecimento, isEntregaParcial);
		
		conhecimentoNovo.setDtColeta(conhecimentoOriginal.getDtColeta());
		
		//Copia valores do conhecimento que esta na sessao.
		conhecimentoNovo.setDadosCliente(conhecimentoCalculo.getDadosCliente());

		conhecimentoNovo.setDsRespAutDevMerc(conhecimentoCalculo.getDsRespAutDevMerc());
		conhecimentoNovo.setDtAutDevMerc(conhecimentoCalculo.getDtAutDevMerc());

		conhecimentoNovo.setVlTotalParcelas(conhecimentoCalculo.getVlTotalParcelas());
		conhecimentoNovo.setVlTotalServicos(conhecimentoCalculo.getVlTotalServicos());
		conhecimentoNovo.setVlTotalDocServico(conhecimentoCalculo.getVlTotalDocServico());
		conhecimentoNovo.setVlDesconto(conhecimentoCalculo.getVlDesconto());
		conhecimentoNovo.setVlLiquido(conhecimentoCalculo.getVlLiquido());

		conhecimentoNovo.setVlImposto(conhecimentoCalculo.getVlImposto());
		conhecimentoNovo.setVlImpostoPesoDeclarado(conhecimentoCalculo.getVlImposto());
		conhecimentoNovo.setVlBaseCalcImposto(conhecimentoCalculo.getVlBaseCalcImposto());
		conhecimentoNovo.setPcAliquotaIcms(conhecimentoCalculo.getPcAliquotaIcms());
		conhecimentoNovo.setTipoTributacaoIcms(conhecimentoCalculo.getTipoTributacaoIcms());
		conhecimentoNovo.setVlIcmsSubstituicaoTributaria(conhecimentoCalculo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setVlIcmsSubstituicaoTributariaPesoDeclarado(conhecimentoCalculo.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setBlIncideIcmsPedagio(conhecimentoCalculo.getBlIncideIcmsPedagio());
		conhecimentoNovo.setTpFrete(conhecimentoCalculo.getTpFrete());
		conhecimentoNovo.setTpMotivoLiberacao(conhecimentoCalculo.getTpMotivoLiberacao());
		/** INVERTE DADOS **/
		//Filiais Origem/Destino
		Filial destino = conhecimentoOriginal.getClienteByIdClienteRemetente().getFilialByIdFilialAtendeOperacional();
			   destino = destino == null ? conhecimentoOriginal.getFilialByIdFilialOrigem() : destino;
		
		conhecimentoNovo.setFilialByIdFilialDestino(destino);
		conhecimentoNovo.setFilialDestinoOperacional(destino);
		Cliente clienteRemetenteOriginal = conhecimentoOriginal.getClienteByIdClienteRemetente();
		
		//Clientes Destinatario/Remetente
		conhecimentoNovo.setClienteByIdClienteDestinatario(clienteRemetenteOriginal);
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		//Inscricao Estadual
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimentoOriginal.getInscricaoEstadualRemetente());
		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimentoOriginal.getInscricaoEstadualDestinatario());
		//Municipios de Coleta/Entrega
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio());
		//CEPs de Coleta/Entrega
		conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepColeta());
		conhecimentoNovo.setNrCepColeta(conhecimentoOriginal.getNrCepEntrega());

		conhecimentoNovo.setTpDocumentoServico(conhecimentoCalculo.getTpDocumentoServico());
		conhecimentoNovo.setTpDoctoServico(conhecimentoCalculo.getTpDocumentoServico());
		
		//Pre-Conhecimento
		conhecimentoNovo.setTpSituacaoConhecimento(new DomainValue("P"));

		conhecimentoNovo.setMoeda(conhecimentoOriginal.getMoeda());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		//ID_USUARIO_INCLUSAO = Usuário logado (USUARIO.ID_USUARIO)
		conhecimentoNovo.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		
		//ID_FILIAL_ORIGEM = Filial do usuário logado (FILIAL.ID_FILIAL)
		conhecimentoNovo.setFilialOrigem(filial);
		conhecimentoNovo.setFilialByIdFilialOrigem(filial);
		conhecimentoNovo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		//BL_BLOQUEADO = "N"
		conhecimentoNovo.setBlBloqueado(Boolean.FALSE);
		conhecimentoNovo.setPaisOrigem(conhecimentoOriginal.getPaisOrigem());

		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setPsReferenciaCalculo(conhecimentoOriginal.getPsReferenciaCalculo());
		conhecimentoNovo.setTabelaPreco(conhecimentoOriginal.getTabelaPreco());
		conhecimentoNovo.setTpCalculoPreco(conhecimentoCalculo.getTpCalculoPreco());
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		
		if(conhecimentoOriginal.getClienteByIdClienteRedespacho()!= null){
	          conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteRedespacho());
	          conhecimentoNovo.setBlRedespachoColeta(Boolean.TRUE);
		}else{
		    conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		    conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		}

		conhecimentoNovo.setClienteByIdClienteBaseCalculo(conhecimentoOriginal.getClienteByIdClienteBaseCalculo());

		/** Tabela CONHECIMENTO **/
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO));
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());

		conhecimentoNovo.setDensidade(conhecimentoOriginal.getDensidade());
		conhecimentoNovo.setDsEspecieVolume(conhecimentoOriginal.getDsEspecieVolume());
		if (conhecimentoNovo.getBlIndicadorEdi() == null) conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		conhecimentoNovo.setBlIndicadorFretePercentual(Boolean.FALSE);
		conhecimentoNovo.setBlPermiteTransferencia(Boolean.TRUE);
		conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		conhecimentoNovo.setBlSeguroRr(Boolean.FALSE);
		conhecimentoNovo.setProduto(conhecimentoOriginal.getProduto());
		conhecimentoNovo.setPsAforado(conhecimentoOriginal.getPsAforado());
		conhecimentoNovo.setDsCodigoColeta(conhecimentoOriginal.getDsCodigoColeta());
		conhecimentoNovo.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);
		conhecimentoNovo.setPsReal(conhecimentoOriginal.getPsReal());
		conhecimentoNovo.setVlMercadoria(conhecimentoOriginal.getVlMercadoria());
		conhecimentoNovo.setQtVolumes(qtdVolume);
		conhecimentoNovo.setBlProdutoControlado(conhecimentoOriginal.getBlProdutoControlado());
		conhecimentoNovo.setBlProdutoPerigoso(conhecimentoOriginal.getBlProdutoPerigoso());

		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(conhecimentoOriginal.getTpDevedorFrete().getValue())){
			conhecimentoNovo.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL));
		}else if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(conhecimentoOriginal.getTpDevedorFrete().getValue())){
		    conhecimentoNovo.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO));
		}else if(ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoOriginal.getTpFrete().getValue())
			&& !ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(conhecimentoOriginal.getTpDevedorFrete().getValue())){
			conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		} else {
			conhecimentoNovo.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO));
		}
		
		if(conhecimentoCalculo.getTpCalculoPreco().getValue().equals(ConstantesExpedicao.CALCULO_MANUAL)){
			ConhecimentoUtils.copyLiberacaoDocServs(conhecimentoCalculo, conhecimentoNovo);
		}
		
		copyDevedorDocServ(conhecimentoOriginal, conhecimentoNovo);

		//Seta Enderecos de Entrega
		setLocalEntregaConhecimento(conhecimentoNovo);
		//Altera CFOP
		setNrCfop(conhecimentoNovo);
		//Altera Rota Entrega Sugerida//
		setRotaColetaEntregaSugerida(conhecimentoNovo);

		//Tabela VALOR_CUSTO
		TipoCusto tipoCusto = tipoCustoService.findByDsTipoCusto(ConstantesExpedicao.TIPO_ICMS);
		ConhecimentoUtils.copyValorCusto(conhecimentoNovo,tipoCusto);
        //Tabela DADO_COMPLEMENTO
        ConhecimentoUtils.copyDadosComplemento(conhecimentoOriginal, conhecimentoNovo);
		//Tabela NOTA_FISCAL_CONHECIMENTO
		ConhecimentoUtils.copyNotasFiscaisConhecimento(conhecimentoOriginal, conhecimentoNovo, notasFiscaisConhecimento, isEntregaParcial);
		//Tabela PARCELA_DOCTO_SERVICO
		ConhecimentoUtils.copyParcelasDoctoServico(conhecimentoCalculo, conhecimentoNovo);
		//Tabela OBSERVACAO_DOCTO_SERVICOS
		String dsObservacaoGeral = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL);
		ConhecimentoUtils.copyObservacoesDoctoServico(conhecimentoCalculo, conhecimentoNovo, dsObservacaoGeral);
		String dsProdutoPerigoso = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_PERIGOSO);
		String dsProdutoControlado = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_CONTROLADO);
		conhecimentoNovo.getObservacaoDoctoServicos().addAll(ConhecimentoUtils.criaObservacoesDoctoServicoPerigosoControlado(conhecimentoNovo, dsProdutoPerigoso, dsProdutoControlado));
		
		//Tabela IMPOSTO_SERVICO
		ConhecimentoUtils.copyImpostoServico(conhecimentoCalculo, conhecimentoNovo);

		MonitoramentoDescarga monitoramentoDescarga = createMonitoramentoDescarga(conhecimentoOriginal, qtdVolume);
		monitoramentoDescarga = (MonitoramentoDescarga) monitoramentoDescargaService.store(monitoramentoDescarga);
		createVolumeNotaFiscal(conhecimentoNovo, mapNotasFiscaisConhecimento, monitoramentoDescarga);
		
		if (!isEntregaParcial) {
            /* LMS-1211 */
            if (conhecimentoOriginal.getBlBloqueado()) {
                //Inclui Eventos
                String nrCtrc = ConhecimentoUtils.formatConhecimento(
                        conhecimentoOriginal.getFilialByIdFilialOrigem().getSgFilial(),
                        conhecimentoOriginal.getNrConhecimento());

                Short[] cdOcorrencias = new Short[]{
                    ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_DEVOLVIDA,
                    ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_DEVOLVIDA_LEG
                };

                List<OcorrenciaDoctoServico> ocorrenciaDoctoList = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoByCdOcorrencia(conhecimentoOriginal.getIdDoctoServico(), cdOcorrencias, false);
                OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_DEVOLVIDA);

                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
                        idConhecimentoOriginal,
                        conhecimentoOriginal.getFilialByIdFilialOrigem().getIdFilial(),
                        nrCtrc,
                        JTDateTimeUtils.getDataHoraAtual(),
                        null,
                        null,
                        conhecimentoOriginal.getTpDocumentoServico().getValue(),
                        (Long) null,
                        ocorrenciaPendencia == null ? null : ocorrenciaPendencia.getIdOcorrenciaPendencia(),
                        Boolean.FALSE,
						SessionUtils.getUsuarioLogado()
                );

                if (ocorrenciaDoctoList.size() > 0) {
                    ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(conhecimentoOriginal.getIdDoctoServico(), ocorrenciaPendencia.getIdOcorrenciaPendencia(), null, faseProcesso);
                }
            } else {
                Short cdLocalizacaoMercadoria = localizacaoMercadoriaService.findById(((DoctoServico) conhecimentoOriginal).getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()).getCdLocalizacaoMercadoria();

                if (cdLocalizacaoMercadoria.compareTo(ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA) != 0) {
                    throw new BusinessException("LMS-04094");
                }
            }
		}
		
		//Cancela Recibos de Reembolso Ativos do Conhecimento
		reciboReembolsoService.executeCancelaReciboByDoctoServico(idConhecimentoOriginal);

		//Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO.
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimentoOriginal.getDevedorDocServs().get(0)).getCliente();
		generateObsDoctoClienteByObsConhecimento(clienteResponsavel, conhecimentoNovo);

		//Define o Fluxo de Carga
		setFluxoCarga(conhecimentoNovo, blBloqueiaSubcontratada);

		conhecimentoNovo.setTpCargaDocumento(conhecimentoOriginal.getTpCargaDocumento());
		
		Long idClienteAtendido = conhecimentoNovo.getClienteByIdClienteDestinatario().getIdCliente();
		SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);
		
		/*
		 * Adicionado esse código por causa do desvio de carga
		 * */
		Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();
		Map atendimento = ppeService.findAtendimentoMunicipio(
			conhecimentoNovo.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
			conhecimentoNovo.getServico().getIdServico(),
			Boolean.FALSE,
			JTDateTimeUtils.getDataAtual(),
			conhecimentoNovo.getNrCepEntrega(),
			idClienteAtendido, 
			conhecimentoNovo.getClienteByIdClienteRemetente().getIdCliente(),
			segmentoMercado.getIdSegmentoMercado(),
			municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa(),
			filial.getIdFilial(),
			"N", 
			conhecimentoNovo.getNaturezaProduto().getIdNaturezaProduto(),
			true
		);		
		Filial filialDestino = filialService.findById(MapUtils.getLong(atendimento, "idFilial"));
		conhecimentoNovo.setFilialByIdFilialDestino(filialDestino);
		conhecimentoNovo.setFilialDestinoOperacional(filialDestino);	
		
		Serializable result = conhecimentoService.store(conhecimentoNovo);
		
		if (isEntregaParcial) {
		    this.storeNotaFiscalOperada(notasFiscaisConhecimento, conhecimentoNovo);
		}
		
		liberacaoNotaNaturaService.atualizaTerraNaturaReentregaDevolucaoDigitado(conhecimentoOriginal, conhecimentoNovo);
		
		validateContingencia(conhecimentoNovo, monitoramentoDescarga);

		return result;
	}
	
	private void storeNotaFiscalOperada(List<NotaFiscalConhecimento> notasFiscaisConhecimento, Conhecimento conhecimento) {
        List<NotaFiscalOperada> notasFiscaisOperadas = new ArrayList<NotaFiscalOperada>();
        
        if(CollectionUtils.isEmpty(notasFiscaisConhecimento)){
            throw new BusinessException("LMS-17038");
        }
        
        for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
            NotaFiscalOperada notaFiscalOperada = new NotaFiscalOperada();
            notaFiscalOperada.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimento);
            notaFiscalOperada.setTpSituacao(new DomainValue("DV"));
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
	
	@SuppressWarnings("unchecked")
	private void validateContingencia(Conhecimento conhecimento, MonitoramentoDescarga monitoramentoDescarga) {
		Long idFilial 				= SessionUtils.getFilialSessao().getIdFilial();
		Contingencia contingencia 	= getContingenciaService().findByFilial(idFilial, "A", "E");

		//LMS-2672
		if(ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento)){
			executeAtualizaCTRCSemEtiqueta(conhecimento, monitoramentoDescarga.getIdMonitoramentoDescarga());
		}else if (contingencia != null) {
			executeAtualizaCTRCSemEtiqueta(conhecimento, monitoramentoDescarga.getIdMonitoramentoDescarga());
		}
	}

	private MonitoramentoDescarga createMonitoramentoDescarga(Conhecimento conhecimentoOriginal, Integer totalqtdVolume) {
		MonitoramentoDescarga monitoramentoDescarga = new MonitoramentoDescarga();
		monitoramentoDescarga.setFilial(SessionUtils.getFilialSessao());
		monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA));
		monitoramentoDescarga.setDhChegadaVeiculo(conhecimentoOriginal.getDtColeta());
		monitoramentoDescarga.setNrFrota(ConstantesExpedicao.NR_FROTA_DEVOLUCAO);
		monitoramentoDescarga.setNrPlaca(ConstantesExpedicao.NR_FROTA_DEVOLUCAO);
		monitoramentoDescarga.setQtVolumesTotal(Long.valueOf(totalqtdVolume));
		monitoramentoDescarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		return monitoramentoDescarga;
	}
	
	private void createVolumeNotaFiscal(Conhecimento conhecimentoNovo, List<Map<String, Object>> notas, MonitoramentoDescarga monitoramentoDescarga) {
		VolumeNotaFiscal volumeNotaFiscal = null;
		Integer nrSequencia = Integer.valueOf(0);
		NotaFiscalConhecimento notaFiscalConhecimento = null;
		for (Map<String, Object> nota : notas) {
			BigDecimal nrVolumeColetaInicial = nota.get("nrVolumeColetaInicial") == null ? BigDecimal.ZERO : new BigDecimal((Long) nota.get("nrVolumeColetaInicial"));
			BigDecimal nrVolumeColetaFinal = nota.get("nrVolumeColetaInicial") == null ? BigDecimal.ONE : new BigDecimal((Long) nota.get("nrVolumeColetaFinal"));
			notaFiscalConhecimento = findNotaFiscalConhecimentoByIdNota((Integer) nota.get("nrNotaFiscal"), conhecimentoNovo);
			if(notaFiscalConhecimento.getVolumeNotaFiscais() == null) {
				notaFiscalConhecimento.setVolumeNotaFiscais(new LinkedList());
			}
			int qtVolumes = nrVolumeColetaFinal.subtract(BigDecimal.ONE).subtract(nrVolumeColetaInicial).intValue();
			for (int i = 0; i < qtVolumes; i++) {
				volumeNotaFiscal = new VolumeNotaFiscal();
				volumeNotaFiscal.setMonitoramentoDescarga(monitoramentoDescarga);
				volumeNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
				volumeNotaFiscal.setNrSequencia(++nrSequencia);
				volumeNotaFiscal.setNrSequenciaPalete(nrSequencia);
				String nrVolumeColetaInicialStr = String.valueOf(nrVolumeColetaInicial.add(new BigDecimal(i+1)));
				volumeNotaFiscal.setNrVolumeColeta(FormatUtils.fillNumberWithZero(nrVolumeColetaInicialStr, 12));
				volumeNotaFiscal.setQtVolumes(BooleanUtils.isTrue(conhecimentoNovo.getBlPaletizacao()) ? 0  : 1);
				volumeNotaFiscal.setTpVolume(BooleanUtils.isTrue(conhecimentoNovo.getBlPaletizacao()) ? "M"  : "U");
				notaFiscalConhecimento.getVolumeNotaFiscais().add(volumeNotaFiscal);
			}
		}
	}
	
	private NotaFiscalConhecimento findNotaFiscalConhecimentoByIdNota(Integer nrNotaFiscal, Conhecimento conhecimentoNovo) {
		List<NotaFiscalConhecimento> notas = conhecimentoNovo.getNotaFiscalConhecimentos();
		for (NotaFiscalConhecimento notaFiscalConhecimento : notas) {
			if(notaFiscalConhecimento.getNrNotaFiscal().equals(nrNotaFiscal)) {
				return notaFiscalConhecimento;
			}
		}
		return null;
	}
	
	private void copyDevedorDocServ(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		//Tabela DEVEDOR_DOC_SERV (Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original):
		
		Cliente devedorOriginal = null;
		List<DevedorDocServ> devedoresOriginal = conhecimentoOriginal.getDevedorDocServs();
		for (DevedorDocServ devedorDocServ : devedoresOriginal) {
			devedorOriginal = devedorDocServ.getCliente();
		}
		
		InscricaoEstadual inscricaoEstadualDevedorOriginal = getInscricaoEstadualService().findIeByIdPessoaAtivoPadrao(devedorOriginal.getIdCliente());

		ConhecimentoUtils.copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo, inscricaoEstadualDevedorOriginal);
		//Tabela DEVEDOR_DOC_SERV_DEV
		ConhecimentoUtils.copyDevedoresDoctoServicoFaturamento(conhecimentoOriginal, conhecimentoNovo, new DomainValue("P"));

		//Se TP_FRETE (do CTRC original) = FOB e TP_DEVEDOR_FRETE (do CTRC original) = "D" (Destinatário) 
		// - ID_CLIENTE = ID_CLIENTE_REMETENTE do CTRC original
		// - TP_DEVEDOR_FRETE = "R" (Remetente)
		if(ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoOriginal.getTpFrete().getValue())
			&& ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(conhecimentoOriginal.getTpDevedorFrete().getValue())
		) {
			List<DevedorDocServ> devedores = conhecimentoNovo.getDevedorDocServs();
			for (DevedorDocServ devedorDocServ : devedores) {
				devedorDocServ.setCliente(conhecimentoOriginal.getClienteByIdClienteRemetente());
				devedorDocServ.setInscricaoEstadual(getInscricaoEstadualService().findIeByIdPessoaAtivoPadrao(conhecimentoOriginal.getClienteByIdClienteRemetente().getIdCliente()));
			}

			List<DevedorDocServFat> faturas = conhecimentoNovo.getDevedorDocServFats();
			for (DevedorDocServFat devedorDocServFat : faturas) {
				devedorDocServFat.setCliente(conhecimentoOriginal.getClienteByIdClienteRemetente());
				devedorDocServFat.setDivisaoCliente(getDivisaoClienteDevedor(conhecimentoOriginal.getClienteByIdClienteBaseCalculo(), conhecimentoOriginal.getClienteByIdClienteRemetente(), null, conhecimentoNovo.getServico()));
			}
		}
		
		if(ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimentoNovo.getTpFrete().getValue()) ||
				(ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoNovo.getTpFrete().getValue()) && 
						conhecimentoNovo.getClienteByIdClienteConsignatario() != null)) {
			List<DevedorDocServ> devedores = conhecimentoNovo.getDevedorDocServs();
			for (DevedorDocServ devedorDocServ : devedores) {
				devedorDocServ.setFilial(conhecimentoNovo.getFilialOrigem());
			}
			List<DevedorDocServFat> faturas = conhecimentoNovo.getDevedorDocServFats();
			for (DevedorDocServFat devedorDocServFat : faturas) {
				devedorDocServFat.setFilial(conhecimentoNovo.getFilialOrigem());
			}
		} else if(ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoNovo.getTpFrete().getValue()) 
				&& conhecimentoNovo.getClienteByIdClienteConsignatario() == null) {
			List<DevedorDocServ> devedores = conhecimentoNovo.getDevedorDocServs();
			for (DevedorDocServ devedorDocServ : devedores) {
				devedorDocServ.setFilial(conhecimentoNovo.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel());
			}
			List<DevedorDocServFat> faturas = conhecimentoNovo.getDevedorDocServFats();
			for (DevedorDocServFat devedorDocServFat : faturas) {
				devedorDocServFat.setFilial(conhecimentoNovo.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel());
			}
		}
	}

	/**
	 * Busca Conhecimento de Devolucao
	 * @param nrConhecimento
	 * @param idFilialOrigem
	 * @return
	 */
	public List<Map<String, Object>> findConhecimentoDevolucao(Long nrConhecimento, Long idFilialOrigem, String tpDoctoServico) {
		/* CQPRO00028986 - Ajuste para pegar conhecimentos com status diferente de Cancelado e Pré-conhecimento */
		List<Map<String, Object>> result = conhecimentoService.findByNrConhecimentoIdFilialOrigem(nrConhecimento, idFilialOrigem, "", tpDoctoServico);
		if(result.isEmpty()) {
			return null;
		} else {
			if(result.size() > 1){
				return null;
			}
			String tpSituacaoConhecimento = (String)((Map)result.get(0).get("tpSituacaoConhecimento")).get("value"); 
			if(tpSituacaoConhecimento.equals("C") || tpSituacaoConhecimento.equals("P"))
				return null;						
		}
		
		Map<String, Object> conhecimento = result.get(0);
		Short cdLocalizacaoMercadoria = (Short) conhecimento.get("cdLocalizacaoMercadoria");
		long idDoctoServico = (Long) conhecimento.get("idDoctoServico"); 		
		boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(idDoctoServico);
		conhecimento.put("isEntregaParcial", isEntregaParcial);
		
		if (isEntregaParcial) {
			if (!cdLocalizacaoMercadoria.equals(ConstantesExpedicao.CD_MERCADORIA_NO_TERMINAL) &&
			    !cdLocalizacaoMercadoria.equals(ConstantesExpedicao.CD_MERCADORIA_RETORNADA_NO_TERMINAL)) {
				throw new BusinessException("LMS-04094");
			}	
			
			Map<String, Boolean> validacaoNF = this.validateExisteOcorrenciaEntregaNF(idDoctoServico);
			String msg = tpDoctoServico + " " + conhecimento.get("sgFilialOrigem") + conhecimento.get("nrDoctoServico");

			if (!validacaoNF.get("blExisteNFPendente").booleanValue()) {
				throw new BusinessException("LMS-04575", new Object[]{ msg });
			}

			if (!validacaoNF.get("blExisteOcorrencia").booleanValue()) {
				throw new BusinessException("LMS-04576", new Object[]{ msg });
			}
		} else {
			/* LMS-1211 */
			boolean blBloqueado = (Boolean) conhecimento.get("blBloqueado");
			if(blBloqueado){
				Short[] cdOcorrencias = new Short[]{
						ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_DEVOLVIDA,
						ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_DEVOLVIDA_LEG
				};
				List<OcorrenciaDoctoServico> ocorrenciaDoctoList = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoByCdOcorrencia(idDoctoServico, cdOcorrencias, false);
				if(ocorrenciaDoctoList.isEmpty()){
					throw new BusinessException("LMS-04094");
				}
			}
			else{
				if(cdLocalizacaoMercadoria.shortValue() != ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA.shortValue()){
					throw new BusinessException("LMS-04094");
				}	
			}


			Map<String, Object> tpConhecimento = (Map<String, Object>) conhecimento.get("tpConhecimento");
			if(!ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(tpConhecimento.get("value")) && !ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(tpConhecimento.get("value"))) {
				throw new BusinessException("LMS-04094");
			}
	
			// Regra 1.4
			List<EventoDocumentoServico> eventosEntrega = eventoDocumentoServicoService.findEventoDoctoServicoComCodEntregaParcial(idDoctoServico, ConstantesSim.EVENTO_ENTREGA, new DomainValue("R"), Boolean.FALSE);
			if(!eventosEntrega.isEmpty()) {
				throw new BusinessException("LMS-04094");
			}
		}	
		
		List<Map<String, Object>> notasFiscaisConhecimento = createNotasFiscaisConhecimento((Long) conhecimento.get("idDoctoServico"));
		
		if (isEntregaParcial) {
			notasFiscaisConhecimento = this.filtraNfDisponivel(notasFiscaisConhecimento, notaFiscalConhecimentoService.findNfDisponivel(idDoctoServico));
		}
		conhecimento.put("notasFiscaisConhecimento", notasFiscaisConhecimento);
		
		Long idClienteRemetente = (Long) ((Map) conhecimento.get("remetente")).get("idCliente");
		DateTime dhEmissao = (DateTime) conhecimento.get("dhEmissao");
		Map<String, Object> remetente = (Map<String, Object>) conhecimento.get("remetente");
		Boolean blPermiteCte = (Boolean) remetente.get("blPermiteCte");
		
		Boolean isDocumentoEletronico = conhecimentoReentregaService.isDocumentoEletronico(idDoctoServico, blPermiteCte); 
		conhecimento.put("isDocumentoEletronico", isDocumentoEletronico);
		
		Boolean isPinSuframaRequired = digitarNotaService.findObrigatoriedadePinSuframa((Long) remetente.get("idCliente"), null);
		conhecimento.put("isPinSuframaRequired", isPinSuframaRequired);
		
		liberacaoNotaNaturaService.validateTerraNaturaCTRCReentregaDevolucao(idDoctoServico, idFilialOrigem, idClienteRemetente, dhEmissao, ConstantesExpedicao.NM_PARAMETRO_TP_SERVICO_DEVOLUCAO_NATURA);
		
		return result;
	}
	
	public Map<String, Boolean> validateExisteOcorrenciaEntregaNF(long idDoctoServico) {
		Map<String, Boolean> retorno = new HashMap<String, Boolean>();
		boolean blExisteNFPendente = true;
		boolean blExisteOcorrencia = false;
		
		List<Long> notasFiscaisConhecimentoNaoOperadas = notaFiscalConhecimentoService.findNfDisponivel(idDoctoServico);
		
		if (notasFiscaisConhecimentoNaoOperadas.isEmpty()) {
			blExisteNFPendente = false;
		} else if (this.validaNotasPossuemEntregaParaUltimoManifesto(idDoctoServico, notasFiscaisConhecimentoNaoOperadas)) {
			blExisteOcorrencia = true;
		}
		
		retorno.put("blExisteNFPendente", blExisteNFPendente);
		retorno.put("blExisteOcorrencia", blExisteOcorrencia);
		
		return retorno;
	}
	
	private List<Map<String, Object>> filtraNfDisponivel(List<Map<String, Object>> notasFiscaisConhecimento, 
	List<Long> notasFiscaisConhecimentoNaoEntregue) {
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
	
	private boolean validaNotasPossuemEntregaParaUltimoManifesto(Long idDoctoServico, List<Long> notasFiscaisConhecimentoNaoEntregue) {
		return notasFiscaisConhecimentoNaoEntregue.size() == entregaNotaFiscalService.findNotasComOcorrenciaEntrega(
		  manifestoService.findIdUltimoManifestoEntregaOrViagemEntregaDireta(idDoctoServico),
		  notasFiscaisConhecimentoNaoEntregue
		).size();
	}

	private List<Map<String, Object>> createNotasFiscaisConhecimento(Long idDoctoServico){
		List<Map<String, Object>> notasFiscaisConhecimento = notaFiscalConhecimentoService.findNFByIdDoctoServico(idDoctoServico);
		return populateTipoDocumentoDescricao(notasFiscaisConhecimento);
	}
	
	private List<Map<String, Object>> populateTipoDocumentoDescricao(List<Map<String, Object>> notasFiscaisConhecimento){
		for (Map<String, Object> map : notasFiscaisConhecimento) {
			map.put("tpDocumentoDesc", map.get("tpDocumento") == null ? "" : domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_CARGA", map.get("tpDocumento").toString()).getDescriptionAsString());
		}
		return notasFiscaisConhecimento;
	}

	/**
	 * Verifica se já existe um Conhecimento de devolução para o Conhecimento selecionado.
	 * @param conhecimento
	 */
	public void validateConhecimentoDevolucao(Conhecimento conhecimento) {
		DoctoServico doctoServicoOriginal = conhecimento.getDoctoServicoOriginal();
		boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(doctoServicoOriginal.getIdDoctoServico());

		if (!isEntregaParcial) {
			Integer count = conhecimentoService.getRowCountByDoctoServicoOriginal(doctoServicoOriginal.getIdDoctoServico(), doctoServicoOriginal.getTpDocumentoServico().getValue(), ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO);
			if(count.intValue() > 0) {
				throw new BusinessException("LMS-04155");
			}
		}
		
		if( (JTDateTimeUtils.comparaData(conhecimento.getDtAutDevMerc(), JTDateTimeUtils.getDataAtual()) > 0) ||
				(JTDateTimeUtils.comparaData(conhecimento.getDtAutDevMerc(), doctoServicoOriginal.getDhEmissao().toYearMonthDay()) < 0) ) {
			throw (new BusinessException("LMS-04035"));
		}
	}

	public void executeValidacoesParaBloqueioValores(TypedFlatMap param) {
		Conhecimento conhecimento = (Conhecimento) param.get("conhecimento");

		TypedFlatMap map = new TypedFlatMap();
		map.put("vlFrete", conhecimento.getVlTotalDocServico());

		if (conhecimento.getDoctoServicoOriginal() != null) {
			if (conhecimento.getDoctoServicoOriginal().getVlMercadoria() == null) {
				map.put("vlMercadoria", doctoServicoService.findById(conhecimento.getDoctoServicoOriginal().getIdDoctoServico()).getVlMercadoria());
			} else {
				map.put("vlMercadoria", conhecimento.getDoctoServicoOriginal().getVlMercadoria());
			}
		}

		doctoServicoService.executeValidacaoLimiteValorFrete(map);
		doctoServicoService.executeValidacaoPercentualValorMercadoria(map);
	}
	
	/**
	 * Busca e Seta Endereco de Entrega Real do Conhecimento.
	 * 
	 * @author Andre Valadas.
	 * @param conhecimento
	 */
	private void setLocalEntregaConhecimento(Conhecimento conhecimento) {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(conhecimento.getClienteByIdClienteDestinatario().getIdCliente());
		conhecimento.setDsEnderecoEntregaReal(ConhecimentoUtils.getEnderecoEntregaReal(enderecoPessoa));
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}

	public DigitarNotaService getDigitarNotaService() {
		return digitarNotaService;
	}

	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	
	public void setEntregaNotaFiscalService(EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setConhecimentoReentregaService(ConhecimentoReentregaService conhecimentoReentregaService) {
		this.conhecimentoReentregaService = conhecimentoReentregaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
    public void setNotaFiscalOperadaService(NotaFiscalOperadaService notaFiscalOperadaService) {
        this.notaFiscalOperadaService = notaFiscalOperadaService;
    }
}
