package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ConfiguracaoComunicacao;
import com.mercurio.lms.sim.model.EventoClienteRecebe;

/**
 * DAO pattern.    
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoClienteRecebeDAO extends BaseCrudDao<EventoClienteRecebe, Long>
{


    /**
     * Ádamo B. Azambuja
     * Realiza um find para buscar a quantidade de linhas do resultado para montar a paginacao em tela
     * conforme o filtro.
     * Ele utiliza o getSqlTemplate para montar o sql em outro metodo, porqque este sql template tambem é usado no 
     * getRowCountCustom logo abaixo*/
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate hql = this.getSqlTemplate(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }

    /**
     * Ádamo B. Azambuja
     * Monta o sql para ser usado no getRowCountCustom logo acima e para o find paginated custom abaixo, apenas monta
     * o sql, quem executa sao as funcoes que chamam o mesmo 
     * */
    public SqlTemplate getSqlTemplate(TypedFlatMap criteria){
        SqlTemplate sql = new SqlTemplate();
        
        // Relacionamentos
        sql.addFrom((new StringBuffer(ConfiguracaoComunicacao.class.getName())).append(" AS CC ")
        		.append("LEFT JOIN FETCH CC.cliente AS CLI ")
        		.append("INNER JOIN FETCH CLI.pessoa AS PES ")
        		.toString());

        sql.addCriteria("CC.tpCliente","=",criteria.getDomainValue("tipoCliente").getValue());
        sql.addCriteria("CLI.idCliente","=",criteria.getLong("cliente.idCliente"));
        sql.addCriteria("CC.tpAcessoEvento","=",criteria.getDomainValue("tipoAcesso").getValue());
        sql.addCriteria("CC.tpMeioComunicacao","=",criteria.getDomainValue("formaComunicacao").getValue());
        sql.addCriteria("CC.tpDocumento","=",criteria.getDomainValue("tpDocumentoCliente").getValue());
        sql.addCriteria("CC.servico.idServico","=",criteria.getLong("servico"));
        sql.addCriteria("CC.dtVigenciaInicial",">=",criteria.getYearMonthDay("dataInicio"));
        sql.addCriteria("CC.dtVigenciaFinal","<=",criteria.getYearMonthDay("dataFim"));
        sql.addCriteria("CC.hrDeterminado","=",criteria.getTimeOfDay("horario"));
        sql.addCriteria("CC.blComunicacaoCadaEvento","=",criteria.getString("comunicarCadaEvento"));
        sql.addCriteria("CC.blSomenteDiasUteis","=",criteria.getString("somenteDiasUteis"));
        sql.addCriteria("CC.nrIntervaloComunicacao","=",criteria.getInteger("intervaloComunicacao"));

        sql.addOrderBy("CLI.tpCliente");
        sql.addOrderBy("PES.nmFantasia");
        return sql;
    }

    /**
     * Ádamo B. Azambuja
     * Realiza um find para buscar os dados da tela de configuracao, retorna uma lista de configuracoes de comunicacao
     * conforme o filtro.
     * Ele utiliza o getSqlTemplate para montar o sql em outro metodo, porqque este sql template tambem é usado no 
     * getRowCountCustom logo acima*/
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlTemplate(criteria);
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
        return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

    /**
     * Ádamo B. Azambuja
     * Find para tela de detalhamento das configuracaoes de comunicacao de um cliente
     */
    public ResultSetPage findByIdDetalhamento(Long id) {
		SqlTemplate sql = new SqlTemplate();
        sql.addFrom((new StringBuffer(ConfiguracaoComunicacao.class.getName())).append(" AS CC ")
        		.append("LEFT JOIN FETCH CC.eventoClienteRecebes AS ECR ")
        		.append("LEFT JOIN FETCH CC.servico AS SRV ")
        		.append("LEFT JOIN FETCH ECR.evento AS EVT ")
        		.append("LEFT JOIN FETCH EVT.descricaoEvento as DE ")
        		.append("LEFT JOIN FETCH EVT.localizacaoMercadoria as LM ")
        		.append("LEFT JOIN FETCH CC.cliente AS CLI ")
        		.append("LEFT JOIN FETCH CLI.pessoa AS PES ")
        		.toString());
        sql.addCriteria("CC.idConfiguracaoComunicacao","=",id);
        return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),Integer.valueOf(1),Integer.valueOf(1),sql.getCriteria());
	}

    /**
     * Ádamo B. Azambuja
     * Retorna uma lista de eventos que um cliente possui em uma determinada vigencia
     * Esta regra é usada na action das configuracoes, ela é usada para nao permitir o cadastramento de um mesmo cliente
     * vigente com os mesmos eventos */
    public List validateEventosClienteVigente(Long idCliente, List list,YearMonthDay dataInicial, YearMonthDay dataFim) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoClienteRecebe.class,"ECR")
    		.createAlias("ECR.configuracaoComunicacao","CC")
    		.createAlias("CC.cliente","CLI")
    		.createAlias("ECR.evento","EVT")
    		.add(Restrictions.eq("CLI.idCliente",idCliente))
    		.add(Restrictions.ge("CC.dtVigenciaInicial",dataInicial))
    		.add(Restrictions.le("CC.dtVigenciaFinal",dataFim))
    		.add(Restrictions.in("ECR.evento",list))
		;
		return findByDetachedCriteria(dc); 
	}

    /**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoClienteRecebe.class;
    }

	public List findEventosConfiguracaoComunicacao(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoClienteRecebe.class,"ECR")
		//.createAlias("ECR.configuracaoComunicacao","CC")
		
		.createAlias("ECR.evento","EVT")
		.createAlias("EVT.localizacaoMercadoria","LM")
		.add(Restrictions.eq("ECR.configuracaoComunicacao.idConfiguracaoComunicacao",criteria.getLong("idConfiguracaoComunicacao")));
	
	return findByDetachedCriteria(dc); 
	}
   
	public SqlTemplate getSqlTemplateByObjeto(TypedFlatMap criteria){
	    SqlTemplate sql = new SqlTemplate();
	    
	    // Relacionamentos
	    sql.addFrom((new StringBuffer(ConfiguracaoComunicacao.class.getName())).append(" AS CC ")
	    		.append("LEFT JOIN FETCH CC.eventoClienteRecebes AS ECR ")
	    		.append("LEFT JOIN FETCH ECR.evento AS EVT ")
	    		.append("LEFT JOIN FETCH EVT.localizacaoMercadoria AS LM ")
	    		.toString());
	    sql.addCriteria("CC.idConfiguracaoComunicacao","=",criteria.getLong("idConfiguracaoComunicacao"));
	    return sql;	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}