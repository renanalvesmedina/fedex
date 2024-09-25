package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PagtoPedagioCcDAO extends BaseCrudDao<PagtoPedagioCc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PagtoPedagioCc.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("controleCarga", FetchMode.JOIN);
    	map.put("tipoPagamPostoPassagem", FetchMode.JOIN);
    	map.put("moeda", FetchMode.JOIN);
    	map.put("cartaoPedagio", FetchMode.JOIN);
    	map.put("operadoraCartaoPedagio", FetchMode.JOIN);
    }
    
   
    /**
     * Remove as instâncias do pojo, que estão associados ao controle de carga recebido por parâmetro.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(PagtoPedagioCc.class.getName()).append(" as ppcc ")
	    	.append(" where ppcc.controleCarga.id = :id");

    	getAdsmHibernateTemplate().removeById(sql.toString(), idControleCarga);
    }
    

    private void deletePagtoPedagioCc(List beans) {
    	getAdsmHibernateTemplate().deleteAll(beans);
    	getAdsmHibernateTemplate().flush();
    }
    
    /**
     * 
     * @param beans
     */
    public void storePagtoPedagioCc(List beans) {
    	getAdsmHibernateTemplate().saveOrUpdateAll(beans);
    	getAdsmHibernateTemplate().flush();
    }
    
	/**
	 * Realiza a operacao de salvar.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param items
	 * @return
	 */
    public void storePagtoPedagioCc(ItemList items) {
    	deletePagtoPedagioCc(items.getRemovedItems());
 	   	storePagtoPedagioCc(items.getNewOrModifiedItems());
    }

    /**
     * Busca os PagtoPedagioCc de um controle de carga e que tenha idTipoPagamPostoPassagem igual ao passado por parâmetro.
     * @param idControleCarga
     * @param idTipoPagamPostoPassagem
     * @return
     */
    public List findPagtoPedagioCcByIdCcAndIdPagamPostoPassagem(Long idControleCarga, Long idTipoPagamPostoPassagem){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select ppcc from PagtoPedagioCc as ppcc ");
    	sql.append("where ppcc.controleCarga.id = "+idControleCarga+" ");
    	sql.append("and ppcc.tipoPagamPostoPassagem.id = "+idTipoPagamPostoPassagem);
    	return getAdsmHibernateTemplate().find(sql.toString());
    }

    public List findPagtoPedagioCcByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("select new map(ppcc.idPagtoPedagioCc as idPagtoPedagioCc, ")
	    	.append("cc.idControleCarga as controleCarga_idControleCarga, ")
	    	.append("ppcc.vlPedagio as vlPedagio, ")
	    	.append("ppcc.versao as versao, ")
			.append("cp.idCartaoPedagio as cartaoPedagio_idCartaoPedagio, ")
			.append("cp.nrCartao as cartaoPedagio_nrCartao, ")
			.append("tppp.idTipoPagamPostoPassagem as tipoPagamPostoPassagem_idTipoPagamPostoPassagem, ")
			.append("tppp.blCartaoPedagio as tipoPagamPostoPassagem_blCartaoPedagio, ")
			.append("tppp.dsTipoPagamPostoPassagem as tipoPagamPostoPassagem_dsTipoPagamPostoPassagem, ")
			.append("ocp.idOperadoraCartaoPedagio as operadoraCartaoPedagio_idOperadoraCartaoPedagio, ")
			.append("pessoaOperadora.nmPessoa as operadoraCartaoPedagio_pessoa_nmPessoa, ")
			.append("moeda.idMoeda as moeda_idMoeda, ")
			.append("moeda.sgMoeda as moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moeda_dsSimbolo ")
			.append(") ")
			.append("from ")
			.append(PagtoPedagioCc.class.getName()).append(" as ppcc ")
			.append("inner join ppcc.controleCarga as cc ")
    		.append("inner join ppcc.tipoPagamPostoPassagem as tppp ")
    		.append("inner join ppcc.moeda as moeda ")
    		.append("left join ppcc.cartaoPedagio as cp ")
    		.append("left join ppcc.operadoraCartaoPedagio as ocp ")
    		.append("left join ocp.pessoa as pessoaOperadora ")
    		.append("where ")
    		.append("cc.id = ? ")
    		.append("order by ")
    		.append(OrderVarcharI18n.hqlOrder("tppp.dsTipoPagamPostoPassagem", LocaleContextHolder.getLocale()) );

		
		Object obj[] = new Object[1];
		obj[0] = idControleCarga;

		List lista = getAdsmHibernateTemplate().find(sql.toString(), obj);
	    return lista;
    }
    
    
    /**
     * Verifica se existe algum pagtoPedagioCc onde não tenha sido informado a operadora/número do cartão para blCartaoPedagio = 'S'. 
     * @param idControleCarga
     * @return True, se existe algum registro, caso contrário, False.
     */
    public Boolean validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(Long idControleCarga){
    	StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(PagtoPedagioCc.class.getName()).append(" as ppcc ")
		.append("inner join ppcc.tipoPagamPostoPassagem as tppp ")
    	.append("where ppcc.controleCarga.id = ? ")
    	.append("and tppp.blCartaoPedagio = ? ")
    	.append("and (ppcc.cartaoPedagio.id is null or ppcc.operadoraCartaoPedagio.id is null) ");

    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[] {idControleCarga, Boolean.TRUE});
    	return (rowCount.intValue() > 0); 
    }
}