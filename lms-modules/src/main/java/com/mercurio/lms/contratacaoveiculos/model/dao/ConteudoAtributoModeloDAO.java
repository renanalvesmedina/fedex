package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConteudoAtributoModeloDAO extends BaseCrudDao<ConteudoAtributoModelo, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ConteudoAtributoModelo.class;
	}

	public List<ConteudoAtributoModelo> buscaConteudoAtributo(Long idModeloMeioTranspAtributo){
		DetachedCriteria dc = createDetachedCriteria();
		DetachedCriteria dcModeloAtributo = dc.createCriteria("modeloMeioTranspAtributo");
		dcModeloAtributo.add(Restrictions.eq("idModeloMeioTranspAtributo",idModeloMeioTranspAtributo));

		return findByDetachedCriteria(dc);
	}

	/**
	 * Retorna lista com o conteúdo de um atributo.
	 * 
	 * @param idModeloMeioTranspAtributo
	 * @return
	 */
	public List findConteudoByAtributo(Long idModeloMeioTranspAtributo) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "m");

		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("m.idConteudoAtributoModelo"), "idConteudoAtributoModelo");
		pl.add(Projections.property("m.dsConteudoAtributoModelo"), "dsConteudoAtributoModelo");

		dc.setProjection(pl);

		dc.createAlias("modeloMeioTranspAtributo", "mmta");
		dc.add(Restrictions.eq("mmta.idModeloMeioTranspAtributo", idModeloMeioTranspAtributo));

		dc.addOrder(OrderVarcharI18n.asc("m.dsConteudoAtributoModelo", LocaleContextHolder.getLocale()));

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Solicitação CQPRO00006052 da integração.
	 * Método que retorna uma instancia da classe ConteudoAtributoModelo de acordo com os parâmetros passados.
	 * @param idModeloMeioTranspAtributo
	 * @param dsConteudoAtributoModelo 
	 * @return
	 */
	public ConteudoAtributoModelo findConteudoAtributoTipoMeioTransporte(Long idModeloMeioTranspAtributo, String dsConteudoAtributoModelo){
		DetachedCriteria dc = DetachedCriteria.forClass(ConteudoAtributoModelo.class, "cma");
		dc.add(Restrictions.eq("cma.modeloMeioTranspAtributo.id", idModeloMeioTranspAtributo));
		dc.add(BaseCompareVarcharI18n.eq("cma.dsConteudoAtributoModelo", dsConteudoAtributoModelo, LocaleContextHolder.getLocale()));
		return (ConteudoAtributoModelo)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}