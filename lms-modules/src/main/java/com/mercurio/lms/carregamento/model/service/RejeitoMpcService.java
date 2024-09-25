package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoMpc;
import com.mercurio.lms.carregamento.model.RejeitoMpc;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.carregamento.model.dao.RejeitoMpcDAO;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.gm.model.service.CarregamentoService;
import com.mercurio.lms.gm.model.service.HistoricoCarregamentoService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para o Rejeito do MPC. Atende a demanda LMS-2788
 * 
 * @author pfernandes@voiza.com.br
 * @spring.bean id="lms.carregamento.rejeitoMPCService"
 */
public class RejeitoMpcService extends CrudService<RejeitoMpc, Long> {
	private HistoricoMpcService historicoMpcService;
	private VolumeService volumeService;
	private CarregamentoService carregamentoService;
	private UsuarioLMSService usuarioLMSService;
	private HistoricoCarregamentoService historicoCarregamentoService;

	public Serializable findById(Long id) {
		return super.findById(id);
	}

	@Override
	public List find(Map criteria) {
		return super.find(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 * 
	 * 
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Retorna o uma lista de Rejeito Mpc de acordo com a situação e a abrangência
	 */
	@SuppressWarnings("unchecked")
	public List findRejeitoMpcBySituacaoAbrangencia(Map param) {
		List<Map> listRejeitoMpc = getRejeitoMpcDAO().findRejeitoMpcBySituacaoAbrangencia(param);

		for (Map mapRejeitoMpc : listRejeitoMpc) {
			mapRejeitoMpc.put("stRejeito", ((DomainValue) mapRejeitoMpc.get("stRejeito")).getValue());
			mapRejeitoMpc.put("tpAbrangencia", ((DomainValue) mapRejeitoMpc.get("tpAbrangencia")).getValue());
			mapRejeitoMpc.put("tpAutorizacao", ((DomainValue) mapRejeitoMpc.get("tpAutorizacao")).getValue());
			mapRejeitoMpc.put("tpRejeito", ((DomainValue) mapRejeitoMpc.get("tpRejeito")).getValue());
		}

		return listRejeitoMpc;
	}

	/**
	 * Método que deve ser chamado para iniciar o armazenado das informações de rejeição de mpc quando REJEITO_MPC.tp_abrangencia = "M", ou seja "MAPA". Referente a demanda LMS-2788
	 * 
	 * @param map
	 *            vindo da tela de rejeito mpc
	 */
	public Map<String, Object> executeAbrangenciaMapa(Map<String, Object> map) {

		Long rejeitoMpcId = Long.parseLong(map.get("idRejeitoMpc").toString());
		RejeitoMpc rejeitoMpc = (RejeitoMpc) this.findById(rejeitoMpcId);

		Long carregamentoId = Long.parseLong(map.get("idCarregamento").toString());
		Carregamento carregamento = (Carregamento) carregamentoService.findCarregamentoById(carregamentoId);

		String mpcText = map.get("mpc").toString();
		Long mpc = Long.parseLong(mpcText);

		UsuarioLMS autorizador = null;

		if (StringUtils.isNotBlank(map.get("idAutorizador").toString())) {
			Long idAutorizador = Long.parseLong(map.get("idAutorizador").toString());
			autorizador = usuarioLMSService.findById(idAutorizador);
		}

		storeHistoricoMpc(rejeitoMpc, mpc, carregamento, autorizador);
		updateVolumes(mpc, carregamento, autorizador, null);
		storeHistoricoVolume(mpc, carregamento, autorizador, null);

		Map<String, Object> resposta = new HashMap<String, Object>();

		resposta.put("MensagemRetorno", "LMS-45103");

		return resposta;

	}

	/**
	 * Método que deve ser chamado para iniciar o armazenado das informações de rejeição de mpc quando REJEITO_MPC.tp_abrangencia = "V", ou seja "VOLUME". Referente a demanda LMS-2788
	 * 
	 * @param map
	 *            vindo da tela de rejeito mpc
	 */
	public Map<String, Object> executeAbrangenciaVolume(Map<String, Object> map) {

		Long carregamentoId = Long.parseLong(map.get("idCarregamento").toString());
		Carregamento carregamento = (Carregamento) carregamentoService.findCarregamentoById(carregamentoId);

		Long volumeId = Long.parseLong(map.get("idVolume").toString());
		Volume volume = (Volume) volumeService.findById(volumeId);

		Long mpc = Long.parseLong(map.get("mpc").toString());

		UsuarioLMS autorizador = null;

		if (StringUtils.isNotBlank(map.get("idAutorizador").toString())) {
			Long idAutorizador = Long.parseLong(map.get("idAutorizador").toString());
			autorizador = usuarioLMSService.findById(idAutorizador);
		}

		updateVolumes(mpc, carregamento, autorizador, volume);
		storeHistoricoVolume(mpc, carregamento, autorizador, volume);

		Map<String, Object> resposta = new HashMap<String, Object>();

		resposta.put("MensagemRetorno", "LMS-45120");

		return resposta;

	}

	/**
	 * Método para realizar a inserção na tabela HISTORICO_MPC
	 * 
	 * @author pfernandes@voiza.com.br
	 * 
	 * @param rejeitoMpc
	 * @param mpc
	 * @param carregamento
	 * @return
	 */
	private void storeHistoricoMpc(RejeitoMpc rejeitoMpc, Long mpc, Carregamento carregamento, UsuarioLMS autorizador) {

		HistoricoMpc historicoMpc = new HistoricoMpc();
		CabecalhoCarregamento cabecalhoCarregamento = carregamentoService.findCabecalhoCarregamentoByMapaCarregamento(mpc);

		historicoMpc.setCabecalhoCarregamento(cabecalhoCarregamento);
		historicoMpc.setRejeitoMPC(rejeitoMpc);
		historicoMpc.setDhHistoricoMPC(JTDateTimeUtils.getDataHoraAtual());

		if (autorizador != null) {
			historicoMpc.setResponsavel(autorizador);
		} else {
			UsuarioLMS usuario = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
			historicoMpc.setResponsavel(usuario);
		}

		historicoMpc.setCarregamento(carregamento);

		historicoMpcService.store(historicoMpc);

	}

	/**
	 * Método utilizado para atualizar as informações do volume que pertence ao mpc e carregamento informado
	 * 
	 * @author pfernandes@voiza.com.br
	 * 
	 * @param mpc
	 * @param carregamento
	 */
	private void updateVolumes(Long mpc, Carregamento carregamento, UsuarioLMS autorizador, Volume volume) {

		Map<String, Object> params = new HashMap<String, Object>();
		boolean isVolume = false;

		if (autorizador != null) {
			params.put("matriculaResponsavel", autorizador.getUsuarioADSM().getIdUsuario());
		} else {
			params.put("matriculaResponsavel", SessionUtils.getUsuarioLogado().getIdUsuario());
		}

		params.put("mpc", mpc);
		params.put("idCarregamento", carregamento.getIdCarregamento());

		if (volume != null) {
			params.put("idVolume", volume.getIdVolume());
			isVolume = true;
		}

		volumeService.updateStatusEMatriculaDeVolumes(params, isVolume);

	}

	/**
	 * Método para inserir os registros na tabela HISTORICO_VOLUME
	 * 
	 * @author pfernandes@voiza.com.br
	 * 
	 * @param mpc
	 * @param carregamento
	 */
	private void storeHistoricoVolume(Long mpc, Carregamento carregamento, UsuarioLMS autorizador, Volume volume) {

		UsuarioLMS usuario = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());

		volumeService.storeHistoricoVolume(mpc, carregamento, usuario, autorizador, volume);

		String[] status = { "1", "2", "3", "7" };

		List<Volume> listaVolume = volumeService.findVolumesByCarregamentoEStatus(carregamento.getIdCarregamento(), status);

		if (listaVolume == null || listaVolume.isEmpty()) {
			updateStatusDoCarregamento(carregamento);
			storeHistoricoCarregamento(carregamento, autorizador);
		}

	}

	/**
	 * Método para atualizar o CODIGO_STATUS da tabela CARREGAMENTO. Isso ocorrer apenas se o Carregamento não possuir nenhum volume.
	 * 
	 * @param carregamento
	 */
	private void updateStatusDoCarregamento(Carregamento carregamento) {

		List<Carregamento> listCarregamentos = new ArrayList<Carregamento>();

		carregamento.setCodigoStatus(new DomainValue("1"));
		listCarregamentos.add(carregamento);

		carregamentoService.storeAll(listCarregamentos);

	}

	/**
	 * Método responsável por inserir os registros do carregamento na tabela HISTORICO_CARREGAMENTO
	 * 
	 * @param carregamento
	 */
	private void storeHistoricoCarregamento(Carregamento carregamento, UsuarioLMS autorizador) {

		UsuarioLMS usuario = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());

		HistoricoCarregamento historicoCarregamento = new HistoricoCarregamento();

		historicoCarregamento.setIdCarregamento(carregamento.getIdCarregamento());
		historicoCarregamento.setStatusCarregamento(new DomainValue("1"));
		historicoCarregamento.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
		historicoCarregamento.setRotaCarregamento(carregamento.getRotaCarregamento());
		historicoCarregamento.setDataHistorico(JTDateTimeUtils.getDataHoraAtual());

		if (autorizador != null) {
			historicoCarregamento.setMatriculaChefia(autorizador.getUsuarioADSM().getIdUsuario());
		} else {
			historicoCarregamento.setMatriculaChefia(usuario.getUsuarioADSM().getIdUsuario());
		}

		historicoCarregamentoService.store(historicoCarregamento);
	}

	public boolean findExigeAutirizacao(Long idRejeitoMpc) {
		return getRejeitoMpcDAO().findExigeAutirizacao(idRejeitoMpc);
	}

	@Override
	public void storeAll(List<RejeitoMpc> list) {
		super.storeAll(list);
	}

	@Override
	public Serializable store(RejeitoMpc bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setRejeitoMpcDAO(RejeitoMpcDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private RejeitoMpcDAO getRejeitoMpcDAO() {
		return (RejeitoMpcDAO) getDao();
	}

	public HistoricoMpcService getHistoricoMpcService() {
		return historicoMpcService;
	}

	public void setHistoricoMpcService(HistoricoMpcService historicoMpcService) {
		this.historicoMpcService = historicoMpcService;
	}

	public VolumeService getVolumeService() {
		return volumeService;
	}

	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}

	public CarregamentoService getCarregamentoService() {
		return carregamentoService;
	}

	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public HistoricoCarregamentoService getHistoricoCarregamentoService() {
		return historicoCarregamentoService;
	}

	public void setHistoricoCarregamentoService(HistoricoCarregamentoService historicoCarregamentoService) {
		this.historicoCarregamentoService = historicoCarregamentoService;
	}
}
