package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.MotoristaRotaViagem;
import com.mercurio.lms.municipios.model.RotaViagem;
import com.mercurio.lms.municipios.model.dao.MotoristaRotaViagemDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.motoristaRotaViagemService"
 */
public class MotoristaRotaViagemService extends CrudService<MotoristaRotaViagem, Long> {

	private VigenciaService vigenciaService;
	
	/**
	 * Recupera uma inst�ncia de <code>MotoristaRotaViagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public MotoristaRotaViagem findById(java.lang.Long id) {
        return (MotoristaRotaViagem)super.findById(id);
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
    public java.io.Serializable store(MotoristaRotaViagem bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotoristaRotaViagemDAO(MotoristaRotaViagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MotoristaRotaViagemDAO getMotoristaRotaViagemDAO() {
        return (MotoristaRotaViagemDAO) getDao();
    }
    
    protected MotoristaRotaViagem beforeStore(MotoristaRotaViagem bean) {
    	MotoristaRotaViagem pojo = (MotoristaRotaViagem)bean;
    	RotaViagem rotaViagem = pojo.getRotaViagem();
		YearMonthDay dtVigenciaInicial = pojo.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = pojo.getDtVigenciaFinal();
		
		vigenciaService.validaVigenciaBeforeStore(pojo);
		// valida se vig�ncia informada 'fecha' com a vig�ncia da rota viagem.
		if (!vigenciaService.validateEntidadeVigente(rotaViagem,
				dtVigenciaInicial,
				dtVigenciaFinal)) {
			throw new BusinessException("LMS-29023");
		}
		
		if (getMotoristaRotaViagemDAO().validateDuplicated(
    			pojo.getIdMotoristaRotaViagem(),
    			dtVigenciaInicial,
    			dtVigenciaFinal,
    			pojo.getMotorista().getIdMotorista(),
    			rotaViagem.getIdRotaViagem()))
    		throw new BusinessException("LMS-00003");
    	return super.beforeStore(bean);
    }
    
    /**
     * Valida a remo��o de um registro de acordo com o padr�o de comportamento de vig�ncias.
     * @param id Id do registro a ser validado.
     */
	private void validaRemoveById(Long id) {
		MotoristaRotaViagem motoristaRotaViagem = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(motoristaRotaViagem);
	}
    
    protected void beforeRemoveById(Long id) {
    	validaRemoveById((Long)id);
		super.beforeRemoveById(id);
    }
    
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    protected void beforeRemoveByIds(List ids) {
    	for (Iterator i = ids.iterator() ; i.hasNext() ; )
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
    }
    
    public List findToConsultarRotas(Long idRotaViagem) {
    	return getMotoristaRotaViagemDAO().findToConsultarRotas(idRotaViagem);
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * 
	 * @param idRotaViagem
	 * @return
	 */
	public List findByIdRotaIdaVolta(Long idRotaViagem) {
		List result = getMotoristaRotaViagemDAO().findByIdRotaIdaVolta(idRotaViagem);
		return new AliasToNestedBeanResultTransformer(MotoristaRotaViagem.class).transformListResult(result);
	}
}