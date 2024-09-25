package com.mercurio.lms.franqueados.model.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.util.FTPClientHolder;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.AnexoLancamentoFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.dao.LancamentoFranqueadoDAO;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class LancamentoFranqueadoService extends CrudService<LancamentoFranqueado, Long> {

	private static final Locale BR_LOCALE = new Locale("pt", "BR");
	private final String EXCESSAO_IMPORTACAO_FTP = "Excessao ao processar arquivo do FTP de importação de frete internacional franqueado";
	private final String QUEBRA_PAGINA = "\n";

	private Logger log = LogManager.getLogger(this.getClass());
	private WorkflowPendenciaService workflowPendenciaService;
	private ContaContabilFranqueadoService contaContabilFranqueadoService;
	private FranquiaService franquiaService;
	private AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainValueService domainValueService;
	private ParametroGeralService parametroGeralService;
	private RecursoMensagemService recursoMensagemService ;
	
	/**
	 * Executa a Importacao arquivo CSV e retorna uma lista de faturas para 
	 * envio ao Serasa 
	 * 
	 * @param parameters
	 * @return Map
	 */
	public List<LancamentoFranqueado> executeImportacao(TypedFlatMap parameters) {
		Scanner sc = null;
		List<LancamentoFranqueado> list = new ArrayList<LancamentoFranqueado>();
		try {
			String arquivoCSV = MapUtils.getString(parameters, "arquivoCSV");
			
			if(arquivoCSV == null){
				throw new BusinessException("LMS-00001", new Object[]{"Arquivo CSV"});
			}
			
			byte[] tmpArquivo = Base64Util.decode(arquivoCSV);
			byte[] arquivo = Arrays.copyOfRange(tmpArquivo, 1024, tmpArquivo.length);
			
			InputStream is = new ByteArrayInputStream(arquivo);
			sc = new Scanner(is).useDelimiter("[;\r\n]+");
			sc.useLocale(SessionUtils.getUsuarioLogado().getLocale());
			list = this.processaArquivoCSV(sc);

		} catch (IOException e) {
			throw new BusinessException("Erro ao processar o arquivo:",e);
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
			} catch (Exception e) {	
				
			}
		}
		
		return list;
	}

	/**
	 * Processa o arquivo csv
	 * 
	 * @param sc
	 * @param tpLote 
	 * @return Map
	 */
	@SuppressWarnings("rawtypes")
	private List<LancamentoFranqueado> processaArquivoCSV(Scanner sc) {

		List<LancamentoFranqueado> lancamentosFranqueados = new ArrayList<LancamentoFranqueado>();

		boolean cabecalho = true;

		while (sc.hasNext()) {

			if(cabecalho) {
				sc.nextLine();
				cabecalho = false;
			}
			
			String documento = null;
			Integer ano = null;
			Integer mes = null;
			String franquia = null;
			BigDecimal vlLancamento = null;
			
			try{
				sc.next();
				documento = sc.next().trim();
				sc.next();
				sc.next();
				ano = Integer.valueOf(sc.next().trim());
				mes = Integer.valueOf(sc.next().trim());
				franquia = sc.next().trim();
				
				String trim =sc.next().trim();
				if(!trim.matches("^[0-9.,]+$")){
					throw new BusinessException("LMS-46105");
				}
				
				Number vlLancamentoDouble = DecimalFormat.getInstance(BR_LOCALE).parse(trim);
				vlLancamento = BigDecimal.valueOf(vlLancamentoDouble.doubleValue());
				
				sc.nextLine();
			} catch (Exception e){
				throw new BusinessException("LMS-46105");
			}
			
			validateRow(franquia, documento, ano, mes,vlLancamento);
			
			String[] tmpDocto = documento.trim().split("\\.");
			String sgDocto = null;
			Short cdDocto = null;
			Integer nrDocto = null;
			try{
				sgDocto = tmpDocto[0];
				cdDocto = Short.decode(tmpDocto[1]);
				nrDocto = Integer.valueOf(tmpDocto[2]);
			} catch (Exception e){
				throw new BusinessException("LMS-46105");
			}
			
			
			Franquia franquiaObj = franquiaService.findFranquiasAtivasBySiglaFranquia(franquia, 361L, null);
			List franquias = findByIdFranquiaSiglaCodigoNroDocto(franquiaObj.getIdFranquia(), sgDocto, cdDocto, nrDocto);
			if (franquias.size() > 0) {
				throw new BusinessException("LMS-46103", new Object[] { documento });
			}

			
			YearMonthDay dtCompetencia = new YearMonthDay(Integer.valueOf(ano).intValue(), Integer.valueOf(mes).intValue(), 1);

			ContaContabilFranqueado contabilFranqueado = contaContabilFranqueadoService.findContaByTipo(dtCompetencia, ConstantesFranqueado.CONTA_CONTABIL_IRE, ConstantesFranqueado.TIPO_LANCAMENTO_CREDITO);
			if (contabilFranqueado == null) {
				throw new BusinessException("LMS-46103", new Object[] { sgDocto+"."+cdDocto+"."+nrDocto });
			}


			LancamentoFranqueado lancamentoFranqueado = new LancamentoFranqueado();
			
			lancamentoFranqueado.setSgDoctoInternacional(sgDocto);
			lancamentoFranqueado.setCdDoctoInternacional(cdDocto);
			lancamentoFranqueado.setNrDoctoInternacional(nrDocto);
			lancamentoFranqueado.setDtCompetencia(dtCompetencia);
			lancamentoFranqueado.setFranquia(franquiaObj);
			lancamentoFranqueado.setContaContabilFranqueado(contabilFranqueado);
			lancamentoFranqueado.setVlLancamento(vlLancamento);
			StringBuilder descricaoLancamento = new StringBuilder();
			descricaoLancamento.append(ConstantesFranqueado.PARTICIPACAO_FRETE_INTERNACIONAL).append(" - ").append(sgDocto).append(".").append(cdDocto).append(".").append(nrDocto);
			lancamentoFranqueado.setDsLancamento(descricaoLancamento.toString());
			lancamentosFranqueados.add(lancamentoFranqueado);
		}


		storeAllAndGeneratePendencia(lancamentosFranqueados);

		return lancamentosFranqueados;

	}

	private void validateRow(String franquia, String documento, Integer ano, Integer mes,
			BigDecimal vlLancamento) {
		
		String[] tmpDocto = documento.trim().split("\\."); 

		if(tmpDocto == null){	
			throw new BusinessException("LMS-46105");
		} else if(tmpDocto.length <= 1){
			throw new BusinessException("LMS-46105");
		}
		

		if(ano == null || mes == null || franquia == null || vlLancamento == null){
			throw new BusinessException("LMS-46105");
		}
	}


	
	private Pendencia generatePendencia(LancamentoFranqueado lancamentoFranqueado, Short nrEvento){
		
		Pendencia pendencia = workflowPendenciaService.generatePendencia(lancamentoFranqueado.getFranquia().getIdFranquia(), 
				                                          nrEvento, 
				                                          lancamentoFranqueado.getIdLancamentoFrq(), 
				                                          gerarDescricao(lancamentoFranqueado), 
				                                          JTDateTimeUtils.getDataHoraAtual());
		return pendencia;
	}			
	
	private String gerarDescricao(LancamentoFranqueado lancamentoFranqueado) {
		StringBuilder descricao = new StringBuilder();
		descricao.append(configuracoesFacade.getMensagem("franquia")).append(": ").append( lancamentoFranqueado.getFranquia().getFilial().getSgFilial() );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("competencia")).append(": ").append( lancamentoFranqueado.getDtCompetencia().toString("MM/yyyy") );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("lancamento")).append(": ").append( lancamentoFranqueado.getContaContabilFranqueado().getDsContaContabil() );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("valor")).append(": ").append( FormatUtils.formatDecimal("#,##0.00", lancamentoFranqueado.getVlLancamento(), true) );
		descricao.append(QUEBRA_PAGINA);
		if(lancamentoFranqueado.getCdDoctoInternacional() != null){
			descricao.append(configuracoesFacade.getMensagem("siglaDocNacional")).append(": ").append( lancamentoFranqueado.getSgDoctoInternacional() );
			descricao.append(QUEBRA_PAGINA);
			descricao.append(configuracoesFacade.getMensagem("codigoDocNacional")).append(": ").append( lancamentoFranqueado.getCdDoctoInternacional() );
			descricao.append(QUEBRA_PAGINA);
			descricao.append(configuracoesFacade.getMensagem("numeroDocNacional")).append(": ").append( lancamentoFranqueado.getNrDoctoInternacional() );
			descricao.append(QUEBRA_PAGINA);
		}
		
		DomainValue tpSituacaoPendencia = domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", lancamentoFranqueado.getTpSituacaoPendencia().getValue());  
		descricao.append(configuracoesFacade.getMensagem("situacaoAprovacao")).append(": ").append( tpSituacaoPendencia.getDescription() );
		
		if(StringUtils.isNotBlank(lancamentoFranqueado.getDsLancamento())){
			descricao.append(QUEBRA_PAGINA);
			descricao.append(configuracoesFacade.getMensagem("descricao")).append(": ").append( lancamentoFranqueado.getDsLancamento() );
		}
		
		if(StringUtils.isNotBlank(lancamentoFranqueado.getObLancamento())){
			descricao.append(QUEBRA_PAGINA);
			descricao.append(configuracoesFacade.getMensagem("observacao")).append(": ").append( lancamentoFranqueado.getObLancamento() );
		}
		
		if(lancamentoFranqueado.getNrParcelas() != null){
			descricao.append(QUEBRA_PAGINA);
			descricao.append(configuracoesFacade.getMensagem("parcelas")).append(": ").append( lancamentoFranqueado.getNrParcelas() );
		}
		
		return descricao.toString();
	}

	public String executeWorkflow(List<Long> listIds, List<String> listTpSituacao) {
		if(CollectionUtils.isNotEmpty(listIds)){
			Iterator<String> situacaoIt = listTpSituacao.iterator();
			for(Long id:listIds){
				LancamentoFranqueado lancamentoFranqueado = findById(id);
				String situacao = situacaoIt.next();
				if (lancamentoFranqueado != null && !situacao.equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_CANCELADO)){
					lancamentoFranqueado.setTpSituacaoPendencia(new DomainValue(situacao));
					
					if(lancamentoFranqueado.getContaContabilFranqueado().getTpContaContabil().getValue().equals(ConstantesFranqueado.CONTA_CONTABIL_CREDITO_ANTECIPACAO) && situacao.equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO)){
						List<LancamentoFranqueado> lancamentos = new ArrayList<LancamentoFranqueado>();
						lancamentos.add(lancamentoFranqueado);
						lancamentos.addAll(createParcelas(lancamentoFranqueado));

						super.storeAll(lancamentos);
					}
				}
			}
		}
		
		return null;
	}

	private List<LancamentoFranqueado> createParcelas(LancamentoFranqueado lancamentoFranqueado) {
		List<LancamentoFranqueado> lancamentos = new ArrayList<LancamentoFranqueado>();
		
		int nrParcelas = lancamentoFranqueado.getNrParcelas().intValue();
		boolean primeiraParcela = true;
		
		for (int i = 1; i <= nrParcelas; i++) {
			LancamentoFranqueado lancamento = new LancamentoFranqueado();
			ContaContabilFranqueado conta = contaContabilFranqueadoService.findContaByTipo(lancamento.getDtCompetencia(),  ConstantesFranqueado.CONTA_CONTABIL_DEBITO_ANTECIPACAO, ConstantesFranqueado.TIPO_LANCAMENTO_DEBITO);
					
			lancamento.setContaContabilFranqueado(conta);
			
			lancamento.setFranquia(lancamentoFranqueado.getFranquia());
			lancamento.setDtCompetencia(lancamentoFranqueado.getDtCompetencia().plusMonths(i));
				
			BigDecimal valorContabil = null;
			BigDecimal valorLancamento = lancamentoFranqueado.getVlLancamento().divide(BigDecimal.valueOf(lancamentoFranqueado.getNrParcelas()),2,FranqueadoUtils.ROUND_TYPE);
			
			if(primeiraParcela){
				BigDecimal resto = lancamentoFranqueado.getVlLancamento().subtract((valorLancamento.multiply(BigDecimal.valueOf(lancamentoFranqueado.getNrParcelas()))));
				valorLancamento = valorLancamento.add(resto);
				valorContabil = lancamentoFranqueado.getVlLancamento();
				primeiraParcela = false;
			}
			
			lancamento.setVlLancamento(valorLancamento);
			lancamento.setVlContabil(valorContabil);
			
			lancamento.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO));
			lancamento.setDsLancamento(lancamentoFranqueado.getDsLancamento()+" Parcela "+i+" de "+lancamentoFranqueado.getNrParcelas());
			
			lancamentos.add(lancamento);
		}
		
		return lancamentos;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Serializable store(LancamentoFranqueado lancamentoFranqueado, List anexosNovos, List anexosRemovidos){
		lancamentoFranqueado = (LancamentoFranqueado) store(lancamentoFranqueado);

		List anexos = new ArrayList();
		for (Object object : anexosNovos) {
			AnexoLancamentoFranqueado anexo = (AnexoLancamentoFranqueado)object;
			anexo.setLancamento(lancamentoFranqueado);
			
			anexos.add(anexo);
		}
		
		anexoLancamentoFranqueadoService.storeAll(anexos);
		
		List<Long> idsAnexosRemoved = new ArrayList<Long>();
		for (Object object : anexosRemovidos) {
			AnexoLancamentoFranqueado anexo = (AnexoLancamentoFranqueado)object;
			idsAnexosRemoved.add(anexo.getIdAnexoLancamentoFrq());
		}
		
		anexoLancamentoFranqueadoService.removeByIds(idsAnexosRemoved);

		return lancamentoFranqueado;
	}
	
	@Override
	public Serializable store(LancamentoFranqueado lancamentoFranqueado) {
		validarLancamentoStore(lancamentoFranqueado);
		super.store(lancamentoFranqueado);
		afterStore(lancamentoFranqueado);
		return lancamentoFranqueado;
	}
	
	public void storeAllAndGeneratePendencia(List<LancamentoFranqueado> list) {
		for (LancamentoFranqueado lancamentoFranqueado : list) {
			validarLancamentoStore(lancamentoFranqueado);
		}
		
		super.storeAll(list);
		
		for (LancamentoFranqueado lancamentoFranqueado : list) {
			afterStore(lancamentoFranqueado);
		}
		
	}

	private void validarLancamentoStore(LancamentoFranqueado lancamentoFranqueado) {
		validarLancamento(lancamentoFranqueado);
		validarDocumentoInternacional(lancamentoFranqueado);
	}
	
	private void validarLancamento(LancamentoFranqueado lancamentoFranqueado) {
		YearMonthDay competenciaLancamento = lancamentoFranqueado.getDtCompetencia();
		YearMonthDay competenciaAtual = FranqueadoUtils.buscarPrimeiroDiaMesAtual();
		
		if(lancamentoFranqueado.isManual() && (competenciaLancamento == null || competenciaLancamento.compareTo(competenciaAtual) < 0) ){
			throw new BusinessException("LMS-46102");
		}
		
		if(lancamentoFranqueado.isManual() && lancamentoFranqueado.getContaContabilFranqueado() != null && !lancamentoFranqueado.getContaContabilFranqueado().getBlPermiteAlteracao()){
			throw new BusinessException("LMS-46104");
		}
		
		if(lancamentoFranqueado.getContaContabilFranqueado() != null &&
				lancamentoFranqueado.getContaContabilFranqueado().getTpContaContabil().getValue().equals(ConstantesFranqueado.CONTA_CONTABIL_CREDITO_ANTECIPACAO) &&
				lancamentoFranqueado.getTpSituacaoPendencia().getValue().equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO)
				){
			throw new BusinessException("LMS-46106");
		}
		
	}
	
	private void validarDocumentoInternacional(LancamentoFranqueado lancamentoFranqueado) {
		if(lancamentoFranqueado.getSgDoctoInternacional() != null && lancamentoFranqueado.getCdDoctoInternacional() != null 
				&& lancamentoFranqueado.getNrDoctoInternacional() != null){
			
			if (findByIdFranquiaSiglaCodigoNroDocto(lancamentoFranqueado.getFranquia().getIdFranquia(),
					lancamentoFranqueado.getSgDoctoInternacional(),
					lancamentoFranqueado.getCdDoctoInternacional(),
					lancamentoFranqueado.getNrDoctoInternacional(),
					lancamentoFranqueado.getIdLancamentoFrq()).size() > 0) {
				throw new BusinessException("LMS-46103", new Object[] { lancamentoFranqueado.getSgDoctoInternacional() + "." + lancamentoFranqueado.getCdDoctoInternacional() + "." + lancamentoFranqueado.getNrDoctoInternacional() });
			}
			
		}
	}

	private void afterStore(LancamentoFranqueado lancamentoFranqueado) {
		cancelarPendenciaLancamentoFranqueado(lancamentoFranqueado);
		gerarPendenciaLancamentoFranqueado(lancamentoFranqueado);
	}

	private void gerarPendenciaLancamentoFranqueado(LancamentoFranqueado lancamentoFranqueado) {
		Object paramentroVlMinimoFranqueado = null;
		Object paramentroVl1FaixaAprovCredFranqueado = null;
		Object paramentroVl2FaixaAprovCredFranqueado = null;
		
		if(lancamentoFranqueado.isManual()){
			paramentroVlMinimoFranqueado = configuracoesFacade.getValorParametro(ConstantesFranqueado.VALOR_MINIMO_PENDENCIA_FRQ);
			paramentroVl1FaixaAprovCredFranqueado = configuracoesFacade.getValorParametro(ConstantesFranqueado.FAIXA_1_APROV_CRED_FRQ);
			paramentroVl2FaixaAprovCredFranqueado = configuracoesFacade.getValorParametro(ConstantesFranqueado.FAIXA_2_APROV_CRED_FRQ);
		}
		
		if( lancamentoFranqueado.isManual() && paramentroVlMinimoFranqueado != null && paramentroVlMinimoFranqueado instanceof BigDecimal && lancamentoFranqueado.getVlLancamento().compareTo( (BigDecimal)paramentroVlMinimoFranqueado ) > 0 ){
			Short nrEvento = ConstantesWorkflow.NR4601_LIBERACAO_LANCAMENTO_MANUAL;

			if (lancamentoFranqueado.getContaContabilFranqueado().getTpContaContabil().getValue().equals(ConstantesFranqueado.CONTA_CONTABIL_CREDITO_DIVERSOS) ||
					lancamentoFranqueado.getContaContabilFranqueado().getTpContaContabil().getValue().equals(ConstantesFranqueado.CONTA_CONTABIL_CREDITO_ANTECIPACAO)) {
				
				if (paramentroVl1FaixaAprovCredFranqueado != null && paramentroVl1FaixaAprovCredFranqueado instanceof BigDecimal &&
						paramentroVl2FaixaAprovCredFranqueado != null && paramentroVl2FaixaAprovCredFranqueado instanceof BigDecimal) {
					
					if (lancamentoFranqueado.getVlLancamento().compareTo((BigDecimal) paramentroVl1FaixaAprovCredFranqueado) <= 0) {
						nrEvento = ConstantesWorkflow.NR4604_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_1;
					} else if (lancamentoFranqueado.getVlLancamento().compareTo((BigDecimal) paramentroVl2FaixaAprovCredFranqueado) <= 0) {
						nrEvento = ConstantesWorkflow.NR4605_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_2;
					} else {
						nrEvento = ConstantesWorkflow.NR4606_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_3;
					}
				}
			}
			
			lancamentoFranqueado.setPendencia(generatePendencia(lancamentoFranqueado, nrEvento));
			lancamentoFranqueado.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_EM_APROVACAO));
		}else{
			lancamentoFranqueado.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO));
		}
		this.getDao().store(lancamentoFranqueado);
	}
	
	private void cancelarPendenciaLancamentoFranqueado(LancamentoFranqueado lancamentoFranqueado) {
		if(lancamentoFranqueado.getTpSituacaoPendencia() != null 
				&& lancamentoFranqueado.getTpSituacaoPendencia().getValue().equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_EM_APROVACAO)
				&& lancamentoFranqueado.getPendencia() != null){
			workflowPendenciaService.cancelPendencia(lancamentoFranqueado.getPendencia());
		}
	}

	public LancamentoFranqueado findById(Long id) {
		LancamentoFranqueado lancamentoFranqueado = getLancamentoFranqueadoDAO().findById(id);
		Hibernate.initialize(lancamentoFranqueado);
        return lancamentoFranqueado;
    }

    @SuppressWarnings("rawtypes")
	public List findByIdFranquiaSiglaCodigoNroDocto(Long idFranquia, String sgDocto, Short cdDocto, Integer nrDocto) {
		return findByIdFranquiaSiglaCodigoNroDocto(idFranquia, sgDocto, cdDocto, nrDocto, null);
	}
	
    @SuppressWarnings("rawtypes")
	public List findByIdFranquiaSiglaCodigoNroDocto(Long idFranquia, String sgDocto, Short cdDocto, Integer nrDocto, Long idLancamento) {
		return getLancamentoFranqueadoDAO().findByIdFranquiaSiglaCodigoNroDocto(idFranquia, sgDocto, cdDocto, nrDocto, idLancamento);
	}

    public boolean isDesabilitarCampos(LancamentoFranqueado lancamentoFranqueado) {
		if(lancamentoFranqueado.getDtCompetencia() == null || lancamentoFranqueado.getDtCompetencia().compareTo(FranqueadoUtils.buscarPrimeiroDiaMesAtual()) < 0){
			return true;
		}
		if(lancamentoFranqueado.getContaContabilFranqueado() == null || !lancamentoFranqueado.getContaContabilFranqueado().getBlPermiteAlteracao()){
			return true;
		}
		
		return false;
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for (Long idLancamento : ids) {
			beforeRemove(idLancamento);
		}
		super.removeByIds(ids);
	}
	
	@Override
	public void removeById(Long id) {
		beforeRemove(id);
		super.removeById(id);
	}

	private void beforeRemove(Long id) {
		LancamentoFranqueado lancamentoFranqueado = findById(id);
		validarLancamento(lancamentoFranqueado);
		cancelarPendenciaLancamentoFranqueado(lancamentoFranqueado);
		
		List<Long> idsAnexosRemoved = new ArrayList<Long>();
		for (Object object : lancamentoFranqueado.getAnexos()) {
			AnexoLancamentoFranqueado anexo = (AnexoLancamentoFranqueado)object;
			idsAnexosRemoved.add(anexo.getIdAnexoLancamentoFrq());
		}
		anexoLancamentoFranqueadoService.removeByIds(idsAnexosRemoved);
	}
	
	@AssynchronousMethod(name = "franqueados.executeImportacaoDocumentosInternacionaisFranqueado", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeImportacaoDocumentosInternacionaisFranqueado() {
		
		String dadosFtp = (String) parametroGeralService
				.findConteudoByNomeParametro("IRE_FRANQUEADO_FTP_CONFIG", false);
		String[] dados = dadosFtp.split(";");
		if (dados == null || dados.length < 3) {
			throw new BusinessException("LMS-27051",
					new Object[] { "IRE_FRANQUEADO_FTP_CONFIG" });
		}
		
		String host = dados[0];
		String diretorioFTP = dados[1];
		String username = dados[2];
		String password = dados[3];
		String systemKey = FTPClientConfig.SYST_NT;
		
		FTPClientHolder ftp = new FTPClientHolder(host, username, password, diretorioFTP, systemKey);
		
		try {
			try {
				ftp.connect();
				FTPFile[] filesProcessando = ftp.listFiles("*." + ConstantesFranqueado.IMPORTACAO_PROCESSANDO_EXT);

				if (filesProcessando.length == 0) {

					FTPFile[] files = limitNumberOfFiles(
							ftp.listFiles("*." + ConstantesFranqueado.IMPORTACAO_INPUT_EXT), ConstantesFranqueado.IMPORTACAO_MAX_FILES);

					LinkedList<String> names = new LinkedList<String>();
					String dirName = new YearMonthDay().toString("ddMMyyyy");

					for (FTPFile file : files) {
						if (file.isFile() && file.getName().toLowerCase().endsWith(ConstantesFranqueado.IMPORTACAO_INPUT_EXT)) {
							names.offer(file.getName().substring(0,	file.getName().toUpperCase()
											.indexOf("." + ConstantesFranqueado.IMPORTACAO_INPUT_EXT.toUpperCase())));
							
							boolean directoryExists = ftp.changeWorkingDirectory(dirName);
							if (!directoryExists) {
								ftp.makeDirectory(ftp.printWorkingDirectory() + "/" + dirName);
								ftp.changeWorkingDirectory(dirName);
							}
							ftp.changeWorkingDirectory("..");
							
							ftp.renameFile(file.getName(), dirName + "/" + file.getName());
						}
					}
					for (String name : names) {
						try {
							ftp.changeWorkingDirectory(diretorioFTP + "/" + dirName);
							ftp.renameFile(name + "." + ConstantesFranqueado.IMPORTACAO_INPUT_EXT,
									name + "." + ConstantesFranqueado.IMPORTACAO_PROCESSANDO_EXT);
							InputStream is = ftp.retrieveFileStream(name + "." + ConstantesFranqueado.IMPORTACAO_PROCESSANDO_EXT);
							ftp.completePendingCommand();
							readFtpImportacaoDocumentosInternacionaisFrqFile(is, name.toLowerCase());
							ftp.renameFile(name + "." + ConstantesFranqueado.IMPORTACAO_PROCESSANDO_EXT,
									name + "." + ConstantesFranqueado.IMPORTACAO_OK_EXT);
						} catch (BusinessException be) {
							Object[] arguments = be.getMessageArguments();
							String message = recursoMensagemService.findByChave(be.getMessageKey(), arguments);
							onFTPError(ftp, name, message);
						} catch (Throwable e) {
							onFTPError(ftp, name, e.getMessage());
						}
					}
				}
			} finally {
				ftp.disconnect();
			}
		} catch (Exception e) {
			log.error(EXCESSAO_IMPORTACAO_FTP, e);
		}
	}

	public void onFTPError(FTPClientHolder ftp, String name, String messageError) throws IOException {
		ftp.renameFile(name + "." + ConstantesFranqueado.IMPORTACAO_PROCESSANDO_EXT, name + "." + ConstantesFranqueado.IMPORTACAO_NOK_EXT);
		String errFileName = name + "." + ConstantesFranqueado.IMPORTACAO_ERR_EXT;
		try {
			OutputStream out = ftp.storeFileStream(errFileName);
			try {
				PrintStream ps = new PrintStream(out);
				ps.write(messageError.getBytes());
				out.flush(); 
			} finally {
				out.close();
			}
		} catch (Throwable e) {
			log.error(e);
		}
		ftp.completePendingCommand();
		log.error(EXCESSAO_IMPORTACAO_FTP + " [" + name + "." + ConstantesFranqueado.IMPORTACAO_INPUT_EXT+"]");
	}
	
	private void readFtpImportacaoDocumentosInternacionaisFrqFile(InputStream is, String fileName)
			throws Exception {
		Scanner sc = new Scanner(is).useDelimiter("[;\r\n]+");
		sc.useLocale(SessionUtils.getUsuarioLogado().getLocale());
		this.processaArquivoCSV(sc);
	}
	
	private FTPFile[] limitNumberOfFiles(FTPFile[] all, int max) {
		int last = all.length < max ? all.length : max;
		FTPFile[] files = new FTPFile[last];
		System.arraycopy(all, 0, files, 0, last);
		return files;
	}
	
	@Override
	public void storeAll(List<LancamentoFranqueado> list) {
		super.storeAll(list);
	}
	
	public void storeAllNewSession(List<LancamentoFranqueado> lancamentoFranqueadoList) {
		getLancamentoFranqueadoDAO().storeAllNewSession(lancamentoFranqueadoList);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaTRIIRE(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaTRIIRE(dtCompetencia, idFranquia);
	}

	
	@SuppressWarnings("rawtypes")
	public List getConsultaOver60(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaOver60(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaIndenizacoes(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaIndenizacoes(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculos(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaRecalculos(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculosFrete(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaRecalculosFrete(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculosServicos(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaRecalculosServicos(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaBDMs(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaBDMs(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaCreditosDiversos(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaCreditosDiversos(dtCompetencia, idFranquia);
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaDebitosDiversos(YearMonthDay dtCompetencia, Long idFranquia) {
		return getLancamentoFranqueadoDAO().getConsultaDebitosDiversos(dtCompetencia, idFranquia);
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoIRE(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {
		return getLancamentoFranqueadoDAO().findRelatorioAnaliticoIRE(filtraFranquia, isCSV, parameters);
	}
	
	public void setContaContabilFranqueadoService(ContaContabilFranqueadoService contaContabilFranqueadoService) {
		this.contaContabilFranqueadoService = contaContabilFranqueadoService;
	}

	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public LancamentoFranqueadoDAO getLancamentoFranqueadoDAO() {
		return (LancamentoFranqueadoDAO) getDao();
	}

	public void setLancamentoFranqueadoDAO(LancamentoFranqueadoDAO lancamentoFranqueadoDAO) {
		setDao(lancamentoFranqueadoDAO);
	}

	public void setAnexoLancamentoFranqueadoService(
			AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService) {
		this.anexoLancamentoFranqueadoService = anexoLancamentoFranqueadoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public Serializable storeAutomatico(LancamentoFranqueado lancamentoFranqueado) {
		return super.store(lancamentoFranqueado);
	}
}
