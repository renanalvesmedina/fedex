package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MunicipioFilialUFOrigem;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MunicipioFilialUFOrigemDAO extends BaseCrudDao<MunicipioFilialUFOrigem, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("unidadeFederativa",FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
		fetchModes.put("unidadeFederativa",FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MunicipioFilialUFOrigem.class;
    }
    /**
     * Verifica se já existe uma unidadeFederativa relacionada a MunicipioFilial que esteja vigente. (e não seja a própria MunicipioFilialUFOrigem (em caso de alteracao))
     * @author Samuel Herrmann	
     * @param idUnidadeFederativa
     * @param idMunicipioFilial
     * @param idMunicipioFilialUFOrigem
     * @param dtVigenciaIniciaç TODO
     * @param dtVigenciaFinal TODO
     * @return true caso exista ou false caso não exista 
     */
    
	public boolean verificaUnidFederatAtendidas(Long idUnidadeFederativa,Long idMunicipioFilial, Long idMunicipioFilialUFOrigem, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();
		if (idMunicipioFilialUFOrigem != null)
			dc.add(Restrictions.ne("idMunicipioFilialUFOrigem",idMunicipioFilialUFOrigem));
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial",idMunicipioFilial));
		dc.add(Restrictions.eq("unidadeFederativa.idUnidadeFederativa",idUnidadeFederativa));
		dc.add(Restrictions.eq("unidadeFederativa.idUnidadeFederativa",idUnidadeFederativa));
		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
		
		return findByDetachedCriteria(dc).size() > 0;
	}

    /**
     * Consulta registros vigentes para o municipio X Filial informado
     * @param idMunicipioFilial
     * @param dtVigenciaFinal 
     * @param dtVigenciaInicial 
     * @return
     */
    public List findUfOrigemVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	    	    	
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	
		return findByDetachedCriteria(dc);
    }
    
    /**
     * Verifica se existe registro para o atendimento e a UF informados, dentro da vigencia informada
     * @param idMunicipioFilial
     * @param idUf
     * @param dtVigencia
     * @return
     */
    public boolean verificaExisteMunicipioFilialUfOrigem(Long idMunicipioFilial, Long idUf, YearMonthDay dtVigencia){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	dc.add(Restrictions.eq("unidadeFederativa.idUnidadeFederativa", idUf));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
		
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
		
    }
    
    public boolean verificaExisteMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigencia){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
		
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
    }

	public boolean verificaMunicipioFilialVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.createAlias("municipioFilial", "mf");
		dc.add(Restrictions.eq("mf.municipio.id", idMunicipio)); 		
		dc.add(Restrictions.eq("mf.filial.id", idFilial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));

		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

    //busca um registro pelo pai(municipioFilial)
    public boolean findUfByMunFil(Long idMunicipioFilial) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

    //busca todos as uf vigentes de um municipio atendido 
    public List findUFsAtendidasByMunicipioFilial(Long idMunicipioFilial) {
 	   StringBuffer hql = new StringBuffer()
 	   		.append("select new Map(mfuf.unidadeFederativa.sgUnidadeFederativa as sgUnidadeFederativa, mfuf.unidadeFederativa.nmUnidadeFederativa as nmUnidadeFederativa) ")
 	   		.append("from "+MunicipioFilialUFOrigem.class.getName()+" as mfuf ")
 	   		.append("where mfuf.municipioFilial.idMunicipioFilial = ? ")
 	   		.append("and mfuf.dtVigenciaInicial <= ? ")
 	   		.append("and mfuf.dtVigenciaFinal >= ?");
 	   
 	   YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();		
 	   List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
 	   return lista; 
    }
    
}