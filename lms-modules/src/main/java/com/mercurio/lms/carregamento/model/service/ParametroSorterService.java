package com.mercurio.lms.carregamento.model.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ParametroSorter;
import com.mercurio.lms.carregamento.model.dao.ParametroSorterDAO;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 *   Código do Jira :Requisito LMS560
 *   Autor/Data:  Tairone Lopes / 20/07/2011
 *   Descrição: Geração e envio de arquivo xml para o sorter
 *
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.parametroSorterService"
 */
public class ParametroSorterService extends CrudService<ParametroSorter, Long> {

	private ConteudoParametroFilialService conteudoParametroFilialService;
	private FilialService filialService;
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setParametroSorterDAO(ParametroSorterDAO dao) {
		setDao( dao );
	}

	
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ParametroSorterDAO getParametroSorterDAO() {
		return (ParametroSorterDAO) getDao();
	}
	
	public ParametroSorter findById(Long id){
		return (ParametroSorter) super.findById(id);
	}
	
	
	public ParametroSorter store(ParametroSorter bean){
		Filial filial = filialService.findById(bean.getFilial().getIdFilial());
		bean.setFilial(filial);
		super.store(bean);
		return bean;
	} 
	
	
	public ResultSetPage<Map<String, Object>> findPaginatedParametroSorter(TypedFlatMap criteria) {
		return getParametroSorterDAO().findPaginatedParametroSorter(criteria);
	}
	
	public List findLookupFilial(Map criteria){
		return filialService.findLookupFilial(criteria);
		
	}
	
	/**
	 * Método que responsável por chamar a camada service, metodo enviarParaSorter, 
	 * passando como parâmetro uma lista de ParametroSorter, que estão selecionados, 
	 * pelo checkbox ou consultados na aba de detalhamento, Se o retorno da rotina foi TRUE, 
	 * então apresentar mensagem de que a geração foi bem sucedida (LMS-05327 ? Arquivo(s) gerado(s) com sucesso), 
	 * caso contrario, apresentar mensagem de falha na geração (LMS-05326 ? Ocorreram problemas na geração dos arquivos para o SORTER). 
	 * Habilitar os controles da tela que foram desabilitados anteriormente	
	 */
	public void executeEnviarParaSorter(List<ParametroSorter> parametrosSorter){
			
		//busca os conhecimentos aereos
		List<String> codigosBarraAereo = null;

		boolean temConhecimentoAereo = false;
		if(parametrosSorter!=null){

			for(ParametroSorter parametroSorter : parametrosSorter){
				//valida se existe conhecimento aereo
				temConhecimentoAereo = (parametroSorter.getNmAereo()!=null && !parametroSorter.getNmAereo().trim().equals(""));
			}
			if(temConhecimentoAereo){
				codigosBarraAereo = getParametroSorterDAO().findDocumentosAereos();
			}
			
			//validando endereco do repositorio
			for(ParametroSorter parametroSorter : parametrosSorter){
				String vlParametrosFilial = getLayoutConexao(parametroSorter.getFilial());
				if(vlParametrosFilial == null || !validarRepositorioDadosSorter(vlParametrosFilial)){
					throw new BusinessException("LMS-05326");
				}
				
				List<String> codigosBarraAgendamento = null;
				//valida conhecimento agendado
				if(parametroSorter.getNmAgendamento()!=null && !parametroSorter.getNmAgendamento().trim().equals("")){
					//busca conhecimentos agendados desta filial
					codigosBarraAgendamento = buscaAgendamentos(parametroSorter);
				}
				
				//caso as duas listas estejam vazias nao enviar para sorter
				if((codigosBarraAgendamento!= null && !codigosBarraAgendamento.isEmpty())
						|| ( codigosBarraAereo!=null && !codigosBarraAereo.isEmpty()) ){

					//conhecimento aereo é consultado unica vez 
					//e colocado no xml apenas nos parametros que tem conhecimentos Aereos
					//cria arquivo xml
					String xml = criarXML(parametroSorter, codigosBarraAereo, codigosBarraAgendamento);
				//envia arquivo xml 
					enviaXML(xml, vlParametrosFilial);
				}else{
					throw new BusinessException("LMS-05326");
				}
				
			}
		}
		
		throw new BusinessException("LMS-05327");
		
	}
	
	private void enviaXML(String xml, String vlParametrosFilial){
		//String vlParametrosFilial = getLayoutConexao(filial);
		String[] vlParametrosFilialSplit = vlParametrosFilial.split(";");
		try{
		//chama api de comunicacao
		enviaArquivo(vlParametrosFilialSplit, xml);
		}catch(Exception e){
			new BusinessException("LMS-05326");
		}
	}
	
	private String criarXML(ParametroSorter parametroSorter, List<String> codigosBarraAereo, List<String> codigosBarraAgendamento){
		StringBuffer xml = new StringBuffer();
		xml.append("<Announcement>");
		xml.append("<PlanID>PM"+generatePlanID()+"</PlanID>"); 
		xml.append("<PlanMode></PlanMode>"); 
		xml.append("<PlanDescription></PlanDescription>"); 
		xml.append("<PlanDetails type=\"Announcements\">");
		
		if(parametroSorter.getNmAgendamento()!=null && !parametroSorter.getNmAgendamento().trim().equals("")){
			if(codigosBarraAgendamento!=null && codigosBarraAgendamento.size()>0){
				for(String codigoBarraAgendamento : codigosBarraAgendamento){
					xml.append("<Barcode value=\""+codigoBarraAgendamento+"\">");
					xml.append("<Destination>"+parametroSorter.getNmAgendamento()+"</Destination>"); 
					xml.append("</Barcode>");		
				}
			}
		}
		
		if(parametroSorter.getNmAereo()!=null && !parametroSorter.getNmAereo().trim().equals("")){
			if(codigosBarraAereo!=null && codigosBarraAereo.size()>0){
				for(String codigoBarraAereo : codigosBarraAereo){
					xml.append("<Barcode value=\""+codigoBarraAereo+"\">");
					xml.append("<Destination>"+parametroSorter.getNmAereo()+"</Destination>"); 
					xml.append("</Barcode>");
				}
			}
		}
		
		xml.append("</PlanDetails>");
		xml.append("</Announcement>");

		return xml.toString();
	}
	
	private Boolean validarRepositorioDadosSorter(String vlParametrosFilial) {
		try {
		//String vlParametrosFilial = getLayoutConexao(filial);
		String[] vlParametrosFilialSplit = vlParametrosFilial.split(";");
		String host = vlParametrosFilialSplit[0];
		String user = vlParametrosFilialSplit[1];
		String pass = vlParametrosFilialSplit[2];
		//chama API de conexao e testa
		FTPClient ftp = getFTPClient(vlParametrosFilialSplit);
		
		ftp.connect(host);
		checkReply(ftp, "FTP server refused connection.");

		ftp.login(user, pass);
		checkReply(ftp, "FTP server refused login.");
		ftp.disconnect();	
		} catch (IOException e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	private String  getLayoutConexao(Filial filial){
		return (String) conteudoParametroFilialService.findConteudoByNomeParametro(filial.getIdFilial(), "REP_DADOS_SORTER", false);
	}
	
	
	private void checkReply(FTPClient ftp, String message) throws IOException {
		String replyString = ftp.getReplyString();
		int reply = ftp.getReplyCode();  
		if (!FTPReply.isPositiveCompletion(reply)) {  
			throw new IOException("Erro na conexao FTP (" + message + ")  " + reply + " - " + replyString);
		}  
	}
	
	private FTPClient getFTPClient(String[] vlParametrosFilialSplit){
			
		FTPClient ftp = new FTPClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);

		if(vlParametrosFilialSplit.length == 6 && vlParametrosFilialSplit[5].equals("UX")){
			conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		}

		ftp.configure(conf);
		return ftp;
	}
	
	
	private void enviaArquivo(String[] vlParametrosFilialSplit, String xml) throws Exception{
		
		FTPClient ftp = getFTPClient(vlParametrosFilialSplit);
		String host = vlParametrosFilialSplit[0];
		String user = vlParametrosFilialSplit[1];
		String pass = vlParametrosFilialSplit[2];
		String fileName = vlParametrosFilialSplit[3];
		String directory = vlParametrosFilialSplit[4];

		
			ftp.connect(host);
			checkReply(ftp, "FTP server refused connection.");

			ftp.login(user, pass);
			checkReply(ftp, "FTP server refused login.");

			ftp.changeWorkingDirectory("/"+directory);
			InputStream file = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			
			ftp.storeFile(fileName, file);
			checkReply(ftp, "FTP server refused listing names.");

			ftp.logout();
			ftp.disconnect();
		

	}

	private String generatePlanID(){
		//Identificador único. Utilizar yyyymmddhh24missXXXXXX, onde XXXXXX será um número randômico de 6 caracteres
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss"); 
		Random rnd = new Random();
		int rand = rnd.nextInt(999999 + 1 );

		String planID = "" + sdf.format(new Date());
		planID += String.format("%06d",  rand);

		return planID;
	}
	
	private List buscaAgendamentos(ParametroSorter parametroSorter){
		List codigosBarraAgendamento = getParametroSorterDAO().findAgendamentos(parametroSorter.getFilial().getIdFilial());
		return codigosBarraAgendamento;
	}
	
	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

}