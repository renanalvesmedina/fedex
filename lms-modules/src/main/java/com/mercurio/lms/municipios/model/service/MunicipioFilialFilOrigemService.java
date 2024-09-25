package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
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
import com.mercurio.lms.municipios.model.MunicipioFilialFilOrigem;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialFilOrigemDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.municipioFilialFilOrigemService"
 */
public class MunicipioFilialFilOrigemService extends CrudService<MunicipioFilialFilOrigem, Long> {
	private MunicipioFilialService municipioFilialService;
	private VigenciaService vigenciaService;
	private FilialService filialService;

	public ResultSetPage findPaginated(Map criteria) {
		List<String> included = new ArrayList<String>();
		included.add("idMunicipioFilialFilOrigem");
		included.add("filial.siglaNomeFilial");
		included.add("filial.sgFilial");
		included.add("filial.idFilial");
		included.add("filial.pessoa.nomePessoa");
		included.add("municipioFilial.filial.pessoa.nmPessoa");
		included.add("municipioFilial.municipio.nmMunicipio");
		included.add("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa");
		included.add("municipioFilial.municipio.unidadeFederativa.pais.nmPais");
		included.add("municipioFilial.municipio.blDistrito");
		included.add("municipioFilial.municipio.municipioDistrito.nmMunicipio");
		included.add("filial.pessoa.nmPessoa");
		included.add("dtVigenciaInicial");
		included.add("dtVigenciaFinal");
 
		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	/**
	 * Recupera uma instância de <code>MunFilFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Um map.
	 */
	public MunicipioFilialFilOrigem findById(java.lang.Long id) {
		return (MunicipioFilialFilOrigem)super.findById(id);
	}

	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public List<MunicipioFilialFilOrigem> findFilOrigemVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialFilOrigemDAO().findFilOrigemVigenteByMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica se existe registro para o atendimento e filial informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param idFilial
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialFilOrigem(Long idMunicipioFilial, Long idFilial, YearMonthDay dtVigencia){
		return getMunicipioFilialFilOrigemDAO().verificaExisteMunicipioFilialFilOrigem(idMunicipioFilial, idFilial, dtVigencia);
	}

	public boolean validateExisteMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigenciaFinal){
		return getMunicipioFilialFilOrigemDAO().verificaExisteMunicipioFilialVigente(idMunicipioFilial, dtVigenciaFinal);
	}

	public boolean validateMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
		return getMunicipioFilialFilOrigemDAO().verificaMunicipioVigenciaFutura(idMunicipio, idFilial, dtVigencia);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Override
	public void removeById(Long id) {
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
	public java.io.Serializable store(MunicipioFilialFilOrigem bean) {
		return super.store(bean);
	}

	@Override
	protected MunicipioFilialFilOrigem beforeStore(MunicipioFilialFilOrigem bean) {
		MunicipioFilialFilOrigem municipioFilialFilOrigem = (MunicipioFilialFilOrigem)bean;

		vigenciaService.validaVigenciaBeforeStore(municipioFilialFilOrigem);

		if(!municipioFilialService.isMunicipioFilialVigente(municipioFilialFilOrigem.getMunicipioFilial().getIdMunicipioFilial(), 
				municipioFilialFilOrigem.getDtVigenciaInicial(), municipioFilialFilOrigem.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29022");

		if(getMunicipioFilialFilOrigemDAO().verificaVigenciaMunicipioFiliailFil(((MunicipioFilialFilOrigem)bean)))
			throw new BusinessException("LMS-00003");

		filialService.verificaExistenciaHistoricoFilial(
				municipioFilialFilOrigem.getFilial().getIdFilial(),
				municipioFilialFilOrigem.getDtVigenciaInicial(),
				municipioFilialFilOrigem.getDtVigenciaFinal()
		);

		return super.beforeStore(bean);
	}

	@Override
	protected void beforeRemoveByIds(List ids) {
		MunicipioFilialFilOrigem bean = null;
		for(Iterator<Serializable> ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long)ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		List<Serializable> list = new ArrayList<Serializable>();
		list.add(id);
		beforeRemoveByIds(list);
	}

	public boolean findFilialByMunFil(Long idMunicipioFilial){
		return getMunicipioFilialFilOrigemDAO().findFilialByMunFil(idMunicipioFilial);
	}

	//busca todas as Filiais vigentes de um municipio atendido 
	public List<Map<String, Object>> findFilAtendidasByMunicipioFilial(Long idMunicipioFilial) {
		return getMunicipioFilialFilOrigemDAO().findFilAtendidasByMunicipioFilial(idMunicipioFilial);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioFilialFilOrigemDAO(MunicipioFilialFilOrigemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MunicipioFilialFilOrigemDAO getMunicipioFilialFilOrigemDAO() {
		return (MunicipioFilialFilOrigemDAO) getDao();
	}

	/**
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

 }