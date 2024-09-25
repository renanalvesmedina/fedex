package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.dao.FilialRotaDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.filialRotaService"
 */
public class FilialRotaService extends CrudService<FilialRota, Long> {
	private FilialService filialService;
	
	public List findByIdRota(Long idRota){
		return getFilialRotaDAO().findByIdRota(idRota);
	}


	/**
	 * Recupera uma instância de <code>FilialRota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public FilialRota findById(java.lang.Long id) {
        return (FilialRota)super.findById(id);
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
    public java.io.Serializable store(FilialRota bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFilialRotaDAO(FilialRotaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FilialRotaDAO getFilialRotaDAO() {
        return (FilialRotaDAO) getDao();
    }
    
    //retorna os ids da filialRota onde existir a filialOrigem e a filialDestino
    public List findFilialRotaOrigemDestinoByRota(Long idRota){
    	return getFilialRotaDAO().findFilialRotaOrigemDestinoByRota(idRota);
    }
    
    public FilialRota findFilialRotaOrigemByRota(Long idRota){
    	return getFilialRotaDAO().findFilialRotaOrigemByRota(idRota);
    }
    
    public List findListFilialRotaOrigemByRota(Long idRota){
    	return getFilialRotaDAO().findListFilialRotaOrigemByRota(idRota);
    }

    public List findFiliaisRotaByRota(Long idRota){
    	List lOld = getFilialRotaDAO().findFiliaisRotaByRota(idRota);
    	List l = new ArrayList();
    	
    	for (Iterator i = lOld.iterator() ; i.hasNext() ;) {
    		Map oldMap = (Map)i.next();
    		Set set = oldMap.keySet();
    		Map map = new HashMap();
    		for (Iterator i2 = set.iterator(); i2.hasNext();) {
    			String key = ((String)i2.next());
    			ReflectionUtils.setNestedBeanPropertyValue(map,key.replace('_','.'),
    					ReflectionUtils.getNestedBeanPropertyValue(oldMap,key));
    		}
    		l.add(map);
    	}
    		
    	return l;
    }
    
    public List<FilialRota> findByIdFilialOrigem(Long idFilialOrigem){
    	return getFilialRotaDAO().findByIdFilialOrigem(idFilialOrigem);
    }
    public Boolean existsFilialRotaByIdControleCarga(Long idControleCarga, Long idFilial){
    	return getFilialRotaDAO().existsFilialRotaByIdControleCarga(idControleCarga, idFilial);
    }
    
    /**
     * Retorna lista ordenada com instâncias de Filial correspondentes às filial da rota.
     * @param idRota
     * @return
     */
    public List findFiliaisRotaByRotaPojos(Long idRota){
    	List lOld = getFilialRotaDAO().findFiliaisRotaByRota(idRota);
    	List l = new ArrayList();
    	
    	for (Iterator i = lOld.iterator() ; i.hasNext() ;) {
    		Map map = (Map)i.next();
    		Filial filial = new Filial();
    		
    		filial.setIdFilial((Long)map.get("filial_idFilial"));
    		filial.setSgFilial((String)map.get("filial_sgFilial"));
    		
    		l.add(filial);
    	}
    		
    	return l;
    }
    
    
    /**
     * Retorna uma lista de filiais rotas da rotaIdaVolta ou da rota
     * @param idRota
     * @param idRotaIdaVolta
     * @return
     */   
    public List findFiliaisRotaByRotaOrRotaIdaVolta(Long idRota, Long idRotaIdaVolta){
	    List result = getFilialRotaDAO().findFiliaisRotaByRotaOrRotaIdaVolta(idRota, idRotaIdaVolta);
	    result = new AliasToNestedBeanResultTransformer(FilialRota.class).transformListResult(result);
	    return result;
    }


    /**
     * Busca as filiais de uma rota.
     * 
     * @param idRotaIdaVolta
     * @param idRota
     * @return
     */
    public List findFiliaisRota(Long idRotaIdaVolta, Long idRota) {
   		return getFilialRotaDAO().findFiliaisRota(idRotaIdaVolta, idRota);
    }

    /**
     * Verifica se a filial do usuário logado tem permissão para gerar controle de carga para a 
     * rota recebida por parâmetro.
     * 
     * @param idRota
     * @return True, se a Filial é a filial de origem da rota, caso contrário, False.
     */
    public Boolean validatePermissaoFilialUsuarioParaGerarControleCarga(Long idRota) {
    	Map mapFilial = new HashMap();
    	mapFilial.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());

    	Map mapRota = new HashMap();
    	mapRota.put("idRota", idRota);

    	Map map = new HashMap();
    	map.put("filial", mapFilial);
    	map.put("rota", mapRota);
    	List result = find(map);
    	if (result.isEmpty())
    		throw new BusinessException("LMS-05099");

    	FilialRota fr = findFilialRotaOrigemByRota(idRota);
    	
    	if(!filialService.findIsFilialLMS(fr.getFilial())){
    		throw new BusinessException("LMS-05185"); //TODO daniel
    	}
    	
    	
    	if (fr.getNrOrdem().longValue() == 1)
    		return Boolean.TRUE;
    	
    	return Boolean.FALSE;
    }

	 /**
     * Busca as filiais restantes da rota, a partir do id da filial atual e do id da rota
     * @param idFilialAtual
     * @param idFilialDestino
     * @param idControleCarga
     * @return
     */
    public List<FilialRota> findFiliaisRestantesByRota(Long idRota, Long idFilialAtual) {
    	return getFilialRotaDAO().findFiliaisRestantesByRota(idRota, idFilialAtual);
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}