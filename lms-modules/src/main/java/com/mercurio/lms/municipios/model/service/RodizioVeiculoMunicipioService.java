package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RodizioVeiculoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.RodizioVeiculoMunicipioDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rodizioVeiculoMunicipioService"
 */
public class RodizioVeiculoMunicipioService extends CrudService<RodizioVeiculoMunicipio, Long> {
	
	private VigenciaService vigenciaService;

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	protected void beforeRemoveById(Long id) {
		RodizioVeiculoMunicipio rodizioVeiculoMunicipio = (RodizioVeiculoMunicipio)findById(id);
		if(rodizioVeiculoMunicipio != null){
			JTVigenciaUtils.validaVigenciaRemocao(rodizioVeiculoMunicipio);
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for(int i = 0; i<ids.size(); i++){
			RodizioVeiculoMunicipio rodizioVeiculoMunicipio = (RodizioVeiculoMunicipio)findById((Long)ids.get(i));
			if(rodizioVeiculoMunicipio != null){
				JTVigenciaUtils.validaVigenciaRemocao(rodizioVeiculoMunicipio);
			}
		}
		super.beforeRemoveByIds(ids);
	}
	
	public TypedFlatMap findByIdEValidaVigencia(Long id){
		RodizioVeiculoMunicipio rodizioVeiculoMunicipio = (RodizioVeiculoMunicipio)findById(id);
		TypedFlatMap mapRodizioVeiculoMunicipio = new TypedFlatMap();
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rodizioVeiculoMunicipio);
		mapRodizioVeiculoMunicipio.put("acaoVigenciaAtual",acaoVigencia);
		
		final Municipio municipio = rodizioVeiculoMunicipio.getMunicipio();
		mapRodizioVeiculoMunicipio.put("municipio.idMunicipio",municipio.getIdMunicipio());
		mapRodizioVeiculoMunicipio.put("municipio.nmMunicipio",municipio.getNmMunicipio());
		
		final UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
		mapRodizioVeiculoMunicipio.put("municipio.unidadeFederativa.sgUnidadeFederativa",unidadeFederativa.getSgUnidadeFederativa());
		mapRodizioVeiculoMunicipio.put("municipio.unidadeFederativa.nmUnidadeFederativa",unidadeFederativa.getNmUnidadeFederativa());
		mapRodizioVeiculoMunicipio.put("municipio.unidadeFederativa.pais.nmPais",unidadeFederativa.getPais().getNmPais().getValue());
		mapRodizioVeiculoMunicipio.put("diaSemana",rodizioVeiculoMunicipio.getDiaSemana().getValue());
		mapRodizioVeiculoMunicipio.put("nrFinalPlaca",rodizioVeiculoMunicipio.getNrFinalPlaca());
		mapRodizioVeiculoMunicipio.put("hrRodizioInicial",rodizioVeiculoMunicipio.getHrRodizioInicial());
		mapRodizioVeiculoMunicipio.put("hrRodizioFinal",rodizioVeiculoMunicipio.getHrRodizioFinal());
		mapRodizioVeiculoMunicipio.put("dtVigenciaInicial",rodizioVeiculoMunicipio.getDtVigenciaInicial());
		mapRodizioVeiculoMunicipio.put("dtVigenciaFinal",rodizioVeiculoMunicipio.getDtVigenciaFinal());
		mapRodizioVeiculoMunicipio.put("idRodizioVeiculoMunicipio",rodizioVeiculoMunicipio.getIdRodizioVeiculoMunicipio());
		
		return mapRodizioVeiculoMunicipio;
	}

	/**
	 * Recupera uma instância de <code>RodizioVeiculoMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RodizioVeiculoMunicipio findById(java.lang.Long id) {
        return (RodizioVeiculoMunicipio)super.findById(id);
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
		return getRodizioVeiculoMunicipioDAO().getRowCount(criteria);
	}
    
    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage list = getRodizioVeiculoMunicipioDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(list.getList());
		list.setList(lista);
		return list;
		
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
    public java.io.Serializable store(RodizioVeiculoMunicipio bean) {
        return super.store(bean);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	RodizioVeiculoMunicipio rodizioVeiculoMunicipio = new RodizioVeiculoMunicipio();
    	ReflectionUtils.copyNestedBean(rodizioVeiculoMunicipio,bean);
    	    	
//    	valida data vigencia inicial
		vigenciaService.validaVigenciaBeforeStore(rodizioVeiculoMunicipio);
		
		super.store(rodizioVeiculoMunicipio);
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rodizioVeiculoMunicipio);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idRodizioVeiculoMunicipio",rodizioVeiculoMunicipio.getIdRodizioVeiculoMunicipio());
        return bean;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRodizioVeiculoMunicipioDAO(RodizioVeiculoMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RodizioVeiculoMunicipioDAO getRodizioVeiculoMunicipioDAO() {
        return (RodizioVeiculoMunicipioDAO) getDao();
    }
   
    /**
     * Antes de armazenar, verifica registros vigentes.
     */
	protected RodizioVeiculoMunicipio beforeStore(RodizioVeiculoMunicipio bean) {
		RodizioVeiculoMunicipio rodizioVeiculoMunicipio = (RodizioVeiculoMunicipio)bean;
		
		if((rodizioVeiculoMunicipio.getHrRodizioInicial() != null && rodizioVeiculoMunicipio.getHrRodizioFinal() == null)
			||(rodizioVeiculoMunicipio.getHrRodizioFinal() != null && rodizioVeiculoMunicipio.getHrRodizioInicial() == null))
			throw new BusinessException("LMS-29119");
		
				
		if(getRodizioVeiculoMunicipioDAO().verificaRodiziosVigentes(rodizioVeiculoMunicipio)){
			throw new BusinessException("LMS-00003");
		}
		
		return super.beforeStore(bean);
	}

	
    
   }