package com.mercurio.lms.expedicao.model.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.expedicao.AwbCiaAerea;
import br.com.tntbrasil.integracao.domains.expedicao.CancelAwbCiaAerea;
import br.com.tntbrasil.integracao.domains.expedicao.ParcelaAwbCiaAerea;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ParcelaAwb;
import com.mercurio.lms.expedicao.model.PreAlerta;
import com.mercurio.lms.expedicao.model.dao.AwbDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.FTPClientHolder;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD: 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.awbService"
 */

@Assynchronous(name = "awbService")
public class AwbService extends CrudService<Awb, Long> {
	private PreAlertaService preAlertaService;
	private CtoAwbService ctoAwbService;
	private ParametroGeralService parametroGeralService;
	private RecursoMensagemService recursoMensagemService;
	private FilialService filialService;
	private LogCargaAwbService logCargaAwbService;
	private PessoaService pessoaService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private AeroportoService aeroportoService;
	private ClienteService clienteService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private DomainValueService domainValueService;
	private ProdutoEspecificoService produtoEspecificoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private ConhecimentoService conhecimentoService;
	private ParcelaAwbService parcelaAwbService;
	private LiberaAWBComplementarService liberaAWBComplementarService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private DoctoServicoService doctoServicoService;
	private AnexoAwbService anexoAwbService;
	private UsuarioService usuarioService;
	private PedidoColetaService pedidoColetaService;
	private AwbOcorrenciaService awbOcorrenciaService;
	private CalculoFreteCiaAereaService calculoFreteCiaAereaService;
	private IntegracaoJmsService integracaoJmsService;
	
	private static final int PERCENTUAL = 100;
	private static final int CASAS_DECIMAIS = 2;
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	/**
	 * Recupera uma instância de <code>Awb</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Awb findById(java.lang.Long id) {
		return (Awb)super.findById(id);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idFilialOrigem
	 * @param idCiaFilialMercurio
	 * @param nrAwb
	 * @param dsSerie
	 * @return <Awb>
	 */
	public Awb findAwb(Long idFilialOrigem, Long idCiaFilialMercurio, Long nrAwb, String dsSerie) {
		return getAwbDAO().findAwb(idFilialOrigem, idCiaFilialMercurio, nrAwb, dsSerie);
	}

	public List findLookupByNrAwbIdCiaAereaIdFilial(Long nrAwb, Long idCiaAerea, Long idFilial, String tpStatusAwb) {
		List result = findByNrAwbByCiaAerea(nrAwb, idCiaAerea, idFilial, tpStatusAwb);

		if(result.size() > 0) {
			Map m = (Map) result.get(0);
			String tp = (String)m.get("tpStatusAwb.value");
			if(!"E".equals(tp)) {
				throw new BusinessException("LMS-04131");
			}
			validateAwbVooConfirmado(nrAwb);
		}
		return result;
	}

	public void validateAwbVooConfirmado(Long nrAwb) {
		List result = preAlertaService.findByNrAwb(nrAwb);
		if(result.size() > 0) {
			boolean vooConfirmado = ((TypedFlatMap)result.get(0)).getBoolean("blVooConfirmado").booleanValue();
			if (vooConfirmado) {
				throw(new BusinessException("LMS-04132"));
			}
		}
	}

	public List findByNrAwb(String nrAwb){
		return getAwbDAO().findByNrAwb(nrAwb);
	}

	@Override
	protected void beforeFindSuggest(Map<String, Object> filter) {
		if (ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(filter.get("tpStatusAwb"))) {
			filter.putAll(AwbUtils.splitNrAwbToMap(filter.get("nrAwb").toString()));
		} else {
			filter.put("idAwb", filter.get("nrAwb"));
			filter.remove("nrAwb");
		}
	}

	public List findByNrAwbByCiaAerea(Long nrAwb, Long idCiaAerea, Long idFilialOrigem, String tpStatusAwb) {
		Awb awb = AwbUtils.splitNrAwb(nrAwb.toString());
		return getAwbDAO().findByNrAwbByCiaAerea(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb(),idCiaAerea,idFilialOrigem,tpStatusAwb);
	}

	public List findByNrAwbByEmpresa(String dsSerie, Long nrAwb, Long idEmpresa, Integer digito) {
		return getAwbDAO().findByNrAwbByEmpresa(dsSerie, nrAwb,idEmpresa, digito);
	}
	
	public Awb findPreAwb(Long nrAwb, String tpStatusAwb, Long idFilial) {
		Awb awb = getAwbDAO().findPreAwb(nrAwb, tpStatusAwb, idFilial);
		if(awb != null){
			awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa();
		}
		return awb;
	}
	
	public List findLookupAwb(Map criteria) {
		Map ciaFilialMercurio = new HashMap();
		Map empresa = new HashMap();
		if (criteria.get("idEmpresa") != null) {
			empresa.put("idEmpresa", criteria.get("idEmpresa"));
		} else {
			empresa.put("idEmpresa", criteria.get("idEmpresaCiaAerea"));
		}
		ciaFilialMercurio.put("empresa", empresa);
		criteria.put("ciaFilialMercurio", ciaFilialMercurio);
		criteria.remove("idEmpresa");
		Object sNrAwb = criteria.get("nrAwb");
		if(ConstantesAwb.TP_STATUS_PRE_AWB.equals(criteria.get("tpStatusAwb"))){
			Long nrAwb = LongUtils.getLong(sNrAwb);
			criteria.put("idAwb", nrAwb);
			criteria.remove("nrAwb");
		} else {
			Awb awb = AwbUtils.splitNrAwb(sNrAwb.toString());
			criteria.put("nrAwb", awb.getNrAwb() );
			criteria.put("dsSerie", awb.getDsSerie());
			criteria.put("dvAwb", awb.getDvAwb());
		}
		
		return findLookup(criteria);
	}

	public List findDocumentosServico(Long idAwb){
		return getAwbDAO().findDocumentosServico(idAwb);
	}
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(Awb bean) {
		return super.store(bean);
	}

	/**
	 * Força execucao do Flush.
	 * @author Andre Valadas
	 */
	public void executeFlush() {
		getAwbDAO().getAdsmHibernateTemplate().flush();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param dao
	 */
	public void setAwbDAO(AwbDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private AwbDAO getAwbDAO() {
		return (AwbDAO) getDao();
	}
	
	/**
	 * GT2
	 * Busca os AWBs para gravar na prestação de contas
	 * @param idFilialPrestadora
	 * @param idCiaAerea
	 * @param dtDecendioInicial
	 * @param dtDecendioFinal
	 * @return Uma lista contendo o retorno da consulta
	 */
	public List findPrestacaoContasICMS(Long idFilialPrestadora, Long idCiaAerea, YearMonthDay dtDecendioInicial, YearMonthDay dtDecendioFinal) {
		return getAwbDAO().findPrestacaoContasICMS(idFilialPrestadora, idCiaAerea, dtDecendioInicial, dtDecendioFinal);
	}

	/**
	 * GT2
	 * Busca os valores acumulados conforme aliquota de icms
	 * @param idFilialPrestadora
	 * @param idCiaAerea
	 * @param dtDecendioInicial
	 * @param dtDecendioFinal
	 * @return Uma lista contendo o retorno da consulta 
	 */
	public List findPrestacaoContas(Long idFilialPrestadora, Long idCiaAerea, YearMonthDay dtDecendioInicial, YearMonthDay dtDecendioFinal){
		return getAwbDAO().findPrestacaoContas(idFilialPrestadora, idCiaAerea, dtDecendioInicial, dtDecendioFinal);
	}
	
	/**
	 * GT2
	 * Seta para null o id da prestação do awb
	 * @param idPrestacaoConta
	 */
	public void updateAwbDesmarcarPrestacaoConta(Long idPrestacaoConta){
		this.getAwbDAO().updateAwbDesmarcarPrestacaoConta(idPrestacaoConta);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getAwbDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getAwbDAO().getRowCount(criteria);
	}
	
	public void executeCancelarAwbs(TypedFlatMap data) {
		DomainValue tpMotivoCancelamento = getDomainValueService().findDomainValueByValue("DM_TIPO_CANCELAMENTO_AWB", data.getDomainValue("tpMotivoCancelamentoAwb").getValue()); 
		String dsMotivoCancelamento = data.getString("dsTpMotivoCancelamentoAwb");
	
		Long idAwb = data.getLong("idAwb");
		if (idAwb != null) {
			Awb awb = findById(idAwb);
			
			if (!SessionUtils.isFilialSessaoMatriz()) {
				throw new BusinessException("LMS-04524");
			}
			
			if (!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equals(awb.getTpStatusAwb().getValue())) {
				throw new BusinessException("LMS-04187");
			}
			DomainValue cancelado = new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO);
			awb.setTpStatusAwb(cancelado);
			
			awb.setTpMotivoCancelamento(tpMotivoCancelamento);
			awb.setDsMotivoCancelamento(dsMotivoCancelamento);
			awb.setUsuarioCancelamento(SessionUtils.getUsuarioLogado());
			awb.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
			
			store(awb);
			executeCalcularRateioCtoAwbCancelamento(awb);
			
			String justificativaWKCancelamento = awb.getTpMotivoCancelamento().getDescriptionAsString() + " - " + dsMotivoCancelamento;
			executeWorkflowCancelamentoAwb(awb, justificativaWKCancelamento, SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());
		}
	}

	private void executeWorkflowCancelamentoAwb(Awb awbCancelamento, String justificativa, Usuario usuario, Long idEmpresa) {
		DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
		String chaveAwbFormatada = AwbUtils.getSgEmpresaAndNrAwbFormated(awbCancelamento);
		String dsProcesso = configuracoesFacade.getMensagem("LMS-04517", new Object[] { 
				chaveAwbFormatada,
				justificativa });
		Long idFilial = awbCancelamento.getFilialByIdFilialOrigem().getIdFilial();
		Long idProcesso = awbCancelamento.getIdAwb();
		Short nrTipoEvento = ConstantesWorkflow.NR406_XML_CANCELAMENTO_AWB;
		
		getWorkflowPendenciaService().generatePendencia(idFilial, nrTipoEvento, idProcesso, dsProcesso, dhLiberacao, usuario, idEmpresa);
	}

	private void generatePreAlerta(Awb awb) {
		PreAlerta preAlerta = new PreAlerta();
		preAlerta.setAwb(awb);
		preAlerta.setBlVooConfirmado(Boolean.FALSE);
		preAlertaService.store(preAlerta);
	}
	
	public void validateRentabilidadeAwb(List<CtoAwb> ctosAwb, Double vlFrete, String justificativaPrejuizoAwb, String tipoAwb) {
		Double valorFreteLiquidoDocumentos = getValorLiquidoFreteDocumentos(ctosAwb);
		Double custo = calcularRentabilidade(valorFreteLiquidoDocumentos, getPercentualRentabilidade());
		
		if(vlFrete.doubleValue() > custo && !StringUtils.isNotBlank(justificativaPrejuizoAwb)){
			throw new BusinessException("LMS-04513", new Object[]{
					tipoAwb,
					FormatUtils.formatBigDecimalWithPattern(BigDecimal.valueOf(valorFreteLiquidoDocumentos).setScale(CASAS_DECIMAIS, RoundingMode.CEILING), "#,##0.00"), 
					FormatUtils.formatBigDecimalWithPattern(BigDecimal.valueOf(custo).setScale(CASAS_DECIMAIS, RoundingMode.CEILING), "#,##0.00")});
		}
	}

	private Double getPercentualRentabilidade() {
		ParametroGeral rentabilidadeAbw = parametroGeralService.findByNomeParametro("PERCENTUAL_RENT_AWB");
		return Double.parseDouble(rentabilidadeAbw.getDsConteudo());
	}
	
	private Double getValorLiquidoFreteDocumentos(List<CtoAwb> ctosAwb) {
		Double valorFreteLiquidoDocumentos = new Double(0);
		for (CtoAwb cto : ctosAwb){
			if(cto.getConhecimento().getVlLiquido() == null){
				cto.getConhecimento().setVlLiquido(getDoctoServicoService().findVlFreteLiquidoByIdDoctoServico(cto.getConhecimento().getIdDoctoServico()));
			}
			valorFreteLiquidoDocumentos += cto.getConhecimento().getVlLiquido().doubleValue();
		}
		return valorFreteLiquidoDocumentos;
	}

	/**
	 * 
	 * @param valorFreteLiquidoDocumentos 
	 * @param percentualRentabilidade  
	 * @return
	 */
	public double calcularRentabilidade(Double valorFreteLiquidoDocumentos, Double percentualRentabilidade) {
		double rentabilidade = (valorFreteLiquidoDocumentos * percentualRentabilidade) / PERCENTUAL;
		return BigDecimal.valueOf(rentabilidade).setScale(CASAS_DECIMAIS, RoundingMode.CEILING).doubleValue();
	}

	public void onFTPError(FTPClientHolder ftp, String name, String messageError) throws IOException {
		ftp.renameFile(name + "." + ConstantesAwb.EXTENSAO_PROCESSANDO, name + "." + ConstantesAwb.EXTENSAO_NOK);
		String errFileName = name + "." + ConstantesAwb.EXTENSAO_ERR;
		OutputStream out = ftp.storeFileStream(errFileName);
		PrintStream ps = new PrintStream(out);
		try {
			ps.write(messageError.getBytes());
			out.flush();
		} finally {
			out.close();
			ps.close();
		}

		ftp.completePendingCommand();
	}
	
	public Awb findUltimoAwbByDoctoServico(Long idDoctoServico) {
		return getAwbDAO().findUltimoAwbByDoctoServico(idDoctoServico);
	}
	
	public String findPreAwbAwbByIdDoctoServico(Long idDoctoServico) {
		Awb awb = findUltimoAwbByDoctoServico(idDoctoServico);

		String strAwb = "";
		String ciaAerea = "";
		if (awb != null && awb.getNrAwb() != null && awb.getNrAwb() > 0) {
			String nrAwbFormatado = AwbUtils.getNrAwbFormated(awb);
			ciaAerea = setSgCiaAerea(awb.getCiaFilialMercurio());
			if(!"".equals(ciaAerea)){
				strAwb = ciaAerea + " " + nrAwbFormatado;
			}
		} else {
			if(awb != null ){
				ciaAerea = setSgCiaAerea(awb.getCiaFilialMercurio());
				if(!"".equals(ciaAerea) ){
					strAwb = ciaAerea + " " + awb.getIdAwb();
				}
			}
		}
		return strAwb;
	}
	
	
	/***************** INICIO PROCESSO DE IMPORTAÇÃO AUTOMÁTICA DE  AWB *********************/
		
	public void storeCancelaAwb(CancelAwbCiaAerea dto) {
		if(StringUtils.isNotBlank(dto.getNrChaveAwb())){
			Usuario usuario = findUsuarioInclusaoAutomatica();
			Awb awb = getAwbDAO().findByNrChave(dto.getNrChaveAwb()).get(0);
			getAwbDAO().updateCancelarAwb(dto.getNrChaveAwb(), dto.getJustificativa(), usuario.getIdUsuario());
			storeAnexosAwb(dto.getListaAnexos(), awb, false);
			executeCalcularRateioCtoAwbCancelamento(awb);
			
			ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(ConstantesExpedicao.ID_EMPRESA_MERCURIO);
			executeWorkflowCancelamentoAwb(awb, dto.getJustificativa(), usuario, Long.valueOf(parametroGeral.getDsConteudo()));
		}			
	}

	public void storeAwb(AwbCiaAerea dto) {
		Awb awb = validaDto(dto);
		awb = setDadosAwbFromDto(awb, dto);
		List<byte[]> anexos = dto.getListaAnexos();
		store(awb);
				
		vincularAwbAosCtes(dto.getListNrChaveCteAnt(), dto.getNrChaveInfNfe(), awb);
		storeParcelasAwb(dto.getParcelasAwb(), awb);
		storeAnexosAwb(anexos, awb, true);
		generatePreAlerta(awb);
		generateEventoAwbEmitido(awb);
	}
	
	public void generateEventoAwbEmitido(Awb awb) {
		List<CtoAwb> listCtoAwb = awb.getCtoAwbs();
		
		if(CollectionUtils.isEmpty(listCtoAwb) && !"CO".equals(awb.getTpAwb().getValue())) {
			listCtoAwb = ctoAwbService.findByIdAwb(awb.getIdAwb());
		}
		
		CiaFilialMercurio ciaFilialMercurio = awb.getCiaFilialMercurio();
		if (ciaFilialMercurio != null) {
			Empresa empresa = awb.getCiaFilialMercurio().getEmpresa();
			if(empresa == null || empresa.getIdEmpresa() == null) {
				awb.setCiaFilialMercurio(ciaFilialMercurioService.findById(ciaFilialMercurio.getIdCiaFilialMercurio()));
			}
			
		}
		
		String nrAwbFormatado = AwbUtils.getSgEmpresaAndNrAwbFormated(awb); 
		for (CtoAwb ctoAwb : listCtoAwb) {
			pedidoColetaService.generateEventoDoctoVolume(ctoAwb.getConhecimento(), nrAwbFormatado, ConstantesExpedicao.AIRWAY_BILL, ConstantesEventosDocumentoServico.CD_EVENTO_AWB_EMITIDO, null, awb.getFilialByIdFilialOrigem().getIdFilial(),null, findUsuarioInclusaoAutomatica(),awb.getDhEmissao());
		}
	}		
	
	private void storeAnexosAwb(List<byte[]> anexos, Awb awb, Boolean insereAwb) {
		anexoAwbService.store(anexos, awb, insereAwb);
	}

	private void storeParcelasAwb(List<ParcelaAwbCiaAerea> parcelasADto, Awb awbInsersaoAutomatica) {
		List<ParcelaAwb> parcelasAwb= new ArrayList<ParcelaAwb>();
		for (ParcelaAwbCiaAerea parcelaDto :  parcelasADto){
			ParcelaAwb parcelaAwb = new ParcelaAwb();
			parcelaAwb.setDsParcelaAwb(parcelaDto.getDsParcela());
			parcelaAwb.setVlParcelaAwb(parcelaDto.getVlParcela());
			parcelasAwb.add(parcelaAwb);
		}
		parcelaAwbService.store(parcelasAwb, awbInsersaoAutomatica);
	}
	
	private Awb setDadosAwbFromDto(Awb awb, AwbCiaAerea dto) {
		awb.setNrSerieDacte(dto.getNrSerieDacte());
		awb.setNrDacte(dto.getNrDacte());
		awb.setDsModeloDacte(dto.getDsModeloDacte());
		awb.setTpCte(dto.getTpCte());
		awb.setTpServico(dto.getTpServico());
		awb.setDsNaturezaPrestacao(dto.getDsNaturezaPrestacao());
		awb.setDsAutorizacaoUso(dto.getDsAutorizacaoUso()+ " " + JTDateTimeUtils.formatDateTimeToString(new DateTime(dto.getDhReceb()), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN));
		awb.setDhDigitacao(JTDateTimeUtils.getDataHoraAtual());
		awb.setDhEmissao(JTDateTimeUtils.formatStringToDateTimeWithSeconds(dto.getDhEmissao()));
		
		String sTpFrete = (LongUtils.ZERO.toString()).equals(dto.getForPag()) ? "C" : "F"; 
		DomainValue tpFrete = getDomainValueService().findDomainValueByValue("DM_TIPO_FRETE", sTpFrete); 
		awb.setTpFrete(tpFrete);
		
		if("E".equals(dto.getTpTarifa()) && StringUtils.isNotEmpty(dto.getCdTarifa())){
			ProdutoEspecifico produtoEspecifico = produtoEspecificoService.findByNrTarifa(Short.valueOf(dto.getCdTarifa()));
			awb.setProdutoEspecifico(produtoEspecifico);
		}
		
		awb.setVlFrete(dto.getVlFrete());
		awb.setVlFretePeso(dto.getVlFretePeso());
		awb.setVlTaxaTerrestre(BigDecimal.ZERO);
		awb.setVlTaxaCombustivel(BigDecimal.ZERO);
		awb.setVlICMS(dto.getVlICMS());
		awb.setPcAliquotaICMS(dto.getPcAliquotaICMS());
		awb.setNaturezaProduto(null); 
		awb.setVlQuiloTarifaSpot(BigDecimal.ZERO);
		awb.setMoeda(getMoedaByParametroGeral());
		
		awb.setObAwb(dto.getObAwb());
		awb.setTpStatusAwb(getDomainValueService().findDomainValueByValue("DM_STATUS_AWB", "E")); 
		awb.setTpLocalEmissao(new DomainValue("C")); 
		awb.setVlBaseCalcImposto(dto.getVlBaseCalcImposto());
		awb.setQtVolumes(dto.getQtVolumes());
		
		awb.setDsProddominante(dto.getDsProdPredominante());
		awb.setVlTotalMercadoria(dto.getVlTotalMercadoria());
		awb.setNmResponsavelApolice(dto.getNmResponsavelApolice());
		awb.setVlTotalServico(dto.getVlTotalServico());
		awb.setVlReceber(dto.getVlReceber());
		awb.setDsSituacaoTributaria(dto.getDsSituacaoTributaria());
		awb.setDsInfManuseio(dto.getDsInfManuseio());
		awb.setCdCargaEspecial(dto.getCdCargaEspecial());
		awb.setDsCaracAdicServ(dto.getDsCaracAdicServ());
		awb.setDtPrevistaEntrega(JTDateTimeUtils.formatStringToYearMonthDay(dto.getDtPrevistaEntrega())); 
		awb.setDsClasse(dto.getDsClasse());
		awb.setVlTarifa(dto.getVlTarifa());
		awb.setDsIdentificacaoTomador(dto.getDsIdentificacaoTomador());
		awb.setDsIdentificacaoEmissor(dto.getDsIdentificacaoEmissor());
		awb.setBlRetira("0".equals(dto.getBlRetira()) ? false : true);
		awb.setTpAwb(new DomainValue(ConstantesAwb.TP_AWB_NORMAL));
		awb.setNrApoliceSeguro(dto.getNrApoliceSeguro());
		awb.setTpLocalizacao(new DomainValue(ConstantesAwb.AGUARDANDO_EMBARQUE));
		awb.setPsTotal(dto.getPsTotal());
		awb.setPsBaseCalc(dto.getPsBaseCalc());
		awb.setPsCubado(dto.getPsCubado());
		
		awb.setDsOutCaracteristicas(dto.getDsOutCaracteristicas());
		awb.setDsUsoEmissor(dto.getDsUsoEmissor());
		awb.setNrOperacionalAereo(dto.getNrOperacionalAereo());
		awb.setNrMinuta(dto.getNrMinuta());
		awb.setCdTarifa(dto.getCdTarifa());
		
		if(StringUtils.isNotBlank(dto.getxCaracSer())){
			awb.setTpCaracteristicaServico(findTpCaracteristicaServicoByParametroGeral(dto.getxCaracSer()));
		}else if(StringUtils.isNotBlank(dto.getObAwb())){
			awb.setTpCaracteristicaServico(findTpCaracteristicaServicoByParametroGeral(dto.getObAwb()));
		}
	
		awb.setUsuarioInclusao(findUsuarioInclusaoAutomatica());
		
		awb.setVlFreteCalculado(executeCalculoFreteAwb(awb));
		
		return awb;
	}

	@SuppressWarnings("unchecked")
	private BigDecimal executeCalculoFreteAwb(Awb awb) {
		BigDecimal vlFreteCalculado = BigDecimal.ZERO;

		if (awb.getTpCaracteristicaServico() != null) {
			CiaFilialMercurio cfm = ciaFilialMercurioService.findById(awb.getCiaFilialMercurio().getIdCiaFilialMercurio());
			List<CalculoFreteCiaAerea> calculos = calculoFreteCiaAereaService.executeCalculosFreteAWB(awb, cfm.getEmpresa().getIdEmpresa());
			if (calculos != null && !calculos.isEmpty()) {
				vlFreteCalculado = calculos.get(0).getVlTotal();
			}
		}

		return vlFreteCalculado;
	}
	
	private String findTpCaracteristicaServicoByParametroGeral(String caracServicoAwb){
		String tpCaracteristicaServico = null;
	
		tpCaracteristicaServico = setTpCaracteristicaByParamentroGeral(caracServicoAwb, parametroGeralService.findByNomeParametro("SERVICO_CONVENCIONAL", false), "C");
		if(!StringUtils.isNotBlank(tpCaracteristicaServico)){
			tpCaracteristicaServico = setTpCaracteristicaByParamentroGeral(caracServicoAwb , parametroGeralService.findByNomeParametro("SERVICO_PROX_DIA", false), "P");
			if(!StringUtils.isNotBlank(tpCaracteristicaServico)){
				tpCaracteristicaServico = setTpCaracteristicaByParamentroGeral(caracServicoAwb , parametroGeralService.findByNomeParametro("SERVICO_PROX_VOO", false), "V");
			}
		}	
		return tpCaracteristicaServico;		
	}
	
	private String setTpCaracteristicaByParamentroGeral(String valorCaracServicoAwb, ParametroGeral parametroGeral, String valorCaracServicoBD) {
		String [] sTipos=parametroGeral.getDsConteudo().split(";");
		for (int i = 0; i < sTipos.length; i++) {
			if(valorCaracServicoAwb.contains(sTipos[i])){
				return valorCaracServicoBD;
			}
		}
		return null;
	}
	
	
	public Usuario findUsuarioInclusaoAutomatica() {
		ParametroGeral idUsuario = parametroGeralService.findByNomeParametro("ID_USUARIO_PADRAO_INTEGRACAO");
		Usuario usuario = usuarioService.findById(Long.valueOf(idUsuario.getDsConteudo()));
		return usuario;
	}

	private void vincularAwbAosCtes(List<String> listNrChaveCtesAnt, List<String> listNrChaveInfNfe, Awb awb) {
		List<Long> listIdConhecimento = new ArrayList<Long>();
		List<CtoAwb> ctos = new ArrayList<CtoAwb>();
		if(CollectionUtils.isNotEmpty(listNrChaveInfNfe)) {
			for (String nrChaveInfNfe : listNrChaveInfNfe) {
				List<NotaFiscalConhecimento>list = notaFiscalConhecimentoService.findByNrChave(nrChaveInfNfe);
				for (NotaFiscalConhecimento nfc : list) {
					listIdConhecimento.add(nfc.getConhecimento().getIdDoctoServico());
				}
			}
		} else if(CollectionUtils.isNotEmpty(listNrChaveCtesAnt)) {
			for (String nrChave : listNrChaveCtesAnt) {
				if(StringUtils.isNotBlank(nrChave)){
					MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findByNrChave(nrChave);
					listIdConhecimento.add(mde.getDoctoServico().getIdDoctoServico());
				}
			}
		}
		
		for (Long id : listIdConhecimento) {
			CtoAwb ctoAwb = new CtoAwb();
			ctoAwb.setAwb(awb);
			ctoAwb.setConhecimento(conhecimentoService.findById(id));
			ctos.add(ctoAwb);
		}
		
		if(CollectionUtils.isNotEmpty(ctos)){
			ctoAwbService.storeAll(executeCalcularRateioCtoAwb(awb, ctos, null));
		}
		
		awb.setCtoAwbs(ctos);
	}

	private void executeCalcularRateioCtoAwbCancelamento(Awb awb) {
		List<CtoAwb> ctoAwbs = null;

		if (ConstantesExpedicao.TP_AWB_NORMAL.equals(awb.getTpAwb().getValue())) {
			ctoAwbs = awb.getCtoAwbs();

			if(ctoAwbs != null){
				for (CtoAwb ctoAwb : ctoAwbs) {
					ctoAwb.setVlCusto(BigDecimal.ZERO);
				}
			}

		} else if (ConstantesExpedicao.TP_AWB_COMPLEMENTAR.equals(awb.getTpAwb().getValue())) {
			LiberaAWBComplementar liberaAWBComplementar = liberaAWBComplementarService.findByIdAwbComplementar(awb.getIdAwb());
			if(liberaAWBComplementar != null){
				ctoAwbs = executeCalcularRateioCtoAwb(liberaAWBComplementar.getAwbOriginal(), liberaAWBComplementar.getAwbOriginal().getCtoAwbs(), null);
			}
		}

		if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
			ctoAwbService.storeAll(ctoAwbs);
		}
	}
	
	public List<CtoAwb> executeCalcularRateioCtoAwb(Awb awb, List<CtoAwb> ctosAwb, Awb awbOriginal) {
		if(ctosAwb != null && !ctosAwb.isEmpty() && isAwbNaoCancelado(awb, awbOriginal)){
		
			BigDecimal vlFrete = awb.getVlFrete();
			if(awbOriginal != null){
				vlFrete = vlFrete.add(awbOriginal.getVlFrete());
			}
			
			BigDecimal psTotal = calcularPesoTotal(ctosAwb);
			BigDecimal psCto = BigDecimal.ZERO;
			BigDecimal vlCusto = BigDecimal.ZERO;
			BigDecimal vlFreteAux = BigDecimal.ZERO;
	
			for(CtoAwb ctoAwb : ctosAwb) {
				if(ctoAwb.getAwb() == null){
					ctoAwb.setAwb(awb);
				}
				
				Conhecimento conhecimento = ctoAwb.getConhecimento();
				if (conhecimento != null && conhecimento.getQtVolumes() != null) {
					psCto = getMaiorPeso(conhecimento.getPsAforado(), conhecimento.getPsReferenciaCalculo(), conhecimento.getPsReal());
					vlCusto = vlFrete.divide(psTotal, 6, RoundingMode.HALF_UP).multiply(psCto).setScale(2, RoundingMode.HALF_UP);
					ctoAwb.setVlCusto(vlCusto);
					vlFreteAux = vlFreteAux.add(vlCusto);
				}
			}
			
			if(vlFrete.compareTo(vlFreteAux) != 0){
				CtoAwb ctoAwbUltimo = ctosAwb.get(ctosAwb.size() - 1);  
				ctoAwbUltimo.setVlCusto(ctoAwbUltimo.getVlCusto().add(vlFrete.subtract(vlFreteAux)));
			}
		}
		
		return ctosAwb;
	}
	
	private Boolean isAwbNaoCancelado(Awb awb, Awb awbOriginal) {
		Boolean awbNaoCancelado = Boolean.TRUE;
		if(awbOriginal != null){
			awbNaoCancelado = !ConstantesAwb.TP_STATUS_CANCELADO.contentEquals(awbOriginal.getTpStatusAwb().getValue());
		} else {
			awbNaoCancelado = !ConstantesAwb.TP_STATUS_CANCELADO.contentEquals(awb.getTpStatusAwb().getValue());
		}
		return awbNaoCancelado;
	}

	private BigDecimal calcularPesoTotal(List<CtoAwb> ctosAwb){
		BigDecimal psTotal = BigDecimal.ZERO;
		
		for(CtoAwb ctoAwb : ctosAwb) {
			Conhecimento conhecimento = ctoAwb.getConhecimento();
			if (conhecimento != null && conhecimento.getQtVolumes() != null) {
				psTotal = psTotal.add(getMaiorPeso(conhecimento.getPsAforado(), conhecimento.getPsReferenciaCalculo(), conhecimento.getPsReal()));
			}
		}

		return psTotal;
	}
	
	private BigDecimal getMaiorPeso(BigDecimal psAforado, BigDecimal psFaturado, BigDecimal psDeclarado) {
		List<BigDecimal> somaPs = new ArrayList<BigDecimal>();
		somaPs.add(BigDecimal.ZERO);
		
		if(psAforado != null){
			somaPs.add(psAforado);
		}
		
		if(psFaturado != null){
			somaPs.add(psFaturado);
		}
		
		if(psDeclarado != null){
			somaPs.add(psDeclarado);
		}
		
		Collections.sort(somaPs, Collections.reverseOrder());
		
		return somaPs.get(0);
	}
	
	private Moeda getMoedaByParametroGeral() {
		ParametroGeral paramGeralMoeda = parametroGeralService.findByNomeParametro(ConstantesAwb.ID_MOEDA_REAL);
		Long idMoeda = Long.valueOf(paramGeralMoeda.getDsConteudo());
		Moeda moeda = null;
		if(idMoeda != null) {
			moeda = new Moeda();
			moeda.setIdMoeda(idMoeda);
		}
		return moeda;
	}
	
	/** INICIO - VALIDAÇÃO DA IMPORTAÇÃO  */
	 
	private Awb validaDto(AwbCiaAerea dto) {
		try {
			Awb awb = new Awb();
			awb.setDsSerie(dto.getDsSerie());
			awb.setNrAwb(dto.getNrAwb());
			awb.setDvAwb(dto.getDigito());
			String nrChave = dto.getNrChaveAwb();
			List<Awb> listAwb = this.getAwbDAO().findByNrChave(nrChave);
			if(CollectionUtils.isNotEmpty(listAwb)) {
				throw new BusinessException("LMS-04521", new Object[] {nrChave, AwbUtils.getSgEmpresaAndNrAwbFormated(listAwb.get(0))});
			}
			
			awb.setFilialByIdFilialDestino(this.validateFilial(dto.getIdentificacaoRecebedor(), "Destino"));
			awb.setFilialByIdFilialOrigem(this.validateFilial(dto.getNrIdentificacaoExp(), "Origem"));
			
			awb.setCiaFilialMercurio(this.validateCiaFilialTntMercurio(dto.getNrIdentificacaoEmit(), dto.getDhVigenciaEmit(), awb.getFilialByIdFilialOrigem().getIdFilial()));
			
			Awb awbJaCadastrado = this.findByCiaFilialMercurioAndDsSerieAndNrAwb(awb.getCiaFilialMercurio().getIdCiaFilialMercurio(), awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb());
			if (awbJaCadastrado != null) {
				throw new BusinessException("LMS-04521", new Object[] {nrChave, AwbUtils.getSgEmpresaAndNrAwbFormated(awbJaCadastrado)});
			}
			
			awb.setAeroportoByIdAeroportoOrigem(this.validaAeroporto(dto.getAeroportoOrigem()));
			awb.setAeroportoByIdAeroportoDestino(this.validaAeroporto(dto.getAeroportoDestino()));
			
			awb.setClienteByIdClienteExpedidor(this.validateCliente(dto.getNrIdentificacaoExp(), "Expedidor"));
			awb.setClienteByIdClienteDestinatario(this.validateCliente(dto.getIdentificacaoRecebedor(), "Destinatário"));
			awb.setClienteByIdClienteTomador(this.validateCliente(dto.getDsIdentificacaoTomador(), "Tomador"));			
			
			this.validateVinculoNotaFiscalCteAereo(dto.getNrChaveInfNfe());
			
			InscricaoEstadual ieExpedidor = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(awb.getClienteByIdClienteExpedidor().getIdCliente());
			InscricaoEstadual ieDestinatario = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(awb.getClienteByIdClienteDestinatario().getIdCliente());
			
			awb.setInscricaoEstadualExpedidor(ieExpedidor);
			awb.setInscricaoEstadualDestinatario(ieDestinatario);
			awb.setNrChave(nrChave);
			
			this.validateCteTntVinculadosCteCiaAerea(dto.getListNrChaveCteAnt());
			
			if (CollectionUtils.isEmpty(dto.getListNrChaveCteAnt())) {
				throw new BusinessException("LMS-04175");
			}
			
			return awb;
		} catch (BusinessException businessException) {			
			String message = configuracoesFacade.getMensagem(businessException.getMessageKey(), businessException.getMessageArguments());			
			boolean isNewLog = logCargaAwbService.isNewLogCargaAwb(message, dto.getNrChaveAwb());
			if(isNewLog){
				logCargaAwbService.generateLogCargaAwb(dto.getDsSerie(), dto.getNrAwb(), message, dto.getNrChaveAwb());
			}
			throw businessException;
		}
	}
		
	private void validateCteTntVinculadosCteCiaAerea(List<String> listNrChaveCteAnt) {
		if (CollectionUtils.isEmpty(listNrChaveCteAnt)) {
			return;
		}
		
		for (String chaveMDE : listNrChaveCteAnt) {
			if(StringUtils.isBlank(chaveMDE)){
				continue;
			}
			
			MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findByNrChave(chaveMDE);		
			
			if(mde == null) {
				throw new BusinessException("cteNaoCadastrado", new Object[]{chaveMDE});
			} else {
				DoctoServico doctoServico = mde.getDoctoServico();
				this.verificaCteNaoEmitido(chaveMDE, doctoServico);
				this.verificaRelacaoCteAwb(chaveMDE, doctoServico);
			}
		}
		
	}
	
	private void verificaRelacaoCteAwb(String chaveMDE, DoctoServico doctoServico) {
		Awb awb = this.findByDoctoServicoAndTpStatusAwb(doctoServico.getIdDoctoServico(), "E");
		if(awb != null){
			throw new BusinessException("LMS-04516", new Object[]{chaveMDE, AwbUtils.getSgEmpresaAndNrAwbFormated(awb)});
		}
	}
	
	private void verificaCteNaoEmitido(String chaveMDE, DoctoServico doctoServico) {
		if(!"E".equals(doctoServico.getTpSituacaoConhecimento().getValue())){
			throw new BusinessException("cteNaoSituacaoEmitido", new Object[]{chaveMDE});
		}
	}	
	
	private void validateVinculoNotaFiscalCteAereo(List<String> listNrChaves) {
		if(CollectionUtils.isNotEmpty(listNrChaves)) {
			for (String chave : listNrChaves) {
				List<NotaFiscalConhecimento> listNfc = notaFiscalConhecimentoService.findByNrChave(chave);
				
				if (CollectionUtils.isEmpty(listNfc)) {
					throw new BusinessException("notaFiscalNaoCadastrada", new Object[]{chave});
				} else if(listNfc.size() > 1){
					throw new BusinessException("notaFiscalDuplicada", new Object[]{chave});
				}
			} 
		}
	}
	
	private Cliente validateCliente(String nrIdentificacao, String dsMsgCliente) {		
		Cliente cliente = clienteService.findByNrIdentificacao(nrIdentificacao);		
		if (cliente == null) {
			throw new BusinessException("clienteNaoCadastrado", new Object[]{FormatUtils.formatCNPJ(nrIdentificacao), dsMsgCliente});
		}		
		return cliente;
	} 

	private Filial validateFilial(String nrIdentificacao, String dsMsgFilial) {
		
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("ID_EMPRESA_MERCURIO");
		Filial filial = null; 
		if (parametroGeral != null) {
			Long idEmpresa = Long.valueOf(parametroGeral.getDsConteudo());
			filial = filialService.findFilialRespByNrIdentificacaoAndIdEmpresa(nrIdentificacao, idEmpresa);
			
			if (filial == null) {				
				throw new BusinessException("filialNaoCadastrada", new Object[] {dsMsgFilial, FormatUtils.formatCNPJ(nrIdentificacao)});
			}
		}

		return filial;
	}
	
	private CiaFilialMercurio validateCiaFilialTntMercurio(String nrIdentificacaoEmit, String dhVigenciaEmit, Long idFilial) {
		
		String nrIdentificacaoEmitFormatado = FormatUtils.formatCNPJ(nrIdentificacaoEmit);
		
		Pessoa pessoa = pessoaService.findByNrIdentificacao(nrIdentificacaoEmit);
		if (pessoa == null) {
			throw new BusinessException("emitenteNaoCadastrado", new Object[] { nrIdentificacaoEmitFormatado });
		}

		YearMonthDay dthr = new YearMonthDay(dhVigenciaEmit);
		Long idCiaFilialMercurio = ciaFilialMercurioService.findByIdFilialAndNrIdentificacaoBetweenDtVigencia(idFilial, nrIdentificacaoEmit, dthr);
		if (idCiaFilialMercurio == null) {
			throw new BusinessException("filialCiaAereaSemVinculoTnt", new Object[] { nrIdentificacaoEmitFormatado });
		}
		
		CiaFilialMercurio cfm = new CiaFilialMercurio();
		cfm.setIdCiaFilialMercurio(idCiaFilialMercurio);
		return cfm;
	}
	
	private Aeroporto validaAeroporto(String sgAeroporto) {
		
		if (StringUtils.isNotBlank(sgAeroporto)) {
			Aeroporto aeroporto = aeroportoService.findBySgAeroportoAndTpSituacaoAtivo(sgAeroporto);			
			if(aeroporto == null) {
				throw new BusinessException("aeroportoNaoCadastradoOuInativo", new Object[]{sgAeroporto});
			}			
			return aeroporto;
		}
		return null;
	}
	
	/** FIM - VALIDAÇÃO DA IMPORTAÇÃO */
	
	/***************** FIM PROCESSO DE IMPORTAÇÃO AUTOMÁTICA DE  AWB *********************/
	
	public Empresa findResponsavelAwbEmpresaParceira(Long idAwb) {
		return getAwbDAO().findResponsavelAwbEmpresaParceira(idAwb);
	}
	
	private String setSgCiaAerea(CiaFilialMercurio ciaFilialMercurio) {
		return ciaFilialMercurio == null ? "" : (ciaFilialMercurio.getEmpresa() == null ? ""
				: (ciaFilialMercurio.getEmpresa().getSgEmpresa() == null ? "" : ciaFilialMercurio.getEmpresa().getSgEmpresa()));
	}

	public Awb findByCiaFilialMercurioAndDsSerieAndNrAwb(Long idCiaFilialMercurio, String dsSerie, Long nrAwb, Integer digito) {
		return this.getAwbDAO().findByCiaFilialMercurioAndDsSerieAndNrAwb(idCiaFilialMercurio, dsSerie, nrAwb, digito);
	}

	public List<Awb> findBySerieByNrAwbByDvAwbCiaAerea(String dsSerie,Long nrAwb, Integer dvAwb, Long idCiaAerea) {
		return this.getAwbDAO().findBySerieByNrAwbByDvAwbCiaAerea(dsSerie, nrAwb, dvAwb, idCiaAerea);
	}

	public Awb findAwbByDoctoServicoAndManifesto(Long idDoctoServico, Long idManifesto) {
		return this.getAwbDAO().findAwbByDoctoServicoAndManifesto(idDoctoServico, idManifesto);
	}

	public Awb findByDoctoServicoAndTpStatusAwb(Long idDoctoServico, String tpStatusAwb) {
		return this.getAwbDAO().findByDoctoServicoAndTpStatusAwb(idDoctoServico, tpStatusAwb);
	}	
	
	public java.util.List<Map<String, Object>> findDadosByChaveAwb(String chave) {
		return this.getAwbDAO().findDadosByChaveAwb(chave);
	}
	
	/**
	 * @param preAlertaService
	 */
	public void setPreAlertaService(PreAlertaService preAlertaService) {
		this.preAlertaService = preAlertaService;
	}

	/**
	 * @return Returns the ctoAwbService.
	 */
	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	/**
	 * @param ctoAwbService The ctoAwbService to set.
	 */
	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public boolean findCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(Awb awb) {
		return getAwbDAO().findCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(awb);
	}

	public Awb findByIdDoctoServicoAndFilialOrigem(Long idDoctoServico, Long idFilialOrigem) {
		return getAwbDAO().findByIdDoctoServicoAndFilialOrigem(idDoctoServico, idFilialOrigem);
	}

	public Awb findPreAwbByIdDoctoServicoAndFilialOrigem(Long idDoctoServico, Long idFilialOrigem) {
		return getAwbDAO().findPreAwbByIdDoctoServicoAndFilialOrigem(idDoctoServico, idFilialOrigem);
	}

	public TypedFlatMap findDadosColetaAwb(Long idAwb, Long idConhecimento) {
		return getAwbDAO().findDadosColetaAwb(idAwb, idConhecimento);
	}
	
	public TypedFlatMap findDadosColetaAwb(Long idAwb) {
		return findDadosColetaAwb(idAwb, null);
	}

	public String findAwbLogCargaAwb(String chaveAwb) {
		return getAwbDAO().findAwbLogCargaAwb(chaveAwb);
	}

	public void updateConferenciaAwb(Long idAwb) {
		getAwbDAO().updateConferenciaAwb(idAwb);
	}
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public List findMonitoramentoNetworkAereoCiaAerea(TypedFlatMap tfm){
		return getAwbDAO().findMonitoramentoNetworkAereoCiaAerea(tfm);
	}
	
	public List findMonitoramentoNetworkAereoAwb(TypedFlatMap criteria) {
		return getAwbDAO().findMonitoramentoNetworkAereoAwb(criteria);
	}
	

	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public LogCargaAwbService getLogCargaAwbService() {
		return logCargaAwbService;
	}

	public void setLogCargaAwbService(LogCargaAwbService logCargaAwbService) {
		this.logCargaAwbService = logCargaAwbService;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public CiaFilialMercurioService getCiaFilialMercurioService() {
		return ciaFilialMercurioService;
	}

	public void setCiaFilialMercurioService(
			CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}

	public AeroportoService getAeroportoService() {
		return aeroportoService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
		return monitoramentoDocEletronicoService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ProdutoEspecificoService getProdutoEspecificoService() {
		return produtoEspecificoService;
	}

	public void setProdutoEspecificoService(ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public Map<String, Object> findChaveJaUsadaEmAWB(String nrChave) {
		return getAwbDAO().findChaveJaUsadaEmAWB(nrChave);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedAwbsByIdConhecimento(Long idConhecimento) {
		ResultSetPage<Map<String, Object>> rs = getAwbDAO().findPaginatedAwbsByIdConhecimento(idConhecimento);
		List<Map<String, Object>> result = rs.getList();
		for (Map<String, Object> map : result) {
			if(ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(map.get("tpStatusAwb.value").toString())){
				map.put("nrAwbFormatado", map.get("ciaFilialMercurio.empresa.sgEmpresa") + " " + 
										  AwbUtils.getNrAwbFormated(map.get("dsSerieAwb").toString(), 
												  Long.valueOf(map.get("nrAwb").toString()), 
												  Integer.valueOf(map.get("dvAwb").toString())));
			}
			else if ((ConstantesExpedicao.TP_STATUS_PRE_AWB.equals(map.get("tpStatusAwb.value").toString()))){
				map.put("nrAwbFormatado", map.get("ciaFilialMercurio.empresa.sgEmpresa") + " " + map.get("idAwb").toString());
			}
			else{
				if(Long.valueOf(map.get("nrAwb").toString()) > 0){
					map.put("nrAwbFormatado", map.get("ciaFilialMercurio.empresa.sgEmpresa") + " " + 
							  AwbUtils.getNrAwbFormated(map.get("dsSerieAwb").toString(), 
									  Long.valueOf(map.get("nrAwb").toString()), 
									  Integer.valueOf(map.get("dvAwb").toString())));
				}
				else{
					map.put("nrAwbFormatado", map.get("ciaFilialMercurio.empresa.sgEmpresa") + " " + map.get("idAwb").toString());
				}
			}
			
			//AWB SUBSTITUIDO
			if(map.get("tpStatusAwbSubs.value") != null && ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(map.get("tpStatusAwbSubs.value").toString())){
				map.put("nrAwbFormatadoSubs", map.get("ciaFilialMercurioSubs.empresa.sgEmpresa") + " " + 
										  AwbUtils.getNrAwbFormated(map.get("dsSerieAwbSubs").toString(), 
												  Long.valueOf(map.get("nrAwbSubs").toString()), 
												  Integer.valueOf(map.get("dvAwbSubs").toString())));
			}
			else if ((map.get("tpStatusAwbSubs.value") != null && ConstantesExpedicao.TP_STATUS_PRE_AWB.equals(map.get("tpStatusAwbSubs.value").toString()))){
				map.put("nrAwbFormatadoSubs", map.get("ciaFilialMercurioSubs.empresa.sgEmpresa") + " " + map.get("idAwbSubs").toString());
			}
			else if(map.get("nrAwbSubs") != null){
				if(Long.valueOf(map.get("nrAwbSubs").toString()) > 0){
					map.put("nrAwbFormatadoSubs", map.get("ciaFilialMercurioSubs.empresa.sgEmpresa") + " " + 
							  AwbUtils.getNrAwbFormated(map.get("dsSerieAwbSubs").toString(), 
									  Long.valueOf(map.get("nrAwbSubs").toString()), 
									  Integer.valueOf(map.get("dvAwbSubs").toString())));
				}
				else{
					map.put("nrAwbFormatadoSubs", map.get("ciaFilialMercurioSubs.empresa.sgEmpresa") + " " + map.get("idAwbSubs").toString());
				}
			}
			map.put("tpMotivoCancelamento", map.get("tpMotivoCancelamento.description"));
			map.put("tpSituacao", map.get("tpStatusAwb.description"));
			
			setDhEmissao(map);
			
		}
		return rs;
	}
	
	private void setDhEmissao(Map<String, Object> map) {
		if (map.get("dhEmissao") == null) {
			map.put("dhEmissao", map.get("dhDigitacao"));
			map.remove("dhDigitacao");
		}
	}
	
	public Awb findAWBComplementadoByIdAwbComplementar(Long idAwbComplementar) {
		return getAwbDAO().findAWBComplementadoByIdAwbComplementar(idAwbComplementar);
	}

	public List<Map<String, Object>> findParcelasForReport(Long idAwb, int mod) {
		return getAwbDAO().findParcelasForReport(idAwb, mod);
	}

	public ParcelaAwbService getParcelaAwbService() {
		return parcelaAwbService;
	}

	public void setParcelaAwbService(ParcelaAwbService parcelaAwbService) {
		this.parcelaAwbService = parcelaAwbService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public AnexoAwbService getAnexoAwbService() {
		return anexoAwbService;
	}

	public void setAnexoAwbService(AnexoAwbService anexoAwbService) {
		this.anexoAwbService = anexoAwbService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	
	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}

	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}

	public CalculoFreteCiaAereaService getCalculoFreteCiaAereaService() {
		return calculoFreteCiaAereaService;
	}

	public void setCalculoFreteCiaAereaService(
			CalculoFreteCiaAereaService calculoFreteCiaAereaService) {
		this.calculoFreteCiaAereaService = calculoFreteCiaAereaService;
	}

	public void setLiberaAWBComplementarService(LiberaAWBComplementarService liberaAWBComplementarService) {
		this.liberaAWBComplementarService = liberaAWBComplementarService;
	}
}
