package com.mercurio.lms.sgr.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sgr.model.EscoltaOperadoraMct;
import com.mercurio.lms.sgr.model.dao.EscoltaOperadoraMctDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.escoltaOperadoraMctService"
 */
public class EscoltaOperadoraMctService extends CrudService<EscoltaOperadoraMct, Long> {

	private EscoltaService escoltaService;
	
	/**
	 * Recupera uma instância de <code>EscoltaOperadoraMct</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EscoltaOperadoraMct findById(java.lang.Long id) {
        return (EscoltaOperadoraMct)super.findById(id);
    }

	/**
	 * Recupera uma instância de <code>EscoltaOperadoraMct</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public EscoltaOperadoraMct findByIdDetalhamento(java.lang.Long id) {
    	EscoltaOperadoraMct escoltaOperadoraMct = (EscoltaOperadoraMct)super.findById(id);
    	escoltaOperadoraMct.getOperadoraMct().getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(
    			escoltaOperadoraMct.getOperadoraMct().getPessoa().getTpIdentificacao().getValue(),
    			escoltaOperadoraMct.getOperadoraMct().getPessoa().getNrIdentificacao()));

    	escoltaOperadoraMct.getEscolta().getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(
    			escoltaOperadoraMct.getEscolta().getPessoa().getTpIdentificacao().getValue(),
    			escoltaOperadoraMct.getEscolta().getPessoa().getNrIdentificacao()));
    	
    	return escoltaOperadoraMct;
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
    public java.io.Serializable store(EscoltaOperadoraMct bean) {
    	
    	/**
    	if (bean.getIdEscoltaOperadoraMct()==null) {
    		Escolta escolta = getEscoltaService().findById(bean.getEscolta().getIdEscolta());
    		bean.setEscolta(escolta);
    	}
    	*/
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEscoltaOperadoraMctDAO(EscoltaOperadoraMctDAO dao) {
        setDao( dao );
    }
    
    public Integer getRowCount(Map criteria) {
        criteria = this.removeMaskNrIdentificacao(criteria);
        return super.getRowCount(criteria);
	}
    
	public ResultSetPage findPaginated(Map criteria) {
        criteria = this.removeMaskNrIdentificacao(criteria);
        return super.findPaginated(criteria);
	}

    /**
     * Remove a mascara do nrIdentificacao
     * 
     * @param criteria
     * @return
     */
    private Map removeMaskNrIdentificacao(Map criteria) {
    	Map escolta = (Map)criteria.get("escolta");
    	
    	if(escolta!=null) {
			Map pessoa = (Map)escolta.get("pessoa");
			
			if (pessoa!=null) {
				String nrIdentificacao = (String)pessoa.get("nrIdentificacao");

				if (nrIdentificacao != null && !nrIdentificacao.equals("") && nrIdentificacao.length() > 1) {
		        	String nrSemMascara = PessoaUtils.clearIdentificacao(nrIdentificacao);
		        	pessoa.put("nrIdentificacao",nrSemMascara);
		        	escolta.put("pessoa", pessoa);
		        	criteria.put("escolta", escolta);
				}
			}
    	}
        return criteria;
    }


	/**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EscoltaOperadoraMctDAO getEscoltaOperadoraMctDAO() {
        return (EscoltaOperadoraMctDAO) getDao();
    }

	public EscoltaService getEscoltaService() {
		return escoltaService;
	}

	public void setEscoltaService(EscoltaService escoltaService) {
		this.escoltaService = escoltaService;
	}
   }