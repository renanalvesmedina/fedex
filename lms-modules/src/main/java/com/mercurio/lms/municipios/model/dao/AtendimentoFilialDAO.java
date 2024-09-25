package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.AtendimentoFilial;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AtendimentoFilialDAO extends BaseCrudDao<AtendimentoFilial, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AtendimentoFilial.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	/**
	 * Valida se o horario filial não colide com outro horário já cadastrado num mesmo horário e nos mesmos dias. 
	 * @param bean
	 * @return
	 */
	public boolean verificaAtendimentoFilialVigentes(AtendimentoFilial bean) {
		StringBuffer hql = new StringBuffer()
		.append("from " + AtendimentoFilial.class.getName() + " as af ")
		.append("join fetch af.filial ")
		.append("where af.filial.id = :idFilial ")
		.append("and af.tpAtendimento = :tpAtendimento ");
		
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
		
		if (!dias.toString().equals("")) {
			hql.append("and ( ").append(dias).append(" ) ");			
		}
		
		hql.append("and not(af.hrAtendimentoFinal < :hrAtendimentoInicial " +
	        		"or af.hrAtendimentoInicial > :hrAtendimentoFinal) ");
		
		if (bean.getIdAtendimentoFilial() != null)
			hql.append("and af.idAtendimentoFilial != :idAtendimentoFilial ");
		
		if (bean.getDtVigenciaFinal() != null)
			hql.append("and not((af.dtVigenciaFinal < :dtVigenciaInicial " +
							    "and af.dtVigenciaFinal is not null) " +
					        "or af.dtVigenciaInicial > :dtVigenciaFinal) ");
    	else
    		hql.append("and not(af.dtVigenciaFinal < :dtVigenciaInicial and af.dtVigenciaFinal is not null) ");

    	List lCampos =  new ArrayList();
    	if (bean.getIdAtendimentoFilial() != null)
    		lCampos.add("idAtendimentoFilial");
    	lCampos.add("idFilial");
    	lCampos.add("tpAtendimento");
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
    	if (bean.getIdAtendimentoFilial() != null)
    		lValores.add(bean.getIdAtendimentoFilial());
    	lValores.add(bean.getFilial().getIdFilial());
    	lValores.add(bean.getTpAtendimento().getValue());
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

}