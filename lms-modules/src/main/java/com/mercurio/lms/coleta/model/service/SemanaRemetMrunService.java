package com.mercurio.lms.coleta.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeConstants;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.SemanaRemetMrun;
import com.mercurio.lms.coleta.model.dao.SemanaRemetMrunDAO;
import com.mercurio.lms.municipios.model.Filial;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.semanaRemetMrunService"
 */
public class SemanaRemetMrunService extends CrudService<SemanaRemetMrun, Long> {


	/**
	 * Recupera uma instância de <code>SemanaRemetMrun</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SemanaRemetMrun findById(java.lang.Long id) {
        return getSemanaRemetMrunDAO().findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Deleta SemanaRemetMRun com o ID do MilkRemetente
	 * 
	 * @param idMilkRemetente
	 */    
    public void removeByIdMilkRemetente(Long idMilkRemetente) {
    	this.getSemanaRemetMrunDAO().removeByIdMilkRemetente(idMilkRemetente);
    }    
    
	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(SemanaRemetMrun bean) {
        return super.store(bean);
    }
    
	public List findSemanaByIdMilkRemetente(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("milkRemetente.idMilkRemetente", id);		
    	List semanaRemetMilk = find(criteria);
    	return semanaRemetMilk;
	}
    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSemanaRemetMrunDAO(SemanaRemetMrunDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SemanaRemetMrunDAO getSemanaRemetMrunDAO() {
        return (SemanaRemetMrunDAO) getDao();
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
        return getSemanaRemetMrunDAO().findSemanasRemetMrunByFaixaDiasAndFilialAtendeOperacional(filial, siglaFaixa);
    }
    
    /**
     * Para uma <code>SemanaRemetMrun</code> retorna a hora inicial de acordo com o dia
     * da semana passado por parâmetro. Caso o dia da semana não estiver nesse intervalo, retorna null;
     * @param semanaRemetMrun
     * @param diaSemana
     * @return Time
     */
    public TimeOfDay getHrInicialSemanaRemetMrun(SemanaRemetMrun semanaRemetMrun, int diaSemana){
        switch (diaSemana) {
        case DateTimeConstants.MONDAY: //Segunda-feira
            if (semanaRemetMrun.getNrSegundaFeira()!=null){
                if (semanaRemetMrun.getNrSegundaFeira().intValue()>0){
                    return semanaRemetMrun.getHrInicialSegundaFeira();
                }
            }
        case DateTimeConstants.TUESDAY: //Terca-feira
            if (semanaRemetMrun.getNrTercaFeira()!=null){
                if (semanaRemetMrun.getNrTercaFeira().intValue()>0){
                    return semanaRemetMrun.getHrInicialTercaFeira();
                }
            }
        case DateTimeConstants.WEDNESDAY: //Quarta-feira
            if (semanaRemetMrun.getNrQuartaFeira()!=null){
                if (semanaRemetMrun.getNrQuartaFeira().intValue()>0){
                    return semanaRemetMrun.getHrInicialQuartaFeira();
                }
            }
        case DateTimeConstants.THURSDAY: //Quinta-feira
            if (semanaRemetMrun.getNrQuintaFeira()!=null){
                if (semanaRemetMrun.getNrQuintaFeira().intValue()>0){
                    return semanaRemetMrun.getHrInicialQuintaFeira();
                }
            }
        case DateTimeConstants.FRIDAY: //Sexta-feira
            if (semanaRemetMrun.getNrSextaFeira()!=null){
                if (semanaRemetMrun.getNrSextaFeira().intValue()>0){
                    return semanaRemetMrun.getHrInicialSextaFeira();
                }
            }
        case DateTimeConstants.SATURDAY: //Sábado
            if (semanaRemetMrun.getNrSabado()!=null){
                if (semanaRemetMrun.getNrSabado().intValue()>0){
                    return semanaRemetMrun.getHrInicialSabado();
                }
            }
        case DateTimeConstants.SUNDAY: //Domingo
            if (semanaRemetMrun.getNrDomingo()!=null){
                if (semanaRemetMrun.getNrDomingo().intValue()>0){
                    return semanaRemetMrun.getHrInicialDomingo();
                }
            }            
        default:
            return null;
        }
    }
    
    /**
     * Para uma <code>SemanaRemetMrun</code> retorna a hora final de acordo com o dia
     * da semana passado por parâmetro. Caso o dia da semana não estiver nesse intervalo, retorna null;
     * @param semanaRemetMrun
     * @param diaSemana
     * @return Time
     */
    public TimeOfDay getHrFinalSemanaRemetMrun(SemanaRemetMrun semanaRemetMrun, int diaSemana){
        switch (diaSemana) {
        case DateTimeConstants.MONDAY: //Segunda-feira
            if (semanaRemetMrun.getNrSegundaFeira()!=null){
                if (semanaRemetMrun.getNrSegundaFeira().intValue()>0){
                    return semanaRemetMrun.getHrFinalSegundaFeira();
                }
            }
        case DateTimeConstants.TUESDAY: //Terca-feira
            if (semanaRemetMrun.getNrTercaFeira()!=null){
                if (semanaRemetMrun.getNrTercaFeira().intValue()>0){
                    return semanaRemetMrun.getHrFinalTercaFeira();
                }
            }
        case DateTimeConstants.WEDNESDAY: //Quarta-feira
            if (semanaRemetMrun.getNrQuartaFeira()!=null){
                if (semanaRemetMrun.getNrQuartaFeira().intValue()>0){
                    return semanaRemetMrun.getHrFinalQuartaFeira();
                }
            }
        case DateTimeConstants.THURSDAY: //Quinta-feira
            if (semanaRemetMrun.getNrQuintaFeira()!=null){
                if (semanaRemetMrun.getNrQuintaFeira().intValue()>0){
                    return semanaRemetMrun.getHrFinalQuintaFeira();
                }
            }
        case DateTimeConstants.FRIDAY: //Sexta-feira
            if (semanaRemetMrun.getNrSextaFeira()!=null){
                if (semanaRemetMrun.getNrSextaFeira().intValue()>0){
                    return semanaRemetMrun.getHrFinalSextaFeira();
                }
            }
        case DateTimeConstants.SATURDAY: //Sábado
            if (semanaRemetMrun.getNrSabado()!=null){
                if (semanaRemetMrun.getNrSabado().intValue()>0){
                    return semanaRemetMrun.getHrFinalSabado();
                }
            }
        case DateTimeConstants.SUNDAY: //Domingo
            if (semanaRemetMrun.getNrDomingo()!=null){
                if (semanaRemetMrun.getNrDomingo().intValue()>0){
                    return semanaRemetMrun.getHrFinalDomingo();
                }
            }            
        default:
            return null;
        }
    }
    
	/**
     * Retorna o Maior Número do dia da semana passado por parâmetro existente na Tabela SEMANA_REMET_MRUN
     *
     * diaSemana pode ter os seguintes parâmetros:
     * -> nrSegundaFeria
     * -> nrTercaFeria
     * -> nrQuartaFeria
     * -> nrQuintaFeria
     * -> nrSextaFeria
     * -> nrSabado
     * -> nrDomingo
     * 
     * @param idMilkRun
     * @param diaSemana
     * @return
     */
    public Short findMaiorNrDiaSemana(Long idMilkRun, String tipoSemana, String diaSemana) {
        return getSemanaRemetMrunDAO().findMaiorNrDiaSemana(idMilkRun, tipoSemana, diaSemana);
    }
    
    public List findSemanaRemetMRunByIdMilkRun(Long idMilkRun) {
    	return getSemanaRemetMrunDAO().findSemanaRemetMRunByIdMilkRun(idMilkRun);
    }
    
}