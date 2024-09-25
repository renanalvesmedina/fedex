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
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DiaSemanaDAO extends BaseCrudDao<DiaSemana, Long>
{
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
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
	 * Retorna a lista de registros de dia semana filtrado pelos parametros do m�todo.
	 * 
	 * @author Micka�l Jalbert
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
     * Obt�m o crit�rio de pesquisa em Criteria da pesquisa
     * que compara os dias checados com os dias marcados no banco 
     * apartir de um contexto especificado.
     * Dessa forma: se segunda checado: adiciona um and em segunda. 
     *				se ter�a checado:               ||
     *				...
     * 
     * @param bean O bean.
     * @param context Contexto da pesquisa (alias antes do atributo).
     * @return O crit�rio devidamente montado.
     * @author luisfco
     */
    public Criterion getCriterionDiasChecados(OperacaoServicoLocaliza osl, String context) {
    	
    	if (!"".equals(context))
    		context += ".";

    	Criterion orDiasSemana = null;
    	
    	// Problema: precisa-se de avliar um crit�rio de (OR) que 
    	// avalie todos os dias da semana que foram checados na tela.
    	// No entanto o m�todo Restrictions.or() s� aceita dois crit�rios e n�o (n) crit�rios.
    	// Dessa forma, a solu��o adotada foi criar um mapa contendo todos os dias da semana que
    	// foram checados.
    	// Para cada dia da semana � criada uma Restrictions.EQ()
    	// Ap�s isso, s�o tomadas as Restrictions duas a duas e adicionadas a um Restrictions.OR() 
    	// A restriction que sobrar � aninhada ao conjunto de Restrictions montado.
    	
    	// Map que possuir� todos os dias checados
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

    	// lista de Restrictions individuais que far�o parte do (OR)
    	List restricoes = new ArrayList();
    	
    	// para cada dia da semana, adiciona uma restri��o de (EQ) 
    	// Ex.: Restrictions.eq( "blDomingo", osl.getBlDomingo() )
    	for (Iterator it = diasSemana.entrySet().iterator(); it.hasNext(); ) {
    		Map.Entry en = (Entry) it.next();
    		
    		restricoes.add(Restrictions.eq((String)en.getKey(), en.getValue()));
    	}
    	    	
    	Integer ultimaRestricaoImpar = null;
    	
    	// se n�mero �mpar de restricoes, ent�o a �ltima restri��o
    	// � a (�mpar) que ser� aninhada �s demais restri��es irm�s
    	if (restricoes.size() % 2 == 1) {
    		ultimaRestricaoImpar = Integer.valueOf( restricoes.size() - 1 );
    	}

		int i = 0;
		while (i < restricoes.size()) {
					
			// se � uma restri��o par normal
			if (! Integer.valueOf(i).equals(ultimaRestricaoImpar)) {
				if (orDiasSemana == null)
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1));
				else 
					orDiasSemana = Restrictions.or(orDiasSemana, 
									Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1)));

			// se � a �ltima restri��o (�mpar)
			} else {
				if (orDiasSemana == null)
					orDiasSemana = (Criterion) restricoes.get(i);
				else 
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), orDiasSemana);
			}
    		
			// as restri��es de (OR) s�o avaliadas duas a duas
    		i += 2;
		}

		return orDiasSemana;
    }

	
}