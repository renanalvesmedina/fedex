package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.FaturaRecibo;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.dao.ReciboDAO;
import com.mercurio.lms.contasreceber.model.param.ReciboParam;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.reciboService"
 */
public class ReciboService extends CrudService<Recibo, Long> {
	private DevedorDocServFatService devedorDocServFatService;
	private ConfiguracoesFacade configuracoesFacade;
	private FaturaService faturaService;
	private WorkflowPendenciaService workflowPendenciaService;
	private GerarFaturaReciboService gerarFaturaReciboService;
	private DoctoServicoService doctoServicoService;
	private AtualizarSituacaoFaturaService atualizarSituacaoFaturaService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;

	/**
	 * Recupera uma inst�ncia de <code>Recibo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Recibo findById(java.lang.Long id) {
		return (Recibo)super.findById(id);
	}

	protected void beforeRemoveById(Long id) {
		Recibo recibo = findById((Long)id);
		//Validar se pode modificar o Recibo
		validateRecibo(recibo);

		updateFatura((Long)id);
		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for(Long id : ids) {
			beforeRemoveById(id);
		}
		super.beforeRemoveByIds(ids);
	}

	private void updateFatura(Long idRecibo){
		List<Fatura> lstFatura = faturaService.findByRecibo(idRecibo);
		for (Fatura fatura : lstFatura) {
			atualizarSituacaoFaturaService.executeVoltarRecibo(fatura.getIdFatura());
		}
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Recibo bean) {
		Serializable retorno = super.store(bean);
		afterStore((Recibo)bean);
		getReciboDAO().store(bean);
		return retorno;
	}

	/**
	 * Store b�sico do Recibo
	 *
	 * @author Micka�l Jalbert
	 * @since 31/08/2006
	 *
	 * @param recibo
	 * @return
	 */
	public Recibo storeBasic(Recibo recibo) {
		getReciboDAO().store(recibo);
		return recibo;
	}

	@Override
	protected Recibo beforeInsert(Recibo bean) {
		bean.setTpSituacaoRecibo(new DomainValue("D"));
		bean.setNrRecibo(findNextNrRecibo(bean.getFilialByIdFilialEmissora().getIdFilial()));
		return super.beforeInsert(bean);
	}

	@Override
	protected Recibo beforeStore(Recibo bean) {
		bean.setVlDiferencaCambial(new BigDecimal(0));
		bean.setVlTotalRecibo(new BigDecimal(0));
		bean.setVlTotalDocumentos(new BigDecimal(0));
		bean.setVlTotalJuros(new BigDecimal(0));
		bean.setVlTotalDesconto(new BigDecimal(0));

		//Validar se pode modificar o Recibo
		validateRecibo(bean);

		return super.beforeStore(bean);
	}

	/**
	 * CUIDADO, essem�todo modifica a list de Fatura Recibo (lstFaturaRecibo)
	 * 
	 * @author Micka�l Jalbert
	 * @since 08/05/2006
	 * 
	 * @param Recibo recibo
	 * @param List lstFaturaRecibo
	 */
	private List<Long> beforeStore(Recibo recibo, List<FaturaRecibo> lstFaturaRecibo, List<FaturaRecibo> lstFaturaReciboDeleted) {
		beforeStore(recibo);

		List<FaturaRecibo> lstFaturaReciboFinal = new ArrayList<FaturaRecibo>();
		for (FaturaRecibo faturaRecibo : lstFaturaRecibo) {
			//Se � uma nova fatura, gerar ela com situa��o 'Em recibo'
			if (faturaRecibo.getFatura().getIdFatura() == null){
				DevedorDocServFat devedorDocServFat = ((ItemFatura)faturaRecibo.getFatura().getItemFaturas().get(0)).getDevedorDocServFat();
				Fatura fatura = gerarFaturaReciboService.storeFaturaWithDevedorDocServFat(new Fatura(), devedorDocServFat);
				faturaRecibo.setFatura(fatura);
			//Sen�o, atualizar a fatura a 'Em recibo'
			} else {
				faturaRecibo.getFatura().setTpSituacaoFatura(new DomainValue("RC"));
				faturaService.storeBasic(faturaRecibo.getFatura());
			}
			lstFaturaReciboFinal.add(faturaRecibo);
		}

		List<Long> lstFaturaDeleted = new ArrayList<Long>();
		//Monta a lista das faturas apagadas
		for (FaturaRecibo faturaRecibo : lstFaturaReciboDeleted) {
			lstFaturaDeleted.add(faturaRecibo.getFatura().getIdFatura());
		}

		lstFaturaRecibo = lstFaturaReciboFinal;

		return lstFaturaDeleted;
	}

	private Recibo afterStore(Recibo recibo) {
		Map<String, Object> mapSomatorio = findSomatorio(recibo.getIdRecibo());
		TypedFlatMap somatorios = new TypedFlatMap();

		somatorios.putAll(mapSomatorio);

		recibo.setVlTotalDesconto(somatorios.getBigDecimal("vlDesconto"));
		recibo.setVlTotalJuros(somatorios.getBigDecimal("vlJuroCalculado"));
		recibo.setVlTotalJuroRecebido(somatorios.getBigDecimal("vlJuroRecebido"));
		recibo.setVlTotalDocumentos(somatorios.getBigDecimal("vlTotal"));
		recibo.setVlTotalRecibo(somatorios.getBigDecimal("vlTotalRecibo"));

		return recibo;
	}

	private Recibo afterStore(Recibo recibo, List<FaturaRecibo> lstFaturaRecibo, List<Long> lstFaturaDeleted) {
		afterStore(recibo);

		//Setar a fatura no recibo
		for (FaturaRecibo faturaRecibo : lstFaturaRecibo) {
			faturaRecibo.getFatura().setRecibo(recibo);
			faturaService.storeBasic(faturaRecibo.getFatura());
		}

		atualizarSituacaoFaturaService.executeVoltarRecibo(lstFaturaDeleted);

		return recibo;
	}

	/**
	 * Gera um Recibo para a fatura informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/10/2006
	 * 
	 * @param Recibo recibo
	 * @param Fatura fatura
	 * @return Recibo
	 */
	public Recibo generateRecibo(Recibo recibo, Fatura fatura) {
		List<FaturaRecibo> lstFaturaRecibo = new ArrayList<FaturaRecibo>(1);

		if (recibo.getFilialByIdFilialEmissora() == null){
			recibo.setFilialByIdFilialEmissora(fatura.getFilialByIdFilial());
		}

		if (recibo.getTpSituacaoRecibo() == null){
			recibo.setTpSituacaoRecibo(new DomainValue("D"));
		}

		if (recibo.getDtEmissao() == null){
			recibo.setDtEmissao(JTDateTimeUtils.getDataAtual());
		}

		validateFatura(fatura, null);

		FaturaRecibo faturaRecibo = new FaturaRecibo();

		faturaRecibo.setFatura(fatura);
		faturaRecibo.setRecibo(recibo);
		faturaRecibo.setVlCobrado(fatura.getVlTotal());
		faturaRecibo.setVlJuroRecebido(BigDecimal.ZERO);

		lstFaturaRecibo.add(faturaRecibo);

		return store(recibo, lstFaturaRecibo, new ArrayList<FaturaRecibo>(1));
	}

	public Recibo store(Recibo recibo, List<FaturaRecibo> lstFaturaRecibo, List<FaturaRecibo> lstFaturaReciboDeleted){
		List<Long> lstFaturaDeleted = beforeStore(recibo, lstFaturaRecibo, lstFaturaReciboDeleted);

		this.getReciboDAO().store(recibo,lstFaturaRecibo,lstFaturaReciboDeleted);

		this.afterStore(recibo, lstFaturaRecibo, lstFaturaDeleted); 

		return recibo;
	}

	public Recibo store(Recibo recibo, ItemList faturaRecibo) {
		boolean rollbackMasterId = recibo.getIdRecibo() == null;
		List<FaturaRecibo> lstFaturaRecibo = faturaRecibo.getNewOrModifiedItems();
		List<FaturaRecibo> lstFaturaReciboDeleted = faturaRecibo.getRemovedItems();
		try {
			if (!faturaRecibo.hasItems()){
				throw new BusinessException("LMS-36130");
			} else if (faturaRecibo.size() > 9) {
				throw new BusinessException("LMS-36197");
			}
			store(recibo, lstFaturaRecibo, lstFaturaReciboDeleted);
		} catch (RuntimeException e) {
			this.rollbackMasterState(recibo, rollbackMasterId, e);
			faturaRecibo.rollbackItemsState();
			throw e;			
		}
		return recibo;
	}

	public Recibo cancelRecibo(Recibo recibo){
		//Validar se pode modificar o Recibo
		validateRecibo(recibo);

		recibo.setPendencia(generatePendencia(recibo));

		recibo.setTpSituacaoAprovacao(new DomainValue("E"));

		getReciboDAO().store(recibo);

		return recibo;
	}

	/**
	 * � chamado na aprova��o/reprova��o de um evento gerado no cancelamento de um recibo
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param List lstIds
	 * @param List lstSituacao
	 */
	public String executeWorkflow(List<Long> lstIds, List<String> lstSituacao) {
		Long idRecibo = lstIds.get(0);
		String tpSituacaoAprovacao = lstSituacao.get(0); 

		Recibo recibo = findById(idRecibo);

		recibo.setTpSituacaoAprovacao(new DomainValue(tpSituacaoAprovacao));

		if (tpSituacaoAprovacao.equals("A")){
			recibo.setTpSituacaoRecibo(new DomainValue("C"));

			List<Long> lstFatura = faturaService.findIdFaturaByRecibo(recibo.getIdRecibo());

			atualizarSituacaoFaturaService.executeVoltarRecibo(lstFatura);
		}

		getReciboDAO().store(recibo);

		return null;
	}

	/**
	 * Valida se a fatura informada est� em acordo com as regras de inser��o de um recibo.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/05/2006
	 * 
	 * @param Fatura fatura
	 */
	public void validateFatura(Fatura fatura, Long idCliente) {
		String tpSituacao = fatura.getTpSituacaoFatura().getValue();

		if (validateRelacaoPagamentoParcial(fatura.getIdFatura())) {
			throw new BusinessException("LMS-36268");
		}

		//Se a situa��o da fatura for diferente de 'Digitado', 'Emitido' e 'Em boleto' lan�ar uma exception
		if (!tpSituacao.equals("DI") && !tpSituacao.equals("EM") && !tpSituacao.equals("BL")){
			throw new BusinessException("LMS-36108");
		}

		//O cliente da fatura n�o pode ser diferente do cliente do primeiro item do recibo
		if (idCliente != null && !fatura.getCliente().getIdCliente().equals(idCliente)){
			throw new BusinessException("LMS-36227");
		}
	}

	public boolean validateRelacaoPagamentoParcial(Long idFatura){		
		return relacaoPagtoParcialService.validateRelacaoPagamentoParcial(idFatura);
	}
	
	public void validateDescontoDocumento(String tpDocumento, Long idDocumento){
		// Caso seja fatura.
		if (tpDocumento.equals("FAT")){ 			
			Fatura fatura = faturaService.findById(idDocumento);
			DomainValue tpSituacaoAprovacao = fatura.getTpSituacaoAprovacao();
			if (tpSituacaoAprovacao != null 
					&& !"A".equals(tpSituacaoAprovacao.getValue())) {
				throw new BusinessException("LMS-36095");
			}
		// Caso seja devedor.
		} else {
			Desconto desconto = devedorDocServFatService.findById(idDocumento).getDesconto();
			if (desconto != null){
				DomainValue tpSituacaAprovacao = desconto.getTpSituacaoAprovacao();
				if (tpSituacaAprovacao != null 
						&& !"A".equals(tpSituacaAprovacao.getValue())) {
					throw new BusinessException("LMS-36010");
				}
			}
		}
	}
	
	/**
	 * Valida se a fatura informada est� em acordo com as regras de inser��o de um recibo.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/05/2006
	 * 
	 * @param Fatura fatura
	 */
	public void validateDevedorDocServFat(DevedorDocServFat devedorDocServFat, Long idCliente) {
		devedorDocServFatService.validateDisponibilidadeDevedorDocServFat(devedorDocServFat);
		doctoServicoService.validatePermissaoDocumentoUsuario(devedorDocServFat.getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());

		//O cliente da fatura n�o pode ser diferente do cliente do primeiro item do recibo
		if (idCliente != null && !devedorDocServFat.getCliente().getIdCliente().equals(idCliente)){
			throw new BusinessException("LMS-36227");
		}
	}

	/**
	 * Valida se o Recibo informado pode ser modificado.
	 * 
	 * @author Micka�l Jalbert
	 * @since 08/05/2006
	 * 
	 * @param Recibo recibo
	 */
	public void validateRecibo(Recibo recibo) {
		//Se o Recibo est� cancelado, lan�ar uma exception
		if (recibo.getTpSituacaoRecibo().getValue().equals("C")){
			throw new BusinessException("LMS-36110");
		}

		//Se a situa��o do recibo for 'Inutilizado' ou 'Recebido', lan�ar uma exception
		if (recibo.getTpSituacaoRecibo().getValue().equals("I") || recibo.getTpSituacaoRecibo().getValue().equals("R")){
			throw new BusinessException("LMS-36194");
		}

		//Se a filial do Recibo for diferente da filial da sess�o, lan�ar uma excpetion
		if (!recibo.getFilialByIdFilialEmissora().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())){
			throw new BusinessException("LMS-00063");
		}

		//Se o Recibo est� 'Em aprova��o', lan�ar uma exception
		if (recibo.getTpSituacaoAprovacao() != null && recibo.getTpSituacaoAprovacao().getValue().equals("E")){
			throw new BusinessException("LMS-36231");
		}
	}

	/**
	 * Retorna true se o Recibo informado est� em redeco ativo
	 * 
	 * @author Micka�l Jalbert
	 * @since 12/07/2006
	 * 
	 * @param Long idRecibo
	 * 
	 * @return Boolean
	 */
	public Boolean validateReciboEmRedeco(Long idRecibo) {
		return getReciboDAO().validateReciboEmRedeco(idRecibo);
	}

	/**
	 * Gera uma pendencia de cancelamento (workflow) do Recibo informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 08/05/2006
	 * 
	 * @param Recibo recibo
	 * @return Pendencia
	 */
	private Pendencia generatePendencia(Recibo recibo){
		return workflowPendenciaService.generatePendencia(recibo.getFilialByIdFilialEmissora().getIdFilial(), 
				ConstantesWorkflow.NR3614_CANC_RECB, recibo.getIdRecibo(), 
				recibo.getFilialByIdFilialEmissora().getSgFilial() + " - " + recibo.getNrRecibo(), 
				JTDateTimeUtils.getDataHoraAtual());
	}

	/**
	 * Buscar o pr�ximo n�mero de recibo da filial informado.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/05/2006
	 * 
	 * @param Long idFilial
	 * @return Long
	 */
	public Long findNextNrRecibo(Long idFilial){
		return configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_RECIBO", true);
	}

	/**
	 * Cancela todos os recibos de descontos do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/07/2006
	 * 
	 * @param Long idRedeco
	 */
	public void cancelFaturaOfReciboRedeco(Long idRedeco){
		List<Fatura> lstFatura = findFaturaOfReciboByIdRedeco(idRedeco);
		for (Fatura fatura : lstFatura) {
			
		if(SessionUtils.isIntegrationRunning() && (fatura.getNrFatura().compareTo(Long.valueOf(10000000)) > 0)){
			fatura.setTpSituacaoFatura(new DomainValue("CA"));
		}else{
			fatura.setTpSituacaoFatura(new DomainValue("EM"));
		}
			getReciboDAO().store(fatura);
		}
	}

	/**
	 * Retorna a lista de recibos do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/07/2006
	 * 
	 * @param Long idRedeco
	 * @return List
	 */
	public List<Fatura> findFaturaOfReciboByIdRedeco(Long idRedeco){
		return getReciboDAO().findFaturaOfReciboByIdRedeco(idRedeco);
	}

	public ResultSetPage findPaginated(ReciboParam reciboParam, FindDefinition findDef){
		return getReciboDAO().findPaginated(reciboParam, findDef);
	}

	public Integer getRowCount(ReciboParam reciboParam){
		return getReciboDAO().getRowCount(reciboParam);
	}

	/**
	 * M�todo respons�vel por buscar recibos ade acordo com o n�mero de recibo e a sigla da filial passados no filtro
	 * 
	 * @param nrRecibo
	 * @param sgFilial
	 * @return List 
	 */
	public List<Recibo> findReciboByFilial(Long nrRecibo, Long idFilial){
		return getReciboDAO().findReciboByFilial(nrRecibo, idFilial);
	}

	/**
	 * Retorna o recibo da fatura informada
	 * 
	 * @param Long idFatura
	 * @return Recibo
	 * */
	public Recibo findByFatura(Long idFatura){
		List<Recibo> lstRecibo = this.getReciboDAO().findByFatura(idFatura);
		if (lstRecibo.size() == 1) {
			return lstRecibo.get(0);
		} else {
			return null;
		}
	} 

	public List<Recibo> findByFaturas(List<Long> idFatura){
		return getReciboDAO().findByFaturas(idFatura);
	} 

	
	/**
	 * Retorna o recibo do n�mero e a filial informado. 
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/07/2006
	 * 
	 * @param Long nrRecibo
	 * @param Long idFilial
	 * 
	 * @return List
	 */
	public List<Recibo> findByNrReciboByFilial(Long nrRecibo, Long idFilial){
		return getReciboDAO().findByNrReciboByFilial(nrRecibo, idFilial);
	}

	public List<FaturaRecibo> findFaturaRecibo(Long masterId){
		return this.getReciboDAO().findFaturaRecibo(masterId);
	}

	public Integer getRowCountFaturaRecibo(Long masterId){
		return this.getReciboDAO().getRowCountFaturaRecibo(masterId);
	}

	/**
	 * Retorna a soma dos juros recebidos do recibo informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param idRecibo
	 * 
	 * @return BigDecimal
	 */
	public Map<String, Object> findSomatorio(Long idRecibo){
		return getReciboDAO().findSomatorio(idRecibo);
	}

	/**
	 * Valida a permiss�o de acesso do usu�rio ao recibo informado
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 21/09/2006
	 *
	 * @param idRecibo Identificador do Recibo
	 * @param idFilial Identificador da filial
	 */
	public Boolean validatePermissaoReciboUsuario(Long idRecibo, Long idFilial){
		return getReciboDAO().validatePermissaoReciboUsuario(idRecibo, idFilial);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setReciboDAO(ReciboDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ReciboDAO getReciboDAO() {
		return (ReciboDAO) getDao();
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setGerarFaturaReciboService(GerarFaturaReciboService gerarFaturaReciboService) {
		this.gerarFaturaReciboService = gerarFaturaReciboService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setAtualizarSituacaoFaturaService(AtualizarSituacaoFaturaService atualizarSituacaoFaturaService) {
		this.atualizarSituacaoFaturaService = atualizarSituacaoFaturaService;
	}
	
	public void setRelacaoPagtoParcialService(RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}

}