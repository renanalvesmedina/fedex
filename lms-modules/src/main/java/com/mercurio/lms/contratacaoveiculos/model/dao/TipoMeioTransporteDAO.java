package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoMeioTransporteDAO extends BaseCrudDao<TipoMeioTransporte, Long> {

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("tipoMeioTransporte", FetchMode.JOIN);
	}

	public List findListByCriteria(Map criterions) { 
		List lista = new ArrayList();
		lista.add("dsTipoMeioTransporte");
		return super.findListByCriteria(criterions,lista);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
		fetchModes.put("eixosTipoMeioTransporte", FetchMode.SELECT);
	}

	protected void initFindListLazyProperties(Map fetchModes) {
		fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
		fetchModes.put("eixosTipoMeioTransporte", FetchMode.SELECT);
		fetchModes.put("tipoMeioTransporte.eixosTipoMeioTransporte", FetchMode.SELECT);
		super.initFindListLazyProperties(fetchModes);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TipoMeioTransporte.class;
	}

	public List findTiposSemComposicao(Map criteria) {
		List filters = new ArrayList();
		StringBuffer hql = new StringBuffer()
		.append("from ").append(getPersistentClass().getName()).append(" TMT ")
		.append(" left join FETCH TMT.tipoMeioTransporte TMTC ")
		.append(" where not exists (select TMT2.id from ").append(getPersistentClass().getName()).append(" TMT2 ")
		.append("					where TMT2.tipoMeioTransporte.id = TMT.id AND TMT2.tpSituacao = ? ")
		.append(") ");
		filters.add("A");

		if(criteria != null) {
			if (StringUtils.isNotBlank((String)criteria.get("tpMeioTransporte"))) {
				hql.append("AND TMT.tpMeioTransporte = ? ");
				filters.add(criteria.get("tpMeioTransporte"));
			}
			if (StringUtils.isNotBlank((String)criteria.get("tpSituacao"))) {
				hql.append("AND TMT.tpSituacao = ? ");
				filters.add(criteria.get("tpSituacao"));
			}
		}
		hql.append(" order by TMT.dsTipoMeioTransporte");
		return getAdsmHibernateTemplate().find(hql.toString(),filters.toArray());
	}

	public List findComposicoesByTipo(Long idTipoMeioTransporte) {
		StringBuffer hql = new StringBuffer()
				.append("select TMT from ").append(getPersistentClass().getName()).append(" TMT ")
				.append("where TMT.tipoMeioTransporte.id = ? ")
				.append("  and TMT.tpSituacao = ?");
		
		return getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idTipoMeioTransporte,"A"});
	}

	public TipoMeioTransporte findTipoMeioTransporteCompostoByIdMeioTransporte(Long idMeioTransporte){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("tmt_c");

		sql.addFrom("MeioTransporte mt inner join mt.modeloMeioTransporte mmt " +
									"inner join mmt.tipoMeioTransporte tmt " +
									"inner join tmt.tipoMeioTransporte tmt_c");

		sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);

		List retorno = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		return (TipoMeioTransporte) (retorno.size() > 0 ? retorno.get(0) : null);
	}
	

	public TipoMeioTransporte findTipoMeioTransporteByIdMeioTransporte( Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("tmt");

		sql.addFrom("MeioTransporte mt inner join mt.modeloMeioTransporte mmt " +
									"inner join mmt.tipoMeioTransporte tmt ");

		sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);

		List retorno = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		return (TipoMeioTransporte) (retorno.size() > 0 ? retorno.get(0) : null);
	}

	public void storeTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
		if (tipoMeioTransporte.getIdTipoMeioTransporte() != null) {
			TipoMeioTransporte tipoMeioTransporteOld = (TipoMeioTransporte) get(getPersistentClass(), tipoMeioTransporte.getIdTipoMeioTransporte());
			List<EixosTipoMeioTransporte> eixos = tipoMeioTransporteOld.getEixosTipoMeioTransporte();

			if(tipoMeioTransporte.getEixosTipoMeioTransporte() == null) {
				eixos.clear();
			} else {
				final List<EixosTipoMeioTransporte> listaEixos = tipoMeioTransporte.getEixosTipoMeioTransporte();

				for(Iterator<EixosTipoMeioTransporte> it = eixos.iterator(); it.hasNext() ;) {
					EixosTipoMeioTransporte eixo = it.next();
					if(!listaEixos.contains(eixo)) {
						it.remove();
					} else {
						eixo.setQtEixos(listaEixos.get(listaEixos.indexOf(eixo)).getQtEixos());
					}
				}
				for(EixosTipoMeioTransporte eixo : listaEixos) {
					if(eixo.getIdEixosTipoMeioTransporte() == null) {
						eixos.add(eixo);
					}
				}
			}
			tipoMeioTransporte.setEixosTipoMeioTransporte(eixos);
			getAdsmHibernateTemplate().evict(tipoMeioTransporteOld);
		}
		store(tipoMeioTransporte);
	}

	/**
	 * Retorna o max da quantidade de eixos do tipo de meio de transporte informado
	 * @param idTipoMeioTransporte
	 * @return
	 */
	public List findEixosTipoMeioTransporte(Long idTipoMeioTransporte){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(emt.qtEixos as qtEixos)");
		sql.addFrom("EixosTipoMeioTransporte emt");
		sql.addCriteria("emt.tipoMeioTransporte.id", "=", idTipoMeioTransporte);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna o max da quantidade de eixos do tipo de meio de transporte informado
	 * @param idTipoMeioTransporte
	 * @return
	 */
	public Integer findQuantidadeEixosTipoMeioTransporte(Long idTipoMeioTransporte){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("nvl(max(emt.qtEixos),0)");
		sql.addFrom("EixosTipoMeioTransporte emt");
		sql.addCriteria("emt.tipoMeioTransporte.id", "=", idTipoMeioTransporte);

		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		return (Integer)(result.isEmpty() ? null : result.get(0));
	}

	public List findCombo(Map criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(tmt.idTipoMeioTransporte as idTipoMeioTransporte ");
		sql.addProjection("tmt.dsTipoMeioTransporte as dsTipoMeioTransporte");
		sql.addProjection("tmt.tpSituacao as tpSituacao)");
		sql.addFrom(TipoMeioTransporte.class.getName(), "tmt");
		if (criteria != null) {
			if (criteria.get("tpSituacao") != null) {
				sql.addCustomCriteria(" tmt.tpSituacao = ?");
				sql.addCriteriaValue(criteria.get("tpSituacao"));
			}
			if (criteria.get("tpMeioTransporte") != null) {
				sql.addCustomCriteria(" tmt.tpMeioTransporte = ?");
				sql.addCriteriaValue(criteria.get("tpMeioTransporte"));
			}
		}
		sql.addOrderBy("tmt.dsTipoMeioTransporte");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
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
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("tpCategoria",tpCategoria));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

}