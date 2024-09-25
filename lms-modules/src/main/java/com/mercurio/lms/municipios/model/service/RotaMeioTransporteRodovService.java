package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaMeioTransporteRodov;
import com.mercurio.lms.municipios.model.RotaTipoMeioTransporte;
import com.mercurio.lms.municipios.model.dao.RotaMeioTransporteRodovDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.rotaMeioTransporteRodovService"
 */
public class RotaMeioTransporteRodovService extends CrudService<RotaMeioTransporteRodov, Long> {
	private VigenciaService vigenciaService;
	private MeioTranspProprietarioService meioTranspProprietarioService;

	private RotaTipoMeioTransporteService rotaTipoMeioTransporteService;
	/**
	 * Recupera uma inst�ncia de <code>RotaMeioTransporteRodov</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
	public RotaMeioTransporteRodov findById(java.lang.Long id) {
		return (RotaMeioTransporteRodov)super.findById(id);
	}

	public ResultSetPage findPaginatedPrincipal(TypedFlatMap criteria) {
		ResultSetPage aux = getRotaMeioTransporteRodovDAO().findPaginatedPrincipal(criteria,FindDefinition.createFindDefinition(criteria));
		List<Object[]> result = aux.getList();
		List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
		for(Object[] obj : result) {
			Map<String, Object> bean = new HashMap<String, Object>();
			bean.put("idRotaMeioTransporteRodov",obj[0]);
			bean.put("sgFilial", obj[1].toString());
			bean.put("pessoaFilial", obj[2].toString());

			bean.put("dtVigenciaInicial", obj[3]);
			if(obj[4]!= null )
				bean.put("dtVigenciaFinal", obj[4]);

			if (obj[5]!= null && obj[6] != null ){
				String str = obj[5].toString() + " - " + obj[6].toString();
				bean.put("frotaIdentificador", str);
			}

			bean.put("nrFrota", obj[5].toString());
			bean.put("nrIdentificador", obj[6].toString());

			if(obj[7]!= null)
				bean.put("proprietario", obj[7].toString());

			lista.add(bean);
		}
		aux.setList(lista);
		return aux;
	}

	public Integer getRowCountPrincipal(TypedFlatMap criteria) {
		return getRotaMeioTransporteRodovDAO().getRowCountPrincipal(criteria);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(RotaMeioTransporteRodov bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setRotaMeioTransporteRodovDAO(RotaMeioTransporteRodovDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private RotaMeioTransporteRodovDAO getRotaMeioTransporteRodovDAO() {
		return (RotaMeioTransporteRodovDAO) getDao();
	}

	public TypedFlatMap findByIdDetalhamento(java.lang.Long id) { 
		RotaMeioTransporteRodov rotaMeioTransporteRodov = (RotaMeioTransporteRodov)super.findById(id);
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rotaMeioTransporteRodov);

		TypedFlatMap mapRotaMeioTransporteRodov = new TypedFlatMap();

		mapRotaMeioTransporteRodov.put("acaoVigenciaAtual", acaoVigencia);

		final RotaTipoMeioTransporte rotaTipoMeioTransporte = rotaMeioTransporteRodov.getRotaTipoMeioTransporte();
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.idRotaTipoMeioTransporte", rotaTipoMeioTransporte.getIdRotaTipoMeioTransporte());
		final RotaColetaEntrega rotaColetaEntrega = rotaTipoMeioTransporte.getRotaColetaEntrega();
		final Filial filial = rotaColetaEntrega.getFilial();
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.rotaColetaEntrega.filial.idFilial", filial.getIdFilial());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.rotaColetaEntrega.filial.sgFilial", filial.getSgFilial());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.rotaColetaEntrega.filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.rotaColetaEntrega.nrRota", rotaColetaEntrega.getNrRota());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.rotaColetaEntrega.dsRota", rotaColetaEntrega.getDsRota());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",rotaTipoMeioTransporte.getTipoMeioTransporte().getIdTipoMeioTransporte());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte",rotaTipoMeioTransporte.getTipoMeioTransporte().getDsTipoMeioTransporte());
		mapRotaMeioTransporteRodov.put("rotaTipoMeioTransporte.tipoMeioTransporte.tpMeioTransporte",rotaTipoMeioTransporte.getTipoMeioTransporte().getTpMeioTransporte().getValue());
		mapRotaMeioTransporteRodov.put("dtVigenciaInicial", rotaMeioTransporteRodov.getDtVigenciaInicial());
		mapRotaMeioTransporteRodov.put("dtVigenciaFinal", rotaMeioTransporteRodov.getDtVigenciaFinal());
		mapRotaMeioTransporteRodov.put("idRotaMeioTransporteRodov",rotaMeioTransporteRodov.getIdRotaMeioTransporteRodov());
		mapRotaMeioTransporteRodov.put("meioTransporteRodoviario2.meioTransporte.nrFrota", rotaMeioTransporteRodov.getMeioTransporteRodoviario().getMeioTransporte().getNrFrota());
		mapRotaMeioTransporteRodov.put("meioTransporteRodoviario.idMeioTransporte", rotaMeioTransporteRodov.getMeioTransporteRodoviario().getIdMeioTransporte());
		mapRotaMeioTransporteRodov.put("meioTransporteRodoviario.meioTransporte.nrIdentificador", rotaMeioTransporteRodov.getMeioTransporteRodoviario().getMeioTransporte().getNrIdentificador());
		mapRotaMeioTransporteRodov.put("proprietario.nmPessoa", findProprietarioByMeioTransporte(rotaMeioTransporteRodov.getMeioTransporteRodoviario().getIdMeioTransporte()).get("nmPessoa"));
		return mapRotaMeioTransporteRodov;
	}

	private void validaRemoveById(Long id) {
		RotaMeioTransporteRodov rotaMeioTransporteRodov = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(rotaMeioTransporteRodov);
	}
	
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		validaRemoveById((Long)id);
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids)
			validaRemoveById(id);
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map<String, Object> storeMap(Map<String, Object> bean) {
		RotaMeioTransporteRodov rotaMeioTransporteRodov = new RotaMeioTransporteRodov();
		ReflectionUtils.copyNestedBean(rotaMeioTransporteRodov,bean);
		vigenciaService.validaVigenciaBeforeStore(rotaMeioTransporteRodov);

		// Antes de chamar o beforeStore, eu materializo os objetos devido regra necess�ria no beforeStore. Fa�o esse load aqui, devido beforeStore 
		// ser chamado por outros m�todos que n�o esse map, assim n�o preciso tratar FK NULL
		MeioTransporteRodoviario meioTransporteRodoviario = (MeioTransporteRodoviario)getDao().getAdsmHibernateTemplate().load(MeioTransporteRodoviario.class, rotaMeioTransporteRodov.getMeioTransporteRodoviario().getIdMeioTransporte());
		rotaMeioTransporteRodov.setMeioTransporteRodoviario(meioTransporteRodoviario);

		RotaTipoMeioTransporte rotaTipoMeioTransporte = (RotaTipoMeioTransporte)getDao().getAdsmHibernateTemplate().load(RotaTipoMeioTransporte.class, rotaMeioTransporteRodov.getRotaTipoMeioTransporte().getIdRotaTipoMeioTransporte());
		rotaMeioTransporteRodov.setRotaTipoMeioTransporte(rotaTipoMeioTransporte);

		super.store(rotaMeioTransporteRodov);
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rotaMeioTransporteRodov);
		bean.put("acaoVigenciaAtual", acaoVigencia);
		bean.put("idRotaMeioTransporteRodov", rotaMeioTransporteRodov.getIdRotaMeioTransporteRodov());
		return bean;
	}

	@Override
	protected RotaMeioTransporteRodov beforeStore(RotaMeioTransporteRodov bean) {
		Long idTipoMeioTransporte = bean.getMeioTransporteRodoviario().getMeioTransporte().getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte();
		if( (!idTipoMeioTransporte.equals(bean.getRotaTipoMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte()))&&
			(bean.getRotaTipoMeioTransporte().getTipoMeioTransporte().getTipoMeioTransporte() == null || 
					(bean.getRotaTipoMeioTransporte().getTipoMeioTransporte().getTipoMeioTransporte() != null
							&& !idTipoMeioTransporte.equals(bean.getRotaTipoMeioTransporte().getTipoMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte())
					)
			) 
		) {
			throw new BusinessException("LMS-29165");
		}

		if (getRotaMeioTransporteRodovDAO().verificaRotaMeioTransporteVigente(bean)) {
			throw new BusinessException("LMS-00003");
		}
		if (rotaTipoMeioTransporteService.findRotaTipoMeioTransporteById(bean.getRotaTipoMeioTransporte().getIdRotaTipoMeioTransporte(),
				bean.getDtVigenciaInicial(), bean.getDtVigenciaFinal()).size() == 0)
					throw new BusinessException("LMS-29023");
		return super.beforeStore(bean);
	}

	public Map<String, Object> findProprietarioByMeioTransporte(Long idMeioTransporteRodoviario){
		Map<String, Object> retorno = new HashMap<String, Object>();
		Map<String, Object> aux = meioTranspProprietarioService.findInfoMeioTransporte(idMeioTransporteRodoviario);
		if (aux != null){
			retorno.put("nmPessoa",aux.get("nmPessoa"));
		}
		return retorno;
	}

	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setRotaTipoMeioTransporteService(RotaTipoMeioTransporteService rotaTipoMeioTransporteService) {
		this.rotaTipoMeioTransporteService = rotaTipoMeioTransporteService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}