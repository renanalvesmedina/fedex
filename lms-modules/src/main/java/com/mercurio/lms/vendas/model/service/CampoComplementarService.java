package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.CampoComplementar;
import com.mercurio.lms.vendas.model.dao.CampoComplementarDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.campoComplementarService"
 */
public class CampoComplementarService extends CrudService<CampoComplementar, Long> {

	/**
	 * Recupera uma inst�ncia de <code>CampoComplementar</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CampoComplementar findById(java.lang.Long id) {
		return (CampoComplementar)super.findById(id);
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
	public java.io.Serializable store(CampoComplementar bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCampoComplementarDAO(CampoComplementarDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CampoComplementarDAO getCampoComplementarDAO() {
		return (CampoComplementarDAO) getDao();
	}

	/**
	 * Ver regra de neg�cio "Ao clicar em salvar".
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	protected CampoComplementar beforeStore(CampoComplementar bean) {
		CampoComplementar cc = (CampoComplementar)bean;
		if("D".equalsIgnoreCase(cc.getTpCampoComplementar().getValue()) 
				&& StringUtils.isBlank(cc.getDsFormatacao()))
				throw new BusinessException("LMS-01003");
		if((StringUtils.isNotBlank(cc.getDsFormatacao()) && cc.getNrTamanho() !=null)  
			 || (StringUtils.isBlank(cc.getDsFormatacao()) && cc.getNrTamanho() ==null) ) {
			if("A".equalsIgnoreCase(cc.getTpCampoComplementar().getValue()))
				throw new BusinessException("LMS-01001");
			else if("N".equalsIgnoreCase(cc.getTpCampoComplementar().getValue()))	
				throw new BusinessException("LMS-01002");	
		}
		return super.beforeStore(bean);
	}

}
