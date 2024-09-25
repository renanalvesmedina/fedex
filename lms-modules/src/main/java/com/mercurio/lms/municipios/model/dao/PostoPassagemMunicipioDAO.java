package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.PostoPassagemMunicipio;
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
public class PostoPassagemMunicipioDAO extends BaseCrudDao<PostoPassagemMunicipio, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoPassagemMunicipio.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("postoPassagem",FetchMode.JOIN);
		fetchModes.put("postoPassagem.municipio",FetchMode.JOIN);
		fetchModes.put("postoPassagem.rodovia",FetchMode.JOIN);
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchModes);
    }
    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("postoPassagem",FetchMode.JOIN);
    	fetchModes.put("postoPassagem.municipio",FetchMode.JOIN);
    	fetchModes.put("postoPassagem.rodovia",FetchMode.JOIN);
    	fetchModes.put("postoPassagem.concessionaria",FetchMode.JOIN);
    	fetchModes.put("postoPassagem.concessionaria.pessoa",FetchMode.JOIN);
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchModes);
    }
    
    public boolean isVigenciaValida(PostoPassagemMunicipio ppm) {
    	DetachedCriteria dc = createDetachedCriteria();
    	if (ppm.getIdPostoPassagemMunicipio() != null)
    		dc.add(Restrictions.ne("idPostoPassagemMunicipio",ppm.getIdPostoPassagemMunicipio()));
    	dc.add(Restrictions.eq("municipioFilial.id",ppm.getMunicipioFilial().getIdMunicipioFilial()));
    	dc.add(Restrictions.eq("postoPassagem.id",ppm.getPostoPassagem().getIdPostoPassagem()));
    	JTVigenciaUtils.getDetachedVigencia(dc,ppm.getDtVigenciaInicial(),ppm.getDtVigenciaFinal());
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (rs == null || rs.size() == 0)
    		return true;
    	return false;
    }
   
    /**
     * Conta a quantidade de postos de passagem entre o municipio e a filial, vigentes na data informada
     * @param idMunicipioFilial
     * @param dtVigencia
     * @return
     */
   public Integer findQtdPostosPassagemEntreMunicipioEFilial(Long idMunicipioFilial, YearMonthDay dtVigencia){
	   
	   DetachedCriteria dc = createDetachedCriteria();
	   
	   dc.setProjection(Projections.rowCount());
	   dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
	   dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
	   dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
	   
	   return (Integer)findByDetachedCriteria(dc).get(0);
	   
   }
   
   /**
    * Conta a quantidade de postos de passagem bidirecionais (ida e volta) entre o municipio e a filial, vigentes na data informada
    * @param idMunicipioFilial
    * @param dtVigencia
    * @return
    */
   public Integer findQtdPostosPassagemBidirecionalEntreMunicipioEFilial(Long idMunicipioFilial, YearMonthDay dtVigencia){
	   
	   DetachedCriteria dc = createDetachedCriteria();
	   
	   dc.setProjection(Projections.rowCount());
	   dc.createAlias("postoPassagem", "pp");
	   dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
	   dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
	   dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
	   dc.add(Restrictions.eq("pp.tpSentidoCobranca", "BD"));
	   
	   return (Integer)findByDetachedCriteria(dc).get(0);
	   
   }
   public List findPostosPassagemVigentesByMunFil(Long idMunicipioFilial){
	   StringBuffer hql = new StringBuffer()
	   .append("select new Map(pp.tpPostoPassagem as postoPassagem_tpPostoPassagem, ")
	   .append("mun.nmMunicipio as postoPassagem_municipio_nmMunicipio, ")
	   .append("pp.tpSentidoCobranca as postoPassagem_tpSentidoCobranca, ")
	   .append("pp.nrKm as postoPassagem_nrKm, ")
	   .append("rod.sgRodovia as postoPassagem_rodovia_sgRodovia, ")
	   .append("pes.nmPessoa as postoPassagem_concessionaria_pessoa_nmPessoa, ppm.idPostoPassagemMunicipio as idPostoPassagemMunicipio) ")
	   .append("from "+PostoPassagemMunicipio.class.getName()+" as ppm ")
	   .append("inner join ppm.postoPassagem as pp ")
	   .append("left outer join pp.municipio as mun ")
	   .append("left outer join pp.rodovia as rod ")
	   .append("left outer join pp.concessionaria as conce ")
	   .append("left outer join conce.pessoa as pes ")
	   .append("where ppm.municipioFilial.idMunicipioFilial = ? ")
	   .append("and ppm.dtVigenciaInicial <= ? ")
	   .append("and ppm.dtVigenciaFinal >= ? ")
	   
	   .append("order by pp.tpPostoPassagem, mun.nmMunicipio ");
	   
	   YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
	   List lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idMunicipioFilial,dataAtual,dataAtual});
	   return lista;
		  
   } 
    
   
   public List findByIdPostoPassagemMunicipio(Long idPostoPassagemMunicipio){
	   StringBuffer hql = new StringBuffer()
	   .append("select new Map(" )
	   .append("pp.tpPostoPassagem as postoPassagem_tpPostoPassagem_description, ")
	   .append("mun.nmMunicipio as postoPassagem_municipio_nmMunicipio, ")
	   .append("pp.tpSentidoCobranca as postoPassagem_tpSentidoCobranca_description, ")
	   .append("pp.nrKm as postoPassagem_nrKm, ")
	   .append("rod.sgRodovia as postoPassagem_rodovia_sgRodovia, ")
	   .append("pes.nmPessoa as postoPassagem_concessionaria_pessoa_nmPessoa, ")
	   .append("ppm.idPostoPassagemMunicipio as idPostoPassagemMunicipio, ")
	   .append("pp.idPostoPassagem as idPostoPassagem, ")
	   
	   .append("fil.sgFilial as sgFilial, ")
	   .append("filPes.nmFantasia as nmFantasia, ")
	   .append("munFil.nmMunicipio as municipioFilial_municipio_nmMunicipio, ")
	   .append("munFilUf.sgUnidadeFederativa as municipioFilial_municipio_unidadeFederativa_sgUnidadeFederativa, ")
	   .append("munFilUf.nmUnidadeFederativa as municipioFilial_municipio_unidadeFederativa_nmUnidadeFederativa, ")
	   .append("munFilPais.nmPais as municipioFilial_municipio_unidadeFederativa_pais_dsPais) ")
	   
	   .append("from "+PostoPassagemMunicipio.class.getName()+" as ppm ")
	   
	   .append("left outer join ppm.municipioFilial.filial as fil ")
	   .append("left outer join fil.pessoa as filPes ")
	   .append("left outer join ppm.municipioFilial.municipio as munFil ")
	   .append("left outer join munFil.unidadeFederativa as munFilUf ")
	   .append("left outer join munFilUf.pais as  munFilPais ")
	   
	   .append("inner join ppm.postoPassagem as pp ")
	   .append("left outer join pp.municipio as mun ")
	   .append("left outer join pp.rodovia as rod ")
	   .append("left outer join pp.concessionaria as conce ")
	   .append("left outer join conce.pessoa as pes ")
	   .append("where ppm.idPostoPassagemMunicipio = ? ");
	   
	   	   
	    List lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idPostoPassagemMunicipio});
	   return lista;
		  
   }
   
   /**
    * LMS-2537
    * Rotina para calculo de pontos de passagens entre um fluxo de carga 
    * 
    * @param idMunicipioOrigem
    * @param idMunicipioDestino
    * @param idFilialOrigem
    * @param idFilialDestino
    * @param dtPesquisa
    * @param idFFServico
    * @return
    */
   public Integer countPostosPassagens(Long idMunicipioOrigem, Long idMunicipioDestino, Long idFilialOrigem, Long idFilialDestino,
			YearMonthDay dtPesquisa, Long idFFServico) {
   
		SqlTemplate sqlTemplate = getSqlCountPostosPassagens(idMunicipioOrigem, idMunicipioDestino, idFilialOrigem, idFilialDestino, dtPesquisa, idFFServico);		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
	  			sqlQuery.addScalar("QT_PEDAGIO",Hibernate.LONG);
			}
		};	
   
		Long result = (Long)getAdsmHibernateTemplate().findByIdBySql(sqlTemplate.getSql(), sqlTemplate.getCriteria(),csq);		
		return result.intValue();	
	}
   
   /**
    * Retorna o sqltemplate para a consulta a ser usada na rotina de Conta Postos de passagens(LMS-2537)
    * @param idMunicipioOrigem
    * @param idMunicipioDestino
    * @param idFilialOrigem
    * @param idFilialDestino
    * @param dtPesquisa
    * @param idFFServico
    * @return
    */
   public static SqlTemplate getSqlCountPostosPassagens(Long idMunicipioOrigem, Long idMunicipioDestino, Long idFilialOrigem, Long idFilialDestino,
			YearMonthDay dtPesquisa, Long idFFServico) {
   
		if (idFilialOrigem == null) {
			throw new IllegalArgumentException("O id da filial de origem passada como parametro não pode ser nulo");
		}
		if (idFilialDestino == null) {
			throw new IllegalArgumentException("O id da filial de destino passada como parametro não pode ser nulo");
		}
   
		List<Object> values = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT \n");
		sb.append("  --SOMAR TODOS OS POSTOS DE PASSAGEM DO MUNICIPIO FILIAL DE ORIGEM \n");
		sb.append("  ( \n");
		sb.append("            ( SELECT COUNT ( 1 ) \n");
		sb.append("              FROM   posto_passagem_municipio ppm_fo \n");
		sb.append("              JOIN   posto_passagem           pp_fo  ON ppm_fo.id_posto_passagem = pp_fo.id_posto_passagem \n");
		sb.append("              JOIN   municipio_filial         m_fo ON ppm_fo.id_municipio_filial = m_fo.id_municipio_filial \n");
		sb.append("              JOIN   endereco_pessoa          e_fo  ON  m_fo.id_municipio = e_fo.id_municipio \n");
		sb.append("              JOIN   pessoa                   pessoa_fo   ON  e_fo.id_endereco_pessoa = pessoa_fo.id_endereco_pessoa \n");
		sb.append("              JOIN   filial                   filial_o     ON  filial_o.id_filial = pessoa_fo.id_pessoa \n");
   
		sb.append("              ,      posto_passagem_municipio ppm_fd \n");
		sb.append("              JOIN   posto_passagem           pp_fd  ON ppm_fd.id_posto_passagem = pp_fd.id_posto_passagem \n");
		sb.append("              JOIN   municipio_filial         m_fd ON ppm_fd.id_municipio_filial = m_fd.id_municipio_filial \n");
		sb.append("              JOIN   endereco_pessoa          e_fd  ON  m_fd.id_municipio = e_fd.id_municipio \n");
		sb.append("              JOIN   pessoa                   pessoa_fd   ON  e_fd.id_endereco_pessoa = pessoa_fd.id_endereco_pessoa \n");
		sb.append("              JOIN   filial                   filial_d     ON  filial_d.id_filial = pessoa_fd.id_pessoa \n");
   
		sb.append(" \n");
   
		sb.append("WHERE  1=1 \n");
   
		if(idFilialOrigem != null) {
			sb.append(" 		AND filial_o.id_filial = ?  \n");
			values.add(idFilialOrigem);
		}
   
		if(idFilialDestino != null) {
			sb.append("              AND filial_d.id_filial = ?   \n");
			values.add(idFilialDestino);
		}
   
		if(idMunicipioOrigem != null) {
			sb.append(" 		AND m_fo.id_municipio = ?  \n");
			values.add(idMunicipioOrigem);
		}
   
		if(idMunicipioDestino != null) {
			sb.append(" 		AND m_fd.id_municipio = ?  \n");
			values.add(idMunicipioDestino);
		}
   
		sb.append("              AND   ? BETWEEN ppm_fd.dt_vigencia_inicial AND ppm_fd.dt_vigencia_final \n");
		sb.append("              AND   ? BETWEEN ppm_fo.dt_vigencia_inicial AND ppm_fo.dt_vigencia_final \n");
		sb.append("              AND   ? BETWEEN pp_fd.dt_vigencia_inicial AND pp_fd.dt_vigencia_final \n");
		sb.append("              AND   ? BETWEEN pp_fo.dt_vigencia_inicial AND pp_fo.dt_vigencia_final \n");
   
		if(dtPesquisa == null) {
			dtPesquisa = JTDateTimeUtils.getDataAtual();			
		}
		values.add(dtPesquisa);
		values.add(dtPesquisa);
		values.add(dtPesquisa);
		values.add(dtPesquisa);
   
		sb.append("              AND   (   m_fo.id_municipio != m_fd.id_municipio \n");
		sb.append("                    OR  m_fo.id_municipio = m_fd.id_municipio \n");
		sb.append("                    AND pp_fd.tp_sentido_cobranca = 'BD'  ) ) \n");
   
   
		sb.append("              + ( \n");
		sb.append("              SELECT COUNT ( 1 ) \n");
		sb.append("              FROM   posto_passagem_trecho ppt \n");
		sb.append("              JOIN   ordem_filial_fluxo    offo ON ppt.id_filial_origem = offo.id_filial \n");
		sb.append("              JOIN   ordem_filial_fluxo    offd ON ppt.id_filial_destino = offd.id_filial \n");
		sb.append("              ,  fluxo_filial ff \n");
   
		sb.append("              WHERE  1=1   \n");
   
		if(idFilialOrigem != null) {
			sb.append("              AND ff.id_filial_origem = ?  \n");
			values.add(idFilialOrigem);
		}
   
		if(idFilialDestino != null) {
			sb.append("              AND    ff.id_filial_destino = ?  \n");
			values.add(idFilialDestino);
		}
   
		if(idFFServico != null) {
			sb.append("              AND  ff.id_servico = ?  \n");
			values.add(idFFServico);
		}
		else {
			sb.append("              AND  ff.id_servico is null  \n");
		}
   
		sb.append("              AND   ? between ff.dt_vigencia_inicial and ff.dt_vigencia_final \n");
		values.add(dtPesquisa);
    
		sb.append("              AND    offo.id_fluxo_filial = ff.id_fluxo_filial \n");
		sb.append("              AND    offd.id_fluxo_filial = ff.id_fluxo_filial \n");
		sb.append("              AND    offo.nr_ordem + 1 = offd.nr_ordem \n");
		sb.append("              AND    ? BETWEEN ppt.dt_vigencia_inicial AND ppt.dt_vigencia_final ))  AS QT_PEDAGIO \n");
		values.add(dtPesquisa);
		
		sb.append("  FROM            dual \n");
		
		SqlTemplate sqlTemplate = new SqlTemplate(sb.toString());
		sqlTemplate.addCriteriaValue(values.toArray());
		
		return sqlTemplate;
	}  
   
}