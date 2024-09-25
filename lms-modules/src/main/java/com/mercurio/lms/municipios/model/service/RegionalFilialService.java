package com.mercurio.lms.municipios.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.municipios.model.dao.RegionalFilialDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.regionalFilialService"
 */
public class RegionalFilialService extends CrudService<RegionalFilial, Long> {
	
	private FilialService filialService;
	private RegionalService regionalService;
	private VigenciaService vigenciaService;

	// Uma regional não pode possuir duas filiais sedes vigentes. Caso ocorra
	// essa situação a seguinte mensagem deve ser exibida LMS-29009.
	protected RegionalFilial beforeStore(RegionalFilial bean) {
		RegionalFilial regionalFilial = (RegionalFilial)bean;

		if (regionalService.findRegionalByIdRegional(regionalFilial.getRegional().getIdRegional(),regionalFilial.getDtVigenciaInicial(),regionalFilial.getDtVigenciaFinal()).size() == 0)
			throw new BusinessException("LMS-29097");

		//3- Uma mesma filial não pode estar vigente em mais de uma regional. Caso isso ocorra exibir a mensagem LMS-00003 e abortar o processo.
		if (regionalFilial.getFilial().getIdFilial() != null) {
			if (getRegionalFilialDAO().verificaFilialVigente(regionalFilial)) {
				throw new BusinessException("LMS-29070");
			}
		}

		//4- Uma regional não pode possuir duas filiais sedes vigentes.
		if (regionalFilial.getRegional() != null) {
			if (regionalFilial.getBlSedeRegional().equals(Boolean.TRUE)) {
				if (getRegionalFilialDAO().verificaFiliaisSedeVigentes(
						regionalFilial)) {
					throw new BusinessException("LMS-29009");
				}
			}
		}

		return super.beforeStore(bean);
	}
	
	
	public List findLookupRegionalFilial(TypedFlatMap criteria) {
		return getRegionalFilialDAO().findLookupRegionalFilial(criteria);
	}
	
	protected void beforeRemoveById(Long id) {
		RegionalFilial regionalFilial = findByIdPersonalizado((Long)id);
		if(regionalFilial.getDtVigenciaInicial()!= null){
			JTVigenciaUtils.validaVigenciaRemocao(regionalFilial);
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		if (!ids.isEmpty()) {
			for (int i = 0; i < ids.size(); i++) {
				
				RegionalFilial regionalFilial = findById((Long)ids.get(0));
				if(regionalFilial.getDtVigenciaInicial() != null){
					JTVigenciaUtils.validaVigenciaRemocao(regionalFilial);
				}
			}
		}
		super.beforeRemoveByIds(ids);
	}
	
	public Regional findLastRegionalVigente(Long idFilial) {
		return getRegionalFilialDAO().findLastRegionalVigente(idFilial);
	}	

	/**
	 * Recupera uma instância de <code>RegionalFilial</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RegionalFilial findById(java.lang.Long id) {
		return (RegionalFilial) super.findById(id);
	}
	
	public RegionalFilial findByIdPersonalizado(java.lang.Long id) {
		List lista = getRegionalFilialDAO().findByIdPersonalizado(id);
		return (RegionalFilial)lista.get(0);
	}
	
	
	//metodo chamado ao detalhar um registro, verifica se as vigencias estao vigentes e passa um flag para o javascript
	public TypedFlatMap findByIdEValidaDtVigencia(java.lang.Long id) {
		RegionalFilial regionalFilial = (RegionalFilial)findById(id);
		TypedFlatMap mapRegionalFilial = new TypedFlatMap();
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(regionalFilial);
		
		mapRegionalFilial.put("idRegionalFilial", regionalFilial.getIdRegionalFilial());
		mapRegionalFilial.put("acaoVigenciaAtual", acaoVigencia);
		mapRegionalFilial.put("dtVigenciaFinal", regionalFilial.getDtVigenciaFinal());
		mapRegionalFilial.put("dtVigenciaInicial", regionalFilial.getDtVigenciaInicial());
		mapRegionalFilial.put("blSedeRegional", regionalFilial.getBlSedeRegional());
		
		final Regional regional = regionalFilial.getRegional();
		mapRegionalFilial.put("regional.idRegional", regional.getIdRegional());
		mapRegionalFilial.put("regional.dtVigenciaFinal", regional.getDtVigenciaFinal());
		mapRegionalFilial.put("regional.dtVigenciaInicial", regional.getDtVigenciaInicial());
		if(regional.getUsuario()!= null)
			mapRegionalFilial.put("regional.usuario.nmUsuario", regional.getUsuario().getNmUsuario());

		final Filial filial = regionalFilial.getFilial();
		mapRegionalFilial.put("filial.idFilial", filial.getIdFilial());
		mapRegionalFilial.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		mapRegionalFilial.put("filial.sgFilial", filial.getSgFilial());

		return mapRegionalFilial;
	}

	/**
	 * Busca a RegionalFilial responsável.<BR>
	 * Busca pelo Endereco da Pessoa com Municipio e RegionalFilial -- todos vigentes pela data atual. 
	 * @author Robson Edemar Gehl
	 * @param tpEndereco
	 * @param blPadraoMcd
	 * @param dtVigencia
	 * @return
	 */
	public RegionalFilial findRegionalFilialResponsavel(Long idMunicipio, Boolean blPadraoMcd){
		List list = getRegionalFilialDAO().findRegionalFilialResponsavel(idMunicipio, blPadraoMcd);
		if (!list.isEmpty()){
			return (RegionalFilial) list.get(0);
		}
		return null;
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
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RegionalFilial bean) {
		return super.store(bean);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(Map bean) {
		RegionalFilial regionalFilial = new RegionalFilial();
		ReflectionUtils.copyNestedBean(regionalFilial,bean);

		if (regionalFilial != null) {
				//2-valida se as datas vigentes da regionalFilial estão no intervalo da filial
				filialService.verificaExistenciaHistoricoFilial(regionalFilial.getFilial().getIdFilial(),regionalFilial.getDtVigenciaInicial(),regionalFilial.getDtVigenciaFinal());
				vigenciaService.validaVigenciaBeforeStore(regionalFilial);
		}	
		super.store(regionalFilial);
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(regionalFilial);
		bean.put("acaoVigenciaAtual", acaoVigencia);
		bean.put("idRegionalFilial", regionalFilial.getIdRegionalFilial());
		return bean;
	}	

	/**
	 * Não deve permitir alteração de datas de vigência da regional
	 * para datas fora dos intervalos dos registro filhos cadastrados em Regional filial .
	 */
	public boolean findFilhosVigentesByVigenciaPai(Long idRegional, YearMonthDay dtInicioVigenciaPai, YearMonthDay dtFimVigenciaPai){
		return getRegionalFilialDAO().findFilhosVigentesByVigenciaPai(idRegional,dtInicioVigenciaPai,dtFimVigenciaPai);
	}

	public List findBySgFilialIdRegional(String sgFilial, Long idRegional){
		return getRegionalFilialDAO().findBySgFilialIdRegional(sgFilial,idRegional);
	}

	/**
	 * Workaround necessário para suprir uma solução parcial proposta pela arquitetura 
	 * para um problema na lookup de regional-filial. 
	 * @param sgFilial
	 * @param idRegional
	 * @return
	 */
	public List findBySgFilialIdRegional2(String sgFilial, Long idRegional){
		List l = getRegionalFilialDAO().findBySgFilialIdRegional(sgFilial,idRegional);
		if(!l.isEmpty()){
			for(Iterator it = l.iterator();it.hasNext();){
				Map m = (Map)it.next();
				Map m1 = new HashMap();
				m1.put("sgFilial",m.get("sgFilial"));
				m.put("filial",m1);
				Map m2 = new HashMap();
				m2.put("nmFantasia",m.get("nmFantasia"));
				m1.put("pessoa",m2);
				Map m3 = new HashMap();
				m.put("regional",m3);
				m3.put("idRegional",m.get("idRegional"));
			}
		}
		return l;
	}
	
	/**
	 * Busca apenas nas filiais que o usuario identificado por idUsuario tem
	 * acesso.
	 * 
	 * @param sgFilial sigla da filial a ser procurada
	 * @param idRegional identificador da regional a ser procurada
	 * @return lista de filiais que o usuario tem acesso filtrado pelos criterios recebidos
	 */
	public List findBySgFilialIdRegionalUsuarioLogado(String sgFilial, Long idRegional){
		List l = getRegionalFilialDAO().findBySgFilialIdRegionalUsuarioLogado(sgFilial,idRegional);
		if(!l.isEmpty()){
			for(Iterator it = l.iterator();it.hasNext();){
				Map m = (Map)it.next();
				Map m1 = new HashMap();
				m1.put("sgFilial",m.get("sgFilial"));
				m.put("filial",m1);
				Map m2 = new HashMap();
				m2.put("nmFantasia",m.get("nmFantasia"));
				m1.put("pessoa",m2);
				Map m3 = new HashMap();
				m.put("regional",m3);
				m3.put("idRegional",m.get("idRegional"));
			}
		}
		return l;
	}
	
	public Map<String, Object> findRegionalByIdFilial(Long idFilial) {
		Regional regional = this.findRegional(idFilial);
		Map<String, Object> result = new HashMap<String, Object>();
		if(regional != null) {
			result.put("idRegional", regional.getIdRegional());
			result.put("sgRegional", regional.getSgRegional());
			result.put("dsRegional", regional.getDsRegional());
		}
		return result;
	}

	public Regional findRegional(Long idFilial) {
		RegionalFilial regionalFilial = getRegionalFilialDAO().findRegionalFilial(idFilial, JTDateTimeUtils.getDataAtual());
		if(regionalFilial != null) {
			return regionalFilial.getRegional();			
		}
		return null;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getRegionalFilialDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRegionalFilialDAO().getRowCountCustom(criteria);
	}

	public Boolean findByIdRegionalAndIdEmpresa(Long idRegional, Long idEmpresa) {
		return getRegionalFilialDAO().findByIdRegionalAndIdEmpresa(idRegional,idEmpresa);
	}

	public List findByRegionais(List regionais) {
		return getRegionalFilialDAO().findByRegional(regionais);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRegionalFilialDAO(RegionalFilialDAO dao) {
		setDao( dao );
	}
	private RegionalFilialDAO getRegionalFilialDAO() {
		return (RegionalFilialDAO) getDao();
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
}