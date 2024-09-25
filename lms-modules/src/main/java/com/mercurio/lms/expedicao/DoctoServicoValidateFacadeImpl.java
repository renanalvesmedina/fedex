/**
 * 
 */
package com.mercurio.lms.expedicao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * LMS-414
 * @author André Valadas
 */
public class DoctoServicoValidateFacadeImpl implements DoctoServicoValidateFacade {

	private ParametroGeralService parametroGeralService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;

	/**
	 * Busca LIMITE PESO VOLUME para validação de Pesagem e Emissao de Etiquetas 
	 * @return
	 */
	public BigDecimal findLimitePesoVolume() {
		return BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_VOLUME, false));
	}

	/**
	 * Busca LIMITE PESO VOLUME para validação do Cálculo de Frete
	 * @return
	 */
	public BigDecimal findLimitePesoVolumeCalculoFrete() {
		return BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_VOLUME_CALCULO_FRETE, false));
	}

	/**
	 * Busca LLIMITE MAXIMO DE VALOR DA MERCADORIA para validação do Cálculo de Frete
	 * @return
	 */
	public BigDecimal findLimiteValorCalculoFrete() {
		return BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_RELACAO_VL_FRETE_MERCADORIA, false));
	}

	/**
	 * Valida se existe já existe evento de cancelamento
	 * @param calculoFrete
	 * @return
	 */
	private Boolean validateEventoCancelado(final CalculoFrete calculoFrete) {
		Long idDoctoServico = calculoFrete.getIdDoctoServico();
		if(!LongUtils.hasValue(idDoctoServico)) {
			idDoctoServico = calculoFrete.getDoctoServico().getIdDoctoServico();
			}
		final List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findEventoDoctoServico(idDoctoServico, ConstantesSim.EVENTO_DOCUMENTO_CANCELADO);
		/** Verifica se existe evento de conhecimento cancelado */
		return eventos.isEmpty();
		}

	/**
	 * Valida o LIMITE MAXIMO DE VOLUMES PARA CALCULAR FRETE
	 * @param psTotalVolumes
	 */
	public void verifyLimitePeso(final CalculoFrete calculoFrete) {
		if(!validateEventoCancelado(calculoFrete)) {
			return;
		}

		final BigDecimal limitePesoVolume = this.findLimitePesoVolumeCalculoFrete();
		if (BooleanUtils.isFalse(validateLimitePeso(calculoFrete.getPsReferencia(), limitePesoVolume))) {		
			throw new BusinessException("LMS-04354", new Object[] {FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_3_CASAS, calculoFrete.getPsReferencia()), FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_3_CASAS, limitePesoVolume)});
		}
	}
	public Boolean validateLimitePeso(final BigDecimal psTotalVolumes, final BigDecimal limitePesoVolume) {
		return !CompareUtils.gt(psTotalVolumes, limitePesoVolume);
	}

	/**
	 * @param parametroGeralService the parametroGeralService to set
	 */
	public void setParametroGeralService(final ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	/**
	 * @param eventoDocumentoServicoService the eventoDocumentoServicoService to set
	 */
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
}