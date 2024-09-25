package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.mercurio.adsm.batch.job.JobInfo;

public interface JobInterfaceService {

	/**
	 * Retorna as datas das próximas n execuções de uma expressão de acordo com
	 * a expressao da cron informada e com a data atual.
	 * <br>
	 * Caso a expressão seja inválida, lança uma Exceção com a mensagem
	 * expressaoCronInvalida e junto com a mensagem original de erro.
	 * 
	 * @param numeroExecucoes Número de execuções
	 * @param cronExpressionValue Expressão da Cron
	 * @throws ParseException
	 * @return
	 */
	String findNextExecutions(int numeroExecucoes, String cronExpressionValue) throws ParseException;

	
	/**
	 * Cria um novo Job no sistema de agendamento usando usuario e senha
	 * informados. O password informado deve estar encriptado, ou seja, deve ser
	 * proveniente do cadastro do usuario
	 * 
	 * @param idJob
	 * @param name
	 * @param jobDescription
	 * @param cronExpression
	 * @param args
	 * @param username
	 * @param ccUsers logins dos usuarios que serão informados após a execução do processo
	 * @return
	 */
	JobInfo schedule(Long idJob,
							 String name,
							 String jobDescription, 
							 String cronExpression,
							 Serializable[] args, 
							 String username,
							 Set<String> ccUsers);

	JobInfo schedule(String name,
			 String jobDescription, 
			 String cronExpression,
			 Serializable[] args, 
			 String username,
			 Set<String> ccUsers);
	
	/**
	 * Retorna uma lista de <code>Jobs</code> cadastrados.
	 * 
	 * @param name
	 * @param login
	 * @param jobGroup
	 * @return
	 */
	List<JobInfo> findJobs(String name, String login, String jobGroup);

	/**
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	JobInfo findById(String jobName, String jobGroup);	

	/**
	 * Exclui um <code>Job</code> do Sistema de agendamento.
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	void deleteJob(String jobName, String jobGroup);

	/**
	 * Valida a expressão <b>Cron</b>.
	 * 
	 * @param cronExpression
	 * @return true caso a expressao seja válida.
	 */
	boolean validateCronExpression(String cronExpression);

}