package com.mercurio.lms.configuracoes.action;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.job.JobInfo;
import com.mercurio.adsm.batch.model.SchedulableMethodCollector;
import com.mercurio.adsm.batch.model.SchedulableUnit;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.DirectoryWatcherService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.GrupoJobService;
import com.mercurio.lms.configuracoes.model.service.JobInterfaceService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 */
public class ManterJobAction extends CrudAction  {
	
	private JobInterfaceService jobService;
	private GrupoJobService grupoJobService;
	private UsuarioService usuarioService;
	private SchedulableMethodCollector schedulableMethodCollector;
	private ParametroGeralService parametroGeralService;
	private DirectoryWatcherService directoryWatcherService;
	private AwbService awbService;

	public void setJobService(JobInterfaceService jobService) {
		this.jobService = jobService;
	}
	
	public void setGrupoJobService(GrupoJobService grupoJobService) {
		this.grupoJobService = grupoJobService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public List<TypedFlatMap> findJobGroups() {
		List<TypedFlatMap> groups = new LinkedList<TypedFlatMap>();
		
		final BatchType[] values = BatchType.values();
		for (int i = 0; i < values.length; i++) {
			final boolean bBatch = !BatchType.isBusinessBatch(values[i]);
			if (bBatch) {
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("jobGroupId", values[i].name());
				tfm.put("jobGroupName", values[i].name());
				groups.add(tfm);
			}
		}
		
		return groups;
	}
	
	// preenche a combobox com as serviços que são agendaveis
	public List<TypedFlatMap> findAllSchedulableUnit(String typeStr) {
		final BatchType type = BatchType.valueOf(typeStr);
		final Collection<SchedulableUnit> schedulables = this.schedulableMethodCollector.getSchedulables();
		final List<TypedFlatMap> scs = new ArrayList<TypedFlatMap>();			

		for(SchedulableUnit schedulable : schedulables) {
			if(schedulable.getAssynchronousMethod().type().equals(type)) {
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("schedulableUnitName", schedulable.getName());
				tfm.put("schedulableUnitId", schedulable.getName());
				scs.add(tfm);
			}
		}

		Collections.sort(scs, new Comparator<TypedFlatMap>() {
			@Override
			public int compare(TypedFlatMap o1, TypedFlatMap o2) {			
				return o1.getString("schedulableUnitName").compareTo(o2.getString("schedulableUnitName"));
			}
		});
			
		return scs;
	}

	public List findJobs(TypedFlatMap criteria) {
		
		String jobGroup = criteria.getString("jobGroup").replace('%', ' ').trim();
		Long idJob = criteria.getLong("idJob");
		String name = criteria.getString("schedulableUnit").replace('%', ' ').trim();
		String login = criteria.getString("login").replace('%', ' ').trim();

		List<JobInfo> jobs = this.jobService.findJobs(name, login, jobGroup);

		CollectionUtils.filter(jobs, new Predicate() {

			public boolean evaluate(Object object) {
				JobInfo jobInfo = (JobInfo) object;
				SchedulableUnit su = schedulableMethodCollector.getSchedulableUnit(jobInfo.getJobData().getName());
				return !BatchType.isBusinessBatch(su.getAssynchronousMethod().type());
			}
			
		});
		
		CollectionUtils.transform(jobs, new Transformer() {

			public Object transform(Object input) {
				JobInfo jobInfo = (JobInfo) input;
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("id", jobInfo.getJobName()+","+jobInfo.getJobGroup());
				tfm.put("idJob", jobInfo.getJobData().getId());
				tfm.put("name", jobInfo.getJobData().getName());
				tfm.put("description", jobInfo.getDescription());
				tfm.put("jobGroup", jobInfo.getJobGroup());
				tfm.put("cronExpression", jobInfo.getCronExpression());
				return tfm;
			}
			
		});
		return jobs;

	}
	
	public String findNextExecutions(String cronExpression){
		try {
			return this.jobService.findNextExecutions(4, cronExpression);
		} catch (ParseException e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	public TypedFlatMap schedule(TypedFlatMap criteria) {
		
		Long idJob = criteria.getLong("idJob");
		String schedulableUnit = criteria.getString("schedulableUnit");
		String jobGroup = criteria.getString("jobGroup");
		String description = criteria.getString("description");
		String cronExpression = criteria.getString("cronExpression");
		String login = criteria.getString("login");
		
		final Map<String, String> mapParameters = new HashMap<String, String>();
		List parameters = criteria.getList("parameters");
		if (parameters!=null) {
			for (Iterator iter = parameters.iterator(); iter.hasNext();) {
				TypedFlatMap parametro = (TypedFlatMap) iter.next();
				mapParameters.put(parametro.getString("name"), parametro.getString("value"));
			}
		}
		
		List ccUsers = criteria.getList("ccUsers");
		Set<String> ccUsersLogins = new HashSet<String>();
		
		if (ccUsers!=null) {
			for (Iterator iter = ccUsers.iterator(); iter.hasNext();) {
				TypedFlatMap user = (TypedFlatMap) iter.next();
				Usuario u = usuarioService.findById(user.getLong("usuario.idUsuario"));
				ccUsersLogins.add(u.getLogin());
			}
		}
		
		JobInfo jobInfo = this.jobService.schedule(idJob,
												schedulableUnit, 
												description, 
												cronExpression, 
												null, 
												login, 
												ccUsersLogins);
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("id", jobInfo.getJobName()+","+jobInfo.getJobGroup());
		tfm.put("idJob", jobInfo.getJobData().getId());
		return tfm;
		
	}
	
	public boolean validateCronExpression(String cronExpression) {
		
		if (!this.jobService.validateCronExpression(cronExpression)) {
			throw new BusinessException("expressaoCronInvalida");
		}
		return  true;
	}
	
	/**
	 * Remove um job cadastrado apartir da tela de detalhamento.
	 * 
	 * @param criteria
	 */
	public void removeJob(String id) {
		
		String[] ids = id.split(",");
		if (ids == null || ids.length!=2) {
			throw new IllegalArgumentException("id inválido");
		}
		
		this.jobService.deleteJob(ids[0], ids[1]);
	}
	
	// FIXME - rotina de (associada o botão) deve ser retirada dessa ACTIONS e colocada na ACTION correta(CarregamentoAutomaticoAction)
	public void executeCarregamentoManual() {
		directoryWatcherService.execute(parametroGeralService.findSimpleConteudoByNomeParametro("DIRETORIO_CARGA_MPC_GMB"));		
	}


	/**
	 * Retorna um job em expecifico...
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findById(TypedFlatMap criteria){
		
		// o id deve ser uma concatenação de: jobName,jobGroup 
		String id = criteria.getString("id");
		String[] ids = id.split(",");
		if (ids == null || ids.length!=2) {
			throw new IllegalArgumentException("id inválido");
		}
		JobInfo jobInfo = this.jobService.findById(ids[0], ids[1]);
		
		TypedFlatMap tfm = new TypedFlatMap();
		
		tfm.put("id", jobInfo.getJobName()+","+jobInfo.getJobGroup());
		tfm.put("idJob", jobInfo.getJobData().getId());
		tfm.put("schedulableUnit", jobInfo.getJobData().getName());
		tfm.put("cronExpression", jobInfo.getCronExpression());
		tfm.put("login", jobInfo.getJobData().getUsername());
		tfm.put("description", jobInfo.getDescription());
		tfm.put("jobGroup", jobInfo.getJobGroup());
		final Serializable[] args = jobInfo.getJobData().getArgs();
		List paramsList = convertArgsToMap(args);
		tfm.put("parameters", paramsList);
		
		return tfm;
	}

	public static List convertArgsToMap(final Serializable[] args) {
		List paramsList = new LinkedList();
		if ( args != null ) {
			if ( ( args.length == 1 )&& ( args[0] instanceof Map )) {
				Map params = (Map) args[0];
				Set<Map.Entry> entries = params.entrySet();
				for (Map.Entry entry : entries) {
					TypedFlatMap param = new TypedFlatMap();
					param.put("name", entry.getKey());
					final Object value = entry.getValue();
					if (value != null) {
						param.put("value", value.toString());
					} else {
						param.put("value", "");
					}
					paramsList.add(param);
				}
			} else {
				for (int i = 0; i < args.length; i++) {
					TypedFlatMap param = new TypedFlatMap();
					final Object value = args[i];
					if (value != null) {
						param.put("value", value.toString());
					} else {
						param.put("value", "");
					}
					paramsList.add(param);
				}
			}
		}
		return paramsList;
	}
	
	/**
	 * Delete os jobs cadastrados a partir da grid da tela
	 */
	@ParametrizedAttribute( type=String.class )
	@Override
    public void removeByIds(List removeIds) {
    	
		for (String id : ((List<String>)removeIds)) {
			String[] ids = id.split(",");
			if (ids == null || ids.length!=2) {
				throw new IllegalArgumentException("id inválido");
			}
			
			this.jobService.deleteJob(ids[0], ids[1]);
			
		}
    }
    
    /**
     * Find da lookup de grupoJob.
     * 
     * @param criteria
     * @return
     */
    public List findLookupGrupoJob(Map criteria) {
    	return this.grupoJobService.findLookup(criteria);	
    }
    
    /**
	 * Find da lookup de funcionário.
	 * @param criteria
	 * @return List com usuários encontrados.
	 */
	public List findLookupUsuario(TypedFlatMap criteria) {
		
		String nrMatricula = criteria.getString("nrMatricula");
		
		return usuarioService.findLookupUsuarioFuncionario(
				null,
				nrMatricula,
				SessionUtils.getFilialSessao().getIdFilial(),
				null,
				null, 
				null,
				true);
	}
	
	/**
	 * Busca algums dos dados do usuario logado, que está na sessão.
	 * 
	 * @return map
	 */
	public TypedFlatMap getDataUsuario() {

		TypedFlatMap result = new TypedFlatMap();
		result.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		result.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
		result.put("nmFantasia", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());

		return result;
	}
	
	/**
     * Verifica se o usuário selecionado possui um email cadastrado.
     * 
     * @param criteria
     */
    public void validateEmailUsuario(TypedFlatMap criteria) {
    	
    	Usuario usuario = this.usuarioService.findById(criteria.getLong("idUsuario"));
    	
    	if ((usuario.getDsEmail()==null) || (usuario.getDsEmail().equals(""))) {
    		throw new BusinessException("LMS-27082");
    	}
    }

	public void setSchedulableMethodCollector(
			SchedulableMethodCollector schedulableMethodCollector) {
		this.schedulableMethodCollector = schedulableMethodCollector;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
}

	public void setDirectoryWatcherService(
			DirectoryWatcherService directoryWatcherService) {
		this.directoryWatcherService = directoryWatcherService;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}



}
