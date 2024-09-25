package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoLocalizacaoMunicipioDAO extends BaseCrudDao<TipoLocalizacaoMunicipio, Long>
{

	public static final Long CAPITAL = Long.valueOf(1);
	public static final Long GRANDE_CAPITAL = Long.valueOf(2);
	public static final Long INTERIOR = Long.valueOf(3);
	
	public List findListByCriteria(Map criterions) {
    	if (criterions == null) criterions = new HashMap();
    	List order = new ArrayList(1);
    	order.add("dsTipoLocalizacaoMunicipio");
        return super.findListByCriteria(criterions, order);
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoLocalizacaoMunicipio.class;
    }
    
    /**
     * Solicitação CQPRO00005944 da Integração.
     * O método assume que a descrição passada para o 
     * método seja no idioma português/Brasil (pt_BR)
     * @param dsTipoLocalizacao
     * @return
     */
    public List findTipoLocalizacaoMunicipio(String dsTipoLocalizacao, String tpLocalizacao){
    	DetachedCriteria dc = DetachedCriteria.forClass(TipoLocalizacaoMunicipio.class, "tlm");
    	dc.add(BaseCompareVarcharI18n.eq("tlm.dsTipoLocalizacaoMunicipio", dsTipoLocalizacao, new Locale("pt", "BR")));
    	if(StringUtils.isNotBlank(tpLocalizacao)) {
    		dc.add(Restrictions.eq("tlm.tpLocalizacao", tpLocalizacao));
    	}
    	return findByDetachedCriteria(dc);
    }
    
    public List findTipoLocalizacaoOperacional(){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("localizacao");
    	hql.addFrom(TipoLocalizacaoMunicipio.class.getName(), "localizacao");
    	hql.addCriteria("localizacao.tpLocalizacao", "=", "O");
    	return getHibernateTemplate().find(hql.toString(),hql.getCriteria());
    }
    
    /**
     * Busca o tipo de localização do município representado pelo seu id de acordo 
     * com as suas datas de vigência para a filial e operacao.
     * 
     * @param idMunicipio
     * @return
     */
    public TipoLocalizacaoMunicipio findByIdMunicipio(Long idMunicipio, boolean isLocalizacaoFob){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("tlm");
    	hql.addInnerJoin(OperacaoServicoLocaliza.class.getName(),"osl");
    	if(isLocalizacaoFob) {
    		hql.addInnerJoin("osl.tipoLocalizacaoMunicipioFob", "tlm");
    	} else {
    		hql.addInnerJoin("osl.tipoLocalizacaoMunicipio", "tlm");
    	}
    	hql.addInnerJoin("osl.municipioFilial","mf");

    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	hql.addCriteria("mf.dtVigenciaInicial", "<=", dataAtual);
    	hql.addCriteria("mf.dtVigenciaFinal", ">=", dataAtual);
    	hql.addCriteria("osl.dtVigenciaInicial", "<=", dataAtual);
    	hql.addCriteria("osl.dtVigenciaFinal", ">=", dataAtual);

    	hql.addCriteriaIn("osl.tpOperacao",new String[]{"A","E"});
    	hql.addCriteria("mf.municipio.id", "=", idMunicipio);
    	hql.addCustomCriteria("(osl.servico.id is null or osl.servico.id = 1)");

    	return (TipoLocalizacaoMunicipio) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

	public List findByTipoPropostaFilial(Long[] tiposPropostaFilial) {
		SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("localizacao");
    	hql.addFrom(TipoLocalizacaoMunicipio.class.getName(), "localizacao");
    	hql.addCriteria("localizacao.tpLocalizacao", "=", "P");
    	hql.addCriteriaIn("localizacao.idTipoLocalizacaoMunicipio", tiposPropostaFilial);
    	return getHibernateTemplate().find(hql.toString(),hql.getCriteria());
	}
}