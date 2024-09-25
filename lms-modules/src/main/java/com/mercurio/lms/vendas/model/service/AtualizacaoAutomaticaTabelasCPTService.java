package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.lms.carregamento.model.dao.GerarDadosFrotaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.CptFuncionario;
import com.mercurio.lms.vendas.model.CptVeiculo;

/**
 * Classe responsável por atualizar as tabelas CPT
 * 
 */
@Assynchronous(name = "AtualizacaoAutomaticaTabelasCPTService")
public class AtualizacaoAutomaticaTabelasCPTService {

	private GerarDadosFrotaDAO 		gerarDadosFrotaDAO;
	private CptVeiculoService  		cptVeiculoService;
	private CptFuncionarioService 	cptFuncionarioService;
	
	private BatchLogger batchLogger;

	@AssynchronousMethod( name="atualizacaoAutomaticaTabelasCPTService.storeDadosCPT",
							type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void storeDadosCPT() {
		batchLogger.info("Inicio do processo de atualização das Tabelas CPT");
		
		String crazyVar = StringUtils.rightPad("", 8, ' ');

		Boolean atualizaCPT = Boolean.FALSE;
		
		/**
		 * Atualiza a tabela F1201
		 */
		List<CptVeiculo> listVeiculo = cptVeiculoService.findVigencias();		
		String idCliente = null;
		if(listVeiculo != null && !listVeiculo.isEmpty()){
			for(CptVeiculo cpt : listVeiculo){
				if(cpt.getNrFrota() == null){
					continue;
				}
				atualizaCPT = JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0 
					|| JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0;
				
				if(atualizaCPT){
					if(JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0){
						idCliente = String.valueOf(cpt.getCliente().getIdCliente());
						gerarDadosFrotaDAO.updateFAUNIT(cpt.getNrFrota(), StringUtils.leftPad(idCliente, 8, '0'));					
						cpt.setBlVigenciaInicial(Boolean.TRUE);
					}
					if(JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0){
						gerarDadosFrotaDAO.updateFAUNIT(cpt.getNrFrota(), crazyVar);
						cpt.setBlVigenciaFinal(Boolean.TRUE);
					}
					cptVeiculoService.store(cpt);
				}//atualizaCPT
			}
		}
		
		List<CptFuncionario> listFuncionario = cptFuncionarioService.findVigencias();
		if(listFuncionario != null && !listFuncionario.isEmpty()){
			for(CptFuncionario cpt : listFuncionario){
				if(cpt.getNrMatricula() == null){
					continue;
				}
				atualizaCPT = JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0 
					|| JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0;
				
				if(atualizaCPT){
					if(JTDateTimeUtils.comparaData(cpt.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0){
						idCliente = String.valueOf(cpt.getCliente().getIdCliente());
						gerarDadosFrotaDAO.updateCODCLIDED(cpt.getNrMatricula(), StringUtils.leftPad(idCliente, 8, '0'));
						cpt.setBlVigenciaInicial(Boolean.TRUE);
					}
					if(JTDateTimeUtils.comparaData(cpt.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) <= 0){
						gerarDadosFrotaDAO.updateCODCLIDED(cpt.getNrMatricula(), crazyVar);
						cpt.setBlVigenciaFinal(Boolean.TRUE);
					}				
					cptFuncionarioService.store(cpt);
				}//atualizaCPT
			}	
		}
				
		batchLogger.info("Atualização das tabelas CPT concluída.");		
	}

	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(AtualizacaoAutomaticaTabelasCPTService.class);
	}

	public GerarDadosFrotaDAO getGerarDadosFrotaDAO() {
		return gerarDadosFrotaDAO;
	}

	public void setGerarDadosFrotaDAO(GerarDadosFrotaDAO gerarDadosFrotaDAO) {
		this.gerarDadosFrotaDAO = gerarDadosFrotaDAO;
	}

	public CptVeiculoService getCptVeiculoService() {
		return cptVeiculoService;
	}

	public void setCptVeiculoService(CptVeiculoService cptVeiculoService) {
		this.cptVeiculoService = cptVeiculoService;
	}

	public CptFuncionarioService getCptFuncionarioService() {
		return cptFuncionarioService;
	}

	public void setCptFuncionarioService(CptFuncionarioService cptFuncionarioService) {
		this.cptFuncionarioService = cptFuncionarioService;
	}
	
}