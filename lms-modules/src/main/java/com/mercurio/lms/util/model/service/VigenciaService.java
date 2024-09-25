package com.mercurio.lms.util.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.Vigencia;
import com.mercurio.lms.util.model.dao.VigenciaDAO;
/**
 * M�todos de tratamento do comportamento de vig�ncias. 
 * 
 * @author Felipe Ferreira
 * @spring.bean id="lms.utils.vigenciaService"
 */
public class VigenciaService {

	private VigenciaDAO dao;
	
	/**
	 * Valida se uma entidade que implementa a interface Vigencia est� vigente no "range" informado.
	 * 
	 * @param vigencia
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * 
	 * @return true se a entidade est� vigente.
	 */
    public boolean validateEntidadeVigente(Vigencia vigencia, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	if (dtVigenciaInicial == null) {
    		throw new IllegalArgumentException("dtVigenciaInicial n�o pode ser null.");
    	}
    	
    	List vigentes = getVigenciaDAO().findEntidadeVigente(vigencia,dtVigenciaInicial,dtVigenciaFinal);
    	
    	return vigentes.size() > 0;
	}
    
    /**
     * Valida se uma entidade que implementa a interface Vigencia est� vigente na data de refer�ncia informada.
     * 
     * @param vigencia
     * @param dtReferencia
     * 
     * @return true se a entidade est� vigente.
     */
    public boolean validateEntidadeVigente(Vigencia vigencia, YearMonthDay dtReferencia) {
    	if (dtReferencia == null) {
    		throw new IllegalArgumentException("dtReferencia n�o pode ser null.");
    	}
    	
    	List vigentes = getVigenciaDAO().findEntidadeVigente(vigencia,dtReferencia);
    	
    	return vigentes.size() > 0;
    }
    
    /**
     * Consulta todas entidades que est�o vigentes no "range" informado.
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
     * Consulta todas entidades que est�o vigentes na data de refer�ncia informada.
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
     * Valida se uma entidade pode ser salva no banco com a vig�ncia setada.
     * 
     * @param vigencia
     */
    public void validaVigenciaBeforeStore(Vigencia vigencia) {
    	validaVigenciaBeforeStore(vigencia,Integer.valueOf(0));
    }
    
    /** valida se uma entidade pode ser salva no banco com a vig�ncia setada. 
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
	 * Recebe data de vig�ncia inicial e compara com data de vigencia inicial antes de ser alterado na tela.
	 * Ap�s, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exce��o
	 * 
	 * @param dtInicial Data de Vig�ncia incial a ser validada.
	 * @param dtInicialOld Data de vig�ncia antes de ser alterada na tela.
	 * @param dtFinal Data de Vig�ncia final a ser validada.  
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
	 * Valida data de vig�ncia inicial para inserir uma entidade.
	 * 
	 * @param YearMonthDay dtVigenciaInicial.
	 * @return  
	 */
	public static void validaVigenciaInsercao(YearMonthDay dtVigenciaInicial) {
		if (dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual()) < 0)
			throw new BusinessException("LMS-00006");
	}
	
	
	/**
	 * Recebe data de vig�ncia inicial da tela e compara com data de vigencia inicial da base.
	 * Ap�s, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exce��o
	 * 
	 * @param dtInicial Data de Vig�ncia incial a ser validada.
	 * @param dtInicialOld Data de vig�ncia antes de ser alterada na tela.
	 * @param dtFinal Data de Vig�ncia final a ser validada.  
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVigenciaDAO(VigenciaDAO dao) {
        this.dao = dao;
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private VigenciaDAO getVigenciaDAO() {
        return (VigenciaDAO)this.dao;
    }
    
    /**
     * Valida se h� dependentes de uma entidade e se os mesmos possuem vig�cnia consistente com a vig�ncia do master.
     * 
     * @param clazz Classe da entidade que deseja-se validar.
     * @param strMaster Nome do atributo do relacionamento que est� na entidade de clazz.
     * @param master Entidade que extende Vigencia e que ter� os par�metros para valida��o.
     * @return
     * 		<b>false</b><br> 
     * 			Se master n�o possuir id;<br>
     * 			Se master n�o possuir filhos;<br>
     * 			Se master possuir filhos, e estes filhos possuem vig�ncia entre a vig�ncia de master;<br>
     * 		<b>true</b><br>
     * 			Se master possuir filhos, e estes filhos possuem vig�ncia "maior" que a vig�ncia de master; 
     */
    public boolean validateIntegridadeVigencia(Class clazz, String strMaster, Vigencia master) {
    	return getVigenciaDAO().validateIntegridadeVigencia(clazz,strMaster,master);
    }
    
    
    
    /**
     * Verifica se a data In�cio Vig�ncia � maior ou igual � data atual. 
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
     * Verifica se a data In�cio Vig�ncia � maior do que a data atual. 
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
