package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoPagamentoPostoDAO extends BaseCrudDao<TipoPagamentoPosto, Long>
{
	
	

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoPagamentoPosto.class;
    }
    protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("tipoPagamPostoPassagem",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchModes);
    }
    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("tipoPagamPostoPassagem",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchModes);
    }
    public List getPostoPassagemVigente(Long idPostoPassagem,YearMonthDay dtInicial,YearMonthDay dtFinal) {
	    DetachedCriteria dc = DetachedCriteria.forClass(PostoPassagem.class);
		dc.add(Restrictions.eq("idPostoPassagem",idPostoPassagem));
		dc.add(Restrictions.le("dtVigenciaInicial",dtInicial));
		dc.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",dtInicial),Restrictions.isNull("dtVigenciaFinal")));
		if (dtFinal != null)
			dc.add(Restrictions.or(Restrictions.isNull("dtVigenciaFinal"),
					Restrictions.ge("dtVigenciaFinal",dtFinal)));
		return findByDetachedCriteria(dc);
    }
    
    /*
     * Verifica se existe outro tipo de passagem vinculado ao mesmo posto passagem na vigencia escolida 
     */
    public List findTpPostoPassagemEquals(TipoPagamentoPosto bean) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("postoPassagem","pp");
    	if (bean.getIdTipoPagamentoPosto() != null)
    		dc.add(Restrictions.ne("idTipoPagamentoPosto",bean.getIdTipoPagamentoPosto()));

    	dc.add(Restrictions.or(Restrictions.eq("tipoPagamPostoPassagem.idTipoPagamPostoPassagem",bean.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem()),
    			Restrictions.eq("nrPrioridadeUso",bean.getNrPrioridadeUso())
    			));
    	dc.add(Restrictions.eq("pp.idPostoPassagem",bean.getPostoPassagem().getIdPostoPassagem()));
    	JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());
    	return findByDetachedCriteria(dc);
    }
    
    public TipoPagamentoPosto findByPostoPassagemAndMenorPrioridade(Long idPostoPassagem,YearMonthDay vigenteEm, Boolean blNaoConsiderarSemParar, Long idPagtoPPSemParar) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addFrom(getPersistentClass().getName(),"TPP");
    	hql.addCriteria("TPP.postoPassagem.id","=",idPostoPassagem);
    	
    	if (blNaoConsiderarSemParar != null && blNaoConsiderarSemParar){ 
	    	hql.addCustomCriteria(new StringBuffer("TPP.nrPrioridadeUso = " +
	    						"(SELECT MIN(TMP.nrPrioridadeUso) FROM ")
	    						.append(getPersistentClass().getName()).append(" AS TMP " +
	    						"WHERE TMP.postoPassagem.id = ? " +
	    						"AND TMP.dtVigenciaInicial <= ? " +
	    						"AND TMP.dtVigenciaFinal >= ? " +
	    						"AND TMP.tipoPagamPostoPassagem.id <> ? )").toString());
    	} else {
    		hql.addCustomCriteria(new StringBuffer("TPP.nrPrioridadeUso = " +
								"(SELECT MIN(TMP.nrPrioridadeUso) FROM ")
								.append(getPersistentClass().getName()).append(" AS TMP " +
								"WHERE TMP.postoPassagem.id = ? " +
								"AND TMP.dtVigenciaInicial <= ? " +
								"AND TMP.dtVigenciaFinal >= ? )").toString());
    	}
    	
    	hql.addCriteriaValue(idPostoPassagem);
    	hql.addCriteriaValue(vigenteEm);
    	hql.addCriteriaValue(vigenteEm);
    	
    	if (blNaoConsiderarSemParar != null && blNaoConsiderarSemParar)
    		hql.addCriteriaValue(idPagtoPPSemParar);
    	
    	
    	hql.addCriteria("TPP.dtVigenciaInicial","<=",vigenteEm);
    	hql.addCriteria("TPP.dtVigenciaFinal",">=",vigenteEm);
    	
    	
    	
    	return (TipoPagamentoPosto)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    	
    	
    }
    
    /*
     *  Não deve permitir alteração de datas de vigência do posto de
	    passagem(pai) para datas fora dos intervalos  dos
	    registro filhos cadastrados em tipo de pagamento .
     */
    public boolean findFilhosVigentesByVigenciaPai(Long idPostoPassagem, YearMonthDay dtInicioVigenciaPai,YearMonthDay dtFimVigenciaPai){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("postoPassagem.idPostoPassagem",idPostoPassagem));
    	int total = findByDetachedCriteria(dc).size();
    	if(total == 0)
    		return false;
    	if (dtInicioVigenciaPai != null)
    		dc.add(Restrictions.ge("dtVigenciaInicial",dtInicioVigenciaPai));
    	if(dtFimVigenciaPai != null){
    		dc.add(Restrictions.le("dtVigenciaFinal",dtFimVigenciaPai));
	    	dc.add(Restrictions.isNotNull("dtVigenciaFinal"));
    	}
    	
    	return !(findByDetachedCriteria(dc).size()>0);
    }
}