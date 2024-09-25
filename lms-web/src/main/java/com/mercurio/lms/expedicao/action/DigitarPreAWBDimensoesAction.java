/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.expedicao.digitarPreAWBDimensoesAction"
 */
public class DigitarPreAWBDimensoesAction extends DigitarNotaDimensoesAction {

	private ConfiguracoesFacade configuracoesFacade;

	public TypedFlatMap executeCalculoPesoCubado(TypedFlatMap data) {
		TypedFlatMap result = new TypedFlatMap();
		BigDecimal parametro = (BigDecimal) getConfiguracoesFacade().getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO);
		BigDecimal psCubado = data.getBigDecimal("psCubado");
		if(psCubado == null) {
			psCubado = BigDecimalUtils.ZERO;
		}
		if (parametro != null && CompareUtils.ne(parametro, BigDecimalUtils.ZERO)) {
			List dimensoes = getDimensoesInSession();
			if (dimensoes != null && dimensoes.size() > 0) {
				for (int i = 0; i < dimensoes.size(); i++) {
					Dimensao dimensao = (Dimensao) dimensoes.get(i);
					Integer areaBase = IntegerUtils.multiply(dimensao.getNrLargura(), dimensao.getNrComprimento());
					Integer volume = IntegerUtils.multiply(dimensao.getNrAltura(), areaBase);
					BigDecimal volumeTotal = new BigDecimal(IntegerUtils.multiply(volume, dimensao.getNrQuantidade()));
					BigDecimal total = BigDecimalUtils.divide(volumeTotal, parametro);
					
					psCubado = psCubado.add(total);
				}
			}
		}
		result.put("psCubado", psCubado);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mercurio.lms.expedicao.action.DigitarNotaDimensoesAction#getDimensoesInSession()
	 */
	protected List getDimensoesInSession() {
		Awb awb = AwbUtils.getAwbInSession();
		List dimensoes = awb.getDimensoes();
		if(dimensoes == null) {
			dimensoes = new ArrayList();
			awb.setDimensoes(dimensoes);
		}
		return dimensoes;
	}

	/**
	 * @return Returns the configuracoesFacade.
	 */
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}