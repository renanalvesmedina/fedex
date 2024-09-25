package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialCliOrigemDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.municipioFilialCliOrigemService"
 */
public class MunicipioFilialCliOrigemService extends CrudService<MunicipioFilialCliOrigem, Long> {
	private MunicipioFilialService municipioFilialService;
	private VigenciaService vigenciaService;

	public ResultSetPage findPaginated(Map criteria) {
		List<String> included = new ArrayList<String>();
		included.add("idMunicipioFilialCliOrigem");
		included.add("cliente.pessoa.nmPessoa");
		included.add("cliente.pessoa.tpIdentificacao");
		included.add("cliente.pessoa.nrIdentificacaoFormatado");
		included.add("dtVigenciaInicial");
		included.add("dtVigenciaFinal");

		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public List findFilialCliVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialCliOrigemDAO().findCliOrigemVigenteByMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica se existem registros para o atendimento e cliente informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param idCliente
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialCliente(Long idMunicipioFilial, Long idCliente, YearMonthDay dtVigencia){
		return getMunicipioFilialCliOrigemDAO().verificaExisteMunicipioFilialCliente(idMunicipioFilial, idCliente, dtVigencia);
	}

	public boolean existsMunicipioFilialClienteOrigemVigente(Long idMunicipioFilial, YearMonthDay dtVigencia) {
		Integer nrRows = getMunicipioFilialCliOrigemDAO().findRowCountMunicipioFilialClienteOrigem(idMunicipioFilial, dtVigencia);
		return CompareUtils.gt(nrRows, IntegerUtils.ZERO);
	} 

	public boolean existsMunicipioFilialClienteOrigem(Long idMunicipioFilial) {
		Integer nrRows = getMunicipioFilialCliOrigemDAO().findRowCountMunicipioFilialClienteOrigem(idMunicipioFilial, null);
		return CompareUtils.gt(nrRows, IntegerUtils.ZERO);
	}

	public boolean validateMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
		return getMunicipioFilialCliOrigemDAO().verificaMunicipioVigenciaFutura(idMunicipio, idFilial, dtVigencia);
	}

	protected MunicipioFilialCliOrigem beforeStore(MunicipioFilialCliOrigem bean) {
		MunicipioFilialCliOrigem munFilCli = (MunicipioFilialCliOrigem)bean;

		// Testando LMS-29022
		municipioFilialService.validateVigenciaAtendimento(munFilCli.getMunicipioFilial().getIdMunicipioFilial(), munFilCli.getDtVigenciaInicial(), munFilCli.getDtVigenciaFinal());

		// Testando LMS-00003
		if(!getMunicipioFilialCliOrigemDAO().verificaRemetentesAtendidos(munFilCli))
			throw new BusinessException("LMS-00003");

		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>MunFilCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public MunicipioFilialCliOrigem findById(java.lang.Long id) {
		return (MunicipioFilialCliOrigem) super.findById(id);
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
	 *
	 *
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
	public java.io.Serializable store(MunicipioFilialCliOrigem bean) {
		return super.store(bean);
	}

	public Map storeMap(Map bean) {
		MunicipioFilialCliOrigem municipioFilial = new MunicipioFilialCliOrigem();
		ReflectionUtils.copyNestedBean(municipioFilial, bean);

		vigenciaService.validaVigenciaBeforeStore(municipioFilial);

		super.store(municipioFilial);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMunicipioFilialCliOrigem", municipioFilial.getIdMunicipioFilialCliOrigem());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(municipioFilial));

		return retorno;
	}

	protected void beforeRemoveByIds(List ids) {
		MunicipioFilialCliOrigem bean = null;
		for(Iterator<Long> ie = ids.iterator(); ie.hasNext();) {
			bean = findById(ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}

	//busca todos os clientes vigentes de um municipio atendido 
	public List findCliAtendidosByMunicipioFilial(Long idMunicipioFilial) {
		return getMunicipioFilialCliOrigemDAO().findCliAtendidosByMunicipioFilial(idMunicipioFilial);
	}

	// **************************GETTER AND SETTER*************************************//
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioFilialCliOrigemDAO(MunicipioFilialCliOrigemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MunicipioFilialCliOrigemDAO getMunicipioFilialCliOrigemDAO() {
		return (MunicipioFilialCliOrigemDAO) getDao();
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

}