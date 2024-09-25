package com.mercurio.lms.financeiro.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.dao.CedenteDAO;
import com.mercurio.lms.contasreceber.model.dao.PreFaturaDAO;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.model.service.GerarFaturaArquivoRecebidoService;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;

import br.com.tntbrasil.integracao.domains.financeiro.DocumentoPreFatura;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;

public class PreFaturaNaturaService {

	private static final long FILIAL_INEXISTENTE = -1L;
	private static final long CLIENTE_INEXISTENTE = -1L;
	
	private static final Integer LINHA_INICIAL = 1;
	
	private PreFaturaDAO preFaturaDAO;  
	
	private DoctoServicoService doctoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private DevedorDocServFatService devedorDocServFatService;
	private GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService;
	
	private PreFaturaNaturaValidationService preFaturaNaturaValidationService;
	private ParametroGeralService parametroGeralService;
	private CedenteDAO cedenteDAO;
	
	private DataVencimentoService dataVencimentoService;
	
	private IntegracaoJmsService integracaoJmsService;
	
	private FilialService filialService;

	public FaturaDMN executeImportarPreFaturaNatura(List<DocumentoPreFatura> documentos) {
		Integer linha = LINHA_INICIAL;
		Long idFilialAtual = FILIAL_INEXISTENTE;
		Long idClienteAtual = CLIENTE_INEXISTENTE;
		if(documentos == null || documentos.size() < 1){
			return null;
		}
		List<DevedorDocServFat> devedores = new ArrayList<DevedorDocServFat>();
		List<Long> devedorIds = new ArrayList<Long>();
		List<DoctoServico> doctoServicos = new ArrayList<DoctoServico>();
		for(DocumentoPreFatura documento : documentos){
			validateIsCamposPreenchidos(documento, linha);
			PreFaturaResult preFaturaResult = importarPreFatura(documento, linha, idFilialAtual, idClienteAtual);
			devedores.add(preFaturaResult.getDevedor());
			devedorIds.add(preFaturaResult.getDevedor().getIdDevedorDocServFat());
			doctoServicos.add(preFaturaResult.getDoctoServico());
			idFilialAtual = preFaturaResult.getIdFilial();
			idClienteAtual = preFaturaResult.getIdCliente();
			linha++;
		}

		FaturaDMN faturaResult = convertToFaturaCanonico(storeFatura(devedorIds, doctoServicos, devedores, documentos.get(0)), documentos.get(0).getCdFilialNatura());
		
		enviarParaFila(faturaResult);
		
		return faturaResult;
	}


	private void enviarParaFila(FaturaDMN faturaResult) {
		JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FINANCEIRO_FATURA_NATURA_ACEITE);
		jmsMessageSender.addMsg(faturaResult);			
		integracaoJmsService.storeMessage(jmsMessageSender);		
	}


	private FaturaDMN convertToFaturaCanonico(
			com.mercurio.lms.contasreceber.model.Fatura storeFatura, String cdFilialNatura) {
		
		FaturaDMN fatura = new FaturaDMN();
		fatura.setIdFatura(storeFatura.getIdFatura());
		fatura.setIdFilial(storeFatura.getFilialByIdFilial().getIdFilial());
		fatura.setSgFilial(storeFatura.getFilialByIdFilial().getSgFilial());
		if(storeFatura.getFilialByIdFilial().getPessoa() != null){
			fatura.setNrIdentificacaoFilial(storeFatura.getFilialByIdFilial().getPessoa().getNrIdentificacao());
			fatura.setNmPessoaFilial(storeFatura.getFilialByIdFilial().getPessoa().getNmPessoa());
		}
		fatura.setNrFatura(storeFatura.getNrFatura());
		fatura.setDtEmissao(storeFatura.getDtEmissao());
		fatura.setCdFilialNatura(cdFilialNatura);
		fatura.setNrPrefatura(storeFatura.getNrPreFatura());
		fatura.setDtPrefatura(storeFatura.getDtPreFatura());
		fatura.setDsDestinatarioEmailRetorno(getValorParametroGeralDestinatario());
		
		return fatura;
	}

	
	private String getValorParametroGeralDestinatario() {
		return (String) parametroGeralService.findConteudoByNomeParametro("DS_DEST_EMAIL_PRE_NATURA", false);
	}


	private PreFaturaResult importarPreFatura(DocumentoPreFatura documento, Integer linha, Long idFilialAtual, Long idClienteAtual) {

		Filial filial = findFilial(documento.getCdFilialNatura());
		preFaturaNaturaValidationService.validateExistsFilial(documento, linha, filial);
		
		DoctoServico dctoServico = findDocumentoServico(filial.getIdFilial(), documento.getNrDocumento(), documento.getDtEmissao());
		preFaturaNaturaValidationService.validateExistsDoctoServico(documento, dctoServico, linha, filial);
		
		DevedorDocServFat devedorDoc = findDevedor(dctoServico);
		
		preFaturaNaturaValidationService.validatePrimeiroDocumentoDeServicoPossuiDivisaoDeCobranca(devedorDoc, linha);
		
		preFaturaNaturaValidationService.validateFiliaisDeCobrancaIguais(linha, devedorDoc, idFilialAtual);

		preFaturaNaturaValidationService.validateClienteIgual(linha, devedorDoc, idClienteAtual);
		
		preFaturaNaturaValidationService.validateDoctoServicoNaoLiquidado(linha, devedorDoc);
		
		preFaturaNaturaValidationService.validateDoctoServicoNaoFaturado(linha, devedorDoc);
		
		preFaturaNaturaValidationService.validateNotaFiscalAutorizada(linha, dctoServico);
		
		preFaturaNaturaValidationService.validateNaoTemDescontoInvalido(linha, dctoServico, devedorDoc);
		
		preFaturaNaturaValidationService.validateSituacaoDoctoServico(linha, dctoServico);
		
		return new PreFaturaResult(devedorDoc, devedorDoc.getFilial().getIdFilial(), devedorDoc.getCliente().getIdCliente(), dctoServico);
	}

	private com.mercurio.lms.contasreceber.model.Fatura storeFatura(List<Long> devedorIds, List<DoctoServico> doctoServicos, List<DevedorDocServFat> devedores, DocumentoPreFatura primeiroDocumentoPreFatura) {
		
		com.mercurio.lms.contasreceber.model.Fatura faturaStore = gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(populateFatura(devedores, doctoServicos, primeiroDocumentoPreFatura),devedorIds);
		
		faturaStore.setFilialByIdFilial( filialService.findById(faturaStore.getFilialByIdFilial().getIdFilial()));
		
		return faturaStore;
	}


	private com.mercurio.lms.contasreceber.model.Fatura populateFatura(List<DevedorDocServFat> devedores, List<DoctoServico> doctoServicos, DocumentoPreFatura primeiroDocumentoPreFatura) {
		
		com.mercurio.lms.contasreceber.model.Fatura fatura = new com.mercurio.lms.contasreceber.model.Fatura();
		
		fatura.setFilialByIdFilial(new Filial(devedores.get(0).getFilial().getIdFilial()));
		fatura.setCliente(devedores.get(0).getCliente());
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(devedores.get(0).getDivisaoCliente().getIdDivisaoCliente());
		fatura.setDivisaoCliente(divisaoCliente);
		
		Cedente cedente = new Cedente();
		cedente.setIdCedente(cedenteDAO.findIdCedenteAtual());
		fatura.setCedente(cedente );
		
		Servico servico = new Servico();
		servico.setIdServico(doctoServicos.get(0).getServico().getIdServico());
		fatura.setServico(servico );
		
		fatura.setTpModal(doctoServicos.get(0).getServico().getTpModal());
		fatura.setTpAbrangencia(doctoServicos.get(0).getServico().getTpAbrangencia());
		fatura.setNrPreFatura(primeiroDocumentoPreFatura.getNrPreFatura().toString());
		fatura.setDtPreFatura(primeiroDocumentoPreFatura.getDtPreFatura().toYearMonthDay());
		fatura.setDtImpotacao(JTDateTimeUtils.getDataAtual());
		fatura.setDtEmissao(JTDateTimeUtils.getDataAtual());
		fatura.setDtVencimento(dataVencimentoService.generateDataVencimento(
				devedores.get(0).getFilial().getIdFilial(), 
				devedores.get(0).getDivisaoCliente().getIdDivisaoCliente(),
				"C",
				JTDateTimeUtils.getDataAtual(),
				doctoServicos.get(0).getServico().getTpModal().getValue(),
				doctoServicos.get(0).getServico().getTpAbrangencia().getValue(),
				doctoServicos.get(0).getServico().getIdServico()
				));
		fatura.setBlGerarBoleto(false);
		fatura.setBlGerarEdi(true);
		fatura.setTpFatura(new DomainValue("R"));
		fatura.setTpOrigem(new DomainValue("P"));
		fatura.setQtDocumentos(doctoServicos.size());
		
		return fatura;
	}



	public Filial findIdFilialPrimeiroDocumento(List<DocumentoPreFatura> documentos){

		validateDocumentosNotEmpty(documentos);
		DocumentoPreFatura documento = documentos.get(0);
		
		validateIsCamposPreenchidos(documento, LINHA_INICIAL);
		
		Filial filial = findFilial(documento.getCdFilialNatura());
		preFaturaNaturaValidationService.validateExistsFilial(documento, LINHA_INICIAL, filial);
		
		DoctoServico dctoServico = findDocumentoServico(filial.getIdFilial(), documento.getNrDocumento(), documento.getDtEmissao());
		preFaturaNaturaValidationService.validateExistsDoctoServico(documento, dctoServico, LINHA_INICIAL, filial);
		
		DevedorDocServFat devedorDoc = findDevedor(dctoServico);
		
		return devedorDoc.getFilial();
	}

	private DevedorDocServFat findDevedor(DoctoServico docto) {
		DevedorDocServFat devedorDoc = null;
		List doctos = devedorDocServFatService.findDevedorDocServFatByDoctoServico(docto.getIdDoctoServico());
		if ( doctos != null || !doctos.isEmpty() ){
			Map<String,Object> doctServFatMap = (Map<String,Object>) doctos.get(0);
			devedorDoc = devedorDocServFatService.findById((Long)doctServFatMap.get("idDevedorDocServFat"));
		}
		return devedorDoc;
	}




	private DoctoServico findDocumentoServico(Long idFilial,
			Long nrDocumento, DateTime dtEmissao) {
		return doctoServicoService.findDoctoServicoByFilialNumeroEDhEmissao(idFilial, nrDocumento, dtEmissao);
	}

	private Filial findFilial(String cdFilialNatura) {
		Filial filial = null;
		Long idFilial = null;
		try{
			idFilial = preFaturaDAO.findDeParaFilialNatura(cdFilialNatura);
		}catch(Exception e){
			return filial;
		}
		if(idFilial!=null){
			filial = new Filial();
			filial.setIdFilial(idFilial);
			filial.setSgFilial(cdFilialNatura);
		}
		return filial;
	}

	private void validateIsCamposPreenchidos(DocumentoPreFatura documento, Integer linha) {
		
		if(!isPreenchidoDocumento(documento)){
			 throw new PreFaturaNaturaException("Linha " + linha + ": " + configuracoesFacade.getMensagem("LMS-36337"));
		}

	}

	private boolean isPreenchidoDocumento(DocumentoPreFatura documento) {
		
		return documento.getNrDocumento() != null && documento.getDtEmissao() != null && documento.getCdFilialNatura() != null 
				&& documento.getNrPreFatura() != null && documento.getDtPreFatura() != null;
	}


	public void validateDocumentosNotEmpty(List<DocumentoPreFatura> documentos) {
		if(documentos.size() < 1){
			 throw new PreFaturaNaturaException( configuracoesFacade.getMensagem("LMS-36337"));
		}
	}
	
	static class PreFaturaResult{
		private DevedorDocServFat devedor;
		private Long idFilial;
		private Long idCliente;
		private DoctoServico doctoServico;
		
		public PreFaturaResult(DevedorDocServFat devedor, Long idFilial,
				Long idCliente, DoctoServico doctoServico) {
			super();
			this.devedor = devedor;
			this.idFilial = idFilial;
			this.idCliente = idCliente;
			this.doctoServico = doctoServico;
		}
		public DevedorDocServFat getDevedor() {
			return devedor;
		}
		public Long getIdFilial() {
			return idFilial;
		}
		public Long getIdCliente() {
			return idCliente;
		}
		public DoctoServico getDoctoServico() {
			return doctoServico;
		}
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setGerarFaturaArquivoRecebidoService(
			GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService) {
		this.gerarFaturaArquivoRecebidoService = gerarFaturaArquivoRecebidoService;
	}
	public void setPreFaturaDAO(PreFaturaDAO preFaturaDAO) {
		this.preFaturaDAO = preFaturaDAO;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	
	
	public void setPreFaturaNaturaValidationService(
			PreFaturaNaturaValidationService preFaturaNaturaValidationService) {
		this.preFaturaNaturaValidationService = preFaturaNaturaValidationService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setCedenteDAO(CedenteDAO cedenteDAO) {
		this.cedenteDAO = cedenteDAO;
	}
	public void setDataVencimentoService(
			DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}
	
	public void setIntegracaoJmsService(
			IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}