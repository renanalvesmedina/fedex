package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class FluxoContratacaoDAO extends BaseCrudDao<FluxoContratacao, Long> {
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return FluxoContratacao.class;
    }

    /**
     * Retorna os fluxos contrata��o da solicita��o cuja filial de origem est� implantada na data do par�metro today, 
     * ou cujo tipo de abrang�ncia seja o tipo de abrang�ncia informado
     * @param idSolicitacaoContratacao
     * @param today Data a ser considerada no teste se a filial j� foi implantada (normalmente a data atual)
     * @param tpAbrangencia Tipo de abrang�ncia a ser considerada, caso o fluxo seja do tipo de abrang�ncia, 
     * 	desconsidera a data de implanta��o
     * @return
     */
    public List<FluxoContratacao> find(Long idSolicitacaoContratacao, YearMonthDay dtToday, String tpAbrangencia) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "fc");
    	dc.createAlias("fc.filialOrigem", "fo");
    	dc.createAlias("fc.filialDestino", "fd");
    	dc.createAlias("fc.solicitacaoContratacao", "sc");
    	dc.createAlias("sc.moedaPais", "mp");
    	dc.add(Restrictions.eq("sc.id", idSolicitacaoContratacao));

		//Retornar apenas as filiais que n�o foram implantadas (fluxoContratacao.filialDestino.dtImplantacaoLMS for nula ou for maior que a data atual)
		if(dtToday != null) {
			LogicalExpression or = Restrictions.or(Restrictions.isNull("fo.dtImplantacaoLMS"), Restrictions.gt("fo.dtImplantacaoLMS", dtToday));
			if(tpAbrangencia != null) {
				or = Restrictions.or(or, Restrictions.eq("fc.tpAbrangencia", tpAbrangencia));
			}
			dc.add(or);
		}
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    /**
     * 
     * @param idSolicitacaoContratacao (required)
     * @param idFilialOrigem
     * @param idFilialDestino
     * @return
     */
    public List findFluxoContratacaoByIdSolicitacaoContratacao(Long idSolicitacaoContratacao, Long idFilialOrigem, Long idFilialDestino) {
    	StringBuffer hql = new StringBuffer()
		.append("select fc ")
		.append("from ").append(FluxoContratacao.class.getName()).append(" as fc ")
		.append("where ")
		.append("fc.solicitacaoContratacao.id = ? ");

    	List parametros = new ArrayList();
    	parametros.add(idSolicitacaoContratacao);

    	if (idFilialOrigem != null) {
    		hql.append("and fc.filialOrigem.id = ? ");
    		parametros.add(idFilialOrigem);
    	}
    	if (idFilialDestino != null) {
    		hql.append("and fc.filialDestino.id = ? ");
    		parametros.add(idFilialDestino);
    	}
    	return getAdsmHibernateTemplate().find(hql.toString(), parametros.toArray());
    }
}
