package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.franqueados.model.DescontoFranqueado;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.LimiteParticipacaoFranqueado;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public abstract class CalculoDocumentoFranqueados implements ICalculoFranquiado {

	private Long idFranquia;
	protected BigDecimal pcLimite = BigDecimal.ZERO;
	private CalculoFranqueadoParametros parametrosFranqueado;
	private ConhecimentoFranqueadoDTO documentoDTO;
	private DoctoServicoFranqueado doctoServicoFranqueado;

	protected abstract void setMunicipio();

	protected abstract void setTipoFrete();

	protected abstract void setGris();

	protected abstract void setGeneralidade();

	protected abstract void setTipoOperacao();

	protected abstract void setCustoCarreteiro();
	
	protected abstract void setCustoAereo();

	protected abstract void calcularValorParticipacao();
	
	public void executarCalculo() {
		createDoctoServicoFranqueado();
		calcularBaseCalculo();
	}

	private void calcularBaseCalculo() {

		setTipoFrete();
		
		setMunicipio();

		setTipoOperacao();

		setPis();

		setConfins();

		setDesconto();

		setGris();

		setCustoCarreteiro();
		
		setCustoAereo();

		setGeneralidade();

		setBaseCalculo();

		calcularValorParticipacao();

	}

	private void createDoctoServicoFranqueado() {
		if (doctoServicoFranqueado == null) {
			Franquia franquia = new Franquia();
			franquia.setIdFranquia(idFranquia);
			doctoServicoFranqueado = new DoctoServicoFranqueado();
			doctoServicoFranqueado.setFranquia(franquia);
			doctoServicoFranqueado.setDtCompetencia(getParametrosFranqueado().getCompetencia().getInicio());
			
			Conhecimento conhecimento = new Conhecimento();
			conhecimento.setIdDoctoServico(getDTO().getIdDoctoServico());
			doctoServicoFranqueado.setConhecimento(conhecimento);
			
			if(getDTO().getIdDoctoServicoFranqueadoOriginal() != null){
				DoctoServicoFranqueado doctoServicoOriginal = new DoctoServicoFranqueado();
				doctoServicoOriginal.setIdDoctoServicoFrq(getDTO().getIdDoctoServicoFranqueadoOriginal());
				doctoServicoOriginal.setVlParticipacao(getDTO().getVlParticipacaoOriginal());
				doctoServicoFranqueado.setDoctoServicoFranqueadoOriginal(doctoServicoOriginal);
			}
			
			doctoServicoFranqueado.setVlDoctoServico(getDTO().getVlTotalDocServico());
			doctoServicoFranqueado.setVlMercadoria(getDTO().getVlMercadoria());
			doctoServicoFranqueado.setVlIcms(getDTO().getVlImposto());
			
			doctoServicoFranqueado.setNrKmTransferencia(getDTO().getNrDistancia());
		}
	}

	public DoctoServicoFranqueado getDoctoServicoFranqueado() {
		return doctoServicoFranqueado;
	}

	public Serializable getDocumentoFranqueado() {
		return getDoctoServicoFranqueado();
	}
	
	public void clearDoctoServicoFranqueado() {
		doctoServicoFranqueado = null;
	}

	private void setPis() {
		BigDecimal vlTotalDocServico = documentoDTO.getVlTotalDocServico();
		BigDecimal vlAliquota = parametrosFranqueado.getRepasseFranqueado().getPcAliqPis();
		getDoctoServicoFranqueado().setVlPis(FranqueadoUtils.calcularPercentualTruncado(vlTotalDocServico, vlAliquota));
	}

	private void setConfins() {
		BigDecimal vlTotalDocServico = documentoDTO.getVlTotalDocServico();
		BigDecimal vlAliquota = parametrosFranqueado.getRepasseFranqueado().getPcAliqCofins();
		getDoctoServicoFranqueado().setVlCofins(FranqueadoUtils.calcularPercentualTruncado(vlTotalDocServico, vlAliquota));
	}

	private void setDesconto() {
		BigDecimal vlDesconto = BigDecimal.ZERO;
		if(documentoDTO.getIdDevedorDocServFat() != null ){
			if(documentoDTO.getIdDesconto() != null && ConstantesVendas.SITUACAO_ATIVO.equals(documentoDTO.getTpSituacaoAprovacao())){
				DescontoFranqueado descontoFranqueado = parametrosFranqueado.getDescontosFranqueados(documentoDTO.getIdMotivoDesconto(),
						getDoctoServicoFranqueado().getTpFrete().getValue());
				if (descontoFranqueado != null) {
					vlDesconto = FranqueadoUtils.calcularPercentual(documentoDTO.getVlDesconto(), descontoFranqueado.getPcDesconto());
				}
			}
		}
		getDoctoServicoFranqueado().setVlDesconto(vlDesconto);
	}

	private void setBaseCalculo() {
		BigDecimal vlBaseCalculoTotal = documentoDTO.getVlTotalDocServico().subtract(doctoServicoFranqueado.getVlPis());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlCofins());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,documentoDTO.getVlImposto());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlDesconto());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlGris());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlCustoCarreteiro());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlCustoAereo());
		vlBaseCalculoTotal = substract(vlBaseCalculoTotal,doctoServicoFranqueado.getVlGeneralidade());
		vlBaseCalculoTotal = vlBaseCalculoTotal.setScale(2, FranqueadoUtils.ROUND_TYPE);

		if (vlBaseCalculoTotal.compareTo(BigDecimal.ZERO) < 0) {
			doctoServicoFranqueado.setVlAjusteBaseNegativa(vlBaseCalculoTotal);
		} else {
			doctoServicoFranqueado.setVlAjusteBaseNegativa(BigDecimal.ZERO);
		}
		if (doctoServicoFranqueado.getVlAjusteBaseNegativa().compareTo(BigDecimal.ZERO) != 0) {
			doctoServicoFranqueado.setVlBaseCalculo(BigDecimal.ZERO);
		} else {
			doctoServicoFranqueado.setVlBaseCalculo(vlBaseCalculoTotal);
		}

	}

	private BigDecimal substract(BigDecimal valor, BigDecimal sub){
		if( valor == null ){
			valor = BigDecimal.ZERO;
		}
		
		if( sub == null ){
			sub = BigDecimal.ZERO;
		}
		
		return valor.subtract(sub);
	}
	
	protected BigDecimal getPcLimite(){
		return pcLimite;
	}
	
	protected void calculoPcLimite() {
		List<LimiteParticipacaoFranqueado> lstLimiteParticipacao = getParametrosFranqueado().getLstLimiteParticipacao();
		
		DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
		//2.1
		int nrKmBase = 0;

		if( doc.getNrKmTransferencia() > 0 ) {
			nrKmBase = doc.getNrKmTransferencia();
		} else {
			nrKmBase = doc.getNrKmColetaEntrega();
		}

		//2.2
		boolean testLimite = false;

		
		for (LimiteParticipacaoFranqueado limiteParticipacaoFranqueado : lstLimiteParticipacao) {
			if (limiteParticipacaoFranqueado.getTpFrete().getValue().equals(doc.getTpFrete().getValue())	&& 
					limiteParticipacaoFranqueado.getTpOperacao().getValue().equals(doc.getTpOperacao().getValue()) &&
					limiteParticipacaoFranqueado.getNrKm() >= nrKmBase ) {
				this.pcLimite = limiteParticipacaoFranqueado.getPcLimite();
				testLimite = true;
				break;
			}
		}

		if(!testLimite) {
			//TODO-franqueados acertar a mensagem, ou, não deve nunca ser lançado, mas também não pode ir adiante se ocorrer
			throw new BusinessException("ERRO GRAVE! Limite não encontrado. ");
		}
	}
	
	protected void calcularParticipacaoDesconto() {
		DoctoServicoFranqueado documentoFranqueado = getDoctoServicoFranqueado();
		BigDecimal pcLimite = getPcLimite();
		BigDecimal vlLimite = FranqueadoUtils.calcularPercentual(documentoFranqueado.getVlBaseCalculo(), pcLimite);
		if (documentoFranqueado.getVlParticipacao() != null && documentoFranqueado.getVlParticipacao().compareTo(vlLimite) > 0) {
			documentoFranqueado.setVlDescontoLimitador(BigDecimal.ZERO);
			if (vlLimite.compareTo(BigDecimal.ZERO) == 0){
				documentoFranqueado.setVlParticipacao(BigDecimal.ZERO);
			}else if (pcLimite.compareTo(BigDecimal.valueOf(100)) < 0){
				documentoFranqueado.setVlParticipacao(vlLimite.setScale(2, FranqueadoUtils.ROUND_TYPE));
			}
		}
	}

	public void setIdFranquia(Long idFranquia) {
		this.idFranquia = idFranquia;
	}
	
	protected Long getIdFranquia() {
		return idFranquia;
	}

	public void setParametrosFranqueado(CalculoFranqueadoParametros parametrosFranqueado) {
		this.parametrosFranqueado = parametrosFranqueado;
	}

	public void setDocumento(DocumentoFranqueadoDTO documento) {
		this.documentoDTO = (ConhecimentoFranqueadoDTO)documento;
	}

	public ConhecimentoFranqueadoDTO getDTO() {
		return documentoDTO;
	}

	protected CalculoFranqueadoParametros getParametrosFranqueado() {
		return parametrosFranqueado;
	}

}
