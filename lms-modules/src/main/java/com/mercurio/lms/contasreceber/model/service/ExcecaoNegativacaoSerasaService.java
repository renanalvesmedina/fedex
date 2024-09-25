package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.dto.ProcessaArquivoExcecaoNegativacaoDTO;
import com.mercurio.lms.contasreceber.model.ExcecaoNegativacaoSerasa;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.ExcecaoNegativacaoSerasaDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ExcecaoNegativacaoSerasaService extends CrudService<ExcecaoNegativacaoSerasa, Long> {

	private ExcecaoNegativacaoSerasaDAO excecaoNegativacaoSerasaDAO;
	private FilialService filialService;
	private FaturaService faturaService;
	private ConfiguracoesFacade configuracoesFacade;
	private static final int PRIMEIRA_LINHA = 2;
	
	public List<String> executeImportar(String arquivo){
		List<String> erros = null;
		
		erros = importar(arquivo);
		
		return erros;
	}

	private List<String> importar(String arquivo) {
		List<String> erros = new ArrayList<String>();
		Map<String, String> errosMap = new HashMap<String, String>();
		List<ProcessaArquivoExcecaoNegativacaoDTO> linhas = null;
		try{
			linhas = parse(arquivo, erros, errosMap);	
		}catch(Exception e) {
			erros.add(e.getMessage());
			return erros;
		}
		
		List<String> novosErros = new ArrayList<String>();
		
		Integer i=PRIMEIRA_LINHA;
		Map<Long,Long> clientesInseridos = new HashMap<Long,Long>();
		Map<Long,List<Long>> clientesMunicipios = new HashMap<Long,List<Long>>();
		for(ProcessaArquivoExcecaoNegativacaoDTO entry : linhas){
			if(erros.toString().contains("Linha " + i)) {
				novosErros.add("Linha "+i+": " + errosMap.get("Linha " + i));
				i++;
			} else {
				List<String> errosLinha = processaLinha(entry, i, clientesInseridos, clientesMunicipios);
				novosErros.addAll(errosLinha);
				i++;
			}
		}
		return novosErros;
	}

	private List<ProcessaArquivoExcecaoNegativacaoDTO> parse(String arquivo, List<String> erros, Map<String, String> errosMap) {
		List<ProcessaArquivoExcecaoNegativacaoDTO> result = new ArrayList<ProcessaArquivoExcecaoNegativacaoDTO>();
		List<String> linhas = Arrays.asList(arquivo.split("\\r?\\n"));
		
		linhas = linhas.subList(1,linhas.size());
		
		int linhaNr = PRIMEIRA_LINHA;
		for(String linha : linhas){
			List<String> campos = Arrays.asList(linha.split(";"));
			if (campos.size() >= 4) {
				ProcessaArquivoExcecaoNegativacaoDTO dto = new ProcessaArquivoExcecaoNegativacaoDTO(linha);
				result.add(dto);
			} else {
				String mensagem = configuracoesFacade.getMensagem("LMS-36313");
				erros.add("Linha "+linhaNr+": " + mensagem);
				errosMap.put("Linha "+linhaNr, mensagem);
				result.add(null);
			}
			linhaNr++;
		}
		
		return result;
	}
	
	private List<String> processaLinha(ProcessaArquivoExcecaoNegativacaoDTO entry, Integer index, Map<Long,Long> clientesInseridos, Map<Long, List<Long>> clientesMunicipios) {
		List<String> erros = new ArrayList<String>();
		
		try {
			
			if (entry == null) {
				return erros;
			}
			
			try {
				Long.parseLong(entry.getFatura());
			} catch (Exception e) {
				String mensagem = configuracoesFacade.getMensagem("LMS-36313");
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			try {
				new SimpleDateFormat("dd/MM/yyyy").parse(entry.getDtInicial());
				if (!"".equals(entry.getDtFinal())) {
					new SimpleDateFormat("dd/MM/yyyy").parse(entry.getDtFinal());
				}
			} catch (Exception e) {
				String mensagem = configuracoesFacade.getMensagem("LMS-36313");
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			
			Filial filial = filialService.findBySgFilialAndIdEmpresa(entry.getFilial(), 361L);
			if (filial == null) {
				String mensagem = configuracoesFacade.getMensagem("LMS-36275", new Object[]{entry.getFilial()});
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			long nrFatura = Long.parseLong(entry.getFatura());
			Fatura fatura = faturaService.findFaturaByNrFaturaAndIdFilial(nrFatura, filial.getIdFilial()) ;
			if (fatura == null) {
				String mensagem = configuracoesFacade.getMensagem("LMS-36276", new Object[]{entry.getFilial(), entry.getFatura()});
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			if ("LI".equals(fatura.getTpSituacaoFatura().getValue()) || "CA".equals(fatura.getTpSituacaoFatura().getValue())){
				String mensagem = configuracoesFacade.getMensagem("LMS-36277", new Object[]{entry.getFilial(), entry.getFatura()});
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			TypedFlatMap map = new TypedFlatMap();
			map.put("id_fatura", fatura.getIdFatura());
			List<ExcecaoNegativacaoSerasa> lista = findAll(map);
			if(lista.size() > 0){
				String mensagem = configuracoesFacade.getMensagem("LMS-36314", new Object[]{entry.getFilial(), entry.getFatura()});
				erros.add("Linha "+index+": " + mensagem);
				return erros;
			}
			
			if(erros.size() == 0) {
				ExcecaoNegativacaoSerasa e = new ExcecaoNegativacaoSerasa();
				e.setFatura(fatura);
				e.setDtVigenciaInicial(new YearMonthDay(DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(entry.getDtInicial()).toYearMonthDay()));
				if (!"".equals(entry.getDtFinal())) {
					e.setDtVigenciaFinal(new YearMonthDay(DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(entry.getDtFinal()).toYearMonthDay()));
				}
				e.setObExcecaoNegativacaoSerasa(entry.getObservacao());
				e.setUsuario(SessionUtils.getUsuarioLogado());
				e.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
				
				store(e);
			}
		
		}catch(Exception e) {
			erros.add(e.getMessage());
		}
		
		return erros;
	}
	
	public Serializable store(ExcecaoNegativacaoSerasa entity) {
		return super.store(entity);
	}
	
	public List<ExcecaoNegativacaoSerasa> findAll(TypedFlatMap filtro) {
		return excecaoNegativacaoSerasaDAO.findAll(filtro);
	}
	
	public ExcecaoNegativacaoSerasa findExcecaoById(Long id) {
		return excecaoNegativacaoSerasaDAO.findExcecaoById(id);
	}
	
	public void setExcecoesClienteFinanceiroDAO(ExcecaoNegativacaoSerasaDAO excecaoNegativacaoSerasaDAO) {
		this.excecaoNegativacaoSerasaDAO = excecaoNegativacaoSerasaDAO;
		setDao( excecaoNegativacaoSerasaDAO );
	}
	
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}