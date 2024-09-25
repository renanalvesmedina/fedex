package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTranspRodoMotoristaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTranspRodoMotoristaService"
 */
public class MeioTranspRodoMotoristaService extends CrudService<MeioTranspRodoMotorista, Long> {
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>MeioTranspRodoMotorista</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTranspRodoMotorista findById(java.lang.Long id) {
		return (MeioTranspRodoMotorista)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>MeioTranspRodoMotorista</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map findByIdDetalhamento(java.lang.Long id) {
		MeioTranspRodoMotorista meioTranspRodoMotorista = (MeioTranspRodoMotorista)super.findById(id);
		MeioTransporteRodoviario meioTransporteRodoviario = meioTranspRodoMotorista.getMeioTransporteRodoviario();
		MeioTransporte meioTransporte = meioTransporteRodoviario.getMeioTransporte();
		Motorista motorista = meioTranspRodoMotorista.getMotorista();
		Pessoa pessoa = motorista.getPessoa();

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMeioTranspRodoMotorista",meioTranspRodoMotorista.getIdMeioTranspRodoMotorista());
		retorno.put("motorista.idMotorista",motorista.getIdMotorista());
		retorno.put("motorista.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
		String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
		retorno.put("motorista.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
		retorno.put("motorista.pessoa.nmPessoa",pessoa.getNmPessoa());

		retorno.put("meioTransporteRodoviario2.idMeioTransporte",meioTransporte.getIdMeioTransporte());
		retorno.put("meioTransporteRodoviario2.meioTransporte.nrFrota",meioTransporte.getNrFrota());
		retorno.put("meioTransporteRodoviario.idMeioTransporte",meioTransporte.getIdMeioTransporte());
		retorno.put("meioTransporteRodoviario.meioTransporte.nrIdentificador",meioTransporte.getNrIdentificador());

		retorno.put("dtVigenciaInicial",meioTranspRodoMotorista.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal",meioTranspRodoMotorista.getDtVigenciaFinal());
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspRodoMotorista);
		retorno.put("acaoVigenciaAtual",acaoVigencia);
		return retorno;
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
	public java.io.Serializable store(MeioTranspRodoMotorista bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeMap(Map bean) {
		MeioTranspRodoMotorista meioTranspRodoMotorista = new MeioTranspRodoMotorista();

		ReflectionUtils.copyNestedBean(meioTranspRodoMotorista,bean);

		vigenciaService.validaVigenciaBeforeStore(meioTranspRodoMotorista);

		super.store(meioTranspRodoMotorista);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMeioTranspRodoMotorista",meioTranspRodoMotorista.getIdMeioTranspRodoMotorista());
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspRodoMotorista);
		retorno.put("acaoVigenciaAtual",acaoVigencia);
		return retorno;
	}

	protected MeioTranspRodoMotorista beforeStore(MeioTranspRodoMotorista bean) {
		MeioTranspRodoMotorista mt = (MeioTranspRodoMotorista)bean;
		if (validateApenasUmVigente(
				mt.getIdMeioTranspRodoMotorista(),
				mt.getMeioTransporteRodoviario().getIdMeioTransporte(),
				mt.getDtVigenciaInicial(),
				mt.getDtVigenciaFinal()))
			throw new BusinessException("LMS-00003");
		return super.beforeStore(bean);
	}

	/**
	 * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {
		MeioTranspRodoMotorista meioTranspRodoMotorista = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(meioTranspRodoMotorista);
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

	/**
	 * Valida se já existe algum registro vigente com mesmo Meio de Transporte para um motorista.
	 * 
	 * @param idMeioTranspRodoMotorista Caso deseja-se excluir uma associação da validação.
	 * @param idMeioTransporte
	 * @return
	 */
	public boolean validateApenasUmVigente(Long idMeioTranspRodoMotorista, Long idMeioTransporte, 
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) { 
		return getMeioTranspRodoMotoristaDAO().validateApenasUmVigente(idMeioTranspRodoMotorista,
				idMeioTransporte,dtVigenciaInicial,dtVigenciaFinal);
	}

	public MeioTranspRodoMotorista findRelacaoVigente(Long idMotorista, Long idMeioTransporte, 
			YearMonthDay dtReferencia) {
		return getMeioTranspRodoMotoristaDAO().findRelacaoVigente(idMotorista,
				idMeioTransporte,dtReferencia);
	}
	
	public List<Motorista> findMotoristaVigenteBySolicitacaoContratacao(SolicitacaoContratacao solicitacaoContratacao){
		return getMeioTranspRodoMotoristaDAO().findMotoristaVigenteBySolicitacaoContratacao(solicitacaoContratacao);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTranspRodoMotoristaDAO(MeioTranspRodoMotoristaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTranspRodoMotoristaDAO getMeioTranspRodoMotoristaDAO() {
		return (MeioTranspRodoMotoristaDAO) getDao();
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}