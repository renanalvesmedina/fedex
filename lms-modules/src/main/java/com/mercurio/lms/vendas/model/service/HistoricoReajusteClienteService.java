package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.HistoricoReajusteCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.dao.HistoricoReajusteClienteDAO;

public class HistoricoReajusteClienteService extends CrudService<HistoricoReajusteCliente, Long>{

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage result = getHistoricoReajusteClienteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<TypedFlatMap> toReturn = result.getList();
		for (TypedFlatMap data : toReturn) {
			/** Tabela de Preços Anterior */
			String tpTipoTabelaPreco = data.getString("tabelaPrecoAnterior.tipoTabelaPreco.tpTipoTabelaPreco.value");
			String tpSubtipoTabelaPreco = data.getString("tabelaPrecoAnterior.subtipoTabelaPreco.tpSubtipoTabelaPreco");
			Integer nrVersao = data.getInteger("tabelaPrecoAnterior.tipoTabelaPreco.nrVersao");
			String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
			data.put("tabelaPrecoAnteriorString", tabelaPrecoString);

			/** Tabela de Preços Nova */
			tpTipoTabelaPreco = data.getString("tabelaPrecoNova.tipoTabelaPreco.tpTipoTabelaPreco.value");
			tpSubtipoTabelaPreco = data.getString("tabelaPrecoNova.subtipoTabelaPreco.tpSubtipoTabelaPreco");
			nrVersao = data.getInteger("tabelaPrecoNova.tipoTabelaPreco.nrVersao");
			tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
			data.put("tabelaPrecoNovaString", tabelaPrecoString);
		}
		return result;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getHistoricoReajusteClienteDAO().getRowCount(criteria);
	}

	public Serializable store(HistoricoReajusteCliente historicoReajusteCliente) {
		return getHistoricoReajusteClienteDAO().store(historicoReajusteCliente);
	}

	/**
	 * Gera Histórico de acordo com parametros
	 * @param idTabelaDivisaoCliente
	 * @param idTabelaPrecoAnterior
	 * @param idTabelaPrecoNova
	 * @param pcReajuste
	 * @param tpFormaReajuste
	 */
	public void generateHistoricoReajusteCliente(
			Long idTabelaDivisaoCliente
			,Long idTabelaPrecoAnterior
			,Long idTabelaPrecoNova
			,BigDecimal pcReajuste
			,String tpFormaReajuste) {

		HistoricoReajusteCliente hrc = new HistoricoReajusteCliente();
		hrc.setTpFormaReajuste(new DomainValue(tpFormaReajuste));
		hrc.setDtReajuste(JTDateTimeUtils.getDataAtual());
		hrc.setPcReajuste(pcReajuste);

		TabelaDivisaoCliente tdc = new TabelaDivisaoCliente();
		tdc.setIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		hrc.setTabelaDivisaoCliente(tdc);

		TabelaPreco tabelaPrecoAnterior = new TabelaPreco();
		tabelaPrecoAnterior.setIdTabelaPreco(idTabelaPrecoAnterior);
		hrc.setTabelaPrecoAnterior(tabelaPrecoAnterior);

		TabelaPreco tabelaPrecoNova = new TabelaPreco();
		tabelaPrecoNova.setIdTabelaPreco(idTabelaPrecoNova);
		hrc.setTabelaPrecoNova(tabelaPrecoNova);

		getHistoricoReajusteClienteDAO().store(hrc);
	}

	public List<HistoricoReajusteCliente> findHistoricoReajusteClienteByIdDivisaoCliente(Long idDivisaoCliente) {
		return getHistoricoReajusteClienteDAO().findHistoricoReajusteClienteByIdDivisaoCliente(idDivisaoCliente);
	}
	
	public void setHistoricoReajusteClienteDAO(HistoricoReajusteClienteDAO historicoReajusteClienteDAO) {
		this.setDao(historicoReajusteClienteDAO);
	}
	public HistoricoReajusteClienteDAO getHistoricoReajusteClienteDAO() {
		return (HistoricoReajusteClienteDAO)getDao();
	}
}