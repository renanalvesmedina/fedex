package com.mercurio.lms.tributos.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.AliquotaIssMunicipioServ;
import com.mercurio.lms.tributos.model.IssMunicipioServico;
import com.mercurio.lms.tributos.model.ServicoMunicipio;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AliquotaIssMunicipioServDAO extends BaseCrudDao<AliquotaIssMunicipioServ, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaIssMunicipioServ.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
        lazyFindById.put("issMunicipioServico",FetchMode.JOIN);      
    }

    /**
     * Verifica se existe conflito de vigências para a AliquotaIssMunicipioServ a ser salvo
     * @param aliquotaIssMunicipioServ Objeto que será salvo
     * @return boolean que indica se existe conflito ou não de vigências
     */
    public boolean existeConflitoVigencias(AliquotaIssMunicipioServ aliquotaIssMunicipioServ){
        
        DetachedCriteria dc = DetachedCriteria.forClass(AliquotaIssMunicipioServ.class);
        
        dc.add(Restrictions.eq("issMunicipioServico.idIssMunicipioServico",
                               aliquotaIssMunicipioServ.getIssMunicipioServico().getIdIssMunicipioServico()));
        
        if( aliquotaIssMunicipioServ.getIdAliquotaIssMunicipioServ() != null ){
            dc.add(Restrictions.ne("idAliquotaIssMunicipioServ",aliquotaIssMunicipioServ.getIdAliquotaIssMunicipioServ()));
        }            
        
        dc = JTVigenciaUtils.getDetachedVigencia(dc,
	                                               aliquotaIssMunicipioServ.getDtVigenciaInicial(), 
	                                               aliquotaIssMunicipioServ.getDtVigenciaFinal());       
        
        List list = findByDetachedCriteria(dc);
        
        return !list.isEmpty();
    }

    /**
     * Busca as aliquotasIssMunicipioServico de acordo com os critérios de pesquisa e que esteja vigente de acordo com a data base
     * @param idServicoAdicional Identificador do Serviço Adicional
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param idMunicipioIncidencia Identificador do Município de Incidência
     * @param dtBase Data Base
     * @return Lista de aliquotasIssMunicipio com os seguintes dados:
     * @param isCarreteiro Booleano que indica se a pesquisa está sendo feita para cálculo do ISS <code>false</code> ou do ISS Carreteiro <code>true</code> 
     *          - AliquotaIssMunicipioServ.idAliquotaIssMunicipioServ
     *          - AliquotaIssMunicipioServ.pcAliquota
     *          - AliquotaIssMunicipioServ.pcEmbute
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.idIssMunicipioServico
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.ServicoMunicipio.idServicoMunicipio
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.ServicoMunicipio.nrServicoMunicipio
     */
    public List findAliquotaIssMunicipioServicoByCriterios(Long idServicoAdicional, Long idServicoTributo, Long idMunicipioIncidencia, YearMonthDay dtBase, Boolean isCarreteiro) {
        
        List retorno = new ArrayList();
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("aliq.idAliquotaIssMunicipioServ, " +
                          "aliq.pcAliquota, " +
                          "aliq.pcEmbute, " +
                          "iss.idIssMunicipioServico," +
                          "serv.idServicoMunicipio, " +
                          "serv.nrServicoMunicipio, " +
                          "aliq.blRetencaoTomadorServico");
        
        StringBuffer joins = new StringBuffer()
            .append(" inner join aliq.issMunicipioServico iss ")
            .append(" left  join iss.servicoMunicipio serv ")
            .append(" left  join iss.servicoTributo servT ")
            .append(" left  join iss.servicoAdicional servA ")
            .append(" inner join iss.municipio m ");
            
        sql.addFrom( getPersistentClass().getName() + " aliq " + joins.toString() );
        
        if( idServicoAdicional != null ){
            sql.addCustomCriteria("servA.id is not null and servA.id = ? ");
            sql.addCriteriaValue(idServicoAdicional);
        } else if ( isCarreteiro.booleanValue() ){
            sql.addCustomCriteria("servA.id is null");
        }
        
        if( idServicoTributo != null ){
            sql.addCustomCriteria("servT.id is not null and servT.id = ? ");
            sql.addCriteriaValue(idServicoTributo);
        }
        
        sql.addCriteria("m.id","=", idMunicipioIncidencia);
        
        sql.addCustomCriteria("( aliq.dtVigenciaInicial <= ? and aliq.dtVigenciaFinal >= ?  )");
        
        sql.addCriteriaValue(dtBase);
        sql.addCriteriaValue(dtBase);        
        
        List aliquotas = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
        
        if( !aliquotas.isEmpty() ){
            
            AliquotaIssMunicipioServ aliquotaIssMunicipioServ = null;
            ServicoMunicipio servicoMunicipio = null;
            IssMunicipioServico issMunicipioServico = null;
            
            for (Iterator iter = aliquotas.iterator(); iter.hasNext();) {          
                
                Object[] element = (Object[]) iter.next();
                
                aliquotaIssMunicipioServ = new AliquotaIssMunicipioServ();
                
                aliquotaIssMunicipioServ.setIdAliquotaIssMunicipioServ((Long) element[0]);
                aliquotaIssMunicipioServ.setPcAliquota((BigDecimal) element[1]);
                aliquotaIssMunicipioServ.setPcEmbute((BigDecimal) element[2]);
                aliquotaIssMunicipioServ.setBlRetencaoTomadorServico((Boolean) element[6]);
                
                issMunicipioServico = new IssMunicipioServico();
                
                servicoMunicipio = null;
                
                issMunicipioServico.setIdIssMunicipioServico((Long) element[3]);
                
                if( element[4] != null ){
                    servicoMunicipio = new ServicoMunicipio();                
                    servicoMunicipio.setIdServicoMunicipio((Long)element[4]);
                    servicoMunicipio.setNrServicoMunicipio((String)element[5]);
                }
                
                issMunicipioServico.setServicoMunicipio(servicoMunicipio);
                aliquotaIssMunicipioServ.setIssMunicipioServico(issMunicipioServico);
                
                retorno.add(aliquotaIssMunicipioServ);
                
            }
            
            return retorno;
            
        } else {
            return retorno;
        }

    }

    /**
     * Busca a aliquota iss municipio serv para verificação da emissão da nota fiscal de serviço
     *
     * @author José Rodrigo Moraes
     * @since 12/12/2006
     *
     * @param idServicoAdicional
     * @param idServicoTributo
     * @param idMunicipioIncidencia
     * @param dtBase
     * @return
     */
	public AliquotaIssMunicipioServ findAliquotaIssMunicipioServByNfServico(Long idServicoAdicional, Long idServicoTributo, Long idMunicipioIncidencia, YearMonthDay dtBase) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("aliq");		
		hql.addInnerJoin(getPersistentClass().getName(),"aliq");
		hql.addInnerJoin("aliq.issMunicipioServico","iss");
		hql.addLeftOuterJoin("iss.servicoMunicipio","sm");
		hql.addLeftOuterJoin("iss.servicoAdicional","sa");
		hql.addLeftOuterJoin("iss.servicoTributo","st");
		hql.addLeftOuterJoin("iss.municipio","m");
		
		hql.addCriteria("sa.id","=",idServicoAdicional);
		hql.addCriteria("st.id","=",idServicoTributo);
		hql.addCriteria("m.id","=",idMunicipioIncidencia);
		
		hql.addCustomCriteria("? between aliq.dtVigenciaInicial and aliq.dtVigenciaFinal");
		hql.addCriteriaValue(dtBase);
		
		return (AliquotaIssMunicipioServ) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
		
	}


}