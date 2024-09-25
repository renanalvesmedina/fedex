package com.mercurio.lms.vol.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolRetiradasEqptos;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolRetiradasEqptosDAO extends BaseCrudDao<VolRetiradasEqptos, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolRetiradasEqptos.class;
    }

    public ResultSetPage findPaginatedControleEquipamentos(TypedFlatMap criteria,FindDefinition findDef) {
    	SqlTemplate sql = mountSql(criteria);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("new Map(re.idRetiradaEqp as idRetiradaEqp,");
    	sb.append("fi.sgFilial as sgFilial,");
    	sb.append("gf.dsNome as dsNome,");
    	sb.append("mt.nrIdentificador as nrIdentificador,");
		sb.append("mt.nrFrota as nrFrota,");   	
    	sb.append("me.dsNome as dsModelo,");
    	sb.append("eq.dsNumero as dsNumero,");
    	sb.append("moto.blTermoComp as assinouTermo,");
    	sb.append("pes.nrIdentificacao as nrIdentificacao,");
    	sb.append("pes.nmPessoa as nmPessoa,");
    	sb.append("re.dhRetirada as dhRetirada,");
    	sb.append("re.dhDevolucao as dhDevolucao) ");
    	
    	sql.addOrderBy("gf.dsNome");
    	sql.addOrderBy("re.dhRetirada.value desc");
    	
    	sql.addProjection(sb.toString());
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }

    public Integer getRowCountControleEquipamentos(TypedFlatMap criteria) {
    	SqlTemplate sql = mountSql(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
    
    private SqlTemplate mountSql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolRetiradasEqptos re ");
		sb.append("inner join re.volEquipamento eq ");
		sb.append("inner join eq.filial fi ");
		sb.append("inner join re.meioTransporte mt ");
		sb.append("left outer join eq.volModeloseqp me ");
		sb.append("left outer join mt.volGrfsVeiculos gv ");
		sb.append("left outer join gv.volGruposFrota gf ");
		sb.append("left outer join re.motorista moto ");
		sb.append("inner join moto.pessoa pes ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("fi.idFilial","=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("gf.idGrupoFrota","=", criteria.getLong("grupo.idGrupoFrota"));
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		sql.addCriteria("eq.idEquipamento","=", criteria.getLong("volEquipamentos.idEquipamento"));
		
		sql.addCriteria("re.dhRetirada.value",">=", criteria.getYearMonthDay("dhRetiradaInicial"));
		
		if (criteria.getYearMonthDay("dhRetiradaFinal") != null) {
			sql.addCriteria("re.dhRetirada.value","<=", JTDateTimeUtils.createWithMaxTime(criteria.getYearMonthDay("dhRetiradaFinal")));	
		}
		
		if (criteria.getString("tipoStatus").equals("R")) {
			sql.addCustomCriteria("re.dhRetirada.value is not null AND re.dhDevolucao.value is null");
		} else if (criteria.getString("tipoStatus").equals("D")) {
			sql.addCustomCriteria("re.dhDevolucao.value is not null");
		}
		
		sql.addCriteria("moto.idMotorista","=", criteria.getLong("motorista.idMotorista"));
		
		return sql;
	}


    public Boolean findEquipamentosRetirado(Long idEquipamento) {
        SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolRetiradasEqptos re ");
		sb.append("inner join re.volEquipamento eq ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("eq.idEquipamento","=",idEquipamento);
		sql.addCustomCriteria("re.dhDevolucao.value is null");
		
    	return (getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria()).intValue() >0 ? Boolean.TRUE : Boolean.FALSE);
	}
    
    /**
     * Retorna as retiradas que ainda não tiveram devolução.
     * @param idEquipamento
     * @return list de retiradas sem devolução.
     */
    public List findRetiradaEquipamentoByIdEquipamento(Long idEquipamento) {
        SqlTemplate sql = new SqlTemplate() ;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" VolRetiradasEqptos re ");
		sb.append("inner join re.volEquipamento eq ");
		
		sql.addFrom(sb.toString());
		
		sql.addProjection("re");
		
		sql.addCriteria("eq.idEquipamento","=",idEquipamento);
		sql.addCustomCriteria("re.dhDevolucao.value is null");
		
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
    
    public List findPendenciaDevolucao(Long id, String tipoUsuario) {
    	SqlTemplate sql =  this.createHQLFindPendenciaDevolucao( id, tipoUsuario );
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map (reqto)");
    	sql.addProjection(projecao.toString());
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
    private SqlTemplate createHQLFindPendenciaDevolucao(Long id, String tipoUsuario) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(VolRetiradasEqptos.class.getName(), "as reqto");
    	if( tipoUsuario.equalsIgnoreCase("funcionario")) {
    		sql.addCriteria("reqto.usuario", "=", id);
    	}
    	else {
    		sql.addCriteria("reqto.prestadorServico", "=", id);
    	}
    	sql.addCustomCriteria("reqto.dhDevolucao.value is null");
    	
    	return sql;
    }
    
}