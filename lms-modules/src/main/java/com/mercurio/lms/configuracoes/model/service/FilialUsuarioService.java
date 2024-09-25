package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.ListUtils;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;
import com.mercurio.lms.configuracoes.model.dao.FilialUsuarioDAO;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.filialUsuarioService"
 */
public class FilialUsuarioService extends CrudService<FilialUsuario, Long> {

	private RegionalFilialService regionalFilialService;
	
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	/**
	 * Recupera uma inst�ncia de <code>FilialUsuario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public FilialUsuario findById(java.lang.Long id) {
        return (FilialUsuario)super.findById(id);
    }
    
    public List findByIdUsuario(java.lang.Long id) {
        return getFilialUsuarioDAO().findByIdUsuario(id);
    }

    public FilialUsuario findByIdFilialEmpresaUsuario(Long idEmpresaUsuario, Long idFilial){
    	return getFilialUsuarioDAO().findByIdFilialEmpresaUsuario( idEmpresaUsuario, idFilial );
    }
    
    public List findByIdEmpresaUsuario(java.lang.Long id) {
        return getFilialUsuarioDAO().findByIdEmpresaUsuario(id);
    }
    
    public List findByIdEmpresaUsuarioSemFetch(Long id){
    	return getFilialUsuarioDAO().findByIdEmpresaUsuarioSemFetch(id);
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
    

    public void removeByUsuario(Long id) {
		getFilialUsuarioDAO().removeByUsuario(id);
	}

    public void removeByIdEmpresaUsuario(Long id) {
		getFilialUsuarioDAO().removeByIdEmpresaUsuario(id);
	}
    
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByUsuarios(List ids) {
		getFilialUsuarioDAO().removeByUsuarios(ids);
	}

    
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FilialUsuario bean) {   	
    	return super.store(bean);        
    }
    
    public void storeALL(List filiaisUsuario)
    {
    	if (filiaisUsuario==null || filiaisUsuario.size()==0) return;
    	
    	
    	/* Obtem a List de Filiais a serem gravadas. */
    	List filiaisAGravar = getListFiliais(filiaisUsuario);    	    	

    	List filiaisConformeRegionais = null;
    	
    	List listRegionaisAux = getListRegionais( ((FilialUsuario)filiaisUsuario.get(0)).getEmpresaUsuario().getRegionalUsuario() );    	
    	if (listRegionaisAux!=null)
    	{
    	  /* Obtem a lista de todas as Filiais conforme as regionais. */
    		filiaisConformeRegionais = regionalFilialService.findByRegionais
    										(
    											listRegionaisAux
    										);
    	}    	
    	
    	
    	if (filiaisAGravar!=null)
    	{    	
    		List gravar = filiaisAGravar;
    		if (filiaisConformeRegionais!=null)
    		{
    			/* Verifica quais filiais n�o pertem as regionais j� informadas, assim obtendo a List de Filiais a serem gravadas. */
    			gravar = ListUtils.subtract(filiaisAGravar,filiaisConformeRegionais);
    		}    		
	    	
	    	/* Persist todas as Filiais Usuario. */
	    	ListIterator lit = filiaisUsuario.listIterator();
	    	while(lit.hasNext())
	    	{
	    		
	    		FilialUsuario filialUsuario = (FilialUsuario)lit.next();
	    		if ( gravar.contains( filialUsuario.getFilial() ) ) store( filialUsuario );
	    		
	    	}
    	}
    	
    }
    
    private List getListFiliais(List filiaisUsuario)
    {
    	List filiais 	 = new ArrayList();
    	ListIterator lit = filiaisUsuario.listIterator();
    	
    	while(lit.hasNext())
    	{
    		filiais.add( ((FilialUsuario)lit.next()).getFilial() );
    	}
    	
    	return filiais;
    	
    }
    
    private List getListRegionais(List regionaisUsuario)
    {
    	if (regionaisUsuario!=null)
    	{    	
	    	List regionais   = new ArrayList();
	    	ListIterator lit = regionaisUsuario.listIterator();
	    	
	    	while(lit.hasNext())
	    	{
	    		regionais.add( ((RegionalUsuario)lit.next()).getRegional().getIdRegional() );
	    	}
	    	
	    	return regionais;
    	}
    	return null;
    }
    

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setFilialUsuarioDAO(FilialUsuarioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private FilialUsuarioDAO getFilialUsuarioDAO() {
        return (FilialUsuarioDAO) getDao();
    }
    
    public void storeWorkFlowForAll(List bean) {

        getFilialUsuarioDAO().storeWorkFlowForAll(bean);

           }


   }