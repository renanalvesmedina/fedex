package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalOperadaDAO extends BaseCrudDao<NotaFiscalOperada, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalOperada.class;
    }
    
    public List<NotaFiscalOperada> findByIdNotaFiscalConhecimentoFinalizada(Long idNotaFiscalConhecimento) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);

        StringBuilder query = new StringBuilder();
        query.append(" select nfo");
        query.append(" from NotaFiscalOperada as nfo, MonitoramentoDocEletronico as mde");
        query.append(" where nfo.notaFiscalConhecimentoOriginal.id = :idNotaFiscalConhecimento ");
        query.append(" and nfo.tpSituacao in ('EN', 'DV', 'RF') ");
        query.append(" and nfo.doctoServico.id = mde.doctoServico.id ");
        query.append(" and mde.tpSituacaoDocumento = 'A' ");
        
        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    public List<NotaFiscalOperada> findNotaDevolvidaOuRefatorada(Long idDoctoServico) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idDoctoServico", idDoctoServico);

        StringBuilder query = new StringBuilder();
        query.append(" select nfo");
        query.append(" from NotaFiscalOperada as nfo ");
        query.append(" where nfo.doctoServico.id = :idDoctoServico ");
        query.append(" and nfo.tpSituacao in ('DV','RF','RE') ");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    public List<NotaFiscalOperada> findByIdNotaFiscalConhecimentoidDoctoServicoTpSituacao(Long idNotaFiscalConhecimento, Long idDoctoServico, String tpSituacao) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);
        parametersValues.put("idDoctoServico", idDoctoServico);
        parametersValues.put("tpSituacao", tpSituacao);

        StringBuilder query = new StringBuilder();
        query.append(" select nfo");
        query.append(" from NotaFiscalOperada as nfo ");
        query.append(" where nfo.notaFiscalConhecimentoOriginal.id = :idNotaFiscalConhecimento ");
        query.append(" and nfo.doctoServico.id = :idDoctoServico ");
        query.append(" and nfo.tpSituacao = :tpSituacao ");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    public boolean validateExistePreManifestoDocumentoPreManifestoVolume(Long idDoctoServico, Long idVolumeNotaFiscal) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("idDoctoServico", idDoctoServico);
        params.put("idVolumeNotaFiscal", idVolumeNotaFiscal);
    
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1 ");
        sql.append(" FROM NOTA_FISCAL_OPERADA NFO ");
        sql.append(" JOIN NOTA_FISCAL_CONHECIMENTO NFC on NFO.ID_NOTA_FISCAL_CTO_ORIGINAL = NFC.ID_NOTA_FISCAL_CONHECIMENTO ");
        sql.append(" JOIN VOLUME_NOTA_FISCAL VNF on NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO ");
        sql.append(" WHERE VNF.ID_VOLUME_NOTA_FISCAL = :idVolumeNotaFiscal ");
        sql.append(" AND  NFC.ID_CONHECIMENTO = :idDoctoServico ");
        sql.append(" AND  NFO.TP_SITUACAO <> 'RE' ");
        sql.append(" AND ROWNUM = 1 ");
        
        return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params) > 0;
    }

    
    public List<NotaFiscalOperada> findByIdDoctoServico(Long idDoctoServico) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idDoctoServico", idDoctoServico);

        StringBuilder query = new StringBuilder();
        query.append(" select nfo");
        query.append(" from NotaFiscalOperada as nfo ");
        query.append(" where nfo.doctoServico.id = :idDoctoServico ");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    @Override		
	public Integer getRowCount(Map criteria) {				
		return getAdsmHibernateTemplate().getRowCountForQuery(this.getHqlPaginated(criteria), criteria);
	}
    
    public String getHqlPaginated(Map<String,Object> criteria) {
		StringBuilder hql = new StringBuilder();
		hql.append("from " + NotaFiscalOperada.class.getName() + " as nfo ");
    	hql.append("left join fetch nfo.notaFiscalConhecimentoOriginal as nfc ");
		hql.append("where 1=1 ");
		
		if(criteria.get("idNotaFiscalConhecimento") != null) {
			hql.append("and nfc.id = :idNotaFiscalConhecimento ");			
		}
		return hql.toString();
	}
    
    public ResultSetPage<Map<String, Object>> findPaginatedBySql(PaginatedQuery paginatedQuery) {    	
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("TIPO_DOCUMENTO", Hibernate.STRING);
      			sqlQuery.addScalar("TP_SITUACAO", Hibernate.STRING);
    			sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
    			sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
    		}
    	};    	
    	
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(this.getSqlPaginated(), paginatedQuery.getCurrentPage(), 
														paginatedQuery.getPageSize(), paginatedQuery.getCriteria(), csq);
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Object[] obj : (List<Object[]>)rsp.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tpDocumento", obj[0]);
			map.put("tpSituacao", obj[1]);
			map.put("sgFilial", obj[2]);
			map.put("nrDoctoServico", obj[3]);
			list.add(map);
		}
		rsp.setList(list);
		
		return rsp;
	}
    
    private String getSqlPaginated(){
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT  ");
        sql.append("    (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("       FROM valor_dominio");
        sql.append("      WHERE id_dominio IN");
        sql.append("            (SELECT id_dominio");
        sql.append("               FROM dominio");
        sql.append("              WHERE nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO')");
        sql.append("        AND vl_valor_dominio = ds.tp_documento_servico) as TIPO_DOCUMENTO,");
    	sql.append(" (select VI18N(VM.DS_VALOR_DOMINIO_I) ");
    	sql.append("   from VALOR_DOMINIO VM ");
    	sql.append("   inner join DOMINIO DM on DM.ID_DOMINIO = VM.ID_DOMINIO ");
    	sql.append("   where DM.NM_DOMINIO = 'DM_SITUACAO_NOTA_FISCAL_OPERADA' ");
    	sql.append("   and VM.VL_VALOR_DOMINIO = nfo.tp_situacao	 ");
    	sql.append(" 	) as TP_SITUACAO, ");
    	sql.append(" fo.sg_filial as SG_FILIAL, ");
    	sql.append(" ds.nr_docto_servico as NR_DOCTO_SERVICO ");
    	sql.append(" FROM nota_fiscal_operada nfo ");
    	sql.append(" JOIN docto_servico ds ON nfo.id_docto_servico = ds.id_docto_servico ");
    	sql.append(" JOIN filial fo ON ds.id_filial_origem = fo.id_filial ");
    	sql.append(" WHERE 1 = 1 ");
    	sql.append(" AND nfo.id_nota_fiscal_cto_original = :idNotaFiscalConhecimento ");
    	sql.append("ORDER BY ds.id_docto_servico");
    	return sql.toString();
    }

	public void removeNotasFinalizadasByIdNotaFiscalConhecimento(
			Long idNotaFiscalConhecimento) {
		
		String deleteSql = "delete NOTA_FISCAL_OPERADA where ID_NOTA_FISCAL_CTO_ORIGINAL = :idNotaFiscalConhecimento"
				+ " and TP_SITUACAO = 'EN'";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);
		getAdsmHibernateTemplate().executeUpdateBySql(deleteSql, param);
		
	}
	
	public void removeByIdDoctoServico(Long idDoctoServico) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idDoctoServico", idDoctoServico);

		String sql = "DELETE nota_fiscal_operada WHERE id_docto_servico = :idDoctoServico";
		getAdsmHibernateTemplate().executeUpdateBySql(sql, param);
	}
}