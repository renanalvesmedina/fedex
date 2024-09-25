package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.municipios.model.MeioTransporteRotaViagem;
import com.mercurio.lms.municipios.model.RotaViagem;
import com.mercurio.lms.municipios.model.dao.MeioTransporteRotaViagemDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.meioTransporteRotaViagemService"
 */
public class MeioTransporteRotaViagemService extends CrudService<MeioTransporteRotaViagem, Long> {

	private VigenciaService vigenciaService;
		
	/**
	 * Recupera uma instância de <code>MeioTransporteRotaViagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public MeioTransporteRotaViagem findById(java.lang.Long id) {
        return (MeioTransporteRotaViagem)super.findById(id);
    }
    
    public ResultSetPage findPaginatedDefault(TypedFlatMap criteria) {
    	 ResultSetPage rsp = getMeioTransporteRotaViagemDAO().findPaginatedDefault(criteria,FindDefinition.createFindDefinition(criteria));
    	 List result = new ArrayList();
    	 Map registro = new HashMap();
    	 for (Iterator i = rsp.getList().iterator() ; i.hasNext() ; ) {
    		 registro = (Map)i.next();
    		 Map mt = new TypedFlatMap();

    		 mt.put("idMeioTransporteRotaViagem",(Long)registro.get("idMeioTransporteRotaViagem"));
    		 mt.put("meioTransporteRodoviario.meioTransporte.nrFrota",(String)registro.get("nrFrota"));
    		 mt.put("meioTransporteRodoviario.meioTransporte.nrIdentificador",(String)registro.get("nrIdentificador"));
    		 
    		 mt.put("meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte",
    				 (String)registro.get("dsModeloMeioTransporte"));
    		 mt.put("meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte",
    				 (String)registro.get("dsMarcaMeioTransporte"));
    		 mt.put("meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte",
    				 registro.get("dsTipoMeioTransporte") != null ? 
    						 ((String)registro.get("dsTipoMeioTransporte")) : null);
    		 mt.put("meioTransporteRodoviario.meioTransporte.nrAnoFabricao",(Short)registro.get("nrAnoFabricao"));
    		 
    		 mt.put("dtVigenciaInicial",(YearMonthDay)registro.get("dtVigenciaInicial"));
    		 mt.put("dtVigenciaFinal",(YearMonthDay)registro.get("dtVigenciaFinal"));

    		 result.add(mt);
    	 }
    	 rsp.setList(result);
    	 return rsp;
    }
    
    public Integer findRowCountDefault(TypedFlatMap criteria) {
    	return getMeioTransporteRotaViagemDAO().findRowCountDefault(criteria);
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
    public java.io.Serializable store(MeioTransporteRotaViagem bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMeioTransporteRotaViagemDAO(MeioTransporteRotaViagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MeioTransporteRotaViagemDAO getMeioTransporteRotaViagemDAO() {
        return (MeioTransporteRotaViagemDAO) getDao();
    }
        
    protected MeioTransporteRotaViagem beforeStore(MeioTransporteRotaViagem bean) {
    	MeioTransporteRotaViagem pojo = (MeioTransporteRotaViagem)bean;
    	    	
    	RotaViagem rotaViagem = pojo.getRotaViagem();
		YearMonthDay dtVigenciaInicial = pojo.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = pojo.getDtVigenciaFinal();
		vigenciaService.validaVigenciaBeforeStore(pojo);
		// valida se vigência informada 'fecha' com a vigência da rota viagem.
		if (!vigenciaService.validateEntidadeVigente(rotaViagem,
				dtVigenciaInicial,
				dtVigenciaFinal)) {
			throw new BusinessException("LMS-29023");
		}
		
		validateTipoMeioTransporteConsistente(pojo);
		
		if (getMeioTransporteRotaViagemDAO().validateDuplicated(
    			pojo.getIdMeioTransporteRotaViagem(),
    			dtVigenciaInicial,
    			dtVigenciaFinal,
    			pojo.getMeioTransporteRodoviario().getIdMeioTransporte(),
    			rotaViagem.getIdRotaViagem()))
    		throw new BusinessException("LMS-00003");
    	return super.beforeStore(bean);
    }
    
    private void validateTipoMeioTransporteConsistente(MeioTransporteRotaViagem bean) {
    	MeioTransporte meioTransporte = (MeioTransporte)getMeioTransporteRotaViagemDAO()
    			.getHibernateTemplate().load(MeioTransporte.class,bean.getMeioTransporteRodoviario().getIdMeioTransporte());
    	ModeloMeioTransporte modelo = meioTransporte.getModeloMeioTransporte();
    	
    	RotaViagem rotaViagem = (RotaViagem)getMeioTransporteRotaViagemDAO()
				.getHibernateTemplate().load(RotaViagem.class,bean.getRotaViagem().getIdRotaViagem());
    	TipoMeioTransporte tipoRotaViagem = rotaViagem.getTipoMeioTransporte();
    	
    	Long idTipo = modelo.getTipoMeioTransporte().getIdTipoMeioTransporte();
    	Long idTipoRotaViagem = tipoRotaViagem.getIdTipoMeioTransporte();
    	TipoMeioTransporte tipoComposto = tipoRotaViagem.getTipoMeioTransporte();
    	
    	Long idTipoComposto = (tipoComposto != null) ? tipoComposto.getIdTipoMeioTransporte() : Long.valueOf(-1);
    	    	
    	if (idTipo.compareTo(idTipoRotaViagem) != 0 && idTipo.compareTo(idTipoComposto) != 0) {
    		throw new BusinessException("LMS-26062");
    	}
    }
    
    /**
     * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
     * @param id Id do registro a ser validado.
     */
	private void validaRemoveById(Long id) {
		MeioTransporteRotaViagem meioTransporteRotaViagem = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(meioTransporteRotaViagem);
	}
    
    protected void beforeRemoveById(Long id) {
    	validaRemoveById((Long)id);
		super.beforeRemoveById(id);
    }
    
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    protected void beforeRemoveByIds(List ids) {
    	for (Iterator i = ids.iterator() ; i.hasNext() ; )
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
    }
    
    public List findToConsultarRotas(Long idRotaViagem) {
    	return getMeioTransporteRotaViagemDAO().findToConsultarRotas(idRotaViagem);
    }
    
    /**
     * Carrega tipo de meio de transporte e composto na sessão.
     * @param id
     * @return
     */
    public TipoMeioTransporte findTipoMeioTransporteAndLoadComposto(Long id) {
    	TipoMeioTransporte tmt = (TipoMeioTransporte)getMeioTransporteRotaViagemDAO().getAdsmHibernateTemplate()
    			.load(TipoMeioTransporte.class,id);
    	tmt.getTipoMeioTransporte();
    	return tmt;
    }
    
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
    
	
	/**
     * Verifica se existe um veículo cadastrado para a rota de viagem.
     * 
     * @param idRotaViagem
     * @param idMeioTransporte
     * @return True se encontrar, caso contrário, False.
     */
    public Boolean validateMeioTransporteWithRotaViagem(Long idRotaViagem, Long idMeioTransporte) {
    	return getMeioTransporteRotaViagemDAO().findMeioTransporteWithRotaViagem(idRotaViagem, idMeioTransporte);
    }
}