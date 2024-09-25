package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.expedicao.VolumeRPP;
import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.ControleEsteira;
import com.mercurio.lms.expedicao.model.dao.ControleEsteiraDAO;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.expedicao.controleEsteiraService"
 */
@Assynchronous
public class ControleEsteiraService extends CrudService<ControleEsteira, Long> {

	private static final String DEFAULT_FILENAME_SORTER = "Announcement.xml";
	private static final int[] BARCODE_ALLOWED_SIZE = {20, 33, 36};
	private static final String ATIVA_SORTER_PORTARIA = "ATIVA_SORTER_PORTARIA";

	private VolumeNotaFiscalService volumeNotaFiscalService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private CalcularFreteService calcularFreteService;
	private ParametroGeralService parametroGeralService;
	private PaisService paisService;
	private GeracaoXmlSorterService geracaoXmlSorterService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private VolumeNaoProcessadoService volumeNaoProcessadoService;

	private void checkReply(FTPClient ftp, String message) throws IOException {
		String replyString = ftp.getReplyString();
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			throw new IOException("Erro na conexao FTP (" + message + ")  "
					+ reply + " - " + replyString);
		}
	}

	@AssynchronousMethod(name = "expedicao.ExecuteGerarXmlSorter", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeGerarXmlSorter() {
		if(isExecutaPortaria()){
			return;
		}
	    Map<Long,List> xmlsFiliais = this.geracaoXmlSorterService.findDadosGeracaoXmlSorter();
	    sendFtpSorter(xmlsFiliais);
		
	}

	/**
	 * 
	 */
	private boolean isExecutaPortaria() {
		Object parametro = parametroGeralService.findConteudoByNomeParametro(ATIVA_SORTER_PORTARIA, false);
	    if(parametro!= null){
	    	if("S".equalsIgnoreCase(String.valueOf(parametro))){
	    		return true;
	    	}
	    }
	    return false;
	}
	
	
	public void executeGerarXmlSorterPortaria(Long idControleCarga) {
		if(isExecutaPortaria()){
			Map<Long,List> xmlsFiliais = this.geracaoXmlSorterService.findDadosGeracaoXmlSorterPortaria(idControleCarga	);
			sendFtpSorter(xmlsFiliais);
		}
	}
	

	/**
	 * @param xmlsFiliais
	 */
	private void sendFtpSorter(Map<Long, List> xmlsFiliais) {
		for (Entry<Long,List> xmlsFilial:xmlsFiliais.entrySet()){
		    Long idFilial = xmlsFilial.getKey();
		    try{
		        FTPClient ftp =  getFTPClient(getInfConnectionFTP(idFilial));
	            List<Map<String,String>> xmls = xmlsFilial.getValue();
	            for (Map<String,String> xml : xmls){
	                String fileName = xml.get("fileName");
	                String xmlFile = xml.get("xml");
	                
	                InputStream file = new ByteArrayInputStream(xmlFile.getBytes("UTF-8"));
	                
	                if (DEFAULT_FILENAME_SORTER.equals(fileName)){
	                    ftp.deleteFile(DEFAULT_FILENAME_SORTER);
	                }
	                ftp.storeFile(fileName, file);
	                checkReply(ftp, "FTP server refused listing names.");
	            }
	            ftp.logout();
	            ftp.disconnect();
		    }catch(IOException e){
		        log.error("Erro ao enviar para o FTP da filial " + idFilial, e);
		    }
		    
		}
	}

	/*
	 * @see atualizaInformacaoVolumes
	 * 
	 * Metodo substituido para ser executado através de serviço e não mais por batch
	 */
	@Deprecated
	private FTPClient getFTPClient(Map<String, String> params) throws SocketException, IOException {
		FTPClient ftp = null;
		if (!params.isEmpty()) {

			ftp = new FTPClient();
			FTPClientConfig conf = new FTPClientConfig(
					FTPClientConfig.SYST_NT);

			if ("UX".equals(params.get("syst").trim())) {
				conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
			}

			ftp.configure(conf);

			ftp.connect(params.get("host").trim(),
					IntegerUtils.getInteger(params.get("port").trim()));
			checkReply(ftp, "FTP server refused connection.");
			
			ftp.login(params.get("user").trim(), params.get("password")
					.trim());
			checkReply(ftp, "FTP server refused login.");
			
			if (params.get("folder") != null){
			    ftp.changeWorkingDirectory(params.get("folder").trim()); 
			    checkReply(ftp,"FTP directory does not exist.");
            }
		}
		return ftp;
	}

	/*
	 * @see atualizaInformacaoVolumes
	 * 
	 * Metodo substituido para ser executado através de serviço e não mais por batch
	 */
	@Deprecated
	private Map<String, String> getInfConnectionFTP(Long idFilial) {
		// Host: 10.44.38.54;Port: 21;User: TNT;Password: TNT
		Map<String, String> map = new HashMap<String, String>();
		try {
			String vlParametrosFilial = (String) conteudoParametroFilialService
					.findConteudoByNomeParametro(idFilial, "DADOS_FTP_SORTER",
							false);
			if (vlParametrosFilial != null) {
				String[] splitParams = vlParametrosFilial.split(";");
				for (String val : splitParams) {
					String k = val.split(":")[0];
					String v = val.split(":")[1];
					map.put(k.toLowerCase(), v);
				}
			}
		} catch (Exception e) {
			log.error(
					"Problema a buscar informação da conexão parametro filial DADOS_FTP_SORTER id Filial="
							+ idFilial, e);
		}
		return map;
	}
	
	public void updateInformacaoVolumes(ControleEsteira controleEsteira) {

		validaTamanhoCodigoBarras(controleEsteira.getCodBarras());
		
		volumeNotaFiscalService.generateInformacaoVolumeSorter(controleEsteira);		
	}

	public void atualizaInformacaoDescarga(ControleEsteira controleEsteira) {

		validaTamanhoCodigoBarras(controleEsteira.getCodBarras());

		volumeNotaFiscalService.atualizaInformacaoDescarga(controleEsteira);
	}

	private void validaTamanhoCodigoBarras(String codigoBarras) {

		if(codigoBarras == null || !ArrayUtils.contains(BARCODE_ALLOWED_SIZE, codigoBarras.length())) {
			throw new BusinessException("LMS-45207");
		}
	}

	public ControleEsteira gerarControleEsteira(VolumeRPP volumeRPP){
		ControleEsteira ce = new ControleEsteira();
		ce.setNrLote(volumeRPP.getNrLote());
		ce.setData(JTDateTimeUtils.convertDataStringToYearMonthDay(volumeRPP.getData(), JTDateTimeUtils.DATETIME_WITH_WITHOUT_TIME_PATTERN));
		ce.setHora(volumeRPP.getHora());
		ce.setCodBarras(volumeRPP.getCodBarras());
		ce.setComprimento(volumeRPP.getComprimento());
		ce.setLargura(volumeRPP.getLargura());
		ce.setAltura(volumeRPP.getAltura());
		ce.setPeso(volumeRPP.getPeso());
		return ce;
	}

	public ControleEsteira gerarControleEsteira(String nrLote, String codBarras, String data, String hora) {

		VolumeRPP volumeRPP = new VolumeRPP();

		volumeRPP.setNrLote(nrLote);
		volumeRPP.setCodBarras(codBarras);
		volumeRPP.setData(data);
		volumeRPP.setHora(hora);

		return gerarControleEsteira(volumeRPP);
	}

	/*
	 * Demanda de Contingência para calcular frete 
	 */
	public void executeFinalizaConhecimento(Map<String,Object> conhecimentoRPP){
		volumeNotaFiscalService.executeFinalizaConhecimento(conhecimentoRPP);
	}
	
	/*
	 * Demanda de Contingência para calcular frete 
	 */
	public void executeFinalizaDescargaColeta(Long idMonitoramento){
		volumeNotaFiscalService.executeFinalizaDescargaColeta(idMonitoramento);
	}

	 /**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private ControleEsteiraDAO getControleEsteiraDAO() {
		return (ControleEsteiraDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setControleEsteiraDAO(ControleEsteiraDAO dao) {
		setDao(dao);
	}

	public GeracaoXmlSorterService getGeracaoXmlSorterService() {
		return geracaoXmlSorterService;
	}

	public void setGeracaoXmlSorterService(
			GeracaoXmlSorterService geracaoXmlSorterService) {
		this.geracaoXmlSorterService = geracaoXmlSorterService;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(
			MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public CalcularFreteService getCalcularFreteService() {
		return calcularFreteService;
	}

	public void setCalcularFreteService(
			CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public VolumeNaoProcessadoService getVolumeNaoProcessadoService() {
		return volumeNaoProcessadoService;
	}

	public void setVolumeNaoProcessadoService(
			VolumeNaoProcessadoService volumeNaoProcessadoService) {
		this.volumeNaoProcessadoService = volumeNaoProcessadoService;
	}

	public PaisService getPaisService() {
		return paisService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

}
