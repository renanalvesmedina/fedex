package com.mercurio.lms.vol.model.service;

import java.util.List;

import org.hibernate.ObjectNotFoundException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vol.model.VolFabricante;
import com.mercurio.lms.vol.model.dao.VolFabricanteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.volFabricanteService"
 */
public class VolFabricanteService extends CrudService<VolFabricante, Long> {
	
	private PessoaService pessoaService;
	
	/**
	 * Recupera uma inst�ncia de <code>VolFabricante</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolFabricante findById(java.lang.Long id) {
        return (VolFabricante)super.findById(id);
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
	 * Verifica exist�ncia de Pessoa.
	 * � lan�ada uma Exception se fabricante j� estiver cadastrado.
	 * 
	 * @param Long idFabricante
	 */
	public void validateNovoFabricante(Long idFabricante){
		if (idFabricante != null){
			VolFabricante fabricante = null;
			try {
				fabricante = findById(idFabricante);
			} catch (ObjectNotFoundException e){				
			}
			
			if (fabricante != null) {
				throw new BusinessException("LMS-41003");
			}
		}
	}
    
    /**
	 * Seta a data atual no campo dhInclusao
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeInsert(java.lang.Object)
	 */
	@Override
	protected VolFabricante beforeInsert(VolFabricante bean) {
		bean.getPessoa().setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		validateNovoFabricante(bean.getPessoa().getIdPessoa());
		return super.beforeInsert(bean);
	} 

	public java.io.Serializable store(VolFabricante bean) {
		Long id =  (Long)getPessoaService().store(bean.getPessoa());

		VolFabricante volFabricante = new  VolFabricante();
		if (bean.getIdFabricante() != null)
		   volFabricante = findById(id);

		volFabricante.setPessoa(bean.getPessoa());
		volFabricante.setTpSituacao(bean.getTpSituacao());

		return super.store(volFabricante);
	}
    
    public ResultSetPage findPaginatedFabricante(TypedFlatMap criteria){
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria); 	
    	return getVolFabricanteDAO().findPaginatedFabricante(criteria, fd);
    }
       
    public Integer getRowCountFabricante(TypedFlatMap criteria){
    	return getVolFabricanteDAO().getRowCountFabricante(criteria);
    }

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolFabricanteDAO(VolFabricanteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private VolFabricanteDAO getVolFabricanteDAO() {
        return (VolFabricanteDAO) getDao();
    }
    
    public List findLookupFabricante(TypedFlatMap criteria) {
		PessoaUtils.validateIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
    	return getVolFabricanteDAO().findLookupFabricante(criteria);
    }
    
   }