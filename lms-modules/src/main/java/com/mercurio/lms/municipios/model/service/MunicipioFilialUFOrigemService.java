package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MunicipioFilialUFOrigem;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialUFOrigemDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.municipioFilialUFOrigemService"
 */
public class MunicipioFilialUFOrigemService extends CrudService<MunicipioFilialUFOrigem, Long> {
	private MunicipioFilialService municipioFilialService;
	private VigenciaService vigenciaService;

	public ResultSetPage findPaginated(Map criteria) {
		List<String> included = new ArrayList<String>();
		included.add("idMunicipioFilialUFOrigem");
		included.add("municipioFilial.filial.nmPessoa");
		included.add("municipioFilial.municipio.nmMunicipio");
		included.add("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa");
		included.add("municipioFilial.municipio.unidadeFederativa.pais.nmPais");
		included.add("municipioFilial.municipio.blDistrito");
		included.add("municipioFilial.municipio.municipioDistrito.nmMunicipio");
		included.add("unidadeFederativa.siglaDescricao");
		included.add("dtVigenciaInicial");
		included.add("dtVigenciaFinal");

		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	protected MunicipioFilialUFOrigem beforeStore(MunicipioFilialUFOrigem bean) {
		MunicipioFilialUFOrigem munFilUnidFederat = (MunicipioFilialUFOrigem)bean;
		if(getMunicipioFilialUFOrigemDAO().verificaUnidFederatAtendidas(
				munFilUnidFederat.getUnidadeFederativa().getIdUnidadeFederativa(),
				munFilUnidFederat.getMunicipioFilial().getIdMunicipioFilial(),
				munFilUnidFederat.getIdMunicipioFilialUFOrigem(), 
				munFilUnidFederat.getDtVigenciaInicial(), 
				munFilUnidFederat.getDtVigenciaFinal()
			)
		) {
			throw new BusinessException("LMS-00003");
		}

		if (!municipioFilialService.isMunicipioFilialVigente(
				munFilUnidFederat.getMunicipioFilial().getIdMunicipioFilial(),
				munFilUnidFederat.getDtVigenciaInicial(),
				munFilUnidFederat.getDtVigenciaFinal()
			)
		) {
			throw new BusinessException("LMS-29022");
		}

		return super.beforeStore(bean);
	}

	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param date2 
	 * @param date 
	 * @return
	 */
	public List findUFOrigemByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialUFOrigemDAO().findUfOrigemVigenteByMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica se existe registro para o atendimento e a UF informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param idUf
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialUfOrigem(Long idMunicipioFilial, Long idUf, YearMonthDay dtVigencia){
		return getMunicipioFilialUFOrigemDAO().verificaExisteMunicipioFilialUfOrigem(idMunicipioFilial, idUf, dtVigencia);
	}

	public boolean validateExisteMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigencia){
		return getMunicipioFilialUFOrigemDAO().verificaExisteMunicipioFilialVigente(idMunicipioFilial, dtVigencia);
	}

	public boolean validateMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
		return getMunicipioFilialUFOrigemDAO().verificaMunicipioFilialVigenciaFutura(idMunicipio, idFilial, dtVigencia);
	}

	/**
	 * Recupera uma instância de <code>MunFilUnidFederat</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MunicipioFilialUFOrigem findById(java.lang.Long id) {
		return (MunicipioFilialUFOrigem)super.findById(id);
	}

	public Map findByIdDetalhamento(java.lang.Long id) {
		MunicipioFilialUFOrigem bean = (MunicipioFilialUFOrigem) super.findById(id);
		TypedFlatMap retorno = bean2map(bean);
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
		retorno.put("acaoVigenciaAtual",acaoVigencia);

		return retorno;
	}	

	private TypedFlatMap bean2map(MunicipioFilialUFOrigem bean){
		TypedFlatMap map = new TypedFlatMap();

		map.put("idMunicipioFilialUFOrigem", bean.getIdMunicipioFilialUFOrigem());
		map.put("unidadeFederativa.sgUnidadeFederativa", bean.getUnidadeFederativa().getSgUnidadeFederativa());
		map.put("unidadeFederativa.nmUnidadeFederativa", bean.getUnidadeFederativa().getNmUnidadeFederativa());
		map.put("unidadeFederativa.idUnidadeFederativa", bean.getUnidadeFederativa().getIdUnidadeFederativa());
		map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
		map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());

		return map;
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MunicipioFilialUFOrigem bean) {
		return super.store(bean);
	}

	public java.io.Serializable storeMap(Map bean) {
		MunicipioFilialUFOrigem municipioUf = new MunicipioFilialUFOrigem();
		ReflectionUtils.copyNestedBean(municipioUf, bean);

		vigenciaService.validaVigenciaBeforeStore(municipioUf);

		super.store(municipioUf);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMunicipioFilialUFOrigem", municipioUf.getIdMunicipioFilialUFOrigem());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(municipioUf));
		
		return retorno;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioFilialUFOrigemDAO(MunicipioFilialUFOrigemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MunicipioFilialUFOrigemDAO getMunicipioFilialUFOrigemDAO() {
		return (MunicipioFilialUFOrigemDAO) getDao();
	}

	protected void beforeRemoveByIds(List ids) {
		MunicipioFilialUFOrigem bean = null;
		for(Iterator ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long)ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}

	public boolean findUfByMunFil(Long idMunicipioFilial){
		return getMunicipioFilialUFOrigemDAO().findUfByMunFil(idMunicipioFilial);
	}
	
	/**
	 * Busca todos as uf vigentes de um municipio atendido
	 */
	public List findUFsAtendidasByMunicipioFilial(Long idMunicipioFilial) {
		return getMunicipioFilialUFOrigemDAO().findUFsAtendidasByMunicipioFilial(idMunicipioFilial);
	}

	/**
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	
}