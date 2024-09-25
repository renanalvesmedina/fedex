package com.mercurio.lms.portaria.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.BoxFinalidade;
import com.mercurio.lms.portaria.model.Finalidade;
import com.mercurio.lms.portaria.model.dao.BoxFinalidadeDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.boxFinalidadeService"
 */
public class BoxFinalidadeService extends CrudService<BoxFinalidade, Long> {

	VigenciaService vigenciaService;
	private BoxService boxService;
	
	/**
	 * Recupera uma instância de <code>BoxFinalidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public BoxFinalidade findById(Long id) {
        return (BoxFinalidade)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    public Map findByIdDetalhamento(java.lang.Long id) {
    	BoxFinalidade bean = (BoxFinalidade) super.findById(id);
    	Finalidade finalidade = bean.getFinalidade();
    	Servico servico = bean.getServico();
    	
    	Map retorno = new TypedFlatMap();
	    
	    retorno.put("idBoxFinalidade", bean.getIdBoxFinalidade());
	    
	    retorno.put("finalidade.idFinalidade",finalidade.getIdFinalidade());
	    if (servico != null)
	    	retorno.put("servico.idServico",servico.getIdServico());
	    
	    retorno.put("hrInicial",bean.getHrInicial());
	    retorno.put("hrFinal",bean.getHrFinal());
	    retorno.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
	    retorno.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
	    
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    return retorno;
    }
    
	protected void beforeRemoveById(Long id) {
		BoxFinalidade bean = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(bean);
		super.beforeRemoveById(id);
	}

    protected void beforeRemoveByIds(List ids) {
    	BoxFinalidade bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    
    
    protected BoxFinalidade beforeStore(BoxFinalidade bean) {
		
    	BoxFinalidade boxFinalidade = (BoxFinalidade) bean;
    	
    	Long idServico = (boxFinalidade.getServico() != null) ? boxFinalidade.getServico().getIdServico() : null;
    	
    	if (getBoxFinalidadeDAO().verificaVigenciaByFaixaHorario(boxFinalidade.getIdBoxFinalidade(),
													    			boxFinalidade.getBox().getIdBox(),
													    			idServico,
													    			boxFinalidade.getHrInicial(),
													    			boxFinalidade.getHrFinal(),
													    			boxFinalidade.getDtVigenciaInicial(), boxFinalidade.getDtVigenciaFinal()))
    		
			throw new BusinessException("LMS-00003");
	
		if (!boxService.validateIsBoxVigente(boxFinalidade.getBox().getIdBox(), boxFinalidade.getDtVigenciaInicial(), boxFinalidade.getDtVigenciaFinal())){
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
    public java.io.Serializable store(BoxFinalidade bean) {
        return super.store(bean);
    }

    public Map storeMap(TypedFlatMap map) {

    	BoxFinalidade bean = map2bean(map);					                     

    	vigenciaService.validaVigenciaBeforeStore(bean);
	    
	    super.store(bean);
	    
	    Map retorno = new HashMap();
	    
	    retorno.put("idBoxFinalidade", bean.getIdBoxFinalidade());
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    return retorno;
	}
	
	
	private BoxFinalidade map2bean(TypedFlatMap map){
		BoxFinalidade bean = new BoxFinalidade();
		bean.setIdBoxFinalidade(map.getLong("idBoxFinalidade"));
		bean.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		bean.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		bean.setHrFinal(map.getTimeOfDay("hrFinal"));
		bean.setHrInicial(map.getTimeOfDay("hrInicial"));
		
		if (map.getLong("servico.idServico") != null) {
			Servico servico = new Servico();
			servico.setIdServico(map.getLong("servico.idServico"));
			bean.setServico(servico);
		}
		
		Finalidade finalidade = new Finalidade();
		finalidade.setIdFinalidade(map.getLong("finalidade.idFinalidade"));
		bean.setFinalidade(finalidade);
		
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
    public void setBoxFinalidadeDAO(BoxFinalidadeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private BoxFinalidadeDAO getBoxFinalidadeDAO() {
        return (BoxFinalidadeDAO) getDao();
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