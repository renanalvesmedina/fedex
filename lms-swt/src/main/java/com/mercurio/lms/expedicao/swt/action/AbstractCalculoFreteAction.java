/**
 * 
 */
package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoNormalService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * @author luisp
 *
 */
public class AbstractCalculoFreteAction extends CrudAction {

	private DivisaoClienteService divisaoClienteService;
	private ParcelaPrecoService parcelaPrecoService;
	protected ConhecimentoService conhecimentoService;
	private ParametroGeralService parametroGeralService;
	protected ConhecimentoNormalService conhecimentoNormalService;
	protected DoctoServicoService doctoServicoService;
	
	public List findDivisaoCliente(Map parameters) {
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		Cliente cliente = calculoFrete.getClienteBase();
		if(ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue())) {
			List divisoes = divisaoClienteService.findDivisaoClienteByIdServico(cliente.getIdCliente(), calculoFrete.getIdServico(), "A");
			if (divisoes != null && !divisoes.isEmpty()) {
				List<Map> result = new ArrayList<Map>();
				for (int i = 0; i < divisoes.size(); i++) {
					DivisaoCliente divisaoCliente = (DivisaoCliente) divisoes.get(i);
					Map<String, Object> dc = new HashMap<String, Object>();
					dc.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
					dc.put("dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
					result.add(dc);
				}
				return result;
			}
		}
		return null;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findDataGrid(Map parameters) {
		Map retorno = new HashMap();
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		if(calculoFrete.getBlCalculaParcelas().booleanValue()) {
			
			List listParcelas = new ArrayList();
			ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("PARCELAS_CALC_MANUAL", false);
			if (parametroGeral != null && parametroGeral.getDsConteudo() != null ) {
				String [] dsConteudo = parametroGeral.getDsConteudo().split(";");
				for (String vlConteudo : dsConteudo) {
					Map parcela = new HashMap();
					parcela.putAll(parcelaPrecoService.findParcelaByCdParcelaPreco(vlConteudo));
				parcela.put("dsParcela", parcela.remove("nmParcelaPreco"));
				parcela.put("idParcela", parcela.remove("idParcelaPreco"));
					listParcelas.add(parcela);
			}
		}
			retorno.put("parcelaFrete", listParcelas);	
		}
		return retorno;
	}
	
	/**
	 * Método que chama o serviço para o calculo do frete manual
	 * autor Julio Cesar Fernandes Corrêa
	 * 19/12/2005
	 * @param parameters
	 * @return 
	 */
	public Map executaCalculoManual(Map parameters) {
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		
		conhecimentoNormalService.validateCalculoFreteManual(calculoFrete, conhecimento, parameters);

		TypedFlatMap map = new TypedFlatMap();
		map.put("vlFrete", conhecimento.getVlTotalDocServico());
		doctoServicoService.executeValidacaoLimiteValorFrete(map);
		
		map.put("vlMercadoria", conhecimento.getVlMercadoria());
		doctoServicoService.executeValidacaoPercentualValorMercadoria(map);
		
		return montarParcelasCalculo(conhecimento, calculoFrete);
	}
	
	/**
	 * Após o calculo do frete, é necessário montar as parcelas do doctoservico, além de calcular os
	 * subtotais do valor do frete e do valor dos serviços.
	 * 
	 * autor Julio Cesar Fernandes Corrêa
	 * 19/12/2005
	 * @param ds
	 * @param calculo
	 * @return
	 */
	protected Map montarParcelasCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		Map retorno = new HashMap();
		List parcelasDoctoServ = new ArrayList();

		/** Parcelas */
		if(calculoFrete.getBlCalculaParcelas().booleanValue() && calculoFrete.getParcelas() != null) {
			List parcelas = new ArrayList();
			CalculoFreteUtils.ordenaParcelas(calculoFrete.getParcelas());
			for (Iterator iter = calculoFrete.getParcelas().iterator(); iter.hasNext();) {
				ParcelaServico parcelaServico = (ParcelaServico) iter.next();
				ParcelaPreco parcelaPreco = parcelaServico.getParcelaPreco();
				Map parcela = new HashMap();
				parcela.put("vlParcela", parcelaServico.getVlParcela());
				parcela.put("dsParcela", null);
				if (parcelaPreco.getDsParcelaPreco() != null) {
					parcela.put("dsParcela", parcelaPreco.getDsParcelaPreco().getValue());
				}
				parcelas.add(parcela);
				parcelasDoctoServ.add(new ParcelaDoctoServico(parcelaServico.getVlBrutoParcela(), parcelaServico.getVlParcela(), parcelaPreco, conhecimento));
			}
			retorno.put("parcelasFrete", parcelas);
		}

		/** Servicos Adicionais */
		if(calculoFrete.getBlCalculaServicosAdicionais().booleanValue() && calculoFrete.getServicosAdicionais() != null) {
			List servicos = new ArrayList();
			for (Iterator iter = calculoFrete.getServicosAdicionais().iterator(); iter.hasNext();) {
				ParcelaServico parcelaServico = (ParcelaServico) iter.next();
				ParcelaPreco parcelaPreco = parcelaServico.getParcelaPreco();
				Map parcela = new HashMap();
				parcela.put("vlServico", parcelaServico.getVlParcela());
				parcela.put("dsServico", null);
				if (parcelaPreco.getDsParcelaPreco() != null) {
					parcela.put("dsServico", parcelaPreco.getDsParcelaPreco().getValue());
				}
				servicos.add(parcela);
				parcelasDoctoServ.add(new ParcelaDoctoServico(parcelaServico.getVlBrutoParcela(), parcelaServico.getVlParcela(), parcelaPreco, conhecimento));
			}
			retorno.put("servicos", servicos);
		}
		retorno.put("vlTotalFrete", calculoFrete.getVlTotalParcelas());
		retorno.put("vlTotalServico", calculoFrete.getVlTotalServicosAdicionais());
		retorno.put("vlTotalCtrc", calculoFrete.getVlTotal());
		retorno.put("vlDesconto", calculoFrete.getVlDesconto());
		
		conhecimento.setParcelaDoctoServicos(parcelasDoctoServ);

		//Setar o valor do vlFretePesoDeclarado com valor do VL_DEVIDO que retorna do calculo do ICMS - CQPRO00026981
		conhecimento.setVlFretePesoDeclarado(calculoFrete.getVlDevido());

		retorno.put("conhecimento", conhecimento);
		retorno.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);

		return retorno;
	}

	
	/*
	 * GETTERS E SETTERS
	 */

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
}
	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
}
