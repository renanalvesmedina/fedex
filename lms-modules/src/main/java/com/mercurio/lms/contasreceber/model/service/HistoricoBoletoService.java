package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.ArquivoRemessaDTO;
import br.com.tntbrasil.integracao.domains.financeiro.ArquivoRemessaPaiDTO;
import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.HistoricoBoletoDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Boleto.SITUACAOBOLETO;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.dao.HistoricoBoletoDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.util.StringUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.service.PendenciaService;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.historicoBoletoService"
 */

public class HistoricoBoletoService extends CrudService<HistoricoBoleto, Long> {
	private static final String REMOVE_DECIMAL_REGEX = "[,\\.]";
	private CedenteService cedenteService;
	private ConfiguracoesFacade configuracoesFacade;
	private OcorrenciaBancoService ocorrenciaBancoService;
	private QuestionamentoFaturasService questionamentoFaturasService;
	private GerarFaturaBoletoService gerarFaturaService;
	private PendenciaService pendenciaService;
	private FaturaService faturaService;
	private FilialService filialService;
	private BoletoAnexoService boletoAnexoService;
	private MotivoOcorrenciaBancoService motivoOcorrenciaBancoService;
	private EnvioOcorrenciaBoletoBancoService envioOcorrenciaBoletoBancoService;
	private UsuarioService usuarioService;
	private RetornoBancoService retornoBancoService;
	private ParametroGeralService parametroGeralService;
	private EnderecoPessoaService enderecoPessoaService;
	private BoletoService boletoService;
	private HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService;
	
	private IntegracaoJmsService integracaoJmsService;

	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	private static final Set<String> TP_BOLETOS_EM_BANCO = new HashSet<String>(Arrays.asList(new String[]{ "BN", "BP" }));
	private static final long USUARIO_INTEGRACAO = 5000L;
	/**
	 * Recupera uma inst�ncia de <code>HistoricoBoleto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public HistoricoBoleto findById(java.lang.Long id) {
		return (HistoricoBoleto)super.findById(id);
	}

	public HistoricoBoleto findByIdLoadLazyProperties(java.lang.Long id) {
		return (HistoricoBoleto)super.findByIdInitLazyProperties(id, true);
	}

	
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@Override
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
	@Override
	public java.io.Serializable store(HistoricoBoleto bean) {
		if (bean.getBoleto().getFatura()!=null && !SessionUtils.isIntegrationRunning()){
			Fatura fatura = bean.getBoleto().getFatura();
			if (fatura.getBlConhecimentoResumo() != null && fatura.getBlConhecimentoResumo()){
        		throw new BusinessException("LMS-36256");
        	}
		}
		Serializable historicoBoletoId = super.store(bean);

		envioOcorrenciaBoletoBancoService.execute(bean);
		
		return historicoBoletoId;
	}

	
	/**
	 * M�todo que executa uma consulta para a tela:
	 * Consultar Hist�rico das Ocorr�ncias do Boleto
	 *
	 * Busca os dados do Historico da Movimenta��o
	 * 
	 * @author Diego Umpierre - LMS
	 * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
	 * @param idBoleto identificador do boleto, findDef FindDefinition
	 *
	 * @return ResultSetPage com o resultado da consulta de acordo com os parametros.
	 */
	public ResultSetPage findPaginatedHistMov(Long idBoleto, FindDefinition findDef) {
 		if (idBoleto == null ){
 			return null;
 		}
		return getHistoricoBoletoDAO().findPaginatedHistMov(idBoleto, findDef); 
	}

	/**
	 * Retorna a lista de HistoricoBoleto por boleto
	 * 
	 * @author Micka�l Jalbert
	 * @since 25/04/2006
	 * 
	 * @param Long idBoleto
	 * @return List
	 * */	
	public List findByBoleto(Long idBoleto){
		return getHistoricoBoletoDAO().findByBoleto(idBoleto); 
	}


	public HistoricoBoleto generateHistoricoBoleto(Boleto boleto, Short codigoOcorrenciaBoleto, String origem){
		return generateHistoricoBoleto(boleto, codigoOcorrenciaBoleto, origem,null);
	}
	/**
	 * Gera um historico boleto a partir dos dados informado
	 * 
	 * @author Micka�l Jalbert
	 * @param tpSituacaoAprovacao 
	 * @since 25/04/2006
	 * 
	 * @param Boleto boleto
	 * @param Short codigoOcorrenciaBoleto
	 * @param String origem 
	 * @param MotivoOcorrencia motivoOcorrencia 
	 * @param String dsHistoricoBoleto
	 * @return HistoricoBoleto
	 * */
	public HistoricoBoleto generateHistoricoBoleto(Boleto boleto, Short codigoOcorrenciaBoleto, String origem, DomainValue tpSituacaoAprovacao){
		return generateHistoricoBoletoAndCancelActives(boleto, codigoOcorrenciaBoleto, origem, null, null, tpSituacaoAprovacao);
	}


	public HistoricoBoleto generateHistoricoBoleto(Boleto boleto, Short codigoOcorrenciaBoleto, String origem, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto){
		return generateHistoricoBoleto(boleto, codigoOcorrenciaBoleto, origem, motivoOcorrencia, dsHistoricoBoleto, null);
	}
	
	/**
	 * Gera um historico boleto a partir dos dados informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 25/04/2006
	 * 
	 * @param Boleto boleto
	 * @param Short codigoOcorrenciaBoleto
	 * @param String origem
	 * @param MotivoOcorrencia motivoOcorrencia 
	 * @param String dsHistoricoBoleto
	 * @return HistoricoBoleto
	 */
	public HistoricoBoleto generateHistoricoBoleto(Boleto boleto, Short codigoOcorrenciaBoleto, String origem, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto, DomainValue tpSituacaoAprovacao){
		// Cancela os Hist�ricos (e as Pendencias) de boleto ativos
		cancelHistoricoBoleto(boleto.getIdBoleto()); 
		HistoricoBoleto historicoBoleto = generateHistoricoBoletoDefault(boleto, codigoOcorrenciaBoleto, origem, motivoOcorrencia, dsHistoricoBoleto, tpSituacaoAprovacao);
		return historicoBoleto;
	}

	/**
	 * Gera o hist�ricoBoleto e cancela os hist�ricos com a mesma ocorr�nciaBanco. 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 10/08/2007
	 *
	 * @param boleto
	 * @param codigoOcorrenciaBoleto
	 * @param origem
	 * @param motivoOcorrencia
	 * @param dsHistoricoBoleto
	 * @param tpSituacaoAprovacao 
	 * @return
	 *
	 */
	public HistoricoBoleto generateHistoricoBoletoAndCancelActives(Boleto boleto, Short codigoOcorrenciaBoleto, String origem, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto, DomainValue tpSituacaoAprovacao){
		cancelaHistoricosComOcorrenciaIguais(boleto, codigoOcorrenciaBoleto);

		HistoricoBoleto historicoBoleto = generateHistoricoBoletoDefault(
				boleto, 
				codigoOcorrenciaBoleto, 
				origem, 
				motivoOcorrencia, 
				dsHistoricoBoleto,tpSituacaoAprovacao);
		return historicoBoleto;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 14/08/2007
	 *
	 * @param boleto
	 * @param codigoOcorrenciaBoleto
	 *
	 */
	public void cancelaHistoricosComOcorrenciaIguais(Boleto boleto, Short codigoOcorrenciaBoleto) {
		List<HistoricoBoleto> historicos = findHistoricosByBoletoAndOcorrencia(
				boleto.getIdBoleto(), 
				codigoOcorrenciaBoleto, 
				"A"
		);
		// Cancela os Hist�ricos (e as Pendencias) de boleto ativos
		cancelHistoricosBoleto(historicos);
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @param tpSituacaoAprovacao 
	 * @since 10/08/2007
	 *
	 * @return
	 * @throws BusinessException
	 *
	 */
	private HistoricoBoleto generateHistoricoBoletoDefault(Boleto boleto, Short codigoOcorrenciaBoleto, String origem, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto, DomainValue tpSituacaoAprovacao) {
		HistoricoBoleto historicoBoleto = new HistoricoBoleto();

		Cedente cedente = cedenteService.findById(boleto.getCedente().getIdCedente());

		OcorrenciaBanco ocorrenciaBanco = ocorrenciaBancoService.findByBancoNrOcorrenciaTpOcorrencia(cedente.getAgenciaBancaria().getBanco().getIdBanco(), codigoOcorrenciaBoleto, origem);
		if (ocorrenciaBanco == null){
			throw new BusinessException("LMS-36113");
		}

		historicoBoleto.setBoleto(boleto);
		historicoBoleto.setOcorrenciaBanco(ocorrenciaBanco);
		historicoBoleto.setDhOcorrencia(JTDateTimeUtils.getDataHoraAtual());
		historicoBoleto.setDsHistoricoBoleto(dsHistoricoBoleto);
		historicoBoleto.setMotivoOcorrencia(motivoOcorrencia);
		historicoBoleto.setUsuario(SessionUtils.getUsuarioLogado());
		historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("A"));
		historicoBoleto.setTpSituacaoAprovacao(tpSituacaoAprovacao);

		store(historicoBoleto);
		return historicoBoleto;
	}  

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 10/08/2007
	 *
	 * @param historicos
	 *
	 */
	private void cancelHistoricosBoleto(List<HistoricoBoleto> historicos) {
		for (HistoricoBoleto hb : historicos) {
			cancelHistoricoBoleto(hb);
		}
	}

	/**
	 * Carrega hist�ricos boleto de acordo com o nrOcorrenciaBanco, idBoleto e a situa��o do hist�rico.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 10/08/2007
	 *
	 * @param idBoleto
	 * @param tpSituacao
	 * @param nrOcorrenciaBanco
	 * @return
	 *
	 */
	public List<HistoricoBoleto> findHistoricosByBoletoAndOcorrencia(Long idBoleto, Short nrOcorrenciaBanco, String tpSituacao){
		return getHistoricoBoletoDAO().findHistoricosByBoletoAndOcorrencia(idBoleto, nrOcorrenciaBanco, tpSituacao);
	}

	/**
	 * Atualiza todos os 'historicos de boleto' para 'cancelado' quando eles s�o 'ativo'
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/11/2006
	 * 
	 * @param Long idBoleto
	 */
	public void cancelHistoricoBoleto(Long idBoleto){
		List <HistoricoBoleto>lstHistoricoBoleto = findByBoleto(idBoleto, "A");
		cancelHistoricosBoleto(lstHistoricoBoleto);
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 02/03/2007
	 *
	 * @param hb
	 */
	private void cancelHistoricoBoleto(HistoricoBoleto hb) {
		hb.setTpSituacaoHistoricoBoleto(new DomainValue("C"));

		store(hb);
	}

	/**
	 * Faz a contagem de tuplas da consulta findPaginatedHistMov
	 * 
	 * @param idBoleto
	 * @return
	 * @author Diego Umpierre - LMS
	 * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
	 */
	public Integer getRowCountHistMov(Long  idBoleto) {
 		if (idBoleto == null ){
 			return null;
 		}	
 		return getHistoricoBoletoDAO().getRowCountHistMov(idBoleto); 
	}

	/**
	 * Valida se deve ser gerado um historicoBoleto de cancelamento de abatimento.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 20/08/2007
	 *
	 * @param boleto
	 *
	 */
	private void gerarHistoricoCancelamentoAbatimento(Boleto boleto){
		String gerarCancelamentoAbatimento = getHistoricoBoletoDAO()
			.validateGerarHistoricoCanlamentoAbatimento(boleto
				.getIdBoleto());

		// Caso exista um hist�ricoBoleto de abatimento com dhOcorrencia maior que  
		// a dhOcorrencia do �ltimo historicoBoleto de cancelamento de abatimento, gera 
		// um novo historicoBoleto de cancelamento de abatimento para cancelar o mesmo.
		if (!"N".equalsIgnoreCase(gerarCancelamentoAbatimento)) {
			this.generateHistoricoBoleto(
					boleto,  
					ConstantesConfiguracoes.CD_OCORRENCIA_CANCELAMENTO_ABATIMENTO, 
					"REM"
			);
		}
	}

	/**
	 *	Cancela os historicos de abatimento a transmitir. 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 *
	 * @param boleto
	 *
	 */
	public void cancelHistoricoBoletoAbatimento(Boleto boleto) {
		// Caso a situa��o do boleto seja BN ou BP, faz algumas valida��es nos historicos do boleto.
		if ("BN".equals(boleto.getTpSituacaoBoleto().getValue()) ||
				"BP".equals(boleto.getTpSituacaoBoleto().getValue())) { 

			// Itera os hist�ricos do boleto.
			for ( HistoricoBoleto hb : (List<HistoricoBoleto>) boleto.getHistoricoBoletos()) {
				OcorrenciaBanco ob = hb.getOcorrenciaBanco();
				if ("REM".equals(ob.getTpOcorrenciaBanco().getValue())) {
					if (ob.getNrOcorrenciaBanco() == 4) {
						// Caso tpOcorrenciaBanco seja 'REM' e tpSituacaoHistoricoBoleto seja 'A'
						// e nrOcorrenciaBanco seja 4, cancela o hist�ricoBoleto.
						if ("E".equals(hb.getTpSituacaoHistoricoBoleto().getValue())) {
							cancelHistoricoBoleto(hb);
						}
					}
				}
			}

			this.getHistoricoBoletoDAO().flush();

			// Caso exista um hist�ricoBoleto de abatimento com dhOcorrencia maior que  
			// a dhOcorrencia do �ltimo historicoBoleto de cancelamento de abatimento, gera 
			// um novo historicoBoleto de cancelamento de abatimento para cancelar o mesmo.
			this.gerarHistoricoCancelamentoAbatimento(boleto); 
		}
	}

	/**
	 * gera um questionamento fatura
	 * 
	 * @param historicoBoleto
	 * @param dsProcesso
	 * @param dtVencimentoNovo 
	 * @param tpDocumentoServico 
	 */
	public void generateQuestionamentoFatura(HistoricoBoleto historicoBoleto,
												String dsProcesso, 
												DoctoServico documentoServico,
												boolean aprovacaoProrrogaVencimento,
												boolean aprovacaoCancelamento,
												YearMonthDay dtVencimentoDocumento,
												YearMonthDay dtVencimentoNovo,
												MotivoOcorrencia motivoCancelamento,
												MotivoOcorrencia motivoProrrogacao ) {
		
		QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
		questionamentoFatura.setVlAbatimentoSolicitado(BigDecimal.ZERO);
		questionamentoFatura.setNrBoleto(historicoBoleto.getBoleto().getNrBoleto());
		questionamentoFatura.setBlProrrogaVencimentoSol(aprovacaoProrrogaVencimento);
		questionamentoFatura.setBlBaixaTitCancelSol(aprovacaoCancelamento);
		questionamentoFatura.setDtVencimentoSolicitado(dtVencimentoNovo);
		questionamentoFatura.setDtVencimentoDocumento(dtVencimentoDocumento);
		questionamentoFatura.setMotivoProrrogacaoVcto(motivoProrrogacao);
		questionamentoFatura.setMotivoCancelamento(motivoCancelamento);
		questionamentoFatura.setTpSituacaoBoleto(SITUACAOBOLETO.getSituacaoBloquete(historicoBoleto.getBoleto().getTpSituacaoBoleto()));
		questionamentoFatura.setDtEmissaoDocumento(historicoBoleto.getBoleto().getDtEmissao());
		
		if(aprovacaoProrrogaVencimento){
			questionamentoFatura.setObAbatimento("Aprova��o para prorroga��o de vencimento referente a fatura sigla e n�mero e o novo vencimento.");
		}
		
		if(aprovacaoCancelamento){
			questionamentoFatura.setObAbatimento(dsProcesso);
		}
		
		questionamentoFatura.setFatura(historicoBoleto.getBoleto().getFatura());
		questionamentoFatura.setTpDocumento(new DomainValue("R"));
		
		HistoricoBoleto hb = findLastHistoricoBoletoWithPendencia(historicoBoleto.getBoleto());
		if(hb == null){
			hb = new HistoricoBoleto();
		}
		
		Long idPendenciaAntigo = hb.getIdPendencia();
		
		historicoBoleto.setIdPendencia((Long) questionamentoFaturasService.storeGenericQuestionamentoFatura(questionamentoFatura, filialService.findById(historicoBoleto.getBoleto().getFatura().getFilialByIdFilialCobradora().getIdFilial()) ,  
				historicoBoleto.getBoleto().getFatura().getCliente(), 
				historicoBoleto.getBoleto().getVlTotal(),
				idPendenciaAntigo, historicoBoleto.getDsHistoricoBoleto()));
		
		// se esta aproveitanod o mesmo questionamento, removo 
		if(idPendenciaAntigo != null && idPendenciaAntigo.compareTo(historicoBoleto.getIdPendencia()) == 0){
			hb.setIdPendencia(null);
			hb.setTpSituacaoAprovacao(null);
			store(hb);
		} else {
			boletoAnexoService.executeLimparEnvioAnexos(historicoBoleto.getBoleto());
		}
		historicoBoleto.setTpSituacaoAprovacao( new DomainValue("E") );
		
	}

	/**
	 * Retorna a lista de HistoricoBoleto por boleto de acordo com a situa��o passada
	 * 
	 * @author Micka�l Jalbert
	 * @since 02/03/2007
	 * 
	 * @param Long idBoleto
	 * @return List
	 * */	
	public List findByBoleto(Long idBoleto, String tpSituacao){
		return getHistoricoBoletoDAO().findByBoleto(idBoleto, tpSituacao);
	}

	public HistoricoBoleto findByIdQuestionamentoFatura( Long idQuestionamentoFatura) {
		return getHistoricoBoletoDAO().findByIdPendencia(idQuestionamentoFatura);
	}
	

	public HistoricoBoleto findLastHistoricoBoletoWithPendencia(Boleto boleto) {
		return getHistoricoBoletoDAO().findLastHistoricoBoletoWithPendencia(boleto);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setHistoricoBoletoDAO(HistoricoBoletoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private HistoricoBoletoDAO getHistoricoBoletoDAO() {
		return (HistoricoBoletoDAO) getDao();
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setOcorrenciaBancoService(OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public QuestionamentoFaturasService getQuestionamentoFaturasService() {
		return questionamentoFaturasService;
	}

	public void setGerarFaturaService(GerarFaturaBoletoService gerarFaturaService) {
		this.gerarFaturaService = gerarFaturaService;
	}

	public GerarFaturaBoletoService getGerarFaturaService() {
		return gerarFaturaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setBoletoAnexoService(BoletoAnexoService boletoAnexoService) {
		this.boletoAnexoService = boletoAnexoService;
	}

	/**
	 * LMS-6106 - Busca �ltimo <tt>HistoricoBoleto</tt> de um determinado
	 * <tt>Boleto</tt> para o processo de cancelamento de boleto. Utilizado para
	 * recuperar o motivo do cancelamento (<tt>idMotivoOcorrencia</tt>) e o
	 * campo de observa��es (<tt>dsHistoricoBoleto</tt>).
	 * 
	 * @param idBoleto
	 *            id do <tt>Boleto</tt>
	 * @return �ltimo <tt>HistoricoBoleto<tt> para cancelamento de boleto
	 */
	public HistoricoBoleto findManterBoletosObservacao(Long idBoleto) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("boleto.idBoleto", idBoleto));
		criterions.add(Restrictions.eq("tpSituacaoHistoricoBoleto", new DomainValue("A")));
		criterions.add(Restrictions.eq("tpSituacaoAprovacao", new DomainValue("E")));
		Order order = Order.desc("idHistoricoBoleto");
		List<HistoricoBoleto> result = getHistoricoBoletoDAO().findByCriterion(criterions, order);
		return result == null || result.isEmpty() ? null : (HistoricoBoleto) result.get(0);
	}

	public boolean rotinaInclusaoHistoricoBoleto(BoletoDMN boletoDmn, 
			Boleto boleto) {
		List<OcorrenciaBanco> findOcorrenciaBancoForRetornoBanco = ocorrenciaBancoService.findOcorrenciaBancoForRetornoBanco(boletoDmn.getNrOcorrencia().shortValue(), boleto.getCedente().getIdCedente());
		
		if (findOcorrenciaBancoForRetornoBanco.isEmpty()) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36360", new Object[]{boletoDmn.getNrOcorrencia()});
			return false;
		}
		
		HistoricoBoleto historicoBoleto = new HistoricoBoleto();
		historicoBoleto.setOcorrenciaBanco(findOcorrenciaBancoForRetornoBanco.iterator().next());
		historicoBoleto.setBoleto(boleto);
		historicoBoleto.setUsuario(getUsuario());
		historicoBoleto.setDhOcorrencia(boletoDmn.getDtMovimento().toDateTimeAtMidnight());
		historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("T"));

		if (boletoDmn.getNrMotivoRejeicao() != null) {
			HistoricoMotivoOcorrencia historicoMotivoOcorrencia = new HistoricoMotivoOcorrencia();
			
			historicoMotivoOcorrencia.setHistoricoBoleto(historicoBoleto);
			
			Long nrOcorrenciaBanco = boletoDmn.getNrOcorrencia();
			Long idCedente = boleto.getCedente().getIdCedente();
			Long motivoOcorrenciaBancos = boletoDmn.getNrMotivoRejeicao();
			
			List<MotivoOcorrenciaBanco> findMotivoOcorrenciaForRetornoBanco = motivoOcorrenciaBancoService.findMotivoOcorrenciaForRetornoBanco(nrOcorrenciaBanco.shortValue(), idCedente, motivoOcorrenciaBancos.shortValue());
			
			if(findMotivoOcorrenciaForRetornoBanco.isEmpty()) {
				retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36361", new Object[]{boletoDmn.getNrMotivoRejeicao()});
				return false;
			}
			
			historicoMotivoOcorrencia.setMotivoOcorrenciaBanco(findMotivoOcorrenciaForRetornoBanco.iterator().next());
		}

		this.store(historicoBoleto);

		return true;
	}
	
	public List<Map<String, Object>> findHistoricoBoletoByDhOcorrencias(Date dhOcorrencia, Long idCedente) {
		return  getHistoricoBoletoDAO().findHistoricoBoletoByDhOcorrencias(dhOcorrencia, idCedente);
	}
	
	public List<HistoricoBoletoDMN> generateBoletosHistorico() {
		Date dataInicio = (Date) configuracoesFacade.getValorParametro("DATA_INICIO_CORTE_ENVIO_HSBC");
		Long idCedenteHSBC = ((BigDecimal) configuracoesFacade.getValorParametro("ID_CEDENTE_HSBC")).longValue();
		
		List<Map<String, Object>>historicoBoletosList = findHistoricoBoletoByDhOcorrencias(dataInicio,idCedenteHSBC);
		List<HistoricoBoletoDMN> historicoBoletosDTOList = new ArrayList<HistoricoBoletoDMN>();
		String percentualMulta = parametroGeralService.findByNomeParametro("PC_MULTA_ATRASO_BOLETO").getDsConteudo();
		Map<Long, String> historicoBoletoMap = new HashMap<Long, String>();
		Map<Long, Map<String, String>> boletoMap = new HashMap<Long, Map<String, String>>();
		for (Map<String, Object> historicoBoletoDTOMap : historicoBoletosList) {
			updateTiposSituacaoBoleto(boletoMap, historicoBoletoDTOMap);
			historicoBoletoMap.put((Long)historicoBoletoDTOMap.get("ID_HISTORICO_BOLETO"), "T");
			historicoBoletosDTOList.add(createBoletoCanonico(historicoBoletoDTOMap, percentualMulta));
		}
		getHistoricoBoletoDAO().executeUpdateHistoricoBoleto(historicoBoletoMap);
		getHistoricoBoletoDAO().executeUpdateBoleto(boletoMap);
		return  historicoBoletosDTOList;
	}
	
	private void updateTiposSituacaoBoleto(Map<Long, Map<String, String>> boletoMap, Map<String, Object> historicoBoletoDTOMap) {
		Map<String, String> tipos = new HashMap<String, String>();
		String tpSituacaoBoleto = (String) historicoBoletoDTOMap.get("TP_SITUACAO_BOLETO");
		Short nrOcorrenciaBanco = (Short) historicoBoletoDTOMap.get("NR_OCORRENCIA_BANCO");
		if (("EM".equals(tpSituacaoBoleto) || "DI".equals(tpSituacaoBoleto)) && ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_ENVIO.equals(nrOcorrenciaBanco)) {
			tipos.put("tpSituacaoAntBoleto", tpSituacaoBoleto);
			tipos.put("tpSituacaoBoleto", "BN");
			boletoMap.put((Long)historicoBoletoDTOMap.get("ID_BOLETO"), tipos);
		} else if (TP_BOLETOS_EM_BANCO.contains(tpSituacaoBoleto) && ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_BAIXA.equals(nrOcorrenciaBanco)) {
			tipos.put("tpSituacaoAntBoleto", tpSituacaoBoleto);
			tipos.put("tpSituacaoBoleto", "EM");
			boletoMap.put((Long)historicoBoletoDTOMap.get("ID_BOLETO"), tipos);
		} else if ("BN".equals(tpSituacaoBoleto) && ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_PROTESTO.equals(nrOcorrenciaBanco)) {
			tipos.put("tpSituacaoAntBoleto", tpSituacaoBoleto);
			tipos.put("tpSituacaoBoleto", "BP");
			boletoMap.put((Long)historicoBoletoDTOMap.get("ID_BOLETO"), tipos);
		}
		
	}
	
	private HistoricoBoletoDMN createBoletoCanonico(Map<String, Object> historicoBoleto, String percentualMulta) {
		
		String enderecoPadrao = "";
		
		if (historicoBoleto.get("ID_ENDERECO") == null) {
			enderecoPadrao = "_DEF";
		}
		
		String enderecoPessoaComplemento = null;
		if (historicoBoleto.get("DS_TIPO_LOGRADOURO" + enderecoPadrao) != null) {
			enderecoPessoaComplemento = enderecoPessoaService.formatEnderecoPessoaComplemento(
					(String)historicoBoleto.get("DS_TIPO_LOGRADOURO" + enderecoPadrao),
					(String)historicoBoleto.get("DS_ENDERECO" + enderecoPadrao),
					(String)historicoBoleto.get("NR_ENDERECO" + enderecoPadrao),
					(String)historicoBoleto.get("DS_COMPLEMENTO" + enderecoPadrao)
			);
		}
		
		BigDecimal pcMultaAtraso = new BigDecimal(percentualMulta);
		BigDecimal valorCobrado = (BigDecimal)historicoBoleto.get("VL_TOTAL"); 
		BigDecimal vlMulta = valorCobrado.multiply(pcMultaAtraso.divide(BigDecimalUtils.HUNDRED));
		vlMulta = BigDecimalUtils.round(vlMulta);
	
		return new HistoricoBoletoDMN(
				(Long)historicoBoleto.get("ID_HISTORICO_BOLETO"),
				(Short)historicoBoleto.get("NR_BANCO"),
				(Short)historicoBoleto.get("NR_AGENCIA_BANCARIA"),
				(String)historicoBoleto.get("NR_DIGITO"),
				(String)historicoBoleto.get("NR_CONTA_CORRENTE"),
				(String)historicoBoleto.get("DS_NOME_ARQUIVO_COBRANCA"),
				(String)historicoBoleto.get("NR_IDENTIFICACAO_FILIAL"),
				(String)historicoBoleto.get("SG_FILIAL"),
				(Long)historicoBoleto.get("NR_FATURA"),
				(String)historicoBoleto.get("NR_BOLETO"),
				(Short)historicoBoleto.get("NR_OCORRENCIA_BANCO"),
				(YearMonthDay)historicoBoleto.get("DT_EMISSAO"),
				(YearMonthDay)historicoBoleto.get("DT_VENCIMENTO"),
				(BigDecimal)historicoBoleto.get("VL_TOTAL"),
				(BigDecimal)historicoBoleto.get("VL_JUROS_DIA"),
				(BigDecimal)historicoBoleto.get("VL_DESCONTO"),
				vlMulta,
				(String)historicoBoleto.get("TP_PESSOA_CLIENTE"),
				(String)historicoBoleto.get("NR_IDENTIFICACAO"),
				(String)historicoBoleto.get("NM_PESSOA"),
				enderecoPessoaComplemento,
				(String)historicoBoleto.get("NR_CEP" + enderecoPadrao),
				(String)historicoBoleto.get("DS_BAIRRO" + enderecoPadrao),
				(String)historicoBoleto.get("NM_MUNICIPIO" + enderecoPadrao),
				(String)historicoBoleto.get("SG_UNIDADE_FEDERATIVA" + enderecoPadrao),
				null,null,null,null,null,null,null,null
				);
	
	}

	private Usuario getUsuario() {
		if (SessionUtils.getFilialSessao() == null) {
			return usuarioService.findById(USUARIO_INTEGRACAO);
		}
		
		return SessionUtils.getUsuarioLogado();
	}

	public MotivoOcorrenciaBancoService getMotivoOcorrenciaBancoService() {
		return motivoOcorrenciaBancoService;
	}
	public void setMotivoOcorrenciaBancoService(
			MotivoOcorrenciaBancoService motivoOcorrenciaBancoService) {
		this.motivoOcorrenciaBancoService = motivoOcorrenciaBancoService;
	}
	
	public void setEnvioOcorrenciaBoletoBancoService(EnvioOcorrenciaBoletoBancoService envioOcorrenciaBoletoBancoService) {
		this.envioOcorrenciaBoletoBancoService = envioOcorrenciaBoletoBancoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setRetornoBancoService(RetornoBancoService retornoBancoService) {
		this.retornoBancoService = retornoBancoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void updateHistoricoBoletoBradesco(Long idHistoricoBoleto,
			Long seqRemessa) {
		HistoricoBoleto historicoBoleto = findById(idHistoricoBoleto);
		if(historicoBoleto != null){
			Boleto boleto = historicoBoleto.getBoleto();
			if ("EM".equals(boleto.getTpSituacaoBoleto().getValue()) &&
					Short.valueOf("1").equals(historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco())){
				boleto.setTpSituacaoBoleto(new DomainValue("BN"));
			}else if("BN".equals(boleto.getTpSituacaoBoleto().getValue()) &&
					Short.valueOf("2").equals(historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco())){
				boleto.setTpSituacaoBoleto(new DomainValue("EM"));
			}else if("BN".equals(boleto.getTpSituacaoBoleto().getValue()) &&
					Short.valueOf("9").equals(historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco())){
				boleto.setTpSituacaoBoleto(new DomainValue("BP"));
			}
			boletoService.storeBasic(boleto);
			
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("T"));
			store(historicoBoleto);
			
     	     if (boleto.getCedente().getSqCobranca() <= seqRemessa){
     	    	boleto.getCedente().setSqCobranca(seqRemessa+1);
     	    	cedenteService.store(boleto.getCedente());
     	     }
		}
	}
	
	public void generateEnvioBoletoBradesco(){
		
		if (isFinanceiroCorporativo()){
			return;
		}
		
		Date dataInicio = (Date) configuracoesFacade.getValorParametro("DATA_INICIO_CORTE_ENVIO_HSBC");
		Long idCedenteBradesco = ((BigDecimal) configuracoesFacade.getValorParametro("ID_CEDENTE_BRADESCO")).longValue();
		
		List<Map<String, Object>> historicosBoletoList = findHistoricoBoletoByDhOcorrencias(dataInicio,idCedenteBradesco);
		
		boolean primeiroRegistro = true;
		
		ArquivoRemessaPaiDTO arquivoRemessaPaiDTO = new ArquivoRemessaPaiDTO();
		
		for (Map<String, Object> historicoBoletoMap : historicosBoletoList) {
			HistoricoBoletoDMN historicoBoletoDmn = buildHistoricoBoletoDmn(historicoBoletoMap);
			
			if(isBoletoCancelado(historicoBoletoDmn.getTpSituacaoBoleto()) 
					&& !Short.valueOf("2").equals(historicoBoletoDmn.getNrOcorrenciaBanco())){
				storeHistoricoBoleto(historicoBoletoDmn, new DomainValue("T"));
			}else if(isBoletoVencido(historicoBoletoDmn.getDtVencimento())
					&& Short.valueOf("1").equals(historicoBoletoDmn.getNrOcorrenciaBanco())){
				HistoricoBoleto historicoBoleto = storeHistoricoBoleto(historicoBoletoDmn, new DomainValue("C"));
				List<MotivoOcorrenciaBanco> motivosMotivoOcorrenciaBanco = motivoOcorrenciaBancoService.findMotivoOcorrenciaForRetornoBanco(Short.valueOf("3"), 
						historicoBoleto.getBoleto().getCedente().getIdCedente(), Short.valueOf("66"));
				storeHistoricoMotivoOcorrencia(historicoBoleto, motivosMotivoOcorrenciaBanco);
			}else if (Short.valueOf("9").equals(historicoBoletoDmn.getNrOcorrenciaBanco())
					&& isBoletoMercurio(historicoBoletoDmn.getNrIdentificacao())){
				HistoricoBoleto historicoBoleto = storeHistoricoBoleto(historicoBoletoDmn, new DomainValue("C"));
				List<MotivoOcorrenciaBanco> motivosMotivoOcorrenciaBanco = motivoOcorrenciaBancoService.findMotivoOcorrenciaForRetornoBanco(Short.valueOf("3"), 
						historicoBoleto.getBoleto().getCedente().getIdCedente(), Short.valueOf("90"));
				storeHistoricoMotivoOcorrencia(historicoBoleto, motivosMotivoOcorrenciaBanco);
			}else {
				arquivoRemessaPaiDTO.addArquivoRemessa(buildHistoricoBoleto(historicoBoletoDmn));
			}
			
			if(primeiroRegistro){
				buildPrimeiroRegistroArquivoRemessaPai(arquivoRemessaPaiDTO, historicoBoletoDmn);
				primeiroRegistro = false;
			}
		}
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.ENVIO_BANCARIO_BRADESCO, arquivoRemessaPaiDTO);
		integracaoJmsService.storeMessage(msg);
	}
	
	private HistoricoBoletoDMN buildHistoricoBoletoDmn(Map<String, Object> historicoBoletoMap) {
		return new HistoricoBoletoDMN((Long)historicoBoletoMap.get("ID_HISTORICO_BOLETO"), null, 
				(Short)historicoBoletoMap.get("NR_AGENCIA_BANCARIA"), null, (String)historicoBoletoMap.get("NR_CONTA_CORRENTE"), 
				(String)historicoBoletoMap.get("DS_NOME_ARQUIVO_COBRANCA"), null, (String)historicoBoletoMap.get("SG_FILIAL"), 
				(Long)historicoBoletoMap.get("NR_FATURA"), (String)historicoBoletoMap.get("NR_BOLETO"), (Short)historicoBoletoMap.get("NR_OCORRENCIA_BANCO"), 
				(YearMonthDay)historicoBoletoMap.get("DT_EMISSAO"), (YearMonthDay)historicoBoletoMap.get("DT_VENCIMENTO"), 
				(BigDecimal)historicoBoletoMap.get("VL_TOTAL"), (BigDecimal)historicoBoletoMap.get("VL_JUROS_DIA"), 
				(BigDecimal)historicoBoletoMap.get("VL_DESCONTO"), null, (String)historicoBoletoMap.get("TP_PESSOA_CLIENTE"),
				(String)historicoBoletoMap.get("NR_IDENTIFICACAO"), (String)historicoBoletoMap.get("NM_PESSOA"), 
				null, null, null, null, null, (Long)historicoBoletoMap.get("NR_CARTEIRA"), 
				(String)historicoBoletoMap.get("DS_ENDERECO_DEF"), (String)historicoBoletoMap.get("NR_ENDERECO_DEF"), 
				(String)historicoBoletoMap.get("DS_TIPO_LOGRADOURO_DEF"), (String)historicoBoletoMap.get("DS_COMPLEMENTO_DEF"), 
				(String)historicoBoletoMap.get("NR_CEP_DEF"), (Long)historicoBoletoMap.get("SQ_COBRANCA"), 
				(String)historicoBoletoMap.get("TP_SITUACAO_BOLETO"));
	}
	
	private HistoricoBoleto storeHistoricoBoleto(HistoricoBoletoDMN historicoBoletoDmn, 
			DomainValue tpSituacaoHistoricoBoleto) {
		HistoricoBoleto historicoBoleto = findById(historicoBoletoDmn.getIdHistoricoBoleto());
		historicoBoleto.setTpSituacaoHistoricoBoleto(tpSituacaoHistoricoBoleto);
		store(historicoBoleto);
		return historicoBoleto;
	}
	
	private void storeHistoricoMotivoOcorrencia(HistoricoBoleto historicoBoleto,
			List<MotivoOcorrenciaBanco> motivosMotivoOcorrenciaBanco) {
		if (motivosMotivoOcorrenciaBanco != null && motivosMotivoOcorrenciaBanco.size()>0){
			HistoricoMotivoOcorrencia historicoMotivoOcorrencia = new HistoricoMotivoOcorrencia();
			historicoMotivoOcorrencia.setHistoricoBoleto(historicoBoleto);
			historicoMotivoOcorrencia.setMotivoOcorrenciaBanco(motivosMotivoOcorrenciaBanco.get(0));
			historicoMotivoOcorrenciaService.store(historicoMotivoOcorrencia);
		}
	}
	
	private ArquivoRemessaDTO buildHistoricoBoleto(HistoricoBoletoDMN historicoBoletoDmn) {
		ArquivoRemessaDTO dto = new ArquivoRemessaDTO();
		
		dto.setIdHistoricoBoleto(historicoBoletoDmn.getIdHistoricoBoleto());
		dto.setEmpresaBeneficiaria(buildEmpresaBeneficiaria(historicoBoletoDmn));
		dto.setNrBoleto(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrBoleto(), 25) );
		
		BigDecimal pcMulta = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("PC_MULTA_ATRASO_BOLETO", false);
		dto.setPcMulta(FormatUtils.formatBigDecimalWithPattern(pcMulta, "00.00").replaceAll(REMOVE_DECIMAL_REGEX, ""));
		
		String nrTitulo = historicoBoletoDmn.getNrBoleto().substring(1, 12);
		dto.setNrTitulo(FormatUtils.fillNumberWithZero(nrTitulo, 11));
		dto.setDvTitulo(historicoBoletoDmn.getNrBoleto().substring(12, 13));		
		
		String nrDias = ((BigDecimal)parametroGeralService.findConteudoByNomeParametroWithoutException("NR_DIAS_DECURSO_PRAZO_BRADESCO", false)).toString();
		dto.setInstrucao2(FormatUtils.fillNumberWithZero(nrDias, 2));
		
		dto.setNrOcorrenciaBanco(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrOcorrenciaBanco().toString(),2));
		
		dto.setNrFatura(historicoBoletoDmn.getSgFilial() + FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrFatura().toString(), 7));
		dto.setDtVencimento(historicoBoletoDmn.getDtVencimento().toString("ddMMyy"));
		dto.setVlTotal(
				FormatUtils.formatBigDecimalWithPattern(historicoBoletoDmn.getVlTotal(), "00000000000.00").replaceAll(REMOVE_DECIMAL_REGEX, "")
				);
		dto.setDtEmissao(historicoBoletoDmn.getDtEmissao().toString("ddMMyy"));
		dto.setVlJurosDia((FormatUtils.formatBigDecimalWithPattern(historicoBoletoDmn.getVlJurosDia(), "00000000000.00").replaceAll(REMOVE_DECIMAL_REGEX, "")));
		dto.setVlDesconto((FormatUtils.formatBigDecimalWithPattern(historicoBoletoDmn.getVlDesconto(), "00000000000.00").replaceAll(REMOVE_DECIMAL_REGEX, "")));
		
		dto.setTpPessoaCliente("J".equals(historicoBoletoDmn.getTpPessoaCliente())?"02":"01");
		dto.setNrIdentificacao(historicoBoletoDmn.getNrIdentificacao());
		dto.setNmPessoa(FormatUtils.fillStringWithSpace(historicoBoletoDmn.getNmPessoa(), 40).substring(0, 40));
		
		String endereco = enderecoPessoaService.formatEnderecoPessoaComplemento(historicoBoletoDmn);
		dto.setEnderecoPessoaCompleto(FormatUtils.fillStringWithSpace(endereco, 40).substring(0, 40));
		dto.setNrCEP(historicoBoletoDmn.getNrCepDef().substring(0,5));
		dto.setSufixoCEP(historicoBoletoDmn.getNrCepDef().substring(5,8));
		
		return dto;
	}
	
	private void buildPrimeiroRegistroArquivoRemessaPai(ArquivoRemessaPaiDTO arquivoRemessaPaiDTO,
			HistoricoBoletoDMN historicoBoletoDmn) {
		arquivoRemessaPaiDTO.setDsNomeArquivoCobranca(formatFileName(historicoBoletoDmn.getDsNomeArquivoCobranca(), JTDateTimeUtils.getDataAtual()));
		arquivoRemessaPaiDTO.setSeqRemessa(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getSqCobranca().toString(), 7));
		BigDecimal cdEmpresa = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("CD_EMPRESA_TNT_BRADESCO", false);
		arquivoRemessaPaiDTO.setCdEmpresa(FormatUtils.fillNumberWithZero(cdEmpresa.toString(),20));
	}
	
	private String buildEmpresaBeneficiaria(HistoricoBoletoDMN historicoBoletoDmn) {
		StringBuilder cdEmpresa = new StringBuilder();
	
		cdEmpresa.append("0");
		cdEmpresa.append(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrCarteira().toString(), 3));
		cdEmpresa.append(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrAgenciaBancaria().toString(), 5));
		cdEmpresa.append(FormatUtils.fillNumberWithZero(historicoBoletoDmn.getNrContaCorrente(), 8));
		
		return cdEmpresa.toString();
	}
	
	private boolean isBoletoMercurio(String nrIdentificacaoPessoaClienteFaturaBoleto) {
		String cnpjMercurio = ((BigDecimal)parametroGeralService.findConteudoByNomeParametroWithoutException("NR_CNPJ_MERCURIO", false)).toString();
		return nrIdentificacaoPessoaClienteFaturaBoleto.startsWith(cnpjMercurio);
	}
	
	private boolean isBoletoCancelado(String tpSituacaoBoleto){
		return "CA".equals(tpSituacaoBoleto.trim())
				|| "LI".equals(tpSituacaoBoleto.trim());
	}
	
	private boolean isBoletoVencido(YearMonthDay dtVencimentoBoleto){
		return JTDateTimeUtils.comparaData(dtVencimentoBoleto, JTDateTimeUtils.getDataAtual()) <= 0;
	}

	private boolean isFinanceiroCorporativo() {
		String blFinanceiroCorporativo = (String)parametroGeralService.findConteudoByNomeParametro("BL_FINANCEIRO_CORPORATIVO", false);
		return "S".equals(blFinanceiroCorporativo);
	}
	
	public void setHistoricoMotivoOcorrenciaService(
			HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService) {
		this.historicoMotivoOcorrenciaService = historicoMotivoOcorrenciaService;
	}
	
	
	private static String formatFileName(String fileName, YearMonthDay date){
		fileName = fileName.replaceAll("DD", FormatUtils.fillNumberWithZero(Long.valueOf(date.getDayOfMonth()).toString(), 2));
		fileName = fileName.replaceAll("MM", FormatUtils.fillNumberWithZero(Long.valueOf(date.getMonthOfYear()).toString(), 2));
		return fileName;
	}
	
	
}