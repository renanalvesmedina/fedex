package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.lms.vendas.model.dao.TabelaDivisaoClienteDAO;

public class AtualizacaoAutomaticaService {

	public static final long SERVICO_AEREO = 2L;
	public static final long SERVICO_RODOVIARIO = 1L;
	
	private static final int DESC_DIVISAO_CLIENTE = 1;
	private static final int PRIMEIRA_LINHA = 1;
	private static final int CNPJ = 0;
	private TabelaDivisaoClienteDAO tabelaDivisaoClienteDAO; 
	protected Logger log = LogManager.getLogger(AtualizacaoAutomaticaService.class);

    
	

	public List<Map<String,Object>> executeImportar(String arquivo) {
		List<String> linhas = parseLinhas(arquivo);
		List<Map<String,Object>> linhasNotFound = new ArrayList<Map<String,Object>>();
		List<Long>   idsTabelaDivisaocliente = new ArrayList<Long>();
		
		processFile(linhas, idsTabelaDivisaocliente, linhasNotFound); 

		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < idsTabelaDivisaocliente.size(); i++) {
			ids.add(idsTabelaDivisaocliente.get(i));
			if ( (i+1) % 1000 == 0) { 
				tabelaDivisaoClienteDAO.updateAtualizacaoAutomaticaByNrIdentificacaoAndDivisaoCliente(ids);
				ids = new ArrayList<Long>();
			}
		}
		
		if(CollectionUtils.isNotEmpty(ids)){
			tabelaDivisaoClienteDAO.updateAtualizacaoAutomaticaByNrIdentificacaoAndDivisaoCliente(ids);
		}
		
		return linhasNotFound;
	}

	private void processFile(List<String> linhas, List<Long> idsTabelaDivisaocliente, List<Map<String,Object>> linhasNotFound){
		for (String linha : linhas) {
			Long id = null;
			List<String> fields  = Arrays.asList(linha.split(";"));
			
			if(fields != null && fields.size() > 1){
				id = tabelaDivisaoClienteDAO.findIdTabelaDivisaoclienteByNrIdentificacaoAndDivisaoCliente(fields.get(CNPJ).trim(), fields.get(DESC_DIVISAO_CLIENTE).trim());
			}
			
			if(id == null){
				Map map = new HashMap<String, Object>();
				map.put("fields", linha);
				linhasNotFound.add(map);
			} else {
				idsTabelaDivisaocliente.add(id);
			}
		}
	}


	private List<String> parseLinhas(String arquivo) {
		List<String> linhas = Arrays.asList(arquivo.split("\\r?\\n"));
		return linhas.subList(PRIMEIRA_LINHA,linhas.size());
	}
	
	public void setTabelaDivisaoClienteDAO(TabelaDivisaoClienteDAO tabelaDivisaoClienteDAO) {
		this.tabelaDivisaoClienteDAO = tabelaDivisaoClienteDAO;
	}



	public void executeAtualizacaoManual(List<Long> tipoServico) {
		tabelaDivisaoClienteDAO.executeAtualizacaoManual(tipoServico);
		
	}

	
	public List<Long> findAtualizacaoManual(Long tipoServico){
		return tabelaDivisaoClienteDAO.findAtualizacaoManual(tipoServico);
	}
	
}
