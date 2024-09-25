package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * classe inutil do daniel que deviria estar dentro de  EmitirCTRService
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emissaoCtoService"
 */
public class EmissaoCtoService {

	private static final String STATUS_SOM_EMITIDO = "2";
	private static final String STATUS_SOM_CANCELADO = "9";
	
	private EmitirCTRService emitirCTRService;
	private EmitirDocumentoService emitirDocumentoService;
	private GerarCTRService gerarCTRService;
	private ConhecimentoService conhecimentoService;
	
	public void storeEmitirConhecimento(Long idConhecimento,
			ImpressoraFormulario impressoraFormulario,
			Long nrProximoCodigoBarras,
			String tpOperacaoEmissao,
			Long idFilial,
			Boolean lmsImplantadoFilial,
			List forms,
			List<String> ctrcs,
			String statusConhSOM,
			VolumeNotaFiscal volumeNotaFiscal,
			Boolean semNumeroReservado) {
		
		storeEmitirConhecimento(idConhecimento, 
				impressoraFormulario, 
				nrProximoCodigoBarras, 
				tpOperacaoEmissao, 
				idFilial, 
				lmsImplantadoFilial, 
				forms, 
				ctrcs, 
				statusConhSOM, 
				volumeNotaFiscal, 
				semNumeroReservado, 
				Boolean.FALSE);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeEmitirConhecimento(Long idConhecimento,
			ImpressoraFormulario impressoraFormulario,
			Long nrProximoCodigoBarras,
			String tpOperacaoEmissao,
			Long idFilial,
			Boolean lmsImplantadoFilial,
			List forms,
			List<String> ctrcs,
			String statusConhSOM,
			VolumeNotaFiscal volumeNotaFiscal,
			Boolean semNumeroReservado,
			Boolean blExecutarVerificacaoDocumentoManifestado) {
		
		/*Obtem o conhecimento*/ 
		Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

		/*Se o CTRC for Reentrega, Complemento de ICMS e Complemento de Frete atualiza a emissao com a data atual*/				
		if(BooleanUtils.isTrue(semNumeroReservado)){
			conhecimento.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());			
		}
		
		/*Verifica se é uma emissao*/		
		if(ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)) {
			Boolean aferido = conhecimento.getBlPesoAferido() != null && conhecimento.getBlPesoAferido();
			
			/*Atualiza o conhecimento*/
			if((aferido && conhecimento.getNrConhecimento() > 0) || (aferido && semNumeroReservado)) {
				emitirCTRService.updateDados(conhecimento, impressoraFormulario, new DomainValue("E"), lmsImplantadoFilial ? "A" : null, nrProximoCodigoBarras, semNumeroReservado);
				statusConhSOM = STATUS_SOM_EMITIDO;
			} else if(conhecimento.getBlPesoAferido() == null || !conhecimento.getBlPesoAferido() || conhecimento.getNrConhecimento() <= 0) {
				conhecimento.setNrConhecimento(volumeNotaFiscal.getNrConhecimento());
				conhecimento.setNrDoctoServico(volumeNotaFiscal.getNrConhecimento());
				conhecimento.setDvConhecimento(ConhecimentoUtils.getDigitoVerificador(conhecimento.getNrConhecimento()));
				conhecimento.setPsReferenciaCalculo(BigDecimalUtils.ZERO);
				conhecimento.setVlTotalDocServico(new BigDecimal(0.01));
				conhecimento.setVlLiquido(new BigDecimal(0.01));
				emitirCTRService.updateDados(conhecimento, impressoraFormulario, new DomainValue("C"), lmsImplantadoFilial ? "A" : null, nrProximoCodigoBarras, semNumeroReservado);
				statusConhSOM = STATUS_SOM_CANCELADO;
			}
			
			/*Grava eventos para determinado tipo de conhecimento*/
			conhecimento.setBlExecutarVerificacaoDocumentoManifestado(blExecutarVerificacaoDocumentoManifestado);
			emitirCTRService.storeEventos(conhecimento, idFilial);

		} else if(ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao)) {
			emitirCTRService.updateDados(conhecimento, impressoraFormulario, conhecimento.getTpSituacaoConhecimento(), lmsImplantadoFilial ? "A" : null, nrProximoCodigoBarras, semNumeroReservado);
			if(conhecimento.getTpSituacaoConhecimento().equals(new DomainValue("C"))){
				statusConhSOM = STATUS_SOM_CANCELADO;
			} else {
				statusConhSOM = STATUS_SOM_EMITIDO;
			}
		}
		
		/*Acumula variaveis para mandar para impressao*/
		ctrcs.add(getGerarCTRService().generateCTR(conhecimento)); // acumula variaveis para mandar para impressao
		
		/*Acumula variaveis para mandar para som*/
		if(!lmsImplantadoFilial) { 
			Map mapForms = new HashMap();
			mapForms.put("nrFormulario", conhecimento.getNrFormulario());
			mapForms.put("nrConhecimento", conhecimento.getNrConhecimento());
			mapForms.put("dhEmissao", conhecimento.getDhEmissao());
			mapForms.put("dataSOM", getConhecimentoService().findDataToSOM(conhecimento.getIdDoctoServico(), statusConhSOM));
			forms.add(mapForms);
		}
	
	}

	/**
	 * @param emitirCTRService the emitirCTRService to set
	 */
	public void setEmitirCTRService(EmitirCTRService emitirCTRService) {
		this.emitirCTRService = emitirCTRService;
	}

	/**
	 * @param emitirDocumentoService the emitirDocumentoService to set
	 */
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}

	/**
	 * @return the emitirDocumentoService
	 */
	public EmitirDocumentoService getEmitirDocumentoService() {
		return emitirDocumentoService;
	}

	/**
	 * @param gerarCTRService the gerarCTRService to set
	 */
	public void setGerarCTRService(GerarCTRService gerarCTRService) {
		this.gerarCTRService = gerarCTRService;
	}

	/**
	 * @return the gerarCTRService
	 */
	public GerarCTRService getGerarCTRService() {
		return gerarCTRService;
	}

	/**
	 * @param conhecimentoService the conhecimentoService to set
	 */
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	/**
	 * @return the conhecimentoService
	 */
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}
}
