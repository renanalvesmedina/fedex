package com.mercurio.lms.sim.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ContatoComunicacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ContatoComunicacaoDAO extends BaseCrudDao<ContatoComunicacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ContatoComunicacao.class;
    }

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
        SqlTemplate sql = new SqlTemplate();
        // Relacionamentos
        sql.addFrom((new StringBuffer(ContatoComunicacao.class.getName())).append(" AS CC ")
        		.append("LEFT JOIN FETCH CC.configuracaoComunicacao AS CCO ")
        		.append("LEFT JOIN FETCH CC.contato AS CTO ")
        		.append("LEFT JOIN FETCH CC.telefoneContato AS TC ")
        		.append("LEFT JOIN FETCH TC.telefoneEndereco AS TE ")
        		.toString());
        sql.addCriteria("CCO.cliente.idCliente","=",criteria.getLong("cliente.idCliente"));
        sql.addOrderBy("CTO.nmContato");
        sql.addOrderBy("CC.dtVigenciaInicial");
        
        FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

        return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());

	}

	public ResultSetPage findByIdDetalhamento(Long id) {
		SqlTemplate sql = new SqlTemplate();
		        // Relacionamentos
		sql.addFrom((new StringBuffer(ContatoComunicacao.class.getName())).append(" AS CC ")
				.append("LEFT JOIN FETCH CC.contato AS CTO ")
				.append("LEFT JOIN FETCH CC.telefoneContato as TCT ")
				.append("LEFT JOIN FETCH TCT.telefoneEndereco as TEN ")
				.toString());
        sql.addCriteria("CC.idContatoComunicacao","=",id);
        return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),Integer.valueOf(1),Integer.valueOf(1),sql.getCriteria());
	}

   


}