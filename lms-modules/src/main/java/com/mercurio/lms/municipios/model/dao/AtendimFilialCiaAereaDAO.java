package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.AtendimFilialCiaAerea;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AtendimFilialCiaAereaDAO extends BaseCrudDao<AtendimFilialCiaAerea, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AtendimFilialCiaAerea.class;
    }

	protected void initFindByIdLazyProperties(Map fetchModes) {		
		fetchModes.put("filialCiaAerea", FetchMode.JOIN);		
		fetchModes.put("filialCiaAerea.pessoa", FetchMode.JOIN);
		fetchModes.put("filialCiaAerea.aeroporto",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.aeroporto.pessoa",FetchMode.JOIN);    	
    	fetchModes.put("filialCiaAerea.empresa",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.empresa.pessoa",FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map fetchModes) {		
		fetchModes.put("filialCiaAerea", FetchMode.JOIN);
		fetchModes.put("filialCiaAerea.pessoa", FetchMode.JOIN);
		fetchModes.put("filialCiaAerea.aeroporto",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.aeroporto.pessoa",FetchMode.JOIN);    	
    	fetchModes.put("filialCiaAerea.empresa",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.empresa.pessoa",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filialCiaAerea", FetchMode.JOIN);
		fetchModes.put("filialCiaAerea.pessoa", FetchMode.JOIN);
		fetchModes.put("filialCiaAerea.aeroporto",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.aeroporto.pessoa",FetchMode.JOIN);    	
    	fetchModes.put("filialCiaAerea.empresa",FetchMode.JOIN);
    	fetchModes.put("filialCiaAerea.empresa.pessoa",FetchMode.JOIN);
	}

	
	/**
	 * Valida se o horario da Cia. Aerea nao colide com outro horario ja cadastrado num mesmo horario e nos mesmos dias 
	 * @param bean
	 * @return
	 */
	public boolean validaVigenciaCiaAerea(AtendimFilialCiaAerea bean){
		StringBuffer hql = new StringBuffer()
		.append("from " + this.getPersistentClass().getName() +" as af ")
		.append("where af.filialCiaAerea.id = :idFilialCiaAerea ");
					
		hql.append("and ( ");
		StringBuffer dias = new StringBuffer();
		
		if (bean.getBlDomingo().booleanValue()) {
			dias.append(dias.length() > 0 ? " or " : "");			
			dias.append(" af.blDomingo = :blDomingo ");
		}
		if (bean.getBlSegunda().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blSegunda = :blSegunda ");
		}
		if (bean.getBlTerca().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blTerca = :blTerca ");
		}
		if (bean.getBlQuarta().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blQuarta = :blQuarta ");		
		}
		if (bean.getBlQuinta().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blQuinta = :blQuinta ");
		}
		if (bean.getBlSexta().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blSexta = :blSexta ");
		}
		if (bean.getBlSabado().booleanValue()){
			dias.append(dias.length() > 0 ? " or " : "");
			dias.append(" af.blSabado = :blSabado ");		
		}
		
		hql.append(dias.toString() + ")");
		
		hql.append("and not(af.hrAtendimentoFinal < :hrAtendimentoInicial " +
	        		"or af.hrAtendimentoInicial > :hrAtendimentoFinal) ");
		
		if (bean.getIdAtendimFilialCiaAerea() != null)
			hql.append("and af.idAtendimFilialCiaAerea != :idAtendimFilialCiaAerea ");
		
		if (bean.getDtVigenciaFinal() != null)
			hql.append("and not((af.dtVigenciaFinal < :dtVigenciaInicial " +
							    "and af.dtVigenciaFinal is not null) " +
					        "or af.dtVigenciaInicial > :dtVigenciaFinal) ");
    	else
    		hql.append("and not(af.dtVigenciaFinal < :dtVigenciaInicial and af.dtVigenciaFinal is not null) ");

    	List lCampos =  new ArrayList();
    	if (bean.getIdAtendimFilialCiaAerea() != null)
    		lCampos.add("idAtendimFilialCiaAerea");
    	lCampos.add("idFilialCiaAerea");    	    	
		
		if (bean.getBlDomingo().booleanValue())lCampos.add("blDomingo");		
		if (bean.getBlSegunda().booleanValue())lCampos.add("blSegunda");
		if (bean.getBlTerca().booleanValue())lCampos.add("blTerca");
		if (bean.getBlQuarta().booleanValue())lCampos.add("blQuarta");	
		if (bean.getBlQuinta().booleanValue())lCampos.add("blQuinta");
		if (bean.getBlSexta().booleanValue())lCampos.add("blSexta");
		if (bean.getBlSabado().booleanValue())lCampos.add("blSabado");  	
    	
    	lCampos.add("hrAtendimentoInicial");
    	lCampos.add("hrAtendimentoFinal");
    	lCampos.add("dtVigenciaInicial");
    	if (bean.getDtVigenciaFinal() != null)
    		lCampos.add("dtVigenciaFinal");
   		String[] campos = new String[lCampos.size()];
   		for(int x = 0; x < lCampos.size(); x++)
   			campos[x] = (String)lCampos.get(x);

    	List lValores =  new ArrayList();
    	if (bean.getIdAtendimFilialCiaAerea() != null)
    		lValores.add(bean.getIdAtendimFilialCiaAerea());
    	lValores.add(bean.getFilialCiaAerea().getIdFilialCiaAerea());    
    	
		if (bean.getBlDomingo().booleanValue())lValores.add(bean.getBlDomingo());	
		if (bean.getBlSegunda().booleanValue())lValores.add(bean.getBlSegunda());
		if (bean.getBlTerca().booleanValue())lValores.add(bean.getBlTerca());
		if (bean.getBlQuarta().booleanValue())lValores.add(bean.getBlQuarta());
		if (bean.getBlQuinta().booleanValue())lValores.add(bean.getBlQuinta());
		if (bean.getBlSexta().booleanValue())lValores.add(bean.getBlSexta());
		if (bean.getBlSabado().booleanValue())lValores.add(bean.getBlSabado());    	
    	
    	lValores.add(bean.getHrAtendimentoInicial());
    	lValores.add(bean.getHrAtendimentoFinal());
    	lValores.add(bean.getDtVigenciaInicial());
    	if (bean.getDtVigenciaFinal() != null)
    		lValores.add(bean.getDtVigenciaFinal());
    	Object[] values = lValores.toArray();
    	    	
    	List list = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),campos,values);
    	
    	return list.size()>0;
	}
	
	 public boolean findAtendimByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
	    	DetachedCriteria dc = createDetachedCriteria();
	    	dc.add(Restrictions.eq("filialCiaAerea.idFilialCiaAerea",idFilialCiaAerea));
	    	dc.add(Restrictions.or(
	    			Restrictions.or(
		    			Restrictions.and(
		    					Restrictions.lt("dtVigenciaInicial",dataInicio),
		    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim))),
		    			Restrictions.and(
		    	    					Restrictions.gt("dtVigenciaInicial",dataInicio),
		    	    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim)))),
		    	   Restrictions.or(
		    			   Restrictions.lt("dtVigenciaInicial",dataInicio),
		    			   Restrictions.gt("dtVigenciaFinal",dataFim))));
			return findByDetachedCriteria(dc).size()>0;
		}
	
}