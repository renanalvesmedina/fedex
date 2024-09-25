package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ImpressoraComputador;
import com.mercurio.lms.expedicao.model.dao.ImpressoraComputadorDAO;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.impressoraComputadorService"
 */
public class ImpressoraComputadorService extends CrudService<ImpressoraComputador, Long> {

	/**
	 * Recupera uma instância de <code>Impressora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ImpressoraComputador findById(java.lang.Long id) {
		ImpressoraComputador impressoraComputador = (ImpressoraComputador)super.findById(id);
		Hibernate.initialize(impressoraComputador.getImpressora().getFilial().getPessoa());
		return impressoraComputador;
	}
	
	public void removeByImpressora(final Long idImpressora) {
		getImpressoraComputadorDAO().removeByImpressora(idImpressora);
	}
	
	public void removeByImpressoras(final List<Long> ids) {
		getImpressoraComputadorDAO().removeByImpressoras(ids);
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
	public java.io.Serializable store(ImpressoraComputador bean) {
		if("M".equals(bean.getImpressora().getTpImpressora().getValue())) {
			List<ImpressoraComputador> list = getImpressoraComputadorDAO().findValidateMatricialUnica(bean.getImpressora().getIdImpressora(), bean.getIdImpressoraComputador());
			if(list != null && list.size() > 0) {
				throw new BusinessException("LMS-04232");
			}
		}
		List<String> tpImpressoras = new ArrayList<String>();
		if("ED".indexOf(bean.getImpressora().getTpImpressora().getValue()) >= 0) {
			tpImpressoras.add("E");
			tpImpressoras.add("D");
		} else {
			tpImpressoras.add(bean.getImpressora().getTpImpressora().getValue());			
		}
		List<ImpressoraComputador> list = getImpressoraComputadorDAO().findValidateMacTpImpressora(bean.getIdImpressoraComputador(), tpImpressoras, bean.getDsMac());
		if(list != null && list.size() > 0) {
			String dispositivos = new String();
			for (ImpressoraComputador impressoraComputador : list) {
				dispositivos = dispositivos.concat(impressoraComputador.getImpressora().getFilial().getSgFilial() + " - " + impressoraComputador.getImpressora().getCheckinLocalizacao()) + ", ";
		}
			dispositivos = dispositivos.substring(0, dispositivos.lastIndexOf(", "));
			throw new BusinessException("LMS-04233", new Object[] {dispositivos});
		}
		
		if("S".equals(bean.getImpressora().getTpImpressora().getValue())){
			if(getImpressoraComputadorDAO().findValidateDWSBalanca(bean.getDsMac(), "B")){
				throw new BusinessException("LMS-04413", new Object[]{"Balança"});				
			}
			
		}
		
		if("B".equals(bean.getImpressora().getTpImpressora().getValue())){
			if(getImpressoraComputadorDAO().findValidateDWSBalanca(bean.getDsMac(), "S")){
				throw new BusinessException("LMS-04413", new Object[]{"DWS-Sist Comp Pesag/Cub"});				
			}
		}
		
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setImpressoraComputadorDAO(ImpressoraComputadorDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ImpressoraComputadorDAO getImpressoraComputadorDAO() {
		return (ImpressoraComputadorDAO) getDao();
	}
}