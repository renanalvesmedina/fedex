package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CancelarConhecimentoEletronicoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.IntegracaoNDDigitalService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NFEConjugadaService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.inutilizarConhecimentoEletronicoService"
 */
public class InutilizarConhecimentoEletronicoService {
	
	private EmitirCTRService emitirCTRService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ParametroGeralService parametroGeralService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	private CancelarConhecimentoEletronicoService cancelarConhecimentoEletronicoService;
	private ConhecimentoService conhecimentoService;
	private NotaFiscalServicoService notaFiscalServicoService; 
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NFEConjugadaService nfeConjugadaService;
	
	public void geraConhecimentoEletronicoInutilizacao(final Conhecimento conhecimento, final VolumeNotaFiscal volumeNotaFiscal) {
		conhecimento.setNrConhecimento(volumeNotaFiscal.getNrConhecimento());
		conhecimento.setNrDoctoServico(volumeNotaFiscal.getNrConhecimento());
		conhecimento.setDvConhecimento(ConhecimentoUtils.getDigitoVerificador(conhecimento.getNrConhecimento()));
		conhecimento.setPsReferenciaCalculo(BigDecimalUtils.ZERO);
		conhecimento.setVlTotalDocServico(new BigDecimal(0.01));
		conhecimento.setVlLiquido(new BigDecimal(0.01));
		emitirCTRService.updateDados(conhecimento, null, new DomainValue("C"), "A", null,false);
		
		MonitoramentoDocEletronico monitoramentoDocEletronico = new MonitoramentoDocEletronico();
		monitoramentoDocEletronico.setDoctoServico(conhecimento);
		generateIntegracaoInutilizacaoGenerica(conhecimento, monitoramentoDocEletronico);
	}
		
	public void generateInutilizacaoConhecimentoRejeitado(final Long idMonitoramentoDocEletronic){
		final MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
		//ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.24 / Não existe inutilização para NTE ou NSE
		if(ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue())){
			//busca o tipo específico
			final Conhecimento conhecimento = conhecimentoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
			cancelarConhecimentoEletronicoService.generateCancelarConhecimento(conhecimento, true, null);
			
			generateIntegracaoInutilizacaoGenerica(conhecimento, monitoramentoDocEletronico);
		} else if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue())){
			final Conhecimento conhecimento = conhecimentoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
			nfeConjugadaService.generateInutilizarNFE(conhecimento, idMonitoramentoDocEletronic);
			
		} else if(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue())){
			final NotaFiscalServico nota = notaFiscalServicoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
			nfeConjugadaService.generateInutilizarNFE(nota, idMonitoramentoDocEletronic);
		}		
	}

	/**
	 * Chamada na emissao do conhecimento e no botáo inutilizar da tela de Monitoramento de Documentos Eletrônicos
	 * 
	 * @param conhecimento
	 * @param monitoramentoDocEletronico
	 */
	private void generateIntegracaoInutilizacaoGenerica(final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico) {

		final String chave;

		Integer random = gerarConhecimentoEletronicoXMLService.getRandom(conhecimento);
		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("I"));

		if(monitoramentoDocEletronico.getNrChave() == null){
			chave = gerarConhecimentoEletronicoXMLService.gerarChaveAcesso(conhecimento, random);
		monitoramentoDocEletronico.setNrChave(chave.substring(3));
		} else {
			chave = "CTe" + monitoramentoDocEletronico.getNrChave();
		}

		//LMS-4210
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		String conteudoParametro = String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialUsuarioLogado.getIdFilial(), "Versão_XML_CTe", false));

		boolean versao300a = conteudoParametro != null && conteudoParametro.equals(EVersaoXMLCTE.VERSAO_300a.getConteudoParametro());

		if(versao300a) {

			String xml = gerarXMLInutCTE(chave);
			monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("H"));
			TBDatabaseInputCTE tbDatabaseInput = integracaoNDDigitalService.generateIntegracaoInutilizacao(monitoramentoDocEletronico, xml);
			monitoramentoDocEletronico.setIdEnvioDocEletronicoI(tbDatabaseInput.getId());

		}

		monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
		
	}
	
	//LMS-4210
	private String gerarXMLInutCTE(String chave) {
		StringBuilder sb = new StringBuilder();
		sb.append("<cteCancInut>");
		sb.append("<chCTe>");
		sb.append(chave);
		sb.append("</chCTe>");
		sb.append("<xMotivoInut>");
		sb.append(parametroGeralService.findSimpleConteudoByNomeParametro("JUST_INUT_CTE"));
		sb.append("</xMotivoInut>");
		sb.append("</cteCancInut>");
		
		return sb.toString();
	}
	
	public void setEmitirCTRService(EmitirCTRService emitirCTRService) {
		this.emitirCTRService = emitirCTRService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setGerarConhecimentoEletronicoXMLService(GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService) {
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}
	
	public void setCancelarConhecimentoEletronicoService(
			CancelarConhecimentoEletronicoService cancelarConhecimentoEletronicoService) {
		this.cancelarConhecimentoEletronicoService = cancelarConhecimentoEletronicoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}

	public NotaFiscalServicoService getNotaFiscalServicoService() {
		return notaFiscalServicoService;
	}

	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
	
}
