package com.mercurio.lms.configuracoes.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ServidorFilial;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServidorFilialDAO extends BaseCrudDao<ServidorFilial, Long> {
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {

		return super.findPaginated(criteria, findDef);
	}
	
	/**
	 * Busca o IP do servidor da Filial onde o IP do computador esta incluido (nrIpInicial <= IP <= nrIpFinal). 
	 * 
	 * @param ip IP de um computador da filial.
	 * @return IP do servidor da filial do usuario.
	 */
	public Long findServerByIp(Long ip) {
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("sf.nrIpServidor");
		sql.addFrom(ServidorFilial.class.getName() + " as sf ");
		sql.addCriteria("sf.nrIpInicial", "<=", ip, Long.class);
		sql.addCriteria("sf.nrIpFinal", ">=", ip, Long.class);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ServidorFilial.class;
	}
	
	protected void initFindByIdLazyProperties(Map fetchModes) {
		
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}
}
