package com.mercurio.lms.municipios.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoPagamPostoPassagemDAO extends BaseCrudDao<TipoPagamPostoPassagem, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoPagamPostoPassagem.class;
    }

    /**
     * 
     * @param idPostoPassagem
     * @return
     */
    public List findFormasPagamentoPostoPassagemCc(Long idPostoPassagem, String tpControleCarga) {
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	StringBuffer from = new StringBuffer()
    		.append(TipoPagamPostoPassagem.class.getName()).append(" AS tppp ")
    		.append("inner join tppp.tipoPagamentoPostos as tpp ")
    		.append("inner join tpp.postoPassagem as pp ");

    	StringBuffer projecao = new StringBuffer()
    		.append("distinct new map( ")
			.append("tppp.idTipoPagamPostoPassagem as idTipoPagamPostoPassagem, ")
			.append(""+PropertyVarcharI18nProjection.createProjection("tppp.dsTipoPagamPostoPassagem")+" as dsTipoPagamPostoPassagem)");
    	
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(from.toString());
    	sql.addProjection(projecao.toString());
    	sql.addOrderBy(""+PropertyVarcharI18nProjection.createProjection("tppp.dsTipoPagamPostoPassagem")+" ");

    	sql.addCustomCriteria("? between tpp.dtVigenciaInicial and tpp.dtVigenciaFinal", dataAtual);
   		sql.addCriteria("pp.id", "=", idPostoPassagem);
   		
   		if (tpControleCarga != null && tpControleCarga.equals("V")) {
   			sql.addCustomCriteria("tppp.id <> 3 ");
   		}

    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
}