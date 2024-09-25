package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.dao.CalculoNFServicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * @author Claiton Grings 
 *
 * @spring.bean id="lms.expedicao.calculoNFServicoService" autowire="no"
 * @spring.property name="calculoNFServicoDAO" ref="lms.expedicao.calculoNFServicoDAO"
 * @spring.property name="calculoParcelaNFServicoService" ref="lms.expedicao.calculoParcelaNFServicoService"
 * @spring.property name="calculoTributoService" ref="lms.expedicao.calculoTributoService"
*/
public class CalculoNFServicoService extends CalculoServicoService {
	public CalculoNFServicoDAO getCalculoNFServicoDAO() {
		return (CalculoNFServicoDAO) super.getCalculoServicoDAO();
	}
	public void setCalculoNFServicoDAO(CalculoNFServicoDAO calculoNFServicoDAO) {
		super.setCalculoServicoDAO(calculoNFServicoDAO);
	}
	public CalculoParcelaNFServicoService getCalculoParcelaNFServicoService() {
		return (CalculoParcelaNFServicoService) super.getCalculoParcelaServicoService();
	}
	public void setCalculoParcelaNFServicoService(CalculoParcelaNFServicoService calculoParcelaNFServicoService) {
		super.setCalculoParcelaServicoService(calculoParcelaNFServicoService);
	}

	protected void findParametroCliente(CalculoServico calculoServico) {
		if(ClienteUtils.isParametroClienteEspecial(calculoServico.getClienteBase().getTpCliente().getValue())) {
			calculoServico.setParametroCliente(new ParametroCliente());
		}
	}

	protected void findTabelaPreco(CalculoServico calculoServico) throws BusinessException {
		CalculoNFServico calculoNFServico = (CalculoNFServico) calculoServico;
		TabelaPreco tabelaPreco = null;

		if(ClienteUtils.isParametroClienteEspecial(calculoNFServico.getClienteBase().getTpCliente().getValue())) {
			Long idCotacao = null;
			if(ConstantesExpedicao.CALCULO_COTACAO.equals(calculoNFServico.getTpCalculo())) {
				idCotacao = calculoNFServico.getIdCotacao();
			}
			tabelaPreco = getCalculoNFServicoDAO().findTabelaPreco(calculoNFServico.getIdDivisaoCliente(), calculoNFServico.getIdServico(), calculoNFServico.getIdParcelaPreco(), idCotacao);
			if(tabelaPreco == null) {
				tabelaPreco = findTabelaPrecoMercurio(calculoNFServico);
			}
		} else {
			tabelaPreco = findTabelaPrecoMercurio(calculoNFServico);
		}

		if(tabelaPreco == null) {
			throw new BusinessException("LMS-30023");
		}
		calculoNFServico.setTabelaPreco(tabelaPreco);
	}

	protected void findPesoReferencia(CalculoServico calculoServico) {
		//serah informado na tela
	}

	private void findNFServicoNormal(CalculoNFServico calculoNFServico) {
		//verifica parametro e tabela para calculo
		findParametroCliente(calculoNFServico);
		findTabelaPreco(calculoNFServico);

		//calcula servicos adicionais
		findServicosAdicionais(calculoNFServico);
	}

	private void findNFServicoCotacao(CalculoNFServico calculoNFServico) {
		//mesmo calculo do normal
		findNFServicoNormal(calculoNFServico);
	}

	private void findNFServicoManual(CalculoNFServico calculoNFServico) {
		//calcula apenas os impostos
	}

	public CalculoNFServico executeCalculoNFServico(CalculoNFServico calculoNFServico) {
		//Verifica os dados do cliente base
		findClienteBase(calculoNFServico);

		if(ConstantesExpedicao.CALCULO_NORMAL.equals(calculoNFServico.getTpCalculo())) {
			findNFServicoNormal(calculoNFServico);
		} else if(ConstantesExpedicao.CALCULO_COTACAO.equals(calculoNFServico.getTpCalculo())) {
			findNFServicoCotacao(calculoNFServico);
		} else if(ConstantesExpedicao.CALCULO_MANUAL.equals(calculoNFServico.getTpCalculo())) {
			findNFServicoManual(calculoNFServico);
		}

		getCalculoTributoService().calculaTributos(calculoNFServico);
		//executeCalculoSubTotal(calculoNFServico);

		return calculoNFServico;
	}
}