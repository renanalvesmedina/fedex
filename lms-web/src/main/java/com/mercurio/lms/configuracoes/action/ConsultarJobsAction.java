package com.mercurio.lms.configuracoes.action;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.AdsmBatchController;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.job.JobData;
import com.mercurio.adsm.batch.job.JobExecution;
import com.mercurio.adsm.batch.job.JobInfo;
import com.mercurio.adsm.batch.model.LogJob;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConsultarJobsService;

/**
 * 
 */
public class ConsultarJobsAction extends CrudAction {

	private ConsultarJobsService consultarJobsService;
	
	private AdsmBatchController adsmBatchController;

	public void setAdsmBatchController(AdsmBatchController adsmBatchController) {
		this.adsmBatchController = adsmBatchController;
	}

	public ConsultarJobsService getConsultarJobsService() {
		return consultarJobsService;
	}

	public void setConsultarJobsService(
			ConsultarJobsService consultarJobsService) {
		this.consultarJobsService = consultarJobsService;
	}

	public List<TypedFlatMap> findJobGroups() {
		List<TypedFlatMap> groups = new LinkedList<TypedFlatMap>();
		
		final BatchType[] values = BatchType.values();
		for (int i = 0; i < values.length; i++) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("jobGroupId", values[i].name());
			tfm.put("jobGroupName", values[i].name());
			groups.add(tfm);
		}
		
		return groups;
	}	
	
	/**
	 * Realiza o findById
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	public TypedFlatMap findById(Long idStatusJob) throws Exception {

		JobExecution jobExecution = this.adsmBatchController.findJobExecution(idStatusJob);
		
		return (TypedFlatMap) new JobExecutionToMap().transform(jobExecution);
	}

	/**
	 * Faz a paginacao dos logs
	 * 
	 * @param map
	 * @return
	 */
	public ResultSetPage<LogJob> findPaginatedLogs(TypedFlatMap criteria) {
		
		return this.adsmBatchController.findJobLogs(criteria.getLong("idStatusJob"), 
													criteria.getInteger("_currentPage"), 
													criteria.getInteger("_pageSize"));
	}

	public TypedFlatMap validateJob(TypedFlatMap criteria) {
		return null;
	}

	public List getJobParameters(Long idStatusJob) {
		JobExecution jobExecution = this.adsmBatchController.findJobExecution(idStatusJob);
		return ManterJobAction.convertArgsToMap(jobExecution.getJobInfo().getJobData().getArgs());
		
	}
	
	/**
	 * Captura os dados do processo da area de detalhamento para a nova 
	 * chamada do processo.
	 * 
	 * @param criteria
	 */
	public TypedFlatMap validateReExecutarJob(TypedFlatMap criteria) {
		return new TypedFlatMap();
	}
	
	/**
	 * Captura os dados do processo da area de detalhamento para a nova 
	 * chamada do processo.
	 * 
	 * @param criteria
	 */
	public void generateReExecutarJob(TypedFlatMap criteria) { 
		final Long idStatusJob = criteria.getLong("idStatusJob");
		final JobExecution jobExecution = this.adsmBatchController.findJobExecution(idStatusJob);
		if (jobExecution == null) {
			throw new IllegalArgumentException("Batch invalid with idStatusJob="+idStatusJob);
		}
 		this.adsmBatchController.reexecuteJob(jobExecution);
	}

	public ResultSetPage<JobExecution> findJobsExecutions(TypedFlatMap criteria) {
	
		final String jobGroup = criteria.getString("jobGroup");
		final String schedulableUnit = criteria.getString("schedulableUnit");
		final String login = criteria.getString("login");
		final Long jobId = criteria.getLong("jobId");
		final YearMonthDay firedFrom = criteria.getYearMonthDay("firedFrom");
		final YearMonthDay firedUpon = criteria.getYearMonthDay("firedUpon");
		final DomainValue statusJob = criteria.getDomainValue("statusJob");
		final Integer currentPage = criteria.getInteger("_currentPage");
		final Integer pageSize = criteria.getInteger("_pageSize");
		
		final ResultSetPage<JobExecution> rsp = adsmBatchController.findJobsExecutions(jobId, schedulableUnit, login, BatchType.valueOf(jobGroup), firedFrom, firedUpon, statusJob, currentPage, pageSize);
		
		CollectionUtils.transform(rsp.getList(), new JobExecutionToMap());
		
		return rsp;
	}
	
	class JobExecutionToMap implements Transformer {

		public Object transform(Object input) {
			JobExecution jobExecution = (JobExecution) input;
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idStatusJob", jobExecution.getStatusJob().getIdStatusJob());
			final JobInfo jobInfo = jobExecution.getJobInfo();
			if (jobInfo != null ) {
				final DomainValue triggerState = adsmBatchController.getTriggerState(jobInfo.getJobName(), jobInfo.getJobGroup());
				if ( triggerState != null ) {
					tfm.put("statusAgendamento", triggerState.getDescriptionAsString());
				}
				final JobData jobData = jobInfo.getJobData();
				if (jobData != null) {
					tfm.put("idJob", jobData.getId());
					tfm.put("schedulableUnit", jobData.getName());
					tfm.put("login", jobData.getUsername());
				}
				tfm.put("jobGroup", jobInfo.getJobGroup());
				tfm.put("description", jobInfo.getDescription());
				tfm.put("cronExpression", jobInfo.getCronExpression());
			} else {
				tfm.put("idJob", "0");
				tfm.put("jobGroup", "");
				tfm.put("description", "");
				tfm.put("name", "");
				tfm.put("cronExpression", "");
			}
			tfm.put("fireTime", jobExecution.getStatusJob().getFiredTime());
			tfm.put("endTime", jobExecution.getStatusJob().getEndTime());
			tfm.put("message", jobExecution.getStatusJob().getDsMensagens());
			tfm.put("statusJob", jobExecution.getStatusJob().getTpJobStatus().getDescriptionAsString());
			tfm.put("nextFireTime", jobExecution.getNextFiretime());
			return tfm;
		}
		
	}

}
