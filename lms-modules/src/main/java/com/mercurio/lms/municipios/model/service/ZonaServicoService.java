package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.ZonaServico;
import com.mercurio.lms.municipios.model.dao.ZonaServicoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.zonaServicoService"
 */
public class ZonaServicoService extends CrudService<ZonaServico, Long> {

	private VigenciaService vigenciaService;
	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * Recupera uma instância de <code>ZonaServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ZonaServico findById(java.lang.Long id) {
        return (ZonaServico)super.findById(id);
    }
 
    //metodo chamado pelo callback do form para validar datas de vigencia e mandar flags para habilitar ou desabilitar campos no detalhamento
    public TypedFlatMap findByIdEValidaVigencias(Long id){
    	ZonaServico zonaServico = (ZonaServico)findById(id);
    	
    	TypedFlatMap mapZonaServico = new TypedFlatMap();
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(zonaServico);
    	mapZonaServico.put("acaoVigenciaAtual", acaoVigencia);
    	mapZonaServico.put("zona.idZona", zonaServico.getZona().getIdZona());
    	mapZonaServico.put("servico.idServico", zonaServico.getServico().getIdServico());
    	mapZonaServico.put("dtVigenciaInicial", zonaServico.getDtVigenciaInicial());
    	mapZonaServico.put("dtVigenciaFinal", zonaServico.getDtVigenciaFinal());
    	mapZonaServico.put("idZonaServico", zonaServico.getIdZonaServico());
    	
    	return mapZonaServico;
    }

    /* Cria lista para trazer no bean apenas os campos apresentados no Grid. 
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
	public ResultSetPage findPaginated(Map criteria) {
		List included = new ArrayList();
        included.add("idZonaServico");
        included.add("zona.dsZona");
        included.add("zona.tpSituacao");
        included.add("servico.tpSituacao");
        included.add("servico.dsServico");
        included.add("dtVigenciaInicial");
        included.add("dtVigenciaFinal");
 
        ResultSetPage rsp = super.findPaginated(criteria);
        rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
 
        return rsp;
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
    public java.io.Serializable store(ZonaServico bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
        ZonaServico zonaServico = new ZonaServico();

        ReflectionUtils.copyNestedBean(zonaServico,bean);

        vigenciaService.validaVigenciaBeforeStore(zonaServico);
        super.store(zonaServico);
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(zonaServico);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idZonaServico",zonaServico.getIdZonaServico());
        return bean;
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setZonaServicoDAO(ZonaServicoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ZonaServicoDAO getZonaServicoDAO() {
        return (ZonaServicoDAO) getDao();
    }
    
    protected ZonaServico beforeInsert(ZonaServico bean) {
    	return super.beforeInsert(bean);
    }
    
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	protected ZonaServico beforeStore(ZonaServico bean) {
		ZonaServico zonaServico = (ZonaServico)bean;
		
		if (getZonaServicoDAO().consultarVigenciasServicoAvancado(zonaServico)) {
			throw new BusinessException("LMS-00003");
		}
		zonaServico.getZona().setTpSituacao(null);
		return super.beforeStore(bean);
	}

	protected void beforeRemoveById(Long id) {
		ZonaServico zonaServico = (ZonaServico)findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(zonaServico);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			ZonaServico zonaServico = (ZonaServico)findById((Long)ids.get(i));
			JTVigenciaUtils.validaVigenciaRemocao(zonaServico);
		}
		super.beforeRemoveByIds(ids);
	}
}