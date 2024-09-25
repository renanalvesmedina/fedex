package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.PostoPassagemRotaColEnt;
import com.mercurio.lms.municipios.model.dao.PostoPassagemRotaColEntDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.postoPassagemRotaColEntService"
 */
public class PostoPassagemRotaColEntService extends CrudService<PostoPassagemRotaColEnt, Long> {
	private VigenciaService vigenciaService;
	private PostoPassagemService postoPassagemService;
	private RotaColetaEntregaService rotaColetaEntregaService;

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * Recupera uma inst�ncia de <code>PostoPassagemRotaColEnt</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public PostoPassagemRotaColEnt findById(java.lang.Long id) {
        return (PostoPassagemRotaColEnt)super.findById(id);
    }
    
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PostoPassagemRotaColEnt bean) {
        return super.store(bean);
    }
        
    protected PostoPassagemRotaColEnt beforeStore(PostoPassagemRotaColEnt bean) {
    	PostoPassagemRotaColEnt p = (PostoPassagemRotaColEnt)bean;
    	if (!getPostoPassagemRotaColEntDAO().isVigenciaValida(p))
    		throw new BusinessException("LMS-00003");
    	
    	if (postoPassagemService.findPostoPassagemByVigencias(p.getPostoPassagem().getIdPostoPassagem(),p.getDtVigenciaInicial(),p.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (!rotaColetaEntregaService.findRotaColetaEntregaValidaVigencias(p.getRotaColetaEntrega().getIdRotaColetaEntrega(),p.getDtVigenciaInicial(),p.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");

    	return super.beforeStore(bean);
    }

    protected void beforeRemoveByIds(List ids) {
    	PostoPassagemRotaColEnt p = null;
    	for(int x = 0; x < ids.size(); x++) {
    		p = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(p);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	PostoPassagemRotaColEnt p = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(p);
    	super.beforeRemoveById(id);
    }

	/**
	 * Andresa Vargas
	 * 
	 * @param idFilial
	 * @param idPostoPassagem
	 * @param idRotaColetaEntrega
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 * @see lms.com.mercurio.lms.municipios.model.dao.PostoPassagemRotaColEntDAO.findPaginatedCustom
	 */
	public ResultSetPage findPaginatedCustom(Long idFilial, Long idPostoPassagem, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, FindDefinition findDef) {
		return this.getPostoPassagemRotaColEntDAO().findPaginatedCustom(idFilial, idPostoPassagem, idRotaColetaEntrega, dtVigenciaInicial, dtVigenciaFinal, findDef);
	}

	 /**
     * Andresa Vargas
     * 
     * 
     * @param idFilial
     * @param idPostoPassagem
     * @param idRotaColetaEntrega
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * @param findDef
     * @return
     * @see lms.com.mercurio.lms.municipios.model.dao.PostoPassagemRotaColEntDAO.getRowCountCustom
     */
	public Integer getRowCountCustom(Long idFilial, Long idPostoPassagem, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return this.getPostoPassagemRotaColEntDAO().getRowCountCustom(idFilial, idPostoPassagem, idRotaColetaEntrega, dtVigenciaInicial, dtVigenciaFinal);
	}


    
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setPostoPassagemRotaColEntDAO(PostoPassagemRotaColEntDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private PostoPassagemRotaColEntDAO getPostoPassagemRotaColEntDAO() {
        return (PostoPassagemRotaColEntDAO) getDao();
    }
     
    public Map storeMap(Map bean) {
    	PostoPassagemRotaColEnt postoPassagemRotaColEnt = new PostoPassagemRotaColEnt();        
    	ReflectionUtils.copyNestedBean(postoPassagemRotaColEnt ,bean);   
    	
    	vigenciaService.validaVigenciaBeforeStore(postoPassagemRotaColEnt);
    	
        super.store(postoPassagemRotaColEnt);
        
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(postoPassagemRotaColEnt);
        bean.put("acaoVigenciaAtual",acaoVigencia);
        bean.put("idPostoPassagemRotaColEnt", postoPassagemRotaColEnt.getIdPostoPassagemRotaColEnt());
        
        return bean;
    }

	public void setPostoPassagemService(PostoPassagemService postoPassagemService) {
		this.postoPassagemService = postoPassagemService;
	}

	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
    
   }