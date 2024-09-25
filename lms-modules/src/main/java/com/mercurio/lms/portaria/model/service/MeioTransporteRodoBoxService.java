package com.mercurio.lms.portaria.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.MeioTransporteRodoBox;
import com.mercurio.lms.portaria.model.dao.MeioTransporteRodoBoxDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.meioTransporteRodoBoxService"
 */
public class MeioTransporteRodoBoxService extends CrudService<MeioTransporteRodoBox, Long> {

	VigenciaService vigenciaService;
	private BoxService boxService;
	/**
	 * Recupera uma instância de <code>MeioTransporteRodoBox</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public MeioTransporteRodoBox findById(java.lang.Long id) {
        return (MeioTransporteRodoBox)super.findById(id);
    }

    public Map findByIdDetalhamento(java.lang.Long id) {
    	MeioTransporteRodoBox bean = (MeioTransporteRodoBox) super.findById(id);
    	MeioTransporteRodoviario mtRodoviario = bean.getMeioTransporteRodoviario();
    	MeioTransporte meioTransporte = mtRodoviario.getMeioTransporte();
    	
    	Map retorno = new TypedFlatMap();
	    
	    retorno.put("idMeioTransporteRodoBox", bean.getIdMeioTransporteRodoBox());
	    
	    retorno.put("meioTransporteRodoviario.idMeioTransporte",mtRodoviario.getIdMeioTransporte());
	    retorno.put("meioTransporteRodoviario.meioTransporte.nrIdentificador",meioTransporte.getNrIdentificador());
	    retorno.put("meioTransporteRodoviario2.meioTransporte.nrFrota",meioTransporte.getNrFrota());
	    retorno.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
	    retorno.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
	    
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    
	    return retorno;
    }
    
	protected void beforeRemoveById(Long id) {
		MeioTransporteRodoBox bean = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(bean);
		super.beforeRemoveById(id);
	}

    protected void beforeRemoveByIds(List ids) {
    	MeioTransporteRodoBox bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(bean);
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

    
    
    protected MeioTransporteRodoBox beforeStore(MeioTransporteRodoBox bean) {
		
    	MeioTransporteRodoBox mtrbox = (MeioTransporteRodoBox) bean;
    	
		if ( this.getMeioTransporteRodoBoxDAO().verificaVigenciaByBox(mtrbox.getIdMeioTransporteRodoBox(),
																	  mtrbox.getMeioTransporteRodoviario().getIdMeioTransporte(), 
																	  mtrbox.getBox().getIdBox(), 
																	  mtrbox.getDtVigenciaInicial(), 
																	  mtrbox.getDtVigenciaFinal())) {
			throw new BusinessException("LMS-00003");
		}
		
		if (!boxService.validateIsBoxVigente(mtrbox.getBox().getIdBox(), mtrbox.getDtVigenciaInicial(), mtrbox.getDtVigenciaFinal())){
			throw new BusinessException("LMS-29023");
		}
		
		return super.beforeStore(bean);
	}
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(MeioTransporteRodoBox bean) {
        return super.store(bean);
    }

	public Map storeMap(TypedFlatMap map) {

		MeioTransporteRodoBox bean = map2bean(map);					                     

		vigenciaService.validaVigenciaBeforeStore(bean);
	    super.store(bean);
	    
	    Map retorno = new TypedFlatMap();
	    
	    retorno.put("idMeioTransporteRodoBox", bean.getIdMeioTransporteRodoBox());
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    
	    return retorno;
	}
	
	
	private MeioTransporteRodoBox map2bean(TypedFlatMap map){
		MeioTransporteRodoBox bean = new MeioTransporteRodoBox();
		bean.setIdMeioTransporteRodoBox(map.getLong("idMeioTransporteRodoBox"));
		bean.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		bean.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		
		MeioTransporteRodoviario mtr = new MeioTransporteRodoviario();
		mtr.setIdMeioTransporte(map.getLong("meioTransporteRodoviario.idMeioTransporte"));		
		bean.setMeioTransporteRodoviario(mtr);
		
		Box box = new Box();
		box.setIdBox(map.getLong("box.idBox"));
		bean.setBox(box);
		
		return bean;
	}
	
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMeioTransporteRodoBoxDAO(MeioTransporteRodoBoxDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MeioTransporteRodoBoxDAO getMeioTransporteRodoBoxDAO() {
        return (MeioTransporteRodoBoxDAO) getDao();
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * @param boxService The boxService to set.
	 */
	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
   }