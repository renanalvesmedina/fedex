package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoa;
import com.mercurio.lms.configuracoes.model.dao.ObservacaoICMSPessoaDAO;
import com.mercurio.lms.tributos.model.service.EmbasamentoLegalIcmsService;
import com.mercurio.lms.util.LongUtils;

/**
 * @author José Rodrigo Moraes
 * @since  16/06/2006
 * 
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.observacaoICMSPessoaService"
 */
public class ObservacaoICMSPessoaService extends CrudService<ObservacaoICMSPessoa, Long> {
	
	private PessoaService pessoaService;
	private EmbasamentoLegalIcmsService embasamentoLegalIcmsService;

	/**
	 * Recupera uma instância de <code>ObservacaoICMSPessoa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ObservacaoICMSPessoa findById(java.lang.Long id) {
        return (ObservacaoICMSPessoa)super.findById(id);
    }

    public List findVigenteByIe(Long idIe, YearMonthDay dtVigencia, String tpObservacaoIcmsPessoa){
    	return getObservacaoICMSPessoaDAO().findVigenteByIe(idIe, dtVigencia, tpObservacaoIcmsPessoa);
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
    public java.io.Serializable store(ObservacaoICMSPessoa bean) {    	
        return super.store(bean);
    }

    
    /**
     * Salva ObservacaoICMSPessoa adiconando o codigo do embasamento
     * 
     * @param bean
     */
    public void storeObservacaoICMSPessoa(ObservacaoICMSPessoa bean){
    	
    	if(StringUtils.isBlank(bean.getCdEmbLegalMastersaf())){			
			bean.setCdEmbLegalMastersaf(embasamentoLegalIcmsService.findCdEmbasamento().toString());			
		}		    	
    	bean.setIdObservacaoICMSPessoa(LongUtils.getLong(super.store(bean)));    	
    }
    
    /**
     * Antes de salvar verifica se existe alguma Observação de ICMS para a Inscrição Estadual
     * em questão conflitante com a vigência informada
     * @param bean Observação de ICMS da Pessoa a ser salva 
     * @return Observação de ICMS da Pessoa
     */
    public ObservacaoICMSPessoa beforeStore(ObservacaoICMSPessoa bean) {
    	if( getObservacaoICMSPessoaDAO().existeObservacaoICMSPessoaConflitante((ObservacaoICMSPessoa) bean) ){
    		throw new BusinessException("LMS-23019");
    	}    	
    	//Valida se o usuario logado pode alterar a pessoa
    	pessoaService.validateAlteracaoPessoa(pessoaService.findIdPessoaByIdInscricaoEstadual(((ObservacaoICMSPessoa)bean).getInscricaoEstadual().getIdInscricaoEstadual()));
    	return super.beforeStore(bean);
    }

    @Override
    protected void beforeRemoveById(Long id) {
    	//Valida se o usuario logado pode alterar a pessoa
    	pessoaService.validateAlteracaoPessoa(pessoaService.findIdPessoaByIdObservacaoICMSPessoa((Long)id));
    	super.beforeRemoveById(id);
    }

    @Override
    protected void beforeRemoveByIds(List ids) {
    	for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			beforeRemoveById(id);			
		}
    	super.beforeRemoveByIds(ids);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setObservacaoICMSPessoaDAO(ObservacaoICMSPessoaDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ObservacaoICMSPessoaDAO getObservacaoICMSPessoaDAO() {
        return (ObservacaoICMSPessoaDAO) getDao();
    }

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public EmbasamentoLegalIcmsService getEmbasamentoLegalIcmsService() {
		return embasamentoLegalIcmsService;
}

	public void setEmbasamentoLegalIcmsService(
			EmbasamentoLegalIcmsService embasamentoLegalIcmsService) {
		this.embasamentoLegalIcmsService = embasamentoLegalIcmsService;
	}

}
