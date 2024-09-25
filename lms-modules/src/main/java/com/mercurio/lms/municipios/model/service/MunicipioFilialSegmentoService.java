package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MunicipioFilialSegmento;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialSegmentoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.municipioFilialSegmentoService"
 */
public class MunicipioFilialSegmentoService extends CrudService<MunicipioFilialSegmento, Long> {
	 
	private MunicipioFilialService municipioFilialService;
	private VigenciaService vigenciaService;
	
	public ResultSetPage findPaginated(Map criteria) { 
        
        List included = new ArrayList();
        included.add("idMunicipioFilialSegmento");
        included.add("municipioFilial.filial.nmPessoa");
        included.add("municipioFilial.municipio.nmMunicipio");
        included.add("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa");
        included.add("municipioFilial.municipio.unidadeFederativa.pais.nmPais");
        included.add("municipioFilial.municipio.blDistrito");
        included.add("municipioFilial.municipio.municipioDistrito.nmMunicipio");
        included.add("segmentoMercado.dsSegmentoMercado");
        included.add("dtVigenciaInicial");
        included.add("dtVigenciaFinal");
 
        ResultSetPage rsp = super.findPaginated(criteria);
        rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
 
        return rsp;
}
	
	protected MunicipioFilialSegmento beforeStore(MunicipioFilialSegmento bean) {
	    MunicipioFilialSegmento munFilSegmentoMercado = (MunicipioFilialSegmento)bean;
	    
	    // Testando LMS-290022
	    getMunicipioFilialService().validateVigenciaAtendimento(munFilSegmentoMercado.getMunicipioFilial().getIdMunicipioFilial(), munFilSegmentoMercado.getDtVigenciaInicial(), munFilSegmentoMercado.getDtVigenciaFinal());
		
	    // Testando LMS-00003
	    if (! getMunicipioFilialSegmentoDAO().verificaSegmentosMercadoAtendidos(munFilSegmentoMercado))
			throw new BusinessException("LMS-00003");
	    	    
		return super.beforeStore(bean);
		
	}	
		
	
	/**
	 * Recupera uma instância de <code>MunFilSegmentoMercado</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public MunicipioFilialSegmento findById(java.lang.Long id) {
    	return (MunicipioFilialSegmento)super.findById(id);
    }
	
	public Map findByIdDetalhamento(java.lang.Long id) {
    	MunicipioFilialSegmento mfs = (MunicipioFilialSegmento)super.findById(id);
    	TypedFlatMap retorno = bean2map(mfs);
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(mfs);
    	retorno.put("acaoVigenciaAtual",acaoVigencia);
    	
        return retorno;
    }

	private TypedFlatMap bean2map(MunicipioFilialSegmento bean){
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("idMunicipioFilialSegmento", bean.getIdMunicipioFilialSegmento());
		map.put("segmentoMercado.idSegmentoMercado", bean.getSegmentoMercado().getIdSegmentoMercado());
		map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
		map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());
		
		return map;
	}
	
    
    /**
     * Verifica se existem registros para o atendimento e segmento informados, dentro da vigencia informada
     * @param idMunicipioFilial
     * @param idSegmento
     * @param dtVigencia
     * @return
     */
    public boolean verificaExisteMunicipioFilialSegmento(Long idMunicipioFilial, Long idSegmento, YearMonthDay dtVigencia){
    	return getMunicipioFilialSegmentoDAO().verificaExisteMunicipioFilialSegmento(idMunicipioFilial, idSegmento, dtVigencia);
    }
    
    public boolean validateExisteMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigencia){
    	return getMunicipioFilialSegmentoDAO().verificaExisteMunicipioFilialVigente(idMunicipioFilial, dtVigencia);
    }
    
    public boolean validateMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
    	return getMunicipioFilialSegmentoDAO().verificaMunicipioVigenciaFutura(idMunicipio, idFilial, dtVigencia);
    }
    
    /**
     * Consulta registros vigentes para o municipio X Filial informado
     * @param idMunicipioFilial
     * @param dtVigenciaFinal 
     * @param dtVigenciaInicial 
     * @return
     */
    public List findSegmentoVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){    	
    	return getMunicipioFilialSegmentoDAO().findSegmentoVigenteByMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
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
    public java.io.Serializable store(MunicipioFilialSegmento bean) {
    	
        return super.store(bean);
    }

	public Map storeMap(Map bean) {

		  MunicipioFilialSegmento municipioSegmento = new MunicipioFilialSegmento();
	      ReflectionUtils.copyNestedBean(municipioSegmento,bean);                  

	      vigenciaService.validaVigenciaBeforeStore(municipioSegmento);

	      super.store(municipioSegmento);
	      
	      TypedFlatMap retorno = new TypedFlatMap();
	      retorno.put("idMunicipioFilialSegmento", municipioSegmento.getIdMunicipioFilialSegmento());
	      retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(municipioSegmento));
	      
	      return retorno;
	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMunicipioFilialSegmentoDAO(MunicipioFilialSegmentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MunicipioFilialSegmentoDAO getMunicipioFilialSegmentoDAO() {
        return (MunicipioFilialSegmentoDAO) getDao();
    }
    
	
    protected void beforeRemoveByIds(List ids) {
    	MunicipioFilialSegmento bean = null;
    	for(Iterator ie = ids.iterator(); ie.hasNext();) {
    		bean = findById((Long)ie.next());
    		JTVigenciaUtils.validaVigenciaRemocao(bean);	    	
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	List list = new ArrayList();
    	list.add(id);
    	beforeRemoveByIds(list);
    }
    public boolean findSegmentoByMunFil(Long idMunicipioFilial){
    	return getMunicipioFilialSegmentoDAO().findSegmentoByMunFil(idMunicipioFilial);
    }
    
    //busca todos os segmentos vigentes de um municipio atendido 
    public List findSegmentoAtendidoByMunicipioFilial(Long idMunicipioFilial) {
    	return getMunicipioFilialSegmentoDAO().findSegmentoAtendidoByMunicipioFilial(idMunicipioFilial);
    }

    //**************************GETTER AND SETTER*************************************//
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

   }