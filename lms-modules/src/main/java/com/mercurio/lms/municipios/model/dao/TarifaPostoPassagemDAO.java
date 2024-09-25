package com.mercurio.lms.municipios.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaPostoPassagemDAO extends BaseCrudDao<TarifaPostoPassagem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TarifaPostoPassagem.class;
    }
 
    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("valorTarifaPostoPassagems",FetchMode.SELECT);
    	fetchModes.put("valorTarifaPostoPassagems.moedaPais",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchModes);
    }
    
    protected void initFindListLazyProperties(Map fetchModes) {
    	fetchModes.put("valorTarifaPostoPassagems",FetchMode.SELECT);
    	fetchModes.put("valorTarifaPostoPassagems.moedaPais",FetchMode.JOIN);
    	super.initFindListLazyProperties(fetchModes);
	}
    
    public boolean findBeforeStore(TarifaPostoPassagem bean) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("postoPassagem","pp");
    	if (bean.getIdTarifaPostoPassagem() != null)
    		dc.add(Restrictions.ne("idTarifaPostoPassagem",bean.getIdTarifaPostoPassagem()));

    	dc.add(Restrictions.eq("pp.idPostoPassagem",bean.getPostoPassagem().getIdPostoPassagem()));
    	
    	JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());
    	return findByDetachedCriteria(dc).size() != 0;
    }
    /**
     * Implementações referentes a DF02
     */
	public void removeById(Long id) {
		TarifaPostoPassagem bean = (TarifaPostoPassagem)findById(id);
		bean.getValorTarifaPostoPassagems().clear();
		getAdsmHibernateTemplate().delete(bean);
	}
	
	public List findPostoPassagemVigente(Long idPostoPassagem) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("postoPassagem","pp");
		dc.createAlias("pp.municipio","mu");
		dc.createAlias("mu.unidadeFederativa","uf");
		dc.createAlias("uf.pais","ps");
		dc.add(Restrictions.eq("pp.idPostoPassagem",idPostoPassagem));
		dc.add(Restrictions.and(Restrictions.le("dtVigenciaInicial",JTDateTimeUtils.getDataAtual()),
				Restrictions.or(Restrictions.isNull("dtVigenciaFinal"),Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.getDataAtual()))));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	public int removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			removeById(id);
		}
		return ids.size();
	}
	
    public TarifaPostoPassagem store(TarifaPostoPassagem tpp, ItemList items) {
        super.store(tpp);
    	removeValorTarifaPostoPassagem(items.getRemovedItems());
    	storeValorTarifaPostoPassagem(items.getNewOrModifiedItems());
    	getAdsmHibernateTemplate().flush();
        return tpp;
    }


	private void storeValorTarifaPostoPassagem(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeValorTarifaPostoPassagem(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}
 
    public List findValorTarifaPostoPassagemByTarifaPostoPassagemId(Long idTarifaPostoPassagem) {
    	return getAdsmHibernateTemplate().find(new StringBuffer("from ").append(ValorTarifaPostoPassagem.class.getName()).append(" vtpp ")
    		.append("inner join fetch vtpp.tipoMeioTransporte TMT ")
    		.append("left  join fetch vtpp.tipoMeioTransporte.tipoMeioTransporte ")
    		.append("inner join fetch vtpp.moedaPais ")
    		.append("inner join fetch vtpp.moedaPais.moeda ")
    		.append("where vtpp.tarifaPostoPassagem.id = ? order by vtpp.qtEixos, ").append("TMT.dsTipoMeioTransporte").toString(),idTarifaPostoPassagem);
    }

	public Integer getRowCountValorTarifaPostoPassagem(Long idTarifaPostoPassagem) {
		return getAdsmHibernateTemplate().getRowCountForQuery("from " + ValorTarifaPostoPassagem.class.getName()+ " vtpp inner join fetch vtpp.tipoMeioTransporte inner join fetch vtpp.moedaPais inner join fetch vtpp.moedaPais.moeda where vtpp.tarifaPostoPassagem.id = ?", new Object[] {idTarifaPostoPassagem});
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
    
    public List findByPostoPassagem(Long idPostoPassagem,Boolean isVigentes) {
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addFrom(new StringBuffer(TarifaPostoPassagem.class.getName()).append(" TPP ")
    			.append("INNER JOIN FETCH TPP.valorTarifaPostoPassagems VTPP ")
    			.append("INNER JOIN FETCH VTPP.moedaPais MP ")
    			.append("INNER JOIN FETCH MP.moeda M ")
    			.toString());

    	hql.addCriteria("TPP.postoPassagem.id","=",idPostoPassagem);
    	
    	
    	if (isVigentes != null && isVigentes.booleanValue()) {
    		hql.addCriteria("TPP.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
    		hql.addCriteria("TPP.dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
    	}
    	
		hql.addCustomCriteria(new StringBuffer("VTPP.id = (SELECT max(VTPP2.id) FROM ")
				.append(ValorTarifaPostoPassagem.class.getName()).append(" VTPP2 WHERE VTPP2.tarifaPostoPassagem.id = TPP.id) ").toString());

		hql.addOrderBy("TPP.dtVigenciaInicial");
		
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());

    }
}