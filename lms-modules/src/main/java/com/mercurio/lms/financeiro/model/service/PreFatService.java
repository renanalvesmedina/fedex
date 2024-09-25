package com.mercurio.lms.financeiro.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.model.service.GerarFaturaArquivoRecebidoService;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;

import br.com.tntbrasil.integracao.domains.financeiro.DocumentoPreFatura;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;

public class PreFatService {

	private static final long FILIAL_INEXISTENTE = -1L;
	private static final long CLIENTE_INEXISTENTE = -1L;
	private static final String NUMERO_DOCUMETO = "- Número Documento ";
	private static final String DATA_EMISSAO = "- Data Emissão ";
	private static final String CNPJ_FILIAL = "- CNPJ Filial ";
	private static final String NUMERO_PRE_FATURA = "- Número Pré Fatura ";
	private static final String DATA_PRE_FATURA = "- Data Pré Fatura ";
	private static final String ARQUIVO = "Arquivo";
	
	private DoctoServicoService doctoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private DevedorDocServFatService devedorDocServFatService;
	private GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService;
	private PreFatValidationService preFatValidationService;
	private DataVencimentoService dataVencimentoService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;

	public FaturaDMN executeImportarPreFatura(List<DocumentoPreFatura> documentos) {
		boolean blPrimeiroDoc = true;
		Long idFilialAtual = FILIAL_INEXISTENTE;
		Long idClienteAtual = CLIENTE_INEXISTENTE;
		if(CollectionUtils.isEmpty(documentos)){
			return null;
		}
		List<DevedorDocServFat> devedores = new ArrayList<DevedorDocServFat>();
		List<Long> devedorIds = new ArrayList<Long>();
		List<DoctoServico> doctoServicos = new ArrayList<DoctoServico>();
		for(DocumentoPreFatura documento : documentos){
			validateIsCamposPreenchidos(documento);
			PreFaturaResult preFaturaResult = importarPreFatura(documento, idFilialAtual, idClienteAtual, blPrimeiroDoc);
			devedores.add(preFaturaResult.getDevedor());
			devedorIds.add(preFaturaResult.getDevedor().getIdDevedorDocServFat());
			doctoServicos.add(preFaturaResult.getDoctoServico());
			idFilialAtual = preFaturaResult.getIdFilial();
			idClienteAtual = preFaturaResult.getIdCliente();
			blPrimeiroDoc = false;
		}
		
		Fatura storeFatura = storeFatura(devedorIds, doctoServicos, devedores, documentos.get(0));
		FaturaDMN faturaResult = convertToFaturaCanonico(storeFatura);
		
		return faturaResult;
	}

	private FaturaDMN convertToFaturaCanonico(Fatura storeFatura) {
		
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
		fatura.setNrPrefatura(storeFatura.getNrPreFatura());
		fatura.setDtPrefatura(storeFatura.getDtPreFatura());
		
		return fatura;
	}
	
	private PreFaturaResult importarPreFatura(DocumentoPreFatura documento, Long idFilialAtual, Long idClienteAtual, boolean blPrimeiroDoc) {

		Filial filial = findFilial(documento.getCnpjFilial());
		
		preFatValidationService.validateExistsFilial(documento, filial);
		
		DoctoServico dctoServico = findDocumentoServico(filial.getIdFilial(), documento.getNrDocumento(), documento.getDtEmissao());
		
		preFatValidationService.validateExistsDoctoServico(documento, dctoServico, filial);
		
		DevedorDocServFat devedorDoc = findDevedor(dctoServico);
		
		preFatValidationService.validatePrimeiroDocumentoDeServicoPossuiDivisaoDeCobranca(devedorDoc, blPrimeiroDoc);
		
		preFatValidationService.validateFiliaisDeCobrancaIguais(devedorDoc, idFilialAtual);

		preFatValidationService.validateClienteIgual(devedorDoc, idClienteAtual);
		
		preFatValidationService.validateDoctoServicoNaoLiquidado(devedorDoc, dctoServico);
		
		preFatValidationService.validateDoctoServicoNaoFaturado(devedorDoc, dctoServico);
		
		preFatValidationService.validateNotaFiscalAutorizada(dctoServico);
		
		preFatValidationService.validateNaoTemDescontoInvalido(dctoServico, devedorDoc);
		
		preFatValidationService.validateSituacaoDoctoServico(dctoServico);
		
		return new PreFaturaResult(devedorDoc, devedorDoc.getFilial().getIdFilial(), devedorDoc.getCliente().getIdCliente(), dctoServico);
	}

	private Fatura storeFatura(List<Long> devedorIds, List<DoctoServico> doctoServicos, List<DevedorDocServFat> devedores, DocumentoPreFatura primeiroDocumentoPreFatura) {
		
		Fatura faturaStore = gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(populateFatura(devedores, doctoServicos, primeiroDocumentoPreFatura),devedorIds);
		
		faturaStore.setFilialByIdFilial( filialService.findById(faturaStore.getFilialByIdFilial().getIdFilial()));
		
		return faturaStore;
	}

	private Fatura populateFatura(List<DevedorDocServFat> devedores, List<DoctoServico> doctoServicos, DocumentoPreFatura primeiroDocumentoPreFatura) {
		
		Fatura fatura = new Fatura();
		
		fatura.setFilialByIdFilial(new Filial(devedores.get(0).getFilial().getIdFilial()));
		fatura.setCliente(devedores.get(0).getCliente());
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(devedores.get(0).getDivisaoCliente().getIdDivisaoCliente());
		fatura.setDivisaoCliente(divisaoCliente);
		
		Cedente cedente = new Cedente();
		ParametroGeral pgCedente = parametroGeralService.findByNomeParametro("ID_CEDENTE");
		Long idCedente = Long.parseLong(pgCedente.getDsConteudo());
		cedente.setIdCedente(idCedente);
		fatura.setCedente(cedente);
		
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
		
		validateIsCamposPreenchidos(documento);
		
		Filial filial = findFilial(documento.getCnpjFilial());
		preFatValidationService.validateExistsFilial(documento, filial);
		
		DoctoServico dctoServico = findDocumentoServico(filial.getIdFilial(), documento.getNrDocumento(), documento.getDtEmissao());
		preFatValidationService.validateExistsDoctoServico(documento, dctoServico, filial);
		
		DevedorDocServFat devedorDoc = findDevedor(dctoServico);
		
		return devedorDoc.getFilial();
	}

	private DevedorDocServFat findDevedor(DoctoServico docto) {
		DevedorDocServFat devedorDoc = null;
		List doctos = devedorDocServFatService.findDevedorDocServFatByDoctoServico(docto.getIdDoctoServico());
		if (CollectionUtils.isNotEmpty(doctos)){
			Map<String,Object> doctServFatMap = (Map<String,Object>) doctos.get(0);
			devedorDoc = devedorDocServFatService.findById((Long)doctServFatMap.get("idDevedorDocServFat"));
		}
		return devedorDoc;
	}

	private DoctoServico findDocumentoServico(Long idFilial, Long nrDocumento, DateTime dtEmissao) {
		return doctoServicoService.findDoctoServicoByFilialNumeroEDhEmissao(idFilial, nrDocumento, dtEmissao);
	}

	private Filial findFilial(String cnpjFilial) {
		List<String> identificacoes = new ArrayList<String>();
		identificacoes.add(cnpjFilial);
		
		List<Filial> filiais = filialService.findFilialByArrayNrIdentificacaoPessoa(identificacoes);
		
		if (filiais.isEmpty()) {
			return null;
		}
		return (Filial) filiais.get(0);
	}

	private void validateIsCamposPreenchidos(DocumentoPreFatura documento) {
		if(!isPreenchidoDocumento(documento)){
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36384", new Object[] { camposObrigatoriosNaoInformados(documento) }));
		}
	}

	private boolean isPreenchidoDocumento(DocumentoPreFatura documento) {
		return documento.getNrDocumento() != null && documento.getDtEmissao() != null && documento.getCnpjFilial() != null 
				&& documento.getNrPreFatura() != null && documento.getDtPreFatura() != null;
	}
	
	private String camposObrigatoriosNaoInformados(DocumentoPreFatura documento) {
		StringBuilder retorno = new StringBuilder();
		
		retorno.append(documento.getNrDocumento() == null ? NUMERO_DOCUMETO : "");
		retorno.append(documento.getDtEmissao() == null ? DATA_EMISSAO : "");
		retorno.append(documento.getCnpjFilial() == null ? CNPJ_FILIAL : "");
		retorno.append(documento.getNrPreFatura() == null ? NUMERO_PRE_FATURA : "");
		retorno.append(documento.getDtPreFatura() == null ? DATA_PRE_FATURA : "");
		
		return retorno.toString();
	}
	
	public void validateDocumentosNotEmpty(List<DocumentoPreFatura> documentos) {
		if(documentos.isEmpty()){
			 throw new PreFatException( configuracoesFacade.getMensagem("LMS-01130", new Object[] { ARQUIVO }));
		}
	}
	
	static class PreFaturaResult{
		private DevedorDocServFat devedor;
		private Long idFilial;
		private Long idCliente;
		private DoctoServico doctoServico;
		
		public PreFaturaResult(DevedorDocServFat devedor, Long idFilial, Long idCliente, DoctoServico doctoServico) {
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
	
	public void setGerarFaturaArquivoRecebidoService(GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService) {
		this.gerarFaturaArquivoRecebidoService = gerarFaturaArquivoRecebidoService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	
	public void setPreFatValidationService(PreFatValidationService preFatValidationService) {
		this.preFatValidationService = preFatValidationService;
	}
	
	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
}