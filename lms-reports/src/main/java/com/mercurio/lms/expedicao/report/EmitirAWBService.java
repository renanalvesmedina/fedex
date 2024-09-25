package com.mercurio.lms.expedicao.report;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.PreAlerta;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.expedicao.model.service.PreAlertaService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Emitir Conhecimento Aereo.
 * @author Andre Valadas.
 * @spring.bean id="lms.expedicao.emitirAWBService"
 */
public class EmitirAWBService {
	private ConfiguracoesFacade configuracoesFacade;
	private AwbService awbService;
	private EmitirDocumentoService emitirDocumentoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private GerarAWBService gerarAWBService;
	private PreAlertaService preAlertaService;

	/**
	 * Emissao de Conhecimento Aereo.
	 * @author Andre Valadas.
	 * @param idAwb
	 * @return
	 */
	public TypedFlatMap executeEmitirAWB(Long idAwb) {
		/** Valida parametros */
		if (idAwb == null || idAwb.longValue() < 1) {
			throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("awb")});
		}
		/** Busca Awb */
		Awb awb = awbService.findById(idAwb);

		validateAwb(awb);

		/** Conserva Status anterior, para garantir a geração do Pré-Alerta */
		String tpStatusAwb = awb.getTpStatusAwb().getValue();
		/** Atualiza Dados Emissao/Reemissao */
		updateDados(awb);

		/** Gera Formulario de Impressao */
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("awb", gerarAWBService.generateAWB(idAwb));

		/** Envia Pre-Alerta para Pre-AWBs */
		generatePreAlerta(awb, tpStatusAwb, toReturn);

		/** Devolve Status Atualizado do Awb */
		toReturn.put("tpStatusAwb", awb.getTpStatusAwb().getValue());
		return toReturn;
	}

	/**
	 * Valida Emissao do AWB.
	 * @author Andre Valadas.
	 * @param awb
	 */
	private void validateAwb(Awb awb) {
		if (!SessionUtils.getFilialSessao().getIdFilial().equals(awb.getFilialByIdFilialOrigem().getIdFilial())) {
			throw new BusinessException("LMS-04167");
		}
		if (ConstantesExpedicao.TP_STATUS_CANCELADO.equals(awb.getTpStatusAwb().getValue())) {
			throw new BusinessException("LMS-04172");
		}
	}

	/**
	 * Operacoes de Atualizacao do AWB.
	 * @author Andre Valadas.
	 * @param awb
	 */
	private void updateDados(Awb awb) {
		
		/** Data de Emissao eh sempre alterada */
		awb.setDhEmissao(JTDateTimeUtils.getDataHoraAtual().toDateTime(awb.getFilialByIdFilialOrigem().getDateTimeZone()));
		
		/** Emissao Pre-AWB */
		if (ConstantesExpedicao.TP_STATUS_PRE_AWB.equals(awb.getTpStatusAwb().getValue())) {
			
			emitirDocumentoService.generateProximoNumero(awb, ConstantesExpedicao.AIRWAY_BILL);
			awb.setTpStatusAwb(new DomainValue(ConstantesExpedicao.CD_EMISSAO));

			/** Atualiza AWB e Força Flush para inclusao dos eventos */
			awbService.store(awb);
			awbService.executeFlush();

			/** Gera Evento AWB Emitido */
			StringBuilder nrDocumento = new StringBuilder()
				.append(awb.getCiaFilialMercurio().getEmpresa().getSgEmpresa())
				.append(" ")
				.append(AwbUtils.formatNrAwb(awb.getNrAwb(), awb.getDvAwb()));
			incluirEventosRastreabilidadeInternacionalService.insereEventos(
				 ConstantesExpedicao.AIRWAY_BILL
				 ,nrDocumento.toString()
				 ,ConstantesSim.EVENTO_AWB_EMITIDO
				 ,awb.getFilialByIdFilialOrigem().getIdFilial()
				 ,JTDateTimeUtils.getDataHoraAtual().toDateTime(awb.getFilialByIdFilialOrigem().getDateTimeZone())
				 ,null,
				 awb.getCiaFilialMercurio().getIdCiaFilialMercurio());
		} else {
			/** Atualiza AWB no caso de Reemissao */
			awbService.store(awb);
		}
	}

	/**
	 * Gera Pre-Alerta para Pre-AWBs.
	 * @param awb
	 * @param tpStatusAwb
	 * @param toReturn
	 */
	private void generatePreAlerta(Awb awb, String tpStatusAwb, TypedFlatMap toReturn) {
		if (!ConstantesExpedicao.TP_STATUS_PRE_AWB.equals(tpStatusAwb)) {
			return;
		}
		PreAlerta preAlerta = new PreAlerta();
		preAlerta.setAwb(awb);
		preAlerta.setDsVoo(awb.getDsVooPrevisto());
		preAlerta.setBlVooConfirmado(Boolean.FALSE);
		preAlerta.setDhSaida(awb.getDhPrevistaSaida());
		preAlerta.setDhChegada(awb.getDhPrevistaChegada());
		TypedFlatMap data = preAlertaService.executeValidateInStore(preAlerta,null, null, null);
		
		preAlertaService.executeSendEmail(preAlerta, data);
		
		String mailException = data.getString("exception");
		if (StringUtils.isNotBlank(mailException)) {
			toReturn.put("exception", mailException);
		}
	}

	public void setPreAlertaService(PreAlertaService preAlertaService) {
		this.preAlertaService = preAlertaService;
	}
	public void setGerarAWBService(GerarAWBService gerarAWBService) {
		this.gerarAWBService = gerarAWBService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}