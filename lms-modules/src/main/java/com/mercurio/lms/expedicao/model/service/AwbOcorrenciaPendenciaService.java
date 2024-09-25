package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaLocalizacao;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaPendencia;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.TrackingAwb;
import com.mercurio.lms.expedicao.model.dao.AwbOcorrenciaPendenciaDAO;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD: 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.awbService"
 */

@Assynchronous(name = "awbOcorrenciaPendenciaService")
public class AwbOcorrenciaPendenciaService extends CrudService<AwbOcorrenciaPendencia, Long> {
	private static final String DISPONIVEL_RETIRADA = "DR";

	private static final String LIBERADO_PELA_FISCALIZACAO_AEROPORTUARIA = "LF";

	private static final String EM_LIBERACAO_FISCAL_AEROPORTUARIA = "FS";

	private static final String DM_TIPO_LOCALIZACAO_AWB = "DM_TIPO_LOCALIZACAO_AWB";
	
	private static final String TP_OCORRENCIA_BLOQUEIO = "B";
	
	private AwbOcorrenciaService awbOcorrenciaService;
	
	private DomainValueService domainValueService;
	
	private TrackingAwbService trackingAwbService;
	
	private LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService;
	
	private AwbService awbService;
	
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	private ConfiguracoesFacade configuracoesFacade;

	public void validateOcorrenciaPendencia(Long idAwb, Long idOcorrenciaPendencia, Long idCiaAerea) {
		Awb awb = awbService.findById(idAwb);
		OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findById(idOcorrenciaPendencia);
		DomainValue tpLocalizacaoEmLiberacaoFiscal = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, EM_LIBERACAO_FISCAL_AEROPORTUARIA);
		LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea = localizacaoAwbCiaAereaService.findByIdCiaAereaAndTpLocalizacao(idCiaAerea, tpLocalizacaoEmLiberacaoFiscal);
		
		if (localizacaoAwbCiaAerea == null) {
			String param2 = configuracoesFacade.getMensagem("liberacaoEventoDisponibilizacao");
			if (TP_OCORRENCIA_BLOQUEIO.equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {
				param2 = ocorrenciaPendencia.getTpOcorrencia().getDescriptionAsString();
			}
			throw new BusinessException("LMS-04536", new Object[]{awb.getCiaFilialMercurio().getEmpresa().getPessoa().getNmFantasia(), param2});	
		}		
	}	

	public void storeAwbOcorrenciaPendencia(Long idAwb, Long idOcorrenciaPendencia, Long idCiaAerea, DateTime dhOcorrenciaPendencia) {
		Awb awb = awbService.findById(idAwb);
		OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findById(idOcorrenciaPendencia);
		DomainValue tpOcorrencia = ocorrenciaPendencia.getTpOcorrencia();
		
		UsuarioLMS usuarioLms = new UsuarioLMS();
		usuarioLms.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		AwbOcorrenciaPendencia awbOcorrenciaPendencia = populateAwbOcorrenciaPendencia(dhOcorrenciaPendencia, ocorrenciaPendencia, awb, usuarioLms);
		super.store(awbOcorrenciaPendencia);
		
		if (TP_OCORRENCIA_BLOQUEIO.equals(tpOcorrencia.getValue())) {
			DomainValue tpLocalizacaoEmLiberacaoFiscal = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, EM_LIBERACAO_FISCAL_AEROPORTUARIA);
			LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea = getLocalizacaoAwbCiaAwbAerea(idCiaAerea, tpLocalizacaoEmLiberacaoFiscal);
			storeAwbOcorrenciaLocalizacao(dhOcorrenciaPendencia, awb, tpLocalizacaoEmLiberacaoFiscal);
			storeTrackingAwb(dhOcorrenciaPendencia, awb, localizacaoAwbCiaAerea);
		} else {
//			gerar registro - Liberado pela Fiscalização Aeroportuária¦
			DomainValue tpLocalizacaoLiberadoPelaFiscalizacao = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, LIBERADO_PELA_FISCALIZACAO_AEROPORTUARIA);
			LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea = getLocalizacaoAwbCiaAwbAerea(idCiaAerea, tpLocalizacaoLiberadoPelaFiscalizacao);
			storeAwbOcorrenciaLocalizacao(dhOcorrenciaPendencia, awb, tpLocalizacaoLiberadoPelaFiscalizacao);
			storeTrackingAwb(dhOcorrenciaPendencia, awb, localizacaoAwbCiaAerea);
			
//			gerar registro Disponível para Retirada¦
			DomainValue tpLocalizacaoDisponivelRetirada = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, DISPONIVEL_RETIRADA);
			LocalizacaoAwbCiaAerea localizacaoAwbCiaAereaDR = getLocalizacaoAwbCiaAwbAerea(idCiaAerea, tpLocalizacaoDisponivelRetirada);			
			storeAwbOcorrenciaLocalizacao(dhOcorrenciaPendencia, awb, tpLocalizacaoDisponivelRetirada);
			storeTrackingAwb(dhOcorrenciaPendencia, awb, localizacaoAwbCiaAereaDR);		
		}
		
		awb.setTpLocalizacao(getTpLocalizacaoByTpOcorrencia(tpOcorrencia));
		awbService.store(awb);
		
	}

	private AwbOcorrenciaLocalizacao storeAwbOcorrenciaLocalizacao(DateTime dhOcorrenciaPendencia, Awb awb, DomainValue tpLocalizacaoEmLiberacaoFiscal) {
		AwbOcorrenciaLocalizacao awbOcorrenciaLocalizacao = new AwbOcorrenciaLocalizacao();
		awbOcorrenciaLocalizacao.setAwb(awb);
		awbOcorrenciaLocalizacao.setDhOcorrencia(dhOcorrenciaPendencia);		
		awbOcorrenciaLocalizacao.setTpLocalizacao(tpLocalizacaoEmLiberacaoFiscal);
		
		awbOcorrenciaService.store(awbOcorrenciaLocalizacao);
		
		return awbOcorrenciaLocalizacao;
	}

	private TrackingAwb storeTrackingAwb(DateTime dhOcorrenciaPendencia, Awb awb,
			LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea) {
		TrackingAwb trackingAwb = new TrackingAwb();
		trackingAwb.setAwb(awb);
		trackingAwb.setLocalizacaoAwbCiaAerea(localizacaoAwbCiaAerea);
		trackingAwb.setDhEvento(dhOcorrenciaPendencia);
		trackingAwbService.store(trackingAwb);
		
		return trackingAwb;
	}

	private LocalizacaoAwbCiaAerea getLocalizacaoAwbCiaAwbAerea(Long idCiaAerea, DomainValue tpLocalizacao) {		
		return localizacaoAwbCiaAereaService.findByIdCiaAereaAndTpLocalizacao(idCiaAerea, tpLocalizacao);
	}

	private AwbOcorrenciaPendencia populateAwbOcorrenciaPendencia(DateTime dhOcorrenciaPendencia, OcorrenciaPendencia ocorrenciaPendencia, Awb awb, UsuarioLMS usuarioLms) {
		AwbOcorrenciaPendencia awbOcorrenciaPendencia = new AwbOcorrenciaPendencia();		
		awbOcorrenciaPendencia.setAwb(awb);
		awbOcorrenciaPendencia.setUsuarioLms(usuarioLms);
		awbOcorrenciaPendencia.setOcorrenciaPendencia(ocorrenciaPendencia);
		awbOcorrenciaPendencia.setDhOcorrencia(dhOcorrenciaPendencia);
		return awbOcorrenciaPendencia;
	}

	private DomainValue getTpLocalizacaoByTpOcorrencia(DomainValue tpOcorrencia) {
		DomainValue tpLocalizacao = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, DISPONIVEL_RETIRADA);
		if (tpOcorrencia != null && TP_OCORRENCIA_BLOQUEIO.equals(tpOcorrencia.getValue())) {
			tpLocalizacao = domainValueService.findDomainValueByValue(DM_TIPO_LOCALIZACAO_AWB, EM_LIBERACAO_FISCAL_AEROPORTUARIA);
		}
		return tpLocalizacao;
	}
	
	@Override
	public AwbOcorrenciaPendencia store(AwbOcorrenciaPendencia bean) {
		return (AwbOcorrenciaPendencia)super.store(bean);
	}

	public List<AwbOcorrenciaPendencia> findAwbOcorrenciaByAwb(Long idAwb) {
		return getAwbOcorrenciaPendenciaDAO().findAwbOcorrenciaByAwb(idAwb);
	}

	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}

	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public TrackingAwbService getTrackingAwbService() {
		return trackingAwbService;
	}

	public void setTrackingAwbService(TrackingAwbService trackingAwbService) {
		this.trackingAwbService = trackingAwbService;
	}

	public LocalizacaoAwbCiaAereaService getLocalizacaoAwbCiaAereaService() {
		return localizacaoAwbCiaAereaService;
	}

	public void setLocalizacaoAwbCiaAereaService(
			LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService) {
		this.localizacaoAwbCiaAereaService = localizacaoAwbCiaAereaService;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public AwbOcorrenciaPendenciaDAO getAwbOcorrenciaPendenciaDAO() {
		return (AwbOcorrenciaPendenciaDAO) getDao();
	}

	public void setAwbOcorrenciaPendenciaDAO(AwbOcorrenciaPendenciaDAO dao) {
		setDao(dao);
	}

	public OcorrenciaPendenciaService getOcorrenciaPendenciaService() {
		return ocorrenciaPendenciaService;
	}

	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}
