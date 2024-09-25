package com.mercurio.lms.contasreceber.model.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.dao.RedecoDAO;
import com.mercurio.lms.contasreceber.model.param.RedecoSomatorioParam;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servi�o para CRUD:   
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.redecoService"
 */
public class RedecoService extends CrudService<Redeco, Long> {

	private ReciboDescontoService reciboDescontoService;
	
	private FaturaService faturaService;
	
	private ReciboService reciboService;
	
	private DomainValueService domainValueService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
	private BoletoService boletoService;
	
	private WorkflowPendenciaService workflowPendenciaService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private GerarRedecoService gerarRedecoService;
	
	private GerarReciboDescontoService gerarReciboDescontoService;
	
	private DoctoServicoService doctoServicoService;
	
	private MoedaService moedaService;
	
	private PendenciaService pendenciaService;
	
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	
	/**
	 * Find by id da tela Redeco
	 * 
	 * @author Micka�l Jalbert
	 * @since 04/07/2006
	 * 
	 * @param Long idRedeco
	 * @return Redeco
	 */
	public Redeco findByIdTela(Long idRedeco){
		return getRedecoDAO().findByIdTela(idRedeco);
	}
	
	/**
	 * Retorna o redeco com todas as pendencias 'fetchado' se existe.
	 * 
	 * @author Micka�l Jalbert
	 * @since 13/07/2006
	 * 
	 * @param Long idRedeco
	 * 
	 * @return Redeco
	 */
	public Redeco findByIdWithPendencia(Long idRedeco){
		return getRedecoDAO().findByIdWithPendencia(idRedeco);
	}
	
	/**
	 * Valida se o redeco pode ser modificado. � chamado em todos os pontos de modifica��o
	 * de redeco e de item redeco.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/07/2006
	 * 
	 * @param Long idRedeco
	 */
	public void validateRedeco(Long idRedeco){
		Redeco redeco = findById(idRedeco);
		validateRedeco(redeco);
	}	
	
	/**
	 * Valida se o redeco pode ser modificado. � chamado em todos os pontos de modifica��o
	 * de redeco e de item redeco.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/07/2006
	 * 
	 * @param Redeco redeco
	 */
	public void validateRedeco(Redeco redeco){
		
		//Se o redeco est� 'Em aprova��o' no workflow, lan�ar uma exception
		if (redeco.getTpSituacaoWorkflow() != null && redeco.getTpSituacaoWorkflow().getValue().equals("E") && redeco.getIdRedeco() != null){
			throw new BusinessException("LMS-36155");
		}

		validateRedecoSimple(redeco);
	}
	
	/**
	 * Valida se o redeco pode ser modificado ou cancelado. � chamado em todos os pontos de modifica��o
	 * de redeco e de item redeco.
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/07/2006
	 * 
	 * @param Redeco redeco
	 */
	public void validateRedecoSimple(Redeco redeco){
		
		/**
		 * Esta restri��o de filial foi removida para acomodar as aplica��es
		 * da Integra��o Corporativo->LMS.
		 * 
		 * Referente ao caso de uso [RE-00].
		 * 
		 * @since 05/03/2010
		 * @author Christian S. Perone
		 */
		if(!SessionUtils.isIntegrationRunning())		
			if (redeco.getFilial() != null){
		//Se a filial do redeco � diferente da filial da sess�o, lan�ar uma exception
			if (!redeco.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) && !SessionUtils.getFilialSessao().getSgFilial().equals("MTZ")){
				throw new BusinessException("LMS-36198");
			}
		}

		//Se n�o � um novo redeco
		if (redeco.getIdRedeco() != null) {
			//Se o redeco est� 'Liquidado', 'Em cobran�a juridica' ou 'Cancelado', 
			//lan�ar uma exception
			if (redeco.getTpSituacaoRedeco().getValue().equals("LI") ||
				redeco.getTpSituacaoRedeco().getValue().equals("CJ") ||
				(!SessionUtils.isIntegrationRunning() && redeco.getTpSituacaoRedeco().getValue().equals("CA"))){ // se estiver rodando pela integracao nao valida se est� cancelado.
				throw new BusinessException("LMS-36153");
			}		
			
			HistoricoFilial historicoFilial = SessionUtils.getUltimoHistoricoFilialSessao();
			
			//Se o tipo de finalidade � egual a: 'Ocorrencia de banco', 'Desconto do franqueado'
			//ou 'Cobran�a juridica' e que a filial da sess�o n�o � matriz, lan�ar uma exception
			if ((!redeco.getTpFinalidade().getValue().equals("CC") &&
				 !redeco.getTpFinalidade().getValue().equals("CF")) && 
				 !historicoFilial.getTpFilial().getValue().equals("MA")
				 ){
				throw new BusinessException("LMS-36152");
			}
		}
	}	
	
	/**
	 * Valida se o item redeco est� v�lido para ser inserido num redeco
	 * 
	 * @author Micka�l Jalbert
	 * @param string 
	 * @since 10/07/2006
	 * 
	 * @param ItemRedeco itemRedeco
	 * @param YearMonthDay dtEmissao
	 * @param String tpAbrangencia
	 */
	public void validateItemRedeco(ItemRedeco itemRedeco, BigDecimal vlRecebido, YearMonthDay dtEmissao, String tpAbrangencia, boolean isFinalidadeEspecifica, String tpFinalidade){
		BigDecimal vlRecebidoCalculado = null; 
		
		Boolean isFilialMatriz = SessionUtils.isFilialSessaoMatriz();
		
		//Validar s� se � um novo item
		if (itemRedeco.getIdItemRedeco() == null){
			//Se � uma fatura ou NDI
			if (itemRedeco.getFatura() != null && itemRedeco.getFatura().getIdFatura() != null){
				Fatura fatura = itemRedeco.getFatura();

				//Se a fatura est� em aprova��o
				if (fatura.getTpSituacaoAprovacao() != null && (fatura.getTpSituacaoAprovacao().getValue().equals("R") || fatura.getTpSituacaoAprovacao().getValue().equals("E"))){
					throw new BusinessException("LMS-36095");
				}
				
				if( !isFilialMatriz.booleanValue() ){
					// Se a filial de cobran�a da fatura for diferente da filial da sess�o, d� erro
					if (!fatura.getFilialByIdFilialCobradora().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())){
						throw new BusinessException("LMS-36004");
					}
				}				
				
				//Se � fatura
				if (fatura.getTpFatura().getValue().equals("R")){
					validateFatura(fatura, tpFinalidade);
				//Se � NDI
				} else {
					validateNotaDebito(fatura);
				}				
			//Se � um doctoServico
			} else if (itemRedeco.getFatura() != null){
				DevedorDocServFat devedorDocServFat = ((ItemFatura)itemRedeco.getFatura().getItemFaturas().get(0)).getDevedorDocServFat();

				if( !isFilialMatriz.booleanValue() ){
					doctoServicoService.validatePermissaoDocumentoUsuario(devedorDocServFat.getDoctoServico().getIdDoctoServico(), devedorDocServFat.getFilial().getIdFilial());
				}
				
				devedorDocServFatService.validateDevedorDocServFatSemFilial(devedorDocServFat, null);
				
			//Se � um recibo
			} else if (itemRedeco.getRecibo() != null){
				Recibo recibo = itemRedeco.getRecibo();

				if( !isFilialMatriz.booleanValue() ){
					reciboService.validatePermissaoReciboUsuario(recibo.getIdRecibo(),recibo.getFilialByIdFilialEmissora().getIdFilial());
				}
				
				validateRecibo(recibo);
			}
		}
		
		//Buscar o valor a ser recebido
		if (itemRedeco.getFatura() != null && itemRedeco.getFatura().getIdFatura() != null){
			Fatura fatura = itemRedeco.getFatura();

			BigDecimal totalVlPagamento = findVlPagtoFatura(fatura.getIdFatura(), null);
			BigDecimal vlSaldo = fatura.getVlTotal().subtract(fatura.getVlDesconto()).subtract(totalVlPagamento);

			vlRecebidoCalculado = vlSaldo.add(itemRedeco.getVlJuros()).subtract(itemRedeco.getVlTarifa());
			
		//Se � um doctoServico
		} else if (itemRedeco.getFatura() != null){
			DevedorDocServFat devedorDocServFat = ((ItemFatura)itemRedeco.getFatura().getItemFaturas().get(0)).getDevedorDocServFat();
			BigDecimal vlDesconto = new BigDecimal(0);
			
			if (devedorDocServFat.getDesconto() != null && devedorDocServFat.getDesconto().getTpSituacaoAprovacao().getValue().equals("A")){
				vlDesconto = devedorDocServFat.getDesconto().getVlDesconto();
			}
			vlRecebidoCalculado = devedorDocServFat.getVlDevido().add(itemRedeco.getVlJuros()).subtract(vlDesconto).subtract(itemRedeco.getVlTarifa());
			
		//Se � um recibo
		} else if (itemRedeco.getRecibo() != null){
			Recibo recibo = itemRedeco.getRecibo();
			vlRecebidoCalculado = recibo.getVlTotalRecibo().add(itemRedeco.getVlJuros()).subtract(recibo.getVlTotalDesconto()).subtract(itemRedeco.getVlTarifa());
		}
		
		
		//Valida se o valor recebido � o mesmo que o valor da d�vida
		if( !isFinalidadeEspecifica ){
			if (vlRecebidoCalculado.compareTo(vlRecebido) != 0){
				throw new BusinessException("LMS-36159");
			}
		}
	}
	
	/**
	 * Valida se a fatura informada � habilitada a ser inserida no redeco
	 * 
	 * @author Micka�l Jalbert
	 * @param tpFinalidade 
	 * @since 11/07/2006
	 * 
	 * @param Fatura fatura
	 */
	public void validateFatura(Fatura fatura, String tpFinalidade){
		String tpSituacaoFatura = fatura.getTpSituacaoFatura().getValue();
		
		//Se a fatura est� diferente de 'Digitado', 'Emitido', 'Em recibo' e 'Em boleto',
		//lan�ar uma exception
		if (!tpSituacaoFatura.equals("DI") && 
			!tpSituacaoFatura.equals("EM") && 
			!tpSituacaoFatura.equals("BL") && 
			!tpSituacaoFatura.equals("RC") && 
			!(tpSituacaoFatura.equals("LI") && 
				("PR".equals(tpFinalidade)
				|| "DR".equals(tpFinalidade)
				|| "JU".equals(tpFinalidade)
				|| "OR".equals(tpFinalidade))
			)
			){
			throw new BusinessException("LMS-36156");
		//Se a fatura est� 'Em boleto'
		} else if (tpSituacaoFatura.equals("BL")){
			Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
			
			if (boleto != null){
				String tpSituacaoBoleto = boleto.getTpSituacaoBoleto().getValue();
				
				//Se a situa��o do boleto � diferente de 'Digitado', 'Emitido', 'Em banco sem protesto' 
				//e 'Em banco com protesto' 
				if (!tpSituacaoBoleto.equals("EM") && 
					!tpSituacaoBoleto.equals("BN") && 
					!tpSituacaoBoleto.equals("BP") &&
					!(tpSituacaoBoleto.equals("LI") && 
						("PR".equals(tpFinalidade)
						|| "DR".equals(tpFinalidade)
						|| "JU".equals(tpFinalidade)
						|| "OR".equals(tpFinalidade))
					)
					){
					throw new BusinessException("LMS-36156");
				}
			}
		}	

		// Caso a situacao de aprova��o da fatura n�o seja nula
    	// e seja diferente de A, lan�a a exception.
    	DomainValue tpSituacaoAprovacao = fatura
    			.getTpSituacaoAprovacao();
		if (tpSituacaoAprovacao != null
				&& !"A".equals(tpSituacaoAprovacao.getValue())) {
    		throw new BusinessException("LMS-36095");
    	}
		
	}
	
	/**
	 * Carrega o primeiro documento de servi�o da fatura.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param fatura
	 * @return
	 *
	 * DoctoServico
	 *
	 */
	private DoctoServico getFirstDoctoServicoFromFatura (Fatura fatura) {
		fatura = faturaService.findById(fatura.getIdFatura());
		DoctoServico ds = null;
		List itemFaturas = fatura.getItemFaturas();
		if (itemFaturas != null) {	  
			ds = ((ItemFatura)itemFaturas.get(0))    
					.getDevedorDocServFat().getDoctoServico();		
		}
		return ds;
	}
	
	/**
	 * Valida se a nota de d�bito internacional informada � habilitada a ser inserida no redeco
	 * 
	 * @author Micka�l Jalbert
	 * @since 11/07/2006
	 * 
	 * @param Fatura fatura
	 */	
	private void validateNotaDebito(Fatura fatura){
		String tpSituacaoFatura = fatura.getTpSituacaoFatura().getValue();
		
		//Se a NDI � 'Cancelada', lan�ar uma exception
		if (tpSituacaoFatura.equals("CA")){
			throw new BusinessException("LMS-36157");
		}		
	}
	
	/**
	 * Valida se o recibo informado � habilitado a ser inserido no redeco
	 * 
	 * @author Micka�l Jalbert
	 * @since 11/07/2006
	 * 
	 * @param Recibo recibo
	 */	
	private void validateRecibo(Recibo recibo){
		
		//Se o recibo est� 'Cancelado'
		if (recibo.getTpSituacaoRecibo().getValue().equals("C")){
			throw new BusinessException("LMS-36158");
		}
		
		//Valida se o recibo est� num redeco ativo, se est�, lan�ar uma exception
		if (reciboService.validateReciboEmRedeco(recibo.getIdRecibo()).equals(Boolean.TRUE)){
			throw new BusinessException("LMS-36167");
		}
	}
	
	
	/**
	 * Recupera uma inst�ncia de <code>Redeco</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    @Override
	public Redeco findById(java.lang.Long id) {
        return (Redeco)super.findById(id);
    }
    
	/**
	 * Retorna o redeco disconectado do hibernate
	 * 
	 * @author Micka�l Jalbert
	 * @since 16/01/2007
	 * 
	 * @param idRedeco
	 * 
	 * @return redeco
	 */
	public Redeco findByIdDisconnected(Long idRedeco){
        return getRedecoDAO().findByIdDisconnected(idRedeco);
    }    
    
    /**
     * Retorna o redeco pela Fatura      
     * @author Jos� Rodrigo Moraes
     * @since 11/04/2006
     * 
     * @param Long idFatura
     * @return Redeco
     */
    public Redeco findByFatura(Long idFatura){
    	List<Redeco> lstRedeco = this.getRedecoDAO().findByFatura(idFatura);
    	if (lstRedeco.size() == 1) {
    		return lstRedeco.get(0);
    	} else {
    		return null;
    	}
    } 

    public List findItemRedeco(Long masterId){
    	return getRedecoDAO().findItemRedeco(masterId);
    }

    public Integer getRowCountItemRedeco(Long masterId){
		return this.getRedecoDAO().getRowCountItemRedeco(masterId);
    }  

    /**
     * Soma os valores de desconto, juros etc e retorna um objeto RedecoSomatorioParam populado 
     * dos valores do redeco informado.
     * 
     * @author Micka�l Jalbert
     * @since 04/07/2006
     * 
     * @param Long idRedeco
     * 
     * @return RedecoSomatorioParam
     */
    public RedecoSomatorioParam findSomatorio(Long idRedeco){
    	return getRedecoDAO().findSomatorio(idRedeco);
    }
    
    /**
	 * Retorna o novo n�mero de Redeco a partir da filial informada.
	 * 
	 * @author Micka�l Jalbert
	 * @since 11/07/2006
	 * 
	 * @param Long idFilial
	 * 
	 * @return Long
	 */
    public Long findNextNrRedeco(Long idFilial){
    	return configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_REDECO", false);
    }
    
    @Override
	protected void beforeRemoveById(Long id) {
    	validateRedeco((Long)id);
    	
    	faturaService.cancelFaturasByIdRedeco((Long)id);
    	
    	super.beforeRemoveById(id);
    }

    @Override
    protected void beforeRemoveByIds(List<Long> ids) {
    	for(Long id : ids) {
			beforeRemoveById(id);
		}
    	super.beforeRemoveByIds(ids);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * @param id indica a entidade que dever� ser removida.
	 */
    @Override
	public void removeById(Long id) {    	
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
    	for(Long id : ids) {
			validateRedeco(id);
		}
        super.removeByIds(ids);
    }

    @Override
    protected Redeco beforeInsert(Redeco bean) {
    	if(!SessionUtils.isIntegrationRunning()){
    	bean.setNrRedeco(findNextNrRedeco(bean.getFilial().getIdFilial()));
    	}
    	if (bean.getTpSituacaoRedeco() == null){
    		bean.setTpSituacaoRedeco(new DomainValue("DI"));
    	}
    	validateRedeco(bean);
    	return super.beforeInsert(bean);
    }

    @Override
    protected Redeco beforeUpdate(Redeco bean) {
    	validateRedeco(bean);

    	return super.beforeUpdate(bean);
    }

    @Override
    protected Redeco beforeStore(Redeco bean) {
    	bean.setDhTransmissao(null);
    	bean.setVlDiferencaCambialCotacao(new BigDecimal(0));
    	if (bean.getDtRecebimento() != null && bean.getDtRecebimento().isAfter(bean.getDtEmissao())){
    		throw new BusinessException("LMS-36168");
    	}

    	//A data de liquida��o n�o pode ser antes da data de recebimento    	
    	if (bean.getDtLiquidacao() != null && bean.getDtRecebimento() != null && bean.getDtLiquidacao().isBefore(bean.getDtRecebimento())){
    		throw new BusinessException("LMS-36169");
    	}    	

    	if (bean.getIdRedeco() != null){
    		Redeco redecoAnterior = findByIdDisconnected(bean.getIdRedeco());    		
    		RedecoSomatorioParam redecoSomatorioParam = findSomatorio(bean.getIdRedeco());

    		bean.setTpFinalidadeAnterior(redecoAnterior.getTpFinalidade().getValue());
    		bean.setVlTotalDescontos(redecoSomatorioParam.getVlTotalDesconto());
    		bean.setVlTotalJuroRecebido(redecoSomatorioParam.getVlTotalJuros());
    		bean.setVlTotalRecebido(redecoSomatorioParam.getVlTotalPago());
    		
    		bean.setTpRecebimento(redecoAnterior.getTpRecebimento());
    	}

    	//Se a "Finalidade" for "Cobran�a Jur�dica" e a "Situa��o" diferente de �Liquidado� ou �Em cobran�a jur�dica�    	
    	if ((bean.getTpSituacaoRedeco() == null ||(bean.getTpSituacaoRedeco() != null || !bean.getTpSituacaoRedeco().getValue().equals("LI") && !bean.getTpSituacaoRedeco().getValue().equals("CJ"))) && bean.getTpFinalidade().getValue().equals("CJ")){
    		bean.setTpSituacaoRedeco(domainValueService.findDomainValueByValue("DM_STATUS_REDECO", "CC"));
    	}

    	return super.beforeStore(bean);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
    public Serializable store(Redeco bean) {
        return super.store(bean);
    }
    
    public Serializable storeBasic(Redeco bean) {
        getRedecoDAO().store(bean);
        return bean;
    }    

    public Redeco store(Redeco redeco, ItemList lstItemRedeco, ItemListConfig config) {
    	boolean rollbackMasterId = redeco.getIdRedeco() == null;

    	try {

        	if (!lstItemRedeco.hasItems()){
        		throw new BusinessException("LMS-36154");
        	}

    		gerarRedecoService.store(redeco, lstItemRedeco, config);
		} catch (RuntimeException e) {
			this.rollbackMasterState(redeco, rollbackMasterId, e);
			lstItemRedeco.rollbackItemsState();
            throw e;			
		}
		return redeco;
    }    

    public void storeItemRedeco(List<ItemRedeco> lstItemRedecoNew, List<ItemRedeco> lstItemRedecoDeleted){
    	getRedecoDAO().storeItemRedeco(lstItemRedecoNew, lstItemRedecoDeleted);
    }

    public Redeco afterStore(Redeco redeco, ItemList items, ItemListConfig config, Boolean blCancelarReciboDesconto){
    	RedecoSomatorioParam redecoSomatorioParam = findSomatorio(redeco.getIdRedeco());

    	BigDecimal vlRecebido = redecoSomatorioParam.getVlTotalPago()
    		.add(redecoSomatorioParam.getVlTotalJuros()).subtract(redecoSomatorioParam.getVlTotalTarifas())
    		.subtract(redecoSomatorioParam.getVlTotalDesconto());
    	
    	redeco.setVlRecebido(vlRecebido);
    	
		//Se a finalidade ou os valores do redeco mudaram, regerar os workflow
    	if (!redeco.getTpFinalidade().getValue().equals(redeco.getTpFinalidadeAnterior()) ||
    		redeco.getVlTotalDescontos().compareTo(redecoSomatorioParam.getVlTotalDesconto()) < 0 ||
    		redeco.getVlTotalJuroRecebido().compareTo(redecoSomatorioParam.getVlTotalJuros()) < 0 ||
    		redeco.getVlTotalRecebido().compareTo(redecoSomatorioParam.getVlTotalPago()) < 0 ){
    		redeco.setVlTotalRecebido(redecoSomatorioParam.getVlTotalPago());
    		redeco = generateWorkflow(redeco, items, config);
    	}
    	
    	redeco.setVlDiferencaCambialCotacao(redecoSomatorioParam.getVlDiferencaCambialCotacao());    
    	
    	gerarReciboDescontoService.executeGerarReciboDesconto(redeco.getIdRedeco(), blCancelarReciboDesconto);
    	
    	getRedecoDAO().store(redeco);
    	
    	return redeco;
    }

	/**
	 * @author Micka�l Jalbert
	 * @since 16/01/2007
	 * @param redeco
	 * @param items
	 * @param config
	 * @return
	 */
	private Redeco generateWorkflow(Redeco redeco, ItemList items, ItemListConfig config) {
		String tpFinalidade = redeco.getTpFinalidade().getValue();
    	String tpRecebimento = redeco.getTpRecebimento().getValue();
    	
    	//Se o tipo de finalidade for diferente de 'Empresa de cobran�a', 'Ocorr�ncia de banco',
    	//'Lucros e perdas' e 'Desconto de franqueado'
    	if (!tpFinalidade.equals("EC") && !tpFinalidade.equals("OB") && !tpFinalidade.equals("LP") && !tpFinalidade.equals("DF")){
    		generateWorkflowDesconto(redeco, items,config);
    	}
    	
    	//Se o tipo de finalidade for 'Lucros e perdas'
    	if (tpFinalidade.equals("LP")){
    		generateWorkflowLucrosPerdas(redeco, items, config);
    	}
    	
    	//Se o tipo de recebimento for 'Mercadoria' ou 'Duplicata'
    	if (tpRecebimento.equals("ME") || tpRecebimento.equals("DU")){
    		redeco = generateWorkflowRecebimento(redeco);
    	}
		return redeco;
	}
    
    /**
     * Gera um workflow de desconto no redeco informado se existe descontos no filhos
     * 
     * @author Micka�l Jalbert
     * @since 11/07/2006
     * 
     * @param Redeco redeco
     * @param List items
     * 
     * @return Redeco
     */
    private Redeco generateWorkflowDesconto(Redeco redeco, ItemList items, ItemListConfig config){
		BigDecimal vlJuroCalculado = new BigDecimal(0);
		BigDecimal vlJuroRecebido = new BigDecimal(0);
		
		//Calcular o valor total de juros, s� se aplica nas faturas
		for (Iterator<ItemRedeco> iter = items.iterator(redeco.getIdRedeco(),config); iter.hasNext();) {
			ItemRedeco itemRedeco = iter.next();
			if (itemRedeco.getFatura() != null){
				vlJuroCalculado = vlJuroCalculado.add(itemRedeco.getFatura().getVlJuroCalculado());
				vlJuroRecebido = vlJuroRecebido.add(itemRedeco.getVlJuros());
			}
		}
		redeco.setPendenciaDesconto(null);
		return redeco;
    }
    
    /**
     * Gera um workflow de 'lucros e perdas' no redeco informado
     * 
     * @author Micka�l Jalbert
     * @since 11/07/2006
     * 
     * @param Redeco redeco
     * @param List lstItemRedecoNew
     * 
     * @return Redeco
     */
    private Redeco generateWorkflowLucrosPerdas(Redeco redeco, ItemList items, ItemListConfig config){
		//Gerar uma pendencia de lucro e perda
    	Pendencia pendencia = generateWorkflow(redeco, ConstantesWorkflow.NR3619_RED_LCR_PRD, redeco.getFilial().getIdFilial(), mountObservacaoWorkflow(redeco));
		
		if (pendencia != null){
			redeco.setTpSituacaoWorkflow(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", "E"));
		}
    	
		redeco.setPendenciaLucrosPerdas(pendencia);
		
		return redeco;
    }

	/**
	 * @author Micka�l Jalbert
	 * @since 16/01/2007
	 * @param redeco
	 * @return
	 */
	private String mountObservacaoWorkflow(Redeco redeco) {
		StringBuffer observacao = mountObservacaoWorkflowDefault(redeco);
    	observacao.append(configuracoesFacade.getMensagem("observacao"));
    	observacao.append(": ");
    	observacao.append(redeco.getObRedeco());
    	
		return observacao.toString();
	}
	
	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/08/2007
	 *
	 * @param redeco
	 * @return
	 *
	 */
	private StringBuffer mountObservacaoWorkflowDefault(Redeco redeco) {
		StringBuffer observacao = new StringBuffer(); 
    	observacao.append(configuracoesFacade.getMensagem("redeco"));    	
    	observacao.append(": ");
    	observacao.append(redeco.getFilial().getSgFilial());
    	observacao.append(" ");
    	observacao.append(FormatUtils.fillNumberWithZero(redeco.getNrRedeco().toString(), 10));    	
    	observacao.append(" ");
    	observacao.append(configuracoesFacade.getMensagem("valorRedeco"));
    	observacao.append(": ");
    	observacao.append(moedaService.findById(redeco.getMoeda().getIdMoeda()).getSiglaSimbolo());
    	observacao.append(" ");
    	observacao.append(FormatUtils.formatDecimal("#,##0.00", redeco.getVlTotalRecebido()));
    	observacao.append(" ");
    	observacao.append(configuracoesFacade.getMensagem("tipoRecebimento"));
    	observacao.append(": ");
    	observacao.append(domainValueService.findDomainValueByValue("DM_TIPO_RECEBIMENTO", redeco.getTpRecebimento().getValue()).getDescription());
    	observacao.append(" ");
		return observacao;
	}
    
    /**
     * Monta a observa��o a ser usada na gera��o do workflow de Lucros e perdas.
     * Observa��o igual a SG_FILIAL + NR_FATURA + ' - ' + NR_IDENTIFICACAO + NM_CLIENTE
     *
     * @author Jos� Rodrigo Moraes
     * @since 25/09/2006
     *
     * @param fatura Fatura
     * @return observa��o
     */
    private String mountObservacaoWorkflowVisualizacao(Fatura fatura) {
    	
    	String nrIdentificacao = null; 
    	String nome = null;
    	String sgFilial = null;
    	String nrFatura = null;
    	
    	nrIdentificacao = FormatUtils.formatIdentificacao(
    			fatura.getCliente().getPessoa().getTpIdentificacao(),
    			fatura.getCliente().getPessoa().getNrIdentificacao());
    	
    	nome = fatura.getCliente().getPessoa().getNmPessoa();
    	
    	sgFilial = fatura.getFilialByIdFilial().getSgFilial();
    	
    	nrFatura = FormatUtils.formataNrDocumento(fatura.getNrFatura().toString(), "FAT");
    	
		return sgFilial + " " + nrFatura + ", " + nrIdentificacao + " - " + nome;
	}

	/**
     * Insere o id da filial na lista informada se n�o est�
     * 
     * @author Micka�l Jalbert
     * @since 13/07/2006
     * 
     * @param List lstIdFilialCobranca
     * @param Long idFilialCobrancaNew
     * 
     * @return List
     */
    private void validateNewFilial(List<Long> lstIdFilialCobranca, Long idFilialCobrancaNew) {
		boolean blExist = false;

		//Verificar se a filial j� foi adicionada
		for(Long idFilialCobranca : lstIdFilialCobranca) {
			if (idFilialCobranca.equals(idFilialCobrancaNew)){
				blExist = true;
			}
		}

		//Se n�o foi adicionada antes, adicinar na lista de filial a gerar workflow
		if (blExist == false) {
			lstIdFilialCobranca.add(idFilialCobrancaNew);
		}
    }
    
    /**
     * Gera um workflow de 'recebimento' no redeco informado
     * 
     * @author Micka�l Jalbert
     * @since 11/07/2006
     * 
     * @param Redeco redeco
     * 
     * @return Redeco
     */
    private Redeco generateWorkflowRecebimento(Redeco redeco){
		//Gerar uma pendencia de recebimento de mercadoria
    	Pendencia pendencia = generateWorkflow(redeco, ConstantesWorkflow.NR3621_RED_MERC, redeco.getFilial().getIdFilial(), mountObservacaoWorkflow(redeco));
		
		if (pendencia != null){
			redeco.setTpSituacaoWorkflow(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", "E"));
		}
    	
		redeco.setPendenciaRecebimento(pendencia);		
		
		return redeco;
    } 
    
    
    /**
     * Gera um workflow padr�o a partir do redeco informado.
     * 
     * @author Micka�l Jalbert
     * @since 11/07/2006
     * 
     * @param Redeco redeco
     * @param Short cdWorkflow
     * 
     * @return Pendencia
     */
    private Pendencia generateWorkflow(Redeco redeco, Short cdWorkflow, Long idFilial, String observacao){
    	return workflowPendenciaService.generatePendencia(idFilial, 
    			                                          cdWorkflow, 
    			                                          redeco.getIdRedeco(), 
    			                                          (observacao == null ? redeco.getObRedeco() : observacao), 
    			                                          JTDateTimeUtils.getDataHoraAtual());
    }
    

    
    /**
     * @see executeRetransmitirRedeco(Redeco)
     */
    public void executeRetransmitirRedeco(Long idRedeco){
    	Redeco redeco = findById(idRedeco);
    	
    	executeRetransmitirRedeco(redeco);    	
    }    
    
    /**
     * Faz a retransmiss�o do Redeco (zera a data tranmiss�o)
     * 
     * @author Micka�l Jalbert
     * @since 06/07/2006
     * 
     * @param Redeco redeco
     */
    public void executeRetransmitirRedeco(Redeco redeco){
      	// Foi feito um evict do objeto para que o Hibernate dispare um update mesmo
		// que o campo DhTransmissao nao tenha altera��o de valores.
		// Isso � necess�rio para que seja disparada uma trigger no banco de dados.
    	getRedecoDAO().evict(redeco);
    	redeco.setDhTransmissao(null);
    	store(redeco);
    }
    
    /**
     * @see cancelRedeco(Redeco)
     */
    public Redeco cancelRedeco(Long idRedeco){
    	Redeco redeco = findById(idRedeco);
    	
    	return cancelRedeco(redeco);
    }       
    
    public Redeco cancelRedeco(Redeco redeco){
    	//Cancelar os recibos
    	reciboDescontoService.cancelRecibosDescontoRedeco(redeco.getIdRedeco());
    	
    	//Voltar a situa��o anterior da fatura
    	faturaService.cancelFaturasByIdRedeco(redeco.getIdRedeco());
    	
    	//Por cada fatura que est� em boleto, voltar a situa��o anterior do boleto
    	List<Long> lstIdFatura = faturaService.findIdFaturaEmBoletoByIdRedeco(redeco.getIdRedeco());
    	for(Long idFatura : lstIdFatura) {
    		if(SessionUtils.isIntegrationRunning())
    			boletoService.updateSituacaoBoleto(idFatura, "EM");
    		else
			boletoService.updateSituacaoBoletoAnterior(idFatura);
		}
    	
    	//Voltar para emitido as faturas dos recibo
    	reciboService.cancelFaturaOfReciboRedeco(redeco.getIdRedeco());
    	
    	cancelWorkflow(redeco);
    	
    	validateRedecoSimple(redeco);
    	redeco.setTpSituacaoRedeco(domainValueService.findDomainValueByValue("DM_STATUS_REDECO", "CA"));
    	
    	/*Altera a situacao da digita��o do REDECo para concluida*/
    	redeco.setBlDigitacaoConcluida(new DomainValue("S"));
    	
    	getRedecoDAO().store(redeco);
    	
    	return redeco;
    }
    
    private void cancelWorkflow(Redeco redeco){
    	//Se tem pendencia de desconto e que a situa��o est� 
    	if (redeco.getPendenciaDesconto() != null && redeco.getPendenciaDesconto().getTpSituacaoPendencia().getValue().equals("E")){
    		cancelWorkflowSingle(redeco, redeco.getPendenciaDesconto().getIdPendencia());
    	}
    	
    	if (redeco.getPendenciaLucrosPerdas() != null && redeco.getPendenciaLucrosPerdas().getTpSituacaoPendencia().getValue().equals("E")){
    		cancelWorkflowSingle(redeco, redeco.getPendenciaLucrosPerdas().getIdPendencia());
    	}
    	
    	if (redeco.getPendenciaRecebimento() != null && redeco.getPendenciaRecebimento().getTpSituacaoPendencia().getValue().equals("E")){
    		cancelWorkflowSingle(redeco, redeco.getPendenciaRecebimento().getIdPendencia());
    	}
    }

	/**
	 * Cancela a pendencia do redeco informado.
	 * 
	 * @author Micka�l Jalbert
	 * @since 02/10/2006
	 * 
	 * @param Redeco redeco
	 * @param Long idPendencia
	 */
	private void cancelWorkflowSingle(Redeco redeco, Long idPendencia) {
		workflowPendenciaService.cancelPendencia(idPendencia); 
		redeco.setTpSituacaoWorkflow(domainValueService.findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW", "C"));
	}
    
    public String executeWorkflow(List<Long> lstIdProcesso, List<String> lstTpSituacaoWorkflow){
    	Long idRedeco = lstIdProcesso.get(0);
    	String tpSituacaoWorkflow = lstTpSituacaoWorkflow.get(0);
    	Redeco redeco = findByIdWithPendencia(idRedeco);

    	//Se a pendencia foi aprovada
    	if (tpSituacaoWorkflow.equals("A")){
    		Boolean blAprovado = Boolean.TRUE;
			//Se a pendencia n�o � aprovada, n�o trocar a situa��o de workflow do redeco
    		if (redeco.getPendenciaDesconto() != null){
    			if (!redeco.getPendenciaDesconto().getTpSituacaoPendencia().getValue().equals("A")){
    				blAprovado = Boolean.FALSE;
    			}
    		}
    		if (redeco.getPendenciaLucrosPerdas() != null){
    			if (!redeco.getPendenciaLucrosPerdas().getTpSituacaoPendencia().getValue().equals("A")){
    				blAprovado = Boolean.FALSE;
    			}
    		}
    		if (redeco.getPendenciaRecebimento() != null){
    			if (!redeco.getPendenciaRecebimento().getTpSituacaoPendencia().getValue().equals("A")){
    				blAprovado = Boolean.FALSE;
    			}
    		}

    		if (blAprovado.equals(Boolean.TRUE)){
    			redeco.setTpSituacaoWorkflow(new DomainValue("A"));
    			getRedecoDAO().store(redeco);
    		}

    		// Somente gerar workflow de visualiza��o para redeco de lucros e perdas quando o mesmo for aprovado
    		if ( "LP".equals(redeco.getTpFinalidade().getValue()) ) {
    			generateWorkflowVisualizacao(redeco);
    		}

    	//Se a pendencia foi reprovada
    	} else if (tpSituacaoWorkflow.equals("R")){
			//Se a pendencia � 'Em aprova��o', cancelar a pendencia
    		if (redeco.getPendenciaDesconto() != null){
    			if (redeco.getPendenciaDesconto().getTpSituacaoPendencia().getValue().equals("E")){
    				workflowPendenciaService.cancelPendencia(redeco.getPendenciaDesconto().getIdPendencia());
    			}
    		}
    		if (redeco.getPendenciaLucrosPerdas() != null){
    			if (redeco.getPendenciaLucrosPerdas().getTpSituacaoPendencia().getValue().equals("E")){
    				workflowPendenciaService.cancelPendencia(redeco.getPendenciaLucrosPerdas().getIdPendencia());
    			}
    		}
    		if (redeco.getPendenciaRecebimento() != null){
    			if (redeco.getPendenciaRecebimento().getTpSituacaoPendencia().getValue().equals("E")){
    				workflowPendenciaService.cancelPendencia(redeco.getPendenciaRecebimento().getIdPendencia());
    			}
    		}

    		redeco.setTpSituacaoWorkflow(new DomainValue("R"));	
    		redeco = cancelRedeco(redeco);
    	}

    	getRedecoDAO().flush();
    	getRedecoDAO().evict(redeco);
    	return null;
    }

	/**
	 * Gera o workflow de visualiza��o para cada filial envolvida nos documentos do redeco.
	 * 
	 * @author Micka�l Jalbert
	 * @since 15/01/2007
	 * 
	 * @param redeco
	 */
	private void generateWorkflowVisualizacao(Redeco redeco) {
		//Gerar um workflow para cada filial dos filhos do redeco
		List<Long> lstIdFilialCobranca = new ArrayList<Long>();		
		String observacao = null;

		List<ItemRedeco> itensRedeco = findItemRedeco(redeco.getIdRedeco());
		for(ItemRedeco itemRedeco : itensRedeco) {
			//Adiciona a filial do cliente da fatura
			if (itemRedeco.getFatura() != null){
				validateNewFilial(lstIdFilialCobranca, itemRedeco.getFatura().getCliente().getFilialByIdFilialCobranca().getIdFilial());
				observacao = mountObservacaoWorkflowVisualizacao(itemRedeco.getFatura());
			} else {
				//Adiciona as filiais dos clientes das faturas do recibo
				List<Fatura> lstFatura = faturaService.findByRecibo(itemRedeco.getRecibo().getIdRecibo());
				for(Fatura fatura : lstFatura) {
					validateNewFilial(lstIdFilialCobranca, fatura.getCliente().getFilialByIdFilialCobranca().getIdFilial());
					observacao = mountObservacaoWorkflowVisualizacao(itemRedeco.getFatura());
				}
			}
		}

		//Para cada filial, gerar um workflow de visualiza��o de processo
		for(Long idFilialCobranca : lstIdFilialCobranca) {
			generateWorkflow(redeco, ConstantesWorkflow.NR3620_RED_LCR_PRD_CLT, idFilialCobranca, observacao);
		}
	}

	/**
	 * @param itemRedeco
	 */
	public Long getMoeda(ItemRedeco itemRedeco) {
		Long idMoeda = null;
		if (itemRedeco.getFatura() != null){
			if (itemRedeco.getFatura().getIdFatura() != null){
				idMoeda = itemRedeco.getFatura().getMoeda().getIdMoeda();
			} else {
				idMoeda = ((ItemFatura)itemRedeco.getFatura().getItemFaturas().get(0)).getDevedorDocServFat().getDoctoServico().getMoeda().getIdMoeda();
			}
		} else {
			idMoeda = itemRedeco.getRecibo().getCotacaoMoeda().getMoedaPais().getMoeda().getIdMoeda();
		}
		
		return idMoeda;
	}     
	
	/**
	 * @param itemRedeco
	 */
	public Moeda getMoedaByItem(ItemRedeco itemRedeco) {
		Moeda moeda = null;
		if (itemRedeco.getFatura() != null){
			if (itemRedeco.getFatura().getIdFatura() != null){
				moeda = itemRedeco.getFatura().getMoeda();
			} else {
				moeda = ((ItemFatura)itemRedeco.getFatura().getItemFaturas().get(0)).getDevedorDocServFat().getDoctoServico().getMoeda();
			}
		} else {
			moeda = itemRedeco.getRecibo().getCotacaoMoeda().getMoedaPais().getMoeda();
		}
		
		return moeda;
	} 

	public ResultSetPage findPaginatedRedeco(TypedFlatMap map, Integer currentPage, Integer pageSize) {
		return getRedecoDAO().findPaginatedRedeco(map, currentPage, pageSize);
	}
	
	public File executeExportacaoCsv(TypedFlatMap parameters, File generateOutputDir) {
		List<Map<String, Object>> lista = getRedecoDAO().findForCsv(parameters);

		List<Map<String, Object>> novaLista = convertLista(lista);

		return FileUtils.generateReportFile(novaLista, generateOutputDir, REPORT_NAME);
	}

	/**
	 * Calcula saldo das faturas do redeco.
	 */
	public BigDecimal findSaldoFaturasRedeco(Long idRedeco) {
		Map<String, Object> somatoriosRedeco = this.findSomatoriosRedeco(idRedeco);

		BigDecimal vlTotalFat = (BigDecimal) somatoriosRedeco.get("vl_total_fat");
		BigDecimal vlTotalDesc = (BigDecimal) somatoriosRedeco.get("vl_total_desc");
		BigDecimal vlTotalRecebParcial = (BigDecimal) somatoriosRedeco.get("vl_total_receb_parcial");
		
		return vlTotalFat.subtract(vlTotalDesc).subtract(vlTotalRecebParcial);
	}
	
	
	private List<Map<String, Object>> convertLista(List<Map<String, Object>> lista) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> m : lista) {
			list.add(convertMap(m));
		}

		return list;
	}

	private Map<String, Object> convertMap(Map<String, Object> input) {
		Map<String, Object> output = new HashMap<String, Object>();

		output.put("Filial", input.get("Filial"));
		output.put("Número", input.get("Numero"));
		output.put("Emissão", convertFromDate((Date) input.get("Emissao")));
		output.put("Recebimento", convertFromDate((Date) input.get("Recebimento")));
		output.put("Liquidação", convertFromDate((Date) input.get("Liquidacao")));
		output.put("Situação", ((DomainValue) input.get("Situacao")).getDescription());
		output.put("Finalidade", ((DomainValue) input.get("Finalidade")).getDescription());
		output.put("Cobrador", input.get("Cobrador"));
		output.put("Digitação concluída", ((DomainValue) input.get("Digitacao concluida")).getDescription());
		output.put("Qtd de documentos", input.get("Qtd de documentos"));
		output.put("Valor documentos", convertFromBigDecimal((BigDecimal) input.get("Valor documentos")));
		output.put("Total recebido", convertFromBigDecimal((BigDecimal) input.get("Total recebido")));
		output.put("Composição pagamentos", input.get("Composicao pagamentos"));

		return output;
	}

	private String convertFromDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return (date == null) ? "" : sdf.format(date);
	}

	private String convertFromBigDecimal(BigDecimal input) {
		return (input == null) ? "" : NumberFormat
				.getCurrencyInstance(new Locale("pt", "BR")).format(input)
				.substring(3);
	}

	private static final String REPORT_NAME = "INFO_REDECO";

	public List<Map<String, Object>> findForCsv(TypedFlatMap map) {
		return getRedecoDAO().findForCsv(map);
	}
	
	/**
     * Carrega o redeco de acordo com o nrRedeco e o idFilial
     *
     * @author Hector Julian Esnaola Junior
     * @since 05/10/2007
     *
     * @param idFilial
     * @param nrRedeco
     * @return
     *
     */
	public Redeco findRedecoByIdFilialAndNrRedeco(Long idFilial, Long nrRedeco){
		return getRedecoDAO().findRedecoByIdFilialAndNrRedeco(idFilial, nrRedeco);
	}
	/**
     * Retorna um map de dados por fatura que serve no relat�rio 'EmitirDocumentosServicoPendenteCliente'.
     * 
     * @author Micka�l Jalbert
     * @since 28/08/2006
     * 
     * @param Long idFatura
     *  
     * @return Map
     * */
    public Map<String, Object> findDadosEmitirDocumentosServicoPendenteCliente(Long idFatura){
    	return getRedecoDAO().findDadosEmitirDocumentosServicoPendenteCliente(idFatura);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * @param Inst�ncia do DAO.
     */
    public void setRedecoDAO(RedecoDAO dao) {
        setDao( dao );
    }
   
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * @return Inst�ncia do DAO.
     */
    private RedecoDAO getRedecoDAO() {
        return (RedecoDAO) getDao();
    }

	public List<Redeco> findRedeco(TypedFlatMap criteria) {
    	return getRedecoDAO().findRedeco(criteria);
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria){
		return getRedecoDAO().findPaginated(criteria);
	}		

	public Integer getRowCount(TypedFlatMap criteria){
		return getRedecoDAO().getRowCount(criteria);
	}

	public void setReciboDescontoService(ReciboDescontoService reciboDescontoService) {
		this.reciboDescontoService = reciboDescontoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setReciboService(ReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setGerarRedecoService(GerarRedecoService gerarRedecoService) {
		this.gerarRedecoService = gerarRedecoService;
	}

	public void setGerarReciboDescontoService(GerarReciboDescontoService gerarReciboDescontoService) {
		this.gerarReciboDescontoService = gerarReciboDescontoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public void setRelacaoPagtoParcialService(RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}

	public RelacaoPagtoParcialService getRelacaoPagtoParcialService() {
		return relacaoPagtoParcialService;
	}

	public Map<String, Object> findSomatoriosRedeco(Long idRedeco) {
		return getRedecoDAO().findSomatoriosRedeco(idRedeco);
	}

	public BigDecimal findVlPagtoFatura(Long idFatura, Long idRedeco) {
		return getRedecoDAO().findVlPagtoFatura(idFatura, idRedeco);
	}
	
	public List<TypedFlatMap> findRecebimentoRedeco(Long idFatura) {
		List<DomainValue> tpStatus = domainValueService.findDomainValues("DM_STATUS_REDECO");
		List<DomainValue> tpFinalidade = domainValueService.findDomainValues("DM_FINALIDADE_REDECO");

		List<TypedFlatMap> recebimentoRedeco = getRedecoDAO().findRecebimentoRedeco(idFatura);
		
		for (TypedFlatMap recebimento : recebimentoRedeco) {
			String tpSituacaoRedeco = recebimento.getString("tpSituacaoRedeco");
			String tpFinalidadeRedeco = recebimento.getString("tpFinalidade");
	
			String novaSituacao = findSituacaoRedeco(tpSituacaoRedeco, tpStatus);
			String novaFinalidade = findSituacaoRedeco(tpFinalidadeRedeco, tpFinalidade);
			
			recebimento.put("tpSituacaoRedeco", novaSituacao);
			recebimento.put("tpFinalidade", novaFinalidade);
		}
		
		return recebimentoRedeco;
	}

	
	
	private String findSituacaoRedeco(String tpSituacaoRedeco, List<DomainValue> tpStatus) {
		for (DomainValue domainValue : tpStatus) {
			if (domainValue.getValue().equals(tpSituacaoRedeco)) {
				return domainValue.getDescription().toString();
			}
		}
		throw new IllegalArgumentException("Couldn't find domain value for given tpStatusRedeco: " + tpStatus);
	}

	public void updateLancamentoFranquiaBaixaRedeco(Long idRedeco, Long idFilial, Long nrRedeco, YearMonthDay dtLiquidacao){
		getRedecoDAO().updateLancamentoFranquiaBaixaRedeco(idRedeco, idFilial, nrRedeco, dtLiquidacao);
	}
	
	public void updateLancamentoBaixaParcialRedeco(Long idRedeco, Long idFilial, Long nrRedeco, YearMonthDay dtRecebimento){
		DateTime dateTimeAtCurrentTime = dtRecebimento.toDateTimeAtMidnight();
		Date date = dateTimeAtCurrentTime.toDate();
		
		getRedecoDAO().updateLancamentoBaixaParcialRedeco(idRedeco, idFilial, nrRedeco, new java.sql.Date(date.getTime()));
	}
	
	//LMSA-2772
	public Boolean findValidaFiliaisFatura(Long idRedeco) {
		Integer linhas = getRedecoDAO().findNroDoctosServicoFaturaRedeco(idRedeco);
		if (linhas > 1) {
			return false;

		}
		return true;

	}
	
	/**
	 * Busca o tipo de documento de servico para cada fatura de um redeco.
	 */
	public List<Map<String, Object>> findTpDoctoServicoFaturaRedeco(Long idRedeco) {
		return getRedecoDAO().findTpDoctoServicoFaturaRedeco(idRedeco);
	}

}