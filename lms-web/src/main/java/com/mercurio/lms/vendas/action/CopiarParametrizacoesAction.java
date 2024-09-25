package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;

/**
 * @author Regis de Souza Novais
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.copiarParametrizacoesAction"
 */

public class CopiarParametrizacoesAction extends CrudAction {
	private ParametroClienteService parametroClienteService;
	private ClienteService clienteService;
	private DivisaoClienteService divisaoClienteService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;

	public List findClienteLookup(TypedFlatMap criteria) {
		return clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"));
	}

	public List findDivisaoCombo(TypedFlatMap criteria){
		return divisaoClienteService.findByIdCliente(criteria.getLong("cliente.idCliente"));
	}

	public List findTabelaDivisaoClienteCombo(TypedFlatMap criteria) {
		List result = new ArrayList();
		Long idDivisao = null;
		try {
			idDivisao = criteria.getLong("divisaoCliente.idDivisaoCliente");
		} catch(NumberFormatException nfe) {
			return Collections.EMPTY_LIST;		
		}
		List list = tabelaDivisaoClienteService.findTabelaDivisaoClienteComboByDivisaoWithServico(idDivisao);
		for(Iterator it=list.iterator();it.hasNext();) {
			TabelaDivisaoCliente tdc = (TabelaDivisaoCliente)it.next();
			String aux = tdc.getTabelaPreco().getTabelaPrecoString();
			if(tdc.getTabelaPreco().getDsDescricao()!=null && !tdc.getTabelaPreco().getDsDescricao().toString().equals("")) {
				aux = aux + " - " + tdc.getTabelaPreco().getDsDescricao();
			}
			if(tdc.getServico()!=null && tdc.getServico().getDsServico()!=null && !tdc.getServico().getDsServico().toString().equals("")) {
				aux = aux + " - " + tdc.getServico().getDsServico();
			}
			Map tdcMap = new HashMap();
			tdcMap.put("idTabelaDivisaoCliente",tdc.getIdTabelaDivisaoCliente());

			Map tpMap = new HashMap();
			tpMap.put("tabelaPrecoStringDescricao",aux);
		
			Map mMap = new HashMap();
			mMap.put("sgMoeda",tdc.getTabelaPreco().getMoeda().getSiglaSimbolo());
			mMap.put("dsSimbolo",tdc.getTabelaPreco().getMoeda().getSiglaSimbolo());
			tpMap.put("moeda",mMap);

			Map stpMap = new HashMap();
			stpMap.put("idSubtipoTabelaPreco",tdc.getTabelaPreco().getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
			tpMap.put("subtipoTabelaPreco",stpMap);

			Map ttpMap = new HashMap();
			ttpMap.put("tpTipoTabelaPreco",tdc.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
			tpMap.put("tipoTabelaPreco",ttpMap);
			tdcMap.put("tabelaPreco",tpMap);
			result.add(tdcMap);
		}
		return result;
	}

	public Serializable storeCopiaParametrizacao(TypedFlatMap map) {
		DomainValue tpSituacaoParametro	= new DomainValue("A");
		YearMonthDay dtVigenciaFinal = JTDateTimeUtils.getDataAtual();
		Long idClienteOrigem = map.getLong("clienteOrigem.idCliente");
		Long idClienteDestino = map.getLong("clienteDestino.idCliente");
		Long idDivisaoCliente = map.getLong("divisaoCliente.idDivisaoCliente");
		Long idTabelaDivisaoCliente = map.getLong("tabelaDivisaoCliente.idTabelaDivisaoCliente");

		return parametroClienteService.storeCopiaParametrizacao(tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idClienteDestino, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}

}
