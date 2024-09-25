package com.mercurio.lms.carregamento.model.dao;

import java.util.*;

import com.mercurio.lms.municipios.model.Filial;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleTrechoDAO extends BaseCrudDao<ControleTrecho, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ControleTrecho.class;
    }
    
    public ControleTrecho findControleTrechoWithControleCargaById(Long idControleTrecho) {
    	SqlTemplate hql = new SqlTemplate();		
		hql.addFrom(new StringBuilder()
		.append(ControleTrecho.class.getName()).append(" ct")
		.append(" INNER JOIN FETCH ct.controleCarga AS cc ").toString());
		hql.addCriteria("ct.id", "=", idControleTrecho);
		
		return (ControleTrecho) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Obtém o ControleTrecho com a menor previsão de chegada para os parâmetros informados.
     * 
     * @param idControleCarga
     * @param idFilialOrigem
     * @param blTrechoDireto
     * @return
     * @author luisfco
     */
    public ControleTrecho findControleTrechoByBlTrechoDireto(Long idControleCarga, Long idFilialOrigem, Boolean blTrechoDireto) {
    	
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass())
    		.add(Restrictions.eq("controleCarga.id", idControleCarga))
    		.add(Restrictions.eq("filialByIdFilialOrigem.id", idFilialOrigem))
    		.add(Restrictions.eq("blTrechoDireto", blTrechoDireto));
    	
    	
		return (ControleTrecho)getAdsmHibernateTemplate().findUniqueResult(detachedCriteria);
    	
    }

	/**
	 * Obtém os ControleTrechos a partir do ID do Controlecarga, do ID da Filial de Origem e o ID da Filial de Destino.
	 * 
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return ControleTrecho
	 */
    public ControleTrecho findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		SqlTemplate hql = new SqlTemplate();		
		hql.addFrom(ControleTrecho.class.getName() + " ct");
		hql.addCriteria("ct.controleCarga.id", "=", idControleCarga);
		hql.addCriteria("ct.filialByIdFilialOrigem.id", "=", idFilialOrigem);
		hql.addCriteria("ct.filialByIdFilialDestino.id", "=", idFilialDestino);
		hql.addOrderBy("ct.id");
		List lista = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		return lista.isEmpty() ? null : (ControleTrecho)lista.get(0);
    }

    /**
     * Obtém a contagem dos controles de trecho de acordo com os parâmetros recebidos.
     * @param idControleCarga
     * @param idFilialOrigem
     * @param idFilialDestino
     * @return
     */
    public Integer getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
    	SqlTemplate hql = new SqlTemplate();		
		hql.addFrom(ControleTrecho.class.getName() + " ct");
		hql.addCriteria("ct.controleCarga.id", "=", idControleCarga);
		hql.addCriteria("ct.filialByIdFilialOrigem.id", "=", idFilialOrigem);
		hql.addCriteria("ct.filialByIdFilialDestino.id", "=", idFilialDestino);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }
   

    /**
     * 
     * @param newOrModifiedItems
     */
    private void storeControleTrecho(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
		getAdsmHibernateTemplate().flush();
	}

    /**
     * 
     * @param removeItems
     */
	private void removeControleTrecho(List removeItems) {
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
    public void storeControleTrecho(ItemList items) {
    	removeControleTrecho(items.getRemovedItems());
 	   	storeControleTrecho(items.getNewOrModifiedItems());
    }


    /**
     * @param idControleCarga
     * @return
     */
    public List findControleTrechoByControleCarga(Long idControleCarga, Boolean blTrechoDireto, 
    											  Long idFilialOrigem, Long idFilialDestino) 
    {
    	StringBuffer sql = new StringBuffer()
			.append("select new Map( ")
			.append("ct.idControleTrecho as idControleTrecho, ")
			.append("ct.blInseridoManualmente as blInseridoManualmente, ")
			.append("ct.blTrechoDireto as blTrechoDireto, ")
			.append("ct.dhPrevisaoSaida as dhPrevisaoSaida, ")
			.append("ct.nrDistancia as nrDistancia, ")
			.append("ct.nrTempoViagem as nrTempoViagem, ")
			.append("ct.nrTempoOperacao as nrTempoOperacao, ")
			.append("ct.dhSaida as dhSaida, ")
			.append("ct.dhChegada as dhChegada, ")
			.append("ct.versao as versao, ")
			.append("cc.idControleCarga as controleCarga_idControleCarga, ")
			.append("triv.idTrechoRotaIdaVolta as trechoRotaIdaVolta_idTrechoRotaIdaVolta, ")
			.append("filialOrigem.idFilial as filialByIdFilialOrigem_idFilial, ")
			.append("filialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, ")
			.append("filialDestino.idFilial as filialByIdFilialDestino_idFilial, ")
			.append("filialDestino.sgFilial as filialByIdFilialDestino_sgFilial ")
			.append(") ")
			.append("from ").append(ControleTrecho.class.getName()).append(" ct ")
			.append("inner join ct.filialByIdFilialOrigem as filialOrigem ")
			.append("inner join ct.filialByIdFilialDestino as filialDestino ")
			.append("inner join ct.controleCarga as cc ")
			.append("left  join ct.trechoRotaIdaVolta as triv ")
			.append("where cc.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	if (blTrechoDireto != null) {
    		sql.append("and ct.blTrechoDireto = ? ");
    		param.add(blTrechoDireto);
    	}
    	if (idFilialOrigem != null) {
    		sql.append("and filialOrigem.id = ? ");
    		param.add(idFilialOrigem);
    	}
    	if (idFilialDestino != null) {
    		sql.append("and filialDestino.id = ? ");
    		param.add(idFilialDestino);
    	}

    	sql.append("order by ct.dhPrevisaoSaida.value, ct.dhPrevisaoChegada.value ");
    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    /**
     * 
     * @param idControleCarga
     * @param idFilialOrigem
     * @return
     */
    public List findControleTrechoDiretoByControleCarga(Long idControleCarga, Long idFilialOrigem){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(ct.idControleTrecho", "idControleTrecho");
    	sql.addProjection("f.idFilial", "filialByIdFilialDestino_idFilial");
    	sql.addProjection("f.sgFilial", "filialByIdFilialDestino_sgFilial");
    	sql.addProjection("p.nmFantasia", "filialByIdFilialDestino_pessoa_nmFantasia");
    	sql.addProjection("ct.versao", "versao)");
    	
    	sql.addInnerJoin(getPersistentClass().getName(), "ct");
    	sql.addInnerJoin("ct.filialByIdFilialDestino", "f");
    	sql.addInnerJoin("f.pessoa", "p");
    	sql.addCustomCriteria("ct.blTrechoDireto = 'S'");
    	
    	sql.addCriteria("ct.controleCarga.id", "=", idControleCarga);
    	sql.addCriteria("ct.filialByIdFilialOrigem.id", "=", idFilialOrigem);
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * Solicitação CQPRO00006173 da Integração.
     * Método que retorna mais de uma instancia da classe ControleTrecho
     * conforme parametros recebidos.
     * @param idFilialDestino
     * @param idControleCarga
     * @return
     */
    public List findControleTrecho(Long idFilialDestino, Long idControleCarga){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleTrecho.class, "ct");
		dc.add(Restrictions.eq("ct.filialByIdFilialDestino.id", idFilialDestino));
		dc.add(Restrictions.eq("ct.controleCarga.id", idControleCarga));
		return findByDetachedCriteria(dc);
    }
    
    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * @param idControleCarga
     * @param idFilial
     */
    public void removeByIdControleCargaByIdFilial(Long idControleCarga, Long idFilial) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(ControleTrecho.class.getName()).append(" as ct ")
	    	.append(" where ct.controleCarga.id = ? ")
	    	.append("and (ct.filialByIdFilialOrigem.id = ? or ct.filialByIdFilialDestino.id = ?) ");
    	
    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idFilial);
    	param.add(idFilial);
    	
    	super.executeHql(sql.toString(), param);
    }

    
    
    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * @param idControleCarga
     * @param idFilial
     */
    public void storeControleTrechoByControleCarga(Long idControleCarga, Long idFilial) {
    	StringBuffer sql = new StringBuffer()
	    	.append("update ")
	    	.append(ControleTrecho.class.getName()).append(" as ct ")
	    	.append(" set ct.blTrechoDireto = ? ")
	    	.append("where ct.controleCarga.id = ? ")
	    	.append("and ct.filialByIdFilialOrigem.id = ")
	    			.append("(select ct1.filialByIdFilialOrigem.id as idFilial from ")
	    			.append(ControleTrecho.class.getName()).append(" ct1 ")
	    			.append("where ct1.controleCarga.id = ? ")
	    			.append("and ct1.blTrechoDireto = ? ")
	    			.append("and ct1.filialByIdFilialDestino.id = ?) ")
    	.append("and ct.filialByIdFilialDestino.id =  ")
		    		.append("(select ct2.filialByIdFilialDestino.id from ")
	    			.append(ControleTrecho.class.getName()).append(" ct2 ")
	    			.append("where ct2.controleCarga.id = ? ")
	    			.append("and ct2.blTrechoDireto = ? ")
	    			.append("and ct2.filialByIdFilialOrigem.id = ?) ");

    	List param = new ArrayList();
    	param.add(Boolean.TRUE);
    	param.add(idControleCarga);
    	param.add(idControleCarga);
    	param.add(Boolean.TRUE);
    	param.add(idFilial);
   	  	param.add(idControleCarga);
   	  	param.add(Boolean.TRUE);
    	param.add(idFilial);

    	executeHql(sql.toString(), param);
    }
    
    
    /**
     * Verifica se existe algum registro na tabela CONTROLE_TRECHO associado a um MANIFESTO de acordo com os parâmetros.
     * 
     * @param idControleCarga
     * @param idFilial
     * @return Retorna TRUE se encontrar algum registro, caso contrário, FALSE.
     */
    public Boolean findControleTrechoByControleCargaWithManifesto(Long idControleCarga, Long idFilial) {
    	StringBuffer sql = new StringBuffer()
			.append("from ")
			.append(ControleTrecho.class.getName()).append(" ct ")
			.append("inner join ct.manifestos as manifesto ")
			.append("where ct.controleCarga.id = ? ")
			.append("and (ct.filialByIdFilialOrigem.id = ? ")
			.append("or ct.filialByIdFilialDestino.id = ?) ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idFilial);
    	param.add(idFilial);

    	Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
    	return Boolean.valueOf( rows.compareTo(Integer.valueOf(0)) > 0 );
    }
    
    /**
     * Método criado e utilizado somente pela Integração
     * @param idControleCarga
     * @param idFilialOrigem
     * @return
     */
    public List findControleTrechoDireto(Long idControleCarga, Long idFilialOrigem){
    	DetachedCriteria dc = DetachedCriteria.forClass(ControleTrecho.class, "ct");

    	dc.add(Restrictions.eq("ct.filialByIdFilialDestino.id", idFilialOrigem));
		dc.add(Restrictions.eq("ct.controleCarga.id", idControleCarga));
		dc.add(Restrictions.eq("ct.blTrechoDireto",Boolean.TRUE));
		return findByDetachedCriteria(dc);
    }
    
    public void updateDhSaidaByControleCargaIdAndIdFilialOrigem(DateTime horaSaida, Long controleCargaId, Long idFilialOrigem) {
    	StringBuffer sql = new StringBuffer()
	    	.append(" update ")
	    	.append(ControleTrecho.class.getName()).append(" as ct ")
	    	.append(" set ct.dhSaida = ?")
	    	.append(" where ct.controleCarga.id = ?")
	    	.append(" and ct.filialByIdFilialOrigem.id = ?");
		executeHql(sql.toString(), Arrays.asList(new Object[] { horaSaida, controleCargaId, idFilialOrigem }));
    }

	public ControleTrecho findControleTrechoByNrControleCargaAndSgFilialOrigem(String sgFilial, Long nrControleCarga){
		StringBuilder sql = new StringBuilder()

				.append("select ct")
				.append(" from ").append(ControleTrecho.class.getName()).append(" as ct, ")
				.append(Filial.class.getName()).append(" as f, ")
				.append(ControleCarga.class.getName()).append(" as cc ")
				.append(" where  cc.nrControleCarga = ").append(nrControleCarga)
				.append(" and cc.filialByIdFilialOrigem.idFilial = f.idFilial")
				.append(" and cc.idControleCarga = ct.controleCarga.idControleCarga")
				.append(" and ct.filialByIdFilialOrigem.idFilial = f.idFilial")
				.append(" and f.sgFilial = ").append("'").append(sgFilial).append("'");

		List rows = getAdsmHibernateTemplate().find(sql.toString());
		return (rows!= null && !rows.isEmpty()) ? (ControleTrecho)rows.get(0) : null;
	}
}