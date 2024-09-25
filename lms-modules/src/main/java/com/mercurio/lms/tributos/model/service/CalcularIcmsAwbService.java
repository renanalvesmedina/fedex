package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.AliquotaIcmsAereo;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.tributos.calcularIcmsAwbService"
 */
public class CalcularIcmsAwbService {
    
	private AliquotaIcmsAereoService aliquotaIcmsAereoService;
	
	private TipoTributacaoIEService tipoTributacaoIEService;
	
	private UnidadeFederativaService unidadeFederativaService;
	
    /**
     * Assinatura para utilização do GT4
     * @param calculoFrete
     * @return calculoFrete com os impostos calculados
     */
	public CalculoFrete calcularIcmsAwb(CalculoFrete calculoFrete){
		CalcularIcmsParam cip = new CalcularIcmsParam(calculoFrete);
		Long idUfOrigem = cip.getCalculoFrete().getRestricaoRotaOrigem().getIdUnidadeFederativa();
		Long idUfDestino = cip.getCalculoFrete().getRestricaoRotaDestino().getIdUnidadeFederativa();
		Long idIeDestinatario = cip.getCalculoFrete().getDadosCliente().getIdInscricaoEstadualDestinatario();
		
		//String cdCtrcContratante = calculoFrete.get

		BigDecimal pcAliquota = new BigDecimal("0");
		String obIcms = "";
		
		//Buscar o Aliquota vigente
		AliquotaIcmsAereo aliquotaIcmsAereo = aliquotaIcmsAereoService.findVigenteByUF(idUfOrigem, JTDateTimeUtils.getDataAtual());
		
		if (aliquotaIcmsAereo == null){
			throw new BusinessException("LMS-23023", new Object[]{unidadeFederativaService.findById(idUfOrigem).getSgUnidadeFederativa()});
		}
		
		//Buscar o tipo de tributacao
		String tpSituacaoTributaria = getTpSituacaoTributaria(idIeDestinatario);

		//Se a UF de origem e de destino for a mesma que o destinatario
		if (idUfOrigem.equals(idUfDestino)){
			pcAliquota = aliquotaIcmsAereo.getPcAliquotaInterna();
			obIcms = aliquotaIcmsAereo.getObInterno();
		//Se a situação é 'não contribuante'
		} else if (tpSituacaoTributaria.equals("NC")) {
			pcAliquota = aliquotaIcmsAereo.getPcAliquotaDestNC();
			obIcms = aliquotaIcmsAereo.getObDestNC();
		} else {
			pcAliquota = aliquotaIcmsAereo.getPcAliquotaInterestadual();
			obIcms = aliquotaIcmsAereo.getObInterestadual();
		}
		
		setRetorno(cip, pcAliquota, obIcms);
		
    	return cip.getCalculoFrete();
	}
	
	private String getTpSituacaoTributaria(Long idIeDestinatario){
		String tpSituacaoTributaria = "NC";
		TipoTributacaoIE tipoTributacaoIE = tipoTributacaoIEService.findTiposTributacaoIEVigente(idIeDestinatario, JTDateTimeUtils.getDataAtual());
		
		if (tipoTributacaoIE != null){
			tpSituacaoTributaria = tipoTributacaoIE.getTpSituacaoTributaria().getValue();
		}
		
		return tpSituacaoTributaria;
	}
	
	private void setRetorno(CalcularIcmsParam cip, BigDecimal pcAliquota, String obIcms){
		cip.setPcAliquota(pcAliquota);
		cip.setVlIcmsTotal(cip.getVlTotalParcelas().multiply(pcAliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
		cip.setVlBase(cip.getVlTotalParcelas());
		cip.addObIcms(obIcms);
	}

	public void setAliquotaIcmsAereoService(
			AliquotaIcmsAereoService aliquotaIcmsAereoService) {
		this.aliquotaIcmsAereoService = aliquotaIcmsAereoService;
	}

	public void setTipoTributacaoIEService(
			TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

}