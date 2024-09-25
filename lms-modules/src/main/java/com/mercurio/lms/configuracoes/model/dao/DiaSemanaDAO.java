package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.DiaSemana;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DiaSemanaDAO extends BaseCrudDao<DiaSemana, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DiaSemana.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pais", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pais", FetchMode.JOIN);
	}
	
	
	/**
	 * Retorna a lista de registros de dia semana filtrado pelos parametros do método.
	 * 
	 * @author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param Long idMunicipio
	 * @return List
	 */	
	public List findByCriterio(Long idMunicipio){
		SqlTemplate hql = mountHql(idMunicipio);
		
		hql.addProjection("ds");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	private SqlTemplate mountHql(Long idMunicipio){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(DiaSemana.class.getName(), "ds");
		hql.addInnerJoin("ds.pais", "pa");
		hql.addInnerJoin("pa.unidadeFederativas", "uf");
		hql.addInnerJoin("uf.municipios", "mu");
		
		hql.addCriteria("mu.id","=",idMunicipio);
		
		return hql;
	}
	
    /**
     * Obtém o critério de pesquisa em Criteria da pesquisa
     * que compara os dias checados com os dias marcados no banco 
     * apartir de um contexto especificado.
     * Dessa forma: se segunda checado: adiciona um and em segunda. 
     *				se terça checado:               ||
     *				...
     * 
     * @param bean O bean.
     * @param context Contexto da pesquisa (alias antes do atributo).
     * @return O critério devidamente montado.
     * @author luisfco
     */
    public Criterion getCriterionDiasChecados(OperacaoServicoLocaliza osl, String context) {
    	
    	if (!"".equals(context))
    		context += ".";

    	Criterion orDiasSemana = null;
    	
    	// Problema: precisa-se de avliar um critério de (OR) que 
    	// avalie todos os dias da semana que foram checados na tela.
    	// No entanto o método Restrictions.or() só aceita dois critérios e não (n) critérios.
    	// Dessa forma, a solução adotada foi criar um mapa contendo todos os dias da semana que
    	// foram checados.
    	// Para cada dia da semana é criada uma Restrictions.EQ()
    	// Após isso, são tomadas as Restrictions duas a duas e adicionadas a um Restrictions.OR() 
    	// A restriction que sobrar é aninhada ao conjunto de Restrictions montado.
    	
    	// Map que possuirá todos os dias checados
    	HashMap diasSemana = new HashMap();
    	    	
    	if (osl.getBlDomingo().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blDomingo", osl.getBlDomingo());
    	} 
    	if (osl.getBlSegunda().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blSegunda", osl.getBlSegunda());    		
    	}
    	if (osl.getBlTerca().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blTerca", osl.getBlTerca());	
    	}
    	if (osl.getBlQuarta().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blQuarta", osl.getBlQuarta());
    	}
    	if (osl.getBlQuinta().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blQuinta", osl.getBlQuinta());
    	}
    	if (osl.getBlSexta().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blSexta", osl.getBlSexta());
    	}
    	if (osl.getBlSabado().equals(Boolean.TRUE)) {
    		diasSemana.put(context + "blSabado", osl.getBlSabado());
    	}

    	// lista de Restrictions individuais que farão parte do (OR)
    	List restricoes = new ArrayList();
    	
    	// para cada dia da semana, adiciona uma restrição de (EQ) 
    	// Ex.: Restrictions.eq( "blDomingo", osl.getBlDomingo() )
    	for (Iterator it = diasSemana.entrySet().iterator(); it.hasNext(); ) {
    		Map.Entry en = (Entry) it.next();
    		
    		restricoes.add(Restrictions.eq((String)en.getKey(), en.getValue()));
    	}
    	    	
    	Integer ultimaRestricaoImpar = null;
    	
    	// se número ímpar de restricoes, então a última restrição
    	// é a (ímpar) que será aninhada às demais restrições irmãs
    	if (restricoes.size() % 2 == 1) {
    		ultimaRestricaoImpar = Integer.valueOf( restricoes.size() - 1 );
    	}

		int i = 0;
		while (i < restricoes.size()) {
					
			// se é uma restrição par normal
			if (! Integer.valueOf(i).equals(ultimaRestricaoImpar)) {
				if (orDiasSemana == null)
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1));
				else 
					orDiasSemana = Restrictions.or(orDiasSemana, 
									Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1)));

			// se é a última restrição (ímpar)
			} else {
				if (orDiasSemana == null)
					orDiasSemana = (Criterion) restricoes.get(i);
				else 
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), orDiasSemana);
			}
    		
			// as restrições de (OR) são avaliadas duas a duas
    		i += 2;
		}

		return orDiasSemana;
    }

	
}