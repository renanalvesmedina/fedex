package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.TarifaColeta;
import com.mercurio.lms.municipios.model.dao.TarifaColetaDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.tarifaColetaService"
 */
public class TarifaColetaService extends CrudService<TarifaColeta, Long> {
	
    private FilialService filialService;
    private VigenciaService vigenciaService;
    
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * Recupera uma instância de <code>TarifaColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TarifaColeta findById(java.lang.Long id) {
        return (TarifaColeta)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	TarifaColeta bean = findById(id);
    	
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
    	result.put("filialByIdFilial.sgFilial",bean.getFilialByIdFilial().getSgFilial());
    	result.put("filialByIdFilial.idFilial",bean.getFilialByIdFilial().getIdFilial());
		result.put("filialByIdFilial.pessoa.nmFantasia",bean.getFilialByIdFilial().getPessoa().getNmFantasia());
		result.put("municipioFilial.municipio.nmMunicipio",bean.getMunicipio().getNmMunicipio());
		result.put("municipio.idMunicipio",bean.getMunicipio().getIdMunicipio());
		result.put("municipioFilial.idMunicipioFilial",bean.getMunicipio().getIdMunicipio());
		result.put("filialByIdFilialColeta.idFilial",bean.getFilialByIdFilialColeta().getIdFilial());
		result.put("filialByIdFilialColeta.sgFilial",bean.getFilialByIdFilialColeta().getSgFilial());
		result.put("filialByIdFilialColeta.pessoa.nmFantasia",bean.getFilialByIdFilialColeta().getPessoa().getNmFantasia());
		result.put("tarifaPreco.cdTarifaPreco",bean.getTarifaPreco().getCdTarifaPreco());
		result.put("tarifaPreco.idTarifaPreco",bean.getTarifaPreco().getIdTarifaPreco());
		result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
		result.put("idTarifaColeta",bean.getIdTarifaColeta());
    	return result;
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
    public TypedFlatMap storeMap(Map bean) {
    	TarifaColeta tarifaColeta = new TarifaColeta();
    	ReflectionUtils.copyNestedBean(tarifaColeta,bean);
    	vigenciaService.validaVigenciaBeforeStore(tarifaColeta);
    	TypedFlatMap result = new TypedFlatMap();
		result.put("idTarifaColeta",(Long)super.store(tarifaColeta));
		result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(tarifaColeta));
    	return result; 
    }
    
    protected TarifaColeta beforeStore(TarifaColeta bean) {
    	TarifaColeta tarifaColeta = (TarifaColeta) bean;
    	if(tarifaColeta.getFilialByIdFilial().getSgFilial()!= null && tarifaColeta.getFilialByIdFilialColeta().getSgFilial()!= null){
    		if(tarifaColeta.getFilialByIdFilial().getSgFilial().equalsIgnoreCase(tarifaColeta.getFilialByIdFilialColeta().getSgFilial())){
    			throw new BusinessException("LMS-29048");
    		}
    	}
    	//verifica se a vigencia está no intervalo das filiais informadas
    	getFilialService().verificaExistenciaHistoricoFilial(tarifaColeta.getFilialByIdFilial().getIdFilial(),tarifaColeta.getDtVigenciaInicial(),tarifaColeta.getDtVigenciaFinal());
    	getFilialService().verificaExistenciaHistoricoFilial(tarifaColeta.getFilialByIdFilialColeta().getIdFilial(),tarifaColeta.getDtVigenciaInicial(),tarifaColeta.getDtVigenciaFinal());
    	
    	List rs = getTarifaColetaDAO().verificaVigencia((TarifaColeta)bean);
    	if (rs != null && rs.size() > 0)
    		throw new BusinessException("LMS-00003");
    	return super.beforeStore(bean);
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = super.findPaginated(criteria);
    	List newList = new ArrayList();
    	for (Iterator ie = rsp.getList().iterator(); ie.hasNext();) {
    		TarifaColeta bean = (TarifaColeta)ie.next();
    		TypedFlatMap result = new TypedFlatMap();
    		result.put("filialByIdFilial.sgFilial",bean.getFilialByIdFilial().getSgFilial());
    		result.put("filialByIdFilial.pessoa.nmFantasia",bean.getFilialByIdFilial().getPessoa().getNmFantasia());
    		result.put("municipio.nmMunicipio",bean.getMunicipio().getNmMunicipio());
    		result.put("filialByIdFilialColeta.sgFilial",bean.getFilialByIdFilialColeta().getSgFilial());
    		result.put("filialByIdFilialColeta.pessoa.nmFantasia",bean.getFilialByIdFilialColeta().getPessoa().getNmFantasia());
    		result.put("tarifaPreco.cdTarifaPreco",bean.getTarifaPreco().getCdTarifaPreco());
    		result.put("tarifaPreco.nrKmInicial",bean.getTarifaPreco().getNrKmInicial());
    		result.put("tarifaPreco.nrKmFinal",bean.getTarifaPreco().getNrKmFinal());
    		result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
    		result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
    		result.put("idTarifaColeta",bean.getIdTarifaColeta());
    		newList.add(result);
    	}
    	rsp.setList(newList);
    	return rsp;
    }
    
    protected void beforeRemoveByIds(List ids) {
    	TarifaColeta tc = null;
    	for(int x = 0; x < ids.size(); x++) {
    		tc = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(tc);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	TarifaColeta tc = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(tc);
    	super.beforeRemoveById(id);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTarifaColetaDAO(TarifaColetaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TarifaColetaDAO getTarifaColetaDAO() {
        return (TarifaColetaDAO) getDao();
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
   }