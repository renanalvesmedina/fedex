package com.mercurio.lms.util.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.Vigencia;
import com.mercurio.lms.util.model.dao.VigenciaDAO;
/**
 * Métodos de tratamento do comportamento de vigências. 
 * 
 * @author Felipe Ferreira
 * @spring.bean id="lms.utils.vigenciaService"
 */
public class VigenciaService {

	private VigenciaDAO dao;
	
	/**
	 * Valida se uma entidade que implementa a interface Vigencia está vigente no "range" informado.
	 * 
	 * @param vigencia
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * 
	 * @return true se a entidade está vigente.
	 */
    public boolean validateEntidadeVigente(Vigencia vigencia, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	if (dtVigenciaInicial == null) {
    		throw new IllegalArgumentException("dtVigenciaInicial não pode ser null.");
    	}
    	
    	List vigentes = getVigenciaDAO().findEntidadeVigente(vigencia,dtVigenciaInicial,dtVigenciaFinal);
    	
    	return vigentes.size() > 0;
	}
    
    /**
     * Valida se uma entidade que implementa a interface Vigencia está vigente na data de referência informada.
     * 
     * @param vigencia
     * @param dtReferencia
     * 
     * @return true se a entidade está vigente.
     */
    public boolean validateEntidadeVigente(Vigencia vigencia, YearMonthDay dtReferencia) {
    	if (dtReferencia == null) {
    		throw new IllegalArgumentException("dtReferencia não pode ser null.");
    	}
    	
    	List vigentes = getVigenciaDAO().findEntidadeVigente(vigencia,dtReferencia);
    	
    	return vigentes.size() > 0;
    }
    
    /**
     * Consulta todas entidades que estão vigentes no "range" informado.
     * 
     * @param clazz
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * 
     * @return List com todas as etidades vigentes encontradas tipadas na classe informada. 
     */
    public List findEntidadesVigentes(Class clazz, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	return getVigenciaDAO().findEntidadesVigentes(clazz,dtVigenciaInicial,dtVigenciaFinal);
    }
    
    /**
     * Consulta todas entidades que estão vigentes na data de referência informada.
     * 
     * @param clazz
     * @param dtReferencia
     * 
     * @return List com todas as etidades vigentes encontradas tipadas na classe informada. 
     */
    public List findEntidadesVigentes(Class clazz, YearMonthDay dtReferencia) {
    	return getVigenciaDAO().findEntidadesVigentes(clazz,dtReferencia);
    }
    
    
    /**
     * Valida se uma entidade pode ser salva no banco com a vigência setada.
     * 
     * @param vigencia
     */
    public void validaVigenciaBeforeStore(Vigencia vigencia) {
    	validaVigenciaBeforeStore(vigencia,Integer.valueOf(0));
    }
    
    /** valida se uma entidade pode ser salva no banco com a vigência setada. 
     * o ultimo parametro possibilita fechar a vigencia um dia apos a data atual
     * @param vigencia
     * @param isFinalizarVigenciaOntem
     */
    public void validaVigenciaBeforeStore(Vigencia vigencia, Integer diasAnterioresHoje) {
    	
    	Long id = getVigenciaDAO().getIdentifierFromBean(vigencia);
    	
    	YearMonthDay dtVigenciaInicial = vigencia.getDtVigenciaInicial();
    	
    	if (id == null) {
    		validaVigenciaInsercao(dtVigenciaInicial);
    	} else {
    		Vigencia vigenciaOld = (Vigencia)getVigenciaDAO().getHibernateTemplate()
					.get(vigencia.getClass(),id);
    		
			YearMonthDay dtVigenciaFinal = vigencia.getDtVigenciaFinal();
			YearMonthDay dtVigenciaInicialOld = vigenciaOld.getDtVigenciaInicial();
			
			getVigenciaDAO().getHibernateTemplate().evict(vigenciaOld);
	    	
	    	validaVigenciaBeforeStore(dtVigenciaInicial,dtVigenciaInicialOld,dtVigenciaFinal,diasAnterioresHoje);
    	}
    }

	/**
	 * Recebe data de vigência inicial e compara com data de vigencia inicial antes de ser alterado na tela.
	 * Após, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param dtInicial Data de Vigência incial a ser validada.
	 * @param dtInicialOld Data de vigência antes de ser alterada na tela.
	 * @param dtFinal Data de Vigência final a ser validada.  
	 */
	private void validaVigenciaBeforeStore(YearMonthDay dtInicial, YearMonthDay dtInicialOld, YearMonthDay dtFinal, Integer diasAnterioresHoje) {
		if (dtInicialOld != null) {
			if (dtInicialOld.compareTo(dtInicial) != 0) {
				validaVigenciaInsercao(dtInicial);
			}
		}
		
		if(dtFinal != null){
			YearMonthDay dtFimBase = JTDateTimeUtils.getDataAtual().minusDays(diasAnterioresHoje);

			if (dtFinal.compareTo(dtFimBase) < 0) {
				throw (diasAnterioresHoje.intValue() == 0) ? new BusinessException("LMS-00007") : new BusinessException("LMS-27054",new Object[]{diasAnterioresHoje});
			}
		}
	}
    
	/**
	 * Valida data de vigência inicial para inserir uma entidade.
	 * 
	 * @param YearMonthDay dtVigenciaInicial.
	 * @return  
	 */
	public static void validaVigenciaInsercao(YearMonthDay dtVigenciaInicial) {
		if (dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual()) < 0)
			throw new BusinessException("LMS-00006");
	}
	
	
	/**
	 * Recebe data de vigência inicial da tela e compara com data de vigencia inicial da base.
	 * Após, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param dtInicial Data de Vigência incial a ser validada.
	 * @param dtInicialOld Data de vigência antes de ser alterada na tela.
	 * @param dtFinal Data de Vigência final a ser validada.  
	 */
	public void validaVigenciaBeforeRemove(YearMonthDay dtInicial, YearMonthDay dtFinal) {
		if(dtInicial != null){
			if (dtInicial.compareTo(JTDateTimeUtils.getDataAtual())  <= 0){
				throw new BusinessException("LMS-00005");
			}
		}
		
		if(dtFinal != null){
			if (dtFinal.compareTo(JTDateTimeUtils.getDataAtual()) <= 0) {
				throw new BusinessException("LMS-00007");
			}
		}
	}

	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVigenciaDAO(VigenciaDAO dao) {
        this.dao = dao;
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VigenciaDAO getVigenciaDAO() {
        return (VigenciaDAO)this.dao;
    }
    
    /**
     * Valida se há dependentes de uma entidade e se os mesmos possuem vigêcnia consistente com a vigência do master.
     * 
     * @param clazz Classe da entidade que deseja-se validar.
     * @param strMaster Nome do atributo do relacionamento que está na entidade de clazz.
     * @param master Entidade que extende Vigencia e que terá os parâmetros para validação.
     * @return
     * 		<b>false</b><br> 
     * 			Se master não possuir id;<br>
     * 			Se master não possuir filhos;<br>
     * 			Se master possuir filhos, e estes filhos possuem vigência entre a vigência de master;<br>
     * 		<b>true</b><br>
     * 			Se master possuir filhos, e estes filhos possuem vigência "maior" que a vigência de master; 
     */
    public boolean validateIntegridadeVigencia(Class clazz, String strMaster, Vigencia master) {
    	return getVigenciaDAO().validateIntegridadeVigencia(clazz,strMaster,master);
    }
    
    
    
    /**
     * Verifica se a data Início Vigência é maior ou igual à data atual. 
     * @param dtInicioVigencia
     * @param errorMessageKey
     */
    public void validateInicioVigencia(YearMonthDay dtVigenciaInicial, String errorMessageKey) {
    	if(dtVigenciaInicial == null) {
    		throw new BusinessException("LMS-01114");
    	}
    	int diferenca = dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual());
    	if(diferenca < 0) {
    		throw new BusinessException(errorMessageKey);
    	}
    	
    }
    
    /**
     * Verifica se a data Início Vigência é maior do que a data atual. 
     * @param dtInicioVigencia
     * @param errorMessageKey
     */
    public void validateInicioVigencia2(YearMonthDay dtVigenciaInicial, String errorMessageKey) {
    	if(dtVigenciaInicial == null) {
    		throw new BusinessException("LMS-01114");
    	}
    	int diferenca = dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual());
    	if(diferenca <= 0) {
    		throw new BusinessException(errorMessageKey);
    	}
    	
    }

    
}
