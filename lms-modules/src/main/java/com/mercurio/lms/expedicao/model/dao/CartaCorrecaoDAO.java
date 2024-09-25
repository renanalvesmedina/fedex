package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CartaCorrecao;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CartaCorrecaoDAO extends BaseCrudDao<CartaCorrecao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CartaCorrecao.class;
    }

    /**
	 * getRowCountCartaCorrecao Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return Integer
	 */
    public Integer getRowCountCartaCorrecao(TypedFlatMap criteria) {
    	DetachedCriteria dc = this.getQueryPaginated(criteria)
    		.setProjection(Projections.count("cc.id"));
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }

    /**
	 * findPaginatedCartaCorrecao Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return 
	 */
	public ResultSetPage findPaginatedCartaCorrecao(TypedFlatMap criteria) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("cc.idCartaCorrecao"), "idCartaCorrecao")
			.add(Projections.property("cc.dtEmissao"), "dtEmissao")
			.add(Projections.property("cc.nrCampo"), "nrCampo")
			.add(Projections.property("fo.sgFilial"), "sgFilial")
			.add(Projections.property("pes.nmFantasia"), "pessoa.nmFantasia")
			.add(Projections.property("c.sgPais"), "sgPais")
			.add(Projections.property("c.nrPermisso"), "nrPermisso")
			.add(Projections.property("c.nrCrt"), "nrCrt");

		DetachedCriteria dc = this.getQueryPaginated(criteria)
			.setProjection(pl);
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);	
		return findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	/**
	 * getQueryPaginated Visualizar Carta Correcao 
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return
	 */
	private DetachedCriteria getQueryPaginated(TypedFlatMap criteria) {

		Long idFilial = criteria.getLong("filialByIdFilialOrigem.idFilial");
		Long idDoctoServico = criteria.getLong("ctoInternacional.idDoctoServico");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cc")
			.createAlias("cc.ctoInternacional", "c")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("fo.pessoa", "pes");
		if(idDoctoServico != null) {
			dc = dc.add(Restrictions.eq("c.id", idDoctoServico));
		}
		if(idFilial != null) {
			dc = dc.add(Restrictions.eq("fo.id", idFilial));
		}
		dc.addOrder(Order.desc("cc.dtEmissao"))
			.addOrder(Order.asc("fo.sgFilial"))
			.addOrder(Order.asc("c.sgPais"))
			.addOrder(Order.asc("c.nrPermisso"))
			.addOrder(Order.asc("c.nrCrt"));
		return dc;
	}

	public Map findCartaCorrecaoById(Long id) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("cc.idCartaCorrecao"), "idCartaCorrecao")
			.add(Projections.property("cc.dtEmissao"), "dtEmissao")
			.add(Projections.property("cc.nrCampo"), "nrCampo")
			.add(Projections.property("cc.nmDestinatario"), "nmDestinatario")
			.add(Projections.property("cc.dsConteudoAtual"), "dsConteudoAtual")
			.add(Projections.property("cc.dsConteudoAlterado"), "dsConteudoAlterado")
			.add(Projections.property("fo.idFilial"), "filialByIdFilialOrigem_idFilial")
			.add(Projections.property("fo.sgFilial"), "filialByIdFilialOrigem_sgFilial")
			.add(Projections.property("po.nmFantasia"), "filialByIdFilialOrigem_pessoa_nmFantasia")
			.add(Projections.property("cdp.nmPessoa"), "ctoInternacional_clienteByIdClienteDestinatario_pessoa_nmPessoa")
			.add(Projections.property("cdp.nrIdentificacao"), "ctoInternacional_clienteByIdClienteDestinatario_pessoa_nrIdentificacao")
			.add(Projections.property("crp.nmPessoa"), "ctoInternacional_clienteByIdClienteRemetente_pessoa_nmPessoa")
			.add(Projections.property("crp.nrIdentificacao"), "ctoInternacional_clienteByIdClienteRemetente_pessoa_nrIdentificacao")
			.add(Projections.property("c.sgPais"), "ctoInternacional_sgPais")
			.add(Projections.property("c.idDoctoServico"), "ctoInternacional_idDoctoServico")
			.add(Projections.property("c.nrPermisso"), "ctoInternacional_nrPermisso")
			.add(Projections.property("c.nrCrt"), "ctoInternacional_nrCrt");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cc")
			.createAlias("cc.ctoInternacional", "c")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("fo.pessoa", "po")
			.createAlias("c.clienteByIdClienteDestinatario", "cd")
			.createAlias("cd.pessoa", "cdp")
			.createAlias("c.clienteByIdClienteRemetente", "cr")
			.createAlias("cr.pessoa", "crp")
			.setProjection(pl)
			.add(Restrictions.idEq(id))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()) {
			return (Map)l.get(0);
		}
		return null;
	}
}