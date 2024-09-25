package com.mercurio.lms.sim.model.service;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.sim.model.ConfiguracaoComunicacao;
import com.mercurio.lms.sim.model.dao.ConfiguracaoComunicacaoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.configuracaoComunicacaoService"
 */
public class ConfiguracaoComunicacaoService extends CrudService<ConfiguracaoComunicacao, Long> {

	private VigenciaService vigenciaService;
	private EventoClienteRecebeService eventoClienteRecebeService;
	/**
	 * Recupera uma inst�ncia de <code>ConfiguracaoComunicacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ConfiguracaoComunicacao findById(java.lang.Long id) {
        return (ConfiguracaoComunicacao)super.findById(id);
    }
	/*
	 * Regras de vigencia
	 * */
    protected void beforeRemoveById(Long id) {
    	validaRemoveById((Long) id);
		super.beforeRemoveById(id);
	}
	protected void beforeRemoveByIds(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ;)
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}
	private void validaRemoveById(Long id) { 
		ConfiguracaoComunicacao configuracaoComunicacao   = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(configuracaoComunicacao);
	}
    protected ConfiguracaoComunicacao beforeStore(ConfiguracaoComunicacao bean) {
    	ConfiguracaoComunicacao configuracaoComunicacao = (ConfiguracaoComunicacao)bean;
    	vigenciaService.validaVigenciaBeforeStore(configuracaoComunicacao);
    	
    	return super.beforeStore(bean);
    }
    // Este m�todo valida se esta vigente para qualquer consulta
    public boolean validateVigenciaConfiguracaoComunicacao(Long idConfiguracaoComunicacao, DetachedCriteria dc,YearMonthDay dtVigenciaInicial,YearMonthDay dtVigenciaFinal){
    	dc = JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	boolean retorno = false;
    	int totalRegistrosEncontrados = ((List)( getConfiguracaoComunicacaoDAO().getAdsmHibernateTemplate().findByDetachedCriteria(dc))).size();
    	 if (totalRegistrosEncontrados > 0){
    		 retorno =  true;
    	 } 
    	 if ((idConfiguracaoComunicacao != null) && (totalRegistrosEncontrados > 0)){
    		 retorno = false;
    	 }
    	 
    	 return retorno;
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
    public java.io.Serializable store(ConfiguracaoComunicacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setConfiguracaoComunicacaoDAO(ConfiguracaoComunicacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ConfiguracaoComunicacaoDAO getConfiguracaoComunicacaoDAO() {
        return (ConfiguracaoComunicacaoDAO) getDao();
    }
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public List validateEventosClienteVigente(Long idCliente, List list, YearMonthDay dataInicial, YearMonthDay dataFim) {
		return eventoClienteRecebeService.validateEventosClienteVigente(idCliente,list,dataInicial,dataFim);
	}
	public EventoClienteRecebeService getEventoClienteRecebeService() {
		return eventoClienteRecebeService;
	}
	public void setEventoClienteRecebeService(
			EventoClienteRecebeService eventoClienteRecebeService) {
		this.eventoClienteRecebeService = eventoClienteRecebeService;
	}
	public VigenciaService getVigenciaService() {
		return vigenciaService;
	}
   }