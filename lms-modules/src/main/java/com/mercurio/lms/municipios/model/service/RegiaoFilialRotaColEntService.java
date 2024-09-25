package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.dao.RegiaoFilialRotaColEntDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.regiaoFilialRotaColEntService"
 */
public class RegiaoFilialRotaColEntService extends CrudService<RegiaoFilialRotaColEnt, Long> {
	private VigenciaService vigenciaService;
	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	
	public void setRegiaoColetaEntregaFilService(
			RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	//recebe o id da regiao de coleta/entrega + a data de vigencia final - verifica se a regiao filial rota tem a vigencia final maior que que vigencia final da regiao de coleta, se tem ...a vigencia final da regiao de coleta nao poderá ser alterada
	public void verificaVigenciaRegiaoFilialRota(Long idRegiaoColetaEntregaFilial,YearMonthDay dtVigenciaInicial ,YearMonthDay dtVigenciaFinal){
		if (getRegiaoFilialRotaColEntDAO().verificaVigenciaVigenteRegiaoFilialRota(idRegiaoColetaEntregaFilial,dtVigenciaInicial,dtVigenciaFinal)){
			throw new BusinessException("LMS-29031");
		}
	}

	/**
	 * Recupera uma instância de <code>RegiaoFilialRotaColEnt</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RegiaoFilialRotaColEnt findById(java.lang.Long id) {
        return (RegiaoFilialRotaColEnt)super.findById(id);
    }

    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = (RegiaoFilialRotaColEnt) super.findById(id);
        TypedFlatMap mapRegiaoFilialRotaColEnt = new TypedFlatMap();
        mapRegiaoFilialRotaColEnt.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(regiaoFilialRotaColEnt));
        final RotaColetaEntrega rotaColetaEntrega = regiaoFilialRotaColEnt.getRotaColetaEntrega();
		final Filial filial = rotaColetaEntrega.getFilial();
		mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.filial.idFilial",filial.getIdFilial());
        mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.filial.sgFilial",filial.getSgFilial());
        mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
        mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.idRotaColetaEntrega",rotaColetaEntrega.getIdRotaColetaEntrega());
        
        mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.nrRota",rotaColetaEntrega.getNrRota());
        mapRegiaoFilialRotaColEnt.put("rotaColetaEntrega.dsRota",rotaColetaEntrega.getDsRota());
        mapRegiaoFilialRotaColEnt.put("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil",regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getIdRegiaoColetaEntregaFil());
        mapRegiaoFilialRotaColEnt.put("dtVigenciaInicial",regiaoFilialRotaColEnt.getDtVigenciaInicial());
        mapRegiaoFilialRotaColEnt.put("dtVigenciaFinal",regiaoFilialRotaColEnt.getDtVigenciaFinal());
        
        mapRegiaoFilialRotaColEnt.put("regiao.dtVigenciaInicial",regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDtVigenciaInicial());
        mapRegiaoFilialRotaColEnt.put("regiao.dtVigenciaFinal",regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDtVigenciaFinal());
        
        mapRegiaoFilialRotaColEnt.put("idRegiaoFilialRotaColEnt", regiaoFilialRotaColEnt.getIdRegiaoFilialRotaColEnt());
             
    	return mapRegiaoFilialRotaColEnt;
    }
               
	protected void beforeRemoveById(Long id) {
		RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(regiaoFilialRotaColEnt);
		super.beforeRemoveById(id);
	}
	
    protected void beforeRemoveByIds(List ids) {
    	RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = null;
    	for(int x = 0; x < ids.size(); x++) {
    		regiaoFilialRotaColEnt = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(regiaoFilialRotaColEnt);
    	}
    	super.beforeRemoveByIds(ids);
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
    public java.io.Serializable store(RegiaoFilialRotaColEnt bean) {
        return super.store(bean);
    }

    protected RegiaoFilialRotaColEnt beforeStore(RegiaoFilialRotaColEnt bean) {
    	RegiaoFilialRotaColEnt regiaoFilial = (RegiaoFilialRotaColEnt) bean;
    	
    	if(!regiaoColetaEntregaFilService.findIntervaloRegiaoColetaEntrega(regiaoFilial.getRegiaoColetaEntregaFil().getIdRegiaoColetaEntregaFil(),regiaoFilial.getDtVigenciaInicial(), regiaoFilial.getDtVigenciaFinal())
    		|| !rotaColetaEntregaService.findRotaColetaEntregaValidaVigencias(regiaoFilial.getRotaColetaEntrega().getIdRotaColetaEntrega(), regiaoFilial.getDtVigenciaInicial(), regiaoFilial.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (getRegiaoFilialRotaColEntDAO().verificaVigenciaRota(regiaoFilial.getIdRegiaoFilialRotaColEnt(), regiaoFilial.getRotaColetaEntrega().getIdRotaColetaEntrega(), regiaoFilial.getDtVigenciaInicial(), regiaoFilial.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-00003");
    	
    	return super.beforeStore(bean);
    }
     
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map map) {
    	RegiaoFilialRotaColEnt bean = new RegiaoFilialRotaColEnt();
    	
    	ReflectionUtils.copyNestedBean(bean, map);

    	vigenciaService.validaVigenciaBeforeStore(bean);
		    	    	
        super.store(bean);
        map.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(bean));
        map.put("idRegiaoFilialRotaColEnt", bean.getIdRegiaoFilialRotaColEnt());
    	return map;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRegiaoFilialRotaColEntDAO(RegiaoFilialRotaColEntDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RegiaoFilialRotaColEntDAO getRegiaoFilialRotaColEntDAO() {
        return (RegiaoFilialRotaColEntDAO) getDao();
    }

    
	/**
	 * Recupera uma instância de <code>RegiaoFilialRotaColEnt</code> a partir do ID
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RegiaoFilialRotaColEnt findByIdVigencia(java.lang.Long id) {
        return (RegiaoFilialRotaColEnt)super.findById(id);
    }
    

    /**
	 * Recupera instâncias de <code>RegiaoFilialRotaColEnt</code> que estejam vigentes.
	 * @param criteria 
	 * @return
	 */
    public List findByVigencia(Map criteria) {
        return getRegiaoFilialRotaColEntDAO().findByVigencia(criteria);
    }

	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
}