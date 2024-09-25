package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CarregamentoDescargaDAO extends BaseCrudDao<CarregamentoDescarga, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CarregamentoDescarga.class;
    }
        
    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("controleCarga", FetchMode.JOIN);
	}
    
    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("descargaManifestos", FetchMode.SELECT);
    }

	/**
     * Busca todos os dados da tela de controleCargaCarregamento a partir do 'idControleCarga' 
     * 
     * @param idControleCarga
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    public List findCarregamentoDescargaByIdControleCarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao){
    	//Projecao
    	StringBuilder projecao = new StringBuilder();
		projecao.append("new map(carregamentoDescarga.idCarregamentoDescarga as idCarregamentoDescarga, ");
		projecao.append("controleCarga.idControleCarga as idControleCarga, ");
		projecao.append("filialPostoAvancado.sgFilial as sgFilialPostoAvancado, ");
		projecao.append("pessoaPostoAvancado.nmPessoa as nmPessoaPostoAvancado, ");
		projecao.append("carregamentoDescarga.dhInicioOperacao as dhInicioOperacao, ");
		projecao.append("carregamentoDescarga.dhFimOperacao as dhFimOperacao, ");
		projecao.append("filial.sgFilial as sgFilial, ");
		projecao.append("pessoa.nmPessoa as nmPessoa, ");
		projecao.append("pessoa.nmFantasia as nmFantasia, ");		
		projecao.append("carregamentoDescarga.tpStatusOperacao as tpStatusOperacao) ");		
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(projecao.toString());
   		
		//Clausula from com seus respectivos 'Joins'
		sql.addFrom(CarregamentoDescarga.class.getName() + " as carregamentoDescarga " +
				"left join carregamentoDescarga.controleCarga as controleCarga " +
  				"left join carregamentoDescarga.postoAvancado as postoAvancado " +
  				"left join postoAvancado.filial as filialPostoAvancado " +
  				"left join filialPostoAvancado.pessoa as pessoaPostoAvancado " +
  				"left join carregamentoDescarga.filial as filial " +
				"inner join filial.pessoa as pessoa");

		//Restricao filial...
		if (idPostoAvancado!=null) { 
			sql.addCriteria("carregamentoDescarga.postoAvancado.idPostoAvancado", "=", idPostoAvancado);
		} else {
			sql.addCustomCriteria("carregamentoDescarga.postoAvancado.idPostoAvancado is null");
		}
		//Restricao tipo operacao...
		if(tpOperacao != null) {
			sql.addCriteria("carregamentoDescarga.tpOperacao","=", tpOperacao);
			if (tpOperacao.equals("D")) {
				sql.addCustomCriteria("carregamentoDescarga.tpStatusOperacao not in ('P', 'C')");
			}
		}
		//Restricao tipo controle carga...
		sql.addCriteria("controleCarga.idControleCarga", "=", idControleCarga);
		//Restricao tipo filial...
		sql.addCriteria("filial.idFilial", "=", idFilial);
		
		List result = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
        return result;
    }
    
    /**
     * Dispara a consulta contra o banco retornando o numero de registros a serem populados na grid
     * da tela de 'CarregarVeiculoCarregamento' & 'DescarregarVeiculo'
     * 
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    public Integer getRowCountByIdControleCarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao){
    	SqlTemplate sql = this.getQueryIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    	return rowCount;
    }
    
    public Integer getRowCountByIdControleCargaTpOperacao(Long idControleCarga, String tpOperacao){
    	SqlTemplate sql = this.getQueryIdControleCargaTpOperacao(idControleCarga, tpOperacao);
    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    	return rowCount;
    }

    private SqlTemplate getQueryIdControleCargaTpOperacao(Long IdControleCarga, String tpOperacao){
    	StringBuilder projecao = new StringBuilder();
		projecao.append("carregamentoDescarga.idCarregamentoDescarga");
    	
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(projecao.toString());
		
		sql.addFrom(CarregamentoDescarga.class.getName() + " as carregamentoDescarga ");
		
		sql.addCriteria("carregamentoDescarga.controleCarga.id", "=", IdControleCarga);
		sql.addCriteria("carregamentoDescarga.tpOperacao", "=", tpOperacao);
    	
		return sql;
    }
    
    /**
     * Busca o carregamento GM a partir do identificador de controle de carga
     *
     * @param idControleCarga <code>Long</code>
     * @return <code>Long</code>
     * @author Sidarta Silva
     */
    public Long findIdCarregamentoGM(Long idControleCarga) {		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("Volume", "v");
		sql.addFrom("VolumeNotaFiscal", "vnf");
		sql.addFrom("NotaFiscalConhecimento", "nfc");
		
		sql.addFrom("ManifestoNacionalCto", "mnc");
		sql.addFrom("ManifestoViagemNacional", "mvn");
		sql.addFrom("Manifesto", "man");
		sql.addFrom("ControleCarga", "cc");
		sql.addFrom("Conhecimento", "c");
		
		sql.addProjection("v.carregamento.id");
		
		sql.addCustomCriteria("v.codigoVolume = vnf.nrVolumeColeta");
		sql.addCustomCriteria("nfc.id = vnf.notaFiscalConhecimento.id");
		sql.addCustomCriteria("nfc.conhecimento.id = c.id");
		sql.addCustomCriteria("c.id = mnc.conhecimento.id");
		sql.addCustomCriteria("mnc.manifestoViagemNacional.id = mvn.id");
		sql.addCustomCriteria("mvn.manifesto.id = man.id");
		sql.addCustomCriteria("man.controleCarga.id =  cc.id");
		 
		sql.addCustomCriteria("vnf.nrVolumeEmbarque is not null");
		
		sql.addCriteria("cc.id", "=", idControleCarga);
		
		sql.addGroupBy("v.carregamento.id");
		
		return (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * Busca lista de mapas a partir do identificador de carregamento
     *
     * @param idCarregamento <code>Long</code>
     * @return <code>List</code>
     * @author Sidarta Silva
     */
    public List findMapasByIdCarregamentoGM(Long idCarregamento) {
    	SqlTemplate sql = new SqlTemplate();   
    	
		sql.addFrom("Carregamento", "car");
		sql.addFrom("Volume", "v");
		sql.addFrom("VolumeNotaFiscal", "vnf");   
		
		sql.addProjection("v.carregamento.id");
		sql.addProjection("v.mapaCarregamento");
		sql.addProjection("vnf.monitoramentoDescarga.id");
		
		sql.addCustomCriteria("v.carregamento.id = car.id");
		sql.addCustomCriteria("v.codigoVolume = vnf.nrVolumeColeta");

		sql.addCriteria("v.carregamento.id", "=", idCarregamento);
		
		sql.addGroupBy("v.carregamento.id");
		sql.addGroupBy("v.mapaCarregamento");
		sql.addGroupBy("vnf.monitoramentoDescarga.id");
    	
    	return super.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }    

    /**
     * Realiza consulta para popular a grid de 'CarregarVeiculoCarregamento' & 'DescarregarVeiculo'
     * 
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao, FindDefinition findDefinition){
    	SqlTemplate sql = this.getQueryIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(),sql.getCriteria());
    }
    
    /**
     * Realiza consulta para popular a grid de 'CarregarVeiculoCarregamento' & 'DescarregarVeiculo'
     * 
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @param findDefinition
     * @return
     */
    public List findByIdControleCarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao){
    	SqlTemplate sql = this.getQueryIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
		return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    
    /**
     * Monta a consulta utilizada nas rotinas de 'getRowCountByIdControleCarga' e 'findPaginatedByIdControleCarga'
     * 
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    private  SqlTemplate getQueryIdControleCarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao){
    	//Projecao
    	StringBuilder projecao = new StringBuilder();
		projecao.append("new map(manifesto.idManifesto as idManifesto, ");
		projecao.append("filialByIdFilialOrigem.sgFilial as sgFilial, ");		
		projecao.append("manifesto.nrPreManifesto as nrPreManifesto, ");
		projecao.append("manifesto.dhGeracaoPreManifesto as dhGeracaoPreManifesto, ");
		projecao.append("manifesto.tpManifesto as tpManifesto, ");
		projecao.append("manifesto.tpManifestoViagem as tpManifestoViagem, ");		
		projecao.append("manifesto.tpManifestoEntrega as tpManifestoEntrega, ");
		projecao.append("manifesto.tpStatusManifesto as tpStatusManifesto, ");
		projecao.append("manifesto.filialByIdFilialDestino.idFilial as idFilialDestinoManifesto, ");
		projecao.append("carregamentoPreManifesto.dhInicioCarregamento as dhInicioCarregamento, ");
		projecao.append("carregamentoPreManifesto.dhFimCarregamento as dhFimCarregamento, ");
		projecao.append("moeda.dsSimbolo as dsSimbolo, ");
		projecao.append("moeda.sgMoeda as sgMoeda, ");
		projecao.append("manifesto.vlTotalManifesto as vlTotalManifesto, ");		
		projecao.append("carregamentoDescarga.idCarregamentoDescarga as idCarregamentoDescarga) ");
		  		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(projecao.toString());
   		
		//Clausula from com seus respectivos 'Joins'
		sql.addFrom(Manifesto.class.getName() + " as manifesto " +
				"left join manifesto.moeda as moeda " +
				"join manifesto.controleCarga as controleCarga " +
				"join manifesto.filialByIdFilialOrigem as filialByIdFilialOrigem " +
				"left join manifesto.carregamentoPreManifestos as carregamentoPreManifesto " +
  				"left join carregamentoPreManifesto.carregamentoDescarga as carregamentoDescarga ");
		
		//Restricao...
		sql.addCriteria("filialByIdFilialOrigem.id", "=", SessionUtils.getFilialSessao().getIdFilial());
		sql.addCriteria("manifesto.controleCarga.idControleCarga", "=", idControleCarga);
		sql.addCriteria("manifesto.tpStatusManifesto", "<>", "CA");		
		
		SqlTemplate subQuery = this.subQuery(idFilial, idPostoAvancado, tpOperacao);
		
		sql.addCustomCriteria("((carregamentoDescarga.idCarregamentoDescarga in (" + subQuery.getSql() + ")) " + 
							"OR (carregamentoDescarga.idCarregamentoDescarga IS null))");
		
		for (int i = 0; i < subQuery.getCriteria().length; i++) {
            Object object = subQuery.getCriteria()[i];
            sql.addCriteriaValue(object);
        }
		
		sql.addOrderBy("manifesto.nrPreManifesto", "asc");
		
		return sql;
    }
    
    /**
     * Subquery para a consulta do metodo 'getQueryIdControleCarga'
     * 
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    private SqlTemplate subQuery(Long idFilial, Long idPostoAvancado, String tpOperacao){
    	StringBuilder projecao = new StringBuilder();
		projecao.append("carregamentoDescarga.idCarregamentoDescarga");
    	
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(projecao.toString());
		
		sql.addFrom(CarregamentoDescarga.class.getName() + " as carregamentoDescarga ");
		
		sql.addCriteria("carregamentoDescarga.filial.id", "=", idFilial);
		sql.addCriteria("carregamentoDescarga.postoAvancado.id", "=", idPostoAvancado);
		sql.addCriteria("carregamentoDescarga.tpOperacao", "=", tpOperacao);
    	
		return sql;
    }
    
    public List findCarregamentoDescargaByIdFilialByIdControleCarga(Long idFilial, Long idControleCarga) {
    	DetachedCriteria dc = DetachedCriteria.forClass(CarregamentoDescarga.class)
    		.setFetchMode("filial", FetchMode.JOIN)
    		.setFetchMode("box", FetchMode.JOIN)
    		.add(Restrictions.eq("filial.id", idFilial))
    		.add(Restrictions.eq("controleCarga.id", idControleCarga)); 
    	
    	return super.findByDetachedCriteria(dc);
    }
    
    /**
     * LMS 5464 - Retorna o carregamento descarga para o volume informado
     * 
     * @param idVolume
     * @param idFilial 
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescargaByIdVolume(Long idVolume, Long idFilial) {    	
    	SqlTemplate sql = new SqlTemplate();
    	 
    	sql.addProjection("cd");
    	sql.addFrom(CarregamentoDescarga.class.getName() , "cd");
    	sql.addFrom(Filial.class.getName() , "fil");
    	sql.addFrom(ControleCarga.class.getName() , "cc");
    	sql.addFrom(Manifesto.class.getName() , "m");
    	sql.addFrom(PreManifestoVolume.class.getName() , "pmv");
    	sql.addFrom(VolumeNotaFiscal.class.getName() , "vnf");
    	
    	sql.addJoin("cd.controleCarga.id", "cc.id");      // CARREGAMENTO_DESCARGA.ID_CONTROLE_CARGA = CONTROLE_CARGA.ID_CONTROLE_CARGA
    	sql.addJoin("cd.filial.id", "fil.id");            // CARREGAMENTO_DESCARGA.ID_FILIAL = FILIAL.ID_FILIAL 
    	sql.addJoin("cc.id", "m.controleCarga.id");       // CONTROLE_CARGA.ID_CONTROLE_CARGA = MANIFESTO.ID_CONTROLE_CARGA 
    	sql.addJoin("m.id", "pmv.manifesto.id");          // MANIFESTO.ID_MANIFESTO = PRE_MANIFESTO_VOLUME.ID_MANIFESTO 
    	sql.addJoin("pmv.volumeNotaFiscal.id", "vnf.id"); // PRE_MANIFESTO_VOLUME.ID_VOLUME_NOTA_FISCAL = VOLUME_NOTA_FISCAL.ID_VOLUME_NOTA_FISCAL 
    	
    	sql.addCriteria("cd.tpOperacao", "=", "D");       // CARREGAMENTO_DESCARGA.TP_OPERACAO = ‘D’   
    	sql.addCriteria("vnf.id", "=", idVolume);         // VOLUME_NOTA_FISCAL.ID_VOLUME_NOTA_FISCAL = idVolume
    	sql.addCriteria("fil.id", "=", idFilial);         // FILIAL.ID_FILIAL = idFilial
    	sql.addCriteria("cd.tpStatusOperacao", "=", "I"); // CARREGAMENTO_DESCARGA.TP_STATUS_OPERACAO = ‘I’
			
    	return (CarregamentoDescarga) super.getAdsmHibernateTemplate().findUniqueResult(sql.toString(), sql.getCriteria());
    }
    
    /**
     * Retorna uma instancia de CarregamentoDescarca a partir dos parâmetros informados.
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    public List findByIdControleCargaIdFilialIdPostoAvancadoTpOperacao(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(CarregamentoDescarga.class.getName() + " as cd ");
    	sql.addCriteria("cd.controleCarga.id", "=" ,idControleCarga);
    	sql.addCriteria("cd.filial.id", "=", idFilial);
    	sql.addCriteria("cd.postoAvancado.id", "=", idPostoAvancado);
    	sql.addCriteria("cd.tpOperacao", "=", tpOperacao);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    
    public List findByControleCarga(ControleCarga controleCarga, String tpOperacao, Filial filial){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(CarregamentoDescarga.class.getName() + " as cd ");
    	sql.addCustomCriteria("cd.controleCarga = :controleCarga");
    	sql.addCustomCriteria("cd.tpOperacao = :tpOperacao");
    	sql.addCustomCriteria("cd.filial = :filial");
    	
    	Map<String,Object> param = new HashMap<String, Object>();
    	param.put("controleCarga", controleCarga);
    	param.put("tpOperacao", tpOperacao);
    	param.put("filial", filial);
    	
    	return this.getAdsmHibernateTemplate().findByNamedParam(sql.getSql(), param);
    }
    

	/**
	 * @param idControleCarga
 	 * @param idFilial
	 */    
    public List findCarregamentoDescargaByControleCarga(Long idControleCarga, Long idFilial, String tpOperacao) {
    	StringBuilder sql = new StringBuilder()
			.append("select new map(")
			.append("cd.idCarregamentoDescarga as idCarregamentoDescarga, ")
			.append("filial.sgFilial as filial_sgFilial, ")
			.append("pessoa.nmFantasia as filial_pessoa_nmFantasia, ")
			.append("equipe.dsEquipe as dsEquipe, ")
			.append("equipeOperacao.idEquipeOperacao as equipeOperacao_idEquipeOperacao, ")
			.append("cd.dhInicioOperacao as dhInicioOperacao, ")
			.append("cd.dhFimOperacao as dhFimOperacao, ")
			.append("cd.obOperacao as obOperacao) ")
			.append("from ")
			.append(CarregamentoDescarga.class.getName()).append(" as cd ")
			.append("inner join cd.controleCarga as cc ")
			.append("inner join cd.filial as filial ")
			.append("inner join filial.pessoa as pessoa ")
			.append("left join cd.equipeOperacoes as equipeOperacao ")
			.append("left join equipeOperacao.equipe as equipe ")
			.append("where ")
			.append("cc.id = ? ")
			.append("and cd.filial.id = ? ")
			.append("and cd.tpOperacao = ? ")
			.append("and (equipeOperacao.id is null or equipeOperacao.dhInicioOperacao.value = (")
				.append("select max(e1.dhInicioOperacao.value) as dhInicOper ") 
				.append("from ").append(EquipeOperacao.class.getName()).append(" as e1 ")
				.append("where e1.carregamentoDescarga.id = cd.id)) ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idFilial);
    	param.add(tpOperacao);
    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    
    /**
     * Método que retorna um Integer da quantidade de Manifestos a partir do ID do Controle de Carga testando se o 
     * tipo do Controle de Carga é de 'Viagem' ou 'Coleta'.
	 * @param criteria
	 * @return
	 */
    public Integer getRowCountManifestoByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino) {
    	StringBuilder sql = this.mountSqlManifestos(idControleCarga, tpControleCarga, idFilialDestino, true, true);    	
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {});
    }
    
    /**
     * Método que retorna um Integer da quantidade de Manifestos de Viagem e Entrega (menos de Coleta) a partir do 
     * ID do Controle de Carga testando se o tipo do Controle de Carga é de 'Viagem' ou 'Coleta'.
	 * @param criteria
	 * @return
	 */
    public Integer getRowCountManifestoViagemEntregaByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino) {
    	StringBuilder sql = this.mountSqlManifestos(idControleCarga, tpControleCarga, idFilialDestino, false, true);    	
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {});
    } 
    
	/**
	 * Método que retorna um ResultSetPage de Manifestos a partir do ID do Controle de Carga testando se o 
	 * tipo do Controle de Carga é de 'Viagem' ou 'Coleta' e se o tipo modal e tipo abrangência do Manifesto para
	 * pegar o número correspondente. 
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedManifestoByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino, FindDefinition findDefinition) {
    	StringBuilder hql = this.mountSqlManifestos(idControleCarga, tpControleCarga, idFilialDestino, true, false);    	
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
    			sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
    			sqlQuery.addScalar("NR_MANIFESTO", Hibernate.LONG);
    			sqlQuery.addScalar("DH_GERACAO_PRE_MANIFESTO", Hibernate.custom(JodaTimeDateTimeUserType.class));
    			sqlQuery.addScalar("TP_MANIFESTO",Hibernate.STRING);
    			sqlQuery.addScalar("TP_STATUS_MANIFESTO",Hibernate.STRING);
    			sqlQuery.addScalar("BL_BLOQUEADO",Hibernate.STRING);
    			sqlQuery.addScalar("ID_FILIAL_DESTINO",Hibernate.LONG);
    			sqlQuery.addScalar("SG_FILIAL_DESTINO",Hibernate.STRING);
    			sqlQuery.addScalar("DS_SIMBOLO",Hibernate.STRING);
    			sqlQuery.addScalar("SG_MOEDA",Hibernate.STRING);
    			sqlQuery.addScalar("VL_TOTAL_MANIFESTO",Hibernate.BIG_DECIMAL);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(hql.toString(), findDefinition.getCurrentPage(), 
    	    							findDefinition.getPageSize(), new Object[] {}, csq);
    }
    
	/**
	 * Método que retorna um ResultSetPage de Manifestos de Viagem e Entrega (menos de Coleta) a partir do ID
	 *  do Controle de Carga testando se o tipo do Controle de Carga é de 'Viagem' ou 'Coleta'
	 *  e se o tipo modal e tipo abrangência do Manifesto para pegar o número correspondente. 
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedManifestoViagemEntregaByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino, FindDefinition findDefinition) {
    	StringBuilder hql = this.mountSqlManifestos(idControleCarga, tpControleCarga, idFilialDestino, false, false);    	
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
    			sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
    			sqlQuery.addScalar("NR_MANIFESTO", Hibernate.LONG);
    			sqlQuery.addScalar("DH_GERACAO_PRE_MANIFESTO", Hibernate.custom(JodaTimeDateTimeUserType.class));
    			sqlQuery.addScalar("TP_MANIFESTO",Hibernate.STRING);
    			sqlQuery.addScalar("TP_STATUS_MANIFESTO",Hibernate.STRING);
    			sqlQuery.addScalar("BL_BLOQUEADO",Hibernate.STRING);
    			sqlQuery.addScalar("ID_FILIAL_DESTINO",Hibernate.LONG);
    			sqlQuery.addScalar("SG_FILIAL_DESTINO",Hibernate.STRING);
    			sqlQuery.addScalar("DS_SIMBOLO",Hibernate.STRING);
    			sqlQuery.addScalar("SG_MOEDA",Hibernate.STRING);
    			sqlQuery.addScalar("VL_TOTAL_MANIFESTO",Hibernate.BIG_DECIMAL);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(hql.toString(), findDefinition.getCurrentPage(), 
    	    							findDefinition.getPageSize(), new Object[] {}, csq);
    }
    
	
	/**
	 * Método que monta a consulta a ser utilizada pelo 'getRowCountManifestoByIdControleCarga' 
	 * e pelo 'findPaginatedManifestoByIdControleCarga'.
	 * 
	 * @param idControleCarga: ID do Controle de Carga através do qual serão buscados os Manifestos.
	 * @param idFilialDestino: quando setado, busca os Manifestos cuja filial de destino seja igual à filial passada. 
	 * @return
	 */
    private StringBuilder mountSqlManifestos(Long idControleCarga, String tpControleCarga, Long idFilialDestino, boolean includesManifestosColeta, boolean isRowCount) {
    	StringBuilder sql = new StringBuilder();
    	
       	if (tpControleCarga!=null && tpControleCarga.equals("V")) {
       		sql.append("SELECT M.ID_MANIFESTO AS ID_MANIFESTO, \n");
   			sql.append("	   FO.SG_FILIAL AS SG_FILIAL_ORIGEM, \n");
   			sql.append("	   CASE WHEN (M.TP_ABRANGENCIA = 'N') THEN MVN.NR_MANIFESTO_ORIGEM \n");
   			sql.append("      		WHEN (M.TP_ABRANGENCIA = 'I') THEN MI.NR_MANIFESTO_INT \n");
   			sql.append("	   END AS NR_MANIFESTO, \n");
   			sql.append("	   M.DH_GERACAO_PRE_MANIFESTO AS DH_GERACAO_PRE_MANIFESTO, \n");
   			sql.append("	   M.TP_MANIFESTO AS TP_MANIFESTO, \n");
   			sql.append("	   M.TP_STATUS_MANIFESTO AS TP_STATUS_MANIFESTO, \n");
   			sql.append("	   M.BL_BLOQUEADO AS BL_BLOQUEADO, \n");
   			sql.append("	   FD.ID_FILIAL AS ID_FILIAL_DESTINO, \n");
   			sql.append("	   FD.SG_FILIAL AS SG_FILIAL_DESTINO, \n");
   			sql.append("	   MO.DS_SIMBOLO AS DS_SIMBOLO, \n");
   			sql.append("	   MO.SG_MOEDA AS SG_MOEDA, \n");
   			sql.append("	   M.VL_TOTAL_MANIFESTO AS VL_TOTAL_MANIFESTO \n");
       		
       		sql.append("FROM MANIFESTO M, \n");
       		sql.append("	 CONTROLE_CARGA CC, \n");
       		sql.append("     FILIAL FO, \n");
       		sql.append("	 FILIAL FD, \n");
       		sql.append("	 MOEDA MO, \n");
       		sql.append("	 MANIFESTO_VIAGEM_NACIONAL MVN, \n");
       		sql.append("	 MANIFESTO_INTERNACIONAL MI \n");

       		sql.append("WHERE M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA \n");
       		sql.append("      AND M.ID_FILIAL_ORIGEM = FO.ID_FILIAL \n");
       		sql.append("      AND M.ID_FILIAL_DESTINO = FD.ID_FILIAL \n");
       		sql.append("      AND M.ID_MOEDA = MO.ID_MOEDA \n");
       		sql.append("      AND M.ID_MANIFESTO = MVN.ID_MANIFESTO_VIAGEM_NACIONAL(+) \n");
       		sql.append("      AND M.ID_MANIFESTO = MI.ID_MANIFESTO_INTERNACIONAL(+) \n");	
       	    sql.append("      AND CC.TP_CONTROLE_CARGA = 'V' \n");
       	    sql.append("      AND M.TP_MANIFESTO = 'V' \n");
       	    sql.append("      AND TP_STATUS_MANIFESTO <> 'CA' \n");
       	    sql.append("      AND CC.ID_CONTROLE_CARGA = " + idControleCarga + " \n");
       	    if(idFilialDestino!=null){
       	    	sql.append("      AND M.ID_FILIAL_DESTINO = " + idFilialDestino + " \n");
       	    }
       	    
       	} else if (tpControleCarga!=null && tpControleCarga.equals("C")) {
       		
       		sql.append("SELECT  M.ID_MANIFESTO AS ID_MANIFESTO, \n");
       		sql.append("        FO.SG_FILIAL AS SG_FILIAL_ORIGEM, \n");
       		sql.append("        ME.NR_MANIFESTO_ENTREGA AS NR_MANIFESTO, \n");
       		sql.append("        M.DH_GERACAO_PRE_MANIFESTO AS DH_GERACAO_PRE_MANIFESTO, \n");
       		sql.append("        M.TP_MANIFESTO AS TP_MANIFESTO, \n");
       		sql.append("        M.TP_STATUS_MANIFESTO AS TP_STATUS_MANIFESTO, \n");
       		sql.append("        M.BL_BLOQUEADO AS BL_BLOQUEADO, \n");
       		sql.append("        FD.ID_FILIAL AS ID_FILIAL_DESTINO, \n");
       		sql.append("        FD.SG_FILIAL AS SG_FILIAL_DESTINO, \n");
       		sql.append("        MO.DS_SIMBOLO AS DS_SIMBOLO, \n");
       		sql.append("        MO.SG_MOEDA AS SG_MOEDA, \n");
       		sql.append("        M.VL_TOTAL_MANIFESTO AS VL_TOTAL_MANIFESTO \n");
       		
       		sql.append("FROM    MANIFESTO M, \n");
       		sql.append("        CONTROLE_CARGA CC, \n");
       		sql.append("        FILIAL FO, \n");
       		sql.append("        FILIAL FD, \n");
       		sql.append("        MOEDA MO, \n");
       		sql.append("        MANIFESTO_ENTREGA ME \n");
       		
       		sql.append("WHERE   M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA \n");
       		sql.append("        AND M.ID_FILIAL_ORIGEM = FO.ID_FILIAL \n");
       		sql.append("        AND M.ID_FILIAL_DESTINO = FD.ID_FILIAL \n");
       		sql.append("        AND M.ID_MOEDA = MO.ID_MOEDA \n");
       		sql.append("        AND M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA \n");        
       		sql.append("        AND CC.TP_CONTROLE_CARGA = 'C' \n");
       		sql.append("        AND M.TP_MANIFESTO = 'E' \n");
       	    sql.append("        AND TP_STATUS_MANIFESTO <> 'CA' \n");       		
       		sql.append("        AND CC.ID_CONTROLE_CARGA = " + idControleCarga + " \n");
       	    if(idFilialDestino!=null){
       	    	sql.append("      AND M.ID_FILIAL_DESTINO = " + idFilialDestino + " \n");
       	    }
       	    
       	    if (includesManifestosColeta){
       	    	sql.append("UNION \n");
       	    	sql.append("SELECT  MC.ID_MANIFESTO_COLETA AS ID_MANIFESTO, \n");
       	    	sql.append("        FMC.SG_FILIAL AS SG_FILIAL_ORIGEM, \n");
       	    	sql.append("        MC.NR_MANIFESTO AS NR_MANIFESTO, \n");
       	    	sql.append("        MC.DH_GERACAO AS DH_GERACAO_PRE_MANIFESTO, \n");
       	    	sql.append("        NULL AS TP_MANIFESTO, \n");
       	    	sql.append("        MC.TP_STATUS_MANIFESTO_COLETA AS TP_STATUS_MANIFESTO, \n");
       	    	sql.append("        NULL AS BL_BLOQUEADO, \n");
       	    	sql.append("        FMC.ID_FILIAL AS ID_FILIAL_DESTINO, \n"); 
       	    	sql.append("        FMC.SG_FILIAL AS SG_FILIAL_DESTINO, \n");
       	    	sql.append("        NULL AS DS_SIMBOLO, \n");
       	    	sql.append("        NULL AS SG_MOEDA, \n");
       	    	sql.append("        NULL AS VL_TOTAL_MANIFESTO \n");
       	    	sql.append("FROM    CONTROLE_CARGA CC, \n");
       	    	sql.append("        MANIFESTO_COLETA MC, \n");
       	    	sql.append("        FILIAL FMC \n");
       	    	sql.append("WHERE   MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA \n");
       	    	sql.append("        AND MC.ID_FILIAL_ORIGEM = FMC.ID_FILIAL \n");
       	    	sql.append("        AND CC.TP_CONTROLE_CARGA = 'C' \n");
       	    	sql.append("        AND CC.ID_CONTROLE_CARGA = " + idControleCarga + " \n");
       	    	//No caso de Manifesto de Coleta, pegar a filial origem.
       	    	if(idFilialDestino!=null){
       	    		sql.append("      AND MC.ID_FILIAL_ORIGEM = " + idFilialDestino + " \n");
       	    	}
       	    }
       	}
    	if (!sql.toString().equals("") && isRowCount){
    		StringBuilder sqlCount = new StringBuilder();
    		sqlCount.append(" FROM ( ");
    		sqlCount.append(sql);
    		sqlCount.append(" ) ");
    		return sqlCount;
    	}
		return sql;    
    }
    
    /**
     * Solicitação da Integração - CQPRO00005521
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga( long  idControleCarga, long idFilial ) : CarregamentoDescarga
     * OBS.O método assume como critério que a dhFimOperacao seja null;
     * @param idControleCarga
     * @param idFilial 
     * @return
     */
    public List findCarregamentoDescarga(Long idControleCarga, Long idFilial){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(CarregamentoDescarga.class.getName() + " cd");
   		hql.addCriteria("cd.controleCarga.id", "=", idControleCarga);
   		hql.addCriteria("cd.filial.id", "=", idFilial);
   		hql.addCriteria("cd.dhFimOperacao.value", "=", null);
   		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Solicitação da Integração - CQPRO00006071
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String tpOperacao ) : CarregamentoDescarga
     * @param idControleCarga
     * @param idFilial
     * @param tpStatusOperacao
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String tpOperacao){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(CarregamentoDescarga.class.getName() + " cd");
   		hql.addCriteria("cd.controleCarga.id", "=", idControleCarga);
   		hql.addCriteria("cd.filial.id", "=", idFilial);
   		hql.addCriteria("cd.tpStatusOperacao", "=", tpStatusOperacao);
   		hql.addCriteria("cd.tpOperacao", "=", tpOperacao);
   		return (CarregamentoDescarga)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }    
    
    /**
     * Solicitação da Integração - CQPRO00006071
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String tpOperacao ) : CarregamentoDescarga
     * @param idControleCarga
     * @param idFilial
     * @param tpStatusOperacao
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, Object[] tpStatusOperacao, String tpOperacao){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(CarregamentoDescarga.class.getName() + " cd");
   		hql.addCriteria("cd.controleCarga.id", "=", idControleCarga);
   		hql.addCriteria("cd.filial.id", "=", idFilial);
   		hql.addCriteriaIn("cd.tpStatusOperacao", tpStatusOperacao);
   		hql.addCriteria("cd.tpOperacao", "=", tpOperacao);
   		return (CarregamentoDescarga)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }


    /**
     * Solicitação da Integração
     * 
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String condTpStatusOperacao, String tpOperacao ) : CarregamentoDescarga
     * @param idControleCarga
     * @param idFilial
     * @param tpStatusOperacao
     * @param condTpStatusOperacao Parametro que permite buscar por Carregamento Descarga com status da operacao diferente de "C" (Cancelada)
     * @param tpOperacao
     * @return CarregamentoDescarga
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String condTpStatusOperacao, 
    		String tpOperacao){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(CarregamentoDescarga.class.getName() + " cd");
   		hql.addCriteria("cd.controleCarga.id", "=", idControleCarga);
   		hql.addCriteria("cd.filial.id", "=", idFilial);
   		hql.addCriteria("cd.tpStatusOperacao", condTpStatusOperacao, tpStatusOperacao);
   		hql.addCriteria("cd.tpOperacao", "=", tpOperacao);
   		return (CarregamentoDescarga)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }
  
    /**
     * Retorna uma instância do CarregamentoDescarga a partir do idControleCarga, idFilial e tpOperacao
     * @param idControleCarga
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpOperacao){
    	DetachedCriteria dc = DetachedCriteria.forClass(CarregamentoDescarga.class)
		.setFetchMode("filial", FetchMode.JOIN)
		.setFetchMode("box", FetchMode.JOIN)
		.add(Restrictions.eq("filial.id", idFilial))
		.add(Restrictions.eq("controleCarga.id", idControleCarga))
    	.add(Restrictions.eq("tpOperacao", tpOperacao)); 
	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
        
        return (CarregamentoDescarga)criteria.uniqueResult();
    }

    
    public List findCarregamentoDescarga(Long idControleCarga, String tpOperacao){
   	   	DetachedCriteria dc = DetachedCriteria.forClass(CarregamentoDescarga.class)
		.setFetchMode("filial", FetchMode.JOIN)
		.add(Restrictions.eq("controleCarga.id", idControleCarga)) 
   	   	.add(Restrictions.eq("tpOperacao", tpOperacao)); 
	
   	   	return super.findByDetachedCriteria(dc);
    }
    
    /**
     * Verifica se existe CarregamentoDescarga no box passado por parâmetro 
     * 
     * CQPRO00026188
     * 
     * @param idBox
     * @param idFilial
     * @param tpOperacao
     * @return CarregamentoDescarga
     */
    public CarregamentoDescarga findBoxCarregamentoDescarga(Long idBox, Long idFilial, String tpOperacao){
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(CarregamentoDescarga.class, "cd")
    	.add(Restrictions.eq("cd.box.id", idBox))
    	.add(Restrictions.eq("cd.tpOperacao", tpOperacao))
    	.add(Restrictions.eq("cd.filial.id", idFilial));

    	List<CarregamentoDescarga> list = findByDetachedCriteria(dc);
    	if(list != null && !list.isEmpty()){
    		return list.get(0);
    	}    	
    	return null;
    }
        
    public Boolean validateExisteCarregamentoFinalizado(Long idControleCarga, Long idFilial){
   		StringBuilder sql = new StringBuilder()
   		.append("from ")
   		.append(CarregamentoDescarga.class.getName()).append(" cd ")
   		.append("where ")
   		.append("cd.controleCarga.id = ? ")
   		.append("and cd.filial.id = ? ")
   		.append("and cd.tpOperacao = 'C' ")
   		.append("and cd.dhFimOperacao.value is not null ");
   		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga, idFilial});
		return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
    }    
    
    public Boolean validateCarregamentoFinalizado(Long idControleCarga, Long idFilial){
   		StringBuilder sql = new StringBuilder()
   		.append("from ")
   		.append(CarregamentoDescarga.class.getName()).append(" cd ")
   		.append("where ")
   		.append("cd.controleCarga.id = ? ")
   		.append("and cd.filial.id = ? ")
   		.append("and cd.tpOperacao = 'C' ")
   		.append("and cd.tpStatusOperacao = 'F'");
   		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga, idFilial});
		return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
    }
    
    public Boolean validateDescarregamentoFinalizado(Long idControleCarga, Long idFilial){
   		StringBuilder sql = new StringBuilder()
   		.append("from ")
   		.append(CarregamentoDescarga.class.getName()).append(" cd ")
   		.append("where ")
   		.append("cd.controleCarga.id = ? ")
   		.append("and cd.filial.id = ? ")
   		.append("and cd.tpOperacao = 'D' ")
   		.append("and cd.tpStatusOperacao = 'F'");
   		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga, idFilial});
		return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
    }
    
    public Boolean validateDescarregamentoIniciado(Long idControleCarga, Long idFilial){
   		StringBuilder sql = new StringBuilder()
   		.append("from ")
   		.append(CarregamentoDescarga.class.getName()).append(" cd ")
   		.append("where ")
   		.append("cd.controleCarga.id = ? ")
   		.append("and cd.filial.id = ? ")
   		.append("and cd.tpOperacao = 'D' ")
   		.append("and cd.tpStatusOperacao = 'I'");
   		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga, idFilial});
		return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
    }
    
    public Manifesto findManifestoViagem(Long idManifesto, Long idFilial, DateTime dhInicioOperacao, DateTime dhFimOperacao){
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select manifestos from " + Manifesto.class.getName() + " manifestos ");    
    	hql.append(" inner join fetch manifestos.manifestoViagemNacional manifestoViagemNacional ");
    	hql.append(" inner join fetch manifestoViagemNacional.manifestoNacionalVolumes manifestoNacionalVolumes ");
    	hql.append(" inner join fetch manifestoNacionalVolumes.volumeNotaFiscal volumeNotaFiscal ");
    	hql.append(" inner join fetch volumeNotaFiscal.notaFiscalConhecimento notaFiscalConhecimento ");
    	hql.append(" where  manifestos.idManifesto = " + idManifesto);

    	hql.append(" and exists (select eventoVolume from " + EventoVolume.class.getName() + " eventoVolume ");
    	hql.append(" inner join eventoVolume.evento evento ");
    	hql.append(" inner join eventoVolume.volumeNotaFiscal volumeNotaFiscalA ");

    	hql.append(" where  volumeNotaFiscalA.idVolumeNotaFiscal = volumeNotaFiscal.idVolumeNotaFiscal");
    	hql.append(" and evento.cdEvento in (30, 31, 32, 125) ");
    	hql.append(" and eventoVolume.filial.id = " + idFilial);
    	
    	List param = new ArrayList();
    	hql.append(" and eventoVolume.dhEvento.value >= ?" );
    	param.add(dhInicioOperacao);
    	if (dhFimOperacao != null)  {
    		hql.append(" and eventoVolume.dhEvento.value <= ?");
    		param.add(dhFimOperacao);
		}
    	hql.append("  )");
    	List manifestos = getAdsmHibernateTemplate().find(hql.toString(),param.toArray());
    	if (manifestos != null && !manifestos.isEmpty()){
    		return (Manifesto) manifestos.get(0);
    	}
    	return null;
    }
    
    public Manifesto findManifestoEntrega(Long idManifesto, Long idFilial, DateTime dhInicioOperacao, DateTime dhFimOperacao){
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select manifestos from " + Manifesto.class.getName() + " manifestos ");    
    	hql.append(" inner join fetch manifestos.manifestoEntrega manifestoEntrega ");
    	hql.append(" inner join fetch manifestoEntrega.manifestoEntregaVolumes manifestoEntregaVolumes ");
    	hql.append(" inner join fetch manifestoEntregaVolumes.volumeNotaFiscal volumeNotaFiscal ");
    	hql.append(" inner join fetch volumeNotaFiscal.notaFiscalConhecimento notaFiscalConhecimento ");
    	hql.append(" where  manifestos.idManifesto = " + idManifesto);
    	
    	hql.append(" and exists (select eventoVolume from " + EventoVolume.class.getName() + " eventoVolume ");
    	hql.append(" inner join eventoVolume.evento evento ");
    	hql.append(" inner join eventoVolume.volumeNotaFiscal volumeNotaFiscalA ");

    	hql.append(" where  volumeNotaFiscalA.idVolumeNotaFiscal = volumeNotaFiscal.idVolumeNotaFiscal");
    	hql.append(" and evento.cdEvento in (30, 31, 32, 125) ");
    	hql.append(" and eventoVolume.filial.id = " + idFilial);
    	
    	List param = new ArrayList();
    	hql.append(" and eventoVolume.dhEvento.value >= ?" );
    	param.add(dhInicioOperacao);
    	if (dhFimOperacao != null)  {
    		hql.append(" and eventoVolume.dhEvento.value <= ?");
    		param.add(dhFimOperacao);
		}
    	hql.append("  )");
    	List manifestos = getAdsmHibernateTemplate().find(hql.toString(),param.toArray());
    	if (manifestos != null && !manifestos.isEmpty()){
    		return (Manifesto) manifestos.get(0);
    	}
    	return null;
    }
    
    public List<Conhecimento> findManifestosByIdControleCarga(Map<String,Object> criteria){
    	StringBuilder hql = new StringBuilder();
    	if ("D".equals(criteria.get("tpOperacao"))) {
	    	hql.append("select distinct conhecimento from " + this.getPersistentClass().getName() + " carregamentoDescarga ");
	    	
	    	hql.append(" inner join carregamentoDescarga.controleCarga controleCarga ");
	    	hql.append(" inner join controleCarga.manifestos manifestos ");
    	
    		if ("C".equals(criteria.get("tpControleCarga"))) {
		    	hql.append(" inner join  manifestos.manifestoEntrega manifestoEntrega ");
		    	hql.append(" inner join  manifestoEntrega.manifestoEntregaVolumes manifestoEntregaVolumes ");
		    	hql.append(" inner join  manifestoEntregaVolumes.volumeNotaFiscal volumeNotaFiscal ");
    		} else {
    			hql.append(" inner join  manifestos.manifestoViagemNacional manifestoViagemNacional ");
		    	hql.append(" inner join  manifestoViagemNacional.manifestoNacionalVolumes manifestoNacionalVolumes ");
		    	hql.append(" inner join  manifestoNacionalVolumes.volumeNotaFiscal volumeNotaFiscal ");
    		}
    		hql.append(" inner join  volumeNotaFiscal.notaFiscalConhecimento notaFiscalConhecimento ");
    		hql.append(" inner join  notaFiscalConhecimento.conhecimento conhecimento ");
    		hql.append(" where  carregamentoDescarga.idCarregamentoDescarga = " + criteria.get("idCarregamentoDescarga"));
    	} else {
    		hql.append("select distinct conhecimento from " + Conhecimento.class.getName() + " conhecimento ");
    		//hql.append(" inner join  conhecimento.filialOrigem filialOrigem ");
    		hql.append(" inner join  conhecimento.notaFiscalConhecimentos notaFiscalConhecimentos ");
	    	hql.append(" inner join  notaFiscalConhecimentos.volumeNotaFiscais volumeNotaFiscais ");
	    	hql.append(" inner join  volumeNotaFiscais.preManifestoVolumes preManifestoVolumes ");
	    	hql.append(" inner join  preManifestoVolumes.manifesto manifesto ");
	    	hql.append(" inner join  manifesto.controleCarga controleCarga ");
	    	hql.append(" inner join  controleCarga.carregamentoDescargas carregamentoDescargas ");
	    	hql.append(" where  carregamentoDescargas.idCarregamentoDescarga = " + criteria.get("idCarregamentoDescarga"));
    	}

    	List<Conhecimento> conhecimentos = (List<Conhecimento>) getAdsmHibernateTemplate().find(hql.toString());
    	
    	//Ordenacao feita fora do select pois nao eh possivel utilizar order by por filial por causa do distinct
    	Collections.sort(conhecimentos, new Comparator<Conhecimento>() {
			@Override
			public int compare(Conhecimento o1, Conhecimento o2) {
				int valor = o1.getFilialOrigem().getSgFilial().compareTo(o2.getFilialOrigem().getSgFilial());
				if (valor == 0) {
					valor = o1.getNrDoctoServico().compareTo(o2.getNrDoctoServico());
				}
				return valor;
			}
		});

    	return  conhecimentos;
    }
    
    public List findManifestoVolumes(Class classe, Long idDocServico, Map<String, Object> criteria){

    	Long idFilial = (Long) criteria.get("idFilial");
    	DateTime dhInicioOperacao = (DateTime) criteria.get("dhInicioOperacao");
    	DateTime dhFimOperacao = (DateTime) criteria.get("dhFimOperacao");
    	Long idControleCarga = (Long) criteria.get("idControleCarga");
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select manifestoVolume from " + classe.getName() + " manifestoVolume ");    
    	hql.append(" inner join fetch manifestoVolume.volumeNotaFiscal volumeNotaFiscal ");
    	hql.append(" inner join fetch volumeNotaFiscal.notaFiscalConhecimento notaFiscalConhecimento ");
    	
    	
    	List param = new ArrayList();
    	if (classe.getName().equals(PreManifestoVolume.class.getName())) {
    		hql.append(" where  notaFiscalConhecimento.conhecimento.idDoctoServico = " + idDocServico);
    		hql.append(" and  manifestoVolume.manifesto.controleCarga.idControleCarga = " + idControleCarga);
    	} else {
	    	if (classe.getName().equals(ManifestoEntregaVolume.class.getName())) {
	    		hql.append(" where  manifestoVolume.doctoServico.idDoctoServico = " + idDocServico);
	    		hql.append(" and  manifestoVolume.manifestoEntrega.manifesto.controleCarga.idControleCarga = " + idControleCarga);
	    	} else {
	    		hql.append(" where  manifestoVolume.conhecimento.idDoctoServico = " + idDocServico);
	    		hql.append(" and  manifestoVolume.manifestoViagemNacional.manifesto.controleCarga.idControleCarga = " + idControleCarga);
	    	}
	
	    	hql.append(" and exists (select eventoVolume from " + EventoVolume.class.getName() + " eventoVolume ");
	    	hql.append(" inner join eventoVolume.evento evento ");
	    	hql.append(" inner join eventoVolume.volumeNotaFiscal volumeNotaFiscalA ");
	
	    	hql.append(" where  volumeNotaFiscalA.idVolumeNotaFiscal = volumeNotaFiscal.idVolumeNotaFiscal");
	    	hql.append(" and evento.cdEvento in (30, 31, 32, 125) ");
	    	hql.append(" and eventoVolume.filial.id = " + idFilial);
	    	
	    	hql.append(" and eventoVolume.dhEvento.value >= ?" );
	    	param.add(dhInicioOperacao);
	    	if (dhFimOperacao != null)  {
	    		hql.append(" and eventoVolume.dhEvento.value <= ?");
	    		param.add(dhFimOperacao);
			}
	    	hql.append("  )");
    	}
    	List manifestosVolume = getAdsmHibernateTemplate().find(hql.toString(),param.toArray());
    	
    	return manifestosVolume;
    }
    
    public List<NotaFiscalConhecimento> findNotaFiscalConhecimento(Class classe, Long idDocServico, Map<String, Object> criteria){
    	Long idControleCarga = (Long) criteria.get("idControleCarga");
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select notaFiscalConhecimento from " + NotaFiscalConhecimento.class.getName() + " notaFiscalConhecimento ");    
    	hql.append(" where  notaFiscalConhecimento.conhecimento.idDoctoServico = " + idDocServico);
    	if (classe.getName().equals(ManifestoEntregaDocumento.class.getName())) {
    		hql.append(" and exists (select preManifestoDocumento from " + ManifestoEntregaDocumento.class.getName() + " preManifestoDocumento ");
    		hql.append(" inner join preManifestoDocumento.manifestoEntrega.manifesto manifesto ");
	    	hql.append(" where  preManifestoDocumento.doctoServico.idDoctoServico = notaFiscalConhecimento.conhecimento.idDoctoServico");
	    	hql.append(" and manifesto.controleCarga.idControleCarga = " + idControleCarga);
	    	hql.append("  )");
    	} else if (classe.getName().equals(ManifestoNacionalCto.class.getName())) {
    		hql.append(" and exists (select preManifestoDocumento from " + ManifestoNacionalCto.class.getName() + " preManifestoDocumento ");
    		hql.append(" inner join preManifestoDocumento.manifestoViagemNacional.manifesto manifesto ");
	    	hql.append(" where  preManifestoDocumento.conhecimento.idDoctoServico = notaFiscalConhecimento.conhecimento.idDoctoServico");
	    	hql.append(" and manifesto.controleCarga.idControleCarga = " + idControleCarga);
	    	hql.append("  )");
    	} else if (classe.getName().equals(PreManifestoVolume.class.getName())) {
    		hql.append(" and exists (select preManifestoDocumento from " + PreManifestoDocumento.class.getName() + " preManifestoDocumento ");
	    	hql.append(" inner join preManifestoDocumento.manifesto manifesto ");
	    	hql.append(" where  preManifestoDocumento.doctoServico.idDoctoServico = notaFiscalConhecimento.conhecimento.idDoctoServico");
	    	hql.append(" and manifesto.controleCarga.idControleCarga = " + idControleCarga);
	    	hql.append("  )");
    	}

    	List manifestosVolume = getAdsmHibernateTemplate().find(hql.toString());
    	
    	return manifestosVolume;
    }
    
    public ResultSetPage<CarregamentoDescarga> findPaginated(PaginatedQuery paginatedQuery) {
    	Map<String, Object> criteria = paginatedQuery.getCriteria();				
    	StringBuilder hql = this.getHqlPaginated(criteria);
    	if(criteria.get("tpControleCarga") != null)
    		hql.append(" and controleCarga.tpControleCarga = :tpControleCarga ");
    	if(criteria.get("tpOperacao") != null)
    		hql.append(" and carregamentoDescarga.tpOperacao = :tpOperacao ");
    	if(criteria.get("idFilial") != null)
    		hql.append(" and filial.id = :idFilial ");
    	
    	if ("V".equals(criteria.get("tpControleCarga"))) {
			hql.append(" order by box.nrBox, controleCarga.filialByIdFilialDestino.sgFilial");
		} else {
			hql.append(" order by box.nrBox, controleCarga.rotaColetaEntrega.nrRota");
    }  
    	return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
    }  
        	
    public StringBuilder getHqlPaginated(Map<String,Object> criteria) {
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" from " + this.getPersistentClass().getName() + " carregamentoDescarga ");    
    	hql.append(" inner join fetch carregamentoDescarga.filial filial ");
    	/* CONTROLE CARGA */
    	hql.append(" inner join fetch carregamentoDescarga.controleCarga controleCarga ");    	
    	hql.append(" inner join fetch controleCarga.filialByIdFilialOrigem filialOrigem ");   
    	hql.append(" left join fetch controleCarga.meioTransporteByIdTransportado meioTransporte ");    		    	
    	hql.append(" left join fetch controleCarga.meioTransporteByIdSemiRebocado meioTransporteSemiReboque ");    
    	hql.append(" left join fetch controleCarga.manifestos manifestos ");
    	
    	/* Relacionamento one-to-one, sem este fetch estava fazendo vários selects by id para preencher estes campos */
    	hql.append(" left join fetch meioTransporte.meioTransporteRodoviario meioTransporteRodoviario ");
    	hql.append(" left join fetch meioTransporte.modeloMeioTransporte modeloMeioTransporte ");
    	hql.append(" left join fetch meioTransporteSemiReboque.meioTransporteRodoviario meioTransporteRodoviarioSemiReboque ");    	
    	hql.append(" left join fetch meioTransporteSemiReboque.modeloMeioTransporte modeloMeioTransporte ");
    	/* BOX */
    	hql.append(" left join fetch carregamentoDescarga.box box ");
    	hql.append(" left join fetch box.doca doca ");
    	hql.append(" left join fetch doca.terminal terminal ");
    	hql.append(" left join fetch terminal.pessoa pessoa ");
    	hql.append(" where 1=1 ");
    	
    	hql.append(" and carregamentoDescarga.tpStatusOperacao in ('I','E','O','P') ");    	
    	
    	return hql;
    }    
    
    public List<Object[]> findManifestosParaSOM(Long idMonitoramentoDescarga){
    	String sql = "select distinct F_INT_DE_PARA('FILIAL_SIGLA', ds.id_filial_origem, 3) as SIGLA, "
			+ " lpad(mvn.nr_manifesto_origem, 6, '0') as NR_MANIFESTO, "
			+ " f_int_calc_dig_man(F_INT_DE_PARA('FILIAL_SIGLA', ds.id_filial_origem, 3), mvn.nr_manifesto_origem) as DIGITO_VERIFICADOR " 
			+ " from volume_nota_fiscal vnf, "
			+ "            nota_fiscal_conhecimento nfc, "
			+ "            docto_servico ds, "
			+ "            monitoramento_descarga m, "
			+ "            manifesto_nacional_cto mnc, "
			+ "            manifesto_viagem_nacional mvn "
			+ " where vnf.id_nota_fiscal_conhecimento  = nfc.id_nota_fiscal_conhecimento "
			+ " and nfc.id_conhecimento              = ds.id_docto_servico "
			+ " and m.id_monitoramento_descarga      = vnf.id_monitoramento_descarga "
			+ " and mnc.id_conhecimento              = ds.id_docto_servico "
			+ " and mnc.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional "
			+ " and mvn.id_filial                    = ds.id_filial_origem "
			+ " and m.id_monitoramento_descarga      =  :idMonitoramentoDescarga";
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
    	
    	ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("SIGLA", Hibernate.STRING);
				sqlQuery.addScalar("NR_MANIFESTO", Hibernate.STRING);
				sqlQuery.addScalar("DIGITO_VERIFICADOR", Hibernate.INTEGER);
			}
		};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql, Integer.valueOf(1), Integer.valueOf(10000), parameters, configureSqlQuery).getList();
    }
    
    /**
     * LMS - 4460
     * Verificar qual processo está sendo feito e pegar o último, conforme data de Finalização
     * @param idControleCarga
     * @return
     */
    public CarregamentoDescarga findDadosFechamentoOcorrenciaRNCs (Long idControleCarga) {
    
		List<Object> params = new ArrayList<Object>();
    	params.add(idControleCarga);
    	params.add("F");
    	params.add(SessionUtils.getFilialSessao().getIdFilial());

    	StringBuilder sql = new StringBuilder()
		.append("FROM ").append(this.getPersistentClass().getName()).append(" as carregamentoDescarga ")
		.append("WHERE carregamentoDescarga.controleCarga.idControleCarga = ? ")
    	.append("AND carregamentoDescarga.tpStatusOperacao = ? ")
    	.append("AND carregamentoDescarga.filial.idFilial = ? ")
    	.append("ORDER BY dhFimOperacao desc");
		
    	List<CarregamentoDescarga> result = (List<CarregamentoDescarga>) getAdsmHibernateTemplate().find(sql.toString(), params.toArray());

    	// Retorna o primeiro registro da lista
    	return result == null ? null : result.get(0);
    }
    
    /***
	 * LMS-5261 -> Rotina finalizarCarregDoctoManifestadoService - E.T 05.01.02.04 Informar fim de Carregamento
	 * Rotina chamada no processo de aprovação/reprovação do workflow
	 * @param idProcesso          - Identificador do processo 
	 * @param tpSituacaoAprovacao - Tipo de sitação (Aprovada, reprovada, cancelada)
	 */
	public void storeFinalizarCarregDoctoManifestadoService(Long idProcesso, String tpSituacaoAprovacao){
		List parameters = new ArrayList();
		StringBuilder sql = new StringBuilder()
    	.append("update ")
    	.append(CarregamentoDescarga.class.getName() + " as cd ")
    	.append(" set cd.tpStatusWorkflow = ? ")
		.append("where pendencia.idPendencia = (select cd.pendencia.idPendencia from ")
		.append(CarregamentoDescarga.class.getName() + " as cd ")
		.append("join cd.pendencia as p ")
		.append("where p.idProcesso = ? )");		
		
		parameters.add(tpSituacaoAprovacao);
		parameters.add(idProcesso);
		
		executeHql(sql.toString(), parameters);
	}

    public CarregamentoDescarga findByControleCargaAndFilialRomaneio(ControleCarga controleCarga, Filial filialRomaneio) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("cd");
    	sql.addFrom(this.getPersistentClass().getName() + " cd " +
    			"join cd.controleCarga cc " +
    			"join cc.manifestos  m ");
    	sql.addCustomCriteria("cd.tpOperacao = 'D'");
    	sql.addCustomCriteria("cc = :controleCarga");
    	sql.addCustomCriteria("cd.filial = :filial");
    	
    	Map<String,Object> params = new HashMap<String, Object>();
    	params.put("controleCarga", controleCarga);
    	params.put("filial", filialRomaneio);
    	
    	List<CarregamentoDescarga> l = (List<CarregamentoDescarga>) getAdsmHibernateTemplate().findByNamedParam(sql.getSql(), params);;
    	
    	if (l != null && l.size() > 0) {
    	    return l.get(0);
    	}
    	
    	return null;
    } 
}