package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ExcecoesClienteFinanceiro;


public class ExcecoesClienteFinanceiroDAO extends BaseCrudDao<ExcecoesClienteFinanceiro, Long> {

	@Override
	protected final Class getPersistentClass() {
		return ExcecoesClienteFinanceiro.class;
	}

	public ExcecoesClienteFinanceiro findExcecaoById(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ExcecoesClienteFinanceiro.class.getName(), "e" +
			" JOIN fetch e.usuario u " +
			" JOIN fetch e.pessoa p " );
		hql.addCriteria("e.idExcecaoClienteFinanceiro", "=", id);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (ExcecoesClienteFinanceiro)dados.get(0);
		} else {
			return null;
		}		
	}

	public List<ExcecoesClienteFinanceiro> findAll(Map criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ExcecoesClienteFinanceiro.class.getName(), "e" +
				" JOIN fetch e.usuario u " +
				" JOIN fetch e.pessoa p " );
		hql = allCriteria(criteria, hql);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
		if(containValue(criteria, "id_excecao_cliente_financeiro"))
			hql.addCriteria("e.idExcecaoClienteFinanceiro", "=", criteria.get("id_excecao_cliente_financeiro"));

		if(containValue(criteria, "id_cliente"))
			hql.addCriteria("e.pessoa.idPessoa", "=", criteria.get("id_cliente"));

		if(containValue(criteria, "tp_cliente"))
			hql.addCriteria("e.tpCliente", "=", criteria.get("tp_cliente"));

		if(containValue(criteria, "tp_envio_serasa"))
			hql.addCriteria("e.tpEnvioSerasa", "=", criteria.get("tp_envio_serasa"));

		if(containValue(criteria, "tp_envio_faturamento"))
			hql.addCriteria("e.tpEnvioFaturamento", "=", criteria.get("tp_envio_faturamento"));

		if(containValue(criteria, "tp_envio_cobranca_terceira"))
			hql.addCriteria("e.tpEnvioCobrancaTerceira", "=", criteria.get("tp_envio_cobranca_terceira"));

		if(containValue(criteria, "tp_envio_carta_cobranca"))
			hql.addCriteria("e.tpEnvioCartaCobranca", "=", criteria.get("tp_envio_carta_cobranca"));

		if ( containValue(criteria, "tp_envio_cobranca_pro_ativa") )
			hql.addCriteria("e.tpEnvioCobrancaTerceiraProAtiva", "=", criteria.get("tp_envio_cobranca_pro_ativa"));
		
		if(containValue(criteria, "periodo_emissao_inicial"))
			hql.addCriteria("TRUNC(e.dhAlteracao.value)",">=", criteria.get("periodo_emissao_inicial"), YearMonthDay.class);

		if(containValue(criteria, "periodo_emissao_final"))
			hql.addCriteria("TRUNC(e.dhAlteracao.value)","<=", criteria.get("periodo_emissao_final"), YearMonthDay.class);

		return hql;
	}
	
	public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}

}