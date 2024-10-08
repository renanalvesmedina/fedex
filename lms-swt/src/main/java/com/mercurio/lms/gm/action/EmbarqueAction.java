package com.mercurio.lms.gm.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.net.CommandCallback;
import com.mercurio.adsm.net.ConnectionManager;
import com.mercurio.adsm.net.command.LdapValidateLoginCommand;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.gm.model.service.AutorizadorService;
import com.mercurio.lms.gm.model.service.CarregamentoService;
import com.mercurio.lms.gm.model.service.EmbarqueService;
import com.mercurio.lms.gm.model.service.HistoricoCarregamentoService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.gm.report.RelatorioDiscrepanciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * 
 * @spring.bean id="lms.gm.embarqueAction"
 */
public class EmbarqueAction extends CrudAction {
	private EmbarqueService embarqueService;
	private AutorizadorService autorizadorService;
	private CarregamentoService carregamentoService;
	private HistoricoCarregamentoService historicoCarregamentoService;
	private RelatorioDiscrepanciaService relatorioDiscrepanciaService;
	private VolumeService VolumeService;

	public EmbarqueService getEmbarqueService() {
		return embarqueService;
	}

	public void setEmbarqueService(EmbarqueService embarqueService) {
		this.embarqueService = embarqueService;
	}

	public HistoricoCarregamentoService getHistoricoCarregamentoService() {
		return historicoCarregamentoService;
	}

	public void setHistoricoCarregamentoService(HistoricoCarregamentoService historicoCarregamentoService) {
		this.historicoCarregamentoService = historicoCarregamentoService;
	}

	public AutorizadorService getAutorizadorService() {
		return autorizadorService;
	}

	public void setAutorizadorService(AutorizadorService autorizadorService) {
		this.autorizadorService = autorizadorService;
	}

	// Persiste um embarque
	public Map<String, Object> storeCarregamento(Map map) {
		Carregamento c = new Carregamento();
		String cnpj = (String) map.get("cnpjRemeClnt");
		c.setCnpjRemetenteCliente(Long.parseLong(cnpj));
		c.setDtInicio(new DateTime());
		c.setCodigoStatus(new DomainValue("1"));
		String matricula = (String) map.get("matriculaChefia");
		c.setMatriculaChefia(Long.parseLong(matricula));
		c.setTipoCarregamento((String) map.get("tipoCarregamento"));
		c.setFrotaVeiculo((String) map.get("frotaVeiculo"));
		c.setPlacaVeiculo((String) map.get("placaVeiculo"));
		c.setDocaCarregamento((String) map.get("docaCarregamento"));
		c.setRotaCarregamento((String) map.get("rotaCarregamento"));

		embarqueService.store(c);

		HistoricoCarregamento hc = new HistoricoCarregamento();
		hc.setIdCarregamento(c.getIdCarregamento());
		hc.setStatusCarregamento(c.getCodigoStatus());
		hc.setCnpjRemetenteCliente(c.getCnpjRemetenteCliente());
		hc.setRotaCarregamento(c.getRotaCarregamento());
		hc.setDataHistorico(new DateTime());
		hc.setMatriculaChefia(c.getMatriculaChefia());

		historicoCarregamentoService.store(hc);

		Map m = new HashedMap();
		m.put("idCarregamento", c.getIdCarregamento());
		return m;
	}

	public Map<String, Object> storeReabrirCarregamento(Map map) {
		String carregamento = (String) map.get("idCarregamento");
		Long idCarregamento = Long.parseLong(carregamento);
		Carregamento c = (Carregamento) this.embarqueService.findById(idCarregamento);

		c.setDtFim(null);
		c.setCodigoStatus(new DomainValue("2"));

		embarqueService.store(c);

		HistoricoCarregamento hc = new HistoricoCarregamento();
		hc.setIdCarregamento(c.getIdCarregamento());
		hc.setStatusCarregamento(c.getCodigoStatus());
		hc.setCnpjRemetenteCliente(c.getCnpjRemetenteCliente());
		hc.setRotaCarregamento(c.getRotaCarregamento());
		hc.setDataHistorico(new DateTime());
		hc.setMatriculaChefia(c.getMatriculaChefia());

		historicoCarregamentoService.store(hc);

		Map m = new HashedMap();
		m.put("idCarregamento", c.getIdCarregamento());
		return m;
	}

	// valida meio de transporte para o embarque
	public Map<String, Object> validateMeioTransporte(Map map) {
		String barcode = (String) map.get("nrCodigoBarras");
		Long codigoBarrasVeiculo = Long.parseLong(barcode);

		return embarqueService.findMeioTransporteByBarcode(codigoBarrasVeiculo);
	}

	// valida meio de transporte para o embarque
	public Map<String, Object> validateMeioTransportePorFrota(Map map) {
		String nrFrota = (String) map.get("nrFrota");

		return embarqueService.findMeioTransporteByFrota(nrFrota);
	}

	// valida doca para o embarque
	public Map<String, Object> validateDoca(Map map) {
		String doca = (String) map.get("docaCarregamento");
		return embarqueService.findDocaDisponivel(doca);
	}

	// valida rota para o embarque
	public Map<String, Object> validateRota(Map map) {


		String frotaVeiculo = (String) map.get("frotaVeiculo");

		String doca = (String) map.get("docaCarregamento");
		String rotaEmbarque = (String) map.get("rotaCarregamento");
		String tipoCarregamento = (String) map.get("tipoCarregamento");

		return embarqueService.findRotaBySigla(frotaVeiculo, doca, rotaEmbarque, tipoCarregamento);
	}

	// Concluir Embarque
	public Map<String, Object> concluirCarregamento(Map map) throws Exception {
		Carregamento carregamento = new Carregamento();

		String id = (String) map.get("idCarregamento");
		carregamento.setIdCarregamento(Long.parseLong(id));

		String usuario = (String) map.get("matriculaChefia");
		carregamento.setMatriculaChefia(Long.parseLong(usuario));

		Map mapCarregamento = embarqueService.executeConcluirEmbarque(carregamento);

		carregamento = (Carregamento) embarqueService.findById(carregamento.getIdCarregamento());

		enviaRelatorioDiscrepancia(carregamento, id);

		return mapCarregamento;
	}

	// Concluir Embarque com Pendencia
	public void concluirCarregamentoComPendencia(Map map) throws Exception {
		String id = (String) map.get("idCarregamento");
		String usuario = (String) map.get("matriculaChefia");

		Carregamento carregamento = new Carregamento();
		carregamento.setIdCarregamento(Long.parseLong(id));
		carregamento.setMatriculaChefia(Long.parseLong(usuario));
		embarqueService.executeConcluirEmbarqueComPendencia(carregamento);

		carregamento = (Carregamento) embarqueService.findById(carregamento.getIdCarregamento());

		enviaRelatorioDiscrepancia(carregamento, id);
	}

	// Fechamento Embarque
	public Map<String, Object> fecharCarregamento(Map map) throws Exception {
		Carregamento carregamento = new Carregamento();
		String id = (String) map.get("idCarregamento");
		carregamento.setIdCarregamento(Long.parseLong(id));
		return embarqueService.executeFechamentoEmbarque(carregamento);
	}

	/**
	 * Busca o status do carregamento para utilizar no embarque GM. Necessario para garantir que apenas um coletor abra, conclua, cancele e feche um carregamento.
	 * 
	 * Demanda LMS-1538
	 * 
	 * @param idCarregamento
	 * @return
	 */
	public Map<String, Object> getStatusCarregamento(Map map) {
		String id = (String) map.get("idCarregamento");
		Long idCarregamento = Long.parseLong(id);
		String placaVeiculo = (String) map.get("placaVeiculo");

		Carregamento carregamento = new Carregamento();
		if (idCarregamento != 0) {
			carregamento = (Carregamento) carregamentoService.findCarregamentoById(idCarregamento);
		} else {
			// Busca pela placa do veiculo no caso do embarque
			carregamento = (Carregamento) carregamentoService.findCarregamentoByPlacaVeiculo(placaVeiculo);
		}
		Map mapResp = new HashMap();
		if(carregamento != null ){
			mapResp.put("codigoStatus", Integer.parseInt(carregamento.getCodigoStatus().getValue()));
		} else {
			mapResp.put("codigoStatus", 0);
		}
		return mapResp;
	}

	private void enviaRelatorioDiscrepancia(Carregamento carregamento, String id) throws Exception {
		List<Volume> listVolumes = this.VolumeService.findVolumesByCarregamento(carregamento.getIdCarregamento());
		ArrayList<String> listMapasCarregamento = new ArrayList<String>();
		ArrayList<String> listIds = new ArrayList<String>();
		String mapas = "O Relatorio em anexo contempla o carregamento " + carregamento.getFrotaVeiculo() + "/" + carregamento.getPlacaVeiculo() + " do(s) seguinte(s) Mapa(s) de Carregamento(s): ";

		if (listVolumes != null) {
			for (Volume volume : listVolumes) {
				if (!(listMapasCarregamento.contains(volume.getMapaCarregamento().toString()))) {
					listMapasCarregamento.add(volume.getMapaCarregamento().toString());
				}
			}
			for (String mapa : listMapasCarregamento) {
				mapas = mapas + mapa + "; ";
			}
		}

		Map parameters = new HashedMap();
		parameters.put("origem", SessionUtils.getFilialSessao().getIdFilialConcatenado());
		parameters.put("tipoEmitente", "GM");
		parameters.put("dataFechamento", new DateTime());
		parameters.put("emailEndereco", "iardlei.agassi@tntbrasil.com.br");
		parameters.put("emailCC", "iardlei.agassi@tntbrasil.com.br");
		parameters.put("emailTitulo", "Relat�rio de Discrep�ncias GM x TNT");
		parameters.put("emailCorpo", mapas);
		listIds.add(id);
		parameters.put("ids", listIds);
		this.relatorioDiscrepanciaService.executeReport(parameters);
	}

	// Fechamento Embarque com Pendencia
	public void fecharCarregamentoComPendencia(Map map) {
		Carregamento carregamento = new Carregamento();
		String id = (String) map.get("idCarregamento");
		carregamento.setIdCarregamento(Long.parseLong(id));
		embarqueService.executeFechamentoEmbarqueComPendencia(carregamento);
	}

	public void autorizarCarregamentoComPendencia(Map map) {
		final String usuario = (String) map.get("login");
		final String nrPin = (String) map.get("nrPin");
		final ConnectionManager cm = ConnectionManager.getInstance();

		CommandCallback<Serializable> validateLoginCallback = null;
		validateLoginCallback = new CommandCallback<Serializable>() {
			public void onSuccess(Serializable response) {
				autorizadorService.autorizarPendencia(usuario, nrPin);
			}
		};
		cm.submit(new LdapValidateLoginCommand(usuario, nrPin), validateLoginCallback);
	}

	public void retornaMensagem(String messageKey) {
		throw new BusinessException(messageKey);
	}

	public RelatorioDiscrepanciaService getRelatorioDiscrepanciaService() {
		return relatorioDiscrepanciaService;
	}

	public void setRelatorioDiscrepanciaService(RelatorioDiscrepanciaService relatorioDiscrepanciaService) {
		this.relatorioDiscrepanciaService = relatorioDiscrepanciaService;
	}

	public VolumeService getVolumeService() {
		return VolumeService;
	}

	public void setVolumeService(VolumeService volumeService) {
		VolumeService = volumeService;
	}

	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}
}
