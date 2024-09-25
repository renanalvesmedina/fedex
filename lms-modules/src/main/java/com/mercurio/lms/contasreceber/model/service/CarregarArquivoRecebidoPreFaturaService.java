package com.mercurio.lms.contasreceber.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ItemOcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.ItemPreFatura;
import com.mercurio.lms.contasreceber.model.OcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.PreFatura;
import com.mercurio.lms.contasreceber.model.param.ArquivoRecebidoPreFaturaTelaParam;
import com.mercurio.lms.contasreceber.model.param.FaturaArquivoRecebidoParam;
import com.mercurio.lms.contasreceber.model.param.LinhaArquivoRecebidoFaturaParam;
import com.mercurio.lms.contasreceber.model.param.LinhaArquivoRecebidoItemFaturaParam;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Mickaël Jalbert
 * @since 11/05/2006
 *  
 * @see Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.carregarArquivoRecebidoPreFaturaService"
 */
public class CarregarArquivoRecebidoPreFaturaService {	 

	
	/**
	 * Execução da rotina de importação de arquivo de pré-fatura DHL
	 * Método que chama a importação do arquivo
	 */
	
	private static final int COD_FATURA = 1;
	
	private static final int COD_ITEM_FATURA = 3;

	private Logger log = LogManager.getLogger(this.getClass());
	private ReportExecutionManager reportExecutionManager;
	
	
	DevedorDocServFatService devedorDocServFatService;
	
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public TypedFlatMap executeImportacao(ArquivoRecebidoPreFaturaTelaParam arquivoParam) {
		BufferedReader bufferedReader = arquivoParam.getArquivo();
		LinhaArquivoRecebidoFaturaParam linhaFaturaParam = null;
		LinhaArquivoRecebidoItemFaturaParam linhaItemFaturaParam = null;
		
		
		if(!faturaService.validaDtEmissao(arquivoParam.getDtEmissao())){
			throw new BusinessException("LMS-36099");
		}
		
		// Valida para que data de vencimento não seja menor que a data de emissão
		if (arquivoParam.getDtVencimento().isBefore(arquivoParam.getDtEmissao())) {
			throw new BusinessException("LMS-36192");
		}	
		
		
		String linha = null;
		
		
		
		//verifica se o arquivo contem alguma coisa e se a linha de 200 caracters que é o padrao

		//Lê a primeira linha, não precisa dos dados da primeira linha
		linha = readLine(bufferedReader);
		
	 
		/* Tratamento da primeira linha, que começa com o nome do arquivo*/
		String nmArquivo = linha.substring(0,1024);
		linha = linha.substring(1024);
		
		if (linha.length() < 3 || !linha.substring(0,3).equals("000")){
			throw new BusinessException("LMS-36193");
		}
		
		/* Lista que guardam os ids para ações futuras */
		FaturaArquivoRecebidoParam faturaParam = mountFaturaArquivoRecebidoParam(arquivoParam, nmArquivo.trim());		
		
		//Lê a primeira linha de dados
		linha = readLine(bufferedReader);
		
		while (linha != null){
			int tipoRegistro = getTipoLinha(linha);
			
			switch (tipoRegistro){
			case (COD_FATURA):
				linhaFaturaParam = new LinhaArquivoRecebidoFaturaParam(linha);
				addFatura(faturaParam, linhaFaturaParam);
				break;
			case (COD_ITEM_FATURA):
				linhaItemFaturaParam = new LinhaArquivoRecebidoItemFaturaParam(linha);
				if (faturaParam.getFatura()!=null) {
					addItemFatura(faturaParam, linhaItemFaturaParam);
				}
				break;
			default:				
				break;
			}
			
			//Lê a proxima linha de dados
			linha = readLine(bufferedReader);			
		}
		
		addFatura(faturaParam, null);
		faturaService.flush();
		return generateRelatorio(faturaParam.getIdsFatura());
	}
	
	/**
	 * Cada nova fatura gerada tem uma ocorrenciaPréFatura junto para guardar o histórico da importação
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam, Long nrPreFatura
	 * */
	private void generateOcorrenciaFatura(FaturaArquivoRecebidoParam faturaParam, Long nrPreFatura){
		OcorrenciaPreFatura ocorrenciaPreFatura = new OcorrenciaPreFatura();
		
		ocorrenciaPreFatura.setNrPreFatura(nrPreFatura.toString());
		ocorrenciaPreFatura.setCliente(faturaParam.getCliente());		
		ocorrenciaPreFatura.setDtEmissao(faturaParam.getDtEmissao());
		ocorrenciaPreFatura.setDtVencimento(faturaParam.getDtVencimento());
		ocorrenciaPreFatura.setDhImportacao(JTDateTimeUtils.getDataHoraAtual());
		ocorrenciaPreFatura.setNmArquivo(faturaParam.getNmArquivo());
		
		ocorrenciaPreFaturaService.store(ocorrenciaPreFatura);
		
		faturaParam.setOcorrenciaPreFatura(ocorrenciaPreFatura);
	}
	
	/**
	 * Cada nova item_fatura inválido gera uma ocorrenciaItemPréFatura
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam, Long nrPreFatura
	 * @param LinhaArquivoRecebidoItemFaturaParam linhaParam
	 * @param String codMensagem
	 * */
	private void generateItemOcorrenciaFatura(FaturaArquivoRecebidoParam faturaParam, LinhaArquivoRecebidoItemFaturaParam linhaParam, String codMensagem){
		ItemOcorrenciaPreFatura itemOcorrenciaPreFatura = new ItemOcorrenciaPreFatura();
		
		itemOcorrenciaPreFatura.setDtEmissao(linhaParam.getDtEmissaoNotaFiscal());
		itemOcorrenciaPreFatura.setNrDoctoServico(linhaParam.getNrNotaFiscal());
		itemOcorrenciaPreFatura.setObItemOcorrenciaPreFatura(configuracoesFacade.getMensagem(codMensagem));
		itemOcorrenciaPreFatura.setOcorrenciaPreFatura(faturaParam.getOcorrenciaPreFatura());
		itemOcorrenciaPreFatura.setTpDoctoServico(new DomainValue("NFS"));
		
		itemOcorrenciaPreFaturaService.store(itemOcorrenciaPreFatura);
	}
	
	/**
	 * Gera uma fatura com seus itens e uma pré-fatura com seus itens
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam
	 * */
	private void generateFatura(FaturaArquivoRecebidoParam faturaParam){
		Fatura fatura = faturaParam.getFatura();
		List idsDevedorDocServFat = faturaParam.getIdsDevedorDocServFat();
		List idsSemDuplicidade = new ArrayList();
				
		for (Iterator iter = idsDevedorDocServFat.iterator(); iter.hasNext();) {
			Long idDevedor = (Long)iter.next();
			if (!idsSemDuplicidade.contains(idDevedor)) {
				idsSemDuplicidade.add(idDevedor);
			}
		}
		
		//Pode gerar uma fatura só quando tem itens fatura
		if (fatura != null && !idsDevedorDocServFat.isEmpty()) {
			PreFatura preFatura = faturaParam.getPreFatura();
			List itensPreFatura = faturaParam.getItensPreFatura();			
			
			fatura = gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(fatura, idsSemDuplicidade);		

			preFatura.setFatura(fatura);
			
			//Salvar Pre-Fatura
			generatePreFatura(preFatura, itensPreFatura, idsDevedorDocServFat);
			
			//Adicionar a fatura à lista de faturas necessária para a geração do relatório
			faturaParam.addIdsFatura(fatura.getIdFatura());
		}
	}
	
	/**
	 * Gera uma pré-fatura com seus itens
	 * 
	 * @param PreFatura preFatura
	 * @param List itensPreFatura
	 * @param List idsDevedorDocServFat
	 * */	
	private void generatePreFatura(PreFatura preFatura, List itensPreFatura, List idsDevedorDocServFat){
		preFaturaService.store(preFatura);
		
		//Por cada item da pré-fatura
		for (int i = 0; i < itensPreFatura.size(); i++){
			ItemPreFatura itemPreFatura = (ItemPreFatura)itensPreFatura.get(i);
			ItemFatura itemFatura = itemFaturaService.findByDevedorDocServFat((Long)idsDevedorDocServFat.get(i));
			itemPreFatura.setItemFatura(itemFatura);
			
			itemPreFaturaService.store(itemPreFatura);
		}
	}	
	
	/**
	 * Cria um pojo fatura a partir dos dados da linha do arquivo e da tela
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam
	 * @param Long nrPreFatura
	 * 
	 * @return Fatura
	 * */	
	private Fatura mountFatura(FaturaArquivoRecebidoParam faturaParam, Long nrPreFatura){
		Fatura fatura = new Fatura();
		
		fatura.setNrPreFatura(nrPreFatura.toString());
		fatura.setCliente(faturaParam.getCliente());
		fatura.setCedente(faturaParam.getCedente());
		fatura.setDtEmissao(faturaParam.getDtEmissao());
		fatura.setDtVencimento(faturaParam.getDtVencimento());
		fatura.setBlGerarBoleto(faturaParam.getBlGerarBoleto());
		fatura.setDivisaoCliente(faturaParam.getDivisaoCliente());
		
		return fatura;
	}
	
	
	
	/**
	 * Cria um pojo preFatura a partir dos dados da linha do arquivo
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam
	 * @param LinhaArquivoRecebidoFaturaParam linhaParam
	 * 
	 * @return PreFatura
	 * */
	private PreFatura mountPreFatura(FaturaArquivoRecebidoParam faturaParam, LinhaArquivoRecebidoFaturaParam linhaParam){
		PreFatura preFatura = new PreFatura();
		
		preFatura.setCdDeposito(linhaParam.getCdDeposito());
		preFatura.setCdTransportadora(linhaParam.getCdTransportadora());
		preFatura.setDhImportacao(JTDateTimeUtils.getDataHoraAtual());
		preFatura.setDtFinalFechamento(linhaParam.getDtFinalFechamento());
		preFatura.setDtInicioFechamento(linhaParam.getDtInicioFechamento());
		preFatura.setDtVencimento(linhaParam.getDtVencimento());
		preFatura.setFatura(faturaParam.getFatura());
		preFatura.setNmCliente(linhaParam.getNmCliente());
		preFatura.setNrCnpjFornecedor(linhaParam.getNrCnpjFornecedor());
		preFatura.setNrCnpjTransportadora(linhaParam.getNrCnpjTransportadora());
		preFatura.setNrPreFatura(linhaParam.getNrPreFatura());
		preFatura.setTpFrete(linhaParam.getTpFrete());
		preFatura.setTpFreteUrbano(linhaParam.getTpFreteUrbano());
		preFatura.setTpModalidadeFrete(linhaParam.getTpModalidadeFrete());
		preFatura.setVlBloqueio(linhaParam.getVlBloqueio());
		preFatura.setVlDesbloqueio(linhaParam.getVlDesbloqueio());
		preFatura.setVlFrete(linhaParam.getVlFrete());

		return preFatura;
	}
	
	/**
	 * Salva a fatura e a pré-fatura anterior e monta a fatura e pré-fatura atual a partir da linha atual do arquivo
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam
	 * @param LinhaArquivoRecebidoFaturaParam linhaParam
	 * */
	private void addFatura(FaturaArquivoRecebidoParam faturaParam, LinhaArquivoRecebidoFaturaParam linhaParam){
		//Salvar fatura e pré-fatura anterior
		generateFatura(faturaParam);
		
		//Se a fatura noi foi gerada
		if (linhaParam != null && validateFatura(linhaParam.getNrPreFatura()).equals(Boolean.TRUE)){		
			//Salvar ocorrencia de fatura
			generateOcorrenciaFatura(faturaParam, linhaParam.getNrPreFatura());
			
			//Setar nova fatura
			faturaParam.setFatura(mountFatura(faturaParam, linhaParam.getNrPreFatura()));
			faturaParam.setPreFatura(mountPreFatura(faturaParam, linhaParam));
		
		} else {
			faturaParam.setFatura(null);
			faturaParam.setPreFatura(null);		
		}
		
		//Reiniciar as listas
		faturaParam.setIdsDevedorDocServFat(new ArrayList());
		faturaParam.setItenPreFatura(new ArrayList());			
	}
	
	/**
	 * Gera um item_fatura e item_pré_fatura se o devedor_dco_serv_fat é válido
	 * 
	 * @param FaturaArquivoRecebidoParam faturaParam
	 * @param LinhaArquivoRecebidoFaturaParam linhaParam
	 * */	
	private void addItemFatura(FaturaArquivoRecebidoParam faturaParam, LinhaArquivoRecebidoItemFaturaParam linhaParam){
		List lstDevedorDocServFat = devedorDocServFatService.findByNotaFiscalConhecimento(linhaParam.getNrNotaFiscal(), faturaParam.getCliente().getIdCliente(), linhaParam.getDtEmissaoNotaFiscal(), SessionUtils.getFilialSessao().getIdFilial());
		
		//Se não tem registro {
		if (lstDevedorDocServFat.isEmpty()) {
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36131");
		} else if (faturaParam.getFatura() != null){
			for (Iterator iter = lstDevedorDocServFat.iterator(); iter.hasNext();) {
				Object[] devedorDocServFat = (Object[])iter.next();
				
				Boolean blValido = validateDevedorDocServFat((String)devedorDocServFat[4], (String)devedorDocServFat[9], (Long)devedorDocServFat[5], faturaParam, linhaParam, (String)devedorDocServFat[10], (Long)devedorDocServFat[6], (String)devedorDocServFat[11]);
				
				if (blValido.equals(Boolean.TRUE)){				
					ItemPreFatura itemPreFatura = mountItemPreFatura(linhaParam);
					faturaParam.addIdsDevedorDocServFat((Long)devedorDocServFat[8]);
					faturaParam.addItemPreFatura(itemPreFatura);
				}
			}
		}
	}	
	
	/**
	 * Cria um pojo itemPreFatura a partir dos dados da linha do arquivo
	 * 
	 * @param LinhaArquivoRecebidoItemFaturaParam linhaParam
	 * 
	 * @return ItemPreFatura
	 * */	
	private ItemPreFatura mountItemPreFatura(LinhaArquivoRecebidoItemFaturaParam linhaParam){
		ItemPreFatura itemPreFatura = new ItemPreFatura();
		
		itemPreFatura.setCdSerieNotaFiscal(linhaParam.getCdSerieNotaFiscal());
		itemPreFatura.setDtEmissaoNotaFiscal(linhaParam.getDtEmissaoNotaFiscal());
		itemPreFatura.setNmCidadeClienteDestino(linhaParam.getNmCidadeClienteDestino());
		itemPreFatura.setNmClienteDestino(linhaParam.getNmClienteDestino());
		itemPreFatura.setNrCnpjClienteDestino(linhaParam.getNrCnpjClienteDestino());
		itemPreFatura.setNrNotaFiscal(linhaParam.getNrNotaFiscal());
		itemPreFatura.setNrProtocolo(linhaParam.getNrProtocolo());
		itemPreFatura.setNrRoteiro(linhaParam.getNrRoteiro());
		itemPreFatura.setPsAforado(linhaParam.getPsAforado());
		itemPreFatura.setPsMercadoria(linhaParam.getPsMercadoria());
		itemPreFatura.setTpFrete(linhaParam.getTpFrete());
		itemPreFatura.setTpFreteUrbano(linhaParam.getTpFreteUrbano());
		itemPreFatura.setTpModalidadeFrete(linhaParam.getTpModalidadeFrete());
		itemPreFatura.setVlNotaFiscal(linhaParam.getVlNotaFiscal());
		
		return itemPreFatura;
	}
	
	/**
	 * Valida se a pré-fatura já existe no sistema (o arquivo já foi carregado
	 * 
	 * @param Long nrPreFatura
	 * 
	 * @return Boolean
	 * */
	private Boolean validateFatura(Long nrPreFatura){
		List lstFatura = faturaService.findByNrPreFatura(nrPreFatura);
		
		if (!lstFatura.isEmpty()){
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}	
	
	/**
	 * Valida se o devedor_doc_serv_fat e a filial são válido
	 * */
	private Boolean validateDevedorDocServFat(String tpSitucaoCobranca, String tpSituacaoConhecimento, Long idFilial, FaturaArquivoRecebidoParam faturaParam, LinhaArquivoRecebidoItemFaturaParam linhaParam, String sgFilial, Long nrDoctoServico, String tpConhecimento){
		//Se a situação de cobrança for 'Liquidado'
		if (tpSitucaoCobranca.equals("L")){
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36116");
			return Boolean.FALSE;
		}
		
		//Se a situação de cobrança for 'Em fatura'
		if (tpSitucaoCobranca.equals("F")){
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36118");
			return Boolean.FALSE;
		}
		
		//Se a situação do conhecimento for 'Cancelado' 
		if (tpSituacaoConhecimento.equals("C")){
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36117");
			return Boolean.FALSE;
		}
		
		if (!faturaParam.isClienteDHL() && !tpSituacaoConhecimento.equals("E") && !tpSituacaoConhecimento.equals("B")){
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36259");
			return Boolean.FALSE;
		}
		
		if (faturaParam.isClienteDHL() && "NO".equals(tpConhecimento) && !tpSituacaoConhecimento.equals("E") && !tpSituacaoConhecimento.equals("B")) { // LMS-2347
			generateItemOcorrenciaFatura(faturaParam, linhaParam, "LMS-36259");
			return Boolean.FALSE;
		}
		
		//Se a filial for diferente da filial da sessão
		if (!idFilial.equals(SessionUtils.getFilialSessao().getIdFilial())){
			throw new BusinessException("LMS-36119", new Object[]{sgFilial + " " + FormatUtils.formataNrDocumento(nrDoctoServico.toString(), "CTR") + "."});
		}
		
		return Boolean.TRUE;
	}
	
	
	/**
	 * Gera o relatório no fim da imporatação, se não foi incluido nenhuma fatura, lançar um erro.
	 * Retorna o caminho do relatório criado.
	 * 
	 * @param List idsFatura
	 * 
	 * @return String
	 * */
	private TypedFlatMap generateRelatorio(List idsFatura){
		String reportLocation = "";
		TypedFlatMap tfm = new TypedFlatMap();
		
		if (idsFatura.size() > 0) {
			try {
				String importacaoPrefaturas = "";
				
				for (Iterator iter = idsFatura.iterator(); iter.hasNext();) {
					Long id = (Long) iter.next();
					importacaoPrefaturas += id.toString();
					
					if (iter.hasNext())
						importacaoPrefaturas += ", ";						
				}
				
				TypedFlatMap parameters = new TypedFlatMap();
				parameters.put("importacaoPreFaturas", importacaoPrefaturas);
				parameters.put("tpFormatoRelatorio", "pdf");

				reportLocation = this.reportExecutionManager.generateReportLocator("lms.contasreceber.emitirFaturasNacionaisService", parameters);
				tfm.put("ex", "");
			} catch (Exception e) {
				log.error(e);
			}
		} else {
			// REGRA: Arquivo não pertence a este cliente.
			tfm.put("ex", configuracoesFacade.getMensagem("LMS-36112"));
		}
		
		tfm.put("_value", reportLocation);
		
		return tfm;
	}
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * FACILITADORES
	 * 
	 * 
	 * 
	 * */
	
	/**
	 * Lê uma linha do arquivo
	 * */
	private String readLine(BufferedReader bufferedReader){
		try {
			String retorno = bufferedReader.readLine();
			
			if (StringUtils.isNotBlank(retorno)){
				return retorno;
			} else {
				//Fecha o buffer
				bufferedReader.close();				
			}
		} catch (IOException e){
			log.error(e);
		}
		
		return null;
	}
	
	/**
	 * Pega o tipo de linha conforme a linha corrente.
	 * Baseia-se nas constante da classe
	 * @return Tipo da linha
	 */
	public int getTipoLinha(String linha) {
		int tipo = Integer.parseInt(linha.substring(0,3));
		
		switch (tipo) {
			case 1: 
				return COD_FATURA; 
			case 3:
				return COD_ITEM_FATURA; 
			default: 
				return 0;
		}
	}	
	
	
	/**
	 * Monta o objeto facilitador da classe, para evitar assinatura de monstro, foi criado o objeto FaturaArquivoRecebidoParam
	 * É montado a partir dos dados da tela.
	 * 
	 * @param ArquivoRecebidoPreFaturaTelaParam arquivoParam
	 * @param String nmArquivo
	 * 
	 * @return FaturaArquivoRecebidoParam
	 * */
	private FaturaArquivoRecebidoParam mountFaturaArquivoRecebidoParam(ArquivoRecebidoPreFaturaTelaParam arquivoParam, String nmArquivo){
		FaturaArquivoRecebidoParam faturaParam = new FaturaArquivoRecebidoParam();
		faturaParam.setBlGerarBoleto(arquivoParam.getBlGerarBoleto());
		faturaParam.setCedente(arquivoParam.getCedente());
		faturaParam.setCliente(arquivoParam.getCliente());
		faturaParam.setDivisaoCliente(arquivoParam.getDivisaoCliente());
		faturaParam.setDtEmissao(arquivoParam.getDtEmissao());
		faturaParam.setDtVencimento(arquivoParam.getDtVencimento());
		faturaParam.setNmArquivo(nmArquivo);
		faturaParam.setBlClientDHL(arquivoParam.getBlClienteDHL());
		return faturaParam;
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 * SETTER'S E GETTER'S
	 * 
	 * 
	 * 
	 * */

	/**
	 * IoC -> ocorrenciaPreFaturaService
	 */
	OcorrenciaPreFaturaService ocorrenciaPreFaturaService;
	public void setOcorrenciaPreFaturaService(OcorrenciaPreFaturaService ocorrenciaPreFaturaService) {
		this.ocorrenciaPreFaturaService = ocorrenciaPreFaturaService;
	}
	
	/**
	 * IoC -> itemOcorrenciaPreFaturaService
	 */
	ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService;
	public void setItemOcorrenciaPreFaturaService(ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService) {
		this.itemOcorrenciaPreFaturaService = itemOcorrenciaPreFaturaService;
	}
	
	/**
	 * IoC -> faturaService
	 */
	FaturaService faturaService;
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	
	/**
	 * IoC -> gerarFaturaFaturaService
	 */
	GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService;
	public void setGerarFaturaArquivoRecebidoService(GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService) {
		this.gerarFaturaArquivoRecebidoService = gerarFaturaArquivoRecebidoService;
	}

	/**
	 * IoC -> configuracoesFacade
	 */
	ConfiguracoesFacade configuracoesFacade;
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	/**
	 * IoC -> preFaturaService
	 */
	PreFaturaService preFaturaService;
	public void setPreFaturaService(PreFaturaService preFaturaService) {
		this.preFaturaService = preFaturaService;
	}
	
	/**
	 * IoC -> itemPreFaturaService
	 */
	ItemPreFaturaService itemPreFaturaService;
	public void setItemPreFaturaService(ItemPreFaturaService itemPreFaturaService) {
		this.itemPreFaturaService = itemPreFaturaService;
	}
	
	/**
	 * IoC -> itemPreFaturaService
	 */
	ItemFaturaService itemFaturaService;
	public void setItemFaturaService(ItemFaturaService itemFaturaService) {
		this.itemFaturaService = itemFaturaService;
	}	
	
	/**
	 * IoC -> itemPreFaturaService
	 */
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

}
