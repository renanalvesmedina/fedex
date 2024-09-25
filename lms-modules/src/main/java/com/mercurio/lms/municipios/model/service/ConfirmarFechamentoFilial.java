package com.mercurio.lms.municipios.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.dao.FilialDAO;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.workflow.model.Acao;

/**
 * Classe de serviço para CRUD:    
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.confirmarFechamentoFilialService"
 */
public class ConfirmarFechamentoFilial extends CrudService {
	
	private HistoricoFilialService historicoFilialService;
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public void setFilialDAO(FilialDAO filialDAO) {
		setDao( filialDAO );
	}
	public FilialDAO getFilialDAO() {
		return (FilialDAO) getDao();
	}
	
	public String executeWorkflow(List idFilial, List tpStituacao) {
		
        if (idFilial.size() != 1)
        	throw new IllegalArgumentException("Você deve informar apenas uma filial");
        Long idF = (Long)idFilial.get(0);
        String tpStituacaoS = ((String)tpStituacao.get(0));
		if (tpStituacaoS.equalsIgnoreCase("A")) {  
			HistoricoFilial historicoFilial = historicoFilialService.findUltimoHistoricoFilial(idF);
			if (historicoFilial.getDtPrevisaoOperacaoFinal() == null)
				throw new IllegalArgumentException("O Historico da filial não possui data Prevista de abertura, verificar se informou o id da filial correto");

			historicoFilial.setDtRealOperacaoFinal(historicoFilial.getDtPrevisaoOperacaoFinal());
			historicoFilialService.store(historicoFilial);
			
			Filial filial = filialService.findById(idF);
			
			Acao acao = filialService.getAcaoByFilial(idF,null,filial.getPendencia().getIdPendencia());
			StringBuffer obImplantacoes = new StringBuffer(JTFormatUtils.format(historicoFilial.getDtPrevisaoOperacaoInicial()));
			if (acao != null && acao.getObAcao() != null)
				obImplantacoes.append("\n").append(acao.getObAcao());
			if (filial.getObAprovacao() != null)
				obImplantacoes.insert(0,"\n").insert(0,filial.getObAprovacao());

			filial.setObAprovacao(obImplantacoes.toString());
			getFilialDAO().store(filial);

			return configuracoesFacade.getMensagem("fechamentoFilialAprovadoSucesso");
		}else if (tpStituacaoS.equalsIgnoreCase("R")) {
			HistoricoFilial historicoFilial = historicoFilialService.findUltimoHistoricoFilial(idF);
			
			YearMonthDay data = historicoFilial.getDtPrevisaoOperacaoFinal();
			Filial filial = filialService.findById(idF);
			Acao acao = filialService.getAcaoByFilial(idF,filial.getPendencia().getIdPendencia(),null);
			
			if (acao == null || data == null || acao.getDhLiberacao().toYearMonthDay().plusDays(3).compareTo(data) == 0)
				throw new BusinessException("LMS-29103");

			StringBuffer sbReprovacao = new StringBuffer();
			sbReprovacao.append(JTFormatUtils.format(acao.getDhLiberacao().toYearMonthDay().plusDays(3)));
			
			if (filial.getObAprovacao() != null)
				sbReprovacao.insert(0,"\n").insert(0,filial.getObAprovacao());
			
			if (acao != null && acao.getObAcao() != null)
				sbReprovacao.append("\n").append(acao.getObAcao());
			
			filial.setObAprovacao(sbReprovacao.toString());
			getFilialDAO().store(filial);
			return null;
		}else
			return null;
	}
	
	
	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
