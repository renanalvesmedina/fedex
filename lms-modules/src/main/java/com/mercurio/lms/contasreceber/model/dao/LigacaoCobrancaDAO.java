package com.mercurio.lms.contasreceber.model.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.ItemLigacao;
import com.mercurio.lms.contasreceber.model.LigacaoCobranca;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LigacaoCobrancaDAO extends BaseCrudDao<LigacaoCobranca, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LigacaoCobranca.class;
    }

	public java.io.Serializable findByIdMap(Long id) {
		
    	SqlTemplate sql = new SqlTemplate();

    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( LC.idLigacaoCobranca as idLigacaoCobranca, ")
    				.append(" LC.dhLigacaoCobranca as dhLigacaoCobranca, ")
    				.append(" LC.dsLigacaoCobranca as dsLigacaoCobranca, ")
    				.append(" CI.idCobrancaInadimplencia as cobrancaInadimplencia_idCobrancaInadimplencia, ")
    				.append(" CI.dsCobrancaInadimplencia as cobrancaInadimplencia_dsCobrancaInadimplencia, ")
    				.append(" TC.idTelefoneContato as telefoneContato_idTelefoneContato, ")
    				.append(" C.nmContato as contato_nmContato, ")
    				.append(" U.idUsuario as usuario_idUsuario, ")
    				.append(" F.nmFuncionario as usuario_nmUsuario,")
    				.append(" F.nrMatricula as usuario_nrMatricula )")
    				.toString()
    			);
    	
    	sql.addFrom(LigacaoCobranca.class.getName(), " LC " +
    			" JOIN LC.cobrancaInadimplencia CI " +
    			" JOIN LC.usuario U " +
    			" JOIN LC.usuario U " +
    			" JOIN LC.usuario U " +
    			" JOIN U.vfuncionario F " +
    			" JOIN LC.telefoneContato TC " +
    			" JOIN TC.contato C "
    			);
    	
    	if(StringUtils.isNotBlank(id.toString())) {
    		sql.addCriteria("LC.idLigacaoCobranca", "=", id);
    	}
    	
    	List lstLigacaoCobranca = AliasToNestedMapResultTransformer.getInstance().transformListResult(getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria()));
    	
    	HashMap item = (HashMap) lstLigacaoCobranca.get(0);
    	
    	List faturas = this.findFaturasByLigacaoCobranca(id); 
    	
    	/** Formata o número da fatura */
		for (Iterator iter = faturas.iterator(); iter.hasNext();) {
			Map element = (Map) iter.next();
			Long nrFatura = (Long)element.remove("nrFatura");
			String sgFilial = (String)element.remove("sgFilial");
			
			element.put("nrFatura", sgFilial + " - " + FormatUtils.completaDados(nrFatura, "0", 10, 0, true));
		}
    	
    	item.put("itemLigacoes", faturas);
    	
		return (Serializable) item;
	}

	/**
	 * Montagem do combo das faturas existentes p/ a cobranca inadimplencia
	 * @param tfm
	 * @return List
	 */
	public List findComboFaturasInadimplencia(TypedFlatMap tfm) {

		Long idCobrancaInadimplencia = tfm.getLong("idCobrancaInadimplencia");
		
    	SqlTemplate sql = new SqlTemplate();
    	
		sql.addProjection(
				new StringBuffer()
					.append("new Map( F.idFatura as idFatura, ")
					.append(" F.nrFatura as nrFatura, ")
					.append(" FIL.sgFilial as sgFilial ")
					.append(" ) ")
					.toString()
				);
		
		sql.addFrom(ItemCobranca.class.getName(), " IC " +
				" JOIN IC.fatura F "+
				" JOIN IC.cobrancaInadimplencia CI " +
		    	" JOIN F.filialByIdFilial FIL "
		);	
		
    	if( tfm.containsKey("viaIdLigacaoCobranca") && StringUtils.isNotBlank(tfm.getString("viaIdLigacaoCobranca")) ){
            sql.addCriteria("LC.id","=",tfm.getLong("ligacaoCobranca.idLigacaoCobranca"));
        } else if(idCobrancaInadimplencia != null && StringUtils.isNotBlank(idCobrancaInadimplencia.toString())) {
    		sql.addCriteria("CI.idCobrancaInadimplencia", "=", idCobrancaInadimplencia);
        }   
    	
    	sql.addOrderBy("FIL.sgFilial, F.nrFatura");
    	
    	List lista = getHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	return lista;		
	}
    
    /**
     * Montagem da listbox das faturas existentes p/ a cobranca inadimplencia
     * @param tfm Critérios de pesquisa : idCobrancaInadimplencia ou ligacaoCobranca.idLigacaoCobranca
     * @return Lista de faturas
     */
    public List findFaturasInadimplenciaByAgendaCobranca(TypedFlatMap tfm) {

        String idCobrancaInadimplencia = tfm.getString("idCobrancaInadimplencia");
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("new Map( f.idFatura as idFatura, " +
                          "         fil.sgFilial as sgFilial, " +
                          "         f.nrFatura as nrFatura )");
        
        sql.addFrom(ItemLigacao.class.getName() + " as il " +                
                    " join il.itemCobranca as ic " +
                    " join il.ligacaoCobranca as lc " +
                    " join lc.cobrancaInadimplencia as ci " +
                    " join ic.fatura as f " +
                    " join f.filialByIdFilial as fil");
        
        if( tfm.containsKey("viaIdLigacaoCobranca") && StringUtils.isNotBlank(tfm.getString("viaIdLigacaoCobranca")) ){
            sql.addCriteria("lc.id","=",tfm.getLong("ligacaoCobranca.idLigacaoCobranca"));
        } else if(idCobrancaInadimplencia != null && StringUtils.isNotBlank(idCobrancaInadimplencia.toString())) {
            sql.addCriteria("ci.id", "=", idCobrancaInadimplencia);
        }   

        sql.addOrderBy("fil.sgFilial, f.nrFatura");
        
        List lista = getHibernateTemplate().find(sql.getSql(), sql.getCriteria());
        
        /** Formata o número da fatura */
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			Map element = (Map) iter.next();
			Long nrFatura = (Long)element.remove("nrFatura");
			String sgFilial = (String)element.remove("sgFilial");
			
			element.put("descricao", sgFilial + " " + FormatUtils.completaDados(nrFatura, "0", 10, 0, true));
		}
    	
        
        return lista;       
    }

   /**
     * Busca os dados da última Ligação Cobrança de acordo com a cobrança inadimplência
     * @param tfm Map contendo o Identificador da Cobrança Inadimplência
     * @return A última ligação cobrança de acordo com a cobrança inadimplência
     */
    public LigacaoCobranca findDadosLigacaoCobrancaByCobrancaInadimplencia(TypedFlatMap tfm) {
        
        LigacaoCobranca lc = null;        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("new Map(lc.idLigacaoCobranca as idLigacaoCobranca, " +
                          "        u.idUsuario as idUsuario, " +
                          "        u.nmUsuario as nmUsuario, " +
                          "        u.nrMatricula as nrMatricula, " +
                          "        tc.idTelefoneContato as idTelefoneContato, " +
                          "        te.nrTelefone as nrTelefone, " +
                          "        tc.nrRamal as nrRamal, " +
                          "        c.nmContato as nmContato," +
                          "        c.idContato as idContato, " +
                          "        lc.dsLigacaoCobranca as dsLigacaoCobranca," +
                          "        lc.dhLigacaoCobranca as dhLigacaoCobranca)");
        
        sql.addFrom(LigacaoCobranca.class.getName() + " as lc " +
                    "   inner join lc.usuario as u " +
                    "   inner join lc.telefoneContato as tc " +
                    "   inner join tc.contato as c " +
                    "   inner join tc.telefoneEndereco as te " +
                    "   inner join lc.cobrancaInadimplencia ci");
        
        sql.addCriteria("ci.id","=", tfm.getLong("cobrancaInadimplencia.idCobrancaInadimplencia"));
        
        sql.addOrderBy("lc.idLigacaoCobranca");
        
        List resultado = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
        
        if( resultado != null && !resultado.isEmpty() ){
            
            Map ret = (Map) resultado.get(resultado.size()-1);
            
            TypedFlatMap typed = new TypedFlatMap();
            typed.putAll(ret);
            
            Usuario u = new Usuario();
            TelefoneContato tc = new TelefoneContato();
            Contato c = new Contato();
            
            u.setIdUsuario(typed.getLong("idUsuario"));
            u.setNmUsuario(typed.getString("nmUsuario"));
            u.setNrMatricula(typed.getString("nrMatricula"));
            
            tc.setIdTelefoneContato(typed.getLong("idTelefoneContato"));
            c.setIdContato(typed.getLong("idContato"));
            
            if(typed.getString("nmContato") != null && typed.getString("nrTelefone") != null && typed.getString("nrRamal") != null){
            	c.setNmContato(typed.getString("nmContato") + " - " + typed.getString("nrTelefone") + " - " + typed.getString("nrRamal"));
            }else if(typed.getString("nmContato") != null && typed.getString("nrTelefone") != null && typed.getString("nrRamal") == null){
            	c.setNmContato(typed.getString("nmContato") + " - " + typed.getString("nrTelefone"));
            }else{
            	c.setNmContato("");
            }
            tc.setContato(c);
            
            lc = new LigacaoCobranca();
            lc.setIdLigacaoCobranca(typed.getLong("idLigacaoCobranca"));
            lc.setDsLigacaoCobranca(typed.getString("dsLigacaoCobranca"));
            lc.setDhLigacaoCobranca(typed.getDateTime("dhLigacaoCobranca"));
            lc.setUsuario(u);
            lc.setTelefoneContato(tc);
            
            TypedFlatMap nova = new TypedFlatMap();
            nova.put("ligacaoCobranca.idLigacaoCobranca", typed.get("idLigacaoCobranca"));
            nova.put("viaIdLigacaoCobranca","true");
            
            List faturasInadimplencia = findFaturasInadimplenciaByAgendaCobranca(nova);
            
            lc.setItemLigacoes(faturasInadimplencia);
            
        }
        
        return lc;
    }

	public List findFaturasByCobrancaInadimplencia(Long id) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(
				new StringBuffer()
					.append("new Map( IC.idItemCobranca as idItemCobranca, ")
					.append(" F.nrFatura as nrFatura, ")
					.append(" FIL.sgFilial as sgFilial, ")
					.append(" CI.idCobrancaInadimplencia as idCobrancaInadimplencia) ")
					.toString()
				);
		
		sql.addFrom(ItemCobranca.class.getName(), " IC " +
				" JOIN IC.fatura F "+
				" JOIN IC.cobrancaInadimplencia CI " +
		    	" JOIN F.filialByIdFilial FIL "
		);
		
		if(id != null && StringUtils.isNotBlank(id.toString())) {
			sql.addCriteria("IC.cobrancaInadimplencia.idCobrancaInadimplencia", "=", id);
		}
		
		sql.addCriteriaNotIn("F.tpSituacaoFatura", new Object[]{"LI", "CA"}); 
		
		List faturas = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
		return faturas;
	}

	public List findFaturasByLigacaoCobranca(Long id) {
		
    	SqlTemplate sql = new SqlTemplate();

    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( IC.idItemCobranca as idItemCobranca, ")
    				.append(" F.nrFatura as nrFatura, ")
    				.append(" FIL.sgFilial as sgFilial, ")
    				.append(" LC.idLigacaoCobranca as idLigacaoCobranca) ")
    				.toString()
    			);
    	
    	sql.addFrom(LigacaoCobranca.class.getName(), " LC " + 
    			" JOIN LC.itemLigacoes IL" +
    			" JOIN IL.itemCobranca IC " +
    			" JOIN IC.fatura F " +
		    	" JOIN F.filialByIdFilial FIL "
    			);
    	
    	if(StringUtils.isNotBlank(id.toString())) {
    		sql.addCriteria("LC.idLigacaoCobranca", "=", id);
    	}
    	
    	List faturas = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	return faturas;
	}
    
	public ResultSetPage findPaginatedByLigacaoCobranca(TypedFlatMap tfm) {
    	FindDefinition findDef = FindDefinition.createFindDefinition(tfm);
    	
    	Long idUsuario = tfm.getLong("usuario.idUsuario");
    	Long idTelefoneContato = tfm.getLong("telefoneContato.idTelefoneContato");
    	Long idFatura = tfm.getLong("idFatura");
    	DateTime dhLigacaoInicial = tfm.getDateTime("dhLigacaoInicial");
    	DateTime dhLigacaoFinal = tfm.getDateTime("dhLigacaoFinal");
    	
    	SqlTemplate sql = new SqlTemplate();

    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( LC.idLigacaoCobranca as idLigacaoCobranca, ")
    				.append(" LC.dhLigacaoCobranca as ligacaoCobranca, ")
    				.append(" C.nmContato as nmContato, ")
    				.append(" F.nmFuncionario as nmFuncionario )")
    				.toString()
    			);
    	
    	genericFrom(sql);
    	
    	genericWhere(tfm, idUsuario, idTelefoneContato, idFatura,
				dhLigacaoInicial, dhLigacaoFinal, sql);
   		
    	sql.addOrderBy("LC.dhLigacaoCobranca.value, C.nmContato, F.nmFuncionario");

    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		
    	return rsp;
	}

	private void genericFrom(SqlTemplate sql) {
		sql.addFrom(LigacaoCobranca.class.getName(), " LC " + 
    			" JOIN LC.usuario.vfuncionario F " +
    			" JOIN LC.telefoneContato.contato C " +
    			" JOIN LC.cobrancaInadimplencia CI ");
	}

	public Integer getRowCountByLigacaoCobranca(TypedFlatMap tfm) {

    	Long idUsuario = tfm.getLong("usuario.idUsuario");
    	Long idTelefoneContato = tfm.getLong("telefoneContato.idTelefoneContato");
    	Long idFatura = tfm.getLong("idFatura");
    	DateTime dhLigacaoInicial = tfm.getDateTime("dhLigacaoInicial");
    	DateTime dhLigacaoFinal = tfm.getDateTime("dhLigacaoFinal");
    	
    	SqlTemplate sql = new SqlTemplate();   
    	
    	sql.addProjection("count(LC.idLigacaoCobranca)");

    	genericFrom(sql);
    	genericWhere(tfm, idUsuario, idTelefoneContato, idFatura,
				dhLigacaoInicial, dhLigacaoFinal, sql);
    	
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
        return result.intValue();
        
	}

	private void genericWhere(TypedFlatMap tfm, Long idUsuario,
			Long idTelefoneContato, Long idFatura, DateTime dhLigacaoInicial,
			DateTime dhLigacaoFinal, SqlTemplate sql) {
		
		if(idFatura != null) {
   			sql.addCustomCriteria(" EXISTS ( " +
				    				" 	SELECT 		1 " +
				    				" 	FROM 		" + ItemLigacao.class.getName() + " IL " +
				    				" 		JOIN IL.itemCobranca IC " +
				    				" 		JOIN IC.fatura FAT " +
				    				" 	WHERE		IL.ligacaoCobranca.id = LC.id " +
				    				" 	AND			FAT.id = ? " +
				    				" ) ");
    		sql.addCriteriaValue(idFatura);
    	}
    	if(idUsuario != null) {
    		sql.addCriteria("F.usuario.idUsuario", "=", idUsuario);
    	}
    	if(idTelefoneContato != null) {
    		sql.addCriteria("LC.telefoneContato.idTelefoneContato", "=", idTelefoneContato);
    	}
   		sql.addCriteria("LC.dhLigacaoCobranca.value", ">=", dhLigacaoInicial);
   		sql.addCriteria("LC.dhLigacaoCobranca.value", "<=", dhLigacaoFinal);
   		sql.addCriteria("CI.idCobrancaInadimplencia","=",tfm.getLong("cobrancaInadimplencia.idCobrancaInadimplencia"));
   		
	}

}