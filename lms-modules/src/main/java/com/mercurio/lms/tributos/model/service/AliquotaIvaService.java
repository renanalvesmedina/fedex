package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.AliquotaIva;
import com.mercurio.lms.tributos.model.dao.AliquotaIvaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.aliquotaIvaService"
 */
public class AliquotaIvaService extends CrudService<AliquotaIva, Long> {


    protected AliquotaIva beforeStore(AliquotaIva bean) {
		if ( this.verificaExisteVigenciaAliquotaIva((AliquotaIva)bean)) {
			throw new BusinessException("LMS-00047");
		}
		return super.beforeStore(bean);
	}

    /**
     * Busca Percentual da Aliquota vigente para o Pais informado.<BR>
     *@author Robson Edemar Gehl
     * @param idPais
     * @param date
     * @return Percentual da aliquota vigente
     */
    public BigDecimal findAliquotaVigente(Long idPais, YearMonthDay date){
    	return getAliquotaIvaDAO().findAliquotaVigente(idPais, date);
    }
    
    private boolean verificaExisteVigenciaAliquotaIva(AliquotaIva bean){
    	return this.getAliquotaIvaDAO().verificaExisteVigencia(bean);
    }
    
	
	/**
	 * Recupera uma inst�ncia de <code>AliquotaIva</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public AliquotaIva findById(java.lang.Long id) {
        return (AliquotaIva)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(AliquotaIva bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAliquotaIvaDAO(AliquotaIvaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private AliquotaIvaDAO getAliquotaIvaDAO() {
        return (AliquotaIvaDAO) getDao();
    }
   }