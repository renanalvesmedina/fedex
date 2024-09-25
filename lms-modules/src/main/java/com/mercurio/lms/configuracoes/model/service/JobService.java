package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.quartz.CronExpression;

import com.mercurio.adsm.batch.AdsmBatchController;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.job.JobInfo;
import com.mercurio.adsm.batch.model.SchedulableMethodCollector;
import com.mercurio.adsm.batch.model.SchedulableUnit;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.configuracoes.jobService"
 */
public class JobService extends CrudService implements JobInterfaceService {

	private AdsmBatchController controller;

	private GrupoJobService grupoJobService;

	private UsuarioService usuarioService;

	private SchedulableMethodCollector schedulableMethodCollector;

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public GrupoJobService getGrupoJobService() {
		return grupoJobService;
	}

	public void setGrupoJobService(GrupoJobService grupoJobService) {
		this.grupoJobService = grupoJobService;
	}
	
	/**
	 * Retorna uma lista de todos os processo que podem ser agendados do sistema.
	 * @return
	 */
	public Collection<SchedulableUnit> findSchedulables() {
		return this.schedulableMethodCollector.getSchedulables();
	}

	public String findNextExecutions(int numeroExecucoes, String cronExpressionValue) throws ParseException {

		CronExpression cronExpression = new CronExpression(cronExpressionValue);

		// Reduz o numero de execucoes para conseguir o valor correto de
		// execucoes...
		numeroExecucoes--;

		Date data = new Date();
		StringBuilder msg = new StringBuilder();

		DateTime dateTime = null;
		for (int i = 0; i < numeroExecucoes; i++) {
			data = cronExpression.getNextValidTimeAfter(data);

			if (data != null) {
				dateTime = new DateTime(data.getTime(), JTDateTimeUtils.getUserDtz());
				msg.append(JTFormatUtils.format(dateTime, JTFormatUtils.FULL) + "\n");
			} else {
				return msg.toString();
			}
		}

		return msg.toString();
	}

	/**
	 * logica para agendamento
	 * 
	 * @param idJob
	 * @param serviceName
	 * @param jobDescription
	 * @param group
	 * @param cronExpression
	 * @param parameters
	 * @param username
	 * @param password
	 * @return
	 */
	public JobInfo schedule(Long idJob,
									String name, 
									String jobDescription, 
									String cronExpression, 
									Serializable[] args, 
									String username, 
									Set<String> ccUsers) {

		return controller.schedule(idJob,
										name,
										jobDescription, 
										cronExpression, 
										args, 
										username, 
										ccUsers,
										SessionUtils.getFilialSessao().getDateTimeZone()); 

	}

	public JobInfo schedule(String name, 
			String jobDescription, 
			String cronExpression, 
			Serializable[] args, 
			String username, 
			Set<String> ccUsers) {

		return controller.schedule(name,
						jobDescription, 
						cronExpression, 
						args, 
						username, 
						ccUsers,
						SessionUtils.getFilialSessao().getDateTimeZone()); 
	
	}
	
	/**
	 * <p>
	 * Exclui um <code>Job</code> do Sistema de agendamento.
	 * </p>
	 * 
	 * @param groupName
	 * @param serviceName
	 * @return
	 */
	public void deleteJob(String name, String groupName) {
		controller.deleteJob(name, groupName);
	}

	/**
	 * <p>
	 * Valida a expressão <b>Cron</b>.
	 * </p>
	 * 
	 * @param cronExpression
	 * @return true caso a expressao seja válida.
	 */
	public boolean validateCronExpression(String cronExpressionValue) {

		try {
			new CronExpression(cronExpressionValue);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	public void setController(AdsmBatchController controller) {
		this.controller = controller;
	}

	public JobInfo findById(String jobName, String jobGroup) {
		return this.controller.getJobInfo(jobName, jobGroup);
	}
	
	public List<JobInfo> findJobs(String name, String login, String jobGroup) {
		BatchType type = null;
		try {
			type = BatchType.valueOf(jobGroup);
		} catch (Exception e) {
		}
		return this.controller.findJobs(name, login, type);
	}

}