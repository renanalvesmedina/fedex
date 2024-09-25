package com.mercurio.lms.portaria.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.Portaria;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.dao.PortariaDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.portariaService"
 */
public class PortariaService extends CrudService<Portaria, Long> {

	private FilialService filialService;
	private VigenciaService vigenciaService;
	private ControleTrechoService controleTrechoService;
	private ControleCargaService controleCargaService;
	private LacreControleCargaService lacreControleCargaService;
	private OrdemSaidaService ordemSaidaService;
	private ControleEntSaidaTerceiroService controleEntSaidaTerceiroService;
	private InformarChegadaService informarChegadaService;
	private InformarSaidaService informarSaidaService;
	private NewInformarChegadaService newInformarChegadaService;
	private NewInformarSaidaService newInformarSaidaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NewSaidaChegadaDAO newSaidaChegadaDAO;
	private ConfiguracaoAuditoriaService configuracaoAuditoriaService;

	/**
	 * Recupera uma instância de <code>Portaria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Portaria findById(java.lang.Long id) {
        return (Portaria)super.findById(id);
    }

    public List findByFilial(Long idFilial){
    	return getPortariaDAO().findByFilial(idFilial);
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
    public java.io.Serializable store(Portaria bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPortariaDAO(PortariaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PortariaDAO getPortariaDAO() {
        return (PortariaDAO) getDao();
    }
    
    private void validaRemocao(Long id) {
    	Portaria bean = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    }
    
    protected void beforeRemoveByIds(List ids) {
    	Iterator i = ids.iterator();
    	while (i.hasNext()) {
    		validaRemocao((Long)i.next());
    	}
    	super.beforeRemoveByIds(ids);
    }
    protected void beforeRemoveById(Long id) {
    	validaRemocao((Long)id);
    	super.beforeRemoveById(id);
    }
    
    protected Portaria beforeStore(Portaria bean) {
    	Portaria pojo = (Portaria)bean;
    	
    	getVigenciaService().validaVigenciaBeforeStore(pojo);
    	
    	Terminal terminal = pojo.getTerminal();
		if (getPortariaDAO().validateDuplicated(
    			pojo.getIdPortaria(),
    			pojo.getDtVigenciaInicial(),
    			pojo.getDtVigenciaFinal(),
    			pojo.getNrPortaria(),
    			terminal.getFilial().getIdFilial()))
    		throw new BusinessException("LMS-00003");
    	
    	if (pojo.getBlPadraoFilial().booleanValue() == true) {
    		if (getPortariaDAO().validateDuplicatedPadrao(
        			pojo.getIdPortaria(),
        			terminal.getFilial().getIdFilial()))
        		throw new BusinessException("LMS-06003");	
    	}
    	
    	if (getPortariaDAO().validateIsDsPortariaFilial(
    			pojo.getDsPortaria(),
    			pojo.getIdPortaria(),
    			terminal.getFilial().getIdFilial()))
    		throw new BusinessException("LMS-06028");	
    	
    	if (!getVigenciaService().validateEntidadeVigente(terminal,pojo.getDtVigenciaInicial(),pojo.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-06019");
    	
    	return super.beforeStore(bean);
    }

    /**
     * Verifica se existe uma descrição de portaria para a mesma filial
     * @param dsPortaria
     * @param idPortaria
     * @param idFilial
     * @return
     */
    public boolean findDsPortariaFilial(String dsPortaria, Long idPortaria, Long idFilial){
    	return getPortariaDAO().validateIsDsPortariaFilial(dsPortaria, idPortaria, idFilial);
    }    
    
    public List<Object[]> findChegadaViagemIntegracaoFedex(String placaOuFrota, Long idFilial){
    	return getPortariaDAO().findChegadaViagemIntegracaoFedex(placaOuFrota, idFilial);
    }    
    
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public VigenciaService getVigenciaService() {
		return vigenciaService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public ControleTrechoService getControleTrechoService() {
		return controleTrechoService;
	}

	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}

	public void setLacreControleCargaService(
			LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	public OrdemSaidaService getOrdemSaidaService() {
		return ordemSaidaService;
	}

	public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
		this.ordemSaidaService = ordemSaidaService;
	}

	public ControleEntSaidaTerceiroService getControleEntSaidaTerceiroService() {
		return controleEntSaidaTerceiroService;
	}

	public void setControleEntSaidaTerceiroService(
			ControleEntSaidaTerceiroService controleEntSaidaTerceiroService) {
		this.controleEntSaidaTerceiroService = controleEntSaidaTerceiroService;
	}

	public InformarChegadaService getInformarChegadaService() {
		return informarChegadaService;
	}

	public void setInformarChegadaService(
			InformarChegadaService informarChegadaService) {
		this.informarChegadaService = informarChegadaService;
	}

	public InformarSaidaService getInformarSaidaService() {
		return informarSaidaService;
	}

	public void setInformarSaidaService(InformarSaidaService informarSaidaService) {
		this.informarSaidaService = informarSaidaService;
	}

	public NewInformarChegadaService getNewInformarChegadaService() {
		return newInformarChegadaService;
	}

	public void setNewInformarChegadaService(
			NewInformarChegadaService newInformarChegadaService) {
		this.newInformarChegadaService = newInformarChegadaService;
	}

	public NewInformarSaidaService getNewInformarSaidaService() {
		return newInformarSaidaService;
	}

	public void setNewInformarSaidaService(
			NewInformarSaidaService newInformarSaidaService) {
		this.newInformarSaidaService = newInformarSaidaService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NewSaidaChegadaDAO getNewSaidaChegadaDAO() {
		return newSaidaChegadaDAO;
	}

	public void setNewSaidaChegadaDAO(NewSaidaChegadaDAO newSaidaChegadaDAO) {
		this.newSaidaChegadaDAO = newSaidaChegadaDAO;
	}

	public ConfiguracaoAuditoriaService getConfiguracaoAuditoriaService() {
		return configuracaoAuditoriaService;
	}

	public void setConfiguracaoAuditoriaService(
			ConfiguracaoAuditoriaService configuracaoAuditoriaService) {
		this.configuracaoAuditoriaService = configuracaoAuditoriaService;
	}
    
}