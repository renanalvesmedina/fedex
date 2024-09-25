package com.mercurio.lms.coleta.action;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.cadastrarPedidoColetaPesoAforadoAction"
 */

public class CadastrarPedidoColetaPesoAforadoAction extends CrudAction {

	private ParametroGeralService parametroGeralService;
	private ServicoService servicoService;

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setService(PedidoColetaService pedidoColetaService) {
		this.defaultService = pedidoColetaService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public TypedFlatMap calculoPesoAforado(TypedFlatMap criteria) {
		TypedFlatMap dadosCalculo = new TypedFlatMap();

		BigDecimal alturaCm = criteria.getBigDecimal("altura");
		BigDecimal larguraCm = criteria.getBigDecimal("largura");
		BigDecimal profundidadeCm = criteria.getBigDecimal("profundidade");
		BigDecimal qtdVolumes = criteria.getBigDecimal("volumeDetalhe");
		BigDecimal pesoTelaPaiKg = criteria.getBigDecimal("pesoDetalhe");
		BigDecimal pesoAforadoResultanteKg = BigDecimalUtils.ZERO;
		//Caso o usuário tenha informado manualmente o volume.
		BigDecimal volumeM3 = criteria.getBigDecimal("volume");
		BigDecimal volumeCm3 = BigDecimal.ZERO;

		BigDecimal vFatorGeral = BigDecimal.ZERO; 

		if(alturaCm != null && larguraCm != null && profundidadeCm !=null) {
			//Calcula o volume (AxLxP) em centímetros cúbicos e transforma para metros cúbicos (/1000000)
			volumeCm3 = (alturaCm.multiply(larguraCm).multiply(profundidadeCm));
		} else if (volumeM3 != null){
			//Caso contrário, o volume já está informado manualmente na tela (em metros cúbicos).
			volumeCm3 = volumeM3.multiply(new BigDecimal(1000000));
		}
		Servico servico = servicoService.findById(criteria.getLong("idServico"));
		if(ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(servico.getTpModal().getValue())){
			vFatorGeral = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO, false);	
		} else {
			vFatorGeral = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO, false);
		}
		
		dadosCalculo.put("volume", volumeCm3.divide(new BigDecimal(1000000), 2, RoundingMode.HALF_EVEN));
		
		pesoAforadoResultanteKg = (volumeCm3.multiply(qtdVolumes)).divide(vFatorGeral, 3, 0);
		dadosCalculo.put("pesoAforado", pesoAforadoResultanteKg);

		if(pesoAforadoResultanteKg.compareTo(pesoTelaPaiKg) < 0) {
			throw new BusinessException("LMS-02007");
		}
		return dadosCalculo;
	}
}