package com.mercurio.lms.layoutedi.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIItem;
import com.mercurio.lms.edi.model.LogEDIVolume;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.edi.model.service.LogEDIComplementoService;
import com.mercurio.lms.edi.model.service.LogEDIDetalheService;
import com.mercurio.lms.edi.model.service.LogEDIItemService;
import com.mercurio.lms.edi.model.service.LogEDIService;
import com.mercurio.lms.edi.model.service.LogEDIVolumeService;
import com.mercurio.lms.layoutedi.model.Complemento;
import com.mercurio.lms.layoutedi.model.Consignatario;
import com.mercurio.lms.layoutedi.model.Destinatario;
import com.mercurio.lms.layoutedi.model.Detalhe;
import com.mercurio.lms.layoutedi.model.Item;
import com.mercurio.lms.layoutedi.model.LogDetalhe;
import com.mercurio.lms.layoutedi.model.NotaFiscal;
import com.mercurio.lms.layoutedi.model.Redespacho;
import com.mercurio.lms.layoutedi.model.Registro;
import com.mercurio.lms.layoutedi.model.Remetente;
import com.mercurio.lms.layoutedi.model.Tomador;
import com.mercurio.lms.layoutedi.model.Volume;


/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ManterLogEdiAction"
 */
@ServiceSecurity
public class ManterLogEdiAction {
	private Logger log = LogManager.getLogger(this.getClass());
	private  LogEDIService logEDIService;
	private  LogEDIComplementoService logEDIComplementoService;
	private  LogEDIItemService logEDIItemService;
	private  LogEDIVolumeService logEDIVolumeService;
	private  LogEDIDetalheService logEDIDetalheService;
	private ClienteLayoutEDIService clienteLayoutEDIService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	
	@MethodSecurity(processGroup = "layoutedi.manterLogsArquivos", processName = "gravaArquivoLog", authenticationRequired=false)
	public String gravaArquivoLog(Object object) throws Exception{
		String retorno = "";
		try{
			String[] dadosArquivo = (String[])object;
			LogEDI logArquivoEDI = new LogEDI();

			if(dadosArquivo[0] != null && !dadosArquivo[0].equals("")){//update
				logArquivoEDI.setIdLogEdi(Long.valueOf(dadosArquivo[0]));
				logArquivoEDI = logEDIService.findById(Long.valueOf(dadosArquivo[0]));
				
				if(dadosArquivo[5] != null){
					if(logArquivoEDI.getStatusTmp() != null){
						if(dadosArquivo[5].equalsIgnoreCase("erro")){
							logArquivoEDI.setStatusTmp("Erro");
						}else if(dadosArquivo[5].equalsIgnoreCase("concluído com erros") && !logArquivoEDI.getStatusTmp().equalsIgnoreCase("erro")){
							logArquivoEDI.setStatusTmp("Concluído com Erros");
						}else if(dadosArquivo[5].equalsIgnoreCase("concluído sem erros") && !logArquivoEDI.getStatusTmp().equalsIgnoreCase("erro") && !logArquivoEDI.getStatusTmp().equalsIgnoreCase("concluído com erros")){
							logArquivoEDI.setStatusTmp("Concluído sem Erros");
						}
					}else{
						logArquivoEDI.setStatusTmp(dadosArquivo[5]);
					}
				}
				
				Integer totalPartes = Integer.valueOf(dadosArquivo[1].split("_")[5].split("\\.")[0]);
				Integer totalPartesLog = logArquivoEDI.getQtdePartes();
				totalPartesLog++;
				if(totalPartesLog < totalPartes){
					logArquivoEDI.setQtdePartes(totalPartesLog);
				}else{
					logArquivoEDI.setQtdePartes(totalPartesLog);
					logArquivoEDI.setStatus(logArquivoEDI.getStatusTmp());
				}
			}else{//insert
				if(dadosArquivo[1] != null && !dadosArquivo[1].equals("")){
					String nomeArqPart = dadosArquivo[1].substring(0,dadosArquivo[1].lastIndexOf("_"));
					nomeArqPart = nomeArqPart.substring(0,nomeArqPart.lastIndexOf("_"))+".txt";
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("nome", nomeArqPart);
					List arquivosEncontrados = logEDIService.find(params);
					if(arquivosEncontrados != null && arquivosEncontrados.size() > 0){
						LogEDI logEDI = (LogEDI) arquivosEncontrados.get(0);
						return (Long.valueOf(logEDI.getIdLogEdi())).toString();
					}else{
						dadosArquivo[1] = nomeArqPart;
					}
				}
				
				Date data = new Date();
				if(dadosArquivo[2] != null && !dadosArquivo[2].equals("")){
					try{
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						data = format.parse(dadosArquivo[2]);
					}catch (Exception e) {
						log.error(e);
					}
				}
				logArquivoEDI.setData(new YearMonthDay(data));
				
				Date horaInicio = new Date();
				if(dadosArquivo[3] != null && !dadosArquivo[3].equals("")){
					try{
						SimpleDateFormat format = new SimpleDateFormat("mm:HH:ss");
						horaInicio = format.parse(dadosArquivo[3]);
					}catch (Exception e) {
						log.error(e);
					}
				}
				logArquivoEDI.setHoraInicio(new TimeOfDay(horaInicio));	
				if(dadosArquivo[5] == null || dadosArquivo[5].equals("")){
					logArquivoEDI.setStatus("Em processamento");
				}else{
					logArquivoEDI.setStatus(dadosArquivo[5]);
				}
				logArquivoEDI.setQtdePartes(0);
				
				if(dadosArquivo[1] != null && !dadosArquivo[1].equals("")){
					logArquivoEDI.setNome(dadosArquivo[1]);
				}
			}			
			
			Date horaFim = new Date();
			if(dadosArquivo[4] != null && !dadosArquivo[4].equals("")){
				try{
					SimpleDateFormat format = new SimpleDateFormat("mm:HH:ss");
					horaFim = format.parse(dadosArquivo[4]);
					
				}catch (Exception e) {
					log.error(e);
				}
			}
			logArquivoEDI.setHoraFim(new TimeOfDay(horaFim));	
		
			if(dadosArquivo[6] != null && !dadosArquivo[6].equals("")){
				if(dadosArquivo[5] != null && dadosArquivo[5].equalsIgnoreCase("Erro inesperado")){
					logArquivoEDI.setObservacao("idProcesso: "+dadosArquivo[6]);
				}else{
					logArquivoEDI.setObservacao(dadosArquivo[6]);
				}
			}
					
			if(logArquivoEDI.getNome() != null){
				Long idCliente = Long.valueOf(logArquivoEDI.getNome().split("_")[0]);		
				Long idClienteFiliaEmb = this.clienteLayoutEDIService.findById(idCliente).getEmbarcadora().getIdClienteEDIFilialEmbarcadora();
				ClienteEDIFilialEmbarcadora clienteEDIFilialEmbarcadora = this.clienteEDIFilialEmbarcadoraService.findById(idClienteFiliaEmb);
				logArquivoEDI.setFilial(clienteEDIFilialEmbarcadora.getFilial());
				logArquivoEDI.setClienteEDIFilialEmbarcadora(clienteEDIFilialEmbarcadora);
			}
			retorno = (this.logEDIService.store(logArquivoEDI)).toString();
		}catch (Exception e) {
			log.error(e);
			throw new Exception(this.getStackTrace(e));
			
		}
		
		return retorno;		
	}
	
	@MethodSecurity(processGroup = "layoutedi.manterLogsArquivos", processName = "gravaArquivoDetalheLog", authenticationRequired=false)
	public String gravaArquivoDetalheLog(Object object) throws Exception{
		String retorno = "true";
		try{
			LogDetalhe logDetalhe = (LogDetalhe)object;
			if(logDetalhe.getRegistros() != null){
				for (Registro registro : logDetalhe.getRegistros()){
					for (Detalhe detalhe : registro.getDetalhes()) {
						for (NotaFiscal notaFiscal : detalhe.getNotasFiscais()) {
							LogEDIDetalhe logEDIDetalhe = this.converteNotaFiscal(
									logDetalhe.getIdLogMestre(),
									notaFiscal,
									registro.getRemetente(),
									detalhe.getDestinatario(),
									detalhe.getConsignatario(),
									detalhe.getRedespacho(),
									detalhe.getTomador()
									);
							logEDIDetalhe.setLogComplementos(new ArrayList<LogEDIComplemento>());
							logEDIDetalhe.setLogItens(new ArrayList<LogEDIItem>());
							logEDIDetalhe.setLogVolumes(new ArrayList<LogEDIVolume>());
							if(notaFiscal.getItens() != null){
								for (Item item : notaFiscal.getItens()) {
									LogEDIItem logEDIItem = this.setaItem(item);
									logEDIItem.setLogEDIDetalhe(logEDIDetalhe);
									logEDIDetalhe.getLogItens().add(logEDIItem);
								}
							}
							if(notaFiscal.getComplementos() != null){
								for (Complemento complemento : notaFiscal.getComplementos()) {
									LogEDIComplemento logEDIComplemento = this.setaComplemento(complemento);
									logEDIComplemento.setLogEDIDetalhe(logEDIDetalhe);
									logEDIDetalhe.getLogComplementos().add(logEDIComplemento);
								}
							}
							if(notaFiscal.getVolumes() != null){
								for (Volume volume: notaFiscal.getVolumes()) {
									LogEDIVolume logEDIVolume = this.setaVolume(volume);
									logEDIVolume.setLogEDIDetalhe(logEDIDetalhe);
									logEDIDetalhe.getLogVolumes().add(logEDIVolume);
								}
							}
							this.logEDIDetalheService.store(logEDIDetalhe);
						}
					}
				}
			}
		}catch (Exception e) {
			log.error(e);
			throw new Exception(this.getStackTrace(e));
		}
		return retorno;		
	}

	private String getStackTrace(Exception e){
		StringBuilder retorno = new StringBuilder(e.getMessage());
		try{
			if(e.getStackTrace() != null && e.getStackTrace().length > 0){
				for (int i = 0; i < e.getStackTrace().length; i++) {
					retorno.append(e.getStackTrace()[i]).append("\n");
				}
				
			}
		}catch (Exception e2) {
			log.error(e2);
		}
		return retorno.toString();
	}

	public LogEDIService getLogEDIService() {
		return logEDIService;
	}

	public void setLogEDIService(LogEDIService logEDIService) {
		this.logEDIService = logEDIService;
	}

	public LogEDIComplementoService getLogEDIComplementoService() {
		return logEDIComplementoService;
	}

	public void setLogEDIComplementoService(LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}

	public LogEDIItemService getLogEDIItemService() {
		return logEDIItemService;
	}

	public void setLogEDIItemService(LogEDIItemService logEDIItemService) {
		this.logEDIItemService = logEDIItemService;
	}

	public LogEDIVolumeService getLogEDIVolumeService() {
		return logEDIVolumeService;
	}

	public void setLogEDIVolumeService(LogEDIVolumeService logEDIVolumeService) {
		this.logEDIVolumeService = logEDIVolumeService;
	}

	public LogEDIDetalheService getLogEDIDetalheService() {
		return logEDIDetalheService;
	}

	public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
		this.logEDIDetalheService = logEDIDetalheService;
	}
	
	
	
	

	public ClienteLayoutEDIService getClienteLayoutEDIService() {
		return clienteLayoutEDIService;
	}

	public void setClienteLayoutEDIService(ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}

	public ClienteEDIFilialEmbarcadoraService getClienteEDIFilialEmbarcadoraService() {
		return clienteEDIFilialEmbarcadoraService;
	}

	public void setClienteEDIFilialEmbarcadoraService(ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}

	private LogEDIDetalhe converteNotaFiscal(Long idLogArquivoEdi,
			NotaFiscal notaFiscal, 
			Remetente remetente,
			Destinatario destinatario,
			Consignatario consignatario,
			Redespacho redespacho,
			Tomador tomador){
		LogEDIDetalhe logEDI = new LogEDIDetalhe();
		
		LogEDI le = new LogEDI();
		le.setIdLogEdi(idLogArquivoEdi);
		logEDI.setLogEDI(le);
		logEDI.setAliqIcms(notaFiscal.getAliqIcms());
		logEDI.setAliqNf(notaFiscal.getAliqNf());
		logEDI.setCfopNf(notaFiscal.getCfopNf());
		logEDI.setChaveNfe(notaFiscal.getChaveNfe());
		logEDI.setDataEmissaoNf(new YearMonthDay(notaFiscal.getDataEmissaoNf()));
		logEDI.setEspecie(notaFiscal.getEspecie());
		logEDI.setModalFrete(notaFiscal.getModalFrete());
		logEDI.setNatureza(notaFiscal.getNatureza());
		logEDI.setNrNotaFiscal(notaFiscal.getNrNotaFiscal());
		logEDI.setOutrosValores(notaFiscal.getOutrosValores());
		logEDI.setPesoCubado(notaFiscal.getPesoCubado());
		logEDI.setPesoCubadoTotal(notaFiscal.getPesoCubadoTotal());
		logEDI.setPesoReal(notaFiscal.getPesoReal());
		logEDI.setPesoRealTotal(notaFiscal.getPesoRealTotal());
		logEDI.setPinSuframa(notaFiscal.getPinSuframa());
		logEDI.setQtdeVolumes(notaFiscal.getQtdeVolumes());
		logEDI.setSerieNf(notaFiscal.getSerieNf());
		logEDI.setTarifa(notaFiscal.getTarifa());
		logEDI.setTipoFrete(notaFiscal.getTipoFrete());
		logEDI.setTipoTabela(notaFiscal.getTipoTabela());
		logEDI.setVlrAdeme(notaFiscal.getVlrAdeme());
		logEDI.setVlrBaseCalcIcms(notaFiscal.getVlrBaseCalcIcms());
		logEDI.setVlrBaseCalcNf(notaFiscal.getVlrBaseCalcNf());
		logEDI.setVlrBaseCalcStNf(notaFiscal.getVlrBaseCalcStNf());
		logEDI.setVlrCat(notaFiscal.getVlrCat());
		logEDI.setVlrDespacho(notaFiscal.getVlrDespacho());
		logEDI.setVlrFreteLiquido(notaFiscal.getVlrFreteLiquido());
		logEDI.setVlrFretePeso(notaFiscal.getVlrFretePeso());
		logEDI.setVlrFreteValor(notaFiscal.getVlrFreteValor());
		logEDI.setVlrFreteTotal(notaFiscal.getVlrFreteTotal());
		logEDI.setVlrIcms(notaFiscal.getVlrIcms());
		logEDI.setVlrIcmsNf(notaFiscal.getVlrIcmsNf());
		logEDI.setVlrIcmsStNf(notaFiscal.getVlrIcmsStNf());
		logEDI.setVlrItr(notaFiscal.getVlrItr());
		logEDI.setVlrPedagio(notaFiscal.getVlrPedagio());
		logEDI.setVlrTaxas(notaFiscal.getVlrTaxas());
		logEDI.setVlrTotalMerc(notaFiscal.getVlrTotalMerc());
		logEDI.setVlrTotalMercTotal(notaFiscal.getVlrTotalMercTotal());
		logEDI.setVlrTotProdutosNf(notaFiscal.getVlrTotProdutosNf());
		
		
		logEDI.setDsDivisaoCliente(notaFiscal.getDsDivisaoCliente());
		logEDI.setNrCtrcSubcontratante(notaFiscal.getNrCtrcSubcontratante());
		
		if(remetente != null){
			logEDI.setNomeReme(remetente.getNomeReme());
			logEDI.setIeReme(remetente.getIeReme());
			logEDI.setEnderecoReme(remetente.getEnderecoReme());
			logEDI.setBairroReme(remetente.getBairroReme());
			logEDI.setMunicipioReme(remetente.getMunicipioReme());
			logEDI.setUfReme(remetente.getUfReme());
			logEDI.setCepEnderReme(remetente.getCepEnderReme());
			logEDI.setCepMuniReme(remetente.getCepMuniReme());
			logEDI.setCnpjReme(remetente.getCnpjReme());
		}
		if(destinatario != null){
			logEDI.setNomeDest(destinatario.getNomeDest());
			logEDI.setIeDest(destinatario.getIeDest());
			logEDI.setEnderecoDest(destinatario.getEnderecoDest());
			logEDI.setBairroDest(destinatario.getBairroDest());
			logEDI.setMunicipioDest(destinatario.getMunicipioDest());
			logEDI.setUfDest(destinatario.getUfDest());
			logEDI.setCepEnderDest(destinatario.getCepEnderDest());
			logEDI.setCepMunicDest(destinatario.getCepMunicDest());
			logEDI.setCnpjDest(destinatario.getCnpjDest());
		}
		if(consignatario != null){
			logEDI.setNomeConsig(consignatario.getNomeConsig());
			logEDI.setIeConsig(consignatario.getIeConsig());
			logEDI.setEnderecoConsig(consignatario.getEnderecoConsig());
			logEDI.setBairroConsig(consignatario.getBairroConsig());
			logEDI.setMunicipioConsig(consignatario.getMunicipioConsig());
			logEDI.setUfConsig(consignatario.getUfConsig());
			logEDI.setCepEnderConsig(consignatario.getCepEnderConsig());
			logEDI.setCepMunicConsig(consignatario.getCepMunicConsig());
			logEDI.setCnpjConsig(consignatario.getCnpjConsig());
		}
		if(redespacho != null){
			logEDI.setNomeRedesp(redespacho.getNomeRedesp());
			logEDI.setIeRedesp(redespacho.getIeRedesp());
			logEDI.setEnderecoRedesp(redespacho.getEnderecoRedesp());
			logEDI.setBairroRedesp(redespacho.getBairroRedesp());
			logEDI.setMunicipioRedesp(redespacho.getMunicipioRedesp());
			logEDI.setUfRedesp(redespacho.getUfRedesp());
			logEDI.setCepEnderRedesp(redespacho.getCepEnderRedesp().toString());
			logEDI.setCnpjRedesp(redespacho.getCnpjRedesp());
		}
		if(tomador != null){
			logEDI.setNomeTomador(tomador.getNomeTomador());
			logEDI.setIeTomador(tomador.getIeTomador());
			logEDI.setEnderecoTomador(tomador.getEnderecoTomador());
			logEDI.setBairroTomador(tomador.getBairroTomador());
			logEDI.setMunicipioTomador(tomador.getMunicipioTomador());
			logEDI.setUfTomador(tomador.getUfTomador());
			logEDI.setCepEnderTomador(tomador.getCepEnderTomador());
			logEDI.setCepMunicTomador(tomador.getCepMunicTomador());
			logEDI.setCnpjTomador(tomador.getCnpjTomador());
		}		
		logEDI.setObservacao(notaFiscal.getErro());
		
		
		
		if(notaFiscal.getErro() != null && !notaFiscal.getErro().equals("")){
			logEDI.setStatus("Erro");
		}else{
			logEDI.setStatus("OK");
		}
		return logEDI;
	}
	

	private LogEDIItem setaItem(Item item) {
		LogEDIItem logEDIItem = new LogEDIItem();
		
		logEDIItem.setAlturaItem(item.getAlturaItem());
		logEDIItem.setCodItem(item.getCodItem());
		logEDIItem.setComprimentoItem(item.getComprimentoItem());
		logEDIItem.setLarguraItem(item.getLarguraItem());
		logEDIItem.setPesoCubadoItem(item.getPesoCubadoItem());
		logEDIItem.setPesoRealItem(item.getPesoRealItem());
		logEDIItem.setValorItem(item.getValorItem());
		
		
		return logEDIItem;
	}

	private LogEDIVolume setaVolume( Volume volume) {
		LogEDIVolume logEDIVolume = new LogEDIVolume();
		logEDIVolume.setCodigoVolume(volume.getCodigoVolume());
		return logEDIVolume;
	}
	
	
	private LogEDIComplemento setaComplemento(Complemento complemento){
		LogEDIComplemento logEDIComplemento = new LogEDIComplemento();
		logEDIComplemento.setValorComplemento(complemento.getValorComplemento());
		logEDIComplemento.setNomeComplemento(complemento.getNomeComplemento());		
		return logEDIComplemento;
	}

	
}
