package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
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
public class MeioTranspProprietarioDAO extends BaseCrudDao<MeioTranspProprietario, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTranspProprietario.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("meioTransporte",FetchMode.JOIN);
    	lazyFindPaginated.put("proprietario",FetchMode.JOIN);
    	lazyFindPaginated.put("proprietario.pessoa",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("meioTransporte",FetchMode.JOIN);
    	lazyFindById.put("proprietario",FetchMode.JOIN);
    	lazyFindById.put("proprietario.pessoa",FetchMode.JOIN);
    }
    
    /**
     * Valida se já existe algum registro vigente com mesmo Meio de Transporte para um Proprietário.
     * 
     * @param idmeioTranspProprietario Caso deseja-se excluir uma associação da validação.
     * @param idMeioTransporte
     * @return
     */
    public boolean validateApenasUmVigente(Long idMeioTranspProprietario, Long idMeioTransporte,YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {    	
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),idMeioTranspProprietario,dtVigenciaInicial,dtVigenciaFinal);

    	dc.createAlias("meioTransporte","mt");
    	dc.add(Restrictions.eq("mt.idMeioTransporte",idMeioTransporte));
    	return (getAdsmHibernateTemplate().findByDetachedCriteria(dc)).size() > 0;
    }
    
    public List findInfoMeioTransporte(Long idMeioTransporte) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("select new Map(")
    	   .append(" prop.idProprietario as idProprietario, ")
    	   .append(" p.nrIdentificacao as nrIdentificacaoPessoa, ")
    	   .append(" p.tpIdentificacao as tpIdentificacaoPessoa, ")
    	   .append(" p.idPessoa as idPessoa, ")
    	   .append(" p.nmPessoa as nmPessoa, ")
    	   .append(" prop.nrPis as nrPis, ") 
    	   .append(" prop.nrAntt as nrAntt, ")
    	   .append(" ender.dsEndereco as dsEndereco, ")
    	   .append(" ender.nrEndereco as nrEndereco, ")
    	   .append(" ender.dsComplemento as dsComplemento, ")
    	   .append(" ender.dsBairro as dsBairro, ")
    	   .append(" ender.nrCep as nrCep, ")
    	   .append(" mun.nmMunicipio as nmMunicipio, ")
    	   .append(" uf.sgUnidadeFederativa as sgUnidadeFederativa, ")
    	   .append(" pais.nmPais as nmPais, ")
    	   .append(" tel.nrTelefone as nrTelefone, ")
    	   .append(" tel.nrDdd as nrDdd, ")
    	   .append(" p.dsEmail as dsEmail )")
    	   .append("from " + MeioTranspProprietario.class.getName() + " as mtProp \n")
    	   .append(" left join mtProp.proprietario as prop \n")
    	   .append(" left join mtProp.meioTransporte as mt \n")
    	   .append(" left join prop.pessoa as p \n")
    	   .append(" left join p.enderecoPessoas as ender \n")
    	   .append(" left join ender.municipio as mun \n")
    	   .append(" left join mun.unidadeFederativa as uf \n")
    	   .append(" left join uf.pais as pais \n")
    	   .append(" left join prop.telefoneEndereco as tel \n")
    	   .append(" left join tel.telefoneContatos as telCon \n")
    	   .append(" where mt.idMeioTransporte = :idMT ")
    	   .append(" and mtProp.dtVigenciaInicial <= :dtAtual and \n")
    	   .append(" (mtProp.dtVigenciaFinal >= :dtAtual or mtProp.dtVigenciaFinal is null) \n");
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{
    					"idMT",
    					"dtAtual"
    			},
    			new Object[]{
    					idMeioTransporte,
    					JTDateTimeUtils.getDataAtual()
    			}
    		);
    }
    
    /**
     * Retorna o proprietario vigente do meio de transporte
     * 
     * @param idMeioTransporte
     * @return
     */
    
    public List findProprietarioByIdMeioTransporte(Long idMeioTransporte,YearMonthDay vigenteEm) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select prop from ").append(MeioTranspProprietario.class.getName()).append(" as mtProp \n")
    	   .append(" inner join mtProp.proprietario as prop \n")
    	   .append(" inner join FETCH prop.pessoa as p \n")
    	   .append(" where mtProp.meioTransporte.id = :idMT ")
    	   .append("   and mtProp.dtVigenciaInicial <= :dtAtual \n")
    	   .append("   and mtProp.dtVigenciaFinal >= :dtAtual \n");
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),new String[]{"idMT","dtAtual"},	new Object[]{idMeioTransporte,vigenteEm});
    }
    
    public List findProprietarioByMeioTransporte(Long idMeioTransporte) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("select new Map(")
    	   .append(" prop.idProprietario as proprietario_idProprietario, ")
    	   .append(" p.nrIdentificacao as proprietario_pessoa_nrIdentificacao, ")
    	   .append(" p.tpIdentificacao as proprietario_pessoa_tpIdentificacao, ")
    	   .append(" p.nrIdentificacao as nrIdentificacao, ")
    	   .append(" p.tpIdentificacao as tpIdentificacao, ")
    	   .append(" p.idPessoa as idPessoa, ")
    	   .append(" p.nmPessoa as proprietario_pessoa_nmPessoa) ")
    	   .append("from " + MeioTranspProprietario.class.getName() + " as mtProp \n")
    	   .append(" left join mtProp.proprietario as prop \n")
    	   .append(" left join mtProp.meioTransporte as mt \n")
    	   .append(" left join prop.pessoa as p \n")
    	   .append(" where mt.idMeioTransporte = :idMT ")
    	   .append(" and mtProp.dtVigenciaInicial <= :dtAtual and \n")
    	   .append(" (mtProp.dtVigenciaFinal >= :dtAtual or mtProp.dtVigenciaFinal is null) \n");
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"idMT","dtAtual"},	new Object[]{idMeioTransporte,JTDateTimeUtils.getDataAtual()});	
    }
    public Integer getRowCountMeioTransporteByProprietario(Long idProprietario, String tpSituacao) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addFrom(new StringBuilder(MeioTranspProprietario.class.getName()).append(" MTP ")
    		.append("INNER JOIN MTP.meioTransporte MT ").toString());
    	
    	hql.addCriteria("MT.tpSituacao","=",tpSituacao);
    	hql.addCriteria("MTP.proprietario.id","=",idProprietario);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
    }
    
    public MeioTranspProprietario findMeioTransporteBetweenDate(Long idMeioTransporte, YearMonthDay data){
    	DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("meioTransporte.idMeioTransporte", idMeioTransporte));
    	dc.add(Restrictions.le("dtVigenciaInicial", data));
    	dc.add(Restrictions.ge("dtVigenciaFinal", data));
    	List<MeioTranspProprietario> result = findByDetachedCriteria(dc);
    	if(!result.isEmpty()){
    		return result.get(0);
    	}
    	return null;
    }
    
    public YearMonthDay findDtVigenciaFinalById(Long idMeioTranspProprietario){
    	
    	StringBuilder hql = new StringBuilder();
    	hql.append("select new Map(")
    	   .append(" mtProp.dtVigenciaFinal as dtVigenciaFinal ) ")    	    	   
    	   .append("from " + MeioTranspProprietario.class.getName() + " as mtProp \n")    	    	   
    	   .append(" where mtProp.idMeioTranspProprietario = :idMT ");
    	List<Map> result =  getAdsmHibernateTemplate().findByNamedParam(hql.toString(),	new String[]{"idMT"},	new Object[]{idMeioTranspProprietario});
    	if(!result.isEmpty()){
    		return (YearMonthDay) result.get(0).get("dtVigenciaFinal");
    	}
    	return null;
    }
    	
    

	public List findNCPendenteTransportado(Long idMeioTransporte) {	
		StringBuilder hql = getQueryNCPendente();
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"idMeioTransporte","tpControleCarga","tpSituacao","tpVinculo"},	
    			new Object[]{idMeioTransporte,"C","A","P"});	
	}
		
	
	private StringBuilder getQueryNCPendente() {
		StringBuilder hql = new StringBuilder();
		hql.append("select distinct new Map(");
		hql.append(" nc.idNotaCredito as idNotaCredito,");
		hql.append(" nc.nrNotaCredito as nrNotaCredito,");    	       	  
		hql.append(" nc.filial.sgFilial as sgFilial )");
		hql.append(" from " + NotaCredito.class.getName() + " as nc ");
		hql.append(" 	join nc.controleCarga as cc ");
		hql.append(" 	left join cc.meioTransporteByIdTransportado as mt ");
		hql.append(" where mt.idMeioTransporte = :idMeioTransporte ");
		hql.append(" 	and cc.tpControleCarga = :tpControleCarga ");
		hql.append(" 	and mt.tpSituacao = :tpSituacao ");
		hql.append(" 	and mt.tpVinculo !=  :tpVinculo ");
		hql.append(" 	and nc.dhEmissao is null ");
		hql.append(" ");
		return hql;
	}
	
	
	public List findNCSemVinculoTransportado(Long idMeioTransporte) {	
		StringBuilder hql = getQueryNCSemVinculo();
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"idMeioTransporte","tpControleCarga","tpSituacao","tpVinculo","tpSituacaoProp","tpProprietario"},	
    			new Object[]{idMeioTransporte,"C","A","P","A","P"});	
	}
	

	private StringBuilder getQueryNCSemVinculo() {
		StringBuilder hql = new StringBuilder();
		hql.append("select distinct new Map(");		    	       	 
		hql.append(" pe.nrIdentificacao as nrIdentificacao,");
		hql.append(" pe.nmPessoa as nmPessoa,");
		hql.append(" nc.idNotaCredito as idNotaCredito,");
		hql.append(" nc.filial.sgFilial as sgFilial )");
		hql.append(" from " + NotaCredito.class.getName() + " as nc ");
		hql.append(" 	join nc.controleCarga as cc ");
		hql.append(" 	left join cc.meioTransporteByIdTransportado as mt ");
		hql.append("	left join cc.proprietario as prop ");
		hql.append("	left join prop.pessoa as pe ");
		hql.append(" where mt.idMeioTransporte = :idMeioTransporte ");
		hql.append(" 	and cc.tpControleCarga = :tpControleCarga ");
		hql.append(" 	and mt.tpSituacao = :tpSituacao ");
		hql.append(" 	and mt.tpVinculo !=  :tpVinculo ");		
		hql.append(" 	and prop.tpSituacao = :tpSituacaoProp ");
		hql.append(" 	and prop.tpProprietario !=  :tpProprietario ");
		hql.append(" 	and nc.reciboFreteCarreteiro is null ");
		hql.append(" ");
		return hql;
	
		
	}

	public List findManifestoEntregaNormalParceiraPendente(List<Long> ids) {
		StringBuilder hql = getQueryFindManifestoEntregaNormalPendente();
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"ids","tpManifesto","tpManifestoEntrega"},	
    			new Object[]{ids,"E",new Object[]{"EN","EP"}});	
	}
	
	private StringBuilder getQueryFindManifestoEntregaNormalPendente() {
		StringBuilder hql = new StringBuilder();		
		hql.append("select distinct new Map(ME.idManifestoEntrega as idManifestoEntrega," );		
		hql.append(" F.sgFilial as sgFilial," );		
		hql.append(" ME.nrManifestoEntrega as nrManifesto) ");
		hql.append(" from ");
		hql.append(ManifestoEntrega.class.getName()).append(" ME ");
		hql.append(" inner join ME.manifesto as MANIF ");
		hql.append(" inner join ME.filial as F ");		
		hql.append(" left join MANIF.controleCarga as CC ");		
		hql.append(" left join CC.notaCredito as NC ");
		hql.append("  where NC.idNotaCredito in (:ids)  ");
		hql.append(" 	 and MANIF.tpManifesto = :tpManifesto  ");
		hql.append(" 	 and MANIF.tpManifestoEntrega in (:tpManifestoEntrega)  ");
		hql.append(" 	 and ME.dhFechamento is null  ");	
		return hql;
	}

	public Integer getRowCountMeioTransporteByProprietarioVigencia(Long idProprietario) {
		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(MeioTranspProprietario.class.getName()).append(" AS MTP");
		hqlFrom.append(" INNER JOIN MTP.meioTransporte AS mt");

		
		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addFrom(hqlFrom.toString());
    	 
    	
    	hql.addCriteria("MTP.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
    	hql.addCriteria("MTP.dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
    	hql.addCriteria("MTP.proprietario.id","=",idProprietario);
    	hql.addCriteria("mt.tpSituacao", "=" ,"A");
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}
}