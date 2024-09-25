/**
 * 
 */
package com.mercurio.lms.expedicao;

import java.math.BigDecimal;

import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;

/**
 * @author André Valadas
 * @since 02/05/2011
  */
public interface DoctoServicoValidateFacade {

	BigDecimal findLimitePesoVolume();
	BigDecimal findLimitePesoVolumeCalculoFrete();
	BigDecimal findLimiteValorCalculoFrete();

	void verifyLimitePeso(final CalculoFrete calculoFrete);
	Boolean validateLimitePeso(final BigDecimal psTotalVolumes, final BigDecimal limitePesoVolume);


	void setParametroGeralService(final ParametroGeralService parametroGeralService);
	
}
