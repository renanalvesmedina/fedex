package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.ClassificacaoCliente;
import com.mercurio.lms.vendas.model.DescClassificacaoCliente;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClassificacaoClienteDAO extends BaseCrudDao<ClassificacaoCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ClassificacaoCliente.class;
    }
  
 
    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("descClassificacaoCliente", FetchMode.JOIN);
		lazyFindById.put("descClassificacaoCliente.tipoClassificacaoCliente", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("descClassificacaoCliente", FetchMode.JOIN);
		lazyFindPaginated.put("descClassificacaoCliente.tipoClassificacaoCliente", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	
	
	/**
	 * Validar só deve existir apenas um registro de classificacao_cliente
     * por cliente e tipo de classificação
	 * @author alexandrem
	 * @param idCliente
	 * @param idClassificacaoCliente
	 * @param idTipoClassificacaoCliente
	 * @return true senão for violada a regra false caso contrário
	 */
	public Boolean validateClassificacaoCliente(Long idCliente,Long idClassificacaoCliente,Long idTipoClassificacaoCliente) {
		SqlTemplate sql = new SqlTemplate();

	    sql.addProjection("count(dcc)");

        sql.addFrom(ClassificacaoCliente.class.getName() + " as cc");
        sql.addFrom(DescClassificacaoCliente.class.getName() + " as dcc");
        sql.addFrom(TipoClassificacaoCliente.class.getName() + " as tcc");
    	sql.addJoin("cc.descClassificacaoCliente.idDescClassificacaoCliente","dcc.idDescClassificacaoCliente");
    	sql.addJoin("dcc.tipoClassificacaoCliente.idTipoClassificacaoCliente","tcc.idTipoClassificacaoCliente");
     	sql.addCriteria("cc.cliente.idCliente","=",idCliente);
     	sql.addCriteria("cc.idClassificacaoCliente","!=",idClassificacaoCliente);
     	sql.addCriteria("tcc.idTipoClassificacaoCliente","=",idTipoClassificacaoCliente);

     	List numeroRegistro = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
        if (!numeroRegistro.isEmpty() && LongUtils.hasValue(LongUtils.getLong(numeroRegistro.get(0)))) {
           return Boolean.FALSE;
        }
		return Boolean.TRUE;
	}

	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" delete ").append(getPersistentClass().getName())
    	.append(" where cliente.idCliente = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }

}