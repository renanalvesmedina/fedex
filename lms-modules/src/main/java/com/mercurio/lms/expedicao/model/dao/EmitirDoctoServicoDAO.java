package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @author Claiton Grings
 * @spring.bean
 */
public class EmitirDoctoServicoDAO extends AdsmDao {

	public ImpressoraFormulario findImpressoraFormulario(Long idFilial, Long idImpressora, String tpFormulario, Long nrProximoFormulario) {

		DetachedCriteria dc = DetachedCriteria.forClass(ImpressoraFormulario.class, "if");
		dc.createAlias("if.controleFormulario", "cf");
		dc.add(Restrictions.eq("cf.filial.id", idFilial));
		dc.add(Restrictions.eq("cf.tpFormulario", tpFormulario));
		dc.add(Restrictions.eq("cf.tpSituacaoFormulario", "A"));
		dc.add(Restrictions.eq("if.impressora.id", idImpressora));
		dc.add(Restrictions.le("if.nrFormularioInicial", nrProximoFormulario));
		dc.add(Restrictions.ge("if.nrFormularioFinal", nrProximoFormulario));

		ImpressoraFormulario impressoraFormulario = (ImpressoraFormulario)getAdsmHibernateTemplate().findUniqueResult(dc);
		if(impressoraFormulario != null) {
			getAdsmHibernateTemplate().lock(impressoraFormulario, LockMode.UPGRADE_NOWAIT);
		}
		return impressoraFormulario;
	}

}
