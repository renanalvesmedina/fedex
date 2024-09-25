package com.mercurio.lms.vendas.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.VersaoDescritivoPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VersaoDescritivoPceDAO extends BaseCrudDao<VersaoDescritivoPce, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VersaoDescritivoPce.class;
    }

    public List findDataAlertPCE(Long idVersaoDescritivoPce) {
    	StringBuffer sb = new StringBuffer("SELECT ");
    	//PROJECTIONS
    	sb.append("C.idCliente, ").append("P.nrIdentificacao, ").append("P.nmPessoa, ").
    		append("DP.dsDescritivoPce, ")
    		.append(" VCP.dsRegiaoAtuacao, VCP.tpFormaComunicacao, CO.nmContato, CO.dsEmail, ")
    		.append("CO.idContato, VDP.idVersaoDescritivoPce, P.tpIdentificacao, ")
    		.append("US.idUsuario, US.nrMatricula, US.nmUsuario, US.dsEmail ");
    	//JOINS
    	sb.append(" FROM ").append(VersaoDescritivoPce.class.getName()).append(" AS VDP ").
    		append("INNER JOIN VDP.versaoPce AS VP ").
    		append("INNER JOIN VDP.descritivoPce AS DP ").
    		append("INNER JOIN VP.cliente as C ").
    		append("INNER JOIN C.pessoa as P ").
    		append("LEFT JOIN VDP.versaoContatoPces as VCP ").
    		append("LEFT JOIN VCP.contato as CO ").
    		append("LEFT JOIN VCP.usuario as US ");
    	
    	//WHERE
    	sb.append("WHERE VDP.idVersaoDescritivoPce = ?");
    	sb.append("order by CO.idContato");
    	
    	return getAdsmHibernateTemplate().find(sb.toString(),idVersaoDescritivoPce);
    }
    
    public List findEnderecoByContanto(Long idContato) {
    	ProjectionList pl = Projections.projectionList()
    					 .add(Projections.property("EP.dsEndereco"))
    					 .add(Projections.property("EP.nrEndereco"))
    					 .add(Projections.property("EP.dsComplemento"))
    					 .add(Projections.property("EP.dsBairro"))
    					 .add(Projections.property("EP.nrCep"))
    					 .add(Projections.property("MU.nmMunicipio"))
    					 .add(Projections.property("UF.nmUnidadeFederativa"))
    					 .add(Projections.property("PA.nmPais"))
    					 .add(Projections.property("TL.dsTipoLogradouro"));
    					 
    	DetachedCriteria dc = DetachedCriteria.forClass(EnderecoPessoa.class,"EP")
    					   .setProjection(pl)
    					   .createAlias("EP.tipoLogradouro","TL")
    					   .createAlias("EP.municipio","MU")
    					   .createAlias("MU.unidadeFederativa","UF")
    					   .createAlias("UF.pais","PA")
    					   .createAlias("EP.telefoneEnderecos","TE")
    					   .createAlias("TE.telefoneContatos","TC")
    					   .createAlias("TC.contato","CO")
    					   .add(Restrictions.eq("CO.idContato",idContato));
    					   
       return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public List findToConfirmaRecebimentoDoAlerta(Long idVersaoDescritivoPce) {
    	ProjectionList pl = Projections.projectionList()
						 .add(Projections.property("CL.idCliente"))
						 .add(Projections.property("DP.idDescritivoPce"));

    	DetachedCriteria dc = DetachedCriteria.forClass(VersaoDescritivoPce.class,"VDP")
    					   .setProjection(pl)
    					   .createAlias("VDP.descritivoPce","DP")
    					   .createAlias("VDP.versaoPce","VP")
    					   .createAlias("VP.cliente","CL")
    					   .add(Restrictions.eq("VDP.idVersaoDescritivoPce",idVersaoDescritivoPce));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public List findToifExistPceByCriteria(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
    	DetachedCriteria dc = createBaseQuery(idCliente, cdProcesso, cdEvento, cdOcorrencia);
    	dc.setProjection(Projections.projectionList()
    					 .add(Projections.property("VDP.idVersaoDescritivoPce"))
				.add(Projections.property("DP.tpAcao")));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public VersaoDescritivoPce findVersaoDescritivoPceByCriteria(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
    	DetachedCriteria dc = createBaseQuery(idCliente, cdProcesso, cdEvento, cdOcorrencia);
    	return (VersaoDescritivoPce)getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Find ID VersaoDescritivo PCE
     * @author Andre Valadas
     * 
     * @param idCliente
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     * @return
     */
    public Long findIdVersaoDescritivoPceByCriteria(final Long idCliente, final Long cdProcesso, final Long cdEvento, final Long cdOcorrencia) {
    	DetachedCriteria dc = createBaseQuery(idCliente, cdProcesso, cdEvento, cdOcorrencia);
    	dc.setProjection(Projections.property("VDP.idVersaoDescritivoPce"));
    	return (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Cria DetachedCriteria padrão para varias operações do pojo
     * @author André Valadas
     * 
     * @param idCliente
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     * @return
     */
    private DetachedCriteria createBaseQuery(final Long idCliente, final Long cdProcesso, final Long cdEvento, final Long cdOcorrencia) {
    	YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
    	return DetachedCriteria.forClass(VersaoDescritivoPce.class,"VDP")
				   .createAlias("VDP.descritivoPce","DP")
				   .createAlias("DP.ocorrenciaPce","OP")
				   .createAlias("OP.eventoPce","EP")
				   .createAlias("EP.processoPce","PP")
				   .createAlias("VDP.versaoPce","VP")
				   .createAlias("VP.cliente","CL")
				   .add(Restrictions.eq("CL.idCliente",idCliente))
				   .add(Restrictions.eq("OP.cdOcorrenciaPce",cdOcorrencia))
				   .add(Restrictions.eq("EP.cdEventoPce",cdEvento))
				   .add(Restrictions.eq("PP.cdProcessoPce",cdProcesso))
				   .add(Restrictions.le("VP.dtVigenciaInicial", ymd))
				   .add(Restrictions.ge("VP.dtVigenciaFinal", ymd));
    }
    
    public List findDsDescritoById(Long idVersaoDescritivoPce) {
    	ProjectionList pl = Projections.projectionList()
    					 .add(Projections.property("DP.dsDescritivoPce"));
    	DetachedCriteria dc = DetachedCriteria.forClass(VersaoDescritivoPce.class,"VDP")
    					   .setProjection(pl)
    					   .createAlias("descritivoPce","DP")
    					   .add(Restrictions.eq("idVersaoDescritivoPce",idVersaoDescritivoPce));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    
    /**
     * Verifica se o cliente do documento servico em um manifesto tem VersaoDescritivoPce
     * @param idManifesto
     * @param tpManifesto
     * @param tpAcao
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     * @return
     */
    public List <Long> findVersaoDescritivoPcebyManifestoAndTpAcao(Long idManifesto, String tpAcao, Long cdProcesso, 
    		Long cdEvento, Long cdOcorrencia){
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
    	List filtro = new ArrayList();
    	filtro.add(cdOcorrencia);
    	filtro.add(cdEvento);
    	filtro.add(cdProcesso);
    	filtro.add(idManifesto);
    	filtro.add(tpAcao);
    	filtro.add(dataAtual);
    	filtro.add(dataAtual);
    	
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idVersaoDescritivoPce",Hibernate.LONG); 
			}
		};

		HibernateCallback hcb = findBySql( mountSql(), filtro.toArray(),csq);
		return (List)getAdsmHibernateTemplate().execute(hcb); 
	}
	
	public static HibernateCallback findBySql(final String sql,final Object[] parametersValues,final ConfigureSqlQuery configQuery) {

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				
				// chama a impl que configura a SQLQuery.
				configQuery.configQuery(query);
			
				if (parametersValues != null) {
					for (int i = 0; i < parametersValues.length; i++) {
						if( parametersValues[i] instanceof YearMonthDay){
							query.setParameter(i, parametersValues[i], Hibernate.custom(JodaTimeYearMonthDayUserType.class));
						}else{
							query.setParameter(i, parametersValues[i]);
						}
					}
				}
				return query.list();
			}
		};
		return hcb;
	}
    
    private String mountSql(){
    	
      	StringBuffer sql = new StringBuffer();
    	
      	sql.append("SELECT VDP.ID_VERSAO_DESCRITIVO_PCE idVersaoDescritivoPce ");
      
      	//FROM 
      	sql.append("FROM ");
      	sql.append("VERSAO_DESCRITIVO_PCE VDP, ");
        sql.append("DESCRITIVO_PCE DP, ");
       	sql.append("OCORRENCIA_PCE OP, ");
       	sql.append("EVENTO_PCE EP, ");
       	sql.append("PROCESSO_PCE PP, ");
        sql.append("VERSAO_PCE VP, ");
        sql.append("CLIENTE C, ");
        sql.append("MANIFESTO_ENTREGA_DOCUMENTO MED, ");
        sql.append("MANIFESTO_ENTREGA ME, ");
        sql.append("MANIFESTO M, ");
        sql.append("DOCTO_SERVICO DS ");
        
        //WHERE
        sql.append("WHERE ");
        sql.append("VDP.ID_DESCRITIVO_PCE = DP.ID_DESCRITIVO_PCE ");
        sql.append("AND DP.ID_OCORRENCIA_PCE = OP.ID_OCORRENCIA_PCE  ");
        sql.append("AND OP.ID_EVENTO_PCE = EP.ID_EVENTO_PCE ");
        sql.append("AND EP.ID_PROCESSO_PCE = PP.ID_PROCESSO_PCE ");
        sql.append("AND VDP.ID_VERSAO_PCE = VP.ID_VERSAO_PCE ");
        sql.append("AND VP.ID_CLIENTE=C.ID_CLIENTE  ");
        sql.append("AND ( DS.ID_CLIENTE_REMETENTE = C.ID_CLIENTE OR DS.ID_CLIENTE_DESTINATARIO = C.ID_CLIENTE ) ");
        sql.append("AND MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
        sql.append("AND ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ");
        sql.append("AND M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ");
        
        sql.append("AND OP.CD_OCORRENCIA_PCE = ? " );
        sql.append("AND EP.CD_EVENTO_PCE = ? "  );
        sql.append("AND PP.CD_PROCESSO_PCE = ? " );
        sql.append("AND M.ID_MANIFESTO = ? ");
        sql.append("AND DP.TP_ACAO = ? " );
        sql.append("AND VP.DT_VIGENCIA_INICIAL <= ? " );
        sql.append("AND VP.DT_VIGENCIA_FINAL >= ? " );
    	
        return sql.toString();
    }
    
    
}