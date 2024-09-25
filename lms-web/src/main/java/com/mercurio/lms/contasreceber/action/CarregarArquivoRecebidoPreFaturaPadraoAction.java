package com.mercurio.lms.contasreceber.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mercurio.lms.contasreceber.model.Fatura;
import org.apache.commons.lang.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.BASE64Decoder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.param.ArquivoRecebidoPreFaturaPadraoTelaParam;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.contasreceber.model.service.CarregarArquivoRecebidoPreFaturaPadraoService;
import com.mercurio.lms.contasreceber.model.service.CarregarArquivoRecebidoService;
import com.mercurio.lms.contasreceber.model.service.CedenteService;
import com.mercurio.lms.contasreceber.report.EmitirFaturasNacionaisService;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;


/**
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.carregarArquivoRecebidoPreFaturaPadraoAction"
 */
public class CarregarArquivoRecebidoPreFaturaPadraoAction extends ReportActionSupport {

	public static final int COLUNA_F = 5;
	public static final String TIPO_DOCTO_SERVICO_CTE = "CT-e";
	public static final String CSV_DELIMITIER = ";";
	private Logger log = LogManager.getLogger(this.getClass());

	private CarregarArquivoRecebidoPreFaturaPadraoService carregarArquivoRecebidoPreFaturaPadraoService;

	private EmitirFaturasNacionaisService emitirFaturasNacionaisService;

	private DataVencimentoService dataVencimentoService;
	
	private DivisaoClienteService divisaoClienteService;
	
	private CedenteService cedenteService;
	
	private CarregarArquivoRecebidoService carregarArquivoRecebidoService;


	/**
	 * 
	 * Executa a importa��o do arquivo
	 * 
	 * @param params parametros de importação
	 * @return TypedFlatMap
	 */
	public TypedFlatMap executeImportacao(TypedFlatMap params){
		
		ArquivoRecebidoPreFaturaPadraoTelaParam param = new ArquivoRecebidoPreFaturaPadraoTelaParam();
		
		BufferedReader buffer = getBuffer(params.getString("arquivo")); 
		
		/*Obtem o nome do arquivo*/
		String fileName = null;
		try {
			fileName = buffer.readLine().substring(0, 1024).trim();
		} catch (IOException e) {
			log.error(e);
		}
		
		if(BooleanUtils.isFalse(fileName.endsWith("csv"))){				
			throw new BusinessException("Arquivo inv�lido. Somente utilize arquivos csv (Separado por ponto e v�rgula)");
		}

		validarApenasUmTipoDoctoServico(getBuffer(params.getString("arquivo")));
		
		param.setArquivo(buffer);
		
		param.setFileName(fileName);		
		
		param.setBlGerarBoleto(params.getBoolean("blGerarBoleto"));
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(params.getLong("cliente.idCliente"));
		param.setCliente(cliente);
		
		Cedente cedente = new Cedente();
		cedente.setIdCedente(params.getLong("cedente.idCedente"));
		param.setCedente(cedente);
		
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(params.getLong("divisaoCliente.idDivisaoCliente"));
		param.setDivisaoCliente(divisaoCliente);
		
		param.setDtEmissao(params.getYearMonthDay("dtEmissao"));
		
		param.setDtVencimento(params.getYearMonthDay("dtVencimento"));
		
		param.setTpModal(params.getDomainValue("tpModal"));
		param.setTpAbrancencia(params.getDomainValue("tpAbrangencia"));

		return getCarregarArquivoRecebidoPreFaturaPadraoService().executeImportacao(param);
	}

	public TypedFlatMap printReport(TypedFlatMap params){
		return getCarregarArquivoRecebidoPreFaturaPadraoService().generateReport(params.getLong("idFatura"));
	}

	private void validarApenasUmTipoDoctoServico(BufferedReader arquivo) {
		Set tiposDocumentoServico = new HashSet<String>();
		boolean existeTipoDoctoServicoNaoPreenchido = false;

		try {
			String line = arquivo.readLine(); //A primeira linha, relativa ao nome do arquivo, não é necessária
			while((line = arquivo.readLine()) != null) {
				String[] columns = line.split(CSV_DELIMITIER);
				 if(columns.length > 0) {
				 	if(possuiColunaF(columns)) {
						tiposDocumentoServico.add(columns[COLUNA_F]);
					} else {
				 		existeTipoDoctoServicoNaoPreenchido = true;
					}
				 }
            }
		} catch (IOException e) {
			logger.error(e);
		}

		if(tiposDocumentoServico.size() > 1) {
			throw new BusinessException("LMS-36392");
		}

		if(existeTipoDoctoServicoNaoPreenchido) {
			tiposDocumentoServico.remove(TIPO_DOCTO_SERVICO_CTE);
			if(!tiposDocumentoServico.isEmpty()) {
				throw new BusinessException("LMS-36392");
			}
		}
	}

	private boolean possuiColunaF(String[] columns) {
		return columns.length >= 6;
	}

	/**
	 * Cria o buffer para leitura linha a linha do arquivo selecionado
	 * @param arquivo campo da tela
	 * @return BufferedReader
	 */
	private BufferedReader getBuffer(String arquivo) {		
		return new BufferedReader(new InputStreamReader(getByteArrayInputStream(arquivo)));
	}	
	
	/**
	 * Obtem o File
	 * 
	 * @param arquivo
	 * @return
	 */
	private ByteArrayInputStream getByteArrayInputStream(String arquivo){
		
		ByteArrayInputStream ba  = null;
		try {
			ba = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(arquivo));			
		} catch (IOException e) {
			log.error(e);
		}		
		return ba;				
	}
	
	
	/**
	 * Obtem data atual do sistema
	 * @return
	 */
	public YearMonthDay findDataAtual() {
		return JTDateTimeUtils.getDataAtual();
	}
	
	/**
	 * Preenche o combo cedentes
	 * @return
	 */
	public List findComboCedentes(){
		return cedenteService.findComboByActiveValues(new TypedFlatMap());
	}
	
	/**
	 * Preenche o combo divis�o cliente
	 * 
	 * @param criteria
	 * @return
	 */
	public List findComboDivisaoCliente(TypedFlatMap criteria){

		DivisaoClienteParam dcp = new DivisaoClienteParam();
		
		dcp.setIdCliente(criteria.getLong("cliente.idCliente"));
		dcp.setTpSituacao("A");		
		
		return divisaoClienteService.findByIdClienteMatriz(dcp);
	}
	
	public String findDataVencimento(TypedFlatMap tfm) {
		YearMonthDay vencimento = dataVencimentoService.generateDataVencimento(
					SessionUtils.getFilialSessao().getIdFilial(), 
					tfm.getLong("idDivisaoCliente"),
					tfm.getString("C"),
					tfm.getYearMonthDay("dtEmissao"),
					null, // tpModal
					null, // tpAbrangencia
					null  // idServico
				);
		
		return JTFormatUtils.format(vencimento);
	}	
	
	public TypedFlatMap findCedenteByCliente(TypedFlatMap tfm){
		return carregarArquivoRecebidoService.findCedenteByCliente(tfm.getLong("cliente.idCliente"));
	}	
	
	public void setEmitirFaturasNacionaisService(EmitirFaturasNacionaisService emitirFaturasNacionaisService) {
		this.reportServiceSupport = emitirFaturasNacionaisService;
	}

	public EmitirFaturasNacionaisService getEmitirFaturasNacionaisService() {
		return emitirFaturasNacionaisService;
	}

	public CarregarArquivoRecebidoPreFaturaPadraoService getCarregarArquivoRecebidoPreFaturaPadraoService() {
		return carregarArquivoRecebidoPreFaturaPadraoService;
	}

	public void setCarregarArquivoRecebidoPreFaturaPadraoService(
			CarregarArquivoRecebidoPreFaturaPadraoService carregarArquivoRecebidoPreFaturaPadraoService) {
		this.carregarArquivoRecebidoPreFaturaPadraoService = carregarArquivoRecebidoPreFaturaPadraoService;
	}

	public CedenteService getCedenteService() {
		return cedenteService;
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public CarregarArquivoRecebidoService getCarregarArquivoRecebidoService() {
		return carregarArquivoRecebidoService;
	}

	public void setCarregarArquivoRecebidoService(
			CarregarArquivoRecebidoService carregarArquivoRecebidoService) {
		this.carregarArquivoRecebidoService = carregarArquivoRecebidoService;
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public DataVencimentoService getDataVencimentoService() {
		return dataVencimentoService;
	}

	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}

}
