package com.mercurio.lms.seguros.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReguladoraSeguroDAO extends BaseCrudDao<ReguladoraSeguro, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReguladoraSeguro.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("pessoa", FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("pessoa", FetchMode.JOIN);
    }
    
    protected void initFindListLazyProperties(Map map) {
		map.put("pessoa", FetchMode.JOIN);
	}
    
    protected void initFindLookupLazyProperties(Map lazyFindLookup) {
    	lazyFindLookup.put("pessoa", FetchMode.JOIN);
	}

	public List findListByCriteria(Map criterions) {
    	List listaOrder = new ArrayList();
		listaOrder.add("pessoa_.nmPessoa:asc");
		return super.findListByCriteria(criterions,listaOrder);
    }
    
    public List findCombo(String tpSituacao) {
    	ProjectionList pl = Projections.projectionList()
    					 .add(Projections.alias(Projections.property("P.nmPessoa"),"pessoa_nmPessoa"))
    					 .add(Projections.alias(Projections.property("R.tpSituacao"),"tpSituacao"))
    					 .add(Projections.alias(Projections.property("R.idReguladora"),"idReguladora"));
    	DetachedCriteria dc = DetachedCriteria.forClass(ReguladoraSeguro.class,"R")
    					   .setProjection(pl)
    					   .createAlias("R.pessoa","P")
    					   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
    	if (StringUtils.isNotBlank(tpSituacao))
    		dc.add(Restrictions.eq("R.tpSituacao",tpSituacao));
    	dc.addOrder(Order.asc("P.nmPessoa"));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    /**
     * Obtém as reguladoras de seguro associadas ao controle de carga,
     * buscando através de conhecimento nacional e internacional.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
	public List findEmailReguladoraByIdControleCarga(Long idControleCarga, YearMonthDay data) {
		List reguladorasConhecimento = this.findEmailReguladoraByIdControleCargaConhecimento(idControleCarga, data);
		List reguladorasCtoInter = this.findEmailReguladoraByIdControleCargaCtoInter(idControleCarga, data);
		List result = new ArrayList(); 
		result.addAll(reguladorasConhecimento);
		result.addAll(reguladorasCtoInter);
		return result;
	}
	
    /**
     * Obtém as reguladoras de seguro associadas ao controle de carga,
     * através de conhecimento internacional.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
	private List findEmailReguladoraByIdControleCargaCtoInter(Long idControleCarga, YearMonthDay data) {

		// remetentes
		String s1 = new StringBuffer()
		.append(" select distinct new map(pRem.dsEmail as dsEmail, pRem.idPessoa as idPessoa) ")
		.append(" from "+ControleCarga.class.getName()+" cc")
		.append(" join cc.manifestos m")
		.append(" join m.manifestoInternacional mi")
		.append(" join mi.manifestoInternacCtos mic")
		.append(" join mic.ctoInternacional ctoi")
		.append(" join ctoi.clienteByIdClienteRemetente rem")
		.append(" join rem.seguroClientes scRem")
		.append(" join scRem.reguladoraSeguro rsRem")
		.append(" join rsRem.pessoa pRem")
		.append(" where cc.id = ?")
		.append(" and scRem.dtVigenciaInicial <= ?")
		.append(" and scRem.dtVigenciaFinal   >= ?")
		.append(" and pRem.dsEmail is not null")
		.toString();
		
		// destinatarios
		String s2 = new StringBuffer()
		.append(" select distinct new map(pDest.dsEmail as dsEmail, pDest.idPessoa as idPessoa) ")
		.append(" from "+ControleCarga.class.getName()+" cc")
		.append(" join cc.manifestos m")
		.append(" join m.manifestoInternacional mi")
		.append(" join mi.manifestoInternacCtos mic")
		.append(" join mic.ctoInternacional ctoi")
		.append(" join ctoi.clienteByIdClienteDestinatario dest")
		.append(" join dest.seguroClientes scDest")
		.append(" join scDest.reguladoraSeguro rsDest")
		.append(" join rsDest.pessoa pDest")
		.append(" where cc.id = ?")
		.append(" and scDest.dtVigenciaInicial <= ?")
		.append(" and scDest.dtVigenciaFinal   >= ?")
		.append(" and pDest.dsEmail is not null")
		.toString();
		
		List remetentes = getAdsmHibernateTemplate().find(s1, new Object[]{idControleCarga, data, data});
		List destinatarios = getAdsmHibernateTemplate().find(s2, new Object[]{idControleCarga, data, data});

		List result = new ArrayList();
		result.addAll(remetentes);
		result.addAll(destinatarios);
		return result;
	}

    /**
     * Obtém as reguladoras de seguro associadas ao controle de carga,
     * através de conhecimento nacional.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
	private List findEmailReguladoraByIdControleCargaConhecimento(Long idControleCarga, YearMonthDay data) {

		// remetentes
		String s1 = new StringBuffer()
		.append(" select distinct new map(pRem.dsEmail as dsEmail, pRem.idPessoa as idPessoa) ")
		.append(" from "+ControleCarga.class.getName()+" cc")
		.append(" join cc.manifestos m")
		.append(" join m.manifestoViagemNacional mvn")
		.append(" join mvn.manifestoNacionalCtos mnc")
		.append(" join mnc.conhecimento cto")
		.append(" join cto.clienteByIdClienteRemetente rem")
		.append(" join rem.seguroClientes scRem")
		.append(" join scRem.reguladoraSeguro rsRem")
		.append(" join rsRem.pessoa pRem")		
		.append(" where cc.id = ? ")
		.append(" and scRem.dtVigenciaInicial <= ?")
		.append(" and scRem.dtVigenciaFinal   >= ?")
		.append(" and pRem.dsEmail is not null")
		.toString();
		
		// destinatarios
		String s2 = new StringBuffer()
		.append(" select distinct new map(pDest.dsEmail as dsEmail, pDest.idPessoa as idPessoa) ")
		.append(" from "+ControleCarga.class.getName()+" cc")
		.append(" join cc.manifestos m")
		.append(" join m.manifestoViagemNacional mvn")
		.append(" join mvn.manifestoNacionalCtos mnc")
		.append(" join mnc.conhecimento cto")
		.append(" join cto.clienteByIdClienteDestinatario dest")
		.append(" join dest.seguroClientes scDest")
		.append(" join scDest.reguladoraSeguro rsDest")
		.append(" join rsDest.pessoa pDest")
		.append(" where cc.id = ?")
		.append(" and scDest.dtVigenciaInicial <= ?")
		.append(" and scDest.dtVigenciaFinal   >= ?")
		.append(" and pDest.dsEmail is not null")
		.toString();
		
		List remetentes = getAdsmHibernateTemplate().find(s1, new Object[]{idControleCarga, data, data});
		List destinatarios = getAdsmHibernateTemplate().find(s2, new Object[]{idControleCarga, data, data});

		List result = new ArrayList();
		result.addAll(remetentes);
		result.addAll(destinatarios);
		return result;
	}

}