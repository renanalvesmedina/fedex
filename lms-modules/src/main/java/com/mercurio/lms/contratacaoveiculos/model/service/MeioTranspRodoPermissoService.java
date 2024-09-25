package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoPermisso;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTranspRodoPermissoDAO;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTranspRodoPermissoService"
 */
public class MeioTranspRodoPermissoService extends CrudService<MeioTranspRodoPermisso, Long> {
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>MeioTranspRodoPermisso</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTranspRodoPermisso findById(java.lang.Long id) {
		return (MeioTranspRodoPermisso)super.findById(id);
	}
	/**
	 * Metodo chamado ao detalhar um registro, verifica se as vigencias estao vigentes e passa um flag para o javascript
	 */
	public TypedFlatMap findByIdEValidaDtVigencia(java.lang.Long id) {
		MeioTranspRodoPermisso meioTranspRodoPermisso = (MeioTranspRodoPermisso)findById(id);
		TypedFlatMap mapMeioTranspRodoPermisso = new TypedFlatMap();

		mapMeioTranspRodoPermisso.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspRodoPermisso));
		final MeioTransporteRodoviario meioTransporteRodoviario = meioTranspRodoPermisso.getMeioTransporteRodoviario();
		mapMeioTranspRodoPermisso.put("meioTransporteRodoviario.meioTransporte.nrIdentificador",meioTransporteRodoviario.getMeioTransporte().getNrIdentificador());
		mapMeioTranspRodoPermisso.put("meioTransporteRodoviario.idMeioTransporte",meioTransporteRodoviario.getIdMeioTransporte());
		mapMeioTranspRodoPermisso.put("meioTransporteRodoviario.meioTransporte.nrFrota",meioTransporteRodoviario.getMeioTransporte().getNrFrota());
		final Pais pais = meioTranspRodoPermisso.getPais();
		mapMeioTranspRodoPermisso.put("pais.nmPais", pais.getNmPais().getValue());
		mapMeioTranspRodoPermisso.put("pais.idPais",pais.getIdPais());
		mapMeioTranspRodoPermisso.put("nrPermisso",meioTranspRodoPermisso.getNrPermisso());
		mapMeioTranspRodoPermisso.put("dtVigenciaInicial",meioTranspRodoPermisso.getDtVigenciaInicial());
		mapMeioTranspRodoPermisso.put("dtVigenciaFinal",meioTranspRodoPermisso.getDtVigenciaFinal());
		mapMeioTranspRodoPermisso.put("idMeioTranspRodoPermisso",meioTranspRodoPermisso.getIdMeioTranspRodoPermisso());

		return mapMeioTranspRodoPermisso; 
	}
	
	//VALIDA SE AS VIGENCIAS SAO MAIORES QUE A DATA ATUAL
	//VERIFICA SE EXISTEM REGISTROS VIGENTES PARA UM MESMO PAIS E MEIO DE TRANSPORTE RODOVIARIO
	protected MeioTranspRodoPermisso beforeStore(MeioTranspRodoPermisso bean) {
		MeioTranspRodoPermisso meioTranspRodoPermisso = (MeioTranspRodoPermisso)bean;
		if(getMeioTranspRodoPermissoDAO().findMeioTranspRodoByVigencia(meioTranspRodoPermisso)){
			throw new BusinessException("LMS-00003");
		}
		return super.beforeStore(bean);
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
	public java.io.Serializable store(MeioTranspRodoPermisso bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(Map bean) {
		MeioTranspRodoPermisso meioTranspRodoPermisso = new MeioTranspRodoPermisso();

		ReflectionUtils.copyNestedBean(meioTranspRodoPermisso,bean);

		vigenciaService.validaVigenciaBeforeStore(meioTranspRodoPermisso);

		super.store(meioTranspRodoPermisso);
		bean.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspRodoPermisso));
		bean.put("idMeioTranspRodoPermisso",meioTranspRodoPermisso.getIdMeioTranspRodoPermisso());

		return bean;
	}

	protected void beforeRemoveById(Long id) {
		MeioTranspRodoPermisso meioTranspRodoPermisso = (MeioTranspRodoPermisso)findById(id);
		if(meioTranspRodoPermisso.getDtVigenciaInicial()!= null){
			JTVigenciaUtils.validaVigenciaRemocao(meioTranspRodoPermisso);
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		if (!ids.isEmpty()) {
			for (int i = 0; i < ids.size(); i++) {
				MeioTranspRodoPermisso meioTranspRodoPermisso = (MeioTranspRodoPermisso)findById((Long)ids.get(0));
				if(meioTranspRodoPermisso.getDtVigenciaInicial() != null){
					JTVigenciaUtils.validaVigenciaRemocao(meioTranspRodoPermisso);
				}
			}
		}
		super.beforeRemoveByIds(ids);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTranspRodoPermissoDAO(MeioTranspRodoPermissoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTranspRodoPermissoDAO getMeioTranspRodoPermissoDAO() {
		return (MeioTranspRodoPermissoDAO) getDao();
	}

}