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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
	 * Verifica se a pessoa j� est� cadastrada
	 */
	protected VolOperadorasTelefonia beforeInsert(VolOperadorasTelefonia bean) { 
		Pessoa pessoa = bean.getPessoa();
		//VERIFICA SE A PESSOA EST� CADASTRADA
		if (pessoa.getIdPessoa()==null){
			pessoa.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			pessoa.setTpPessoa(new DomainValue("J"));
			getPessoaService().store(pessoa);
		}
		//VERIFICA SE J� EXISTE
		if (getVolOperadorasTelefoniaDAO().findOperadoraExiste(pessoa.getIdPessoa()))
			throw new BusinessException("LMS-41004");
		return bean;
	}
	/**
	 * Recupera uma inst�ncia de <code>VolOperadorasTelefonia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolOperadorasTelefonia findById(java.lang.Long id) {
        return (VolOperadorasTelefonia)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolOperadorasTelefoniaDAO(VolOperadorasTelefoniaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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