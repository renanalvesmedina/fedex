package com.mercurio.lms.vol.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolEquipamentos;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolEquipamentosDAO extends BaseCrudDao<VolEquipamentos, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolEquipamentos.class;
    }
    public ResultSetPage findPaginatedOperadoras(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginatedEquipamentos(criteria);    	    
		
		StringBuffer pj = new StringBuffer()
			.append(" distinct new map( ")
			.append("   EQUI.idEquipamento   as idEquipamento, ")
			.append("   EQUI.dsNumero        as numero, ")
			.append("   MOEQ.dsNome          as modelo_dsModelo, ")			
			.append("   METR.nrFrota         as frota_nrFrota, ")
			.append("   METR.nrIdentificador as frota_nrIdentificador, ")
			.append("   FILI.sgFilial        as filial_sgFilial, ")
			.append("   PESS.nmPessoa        as operadora_nmOperadora, ")
			.append("   TIUS.dsNome          as uso_dsUso, ")
			.append("   EQUI.nmVersao        as versao, ")
			.append("   to_date(trunc(EQUI.dhAtualizacao.value))   as dtAtualizacao, ")
			.append("   sum(case when " )
			.append("		   not REEQ.idRetiradaEqp is null and ") 
			.append("		   REEQ.dhDevolucao.value is null then 1 else 0 end ")
			.append("   ) as retirado ")
			.append(")");
		
		sql.addProjection(pj.toString());
		
		sql.addGroupBy("EQUI.idEquipamento");
		sql.addGroupBy("EQUI.dsNumero");
		sql.addGroupBy("MOEQ.dsNome");
		sql.addGroupBy("METR.nrFrota");
		sql.addGroupBy("METR.nrIdentificador");
		sql.addGroupBy("FILI.sgFilial");
		sql.addGroupBy("PESS.nmPessoa");
		sql.addGroupBy("TIUS.dsNome");
		sql.addGroupBy("EQUI.nmVersao");
		sql.addGroupBy("to_date(trunc(EQUI.dhAtualizacao.value))");
		
		sql.addOrderBy("EQUI.dsNumero");			
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
    			findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }	
    public Integer getRowCountEquipamentos(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginatedEquipamentos(criteria);
		sql.addProjection("COUNT(distinct EQUI.idEquipamento) as rowcount");
		Long i = Long.valueOf(getAdsmHibernateTemplate()
				.find(sql.getSql(),sql.getCriteria()).get(0).toString());		
		return i.intValue();
	} 
    public SqlTemplate createHqlPaginatedEquipamentos(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer from = new StringBuffer()
			.append(VolEquipamentos.class.getName()).append(" as EQUI") 
			.append(" inner join EQUI.filial                  as FILI ")
			.append(" left join EQUI.meioTransporte          as METR ")
			.append(" inner join EQUI.volModeloseqp           as MOEQ ")
			.append(" inner join EQUI.volOperadorasTelefonia  as OPTE ")
			.append(" inner join OPTE.pessoa                  as PESS ")
			.append(" inner join EQUI.volTiposUso             as TIUS ")
			.append(" left outer join EQUI.volRetiradasEqptos as REEQ ")
		;
		
		sql.addFrom(from.toString()); 	   
    	
		sql.addCriteria("EQUI.idEquipamento","=",criteria.getLong("idEquipamento"));
		sql.addCriteria("EQUI.dsNumero","like",criteria.get("numero"));
		sql.addCriteria("EQUI.tpTarifa","=",criteria.get("tarifa"));
		
		sql.addCriteria("FILI.idFilial","=",criteria.getLong("filial.idFilial"));
		sql.addCriteria("MOEQ.idModeloeqp","=",criteria.getLong("volModeloseqp.idModeloeqp"));
		sql.addCriteria("OPTE.idOperadora","=",criteria.getLong("volOperadorasTelefonia.idOperadora"));
		sql.addCriteria("TIUS.idTiposUso","=",criteria.getLong("volTiposUso.idTiposUso"));
		sql.addCriteria("METR.idMeioTransporte","=",criteria.getLong("meioTransporte.idMeioTransporte"));
		
		return sql;    	    	   
	}
    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("meioTransporte", FetchMode.JOIN);
    	lazyFindList.put("volOperadorasTelefonia", FetchMode.JOIN);
    }
    public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("meioTransporte", FetchMode.JOIN);
    	lazyFindById.put("volModeloseqp", FetchMode.JOIN);
    	lazyFindById.put("volOperadorasTelefonia", FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	lazyFindById.put("volOperadorasTelefonia.pessoa", FetchMode.JOIN);
    	lazyFindById.put("volTiposUso", FetchMode.JOIN);    	
    }
    
    public void initFindLookupLazyProperties(Map lazyFindLookup) {
 	   lazyFindLookup.put("meioTransporte",FetchMode.JOIN);
 	   lazyFindLookup.put("filial",FetchMode.JOIN);
    }
    
    public List findEquipamentoByIdMeioTransporte(Long idMeioTransporte, Long idEquipamento) {
    	SqlTemplate sql = this.createHqlFindEquipamentoByIdMeioTransporte(idMeioTransporte, idEquipamento);
    	
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map(")
    		.append("veqto.idEquipamento as idEquipamento, ")
    		.append("veqto.dsNumero as dsNumero ) ");
    		
    	sql.addProjection(projecao.toString());
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
    
    
    private SqlTemplate createHqlFindEquipamentoByIdMeioTransporte(Long idMeioTransporte, Long idEquipamento){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolEquipamentos.class.getName(), "as veqto INNER JOIN veqto.meioTransporte as mt");
    	sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);
    	if (idEquipamento != null) {
        	sql.addCriteria("veqto.idEquipamento", "<>", idEquipamento);
    	}
    	
    	return sql;
    }
    
    /**
     * Retorna o equipamento pelo Imei e filial   
     * @param imei
     * @param idFilial
     * @return
     */
    public Long findIdEquipamentoByImeiEFilial( String imei, long idFilial ){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolEquipamentos.class.getName(), " as equipamento INNER JOIN equipamento.filial as filial");
    	sql.addCriteria("equipamento.dsImei", "=", imei);
    	sql.addCriteria("filial.idFilial", "=", idFilial);
    	
    	StringBuffer projecao = new StringBuffer()
    	.append(" equipamento.idEquipamento ");
		
    	sql.addProjection(projecao.toString());
	
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
    	return result;
    }
    
    
    public VolEquipamentos findEquipamentoByImei( String imei ){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolEquipamentos.class.getName(), " as equipamento ");
    	sql.addCriteria("equipamento.dsImei", "=", imei);
    	
    	StringBuffer projecao = new StringBuffer()
    	.append(" equipamento ");
    	
    	sql.addProjection( projecao.toString() );
    	
    	VolEquipamentos volEquipamentos = (VolEquipamentos)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
    	return volEquipamentos;
    	
    }
    
    public List findMeioTransporte(Long idMeioTransporte) {
    	SqlTemplate sql = this.createHqlFindMeioTransporte(idMeioTransporte);
    	
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map(")
    		.append("mt.nrFrota as nrFrota,")
    		.append("mt.nrIdentificador as nrIdentificador,")
    		.append("veqto.idEquipamento as idEquipamento) ");
    	
    	sql.addProjection(projecao.toString());
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    private SqlTemplate createHqlFindMeioTransporte(Long idMeioTransporte){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolEquipamentos.class.getName(), "as veqto INNER JOIN veqto.meioTransporte as mt");
    	sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);
    	
    	return sql;
    }

}