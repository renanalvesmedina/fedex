package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.JobInterfaceService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistTrocaFilialCliente;
import com.mercurio.lms.municipios.model.HistoricoTrocaFilial;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem;
import com.mercurio.lms.municipios.model.MunicipioFilialFilOrigem;
import com.mercurio.lms.municipios.model.MunicipioFilialIntervCep;
import com.mercurio.lms.municipios.model.MunicipioFilialSegmento;
import com.mercurio.lms.municipios.model.MunicipioFilialUFOrigem;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 *
 * @spring.bean id="lms.municipios.atualizarTrocaFiliaisService"
 */
@Assynchronous
public class AtualizarTrocaFiliaisService {
	
	private BatchLogger batchLogger;
	
	private MunicipioFilialService municipioFilialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private HistoricoTrocaFilialService historicoTrocaFilialService;
	private ClienteService clienteService;
	private EnderecoPessoaService enderecoPessoaService;
	private HistTrocaFilialClienteService histTrocaFilialClienteService;
	
	private MunicipioFilialIntervCepService municipioFilialIntervCepService;
	private MunicipioFilialFilOrigemService municipioFilialFilOrigemService;
	private MunicipioFilialUFOrigemService municipioFilialUFOrigemService;
	private MunicipioFilialSegmentoService municipioFilialSegmentoService;
	private MunicipioFilialCliOrigemService municipioFilialCliOrigemService;
	private JobInterfaceService jobService;
	
	public void setBatchLogger(BatchLogger batchLogger){
		batchLogger.logClass(this.getClass());
		this.batchLogger = batchLogger;
	}
	
	/**
	 * Executa a atualizacao do atendimento do municipio
	 * @param idMunicipioFilialAtual
	 * @param municipioFilial
	 */
	public void execute(Long idMunicipioFilialAtual, MunicipioFilial municipioFilial){
		 
		//Verifica se a data informada é menor ou igual a data atual, se for lança exception
		if (municipioFilial.getDtVigenciaInicial().compareTo(JTDateTimeUtils.getDataAtual()) <= 0){
			throw new BusinessException("LMS-29168");
		}
		
		MunicipioFilial municipioFilialAtual = findMunicipioFilialAtual(idMunicipioFilialAtual);
				
		/* Regra 1 */
		if (verificaFiliais(municipioFilialAtual.getFilial().getIdFilial(), municipioFilial.getFilial().getIdFilial()))
			throw new BusinessException("LMS-29071");
		
		/* Regra 2 */
		if (verificaExisteAtendimentoFuturo(municipioFilial.getMunicipio().getIdMunicipio(), municipioFilial.getFilial().getIdFilial()))
			throw new BusinessException("LMS-29072");
		
		/*Regra 3 */
			if (verificaExisteRotaColetaEntrega(municipioFilialAtual.getFilial().getIdFilial(), municipioFilial.getMunicipio().getIdMunicipio(), municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29073");
	
		/* Regra 5 */
		atualizaDtVigenciaFinalMunicipioFilial(municipioFilialAtual, municipioFilial.getDtVigenciaInicial());
					
		/* Regra 4 */
		municipioFilial = storeMunicipioFilial(municipioFilial);
	
		/* Regra 6 */
		atualizaRegistrosVigentes(idMunicipioFilialAtual, municipioFilial);
		
		/* Regra 7 */
		atualizaVigenciaOperacaoServicoLocaliza(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
	
		/* Regra 8 */
		Long idHistoricoTrocaFilial = criaHistoricoTrocaFilial(municipioFilialAtual, municipioFilial);
		
		/* Regra 9 */
		geraHistoricoClientesAtendidos(municipioFilialAtual.getFilial().getIdFilial(), municipioFilial.getMunicipio().getIdMunicipio(), idHistoricoTrocaFilial);
		
		/* Programa o agendamento da nova data de Atendimento para o Municipio informada na tela */
		executeAgendamentoAtendimentoMunicipio(municipioFilial.getDtVigenciaInicial());
		
	}

	private MunicipioFilial findMunicipioFilialAtual(Long idMunicipioFilial){
		return municipioFilialService.findById(idMunicipioFilial);
	}
	
	/**
	 * Regra 1. Verifica se a Filial atual é igual a filial nova
	 * @param idMunicipioFilialAtual
	 * @param idMunicipioFilial
	 * @return TRUE se a filial atual é igual a filial nova, FALSE caso contrario
	 */
	private boolean verificaFiliais(Long idFilialAtual, Long idFilial){
						
		return (idFilialAtual.equals(idFilial));
	}
	
	/**
	 * Regra 2. Verifica se existe algum atendimento para o municipio X filial numa vigencia futura
	 * @param idMunicipio
	 * @param idFilial
	 * @return TRUE se existe atendimento, FALSE caso contrario 
	 */
	private boolean verificaExisteAtendimentoFuturo(Long idMunicipio, Long idFilial){
		return municipioFilialService.verificaExisteMunicipioFilialVigenciaFutura(idMunicipio, idFilial);
	}
	
	/**
	 * Regra 3. Verifica se existe alguma rota de coleta/entrega para a filial atual na mesma vigencia informada e 
	 * 			associada ao municipio informado
	 * @param idFilial
	 * @param idMunicipio
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return TRUE se existe alguma rota, FALSE caso contrario
	 */
	private boolean verificaExisteRotaColetaEntrega(Long idFilial, Long idMunicipio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return rotaColetaEntregaService.verificaExisteRotaColetaEntrega(idFilial, idMunicipio, dtVigenciaInicial, dtVigenciaFinal);
	}
	
	/**
	 * Regra 4. Cria um registro para o Municipio X Filial novo
	 * @param municipioFilial
	 * @return bean do municipio-filial criado
	 */
	private MunicipioFilial storeMunicipioFilial(MunicipioFilial municipioFilial){
				
		municipioFilialService.store(municipioFilial);
		return municipioFilial;
	}
	
	/**
	 * Regra 5. Atualiza a data de vigencia final do municipioXfilial atual com (data de vigencia inicial nova - 1)
	 * 
	 * @param idMunicipioFilial
	 * @param dtVigencia
	 */ 
	private void atualizaDtVigenciaFinalMunicipioFilial(MunicipioFilial municipioFilial, YearMonthDay dtVigencia){
		
		if (municipioFilial.getDtVigenciaInicial().compareTo(dtVigencia.minusDays(1)) > 0)
			throw new BusinessException("LMS-29090");
		
		if (municipioFilial.getDtVigenciaFinal() != null){
			if (municipioFilial.getDtVigenciaFinal().compareTo(dtVigencia.minusDays(1)) > 0)
				throw new BusinessException("LMS-29090");
		}	
		
		municipioFilial.setDtVigenciaFinal(dtVigencia.minusDays(1));
		
		municipioFilialService.store(municipioFilial);
	}

	/**
	 * Regra 6. Verifica se existem registros vigentes, e se existirem, finaliza a vigencia com (data de vigencia inicial nova - 1)
	 * 			das seguintes entidades:
	 * 			- MUNICIPIO_FILIAL_INTERV_CEP
	 * 			- MUNICIPIO_FILIAL_FIL_ORIGEM
	 * 			- MUNICIPIO_FILIAL_UF_ORIGEM
	 * 			- MUNICIPIO_FILIAL_SEGMENTO
	 * 			- MUNICIPIO_FILIAL_CLI_ORIGEM
	 * @param idMunicipioFilial
	 * @param dtVigencia
	 */
	private void atualizaRegistrosVigentes(Long idMunicipioFilialAtual, MunicipioFilial municipioFilial){
				
		YearMonthDay novaDtVigenciaFinal = municipioFilial.getDtVigenciaInicial().minusDays(1);		
						
		//MUNICIPIO_FILIAL_INTERV_CEP
		List result = municipioFilialIntervCepService.findIntervCepVigenteByMunicipioFilial(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
		for (Iterator itResult = result.iterator(); itResult.hasNext();){
			MunicipioFilialIntervCep intervCep = (MunicipioFilialIntervCep) itResult.next();
			
			if (intervCep.getDtVigenciaInicial().compareTo(novaDtVigenciaFinal) >= 0
					|| (intervCep.getDtVigenciaFinal() != null && intervCep.getDtVigenciaFinal().compareTo(novaDtVigenciaFinal) >= 0))
				throw new BusinessException("LMS-29091");
			
			MunicipioFilialIntervCep novoIntervCep = new MunicipioFilialIntervCep();
			
			novoIntervCep.setIdMunicipioFilialIntervCep(null);
			novoIntervCep.setMunicipioFilial(municipioFilial);
			novoIntervCep.setDtVigenciaFinal(municipioFilial.getDtVigenciaFinal());
			novoIntervCep.setDtVigenciaInicial(municipioFilial.getDtVigenciaInicial());
			novoIntervCep.setNrCepFinal(intervCep.getNrCepFinal());
			novoIntervCep.setNrCepInicial(intervCep.getNrCepInicial());	
			
			intervCep.setDtVigenciaFinal(novaDtVigenciaFinal);
			
			municipioFilialIntervCepService.store(intervCep);			
			municipioFilialIntervCepService.store(novoIntervCep);
		}
		
		//MUNICIPIO_FILIAL_FIL_ORIGEM
		result = municipioFilialFilOrigemService.findFilOrigemVigenteByMunicipioFilial(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
		for (Iterator itResult = result.iterator(); itResult.hasNext();){
			MunicipioFilialFilOrigem filOrigem = (MunicipioFilialFilOrigem) itResult.next();
			
			if (filOrigem.getDtVigenciaInicial().compareTo(novaDtVigenciaFinal) >= 0
					|| (filOrigem.getDtVigenciaFinal() != null && filOrigem.getDtVigenciaFinal().compareTo(novaDtVigenciaFinal) >= 0))
				throw new BusinessException("LMS-29091");
			
			MunicipioFilialFilOrigem novoFilOrigem = new MunicipioFilialFilOrigem();
			
			novoFilOrigem.setIdMunicipioFilialFilOrigem(null);
			novoFilOrigem.setFilial(filOrigem.getFilial());
			novoFilOrigem.setMunicipioFilial(municipioFilial);
			novoFilOrigem.setDtVigenciaFinal(municipioFilial.getDtVigenciaFinal());
			novoFilOrigem.setDtVigenciaInicial(municipioFilial.getDtVigenciaInicial());
			
			filOrigem.setDtVigenciaFinal(novaDtVigenciaFinal);
			
			municipioFilialFilOrigemService.store(filOrigem);			
			municipioFilialFilOrigemService.store(novoFilOrigem);
		}

		//MUNICIPIO_FILIAL_UF_ORIGEM
		result = municipioFilialUFOrigemService.findUFOrigemByMunicipioFilial(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
		for (Iterator itResult = result.iterator(); itResult.hasNext();){
			MunicipioFilialUFOrigem ufOrigem = (MunicipioFilialUFOrigem) itResult.next();
			
			if (ufOrigem.getDtVigenciaInicial().compareTo(novaDtVigenciaFinal) >= 0
					|| (ufOrigem.getDtVigenciaFinal() != null && ufOrigem.getDtVigenciaFinal().compareTo(novaDtVigenciaFinal) >= 0))
				throw new BusinessException("LMS-29091");
			
			MunicipioFilialUFOrigem novoUfOrigem = new MunicipioFilialUFOrigem();
						
			novoUfOrigem.setIdMunicipioFilialUFOrigem(null);
			novoUfOrigem.setUnidadeFederativa(ufOrigem.getUnidadeFederativa());
			novoUfOrigem.setMunicipioFilial(municipioFilial);
			novoUfOrigem.setDtVigenciaFinal(municipioFilial.getDtVigenciaFinal());
			novoUfOrigem.setDtVigenciaInicial(municipioFilial.getDtVigenciaInicial());
			
			ufOrigem.setDtVigenciaFinal(novaDtVigenciaFinal);
			
			municipioFilialUFOrigemService.store(ufOrigem);			
			municipioFilialUFOrigemService.store(novoUfOrigem);
		}

		//MUNICIPIO_FILIAL_SEGMENTO
		result = municipioFilialSegmentoService.findSegmentoVigenteByMunicipioFilial(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
		for (Iterator itResult = result.iterator(); itResult.hasNext();){
			MunicipioFilialSegmento segmento = (MunicipioFilialSegmento) itResult.next();
			
			if (segmento.getDtVigenciaInicial().compareTo(novaDtVigenciaFinal) >= 0
					|| (segmento.getDtVigenciaFinal() != null && segmento.getDtVigenciaFinal().compareTo(novaDtVigenciaFinal) >= 0))
				throw new BusinessException("LMS-29091");
			
			MunicipioFilialSegmento novoSegmento= new MunicipioFilialSegmento();
									
			novoSegmento.setIdMunicipioFilialSegmento(null);
			novoSegmento.setSegmentoMercado(segmento.getSegmentoMercado());
			novoSegmento.setMunicipioFilial(municipioFilial);
			novoSegmento.setDtVigenciaFinal(municipioFilial.getDtVigenciaFinal());
			novoSegmento.setDtVigenciaInicial(municipioFilial.getDtVigenciaInicial());
			
			segmento.setDtVigenciaFinal(novaDtVigenciaFinal);
			
			municipioFilialSegmentoService.store(segmento);			
			municipioFilialSegmentoService.store(novoSegmento);
		}
		
		//MUNICIPIO_FILIAL_CLI_ORIGEM
		result = municipioFilialCliOrigemService.findFilialCliVigenteByMunicipioFilial(idMunicipioFilialAtual, municipioFilial.getDtVigenciaInicial(), municipioFilial.getDtVigenciaFinal());
		for (Iterator itResult = result.iterator(); itResult.hasNext();){
			MunicipioFilialCliOrigem cliOrigem = (MunicipioFilialCliOrigem) itResult.next();
			
			if (cliOrigem.getDtVigenciaInicial().compareTo(novaDtVigenciaFinal) >= 0
					|| (cliOrigem.getDtVigenciaFinal() != null && cliOrigem.getDtVigenciaFinal().compareTo(novaDtVigenciaFinal) >= 0))
				throw new BusinessException("LMS-29091");
			
			MunicipioFilialCliOrigem novoCliOrigem = new MunicipioFilialCliOrigem();
									
			novoCliOrigem.setIdMunicipioFilialCliOrigem(null);
			novoCliOrigem.setCliente(cliOrigem.getCliente());
			novoCliOrigem.setMunicipioFilial(municipioFilial);
			novoCliOrigem.setDtVigenciaFinal(municipioFilial.getDtVigenciaFinal());
			novoCliOrigem.setDtVigenciaInicial(municipioFilial.getDtVigenciaInicial());
			
			cliOrigem.setDtVigenciaFinal(novaDtVigenciaFinal);
			
			municipioFilialCliOrigemService.store(cliOrigem);			
			municipioFilialCliOrigemService.store(novoCliOrigem);
		}
	}
	
	
	/**
	 * Regra 7. Verifica se existe registro vigente na tabela OPERACAO_SERVICO_LOCALIZACAO, 
	 * 			neste caso, finaliza a vigencia com (data de vigencia inicial - 1)
	 * @param idMunicipioFilial
	 * @param dtVigencia
	 */
	private void atualizaVigenciaOperacaoServicoLocaliza(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		
		OperacaoServicoLocaliza operacaoServicoLocaliza =  operacaoServicoLocalizaService.findOperacaoServicoPorMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
		 
		if (operacaoServicoLocaliza != null){			
			 
			// Os tempos de entrega e coleta devem ser convertidos para horas, pois o beforeStore da service de Operacao X Servico X Localizacao
			//multiplica esses valores por 60 antes de fazer o update.
			if (operacaoServicoLocaliza.getNrTempoColeta() != null)
				operacaoServicoLocaliza.setNrTempoColeta(Long.valueOf(operacaoServicoLocaliza.getNrTempoColeta().longValue() / 60));
			
			if (operacaoServicoLocaliza.getNrTempoEntrega() != null)
				operacaoServicoLocaliza.setNrTempoEntrega(Long.valueOf(operacaoServicoLocaliza.getNrTempoEntrega().longValue() / 60));
						
			if (operacaoServicoLocaliza.getDtVigenciaInicial().compareTo(dtVigenciaInicial) >= 0
					|| (operacaoServicoLocaliza.getDtVigenciaFinal() != null && operacaoServicoLocaliza.getDtVigenciaFinal().compareTo(dtVigenciaInicial) >= 0))
				throw new BusinessException("LMS-29130");
			
			operacaoServicoLocaliza.setDtVigenciaFinal(dtVigenciaInicial.minusDays(1));
			operacaoServicoLocalizaService.store(operacaoServicoLocaliza);
		}
	}
	
	/**
	 * Regra 8. Cria um registro na tabela HISTORICO_TROCA_FILIAL
	 * @param idMunicipioFilialAtual
	 * @param idMunicipioFilialNova
	 * @return o id do registro criado
	 */
	private Long criaHistoricoTrocaFilial(MunicipioFilial municipioFilialAtual, MunicipioFilial municipioFilialNova){
				
		HistoricoTrocaFilial historicoTrocaFilial = new HistoricoTrocaFilial();
		historicoTrocaFilial.setIdHistoricoTrocaFilial(null);
		historicoTrocaFilial.setDtInclusao(municipioFilialNova.getDtVigenciaInicial());
		historicoTrocaFilial.setMunicipioFilialByIdMunicipioFilial(municipioFilialAtual);
		historicoTrocaFilial.setMunicipioFilialByIdMunicipioFilialTroca(municipioFilialNova);
		
		return (Long) historicoTrocaFilialService.store(historicoTrocaFilial);
		
	}
	
	/**
	 * Regra 9. Cria registro na tabela HIST_TROCA_FILIAL_CLIENTE para cada cliente do municipio atendido pela nova filial
	 * @param idFilial
	 * @param idMunicipio
	 * @param idOperacaoServicoLocaliza
	 */
	public void geraHistoricoClientesAtendidos(Long idFilial, Long idMunicipio, Long idHistoricoTrocaFilial){
		List clientes = clienteService.findClientesByEnderecoVigente(idMunicipio);
		
		for (Iterator itClientes = clientes.iterator(); itClientes.hasNext();){
			Cliente cliente = (Cliente) itClientes.next();
			EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao(cliente.getIdCliente());
			if (enderecoPessoaPadrao != null){
				if (enderecoPessoaPadrao.getMunicipio().getIdMunicipio().equals(idMunicipio)){
					HistTrocaFilialCliente historicoTroca = new HistTrocaFilialCliente();
					historicoTroca.setIdHistTrocaFilialCliente(null);
					historicoTroca.setCliente(cliente);
					HistoricoTrocaFilial historicoTrocaFilial = new HistoricoTrocaFilial();
					historicoTrocaFilial.setIdHistoricoTrocaFilial(idHistoricoTrocaFilial);
					historicoTroca.setHistoricoTrocaFilial(historicoTrocaFilial);
					
					if (cliente.getBlCobrancaCentralizada().booleanValue() == false &&
						cliente.getFilialByIdFilialCobranca().getIdFilial().equals(idFilial)) {
							historicoTroca.setBlFilialCobranca(Boolean.TRUE);
					} else {
							historicoTroca.setBlFilialCobranca(Boolean.FALSE);
					}
					
					if (cliente.getFilialByIdFilialAtendeOperacional().getIdFilial().equals(idFilial)) {
						historicoTroca.setBlFilialResponsavel(Boolean.TRUE);
					} else {
						historicoTroca.setBlFilialResponsavel(Boolean.FALSE);
					}
					
					if (cliente.getFilialByIdFilialAtendeComercial().getIdFilial().equals(idFilial)) {
						historicoTroca.setBlFilialComercial(Boolean.TRUE);
					} else {
						historicoTroca.setBlFilialComercial(Boolean.FALSE);
					}
					
					histTrocaFilialClienteService.store(historicoTroca);					
				}
			}
		}
		
	}
	
				
	/**
	 * Regra 1. Verifica se existe registro vigente na tabela OPERACAO_SERVICO_LOCALIZA
	 * @param idMunicipioFilial
	 */
	private void verificaExisteOperacaoServico(Long idMunicipioFilial){
		if (!operacaoServicoLocalizaService.verificaExisteOperacaoServicoParaFilialTroca(idMunicipioFilial))
			throw new BusinessException("LMS-29074");
	}
	
	/**
	 * Regra 3. Atualiza a filial de cobranca dos clientes afetados pela mudanca do atendimento
	 * @param histTrocaFilialCliente
	 * @param historicoTrocaFilial
	 */
	private void atualizarFilialCobranca(HistTrocaFilialCliente histTrocaFilialCliente, HistoricoTrocaFilial historicoTrocaFilial){
		
		if (histTrocaFilialCliente.getBlFilialCobranca().booleanValue()){
			
			Cliente cliente = clienteService.findByIdInitLazyProperties(histTrocaFilialCliente.getCliente().getIdCliente(), false);						
			Filial filial = historicoTrocaFilial.getMunicipioFilialByIdMunicipioFilialTroca().getFilial();
			cliente.setFilialByIdFilialCobranca(filial);
						
			clienteService.store(cliente);
			
		}
			
	}
	
	/**
	 * Regra 4. Atualiza a filial operacional dos clientes afetados pela mudanca do atendimento
	 * @param histTrocaFilialCliente
	 * @param historicoTrocaFilial
	 */
	private void atualizarFilialAtendimento(HistTrocaFilialCliente histTrocaFilialCliente, HistoricoTrocaFilial historicoTrocaFilial){
		
		if (histTrocaFilialCliente.getBlFilialResponsavel().booleanValue()){
			
			Cliente cliente = clienteService.findByIdInitLazyProperties(histTrocaFilialCliente.getCliente().getIdCliente(), false);						
			Filial filial = historicoTrocaFilial.getMunicipioFilialByIdMunicipioFilialTroca().getFilial();
			cliente.setFilialByIdFilialAtendeOperacional(filial);
						
			clienteService.store(cliente);
			
		}
			
	}
	
	/**
	 * Regra 5. Atualiza a filial comercial dos clientes afetados pela mudanca do atendimento
	 * @param histTrocaFilialCliente
	 * @param historicoTrocaFilial
	 */
	private void atualizarFilialComercial(HistTrocaFilialCliente histTrocaFilialCliente, HistoricoTrocaFilial historicoTrocaFilial){
		
		if (histTrocaFilialCliente.getBlFilialComercial().booleanValue()){
			
			Cliente cliente = clienteService.findById(histTrocaFilialCliente.getCliente().getIdCliente());						
			Filial filial = historicoTrocaFilial.getMunicipioFilialByIdMunicipioFilialTroca().getFilial();
			cliente.setFilialByIdFilialAtendeComercial(filial);
						
			clienteService.store(cliente);
			
		}
			
	}
	
	private void deleteHistoricoTrocaFilialCliente(List historicos){				
		histTrocaFilialClienteService.removeByIds(historicos);
	}
	
	private void deleteHistoricoTrocaFilial(Long idHistoricoTrocaFilial){
		historicoTrocaFilialService.removeById(idHistoricoTrocaFilial);
	} 
	
	
	/**
	 * Rotina utilizado para programar o agendamento para início do atendimento pelo município.
	 * 
	 * @author Andresa Vargas
	 * @param  dataInicioAtendimento
	 */
	private void executeAgendamentoAtendimentoMunicipio(YearMonthDay dataInicioAtendimento) {

		//calculo da data
		// 0 0 1 13 11 ? 2005
		
		DateTime dataAgendamento = JTDateTimeUtils.yearMonthDayToDateTime(dataInicioAtendimento);
		 
		//Gera a expressão cron
		String cronExpression = String.format("0 %d %d %d %d ? %d",
										dataAgendamento.minuteOfHour().get(), 
										dataAgendamento.hourOfDay().get(),
										dataAgendamento.getDayOfMonth(), 
										dataAgendamento.getMonthOfYear(), 
										dataAgendamento.getYear());

		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		jobService.schedule("municipios.AtualizarTrocaFiliais",
							"Atualizar Atendimento Municipio", 
							cronExpression, 
							new Serializable[]{dataInicioAtendimento}, 
							usuarioLogado.getLogin(),
							Collections.EMPTY_SET);
	}
	
	
	/**
	 * Rotina chamada pelo Quartz para execução de tarefas agendadas
	 * @param 
	 */
	@AssynchronousMethod( name="municipios.AtualizarTrocaFiliais", 
							type=BatchType.BIZZ_BATCH_SERVICE)
	public void executeJobAtendimentoMunicipio(YearMonthDay dtInclusao) {
		batchLogger.info("Inicio executeJobAtendimentoMunicipio");
		List idsHistoricoTrocaFilial = historicoTrocaFilialService.findHistoricoTrocaFilialDataAtual(dtInclusao);
		if (idsHistoricoTrocaFilial != null) {
			batchLogger.info("Varrer idsHistoricoTrocaFilial");
			for (Iterator iter = idsHistoricoTrocaFilial.iterator(); iter.hasNext();) {
				batchLogger.info("Varrendo idsHistoricoTrocaFilial");
				Long idHistoricoTrocaFilial = (Long) iter.next();
				aprovarInicioAtendimentoMunicipio(idHistoricoTrocaFilial);
				batchLogger.info("aprovarInicioAtendimentoMunicipio idHistoricoTrocaFilial="+idHistoricoTrocaFilial);
			}
		}
		batchLogger.info("Fim executeJobAtendimentoMunicipio");
	}
	
	private void aprovarInicioAtendimentoMunicipio(Long idHistoricoTrocaFilial){
		HistoricoTrocaFilial historicoTrocaFilial = historicoTrocaFilialService.findByIdDetalhadoFilial(idHistoricoTrocaFilial);
		
		//Regra 1
		verificaExisteOperacaoServico(historicoTrocaFilial.getMunicipioFilialByIdMunicipioFilialTroca().getIdMunicipioFilial());
		
		Map criteria = new HashMap();
		ReflectionUtils.setNestedBeanPropertyValue(criteria, "historicoTrocaFilial.idHistoricoTrocaFilial", idHistoricoTrocaFilial);
		
		List histTrocaFilialClientes = histTrocaFilialClienteService.find(criteria);
		List idsHistTrocaFilialClientes = new ArrayList();
			
		for(Iterator it = histTrocaFilialClientes.iterator(); it.hasNext();){
			
			HistTrocaFilialCliente histTrocaFilialCliente = (HistTrocaFilialCliente) it.next();
			idsHistTrocaFilialClientes.add(histTrocaFilialCliente.getIdHistTrocaFilialCliente());
												
			//Regra 3
			atualizarFilialCobranca(histTrocaFilialCliente, historicoTrocaFilial);
			
			//Regra 4
			atualizarFilialAtendimento(histTrocaFilialCliente, historicoTrocaFilial);		
			
			//Regra 5			
			atualizarFilialComercial(histTrocaFilialCliente, historicoTrocaFilial);
						
		}
		deleteHistoricoTrocaFilialCliente(idsHistTrocaFilialClientes);
		deleteHistoricoTrocaFilial(idHistoricoTrocaFilial);
		
	}
	
	//##################################### SETTER ########################################//

	/**
	 * @param municipioFilialService The municipioFilialService to set.
	 */
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	/**
	 * @param operacaoServicoLocalizaService The operacaoServicoLocalizaService to set.
	 */
	public void setOperacaoServicoLocalizaService(OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	/**
	 * @param historicoTrocaFilialService The historicoTrocaFilialService to set.
	 */
	public void setHistoricoTrocaFilialService(HistoricoTrocaFilialService historicoTrocaFilialService) {
		this.historicoTrocaFilialService = historicoTrocaFilialService;
	}

	/**
	 * @param municipioFilialIntervCepService The municipioFilialIntervCepService to set.
	 */
	public void setMunicipioFilialIntervCepService(MunicipioFilialIntervCepService municipioFilialIntervCepService) {
		this.municipioFilialIntervCepService = municipioFilialIntervCepService;
	}

	/**
	 * @param municipioFilialFilOrigemService The municipioFilialFilOrigemService to set.
	 */
	public void setMunicipioFilialFilOrigemService(MunicipioFilialFilOrigemService municipioFilialFilOrigemService) {
		this.municipioFilialFilOrigemService = municipioFilialFilOrigemService;
	}

	/**
	 * @param municipioFilialUFOrigemService The municipioFilialUFOrigemService to set.
	 */
	public void setMunicipioFilialUFOrigemService(MunicipioFilialUFOrigemService municipioFilialUFOrigemService) {
		this.municipioFilialUFOrigemService = municipioFilialUFOrigemService;
	}

	/**
	 * @param municipioFilialSegmentoService The municipioFilialSegmentoService to set.
	 */
	public void setMunicipioFilialSegmentoService(MunicipioFilialSegmentoService municipioFilialSegmentoService) {
		this.municipioFilialSegmentoService = municipioFilialSegmentoService;
	}

	/**
	 * @param municipioFilialCliOrigemService The municipioFilialCliOrigemService to set.
	 */
	public void setMunicipioFilialCliOrigemService(MunicipioFilialCliOrigemService municipioFilialCliOrigemService) {
		this.municipioFilialCliOrigemService = municipioFilialCliOrigemService;
	}

	/**
	 * @param clienteService The clienteService to set.
	 */
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/**
	 * @param enderecoPessoaService The enderecoPessoaService to set.
	 */
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * @param histTrocaFilialClienteService The histTrocaFilialClienteService to set.
	 */
	public void setHistTrocaFilialClienteService(HistTrocaFilialClienteService histTrocaFilialClienteService) {
		this.histTrocaFilialClienteService = histTrocaFilialClienteService;
	}

	public void setJobService(JobInterfaceService jobService) {
		this.jobService = jobService;
	}
	
}
