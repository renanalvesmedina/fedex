package com.mercurio.lms.expedicao.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.ContingenciaService;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.IntegracaoNDDigitalService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDescargaService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.gerarConhecimentoEletronicoService"
 */
public class GerarConhecimentoEletronicoService {
	
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	private EmitirCTRService emitirCTRService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ConhecimentoService conhecimentoService;
	private InutilizarConhecimentoEletronicoService inutilizarConhecimentoEletronicoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private ContingenciaService contingenciaService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateConhecimentoEletronico(final Long idConhecimento, final VolumeNotaFiscal volumeNotaFiscal, Boolean semNumeroReservado,Long idMonitoramentoDescarga) {
		String generatedXml = "";
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(idConhecimento);

		/*
		 * Não pode ser recebido por parâmetro pois é outra conexão, traria
		 * problemas com os proxies do Hibernate
		 */
		Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);
		Boolean aferido = conhecimento.getBlPesoAferido() != null && conhecimento.getBlPesoAferido();
		
		Boolean descargaIniciada = false;
		if ( idMonitoramentoDescarga != null ){
			descargaIniciada = "G".equalsIgnoreCase(monitoramentoDescargaService.findById(idMonitoramentoDescarga).getTpSituacaoDescarga().getValue());
		}
		if(BooleanUtils.isTrue(semNumeroReservado)){
			conhecimento.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());			
		}

		/*Atualiza o conhecimento*/
		if((aferido && conhecimento.getNrConhecimento() > 0) || (aferido && semNumeroReservado)) {
			
			emitirCTRService.updateDadosConsignatario(conhecimento);
			emitirCTRService.updateDados(conhecimento, null, conhecimento.getTpSituacaoConhecimento(), "A", null, semNumeroReservado);
			generatedXml = generateConhecimentoEletronicoEmissaoGenerico(conhecimento, monitoramentoDocEletronico);

		} else if ((conhecimento.getBlPesoAferido() == null || !conhecimento.getBlPesoAferido() || conhecimento.getNrConhecimento() <= 0) && !descargaIniciada) {
			if (monitoramentoDocEletronico == null) {
				inutilizarConhecimentoEletronicoService.geraConhecimentoEletronicoInutilizacao(conhecimento, volumeNotaFiscal);
				/*Grava eventos para determinado tipo de conhecimento*/
				emitirCTRService.storeEventos(conhecimento, SessionUtils.getFilialSessao().getIdFilial());
				generatedXml = "CANCELADO";
			}
		}

		return generatedXml;
	}

	
	/**
	 * Usado no envio e reenvio
	 * 
	 * @param conhecimento
	 * @param monitoramentoDocEletronico
	 * @return
	 */
	public String generateConhecimentoEletronicoEmissaoGenerico(final Conhecimento conhecimento, MonitoramentoDocEletronico monitoramentoDocEletronico) {
		Contingencia emContingencia = contingenciaService.findByFilial(SessionUtils.getFilialSessao().getIdFilial(), "A", "C");
		
		if (monitoramentoDocEletronico == null) {
			monitoramentoDocEletronico = new MonitoramentoDocEletronico();
			monitoramentoDocEletronico.setDoctoServico(conhecimento);

		} else if (monitoramentoDocEletronico != null && (monitoramentoDocEletronico.getTpSituacaoDocumento() != null && monitoramentoDocEletronico.getTpSituacaoDocumento().getValue().equals("E")) && emContingencia != null) {

			//JIRA-4048 - Gera e persiste o XML de Abort
			generateConhecimentoEletronicoEmissaoAbort(conhecimento, monitoramentoDocEletronico);
		}

		
		String xml = gerarConhecimentoEletronicoXMLService.gerarXML(conhecimento, monitoramentoDocEletronico, emContingencia);
		
		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("E"));
		
		xml = generateIntegracaoEmissao(monitoramentoDocEletronico, xml);
		
		
		return xml;
	}

	
	/**
	 * Usado para quando em Contingência
	 * 
	 * @param conhecimento
	 * @param monitoramentoDocEletronico
	 * @return
	 */
	public void generateConhecimentoEletronicoEmissaoAbort(final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico) {
		
		
		String xml = gerarConhecimentoEletronicoXMLService.gerarXmlAbort(conhecimento, monitoramentoDocEletronico);
		
		xml = generateIntegracaoEmissao(monitoramentoDocEletronico, xml);
	}
	
	
	private String generateIntegracaoEmissao(final MonitoramentoDocEletronico monitoramentoDocEletronico, String xml) {
		TBDatabaseInputCTE tbDatabaseInput = integracaoNDDigitalService.generateIntegracaoEmissao(monitoramentoDocEletronico, xml);
		
		monitoramentoDocEletronico.setIdEnvioDocEletronicoE(tbDatabaseInput.getId());
		monitoramentoDocEletronico.setDsEnvioEmail(concatenaB2B(xml));
		monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
		
		return xml;
	}

	
	public String findXmlCteComComplementos(Long idDoctoServico){
		return gerarConhecimentoEletronicoXMLService.findXmlCteComComplementos(idDoctoServico); 
	}
	
	public void addListXmlCteComComplementos(List<Map<String,Object>> cteXML){
		gerarConhecimentoEletronicoXMLService.addListXmlCteComComplementos(cteXML);
	}
	
	/**
	 * Concatenacao dos conteudos das tags B2B separados por ';'
	 * 
	 * @param b2b
	 */
	private String concatenaB2B(String b2b) {
	    if(b2b.toString().isEmpty() || !b2b.contains("<B2B>")) {
		return "";
	    }
	    
	    int f1 = b2b.indexOf("<B2B>");
	    int f2 = b2b.lastIndexOf("</B2B>");
	    
	    String emails = b2b.substring(f1, f2).replace("<B2B>", "");
	    String[] emailsList = emails.split("</B2B>");
	    StringBuilder email = new StringBuilder();
	    
	    for (int i = 0; i < emailsList.length; i++) {
		email.append(emailsList[i]);
		email.append("; ");
	    }
	    
	    return email.toString();	    
	}
	
	public void setEmitirCTRService(EmitirCTRService emitirCTRService) {
		this.emitirCTRService = emitirCTRService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setInutilizarConhecimentoEletronicoService(InutilizarConhecimentoEletronicoService inutilizarConhecimentoEletronicoService) {
		this.inutilizarConhecimentoEletronicoService = inutilizarConhecimentoEletronicoService;
	}

	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setGerarConhecimentoEletronicoXMLService(GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService) {
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}

	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}
	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

}