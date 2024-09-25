package com.mercurio.lms.expedicao.model.service;

/**
 * @author Claiton Grings 
*/

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.DoctoServicoValidateFacade;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.ParcelaServicoAdicional;
import com.mercurio.lms.expedicao.model.dao.CalculoServicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public abstract class CalculoServicoService {
	private CalculoServicoDAO calculoServicoDAO;
	private CalculoParcelaServicoService calculoParcelaServicoService;
	private CalculoTributoService calculoTributoService;
	private DoctoServicoValidateFacade doctoServicoValidateFacade;

	protected abstract void findParametroCliente(CalculoServico calculoServico);

	protected abstract void findTabelaPreco(CalculoServico calculoServico) throws BusinessException;

	protected abstract void findPesoReferencia(CalculoServico calculoServico);

	//Verifica cliente base para calculo
	protected void findClienteBase(CalculoServico calculoServico) {
		Cliente clienteBase = calculoServico.getClienteBase();
		if(clienteBase != null && clienteBase.getIdCliente() != null) {
			clienteBase = calculoServicoDAO.findCliente(clienteBase.getIdCliente());
		} else {
			//Se não existe cliente - cria cliente e seta como Eventual
			clienteBase = new Cliente();
			clienteBase.setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_EVENTUAL));
		}
		calculoServico.setClienteBase(clienteBase);
	}

	/**
	 * Busca a tabela preco normal ou FOB
	 * 
	 * 
	 * @param  calculoServico
	 * @return TabelaPreco
	 */
public TabelaPreco findTabelaPrecoMercurio(CalculoServico calculoServico) {

		String tpTipoTabelaPreco = null;
		String tpSubtipoTabelaPreco = null;

		TabelaPreco tabelaPreco = null;

		/*Se o tipo de frete for FOB verifica através do tipo tabela preco T
		e sub tipo tabela preco F*/
		if(ConstantesExpedicao.TP_FRETE_FOB.equals(calculoServico.getTpFrete())){

			tpTipoTabelaPreco    = "T";
			tpSubtipoTabelaPreco = "F";

			tabelaPreco = calculoServicoDAO.findTabelaPreco(calculoServico.getIdServico(), tpTipoTabelaPreco, tpSubtipoTabelaPreco);
			
			if(tabelaPreco != null){
				return tabelaPreco;
			}
		}

		tpSubtipoTabelaPreco = "X";
		
		if(ConstantesExpedicao.TP_FRETE_CIF.equals(calculoServico.getTpFrete())){
			if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoServico.getTpModal())) {
				if(ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoServico.getTpAbrangencia())) {
					tpTipoTabelaPreco = "M";
				} else {
					tpTipoTabelaPreco = "I";
				}
			} else if(ConstantesExpedicao.MODAL_AEREO.equals(calculoServico.getTpModal())) {
				tpTipoTabelaPreco = "A";
			}
		}
		
		return calculoServicoDAO.findTabelaPreco(calculoServico.getIdServico(), tpTipoTabelaPreco, tpSubtipoTabelaPreco);
	}

	protected void findServicosAdicionais(CalculoServico calculoServico) {
		if(calculoServico.getBlCalculaServicosAdicionais().equals(Boolean.FALSE)) {
			return;
		}
		List<ParcelaServicoAdicional> servicosAdicionais = calculoParcelaServicoService.findParcelasServicoAdicional(calculoServico);
		if(servicosAdicionais != null) {
			for (ParcelaServicoAdicional parcelaServicoAdicional : servicosAdicionais) {
				calculoServico.addServicoAdicional(parcelaServicoAdicional);
			}
		}
	}

	protected void executeCalculoSubTotal(CalculoServico calculoFrete){
		calculoFrete.setVlTotalParcelas();
		calculoFrete.setVlTotalServicosAdicionais();
		calculoFrete.setVlTotal(calculoFrete.getVlTotalParcelas().add(calculoFrete.getVlTotalServicosAdicionais()).subtract(calculoFrete.getVlDesconto()));
		calculoFrete.setVlDevido(calculoFrete.getVlDevido());
	}

	protected CalculoServicoDAO getCalculoServicoDAO() {
		return calculoServicoDAO;
	}

	protected void setCalculoServicoDAO(CalculoServicoDAO calculoServicoDAO) {
		this.calculoServicoDAO = calculoServicoDAO;
	}

	protected CalculoParcelaServicoService getCalculoParcelaServicoService() {
		return calculoParcelaServicoService;
	}
	protected void setCalculoParcelaServicoService(CalculoParcelaServicoService calculoParcelaServicoService) {
		this.calculoParcelaServicoService = calculoParcelaServicoService;
	}

	public CalculoTributoService getCalculoTributoService() {
		return calculoTributoService;
	}

	public void setCalculoTributoService(CalculoTributoService calculoTributoService) {
		this.calculoTributoService = calculoTributoService;
	}

	public DoctoServicoValidateFacade getDoctoServicoValidateFacade() {
		return doctoServicoValidateFacade;
	}

	public void setDoctoServicoValidateFacade(DoctoServicoValidateFacade doctoServicoValidateFacade) {
		this.doctoServicoValidateFacade = doctoServicoValidateFacade;
	}

}