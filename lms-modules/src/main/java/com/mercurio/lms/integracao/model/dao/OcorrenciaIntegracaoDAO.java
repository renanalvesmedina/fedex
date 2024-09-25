package com.mercurio.lms.integracao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.integracao.model.OcorrenciaIntegracao;

public class OcorrenciaIntegracaoDAO extends BaseCrudDao<OcorrenciaIntegracao, Long> {

	public OcorrenciaIntegracao findById(Long id) {
		return super.findById(id);
	}
	
	public ResultSetPage findPaginated(String layout, String pi, Boolean approve, FindDefinition findDef) {
		return findPaginated(layout, pi, approve, findDef, null, null, null, null);
	}
	
	public ResultSetPage findPaginated(String layout, String pi, Boolean approve, FindDefinition findDef, String filial, Integer codigoDocumento) {
		return findPaginated(layout, pi, approve, findDef, filial, codigoDocumento, null, null);
	}
	public ResultSetPage findPaginated(String layout, String pi, Boolean approve, FindDefinition findDef, String filial, String codigoDocumento) {
		return findPaginatedOB(layout, pi, approve, findDef, filial, codigoDocumento, null, null);
	}
	
	
	/**
	 * Utilizado para carregar os registros de ocorrencia_integracao 
	 * @param layout Layout do registro que está sendo utilizado para buscar
	 * @param pi Nome da PI que está utilizando na busca dos registros
	 * @param approve Status do registro 
	 * @param findDef 
	 * @param filial filial do registro
	 * @param codigoDocumento Numero do documento
	 * @param filial2 Filial do Registro
	 * @param codigoDocumento2 Numero do registro
	 * @return 
	 */
	public ResultSetPage findPaginated(String layout, String pi, Boolean approve, FindDefinition findDef, String filial, Integer codigoDocumento, String filial2, Integer codigoDocumento2) {
		SqlTemplate sql = sqlQuery(layout, pi, approve, filial, codigoDocumento, filial2, codigoDocumento2);
		sql.addOrderBy(" ocorrencias.filial, ocorrencias.codigoDocumento ");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}	
	/**
	 * Utilizado para carregar os registros de ocorrencia_integracao 
	 * @param layout Layout do registro que está sendo utilizado para buscar
	 * @param pi Nome da PI que está utilizando na busca dos registros
	 * @param approve Status do registro 
	 * @param findDef 
	 * @param filial filial do registro
	 * @param codigoDocumento Numero do documento
	 * @param filial2 Filial do Registro
	 * @param codigoDocumento2 Numero do registro
	 * @return 
	 */
	public ResultSetPage findPaginatedOB(String layout, String pi, Boolean approve, FindDefinition findDef, String filial, String codigoDocumento, String filial2, String codigoDocumento2) {
		SqlTemplate sql = sqlQueryOB(layout, pi, approve, filial, codigoDocumento, filial2, codigoDocumento2);
		sql.addOrderBy(" ocorrencias.filial, ocorrencias.codigoDocumento ");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}
	
	/**
	 * Metodo utilizado para contar quantas ocorrencias tem.
	 * @param layout Layout que deve buscar os registros
	 * @param pi PI que esta cadastrado o registro
	 * @param approve Se os registros não estão aprovados
	 * @param filial Filial do documento
	 * @param codigoDocumento 
	 * @param filial2
	 * @param codigoDocumento2
	 * @return Quantidade de registros 
	 */
	public Integer getRowCount(String layout, String pi, Boolean approve, String filial, Integer codigoDocumento, String filial2, Integer codigoDocumento2) {
		SqlTemplate sql = sqlQuery(layout, pi, approve, filial, codigoDocumento, filial2, codigoDocumento2);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
	}
	/**
	 * Metodo utilizado para contar quantas ocorrencias tem.
	 * @param layout Layout que deve buscar os registros
	 * @param pi PI que esta cadastrado o registro
	 * @param approve Se os registros não estão aprovados
	 * @param filial Filial do documento
	 * @param codigoDocumento 
	 * @param filial2
	 * @param codigoDocumento2
	 * @return Quantidade de registros 
	 */
	public Integer getRowCountOB(String layout, String pi, Boolean approve, String filial, String codigoDocumento, String filial2, String codigoDocumento2) {
		SqlTemplate sql = sqlQueryOB(layout, pi, approve, filial, codigoDocumento, filial2, codigoDocumento2);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
	}
	
	/**
	 * Utilizado pra carregar a query que será utilizada nas consultas
	 * @param layout
	 * @param pi
	 * @param approve
	 * @param filial
	 * @param codigoDocumento
	 * @param filial2
	 * @param codigoDocumento2
	 * @return
	 */	
	private SqlTemplate sqlQuery(String layout, String pi, Boolean approve, String filial, Integer codigoDocumento, String filial2, Integer codigoDocumento2) {
		StringBuilder from = new StringBuilder().append(OcorrenciaIntegracao.class.getName()).append(" as ocorrencias ");
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		sql.addCriteria("ocorrencias.layout", "=", layout);
		sql.addCriteria("ocorrencias.pi", "=", pi);
		sql.addCriteria("ocorrencias.approve", "=", approve, Boolean.class);

		if(layout.equals("JDRP")){
			if(filial != null && !filial.trim().equals("")){
				filial = filial.endsWith("%") ? filial.substring(0, filial.length() -1) : filial;
				sql.addCriteria("ocorrencias.codigoDocumento", "=", filial);
			}
		} else {
			if(filial != null && !filial.trim().equals(""))
				sql.addCriteria("ocorrencias.filial", "=", filial);
			
			if(codigoDocumento != null && !codigoDocumento.equals(""))
				sql.addCriteria("ocorrencias.codigoDocumento", "=", String.valueOf(codigoDocumento));
			
			if(filial2 != null && !filial2.trim().equals(""))
				sql.addCriteria("ocorrencias.filial2", "=", filial2);
			
			if(codigoDocumento2 != null && !codigoDocumento2.equals(""))
				sql.addCriteria("ocorrencias.codigoDocumento2", "=", String.valueOf(codigoDocumento2));
		}
		return sql;
	}	
	/**
	 * Utilizado pra carregar a query que será utilizada nas consultas
	 * @param layout
	 * @param pi
	 * @param approve
	 * @param filial
	 * @param codigoDocumento
	 * @param filial2
	 * @param codigoDocumento2
	 * @return
	 */	
	private SqlTemplate sqlQueryOB(String layout, String pi, Boolean approve, String filial, String codigoDocumento, String filial2, String codigoDocumento2) {
		StringBuilder from = new StringBuilder().append(OcorrenciaIntegracao.class.getName()).append(" as ocorrencias ");
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		sql.addCriteria("ocorrencias.layout", "=", layout);
		sql.addCriteria("ocorrencias.pi", "=", pi);
		sql.addCriteria("ocorrencias.approve", "=", approve, Boolean.class);

		if(filial != null && !filial.trim().equals(""))
			sql.addCriteria("ocorrencias.filial", "=", filial);
			
		if(codigoDocumento != null && !codigoDocumento.equals(""))
			sql.addCriteria("ocorrencias.codigoDocumento", "=", codigoDocumento);
			
		if(filial2 != null && !filial2.trim().equals(""))
			sql.addCriteria("ocorrencias.filial2", "=", filial2);
			
		if(codigoDocumento2 != null && !codigoDocumento2.equals(""))
			sql.addCriteria("ocorrencias.codigoDocumento2", "=", String.valueOf(codigoDocumento2));

		return sql;
	}
	
	public void updateOcorrenciaIntegracao(OcorrenciaIntegracao ocorrencia){
		this.getHibernateTemplate().update(ocorrencia);
	}

	@Override
	protected Class getPersistentClass() {
		return OcorrenciaIntegracao.class;
	}
}