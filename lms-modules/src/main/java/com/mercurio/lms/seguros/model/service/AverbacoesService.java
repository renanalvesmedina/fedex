package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.Averbacao;
import com.mercurio.lms.seguros.model.AverbacaoAnexo;
import com.mercurio.lms.seguros.model.dao.AverbacoesDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.averbacoesService"
 */
public class AverbacoesService extends CrudService<Averbacao, Long>{

	private AverbacoesAnexoService averbacoesAnexoService;
	
	/**
	 * Recupera uma instância de <code>Averbacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Averbacao findById(java.lang.Long id) {
        return (Averbacao)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {    	
        List<AverbacaoAnexo> anexos = getAverbacoesAnexoService().findAnexosByIdAverbacao(id);
        for (AverbacaoAnexo anexo : anexos) {
        	getAverbacoesAnexoService().removeById(anexo.getIdAverbacaoAnexo());
		}
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
    public java.io.Serializable store(Averbacao bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeAverbacoes(Averbacao bean, ItemList listAnexos) {
    	store(bean);
    	
    	if(listAnexos != null) {
    		for (Object anexos : listAnexos.getNewOrModifiedItems()) {
    			AverbacaoAnexo averbacoesAnexos = (AverbacaoAnexo) anexos;
    			averbacoesAnexos.setAverbacao(bean);
    			averbacoesAnexoService.store(averbacoesAnexos);
    		}
    		
    		for (Object anexos : listAnexos.getRemovedItems()) {
    			AverbacaoAnexo averbacoesAnexos = (AverbacaoAnexo) anexos;
    			averbacoesAnexoService.removeById(averbacoesAnexos.getIdAverbacaoAnexo());
    		}
    	}
    	
        return bean.getIdAverbacao();    	
    }

    public ResultSetPage findPaginatedAverbacoes(TypedFlatMap tfm){
    	List list = new ArrayList();
    	ResultSetPage rsp = getAverbacoesDAO().findPaginatedAverbacoes(FindDefinition.createFindDefinition(tfm), tfm);
    	
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		Object[] result = (Object[]) it.next();
    		Map map = new HashMap(13);
    		map.put("idAverbacao", result[0]);
    		map.put("cliente", FormatUtils.formatCpfCnpj(result[1].toString())  + " " + result[2]);
    		map.put("tpModal", result[3]);
    		map.put("tpFrete", result[4]);
    		map.put("tpSeguro", result[5]);
    		map.put("dtViagem", result[6]);
    		map.put("vlEstimado", result[7]);
    		map.put("psTotal", result[8]);
    		map.put("filialOrigem", result[9]);
    		map.put("filialDestino", result[10]);
    		map.put("nmCorretora", result[11]);
    		map.put("nmSeguradora", result[12]);
    		list.add(map);
    	}
    	
    	rsp.setList(list);
    	return rsp;
    }
    
    public Integer getRowCountAverbacoes(TypedFlatMap tfm){
    	return getAverbacoesDAO().getRowCountAverbacoes(tfm);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAverbacoesDAO(AverbacoesDAO dao){
    	setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public AverbacoesDAO getAverbacoesDAO(){
    	return (AverbacoesDAO) getDao();
    }
    
    public AverbacoesAnexoService getAverbacoesAnexoService() {
		return averbacoesAnexoService;
	}

	public void setAverbacoesAnexoService(
			AverbacoesAnexoService averbacoesAnexoService) {
		this.averbacoesAnexoService = averbacoesAnexoService;
	}    
}
