package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.SemanaRemetMrun;
import com.mercurio.lms.municipios.model.Filial;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SemanaRemetMrunDAO extends BaseCrudDao<SemanaRemetMrun, Long> {
	private static final String HQL_FIND_SEMANA_REMET_MRUN_BY_ID_MILK_RUN = " from " + SemanaRemetMrun.class.getName() + " smr " +
																			"	join fetch smr.milkRemetente mr " +
																			"	join fetch mr.cliente c " +
																			"	join fetch c.pessoa " +
																			" where mr.milkRun.id = ? " +
																			" order by smr.tpSemanaDoMes, " +
																			"		   smr.nrDomingo, smr.nrSegundaFeira, " +
																			"		   smr.nrTercaFeira, smr.nrQuartaFeira, " +
																			"		   smr.nrQuintaFeira, smr.nrSextaFeira, " +
																			"		   smr.nrSabado ";
	
	public SemanaRemetMrun findById(Long idSemanaRemetMrun) {
		SemanaRemetMrun semanaRemetMrun = null;
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(SemanaRemetMrun.class);    	
    	dc.add(Restrictions.eq("id", idSemanaRemetMrun));
    	dc.setFetchMode("milkRemetente", FetchMode.JOIN);

		List result = super.findByDetachedCriteria(dc);
		if(result.size() == 1) {
			semanaRemetMrun = (SemanaRemetMrun) result.get(0);
		}    	

    	return semanaRemetMrun;
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SemanaRemetMrun.class;
    }

    /**
     * Retorna todas as semanasRemetentesMilkRun para os clientes cuja filialAtendeOperacional
     * seja igual à filial passada por parâmetro e a faixa de dias (tpSemanaDoMes) 
     * corresponda à faixa passada por parâmetro ("P"=primeira faixa ...até "Q"=quarta faixa)
     * @param filial
     * @param dataProcessoColeta
     * @return
     */
    public List findSemanasRemetMrunByFaixaDiasAndFilialAtendeOperacional(Filial filial, String siglaFaixa) {
        DetachedCriteria dcSemRemet = DetachedCriteria.forClass(SemanaRemetMrun.class)
        	.createAlias("milkRemetente","mre")
        	.createAlias("mre.cliente","cli")
        	.createAlias("cli.filialByIdFilialAtendeOperacional", "fil")
        	.add(Restrictions.eq("fil.id", filial.getIdFilial()))
        	.add(Restrictions.eq("tpSemanaDoMes",siglaFaixa));

        return super.findByDetachedCriteria(dcSemRemet);
    }
    
    
    /**
     * Retorna o Maior Número do dia da semana existente na Tabela SEMANA_REMET_MRUN 
     * 
     * @param idMilkRun
     * @param diaSemana
     * @return Short: maior número do dia da semana. Caso nao encontre, retorna null
     */
    public Short findMaiorNrDiaSemana(Long idMilkRun, String tipoSemana, String diaSemana) {
        DetachedCriteria dc = DetachedCriteria.forClass(SemanaRemetMrun.class);
                
        dc.setProjection(Projections.max(diaSemana));
        
		dc.createCriteria("milkRemetente")
			.createCriteria("milkRun")
				.add(Restrictions.eq("idMilkRun", idMilkRun));		
		dc.add(Restrictions.eq("tpSemanaDoMes", tipoSemana));

        List result = super.findByDetachedCriteria(dc);
        if(result != null) {
        	return (Short) result.get(0);
        }
       	return null;
    }    
    
    /**
     * Método que busca uma List de SemanasRemetMRun passando como parâmetro 
     * o ID do MilkRun. 
     * 
     * @param idMilkRun
     * @return
     */
	public List findSemanaRemetMRunByIdMilkRun(Long idMilkRun) {        
		return getAdsmHibernateTemplate().find(HQL_FIND_SEMANA_REMET_MRUN_BY_ID_MILK_RUN, idMilkRun);
	}

	/**
	 * Deleta SemanaRemetMRun com o ID do MilkRemetente
	 * 
	 * @param idMilkRemetente
	 */
    public void removeByIdMilkRemetente(Serializable idMilkRemetente) {
        String sql = "delete from " + SemanaRemetMrun.class.getName() + " as srm " +
        			 " where " +
        			 "srm.milkRemetente.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idMilkRemetente);
    }  

}