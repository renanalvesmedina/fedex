package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.dao.ImpressoraDAO;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.impressoraService"
 */
public class ImpressoraService extends CrudService<Impressora, Long> {

	private ImpressoraComputadorService impressoraComputadorService;
	private DomainValueService domainValueService;
	
	/**
	 * Recupera uma instância de <code>Impressora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Impressora findById(java.lang.Long id) {
		Impressora impressora = (Impressora)super.findById(id);
		if (impressora.getBalanca() != null) {//Carrega balança
			impressora.getBalanca().getIdBalanca();
	}
		return impressora;
	}

	/**
	 * Busca as Impressoras com dados específicos para Combobox
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public List findCombo(Map map){
		return getImpressoraDAO().findCombo(map);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		impressoraComputadorService.removeByImpressora(id);
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
		impressoraComputadorService.removeByImpressoras(ids);
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Impressora bean) {
		String tpImpressora = bean.getTpImpressora().getValue();
		if ("E".equals(tpImpressora) || "D".equals(tpImpressora) || "W".equals(tpImpressora) || "P".equals(tpImpressora)) {
			bean.setBlEtiquetaNova(Boolean.TRUE);
		} else {
			bean.setBlEtiquetaNova(Boolean.FALSE);
		}
		if (bean.getBalanca() != null && !"B".equals(bean.getTpImpressora().getValue())) {
			bean.setBalanca(null);
		}
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setImpressoraDAO(ImpressoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ImpressoraDAO getImpressoraDAO() {
		return (ImpressoraDAO) getDao();
	}

	public List findImpressorasByIdFilial(java.lang.Long id) {
		Map map = new HashMap();
		map.put("filial.idFilial",id);
		return find(map);
	}

	public Impressora findByIdFilial(java.lang.Long id) {
		Map map = new HashMap();
		map.put("idFilial",id);
		List l = find(map);
		if(!l.isEmpty()) {
			return (Impressora) l.get(0);
		}
		return null;
	}

	public Impressora findImpressoraUsuario(Long idFilial, String dsMacAddress) {
		return findImpressoraUsuario(idFilial, dsMacAddress, null);
	}
	
	public List findValidateIpInformadoImpressora(Long idImpressora, Long nrIp) {
		return getImpressoraDAO().findValidateIpInformadoImpressora(idImpressora, nrIp);
	}
	
	public Map findDispositivoComputadorUsuario(Long idFilial, String dsMacAddress, String tpImpressora) {
		List<String> tpImpressoras = new ArrayList<String>();
		if("EDPW".indexOf(tpImpressora) >= 0) {
			tpImpressoras.add("E");
			tpImpressoras.add("D");
			tpImpressoras.add("P");
			tpImpressoras.add("W");
		} else {
			tpImpressoras.add(tpImpressora);			
			tpImpressoras.add("S");
		}
		List<Map> result = getImpressoraDAO().findDispositivoComputadorUsuario(idFilial, dsMacAddress, tpImpressoras);
		DomainValue tipoImpressora = domainValueService.findDomainValueByValue("DM_TIPO_IMPRESSORA", tpImpressora);

		Impressora impressora = null;
		if(result != null && result.size() == 1) {
			Map map = (Map) result.get(0);
			impressora = (Impressora) map.get("impressora");
			if (impressora.getBalanca() != null) {
				impressora.getBalanca().getIdBalanca();
			}
			if(impressora == null) {
				throw new BusinessException("LMS-04136", new Object[] {tipoImpressora.getDescriptionAsString()});
			}
			return map;
		} else {
			throw new BusinessException("LMS-04136", new Object[] {tipoImpressora.getDescriptionAsString()});
		}
	}
	
	public Impressora findImpressoraUsuario(Long idFilial, String dsMacAddress, String tpImpressora) {
		List<String> tpImpressoras = new ArrayList<String>();
		if("EDPW".indexOf(tpImpressora) >= 0) {
			tpImpressoras.add("E");
			tpImpressoras.add("D");
			tpImpressoras.add("P");
			tpImpressoras.add("W");
		} else {
			tpImpressoras.add(tpImpressora);			
		}
		List result = getImpressoraDAO().findImpressoraUsuario(idFilial, dsMacAddress, tpImpressoras);
		DomainValue tipoImpressora = domainValueService.findDomainValueByValue("DM_TIPO_IMPRESSORA", tpImpressora);

		Impressora impressora = null;
		if(result != null && result.size() == 1) {
			impressora = (Impressora) result.get(0);
		if(impressora == null) {
				throw new BusinessException("LMS-04136", new Object[] {tipoImpressora.getDescriptionAsString()});
		}
		return impressora;
		} else {
			throw new BusinessException("LMS-04136", new Object[] {tipoImpressora.getDescriptionAsString()});
	}
	}

	public ImpressoraComputadorService getImpressoraComputadorService() {
		return impressoraComputadorService;
	}

	public void setImpressoraComputadorService(
			ImpressoraComputadorService impressoraComputadorService) {
		this.impressoraComputadorService = impressoraComputadorService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
