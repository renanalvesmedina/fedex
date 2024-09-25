package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.HistoricoMpc;
import com.mercurio.lms.carregamento.model.HistoricoVolume;
import com.mercurio.lms.carregamento.model.LiberaMpc;
import com.mercurio.lms.carregamento.model.dao.HistoricoMpcDAO;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.gm.model.dao.CabecalhoCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.EmbarqueDAO;
import com.mercurio.lms.gm.model.dao.HistoricoVolumeDAO;

/**
 * Classe de serviço para o Historico do MPC. Atende a demanda LMS-2788
 * 
 * @author pfernandes@voiza.com.br
 * @spring.bean id="lms.carregamento.HistoricoMpcService"
 */
public class HistoricoMpcService extends CrudService<HistoricoMpc, Long> {

	private HistoricoVolumeDAO historicoVolumeDAO;
	private EmbarqueDAO embarqueDao;
	private CabecalhoCarregamentoDAO cabecalhoCarregamentoDao;
	private UsuarioLMSService usuarioLMSService;
	private LiberarMpcService liberarMpcService;

	@Override
	public Serializable store(HistoricoMpc bean) {
		return super.store(bean);
	}

	/**
	 * Serviço responsável por salvar o histórico do mpc. Este serviço é utilizado no GM quando há a necessidade de liberar o "Carregar" do mpc com pendências. Indepêndte se a a autorização é ncessária ficará gravado um log para registro.
	 * 
	 * Demanda LMS-2791
	 * 
	 * @author mxavier@voiza.com.br
	 * 
	 * @param idCarregamento
	 * @param usuarioLogado
	 * @param mpcDaConferencia
	 * @param idUsuarioAutorizador
	 * @param pinAutorizador
	 */
	public void storeHistoricoMpc(Long idCarregamento, Long usuarioLogado, Long mpcDaConferencia, Long idUsuarioAutorizador, Long idDescricaoLiberaMpc) {

		Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);
		CabecalhoCarregamento cabecalhoCarregamento = cabecalhoCarregamentoDao.findCabecalhoByMapaCarregamento(mpcDaConferencia);
		LiberaMpc liberaMpc = liberarMpcService.find(idDescricaoLiberaMpc, null, null, null, null);

		HistoricoMpc historicoMpc = new HistoricoMpc();
		historicoMpc.setCabecalhoCarregamento(cabecalhoCarregamento);
		historicoMpc.setLiberaMPC(liberaMpc);
		historicoMpc.setDhHistoricoMPC(new DateTime());

		// Caso seja exigida autorização ele grava o ususario que autorizou,
		// caso contrário grava o usuário logado no GM
		if (idUsuarioAutorizador != null) {
			historicoMpc.setResponsavel(usuarioLMSService.findById(idUsuarioAutorizador));
		} else if (usuarioLogado != null) {
			historicoMpc.setResponsavel(usuarioLMSService.findById(usuarioLogado));
		}

		historicoMpc.setCarregamento(carregamento);

		super.store(historicoMpc);
	}

	/**
	 * Busca o histórico do MPC a partir do id do rejeita.
	 * 
	 * Demanda LMS-2794
	 * 
	 * @param idRejeita
	 * @return
	 */
	public List<HistoricoMpc> findByIdRejeitaMpc(Long idRejeita){
		 List criterions = new LinkedList();
		 criterions.add(Restrictions.eq("rejeitoMPC.idRejeitoMPC", idRejeita));
		 
		 return getDao().findByCriterion(criterions, null);
	}
		
	public List<HistoricoMpc> findByIdLiberaMpc(Long idLibera){
		 List criterions = new LinkedList();
		 criterions.add(Restrictions.eq("liberaMPC.idLiberaMPC", idLibera));
		 
		 return getDao().findByCriterion(criterions, null);
	}
	
	/**
	 * Busca o histórico do volume a partir do id do rejeita.
	 * 
	 * Demanda LMS-2794
	 * 
	 * @param idRejeita
	 * @return
	 */
	public List<HistoricoVolume> findHistorivoVolumeByIdRejeita(Long idRejeita){
		 List criterions = new LinkedList();
		 criterions.add(Restrictions.eq("idRejeitoMpc", idRejeita));
		 
		 return historicoVolumeDAO.findByCriterion(criterions, null);
	}
	
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setEmbarqueDao(EmbarqueDAO embarqueDao) {
		this.embarqueDao = embarqueDao;
	}

	public void setCabecalhoCarregamentoDao(CabecalhoCarregamentoDAO cabecalhoCarregamentoDao) {
		this.cabecalhoCarregamentoDao = cabecalhoCarregamentoDao;
	}

	public void setLiberarMpcService(LiberarMpcService liberarMpcService) {
		this.liberarMpcService = liberarMpcService;
	}

	public void setHistoricoVolumeDAO(HistoricoVolumeDAO historicoVolumeDAO) {
		this.historicoVolumeDAO = historicoVolumeDAO;
	}

	public void setHistoricoMpcDao(HistoricoMpcDAO dao) {
		setDao(dao);
	}		
}
