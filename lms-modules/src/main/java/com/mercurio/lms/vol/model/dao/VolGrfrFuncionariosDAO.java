package com.mercurio.lms.vol.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vol.model.VolGrfrFuncionarios;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolGrfrFuncionariosDAO extends BaseCrudDao<VolGrfrFuncionarios, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolGrfrFuncionarios.class;
    }

    public Boolean getRowCountGruposFrota(Long idGrupoFrota,Long idUsuario) {
	   	SqlTemplate sql = new SqlTemplate();
	   	
	   	StringBuffer sb = new StringBuffer();
		sb.append("VolGruposFrotas gf ");
		sb.append("inner join gf.volGrfrFuncionarios vf ");
		sb.append("inner join vf.usuario usu ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("gf.idGrupoFrota","=",idGrupoFrota);
		sql.addCriteria("usu.idUsuario","=",idUsuario);
		
	    Integer result = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria()); 
	    return (result.intValue()> 0 ? Boolean.TRUE:Boolean.FALSE);	
	}


    public List findGruposFrotaByUsuario(Long idUsuario) {
        SqlTemplate sql = new SqlTemplate();
	   	
	   	StringBuffer sb = new StringBuffer();
		sb.append("VolGruposFrotas gf ");
		sb.append("inner join gf.volGrfrFuncionarios vf ");
		sb.append("inner join vf.usuario usu ");
		
		sql.addProjection("gf.idGrupoFrota");
		sql.addFrom(sb.toString());
		
		sql.addCriteria("usu.idUsuario","=",idUsuario);
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }

}