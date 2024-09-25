package com.mercurio.lms.tributos.model.service;
import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.tributos.model.ExcecaoICMSCliente;
import com.mercurio.lms.tributos.model.dao.ExcecaoICMSClienteDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.excessaoICMSClienteService"
 */
public class ExcecaoICMSClienteService extends CrudService<ExcecaoICMSCliente, Long> {


	/**
	 * Set PessoaService  (Inversion of control)
	 */
	private PessoaService pessoaService;
	private ExcecaoICMSNaturezaService excecaoICMSNaturezaService;
	private EmbasamentoLegalIcmsService embasamentoLegalIcmsService;
	
	public void setPessoaService(PessoaService pessoaService){
		this.pessoaService = pessoaService;
	}

	public void setExcecaoICMSNaturezaService(ExcecaoICMSNaturezaService excecaoICMSNaturezaService) {
		this.excecaoICMSNaturezaService = excecaoICMSNaturezaService;
	}


	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ExcecaoICMSCliente findById(java.lang.Long id) {
        return (ExcecaoICMSCliente)super.findById(id);
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
     * Realiza algumas valida��es antes do store.
     *
     * @author Hector Julian Esnaola Junior
     * @since 23/10/2007
     *
     * @param bean
     * @return
     *
     */
	public ExcecaoICMSCliente beforeStore(ExcecaoICMSCliente bean) {
		ExcecaoICMSCliente eic = (ExcecaoICMSCliente) bean;
		
		// Aceitar tipo de tributacao normal somente quando blSubcontratacao for true.
		if ("normal".equalsIgnoreCase(eic.getTipoTributacaoIcms().getDsTipoTributacaoIcms())) {
			if (!eic.getBlSubcontratacao()) {
				throw new BusinessException("LMS-23029");
			}
		}
		
		/** Busca registros na base com intervalos de vig�ncia iguais */
		List lst = getExcecaoICMSClienteDAO().findExcecaoICMSClienteByVigenciaEquals(eic.getDtVigenciaInicial()
						, eic.getDtVigenciaFinal()
						, eic.getUnidadeFederativa().getIdUnidadeFederativa()
						, eic.getTipoTributacaoIcms().getIdTipoTributacaoIcms()
						, eic.getTpFrete().getValue()
						, eic.getNrCNPJParcialDev()
						, eic.getIdExcecaoICMSCliente());
		
		/** Verifica se n�o j� n�o existe nenhum registro na base com o mesmo intervalo de vig�ncia */
		if(!lst.isEmpty()){ 
			throw new BusinessException("LMS-00047");
		}
		 
		return super.beforeStore(bean);
	}
	
	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/10/2007
	 *
	 * @param nrIdentificacao
	 * @param tipoCnpj
	 * @return
	 *
	 */
	public String formatCnpj(String nrIdentificacao, String tipoCnpj) {
		if (nrIdentificacao != null) {
			if ("P".equals(tipoCnpj)) {
				nrIdentificacao = FormatUtils
						.completaDados(nrIdentificacao, "0", 8, 0, true);
			} else {
				nrIdentificacao = FormatUtils
						.completaDados(nrIdentificacao, "0", 14, 0, true);
			}
		}
		return nrIdentificacao;
	}
	
    /**
	 * Salva o objeto ExcecaoICMSCliente 
	 * 
	 * Seta o codigo do embasamento no objeto caso n�o possuir
	 * 
	 * @param bean
	 */
    public void storeExcecaoIcmsCliente(ExcecaoICMSCliente bean) { 
    	
    	if(bean.getIdExcecaoICMSCliente() == null){
    		bean.setCdEmbLegalMastersaf(embasamentoLegalIcmsService.findCdEmbasamento().toString());
    	}    	    	
    	bean.setIdExcecaoICMSCliente(LongUtils.getLong(super.store(bean)));    
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
    protected Serializable store(ExcecaoICMSCliente bean) {    	
        return super.store(bean);
    }
    
    
    /**
     * Retorna uma ExcecaoICMSCliente a partir dos filtros informado
     * 
     * @author Micka�l Jalbert
     * @since 01/06/2006
     * 
     * @param String nrIdentificacaoDev
     * @param String nrIdentificacaoRem
     * @param String tpFrete
     * @param Long idUfOrigem
     * @param YearMonthDay dtVigencia
     * 
     * @return ExcecaoICMSCliente
     * */    
    public ExcecaoICMSCliente findByUK(String nrIdentificacaoDev, String nrIdentificacaoRem, String tpFrete, Long idUfOrigem, YearMonthDay dtVigencia){
    	return getExcecaoICMSClienteDAO().findByUK(nrIdentificacaoDev, nrIdentificacaoRem, tpFrete, idUfOrigem, dtVigencia);
    }    
    
    /**
     * Busca Excecao ICMS Cliente via constulta SQL
     * 
     * @author Andr� Valadas
     * @since 05/05/2009
     * 
     * @param nrIdentificacaoDev
     * @param nrIdentificacaoRem
     * @param tpFrete
     * @param idUfFilialOrigem
     * @param idUfDestino
     * @param idFilialOrigem
     * @param idNaturezaProduto
     * @param vlParametroTributacaoDevido
     * @param dtVigencia
     * @return 
     */
    public ExcecaoICMSCliente findExcecaoICMSCliente(
    		String nrIdentificacaoDev,
    		String nrIdentificacaoRem,
    		String tpFrete,
    		Long idUfFilialOrigem,
    		Long idUfDestino,
    		Long idFilialOrigem,
    		Long idNaturezaProduto,
    		Long vlParametroTributacaoDevido,
    		YearMonthDay dtVigencia) {
    	List<ExcecaoICMSCliente> result = getExcecaoICMSClienteDAO().findExcecaoICMSCliente(nrIdentificacaoDev, nrIdentificacaoRem, tpFrete, idUfFilialOrigem, idUfDestino, idFilialOrigem, idNaturezaProduto, vlParametroTributacaoDevido, dtVigencia);
    	if(!result.isEmpty()) {
    		return result.get(0);
    	}
		return null;
    }

    /**
	 * M�todo respons�vel por buscar nrCNPJParcialDev igual as primeiras 8 posi��es do nrIdentificacao da Pessoa 
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcialDev
	 * @return List <ExcecaoICMSCliente>
	 */
	public List findPessoaByNrIdentificacao(String nrCNPJParcialDev){
		List lst = pessoaService.findPessoasByNrIdentificacao(nrCNPJParcialDev);
		if(lst == null || lst.isEmpty()){
			throw new BusinessException("LMS-23030");
		}
		return lst;
	}

	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setExcecaoICMSClienteDAO(ExcecaoICMSClienteDAO dao) {
        setDao( dao );
    }
    
    
    /**
     * Remove a entidade ExcecaoICMSCliente atraves do idExcecaoICMSCliente
     * e todos os ExcecaoICMSNatureza vinculados 
     * 
     * @param ids Lista de ids ExcecaoICMSCliente
     */
    public void removeExcecaoICMSCliente(List ids){
    	for(Object id : ids){
    		excecaoICMSNaturezaService.removeByIdExcecaoICMSCliente(LongUtils.getLong(id));    	
    	}
    	removeByIds(ids);
    }    
    
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ExcecaoICMSClienteDAO getExcecaoICMSClienteDAO() {
        return (ExcecaoICMSClienteDAO) getDao();
    }
    
	public EmbasamentoLegalIcmsService getEmbasamentoLegalIcmsService() {
		return embasamentoLegalIcmsService;
	}

	public void setEmbasamentoLegalIcmsService(
			EmbasamentoLegalIcmsService embasamentoLegalIcmsService) {
		this.embasamentoLegalIcmsService = embasamentoLegalIcmsService;
	}

}