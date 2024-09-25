package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;

public class ItemLoteCobrancaTerceiraDAO extends BaseCrudDao<ItemLoteCobrancaTerceira, Long> {

    protected final Class getPersistentClass() {
        return ItemLoteCobrancaTerceira.class;
    }
	
    public List<ItemLoteCobrancaTerceira> findByFaturaAndLoteCobranda(Long idFatura,Long idLoteCobranca){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addFrom(ItemLoteCobrancaTerceira.class.getName(), "i" );
    	hql.addCriteria("i.fatura.idFatura", "=", idFatura);
    	hql.addCriteria("i.loteCobrancaTerceira.idLoteCobrancaTerceira", "=", idLoteCobranca);
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    public List<ItemLoteCobrancaTerceira> findAll(Map criteria) {
    	SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ItemLoteCobrancaTerceira.class.getName(), "i" +
				" JOIN fetch i.loteCobrancaTerceira l " +
				" JOIN fetch i.loteCobrancaTerceira.usuario u " +
				" JOIN fetch i.fatura.filialByIdFilial fi " +
				" JOIN fetch i.fatura f " );
		hql.addOrderBy(" i.loteCobrancaTerceira.nrLote ");
		hql = allCriteria(criteria, hql);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    public void removeByIdLoteCobranca(Long idLoteCobraca){
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(ItemLoteCobrancaTerceira.class.getName()).append(" i")
    	.append(" WHERE i.loteCobrancaTerceira.idLoteCobrancaTerceira = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idLoteCobraca);
    }
    
    private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
		if(containValue(criteria, "id_item_lote_cobranca_terceira"))
			hql.addCriteria("i.idItemLoteCobrancaTerceira", "=", criteria.get("id_item_lote_cobranca_terceira"));

		if(containValue(criteria, "id_fatura")) {
			hql.addCriteria("i.fatura.idFatura", "=", criteria.get("id_fatura"));
		}
		
		if(containValue(criteria, "id_filial")) {
			hql.addCriteria("i.fatura.filialByIdFilial.idFilial", "=", criteria.get("id_filial"));
		}
		
		if(containValue(criteria, "tp_lote_enviado")){
			if("S".equals(((DomainValue)criteria.get("tp_lote_enviado")).getValue())) {
				hql.addCustomCriteria("i.loteCobrancaTerceira.dhEnvio is not null");
			} else {
				hql.addCustomCriteria("i.loteCobrancaTerceira.dhEnvio is null");
			}
		}
		
		if(containValue(criteria, "tp_lote")){
			hql.addCriteria("i.loteCobrancaTerceira.tpLote", "=", criteria.get("tp_lote"));
		}

		if(containValue(criteria, "nr_lote")){
			hql.addCriteria("i.loteCobrancaTerceira.nrLote", "=", criteria.get("nr_lote"));
		}
		
		if(containValue(criteria, "descricao")){
			hql.addCriteria("i.loteCobrancaTerceira.dsLote", "=", criteria.get("descricao"));
		}
		
		if(containValue(criteria, "dt_alteracao_inicial")) {
			hql.addCriteria("TRUNC(i.loteCobrancaTerceira.dhAlteracao.value)",">=", criteria.get("dt_alteracao_inicial"), YearMonthDay.class);
		}

		if(containValue(criteria, "dt_alteracao_final")) {
			hql.addCriteria("TRUNC(i.loteCobrancaTerceira.dhAlteracao.value)","<=", criteria.get("dt_alteracao_final"), YearMonthDay.class);
		}
		
		if(containValue(criteria, "dt_envio_inicial")) {
			hql.addCriteria("TRUNC(i.loteCobrancaTerceira.dhEnvio.value)",">=", criteria.get("dt_envio_inicial"), YearMonthDay.class);
		}

		if(containValue(criteria, "dt_envio_final")) {
			hql.addCriteria("TRUNC(i.loteCobrancaTerceira.dhEnvio.value)","<=", criteria.get("dt_envio_final"), YearMonthDay.class);
		}

		return hql;
	}
    
    public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}
    
    public ItemLoteCobrancaTerceira findLoteCobrancaById(Long id) {
    	SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ItemLoteCobrancaTerceira.class.getName(), "i" +
			" JOIN fetch i.fatura f " );
		hql.addCriteria("i.idItemLoteCobrancaTerceira", "=", id);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (ItemLoteCobrancaTerceira) dados.get(0);
		} else {
			return null;
		}
    }
    
	public ItemLoteCobrancaTerceira store(final ItemLoteCobrancaTerceira item) {
		super.store(item, true);
		return item;
	}

	public void removeFaturasByIds(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM ItemLoteCobrancaTerceira WHERE idItemLoteCobrancaTerceira IN (:id)", ids);
	}

}