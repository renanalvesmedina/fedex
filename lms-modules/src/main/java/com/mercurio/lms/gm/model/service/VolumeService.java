package com.mercurio.lms.gm.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.DetalheRota;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoVolume;
import com.mercurio.lms.carregamento.model.RotaEmbarque;
import com.mercurio.lms.carregamento.model.TotalCarregamento;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTransporteDAO;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDescargaService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.gm.model.dao.CabecalhoCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.DetalheCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.DetalheRotaDAO;
import com.mercurio.lms.gm.model.dao.EmbarqueDAO;
import com.mercurio.lms.gm.model.dao.HistoricoVolumeDAO;
import com.mercurio.lms.gm.model.dao.RotaEmbarqueDAO;
import com.mercurio.lms.gm.model.dao.TotalCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.VolumeDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.gm.volumeService"
 */
public class VolumeService extends CrudService<Volume, Long> {
	private VolumeDAO volumeDao;
	private EmbarqueDAO embarqueDao;
	private RotaEmbarqueDAO rotaEmbarqueDao;
	private DetalheCarregamentoDAO detalheCarregamentoDao;
	private DetalheRotaDAO detalheRotaDao;
	private MeioTransporteDAO meioTransporteDao;
	private HistoricoVolumeDAO historicoVolumeDao;
	private CabecalhoCarregamentoDAO cabecalhoCarregamentoDao;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private HistoricoCarregamentoService historicoCarregamentoService;
	private MeioTransporteService meioTransporteService;
	protected ConfiguracoesFacade configuracoesFacade;
	private CarregamentoService carregamentoService;
	private TotalCarregamentoDAO totalCarregamentoDAO;
	private UsuarioLMSService usuarioLMSService;


	/**
	 * Recebe o codigo de barras do volume para realizar a pesquisa e as validações.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume, codigo de barras
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findVolumeByCodigoVolume(String codigoVolume) {

		DetalheCarregamento detalheCarregamento = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(codigoVolume);
		if (detalheCarregamento == null) {
			throw new BusinessException("LMS-04324");
		}

		Volume volume = volumeDao.findVolumeByCodigoVolume(codigoVolume);

		Map map = new HashMap();

		if (volume != null) {
			Carregamento carregamento = embarqueDao.findCarregamentoByid(volume.getCarregamento().getIdCarregamento());			

			map.put("mpc", volume.getMapaCarregamento().toString());
			
			// INICIO DO TESTE DE CARREGAMENTO BASICO
			//Este teste é para garantir que  volumes basicos relacionados a carregamentos basicos possam ser exibido na tela de consultar volume
			//seta os campos referente ao veiculo e ao embarque vazio.
			if(carregamento.getPlacaVeiculo() != null && carregamento.getFrotaVeiculo() != null && carregamento.getDocaCarregamento() != null) {
				map.put("veiculo", carregamento.getFrotaVeiculo() + "/" + carregamento.getPlacaVeiculo());
				map.put("doca", carregamento.getDocaCarregamento());
			} else {
				map.put("veiculo", "");
				map.put("doca", "");
			}
			
			if(carregamento.getRotaCarregamento() != null) {
				RotaEmbarque rotaEmbarque = rotaEmbarqueDao.findRotaBySigla(carregamento.getRotaCarregamento());
				map.put("rota", rotaEmbarque.getSiglaRota());
			} else {
				map.put("rota", "");
			}
			// FIM DO TESTE DE CARREGAMENTO BASICO
			map.put("situacao", volume.getCodigoStatus().getDescription());
			map.put("destinatario", detalheCarregamento.getCodigoDestino());
			map.put("sofreuInteracao", Boolean.TRUE);
		} else {
			map.put("mpc", detalheCarregamento.getMapaCarregamento().toString());
			map.put("rota", detalheCarregamento.getRotaDestino());
			map.put("destinatario", detalheCarregamento.getCodigoDestino());
			map.put("sofreuInteracao", Boolean.FALSE);
		}

		return map;
	}

	/**
	 * Recebe um codigo de barras de um veiculo para validação.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findMeioTransporteByBarcode(Long barcode) {
		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByCodigoBarras(barcode);

		if (meioTransporte.getNrFrota() == null) {
			throw new BusinessException("LMS-04313");
		}
		return findMapMeioTransporteValido(meioTransporte);
	}

	// Retorna o historico do volume
	public List findListaHistoricoVolumeByVolume(Map param) {
		List<Map> listaHistoricoVolume = volumeDao.findListaHistoricoVolumeByVolume(param);

		for (Map m : listaHistoricoVolume) {
			m.put("DescricaoCodigoStatus", ((DomainValue) m.get("DescricaoCodigoStatus")).getDescription().toString());
			m.put("dataHistorico", m.get("dataHistorico").toString());
		}

		return listaHistoricoVolume;
	}

	/**
	 * Recebe um Id de Carregamento e Retorna os Volumes do mesmo.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            idCarregamento
	 * @return Lista de Volumes.
	 */
	public List findVolumesByCarregamento(Long idCarregamento) {
		List<Volume> listVolumesCarregamento = volumeDao.findVolumeByIdCarregamento(idCarregamento);

		return listVolumesCarregamento;
	}

	/**
	 * Recebe um MeioTransporte para as demais validaçoes dos demais dados da tela.
	 * 
	 * @author Samuel Alves
	 * @param MeioTransporte
	 *            meioTransporte
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findMapMeioTransporteValido(MeioTransporte meioTransporte) {
		Map map = new HashMap();
		List<Carregamento> listCarregamento = embarqueDao.findCarregamentoByFrotaVeiculo(meioTransporte.getNrFrota());
		for (Carregamento carregamento : listCarregamento) {
			if (carregamento.getFrotaVeiculo() != null) {
				if (!carregamento.getCodigoStatus().getValue().equals("1") || !carregamento.getCodigoStatus().getValue().equals("2")) {
					throw new BusinessException("LMS-04323");
				} else {
					map.put("meioTransporte", meioTransporte.getNrFrota());
					map.put("rotaCarregamento", carregamento.getRotaCarregamento());
					map.put("cnpjRemeClnt", carregamento.getCnpjRemetenteCliente());
				}
			}
		}
		return map;
	}

	/**
	 * Recebe um codigo de barras de um volume para validação e inclusão de registro de carregamento de volume.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume
	 * @param String
	 *            codigoVeiculo
	 * @param String
	 *            doca
	 * @param Long
	 *            usuarioLogado
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map executeVolumeByCodigo(String codigoVolume, Long idCarregamento, Long usuarioLogado, Long mpcDaConferencia) {
		Map map = new HashMap();

		DetalheCarregamento detalheCarregamento = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(codigoVolume);
		
		if (detalheCarregamento == null) {
			throw new BusinessException("LMS-04324");
		}
		
		Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);
		
		if (!(carregamento.getCodigoStatus().getValue().equals("1") || carregamento.getCodigoStatus().getValue().equals("2"))) {
			throw new BusinessException("LMS- Carregamento Indisponivel para Embarque");
		}

		CabecalhoCarregamento cabecalhoCarregamento = cabecalhoCarregamentoDao.findCabecalhoByid(detalheCarregamento.getIdCabecalhoCarregamento());
		Volume volume = volumeDao.findVolumeByCodigoVolume(codigoVolume);		
		Boolean foraDestino = false;
		Volume v;

		// Verifica se o volume esta apto para embarque
		if (volume != null && (!volume.getCodigoStatus().getValue().equals("4") && !volume.getCodigoStatus().getValue().equals("5") && !volume.getCodigoStatus().getValue().equals("0"))) {
			// Verifica se o volume esta em conferência
			if (volume.getCodigoStatus().getValue().equals("7")) {

				// Verifica se o volume esta em outro veiculo
				if (!volume.getCarregamento().getIdCarregamento().equals(idCarregamento)) {
					throw new BusinessException("LMS-45123", new Object[] { volume.getCarregamento().getPlacaVeiculo() }); // aqui
				} else {
					
					// Verifica se o MpC está em conferência por outro usuário no mesmo carregamento 
					Volume volumeConferidoPorOutro = validateConferenciaMpcCarregadoPorOutroUsuario(idCarregamento, detalheCarregamento.getMapaCarregamento(), usuarioLogado);
					if (volumeConferidoPorOutro != null) {
						throw new BusinessException("LMS-45119", new Object[] { volumeConferidoPorOutro.getMatriculaResponsavel().toString() });
					}
					
					TotalCarregamento totalCarregamento = totalCarregamentoDAO.findTotalByMapaCarregamento(detalheCarregamento.getMapaCarregamento());

					Integer numeroDeVolumes = 0;
					// Estaremos contando os volumes por mapa e não pelo carregamento em questão.
					numeroDeVolumes = volumeDao.getRowCountVolumesMpcByIdCarregamentoStatusVolume(null, volume.getMapaCarregamento(), new String[] { "1", "2", "3", "6", "7" });

					map.put("mapaCarregamento", detalheCarregamento.getMapaCarregamento());
					map.put("totalVolumesConferidos", numeroDeVolumes);
					map.put("totalVolumes", totalCarregamento.getTotalVolume());
					map.put("idCabecalhoCarregamento", cabecalhoCarregamento.getIdCabecalhoCarregamento());
					map.put("volumeJaConferido", true);
					return map;
				}
			} else {
				throw new BusinessException("LMS-04325");
			}
		}

		// Validar a rota
		Boolean foraDaRota = true;
		RotaEmbarque rotaEmbarque = rotaEmbarqueDao.findRotaBySigla(carregamento.getRotaCarregamento());
		List<DetalheRota> listDetalheRota = detalheRotaDao.findDetalheRotaByIdRotaEmbarque(rotaEmbarque.getIdRotaEmbarque());
		for (DetalheRota detalheRota : listDetalheRota) {
			if (detalheRota.getSiglaRota().equals(detalheCarregamento.getRotaDestino())) {
				foraDaRota = false;
			}
		}

		if (foraDaRota) {
			throw new BusinessException("LMS-04326");
		}

		// Se o volume lido não pertence ao carregamento
		if (mpcDaConferencia != null && !detalheCarregamento.getMapaCarregamento().equals(mpcDaConferencia)) {
			throw new BusinessException("LMS-45111");
		}

		// Se o volume lido não pertence ao usuário que esta bipando
		Volume volumeConferidoPorOutro = validateConferenciaMpcCarregadoPorOutroUsuario(idCarregamento, detalheCarregamento.getMapaCarregamento(), usuarioLogado);
		if (volumeConferidoPorOutro != null) {
			throw new BusinessException("LMS-45119", new Object[] { volumeConferidoPorOutro.getMatriculaResponsavel().toString() });
		}

		if (volume != null) {
			volume.setCarregamento(carregamento);
			volume.setMapaCarregamento(detalheCarregamento.getMapaCarregamento());
			volume.setCodigoVolume(codigoVolume);
			volume.setCnpjDestinatario(carregamento.getCnpjRemetenteCliente());
			volume.setDataEmbarque(new DateTime());
			volume.setItemVolume(detalheCarregamento.getItemVolume());
			volume.setCubagemVolume(detalheCarregamento.getCubagemVolume());
			volume.setPesoVolume(detalheCarregamento.getPesoVolume());
			volume.setCodigoStatus(new DomainValue("7"));
			volume.setMatriculaResponsavel(usuarioLogado);

			volumeDao.store(volume, true);
			v = volume;
		} else {
			List<Volume> listVolumes = this.findVolumesByCarregamento(carregamento.getIdCarregamento());

			if (listVolumes != null) {
				DetalheCarregamento detalhePrimeiroVolume = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(listVolumes.get(0).getCodigoVolume());

				if ("D".equals(carregamento.getTipoCarregamento())) {
					if (!(detalheCarregamento.getCodigoDestino().equals(detalhePrimeiroVolume.getCodigoDestino()))) {
						foraDestino = true;
					}
				}
			}

			Volume newVolume = new Volume();
			newVolume.setCarregamento(new Carregamento(idCarregamento));
			newVolume.setCabecalhoCarregamento(new CabecalhoCarregamento(detalheCarregamento.getIdCabecalhoCarregamento()));
			newVolume.setMapaCarregamento(detalheCarregamento.getMapaCarregamento());
			newVolume.setCodigoVolume(codigoVolume);
			newVolume.setCnpjDestinatario(carregamento.getCnpjRemetenteCliente());
			newVolume.setDataEmbarque(new DateTime());
			newVolume.setItemVolume(detalheCarregamento.getItemVolume());
			newVolume.setCubagemVolume(detalheCarregamento.getCubagemVolume());
			newVolume.setPesoVolume(detalheCarregamento.getPesoVolume());
			newVolume.setCodigoStatus(new DomainValue("7"));
			newVolume.setMatriculaResponsavel(usuarioLogado);

			this.store(newVolume);
			v = volumeDao.findVolumeByCodigoVolume(codigoVolume);
		}

		HistoricoVolume historicoVolume = new HistoricoVolume();
		historicoVolume.setIdVolume(v.getIdVolume());
		historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
		historicoVolume.setDataHistorico(new DateTime());
		historicoVolume.setCodigoVolume(codigoVolume);
		historicoVolume.setCodigoStatus(new DomainValue("7"));
		historicoVolume.setMatriculaResponsavel(usuarioLogado);
		historicoVolume.setAutorizador(null);

		historicoVolumeDao.storeHistoricoVolume(historicoVolume);

		if (carregamento.getCodigoStatus().getValue().equals("1")) {
			HistoricoCarregamento hc = new HistoricoCarregamento();
			hc.setIdCarregamento(carregamento.getIdCarregamento());
			hc.setStatusCarregamento(new DomainValue("2"));
			hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
			hc.setRotaCarregamento(carregamento.getRotaCarregamento());
			hc.setDataHistorico(new DateTime());
			hc.setMatriculaChefia(usuarioLogado);

			historicoCarregamentoService.store(hc);
		}

		carregamento.setCodigoStatus(new DomainValue("2"));

		embarqueDao.store(carregamento);

		if (foraDestino) {
			map.put("foraDestino", (Boolean) true);
		} else {
			map.put("foraDestino", (Boolean) false);
		}

		TotalCarregamento totalCarregamento = totalCarregamentoDAO.findTotalByMapaCarregamento(detalheCarregamento.getMapaCarregamento());

		Integer numeroDeVolumes = 0;
		// Estaremos contando os volumes por mapa e não pelo carregamento em
		// questão.
		numeroDeVolumes = volumeDao.getRowCountVolumesMpcByIdCarregamentoStatusVolume(null, v.getMapaCarregamento(), new String[] { "1", "2", "3", "6", "7" });

		map.put("mapaCarregamento", detalheCarregamento.getMapaCarregamento());
		map.put("totalVolumesConferidos", numeroDeVolumes);
		map.put("totalVolumes", totalCarregamento.getTotalVolume());
		map.put("idCabecalhoCarregamento", cabecalhoCarregamento.getIdCabecalhoCarregamento());
		map.put("volumeJaConferido", false);	
		
		return map;
	}

	/**
	 * Se o MpC já está em conferência por outro usuário no mesmo carregamento return true caso contrário retorna false
	 * 
	 * @param idCarregamento
	 *            id do carregamento onde o mapa esta sendo conferido
	 * @param mpcDaConferencia
	 *            mapa sendo conferido
	 * @param matricula
	 *            do usuario logado
	 * @return
	 */
	public Volume validateConferenciaMpcCarregadoPorOutroUsuario(Long idCarregamento, Long mpcDaConferencia, Long matricula) {
		return this.volumeDao.findVolumeMpcConferidoByIdCarregamentoEResponsavelComMatriculaDiferente(idCarregamento, mpcDaConferencia, matricula);
	}

	/**
	 * Serviço responsavel por finalizar um carregamento. Passa todos os volume do carregamento para status Carregado(7) e gera um histórico para cada volume.
	 * 
	 * Demanda LMS-2772
	 * 
	 * @param idCarregamento
	 * @param usuarioLogado
	 * @param mpcDaConferencia
	 * @return
	 */
	public Map storeCarregamentoVolumes(Long idCarregamento, Long usuarioLogado, Long mpcDaConferencia, Long idUsuarioAutorizador) {
		Map map = new HashMap();
		map.put("storeCarregarVolumeResposta", (Boolean) false);

		Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);
		List<Volume> listVolumes = this.findVolumesByCarregamento(carregamento.getIdCarregamento());

		for (Volume volume : listVolumes) {

			if (volume.getCodigoStatus().getValue().equals("7") && volume.getMapaCarregamento().equals(mpcDaConferencia)) {

				volume.setCodigoStatus(new DomainValue("1"));

				if (idUsuarioAutorizador != null) {
					volume.setMatriculaResponsavel(idUsuarioAutorizador);
				} else {
					volume.setMatriculaResponsavel(usuarioLogado);
				}
				volumeDao.store(volume, true);

				HistoricoVolume historicoVolume = new HistoricoVolume();
				historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
				historicoVolume.setIdVolume(volume.getIdVolume());
				historicoVolume.setDataHistorico(new DateTime());
				historicoVolume.setCodigoVolume(volume.getCodigoVolume());
				historicoVolume.setCodigoStatus(new DomainValue("1"));
				historicoVolume.setMatriculaResponsavel(usuarioLogado);

				if (idUsuarioAutorizador != null && !idUsuarioAutorizador.equals("")) {
					UsuarioLMS usuarioLMS = usuarioLMSService.findById(idUsuarioAutorizador);
					historicoVolume.setAutorizador(usuarioLMS.getUsuarioADSM().getNrMatricula());
				} else {
					historicoVolume.setAutorizador(null);
				}

				historicoVolumeDao.storeHistoricoVolume(historicoVolume);
			}
		}

		map.put("storeCarregarVolumeResposta", (Boolean) true);

		return map;
	}

	/**
	 * Serviço responsavel por descarregar os volumes carregados. Utilizado para quando o carregamento é cancelado e é necessario descarregar volumes já carregados. Caso haja algum volume ele faz um update dos mesmos passando seu status para descarregado(4), se não houver nenhum volume só gerado um historio do carregamento.
	 * 
	 * Demanda LMS-2772
	 * 
	 * @param idCarregamento
	 * @param usuarioLogado
	 * @param mpcDaConferencia
	 * @param cnpjRemetenteCliente
	 * @return
	 */
	public Map executeDescarregarVolumesMpC(Long idCarregamento, Long usuarioLogado, Long mpcDaConferencia, Long cnpjRemetenteCliente) {
		Map map = new HashMap();
		map.put("storeCarregarVolumeResposta", (Boolean) false);

		Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);
		List<Volume> listVolumesCarregados = volumeDao.findVolumesCarregadosByIdCarregamento(idCarregamento);

		if (listVolumesCarregados == null || listVolumesCarregados.isEmpty()) {
			storeHistoricoCarregamento(idCarregamento, usuarioLogado, mpcDaConferencia, cnpjRemetenteCliente);
		} else {
			for (Volume volume : listVolumesCarregados) {

				if (volume.getCodigoStatus().getValue().equals("7") && volume.getMapaCarregamento().equals(mpcDaConferencia)) {
					volume.setCodigoStatus(new DomainValue("4"));
					volume.setMatriculaResponsavel(usuarioLogado);
					volumeDao.store(volume, true);

					HistoricoVolume historicoVolume = new HistoricoVolume();
					historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
					historicoVolume.setIdVolume(volume.getIdVolume());
					historicoVolume.setDataHistorico(new DateTime());
					historicoVolume.setCodigoVolume(volume.getCodigoVolume());
					historicoVolume.setCodigoStatus(new DomainValue("4"));
					historicoVolume.setMatriculaResponsavel(usuarioLogado);
					historicoVolume.setAutorizador(null);

					historicoVolumeDao.storeHistoricoVolume(historicoVolume);
				}
			}
		}

		// pesquisar novamente após a alteração dos status dos volumes do mapa, caso não tenho volumes em 1,2,3 e 7 atualiza o carregamento e gera histórico
		listVolumesCarregados = volumeDao.findVolumesCarregadosByIdCarregamento(idCarregamento);
		if (listVolumesCarregados == null || listVolumesCarregados.isEmpty()) {
			storeHistoricoCarregamento(idCarregamento, usuarioLogado, mpcDaConferencia, cnpjRemetenteCliente);
		}

		map.put("storeCarregarVolumeResposta", (Boolean) true);
		return map;
	}

	/**
	 * Serviço para salvar o histórico do carregamento. Usado na tela de Carregar Volume, quando se finaliza um carregamento e quando sai da tela.
	 * 
	 * Demanda LMS-2772
	 * 
	 * @param idCarregamento
	 * @param usuarioLogado
	 * @param mpcDaConferencia
	 * @param cnpjRemetenteCliente
	 * @return
	 */
	public Map storeHistoricoCarregamento(Long idCarregamento, Long usuarioLogado, Long mpcDaConferencia, Long cnpjRemetenteCliente) {
		Map map = new HashMap();

		Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);

		List<Volume> listVolumesCarregados = volumeDao.findVolumesCarregadosByIdCarregamento(idCarregamento);

		if (listVolumesCarregados == null || listVolumesCarregados.isEmpty()) {
			carregamento.setCodigoStatus(new DomainValue("1"));
			carregamentoService.store(carregamento);

			HistoricoCarregamento hc = new HistoricoCarregamento();
			hc.setIdCarregamento(carregamento.getIdCarregamento());
			hc.setStatusCarregamento(new DomainValue("1"));
			hc.setCnpjRemetenteCliente(cnpjRemetenteCliente);
			hc.setRotaCarregamento(carregamento.getRotaCarregamento());
			hc.setDataHistorico(new DateTime());
			hc.setMatriculaChefia(usuarioLogado);

			historicoCarregamentoService.store(hc);
		}

		return map;
	}

	/**
	 * Recebe um codigo de barras de um veiculo para validação na tela de desembarque de volume
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map validateMeioTransporteByBarcode(Long barcode) {
		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByCodigoBarras(barcode);

		if (meioTransporte.getNrFrota() == null) {
			throw new BusinessException("LMS-04313");
		}

		Map map = new HashMap();
		List<Carregamento> listCarregamento = embarqueDao.findCarregamentoByFrotaVeiculo(meioTransporte.getNrFrota());
		for (Carregamento carregamento : listCarregamento) {
			if (carregamento.getFrotaVeiculo() != null) {
				if (!carregamento.getCodigoStatus().getValue().equals("2")) {
					throw new BusinessException("LMS-04323");
				} else {
					map.put("meioTransporte", meioTransporte.getNrFrota());
					map.put("doca", carregamento.getDocaCarregamento());
				}
			}
		}
		return map;
	}

	/**
	 * Recebe o codigo de barras do volume para realizar a pesquisa e as validações da tela de Descarregamento de volume para a tela de desembarque.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume
	 * @param Long
	 *            codigoBarrasVeiculo
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map executeVolumeByCodigo(String codigoVolume, Long codigoBarrasVeiculo, String doca, Long usuarioLogado) {

		DetalheCarregamento detalheCarregamento = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(codigoVolume);
		Volume volume = volumeDao.findVolumeByCodigoVolume(codigoVolume);
		Carregamento carregamento = embarqueDao.findCarregamentoByid(volume.getCarregamento().getIdCarregamento());
		MeioTransporte meioTransporte = meioTransporteDao.findMeioTransporteByCodigoBarras(codigoBarrasVeiculo);

		Map map = new HashMap();
		if (detalheCarregamento == null) {
			throw new BusinessException("LMS-04324");
		}

		if (!volume.getCodigoVolume().equals(codigoVolume) || !carregamento.getCodigoStatus().getValue().equals("1") || !carregamento.getCodigoStatus().getValue().equals("2") || !carregamento.getCodigoStatus().getValue().equals("3") || volume.getCarregamento().getIdCarregamento() != carregamento
				.getIdCarregamento() || carregamento.getFrotaVeiculo() != meioTransporte.getNrFrota()) {
			throw new BusinessException("LMS-04329");
		}

		volume.setCodigoVolume(codigoVolume);
		volume.setCodigoStatus(new DomainValue("4"));
		volume.setMatriculaResponsavel(usuarioLogado);

		volumeDao.storeVolume(volume);

		HistoricoVolume historicoVolume = new HistoricoVolume();
		historicoVolume.setIdCarregamento(volume.getCarregamento().getIdCarregamento());
		historicoVolume.setDataHistorico(new DateTime());
		historicoVolume.setCodigoVolume(codigoVolume);
		historicoVolume.setCodigoStatus(new DomainValue("4"));
		historicoVolume.setMatriculaResponsavel(usuarioLogado);

		historicoVolumeDao.storeHistoricoVolume(historicoVolume);

		map.put("veiculo", String.valueOf(codigoBarrasVeiculo));
		map.put("doca", doca);
		map.put("volume", codigoVolume);

		return map;
	}

	/**
	 * Recebe o codigo de barras do volume para realizar a pesquisa e as validações da tela de Descarregamento de volume para a tela de desembarque.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume
	 * @param Long
	 *            codigoBarrasVeiculo
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map executeValidateVolume(String codigoVolume) {
		Map map = new HashMap();
		Volume volume = volumeDao.findVolumeByCodigoVolume(codigoVolume);

		DetalheCarregamento detalheCarregamento = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(codigoVolume);

		if (volume != null) {
			if (detalheCarregamento == null) {
				throw new BusinessException("LMS-04324");
			}

			if (!(volume.getCodigoStatus().getValue().equals("1") || volume.getCodigoStatus().getValue().equals("2") || volume.getCodigoStatus().getValue().equals("3"))) {

				throw new BusinessException("LMS-04329");

			}

			if (volume.getCarregamento().getCodigoStatus().getValue().equals("6")) {
				throw new BusinessException("LMS-04328");
			}
		} else {
			throw new BusinessException("LMS-04329");
		}

		map.put("idVolume", volume.getIdVolume());
		map.put("carregamento", volume.getCarregamento().getMapped());
		map.put("mapaCarregamento", volume.getMapaCarregamento());
		map.put("volumeMpC", totalCarregamentoDAO.findTotalCarregamentoByIdCabecalhoCarregamento(detalheCarregamento.getIdCabecalhoCarregamento()));
		map.put("volumeCarregado", this.getRowCountVolumesCarregados(volume.getCarregamento().getIdCarregamento(), volume.getMapaCarregamento()));
		return map;
	}

	public Map executeValidateVolumeAvariaDefeito(String codigoVolume, Long usuarioLogado) {

		Map map = new HashMap();
		Volume volume = volumeDao.findVolumeByCodigoVolume(codigoVolume);

		DetalheCarregamento detalheCarregamento = detalheCarregamentoDao.findDetalheCarregamentoByCodigoVolume(codigoVolume);

		if (detalheCarregamento == null) {
			throw new BusinessException("LMS-04324");
		}

		if (volume != null) {
			// LMS-2774
			// Volume Expedido - Indisponível para marcação de defeito/avaria
			if (volume.getCodigoStatus().getValue().equals("6")) {
				throw new BusinessException("LMS-04332");
			}
			map.put("StatusVolumeAtual", volume.getCodigoStatus().getValue());
			map.put("IdCarregamentoAtual", volume.getCarregamento().getIdCarregamento());
			map.put("PlacaVeiculo", volume.getCarregamento().getPlacaVeiculo());
			map.put("Destino", volume.getCarregamento().getRotaCarregamento());
			map.put("StatusCarregamento", volume.getCarregamento().getCodigoStatus().getValue());
			map.put("DescricaoStatusCarregamento", volume.getCarregamento().getCodigoStatus().getDescription().toString());
			map.put("StatusVolume", volume.getCodigoStatus().getValue());
			map.put("DescricaoStatusVolume", volume.getCodigoStatus().getDescription().toString());
			map.put("IdVolume", volume.getIdVolume());

		} else {
			// Se não existe carregamento básico, insere um novo carregamento
			// básico
			Carregamento carregamentoBasico = new Carregamento();
			carregamentoBasico.setCodigoStatus(new DomainValue("0")); // Avaria

			List<Carregamento> listCarregamentos = new ArrayList<Carregamento>();
			listCarregamentos.add(carregamentoBasico);

			carregamentoService.storeAll(listCarregamentos);

			Volume volumeBasico = new Volume();
			volumeBasico.setCabecalhoCarregamento(new CabecalhoCarregamento(detalheCarregamento.getIdCabecalhoCarregamento()));
			volumeBasico.setCarregamento(carregamentoBasico);
			volumeBasico.setMapaCarregamento(detalheCarregamento.getMapaCarregamento());
			volumeBasico.setCodigoVolume(codigoVolume);
			volumeBasico.setDataEmbarque(new DateTime());
			volumeBasico.setItemVolume(detalheCarregamento.getItemVolume());
			volumeBasico.setCubagemVolume(detalheCarregamento.getCubagemVolume());
			volumeBasico.setPesoVolume(detalheCarregamento.getPesoVolume());
			volumeBasico.setCodigoStatus(new DomainValue("0")); // Básico
			volumeBasico.setMatriculaResponsavel(usuarioLogado);

			volumeDao.store(volumeBasico);

			map.put("StatusVolumeAtual", 0);
			map.put("IdCarregamentoAtual", carregamentoBasico.getIdCarregamento());
			map.put("PlacaVeiculo", "");
			map.put("Destino", "");
			map.put("StatusCarregamento", "");
			map.put("StatusVolume", "");
			map.put("IdVolume", volumeBasico.getIdVolume());

		}

		return map;
	}

	/**
	 * Retorna total de volumes do MpC carregados no veículo (soma VOLUME.id_carregamento Onde VOLUME.mapa_carregamento = campo [5] e VOLUME.id_carregamento = carregamentoVolume e VOLUME.codigo_status in (1,2,3));
	 * 
	 * @param idCarregamento
	 * @param mapaCarregamento
	 * @return
	 */
	public Integer getRowCountVolumesCarregados(Long idCarregamento, Long mapaCarregamento) {
		return volumeDao.getRowCountVolumesMpcByIdCarregamentoStatusVolume(idCarregamento, mapaCarregamento, new String[] { "1", "2", "3" });
	}

	/**
	 * Retorna total de volumes do MpC carregados no veículo, caso o id do mesmo seja informado, onde o status do volume esteja no array de status passado como parametro <br>
	 * Caso o idCarregamento não seja informado retorna o total de volumes do MpC onde o status do volume esteja no array de status passado como parametro
	 * 
	 * @param idCarregamento
	 * @param mapaCarregamento
	 * @param status
	 * @return
	 */
	public Integer getRowCountVolumesMpcByIdCarregamentoStatusVolume(Long idCarregamento, Long mapaCarregamento, String[] status) {
		return volumeDao.getRowCountVolumesMpcByIdCarregamentoStatusVolume(idCarregamento, mapaCarregamento, status);
	}

	public void executeVolumeDescarga(Long idVolume, Long idCarregamento, Long usuarioLogado) {
		Volume volume = (Volume) this.findById(idVolume);

		volume.setCodigoStatus(new DomainValue("4"));
		volume.setMatriculaResponsavel(usuarioLogado);
		volumeDao.store(volume, true);

		HistoricoVolume historicoVolume = new HistoricoVolume();
		historicoVolume.setIdVolume(volume.getIdVolume());
		historicoVolume.setIdCarregamento(volume.getCarregamento().getIdCarregamento());
		historicoVolume.setDataHistorico(new DateTime());
		historicoVolume.setCodigoVolume(volume.getCodigoVolume());
		historicoVolume.setCodigoStatus(new DomainValue("4"));
		historicoVolume.setMatriculaResponsavel(usuarioLogado);

		historicoVolumeDao.storeHistoricoVolume(historicoVolume);

		List<Volume> listVolume = this.findVolumesByCarregamento(idCarregamento);
		Boolean atualizaCarregamento = true;

		if (listVolume != null) {
			for (Volume v : listVolume) {
				if (v.getCodigoStatus().getValue().equals("1") || v.getCodigoStatus().getValue().equals("2") || v.getCodigoStatus().getValue().equals("3")) {
					atualizaCarregamento = false;
					break;
				}
			}
		}

		if (atualizaCarregamento == true) {
			Carregamento carregamento = embarqueDao.findCarregamentoByid(idCarregamento);
			carregamento.setCodigoStatus(new DomainValue("1"));
			embarqueDao.store(carregamento, true);

			HistoricoCarregamento hc = new HistoricoCarregamento();
			hc.setIdCarregamento(carregamento.getIdCarregamento());
			hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
			hc.setStatusCarregamento(carregamento.getCodigoStatus());
			hc.setRotaCarregamento(carregamento.getRotaCarregamento());
			hc.setDataHistorico(new DateTime());
			hc.setMatriculaChefia(carregamento.getMatriculaChefia());

			historicoCarregamentoService.store(hc);
		}
	}

	public String executeVolumeAvaria(boolean exigiuAutorizacao, Long idVolume, Long idCarregamento, String codigoVolume, Short codigoStatus, Long idRejeitoMpc, Long matriculaResponsavel, String autorizador) {

		// Atualiza o volume
		Volume volume = (Volume) this.findById(idVolume);

		if ("1".equals(volume.getCodigoStatus().getValue()) || "2".equals(volume.getCodigoStatus().getValue()) || "3".equals(volume.getCodigoStatus().getValue()) || (exigiuAutorizacao && "6".equals(volume.getCodigoStatus().getValue())) || "7".equals(volume.getCodigoStatus().getValue())) {
			volume.setCodigoStatus(new DomainValue("2"));
		} else {
			volume.setCodigoStatus(new DomainValue("5"));
		}

		volume.setMatriculaResponsavel(matriculaResponsavel);

		volumeDao.store(volume, true);

		// Insere histórico do volume básico
		HistoricoVolume historicoVolume = new HistoricoVolume();
		historicoVolume.setIdCarregamento(idCarregamento);
		historicoVolume.setDataHistorico(new DateTime());
		historicoVolume.setCodigoVolume(codigoVolume);
		historicoVolume.setCodigoStatus(new DomainValue(String.valueOf(codigoStatus)));
		historicoVolume.setIdRejeitoMpc(idRejeitoMpc);
		historicoVolume.setMatriculaResponsavel(matriculaResponsavel);
		historicoVolume.setAutorizador(autorizador);
		historicoVolume.setIdVolume(volume.getIdVolume());

		historicoVolumeDao.storeHistoricoVolume(historicoVolume);

		// Insere histórico do volume marcado como avaria defeito
		historicoVolume = new HistoricoVolume();
		historicoVolume.setIdCarregamento(idCarregamento);
		historicoVolume.setDataHistorico(new DateTime());
		historicoVolume.setCodigoVolume(codigoVolume);
		historicoVolume.setCodigoStatus(volume.getCodigoStatus());
		historicoVolume.setIdRejeitoMpc(idRejeitoMpc);
		historicoVolume.setMatriculaResponsavel(volume.getMatriculaResponsavel());
		historicoVolume.setAutorizador(autorizador);
		historicoVolume.setIdVolume(volume.getIdVolume());

		historicoVolumeDao.storeHistoricoVolume(historicoVolume);

		return configuracoesFacade.getMensagem("LMS-04330");
	}

	public Serializable findById(Long id) {
		return super.findById(id);
	}

	@Override
	protected Serializable store(Volume bean) {
		return super.store(bean);
	}

	public Volume findVolumeByCodigoBarras(String codigoVolume) {
		return volumeDao.findVolumeByCodigoVolume(codigoVolume);
	}



	private void validateMonitoramentoDesgarga(final Volume volume, final Carregamento carregamento, boolean isGMDireto) {
		final VolumeNotaFiscal vnf = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(volume.getCodigoVolume());

		if (vnf != null) {

			final MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.findMonitoramentoDescargaByVolumeNotaFiscal(vnf.getIdVolumeNotaFiscal());

			if (ConstantesExpedicao.TP_SITUACAO_DESCARGA_EMISSAO_CONHECIMENTO_REALIZADA.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {

				// Jira LMS-1114
				Object[] arrayObject = volumeNotaFiscalService.countQtdVolumesByIdMonitoramentoDescargaAndTpVolume(monitoramentoDescarga.getIdMonitoramentoDescarga(), isGMDireto);

				Long countVolumeNotaFiscal = (Long) arrayObject[1];

				/**
				 * se existe volumeNotaFiscal
				 */
				if (countVolumeNotaFiscal != null && countVolumeNotaFiscal > 0) {
					Long qtdVolumes = (Long) (arrayObject[0]);

					// Jira LMS-1114
					volumeNotaFiscalService.updatePsAferidoToNull(monitoramentoDescarga.getIdMonitoramentoDescarga(), isGMDireto);

					monitoramentoDescarga.setFilial(SessionUtils.getFilialSessao());
					monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("G"));
					monitoramentoDescarga.setDhInicioDescarga(new DateTime());
					monitoramentoDescarga.setDhUltimaAfericao(new DateTime());

					// Jira LMS-1114
					if (qtdVolumes != null) {
						monitoramentoDescarga.setQtVolumesTotal(qtdVolumes);
					} else {
						monitoramentoDescarga.setQtVolumesTotal(Long.valueOf(0));
					}
					monitoramentoDescargaService.store(monitoramentoDescarga);
				}
			}
		}
	}

	/**
	 * Método criado para atender a demanda LMS-2788. Este método é utilizado para atualizar as colunas 'CODIGO_STATUS' e 'MATRICULA_RESPONSAVEL' da tabela 'VOLUME'
	 */
	public void updateStatusEMatriculaDeVolumes(Map<String, Object> params, boolean isVolume) {
		getVolumeDao().updateStatusMatriculaDeVolumes(params, isVolume);
	}

	/**
	 * Método criado para atender a demanda LMS-2788. Este método é para armazenar Historico de Volumes
	 * 
	 * @param mpc
	 * @param carregamento
	 * @param usuario
	 */
	public void storeHistoricoVolume(Long mpc, Carregamento carregamento, UsuarioLMS usuario, UsuarioLMS autorizador, Volume volume) {

		List<Volume> listVolumes = new ArrayList<Volume>();
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put("mapaCarregamento", mpc);
		criteria.put("carregamento.idCarregamento", carregamento.getIdCarregamento());

		if (volume != null) {
			criteria.put("idVolume", volume.getIdVolume());
		}

		listVolumes = find(criteria);

		for (Volume volumeTemp : listVolumes) {
			HistoricoVolume historicoVolume = new HistoricoVolume();

			historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
			historicoVolume.setIdVolume(volumeTemp.getIdVolume());
			historicoVolume.setDataHistorico(JTDateTimeUtils.getDataHoraAtual());
			historicoVolume.setCodigoVolume(volumeTemp.getCodigoVolume());
			historicoVolume.setCodigoStatus(new DomainValue("4"));
			historicoVolume.setMatriculaResponsavel(usuario.getUsuarioADSM().getIdUsuario());

			if (autorizador != null) {
				historicoVolume.setAutorizador(autorizador.getUsuarioADSM().getNrMatricula());
			} else {
				historicoVolume.setAutorizador(null);
			}

			historicoVolumeDao.storeHistoricoVolume(historicoVolume);
		}

	}

	public List<Volume> findVolumesByCarregamentoEStatus(Long idCarregamento, String[] status) {
		return volumeDao.findVolumesByCarregamentoEStatus(idCarregamento, status);
	}

	/**
	 * Carrega os dados mostrados na tela de consultar MpC LMS-2790
	 * 
	 * @param criteria
	 * @return Map<String,Object>
	 */
	public Map<String, Object> findDadosCabecalhoConsultarMpC(Map criteria) {
		Object[] retornoQuery = this.totalCarregamentoDAO.findDadosCabecalhoConsultarMpC(criteria);

		if (retornoQuery == null || ArrayUtils.isEmpty(retornoQuery)) {
			throw new BusinessException("LMS-45126");
		}

		Map<String, Object> mapaRetorno = new HashMap<String, Object>();

		mapaRetorno.put("totalVolumes", retornoQuery[0]);
		mapaRetorno.put("totalVolumesConferidos", retornoQuery[1]);
		mapaRetorno.put("faltaVolumes", retornoQuery[2]);

		return mapaRetorno;
	}

	/**
	 * Carrega os dados para a listagem da tela de consultar MpC LMS-2790
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, Object> findConsultaMpc(Map param) {
		ResultSetPage rsp = this.volumeDao.findConsultaMpc(param);
		List<Object[]> listFromResult = rsp.getList();
		Map<String, Object> mapRoot = new HashMap<String, Object>();
		List<Map<String, Object>> listaConsultaMpc = new ArrayList<Map<String, Object>>(listFromResult.size());

		for (final Object[] objResult : listFromResult) {
			TypedFlatMap mapResult = mountMapFromConsultaMpc(objResult);
			listaConsultaMpc.add(mapResult);
		}

		mapRoot.put("listaConsultaMpc", listaConsultaMpc);
		return mapRoot;
	}

	private TypedFlatMap mountMapFromConsultaMpc(Object[] objResult) {
		TypedFlatMap mapResult = new TypedFlatMap();
		mapResult.put("codigoVolume", objResult[0]);

		TypedFlatMap statusVolume = new TypedFlatMap();
		statusVolume.put("nomeStatus", objResult[1]);
		mapResult.put("status", statusVolume);

		TypedFlatMap carregamento = new TypedFlatMap();
		carregamento.put("placaVeiculo", objResult[2]);
		mapResult.put("carregamento", carregamento);

		return mapResult;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setVolumeDAO(VolumeDAO dao) {
		setDao(dao);
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public HistoricoCarregamentoService getHistoricoCarregamentoService() {
		return historicoCarregamentoService;
	}

	public void setHistoricoCarregamentoService(HistoricoCarregamentoService historicoCarregamentoService) {
		this.historicoCarregamentoService = historicoCarregamentoService;
	}

	public VolumeDAO getVolumeDao() {
		return volumeDao;
	}

	public void setVolumeDao(VolumeDAO volumeDao) {
		this.volumeDao = volumeDao;
	}

	public EmbarqueDAO getEmbarqueDao() {
		return embarqueDao;
	}

	public void setEmbarqueDao(EmbarqueDAO embarqueDao) {
		this.embarqueDao = embarqueDao;
	}

	public RotaEmbarqueDAO getRotaEmbarqueDao() {
		return rotaEmbarqueDao;
	}

	public void setRotaEmbarqueDao(RotaEmbarqueDAO rotaEmbarqueDao) {
		this.rotaEmbarqueDao = rotaEmbarqueDao;
	}

	public DetalheCarregamentoDAO getDetalheCarregamentoDao() {
		return detalheCarregamentoDao;
	}

	public void setDetalheCarregamentoDao(DetalheCarregamentoDAO detalheCarregamentoDao) {
		this.detalheCarregamentoDao = detalheCarregamentoDao;
	}

	public DetalheRotaDAO getDetalheRotaDao() {
		return detalheRotaDao;
	}

	public void setDetalheRotaDao(DetalheRotaDAO detalheRotaDao) {
		this.detalheRotaDao = detalheRotaDao;
	}

	public MeioTransporteDAO getMeioTransporteDao() {
		return meioTransporteDao;
	}

	public void setMeioTransporteDao(MeioTransporteDAO meioTransporteDao) {
		this.meioTransporteDao = meioTransporteDao;
	}

	public HistoricoVolumeDAO getHistoricoVolumeDao() {
		return historicoVolumeDao;
	}

	public void setHistoricoVolumeDao(HistoricoVolumeDAO historicoVolumeDao) {
		this.historicoVolumeDao = historicoVolumeDao;
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public CabecalhoCarregamentoDAO getCabecalhoCarregamentoDao() {
		return cabecalhoCarregamentoDao;
	}

	public void setCabecalhoCarregamentoDao(CabecalhoCarregamentoDAO cabecalhoCarregamentoDao) {
		this.cabecalhoCarregamentoDao = cabecalhoCarregamentoDao;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public CarregamentoService getCarregamentoService() {
		return carregamentoService;
	}

	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public void setTotalCarregamentoDAO(TotalCarregamentoDAO totalCarregamentoDAO) {
		this.totalCarregamentoDAO = totalCarregamentoDAO;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
}
