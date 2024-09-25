package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.expedicao.model.NaturezaProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NaturezaProdutoDAO extends BaseCrudDao<NaturezaProduto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return NaturezaProduto.class;
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsNaturezaProduto
     * @return <Lista de NaturezaProduto>  
     */
    public List findByDsNaturezaProduto(String dsNaturezaProduto) {
    	DetachedCriteria dc = createDetachedCriteria();
		dc.add(BaseCompareVarcharI18n.ilike("dsNaturezaProduto", dsNaturezaProduto+"%", LocaleContextHolder.getLocale()));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public List findListByCriteria(Map criterions) {
		if (criterions == null) criterions = new HashMap(1);
		List order = new ArrayList(1);
		order.add("dsNaturezaProduto");
		return super.findListByCriteria(criterions, order);
	}
	
	public List findAllAtivo() {
		DetachedCriteria dc = DetachedCriteria.forClass(NaturezaProduto.class, "np")
			.setProjection(
						Projections.projectionList()
							.add(Projections.property("np.idNaturezaProduto"), "idNaturezaProduto")
							.add(Projections.property("np.dsNaturezaProduto"), "dsNaturezaProduto"))
			.add(Restrictions.eq("np.tpSituacao", "A"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP)
			.addOrder(OrderVarcharI18n.asc("np.dsNaturezaProduto", LocaleContextHolder.getLocale()));
		return findByDetachedCriteria(dc);
	}
	


	/**
	 * Busca a naturezaProduto de um conhecimento.
	 * @param idConhecimento
	 * @return
	 */
	public NaturezaProduto findByIdConhecimento(Long idConhecimento){
		String sql = "select np from Conhecimento c, NaturezaProduto np where c.naturezaProduto = np and c.id = ?";
		return (NaturezaProduto)getAdsmHibernateTemplate().findUniqueResult(sql, new Long[]{idConhecimento});
	}

	/**
	 * LMS-6885 - Busca opções para diretiva "chosen" de
	 * {@link NaturezaProduto}, considerando apenas {@code tpSituacao} igual a
	 * {@code "A"} e ordenando por {@code dsNaturezaProduto} e
	 * {@code idNaturezaProduto}.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NaturezaProduto> findChosen() {
		StringBuilder hql = new StringBuilder()
				.append("FROM NaturezaProduto np ")
				.append("WHERE np.tpSituacao = 'A' ")
				.append("ORDER BY np.dsNaturezaProduto, np.idNaturezaProduto ");
		return getAdsmHibernateTemplate().find(hql.toString());
	}
	
	public String findDsNaturezaProdutoByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT (SELECT NP.DS_NATUREZA_PRODUTO_I ");
        sql.append("          FROM NATUREZA_PRODUTO NP, CONHECIMENTO CON ");
        sql.append("          WHERE NP.ID_NATUREZA_PRODUTO = CON.ID_NATUREZA_PRODUTO ");
        sql.append("          AND CON.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO) as DS_NATUREZA_PRODUTO ");
        sql.append(" FROM  PRE_MANIFESTO_DOCUMENTO PMD ");
        sql.append("      ,MANIFESTO M ");
        sql.append("      ,DOCTO_SERVICO DS ");
        sql.append(" WHERE M.tp_status_manifesto <> 'CA' ");
        sql.append(" AND M.ID_MANIFESTO = PMD.ID_MANIFESTO ");
        sql.append(" AND PMD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
        sql.append(" AND DS.VL_MERCADORIA = (SELECT MAX(DSE.VL_MERCADORIA) ");
        sql.append("                         FROM PRE_MANIFESTO_DOCUMENTO PMD2, DOCTO_SERVICO DSE ");
        sql.append("                         WHERE PMD2.ID_MANIFESTO = M.ID_MANIFESTO ");
        sql.append("                         AND DSE.ID_DOCTO_SERVICO = PMD2.ID_DOCTO_SERVICO ");
		sql.append("                         AND DSE.TP_DOCUMENTO_SERVICO <> 'MDA') ");
        sql.append(" AND M.ID_CONTROLE_CARGA = :idControleCarga ");
        sql.append(" AND ROWNUM = 1 ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("DS_NATUREZA_PRODUTO", Hibernate.STRING);
            }
        };

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idControleCarga", idControleCarga);

        List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);

        if (!object.isEmpty()) {
            return (String) object.get(0);
        }

        return null;
	}

	
}
