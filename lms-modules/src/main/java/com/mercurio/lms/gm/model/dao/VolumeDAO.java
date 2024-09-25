package com.mercurio.lms.gm.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoVolume;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean 
 */
public class VolumeDAO extends BaseCrudDao<Volume, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Volume.class;
	}

	/**
	 * Busca Volume pelo codigo do volume.
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume.
	 * @return Volume, nulo, caso negativo.
	 */
	public Volume findVolumeByCodigoVolume(String codigoVolume){
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		dc.createAlias("v.carregamento", "c");
		dc.add(Restrictions.eq("v.codigoVolume",codigoVolume.toLowerCase()).ignoreCase());
		List<Volume> listVolume = findByDetachedCriteria(dc);
		if(listVolume!=null && listVolume.size()>0){
			return listVolume.get(0);
		}else{
			return null;
		}
	}
	
	
	public List<Map> findListaHistoricoVolumeByVolume(Map param){
		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map(hv.idHistoricoVolume as idHistoricoVolume, " +
				"hv.idCarregamento as idCarregamento, " +
				"hv.idVolume as idVolume, " +
				"hv.codigoVolume as codigoVolume, " +
				"hv.codigoStatus as DescricaoCodigoStatus, " +
				"hv.matriculaResponsavel as matriculaResponsavel, " +
				"(SELECT u.nrMatricula FROM " + Usuario.class.getName() + " u WHERE u.idUsuario = hv.matriculaResponsavel) as usuarioMatriculaResponsavel, " + 
				"hv.autorizador as autorizadorClnt, " +
				"TO_CHAR(hv.dataHistorico.value, 'DD/MM/YYYY HH24:MI') as dataHistorico, " +
				"hv.idRejeitoMpc as idRejeitoMpc, " +
				"TO_CHAR(cc.dataDisponivel.value, 'DD/MM/YYYY HH24:MI') as dataDisponivel," +
				"ca.placaVeiculo as placaVeiculo )"
				);

	
		sql.addFrom(HistoricoVolume.class.getName() + " hv");
		
		sql.addFrom(Carregamento.class.getName() + " ca");	
		sql.addFrom(CabecalhoCarregamento.class.getName() + " cc");	
		sql.addFrom(DetalheCarregamento.class.getName() + " dc");
		
		sql.addFrom(DomainValue.class.getName()  + " dv join dv.domain as d ");
    	sql.addJoin("hv.codigoStatus","dv.value"); 

		sql.addCustomCriteria("hv.idCarregamento = ca.idCarregamento");	 
		sql.addCustomCriteria("dc.codigoVolume = hv.codigoVolume");	
		sql.addCustomCriteria("cc.idCabecalhoCarregamento = dc.idCabecalhoCarregamento");
		
		sql.addCriteria("dc.codigoVolume", "=", param.get("codigoVolume"));
		sql.addCriteria("hv.codigoVolume", "=", param.get("codigoVolume") );
		
		sql.addCriteria("d.name","=","DM_STATUS_VOLUME");
		sql.addCustomCriteria("dv.value = to_char(hv.codigoStatus)");
		
		sql.addOrderBy("hv.dataHistorico");
		sql.addOrderBy("ca.placaVeiculo");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description",LocaleContextHolder.getLocale()));
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
	}
	
	
	/**
	 * Busca Volume pelo idCarregamento.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            idCarregamento.
	 * @return Volume, nulo, caso negativo.
	 */
	public List<Volume> findVolumeByIdCarregamento(Long idCarregamento){
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		dc.createAlias("v.carregamento", "c");
		dc.add(Restrictions.eq("c.idCarregamento",idCarregamento));
		List<Volume> listVolume = findByDetachedCriteria(dc);
		if(listVolume!=null && listVolume.size()>0){
			return listVolume;
		}else{
			return null;
		}
	}
	
	/**
	 * Busca Volume carregados para um Carregamento especifico idCarregamento.
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            idCarregamento.
	 * @return Volume, nulo, caso negativo.
	 */		
	public List<Volume> findVolumesCarregadosByIdCarregamento(Long idCarregamento){
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		dc.createAlias("v.carregamento", "c");
		dc.add(Restrictions.eq("c.idCarregamento",idCarregamento));
		dc.add(Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.eq("v.codigoStatus", new DomainValue("1")), Restrictions.eq("v.codigoStatus", new DomainValue("2"))), Restrictions.eq("v.codigoStatus", new DomainValue("3"))), Restrictions.eq("v.codigoStatus", new DomainValue("7"))));
		List<Volume> listVolume = findByDetachedCriteria(dc);
		if(listVolume!=null && listVolume.size()>0){
			return listVolume;
		}else{
			return null;
		}
	}
	
	public List<Volume> findVolumesMonitoramentoDescarga(final String codigoBarras) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select v from ");
		hql.append(Volume.class.getName());
		hql.append(" v join v.carregamento c where c.tipoCarregamento = 'D' and v.codigoStatus = 6 ");
		hql.append(" v.codigoVolume in ( ");
		hql.append(" select vnf1.nrVolumeColeta from ");
		hql.append(VolumeNotaFiscal.class.getName());
		hql.append(" vnf1 join vnf1.notaFiscalConhecimento nfc1 where nfc1.conhecimento.id = ( ");
		hql.append(" select nfc.conhecimento.id from ");
		hql.append(VolumeNotaFiscal.class.getName());
		hql.append(" vnf join vnf.notaFiscalConhecimento nfc where vnf.nrVolumeEmbarque = ? ");
		hql.append(" or (vnf.nrVolumeColeta = ? and vnf.tpVolume in ('M', 'U')))) ");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[] { codigoBarras, codigoBarras });
	}

	public void storeVolume(Volume volume) {
		store(volume);
	}
	
	/**
	 * Método utilizado para realizar o update das informações do volume,
	 * relativo a demanda LMS-2788. Quando há a rejeição de um mapa de
	 * carregamento é necessário atualizar as coluna 'CODIGO_STATUS' para
	 * '4'(descarregado) e a coluna 'MATRICULA_RESPONSAVEL' para o 'Login do
	 * autorizador'
	 * 
	 * @param params
	 * @param isVolume
	 *            - um boolean que deve ser TRUE quando a rejeição é feita por
	 *            VOLUME e não por MAPA.
	 */
	public void updateStatusMatriculaDeVolumes(Map<String,Object> params, boolean isVolume){
		final StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE VOLUME SET CODIGO_STATUS = 4, MATRICULA_RESPONSAVEL = :matriculaResponsavel " + "WHERE MAPA_CARREGAMENTO = :mpc " + "AND ID_CARREGAMENTO = :idCarregamento ");
		
		if (isVolume) {
			sql.append("AND ID_VOLUME = :idVolume ");
		}
		
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(), params));

    }
	
	/**
	 * Método criado para atender a demanda LMS-2788.
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	private HibernateCallback getHibernateCallback(final String sql, final Map<String,Object> params){
		return new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				for( String key : params.keySet() ){
					query.setParameter(key, params.get(key));
				}
				
				query.executeUpdate();
				return null;
			}
		};
    }
	
	/**
	 * Método criado para atender a demanda LMS-2788. Este método verifica se
	 * existe volumes que atendam as condições informadas em 'idCarregamento' e
	 * 'status' e retorna true caso exista o carregamento ou false caso
	 * contrário.
	 * 
	 * @param idCarregamento
	 *            - código do carregamento
	 * @param status
	 *            - conjunto de códigos de status do volume.
	 * @return
	 */
	public List<Volume> findVolumesByCarregamentoEStatus(Long idCarregamento, String[] status) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		
		dc.createAlias("v.carregamento", "c");
		dc.add(Restrictions.eq("c.idCarregamento",idCarregamento));
		dc.add(Restrictions.in("v.codigoStatus", status));
		
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Retorna total de volumes do MpC carregados no veículo, caso o id do mesmo seja informado,
	 * onde o status do volume esteja no array de status passado como parametro 
	 *  <br>
	 *  Caso o idCarregamento não seja informado retorna o total de volumes do MpC
	 * 	onde o status do volume esteja no array de status passado como parametro 
	 * 
	 * @param idCarregamento
	 * @param mapaCarregamento
	 * @param status
	 * @return
	 */
	public Integer getRowCountVolumesMpcByIdCarregamentoStatusVolume(Long idCarregamento, Long mapaCarregamento, String[] status) {
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		dc.createAlias("v.carregamento", "c");
		if(idCarregamento != null) {
			dc.add(Restrictions.eq("c.idCarregamento",idCarregamento));
		}
		dc.add(Restrictions.eq("v.mapaCarregamento",mapaCarregamento));
		dc.add(Restrictions.in("v.codigoStatus", status));
		dc.setProjection(Projections.rowCount());
		
		
		Object ret = getAdsmHibernateTemplate().findUniqueResult(dc);
        return (ret != null ? (Integer) ret : null);
	}
	
	/**
	 * Carrega os dados para a listagem da tela de consultar MpC
	 * 	LMS-2790
	 * @param param
	 * @return
	 */
	public ResultSetPage findConsultaMpc(Map param) {
		List queryParams = new ArrayList();
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT 	dc.codigo_volume AS codigoVolume, \n")
				.append("	case when v.nm_status is null \n") 
				.append("		then 'Falta' \n") 
				.append("	else v.nm_status end AS nomeStatus, \n")
				.append("	v.placa_veiculo AS placaVeiculo \n")
				.append("	FROM detalhe_carregamento dc \n")
				.append(" 	LEFT JOIN (SELECT v1.codigo_volume AS codigo_volume, \n")
				.append("				(SELECT VI18N(vd.ds_valor_dominio_i,'pt_BR') \n")
				.append("					from valor_dominio vd, dominio d  \n")
				.append("						where vd.id_dominio = d.id_dominio   \n")
				.append("							and d.nm_dominio='DM_STATUS_VOLUME'    \n")
				.append("							and vd.vl_valor_dominio = v1.codigo_status) as nm_status, \n")
				.append("							v1.codigo_status as codigo_status, \n")
				.append("					CASE WHEN v1.codigo_status IN (1, 2, 3, 6, 7)   \n")
				.append("							THEN ca.placa_veiculo")
				.append("							ELSE NULL ")
				.append("							END as placa_veiculo ")
				.append("				FROM volume v1, carregamento ca")
				.append("					WHERE ca.id_carregamento = v1.id_carregamento) v ").append("\n")
				.append("	on v.codigo_volume = dc.codigo_volume \n");
				
		if (param.containsKey("mapaCarregamento")) {
			sb.append("WHERE dc.mapa_carregamento = ? \n");
			queryParams.add(param.get("mapaCarregamento"));
		}

		if (param.containsKey("idCabecalhoCarregamento")) {
			sb.append("WHERE dc.id_cabecalho_carregamento = ? \n");
			queryParams.add(param.get("idCabecalhoCarregamento"));
		}

		/**
		 * Ordenação:
		 * status “FALTA”,
		 * status “Descarregado” (código=4),
		 * status “Avaria” (código=5),
		 * *Placa do veículo {
		 * 		status “Conferido” (código=7),
		 * 		status “Carregado” (código=1),
		 *		status “Avaria/Carregado” (código=2),
		 *}
		 *status “Expedido” (código=6) + código de volume
		 *
		 *Se a tela chamadora for a tela Carregar Volume (45.46.02.01 Carregar Volume)
		 *Então
		 *
		 **Para o status 7 mostrar em primeiro lugar o o veículo em questão (CARREGAMENTO.placa_veiculo onde CARREGAMENTO.id_carregamento = (ET:45.46.02.01).IDcampo[1] ), após mostrar os veículos por ordem alfabética do campo CARREGAMENTO.placa_veiculo;
		 **Para o status 1 mostrar os veículos em ordem alfabética (CARREGAMENTO.placa_veiculo);
		 **Para o status 2 mostrar os veículos em ordem alfabética (CARREGAMENTO.placa_veiculo);
		 **Para o status 6 mostrar os veículos em ordem alfabética (CARREGAMENTO.placa_veiculo).
		 *
		 *SeNão
		 *         *Apresentar na ordem de status e após placa conforme segue:
		 *
		 *      Status 7 + placa (CARREGAMENTO.placa_veiculo) > ordem alfabética;
		 *      Status 1 + placa (CARREGAMENTO.placa_veiculo) > ordem alfabética;
		 *      Status 2 + placa (CARREGAMENTO.placa_veiculo) > ordem alfabética;
		 *      Status 6 + placa (CARREGAMENTO.placa_veiculo) > ordem alfabética;
		 * Fim SeNão
		 */
		
		sb.append("ORDER BY CASE WHEN v.codigo_status is null THEN 0 \n")
				.append("			WHEN v.codigo_status = 4 THEN 1 \n")
				.append("			WHEN v.codigo_status = 5 THEN 2 ELSE 9 END, \n");
		
		if (param.containsKey("placaVeiculo")) {
			sb.append("		CASE WHEN v.codigo_status = 7 and v.placa_veiculo = ? THEN 0 \n")
				.append("			WHEN v.codigo_status = 7 THEN 1 \n")
				.append("			WHEN v.codigo_status = 1 THEN 2 \n")
				.append("			WHEN v.codigo_status = 2 THEN 3 ELSE 9 END, \n");
			
			queryParams.add(param.get("placaVeiculo"));
		} else {
			sb.append("		CASE WHEN v.codigo_status = 7 THEN 0 \n")
				.append("			WHEN v.codigo_status = 1 THEN 1 \n")
				.append("			WHEN v.codigo_status = 2 THEN 2 ELSE 9 END, \n");	
		}		
		
		sb.append("		CASE WHEN v.codigo_status = 6 THEN 0 ELSE 9 END, \n")
			.append("			NVL(v.placa_veiculo, 'ZZZ-ZZZZ'), \n")
			.append("		dc.codigo_volume \n");
				
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("codigoVolume", Hibernate.STRING);
				sqlQuery.addScalar("nomeStatus", Hibernate.STRING);
				sqlQuery.addScalar("placaVeiculo", Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findPaginatedBySql(sb.toString(), 1, 100000, queryParams.toArray(), configureSqlQuery);		
	}
	
	/**
	 * Retorna o primeiro volume conferido(codigoStatus = 7) que faz parte do mapa carregamento e do carregamento onde o usuário
	 * responsável seja diferente do passado como parametro.
	 *  
	 * @param idCarregamento
	 * @param mpcDaConferencia
	 * @param matricula
	 * @return
	 */
	public Volume findVolumeMpcConferidoByIdCarregamentoEResponsavelComMatriculaDiferente(Long idCarregamento, Long mapaCarregamento, Long matricula) {
		DetachedCriteria dc = DetachedCriteria.forClass(Volume.class, "v");
		
		dc.createAlias("v.carregamento", "c");
		dc.add(Restrictions.eq("c.idCarregamento", idCarregamento));		
		dc.add(Restrictions.eq("v.mapaCarregamento", mapaCarregamento));
		dc.add(Restrictions.eq("v.codigoStatus", "7"));
		dc.add(Restrictions.ne("v.matriculaResponsavel", matricula));
		
		List<Volume> listVolume = findByDetachedCriteria(dc);
		//Se existe algum volume retorna o primeiro
		if(listVolume != null &&  !listVolume.isEmpty()) {
			return listVolume.get(0);
		}
		
        return null;
	}
}