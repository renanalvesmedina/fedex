package com.mercurio.lms.gm.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoVolume;
import com.mercurio.lms.carregamento.model.RotaEmbarque;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTransporteDAO;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.gm.model.dao.DetalheCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.EmbarqueDAO;
import com.mercurio.lms.gm.model.dao.HistoricoVolumeDAO;
import com.mercurio.lms.gm.model.dao.RotaEmbarqueDAO;
import com.mercurio.lms.gm.model.dao.VolumeDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.gm.embarqueService"
 */
public class EmbarqueService extends CrudService<Carregamento, Long> {
	private MeioTransporteService meioTransporteService;
	private EmbarqueDAO embarqueDao;
	private RotaEmbarqueDAO rotaEmbarqueDao;
	private MeioTransporteDAO meioTransporteDao;
	private VolumeDAO volumeDao;
	private HistoricoVolumeDAO historicoVolumeDao;
	private DetalheCarregamentoDAO detalheCarregamentoDao;
	private HistoricoCarregamentoService historicoCarregamentoService;

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
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

	public MeioTransporteDAO getMeioTransporteDao() {
		return meioTransporteDao;
	}

	public void setMeioTransporteDao(MeioTransporteDAO meioTransporteDao) {
		this.meioTransporteDao = meioTransporteDao;
	}

	public VolumeDAO getVolumeDao() {
		return volumeDao;
	}

	public void setVolumeDao(VolumeDAO volumeDao) {
		this.volumeDao = volumeDao;
	}

	public HistoricoVolumeDAO getHistoricoVolumeDao() {
		return historicoVolumeDao;
	}

	public void setHistoricoVolumeDao(HistoricoVolumeDAO historicoVolumeDao) {
		this.historicoVolumeDao = historicoVolumeDao;
	}

	public DetalheCarregamentoDAO getDetalheCarregamentoDao() {
		return detalheCarregamentoDao;
	}

	public void setDetalheCarregamentoDao(DetalheCarregamentoDAO detalheCarregamentoDao) {
		this.detalheCarregamentoDao = detalheCarregamentoDao;
	}

	/**
	 * Recebe um codigo de barras de um veiculo para validação.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map findMeioTransporteByBarcode(Long barcode) {
		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByCodigoBarras(barcode);

		if (meioTransporte == null) {
			throw new BusinessException("LMS-04313");
		}
		return findMapMeioTransporteValido(meioTransporte);
	}

	public Map findMeioTransporteByFrota(String nrFrota) {
		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByNrFrota(nrFrota);

		if (meioTransporte == null) {
			throw new BusinessException("LMS-04313");
		}
		return findMapMeioTransporteValido(meioTransporte);
	}
	
	/**
	 * Recebe um MeioTransporte para as demais validaçoes dos demais dados da
	 * tela.
	 * 
	 * @author Samuel Alves
	 * @param MeioTransporte
	 *            meioTransporte
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map findMapMeioTransporteValido(MeioTransporte meioTransporte) {

		List<Carregamento> listCarregamento = embarqueDao.findCarregamentoByFrotaVeiculo(meioTransporte.getNrFrota());

		for (Carregamento carregamento : listCarregamento) {
			if (carregamento.getFrotaVeiculo() != null) {
				if (carregamento.getCodigoStatus().getValue().equals("1") || carregamento.getCodigoStatus().getValue().equals("2") || carregamento.getCodigoStatus().getValue().equals("6")) {
					Map map = new HashMap();
					map.put("idCarregamento", carregamento.getIdCarregamento());
					map.put("codigoStatus", carregamento.getCodigoStatus().getValue());
					map.put("docaCarregamento", carregamento.getDocaCarregamento());
					map.put("tipoCarregamento", carregamento.getTipoCarregamento());
					map.put("frotaVeiculo", carregamento.getFrotaVeiculo());
					map.put("placaVeiculo", carregamento.getPlacaVeiculo());
					map.put("rotaCarregamento", carregamento.getRotaCarregamento());
					map.put("cnpjRemeClnt", carregamento.getCnpjRemetenteCliente());
					return map;
				}
			}
		}

		Map map = new HashMap();
		map.put("frotaVeiculo", meioTransporte.getNrFrota());
		map.put("placaVeiculo", meioTransporte.getNrIdentificador());
		return map;
	}

	/**
	 * Recebe uma Doca para validação.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            doca
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map findDocaDisponivel(String doca) {

		List<Carregamento> listCarregamento = embarqueDao.findCarregamentoByDoca(doca);

		if (listCarregamento != null && listCarregamento.size() > 0) {
			for (Carregamento carregamento : listCarregamento) {
				if (carregamento.getCodigoStatus().getValue().equals("1") || carregamento.getCodigoStatus().getValue().equals("2") || carregamento.getCodigoStatus().getValue().equals("6")) {
					throw new BusinessException("LMS-04315");
				}
			}
		}

		Map map = new HashMap();
		map.put("docaCarregamento", doca);
		return map;
	}

	/**
	 * Recebe uma Rota para validação.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            rota
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map findRotaBySigla(String frotaVeiculo, String doca, String rotaEmbarque, String tipoCarregamento) {
		RotaEmbarque rota = rotaEmbarqueDao.findRotaBySigla(rotaEmbarque);

		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByNrFrota(frotaVeiculo);

		int count = 0;

		if (rota != null) {
			List<Carregamento> listCarregamento = embarqueDao.findCarregamentoByRota(rota.getSiglaRota());
			if (listCarregamento != null && listCarregamento.size() > 0) {
				for (Carregamento carregamento : listCarregamento) {
					if ((carregamento.getCodigoStatus().equals(new DomainValue("1")) && carregamento.getTipoCarregamento().equalsIgnoreCase(tipoCarregamento)) || (carregamento.getCodigoStatus().equals(new DomainValue("2")) && carregamento.getTipoCarregamento().equalsIgnoreCase(tipoCarregamento)) || (carregamento
							.getCodigoStatus().equals(new DomainValue("6")) && carregamento.getTipoCarregamento().equalsIgnoreCase(tipoCarregamento)))
						throw new BusinessException("LMS-04317");
				}
			}
		} else {
			throw new BusinessException("LMS-04316");
		}

		if (rota.getHorarioCorte().compareTo(new TimeOfDay()) < 1) {
			throw new BusinessException("LMS-04318");
		}

		Map map = new HashMap();
		map.put("rotaCarregamento", rota.getSiglaRota());
		return map;
	}

	/**
	 * Recebe um Carregamento para inclusão na base de dados.
	 * 
	 * @author Samuel Alves
	 * @param Carregamento
	 *            carregamento
	 */
	public void storeCarregamento(Carregamento carregamento) {
		Carregamento c = carregamento;
		embarqueDao.store(carregamento);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(Carregamento bean) {
		return super.store(bean);
	}

	/**
	 * Recebe o id do carregamento para realizar a conclusao do embarque.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            idCarregamento
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map executeConcluirEmbarque(Carregamento c) {

		Integer totalVolumes = 0;
		boolean divergencias = false;
		BigDecimal totalPeso = new BigDecimal(0);
		BigDecimal totalCubagem = new BigDecimal(0);
		String matriculaChefia = null;
		Carregamento carregamento = embarqueDao.findCarregamentoByid(c.getIdCarregamento());

		List<Volume> listVolumes = volumeDao.findVolumeByIdCarregamento(c.getIdCarregamento());
		ArrayList<DetalheCarregamento> listDetalheCarregamento = new ArrayList();
		ArrayList<Long> listMapas = new ArrayList();

		if (listVolumes != null && listVolumes.size() > 0) {
			for (Volume volume : listVolumes) {
				if (volume.getCodigoStatus().equals(new DomainValue("1")) || volume.getCodigoStatus().equals(new DomainValue("2")) || volume.getCodigoStatus().equals(new DomainValue("3"))) {
					if (listMapas.size() > 0) {
						if (!listMapas.contains(volume.getMapaCarregamento())) {
							listMapas.add(volume.getMapaCarregamento());
						}
					} else {
						listMapas.add(volume.getMapaCarregamento());
					}
					totalVolumes++;
					totalPeso = totalPeso.add(volume.getPesoVolume());
					totalCubagem = totalCubagem.add(volume.getCubagemVolume());
				} else if(volume.getCodigoStatus().equals(new DomainValue("7"))){
					throw new BusinessException("LMS-04341");
				} else {
					divergencias = true;
				}

			}

			for (Long idMapa : listMapas) {
				listDetalheCarregamento = (ArrayList<DetalheCarregamento>) detalheCarregamentoDao.findDetalheCarregamentoByMapaCarregamento(idMapa);

				for (DetalheCarregamento detalheCarregamento : listDetalheCarregamento) {
					Volume volume = volumeDao.findVolumeByCodigoVolume(detalheCarregamento.getCodigoVolume());
					if (volume == null) {
						divergencias = true;
						break;
					} else if (!(volume.getCodigoStatus().equals(new DomainValue("1")) || volume.getCodigoStatus().equals(new DomainValue("2")) || volume.getCodigoStatus().equals(new DomainValue("3")) || volume.getCodigoStatus().equals(new DomainValue("6")))) {
						divergencias = true;
						break;
					}
				}

				if (divergencias) {
					break;
				}
			}
		}

		if (totalVolumes == 0) {
			cancelaCarregamento(carregamento);
			return null;
		}

		if (divergencias) {
			throw new BusinessException("LMS-04319");
		} else {
			carregamento.setDtFim(new DateTime());
			carregamento.setCodigoStatus(new DomainValue("6"));
			carregamento.setTotalVolumes(totalVolumes.longValue());
			carregamento.setTotalPeso(totalPeso);
			carregamento.setTotalCubagem(totalCubagem);
			carregamento.setMatriculaChefia(c.getMatriculaChefia());
			embarqueDao.store(carregamento, true);

			HistoricoCarregamento hc = new HistoricoCarregamento();
			hc.setIdCarregamento(carregamento.getIdCarregamento());
			hc.setStatusCarregamento(carregamento.getCodigoStatus());
			hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
			hc.setRotaCarregamento(carregamento.getRotaCarregamento());
			hc.setDataHistorico(new DateTime());
			hc.setMatriculaChefia(carregamento.getMatriculaChefia());

			historicoCarregamentoService.store(hc);
		}

		Map map = new HashMap();

		map.put("veiculo", carregamento.getFrotaVeiculo());
		map.put("doca", carregamento.getDocaCarregamento());
		return map;
	}

	/**
	 * Recebe o carregamento para realizar o fechamento do embarque com
	 * pendencias.
	 * 
	 * @author Samuel Alves
	 * @param Carregamento
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public void executeConcluirEmbarqueComPendencia(Carregamento c) {
		Integer totalVolumes = 0;
		BigDecimal totalPeso = new BigDecimal(0);
		BigDecimal totalCubagem = new BigDecimal(0);
		Long matriculaChefia = null;

		matriculaChefia = c.getMatriculaChefia();

		Carregamento carregamento = embarqueDao.findCarregamentoByid(c.getIdCarregamento());

		List<Volume> listVolumes = volumeDao.findVolumeByIdCarregamento(c.getIdCarregamento());

		carregamento.setDtFim(new DateTime());
		carregamento.setCodigoStatus(new DomainValue("6"));
		carregamento.setTotalVolumes(totalVolumes.longValue());
		carregamento.setTotalPeso(totalPeso);
		carregamento.setTotalCubagem(totalCubagem);
		carregamento.setMatriculaChefia(matriculaChefia);

		embarqueDao.store(carregamento, true);

		HistoricoCarregamento hc = new HistoricoCarregamento();
		hc.setIdCarregamento(carregamento.getIdCarregamento());
		hc.setStatusCarregamento(carregamento.getCodigoStatus());
		hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
		hc.setRotaCarregamento(carregamento.getRotaCarregamento());
		hc.setDataHistorico(new DateTime());
		hc.setMatriculaChefia(carregamento.getMatriculaChefia());

		historicoCarregamentoService.store(hc);

	}

	/**
	 * Recebe o id do carregamento para realizar o fechamento do embarque.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            idCarregamento
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public Map executeFechamentoEmbarque(Carregamento c) {
		Integer totalVolumes = 0;
		boolean divergencias = false;
		BigDecimal totalPeso = new BigDecimal(0);
		BigDecimal totalCubagem = new BigDecimal(0);

		Carregamento carregamento = embarqueDao.findCarregamentoByid(c.getIdCarregamento());

		List<Volume> listVolumes = volumeDao.findVolumeByIdCarregamento(c.getIdCarregamento());
		ArrayList<DetalheCarregamento> listDetalheCarregamento = new ArrayList();
		ArrayList<DetalheCarregamento> listDetalheCarregamentoMapas = new ArrayList();
		ArrayList<Long> listMapas = new ArrayList();

		if (listVolumes != null && listVolumes.size() > 0) {
			for (Volume volume : listVolumes) {
				if (volume.getCodigoStatus().equals(new DomainValue("1")) || volume.getCodigoStatus().equals(new DomainValue("2")) || volume.getCodigoStatus().equals(new DomainValue("3"))) {
					if (listMapas.size() > 0) {
						if (!listMapas.contains(volume.getMapaCarregamento())) {
							listMapas.add(volume.getMapaCarregamento());
						}
					} else {
						listMapas.add(volume.getMapaCarregamento());
					}
					totalVolumes++;
					totalPeso = totalPeso.add(volume.getPesoVolume());
					totalCubagem = totalCubagem.add(volume.getCubagemVolume());

				}// else Divergencia = true
			}

			for (Long idMapa : listMapas) {
				listDetalheCarregamento = (ArrayList<DetalheCarregamento>) detalheCarregamentoDao.findDetalheCarregamentoByMapaCarregamento(idMapa);
				listDetalheCarregamentoMapas.addAll(listDetalheCarregamento);

				for (DetalheCarregamento detalheCarregamento : listDetalheCarregamento) {
					Volume volume = volumeDao.findVolumeByCodigoVolume(detalheCarregamento.getCodigoVolume());
					if (volume == null) {
						divergencias = true;
						break;
					} else if (!(volume.getCodigoStatus().equals(new DomainValue("1")) || volume.getCodigoStatus().equals(new DomainValue("2")) || volume.getCodigoStatus().equals(new DomainValue("3")) || volume.getCodigoStatus().equals(new DomainValue("6")))) {
						divergencias = true;
						break;
					}
				}

				if (divergencias) {
					break;
				}
			}
		}

		if (totalVolumes == 0) {
			cancelaCarregamento(carregamento);
			return null;
		}

		if (divergencias) {
			throw new BusinessException("LMS-04319");
		} else {
			carregamento.setDtFim(new DateTime());
			carregamento.setCodigoStatus(new DomainValue("3"));
			carregamento.setTotalVolumes(totalVolumes.longValue());
			carregamento.setTotalPeso(totalPeso);
			carregamento.setTotalCubagem(totalCubagem);
			embarqueDao.store(carregamento, true);

			HistoricoCarregamento hc = new HistoricoCarregamento();
			hc.setIdCarregamento(carregamento.getIdCarregamento());
			hc.setStatusCarregamento(carregamento.getCodigoStatus());
			hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
			hc.setRotaCarregamento(carregamento.getRotaCarregamento());
			hc.setDataHistorico(new DateTime());
			hc.setMatriculaChefia(carregamento.getMatriculaChefia());

			historicoCarregamentoService.store(hc);
		}

		for (Volume volume : listVolumes) {
			for (DetalheCarregamento detalheCarregamento : listDetalheCarregamentoMapas) {
				if (volume.getCodigoVolume().equals(detalheCarregamento.getCodigoVolume())) {
					volume.setCodigoStatus(new DomainValue("6"));
					volumeDao.store(volume, true);

					HistoricoVolume historicoVolume = new HistoricoVolume();
					historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
					historicoVolume.setDataHistorico(new DateTime());
					historicoVolume.setCodigoVolume(volume.getCodigoVolume());
					historicoVolume.setCodigoStatus(new DomainValue("6"));
					historicoVolume.setMatriculaResponsavel(volume.getMatriculaResponsavel());
					historicoVolume.setIdVolume(volume.getIdVolume());

					historicoVolumeDao.storeHistoricoVolume(historicoVolume);

				}
			}
		}

		Map map = new HashMap();

		map.put("veiculo", carregamento.getFrotaVeiculo());
		map.put("doca", carregamento.getDocaCarregamento());
		return map;
	}

	public void cancelaCarregamento(Carregamento carregamento) {
		carregamento.setCodigoStatus(new DomainValue("5"));
		carregamento.setDtFim(new DateTime());

		embarqueDao.store(carregamento, true);

		HistoricoCarregamento hc = new HistoricoCarregamento();
		hc.setIdCarregamento(carregamento.getIdCarregamento());
		hc.setStatusCarregamento(carregamento.getCodigoStatus());
		hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
		hc.setRotaCarregamento(carregamento.getRotaCarregamento());
		hc.setDataHistorico(new DateTime());
		hc.setMatriculaChefia(carregamento.getMatriculaChefia());

		historicoCarregamentoService.store(hc);
	}

	/**
	 * Recebe o carregamento para realizar o fechamento do embarque com
	 * pendencias.
	 * 
	 * @author Samuel Alves
	 * @param Carregamento
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos
	 *         Unitizados.
	 */
	public void executeFechamentoEmbarqueComPendencia(Carregamento c) {
		Integer totalVolumes = 0;
		BigDecimal totalPeso = new BigDecimal(0);
		BigDecimal totalCubagem = new BigDecimal(0);

		Carregamento carregamento = embarqueDao.findCarregamentoByid(c.getIdCarregamento());

		List<Volume> listVolumes = volumeDao.findVolumeByIdCarregamento(c.getIdCarregamento());

		for (Volume volume : listVolumes) {
			if (volume.getCodigoStatus().equals(new DomainValue("1")) || volume.getCodigoStatus().equals(new DomainValue("2")) || volume.getCodigoStatus().equals(new DomainValue("3"))) {
				totalVolumes++;
				totalPeso = totalPeso.add(volume.getPesoVolume());
				totalCubagem = totalCubagem.add(volume.getCubagemVolume());

				volume.setCodigoStatus(new DomainValue("6"));
				volumeDao.store(volume, true);

				HistoricoVolume historicoVolume = new HistoricoVolume();
				historicoVolume.setIdCarregamento(carregamento.getIdCarregamento());
				historicoVolume.setDataHistorico(new DateTime());
				historicoVolume.setCodigoVolume(volume.getCodigoVolume());
				historicoVolume.setCodigoStatus(new DomainValue("6"));
				historicoVolume.setMatriculaResponsavel(volume.getMatriculaResponsavel());
				historicoVolume.setIdVolume(volume.getIdVolume());

				historicoVolumeDao.storeHistoricoVolume(historicoVolume);
			}
		}

		carregamento.setDtFim(new DateTime());
		carregamento.setCodigoStatus(new DomainValue("4"));
		carregamento.setTotalVolumes(totalVolumes.longValue());
		carregamento.setTotalPeso(totalPeso);
		carregamento.setTotalCubagem(totalCubagem);

		embarqueDao.store(carregamento, true);

		HistoricoCarregamento hc = new HistoricoCarregamento();
		hc.setIdCarregamento(carregamento.getIdCarregamento());
		hc.setStatusCarregamento(carregamento.getCodigoStatus());
		hc.setCnpjRemetenteCliente(carregamento.getCnpjRemetenteCliente());
		hc.setRotaCarregamento(carregamento.getRotaCarregamento());
		hc.setDataHistorico(new DateTime());
		hc.setMatriculaChefia(carregamento.getMatriculaChefia());

		historicoCarregamentoService.store(hc);

	}

	/**
	 * Busca Carregamento do tipo GM Direto
	 * 
	 * @author André Valadas
	 * 
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public Carregamento findCarregamentoGMDireto(final Long idMonitoramentoDescarga) {
		return getEmbarqueDao().findCarregamentoGMDireto(idMonitoramentoDescarga);
	}

	/**
	 * Busca Carregamento do tipo GM Normal
	 * 
	 * @param idMonitoramentoDescarga
	 *            <code>Long</code>
	 * @return <code>Carregamento</code>
	 * @author Sidarta Silva
	 */
	public Carregamento findCarregamentoGMNormal(Long idMonitoramentoDescarga) {
		return getEmbarqueDao().findCarregamentoGMNormal(idMonitoramentoDescarga);
	}

	public Serializable findById(Long id) {
		return super.findById(id);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setEmbarqueDAO(EmbarqueDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private EmbarqueDAO getEmbarqueDAO() {
		return (EmbarqueDAO) getDao();
	}

	public HistoricoCarregamentoService getHistoricoCarregamentoService() {
		return historicoCarregamentoService;
	}

	public void setHistoricoCarregamentoService(HistoricoCarregamentoService historicoCarregamentoService) {
		this.historicoCarregamentoService = historicoCarregamentoService;
	}
}
