package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MunicipioFilialFilOrigem;
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
public class MunicipioFilialFilOrigemDAO extends BaseCrudDao<MunicipioFilialFilOrigem, Long>
{
   
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MunicipioFilialFilOrigem.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("filial",FetchMode.JOIN);
    	fetchModes.put("filial.pessoa",FetchMode.JOIN);
    	fetchModes.put("municipioFilial",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
     	
	}
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("filial.pessoa",FetchMode.JOIN);
    	fetchModes.put("filial", FetchMode.JOIN);
    	fetchModes.put("municipioFilial", FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio", FetchMode.JOIN);
    	fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);


    }
    
    /**
     * Verifica se já existe essa filial relacionada a MunicipioFilial que esteja vigente. (e não seja a própria MunicipioFilialFliOrigem (em caso de alteracao))
     * @author Samuel Herrmann	
     * @param idFilial
     * @param idMunicipioFilial
     * @param idMunicipioFilialFilOrigem
     * @return true caso exista ou false caso não exista 
     */
    
	public boolean verificaVigenciaMunicipioFiliailFil(MunicipioFilialFilOrigem municipioFilialFilOrigem){
		DetachedCriteria dc = createDetachedCriteria();
		if (municipioFilialFilOrigem.getIdMunicipioFilialFilOrigem() != null)
			dc.add(Restrictions.ne("idMunicipioFilialFilOrigem",municipioFilialFilOrigem.getIdMunicipioFilialFilOrigem()));
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial",municipioFilialFilOrigem.getMunicipioFilial().getIdMunicipioFilial()));
		dc.add(Restrictions.eq("filial.idFilial",municipioFilialFilOrigem.getFilial().getIdFilial()));
		JTVigenciaUtils.getDetachedVigencia(dc, municipioFilialFilOrigem.getDtVigenciaInicial(), municipioFilialFilOrigem.getDtVigenciaFinal());
				 
		return findByDetachedCriteria(dc).size() > 0;
	}
	
	
    /**
     * Consulta registros vigentes para o municipio X Filial informado
     * @param idMunicipioFilial
     * @param dtVigenciaFinal 
     * @param dtVigenciaInicial 
     * @return
     */
    public List findFilOrigemVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
       	
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
    	
		return findByDetachedCriteria(dc);
    }
    
    /**
     * Verifica se existe registro para o atendimento e filial informados, dentro da vigencia informada
     * @param idMunicipioFilial
     * @param idFilial
     * @param dtVigencia
     * @return
     */
    public boolean verificaExisteMunicipioFilialFilOrigem(Long idMunicipioFilial, Long idFilial, YearMonthDay dtVigencia){
    	DetachedCriteria dc = createDetachedCriteria();
       	
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("filial.idFilial", idFilial));
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
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

    public boolean verificaMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
	   	DetachedCriteria dc = createDetachedCriteria();

	   	dc.setProjection(Projections.rowCount());
	   	dc.createAlias("municipioFilial", "mf");
     	dc.add(Restrictions.eq("mf.municipio.id", idMunicipio));     	
		dc.add(Restrictions.eq("mf.filial.id", idFilial));
     	dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigencia)));

		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

   //  busca um registro pelo pai(municipioFilial)
    public boolean findFilialByMunFil(Long idMunicipioFilial){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
   }
    
//  busca todas as filiais vigentes de um municipio atendido 
    public List findFilAtendidasByMunicipioFilial(Long idMunicipioFilial) {
 	   StringBuffer hql = new StringBuffer()
 	   		.append("select new Map(pes.nmFantasia as nmFilial, fil.sgFilial as sgFilial) ")
 	   		.append("from "+MunicipioFilialFilOrigem.class.getName()+" as mffo ")
 	   		.append("left outer join mffo.filial as fil ")
 	   		.append("left outer join fil.pessoa as pes ")
 	   		.append("where mffo.municipioFilial.idMunicipioFilial = ? ")
 	   		.append("and mffo.dtVigenciaInicial <= ? ")
 	   		.append("and mffo.dtVigenciaFinal >= ? ");
 	   
 	   YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();		
 	   List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
 	   return lista; 
    }
}