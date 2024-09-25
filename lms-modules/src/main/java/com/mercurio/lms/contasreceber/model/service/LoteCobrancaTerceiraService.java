package com.mercurio.lms.contasreceber.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.contasreceber.model.dao.LoteCobrancaTerceiraDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public class LoteCobrancaTerceiraService  extends CrudService<LoteCobrancaTerceira, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private LoteCobrancaTerceiraDAO loteCobrancaTerceiraDAO;
	private ItemLoteCobrancaTerceiraService itemLoteCobrancaTerceiraService;
	private FaturaDAO faturaDao;
	private FilialService filialService;
	private FaturaService faturaService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public void setLoteCobrancaTerceiraDAO(LoteCobrancaTerceiraDAO loteCobrancaTerceiraDAO) {
		setDao(loteCobrancaTerceiraDAO);
		this.loteCobrancaTerceiraDAO = loteCobrancaTerceiraDAO;
	}
	
	public void setItemLoteCobrancaTerceiraService(ItemLoteCobrancaTerceiraService itemLoteCobrancaTerceiraService) {
		this.itemLoteCobrancaTerceiraService = itemLoteCobrancaTerceiraService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public List<LoteCobrancaTerceira> findAll(TypedFlatMap filtro) {
		return loteCobrancaTerceiraDAO.findAll(filtro);
	}
	
	public LoteCobrancaTerceira findLoteCobrancaById(Long id) {
		return loteCobrancaTerceiraDAO.findLoteCobrancaById(id);
	}
	
	public Serializable store(LoteCobrancaTerceira entity) {
		return super.store(entity);
	}
	
	@Override
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }
	
	public void setFaturaDao(FaturaDAO faturaDao) {
		this.faturaDao = faturaDao;
	}

	public LoteCobrancaTerceira storeLotecobranca(LoteCobrancaTerceira lote){
		return loteCobrancaTerceiraDAO.storeLoteCobranca(lote);
	}
	
	String limpaLinha(String linha){
		return linha.replace("\t", "").replace("\n", "");
	}
	
	String geraLinha(Object[] row,LoteCobrancaTerceira lote){
		StringBuffer linha = new StringBuffer();
		for(int i=1; i<row.length; i++){
			linha.append(row[i]);
		}
		faturaDao.updateDhEnvioCobTerceiraFatura((Long) row[0]);
		return limpaLinha(linha.toString());
	}
	
	public int getColunaIndiceByName(String name,String[] cabecalho){
		return getColunaIndiceByName(name,cabecalho,-1);
	}
	public int getColunaIndiceByName(String name,String[] cabecalho,int defaultValue){
		for(int i=0; i < cabecalho.length; i++){
			if ( cabecalho[i].trim().equalsIgnoreCase(name.trim()) ){
				return i;
			}
		}
		return defaultValue;
	}
	
	String getDomainValu(DomainValue d){
		if ( d == null ) return "";
		return d.getValue();
	}
	boolean validaFatura(Fatura fatura, String tpLote) {
		Boolean blDataVencimentoInValida = false;
		if ( fatura.getDtVencimento() != null){
			blDataVencimentoInValida = JTDateTimeUtils.comparaData(fatura.getDtVencimento(), JTDateTimeUtils.getDataAtual()) > 0;
		}
		Boolean blSituacaoInValida = "LI".equals(getDomainValu(fatura.getTpSituacaoFatura())) || "CA".equals(getDomainValu(fatura.getTpSituacaoFatura())) ;
		Boolean tpSituacaoAprovacao = "E".equals(getDomainValu(fatura.getTpSituacaoAprovacao())) || "R".equals(getDomainValu(fatura.getTpSituacaoAprovacao()));
		if ("E".equals(tpLote) && (blSituacaoInValida || blDataVencimentoInValida  || tpSituacaoAprovacao)) {
			return false;
		}
		return true;
	}
	
	public List<String> executeImportaArquivoDevolucao(String arquivo,LoteCobrancaTerceira loteCobrancaTerceira){
		List<String> erros = new ArrayList<String>();
		String[] lines = arquivo.split("\n",-1);
		String[] cabecalho = lines[0].split(";");
		for(int i=1; i < lines.length; i++){
			String line = lines[i];
			int linha = i+1;
			String[] rows = line.split(";");
			if ( rows.length < 15 ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36333"));
				continue;
			}
			String titulo = rows[getColunaIndiceByName("Titulo", cabecalho) ];
			Filial filial = null;
			try{
				filial = filialService.findFilialBySgFilialLegado(titulo.substring(0,2));
			}catch(Exception e){
				filial = null;
			}
			if ( filial == null ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36275",new Object[]{( titulo.length() >=2? titulo.substring(0,2): titulo)}));
				continue;
			}
			if (!isNumber(titulo.substring(3))){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36276",new Object[]{filial.getSgFilial(),titulo.substring(2)}));
				continue;
			}
			Fatura fatura = faturaService.findFaturaByNrFaturaAndIdFilial(Long.valueOf(clearText(titulo.substring(2))), filial.getIdFilial());
			if ( fatura == null ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36276",new Object[]{filial.getSgFilial(),titulo.substring(2)}));
				continue;
			}
			if ( !validaFatura(fatura, loteCobrancaTerceira.getTpLote().getValue()) ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36329",new Object[]{filial.getSgFilial(),titulo.substring(2)}));
				continue;
			}
			
			
			if ( getYearMonthDay(rows[getColunaIndiceByName("Dt. devolucao", cabecalho)]) == null){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36332",new Object[]{linha}));
				continue;
			}
			ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = new ItemLoteCobrancaTerceira();
			itemLoteCobrancaTerceira.setLoteCobrancaTerceira(loteCobrancaTerceira);
			itemLoteCobrancaTerceira.setDsMotivo(rows[getColunaIndiceByName("Motivo da Devolucao", cabecalho)]);
			itemLoteCobrancaTerceira.setNrProcesso(rows[getColunaIndiceByName("Processo", cabecalho)]);
			itemLoteCobrancaTerceira.setDtDevolucao(getYearMonthDay(rows[getColunaIndiceByName("Dt. devolucao", cabecalho)]));
			itemLoteCobrancaTerceira.setFatura(fatura);
			executeStoreLoteCobranca(erros, linha, fatura,itemLoteCobrancaTerceira,filial);
		}
		return erros;
	}

	YearMonthDay getYearMonthDay(String dateString){
		if ( JTDateTimeUtils.formatStringToYearMonthDayWithSeconds(dateString)  != null) 
				return JTDateTimeUtils.formatStringToYearMonthDayWithSeconds(dateString);
		if ( JTDateTimeUtils.formatStringToYearMonthDayWithoutSeconds(dateString)  != null) 
			return JTDateTimeUtils.formatStringToYearMonthDayWithoutSeconds(dateString);
		if ( JTDateTimeUtils.formatStringToYearMonthDayWithoutTime(dateString)  != null) 
			return JTDateTimeUtils.formatStringToYearMonthDayWithoutTime(dateString);
		
		return null;
	}
	private void executeStoreLoteCobranca(List<String> erros, int linha,Fatura fatura, ItemLoteCobrancaTerceira itemLoteCobrancaTerceira,Filial filial) {
			List exists = itemLoteCobrancaTerceiraService.findByFaturaAndLoteCobranda(fatura.getIdFatura(), itemLoteCobrancaTerceira.getLoteCobrancaTerceira().getIdLoteCobrancaTerceira());
			if ( exists != null && !exists.isEmpty() ){
				erros.add("Linha "+linha+": Fatura "+fatura.getNrFatura()+" já inserida para lote cobrança terceira "+itemLoteCobrancaTerceira.getLoteCobrancaTerceira().getIdLoteCobrancaTerceira()+", na filial "+filial.getSgFilial()+".");
				return;
			}
			itemLoteCobrancaTerceiraService.store(itemLoteCobrancaTerceira);
	}
	
	public List<String> executeImportaArquivoPagamento(String arquivo,LoteCobrancaTerceira loteCobrancaTerceira){
		List<String> erros = new ArrayList<String>();
		String[] lines = arquivo.split("\n",-1);
		String[] cabecalho = lines[0].split(";");
		for(int i=1; i < lines.length; i++){
			int linha = i+1;
			String line = lines[i];
			String[] rows = line.split(";");
			if ( rows.length < 24 ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36330",new Object[]{""+linha}));
				continue;
			}
			String titulo = rows[getColunaIndiceByName("Titulo", cabecalho) ];
			Filial filial = null;
			try{
				filial = filialService.findFilialBySgFilialLegado(titulo.substring(0,2));
			}catch(Exception e){
				filial = null;
			}
			if ( filial == null ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36275",new Object[]{( titulo.length() >=2? titulo.substring(0,2): titulo)}));
				continue;
			}
			Fatura fatura = null;
			if ( titulo.length() >= 2 ){
				fatura = faturaService.findFaturaByNrFaturaAndIdFilial(Long.valueOf(clearText(titulo.substring(2))), filial.getIdFilial());
			}
			if ( fatura == null ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36276",new Object[]{filial.getSgFilial(),titulo.substring(2)}));
				continue;
			}
			if ( !validaFatura(fatura, loteCobrancaTerceira.getTpLote().getValue()) ){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36329",new Object[]{filial.getSgFilial(),titulo.substring(2)}));
				continue;
			}
			if ( !isNumber(rows[getColunaIndiceByName("Vl. Pagamento", cabecalho)])
					|| !isNumber(rows[getColunaIndiceByName("Juros", cabecalho)])
					|| !isNumber(rows[getColunaIndiceByName("Protesto", cabecalho)],true)
					|| !isNumber(rows[getColunaIndiceByName("Multa", cabecalho)],true)
					|| !isNumber(rows[getColunaIndiceByName("Contrato", cabecalho)])
					|| !isNumber(rows[getColunaIndiceByName("Vl. Credito", cabecalho)])){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36331",new Object[]{i}));
				continue;
			}
			if ( getYearMonthDay(rows[getColunaIndiceByName("Pagamento", cabecalho)]) == null){
				erros.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36332",new Object[]{linha}));
				continue;
			}
			ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = new ItemLoteCobrancaTerceira();
			itemLoteCobrancaTerceira.setLoteCobrancaTerceira(loteCobrancaTerceira);
			itemLoteCobrancaTerceira.setDsMotivo(rows[getColunaIndiceByName("Tipo Pgto", cabecalho)]);
			itemLoteCobrancaTerceira.setNrProcesso(rows[getColunaIndiceByName("Processo", cabecalho)]);
			itemLoteCobrancaTerceira.setDtPagamento(getYearMonthDay(rows[getColunaIndiceByName("Pagamento", cabecalho)]));
			itemLoteCobrancaTerceira.setVlPagamento(new Double(clearText(rows[getColunaIndiceByName("Vl. Pagamento", cabecalho)])));
			itemLoteCobrancaTerceira.setVlJuros(new Double(clearText(rows[getColunaIndiceByName("Juros", cabecalho)])));
			if ( !clearText(rows[getColunaIndiceByName("Protesto", cabecalho)]).isEmpty() )
				itemLoteCobrancaTerceira.setVlProtesto(new Double(clearText(rows[getColunaIndiceByName("Protesto", cabecalho)])));
			if ( !clearText(rows[getColunaIndiceByName("Multa", cabecalho)]).isEmpty() )
				itemLoteCobrancaTerceira.setVlMulta(new Double(clearText(rows[getColunaIndiceByName("Multa", cabecalho)])));
			itemLoteCobrancaTerceira.setVlCredito(new Double(clearText(rows[getColunaIndiceByName("Vl. Credito", cabecalho)])));
			itemLoteCobrancaTerceira.setVlContrato(new Double(clearText(rows[getColunaIndiceByName("Contrato", cabecalho)])));
			itemLoteCobrancaTerceira.setDsHistorico(rows[getColunaIndiceByName("Historico", cabecalho)]);
			itemLoteCobrancaTerceira.setDsObservacao(rows[getColunaIndiceByName("Observacao", cabecalho)]);
			itemLoteCobrancaTerceira.setFatura(fatura);
			executeStoreLoteCobranca(erros, linha, fatura,itemLoteCobrancaTerceira,filial);
		}
		return erros;
	}
	
	public List<String> executeImportaArquivo(String arquivo,Long id){
		LoteCobrancaTerceira loteCobrancaTerceira = findLoteCobrancaById(id);
		if ( loteCobrancaTerceira.getTpLote().getValue().equalsIgnoreCase("E") ){
			return executeImportaArquivoEnvio(arquivo, loteCobrancaTerceira);
		}else if ( loteCobrancaTerceira.getTpLote().getValue().equalsIgnoreCase("P") ){
			return executeImportaArquivoPagamento(arquivo, loteCobrancaTerceira);
		}else if ( loteCobrancaTerceira.getTpLote().getValue().equalsIgnoreCase("D") ){
			return executeImportaArquivoDevolucao(arquivo, loteCobrancaTerceira);
		}
		return null;
	}
	
	public List<String> executeImportaArquivoEnvio(String arquivo,LoteCobrancaTerceira loteCobrancaTerceira){
		List<String> errors = new ArrayList<String>();
		String[] lines = arquivo.split("\n");

		for(int i=1; i< lines.length;i++){
			int linha = i+1;
			String line = lines[i];
			String[] rows = line.split(";",-1);
			if ( rows.length < 2 || !isNumber(rows[1]) ){
				errors.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36274"));
				continue;
			}
			String sgFilial = rows[0];
			Filial filial = null;
			try{
				if ( sgFilial.length() == 2 ) {
					filial = filialService.findFilialBySgFilialLegado(sgFilial);
				}else if ( !sgFilial.isEmpty() ){
					filial = filialService.findFilial(361L, sgFilial);
				}
			}catch(Exception e){
				filial = null;
			}
			if ( filial == null ){
				errors.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36275",new Object[]{sgFilial}));
				continue;
			}
			Fatura fatura = faturaService.findFaturaByNrFaturaAndIdFilial(Long.valueOf(clearText(rows[1])),filial.getIdFilial());	
			if ( fatura == null ){
				errors.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36276",new Object[]{sgFilial,clearText(rows[1])})); 
				continue;
			}
			if ( !validaFatura(fatura, loteCobrancaTerceira.getTpLote().getValue()) ){
				errors.add("Linha "+linha+": "+configuracoesFacade.getMensagem("LMS-36329",new Object[]{filial.getSgFilial(),clearText(rows[1])}));
				continue;
			}
			ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = new ItemLoteCobrancaTerceira();
			itemLoteCobrancaTerceira.setLoteCobrancaTerceira(loteCobrancaTerceira);
			itemLoteCobrancaTerceira.setFatura(fatura);
			executeStoreLoteCobranca(errors, linha, fatura,itemLoteCobrancaTerceira,filial);
		}
		return errors;
	}
	
	boolean isNumber(String str){
		return isNumber(str,false);
	}
	
	boolean isNumber(String str,boolean ignoreEmpty){
		try{
			str = clearText(str);
			if ( str.isEmpty() ) return ignoreEmpty;
			Double l = new Double(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	private String clearText(String str) {
		str = str.trim();
		if (str.contains(",")){
			str = str.replace(".", "");
			str = str.replace(",", ".");
		}
		return str;
	}
	
	public LoteCobrancaTerceira storeArquivoLoteCobrancaTerceira(Long idLoteCobranca){
		StringWriter file = new StringWriter();
		LoteCobrancaTerceira lote = loteCobrancaTerceiraDAO.findById(idLoteCobranca);
		List<Object[]> arquivos = loteCobrancaTerceiraDAO.geraArquivoLoteCobrancaTerceira(idLoteCobranca);
		for(Object[] row : arquivos){
			file.append(geraLinha(row,lote)+"\n");
		}
		try {
			String nomeArquivo = "LOTE_COBRANCA_"+StringUtils.leftPad(lote.getNrLote(),10,'0')+".txt";
			lote.setDcArquivo(FormatUtils.mountFileInArrayByteASCII(nomeArquivo, FormatUtils.removeAccents(file.toString())));
			file.close();
		} catch (IOException e) {
			log.error(e);
		}
		return lote;
	}
	
	public LoteCobrancaTerceira executeGeraArquivoLoteCobrancaTerceira(Long idLoteCobranca){
		return loteCobrancaTerceiraDAO.storeLoteCobranca(storeArquivoLoteCobrancaTerceira(idLoteCobranca));
	}
	
	public List<Object[]> geraArquivoLoteCobrancaTerceira(Long idLoteCobranca){
		return loteCobrancaTerceiraDAO.geraArquivoLoteCobrancaTerceira(idLoteCobranca);
	}
}
