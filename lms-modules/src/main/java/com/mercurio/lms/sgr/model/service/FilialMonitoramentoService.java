package com.mercurio.lms.sgr.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sgr.model.FilialMonitoramento;
import com.mercurio.lms.sgr.model.dao.FilialMonitoramentoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.filialMonitoramentoService"
 */
public class FilialMonitoramentoService extends CrudService<FilialMonitoramento, Long> {
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Recupera uma instância de <code>FilialMonitoramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FilialMonitoramento findById(java.lang.Long id) {
        return (FilialMonitoramento)super.findById(id);
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
    public java.io.Serializable store(FilialMonitoramento bean) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	FilialMonitoramento filialMonitoramento = (FilialMonitoramento) bean;
		Filial filialMonitorada = filialMonitoramento.getFilialByIdFilialMonitorada();
		Filial filialResponsavel = filialMonitoramento.getFilialByIdFilialResponsavel();
		if (!filialService.isFilialVigente(filialResponsavel.getIdFilial(), dataAtual)){
			/* Filial responsável não é vigente na data atual */
			String strFilialResponsavel[] = {configuracoesFacade.getMensagem("filialResponsavel")};
			throw new BusinessException("LMS-11004", strFilialResponsavel);
		}
		if (!filialService.isFilialVigente(filialMonitorada.getIdFilial(), dataAtual)){
			/* Filial monitorada não é vigente na data atual */
			String strFilialMonitorada[] = {configuracoesFacade.getMensagem("filialMonitorada")};
			throw new BusinessException("LMS-11004", strFilialMonitorada);
		}
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFilialMonitoramentoDAO(FilialMonitoramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FilialMonitoramentoDAO getFilialMonitoramentoDAO() {
        return (FilialMonitoramentoDAO) getDao();
    }
   }