package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ServidorFilial;
import com.mercurio.lms.configuracoes.model.dao.ServidorFilialDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.servidorFilialService"
 */
public class ServidorFilialService extends CrudService<ServidorFilial, Long> {

	/**
	 * 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		
		List<String> included = new ArrayList<String>();
		included.add("filial.idFilial");
		included.add("filial.sgFilial");
		included.add("idServidorFilial");
		included.add("nrIpInicial");
		included.add("nrIpFinal");
		included.add("nrIpServidor");
		
		Map<String, Object> filial = new HashMap<String, Object>();
    	filial.put("idFilial", criteria.get("idFilial"));
    	criteria.remove("idFilial");
    	criteria.put("filial", filial);
    	
		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
		return rsp;
	}

	@Override
	public Long store(ServidorFilial bean) {
		Long r = (Long) super.store(bean); 
		return r;
	}
	
	/**
	 * 
	 */
	public Integer getRowCount(Map criteria) {
		return getServidorFilialDAO().getRowCount(criteria);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>ServidorFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ServidorFilial findById(Long id) {
		return (ServidorFilial) super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	/**
	 * Busca o IP do servidor da Filial onde o IP do computador esta incluido (nrIpInicial <= IP <= nrIpFinal). 
	 * 
	 * @param ip IP de um computador da filial.
	 * @return IP do servidor da filial do usuario.
	 */
	public Long findServerByIp(Long ip) {
		
		return getServidorFilialDAO().findServerByIp(ip);
	}
	
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ServidorFilialDAO getServidorFilialDAO() {
		return (ServidorFilialDAO) getDao();
	}
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setServidorFilialDAO(ServidorFilialDAO dao) {
        setDao( dao );
    }
}
