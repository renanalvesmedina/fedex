package com.mercurio.lms.contasreceber.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.BASE64Decoder;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.param.ArquivoRecebidoPreFaturaTelaParam;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.contasreceber.model.service.CarregarArquivoRecebidoPreFaturaService;
import com.mercurio.lms.contasreceber.model.service.CarregarArquivoRecebidoService;
import com.mercurio.lms.contasreceber.model.service.CedenteService;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.AgrupamentoClienteService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * @author Rafael Andrade de Oliveira
 * @since 28/04/2006
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.carregarArquivoRecebidoPreFaturaAction"
 */

public class CarregarArquivoRecebidoPreFaturaAction {

	private Logger log = LogManager.getLogger(this.getClass());

	private DataVencimentoService dataVencimentoService;
	
	private CedenteService cedenteService;
	
	private ClienteService clienteService;
	
	private DivisaoClienteService divisaoClienteService;

	private AgrupamentoClienteService agrupamentoClienteService;

	private CarregarArquivoRecebidoService carregarArquivoRecebidoService;
	
	private CarregarArquivoRecebidoPreFaturaService carregarArquivoRecebidoPreFaturaService;
	
	public CarregarArquivoRecebidoPreFaturaService getCarregarArquivoRecebidoPreFaturaService() {
		return carregarArquivoRecebidoPreFaturaService;
	}

	public void setCarregarArquivoRecebidoPreFaturaService(CarregarArquivoRecebidoPreFaturaService carregarArquivoRecebidoPreFaturaService) {
		this.carregarArquivoRecebidoPreFaturaService = carregarArquivoRecebidoPreFaturaService;
	}

	public DataVencimentoService getDataVencimentoService() {
		return dataVencimentoService;
	}

	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}
	
	public List findComboCedentes(){
		return cedenteService.findComboByActiveValues(new TypedFlatMap());
	}	
	
	public List findComboDivisaoCliente(TypedFlatMap criteria){
		return divisaoClienteService.findByIdClienteMatriz(populateDivisaoClienteParam(criteria));
	}
	
	/**
	 * Popula a DivisaoClienteparam para ser usado como filtro na busca por divisao
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/01/2007
	 *
	 * @param tfm
	 * @return
	 *
	 */
	public DivisaoClienteParam populateDivisaoClienteParam(TypedFlatMap tfm){
		
		DivisaoClienteParam dcp = new DivisaoClienteParam();
		
		dcp.setIdCliente(tfm.getLong("cliente.idCliente"));
		dcp.setTpSituacao("A");
		
		return dcp;		
	}
	
	public List findLookupCliente(Map map){
		return clienteService.findLookup(map);
	}
	
	/**
	 * Execu��o da rotina de importa��o de arquivo de pr�-fatura -> PROCEDA
	 * M�todo que chama a importa��o do arquivo
	 * @param tfm Parametros da tela
	 * @return String com o nome do arquivo do relatorio gerado no servidor
	 */
	
	
	public TypedFlatMap executeImportacao(TypedFlatMap tfm) {
		ArquivoRecebidoPreFaturaTelaParam telaParam = new ArquivoRecebidoPreFaturaTelaParam();
		telaParam.setArquivo(getBuffer(tfm.getString("arquivo")));
		telaParam.setBlGerarBoleto(tfm.getBoolean("gerarBoleto"));
		
		Cliente cliente = clienteService.findById(tfm.getLong("cliente.idCliente"));
		if (cliente != null) {
			telaParam.setCliente(cliente);
		}		
		
		if (cedenteService.findById(tfm.getLong("cedente.idCedente")) != null) {
			telaParam.setCedente(cedenteService.findById(tfm.getLong("cedente.idCedente")));
		}

		if (divisaoClienteService.findById(tfm.getLong("divisaoCliente.idDivisaoCliente")) != null) {
			telaParam.setDivisaoCliente(divisaoClienteService.findById(tfm.getLong("divisaoCliente.idDivisaoCliente")));
		}
		
		telaParam.setDtEmissao(tfm.getYearMonthDay("dtEmissao"));
		telaParam.setDtVencimento(tfm.getYearMonthDay("dtVencimento"));
		
		telaParam.setBlClienteDHL(Boolean.valueOf(tfm.getString("isDHL")));
		
		return getCarregarArquivoRecebidoPreFaturaService().executeImportacao(telaParam);
	}
	
	/**
	 * Busca uma data de vencimento calculada
	 * @param tfm Parametros da tela
	 * @return data de acordo com o c�lculo de vencimento
	 */
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
	
	/**
	 * Cria o buffer para leitura linha a linha do arquivo selecionado
	 * @param arquivo campo da tela
	 * @return BufferedReader
	 */
	private BufferedReader getBuffer(String arquivo) {
		
		ByteArrayInputStream ba = null;
		BufferedReader buff = null;
		
		try {
			ba = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(arquivo));
			buff = new BufferedReader(new InputStreamReader(ba));
		} catch (IOException e) {
			log.error(e);
		}
		
		return buff;
	}	

	public YearMonthDay findDataAtual() {
		return JTDateTimeUtils.getDataAtual();
	}

	public Boolean verifDivisaoClientePossuiAgrup(TypedFlatMap tfm) {
		Long idDivisao = (tfm.getLong("idDivisaoCliente"));

		if (idDivisao != null) {
			Integer ret = agrupamentoClienteService.findByDivisaoClienteId(idDivisao);
			if (ret != null)
				return ret > 0;
		}
		return false;
	}
	
	/**
	 * @author Edenilson
	 * @since  22/08/2006
	 * 
	 * Se o cliente possui um cedente, retorna este para a combo "Cedente", 
	 * caso contr�rio, traz o cedente da filial de cobran�a do cliente, ou seja utilizar 
	 * FILIAL.ID_CEDENTE_BLOQUETO onde FILIAL.ID_FILIAL = CLIENTE.ID_FILIAL_COBRANCA.
	 * 
	 * @param tfm IdCliente
	 * @return Cedente
	 * 
	 * Ficou apontando para o service do outro carregar arquivo, pois � a mesma l�gica
	 * 
	 */
	public TypedFlatMap findCedenteByCliente(TypedFlatMap tfm){
		return carregarArquivoRecebidoService.findCedenteByCliente(tfm.getLong("cliente.idCliente"));
	}
	
	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setCarregarArquivoRecebidoService(
			CarregarArquivoRecebidoService carregarArquivoRecebidoService) {
		this.carregarArquivoRecebidoService = carregarArquivoRecebidoService;
	}

	public void setAgrupamentoClienteService(
			AgrupamentoClienteService agrupamentoClienteService) {
		this.agrupamentoClienteService = agrupamentoClienteService;
	}

}
