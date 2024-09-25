package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class InformacaoDocServicoDAO extends BaseCrudDao<InformacaoDocServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<InformacaoDocServico> getPersistentClass() {
		return InformacaoDocServico.class;
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param dsTipoRegistroComplemento
	 * @param dsCampo
	 * @return InformacaoDocServico
	 */
	public InformacaoDocServico findInformacaoDoctoServico(String dsTipoRegistroComplemento, String dsCampo) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.createAlias("tipoRegistroComplemento", "trc");

		dc.add(Restrictions.eq("dsCampo", dsCampo));
		dc.add(Restrictions.eq("trc.dsTipoRegistroComplemento", dsTipoRegistroComplemento));

		return (InformacaoDocServico) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public InformacaoDocServico findByDsCampo(String dsCampo) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ids")
							.add(Restrictions.eq("ids.dsCampo", dsCampo));

		return (InformacaoDocServico)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public InformacaoDocServico findByDsCampoEqualToDsTipoRegistroComplemento(String dsCampo) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ids")
							.createAlias("tipoRegistroComplemento","trc")
							.add(Restrictions.eq("ids.dsCampo", dsCampo))
							.add(Restrictions.eqProperty("ids.dsCampo", "trc.dsTipoRegistroComplemento"));
		return (InformacaoDocServico)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}