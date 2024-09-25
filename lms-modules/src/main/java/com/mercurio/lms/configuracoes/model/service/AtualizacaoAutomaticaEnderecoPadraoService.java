package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.OcorrenciaEndereco;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe responsável pela geração do Relatório de Faturas Nacionais
 * 
 * @author Rafael Andrade de Oliveira
 * @since 12/04/2006
 * 
 * @spring.bean id="lms.configuracoes.atualizacaoAutomaticaEnderecoPadraoService"
 */
@Assynchronous
public class AtualizacaoAutomaticaEnderecoPadraoService {

	private BatchLogger batchLogger;
	
	/**
	 * Serviço da classe Pessoa
	 */
	private PessoaService pessoaService;
	
	/**
	 * IoC do Spring para pessoaService
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	/**
	 * Serviço da classe EnderecoPessoa
	 */
	EnderecoPessoaService enderecoPessoaService;
	
	/**
	 * IoC do Spring para enderecoPessoaService
	 */
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * Serviço da classe OcorrenciaEndereco
	 */
	OcorrenciaEnderecoService ocorrenciaEnderecoService;
	
	/**
	 * IoC do Spring para ocorrenciaEnderecoService
	 */
	public void setOcorrenciaEnderecoService(OcorrenciaEnderecoService ocorrenciaEnderecoService) {
		this.ocorrenciaEnderecoService = ocorrenciaEnderecoService;
	}

	/**
	 * Rotina de atualização automática de endereço padrão da pessoa
	 * Responsavel por alterações na tabela PESSOA e OCORRENCIA_PESSOA
	 */
	@AssynchronousMethod( name="configuracoes.AtualizacaoAutomaticaEnderecoPadrao",
							type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void storeEnderecoPadrao() {
		
		batchLogger.info("Inicio do processo de atualização de endereços.");
		/* Busca pessoas com endereço padrão fora de vigencia (dt_vigencia_final <= data_atual) */
		List lista = pessoaService.findPessoasComEnderecoForaVigencia();

		batchLogger.info("Consulta por endereços fora da vigência retornou "+lista.size()+" registro(s).");
		
		final DomainValue NAO_ATUALIZADO = new DomainValue("N");
		final DomainValue ATUALIZADO = new DomainValue("A");
		final YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		
		/* Itera a lista caso tenha algum elemento */
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
		
			/* Pega a pessoa na pilha da lista */
			Pessoa pessoa = (Pessoa) iter.next();
			
			/* Busca o endereço padrão da pessoa */
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(pessoa.getIdPessoa(), dataAtual);

			/* Seta o endereço padrão da pessoa na pessoa */
			pessoa.setEnderecoPessoa(enderecoPessoa);
			
			// Atualiza UF da Inscrição estadual
			enderecoPessoaService.atualizaUfInscricaoEstadual(enderecoPessoa, pessoa);		
			
			/* Atualiza na tabela PESSOA (caso nao tenha grava null mesmo) */
			pessoaService.store(pessoa);

			OcorrenciaEndereco ocorrenciaEndereco = new OcorrenciaEndereco();

			/* Seta data atual da ocorrência */ 
			ocorrenciaEndereco.setDtOcorrencia(dataAtual);

			/* Seta pessoa da ocorrência */ 
			ocorrenciaEndereco.setPessoa(pessoa);
			
			/* Seta tipo da ocorrência conforme regra */
			if (enderecoPessoa == null) {
				/* Domínio: "Não atualizado" */
				ocorrenciaEndereco.setTpOcorrencia(NAO_ATUALIZADO);
			} else {
				/* Domínio: "Atualizado" */
				ocorrenciaEndereco.setTpOcorrencia(ATUALIZADO);
			}

			/* Grava na tabela OCORRENCIA_ENDERECO */ 
			ocorrenciaEnderecoService.store(ocorrenciaEndereco);
			
		}

		batchLogger.info("Atualização de endereços concluída.");
		
	}

	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(AtualizacaoAutomaticaEnderecoPadraoService.class);
	}
	
}