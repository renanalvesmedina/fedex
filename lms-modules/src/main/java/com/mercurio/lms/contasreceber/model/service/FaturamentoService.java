package com.mercurio.lms.contasreceber.model.service;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.FaturamentoDAO;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlGroupByFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TipoAgrupamento;


public abstract class FaturamentoService {
	
	private FaturamentoDAO faturamentoDAO;
	
	private GerarFaturaFaturamentoService gerarFaturaFaturamentoService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
    private BatchLogger batchLogger; 
    
    private static final Log logger = LogFactory.getLog(FaturamentoService.class);

    public void setBatchLogger(BatchLogger batchLogger) {
        this.batchLogger = batchLogger;
        batchLogger.logClass(getClass());
    }

	
	public Integer generateFaturamento(SqlFaturamentoParam param) {
		try {
			return executeFaturamento(param);
		} catch (SQLException e) {
			logger.error("Erro no faturamento", e);
			batchLogger.fatal(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * A partir dos parametros da tela, gera o faturamento
	 */
	protected abstract Integer executeFaturamento(SqlFaturamentoParam param) throws SQLException;
	
	public Integer executeSql(SqlFaturamentoParam param, SqlTemplate sql) throws SQLException {
		LinhaSqlFaturamentoParam linha = null;
		LinhaSqlFaturamentoParam linhaAnterior = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		List lstIdDevedoreDocServFat = new ArrayList();
		List lstLinhas = new ArrayList();
		
		try {
			batchLogger.warning("[Faturamento automático] Início execução da consulta de devedores: " + sdf.format(Calendar.getInstance().getTime()));
			lstLinhas = getFaturamentoDAO().executeSql(sql.getSql(), sql.getCriteria());
			batchLogger.warning("[Faturamento automático] Fim execução da consulta de devedores: " + sdf.format(Calendar.getInstance().getTime()));
		}catch (SQLException e) {
			if (param.getBlFaturamentoManual()) {
				throw e;
			}
		}
		
		Iterator iter = lstLinhas.iterator();
		//Por cada devedor
		while (iter.hasNext()) {
			Object[] element = (Object[]) iter.next();
			linha = mountLinhaSqlFaturamento(element);

			// Gera log para visualizar o andamento do processo no batch
			// Para cada cliente processado grava o log
			if (linhaAnterior == null || (linha.getIdCliente() != linhaAnterior.getIdCliente())){
				batchLogger.warning("Processando Cliente: " + linha.getIdCliente());
				batchLogger.warning("[Faturamento automático] Início processamento do cliente: "  + linha.getIdCliente() + " - " + sdf.format(Calendar.getInstance().getTime()));
			}

			//Se tem uma quebra (gerar uma nova fatura com os devedores anteriores acumulado)
			if (linhaAnterior != null &&
				(linha.getIdCliente() != linhaAnterior.getIdCliente() ||
				linha.getIdFilial() != linhaAnterior.getIdFilial() ||
				linha.getIdDivisaoCliente() != linhaAnterior.getIdDivisaoCliente() ||
				linha.getTpModal() != linhaAnterior.getTpModal() ||
				linha.getTpAbrangencia() != linhaAnterior.getTpAbrangencia() ||
				!linha.getTpDocumentoServico().equals(linhaAnterior.getTpDocumentoServico()) ||
				linha.getIdMoeda() != linhaAnterior.getIdMoeda() ||
				linha.getTpFrete() != linhaAnterior.getTpFrete() ||
				linha.getIdServico() != linhaAnterior.getIdServico() || 				
				//Atendiu o número maximo de registro por fatura
				(linha.getNrQteDocRomaneio() != 0 && linha.getNrQteDocRomaneio() == lstIdDevedoreDocServFat.size()) ||
				//Se o mes mudou e que o parametro blAgrupaFaturamentoMes = true
				((linha.getDhEmissao().getMonthOfYear() != linhaAnterior.getDhEmissao().getMonthOfYear() || linha.getDhEmissao().getYear() != linhaAnterior.getDhEmissao().getYear()) && linha.getBlAgrupaFaturamentoMes()) ||
				//Se o agrupamento especifico
				groupByFatura(linha, linhaAnterior))){
				//Gera a fatura
				//LMS-3910 - teste
				if( !lstIdDevedoreDocServFat.isEmpty() ) {
				generateFatura(param, linhaAnterior, lstIdDevedoreDocServFat);
				}
				//Reinicializa a lista de devedores
				lstIdDevedoreDocServFat = new ArrayList();
			}

			//LMS-3910
			if (devedorDocServFatService.findDevedorSituCobrancaCP(linha.getIdDevedorDocServFat())) {
				lstIdDevedoreDocServFat.add(linha.getIdDevedorDocServFat());				
			}
			
			linhaAnterior = linha;
			batchLogger.warning("[Faturamento automático] Fim processamento do cliente: "  + linha.getIdCliente() + " - " + sdf.format(Calendar.getInstance().getTime()));
		}
		
		//Depois de iterar todos os documentos, gerar a última fatura
		if (lstIdDevedoreDocServFat != null 
				&& !lstIdDevedoreDocServFat.isEmpty()){
			generateFatura(param, linhaAnterior, lstIdDevedoreDocServFat);
		}
		
		return lstLinhas.size();
	}

	/**
	 * Gera a fatura baseado nos parametros de faturamentos e a lista de documentos
	 */
	private void generateFatura(SqlFaturamentoParam param, LinhaSqlFaturamentoParam linhaAnterior, List lstIdDevedoreDocServFat) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		batchLogger.warning("[Faturamento automático] Início generateFatura(): " + sdf.format(Calendar.getInstance().getTime()));
		Fatura fatura = null;

		try {
			fatura = mountFatura(param, linhaAnterior);
			
			//Usa o gerarFatura -> AQUI COMEÇA O USO DE HIBERNATE
			gerarFaturaFaturamentoService.storeFaturaWithIdsDevedorDocServFat(fatura, lstIdDevedoreDocServFat);
			
		} catch (BusinessException e) {
			batchLogger.warning("ERRO -> nrFatura : " + fatura.getNrFatura()+ " idFilial : " + fatura.getFilialByIdFilial().getIdFilial() + " tpFatura : " + fatura.getTpFatura().getValue() + " descrição : " + getFaturamentoDAO().findMensagem(e.getMessageKey(), e.getMessageArguments()));
			if (param.getBlFaturamentoManual()) {
				throw e;
			}
		} catch (Exception e) {			
			logger.error("Erro no faturamento", e);
			batchLogger.warning("ERRO -> " + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		batchLogger.warning("[Faturamento automático] Fim generateFatura(): " + sdf.format(Calendar.getInstance().getTime()));
	}

	/**
	 * Método que aplica as regras de agrupamento por fatura especifico de cada tipo de cliente.
	 * Return 'true' se TEM que criar uma nova fatura
	 * 
	 * @param LinhaSqlFaturamentoParam linhaSql
	 * 
	 * @return Boolean
	 */
	protected abstract boolean groupByFatura(LinhaSqlFaturamentoParam linha, LinhaSqlFaturamentoParam linhaAnterior);
	
	/**
	 * Popula com os agrupamentos especias do cliente, o objeto que representa uma linha de registro da query dos documentos
	 */
	protected abstract void mountLinhaSqlFaturamento(LinhaSqlFaturamentoParam linha, Object[] element) throws SQLException;

	/**
	 * Popula com os dados padrão, o objeto que representa uma linha de registro da query dos documentos
	 */
	protected LinhaSqlFaturamentoParam mountLinhaSqlFaturamento(Object[] element) throws SQLException {
		LinhaSqlFaturamentoParam linha = new LinhaSqlFaturamentoParam();

		if (element[0] != null){
			linha.setIdDevedorDocServFat((Long)element[0]);
		}
		if (element[1] != null){
			linha.setIdCliente((Long)element[1]);
		}
		if (element[2] != null){
			linha.setIdFilial((Long)element[2]);
		}
		if (element[3] != null){
			linha.setIdDivisaoCliente((Long)element[3]);
		}
		if (element[4] != null){
			linha.setTpModal(((String)element[4]).charAt(0));
		}
		if (element[5] != null){
			linha.setTpAbrangencia(((String)element[5]).charAt(0));
		}
		if (element[6] != null){
			linha.setTpDocumentoServico((String)element[6]);
		}
		if (element[7] != null){
			linha.setIdMoeda((Long)element[7]);
		}
		if (element[8] != null){
			linha.setTpFrete(((String)element[8]).charAt(0));
		}
		if (element[9] != null){
			linha.setIdServico((Long)element[9]);
		}		
		if (element[10] != null){
			linha.setDhEmissao(new DateTime(((Date)element[10]).getTime()));
		}
		if (element[11] != null){
			linha.setTpCobranca((String)element[11]);
		}
		if (element[12] != null){
			linha.setIdFilialCobranca((Long)element[12]);
		}
		if (element[13] != null){
			linha.setIdCedente((Long)element[13]);
		}
		if (element[14] != null){
			linha.setNrQteDocRomaneio((Long)element[14]);
		}
		
		if (element[15] != null){
			if (((String)element[15]).equals("S")){
				linha.setBlAgrupaFaturamentoMes(Boolean.TRUE);
			} else {
				linha.setBlAgrupaFaturamentoMes(Boolean.FALSE);
			}
		}
		mountLinhaSqlFaturamento(linha, element);
		
		return linha;
	}
	
	/**
	 * Popula o pojo fatura antes de mandar ele para a classe gerarFatura
	 */
	protected Fatura mountFatura(SqlFaturamentoParam param, LinhaSqlFaturamentoParam linha) throws SQLException{
		Fatura fatura = new Fatura();
		
		Long idFilial = getFaturamentoDAO().findFilialCentralizadora(linha.getIdFilial(), linha.getTpModal(), linha.getTpAbrangencia());		
		
		fatura.setBlGerarBoleto(getBlGerarBoleto(param, linha, idFilial));
		
		fatura.setDtVencimento(param.getDtVencimento());
		
		fatura.setCedente(getCedente(param, linha, idFilial));
		
		fatura.setMoeda(getMoeda(linha));
		
		fatura.setCotacaoMoeda(getCotacaoMoeda(param, linha, idFilial));
		
		fatura.setDtEmissao(param.getDtEmissao());
		
		fatura.setVlCotacaoMoeda(param.getVlCotacaoMoeda());
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(linha.getIdCliente());
		fatura.setCliente(cliente);
		
		Filial filial = new Filial();
		filial.setIdFilial(idFilial);
		fatura.setFilialByIdFilial(filial);
		
		Filial filialCobradora = new Filial();
		filialCobradora.setIdFilial(linha.getIdFilial());
		fatura.setFilialByIdFilialCobradora(filialCobradora);
		
		fatura.setDivisaoCliente(getDivisaoCliente(linha));
		
		if (linha.getTpModal() != 0){
			fatura.setTpModal(new DomainValue(Character.toString(linha.getTpModal())));
		}
		
		if (linha.getTpAbrangencia() != 0){
			fatura.setTpAbrangencia(new DomainValue(Character.toString(linha.getTpAbrangencia())));
		}
		
		if (linha.getTpFrete() != 0){
			fatura.setTpFrete(new DomainValue(Character.toString(linha.getTpFrete())));
		}
		
		fatura.setServico(getServico(linha));
		fatura.setAgrupamentoCliente(getAgrupamentoCliente(param));
		
		String dsValorCampo = null;
		
		//Buscar o tipo de agrupamento a partir do primeiro 'CS' encontrado
		if (param.getLstAgrupador() != null){
			int i = 0;
			boolean blExisteComplemento = false;
			
			while (i < param.getLstAgrupador().size() && blExisteComplemento == false) {
				LinhaSqlGroupByFaturamentoParam linhaGroupBy = (LinhaSqlGroupByFaturamentoParam)param.getLstAgrupador().get(i);
				
				if (linhaGroupBy.getTpCampo().equals("CS")){
					dsValorCampo = (String)linha.getLstAgrupamento()[i];
					blExisteComplemento = true;
				}
				i++;
			}
		}
		
		//Se tem um dominio_agrupamento especial (CS)
		if (StringUtils.isNotBlank(dsValorCampo) && param.getIdTipoAgrupamento()==null){
			fatura.setTipoAgrupamento(getTipoAgrupamento(param.getIdAgrupamentoCliente(), dsValorCampo));
		} else {
			fatura.setTipoAgrupamento(getTipoAgrupamento(param));
		}
		
		return fatura;
	}


	private Boolean getBlGerarBoleto(SqlFaturamentoParam param, LinhaSqlFaturamentoParam linha, Long idFilial) throws SQLException {
		Boolean blGerarBoleto = Boolean.FALSE;				
		if (param.getBlGerarBoleto() != null){
			blGerarBoleto = param.getBlGerarBoleto();			
		} else {
			//Se o blEmititeBoleto = true ou tpCobranca != '1'
			if (getFaturamentoDAO().findBlEmiteBoleto(idFilial) && (linha.getTpCobranca() == null || (linha.getTpCobranca() != null && linha.getTpCobranca().charAt(0) != '1'))){
				blGerarBoleto = Boolean.TRUE;
			}
		}
		return blGerarBoleto;
	}
	
	private Cedente getCedente(SqlFaturamentoParam param, LinhaSqlFaturamentoParam linha, Long idFilial) throws SQLException {
		Cedente cedente = null;
		Long idCedente = null;
		
		//Se o cedente vem do parametro
		if (param.getIdCedente() != null && !param.getIdCedente().equals(Long.valueOf(0))){
			idCedente = param.getIdCedente();
		} else if (!idFilial.equals(linha.getIdFilial())){
			idCedente = getFaturamentoDAO().findIdCedente(idFilial);
		} else if (linha.getIdCedente() != 0){
			idCedente = linha.getIdCedente();
		} else {
			idCedente = getFaturamentoDAO().findIdCedente(linha.getIdFilialCobranca());
		}
		
		if (idCedente != null && idCedente.longValue() != 0){
			cedente = new Cedente();
			cedente.setIdCedente(idCedente);
		}
		
		return cedente;
	}
	
	private Moeda getMoeda(LinhaSqlFaturamentoParam linha) {
		Moeda moeda = new Moeda();		
		moeda.setIdMoeda(linha.getIdMoeda());
		
		return moeda;
	}

	private CotacaoMoeda getCotacaoMoeda(SqlFaturamentoParam param, LinhaSqlFaturamentoParam linha, Long idFilial) throws SQLException {
		Long idCotacaoMoeda = null;
		if (param.getIdCotacaoMoeda() != null){
			idCotacaoMoeda = param.getIdCotacaoMoeda();
		} else if (linha.getTpAbrangencia() == 'I') {
			idCotacaoMoeda = getFaturamentoDAO().findIdCotacaoMoeda(idFilial, linha.getIdMoeda());
		}
		
		if (idCotacaoMoeda != null && idCotacaoMoeda != 0){
			CotacaoMoeda cotacaoMoeda = new CotacaoMoeda();
			cotacaoMoeda.setIdCotacaoMoeda(idCotacaoMoeda);
			return cotacaoMoeda;
		} else if (linha.getTpAbrangencia() == 'I'){
			throw new BusinessException("Documento internacional deve ter cotação.");
		} else {
			return null;
		}
	}
	
	private DivisaoCliente getDivisaoCliente(LinhaSqlFaturamentoParam linha) {
		DivisaoCliente divisaoCliente = null;
		
		if (linha.getIdDivisaoCliente() != 0){
			divisaoCliente = new DivisaoCliente();		
			divisaoCliente.setIdDivisaoCliente(linha.getIdDivisaoCliente());
		}
		
		return divisaoCliente;
	}
	
	private Servico getServico(LinhaSqlFaturamentoParam linha) {
		Servico servico = null;
		if (linha.getIdServico() != 0){
			servico = new Servico();
			servico.setIdServico(linha.getIdServico());
		}
		
		return servico;
	}
	
	private AgrupamentoCliente getAgrupamentoCliente(SqlFaturamentoParam param) {
		AgrupamentoCliente agrupamentoCliente = null;
		if (param.getIdAgrupamentoCliente() != null){
			agrupamentoCliente = new AgrupamentoCliente();
			agrupamentoCliente.setIdAgrupamentoCliente(param.getIdAgrupamentoCliente());
		}
		
		return agrupamentoCliente;
	}
	
	private TipoAgrupamento getTipoAgrupamento(SqlFaturamentoParam param) {
		return mountTipoAgrupamento(param.getIdTipoAgrupamento());
	}
	
	private TipoAgrupamento getTipoAgrupamento(long idAgrupamentoCliente, String cdTipoAgrupamento) throws SQLException {
		long idTipoAgrupamento = getFaturamentoDAO().findIdTipoAgrupamento(idAgrupamentoCliente, cdTipoAgrupamento);
		
		return mountTipoAgrupamento(idTipoAgrupamento);
	}	

	private TipoAgrupamento mountTipoAgrupamento(Long idTipoAgrupamento) {
		TipoAgrupamento tipoAgrupamento = null;
		if (idTipoAgrupamento != null && idTipoAgrupamento != 0){
			tipoAgrupamento = new TipoAgrupamento();
			tipoAgrupamento.setIdTipoAgrupamento(idTipoAgrupamento);
		}
		
		return tipoAgrupamento;
	}
	
	public FaturamentoDAO getFaturamentoDAO() {
		return faturamentoDAO;
	}

	public void setFaturamentoDAO(FaturamentoDAO faturamentoDAO) {
		this.faturamentoDAO = faturamentoDAO;
	}

	public void setGerarFaturaFaturamentoService(
			GerarFaturaFaturamentoService gerarFaturaFaturamentoService) {
		this.gerarFaturaFaturamentoService = gerarFaturaFaturamentoService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
}