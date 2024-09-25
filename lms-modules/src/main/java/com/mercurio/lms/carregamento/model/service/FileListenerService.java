package com.mercurio.lms.carregamento.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.mercurio.lms.carregamento.model.ArquivoLogDescompactado;
import com.mercurio.lms.carregamento.model.ArquivoLogProcessamento;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.TotalCarregamento;
import com.mercurio.lms.gm.model.dao.CabecalhoCarregamentoDAO;
import com.mercurio.lms.gm.model.service.CarregamentoService;

public class FileListenerService {

	private ArquivoLogProcessamento arquivoLogProcessamento;

	private ArquivoLogProcessamentoService arquivoLogProcessamentoService;
	private CarregamentoService carregamentoService;
	private CabecalhoCarregamentoDAO cabecalhoCarregamentoDao;
	private static Log log = LogFactory.getLog(FileListenerService.class);

	private static final String QUEBRA_DE_PAGINA = "\f";
	private static final String CUBE = "Cube";
	private static final String WEIGHT = "Weight";
	private static final String TOTAL_CONTAINERS = "Total Containers";

	private static final int POS_CBL_DATE_INI = 0;
	private static final int POS_CBL_DATE_FIM = 16;
	private static final int POS_CBL_WAVE_INI = 66;
	private static final int POS_CBL_DOCK_INI = 66;

	private static final int POS_DTL_ROUTE_INI = 0;
	private static final int POS_DTL_ROUTE_FIM = 3;
	private static final int POS_DTL_CARRIER_INI = 9;
	private static final int POS_DTL_CARRIER_FIM = 13;
	private static final int POS_DTL_CUSTOMER_INI = 15;
	private static final int POS_DTL_CUSTOMER_FIM = 22;
	private static final int POS_DTL_CONTAINER_INI = 65;
	private static final int POS_DTL_CONTAINER_FIM = 75;
	private static final int POS_DTL_DIMENSIONS_INI = 76;
	private static final int POS_DTL_DIMENSIONS_FIM = 94;
	private static final int POS_DTL_WEIGHT_INI = 101;
	private static final int POS_DTL_WEIGHT_FIM = 108;
	private static final int POS_DTL_ITEMNUMBER_INI = 110;
	private static final int POS_DTL_ITEMNUMBER_FIM = 120;

	private static final int POS_RDP_TOTALCONTAINERS_INI = 19;
	private static final int POS_RDP_WEIGHT_INI = 19;
	private static final int POS_RDP_CUBE_INI = 19;
	private DirectoryWatcherService.LogProcessamento logProcessamento;

	/**
	 * Connstructor
	 */
	public FileListenerService() {
		super();
	}

	public void execute(InputStream is, ArquivoLogDescompactado arquivoLogDescompactado) {
		arquivoLogProcessamento = new ArquivoLogProcessamento();
		parserWave(is, arquivoLogDescompactado);
		logarMpC5();
	}

	private void parserWave(InputStream is, ArquivoLogDescompactado arquivoLogDescompactado) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String str;
		boolean isTotalizador = false;
		int cont = 0;
		int quebraLinha = 0;

		// inicializa os pojos
		CabecalhoCarregamento cabecalho = new CabecalhoCarregamento();
		List<DetalheCarregamento> detalhesList = new ArrayList<DetalheCarregamento>();
		TotalCarregamento total = new TotalCarregamento();
		DetalheCarregamento detalhe = null;

		arquivoLogProcessamento.setDhInicioProcessamento(new DateTime());
		arquivoLogProcessamento.setArquivoLogDescompactado(arquivoLogDescompactado);
		
		Boolean interromper = Boolean.FALSE;

		try {
			int totRegistrosMerc = 0;
			int totRegistrosNMerc = 0;

			while (in.ready()) {
				cont++;
				str = in.readLine();

				// validar quando o arquivo tem quebra de pagina
				if (str.equalsIgnoreCase(QUEBRA_DE_PAGINA)) {
					str = in.readLine();
					if (str != null && str.trim().startsWith(TOTAL_CONTAINERS)) {
						isTotalizador = true;
						logProcessamento.addLog("Lendo Rodapé do Arquivo");
						getRodapeContainers(str, total);
					} else {
						quebraLinha = 7;
					}
				} else {
					if (quebraLinha > 0) {
						quebraLinha--;
					} else {
						if (cont == 1) {// cabeçalho
							logProcessamento.addLog("Lendo Data Inicio do Arquivo");
							if (!getCabecalhoDataInicio(str, cabecalho).equals("ok")) {
								return;
							}
						} else {
							if (cont == 4) {// cabeçalho
								logProcessamento.addLog("Lendo Wave do Arquivo");
								if (!getCabecalhoWave(str, cabecalho).equals("ok")) {
									return;
								}
							} else {
								if (cont == 5) {// cabeçalho
									logProcessamento.addLog("Lendo Doca do Arquivo");
									if (!getCabecalhoDock(str, cabecalho).equals("ok")) {
										return;
									}
								} else {
									if (!isTotalizador) {
										isTotalizador = str.startsWith(TOTAL_CONTAINERS);
									}
									if (cont > 8 && !isTotalizador) {

										boolean isMerc = isDetalheCarrierMerc(str);

										if (str.charAt(0) != ' ') {

											if (!getDetalheCarrierMerc(str).equals("ok")) {
												return;
											}

											if (isMerc) {

												totRegistrosMerc++;
												detalhe = new DetalheCarregamento();
												if (!getDetalheCustomer(str, detalhe).equals("ok")) {
													return;
												} else if (!getDetalheRoute(str, detalhe).equals("ok")) {
													return;
												} else if (!getDetalheContainer(str, detalhe).equals("ok")) {
													return;
												} else if (!getDetalheItemNumber(str, detalhe).equals("ok")) {
													return;
												} else if (!getDetalheDimension(str, detalhe).equals("ok")) {
													return;
												} else if (!getDetalheDimensionWeight(str, detalhe).equals("ok")) {
													return;
												}
												detalhesList.add(detalhe);
											} else {
												totRegistrosNMerc++;
											}
										}
									} else {
										if (isTotalizador) {
											if (str.trim().startsWith(TOTAL_CONTAINERS)) { // rodape
												if (!getRodapeContainers(str, total).equals("ok")) {
													return;
												}
											} else {
												if (str.trim().startsWith(WEIGHT)) { // rodape
													if (!getRodapeWeight(str, total).equals("ok")) {
														return;
													}
												} else {
													if (str.trim().startsWith(CUBE)) { // rodape
														if (!getRodapeCube(str, total).equals("ok")) {
															return;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			logProcessamento.addLog("Total de Registros MERC lidos: " + totRegistrosMerc);
			logProcessamento.addLog("Total de Registros não MERC lidos: " + totRegistrosNMerc);
		} catch (Exception e) {
			log.error(e);
			tratamentoDeErroLog(e);
			return;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				log.error(e);
				logProcessamento.addLog("Erro ao fechar o arquivo de leitura: " + e.getMessage());
				arquivoLogProcessamento.setDsErroArquivo("Erro ao fechar o arquivo de leitura: " + e.getMessage());
				interromper = true;
			}
		}
		
		if (interromper) {
			return;
		}

		// se não tem nenhum detalhe não grava nenhuma informação;
		if (detalhesList.size() == 0) {
			return;
		}

		CabecalhoCarregamento cabecalhoCarregamento = carregamentoService.findCabecalhoCarregamentoByMapaCarregamento(cabecalho.getMapaCarregamento());

		if (cabecalhoCarregamento == null) {
			carregamentoService.storeCabecalhoCarregamento(cabecalho);
		} else {
			cabecalho.setIdCabecalhoCarregamento(cabecalhoCarregamento.getIdCabecalhoCarregamento());
		}

		for (DetalheCarregamento detalheCarregamento : detalhesList) {
			detalheCarregamento.setIdCabecalhoCarregamento(cabecalho.getIdCabecalhoCarregamento());
			detalheCarregamento.setMapaCarregamento(cabecalho.getMapaCarregamento());
			detalheCarregamento.setCodigoVolume("M" + detalheCarregamento.getCodigoVolume().toString());

			if (detalheCarregamento.getRotaDestino() == null || "3".equals(detalheCarregamento.getRotaDestino())) {
				continue;
			}

			DetalheCarregamento dc = carregamentoService.findDetalheCarregamentoByCodigoVolume(detalheCarregamento.getCodigoVolume());

			if (dc == null) {
				if (cabecalhoCarregamento == null) {
					detalheCarregamento.setIdCabecalhoCarregamento(cabecalho.getIdCabecalhoCarregamento());
					detalheCarregamento.setMapaCarregamento(cabecalho.getMapaCarregamento());
				} else {
					detalheCarregamento.setIdCabecalhoCarregamento(cabecalhoCarregamento.getIdCabecalhoCarregamento());
					detalheCarregamento.setMapaCarregamento(cabecalhoCarregamento.getMapaCarregamento());
				}

				carregamentoService.storeDetalheCarregamento(detalheCarregamento);
			} else {

				CabecalhoCarregamento cc = cabecalhoCarregamentoDao.findCabecalhoByid(dc.getIdCabecalhoCarregamento());

				if (cabecalho.getDataCriacao().compareTo(cc.getDataCriacao()) > 0) {
					detalheCarregamento.setIdCabecalhoCarregamento(cabecalho.getIdCabecalhoCarregamento());
					detalheCarregamento.setMapaCarregamento(cabecalho.getMapaCarregamento());

					carregamentoService.removeDetalheCarregamento(dc.getIdDetalheCarregamento());
					carregamentoService.storeDetalheCarregamento(detalheCarregamento);

				} else if (cabecalho.getDataCriacao().compareTo(cc.getDataCriacao()) == 0) {
					if (cabecalho.getMapaCarregamento() > cc.getMapaCarregamento()) {
						detalheCarregamento.setIdCabecalhoCarregamento(cabecalho.getIdCabecalhoCarregamento());
						detalheCarregamento.setMapaCarregamento(cabecalho.getMapaCarregamento());

						carregamentoService.removeDetalheCarregamento(dc.getIdDetalheCarregamento());
						carregamentoService.storeDetalheCarregamento(detalheCarregamento);
					}
				}
			}
		}

		TotalCarregamento totalCarregamento = carregamentoService.findTotalCarregamentoByMapaCarregamento(cabecalho.getMapaCarregamento());

		if (totalCarregamento == null) {
			if (cabecalhoCarregamento == null) {
				total.setCabecalhoCarregamento(new CabecalhoCarregamento(cabecalho.getIdCabecalhoCarregamento()));
				total.setMapaCarregamento(cabecalho.getMapaCarregamento());
			} else {
				total.setCabecalhoCarregamento(new CabecalhoCarregamento(cabecalhoCarregamento.getIdCabecalhoCarregamento()));
				total.setMapaCarregamento(cabecalhoCarregamento.getMapaCarregamento());
			}
			carregamentoService.storeTotalCarregamento(total);
		} else {
			if (totalCarregamento.getTotalVolume().compareTo(total.getTotalVolume()) != 0) {
				totalCarregamento.setTotalVolume(total.getTotalVolume());
				totalCarregamento.setTotalPeso(total.getTotalPeso());
				totalCarregamento.setTotalCubagem(total.getTotalCubagem());
				carregamentoService.storeTotalCarregamento(totalCarregamento);
			}
		}
		arquivoLogProcessamento.setDhFimProcessamento(new DateTime());
	}

	private String getDetalheDimensionWeight(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue;
			lineValue = null;
			lineValue = str.substring(POS_DTL_WEIGHT_INI, POS_DTL_WEIGHT_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogPeso("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Peso do Volume em branco do arquivo.");

				throw new NullPointerException("Peso do Volume em branco do arquivo.");
			}
			detalhe.setPesoVolume(BigDecimal.valueOf(Double.valueOf(lineValue)));
			arquivoLogProcessamento.setBlLogPeso("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogPeso("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getDetalheItemNumber(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue;
			lineValue = null;
			lineValue = str.substring(POS_DTL_ITEMNUMBER_INI, POS_DTL_ITEMNUMBER_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogItem("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Item do Volume em branco do arquivo.");

				throw new NullPointerException("Item do Volume em branco do arquivo.");
			}
			detalhe.setItemVolume(lineValue);
			arquivoLogProcessamento.setBlLogItem("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogItem("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getDetalheContainer(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue;
			lineValue = null;
			lineValue = str.substring(POS_DTL_CONTAINER_INI, POS_DTL_CONTAINER_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogVolume("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Código de Volume em branco do arquivo.");

				throw new NullPointerException("Código de Volume em branco do arquivo.");
			}
			detalhe.setCodigoVolume(lineValue);
			arquivoLogProcessamento.setBlLogVolume("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogVolume("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			return erro;
		}
	}

	private String getDetalheCarrierMerc(String str) {
		try {
			String lineValue = null;
			lineValue = str.substring(POS_DTL_CARRIER_INI, POS_DTL_CARRIER_FIM);
			lineValue = validaLinha(lineValue);

			if (!lineValue.equalsIgnoreCase("MERC")) {
				arquivoLogProcessamento.setBlLogCarrier("S");
				arquivoLogProcessamento.setBlLogRota("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("MpC descartado conforme regra de negócio, Carrier diferente de MERC.");

				return "erro";
			}
			arquivoLogProcessamento.setBlLogCarrier("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogCarrier("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private Boolean isDetalheCarrierMerc(String str) {
		String lineValue = null;
		lineValue = str.substring(POS_DTL_CARRIER_INI, POS_DTL_CARRIER_FIM);
		lineValue = validaLinha(lineValue);
		return "MERC".equalsIgnoreCase(lineValue);
	}

	private String getDetalheRoute(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue;
			lineValue = null;
			lineValue = str.substring(POS_DTL_ROUTE_INI, POS_DTL_ROUTE_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue == null || lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogRota("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("MpC descartado conforme regra de negócio, Rota sem identificação.");

				throw new NullPointerException("MpC descartado conforme regra de negócio, Rota sem identificação.");
			}
			if (lineValue.equals("3")) {
				arquivoLogProcessamento.setBlLogRota("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("MpC descartado conforme regra de negócio, Rota com identificação 3.");

				return "erro";
			}
			detalhe.setRotaDestino(lineValue);
			arquivoLogProcessamento.setBlLogRota("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogRota("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getDetalheCustomer(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue = str.substring(POS_DTL_CUSTOMER_INI, POS_DTL_CUSTOMER_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogDestino("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Código de Destino em branco do arquivo.");

				throw new NullPointerException("Código de Destino em branco do arquivo.");
			}
			detalhe.setCodigoDestino(lineValue);
			arquivoLogProcessamento.setBlLogDestino("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogDestino("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getDetalheDimension(String str, DetalheCarregamento detalhe) {
		try {
			String lineValue = str.substring(POS_DTL_DIMENSIONS_INI, POS_DTL_DIMENSIONS_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogCubagem("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Cubagem do Volume em branco do arquivo.");

				throw new NullPointerException("Cubagem do Volume em branco do arquivo.");
			}

			Double volume = Double.valueOf(lineValue);
			detalhe.setCubagemVolume(BigDecimal.valueOf(volume));
			arquivoLogProcessamento.setBlLogCubagem("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogCubagem("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getRodapeCube(String str, TotalCarregamento total) {
		try {
			String lineValue = str.substring(POS_RDP_CUBE_INI);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogTotalCubagem("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Total da Cubagem em branco do arquivo.");

				throw new NullPointerException("Total da Cubagem em branco do arquivo.");
			}
			total.setTotalCubagem(BigDecimal.valueOf(Double.valueOf(lineValue)));
			arquivoLogProcessamento.setBlLogTotalCubagem("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogTotalCubagem("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getRodapeWeight(String str, TotalCarregamento total) {
		try {
			String lineValue = str.substring(POS_RDP_WEIGHT_INI);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogTotalPeso("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Total do Peso em branco do arquivo.");

				throw new NullPointerException("Total do Peso em branco do arquivo.");
			}
			total.setTotalPeso(BigDecimal.valueOf(Double.valueOf(lineValue)));
			arquivoLogProcessamento.setBlLogTotalPeso("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogTotalPeso("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getRodapeContainers(String str, TotalCarregamento total) {
		try {
			String lineValue = str.substring(POS_RDP_TOTALCONTAINERS_INI);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogTotalVolume("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Total de Volume em branco do arquivo.");

				throw new NullPointerException("Total de Volume em branco do arquivo.");
			}
			total.setTotalVolume(Long.valueOf(lineValue));
			arquivoLogProcessamento.setBlLogTotalVolume("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogTotalVolume("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getCabecalhoDock(String str, CabecalhoCarregamento cabecalho) {
		try {
			String lineValue = str.substring(POS_CBL_DOCK_INI);
			lineValue = validaLinha(lineValue);
			lineValue = lineValue.replaceAll("[^0-9]*", "");

			if (lineValue.equals("")) {
				cabecalho.setDocaCarregamento(0L);
				arquivoLogProcessamento.setBlLogDoca("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Doca de Carregamento em branco do arquivo.");

				throw new NullPointerException("Doca de Carregamento em branco do arquivo.");
			}
			cabecalho.setDocaCarregamento(Long.valueOf(lineValue));
			arquivoLogProcessamento.setBlLogDoca("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogDoca("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getCabecalhoWave(String str, CabecalhoCarregamento cabecalho) {
		try {
			String lineValue = str.substring(POS_CBL_WAVE_INI);
			lineValue = validaLinha(lineValue);
			logProcessamento.addLog("Mapa de Carregamento:" + lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogWave("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Número de Mapa de Carregamento em branco.");
				arquivoLogProcessamento.setNrMpc(0L);

				throw new NullPointerException("Número de Mapa de Carregamento em branco.");
			}
			cabecalho.setMapaCarregamento(Long.valueOf(lineValue));
			arquivoLogProcessamento.setNrMpc(Long.parseLong(lineValue));
			arquivoLogProcessamento.setBlLogWave("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogWave("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setNrMpc(0L);
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	private String getCabecalhoDataInicio(String str, CabecalhoCarregamento cabecalho) {
		try {
			String lineValue = str.substring(POS_CBL_DATE_INI, POS_CBL_DATE_FIM);
			lineValue = validaLinha(lineValue);

			if (lineValue.equals("")) {
				arquivoLogProcessamento.setBlLogData("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Data de criação do arquivo em branco.");

				throw new NullPointerException("Data de criação do arquivo em branco.");
			}
			int dia = Integer.parseInt((String) lineValue.substring(0, 2));
			int mes = Integer.parseInt((String) lineValue.substring(3, 5));
			int ano = Integer.parseInt((String) lineValue.substring(6, 10));
			int hra = Integer.parseInt((String) lineValue.substring(11, 13));
			int min = Integer.parseInt((String) lineValue.substring(14));

			String data = (String) lineValue.substring(0, 16);

			if (!validaData(data)) {
				arquivoLogProcessamento.setBlLogData("S");
				arquivoLogProcessamento.setBlLogSucesso("N");
				arquivoLogProcessamento.setDsErroArquivo("Data de criação do arquivo fora do padrão, data arquivo = " + str);
				return "erro";
			}
			cabecalho.setDataCriacao(new DateTime(ano, mes, dia, hra, min, 0, 0));
			// data inicio leitura arquivo
			cabecalho.setDataDisponivel(new DateTime(Calendar.getInstance().getTimeInMillis()));

			arquivoLogProcessamento.setBlLogData("N");
			arquivoLogProcessamento.setDsErroArquivo("");

			return "ok";
		} catch (Exception e) {
			String erro = String.valueOf(e);
			arquivoLogProcessamento.setBlLogData("S");
			arquivoLogProcessamento.setBlLogSucesso("N");
			arquivoLogProcessamento.setDsErroArquivo(erro);
			tratamentoDeErroLog(e);
			return erro;
		}
	}

	/**
	 * Metodo auxiliar para fazer o tratamento de exceções e gravar no arquivo
	 * de log que é gerado. Sem este metodo ao encontrar um erro o processo é
	 * parado e não é gerado o log no arquivo, o que o deixa imcompleto
	 * 
	 * @param e
	 * @return
	 */
	private String tratamentoDeErroLog(Exception e) {
		logProcessamento.addLog("Erro ao processar o arquivo: " + e.getMessage());
		arquivoLogProcessamento.setDsErroArquivo("Erro ao processar o arquivo: " + e.getMessage());
		return "erro";
	}

	private String validaLinha(String lineValue) {
		if (lineValue == null) {
			logProcessamento.addLog("Sem dados na linha para gravar");
			return "";
		}
		return lineValue.trim();
	}

	private boolean validaData(String str) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date date = sdf.parse(str);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/*
	 * Metódo responsavel por salvar o log no banco, independente se se houve
	 * algum problema ou não. Cada parte do log é preenchida no momento em que a
	 * linha do arquivo é lida, se houver algum problema todo o processo é
	 * abortado e salvo.
	 * 
	 * Demanda LMS-1236
	 */
	private void logarMpC5() {
		ArquivoLogProcessamento arquivoLogProcessamentoToStore = new ArquivoLogProcessamento();
		arquivoLogProcessamentoToStore.setIdArquivoLogProcessamento(arquivoLogProcessamento.getIdArquivoLogProcessamento());
		arquivoLogProcessamentoToStore.setArquivoLogDescompactado(arquivoLogProcessamento.getArquivoLogDescompactado());
		arquivoLogProcessamentoToStore.setDhInicioProcessamento(arquivoLogProcessamento.getDhInicioProcessamento());
		arquivoLogProcessamentoToStore.setDhFimProcessamento(arquivoLogProcessamento.getDhFimProcessamento() == null ? new DateTime() : arquivoLogProcessamento.getDhFimProcessamento());
		arquivoLogProcessamentoToStore.setBlLogSucesso(arquivoLogProcessamento.getBlLogSucesso() == null ? "S" : arquivoLogProcessamento.getBlLogSucesso());
		arquivoLogProcessamentoToStore.setBlLogCarrier(arquivoLogProcessamento.getBlLogCarrier() == null ? "N" : arquivoLogProcessamento.getBlLogCarrier());
		arquivoLogProcessamentoToStore.setBlLogData(arquivoLogProcessamento.getBlLogData() == null ? "N" : arquivoLogProcessamento.getBlLogData());
		arquivoLogProcessamentoToStore.setBlLogWave(arquivoLogProcessamento.getBlLogWave() == null ? "N" : arquivoLogProcessamento.getBlLogWave());
		arquivoLogProcessamentoToStore.setBlLogDoca(arquivoLogProcessamento.getBlLogDoca() == null ? "N" : arquivoLogProcessamento.getBlLogDoca());
		arquivoLogProcessamentoToStore.setBlLogDestino(arquivoLogProcessamento.getBlLogDestino() == null ? "N" : arquivoLogProcessamento.getBlLogDestino());
		arquivoLogProcessamentoToStore.setBlLogVolume(arquivoLogProcessamento.getBlLogVolume() == null ? "N" : arquivoLogProcessamento.getBlLogVolume());
		arquivoLogProcessamentoToStore.setBlLogRota(arquivoLogProcessamento.getBlLogRota() == null ? "N" : arquivoLogProcessamento.getBlLogRota());
		arquivoLogProcessamentoToStore.setBlLogItem(arquivoLogProcessamento.getBlLogItem() == null ? "N" : arquivoLogProcessamento.getBlLogItem());
		arquivoLogProcessamentoToStore.setBlLogCubagem(arquivoLogProcessamento.getBlLogCubagem() == null ? "N" : arquivoLogProcessamento.getBlLogCubagem());
		arquivoLogProcessamentoToStore.setBlLogPeso(arquivoLogProcessamento.getBlLogPeso() == null ? "N" : arquivoLogProcessamento.getBlLogPeso());
		arquivoLogProcessamentoToStore.setBlLogTotalVolume(arquivoLogProcessamento.getBlLogTotalVolume() == null ? "N" : arquivoLogProcessamento.getBlLogTotalVolume());
		arquivoLogProcessamentoToStore.setBlLogTotalPeso(arquivoLogProcessamento.getBlLogTotalPeso() == null ? "N" : arquivoLogProcessamento.getBlLogTotalPeso());
		arquivoLogProcessamentoToStore.setBlLogTotalCubagem(arquivoLogProcessamento.getBlLogTotalCubagem() == null ? "N" : arquivoLogProcessamento.getBlLogTotalCubagem());
		arquivoLogProcessamentoToStore.setNrMpc(arquivoLogProcessamento.getNrMpc() == null ? 0L : arquivoLogProcessamento.getNrMpc());
		arquivoLogProcessamentoToStore.setDsErroArquivo(arquivoLogProcessamento.getDsErroArquivo());

		arquivoLogProcessamentoService.store(arquivoLogProcessamentoToStore);
	}

	// GETTER E SETTERS
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public CabecalhoCarregamentoDAO getCabecalhoCarregamentoDao() {
		return cabecalhoCarregamentoDao;
	}

	public void setCabecalhoCarregamentoDao(CabecalhoCarregamentoDAO cabecalhoCarregamentoDao) {
		this.cabecalhoCarregamentoDao = cabecalhoCarregamentoDao;
	}

	public CarregamentoService getCarregamentoService() {
		return carregamentoService;
	}

	public void setLogProcessamento(DirectoryWatcherService.LogProcessamento logProcessamento) {
		this.logProcessamento = logProcessamento;
	}

	public void setArquivoLogProcessamentoService(ArquivoLogProcessamentoService arquivoLogProcessamentoService) {
		this.arquivoLogProcessamentoService = arquivoLogProcessamentoService;
	}
}