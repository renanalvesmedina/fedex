package com.mercurio.lms.indenizacoes.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.EventoRim;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.EventoRimDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.indenizacoes.eventoRimService"
 */
public class EventoRimService extends CrudService<EventoRim, Long> {

	private DomainValueService domainValueService;
	private ReciboIndenizacaoService reciboIndenizacaoService;

	/**
	 * Recupera uma inst�ncia de <code>EventoRim</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public EventoRim findById(java.lang.Long id) {
        return (EventoRim)super.findById(id);
    }

	/**
     * Seta o bean e armazena o evento. 
     * 
     * @param parameters
     * @return Serializable
     */
    public Serializable saveEvntDocumentosRecebidos(Map parameters, String tipoEvento) {

    	EventoRim eventoRim = new EventoRim();
    	
    	Long  idReciboIndenizacao = Long.parseLong((String) parameters.get("idReciboIndenizacao"));
    	ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findReciboIndenizacaoById(idReciboIndenizacao);
    	
		eventoRim.setFilial(SessionUtils.getFilialSessao());
		eventoRim.setReciboIndenizacao(reciboIndenizacao);
    	eventoRim.setUsuario(SessionUtils.getUsuarioLogado());

    	DateTime data = JTDateTimeUtils.getDataHoraAtual();
		eventoRim.setDhEventoRim(data);

		eventoRim.setTpEventoIndenizacao(new DomainValue(tipoEvento));
		
		//Armazena o evento 
		Long retorno = (Long)super.store(eventoRim);													
		
		return retorno;
	}

    /**
     * 
     */
    public ResultSetPage findDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao, FindDefinition fd) {
    	return this.getEventoRimDAO().findPaginatedEventoRimByIdReciboIndenizacao(idReciboIndenizacao, fd);
    }
    public ResultSetPage findDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao, Integer currentPage, Integer pageSize) {
    	return this.getEventoRimDAO().findPaginatedEventoRimByIdReciboIndenizacao(idReciboIndenizacao, currentPage, pageSize);
    }

    public Integer getRowCountEventoRimByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return this.getEventoRimDAO().getRowCountEventoRimByIdReciboIndenizacao(idReciboIndenizacao);
    }

	/**
	 * Verificar se RECIBO_INDENIZACAO TP_STATUS_INDENIZACAO seja 
	 * igual a "P" ou "C", caso positivo, emitir mensagem LMS-21072:
	 * "RIM n�o pode ser pago. Verifique se j� n�o est� pago ou cancelado."
	 *  
	 **/
    public Boolean findStatusIndenizacao(Map parameters) {

    	Long idReciboIndenizacao = Long.parseLong((String) parameters.get("idReciboIndenizacao"));
    	ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findById(idReciboIndenizacao); 

		if ("P".equals(reciboIndenizacao.getTpStatusIndenizacao().getValue())
				|| "C".equals(reciboIndenizacao.getTpStatusIndenizacao().getValue()))
			return true;

		return false;
    }

	/*
	 * Verificar nos eventos do RIM (EVENTO_RIM) se j� houve pagamento manual, 
	 * ou seja, Se existe um registro com TP_EVENTO_INDENIZACAO = "PM" para 
	 * este Recibo. Caso j� exista emitir mensagem LMS-21070:
	 *  "J� existe um pagamento manual para este Recibo de indeniza��o". 
	 *  
	 */
    public Boolean findEventoRimByIdReciboIndenizacao(Map parameters) {
    	
    	Long idReciboIndenizacao = Long.parseLong((String) parameters.get("idReciboIndenizacao"));
    	Long reciboIndenizacao = getEventoRimDAO().findEventoRimByIdReciboIndenizacao(idReciboIndenizacao);
		if (0 < reciboIndenizacao)
			return true;

    	return false;
    }
    
    
	public void storeEventoRim(Filial filial, ReciboIndenizacao reciboIndenizacao, DomainValue domainValue ){
		EventoRim eventoRim = new EventoRim();
		eventoRim.setFilial(filial);
		eventoRim.setReciboIndenizacao(reciboIndenizacao);
		eventoRim.setTpEventoIndenizacao(domainValue);
		eventoRim.setDhEventoRim( JTDateTimeUtils.getDataHoraAtual() );
		this.store(eventoRim);
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
    public java.io.Serializable store(EventoRim bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setEventoRimDAO(EventoRimDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private EventoRimDAO getEventoRimDAO() {
        return (EventoRimDAO) getDao();
    }


	public DomainValueService getDomainValueService() {
		return domainValueService;
	}


	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ReciboIndenizacaoService getReciboIndenizacaService() {
		return reciboIndenizacaoService;
	}

	public void setReciboIndenizacaService(
			ReciboIndenizacaoService reciboIndenizacaService) {
		this.reciboIndenizacaoService = reciboIndenizacaService;
	}

	public ResultSetPage findPaginatedEventoRim(TypedFlatMap tfm) {
		return getEventoRimDAO().findPaginatedEventoRim(tfm, FindDefinition.createFindDefinition(tfm));	
	}

	public Integer getRowCountEventoRim(Long idReciboIndenizacao) {
		return getEventoRimDAO().getRowCountEventoRim(idReciboIndenizacao);
	}

	public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		return getEventoRimDAO().findItensByIdReciboIndenizacao(idReciboIndenizacao);
	}

	public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		return getEventoRimDAO().getRowCountItensByIdReciboIndenizacao(idReciboIndenizacao);
	}

   }