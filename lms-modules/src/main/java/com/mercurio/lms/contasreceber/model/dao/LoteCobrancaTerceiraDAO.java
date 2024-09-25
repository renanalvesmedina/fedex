package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;

public class LoteCobrancaTerceiraDAO extends BaseCrudDao<LoteCobrancaTerceira, Long> {

    protected final Class getPersistentClass() {
        return LoteCobrancaTerceira.class;
    }
	
    public List<LoteCobrancaTerceira> findAll(Map criteria) {
    	SqlTemplate hql = new SqlTemplate();
		hql.addFrom(LoteCobrancaTerceira.class.getName(), "l" +
				" JOIN fetch l.usuario u "+
				" join fetch u.usuarioADSM ua " );
		hql.addOrderBy(" l.nrLote ");
		hql = allCriteria(criteria, hql);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    
    private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
    	if(containValue(criteria, "tp_lote_enviado")){
    		if("S".equals(((DomainValue)criteria.get("tp_lote_enviado")).getValue())) {
    			hql.addCustomCriteria("l.dhEnvio is not null");
    		} else {
    			hql.addCustomCriteria("l.dhEnvio is null");
    		}
    	}

    	if(containValue(criteria, "tp_lote")){
    		hql.addCriteria("l.tpLote", "=", criteria.get("tp_lote"));
    	}

    	if(containValue(criteria, "nr_lote")){
    		hql.addCriteria("l.nrLote", "=", criteria.get("nr_lote"));
    	}

    	if(containValue(criteria, "descricao")){
    		hql.addCriteria("l.dsLote", "like", "%" + criteria.get("descricao") + "%");
    	}

    	if(containValue(criteria, "dt_alteracao_inicial")) {
    		hql.addCriteria("TRUNC(l.dhAlteracao.value)",">=", criteria.get("dt_alteracao_inicial"), YearMonthDay.class);
    	}

    	if(containValue(criteria, "dt_alteracao_final")) {
    		hql.addCriteria("TRUNC(l.dhAlteracao.value)","<=", criteria.get("dt_alteracao_final"), YearMonthDay.class);
    	}

    	if(containValue(criteria, "dt_envio_inicial")) {
    		hql.addCriteria("TRUNC(l.dhEnvio.value)",">=", criteria.get("dt_envio_inicial"), YearMonthDay.class);
    	}

    	if(containValue(criteria, "dt_envio_final")) {
    		hql.addCriteria("TRUNC(l.dhEnvio.value)","<=", criteria.get("dt_envio_final"), YearMonthDay.class);
    	}

		return hql;
	}
    
    public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}
    
    public LoteCobrancaTerceira findById(Long idLoteCobrancaTerceira) {
		return findLoteCobrancaById(idLoteCobrancaTerceira);
	}
    
    public LoteCobrancaTerceira findLoteCobrancaById(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(LoteCobrancaTerceira.class.getName(), "l" +
			" JOIN fetch l.usuario u " +
			" join fetch u.usuarioADSM ua " );

		hql.addCriteria("l.idLoteCobrancaTerceira", "=", id);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (LoteCobrancaTerceira) dados.get(0);
		} else {
			return null;
		}		
	}
    
    public LoteCobrancaTerceira storeLoteCobranca(final LoteCobrancaTerceira loteCobranca) {
		super.store(loteCobranca, true);
		return loteCobranca;
	}
	
	private ConfigureSqlQuery getConfigureSqlQuery() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_fatura", Hibernate.LONG);
				sqlQuery.addScalar("cpnjempresa", Hibernate.STRING);
				sqlQuery.addScalar("nm_empresa", Hibernate.STRING);
				sqlQuery.addScalar("cnpjcliente", Hibernate.STRING);
				sqlQuery.addScalar("nm_devedor", Hibernate.STRING);
				sqlQuery.addScalar("endereco_devedor", Hibernate.STRING);
				sqlQuery.addScalar("bairro_devedor", Hibernate.STRING);
				sqlQuery.addScalar("cidade_devedor", Hibernate.STRING);
				sqlQuery.addScalar("estado_devedor", Hibernate.STRING);
				sqlQuery.addScalar("cep_devedor", Hibernate.STRING);
				sqlQuery.addScalar("ddd_devedor", Hibernate.STRING);
				sqlQuery.addScalar("telefone_devedor", Hibernate.STRING);
				sqlQuery.addScalar("especie_titulo", Hibernate.STRING);
				sqlQuery.addScalar("numero_documento", Hibernate.STRING);
				sqlQuery.addScalar("numero_parcela", Hibernate.STRING);
				sqlQuery.addScalar("data_emissao", Hibernate.STRING);
				sqlQuery.addScalar("data_vencimento", Hibernate.STRING);
				sqlQuery.addScalar("valor_titulo", Hibernate.STRING);
				sqlQuery.addScalar("valor_protesto", Hibernate.STRING);
				sqlQuery.addScalar("observacao", Hibernate.STRING);
				sqlQuery.addScalar("nome_representante", Hibernate.STRING);
				sqlQuery.addScalar("fone_representante", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	public List<Object[]> geraArquivoLoteCobrancaTerceira(Long idLoteCobranca){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT f.id_fatura,pf.nr_identificacao AS cpnjempresa,");
		sql.append("       REPLACE(f_int_formatastr(pf.nm_pessoa, 50, 'D', ' '), '''', ' ') AS nm_empresa,");
		sql.append("       f_int_formatastr(p.nr_identificacao,14,'D',' ') AS cnpjcliente,");
		sql.append("       REPLACE(f_int_formatastr(p.nm_pessoa, 50, 'D', ' '), '''', ' ') AS nm_devedor,");
		sql.append("       REPLACE(f_int_formatastr(nvl(vi18n(tl.ds_tipo_logradouro_i), '') || ' ' || nvl(e.ds_endereco, '') || ', ' || nvl(e.nr_endereco, '') || decode(e.ds_complemento, NULL, '', '/') || nvl(e.ds_complemento, ''), 50, 'D', ' '), '''', ' ') AS endereco_devedor,");
		sql.append("       REPLACE(f_int_formatastr(e.ds_bairro,20,'D',' '), '''', ' ') bairro_devedor,");
		sql.append("       REPLACE(f_int_formatastr(m.nm_municipio,20,'D',' '), '''', ' ') cidade_devedor,");
		sql.append("       uf.sg_unidade_federativa AS estado_devedor,");
		sql.append("       e.nr_cep AS cep_devedor,");
		sql.append("  f_int_formatastr((SELECT nr_ddd");
		sql.append("   FROM telefone_endereco te");	
		sql.append("   WHERE te.id_endereco_pessoa = p.id_endereco_pessoa");
		sql.append("     AND te.tp_uso = 'FO'");
		sql.append("     AND nr_telefone IS NOT NULL");
		sql.append("     AND rownum = 1),3,'D',' ') AS ddd_devedor,");
		sql.append("   f_int_formatastr(regexp_replace(");
		sql.append("                 (SELECT NVL(nr_telefone,'        ') ");
		sql.append("                  FROM telefone_endereco te");
		sql.append("                  WHERE te.id_endereco_pessoa = p.id_endereco_pessoa");
		sql.append("                    AND te.tp_uso = 'FO'");
		sql.append("                    AND nr_telefone IS NOT NULL");
		sql.append("                    AND rownum=1),'[^0-9]',''),8,'D',' ') AS telefone_devedor,");
		sql.append("       'BL' AS especie_titulo,");
		sql.append("       REPLACE(f_int_formatastr(fg.ds_siglaanterior || f.nr_fatura,12,'D',' '), '''', ' ') AS numero_documento,");
		sql.append("       '00' AS numero_parcela,");
		sql.append("       to_char(f.dt_emissao,'dd/MM/yyyy') AS data_emissao,");
		sql.append("       to_char(f.dt_vencimento,'dd/MM/yyyy') AS data_vencimento,");
		sql.append("       f_int_formatastr(replace(TO_CHAR(TRIM(TO_CHAR(GREATEST(f.vl_total -");
		sql.append("                                               nvl((SELECT sum(vl_pagamento)");
		sql.append("                                                FROM relacao_pagto_parcial rpp");
		sql.append("                                                WHERE rpp.id_fatura = f.id_fatura),0) - f.vl_desconto,0.0), '9999999999999990D00')), '9999999999999990D00'),',','.'),15,'E','0') AS valor_titulo,");
		sql.append("       '000000000000.00' AS valor_protesto,");
		sql.append("       f_int_formatastr('',255,'D',' ') AS observacao,");
		sql.append("       f_int_formatastr('',40,'D',' ') AS nome_representante,");
		sql.append("       f_int_formatastr('',12,'D',' ') AS fone_representante");
		sql.append(" FROM ITEM_LOTE_COBRANCA_TERCEIRA tlt,");
		sql.append("     fatura f,");
		sql.append("     pessoa p,");
		sql.append("     pessoa pf,");
		sql.append("     ENDERECO_PESSOA e,");
		sql.append("     tipo_logradouro tl,");
		sql.append("     municipio m,");
		sql.append("     unidade_federativa uf,");
		sql.append("     filial_sigla fg");
		sql.append(" WHERE tlt.id_Fatura = f.id_fatura");
		sql.append("  AND f.id_cliente = p.id_pessoa");
		sql.append("  AND f.id_filial = pf.id_pessoa");
		sql.append("  AND e.id_endereco_pessoa(+) = p.id_endereco_pessoa");
		sql.append("  AND tl.id_tipo_logradouro(+) = e.id_tipo_logradouro");
		sql.append("  AND m.id_municipio(+) = e.id_municipio");
		sql.append("  AND uf.id_unidade_federativa(+) = m.id_unidade_federativa");
		sql.append("  AND fg.id_filial_lms(+) = f.id_filial");
		sql.append("  AND tlt.id_lote_cobranca_terceira = "+idLoteCobranca);
		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, getConfigureSqlQuery());
	}

}