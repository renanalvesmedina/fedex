package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorTarifaPostoPassagemDAO extends BaseCrudDao<ValorTarifaPostoPassagem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorTarifaPostoPassagem.class;
    }

    public List findVTPPByTPPandId(Long idTarifaPostoPassagem,Long idValorTarifaPostoPassagem) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("tarifaPostoPassagem.idTarifaPostoPassagem",idTarifaPostoPassagem));
  	   	if (idValorTarifaPostoPassagem != null)
  	   		dc.add(Restrictions.ne("id",idValorTarifaPostoPassagem));
  	   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    protected void initFindListLazyProperties(Map fetchModes) {
    	fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
    	fetchModes.put("tipoMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
    	fetchModes.put("moedaPais",FetchMode.JOIN);
    	fetchModes.put("moedaPais.moeda",FetchMode.JOIN);
    	super.initFindListLazyProperties(fetchModes);
    }
    
    //busca vlTarifa,idMoeda de todas as tarifas vigentes para aquele postoPassagem, com a forma de cobrança = tipo de veiculo e para o tipo de meio de transporte passado por parametro
	public List findValorTarifaPostoPassagemByPostoPassagem(Long idPostoPassagem, String formaCobranca, Long idTipoMeioTransporte){
		ProjectionList pl = Projections.projectionList()
	       .add(Projections.property("vt.vlTarifa"),"vlTarifa")
	       .add(Projections.property("mp.idMoedaPais"),"idMoedaPais")
	       .add(Projections.property("m.idMoeda"),"idMoeda")
	       .add(Projections.property("m.sgMoeda"),"sgMoeda")
	       .add(Projections.property("m.dsSimbolo"),"dsSimbolo");
		
		DetachedCriteria dc = DetachedCriteria.forClass(ValorTarifaPostoPassagem.class,"vt")
			.createAlias("vt.tarifaPostoPassagem","tpp")
			.createAlias("vt.moedaPais","mp")
			.createAlias("mp.moeda","m")
			.createAlias("vt.tipoMeioTransporte","tmt")
			.createAlias("tpp.postoPassagem", "pp")
            .setProjection(pl)
            .setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP)
            .add(Restrictions.eq("pp.idPostoPassagem",idPostoPassagem))
            .add(Restrictions.eq("tpp.tpFormaCobranca",formaCobranca))
            .add(Restrictions.eq("tmt.idTipoMeioTransporte",idTipoMeioTransporte))
		    .add(
				Restrictions.and(
					 Restrictions.le("tpp.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()),
					 Restrictions.or(
							 Restrictions.ge("tpp.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()),
							 Restrictions.isNull("tpp.dtVigenciaFinal"))));
            
		return findByDetachedCriteria(dc);
	}
	
	public List findListByCriteria(Map criterions) {
		List listaOrder = new ArrayList();
		listaOrder.add("tipoMeioTransporte_.dsTipoMeioTransporte:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}
	
	public List findByTarifaPostoPassagem(Long idTarifaPostoPassagem) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(new StringBuffer(getPersistentClass().getName()).append(" AS VTPP ")
			.append("LEFT JOIN FETCH VTPP.tipoMeioTransporte TMT ")
			.append("LEFT JOIN FETCH TMT.tipoMeioTransporte TMTC ")
			.append("LEFT JOIN FETCH VTPP.moedaPais MP ")
			.append("LEFT JOIN FETCH MP.moeda M ").toString());
			
		hql.addCriteria("VTPP.tarifaPostoPassagem.id","=",idTarifaPostoPassagem);
		
		hql.addOrderBy("VTPP.qtEixos");
		hql.addOrderBy("TMT.dsTipoMeioTransporte");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
}