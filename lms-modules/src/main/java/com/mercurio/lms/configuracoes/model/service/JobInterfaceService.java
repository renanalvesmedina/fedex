package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.mercurio.adsm.batch.job.JobInfo;

public interface JobInterfaceService {

	/**
	 * Retorna as datas das pr�ximas n execu��es de uma express�o de acordo com
	 * a expressao da cron informada e com a data atual.
	 * <br>
	 * Caso a express�o seja inv�lida, lan�a uma Exce��o com a mensagem
	 * expressaoCronInvalida e junto com a mensagem original de erro.
	 * 
	 * @param numeroExecucoes N�mero de execu��es
	 * @param cronExpressionValue Express�o da Cron
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
	 * @param ccUsers logins dos usuarios que ser�o informados ap�s a execu��o do processo
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
	 * Valida a express�o <b>Cron</b>.
	 * 
	 * @param cronExpression
	 * @return true caso a expressao seja v�lida.
	 */
	boolean validateCronExpression(String cronExpression);

}