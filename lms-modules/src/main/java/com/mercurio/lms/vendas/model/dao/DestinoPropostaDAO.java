package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * @spring.bean 
 */
public class DestinoPropostaDAO extends BaseCrudDao<DestinoProposta, Long> {
	private static final String NM_PARCELA_FRETE_QUILO = "pt_BR»Frete Quilo¦";
	
	protected final Class getPersistentClass() {
		return DestinoProposta.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Boolean hasDestinoPropostaByIdProposta(Long idProposta) {
		StringBuilder hql = new StringBuilder();
		hql.append("select 1 ");
		hql.append("from ");
		hql.append(getPersistentClass().getName()).append(" dp ");
		hql.append("where ");
		hql.append("dp.proposta.id = ?");
		
		List param = new ArrayList();
		param.add(idProposta);

		List destinos = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
		
		return !destinos.isEmpty();
	}
	
	public List<DestinoProposta> findDestinosPropostaByIdSimulacao(Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("tipoLocalizacaoMunicipio", FetchMode.JOIN);
		dc.setFetchMode("grupoRegiao", FetchMode.JOIN);
		dc.createAlias("unidadeFederativa", "u");
		dc.createAlias("proposta", "p");
		dc.add(Restrictions.eq("p.simulacao.id", idSimulacao));
		dc.addOrder(Order.asc("u.sgUnidadeFederativa"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public void removeByIdSimulacao(Long idSimulacao) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" DELETE FROM ").append(getPersistentClass().getName());
    	hql.append(" WHERE proposta.id IN ( ");
    	hql.append("  	SELECT p.id ");
    	hql.append("   	FROM ").append(Proposta.class.getName()).append(" p ");
    	hql.append("    WHERE p.simulacao.id = :id) ");
    	getAdsmHibernateTemplate().removeById(hql.toString(), idSimulacao);
	}

    public List findDestinosPropostaAereoByIdProposta(Map criteria) {
        boolean hasProposta = criteria.containsKey("idProposta");
        boolean hasProdutoEspecifico = criteria.containsKey("idProdutoEspecifico");
        
        return getAdsmHibernateTemplate().findBySqlToMappedResult(getQueryForDestinosAereo(criteria).getSql(), criteria, getConfigQueryForDestinosAereos(hasProdutoEspecifico,hasProposta));
    }
    
    private ConfigureSqlQuery getConfigQueryForDestinosAereos(final boolean hasProdutoEspecifico, final boolean hasProposta) {

        return new ConfigureSqlQuery() {
            
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("DS_DESTINO", Hibernate.STRING);
                sqlQuery.addScalar("ID_UNIDADE_FEDERATIVA", Hibernate.LONG);
                sqlQuery.addScalar("ID_AEROPORTO", Hibernate.LONG);
                
                sqlQuery.addScalar("VL_ORIGINAL_FRETE_MINIMO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_ORIGINAL_FRETE_PESO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_ORIGINAL_PS_MINIMO_FRETE", Hibernate.BIG_DECIMAL);
                if (hasProdutoEspecifico){
                    sqlQuery.addScalar("VL_ORIGINAL_PD_ESPECIF", Hibernate.BIG_DECIMAL);
                }
                sqlQuery.addScalar("ID_ROTA_PRECO", Hibernate.LONG);
                
                
                sqlQuery.addScalar("VL_ORIGINAL_ADVALOREM1", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_ORIGINAL_ADVALOREM2", Hibernate.BIG_DECIMAL);

                sqlQuery.addScalar("VL_ORIGINAL_TXCOL_URB_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXCOL_URB_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXCOL_URB_CONV", Hibernate.BIG_DECIMAL);
                
                sqlQuery.addScalar("VL_ORIGINAL_TXCOL_URB_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXCOL_URB_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXCOL_URB_EME", Hibernate.BIG_DECIMAL);
                
                sqlQuery.addScalar("VL_ORIGINAL_TXENT_URB_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXENT_URB_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXENT_URB_CONV", Hibernate.BIG_DECIMAL);
                
                sqlQuery.addScalar("VL_ORIGINAL_TXENT_URB_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXENT_URB_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXENT_URB_EME", Hibernate.BIG_DECIMAL);
                
                sqlQuery.addScalar("VL_ORIGINAL_TXCOL_INT_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXCOL_INT_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXCOL_INT_CONV", Hibernate.BIG_DECIMAL);
                
                sqlQuery.addScalar("VL_ORIGINAL_TXCOL_INT_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXCOL_INT_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXCOL_INT_EME", Hibernate.BIG_DECIMAL);

                sqlQuery.addScalar("VL_ORIGINAL_TXENT_INT_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXENT_INT_CONV", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXENT_INT_CONV", Hibernate.BIG_DECIMAL);

                sqlQuery.addScalar("VL_ORIGINAL_TXENT_INT_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TAX_ORIG_TXENT_INT_EME", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_EXC_ORIG_TXENT_INT_EME", Hibernate.BIG_DECIMAL);
                
                
                if (hasProposta){
                    sqlQuery.addScalar("TP_INDICADOR_FRETE_MINIMO", Hibernate.STRING);
                    sqlQuery.addScalar("VL_FRETE_MINIMO", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDICADOR_FRETE_PESO", Hibernate.STRING);
                    sqlQuery.addScalar("VL_FRETE_PESO", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDICADOR_PD_ESPECIF", Hibernate.STRING);
                    sqlQuery.addScalar("VL_PD_ESPECIF", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDICADOR_ADVALOREM", Hibernate.STRING);
                    sqlQuery.addScalar("VL_ADVALOREM", Hibernate.BIG_DECIMAL);

                    sqlQuery.addScalar("TP_INDICADOR_ADVALOREM2", Hibernate.STRING);
                    sqlQuery.addScalar("VL_ADVALOREM_2", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("PS_MINIMO_FRETE_PESO", Hibernate.BIG_DECIMAL);
                    
                    //TAXA COLETA
                    sqlQuery.addScalar("TP_INDIC_TX_COL_URB_CONV", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_COL_URB_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_COL_URB_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_COL_URB_CONV", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_COL_INT_CONV", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_COL_INT_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_COL_INT_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_COL_INT_CONV", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_COL_URB_EME", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_COL_URB_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_COL_URB_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_COL_URB_EME", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_COL_INT_EME", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_COL_INT_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_COL_INT_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_COL_INT_EME", Hibernate.BIG_DECIMAL);
                    
                    //TAXA ENTREGA
                    sqlQuery.addScalar("TP_INDIC_TX_ENT_URB_CONV", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_ENT_URB_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_ENT_URB_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_ENT_URB_CONV", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_ENT_INT_CONV", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_ENT_INT_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_ENT_INT_CONV", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_ENT_INT_CONV", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_ENT_URB_EME", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_ENT_URB_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_ENT_URB_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_ENT_URB_EME", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("TP_INDIC_TX_ENT_INT_EME", Hibernate.STRING);
                    sqlQuery.addScalar("VL_TX_ENT_INT_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("PS_MIN_TX_ENT_INT_EME", Hibernate.BIG_DECIMAL);
                    sqlQuery.addScalar("VL_EXCED_TX_ENT_INT_EME", Hibernate.BIG_DECIMAL);
                    
                    sqlQuery.addScalar("ID_DESTINO_PROPOSTA", Hibernate.LONG);
                    sqlQuery.addScalar("ID_PROPOSTA", Hibernate.LONG);
                    
                }
                
            }
        };
    }

    private SqlTemplate getQueryForDestinosAereo(Map criteria) {
        Long idProdutoEspecfico = (Long)criteria.get("idProdutoEspecifico");
        
        boolean hasProposta = criteria.get("idProposta") != null; 
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("a.sg_aeroporto","DS_DESTINO");
        sql.addProjection("m.id_unidade_federativa", "ID_UNIDADE_FEDERATIVA");
        sql.addProjection("a.id_aeroporto", "ID_AEROPORTO");
        sql.addProjection("main_rp.ID_ROTA_PRECO", "ID_ROTA_PRECO");
        
        sql.addProjection(getSQLPrecoFrete("IDTarifaMinima"), "VL_ORIGINAL_FRETE_MINIMO");
        sql.addProjection(getSQLPrecoFrete("IDFreteQuilo"), "VL_ORIGINAL_FRETE_PESO");
        sql.addProjection(getSQLPrecoFretePsMinimo("IDFreteQuilo"), "VL_ORIGINAL_PS_MINIMO_FRETE");
        
        
        if (idProdutoEspecfico != null){
            sql.addProjection(getSQLFaixaProgressivaProdutoEspecifico(idProdutoEspecfico), "VL_ORIGINAL_PD_ESPECIF");
        }
        sql.addProjection(getSQLGeneralidade("IDAdvalorem1"), "VL_ORIGINAL_ADVALOREM1");
        sql.addProjection(getSQLGeneralidade("IDAdvalorem2"), "VL_ORIGINAL_ADVALOREM2");
        
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_CONV.vl_taxa", "VL_ORIGINAL_TXCOL_URB_CONV");
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_CONV.ps_taxado", "PS_TAX_ORIG_TXCOL_URB_CONV");
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_CONV.vl_excedente", "VL_EXC_ORIG_TXCOL_URB_CONV");
  
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_EME.vl_taxa", "VL_ORIGINAL_TXCOL_URB_EME");
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_EME.ps_taxado", "PS_TAX_ORIG_TXCOL_URB_EME");
        sql.addProjection("VL_ORIGINAIS_TXCOL_URB_EME.vl_excedente", "VL_EXC_ORIG_TXCOL_URB_EME");

        sql.addProjection("VL_ORIGINAIS_TXENT_URB_CONV.vl_taxa", "VL_ORIGINAL_TXENT_URB_CONV");
        sql.addProjection("VL_ORIGINAIS_TXENT_URB_CONV.ps_taxado", "PS_TAX_ORIG_TXENT_URB_CONV");
        sql.addProjection("VL_ORIGINAIS_TXENT_URB_CONV.vl_excedente", "VL_EXC_ORIG_TXENT_URB_CONV");

        sql.addProjection("VL_ORIGINAIS_TXENT_URB_EME.vl_taxa", "VL_ORIGINAL_TXENT_URB_EME");
        sql.addProjection("VL_ORIGINAIS_TXENT_URB_EME.ps_taxado", "PS_TAX_ORIG_TXENT_URB_EME");
        sql.addProjection("VL_ORIGINAIS_TXENT_URB_EME.vl_excedente", "VL_EXC_ORIG_TXENT_URB_EME");
        
        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_CONV.vl_taxa", "VL_ORIGINAL_TXCOL_INT_CONV");
        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_CONV.ps_taxado", "PS_TAX_ORIG_TXCOL_INT_CONV");
        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_CONV.vl_excedente", "VL_EXC_ORIG_TXCOL_INT_CONV");

        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_EME.vl_taxa", "VL_ORIGINAL_TXCOL_INT_EME");
        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_EME.ps_taxado", "PS_TAX_ORIG_TXCOL_INT_EME");
        sql.addProjection("VL_ORIGINAIS_TXCOL_INT_EME.vl_excedente", "VL_EXC_ORIG_TXCOL_INT_EME");

        sql.addProjection("VL_ORIGINAIS_TXENT_INT_CONV.vl_taxa", "VL_ORIGINAL_TXENT_INT_CONV");
        sql.addProjection("VL_ORIGINAIS_TXENT_INT_CONV.ps_taxado", "PS_TAX_ORIG_TXENT_INT_CONV");
        sql.addProjection("VL_ORIGINAIS_TXENT_INT_CONV.vl_excedente", "VL_EXC_ORIG_TXENT_INT_CONV");
        
        sql.addProjection("VL_ORIGINAIS_TXENT_INT_EME.vl_taxa", "VL_ORIGINAL_TXENT_INT_EME");
        sql.addProjection("VL_ORIGINAIS_TXENT_INT_EME.ps_taxado", "PS_TAX_ORIG_TXENT_INT_EME");
        sql.addProjection("VL_ORIGINAIS_TXENT_INT_EME.vl_excedente", "VL_EXC_ORIG_TXENT_INT_EME");
        
        sql.addFrom("aeroporto","a");
        sql.addFrom("rota_preco","main_rp");
        
        sql.addFrom("pessoa","p");
        sql.addFrom("endereco_pessoa", "ep");
        sql.addFrom("municipio","m");

        sql.addCustomCriteria("a.id_aeroporto = p.id_pessoa");
        sql.addCustomCriteria("p.id_endereco_pessoa = ep.id_endereco_pessoa");
        sql.addCustomCriteria("ep.id_municipio = m.id_municipio");
        
        sql.addCustomCriteria("main_rp.id_aeroporto_destino = a.id_aeroporto");
        sql.addCustomCriteria("main_rp.id_rota_preco in ("+getSqlFiltroRotaPrecoDestinosAereo()+")");
        
        sql.addFrom(getSQLTaxa("IDColetaUrbanaConvencional"), "VL_ORIGINAIS_TXCOL_URB_CONV");
        sql.addFrom(getSQLTaxa("IDColetaUrbanaEmergencia"), "VL_ORIGINAIS_TXCOL_URB_EME");
        sql.addFrom(getSQLTaxa("IDEntregaUrbanaConvencional"), "VL_ORIGINAIS_TXENT_URB_CONV");
        sql.addFrom(getSQLTaxa("IDEntregaUrbanaEmergencia"), "VL_ORIGINAIS_TXENT_URB_EME");
        sql.addFrom(getSQLTaxa("IDColetaInteriorConvencional"), "VL_ORIGINAIS_TXCOL_INT_CONV");
        sql.addFrom(getSQLTaxa("IDColetaInteriorEmergencia"), "VL_ORIGINAIS_TXCOL_INT_EME");
        sql.addFrom(getSQLTaxa("IDEntregaInteriorConvencional"), "VL_ORIGINAIS_TXENT_INT_CONV");
        sql.addFrom(getSQLTaxa("IDEntregaInteriorEmergencia"), "VL_ORIGINAIS_TXENT_INT_EME");
        
        
        if (hasProposta){
            SqlTemplate subqueryDestinos = new SqlTemplate();
            subqueryDestinos.addProjection("*");
            subqueryDestinos.addFrom("destino_proposta");
            subqueryDestinos.addCustomCriteria("id_proposta = :idProposta");
            
            sql.addProjection("dp.*");
            sql.addFrom(subqueryDestinos,"dp");
            
            if(MapUtilsPlus.getBoolean(criteria, "isGenerate")){
            sql.addCustomCriteria("a.id_aeroporto = dp.id_aeroporto(+) ");
            } else {
            	sql.addCustomCriteria("a.id_aeroporto = dp.id_aeroporto ");
            }
        }
        
        sql.addOrderBy("a.sg_aeroporto");
        return sql;
    }

    /**
     * Busca registros de rota_preco baseado nos parametros :idTabelaPreco e :idAeroportoOrigem
     * 
     * @return String sql
     */
    private String getSqlFiltroRotaPrecoDestinosAereo() {
        StringBuilder builder = new StringBuilder();
        builder.append("select rp.id_rota_preco ");
        builder.append("from ");
        builder.append(" preco_frete pf ");
        builder.append(" ,parcela_preco pp");
        builder.append(" ,tabela_preco_parcela tpp");
        builder.append(" ,rota_preco rp");
        builder.append(" where ");
        builder.append("  pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
        builder.append("  and tpp.id_parcela_preco = pp.id_parcela_preco");
        builder.append("  and tpp.id_tabela_preco = :idTabelaPreco");
        builder.append("  and pf.id_rota_preco = rp.id_rota_preco");
        builder.append("  and pp.TP_PRECIFICACAO in ('P')");
        builder.append("  and rp.ID_AEROPORTO_DESTINO is not null");
        builder.append("  and rp.ID_AEROPORTO_ORIGEM = :idAeroportoOrigem");
        builder.append("  and rp.ID_UF_ORIGEM is not null");
        builder.append("  and rp.ID_UF_DESTINO is not null");
        builder.append("  and rp.ID_MUNICIPIO_ORIGEM  is null");
        builder.append("  and rp.ID_MUNICIPIO_DESTINO is null");
    
        return builder.toString();
    }

    private SqlTemplate getSQLGeneralidade(String cdParcelaPreco) {
        
        SqlTemplate template = new SqlTemplate();
        
        template.addProjection("g.vl_generalidade");
        
        template.addFrom("generalidade","g");
        template.addFrom("parcela_preco","pp");
        template.addFrom("tabela_preco_parcela","tpp");
        
        template.addCustomCriteria("g.id_generalidade = tpp.id_tabela_preco_parcela");
        template.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
        
        template.addCustomCriteria("pp.cd_parcela_preco = '"+cdParcelaPreco+"'");
        template.addCustomCriteria("tpp.id_tabela_preco = :idTabelaPreco");

        return template;
    }
    
    private SqlTemplate getSQLTaxa(String cdParcelaPreco) {
        
        SqlTemplate template = new SqlTemplate();
        
        template.addProjection("vt.vl_taxa");
        template.addProjection("vt.ps_taxado");
        template.addProjection("vt.vl_excedente");
        
        template.addFrom("valor_taxa","vt");
        template.addFrom("parcela_preco","pp");
        template.addFrom("tabela_preco_parcela","tpp");
        
        template.addCustomCriteria("vt.id_valor_taxa = tpp.id_tabela_preco_parcela");
        template.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
        
        template.addCustomCriteria("pp.cd_parcela_preco = '"+cdParcelaPreco+"'");
        template.addCustomCriteria("tpp.id_tabela_preco = :idTabelaPreco");

        return template;
    }

    private SqlTemplate getSQLFaixaProgressivaProdutoEspecifico(Long idProdutoEspecifico) {
        SqlTemplate template = new SqlTemplate();

        if (idProdutoEspecifico != null){
            template.addProjection("vfp.vl_fixo ");
            
            template.addFrom("faixa_progressiva","fp");
            template.addFrom("valor_faixa_progressiva","vfp");
            template.addFrom("tabela_preco_parcela","tpp");
            
            template.addCustomCriteria("tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela");
            template.addCustomCriteria("vfp.id_faixa_progressiva = fp.id_faixa_progressiva");
            
            //main_rp eh a rota_preco que vem da query principal 
            template.addCustomCriteria("vfp.id_rota_preco = main_rp.id_rota_preco");
            
            template.addCustomCriteria("fp.id_produto_especifico = :idProdutoEspecifico");
            template.addCustomCriteria("tpp.id_tabela_preco = :idTabelaPreco");

        }else{
            //para evitar uma query a mais caso a proposta não possua um produto especifico
            template.addProjection("null");
        }
        
        
        return template;
    }

    private SqlTemplate getSQLPrecoFrete(String cdParcelaPreco) {
        
        SqlTemplate template = new SqlTemplate();
        
        template.addProjection("pf.vl_preco_frete");
        
        template.addFrom("preco_frete","pf");
        template.addFrom("parcela_preco","pp");
        template.addFrom("tabela_preco_parcela","tpp");
        
        template.addCustomCriteria("pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
        template.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
        
        //main_rp eh a rota_preco que vem da query principal
        template.addCustomCriteria("pf.id_rota_preco = main_rp.id_rota_preco");
        
        template.addCustomCriteria("pp.cd_parcela_preco = '"+cdParcelaPreco+"'");
        template.addCustomCriteria("tpp.id_tabela_preco = :idTabelaPreco");

        return template;
    }   
    
    private SqlTemplate getSQLPrecoFretePsMinimo(String cdParcelaPreco) {
        
        SqlTemplate template = new SqlTemplate();
        
        template.addProjection("pf.ps_minimo");
        
        template.addFrom("preco_frete","pf");
        template.addFrom("parcela_preco","pp");
        template.addFrom("tabela_preco_parcela","tpp");
        
        template.addCustomCriteria("pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
        template.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
        
        //main_rp eh a rota_preco que vem da query principal
        template.addCustomCriteria("pf.id_rota_preco = main_rp.id_rota_preco");
        
        template.addCustomCriteria("pp.cd_parcela_preco = '"+cdParcelaPreco+"'");
        template.addCustomCriteria("tpp.id_tabela_preco = :idTabelaPreco");

        return template;
    }
    
    
    public List<Map<String, Object>> findDestinosPropostaConvencionalAereo(Map criteria){
        final boolean hasProposta = criteria.containsKey("idProposta");
        final String tpRota = (String)criteria.get("tpRota");
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("MAIN_RP.ID_ROTA_PRECO","ID_ROTA_PRECO");
        sql.addProjection("a.sg_aeroporto ||' - '||p.nm_pessoa","DS_DESTINO");
        sql.addProjection("a.id_aeroporto","ID_AEROPORTO");
        sql.addProjection("UF.id_unidade_federativa","ID_UNIDADE_FEDERATIVA");
        if (hasProposta){
            sql.addProjection("DP.ID_DESTINO_PROPOSTA", "ID_DESTINO_PROPOSTA");
        }
        sql.addProjection("r.id_regiao_geografica", "ID_REGIAO_GEOGRAFICA");
        sql.addProjection("VI18N(r.ds_regiao_geografica_i)", "DS_REGIAO_GEOGRAFICA");
        
        
        sql.addFrom("ROTA_PRECO", "MAIN_RP");
        sql.addFrom("AEROPORTO", "A");
        sql.addFrom("PESSOA", "P");
        sql.addFrom("ENDERECO_PESSOA", "EP");
        sql.addFrom("MUNICIPIO", "M");
        sql.addFrom("UNIDADE_FEDERATIVA", "UF");
        sql.addFrom("REGIAO_GEOGRAFICA", "R");
        if (hasProposta){
            String campoAeroportoJoin = null;
            String campoAeroportoOrigem = null;
            if ("D".equals(tpRota)){
                campoAeroportoJoin = "id_aeroporto_destino";
                campoAeroportoOrigem = "id_aeroporto_origem";
            }else{
                campoAeroportoJoin = "id_aeroporto_origem";
                campoAeroportoOrigem = "id_aeroporto_destino";
            }
            
            
            String rotaPrecoSubquery = "SELECT SUB_DP.ID_DESTINO_PROPOSTA, SUB_RP.ID_AEROPORTO_DESTINO, SUB_RP.ID_AEROPORTO_ORIGEM "
                    + "FROM DESTINO_PROPOSTA SUB_DP, "
                    + "ROTA_PRECO SUB_RP "
                    + "WHERE SUB_RP.ID_ROTA_PRECO = SUB_DP.ID_ROTA_PRECO "
                    + "AND SUB_RP."+campoAeroportoJoin+ " <> :idAeroportoOrigem "
                    + "AND SUB_RP."+campoAeroportoOrigem+ " = :idAeroportoOrigem "
                    + "AND ID_PROPOSTA = :idProposta";

            sql.addFrom("("+rotaPrecoSubquery+")", "DP");
            
            
        
			if (MapUtilsPlus.getBoolean(criteria, "isGenerate")) {
				sql.addCustomCriteria("a.id_aeroporto = dp."+campoAeroportoJoin+"(+)");
			} else {
				sql.addCustomCriteria("a.id_aeroporto = dp."+campoAeroportoJoin);
			}
        }
        
        sql.addCustomCriteria("a.id_aeroporto = p.id_pessoa");
        sql.addCustomCriteria("a.tp_situacao = 'A'");
        sql.addCustomCriteria("p.id_endereco_pessoa = ep.id_endereco_pessoa");
        sql.addCustomCriteria("ep.id_municipio = m.id_municipio");
        sql.addCustomCriteria("a.id_aeroporto = p.id_pessoa");
        sql.addCustomCriteria("uf.ID_REGIAO_GEOGRAFICA  = R.ID_REGIAO_GEOGRAFICA");
        
        if ("D".equals(tpRota)){
            sql.addCustomCriteria("main_rp.id_aeroporto_destino = a.id_aeroporto");
            sql.addCustomCriteria("main_rp.ID_AEROPORTO_DESTINO IS NOT NULL");
            sql.addCustomCriteria("main_rp.ID_AEROPORTO_ORIGEM   = :idAeroportoOrigem");
            sql.addCustomCriteria("main_rp.ID_UF_ORIGEM         IS NOT NULL");
            sql.addCustomCriteria("main_rp.ID_UF_DESTINO         = UF.ID_UNIDADE_FEDERATIVA");
        }else if ("O".equals(tpRota)){
            sql.addCustomCriteria("main_rp.id_aeroporto_ORIGEM = a.id_aeroporto");
            sql.addCustomCriteria("main_rp.ID_AEROPORTO_DESTINO = :idAeroportoOrigem");
            sql.addCustomCriteria("main_rp.ID_AEROPORTO_ORIGEM   IS NOT NULL");
            sql.addCustomCriteria("main_rp.ID_UF_ORIGEM         = UF.ID_UNIDADE_FEDERATIVA");
            sql.addCustomCriteria("main_rp.ID_UF_DESTINO        IS NOT NULL ");
        }
        
        
        sql.addCustomCriteria("main_rp.ID_MUNICIPIO_ORIGEM  IS NULL");
        sql.addCustomCriteria("main_rp.ID_MUNICIPIO_DESTINO IS NULL");
        
        sql.addOrderBy("DS_DESTINO");
        
        String sqlPrecoFrete = getSqlFiltroPrecoFreteDestinosAereoConvencional();
        String sqlFaixaProgressiva = getSqlFiltroFaixaProgressivaDestinosAereoConvencional();
        sql.addCustomCriteria("(exists ("+sqlPrecoFrete+") or exists("+sqlFaixaProgressiva+"))");
        
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.getSql(), criteria, new ConfigureSqlQuery(){
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("DS_DESTINO",Hibernate.STRING);
                query.addScalar("ID_ROTA_PRECO",Hibernate.LONG);
                query.addScalar("ID_AEROPORTO",Hibernate.LONG);
                query.addScalar("ID_UNIDADE_FEDERATIVA",Hibernate.LONG);
                
                if (hasProposta){
                    query.addScalar("ID_DESTINO_PROPOSTA",Hibernate.LONG);
                }
                query.addScalar("ID_REGIAO_GEOGRAFICA",Hibernate.LONG);
                query.addScalar("DS_REGIAO_GEOGRAFICA",Hibernate.STRING);
            }
        });
    }
    
    
    /**
     * Busca registros de preco_frete para a tabela de preço
     * 
     * @return String sql
     */
    private String getSqlFiltroPrecoFreteDestinosAereoConvencional() {
        StringBuilder builder = new StringBuilder();
        builder.append("select pf.id_rota_preco ");
        builder.append("from ");
        builder.append(" preco_frete pf ");
        builder.append(" ,parcela_preco pp");
        builder.append(" ,tabela_preco_parcela tpp");
        builder.append(" where ");
        builder.append("  pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
        builder.append("  and tpp.id_parcela_preco = pp.id_parcela_preco");
        builder.append("  and tpp.id_tabela_preco = :idTabelaPreco");
        builder.append("  and pf.id_rota_preco = main_rp.id_rota_preco");
        builder.append("  and pp.TP_PRECIFICACAO in ('P')");
        return builder.toString();
    }
    
    /**
     * Busca registros de faixa_progressiva para a tabela de preço
     * 
     * @return String sql
     */
    private String getSqlFiltroFaixaProgressivaDestinosAereoConvencional() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("select distinct vfp.id_rota_preco"); 
        builder.append(" from"); 
        builder.append(" tabela_preco_parcela tpp,");
        builder.append(" faixa_progressiva fp,");
        builder.append(" valor_faixa_progressiva vfp");
        builder.append(" where"); 
        builder.append(" tpp.id_tabela_preco_parcela = fp.ID_TABELA_PRECO_PARCELA");
        builder.append(" and vfp.id_faixa_progressiva = fp.id_faixa_progressiva");
        builder.append(" and vfp.id_rota_preco = main_rp.id_rota_preco");
        builder.append(" and tpp.id_tabela_preco = :idTabelaPreco");
        
        return builder.toString();
    }
    
    
    public List<Map<String,Object>> findParcelasDestinoConvencionalAereo(Map criteria){
        /**
         * idRotaPreco
         * idTabelaPreco
         */
        String tpGeracaoProposta = (String) criteria.get("tpGeracaoProposta");
    	
        StringBuilder query = new StringBuilder();
        query.append(getSqlTarifaMinimaConvencionalAereo())
            .append("\n union \n")
            .append(getSqlGeneralidadesConvencionalAereo())
            .append("\n union \n")
            .append(getSqlTaxasConvencionalAereo());
        
        if (ConstantesVendas.TP_PROPOSTA_CONVENCIONAL.equals(tpGeracaoProposta)){
            query.append("\n union \n")
            .append(getSqlFaixaProgressivaFretePesoConvencionalAereo())
            .append("\n union \n")
            .append(getSqlFaixaProgressivaProdutoEspecificoConvencionalAereo(false))
            .append(" order by vl_faixa_progressiva, nm_parcela");
            
        } else {
            
            if (ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(tpGeracaoProposta)
            		|| ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG.equals(tpGeracaoProposta) ){
            	 query.append("\n union \n").append(getSqlParcelaFreteQuiloMinimoExcedenteMinimoKg());

            } else {
            	query.append("\n union \n").append(getSqlPsMinimoFreteQuiloConvencionalAereo());
            }
            
            if (criteria.containsKey("idProdutoEspecifico")){
                query.append("\n union \n")
                .append(getSqlFaixaProgressivaProdutoEspecificoConvencionalAereo(true));
            }
        }
        
         return getAdsmHibernateTemplate().findBySqlToMappedResult(query.toString(), criteria, new ConfigureSqlQuery(){
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("NM_PARCELA",Hibernate.STRING);
                query.addScalar("VL_ORIGINAL",Hibernate.BIG_DECIMAL);
                query.addScalar("VL_EXCEDENTE",Hibernate.BIG_DECIMAL);
                query.addScalar("PS_MINIMO",Hibernate.BIG_DECIMAL);
                query.addScalar("CD_PARCELA_PRECO",Hibernate.STRING);
                query.addScalar("ID_VALOR_FAIXA_PROGRESSIVA",Hibernate.LONG);
                query.addScalar("VL_FAIXA_PROGRESSIVA",Hibernate.BIG_DECIMAL);
                query.addScalar("ID_PRODUTO_ESPECIFICO",Hibernate.LONG);
                
            }
        });
    }

    private String getSqlParcelaFreteQuiloMinimoExcedenteMinimoKg(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM ( \n");
        sql.append("SELECT '").append(NM_PARCELA_FRETE_QUILO).append("' AS NM_PARCELA, \n");
        sql.append("  vfp.VL_FIXO                AS VL_ORIGINAL, \n");
        sql.append("  NULL                       AS VL_EXCEDENTE, \n");
        sql.append("  TRUNC ((SELECT pf.vl_preco_frete \n");
        sql.append("  	FROM preco_frete pf, \n");
        sql.append("    parcela_preco pp, \n");
        sql.append("    tabela_preco_parcela tpp \n");
        sql.append("  WHERE pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
        sql.append("  AND tpp.id_parcela_preco         = pp.id_parcela_preco \n");
        sql.append("  AND pf.id_rota_preco             = :idRotaPreco \n");
        sql.append("  AND pp.cd_parcela_preco          = 'IDTarifaMinima' \n");
        sql.append("  AND tpp.id_tabela_preco          = :idTabelaPreco \n");
        sql.append("  ) / vfp.VL_FIXO, 2)            AS PS_MINIMO, \n");
        sql.append("  'IDFreteQuilo'           AS CD_PARCELA_PRECO, \n");
        sql.append("  vfp.id_valor_faixa_progressiva AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  fp.VL_faixa_progressiva        AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                           AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM valor_faixa_progressiva vfp, \n");
        sql.append("  faixa_progressiva fp, \n");
        sql.append("  tabela_preco_parcela tpp, \n");
        sql.append("  parcela_preco pp \n");
        sql.append("WHERE vfp.id_rota_preco         = :idRotaPreco \n");
        sql.append("AND fp.ID_FAIXA_PROGRESSIVA     = vfp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("AND fp.id_produto_especifico   IS NULL \n");
        sql.append("AND pp.cd_parcela_preco         = 'IDFretePeso' \n");
        sql.append("AND tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela \n");
        sql.append("AND pp.id_parcela_preco         = tpp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");
        sql.append("AND rownum                     <= 1 \n");
        sql.append("ORDER BY fp.VL_faixa_progressiva \n");
        sql.append(") \n");
        
        return sql.toString();
    }
    
    private String getSqlTarifaMinimaConvencionalAereo() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  pp.NM_PARCELA_PRECO_I AS NM_PARCELA, \n");
        sql.append("  pf.vl_preco_frete     AS VL_ORIGINAL, \n");
        sql.append("  NULL                  AS VL_EXCEDENTE , \n");
        sql.append("  NULL                  AS PS_MINIMO , \n");
        sql.append("  pp.CD_PARCELA_PRECO, \n");
        sql.append("  NULL                  AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  preco_frete pf, \n");
        sql.append("  parcela_preco pp, \n");
        sql.append("  tabela_preco_parcela tpp \n");
        sql.append("WHERE \n");
        sql.append("  pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
        sql.append("AND tpp.id_parcela_preco     = pp.id_parcela_preco \n");
        sql.append("AND pf.id_rota_preco         = :idRotaPreco \n");
        sql.append("AND pp.cd_parcela_preco      = 'IDTarifaMinima' \n");
        sql.append("AND tpp.id_tabela_preco      = :idTabelaPreco \n");
        
        return sql.toString();
    }
    
    private String getSqlPsMinimoFreteQuiloConvencionalAereo() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  pp.NM_PARCELA_PRECO_I AS NM_PARCELA, \n");
        sql.append("  pf.vl_preco_frete          AS VL_ORIGINAL, \n");
        sql.append("  NULL                  AS VL_EXCEDENTE , \n");
        sql.append("  pf.ps_minimo               AS PS_MINIMO , \n");
        sql.append("  pp.CD_PARCELA_PRECO, \n");
        sql.append("  NULL                  AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  preco_frete pf, \n");
        sql.append("  parcela_preco pp, \n");
        sql.append("  tabela_preco_parcela tpp \n");
        sql.append("WHERE \n");
        sql.append("  pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
        sql.append("AND tpp.id_parcela_preco     = pp.id_parcela_preco \n");
        sql.append("AND pf.id_rota_preco         = :idRotaPreco \n");
        sql.append("AND pp.cd_parcela_preco      = 'IDFreteQuilo' \n");
        sql.append("AND tpp.id_tabela_preco      = :idTabelaPreco \n");
        
        return sql.toString();
    }
    
    private String getSqlTaxasConvencionalAereo(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  pp.NM_PARCELA_PRECO_I     AS NM_PARCELA, \n");
        sql.append("  vt.vl_taxa                AS VL_ORIGINAL, \n");
        sql.append("  vt.VL_EXCEDENTE           AS VL_EXCEDENTE, \n");
        sql.append("  vt.PS_TAXADO              AS PS_MINIMO, \n");
        sql.append("  pp.CD_PARCELA_PRECO       AS CD_PARCELA_PRECO, \n");
        sql.append("  NULL                      AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                      AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                      AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  valor_taxa vt, \n");
        sql.append("  parcela_preco pp, \n");
        sql.append("  tabela_preco_parcela tpp \n");
        sql.append("WHERE \n");
        sql.append("  vt.id_valor_taxa       = tpp.id_tabela_preco_parcela \n");
        sql.append("AND tpp.id_parcela_preco = pp.id_parcela_preco \n");
        sql.append("AND pp.cd_parcela_preco IN('IDEntregaInteriorEmergencia', \n");
        sql.append("  'IDEntregaInteriorConvencional','IDColetaInteriorEmergencia', \n");
        sql.append("  'IDColetaInteriorConvencional', 'IDEntregaUrbanaEmergencia', \n");
        sql.append("  'IDEntregaUrbanaConvencional','IDColetaUrbanaEmergencia', \n");
        sql.append("  'IDColetaUrbanaConvencional') \n");
        sql.append("AND tpp.id_tabela_preco = :idTabelaPreco \n");
        
        return sql.toString();
    }
    
    private String getSqlGeneralidadesConvencionalAereo(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  pp.NM_PARCELA_PRECO_I AS NM_PARCELA, \n");
        sql.append("  g.vl_generalidade     AS VL_ORIGINAL, \n");
        sql.append("  NULL                  AS VL_EXCEDENTE, \n");
        sql.append("  NULL                  AS PS_MINIMO, \n");
        sql.append("  pp.cd_parcela_preco   AS CD_PARCELA_PRECO, \n");
        sql.append("  NULL                  AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                  AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  generalidade g, \n");
        sql.append("  parcela_preco pp, \n");
        sql.append("  tabela_preco_parcela tpp \n");
        sql.append("WHERE \n");
        sql.append("  g.id_generalidade      = tpp.id_tabela_preco_parcela \n");
        sql.append("AND tpp.id_parcela_preco = pp.id_parcela_preco \n");
        sql.append("AND pp.cd_parcela_preco IN ('IDAdvalorem1', 'IDAdvalorem2') \n");
        sql.append("AND tpp.id_tabela_preco  = :idTabelaPreco \n");
        
        return sql.toString();
        
    }
    
    private String getSqlFaixaProgressivaFretePesoConvencionalAereo(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  pp.nm_parcela_preco_i          AS NM_PARCELA, \n");
        sql.append("  vfp.VL_FIXO                   AS VL_ORIGINAL, \n");
        sql.append("  NULL                          AS VL_EXCEDENTE, \n");
        sql.append("  NULL                          AS PS_MINIMO, \n");
        sql.append("  pp.cd_parcela_preco           AS CD_PARCELA_PRECO, \n");
        sql.append("  vfp.id_valor_faixa_progressiva  AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  fp.VL_faixa_progressiva       AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                          AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  valor_faixa_progressiva vfp, \n");
        sql.append("  faixa_progressiva fp, \n");
        sql.append("  tabela_preco_parcela tpp, \n");
        sql.append("  parcela_preco pp \n");
        sql.append("WHERE \n");
        sql.append("  vfp.id_rota_preco             = :idRotaPreco \n");
        sql.append("AND fp.ID_FAIXA_PROGRESSIVA     = vfp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("AND fp.id_produto_especifico   IS NULL \n");
        sql.append("AND pp.cd_parcela_preco         = 'IDFretePeso' \n");
        sql.append("AND tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela \n");
        sql.append("AND pp.id_parcela_preco         = tpp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");

        
        return sql.toString();
    }
    
    private String getSqlFaixaProgressivaProdutoEspecificoConvencionalAereo(boolean produtoEspecifico){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT \n");
        sql.append("  'TE  ' \n");
        sql.append("  || TO_CHAR(pe.NR_TARIFA_ESPECIFICA, '000')  AS NM_parcela, \n");
        sql.append("  vfp.VL_FIXO                AS VL_ORIGINAL, \n");
        sql.append("  NULL                       AS VL_EXCEDENTE, \n");
        sql.append("  NULL                       AS PS_MINIMO, \n");
        sql.append("  pp.cd_parcela_preco        AS CD_PARCELA_PRECO, \n");
        sql.append("  vfp.id_valor_faixa_progressiva AS ID_VALOR_FAIXA_PROGRESSIVA, \n");
        sql.append("  NULL                      AS VL_FAIXA_PROGRESSIVA, \n");
        sql.append("  fp.ID_PRODUTO_ESPECIFICO AS ID_PRODUTO_ESPECIFICO \n");
        sql.append("FROM \n");
        sql.append("  valor_faixa_progressiva vfp, \n");
        sql.append("  faixa_progressiva fp, \n");
        sql.append("  tabela_preco_parcela tpp, \n");
        sql.append("  produto_especifico pe, \n");
        sql.append("  parcela_preco pp \n");
        sql.append("WHERE \n");
        sql.append("  vfp.id_rota_preco             = :idRotaPreco \n");
        sql.append("AND fp.ID_FAIXA_PROGRESSIVA     = vfp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("AND fp.id_produto_especifico    = pe.id_produto_especifico \n");
        if (produtoEspecifico){
            sql.append("AND fp.id_produto_especifico    = :idProdutoEspecifico \n");
        }
        sql.append("AND tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela \n");
        sql.append("AND pp.id_parcela_preco         = tpp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco");
        
        return sql.toString();
    }

    public List<DestinoProposta> findDestinosPropostaInvalidos(Long idAeroportoReferencia, Long idProposta) {
        Map<String,Object> criteria = new HashMap<String, Object>();
        criteria.put("idProposta", idProposta);
        criteria.put("idAeroportoReferencia", idAeroportoReferencia);
        
        String query = "select dp "
                + "from DestinoProposta dp "
                + " where dp.proposta.id = :idProposta "
                + " and dp.rotaPreco.aeroportoByIdAeroportoOrigem.id <> :idAeroportoReferencia "
                + " and dp.rotaPreco.aeroportoByIdAeroportoDestino.id <> :idAeroportoReferencia ";
                
        return getAdsmHibernateTemplate().findByNamedParam(query, criteria);
    }
    
}