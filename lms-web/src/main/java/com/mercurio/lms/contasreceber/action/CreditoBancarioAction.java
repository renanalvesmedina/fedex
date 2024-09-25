package com.mercurio.lms.contasreceber.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.contasreceber.model.service.CreditoBancarioService;
import com.mercurio.lms.municipios.model.service.FilialService;

public class CreditoBancarioAction extends CrudAction {

	private BancoService bancoService;
	private CreditoBancarioService creditoBancarioService;
	private FilialService filialService;
	private DomainValueService domainValueService;

	public List findLookupBanco(TypedFlatMap tfm) {
		return bancoService.findLookup(tfm);
	}

	public BancoService getBancoService() {
		return bancoService;
	}

	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}

	public ResultSetPage findPaginatedCreditoBancario(TypedFlatMap criteria) {
		Map<String, Object> map = criteriaToMapConverter(criteria);

		
		Integer currentPage = criteria.getInteger("_currentPage");
		Integer pageSize = criteria.getInteger("_pageSize");

		return creditoBancarioService.findPaginated(map, currentPage, pageSize);
	}

	public Integer getRowCountCreditoBancario(TypedFlatMap criteria) {
		Map<String, Object> map = criteriaToMapConverter(criteria);

		return creditoBancarioService.findCount(map);
	}
	
	public Integer getRowCountCreditoBancarioLote(TypedFlatMap criteria) {
		Map<String, Object> map = criteriaToMapConverter(criteria);

		return creditoBancarioService.findCountLote(map);
	}

	public ResultSetPage findPaginatedCreditoBancarioLote(TypedFlatMap criteria) {
		Map<String, Object> map = criteriaToMapConverter(criteria);
		Integer currentPage = criteria.getInteger("_currentPage");
		Integer pageSize = criteria.getInteger("_pageSize");

		return creditoBancarioService.findPaginatedAba(map, currentPage, pageSize);
	}

	public Map executeAlocarCreditos(TypedFlatMap params) {
		Map<String, Object> map = criteriaToMapConverter(params);
		
		return creditoBancarioService.executeAlocarCreditos(map);
		
	}

	private Map<String, Object> criteriaToMapConverter(TypedFlatMap criteria) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("idFilial", criteria.getLong("filial.idFilial"));
		map.put("idBanco", criteria.getLong("idBanco"));
		map.put("tpModalidade", null == criteria.getDomainValue("modalidade") || criteria.getDomainValue("modalidade").getValue().isEmpty() ? null : criteria.getDomainValue("modalidade"));
		map.put("tpOrigem", null == criteria.getDomainValue("origemRegistro") || criteria.getDomainValue("origemRegistro").getValue().isEmpty() ? null : criteria.getDomainValue("origemRegistro"));
		map.put("tpSituacao", null != criteria.get("tpSituacao") ? addTpSituacaoCriteria(criteria.get("tpSituacao").toString()) : null);
		map.put("dsCpfCnpj", criteria.getString("cpfCnpj"));
		map.put("dsNomeRazaoSocial", criteria.getString("nomeRazaoSocial"));
		map.put("dsBoleto", criteria.getString("nrBoleto"));
		map.put("obCreditoBancario", criteria.getString("observacoes"));
		map.put("vlCreditoInicial", criteria.getBigDecimal("vlCreditoInicial"));
		map.put("vlCreditoFinal", criteria.getBigDecimal("vlCreditoFinal"));
		map.put("vlSaldoInicial", criteria.getBigDecimal("vlSaldoInicial"));
		map.put("vlSaldoFinal", criteria.getBigDecimal("vlSaldoFinal"));
		map.put("dataAlteracaoInicial", criteria.getYearMonthDay("dataAlteracaoInicial"));
		map.put("dataAlteracaoFinal", criteria.getYearMonthDay("dataAlteracaoFinal"));
		map.put("dataCreditoInicial", criteria.getYearMonthDay("dataCreditoInicial"));
		map.put("dataCreditoFinal", criteria.getYearMonthDay("dataCreditoFinal"));
		map.put("idRedeco", criteria.getString("idRedeco"));
		
		return map;
	}

	private List<DomainValue> addTpSituacaoCriteria(String tpSituacoes) {
		if (tpSituacoes == null || tpSituacoes.isEmpty()) {
			return null;
		}
		
		List<String> situacoesValidas = Arrays.asList(tpSituacoes.split(","));		

		
		List<DomainValue> findDomainValues = domainValueService.findDomainValues("DM_TP_SITUACAO_CRED_BANC");
		List<DomainValue> validsValues = new ArrayList<DomainValue>();
		for (DomainValue dv: findDomainValues) {
			if (situacoesValidas.contains(dv.getValue())) {
				validsValues.add(dv);
			}
		}
		
		return validsValues;
	}

	public CreditoBancarioService getCreditoBancarioService() {
		return creditoBancarioService;
	}

	public void setCreditoBancarioService(CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
