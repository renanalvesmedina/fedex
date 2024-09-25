package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.TipoMeioTransporteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.tipoMeioTransporteService"
 */
public class TipoMeioTransporteService extends CrudService<TipoMeioTransporte, Long> {
	private EixosTipoMeioTransporteService eixosTipoMeioTransporteService;

	/*REGRAS DE NEGOCIO:
	 *1- Um tipo de meio de transporte não pode ser composto por ele mesmo. Caso isso ocorra a seguinte mensagem deve ser exibida LMS - 26003
	 *2- A quantidade de eixos (QT_EIXOS) não pode ser maior que 7. Caso isso ocorra a seguinte mensagem deve ser exibida LMS-26005.
	 *3- A capacidade de peso final (NR_CAPACIDADE_PESO_FINAL) deve ser maior que a capacidade de peso inicial (NR_CAPACIDADE_PESO_INICIAL). Caso isso ocorra a seguinte mensagem deve ser exibida LMS-26004.
	*/
	protected TipoMeioTransporte beforeStore(TipoMeioTransporte bean) {
		TipoMeioTransporte tipoMeioTransporte = (TipoMeioTransporte)bean;

		if (tipoMeioTransporte.getIdTipoMeioTransporte()!= null){
			if (tipoMeioTransporte.getTipoMeioTransporte() != null){
				if(tipoMeioTransporte.getIdTipoMeioTransporte().compareTo(tipoMeioTransporte.getTipoMeioTransporte().getIdTipoMeioTransporte())==0)
					throw new BusinessException("LMS-26003");
			}
		}

		if (tipoMeioTransporte.getEixosTipoMeioTransporte() != null && !tipoMeioTransporte.getEixosTipoMeioTransporte().isEmpty()){
			for (Iterator iter = tipoMeioTransporte.getEixosTipoMeioTransporte().iterator();iter.hasNext();){
				EixosTipoMeioTransporte eixosTipoMeioTransporte = (EixosTipoMeioTransporte)iter.next();
				if (eixosTipoMeioTransporte.getQtEixos().intValue() > 7){
					throw new BusinessException("LMS-26005");
				}
			}
		}

		if(tipoMeioTransporte.getNrCapacidadePesoFinal().compareTo(tipoMeioTransporte.getNrCapacidadePesoInicial())<=0){
			throw new BusinessException("LMS-26004");
		}
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(TypedFlatMap map) {
		TipoMeioTransporte bean = map2bean(map);

		bean = (TipoMeioTransporte) beforeStore(bean);
		getTipoMeioTransporteDAO().storeTipoMeioTransporte(bean);

		map.put("idTipoMeioTransporte", bean.getIdTipoMeioTransporte());

		return map;
	}

	private TipoMeioTransporte map2bean(TypedFlatMap tfp) {
		TipoMeioTransporte bean = new TipoMeioTransporte();

		bean.setIdTipoMeioTransporte(tfp.getLong("idTipoMeioTransporte"));
		bean.setTpMeioTransporte(tfp.getDomainValue("tpMeioTransporte"));

		bean.setDsTipoMeioTransporte(tfp.getString("dsTipoMeioTransporte"));

		if (tfp.getLong("tipoMeioTransporte.idTipoMeioTransporte") !=null){
			TipoMeioTransporte tipoMeioTransporteComps = new TipoMeioTransporte();
			tipoMeioTransporteComps.setIdTipoMeioTransporte(tfp.getLong("tipoMeioTransporte.idTipoMeioTransporte"));	
			bean.setTipoMeioTransporte(tipoMeioTransporteComps);
		}

		bean.setTpCategoria(tfp.getDomainValue("tpCategoria"));
		bean.setTpSituacao(tfp.getDomainValue("tpSituacao"));

		bean.setNrCapacidadePesoInicial(tfp.getInteger("nrCapacidadePesoInicial"));
		bean.setNrCapacidadePesoFinal(tfp.getInteger("nrCapacidadePesoFinal"));

		List eixosTela = tfp.getList("eixosTipoMeioTransporte");
		List eixos = new ArrayList();
		if (eixosTela!= null && !eixosTela.isEmpty()){
			for (Iterator it = eixosTela.iterator(); it.hasNext();){
				TypedFlatMap item = (TypedFlatMap) it.next();
				EixosTipoMeioTransporte eixosTipoMeioTransporte = new EixosTipoMeioTransporte();
				eixosTipoMeioTransporte.setQtEixos(item.getInteger("qtEixos"));
				if(item.getLong("idEixosTipoMeioTransporte")!= null)
					eixosTipoMeioTransporte.setIdEixosTipoMeioTransporte(item.getLong("idEixosTipoMeioTransporte"));
				eixosTipoMeioTransporte.setTipoMeioTransporte(bean);

				eixos.add(eixosTipoMeioTransporte);
			}
			bean.setEixosTipoMeioTransporte(eixos);
		}
		return bean;
	}

	/**
	 * Recupera uma instância de <code>TipoMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TipoMeioTransporte findById(java.lang.Long id) {
		return (TipoMeioTransporte)super.findById(id);
	}

	/**
	 * Localiza uma lista de resultados a partir dos critérios de busca 
	 * informados, retornando apenas os dados de id, ativo e descrição.
	 * 
	 * @param criteria Critérios de busca.
	 * @return Lista de resultados sem paginação.
	 */
	public List findCombo(Map criteria) {
		return getTipoMeioTransporteDAO().findCombo(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		TipoMeioTransporte tipoMeioTransporte = findById(id);
		List eixos = tipoMeioTransporte.getEixosTipoMeioTransporte();
		removeEixos(eixos);

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
		Iterator itIds = ids.iterator();
		while(itIds.hasNext()){
			removeById((Long) itIds.next());
		}
	}

	private void removeEixos(List eixos){
		Iterator itEixos = eixos.iterator();
		List ids = new ArrayList();
		while(itEixos.hasNext()){
			EixosTipoMeioTransporte eixo = (EixosTipoMeioTransporte) itEixos.next();
			ids.add(eixo.getIdEixosTipoMeioTransporte());
		}
		getEixosTipoMeioTransporteService().removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TipoMeioTransporte bean) {
		return super.store(bean);
	}

	/**
	 * Busca tipos de meio de transporte a partir de uma categoria.
	 * 
	 * Método utilizado pela Integração
	 * @author Felipe Ferreira
	 * 
	 * @param tpCategoria
	 * @return Lista de tipos de meio de transporte
	 */
	public List<TipoMeioTransporte> findTiposMeioTransporte(String tpCategoria) {
		return getTipoMeioTransporteDAO().findTiposMeioTransporte(tpCategoria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTipoMeioTransporteDAO(TipoMeioTransporteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TipoMeioTransporteDAO getTipoMeioTransporteDAO() {
		return (TipoMeioTransporteDAO) getDao();
	}

	//busca os tipo de meio de transporte ativos
	public List findAtivos(Map criteria) {
		criteria.put("tpSituacao","A");
		List lista = super.find(criteria);
		return lista;
	}

	public List findTiposSemComposicao(Map criteria) {
		return getTipoMeioTransporteDAO().findTiposSemComposicao(criteria);
	}

	public TipoMeioTransporte findTipoMeioTransporteCompostoByIdMeioTransporte(Long idMeioTransporte){
		return getTipoMeioTransporteDAO().findTipoMeioTransporteCompostoByIdMeioTransporte(idMeioTransporte);
	}
	
	public TipoMeioTransporte findTipoMeioTransporteByIdMeioTransporte(Long idMeioTransporte) {
		return getTipoMeioTransporteDAO().findTipoMeioTransporteByIdMeioTransporte(idMeioTransporte);
	}

	public List findComposicoesByTipo(Long idTipoMeioTransporte) {
		return getTipoMeioTransporteDAO().findComposicoesByTipo(idTipoMeioTransporte);
	}

	/**
	 * 
	 * @param idTipoMeioTransporte
	 * @return
	 * @see getTipoMeioTransporteDAO().findQuantidadeEixosTipoMeioTransporte(idTipoMeioTransporte)
	 */
	public Integer findQuantidadeEixosTipoMeioTransporte(Long idTipoMeioTransporte){
		return getTipoMeioTransporteDAO().findQuantidadeEixosTipoMeioTransporte(idTipoMeioTransporte);
	}

	/**
	 * @return Returns the eixosTipoMeioTransporteService.
	 */
	public EixosTipoMeioTransporteService getEixosTipoMeioTransporteService() {
		return eixosTipoMeioTransporteService;
	}

	/**
	 * @param eixosTipoMeioTransporteService The eixosTipoMeioTransporteService to set.
	 */
	public void setEixosTipoMeioTransporteService(EixosTipoMeioTransporteService eixosTipoMeioTransporteService) {
		this.eixosTipoMeioTransporteService = eixosTipoMeioTransporteService;
	}



}