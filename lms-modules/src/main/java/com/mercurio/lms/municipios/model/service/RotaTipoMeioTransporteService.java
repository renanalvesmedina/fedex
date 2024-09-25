package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaMeioTransporteRodov;
import com.mercurio.lms.municipios.model.RotaTipoMeioTransporte;
import com.mercurio.lms.municipios.model.dao.RotaTipoMeioTransporteDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rotaTipoMeioTransporteService"
 */
public class RotaTipoMeioTransporteService extends CrudService<RotaTipoMeioTransporte, Long> {
	
	private VigenciaService vigenciaService;
	
	private RotaColetaEntregaService rotaColetaEntregaService;

	/**
	 * Recupera uma instância de <code>RotaTipoMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	@Override
    public RotaTipoMeioTransporte findById(Long id) {
        return (RotaTipoMeioTransporte)super.findById(id);
    }

    public List<RotaTipoMeioTransporte> findRotaTipoMeioTransporteById(Long idRotaTipoMeioTransporte, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	return getRotaTipoMeioTransporteDAO().findRotaTipoMeioTransporteById(idRotaTipoMeioTransporte, dtVigenciaInicial, dtVigenciaFinal);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(RotaTipoMeioTransporte bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRotaTipoMeioTransporteDAO(RotaTipoMeioTransporteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RotaTipoMeioTransporteDAO getRotaTipoMeioTransporteDAO() {
        return (RotaTipoMeioTransporteDAO) getDao();
    }
    
    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) { 
    	RotaTipoMeioTransporte rotaTipoMeioTransporte = (RotaTipoMeioTransporte)super.findById(id);
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rotaTipoMeioTransporte);
    	TypedFlatMap mapRotaTipoMeioTransporte = new TypedFlatMap();
    	
    	mapRotaTipoMeioTransporte.put("acaoVigenciaAtual",acaoVigencia);
    	final RotaColetaEntrega rotaColetaEntrega = rotaTipoMeioTransporte.getRotaColetaEntrega();
		final Filial filial = rotaColetaEntrega.getFilial();
		mapRotaTipoMeioTransporte.put("rotaColetaEntrega.filial.idFilial",filial.getIdFilial());
    	mapRotaTipoMeioTransporte.put("rotaColetaEntrega.filial.sgFilial",filial.getSgFilial());
    	mapRotaTipoMeioTransporte.put("rotaColetaEntrega.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	mapRotaTipoMeioTransporte.put("rotaColetaEntrega.idRotaColetaEntrega",rotaColetaEntrega.getIdRotaColetaEntrega());
    	mapRotaTipoMeioTransporte.put("rotaColetaEntrega.nrRota",rotaColetaEntrega.getNrRota());
    	mapRotaTipoMeioTransporte.put("rotaColetaEntrega.dsRota",rotaColetaEntrega.getDsRota());
    	final TipoMeioTransporte tipoMeioTransporte = rotaTipoMeioTransporte.getTipoMeioTransporte();
		mapRotaTipoMeioTransporte.put("tipoMeioTransporte.idTipoMeioTransporte",tipoMeioTransporte.getIdTipoMeioTransporte());
    	mapRotaTipoMeioTransporte.put("tipoMeioTransporte.dsTipoMeioTransporte",tipoMeioTransporte.getDsTipoMeioTransporte());
    	mapRotaTipoMeioTransporte.put("tipoMeioTransporte.tpMeioTransporte",tipoMeioTransporte.getTpMeioTransporte().getValue());
    	mapRotaTipoMeioTransporte.put("dtVigenciaInicial",rotaTipoMeioTransporte.getDtVigenciaInicial());
    	mapRotaTipoMeioTransporte.put("dtVigenciaFinal",rotaTipoMeioTransporte.getDtVigenciaFinal());
    	mapRotaTipoMeioTransporte.put("idRotaTipoMeioTransporte",rotaTipoMeioTransporte.getIdRotaTipoMeioTransporte());
    	    	     
    	return mapRotaTipoMeioTransporte;
    }

	private void validaRemoveById(Long id) {
		RotaTipoMeioTransporte rotaTipoMeioTransporte = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(rotaTipoMeioTransporte);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	validaRemoveById((Long)id);
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
    	for(Long id : ids)
	        validaRemoveById(id);
        super.removeByIds(ids);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map<String, Object> storeMap(Map<String, Object> bean) {
    	RotaTipoMeioTransporte rotaTipoMeioTransporte = new RotaTipoMeioTransporte();

        ReflectionUtils.copyNestedBean(rotaTipoMeioTransporte,bean);
        vigenciaService.validaVigenciaBeforeStore(rotaTipoMeioTransporte);
        super.store(rotaTipoMeioTransporte);
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rotaTipoMeioTransporte);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idRotaTipoMeioTransporte", rotaTipoMeioTransporte.getIdRotaTipoMeioTransporte());
        return bean;
    }

    @Override
    protected RotaTipoMeioTransporte beforeStore(RotaTipoMeioTransporte bean) {
    	if (getRotaTipoMeioTransporteDAO().verificaRotaTipoMeioTransporteVigente(bean)) {
			throw new BusinessException("LMS-00003");
		}
    	if(!rotaColetaEntregaService.findRotaColetaEntregaValidaVigencias(bean.getRotaColetaEntrega().getIdRotaColetaEntrega(),bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");

    	if (vigenciaService.validateIntegridadeVigencia(RotaMeioTransporteRodov.class,"rotaTipoMeioTransporte",bean)){
    		throw new BusinessException("LMS-29133");
    	}
    	return super.beforeStore(bean);
    }

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
    
   }