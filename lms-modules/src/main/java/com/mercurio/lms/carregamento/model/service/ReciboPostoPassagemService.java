package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.ReciboPostoPassagem;
import com.mercurio.lms.carregamento.model.dao.ReciboPostoPassagemDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.reciboPostoPassagemService"
 */
public class ReciboPostoPassagemService extends CrudService<ReciboPostoPassagem, Long> {
	
	private ControleCargaService controleCargaService;
	private PagtoPedagioCcService pagtoPedagioCcService;
	private ConfiguracoesFacade configuracoesFacade;	
	
	public PagtoPedagioCcService getPagtoPedagioCcService() {
		return pagtoPedagioCcService;
	}

	public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
		this.pagtoPedagioCcService = pagtoPedagioCcService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Recupera uma instância de <code>ReciboDesconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ReciboPostoPassagem findById(java.lang.Long id) {
        return (ReciboPostoPassagem)super.findById(id);
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
    public java.io.Serializable store(ReciboPostoPassagem bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setReciboPostoPassagemDAO(ReciboPostoPassagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ReciboPostoPassagemDAO getReciboPostoPassagemDAO() {
        return (ReciboPostoPassagemDAO) getDao();
    }
    
    /**
	 * Gera um recibo de passagem para posterior emissão.
	 * 
	 * @param controleCarga
	 */
	public void generateReciboPostoPassagem(Long idControleCarga) {
		ControleCarga controleCarga =  this.getControleCargaService().findById(idControleCarga);
		Long idPagtoPPDinheiro = Long.valueOf(((BigDecimal) this.getConfiguracoesFacade().getValorParametro("ID_PAGTO_PP_DINHEIRO")).longValue());
		
		List listPagtoPedagioCcs = getPagtoPedagioCcService().findPagtoPedagioCcByIdCcAndIdPagamPostoPassagem(idControleCarga, idPagtoPPDinheiro);

		for (Iterator iter = listPagtoPedagioCcs.iterator(); iter.hasNext();) {
			PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();

			Long nrReciboPostoPassagem = configuracoesFacade.incrementaParametroSequencial(
					controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "NR_REC_POSTO_PASSAG", true);

			ReciboPostoPassagem reciboPostoPassagem = new ReciboPostoPassagem();
			reciboPostoPassagem.setFilial(controleCarga.getFilialByIdFilialOrigem());
			reciboPostoPassagem.setProprietario(controleCarga.getProprietario());
			reciboPostoPassagem.setMotorista(controleCarga.getMotorista());
			reciboPostoPassagem.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
			reciboPostoPassagem.setControleCarga(controleCarga);
			reciboPostoPassagem.setPagtoPedagioCc(pagtoPedagioCc);
			reciboPostoPassagem.setMoeda(pagtoPedagioCc.getMoeda());
			reciboPostoPassagem.setNrReciboPostoPassagem(nrReciboPostoPassagem);
			reciboPostoPassagem.setVlBruto(pagtoPedagioCc.getVlPedagio());
			reciboPostoPassagem.setTpStatusRecibo(new DomainValue("GE"));
			getReciboPostoPassagemDAO().store(reciboPostoPassagem);
		}
	}
	
	/**
	 * Atualiza o status do Recibo de Posto de Passagem.
	 * 
	 * @param idReciboPostoPassagem
	 */
	public void updateReciboPostoPassagem(Long idReciboPostoPassagem) {
		ReciboPostoPassagem reciboPostoPassagem = this.findById(idReciboPostoPassagem);
		
		if (reciboPostoPassagem.getDhEmissao()==null) {
			reciboPostoPassagem.setUsuario(SessionUtils.getUsuarioLogado());
			reciboPostoPassagem.setTpStatusRecibo(new DomainValue("EM"));	
			reciboPostoPassagem.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
		} else {
			reciboPostoPassagem.setTpStatusRecibo(new DomainValue("RE"));
		}
		
		this.store(reciboPostoPassagem);
	}
	
	public Integer getRowCountByIdControleCarga(Long idControleCarga, Boolean blMostrarCancelados) {
		return this.getReciboPostoPassagemDAO().getRowCountByIdControleCarga(idControleCarga, blMostrarCancelados);
	}
	
	public ResultSetPage findPaginatedByControleCarga(Long idControleCarga, Boolean blMostrarCancelados, FindDefinition findDefinition){
		return this.getReciboPostoPassagemDAO().findPaginatedByIdControleCarga(idControleCarga, blMostrarCancelados, findDefinition);
	}
	
	/**
	 * Método que busca uma list de Recibo de Posto de Passagem a partir do ID do Controle de Carga. 
	 * @param idControleCarga
	 * @return
	 */
	public List findReciboPostoPassagemByIdControleCarga(Long idControleCarga) {
		return this.getReciboPostoPassagemDAO().findReciboPostoPassagemByIdControleCarga(idControleCarga);
	}
	
	
	/**
     * Altera o status para o Controle de Carga em questão.
     * 
     * @param idControleCarga
     */
    public void updateStatusReciboByIdControleCarga(Long idControleCarga) {
		getReciboPostoPassagemDAO().updateStatusReciboByIdControleCarga(idControleCarga);
	}
    
    /**
     * Remove do banco todos os registros de ReciboPostoPassagem de um controle de carga.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getReciboPostoPassagemDAO().removeByIdControleCarga(idControleCarga);
    }
}