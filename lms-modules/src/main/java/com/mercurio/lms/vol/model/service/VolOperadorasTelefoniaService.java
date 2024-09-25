package com.mercurio.lms.vol.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolOperadorasTelefonia;
import com.mercurio.lms.vol.model.dao.VolOperadorasTelefoniaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volOperadorasTelefoniaService"
 */
public class VolOperadorasTelefoniaService extends CrudService<VolOperadorasTelefonia, Long> {
	private PessoaService pessoaService;
	public void setPessoaService(PessoaService pessoaService){
		this.pessoaService = pessoaService;
	}
	public PessoaService getPessoaService(){
		return this.pessoaService;
	}
	/**
	 * Verifica se a pessoa já está cadastrada
	 */
	protected VolOperadorasTelefonia beforeInsert(VolOperadorasTelefonia bean) { 
		Pessoa pessoa = bean.getPessoa();
		//VERIFICA SE A PESSOA ESTÁ CADASTRADA
		if (pessoa.getIdPessoa()==null){
			pessoa.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			pessoa.setTpPessoa(new DomainValue("J"));
			getPessoaService().store(pessoa);
		}
		//VERIFICA SE JÁ EXISTE
		if (getVolOperadorasTelefoniaDAO().findOperadoraExiste(pessoa.getIdPessoa()))
			throw new BusinessException("LMS-41004");
		return bean;
	}
	/**
	 * Recupera uma instância de <code>VolOperadorasTelefonia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolOperadorasTelefonia findById(java.lang.Long id) {
        return (VolOperadorasTelefonia)super.findById(id);
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
    public java.io.Serializable store(VolOperadorasTelefonia bean) {
    	Serializable retorno = super.store(bean);  
    	    	
    	if (bean.getPessoa().getIdPessoa() != null){
    		Pessoa pessoaBanc = getPessoaService().findById(bean.getPessoa().getIdPessoa());
    		pessoaBanc.setNmPessoa(bean.getPessoa().getNmPessoa());
    		pessoaBanc.setDsEmail(bean.getPessoa().getDsEmail());    	
    	
    		getPessoaService().store(pessoaBanc);
    	}
    	return retorno;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolOperadorasTelefoniaDAO(VolOperadorasTelefoniaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolOperadorasTelefoniaDAO getVolOperadorasTelefoniaDAO() {
        return (VolOperadorasTelefoniaDAO) getDao();
    }
    public ResultSetPage findPaginatedOperadoras(TypedFlatMap criteria){
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	return getVolOperadorasTelefoniaDAO().findPaginatedOperadoras(criteria, findDef);
    }
    
    public Integer getRowCountOperadoras(TypedFlatMap criteria){
    	return getVolOperadorasTelefoniaDAO().getRowCountOperadoras(criteria);
    }
   }