package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;



/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaPreFaturaService"
 */
public class GerarFaturaPreFaturaService extends GerarFaturaService {
	
	protected void setValorDefaultSpecific(Fatura fatura){
    	fatura.setDtEmissao(JTDateTimeUtils.getDataAtual());
    	fatura.setBlGerarBoleto(Boolean.FALSE);
    	fatura.setBlGerarEdi(Boolean.FALSE);		
		fatura.setTpOrigem(new DomainValue("W"));
		fatura.setTpFatura(new DomainValue("R"));
    	fatura.setTpSituacaoFatura(new DomainValue("DI"));		
	}
	
	@Override
	protected Fatura beforeStore(Fatura fatura) {
		/** Caso a tpSituacaoFatura seja Emitida('EM') a pre-fatura não poderá ser alterada,
		 *  é lançada uma BusinessException */
		if(fatura.getIdFatura() != null && fatura.getTpSituacaoFatura().getValue().equals("EM")){
			throw new BusinessException("LMS-36202");
		}
		return super.beforeStore(fatura);
	}
	

	@Override
	protected Fatura afterStore(Fatura fatura, List lstItemFatura, boolean blNovaFatura) {
		//Se nunca foi gerado pendencia para essa pre-fatura, quer dizer que é uma nova pre-fatura
		fatura.setPendencia(generatePendenciaPreFatura(fatura));

		fatura = calculateSomatorioFatura(fatura);

		generateBoleto(fatura, blNovaFatura, lstItemFatura);

		validateTipoFrete(fatura, lstItemFatura);

		fatura = calculatVlIva(fatura);
		
		faturaService.storeBasic(fatura);
		
		devedorDocServFatService.executeUpdateSituacaoByIdFatura(fatura.getIdFatura(), "F");

		return fatura;
	}
	
	/**
	 * Gera uma pendencia de aprovação de pre-fatura
	 */
	protected Pendencia generatePendenciaPreFatura(Fatura fatura) {
		if (fatura.getTpSituacaoAprovacao() != null && fatura.getTpSituacaoAprovacao().getValue().equals("E")){
			if (fatura.getPendencia() != null){
				workflowPendenciaService.cancelPendencia(fatura.getPendencia().getIdPendencia());
			}
		}
		
		fatura.setTpSituacaoAprovacao(new DomainValue("E"));
		//Gera uma nova pendencia
		return workflowPendenciaService.generatePendencia(
				fatura.getFilialByIdFilial().getIdFilial(), 
				ConstantesWorkflow.NR3611_INCL_PRE_FAT, 
				fatura.getIdFatura(), 
				configuracoesFacade.getMensagem(
						"inclusaoPreFatura", 
						new Object[]{fatura.getNrFatura().toString()}), 
				JTDateTimeUtils.getDataHoraAtual());
	}	
	
	
	/**
	 * É chamado na aprovação/reprovação de um evento gerado na inclusão de uma pré-fatura
	 * 
	 * @author Mickaël Jalbert
	 * @since 11/10/2006
	 * 
	 * @param List lstIds
	 * @param List lstSituacao
	 */
	public String executeWorkflow(List lstIds, List lstSituacao) {
		Long idFatura = (Long)lstIds.get(0);
		String tpSituacaoAprovacao = (String)lstSituacao.get(0); 
		
		Fatura fatura = faturaService.findByIdFatura(idFatura);
		
		fatura.setTpSituacaoAprovacao(new DomainValue(tpSituacaoAprovacao));
		
		faturaService.storeBasic(fatura);
		
		if("R".equals(tpSituacaoAprovacao)){
		faturaService.cancelItemsFatura(idFatura);
		}
		
		return null;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
