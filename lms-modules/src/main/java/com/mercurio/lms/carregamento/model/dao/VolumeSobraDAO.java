package com.mercurio.lms.carregamento.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.carregamento.model.VolumeSobra;

public class VolumeSobraDAO extends BaseCrudDao<VolumeSobra, Long> {

	protected final Class getPersistentClass() {
		return VolumeSobra.class;
	}
	
	public List findVolumesSobraByIdControleCarga(Long idControleCarga) {
		String sql = createSql();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, parameters, configureQuery());
	 }

	private String createSql() {
		  StringBuilder sql = new StringBuilder();

		  sql.append(" SELECT  ");
		  sql.append("  docto_servico.id_docto_servico as idDocumentoServico, ");
		  sql.append("  docto_servico.tp_documento_servico as tpDocumentoServico, ");
		  sql.append("  filial.sg_filial as sgFilial, ");
		  sql.append("  docto_servico.nr_docto_servico as nrDoctoServico, ");
		  sql.append("  volume_nota_fiscal.nr_sequencia as nrSequencia, ");
		  sql.append("  nota_fiscal_conhecimento.qt_volumes as qtVolumes ");
		  sql.append(" FROM ");
		  sql.append(" carregamento_descarga, ");
		  sql.append(" volume_sobra, ");
		  sql.append(" volume_nota_fiscal, ");
		  sql.append(" nota_fiscal_conhecimento, ");
		  sql.append(" docto_servico, ");
		  sql.append(" filial ");
		  sql.append(" WHERE ");
		  sql.append(" carregamento_descarga.tp_operacao = 'D' ");
		  sql.append(" AND carregamento_descarga.id_controle_carga = :idControleCarga ");
		  sql.append(" AND volume_sobra.id_carregamento_descarga = carregamento_descarga.id_carregamento_descarga ");
		  sql.append(" AND volume_sobra.id_volume_nota_fiscal = volume_nota_fiscal.id_volume_nota_fiscal ");
		  sql.append(" AND volume_nota_fiscal.id_nota_fiscal_conhecimento = nota_fiscal_conhecimento.id_nota_fiscal_conhecimento ");
		  sql.append(" AND nota_fiscal_conhecimento.id_conhecimento = docto_servico.id_docto_servico ");
		  sql.append(" AND docto_servico.id_filial_origem = filial.id_filial ");
		  sql.append(" AND NOT EXISTS ( " + createSubquery() + "  ) ");
		  
		  return sql.toString();
		 }


		private String createSubquery() {
			StringBuilder sql = new StringBuilder();
			
			sql.append(" SELECT 1 FROM nao_conformidade ncon, ocorrencia_nao_conformidade ocnc ");
			sql.append(" WHERE ncon.id_nao_conformidade = ocnc.id_nao_conformidade ");
			sql.append(" AND ncon.id_docto_servico = nota_fiscal_conhecimento.id_conhecimento ");
			sql.append(" AND ocnc.tp_status_ocorrencia_nc = 'A' ");
			sql.append(" AND ocnc.id_motivo_abertura_nc = 14 ");
			  
			return sql.toString();
		}
		
		private ConfigureSqlQuery configureQuery() {
			return new ConfigureSqlQuery() {
			  	public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("idDocumentoServico", Hibernate.LONG);
					sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
					sqlQuery.addScalar("sgFilial", Hibernate.STRING);    
					sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);   
					sqlQuery.addScalar("nrSequencia", Hibernate.INTEGER);   
					sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER); 
			   }   
			  };
			 }

}