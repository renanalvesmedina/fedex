package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MunicipioFilialSegmento;
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
public class MunicipioFilialSegmentoDAO extends BaseCrudDao<MunicipioFilialSegmento, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("segmentoMercado",FetchMode.JOIN);
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
	}
	
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("segmentoMercado",FetchMode.JOIN);
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
		
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MunicipioFilialSegmento.class;
    }

   public boolean verificaSegmentosMercadoAtendidos(MunicipioFilialSegmento munFilSegmentoMercado){
   	 DetachedCriteria dc = createDetachedCriteria();
   	 
   	 // caso seja um update em um municipioFilialSegmento,
   	 // então o próprio registro não é critério de pesquisa
   	 if(munFilSegmentoMercado.getIdMunicipioFilialSegmento() != null){
   	 	dc.add(Restrictions.ne("idMunicipioFilialSegmento",munFilSegmentoMercado.getIdMunicipioFilialSegmento() ));
   	 }
   	 
   	 // testando a vigencia de municipioFilialSegmento 
   	 dc = JTVigenciaUtils.getDetachedVigencia(dc, munFilSegmentoMercado.getDtVigenciaInicial(), munFilSegmentoMercado.getDtVigenciaFinal()); 
   	 
   	 // para determinado segmento de mercado
   	 dc.add(Restrictions.eq("segmentoMercado.idSegmentoMercado",munFilSegmentoMercado.getSegmentoMercado().getIdSegmentoMercado()));
   	 
   	 // relacionando com municipioFilial
   	 DetachedCriteria dcMunicipioFilial = dc.createCriteria("municipioFilial");
   	 dcMunicipioFilial.add(Restrictions.eq("idMunicipioFilial",munFilSegmentoMercado.getMunicipioFilial().getIdMunicipioFilial()));

   	 
   	 // retorna se encontrou algum registro
   	 return !(findByDetachedCriteria(dcMunicipioFilial).size() > 0);
   	 
   }

   /**
    * Consulta registros vigentes para o municipio X Filial informado
    * @param idMunicipioFilial
    * @param dtVigenciaFinal 
    * @param dtVigenciaInicial 
    * @return
    */
   public List findSegmentoVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
	   DetachedCriteria dc = createDetachedCriteria();
   	
	   dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
	   JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
	      	
	   return findByDetachedCriteria(dc);
   }
   
   /**
    * Verifica se existem registros para o atendimento e segmento informados, dentro da vigencia informada
    * @param idMunicipioFilial
    * @param idSegmento
    * @param dtVigencia
    * @return
    */
   public boolean verificaExisteMunicipioFilialSegmento(Long idMunicipioFilial, Long idSegmento, YearMonthDay dtVigencia){
	 	DetachedCriteria dc = createDetachedCriteria();
	   	
	 	dc.setProjection(Projections.rowCount());
	 	dc.add(Restrictions.eq("segmentoMercado.idSegmentoMercado", idSegmento));
	   	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
		
		return ((Integer) findByDetachedCriteria(dc).get(0)).intValue() > 0;
		
   }
   
   public boolean verificaExisteMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigencia){
	 	DetachedCriteria dc = createDetachedCriteria();
	   	
	 	dc.setProjection(Projections.rowCount());	 	
	   	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
	   	dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
		
		return ((Integer) findByDetachedCriteria(dc).get(0)).intValue() > 0;
		
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
   
// busca um registro pelo pai(municipioFilial)
   public boolean findSegmentoByMunFil(Long idMunicipioFilial){
   	DetachedCriteria dc = createDetachedCriteria();
   	dc.setProjection(Projections.rowCount());
   	dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
   	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
  }
   
   //busca todos os segmentos vigentes de um municipio atendido 
   public List findSegmentoAtendidoByMunicipioFilial(Long idMunicipioFilial) {
	   StringBuffer hql = new StringBuffer() 
	   		.append("select new Map(mfs.segmentoMercado.dsSegmentoMercado as dsSegmentoMercado )")
	   		.append("from "+MunicipioFilialSegmento.class.getName()+" as mfs ")
	   		.append("where mfs.municipioFilial.idMunicipioFilial = ? ")
	   		.append("and mfs.dtVigenciaInicial <= ? ")
	   		.append("and mfs.dtVigenciaFinal >= ?");
	   
	   YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();		
	   List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
	   return lista; 
   }
   

}