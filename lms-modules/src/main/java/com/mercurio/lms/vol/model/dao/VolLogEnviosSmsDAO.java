package com.mercurio.lms.vol.model.dao;


import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolLogEnviosSms;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolLogEnviosSmsDAO extends BaseCrudDao<VolLogEnviosSms, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolLogEnviosSms.class;
    }

    public ResultSetPage findPaginatedMeioTransporte(TypedFlatMap criteria,FindDefinition findDef) {
	    SqlTemplate sql = mountSql(criteria);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("new Map(fi.sgFilial as sgFilial,");
    	sb.append("eq.idEquipamento as idEquipamento,");
    	sb.append("gf.dsNome as dsNome,");
    	sb.append("mt.nrIdentificador as nrIdentificador,");
		sb.append("mt.nrFrota as nrFrota,");
    	sb.append("eq.dsNumero as dsNumero)");
    	
    	sql.addOrderBy("mt.nrFrota");
    	sql.addProjection(sb.toString());
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }

        
    public Integer getRowCountMeioTransporte(TypedFlatMap criteria) {
    	SqlTemplate sql = mountSql(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
    
    private SqlTemplate mountSql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolEquipamentos eq ");
		sb.append("inner join eq.filial fi ");
		sb.append("inner join eq.meioTransporte mt ");
		sb.append("left outer join mt.volGrfsVeiculos gv ");
		sb.append("left outer join gv.volGruposFrota gf ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("fi.idFilial","=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("gf.idGrupoFrota","=", criteria.getLong("grupo.idGrupoFrota"));
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		
		return sql;
	}

    public List findMeioTransporte(TypedFlatMap criteria) {
	    SqlTemplate sql = mountSql(criteria);

    	StringBuffer sb = new StringBuffer();
    	sb.append("eq.idEquipamento as idEquipamento");
    	sql.addProjection(sb.toString());
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }

    public List findByIdEquipamento(java.lang.Long id) {
        SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolEquipamentos eq ");
		sb.append("inner join eq.filial fi ");
		sb.append("inner join fi.pessoa pes ");
		sb.append("inner join eq.meioTransporte mt ");
		sb.append("left outer join mt.volGrfsVeiculos gv ");
		sb.append("left outer join gv.volGruposFrota gf ");
		
		sql.addFrom(sb.toString());
		sql.addCriteria("eq.idEquipamento","=",id);
		
    	StringBuffer sbf= new StringBuffer();
    	sbf.append("new Map(fi.sgFilial as filial_sgFilial,");
    	sbf.append("fi.idFilial as filial_idFilial,");
    	sbf.append("pes.nmPessoa as filial_pessoa_nmFantasia,");
    	sbf.append("eq.idEquipamento as idEquipamento,");
    	sbf.append("gf.dsNome as grupo_dsNome,");
    	sbf.append("gf.idGrupoFrota as grupo_idGrupoFrota,");
    	sbf.append("mt.nrIdentificador as meioTransporte_nrIdentificador,");
		sbf.append("mt.nrFrota as meioTransporte_nrFrota,");
		sbf.append("mt.idMeioTransporte as meioTransporte_idMeioTransporte)");
    
    	sql.addProjection(sbf.toString());
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
    
    public ResultSetPage findPaginatedHistorico(TypedFlatMap criteria,FindDefinition findDef) {
	    SqlTemplate sql = mountSqlHistorico(criteria);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("new Map(fi.sgFilial as sgFilial,");
    	sb.append("eq.idEquipamento as idEquipamento,");
    	sb.append("gf.dsNome as dsNome,");
    	sb.append("mt.nrIdentificador as nrIdentificador,");
		sb.append("mt.nrFrota as nrFrota,");
		sb.append("vlog.idLogEnvioSms as idLogEnvioSms,");
		sb.append("vlog.obMensagem as obMensagem,");
		sb.append("vlog.dhEnvio as dhEnvio,");
    	sb.append("eq.dsNumero as dsNumero)");
    	
    	sql.addOrderBy("mt.nrFrota");
    	sql.addOrderBy("vlog.dhEnvio.value");
    	sql.addProjection(sb.toString());
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }

        
    public Integer getRowCountHistorico(TypedFlatMap criteria) {
    	SqlTemplate sql = mountSqlHistorico(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
    
    private SqlTemplate mountSqlHistorico(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolLogEnviosSms vlog ");
		sb.append("inner join vlog.volEquipamento eq ");
		sb.append("inner join eq.filial fi ");
		sb.append("inner join eq.meioTransporte mt ");
		sb.append("left outer join mt.volGrfsVeiculos gv ");
		sb.append("left outer join gv.volGruposFrota gf ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("fi.idFilial","=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("gf.idGrupoFrota","=", criteria.getLong("grupo.idGrupoFrota"));
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		
        sql.addCriteria("vlog.dhEnvio.value",">=", criteria.getYearMonthDay("dhEnvioInicial"));
		
		if (criteria.getYearMonthDay("dhEnvioInicial") != null) {
			sql.addCriteria("vlog.dhEnvio.value","<=", JTDateTimeUtils.createWithMaxTime(criteria.getYearMonthDay("dhEnvioFinal")));	
		}
		return sql;
	}
    /**
     * Atualiza chamados pendentes do celular
     * @param idEquipamento
     */
    public void updatePendentes(Long idEquipamento){
    	/*TODO acertar o SQL abaixo pois dessa maneira não funciona, foi feito assim temporariamente para o 
    	 * 	brisosalara poder dar treinamento
    	 */
    /*	StringBuffer sql = new StringBuffer()
    		.append(" update " + VolLogEnviosSms.class.getName() + " set ")
    		.append(" dhRetorno = sysdate, ")
    		.append(" blTimeout = case when ((sysdate - round(dhEnvio,'mi')) * 24 * 60)>30 then 'S' else 'N' end")
    		.append(" where tpEvento = 'C' ")
    		.append(" and dhRetorno.value is null ")
    		.append(" and volEquipamento.id = ?")
    	;*/
    	/*List param = new ArrayList();
    	param.add(idEquipamento);    	
    	Integer registrosAfetados = super.executeHql(sql.toString(),param);    	
    	
    	if (registrosAfetados.intValue()<1){
    		//CASO NÃO ATUALIZE REGISTROS, INSERE
    		VolLogEnviosSms bean = new VolLogEnviosSms();
    		VolEquipamentos equi = new VolEquipamentos();
    		equi.setIdEquipamento(idEquipamento);
    		
    		bean.setVolEquipamento(equi);
    		bean.setTpEvento(new DomainValue("C"));
    		bean.setBlTimeout(false);
    		bean.setObMensagem("Sincronização");
    		bean.setDhEnvio(new DateTime());
    		bean.setDhRetorno(bean.getDhEnvio());

    		this.store(bean);
    	}*/
    }
    
    public DateTime findDhEnvioByMeioTransporte(Long idMeioTransporte) {
        SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolLogEnviosSms vlog ");
		sb.append("inner join vlog.volEquipamento eq ");
		sb.append("inner join eq.meioTransporte mt ");
	
		sql.addFrom(sb.toString());
		sql.addProjection("vlog.dhEnvio");
		
		sql.addCriteria("mt.idMeioTransporte","=", idMeioTransporte);
		sql.addCustomCriteria("vlog.dhEnvio.value = vlog.dhRetorno.value");
		
		return (DateTime) getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0);
    }
    
    public List findDhEnvioColetaByMeioTransporte(Long idMeioTransporte) {
        SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolLogEnviosSms vlog ");
		sb.append("inner join vlog.volEquipamento eq ");
		sb.append("inner join eq.meioTransporte mt ");
	
		sql.addFrom(sb.toString());
		sql.addProjection("vlog.dhEnvio");
		
		sql.addCriteria("mt.idMeioTransporte","=", idMeioTransporte);
		sql.addCriteria("vlog.tpEvento","=","C");
		sql.addCustomCriteria("vlog.dhRetorno.value is null");
		sql.addOrderBy("vlog.dhEnvio.value");
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
}