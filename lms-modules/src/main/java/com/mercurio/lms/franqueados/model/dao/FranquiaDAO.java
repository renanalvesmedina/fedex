package com.mercurio.lms.franqueados.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.franqueados.model.Franqueado;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class FranquiaDAO extends BaseCrudDao<Franquia, Long> {

	@Override
	protected Class<Franquia> getPersistentClass() {
		return Franquia.class;
	}
	
	@Override
	public Franquia findById(Long id){
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as fr ");
		query.append("inner join fetch fr.filial fil ");
		query.append("inner join fetch fil.pessoa p ");
		query.append("where ");
		query.append(" fr.id = :id ");
			
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
			
		return (Franquia) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	public ResultSetPage findPaginatedFranquia(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlTemplateFilterFranquia(criteria);
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());

	}
	
	 public Integer getRowCountFranquia(Map criteria) {
			Long idEmpresa = criteria.get("empresa.id") != null ? (Long) criteria.get("empresa.id") : null;
			String sgFilial = criteria.get("sgFilial") != null ? (String) criteria.get("sgFilial") : null;
			YearMonthDay vigenteEm = criteria.get("vigenteEm") != null ? (YearMonthDay) criteria.get("vigenteEm") : null;
			String nrIdentificacao = criteria.get("nrIdentificacao") != null ? (String) criteria.get("nrIdentificacao") : null;		
			String tpIdentificacao = criteria.get("tpIdentificacao") != null ? (String) criteria.get("tpIdentificacao") : null;
			String nmPessoa = criteria.get("nmPessoa") != null ? (String) criteria.get("nmPessoa") : null;
			String nmFantasia = criteria.get("nmFantasia") != null ? (String) criteria.get("nmFantasia") : null;
			String tpEmpresa = criteria.get("tpEmpresa") != null ? (String) criteria.get("tpEmpresa") : null;

			SqlTemplate hql = new SqlTemplate();
			hql.addInnerJoin(getPersistentClass().getName(), "fr");
			hql.addInnerJoin("fetch fr.filial ", "fl");
			hql.addInnerJoin("fetch fl.pessoa", "pe");
			hql.addInnerJoin("fetch fl.empresa", "em");
			hql.addInnerJoin("fetch em.pessoa", "ep");
			hql.addInnerJoin("fetch fr.franqueadoFranquias", "ff");
			hql.addCriteria("em.id", "=", idEmpresa);
			hql.addCriteria("em.tpEmpresa", "=", tpEmpresa);
			hql.addCriteria("ep.nmFantasia", "like", nmFantasia);
			hql.addCriteria("fl.sgFilial", "like", sgFilial);
			hql.addCriteria("pe.tpIdentificacao", "=", tpIdentificacao);
			hql.addCriteria("pe.nrIdentificacao", "=", nrIdentificacao);
			hql.addCriteria("pe.nmPessoa", "like", nmPessoa);
			hql.addCriteria("ff.dtVigenciaInicial", "<=", vigenteEm);
			hql.addCriteria("ff.dtVigenciaFinal", ">=", vigenteEm);

	    	return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	 }
	 
	private SqlTemplate getSqlTemplateFilterFranquia(Map criteria) {
		Long idEmpresa = criteria.get("franquia.filial.empresa.idEmpresa") != null ? (Long) criteria.get("franquia.filial.empresa.idEmpresa") : null;
		String sgFilial = criteria.get("franquia.filial.sgFilial") != null ? (String) criteria.get("franquia.filial.sgFilial") : null;
		YearMonthDay vigenteEm = criteria.get("vigenteEm") != null ? (YearMonthDay) criteria.get("vigenteEm") : null;
		String nrIdentificacao = criteria.get("franquia.filial.pessoa.nrIdentificacao") != null ? (String) criteria.get("franquia.filial.pessoa.nrIdentificacao") : null;		
		String tpIdentificacao = criteria.get("franquia.filial.pessoa.tpIdentificacao") != null ? (String) criteria.get("franquia.filial.pessoa.tpIdentificacao") : null;
		String nmPessoa = criteria.get("franquia.filial.pessoa.nmPessoa=null") != null ? (String) criteria.get("franquia.filial.pessoa.nmPessoa=null") : null;
		String nmFantasia = criteria.get("franquia.filial.empresa.pessoa.nmFantasia") != null ? (String) criteria.get("franquia.filial.empresa.pessoa.nmFantasia") : null;
		String tpEmpresa = criteria.get("franquia.filial.empresa.pessoa.tpEmpresa") != null ? (String) criteria.get("franquia.filial.empresa.pessoa.tpEmpresa") : null;

		SqlTemplate hql = new SqlTemplate();
		hql.addInnerJoin(getPersistentClass().getName(), "fr");
		hql.addInnerJoin("fetch fr.filial ", "fl");
		hql.addInnerJoin("fetch fl.pessoa", "pe");
		hql.addInnerJoin("fetch fl.empresa", "em");
		hql.addInnerJoin("fetch em.pessoa", "ep");
		hql.addInnerJoin("fetch fr.franqueadoFranquias", "ff");
		hql.addCriteria("em.id", "=", idEmpresa);
		hql.addCriteria("em.tpEmpresa", "=", tpEmpresa);
		hql.addCriteria("ep.nmFantasia", "like", nmFantasia);
		hql.addCriteria("fl.sgFilial", "like", sgFilial);
		hql.addCriteria("pe.tpIdentificacao", "=", tpIdentificacao);
		hql.addCriteria("pe.nrIdentificacao", "=", nrIdentificacao);
		hql.addCriteria("pe.nmPessoa", "like", nmPessoa);
		hql.addCriteria("ff.dtVigenciaInicial", "<=", vigenteEm);
		hql.addCriteria("ff.dtVigenciaFinal", ">=", vigenteEm);

    	return hql;
	}		
	
	public Franquia findLookupFranquia(Map criteria) {
		StringBuilder hql = new StringBuilder("select fr from 	");
		hql.append(Franquia.class.getName()).append(" as fr 	");
		hql.append(" join fr.filial fl							");
		hql.append(" join fl.pessoa pe							");
		hql.append(" join fl.empresa em							");
		hql.append(" join em.pessoa em							");
		hql.append(" join fr.franqueadoFranquias ff				");
		hql.append(" where 										");
		hql.append(" 	em.id = :idEmpresa and					");
		hql.append(" 	fr.id = :idFranquia	and	 				");
		hql.append(" 	ff.dtVigenciaInicial <= :hoje and		");
		hql.append("	ff.dtVigenciaFinal >= :hoje 			");
		
		YearMonthDay hoje = new YearMonthDay();
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idEmpresa", criteria.get("filial.empresa.id"));
		parametros.put("idFranquia", criteria.get("idFranquia"));
		parametros.put("hoje", hoje);
		
		List<Franquia> franquia = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
		return franquia.isEmpty() ? null : franquia.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Franquia> findFranquiasAtivasByIdFranquiaVigencia(Long idFranquia, YearMonthDay data) {
		
		StringBuilder hql = new StringBuilder("select f from ");
		hql.append(Franquia.class.getName()).append(" as f ");
		hql.append(" join f.franqueadoFranquias ff ");
		
		hql.append(" where ff.dtVigenciaInicial <= :data ");
		hql.append(" and ff.dtVigenciaFinal >= :data ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("data", data);
		
		if(idFranquia != null){
			hql.append(" and f.idFranquia = :idFranquia");
			parametros.put("idFranquia", idFranquia);
		}

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<Franquia> findFranquiasAtivasByCompetencia(YearMonthDay data,Long idFranquia) {
		
		StringBuilder hql = new StringBuilder("select f from ");
		hql.append(Franquia.class.getName()).append(" as f ");
		hql.append(" join f.franqueadoFranquias ff ");
		
		hql.append(" where ff.dtVigenciaInicial <= :data ");
		hql.append(" and ff.dtVigenciaFinal >= :data ");
		if( idFranquia != null ){
			hql.append(" and f.idFranquia = :idFranquia ");
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("data", data);
		if( idFranquia != null ){
			parametros.put("idFranquia", idFranquia);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}

	/**
	 * Busca franquia pela sua SIGLA
	 * @param sgFranquia
	 * @param idEmpresa
	 * @param dtVigencia
	 * @return
	 */
	public Franquia findFranquiasAtivasBySiglaFranquia(String sgFranquia, Long idEmpresa, YearMonthDay dtVigencia) {
		
		StringBuilder hql = new StringBuilder("select fr from 	");
		hql.append(Franquia.class.getName()).append(" as fr 	");
		hql.append(" join fr.filial fl							");
		hql.append(" join fl.pessoa pe							");
		hql.append(" join fl.empresa em							");
		hql.append(" join em.pessoa em							");
		hql.append(" join fr.franqueadoFranquias ff				");
		hql.append(" where 										");
		hql.append(" 	em.id = :idEmpresa and					");
		hql.append(" 	fl.sgFilial = :sgFranquia	and			");
		hql.append(" 	ff.dtVigenciaInicial <= :dtVigencia and	");
		hql.append("	ff.dtVigenciaFinal >= :dtVigencia 		");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idEmpresa", idEmpresa);
		parametros.put("sgFranquia", sgFranquia);
		parametros.put("dtVigencia", dtVigencia == null? new YearMonthDay() : dtVigencia);
		
		List<Franquia> franquias = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros); 
		return franquias.isEmpty() ? null : franquias.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> isTpOperacaoEntreFranquias(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia) {
		
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(Franquia.class.getName()).append(" as f ");
		hql.append(" join f.franqueadoFranquias ff ");
		
		hql.append(" where ff.dtVigenciaInicial <= :data ");
		hql.append(" and ff.dtVigenciaFinal >= :data ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("data", dtVigencia);
		
		if (idFilialOrigem != null && idFilialDestino != null) {
			hql.append(" and (f.idFranquia = :idFranquiaOrigem or f.idFranquia = :idFranquiaDestino)");
			parametros.put("idFranquiaOrigem", idFilialOrigem);
			parametros.put("idFranquiaDestino", idFilialDestino);
		}

		return  getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findDadosFranquia(Long idFranquia) {
		
		String query = " SELECT P.nmPessoa, "
				+ " P.nrIdentificacao, "
				+ " EP.dsEndereco, "
				+ " EP.nrEndereco, "
				+ " M.nmMunicipio, "
				+ " UF.sgUnidadeFederativa, "
				+ " FI.sgFilial "
				+ " FROM " 
				+ Franquia.class.getName() + " F, " 
				+ Filial.class.getName() + " FI, " 
				+ FranqueadoFranquia.class.getName() + " FF, " 
				+ Franqueado.class.getName() + " FR, " 
				+ Pessoa.class.getName() + " P, "
				+ EnderecoPessoa.class.getName() + " EP, " 
				+ Municipio.class.getName() + " M, " 
				+ UnidadeFederativa.class.getName() + " UF "
				+ " WHERE F.idFranquia = FF.franquia.idFranquia "
				+ " AND FI.idFilial = F.idFranquia "
				+ " AND FF.franqueado.idFranqueado = FR.idFranqueado "
				+ " AND FR.idFranqueado = P.idPessoa "
				+ " AND EP.pessoa.idPessoa = P.idPessoa "
				+ " AND M.idMunicipio = EP.municipio.idMunicipio "
				+ " AND UF.idUnidadeFederativa = M.unidadeFederativa.idUnidadeFederativa "
				+ " AND F.idFranquia = :idFranquia";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idFranquia", idFranquia);
		
		List<Object> list = getAdsmHibernateTemplate().findByNamedParam(query, parametros);
		Map<String, Object> franquiaDados = new HashMap<String, Object>();
		if (list != null && ! list.isEmpty()) {
			Object[] dados = (Object[]) list.get(0);
			franquiaDados.put("nmPessoa", dados[0]);
			franquiaDados.put("nrIdentificacao", dados[1]);
			franquiaDados.put("dsEndereco", dados[2]);
			franquiaDados.put("nrEndereco", dados[3]);
			franquiaDados.put("nmMunicipio", dados[4]);
			franquiaDados.put("sgUnidadeFederativa", dados[5]);
			franquiaDados.put("sgFilial", dados[6]);
		}
		
		return franquiaDados;
	}

}
