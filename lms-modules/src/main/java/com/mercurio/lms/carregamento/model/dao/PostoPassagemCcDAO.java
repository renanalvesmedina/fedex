package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoPassagemCcDAO extends BaseCrudDao<PostoPassagemCc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoPassagemCc.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("controleCarga", FetchMode.JOIN);
    	map.put("tipoPagamPostoPassagem", FetchMode.JOIN);
    	map.put("postoPassagem", FetchMode.JOIN);
    	map.put("postoPassagem.municipio", FetchMode.JOIN);
    	map.put("postoPassagem.rodovia", FetchMode.JOIN);
    	map.put("moeda", FetchMode.JOIN);
    }
    
    
    protected void initFindByIdLazyProperties(Map map) {
    	map.put("moeda", FetchMode.JOIN);
    }


    /**
     * 
     * @param idControleCarga
     * @return
     */
    public List findPostoPassagemByPagtoPedagioByControleCarga (Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("select new map(sum (ppCc.vlPagar) as vlSomatorio, ")
			.append("tpPagto.idTipoPagamPostoPassagem as idTipoPagamPostoPassagem, ")
			.append(" "+PropertyVarcharI18nProjection.createProjection("tpPagto.dsTipoPagamPostoPassagem")+" as dsTipoPagamPostoPassagem, ")
			.append("tpPagto.blCartaoPedagio as blCartaoPedagio, ")
			.append("moeda.idMoeda as idMoeda, ")
			.append("moeda.sgMoeda as sgMoeda, ")
			.append("moeda.dsSimbolo as dsSimbolo) ")
			.append("from ")
			.append(PostoPassagemCc.class.getName()).append(" AS ppCc ")
			.append("inner join ppCc.controleCarga as cc ")
			.append("inner join ppCc.moeda as moeda ")
			.append("inner join ppCc.tipoPagamPostoPassagem as tpPagto ")
			.append("where ")
			.append("cc.id = ? ")
			.append("group by ")
			.append("tpPagto.idTipoPagamPostoPassagem, "+PropertyVarcharI18nProjection.createProjection("tpPagto.dsTipoPagamPostoPassagem")+", tpPagto.blCartaoPedagio, ")
			.append("moeda.idMoeda, moeda.sgMoeda, moeda.dsSimbolo");

		List param = new ArrayList();
		param.add(idControleCarga);

    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }
    
    /**
     * 
     * @param newOrModifiedItems
     */
    public void storePostoPassagemCc(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
		getAdsmHibernateTemplate().flush();
	}
	
    /**
     * 
     * @param removeItems
     */
    public void removePostoPassagemCc(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}

    
	/**
	 * Realiza a operacao de salvar.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param items
	 * @return
	 */
    public void storePostoPassagemCc(ItemList items) {
    	removePostoPassagemCc(items.getRemovedItems());
 	   	storePostoPassagemCc(items.getNewOrModifiedItems());
    }
    
    
    public List findPostoPassagemCcByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("select new map(ppcc.idPostoPassagemCc as idPostoPassagemCc, ")
	    	.append("ppcc.vlPagar as vlPagar, ")
	    	.append("ppcc.versao as versao, ")
	    	.append("pp.idPostoPassagem as postoPassagem_idPostoPassagem, ")
	    	.append("pp.tpPostoPassagem as postoPassagem_tpPostoPassagem, ")
	    	.append("municipio.nmMunicipio as postoPassagem_municipio_nmMunicipio, ")
	    	.append("rodovia.sgRodovia as postoPassagem_rodovia_sgRodovia, ")
	    	.append("pp.nrKm as postoPassagem_nrKm, ")
			.append("tppp.idTipoPagamPostoPassagem as tipoPagamPostoPassagem_idTipoPagamPostoPassagem, ")
			.append("tppp.blCartaoPedagio as tipoPagamPostoPassagem_blCartaoPedagio, ")
			.append("tppp.dsTipoPagamPostoPassagem as tipoPagamPostoPassagem_dsTipoPagamPostoPassagem, ")
			.append("moeda.idMoeda as moeda_idMoeda, ")
			.append("moeda.sgMoeda as moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
			.append("cc.idControleCarga as controleCarga_idControleCarga ")
			.append(") ")
			.append("from ")
			.append(PostoPassagemCc.class.getName()).append(" as ppcc ")
			.append("inner join ppcc.controleCarga as cc ")
    		.append("inner join ppcc.tipoPagamPostoPassagem as tppp ")
    		.append("inner join ppcc.moeda as moeda ")
    		.append("inner join ppcc.postoPassagem as pp ")
    		.append("inner join pp.municipio as municipio ")
    		.append("left join pp.rodovia as rodovia ")
    		.append("where ")
    		.append("cc.id = ? ");
		
		Object obj[] = new Object[1];
		obj[0] = idControleCarga;

		List lista = getAdsmHibernateTemplate().find(sql.toString(), obj);
	    return lista;
    }

    /**
     * Remove as instâncias do pojo, que estão associados ao controle de carga recebido por parâmetro.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(PostoPassagemCc.class.getName()).append(" as ppcc ")
	    	.append("where ppcc.controleCarga.id = :id");

    	getAdsmHibernateTemplate().removeById(sql.toString(), idControleCarga);
    }
}