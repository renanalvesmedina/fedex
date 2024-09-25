package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemNotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaDebitoNacionalDAO extends BaseCrudDao<NotaDebitoNacional, Long> {

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pendencia",FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return NotaDebitoNacional.class;
	}

	public List findNrNotaDebitoNac(Map map){
		SqlTemplate sql = getSqlNotaDebitoNacionalByDevedorDocServFat(map);
		sql.addProjection("new map(cto.id as idDocumento, cto.nrNotaDebitoNac as nrDocumento, " +
			"filOri.sgFilial as filial_sgFilial, filOri.idFilial as filial_idFilial)");
		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()){ 
			list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		}
		return list;
	}

	private SqlTemplate getSqlNotaDebitoNacionalByDevedorDocServFat(Map criteria){
		TypedFlatMap map = (TypedFlatMap)criteria;
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(NotaDebitoNacional.class.getName() + " cto " +
				" join cto.filialByIdFilialOrigem as filOri " +
				" join cto.devedorDocServFats as doc " +
				" join doc.filial as fil " +
				" join doc.cliente as cli " +
				" join cli.pessoa as pes " +
				" join doc.descontos as des ");

		String tpDocumentoServico = map.getString("tpDocumentoServico");
		if (StringUtils.isNotEmpty(tpDocumentoServico))
			sql.addCriteria("cto.tpDocumentoServico", "=", tpDocumentoServico);

		Long idCliente = map.getLong("cliente.idCliente");
		if (idCliente != null)
			sql.addCriteria("cto.cliente.id","=",idCliente);

		Long idFilial = map.getLong("filial.idFilial");
		if (idFilial != null)
			sql.addCriteria("cto.filialByIdFilialOrigem.id","=",idFilial);	

		String tpSituacaoAprovacao = map.getString("desconto.tpSituacaoAprovacao");
		if (StringUtils.isNotEmpty(tpSituacaoAprovacao))
			sql.addCriteria("des.tpSituacaoAprovacao","=",tpSituacaoAprovacao);

		String nrNotaDebitoNac = map.getString("nrDocumento");
		if (StringUtils.isNotEmpty(nrNotaDebitoNac))
			sql.addCriteria("cto.nrNotaDebitoNac", "=", Long.valueOf(Long.parseLong(nrNotaDebitoNac)) );

		return sql;
	}

	/**
	 * Busca uma lista de NotasDebitoNacional de acordo com os critérios de pesquisa
	 * @param tpDocumentoServico Tipo de documento de serviço - NDN
	 * @param idFilial Identificador da filial de origem
	 * @param nrDocumento Númerod do documento de serviço
	 * @return Lista de NotasDebitoNacional
	 */
	public List findNotaDebitoNacional(String tpDocumentoServico, Long idFilial, Long nrDocumento) {
		SqlTemplate sql = getQueryHqlNotaDebitoNacional(tpDocumentoServico,idFilial,nrDocumento);

		sql.addProjection("ndn");

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Query para busca de um documento NDN de acordo com o seu tipo, filial e número do documento
	 * @param tpDocumentoServico Tipo de documento NDN
	 * @param idFilial Identificador da filial origem
	 * @param nrDocumento Número do documento de serviço
	 * @return SqlTemplate
	 */
	public SqlTemplate getQueryHqlNotaDebitoNacional(String tpDocumentoServico, Long idFilial, Long nrDocumento) {
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(NotaDebitoNacional.class.getName(), "ndn" );
		sql.addInnerJoin("ndn.filialByIdFilialOrigem","f");

		sql.addCriteria("ndn.tpDocumentoServico","=",tpDocumentoServico);
		sql.addCriteria("f.id","=",idFilial);
		sql.addCriteria("ndn.nrNotaDebitoNac","=",nrDocumento);

		return sql;
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}

	public List findItemNotaDebitoNacionalByNotaDebitoNacionalId(Long idNotaDebitoNacional) {		
		DetachedCriteria dc = getDetachedCriteria(idNotaDebitoNacional);
		
		dc.createAlias("fatura","f");
		dc.createAlias("f.filialByIdFilial","fil");
		dc.createAlias("f.cliente","c");
		dc.createAlias("c.pessoa","p");
		
		return super.findByDetachedCriteria(dc);
	}

	private DetachedCriteria getDetachedCriteria(Long idNotaDebitoNacional) {
		return DetachedCriteria.forClass(ItemNotaDebitoNacional.class).add(Restrictions.eq("notaDebitoNacional.id", idNotaDebitoNacional));
	}

	/**
	 * Retorna o tpSituacao da nota de débito nacional informada.
	 *
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 *
	 * @param Long idNotaDebitoNacional
	 * @return DomainValue
	 **/
	public DomainValue findTpSituacaoByNotaDebitoNacional(Long idNotaDebitoNacional) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("n.tpSituacaoNotaDebitoNac");
		hql.addInnerJoin(NotaDebitoNacional.class.getName(), "n");
		hql.addCriteria("n.id", "=", idNotaDebitoNacional);

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()) {
			return (DomainValue) list.get(0);
		}
		return null;
	}

}