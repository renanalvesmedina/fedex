package com.mercurio.lms.sim.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.dto.UltimoEventoDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.dao.EventoDAO;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.eventoService"
 */
public class EventoService extends CrudService<Evento, Long> {

    private LocalizacaoMercadoriaService localizacaoMercadoriaService;

	/**
	 * Recupera uma instância de <code>Evento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Evento findById(java.lang.Long id) {
        return (Evento)super.findById(id);
    }
    
    public Evento findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
    	return (Evento)super.findByIdInitLazyProperties(id, initializeLazyProperties);
    }
    
	/**
	 * Retorna todos os eventos da empresa do usuário logado.
	 *
	 * @param Criterio da tela.
	 * @return Lista dos eventos da empresa do usuário logado.
	 */
    public List findByEmpresaLogada(Map<String, Object> criteria) {
    	if (criteria == null){
    		criteria = new HashMap<String, Object>();
    	}
    	criteria.put("empresa.idEmpresa", ((Empresa) SessionContext.get(SessionKey.EMPRESA_KEY)).getIdEmpresa());
        return (List)getEventoDAO().findListByCriteria(criteria);
    }    

    public Evento findByCdEvento(Short cdEvento){
    	return getEventoDAO().findByCdEvento(cdEvento);
    }

	/**
	 * Consulta o evento que possui o evento de cancelamento informado
	 * @param idEvento
	 * @return
	 */
	public Evento findEventoByEventoCancelamento(Long idEvento){
		return getEventoDAO().findEventoByEventoCancelamento(idEvento);
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
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Evento bean) {
        return super.store(bean);
    }

    @Override
    protected Evento beforeStore(Evento bean) {
    	if (bean.getIdEvento() != null && bean.getCancelaEvento() != null &&
    			bean.getIdEvento().equals(bean.getCancelaEvento().getIdEvento()))
    		throw new BusinessException("LMS-10013");
    	 
    	return super.beforeStore(bean);
    }

    public List<Map<String, Object>> findLookupEvento(Map<String, Object> criteria) {
    	List<Object[]> rs = getEventoDAO().findEvento(criteria);
    	List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
    	for(Object[] obj : rs) {
    		Map<String, Object> newMap = new HashMap<String, Object>();
    		newMap.put("idEvento",obj[0]);

    		StringBuffer sb = new StringBuffer(((VarcharI18n)obj[1]).getValue(LocaleContextHolder.getLocale()));
    		sb.append(((obj[2] != null) ? " - "+((VarcharI18n)obj[2]).getValue(LocaleContextHolder.getLocale()) : ""));

    		newMap.put("dsEvento",sb.toString());
    		newMap.put("cdEvento",obj[3]);

    		newList.add(newMap);
    	} 
    	return newList;
    }

    public List<Evento> findEventos(TypedFlatMap criteria) {
    	return getEventoDAO().findEventos(criteria);
    }
    
    public Long findEventoNaoConformidade(Map<String, Object> criteria){
    	return getEventoDAO().findEventoNaoConformidade(criteria);
    }
    
    public Object[] findEventoByIdDoctoServico(Map<String, Object> criteria) {
    	return getEventoDAO().findEventoByIdDoctoServico(criteria);
    }
    
    
    public UltimoEventoDto findUltimoEvento(Long nrDoctoServico, String sgFilial, Date dhEmissao) {
    	return getEventoDAO().findUltimoEvento(nrDoctoServico, sgFilial, dhEmissao);
    }
    
    public boolean validateIfIsEventoEntregaRealizada(Evento evento) {
        if (evento != null) {
            return localizacaoMercadoriaService.validateIfIsEntregaRealizada(evento.getLocalizacaoMercadoria());
        }

        return false;
    }
    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoDAO(EventoDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoDAO getEventoDAO() {
        return (EventoDAO) getDao();
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }
    
    public void evict(Evento evento){
        getDao().getAdsmHibernateTemplate().evict(evento);
    }

	public Long findIdEventoByCdEvento(Short cdEvento){
    	return getEventoDAO().findIdEventoByCdEvento(cdEvento);
	}
}