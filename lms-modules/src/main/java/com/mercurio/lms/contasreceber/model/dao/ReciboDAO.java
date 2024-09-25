package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;
import com.mercurio.lms.contasreceber.model.FaturaRecibo;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.param.ReciboParam;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionKey;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboDAO extends BaseCrudDao<Recibo, Long>
{

    public Recibo store(Recibo recibo, List lstFaturaRecibo, List lstFaturaReciboDeleted) {
        super.store(recibo);
        removeItemFatura(lstFaturaReciboDeleted);
		storeItemFatura(lstFaturaRecibo);
		getAdsmHibernateTemplate().flush();    
        return recibo;
    }     
    
	public void storeItemFatura(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	public void removeItemFatura(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}	
	
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Recibo.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("pendencia", FetchMode.JOIN);
    	lazyFindById.put("cotacaoMoeda", FetchMode.JOIN);
    	lazyFindById.put("cotacaoMoeda.moedaPais", FetchMode.JOIN);
    	lazyFindById.put("cotacaoMoeda.moedaPais.moeda", FetchMode.JOIN);
    	lazyFindById.put("filialByIdFilialEmissora", FetchMode.JOIN);
    	lazyFindById.put("filialByIdFilialEmissora.pessoa", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }

    /**
     * M�todo respons�vel por buscar recibos ade acordo com o n�mero de recibo e a sigla da filial passados no filtro
     * 
     * @param nrRecibo
     * @param sgFilial
     * @return List 
     */
    public List findReciboByFilial(Long nrRecibo, Long idFilial){
    	DetachedCriteria dc = createDetachedCriteria();    	
		
    	dc.add(Restrictions.eq("nrRecibo", nrRecibo));   
		dc.add(Restrictions.eq("filialByIdFilialEmissora.idFilial", idFilial));   
		
		return findByDetachedCriteria(dc);	
    }
    
    /**
     * Retorna o recibo ativo por id fatura
     * 
     * @param Long idFatura
     * @return List
     * */
    public List findByFatura(Long idFatura){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("rec");
    	sql.addInnerJoin(FaturaRecibo.class.getName(), "item ");
    	sql.addInnerJoin("item.recibo","rec");
    	sql.addInnerJoin("fetch rec.filialByIdFilialEmissora","fil");

    	sql.addCriteria("rec.tpSituacaoRecibo","!=","C");
    	sql.addCriteria("rec.tpSituacaoRecibo","!=","I");
    	sql.addCriteria("item.fatura.id","=",idFatura);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    } 

    
    public List<Recibo> findByFaturas(List<Long> idFaturas){
    	if(idFaturas.isEmpty()) {
    		return new ArrayList<Recibo>();
    	}
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("rec");
    	sql.addInnerJoin(FaturaRecibo.class.getName(), "item ");
    	sql.addInnerJoin("item.recibo","rec");
    	sql.addInnerJoin("fetch rec.filialByIdFilialEmissora","fil");

    	sql.addCriteria("rec.tpSituacaoRecibo","!=","C");
    	sql.addCriteria("rec.tpSituacaoRecibo","!=","I");
    	sql.addCriteriaIn("item.fatura.id",idFaturas);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    } 

    
    
    /**
     * Retorna true se o Recibo informado est� em redeco ativo
     * 
     * @author Micka�l Jalbert
     * @since 12/07/2006
     * 
     * @param Long idRecibo
     * 
     * @return Boolean
     * */
    public Boolean validateReciboEmRedeco(Long idRecibo) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("count(red.id)");
    	
    	hql.addInnerJoin(ItemRedeco.class.getName(), "itr");
    	hql.addInnerJoin("itr.redeco", "red");
    	hql.addCriteria("itr.recibo.id", "=", idRecibo);
    	
    	hql.addCriteria("red.tpSituacaoRedeco", "!=", "CA");
    	
    	List lstCount = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstCount != null && !lstCount.isEmpty()){
    		Integer numReg = (Integer)lstCount.get(0);
    		
    		if (numReg.compareTo(Integer.valueOf(0)) > 0){
    			return Boolean.TRUE;
    		}
    	}
    	
    	return Boolean.FALSE;
    }   
   
    
    public ResultSetPage findPaginated(ReciboParam reciboParam, FindDefinition findDef){
    	SqlTemplate hql = mountHql(reciboParam);
    	
    	hql.addProjection("rec");
    	
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
    }
    
    public Integer getRowCount(ReciboParam reciboParam){
    	SqlTemplate hql = mountHql(reciboParam);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }
    
    private SqlTemplate mountHql(ReciboParam reciboParam){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addInnerJoin(Recibo.class.getName(), "rec");
    	hql.addInnerJoin("fetch rec.filialByIdFilialEmissora", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pes");

    	hql.addCriteria("rec.nrRecibo", "=", reciboParam.getNrRecibo());
    	hql.addCriteria("rec.dtEmissao", ">=", reciboParam.getDtEmissaoInicial());
    	hql.addCriteria("rec.dtEmissao", "<=", reciboParam.getDtEmissaoFinal());
    	hql.addCriteria("fil.id", "=", reciboParam.getIdFilial());
    	hql.addCriteria("rec.tpSituacaoRecibo", "=", reciboParam.getTpSituacaoRecibo());
    	hql.addCriteria("rec.tpSituacaoAprovacao", "=", reciboParam.getTpSituacaoAprovacao());
    	
    	hql.addOrderBy("rec.nrRecibo");
    	
    	return hql;
    }
    
    public List findFaturaRecibo(Long masterId){
    	SqlTemplate hql = mountSqlItem(masterId);
    	hql.addProjection("fatRec");
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    public Integer getRowCountFaturaRecibo(Long masterId){
    	SqlTemplate hql = mountSqlItem(masterId);

    	return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }
    
    private SqlTemplate mountSqlItem(Long idRecibo){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addInnerJoin(FaturaRecibo.class.getName(), "fatRec");
    	hql.addInnerJoin("fetch fatRec.recibo", "rec");    	
    	hql.addInnerJoin("fetch fatRec.fatura", "fat");
    	hql.addInnerJoin("fetch fat.filialByIdFilial", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pessoa");
    	
    	hql.addCriteria("rec.idRecibo","=",idRecibo);
    	return hql;
    }
    
    public void removeById(Long id) {
    	super.removeById(id, true);
    }
    
    public int removeByIds(List ids) {
    	return super.removeByIds(ids, true);
    }
	
	/**
	 * Retorna a lista de recibo do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/07/2006
	 * 
	 * @param Long idRedeco
	 * @return List
	 */
	public List findFaturaOfReciboByIdRedeco(Long idRedeco){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("fat");
		
		hql.addInnerJoin(ItemRedeco.class.getName(), "itr");
		hql.addInnerJoin("itr.recibo", "rec");
		hql.addInnerJoin("rec.faturaRecibos", "fatrec");
		hql.addInnerJoin("fatrec.fatura", "fat");
		
		hql.addCriteria("itr.redeco.id", "=", idRedeco);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
    /**
     * Retorna o recibo do n�mero e a filial informado. 
     * 
     * @author Micka�l Jalbert
     * @since 07/07/2006
     * 
     * @param Long nrRecibo
     * @param Long idFilial
     * 
     * @return List
     */
    public List findByNrReciboByFilial(Long nrRecibo, Long idFilial){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("rec");
    	
    	hql.addInnerJoin(Recibo.class.getName(), "rec");
    	hql.addInnerJoin("fetch rec.filialByIdFilialEmissora", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pes");
    	
    	hql.addCriteria("rec.nrRecibo", "=", nrRecibo);
    	hql.addCriteria("fil.id", "=", idFilial);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }	
    
    /**
     * Retorna a soma dos juros recebidos do recibo informado
     * 
     * @author Micka�l Jalbert
     * @since 23/08/2006
     * 
     * @param idRecibo
     * 
     * @return BigDecimal
     */
    public Map findSomatorio(Long idRecibo){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map" +
    			"(SUM(fr.vlJuroRecebido) AS vlJuroRecebido, " +
    			"SUM(fat.vlDesconto) AS vlDesconto, " +
    			"SUM(fat.vlJuroCalculado) AS vlJuroCalculado, " +
    			"SUM(fat.vlTotal) AS vlTotal, " +
    			"(SUM(fat.vlTotal) - SUM(fat.vlDesconto) + SUM(fr.vlJuroRecebido)) AS vlTotalRecibo) ");
    	
    	hql.addInnerJoin(FaturaRecibo.class.getName(), "fr");
    	hql.addInnerJoin("fr.fatura", "fat");
    	
    	hql.addCriteria("fr.recibo.id", "=", idRecibo);
    	
    	List lstSoma = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstSoma.isEmpty()){
    		return (Map)lstSoma.get(0);
    	} else {
    		return null;
    	}
    }

    /**
	 * Valida a permiss�o de acesso do usu�rio ao recibo informado
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 21/09/2006
	 *
	 * @param idRecibo Identificador do Recibo
	 * @param idFilial Identificador da filial
	 */
	public Boolean validatePermissaoReciboUsuario(Long idRecibo, Long idFilial) {
		
		Boolean temPermissao = Boolean.FALSE;
        
        Filial filialSessao = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("count(*)");
        
        sql.addFrom(CentralizadoraFaturamento.class.getName() + " cf " +
                   "    inner join cf.filialByIdFilialCentralizada filialCentralizada " +
                   "    inner join cf.filialByIdFilialCentralizadora filialCentralizadora ");
        sql.addFrom(Recibo.class.getName() + " rec " +
                   "    inner join rec.filialByIdFilialEmissora filial" );
        
        sql.addCriteria("cf.tpAbrangencia","=","I");//Internacional
        sql.addJoin("filialCentralizada.idFilial","filial.idFilial");
        sql.addCriteria("filialCentralizadora.idFilial","=",filialSessao.getIdFilial());
        sql.addCriteria("rec.id","=",idRecibo);
        
        List ret = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
        
        int retorno = 0;
        
        if( ret != null && !ret.isEmpty() ){
            retorno = ((Integer) ret.get(0)).intValue();
        }
        
        if( retorno > 0 ){
            temPermissao = Boolean.TRUE;
        } else {
            
            sql = new SqlTemplate();
            
            sql.addProjection("count(*)");
            
            sql.addFrom(CentralizadoraFaturamento.class.getName() + " cf " +
                       "    inner join cf.filialByIdFilialCentralizada filialCentralizada " +
                       "    inner join cf.filialByIdFilialCentralizadora filialCentralizadora ");
            sql.addFrom(Recibo.class.getName() + " rec " +
            		   "    inner join rec.filialByIdFilialEmissora filial" );
 
            sql.addCriteria("cf.tpAbrangencia","=","I");//Internacional                       
            sql.addJoin("filialCentralizadora.idFilial","filial.idFilial");
            sql.addCriteria("filialCentralizada.idFilial","=",filialSessao.getIdFilial());            
            sql.addCriteria("fat.id","=",idRecibo);
            
            ret = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
            
            retorno = 0;
            
            if( ret != null && !ret.isEmpty() ){
                retorno = ((Integer) ret.get(0)).intValue();
            }
            
            if( retorno > 0 ){
                temPermissao = Boolean.FALSE;
            } else if ( idFilial != null && idFilial.equals(filialSessao.getIdFilial()) ){
                temPermissao = Boolean.TRUE;
            } else {
                temPermissao = Boolean.FALSE;
            }
            
        }
        
        if( temPermissao.equals(Boolean.FALSE) ){
            throw new BusinessException("LMS-36004");
        }        

        return temPermissao;	
	}    
}

