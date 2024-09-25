package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.AliquotaIcmsAereo;
import com.mercurio.lms.tributos.model.dao.AliquotaIcmsAereoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="systemName.lms.tributos.aliquotaIcmsAereoService"
 */
public class AliquotaIcmsAereoService extends CrudService<AliquotaIcmsAereo, Long> {


	/**
	 * Recupera uma inst�ncia de <code>AliquotaIcmsAereo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public AliquotaIcmsAereo findById(java.lang.Long id) {
        return (AliquotaIcmsAereo)super.findById(id);
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
    public java.io.Serializable store(AliquotaIcmsAereo bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna a AliquotaIcmsAereo vigente por unidade federativa.
     * 
     * @author Micka�l Jalbert
     * @since 26/05/2006
     * 
     * @param Long idUnidadeFederativa
     * @param YearMonthDay dtVigencia
     * 
     * @return AliquotaIcmsAereo
     * */
    public AliquotaIcmsAereo findVigenteByUF(Long idUnidadeFederativa, YearMonthDay dtVigencia){
    	return getAliquotaIcmsAereoDAO().findVigenteByUF(idUnidadeFederativa, dtVigencia);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAliquotaIcmsAereoDAO(AliquotaIcmsAereoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private AliquotaIcmsAereoDAO getAliquotaIcmsAereoDAO() {
        return (AliquotaIcmsAereoDAO) getDao();
    }
   }