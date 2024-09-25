package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoEnderecoPessoaDAO extends BaseCrudDao<TipoEnderecoPessoa, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TipoEnderecoPessoa.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("enderecoPessoa.pessoa", FetchMode.JOIN);
		lazyFindById.put("enderecoPessoa.tipoLogradouro", FetchMode.SELECT);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("enderecoPessoa.tipoLogradouro",FetchMode.SELECT);
	}

	/**
	 * Não permitir que sejam cadastrados 2 tipos de 
	 * endereço Comercial ou 2 tipos de endereço Residencial para a mesma pessoa..
	 * @author Alexandre Menezes
	 * @param idEnderecoPessoa
	 * @param idPessoa
	 * @param tpEndereco
	 * @return true or false
	 */
	public boolean validateTpEndereco(Long idTipoEnderecoPessoa, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idPessoa, String tpEndereco) {
		String[] tpEnderecos = {"COM","RES"};

		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.property("tpEndereco"));

		dc.add(Restrictions.in("tpEndereco",tpEnderecos));
		if (idTipoEnderecoPessoa != null){
			dc.add(Restrictions.not(Restrictions.idEq(idTipoEnderecoPessoa)));
		}
		DetachedCriteria dcEndereco = dc.createCriteria("enderecoPessoa");
		dcEndereco.createAlias("pessoa", "pes");
		dcEndereco.add(Restrictions.eq("pes.idPessoa", idPessoa));
		JTVigenciaUtils.getDetachedVigencia(dcEndereco, dtVigenciaInicial, dtVigenciaFinal);

		List<DomainValue> registros = findByDetachedCriteria(dcEndereco);
		for(DomainValue registro : registros) {
			String tpEnd = registro.getValue(); 	
			if (tpEndereco.equalsIgnoreCase(tpEnd)) {
				return false;
			}
		}
		return true;
	}

	public TipoEnderecoPessoa findTipoEnderecoPessoaByEnderecoPessoa(Long idEnderecoPessoa) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("tep");
		sql.addFrom(TipoEnderecoPessoa.class.getName(),"tep");
		sql.addCriteria("tep.enderecoPessoa.id","=",idEnderecoPessoa);

		List<TipoEnderecoPessoa> result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		if(!result.isEmpty() ){
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Criado para buscar o TipoEnderecoPessoa sem os dados de cliente já recebidos da tela que o chamou
	 *
	 * @author José Rodrigo Moraes
	 * @since 05/12/2006
	 *
	 * @param id
	 * @return
	 */
	public TipoEnderecoPessoa findByIdCustomized(Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("tep");

		sql.addInnerJoin(getPersistentClass().getName(),"tep");
		sql.addInnerJoin("tep.enderecoPessoa","ep");

		sql.addCriteria("tep.id","=",id);

		List <TipoEnderecoPessoa> result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Retorna 'true' de o endereco informado tem o tipo comercial ou residencial
	 * 
	 * @author Mickaël Jalbert
	 * @since 09/03/2007
	 * @param idEnderecoPessoa
	 * @return
	 */
	public boolean isComercialOrResidencial(Long idEnderecoPessoa){
		SqlTemplate sql = new SqlTemplate(); 
		sql.addProjection("count(tep.idTipoEnderecoPessoa)");
		sql.addFrom(TipoEnderecoPessoa.class.getName(),"tep");
		sql.addCriteria("tep.enderecoPessoa.id","=",idEnderecoPessoa);
		sql.addCustomCriteria("tep.tpEndereco in ('COM','RES')");

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return (result.intValue() > 0);
	}

}