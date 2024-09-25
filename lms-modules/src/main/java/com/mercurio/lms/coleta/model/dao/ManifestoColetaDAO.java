package com.mercurio.lms.coleta.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoColetaDAO extends BaseCrudDao<ManifestoColeta, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ManifestoColeta.class;
    } 
    
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("controleCarga", FetchMode.JOIN);
    	map.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
    	map.put("controleCarga.moeda", FetchMode.JOIN);
    	map.put("controleCarga.meioTransporteByIdTransportado", FetchMode.JOIN);
    	map.put("controleCarga.meioTransporteByIdSemiRebocado", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map map) {
    	map.put("filial",FetchMode.JOIN);
    	map.put("filial.pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map map) {
    	map.put("rotaColetaEntrega",FetchMode.JOIN);
    	map.put("controleCarga",FetchMode.JOIN);
    	map.put("controleCarga.moeda",FetchMode.JOIN);
    	map.put("controleCarga.filialByIdFilialOrigem",FetchMode.JOIN);
    	map.put("controleCarga.meioTransporteByIdTransportado",FetchMode.JOIN);
    	map.put("controleCarga.meioTransporteByIdSemiRebocado",FetchMode.JOIN);
    	map.put("filial",FetchMode.JOIN);
    	
    	map.put("eventoManifestoColetas",FetchMode.SELECT);
    	map.put("controleCarga.eventoControleCargas",FetchMode.SELECT);
	}

    /**
     * Retorna uma list de registros de Manifesto de Coleta com o ID do Controle de Carga
     * 
     * @param idControleCarga
     * @return
     */
    public List findManifestoColetaByIdControleCarga(Long idControleCarga) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ManifestoColeta.class);
    	dc.add(Restrictions.eq("controleCarga.id", idControleCarga));    	
    	
    	return  super.findByDetachedCriteria(dc);
    }         
	/**
	 * Busca lista de coletas para o projeto VOL
	 * @param idControleCarga
	 * @return
	 */
	public List findColetasToMobile(Long idControleCarga) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new map(PESS.nmPessoa","CLIE");
		hql.addProjection("PECO.idPedidoColeta","COLE");
		hql.addProjection("PECO.nrColeta","NRCOL");
		hql.addProjection("PESS.nrIdentificacao","CNPJ");
		hql.addProjection("ENPE.nrCep","CEP");
		hql.addProjection("PECO.qtTotalVolumesInformado","VOLS");
		hql.addProjection("ENPE.dsEndereco || '/N ' || nvl(ENPE.nrEndereco,0)","ENDE");
		hql.addProjection("PECO.tpStatusColeta","STAT");
		hql.addProjection("PECO.blConfirmacaoVol","COLE_RE");
		hql.addProjection("PECO.vlTotalVerificado", "VALOR");
		hql.addProjection("case when PECO.tpModoPedidoColeta='AU' then 'Automática' else PECO.obPedidoColeta end","OBSE");
		hql.addProjection("PECO.nmContatoCliente","CONT");
		hql.addProjection("to_char(PECO.hrLimiteColeta,'hh24:mi')","HORA");
		hql.addProjection("'motivo'","MOTI");
		hql.addProjection("PECO.psTotalInformado","PESO");
		hql.addProjection("PECO.blProdutoDiferenciado","PDPR)");
		
		StringBuffer from = new StringBuffer()
			.append(PedidoColeta.class.getName() + "  as PECO ")
			.append(" inner join PECO.manifestoColeta as MACO ")
			.append(" inner join MACO.controleCarga   as COCA ")
			.append(" inner join PECO.cliente         as CLIE ")
			.append(" inner join CLIE.pessoa          as PESS ")
			.append(" inner join PECO.enderecoPessoa  as ENPE ")
		;		
		
		hql.addFrom(from.toString());
		
		hql.addCriteria("COCA.tpControleCarga","=","C");
		hql.addCustomCriteria("not COCA.tpStatusControleCarga in ('FE','CA')");
		hql.addCriteria("COCA.idControleCarga","=",idControleCarga);
		
		List retorno = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		return retorno;
	}
	
    /**
     * Verifica se todos os manifestos relacionados ao controle de carga foram emitidos.
     * 
     * @param idControleCarga
     * @return TRUE se existe, FALSE caso contrário
     */
    public Boolean findVerificaExisteManifestoNaoEmitido(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
    	.append("select count(*) ")
    	.append("from ")
    	.append(ManifestoColeta.class.getName()).append(" as mc ")
		.append("where ")
		.append("mc.dhEmissao.value is null ")
		.append("and mc.controleCarga.id = ? ")
		.append("and mc.tpStatusManifestoColeta <> ? ");

    	List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga, "CA"});
    	return Boolean.valueOf( ((Long)result.get(0)).longValue() > 0 );
    }
    
    
    /**
     * Verifica se há manifestos de coleta relacionados ao controle de carga.
     * 
     * @param idControleCarga
     * @return TRUE se existe, FALSE caso contrário
     */
    public Boolean findVerificaManifestosAssociados(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
    	.append("select count(*) ")
    	.append("from ")
    	.append(ManifestoColeta.class.getName()).append(" as mc ")
		.append("where ")
		.append("mc.dhEmissao.value is not null ")
		.append("and mc.controleCarga.id = ? ");

    	List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    	return Boolean.valueOf( ((Long)result.get(0)).longValue() > 0 );
    }
    
    /**
     * Gera a consulta para a grid da tela de excluirManifestoColeta. Requer o id do <code>ManifestoColeta</code> 
     * que será detalhado.
     * 
     * @param idManifestoColeta
     * @param findDefinition
     * @author Christian Brunkow
     * @return
     */
    public ResultSetPage findPaginatedManifestoColeta(Long idManifestoColeta, FindDefinition findDefinition) {
    	
    	SqlTemplate sql = this.getHQLForManifestoColeta(idManifestoColeta);
	    
	    StringBuffer projecao = new StringBuffer();
		projecao.append("new map(manifestoColeta.idManifestoColeta as idManifestoColeta, ");
		projecao.append("pedidoColeta.idPedidoColeta as idPedidoColeta, ");
		projecao.append("filialByIdFilialResponsavel.sgFilial as sgFilial, ");
		projecao.append("pedidoColeta.nrColeta as nrColeta, ");
		projecao.append("rotaColetaEntrega.dsRota as dsRota, ");
		projecao.append("clientePessoa.nmPessoa as nmPessoa, ");
		projecao.append("pedidoColeta.nrTelefoneCliente as nrTelefoneCliente, ");
		projecao.append("pedidoColeta.qtTotalVolumesVerificado as qtTotalVolumesVerificado, ");
		projecao.append("pedidoColeta.psTotalVerificado as psTotalVerificado, ");
		projecao.append("moeda.sgMoeda as sgMoeda, ");
		projecao.append("moeda.dsSimbolo as dsSimbolo, ");
		projecao.append("pedidoColeta.vlTotalVerificado as vlTotalVerificado, ");
		projecao.append("pedidoColeta.dhPedidoColeta as dhPedidoColeta, ");
		projecao.append("pedidoColeta.dtPrevisaoColeta as dtPrevisaoColeta, ");
		projecao.append("pedidoColeta.nmContatoCliente as nmContatoCliente, ");
		projecao.append("regiaoColetaEntregaFil.dsRegiaoColetaEntregaFil as dsRegiaoColetaEntregaFil, ");
		projecao.append("pedidoColeta.obPedidoColeta as obPedidoColeta, ");
		//nrIdentificacaoFormatado...
		projecao.append("clientePessoa.tpIdentificacao as tpIdentificacao, ");
		projecao.append("clientePessoa.nrIdentificacao as nrIdentificacao, ");
		//enderecoCompleto...
		projecao.append("pedidoColeta.edColeta as edColeta, ");
		projecao.append("pedidoColeta.nrEndereco as nrEndereco, ");
		projecao.append("pedidoColeta.nrEndereco as dsComplementoEndereco, ");
		projecao.append("pedidoColeta.dsBairro as dsBairro, ");
		projecao.append("pedidoColeta.dhColetaDisponivel as dhColetaDisponivel, ");
		projecao.append("pedidoColeta.hrLimiteColeta as hrLimiteColeta) ");
		
		sql.addProjection(projecao.toString());
		
		return getAdsmHibernateTemplate().findPaginated( 
        		sql.getSql(),
        		findDefinition.getCurrentPage(), 
        		findDefinition.getPageSize(), 
        		sql.getCriteria());
	}
    
    /**
     * Gera a rowCount para a grid da tela de excluirManifestoColeta. Requer o id do <code>ManifestoColeta</code> 
     * que será detalhado.
     * 
     * @param idManifestoColeta
     * @author Christian Brunkow
     * @return
     */
    public Integer getRowCountManifestoColeta(Long idManifestoColeta) {
    	SqlTemplate sql = this.getHQLForManifestoColeta(idManifestoColeta);
    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    	return rowCount;
    }
    
    /**
     * <p>Gera o SqlTemplate basico para os find´s:</p>
     * <p>findPaginatedManifestoColeta;</p>
     * <p>getRowCountManifestoColeta;</p>
     * 
     * @param idManifestoColeta
     * @author Christian Brunkow
     * @return
     */
    private SqlTemplate getHQLForManifestoColeta(Long idManifestoColeta) {
    	
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " as manifestoColeta " +
				"join manifestoColeta.filial as filial " +
				"join manifestoColeta.pedidoColetas as pedidoColeta " +
				"left join pedidoColeta.filialByIdFilialResponsavel as filialByIdFilialResponsavel " +
				"left join pedidoColeta.cliente as cliente " +
  				"left join cliente.pessoa as clientePessoa " +
				"left join pedidoColeta.moeda as moeda " +
				"left join pedidoColeta.rotaColetaEntrega as rotaColetaEntrega " +
				"left join rotaColetaEntrega.regiaoFilialRotaColEnts as regiaoFilialRotaColEnts " +
				"left join regiaoFilialRotaColEnts.regiaoColetaEntregaFil as regiaoColetaEntregaFil");

		sql.addCriteria("manifestoColeta.idManifestoColeta", "=", idManifestoColeta);
		
        return sql;    	
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param idRota
     * @return Integer com o numero de registos com os dados da grid.
     */
    public Integer getRowCountByRotaColetaEntregaGE(Long idRota){
    	SqlTemplate sql = this.getHQLByRotaColetaEntregaGE(idRota);
    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    	return rowCount;
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um idRotaColetaEntrega seja informado.
     * Tem como restricao buscar apenas "Tipo de Status de Controle de Carga" setado como "GE". 
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedByRotaColetaEntregaGE(Long idRota, FindDefinition findDefinition){

    	SqlTemplate sql = this.getHQLByRotaColetaEntregaGE(idRota);
    	
    	StringBuffer projecao = new StringBuffer()
		.append("new map(filialByIdFilialOrigem.sgFilial as sgFilialOrigem, ")
		.append("controleCarga.idControleCarga as idControleCarga, ")
		.append("controleCarga.nrControleCarga as nrControleCarga, ")
		.append("controleCarga.psTotalFrota as psTotalFrota, ")
		.append("controleCarga.vlTotalFrota as vlTotalFrota, ")
		.append("manifestoColeta.idManifestoColeta as idManifestoColeta, ")
		.append("manifestoColeta.dhGeracao as dhGeracao, ")
		.append("manifestoColeta.nrManifesto as nrManifesto, ")
		.append("filial.idFilial as filial_idFilial, ")
		.append("filial.sgFilial as filial_sgFilial, ")
		.append("pessoaFilial.nmFantasia as filial_pessoa_nmFantasia, ")
		.append("meioTransporteByIdTransportado.nrFrota as nrFrotaTransportado, ")
		.append("meioTransporteByIdTransportado.nrIdentificador as nrIdentificadorTransportado, ")
		.append("meioTransporteByIdTransportado.nrCapacidadeKg as nrCapacidadeKg, ")
		.append("meioTransporteByIdSemiRebocado.nrFrota as nrFrotaSemiRebocado, ")
		.append("meioTransporteByIdSemiRebocado.nrIdentificador as nrIdentificadorSemiRebocado, ")
		.append("moeda.dsSimbolo as dsSimbolo, ")
		.append("moeda.sgMoeda as sgMoeda) ");
		sql.addProjection(projecao.toString());
		
        return getAdsmHibernateTemplate().findPaginated( 
        		sql.getSql(),
        		findDefinition.getCurrentPage(), 
        		findDefinition.getPageSize(), 
        		sql.getCriteria());        
    }
    
    /**
     * Retorna um SQLTemplate contendo o HQL das consultas de findPaginated e 
     * getRowCount de RotaColetaEntregaGE.
     * 
     * @param idRota
     * @return
     */
    private SqlTemplate getHQLByRotaColetaEntregaGE(Long idRota) {
    	
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " as manifestoColeta " +
				"inner join manifestoColeta.filial as filial " +
				"inner join filial.pessoa as pessoaFilial " +
				"inner join manifestoColeta.controleCarga as controleCarga " +
				"left join controleCarga.rotaColetaEntrega as rotaColetaEntrega " +
  				"left join controleCarga.meioTransporteByIdTransportado as meioTransporteByIdTransportado " +
				"left join controleCarga.meioTransporteByIdSemiRebocado as meioTransporteByIdSemiRebocado " +
				"left join controleCarga.moeda as moeda " +
				"inner join controleCarga.filialByIdFilialOrigem as filialByIdFilialOrigem");

		sql.addCriteria("rotaColetaEntrega.idRotaColetaEntrega", "=", idRota);
        return sql;
    }
    
    /**
     * Busca o manifesto de coleta mais antigo com o status passado por parâmetro para um controle de cargas especificado.
     * @param idControleCarga
     * @param tpStatusManifestoColeta
     * @return
     */
    public ManifestoColeta findManifestoColetaMaisAntigoByIdControleCargaByTpStatusManifestoColeta(Long idControleCarga, String tpStatusManifestoColeta) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ManifestoColeta.class, "mc");
    	dc.add(Restrictions.eq("mc.controleCarga.id", idControleCarga));
    	dc.add(Restrictions.eq("mc.tpStatusManifestoColeta", tpStatusManifestoColeta));
    	dc.addOrder(Order.asc("mc.dhGeracao.value"));
    	List listManifestosColeta = super.findByDetachedCriteria(dc);
    	if (listManifestosColeta.size()>0){
    		return (ManifestoColeta)listManifestosColeta.get(0);
    	}
    	return null;
    }
    
    
    
    /**
	 * Busca os manifestos que tem status diferente de CA e com tpEventoManifestoColeta = "EM" 
	 * que estejam vinculados ao controle de carga recebido por parâmetro.
	 *
	 * @param idControleCarga
	 * @return True se encontrar algum registro, caso contrário, False.
	 */
    public Boolean findManifestoNaoCanceladoByControleCarga(Long idControleCarga){
    	StringBuffer sql = new StringBuffer()
    	.append("from ")
    	.append(ManifestoColeta.class.getName()).append(" as mc ")
    	.append("where ")
    	.append("mc.controleCarga.id = ? ")
    	.append("and mc.tpStatusManifestoColeta <> 'CA' ")
    	.append("and exists (")
	    	.append(" select 1 from ")
	    	.append(EventoManifestoColeta.class.getName()).append(" as emc ")
	    	.append("where emc.manifestoColeta.id = mc.id ")
	    	.append("and emc.tpEventoManifestoColeta = 'EM') ");    	

    	Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga});
    	return Boolean.valueOf(!rows.equals(0));
    }


	/**
     * Alterar o campo tpStatusManifestoColeta para "CA" (Cancelado) na tabela MANIFESTO_COLETA 
     * para o Controle de Carga em questão.
     * 
     * @param idControleCarga
     */
    public void updateSituacaoManifestoColetaByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("update ")
	    	.append(ManifestoColeta.class.getName()).append(" as mc ")
	    	.append(" set mc.tpStatusManifestoColeta = 'CA' ")
	    	.append("where mc.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	executeHql(sql.toString(), param);
    }

    /**
	 * Consulta a quantidade de coletas efetuadas
	 * 
	 * @param idManifestoColeta
	 * @return
	 */
	public Integer findQuantidadeColetasEfetuadasByIdManifestoColeta(Long idManifestoColeta) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(mc)  ");
		hql.append(" from ManifestoColeta as mc ");
		hql.append(" join mc.pedidoColetas as pc ");
		hql.append(" where mc.idManifestoColeta = ? ");
		hql.append("   and pc.tpStatusColeta in ('FI','NT','EX') ");
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{ idManifestoColeta });
		
	}

	public List<Map<String, Object>> findManifestoColetaSuggest(final String sgFilial, final Long nrManifesto, final Long idEmpresa) {
		final StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT mc.id_manifesto_coleta, ");
		sql.append("       fo.sg_filial as sg_filial, "); 
		sql.append("       mc.nr_manifesto, ");
		sql.append("       mc.dh_geracao ");
		
		sql.append("  FROM manifesto_coleta mc ");
		sql.append("       inner join filial fo on fo.id_filial = mc.id_filial_origem ");
		
		sql.append(" WHERE fo.sg_filial = :sgFilial ");
		sql.append("   and mc.nr_manifesto = :nrManifesto ");
		if (idEmpresa != null) {
			sql.append("   and fo.id_empresa = :idEmpresa ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_manifesto_coleta", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_manifesto", Hibernate.LONG);
				sqlQuery.addScalar("dh_geracao", Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setString("sgFilial", sgFilial);
				query.setLong("nrManifesto", nrManifesto);
				if (idEmpresa != null) {
					query.setLong("idEmpresa", idEmpresa);
				}
            	csq.configQuery(query);
				return query.list();
			}
		};
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("idManifestoColeta", o[0]);
			map.put("sgFilial", o[1]);
			map.put("nrManifesto", o[2]);
			map.put("dhGeracao", o[3]);
			toReturn.add(map);
			
		}
		
		return toReturn;
	}
}