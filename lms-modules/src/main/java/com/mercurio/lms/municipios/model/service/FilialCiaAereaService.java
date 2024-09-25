package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.municipios.model.dao.FilialCiaAereaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.filialCiaAereaService"
 */
public class FilialCiaAereaService extends CrudService<FilialCiaAerea, Long> {
	private PessoaService pessoaService;
	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialMercurioFilialCiaService filialMercurioFilialCiaService;

	/**
	 * Recupera uma instância de <code>FilialCiaAerea</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public FilialCiaAerea findById(java.lang.Long id) {
		return (FilialCiaAerea)super.findById(id);
	}
 
	/**
	 * Recupera uma instância de <code>FilialCiaAerea</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Map findByIdDetalhamento(java.lang.Long id) {
		FilialCiaAerea filialCiaAerea = (FilialCiaAerea)super.findById(id);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idFilialCiaAerea",filialCiaAerea.getPessoa().getIdPessoa());

		Empresa empresa = filialCiaAerea.getEmpresa();
		retorno.put("empresa.idEmpresa",empresa.getIdEmpresa());
		retorno.put("empresa.pessoa.nmPessoa",empresa.getPessoa().getNmPessoa());
		retorno.put("empresa.dsEmpresa",empresa.getPessoa().getNmPessoa());

		Aeroporto aeroporto = filialCiaAerea.getAeroporto();
		retorno.put("aeroporto.idAeroporto",aeroporto.getPessoa().getIdPessoa());
		retorno.put("aeroporto.sgAeroporto",aeroporto.getSgAeroporto());
		retorno.put("aeroporto.pessoa.nmPessoa",aeroporto.getPessoa().getNmPessoa());

		Pessoa pessoa = filialCiaAerea.getPessoa();
		retorno.put("pessoa.idPessoa",pessoa.getIdPessoa());
		retorno.put("pessoa.tpIdentificacao",pessoa.getTpIdentificacao().getValue());
		retorno.put("pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
		retorno.put("pessoa.nmPessoa",pessoa.getNmPessoa());
		retorno.put("pessoa.dsEmail",pessoa.getDsEmail());
		retorno.put("pessoa.tpPessoa","J");

		retorno.put("cdFornecedor",filialCiaAerea.getCdFornecedor());
		retorno.put("blTaxaTerrestre",filialCiaAerea.getBlTaxaTerrestre());
		retorno.put("blImprimeMinuta.value",filialCiaAerea.getBlImprimeMinuta());
		retorno.put("dtVigenciaInicial",filialCiaAerea.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal",filialCiaAerea.getDtVigenciaFinal());

		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(filialCiaAerea);
		retorno.put("acaoVigenciaAtual",acaoVigencia);

		return retorno;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeMap(Map map) {
		FilialCiaAerea filialCiaAerea = new FilialCiaAerea();
		ReflectionUtils.copyNestedBean(filialCiaAerea,map);

		this.vigenciaService.validaVigenciaBeforeStore(filialCiaAerea);

		this.validaVigenciaExistente(filialCiaAerea);

		// store pessoa
		pessoaService.store(filialCiaAerea);

		this.beforeStore(filialCiaAerea);

		// store da filial cia aerea
		super.store(filialCiaAerea);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idFilialCiaAerea",filialCiaAerea.getPessoa().getIdPessoa());
		Integer acaoVigenciaAtual = JTVigenciaUtils.getIntegerAcaoVigencia(filialCiaAerea);
		retorno.put("acaoVigenciaAtual",acaoVigenciaAtual);

		retorno.put("empresa.dsEmpresa",filialCiaAerea.getEmpresa().getPessoa().getNmPessoa());
		retorno.put("empresa.idEmpresa",filialCiaAerea.getEmpresa().getIdEmpresa());

		return retorno;
	} 

	private void validaVigenciaExistente(FilialCiaAerea filialCiaAerea) {
		YearMonthDay dtVigenciaInicial = filialCiaAerea.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = filialCiaAerea.getDtVigenciaFinal();
		
		Long idAeroporto = filialCiaAerea.getAeroporto().getIdAeroporto();
		Long idEmpresa = filialCiaAerea.getEmpresa().getIdEmpresa();
		
		if(filialCiaAerea.getIdFilialCiaAerea() == null){
			List<FilialCiaAerea> list = findFilialCiaAerea(dtVigenciaInicial, dtVigenciaFinal, idAeroporto, idEmpresa, null);

			if(!list.isEmpty()){
				throw new BusinessException("LMS-00003");
			}
		} else {
			List<FilialCiaAerea> list = findFilialCiaAerea(dtVigenciaInicial, dtVigenciaFinal, idAeroporto, idEmpresa, filialCiaAerea.getIdFilialCiaAerea());

			if(!list.isEmpty()){
				throw new BusinessException("LMS-00003");
			}
		}
		
	}
	
	public List<FilialCiaAerea> findFilialCiaAerea(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idAeroporto, Long idEmpresa, Long idFilialCiaAereaDiferente) {
		return getFilialCiaAereaDAO().findFilialCiaAerea(dtVigenciaInicial, dtVigenciaFinal, idAeroporto, idEmpresa, idFilialCiaAereaDiferente);
	}

	/**
	 * Verifica se a pessoa especificada é Jurídica.
	 * Em uma inclusão: Verifica se existe outra pessoa com o mesmo tipo e número de identificação informados.
	 * @param criteria
	 * @return
	 */
	public List validateIdentificacao(TypedFlatMap map) {
		String tpIdentificacao = map.getString("tpIdentificacao");
		String nrIdentificacao = map.getString("nrIdentificacao");

		List retorno = new ArrayList(1);

		Pessoa pessoa = validateEspecializacao(FilialCiaAerea.class, tpIdentificacao, nrIdentificacao);

		if (pessoa != null) { 
			TypedFlatMap row = new TypedFlatMap();
			row.put("idPessoa",pessoa.getIdPessoa());
			row.put("nmPessoa",pessoa.getNmPessoa());
			row.put("dsEmail",pessoa.getDsEmail());
			row.put("tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("nrIdentificacao",pessoa.getNrIdentificacao());
			retorno.add(row);
		}

		return retorno;
	}

	public Pessoa validateEspecializacao(Class clazz, String tpIdentificacao, String nrIdentificacao) {
		Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao,tpIdentificacao);
		
		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(),clazz,false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-29005");
			}
		}

		return pessoa;
	}

	public ResultSetPage findPaginated(Map criteria) {
		List included = new ArrayList();
		included.add("idFilialCiaAerea");
		//Deve ser retornado o idEmpresa, pois o mesmo é ultilizado na lookup de FilialMercurioFilialCia.
		included.add("empresa.idEmpresa");
		included.add("empresa.pessoa.nmPessoa");
		included.add("pessoa.tpIdentificacao");
		included.add("pessoa.nrIdentificacao");
		included.add("pessoa.nrIdentificacaoFormatado");
		included.add("pessoa.nmPessoa");
		included.add("aeroporto.siglaDescricao");
		included.add("dtVigenciaInicial");
		included.add("dtVigenciaFinal");
		included.add("aeroporto.idAeroporto");
		included.add("aeroporto.sgAeroporto");
		included.add("aeroporto.pessoa.nmPessoa");

		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	public List findLookup(Map criteria) {
		String nrIdentificacao = (String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"pessoa.nrIdentificacao");
		if (nrIdentificacao != null && !nrIdentificacao.equals(""))
			ReflectionUtils.setNestedBeanPropertyValue(criteria,"pessoa.nrIdentificacao",FormatUtils.filterNumber(nrIdentificacao));
		return super.findLookup(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Remove a empresa e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removefilialCiaAereaById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros de FK na pessoa
		}	
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades quee deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List ids) {
		for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			this.removefilialCiaAereaById(id);
		}
	}

	/**
	 * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {		
		FilialCiaAerea filialCiaAerea = (FilialCiaAerea)super.findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(filialCiaAerea);
	}

	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for (int i = 0; i < ids.size() ; i++ )
			validaRemoveById((Long)ids.get(i));
		super.beforeRemoveByIds(ids);
	}

	public FilialCiaAerea beforeStore(FilialCiaAerea filialCiaAerea) {
		if(filialCiaAerea.getIdFilialCiaAerea()!= null){
			YearMonthDay dataInicio = filialCiaAerea.getDtVigenciaInicial();
			YearMonthDay dataFim = filialCiaAerea.getDtVigenciaFinal();
			Long idFilialCiaAerea = filialCiaAerea.getIdFilialCiaAerea();
			if(filialMercurioFilialCiaService.findFilialCiaAreaById(dataInicio,dataFim,idFilialCiaAerea)){
				throw new BusinessException("LMS-29120");
		}
		}
		return super.beforeStore(filialCiaAerea);
	}
	
	/**
	 * Verifica se a filial de cia aerea esta vigente dentro do perido informado 
	 * @param idFilialCiaAerea
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean verificaFilialCiaAereaVigente(Long idFilialCiaAerea, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getFilialCiaAereaDAO().verificaFilialCiaAereaVigente(idFilialCiaAerea, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setFilialCiaAereaDAO(FilialCiaAereaDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private FilialCiaAereaDAO getFilialCiaAereaDAO() {
		return (FilialCiaAereaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setFilialMercurioFilialCiaService(FilialMercurioFilialCiaService filialMercurioFilialCiaService) {
		this.filialMercurioFilialCiaService = filialMercurioFilialCiaService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}