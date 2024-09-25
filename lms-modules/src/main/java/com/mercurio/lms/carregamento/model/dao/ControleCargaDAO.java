package com.mercurio.lms.carregamento.model.dao;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.contratacaoveiculos.model.EventoPuxada;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.CampoComplementar;
import com.mercurio.lms.vendas.model.ValorCampoComplementar;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleCargaDAO extends BaseCrudDao<ControleCarga, Long> {
	private JdbcTemplate jdbcTemplate;
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */

	protected final Class getPersistentClass() {
		return ControleCarga.class;
	}

	protected void initFindByIdLazyProperties(Map map) {
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		map.put("filialByIdFilialDestino", FetchMode.JOIN);
		map.put("filialByIdFilialAtualizaStatus", FetchMode.JOIN);
		map.put("moeda", FetchMode.JOIN);
		map.put("meioTransporteByIdTransportado", FetchMode.JOIN);
		map.put("meioTransporteByIdTransportado.meioTransporteRodoviario", FetchMode.JOIN);
		map.put("meioTransporteByIdTransportado.modeloMeioTransporte", FetchMode.JOIN);
		map.put("meioTransporteByIdTransportado.modeloMeioTransporte.tipoMeioTransporte", FetchMode.JOIN);
		map.put("manifestoColetas", FetchMode.SELECT);
		map.put("motorista", FetchMode.JOIN);
		map.put("motorista.pessoa", FetchMode.JOIN);
		map.put("meioTransporteByIdSemiRebocado", FetchMode.JOIN);
		map.put("solicitacaoContratacao", FetchMode.JOIN);
		map.put("proprietario", FetchMode.JOIN);
		map.put("proprietario.pessoa", FetchMode.JOIN);
		map.put("tabelaColetaEntrega", FetchMode.JOIN);
		map.put("tipoTabelaColetaEntrega", FetchMode.JOIN);
		map.put("rotaColetaEntrega", FetchMode.JOIN);
		map.put("rotaIdaVolta", FetchMode.JOIN);
		map.put("rotaIdaVolta.rota", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map map) {
		map.put("meioTransporteByIdTransportado", FetchMode.JOIN);
		map.put("meioTransporteByIdSemiRebocado", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("filialByIdFilialDestino", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		map.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		map.put("motorista", FetchMode.JOIN);
		map.put("motorista.pessoa", FetchMode.JOIN);
		map.put("rotaIdaVolta", FetchMode.JOIN);
		map.put("rotaIdaVolta.rota", FetchMode.JOIN);
		map.put("proprietario", FetchMode.JOIN);
		map.put("proprietario.pessoa", FetchMode.JOIN);
	}

	
	@Override
	protected void initFindListLazyProperties(Map map) {
		map.put("meioTransporteByIdTransportado", FetchMode.JOIN);
		map.put("meioTransporteByIdSemiRebocado", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("filialByIdFilialDestino", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		map.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		map.put("motorista", FetchMode.JOIN);
		map.put("motorista.pessoa", FetchMode.JOIN);
		map.put("rotaIdaVolta", FetchMode.JOIN);
		map.put("rotaIdaVolta.rota", FetchMode.JOIN);
		map.put("rotaColetaEntrega", FetchMode.JOIN);
	}
	
	public List<ControleCarga> findControleCargaByNotaCredito(Long idNotaCredito) {
		String from = ControleCarga.class.getName() + " as cc " +
		"inner join fetch cc.notasCredito as nc ";

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from);
		
		sql.addCriteria("nc.id", "=", idNotaCredito);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	public List findControleCargaByProprietario(Long idProprietario) {
		String from = ControleCarga.class.getName() + " as cc ";

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from);
		
		sql.addCriteria("cc.proprietario.id", "=", idProprietario);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	@SuppressWarnings("unchecked")
	public List<ControleCarga> findControlesCargaProprietarioNoPeriodo(ControleCarga controleCarga) {
	    DetachedCriteria criteria = DetachedCriteria.forClass(ControleCarga.class);
		criteria.add(Restrictions.eq("meioTransporteByIdTransportado.id",
		        controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte()));
		criteria.add(Restrictions.eq("proprietario.id", controleCarga.getProprietario().getIdProprietario()));
		criteria.add(Restrictions.eq("filialByIdFilialOrigem.id", controleCarga.getFilialByIdFilialOrigem().getIdFilial()));
		criteria.add(Restrictions.eq("tpControleCarga", "C"));
        criteria.add(Restrictions.ge("dhSaidaColetaEntrega", JTDateTimeUtils.getFirstHourOfDay(controleCarga.getDhSaidaColetaEntrega())));
        criteria.add(Restrictions.le("dhSaidaColetaEntrega", JTDateTimeUtils.getLastHourOfDay(controleCarga.getDhSaidaColetaEntrega())));
        criteria.add(Restrictions.ne("idControleCarga", controleCarga.getIdControleCarga()));
        criteria.add(Restrictions.ne("tpStatusControleCarga", "CA"));
        criteria.add(Restrictions.isNotNull("dhSaidaColetaEntrega"));
        criteria.add(Restrictions.isNotNull("dhChegadaColetaEntrega"));

        return findByDetachedCriteria(criteria);
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param tpControleCarga
	 * @param idTransportado
	 * @return
	 */
	public List findControleCargaByTransmitirColeta(Long idControleCarga, String tpControleCarga, Long idTransportado) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc")
			.setFetchMode("meioTransporteByIdTransportado", FetchMode.JOIN)
			.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN)
			.add(Restrictions.eq("tpControleCarga", tpControleCarga))
			.add(Restrictions.eq("meioTransporteByIdTransportado.id", idTransportado))
			.add(Restrictions.or( 
					Restrictions.or( Restrictions.eq("tpStatusControleCarga", "AE"), Restrictions.eq("tpStatusControleCarga", "TC")),
					Restrictions.eq("tpStatusControleCarga", "AD")
				));
		
		if (idControleCarga != null) {
			dc.add(Restrictions.eq("cc.id", idControleCarga));
		}
		List result = super.findByDetachedCriteria(dc);
		return result;
	}
	
	public List findControleCargaByNrControleByFilial(Long nrControleCarga, Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial", idFilial));
		dc.add(Restrictions.eq("nrControleCarga", nrControleCarga));
		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);		
		dc.setFetchMode("rotaIdaVolta.rota", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		dc.setFetchMode("meioTransporteByIdTransportado", FetchMode.JOIN);
		dc.setFetchMode("meioTransporteByIdSemiRebocado", FetchMode.JOIN);		
		return findByDetachedCriteria(dc); 			
	}
	
	public ControleCarga findControleCargaByNrControleCargaByIdFilial(Long nrControleCarga, Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial", idFilial));
		dc.add(Restrictions.eq("nrControleCarga", nrControleCarga));
		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);		
		dc.setFetchMode("rotaIdaVolta.rota", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		dc.setFetchMode("meioTransporteByIdTransportado", FetchMode.JOIN);
		dc.setFetchMode("meioTransporteByIdSemiRebocado", FetchMode.JOIN);
		dc.setFetchMode("proprietario", FetchMode.JOIN);
		dc.setFetchMode("proprietario.pessoa",FetchMode.JOIN);
		return (ControleCarga)getAdsmHibernateTemplate().findUniqueResult(dc); 			
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateExigenciaCIOTEnviadoIntegCIOT(Long idControleCarga, Boolean blExigeCIOT, Boolean blEnviadoIntegCIOT){
		List param = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("UPDATE ");
		hql.append(getPersistentClass().getName());
		hql.append(" cc ");
		hql.append("SET ");
		
		if(blExigeCIOT != null){
			hql.append(" cc.blExigeCIOT = ? ");
			param.add(blExigeCIOT);
		}
		if(blExigeCIOT != null && blEnviadoIntegCIOT != null){
			hql.append(" , ");
		}
		if(blEnviadoIntegCIOT != null){
			hql.append(" cc.blEnviadoIntegCIOT = ? ");
			param.add(blEnviadoIntegCIOT);
		}
		
		hql.append(" WHERE cc.idControleCarga = ? ");
		param.add(idControleCarga);

		executeHql(hql.toString(), param);
	}

	public List findDocumentosControleCargaInternacional(Long idControleCarga){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("ci.idDoctoServico");
		
		sql.addInnerJoin("ControleCarga", "c"); 
		sql.addInnerJoin("c.manifestos", "m");
		sql.addInnerJoin("m.manifestoInternacional", "mi");
		sql.addInnerJoin("mi.manifestoInternacCtos", "mic");
		sql.addInnerJoin("mic.ctoInternacional", "ci");
									
		sql.addCriteria("c.idControleCarga", "=", idControleCarga);
									
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	public List findDocumentosControleCargaNacional(Long idControleCarga){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("cn.idDoctoServico");
		
		sql.addInnerJoin(getPersistentClass().getName(), "c");
		sql.addInnerJoin("c.manifestos", "m");
		sql.addInnerJoin("m.manifestoViagemNacional", "mfn");
		sql.addInnerJoin("mfn.manifestoNacionalCtos", "mnc");
		sql.addInnerJoin("mnc.conhecimento", "cn");
				
		sql.addCriteria("c.idControleCarga", "=", idControleCarga);
		
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}
	
	public List findDocumentosControleCargaEntrega(Long idControleCarga){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("d.idDoctoServico");
		
		sql.addInnerJoin(getPersistentClass().getName(), "c");
		sql.addInnerJoin("c.manifestos", "m");
		sql.addInnerJoin("m.manifestoEntrega", "mfe");
		sql.addInnerJoin("mfe.manifestoEntregaDocumentos", "med");
		sql.addInnerJoin("med.doctoServico", "d");
				
		sql.addCriteria("c.idControleCarga", "=", idControleCarga);
		
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	/**
	 * Valida se existe uma nota de cr�dito para o controle de carga informado.
	 * 
	 * @author Felipe Ferreira
	 * @param idControleCarga
	 * @return <b>true</b> se encontrar uma nota de cr�dito para o CC informado. 
	 */
	public boolean validateExisteNotaCreditoInControleCarga(Long idControleCarga) {
		StringBuilder hql = new StringBuilder()
			.append("select count(*) ")
			.append(" from " + getPersistentClass().getName() + " CC ")
		.append(" inner join CC.notasCredito NC ")
			.append("where CC.id = ? ")
			.append(" and NC.dhEmissao.value is NOT NULL ");

		Long result = (Long) getAdsmHibernateTemplate().find(hql.toString(), idControleCarga).get(0);
		return (result.intValue() > 0);
	}

	/**
	 * Consulta dados relativos ao controle de carga para as telas de chegada e saida da portaria
	 * @param idControleCarga
	 * @return
	 */
	public List findDadosChegadaSaida(Long idControleCarga, String tpControleCarga){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(cc.idControleCarga", "idControleCarga");
		sql.addProjection("cc.tpControleCarga", "tpControleCarga");
		sql.addProjection("cc.nrControleCarga", "nrControleCarga");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("p.nrIdentificacao", "nrIdentificacao");
		sql.addProjection("p.tpIdentificacao", "tpIdentificacao");
		sql.addProjection("p.nmPessoa", "nmPessoa");
		sql.addProjection("mt.idMeioTransporte", "idMeioTransporte");
		sql.addProjection("mt.nrIdentificador", "nrIdentificadorTransportado");
		sql.addProjection("mt.nrFrota", "nrFrotaTransportado");
		sql.addProjection("tmt.dsTipoMeioTransporte", "dsTipoMeioTransporte");

		if (tpControleCarga.equals("V")){
			sql.addProjection("r.dsRota", "dsRota");
			sql.addProjection("riv.nrRota", "nrRotaIdaVolta");
		} else {
			sql.addProjection("rce.nrRota", "nrRota");
			sql.addProjection("rce.dsRota", "dsRota");	
		}

		sql.addProjection("rot.dsRota", "dsRotaControleCarga");

		sql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");		
		sql.addProjection("reb.nrFrota", "nrFrotaReboque");		
		sql.addProjection("reb.idMeioTransporte", "idReboque)");

		sql.addInnerJoin(getPersistentClass().getName(), "cc");
		sql.addInnerJoin("cc.filialByIdFilialOrigem", "f");
		sql.addInnerJoin("cc.meioTransporteByIdTransportado", "mt");
		sql.addInnerJoin("mt.modeloMeioTransporte", "mdl");
		sql.addInnerJoin("mdl.tipoMeioTransporte", "tmt");		
		sql.addLeftOuterJoin("cc.motorista", "m"); 
		sql.addLeftOuterJoin("m.pessoa", "p");
		sql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb");

		sql.addLeftOuterJoin("cc.rota", "rot");

		if (tpControleCarga.equals("V")){
			sql.addLeftOuterJoin("cc.rotaIdaVolta", "riv");
			sql.addLeftOuterJoin("riv.rota", "r");
		} else {
			sql.addLeftOuterJoin("cc.rotaColetaEntrega", "rce");
		}
		
		sql.addCriteria("cc.idControleCarga", "=", idControleCarga);

		List retorno = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		return retorno;
		
	}
	
	public ControleCarga findByIdCarregamentoDescarga(Long idCarregamentoDescarga){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc")
			.setFetchMode("carregamentoDescargas", FetchMode.JOIN)
			.createAlias("carregamentoDescargas", "cd")
			.add(Restrictions.eq("cd.idCarregamentoDescarga", idCarregamentoDescarga));
		List result = super.findByDetachedCriteria(dc);
		if(!result.isEmpty())
			return (ControleCarga) result.get(0);
		return null;
	}
	
	public List findControleCargaByManifesto(Long idManifesto, Long idManifestoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "pc")
			.setFetchMode("meioTransporteByIdTransportado", FetchMode.JOIN)
			.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN)
			.createAlias("filialByIdFilialOrigem", "filialByIdFilialOrigem");

		if (idManifesto != null) {
			dc.setFetchMode("manifestos", FetchMode.SELECT)
			.createAlias("manifestos", "manifesto")
			.add(Restrictions.eq("manifesto.id", idManifesto));
		}
		else
		if (idManifestoColeta != null) {
			dc.setFetchMode("manifestoColetas", FetchMode.SELECT)
			.createAlias("manifestoColetas", "manifestoColeta")
			.add(Restrictions.eq("manifestoColeta.id", idManifestoColeta));
		}

		dc.add(Restrictions.eq("tpControleCarga", "C"))
		.add(Restrictions.or(
			Restrictions.eq("tpStatusControleCarga", "AE"),
			Restrictions.eq("tpStatusControleCarga", "TC")
		));

		List result = super.findByDetachedCriteria(dc);
		return result;
	}
	
	
	/**
	 * Valida se existe um controle de carga associado ao meio de transporte informado.
	 * 
	 * @param map contendo idSemiRebocado ou idTransportado
	 * @return  
	 */
	public List validateMeioTransporteInControleCarga(Long idTransportado, Long idSemiRebocado, Boolean blUtilizaSemiReboque) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc")
		.createAlias("filialByIdFilialOrigem", "filialByIdFilialOrigem")
		.createAlias("filialByIdFilialAtualizaStatus", "filialByIdFilialAtualizaStatus");

		if (idSemiRebocado != null) {
			dc.createAlias("meioTransporteByIdSemiRebocado", "semiRebocado")
			.add(Restrictions.eq("semiRebocado.id", idSemiRebocado));
		}

		if (idTransportado != null) {
			dc.createAlias("meioTransporteByIdTransportado", "transportado")
			.add(Restrictions.eq("transportado.id", idTransportado));
		}

		List listaStatus = new ArrayList();
		listaStatus.add("FE"); // Fechado
		listaStatus.add("CA"); // Cancelado

		if (blUtilizaSemiReboque != null && blUtilizaSemiReboque) {
			listaStatus.add("ED"); // Em descarga
			listaStatus.add("AD"); // Aguardando descarga
		}

		dc.add(Restrictions.not(Restrictions.in("tpStatusControleCarga", listaStatus)));
		List result = super.findByDetachedCriteria(dc);
		return result;
	}
	
	
	/**
	 * Valida se o ve�culo utilizado no controle de carga j� possui recibo em uma determinada data.
	 * 
	 * @param map contendo idMeioTransporte
	 * @return  
	 */
	public Boolean validateReciboInMeioTransporteControleCarga(Long idMeioTransporte) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual(); 
		String from = ControleCarga.class.getName() + " as cc " +
		"join cc.notasCredito as notasCredito " +
		"join notasCredito.reciboFreteCarreteiro as reciboFreteCarreteiro " +
			"join cc.meioTransporteByIdTransportado as meioTransporteByIdTransportado ";

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from);
		
		sql.addCustomCriteria("meioTransporteByIdTransportado.tpVinculo = 'E'");
		sql.addCriteria("meioTransporteByIdTransportado.id", "=", idMeioTransporte);
		sql.addCriteria("TRUNC(CAST(cc.dhChegadaColetaEntrega.value AS DATE))","=",dataAtual);
		sql.addCustomCriteria("cc.tpStatusControleCarga = 'FE'");
		sql.addCustomCriteria("reciboFreteCarreteiro.tpSituacaoRecibo <> 'CA'");

		final Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
		return IntegerUtils.gtZero(rowCount);
	}
	
	/**
	 * Busca um controle de carga pelo id do manifesto.
	 * Pede tambem como parametro o 
	 * 
	 * @param idManifesto
	 * @param tpOperacao 
	 * @return List
	 */
	public ControleCarga findControleCargaByIdManifesto(Long idManifesto, String tpOperacao) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ControleCarga.class)
			.createAlias("manifestos", "manifesto")
			.setFetchMode("equipeOperacao", FetchMode.JOIN)
			.setFetchMode("carregamentoDescargas", FetchMode.JOIN)
			.add(Restrictions.eq("manifesto.id", idManifesto));
		
			//Caso seja informado um tpOperacao...
			if (tpOperacao!=null) {
				detachedCriteria.createAlias("carregamentoDescargas", "carregamentoDescarga")
					.add(Restrictions.eq("carregamentoDescarga.tpOperacao", tpOperacao));
			}
			
		List result = super.findByDetachedCriteria(detachedCriteria);
			
		return (ControleCarga) result.get(0);
		
	}
	
	/**
	 * Busca todos os dados da tela de controleCargaCarregamento a partir do 'nrControleCarga' de uma
	 * determinada filial onde o controle de carga deve ter o tpStatusControleCarga igual 
	 * a 'GE' ou 'PO' ou 'AV' ou 'EC' ou 'CP'
	 * 
	 * @param nrControleCarga
	 * @param tpOperacao se a opera��o realizada � carregamento ou descarga
	 * @return
	 */
	public List<Map<String, Object>> findControleCargaByNrControleCarga(Long nrControleCarga, Long idFilial, String tpOperacao){

		//Projecao
		StringBuilder projecao = new StringBuilder();
		projecao.append("new map(controleCarga.idControleCarga as idControleCarga, ");
		projecao.append("controleCarga.nrControleCarga as nrControleCarga, ");
		projecao.append("controleCarga.tpControleCarga as tpControleCarga, ");
		projecao.append("controleCarga.tpStatusControleCarga as tpStatusControleCarga, ");
		projecao.append("filialControleCarga.idFilial as idFilialControleCarga, ");
		projecao.append("filialControleCarga.sgFilial as sgFilialControleCarga, ");
		projecao.append("pessoa.nmFantasia as nmFantasia, ");
		projecao.append("filialAtual.idFilial as idFilialAtual, ");
		projecao.append("filialAtual.sgFilial as sgFilialAtual, ");
		projecao.append("rota.dsRota as dsRota, ");
		projecao.append("meioTransporteTransportado.nrFrota as nrFrotaTransporte, ");
		projecao.append("meioTransporteTransportado.nrIdentificador as nrIdentificadorTransporte, ");
		projecao.append("meioTransporteSemiRebocado.nrFrota as nrFrotaSemiReboque, ");
		projecao.append("meioTransporteSemiRebocado.nrIdentificador as nrIdentificadorSemiReboque) ");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(projecao.toString());

		//Clausula from com seus respectivos 'Joins'
		sql.addFrom(ControleCarga.class.getName() + " as controleCarga " +
				"left join controleCarga.rota as rota " +
  				"left join controleCarga.filialByIdFilialOrigem as filialControleCarga " +
  				"left join controleCarga.filialByIdFilialOrigem.pessoa as pessoa " +
  				"left join controleCarga.filialByIdFilialAtualizaStatus as filialAtual " +
				"left join controleCarga.meioTransporteByIdTransportado as meioTransporteTransportado " +
  				"left join controleCarga.meioTransporteByIdSemiRebocado as meioTransporteSemiRebocado ");

		//Restricao filial...
		sql.addCriteria("filialControleCarga.idFilial", "=", idFilial);
		//Restricao tpControleCarga...
		if("C".equals(tpOperacao)) {
			sql.addCustomCriteria("controleCarga.tpStatusControleCarga IN ('GE', 'PO', 'AV', 'EC', 'CP')");
		} else if("D".equals(tpOperacao)) {
			sql.addCustomCriteria("controleCarga.tpStatusControleCarga IN ('AD', 'PO', 'ED', 'EP', 'FE')");
		}

		sql.addCriteria("controleCarga.nrControleCarga", "=", nrControleCarga);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
		
	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param idRotaColetaEntrega
	 * @return Integer com o numero de registos com os dados da grid.
	 */
	public Integer getRowCountByRotaColetaEntregaGE(Long idRota){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(ControleCarga.class.getName() + " as controleCarga " +
				"left join controleCarga.rotaColetaEntrega as rotaColetaEntrega " +
				"left join controleCarga.manifestoColetas as manifestoColeta ");

		sql.addCustomCriteria("rotaColetaEntrega.idRotaColetaEntrega = ? ");
		sql.addCustomCriteria("controleCarga.tpStatusControleCarga in (?, ?, ?) ");
		sql.addCustomCriteria("((manifestoColeta is null) OR (manifestoColeta.tpStatusManifestoColeta = ?)) ");
		
		Object[] parameters = {idRota, "GE", "EC", "PO", "GE"};
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), parameters);
	}

	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idRotaColetaEntrega seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Controle de Carga" e "Manifestos de Coleta" setado como "GE". 
	 * 
	 * @param criteria
	 * @return ResultSetPage com os dados da grid.
	 */
	public ResultSetPage findPaginatedByRotaColetaEntregaGE(Long idRota, FindDefinition findDefinition){

		SqlTemplate sql = new SqlTemplate();
		
		StringBuilder projecao = new StringBuilder();
		projecao.append("new map(filialByIdFilialOrigem.sgFilial as sgFilialOrigem, ");
		projecao.append("controleCarga.idControleCarga as idControleCarga, ");
		projecao.append("controleCarga.nrControleCarga as nrControleCarga, ");
		projecao.append("controleCarga.psTotalFrota as psTotalFrota, ");
		projecao.append("controleCarga.vlTotalFrota as vlTotalFrota, ");
		projecao.append("manifestoColeta.idManifestoColeta as idManifestoColeta, ");
		projecao.append("manifestoColeta.dhGeracao as dhGeracao, ");
		projecao.append("manifestoColeta.nrManifesto as nrManifesto, ");
		projecao.append("filial.sgFilial as sgFilial, ");
		projecao.append("meioTransporteByIdTransportado.nrFrota as nrFrotaTransportado, ");
		projecao.append("meioTransporteByIdTransportado.nrIdentificador as nrIdentificadorTransportado, ");
		projecao.append("meioTransporteByIdTransportado.nrCapacidadeKg as nrCapacidadeKg, ");
		projecao.append("meioTransporteByIdSemiRebocado.nrFrota as nrFrotaSemiRebocado, ");
		projecao.append("meioTransporteByIdSemiRebocado.nrIdentificador as nrIdentificadorSemiRebocado, ");
		projecao.append("moeda.dsSimbolo as dsSimbolo, ");
		projecao.append("moeda.sgMoeda as sgMoeda) ");
		sql.addProjection(projecao.toString());
		
		sql.addFrom(ControleCarga.class.getName() + " as controleCarga " +
				"left join controleCarga.rotaColetaEntrega as rotaColetaEntrega " +
  				"left join controleCarga.manifestoColetas as manifestoColeta " +
				"left join manifestoColeta.filial as filial " +
  				"left join controleCarga.meioTransporteByIdTransportado as meioTransporteByIdTransportado " +
				"left join controleCarga.meioTransporteByIdSemiRebocado as meioTransporteByIdSemiRebocado " +
				"left join controleCarga.moeda as moeda " +
				"left join controleCarga.filialByIdFilialOrigem as filialByIdFilialOrigem");

		sql.addCriteria("rotaColetaEntrega.idRotaColetaEntrega", "=", idRota);
		sql.addCustomCriteria("controleCarga.tpStatusControleCarga in ('GE', 'EC', 'PO')");
		sql.addCustomCriteria("((manifestoColeta is null) OR (manifestoColeta.tpStatusManifestoColeta = 'GE'))");
		
		return getAdsmHibernateTemplate().findPaginated( sql.getSql(),findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());		
	}


	/**
	 * M�todo q calcula o valor total das mercadorias de um Controle de Carga, podendo filtrar apenas documentos
	 * de determinado modal, de determinada abrang�ncia e que tenham ou n�o seguro do cliente.
	 * 
	 * @param idControleCarga
	 * @param blSeguroMercurio
	 * @param tpModal
	 * @param tpAbrangencia
	 * @return
	 */
	public List generateCalculaValorTotalMercadoriaControleCarga(Long idControleCarga, Boolean blSeguroMercurio,
																 String tpModal, String tpAbrangencia, Boolean blIncluirPreManifesto) 
	{
		List param = new ArrayList();

		StringBuilder sql = new StringBuilder()
			.append("select SUM(ds.vl_mercadoria) as totalVlMercadoria, ")
			.append("ds.id_pais as idPais, ")
			.append("ds.id_moeda as idMoeda ")
			.append("from docto_servico ds ")
			.append("inner join servico s on (ds.id_servico = s.id_servico) ")
			.append("where 1=1 ");

		if (tpModal != null) {
			sql.append("and s.tp_modal = ? ");
			param.add(tpModal);
		}
		if (tpAbrangencia != null) {
			sql.append("and s.tp_abrangencia = ? ");
			param.add(tpAbrangencia);
		} 			

		sql.append("and ds.id_docto_servico in (")
			.append("select mnc.id_conhecimento as id_docto_servico ")
			.append("from manifesto m ")
			.append("inner join manifesto_viagem_nacional mvn on (m.id_manifesto = mvn.id_manifesto_viagem_nacional) ")
			.append("inner join manifesto_nacional_cto mnc on (mvn.id_manifesto_viagem_nacional = mnc.id_manifesto_viagem_nacional) ")
			.append("where m.id_controle_carga = ? ")
			.append("and m.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")

			.append("UNION ")

			.append("select mic.id_cto_internacional as id_docto_servico ")	
			.append("from manifesto m2 ")
			.append("inner join manifesto_internacional mi on (m2.id_manifesto = mi.id_manifesto_internacional) ")
			.append("inner join manifesto_internac_cto mic on (mi.id_manifesto_internacional = mic.id_manifesto_internacional) ")
			.append("where m2.id_controle_carga = ? ")
			.append("and m2.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")

			.append("UNION ")

			.append("select doc.id_docto_servico as id_docto_servico ")
			.append("from manifesto m3 ")
			.append("inner join manifesto_entrega me on (m3.id_manifesto = me.id_manifesto_entrega) ")
			.append("inner join manifesto_entrega_documento med on (me.id_manifesto_entrega = med.id_manifesto_entrega) ")
			.append("inner join docto_servico doc on (doc.id_docto_servico = med.id_docto_servico) ")
			.append("where m3.id_controle_carga = ? ")
			.append("and m3.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")
			.append("and doc.tp_documento_servico in ('CTR', 'CRT', 'NFT','CTE','NTE') ");
		
		param.add(idControleCarga);
		param.add(idControleCarga);
		param.add(idControleCarga);
		
		if (blIncluirPreManifesto != null && blIncluirPreManifesto) {
			sql.append("UNION ")
			.append("select doc1.id_docto_servico as id_docto_servico ")
			.append("from manifesto m4 ")
			.append("inner join pre_manifesto_documento pmd on (pmd.id_manifesto = m4.id_manifesto) ")
			.append("inner join docto_servico doc1 on (doc1.id_docto_servico = pmd.id_docto_servico) ")
			.append("where m4.id_controle_carga = ? ")
			.append("and m4.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")
			.append("and doc1.tp_documento_servico in ('CTR', 'CRT', 'NFT','CTE','NTE')");

			param.add(idControleCarga);
		}

		sql.append(") ");


		if (blSeguroMercurio != null) {
			if (!blSeguroMercurio.booleanValue()) {
				sql.append("and exists (")
					.append("select 1 from ")
					.append("devedor_doc_serv dds ")
					.append("inner join cliente c1 on (dds.id_cliente = c1.id_cliente) ")
					.append("inner join seguro_cliente sc1 on (c1.id_cliente = sc1.id_cliente) ")
					.append("where ")
					.append("dds.id_docto_servico = ds.id_docto_servico ")
					.append("and (? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final) ")
					.append("and sc1.tp_modal = s.tp_modal ")
					.append("and sc1.tp_abrangencia = s.tp_abrangencia ")
					.append("and rownum = 1 "+
							"")
				.append(") ");
			} else {
				sql.append("and not exists (")
					.append("select 1 from ")
					.append("devedor_doc_serv dds ")
					.append("inner join cliente c1 on (dds.id_cliente = c1.id_cliente) ")
					.append("inner join seguro_cliente sc1 on (c1.id_cliente = sc1.id_cliente) ")
					.append("where ")
					.append("dds.id_docto_servico = ds.id_docto_servico ")
					.append("and (? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final) ")
					.append("and sc1.tp_modal = s.tp_modal ")
					.append("and sc1.tp_abrangencia = s.tp_abrangencia ")
				.append(") ");
			}
			param.add(JTDateTimeUtils.getDataAtual());
		}
		sql.append("group by ds.id_pais, ds.id_moeda ");			


		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("totalVlMercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("idPais", Hibernate.LONG);
				sqlQuery.addScalar("idMoeda", Hibernate.LONG);
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();		
	}	


	/**
	 * M�todo q calcula o valor total das mercadorias de um Controle de Carga, podendo filtrar apenas documentos
	 * de determinado modal, de determinada abrang�ncia e que tenham ou n�o seguro do cliente. Busca apenas
	 * valores vinculados ao pre manifesto.
	 * 
	 * @param idControleCarga
	 * @param blSeguroMercurio
	 * @param tpModal
	 * @param tpAbrangencia
	 * @return
	 * 
	 * @author Cesar Gabbardo
	 */
	public List generateCalculaValorTotalMercadoriaControleCargaPreManifesto(Long idControleCarga, Boolean blSeguroMercurio) {
		String seguroMercurio = "";
		if (blSeguroMercurio) {
			seguroMercurio = " NOT ";
		}

		StringBuilder sql = new StringBuilder()
		.append("SELECT SUM (vl_mercadoria) AS VLMERCADORIA, id_pais_origem AS IDPAIS, id_moeda AS IDMOEDA ")
		.append("FROM ( ")

		.append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
		.append("doctoservico.id_moeda AS id_moeda, ")
		.append("unidadefederativacoleta.id_pais AS id_pais_origem ")
		.append("FROM ")
		.append("controle_carga cc ") 
		.append("INNER JOIN manifesto manifesto ON (manifesto.id_controle_carga = cc.id_controle_carga ") 
			.append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM')) ")
		.append("INNER JOIN manifesto_viagem_nacional manifestoviagemnacional ")
			.append("ON (manifestoviagemnacional.id_manifesto_viagem_nacional = manifesto.id_manifesto) ")
		.append("INNER JOIN manifesto_nacional_cto manifestonacionalcto ")
			.append("ON (manifestonacionalcto.id_manifesto_viagem_nacional = manifestoviagemnacional.id_manifesto_viagem_nacional) ")
		.append("INNER JOIN conhecimento conhecimento ON (conhecimento.id_conhecimento = manifestonacionalcto.id_conhecimento) ")
		.append("INNER JOIN docto_servico doctoservico ON (doctoservico.id_docto_servico = conhecimento.id_conhecimento) ")
		.append("LEFT JOIN municipio municipiocoleta ON (municipiocoleta.id_municipio = conhecimento.id_municipio_coleta) ")
		.append("LEFT JOIN unidade_federativa unidadefederativacoleta ")
			.append("ON (unidadefederativacoleta.id_unidade_federativa = municipiocoleta.id_unidade_federativa) ")
		.append("LEFT JOIN servico ON (servico.id_servico = doctoservico.id_servico) ")			
		.append("WHERE ")
		.append("cc.id_controle_carga = ? ")
		.append("AND conhecimento.tp_documento_servico IN ('CTR', 'NFT','NTE','CTE') ")
		.append("AND ").append(seguroMercurio).append("EXISTS (select 1 ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		
		.append("UNION ALL ")

		.append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
		.append("doctoservico.id_moeda AS id_moeda, ")
		.append("unidadefederativaorigem.id_pais AS id_pais_origem ")
		.append("FROM ")
		.append("controle_carga cc ") 
		.append("INNER JOIN manifesto manifesto ON (manifesto.id_controle_carga = cc.id_controle_carga ") 
			.append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM')) ")
		.append("INNER JOIN manifesto_internacional manifestointernacional ")
			.append("ON (manifestointernacional.id_manifesto_internacional = manifesto.id_manifesto) ")
		.append("INNER JOIN manifesto_internac_cto manifestointernaccto ")
			.append("ON (manifestointernaccto.id_manifesto_internacional = manifestointernacional.id_manifesto_internacional) ")
		.append("INNER JOIN cto_internacional ctointernacional ")
			.append("ON (ctointernacional.id_cto_internacional = manifestointernaccto.id_cto_internacional) ")
		.append("INNER JOIN docto_servico doctoservico ON (doctoservico.id_docto_servico = ctointernacional.id_cto_internacional) ")
		.append("LEFT JOIN filial filialorigem ON (filialorigem.id_filial = doctoservico.id_filial_origem) ")
		.append("LEFT JOIN pessoa pessoaorigem ON (pessoaorigem.id_pessoa = filialorigem.id_filial) ")
		.append("LEFT JOIN endereco_pessoa enderecoorigem ON (enderecoorigem.id_pessoa = pessoaorigem.id_pessoa) ")
		.append("LEFT JOIN municipio municipioorigem ON (municipioorigem.id_municipio = enderecoorigem.id_municipio) ")
		.append("LEFT JOIN unidade_federativa unidadefederativaorigem ")
			.append("ON (unidadefederativaorigem.id_unidade_federativa = municipioorigem.id_unidade_federativa) ")
		.append("LEFT JOIN servico ON (servico.id_servico = doctoservico.id_servico) ")
		.append("WHERE ")
		.append("cc.id_controle_carga = ? ")
		.append("AND doctoservico.tp_documento_servico = 'CRT' ")
   		.append("AND ").append(seguroMercurio).append("EXISTS (select 1 ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")

		// pr�-manifestos
		.append("UNION ALL ")

		.append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
		.append("doctoservico.id_moeda AS id_moeda, ")
		.append("unidadefederativacoleta.id_pais AS id_pais_origem ")
		.append("FROM ")
		.append("controle_carga cc ")
		.append("INNER JOIN manifesto manifesto ON (manifesto.id_controle_carga = cc.id_controle_carga AND ") 
				.append("manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') AND manifesto.dh_emissao_manifesto IS NULL) ")
		.append("INNER JOIN pre_manifesto_documento premanifestodocumento ON (manifesto.id_manifesto = premanifestodocumento.id_manifesto) ")
		.append("INNER JOIN docto_servico doctoservico ON (doctoservico.id_docto_servico = premanifestodocumento.id_docto_servico) ")
		.append("INNER JOIN conhecimento conhecimento ON (conhecimento.id_conhecimento = doctoservico.id_docto_servico) ")
		.append("LEFT JOIN municipio municipiocoleta ON (municipiocoleta.id_municipio = conhecimento.id_municipio_coleta) ")
		.append("LEFT JOIN unidade_federativa unidadefederativacoleta ON (unidadefederativacoleta.id_unidade_federativa = ")
			.append("municipiocoleta.id_unidade_federativa) ")
		.append("LEFT JOIN servico ON (servico.id_servico = doctoservico.id_servico) ")
		.append("WHERE  ")
		.append("cc.id_controle_carga = ? ")
		.append("AND conhecimento.tp_documento_servico IN ('CTR', 'NFT','NTE','CTE') ")
		.append("AND ").append(seguroMercurio).append("EXISTS (select 1 ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")

		.append("UNION ALL ")

		.append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
		.append("doctoservico.id_moeda AS id_moeda, ")
		.append("unidadefederativaorigem.id_pais AS id_pais_origem ")
		.append("FROM ")
		.append("controle_carga cc ") 
		.append("INNER JOIN manifesto manifesto ON (manifesto.id_controle_carga = cc.id_controle_carga AND ") 
			.append("manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') AND manifesto.dh_emissao_manifesto IS NULL) ")
		.append("INNER JOIN pre_manifesto_documento premanifestodocumento ON (manifesto.id_manifesto = premanifestodocumento.id_manifesto) ")
		.append("INNER JOIN docto_servico doctoservico ON (doctoservico.id_docto_servico = premanifestodocumento.id_docto_servico) ")
		.append("INNER JOIN cto_internacional ctointernacional ON (ctointernacional.id_cto_internacional = doctoservico.id_docto_servico) ")
		.append("LEFT JOIN filial filialorigem ON (filialorigem.id_filial = doctoservico.id_filial_origem) ")
		.append("LEFT JOIN pessoa pessoaorigem ON (pessoaorigem.id_pessoa = filialorigem.id_filial) ")
		.append("LEFT JOIN endereco_pessoa enderecoorigem ON (enderecoorigem.id_pessoa = pessoaorigem.id_pessoa) ")
		.append("LEFT JOIN municipio municipioorigem ON (municipioorigem.id_municipio = enderecoorigem.id_municipio) ")
		.append("LEFT JOIN unidade_federativa unidadefederativaorigem ON (unidadefederativaorigem.id_unidade_federativa = ") 
			.append("municipioorigem.id_unidade_federativa) ")
		.append("LEFT JOIN filial filialdestino ON (filialdestino.id_filial = doctoservico.id_filial_destino) ")
		.append("LEFT JOIN pessoa pessoadestino ON (pessoadestino.id_pessoa = filialdestino.id_filial) ")
		.append("LEFT JOIN endereco_pessoa enderecodestino ON (enderecodestino.id_pessoa = pessoadestino.id_pessoa) ")
		.append("LEFT JOIN municipio municipiodestino ON (municipiodestino.id_municipio = enderecodestino.id_municipio) ")
		.append("LEFT JOIN unidade_federativa unidadefederativadestino ON (unidadefederativadestino.id_unidade_federativa = ") 
			.append("municipiodestino.id_unidade_federativa) ")
		.append("LEFT JOIN servico ON (servico.id_servico = doctoservico.id_servico) ")
		.append("WHERE ")
		.append("cc.id_controle_carga = ? ")
		.append("AND doctoservico.tp_documento_servico = 'CRT' ")
		.append("AND ").append(seguroMercurio).append("EXISTS (select 1 ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")

		.append(") ")
		.append("GROUP BY id_pais_origem, id_moeda");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("VLMERCADORIA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("IDPAIS", Hibernate.LONG);
				sqlQuery.addScalar("IDMOEDA", Hibernate.LONG);
			}
		};

		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		
		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(dtAtual);
		param.add(idControleCarga);
		param.add(dtAtual);
		param.add(idControleCarga);
		param.add(dtAtual);
		param.add(idControleCarga);
		param.add(dtAtual);
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();		
	}	
	
	/**
	 * 
	 * @param idControleCarga
	 */
	public DomainValue findTipoControleCarga(Long idControleCarga) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new map(controleCarga.tpControleCarga as tpControleCarga)");
		sql.addFrom(ControleCarga.class.getName() + " as controleCarga ");
		sql.addCriteria("controleCarga.id", "=", idControleCarga);
		List result = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		Map map = (Map)result.get(0);
		return (DomainValue)map.get("tpControleCarga");
	}
	

	/**
	 * Verifica se um integrante est� em uma outra EQUIPE_OPERACAO que esteja vinculada a um 
	 * CONTROLE_CARGA com status diferente de "FE" e de "CA". 
	 * 
	 * @param idControleCarga
	 * @param idPessoa
	 * @param idUsuario
	 * @return
	 */
	public List findIntegranteEmEquipesComControleCarga(Long idControleCarga, Long idPessoa, Long idUsuario){
		StringBuilder sql = new StringBuilder()
			.append("select new map(cc.idControleCarga as idControleCarga, ")
			.append("cc.nrControleCarga as nrControleCarga, ");
		
		if (idPessoa != null) {
			sql.append("pessoa.nmPessoa as nmIntegrante, ");
		}
		else
		if (idUsuario != null) {
			sql.append("usuario.nmUsuario as nmIntegrante, ");
		}

		sql.append("filialOrigem.sgFilial as sgFilial) ")
			.append("from ")
			.append(ControleCarga.class.getName()).append(" as cc ")
			.append("inner join cc.filialByIdFilialOrigem as filialOrigem ")
  			.append("inner join cc.equipeOperacoes as equipeOperacao ")
			.append("inner join equipeOperacao.integranteEqOperacs as integranteEqOperac ");

		List param = new ArrayList();
		if (idPessoa != null) {
			sql.append("inner join integranteEqOperac.pessoa as pessoa ");
			sql.append("where ");
			sql.append("pessoa.id = ? ");
			param.add(idPessoa);
		}
		else
		if (idUsuario != null) {
			sql.append("inner join integranteEqOperac.usuario as usuario ");
			sql.append("where ");
			sql.append("usuario.id = ? ");
			param.add(idUsuario);
		}
		
		if (idControleCarga != null) {
			sql.append("and cc.id <> ? ");
			param.add(idControleCarga);
		}
		sql.append("and equipeOperacao.dhFimOperacao.value is null ");
		sql.append("and cc.tpStatusControleCarga <> 'FE' and cc.tpStatusControleCarga <> 'CA' "); 
		List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		return result;
	}
	
	
	/**
	 * @param 
	 * @return
	 */
	public java.io.Serializable storeWithFlush(Object object) {
		store(object);
		getAdsmHibernateTemplate().flush(); 
		return getIdentifier(object);
	}
	
	/**
	 * 
	 * @param map
	 * @param param
	 * @param isRowCount
	 * @return
	 */
	private String addSqlByControleCarga(TypedFlatMap map, List param, boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		if (!isRowCount) {
			sql.append("select cc.id_controle_carga as idControleCarga, ")
			.append("cc.nr_controle_carga as nrControleCarga, ")
			.append("cc.dh_geracao as dhGeracao, ")
			.append("cc.dh_previsao_saida as dhPrevisaoSaida, ")
			.append("cc.nr_manif as nrManif, ")
			.append("cc.tp_controle_carga as tpControleCarga, ")
			.append("cc.tp_rota_viagem as tpRotaViagemDominio, ")
			.append("filialOrigem.id_filial as idFilialOrigem, ")
			.append("filialOrigem.sg_filial as sgFilialOrigem, ")
			.append("pessoaFilialOrigem.nm_fantasia as nmFantasiaFilialOrigem, ")
			.append("meioTransportado.id_meio_transporte as idTransportado, ")
			.append("meioTransportado.nr_frota as nrFrotaTransportado, ")
			.append("meioTransportado.nr_identificador as nrIdentificadorTransportado, ")
			.append("meioRebocado.id_meio_transporte as idSemiRebocado, ")
			.append("meioRebocado.nr_frota as nrFrotaSemiRebocado, ")
			.append("meioRebocado.nr_identificador as nrIdentificadorSemiRebocado, ")
			.append("rotaColetaEntrega.ds_rota as dsRotaColeta, ")
			.append("rotaViagem.ds_rota as dsRotaViagem, ")
			.append("filialDestino.id_filial as idFilialDestino, ")
			.append("filialDestino.sg_filial as sgFilialDestino, ")
			.append("pessoaFilialDestino.nm_fantasia as nmFantasiaFilialDestino, ")
			.append("pessoaMotorista.nm_pessoa as nmPessoaMotorista, ")
			.append("pessoaMotorista.nr_identificacao as nrIdentificacaoMotorista, ")
			.append("pessoaMotorista.tp_identificacao as tpIdentificacaoMotorista, ")
			.append("pessoaMotorista.id_pessoa as idPessoaMotorista, ")
			.append("cc.tp_status_controle_carga as tpStatusControleCarga ")
			// LMSA-6340
            .append(", solicita.tp_carga_compartilhada as tpCargaCompartilhada ");
		}
 
		sql.append("from ")
		.append("controle_carga cc ")
		.append("inner join filial filialOrigem on (cc.id_filial_origem = filialOrigem.id_filial) ")
		.append("inner join pessoa pessoaFilialOrigem on (filialOrigem.id_filial = pessoaFilialOrigem.id_pessoa) ")
		.append("left join meio_transporte meioTransportado on (cc.id_transportado = meioTransportado.id_meio_transporte) ")
		.append("left join meio_transporte meioRebocado on (cc.id_semi_rebocado = meioRebocado.id_meio_transporte) ")
		.append("left join rota_coleta_entrega rotaColetaEntrega on (cc.id_rota_coleta_entrega = rotaColetaEntrega.id_rota_coleta_entrega) ")
		.append("left join filial filialDestino on (cc.id_filial_destino = filialDestino.id_filial) ")
		.append("left join pessoa pessoaFilialDestino on (filialDestino.id_filial = pessoaFilialDestino.id_pessoa) ")
		.append("left join motorista on (cc.id_motorista = motorista.id_motorista) ")
		.append("left join pessoa pessoaMotorista on (motorista.id_motorista = pessoaMotorista.id_pessoa) ")
		.append("left join rota rotaViagem on (cc.id_rota = rotaViagem.id_rota) ")
		// LMSA-6340
        .append("left join solicitacao_contratacao solicita on (solicita.id_solicitacao_contratacao = cc.id_solicitacao_contratacao) ")
		.append("where 1=1 ");

		boolean buscaPorVeiculo = false;
		
		if (map.getLong("meioTransporteByIdTransportado.idMeioTransporte") != null) {
			sql.append("and cc.id_transportado = ? ");
			param.add(map.getLong("meioTransporteByIdTransportado.idMeioTransporte"));
			buscaPorVeiculo = true;
		}
		if (map.getLong("meioTransporteByIdSemiRebocado.idMeioTransporte") != null) {
			sql.append("and cc.id_semi_rebocado = ? ");
			param.add(map.getLong("meioTransporteByIdSemiRebocado.idMeioTransporte"));
			buscaPorVeiculo = true;
		}
		
		if (map.getLong("nrControleCarga") != null) {
			sql.append("and cc.nr_controle_carga = ? ");
			param.add(map.getLong("nrControleCarga"));
			buscaPorVeiculo = false;
		}
		if (map.getDomainValue("tpControleCarga") != null && !map.getDomainValue("tpControleCarga").getValue().equals("")) {
			sql.append("and cc.tp_controle_carga = ? ");
			param.add(map.getDomainValue("tpControleCarga").getValue());
			buscaPorVeiculo = false;
		}
		if (map.getDomainValue("tpRotaViagem") != null && !map.getDomainValue("tpRotaViagem").getValue().equals("")) {
			sql.append("and cc.tp_rota_viagem = ? ");
			param.add(map.getDomainValue("tpRotaViagem").getValue());
			buscaPorVeiculo = false;
		}
		if (map.getDomainValue("tpStatusControleCarga") != null && !map.getDomainValue("tpStatusControleCarga").getValue().equals("")) {
			sql.append("and cc.tp_status_controle_carga = ? ");
			param.add(map.getDomainValue("tpStatusControleCarga").getValue());
			buscaPorVeiculo = false;
		}
		if (map.getLong("filialByIdFilialOrigem.idFilial") != null) {
			sql.append("and filialOrigem.id_filial = ? ");
			param.add(map.getLong("filialByIdFilialOrigem.idFilial"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("filialByIdFilialDestino.idFilial") != null) {
			sql.append("and cc.id_filial_destino = ? ");
			param.add(map.getLong("filialByIdFilialDestino.idFilial"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("solicitacaoContratacao.idSolicitacaoContratacao") != null) {
			sql.append("and cc.id_solicitacao_contratacao = ? ");
			param.add(map.getLong("solicitacaoContratacao.idSolicitacaoContratacao"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("rotaIdaVolta.idRotaIdaVolta") != null) {
			sql.append("and cc.id_rota_ida_volta = ? ");
			param.add(map.getLong("rotaIdaVolta.idRotaIdaVolta"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("rotaColetaEntrega.idRotaColetaEntrega") != null) {
			sql.append("and cc.id_rota_coleta_entrega = ? ");
			param.add(map.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("rota.idRota") != null) {
			sql.append("and cc.id_rota = ? ");
			param.add(map.getLong("rota.idRota"));
			buscaPorVeiculo = false;
		}
		
		if (map.getLong("proprietario.idProprietario") != null) {
			sql.append("and cc.id_proprietario = ? ");
			param.add(map.getLong("proprietario.idProprietario"));
			buscaPorVeiculo = false;
		}
		if (map.getLong("motorista.idMotorista") != null) {
			sql.append("and cc.id_motorista = ? ");
			param.add(map.getLong("motorista.idMotorista"));
			buscaPorVeiculo = false;
		}

		if (map.getLong("notaCredito.idNotaCredito") != null) {
			sql.append("and cc.id_controle_carga IN (select id_controle_carga from nota_credito where id_nota_credito = ? ) ");
			param.add(map.getLong("notaCredito.idNotaCredito"));
			buscaPorVeiculo = false;
		}

		if (map.getTimeOfDay("hrPrevisaoSaidaInicial") != null) {
			if (map.getTimeOfDay("hrPrevisaoSaidaFinal") != null) {
				sql.append("and to_date('01/01/1970 ' || to_char(cc.dh_previsao_saida, 'hh24mi'), 'dd/MM/yyyy hh24mi') ");
				sql.append("between ? and ? ");
				param.add(map.getTimeOfDay("hrPrevisaoSaidaInicial"));
				param.add(map.getTimeOfDay("hrPrevisaoSaidaFinal"));
			}
			else {
				sql.append("and to_date('01/01/1970 ' || to_char(cc.dh_previsao_saida, 'hh24mi'), 'dd/MM/yyyy hh24mi') ");
				sql.append(">= ? ");
				param.add(map.getTimeOfDay("hrPrevisaoSaidaInicial"));
			}
			sql.append("and cc.dh_previsao_saida is not null ");
			buscaPorVeiculo = false;
		}
		else
		if (map.getTimeOfDay("hrPrevisaoSaidaFinal") != null) {
			sql.append("and to_date('01/01/1970 ' || to_char(cc.dh_previsao_saida, 'hh24mi'), 'dd/MM/yyyy hh24mi') ");
			sql.append("<= ? ");
			param.add(map.getTimeOfDay("hrPrevisaoSaidaFinal"));
			sql.append("and cc.dh_previsao_saida is not null ");
		}

		if (map.getYearMonthDay("dtGeracaoInicial") != null && map.getYearMonthDay("dtGeracaoFinal") != null) {
			if (!buscaPorVeiculo){
				sql.append("and trunc(cast (cc.dh_geracao as date)) between ? and ? ");
				param.add(map.getYearMonthDay("dtGeracaoInicial"));
				param.add(map.getYearMonthDay("dtGeracaoFinal"));
			}
		}
		
		if(buscaPorVeiculo){
			sql.append("and cc.tp_status_controle_carga not in ('CA','FE')");
		}
		
        // LMSA-6340
        if (map.getDomainValue("tpCargaCompartilhada") != null && !map.getDomainValue("tpCargaCompartilhada").getValue().equals("")) {
            sql.append("and solicita.tp_carga_compartilhada = ? ");
            param.add(map.getDomainValue("tpCargaCompartilhada").getValue());
            buscaPorVeiculo = false;
        }
        
		
		if (!isRowCount) {
			sql.append("order by filialOrigem.sg_filial, cc.nr_controle_carga ");
		}
		
		return sql.toString();
	}
	
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public ResultSetPage findPaginatedControleCarga(TypedFlatMap map, FindDefinition findDefinition) {
		List param = new ArrayList();
		String sql = addSqlByControleCarga(map, param, false);
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idControleCarga",Hibernate.LONG);
				sqlQuery.addScalar("nrControleCarga",Hibernate.LONG);
				sqlQuery.addScalar("dhGeracao",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dhPrevisaoSaida",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("tpControleCarga",Hibernate.STRING);
				sqlQuery.addScalar("tpRotaViagemDominio",Hibernate.STRING);
				sqlQuery.addScalar("idFilialOrigem",Hibernate.LONG);
				sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaFilialOrigem",Hibernate.STRING);
				sqlQuery.addScalar("idTransportado",Hibernate.LONG);
				sqlQuery.addScalar("nrFrotaTransportado",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificadorTransportado",Hibernate.STRING);
				sqlQuery.addScalar("idSemiRebocado",Hibernate.LONG);
				sqlQuery.addScalar("nrFrotaSemiRebocado",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificadorSemiRebocado",Hibernate.STRING);
				sqlQuery.addScalar("dsRotaColeta",Hibernate.STRING);
				sqlQuery.addScalar("dsRotaViagem",Hibernate.STRING);
				sqlQuery.addScalar("idFilialDestino",Hibernate.LONG);
				sqlQuery.addScalar("sgFilialDestino",Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaFilialDestino",Hibernate.STRING);
				sqlQuery.addScalar("nmPessoaMotorista",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoMotorista",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoMotorista",Hibernate.STRING);
				sqlQuery.addScalar("idPessoaMotorista",Hibernate.LONG);
				sqlQuery.addScalar("tpStatusControleCarga",Hibernate.STRING);
				sqlQuery.addScalar("nrManif",Hibernate.LONG);
				// LMSA-6340
				sqlQuery.addScalar("tpCargaCompartilhada", Hibernate.STRING);
			}
		};
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(
				sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray(), csq);
		return rsp;
			
	}

	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public Integer getRowCountControleCarga(TypedFlatMap map) {
		List param = new ArrayList();
		String sql = addSqlByControleCarga(map, param, true);
		return getAdsmHibernateTemplate().getRowCountBySql(sql, param.toArray());
	}


	public Map findByIdControleCarga(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
			.append("select new map(cc.idControleCarga as idControleCarga, ")
			.append("cc.nrControleCarga as nrControleCarga, ")
			.append("cc.nrSMP as nrSMP, ")
			.append("cc.dhPrevisaoSaida as dhPrevisaoSaida, ")
			.append("cc.tpControleCarga as tpControleCarga, ")
			.append("cc.tpStatusControleCarga as tpStatusControleCarga, ")
			.append("cc.tpRotaViagem as tpRotaViagem, ")
			.append("cc.vlFreteCarreteiro as vlFreteCarreteiro, ")
			.append("cc.nrTempoViagem as nrTempoViagem, ")
			.append("cc.vlTotalFrota as vlTotalFrota, ")
			.append("cc.pcOcupacaoInformado as pcOcupacaoInformado, ")
			.append("cc.blEntregaDireta as blEntregaDireta, ")
			.append("notaCredito as notaCredito, ")
			.append("cc.nrManif as nrManif, ")
			.append("filialOrigem.idFilial as filialByIdFilialOrigem_idFilial, ")
			.append("filialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, ")
			.append("pessoaFilialOrigem.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, ")
			.append("filialAtualizaStatus.idFilial as filialByIdFilialAtualizaStatus_idFilial, ")
			.append("filialAtualizaStatus.sgFilial as filialByIdFilialAtualizaStatus_sgFilial, ")
			.append("filialDestino.idFilial as filialByIdFilialDestino_idFilial, ")
			.append("moeda.idMoeda as moeda_idMoeda, ")
			.append("moeda.sgMoeda as moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
			.append("meioTransportado.idMeioTransporte as meioTransporteByIdTransportado_idMeioTransporte, ")
			.append("meioTransportado.nrFrota as meioTransporteByIdTransportado_nrFrota, ")
			.append("meioTransportado.nrIdentificador as meioTransporteByIdTransportado_nrIdentificador, ")
			.append("meioTransportado.tpVinculo as meioTransporteByIdTransportado_tpVinculo, ")
			.append("meioTransporteRodoviario.nrRastreador as meioTransporteByIdTransportado_meioTransporteRodoviario_nrRastreador, ")
			.append("tipoMeioTransporteTransportado.idTipoMeioTransporte as meioTransporteByIdTransportado_modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte, ")
			.append("tipoMeioTransporteTransportado.tpMeioTransporte as meioTransporteByIdTransportado_modeloMeioTransporte_tipoMeioTransporte_tpMeioTransporte, ")
			.append("meioRebocado.idMeioTransporte as meioTransporteByIdSemiRebocado_idMeioTransporte, ")
			.append("meioRebocado.nrFrota as meioTransporteByIdSemiRebocado_nrFrota, ")
			.append("meioRebocado.nrIdentificador as meioTransporteByIdSemiRebocado_nrIdentificador, ")
			.append("rotaColetaEntrega.idRotaColetaEntrega as rotaColetaEntrega_idRotaColetaEntrega, ")
			.append("rotaColetaEntrega.nrRota as rotaColetaEntrega_nrRota, ")
			.append("rotaColetaEntrega.dsRota as rotaColetaEntrega_dsRota, ")
			.append("rotaIdaVolta.idRotaIdaVolta as rotaIdaVolta_idRotaIdaVolta, ")
			.append("rotaViagem.idRotaViagem as rotaIdaVolta_rotaViagem_idRotaViagem, ")
			.append("rotaIdaVolta.nrRota as rotaIdaVolta_nrRota, ")
			.append("rotaByRotaIdaVolta.idRota as rotaIdaVolta_rota_idRota, ")
			.append("rotaByRotaIdaVolta.dsRota as rotaIdaVolta_rota_dsRota, ")
			.append("tipoMeioTransporteByRotaViagem.idTipoMeioTransporte as rotaIdaVolta_rotaViagem_tipoMeioTransporte_idTipoMeioTransporte, ")
			.append("rota.idRota as rota_idRota, ")
			.append("rota.dsRota as rota_dsRota, ")
			.append("solicitacaoContratacao.idSolicitacaoContratacao as solicitacaoContratacao_idSolicitacaoContratacao, ")
			.append("solicitacaoContratacao.nrSolicitacaoContratacao as solicitacaoContratacao_nrSolicitacaoContratacao, ")
			.append("filialSolicitacao.idFilial as solicitacaoContratacao_filial_idFilial, ")
			.append("filialSolicitacao.sgFilial as solicitacaoContratacao_filial_sgFilial, ")
			.append("pessoaFilialSolicitacao.nmFantasia as solicitacaoContratacao_filial_pessoa_nmFantasia, ")
			.append("tabelaColetaEntrega.idTabelaColetaEntrega as tabelaColetaEntrega_idTabelaColetaEntrega, ")
			.append("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega as tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega, ")
			.append("tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega as tipoTabelaColetaEntrega_dsTipoTabelaColetaEntrega, ")
			.append("proprietario.idProprietario as proprietario_idProprietario, ")
			.append("pessoaProprietario.tpIdentificacao as proprietario_pessoa_tpIdentificacao, ")
			.append("pessoaProprietario.nrIdentificacao as proprietario_pessoa_nrIdentificacao, ")
			.append("pessoaProprietario.nmPessoa as proprietario_pessoa_nmPessoa, ")
			.append("motorista.idMotorista as motorista_idMotorista, ")
			.append("pessoaMotorista.tpIdentificacao as motorista_pessoa_tpIdentificacao, ")
			.append("pessoaMotorista.nrIdentificacao as motorista_pessoa_nrIdentificacao, ")
			.append("pessoaMotorista.nmPessoa as motorista_pessoa_nmPessoa ")
			// LMSA-6040
            .append(", solicitacaoContratacao.tpCargaCompartilhada as solicitacaoContratacao_tpCargaCompartilhada")
			
			.append(") ")
			.append("from ")
			.append(ControleCarga.class.getName()).append(" as cc ")
			.append("inner join cc.filialByIdFilialOrigem as filialOrigem ")
			.append("inner join filialOrigem.pessoa as pessoaFilialOrigem ")
			.append("inner join cc.filialByIdFilialAtualizaStatus as filialAtualizaStatus ")
			.append("left join cc.filialByIdFilialDestino as filialDestino ")
			.append("left join cc.moeda as moeda ")
			.append("left join cc.meioTransporteByIdTransportado as meioTransportado ")
			.append("left join meioTransportado.modeloMeioTransporte as modeloMeioTransporte ")
			.append("left join modeloMeioTransporte.tipoMeioTransporte as tipoMeioTransporteTransportado ")
			.append("left join meioTransportado.meioTransporteRodoviario as meioTransporteRodoviario ")
			.append("left join cc.meioTransporteByIdSemiRebocado as meioRebocado ")
			.append("left join cc.rotaIdaVolta as rotaIdaVolta ")
			.append("left join rotaIdaVolta.rota as rotaByRotaIdaVolta ")
			.append("left join rotaIdaVolta.rotaViagem as rotaViagem ")
			.append("left join rotaViagem.tipoMeioTransporte as tipoMeioTransporteByRotaViagem ")
			.append("left join cc.rota as rota ")
			.append("left join cc.rotaColetaEntrega as rotaColetaEntrega ")
			.append("left join cc.solicitacaoContratacao as solicitacaoContratacao ")
			.append("left join solicitacaoContratacao.filial as filialSolicitacao ")
			.append("left join filialSolicitacao.pessoa as pessoaFilialSolicitacao ")
			.append("left join cc.tabelaColetaEntrega as tabelaColetaEntrega ")
			.append("left join cc.tipoTabelaColetaEntrega as tipoTabelaColetaEntrega ")
			.append("left join cc.motorista as motorista ")
			.append("left join cc.notaCredito as notaCredito ")
			.append("left join motorista.pessoa as pessoaMotorista ")
			.append("left join cc.proprietario as proprietario ")
			.append("left join proprietario.pessoa as pessoaProprietario ")
			.append("where ")
			.append("cc.id = ? ");
		
		Object obj[] = new Object[1];
		obj[0] = idControleCarga;

		List lista = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return (Map)lista.get(0);
	}

	/**
	 * M�todo que busca uma entidade ControleCarga sem fazer fetch com relacionamentos.
	 * @param idControleCarga
	 * @return
	 */
	public ControleCarga findByIdBasic(Long idControleCarga){
		return (ControleCarga)getAdsmHibernateTemplate().get(ControleCarga.class, idControleCarga);
	}
	
	
	/**
	 * Soma valores dos Pagamentos Proprietario CC, onde o propriet�rio seja do tipo Merc�rio
	 * @param idControleCarga
	 * @return
	 */
	public BigDecimal getSomaValoresMercurioPagtoProprietarioCC(Long idControleCarga) {
		if (idControleCarga == null)
			return new BigDecimal(0);
		
		StringBuilder sql = new StringBuilder()
		.append(" select pagtoProprietarioCc ")
		.append(" from " + PagtoProprietarioCc.class.getName() + " as pagtoProprietarioCc ")
		.append(" where pagtoProprietarioCc.controleCarga.id = " + idControleCarga)
		.append(" and pagtoProprietarioCc.proprietario.tpProprietario = " + "'P'");
		List result = this.getAdsmHibernateTemplate().find(sql.toString());
		
		BigDecimal sum = new BigDecimal(0);
		if (result != null) {
			Iterator i = result.iterator();
			while (i.hasNext()) {
				PagtoProprietarioCc pagtoProprietarioCc = (PagtoProprietarioCc)i.next();
				if (pagtoProprietarioCc.getVlPagamento()!=null){
					sum = sum.add(pagtoProprietarioCc.getVlPagamento());
				}
				getHibernateTemplate().evict(pagtoProprietarioCc);
			}
		}
		
		return sum;
	}

	public List findTrechosDireto(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		.append("select ")  
		.append("new Map(filialDestino.idFilial as filialByIdFilialDestino_idFilial, ") 
		.append("filialOrigem.idFilial as filialByIdFilialOrigem_idFilial, ")
		.append("controleTrecho.nrDistancia as controleTrecho_nrDistancia, ")
		.append("controleTrecho.idControleTrecho as controleTrecho_idControleTrecho, ")
		.append("controleTrecho.dhPrevisaoSaida as controleTrecho_dhPrevisaoSaida, filialRotaOrigem.nrOrdem as nrOrdemOrigem, filialRotaDestino.nrOrdem as nrOrdemDestino ) ")
		.append("from " + ControleTrecho.class.getName() + " as controleTrecho ")
		.append("inner join controleTrecho.filialByIdFilialDestino as filialDestino ") 
		.append("inner join controleTrecho.filialByIdFilialOrigem as filialOrigem ")
		.append("inner join controleTrecho.controleCarga as controleCarga ")
		.append("inner join controleCarga.filialRotaCcs as filialRotaOrigem " )
		.append("inner join controleCarga.filialRotaCcs as filialRotaDestino " )
		.append("inner join filialRotaOrigem.filial as filialOrdemOrigem ")
		.append("inner join filialRotaDestino.filial as filialOrdemDestino ")
		.append("where controleCarga.id = ? ")
		.append("and filialOrdemOrigem.id = filialOrigem.id ")
		.append("and filialOrdemDestino.id = filialDestino.id ")
		.append("and controleTrecho.blTrechoDireto = ? ");

		List result = getAdsmHibernateTemplate().find(sql.toString(), new Object [] {idControleCarga, Boolean.TRUE});
		return result;		
	}
	/**
	 * Usado para autentica��o no sistema VOL, onde a senha � a hora/minuto da gera��o do controle de carga
	 * @author Marcelo Adamatti
	 * @param idTransportado identificador do meio de transporte
	 * @param hr_min hora e minuto da gera��o do controle de carga
	 * @return ControleCarga
	 */
	public ControleCarga findControleCargaAtual(Long idTransportado, Long hr_min){
		
		String hora;
		
		hora = hr_min.toString();
		if (hr_min < 1000) {
			hora = "0" + hora;
			if (hr_min < 100) {
				hora = "0" + hora;				
			}
		}
		
		StringBuilder sql = new StringBuilder()
			.append("select coca ")
			.append("from " + ControleCarga.class.getName() + " as coca ")
			.append("where  to_char(coca.dhGeracao.value,'hh24mi') = ? ")
			.append("and coca.idControleCarga = (")
			.append("   select max(aux.idControleCarga)  " )
			.append("   from " + ControleCarga.class.getName() + " as aux")
			.append("   where aux.meioTransporteByIdTransportado.idMeioTransporte = ? ")
			.append("   and aux.tpControleCarga = 'C' ")
			.append("   and aux.tpStatusControleCarga<>'FE' ")  
			.append("   and aux.tpStatusControleCarga<>'CA' ")
			.append(")")
		;		
		List result = getAdsmHibernateTemplate().find(sql.toString(), 
				new Object [] {					
					hora,
					idTransportado
				}
		);
		if (result.size()>0) 
			return (ControleCarga) result.get(0);
		return null;
	}
	
	
	/**
	 * 
	 * @param idRotaIdaVolta
	 * @param data
	 * @return
	 */
	public Boolean validateReciboInMeioTransporteControleCarga(Long idRotaIdaVolta, YearMonthDay data) {
		StringBuilder sql = new StringBuilder()
			.append("from ")
			.append(ControleCarga.class.getName()).append(" as cc ")
			.append("where ")
			.append("cc.rotaIdaVolta.id = ? ")
			.append("and trunc(cast (cc.dhPrevisaoSaida.value as date)) = ? ")
			.append("and cc.tpStatusControleCarga <> 'CA' ");

		List param = new ArrayList();
		param.add(idRotaIdaVolta);
		param.add(data);

		final Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
		return !IntegerUtils.gtZero(rowCount);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private String getSqlTemplateByProgramacaoColetasVeiculos(Long idRotaColetaEntrega, Long idMeioTransporte, List param, boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		if (!isRowCount) {
			sql.append("select new map(")
				.append("cc.idControleCarga as idControleCarga, ")
				.append("meioTransporte.idMeioTransporte as meioTransporteByIdTransportado_idMeioTransporte, ")
				.append("meioTransporte.nrFrota as meioTransporteByIdTransportado_nrFrota, ")
				.append("meioTransporte.nrIdentificador as meioTransporteByIdTransportado_nrIdentificador, ")
				.append("meioTransporte.nrCapacidadeKg as meioTransporteByIdTransportado_nrCapacidadeKg, ")
				.append("cc.psAColetar as psAColetar, ")
				.append("cc.psTotalFrota as psTotalFrota, ")
				.append("cc.vlAColetar as vlAColetar, ")
				.append("cc.vlTotalFrota as vlTotalFrota, ")
				.append("cc.pcOcupacaoInformado as pcOcupacaoInformado, ")
				.append("cc.nrControleCarga as nrControleCarga, ")
				.append("moeda.sgMoeda as moeda_sgMoeda, ")
				.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
				.append("filialByIdFilialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, ")
				.append("rotaColetaEntrega.nrRota as rotaColetaEntrega_nrRota, ")
				.append("rotaColetaEntrega.dsRota as rotaColetaEntrega_dsRota, ")
				.append("tipoMeioTransporte.dsTipoMeioTransporte as meioTransporteByIdTransportado_modeloMeioTransporte_tipoMeioTransporte_dsTipoMeioTransporte) ");
		}
		sql.append("from ")
			.append(ControleCarga.class.getName()).append(" as cc ")
			.append("inner join cc.filialByIdFilialOrigem as filialByIdFilialOrigem ")
			.append("inner join cc.manifestoColetas as manifestoColeta ")
			.append("left join cc.moeda as moeda ")
			.append("inner join cc.meioTransporteByIdTransportado as meioTransporte ")
			.append("inner join meioTransporte.modeloMeioTransporte as modeloMeioTransporte ")
			.append("inner join modeloMeioTransporte.tipoMeioTransporte as tipoMeioTransporte ")
			.append("left join cc.rotaColetaEntrega as rotaColetaEntrega ")
			.append("where ")
			.append("cc.tpControleCarga = 'C' ")
			.append("and cc.tpStatusControleCarga in ('TC','AE') ")
			.append("and cc.filialByIdFilialOrigem.id = ? ")
			.append("and manifestoColeta.tpStatusManifestoColeta = 'EM' ");
		
		param.add(SessionUtils.getFilialSessao().getIdFilial());

		if (idRotaColetaEntrega != null) {
	   		sql.append("and cc.rotaColetaEntrega.id = ? ");
	   		param.add(idRotaColetaEntrega);
		}
		if (idMeioTransporte != null) {
			sql.append("and meioTransporte.id = ? ");
			param.add(idMeioTransporte);
		}
		sql.append("order by meioTransporte.nrFrota ");
		return sql.toString();
	}


	/**
	 * 
	 * @param idRotaColetaEntrega
	 * @param idMeioTransporte
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedByProgramacaoColetasVeiculos(Long idRotaColetaEntrega, Long idMeioTransporte, FindDefinition findDefinition) {
		List param = new ArrayList();
		String sql = getSqlTemplateByProgramacaoColetasVeiculos(idRotaColetaEntrega, idMeioTransporte, param, false);
		return getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	}

	/**
	 * 
	 * @param idRotaColetaEntrega
	 * @param idMeioTransporte
	 * @return
	 */
	public Integer getRowCountByProgramacaoColetasVeiculos(Long idRotaColetaEntrega, Long idMeioTransporte){
		List param = new ArrayList();
		String sql = getSqlTemplateByProgramacaoColetasVeiculos(idRotaColetaEntrega, idMeioTransporte, param, true);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
	}
	
	/**
	 * Busca um Controle de Carga com status GE (gerado) que tenha rota coleta entrega igual � rota passada por par�metro e que possua o menor psTotalFrota.
	 * Caso nao entrontre, retorna null;
	 * @param idRotaColetaEntrega
	 * @return
	 */
	public ControleCarga findControleCargaGEByIdRotaColetaEntregaWithLowerPsTotalFrota(Long idRotaColetaEntrega){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");
		dc.add(Restrictions.eq("cc.rotaColetaEntrega.id", idRotaColetaEntrega));
		dc.add(Restrictions.eq("cc.tpStatusControleCarga", "GE"));
		dc.addOrder(Order.asc("cc.psTotalFrota"));
		List listControlesCarga = super.findByDetachedCriteria(dc);
		if (listControlesCarga.size()>1) {
			return (ControleCarga) listControlesCarga.get(0);
		}
		return null;
	}
	
	/**
	 * Solicita��o CQPRO00005467 da integra��o.
	 * 
	 * M�todo que retorna uma ou mais inst�ncias da classe ControleCarga 
	 * de acordo com tpStatusControleCarga e idMeioTransporte.
	 * @param tpStatusControleCarga
	 * @param idMeioTransporte
	 * @return
	 */
	public List findControleCarga(String tpStatusControleCarga, Long idMeioTransporte){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");
		dc.add(Restrictions.eq("cc.tpStatusControleCarga", tpStatusControleCarga));
		dc.add(Restrictions.eq("cc.meioTransporteByIdTransportado.id", idMeioTransporte));
		return super.findByDetachedCriteria(dc);
	}


	/**
	 * 
	 * @param idControleCarga
	 * @param idFilialUsuario
	 * @return
	 */
	public Boolean validateExisteManifestoParaTrechoOrigem(Long idControleCarga, Long idFilialUsuario) {
		StringBuilder sql = new StringBuilder()
			.append("from ")
			.append(ControleCarga.class.getName()).append(" as cc ")
			.append("inner join cc.controleTrechos ct ")
			.append("inner join cc.manifestos manifesto ")
			.append("where ")
			.append("cc.id = ? ")
			.append("and ct.filialByIdFilialOrigem.id = ? ")
			.append("and manifesto.filialByIdFilialOrigem.id = ct.filialByIdFilialOrigem.id ")
			.append("and manifesto.filialByIdFilialDestino.id = ct.filialByIdFilialDestino.id ");

		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idFilialUsuario);

		final Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
		return IntegerUtils.gtZero(rowCount);
	}
	
	
	/**
	 * M�todo que retorna os Controle de Carga que n�o possuem Evento de Coleta.  
	 * @param idControleCarga
	 * @return true se existem Coletas sem baixas para o Controle de Carga
	 * 		   ou false se as baixas foram realizadas para Coletas do Controle de Carga	 		   
	 */
	public boolean existsControleCargaSemOcorrenciaColeta(Long idControleCarga) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select CC.id ");
		sql.append("from ControleCarga CC , ManifestoColeta MC, PedidoColeta PC ");
		sql.append("where  CC.id = MC.controleCarga.id ");
		sql.append("   and MC.id = PC.manifestoColeta.id ");
		sql.append("   and CC.tpControleCarga = ? ");
		sql.append("   and CC.id = ? ");
		sql.append("   and PC.tpStatusColeta in ('TR', 'MA') ");
		Object[] values = {"C", idControleCarga};
		
	   	return !getAdsmHibernateTemplate().find(sql.toString(), values).isEmpty();
	}

	public List<ControleCarga> findByMeioTransporteAndStatusAndControleDeCargaAndIdFilial(Long idTransportado, Long idSemiRebocado, List<String> tpStatus, Long idFilialUsuarioLogado) throws Exception{

		String tpStatusPlaceHolder = String.join(",",Collections.nCopies(tpStatus.size(),"?"));

		Object[] initialArray = {idFilialUsuarioLogado,idSemiRebocado,idTransportado};
		String[] stringArray = tpStatus.toArray(new String[0]);
		Object[] rawArgs = new Object[initialArray.length + stringArray.length + initialArray.length + stringArray.length];
		System.arraycopy(initialArray,0, rawArgs,0, initialArray.length);
		System.arraycopy(stringArray,0, rawArgs, initialArray.length, stringArray.length);
		System.arraycopy(initialArray,0, rawArgs, initialArray.length + stringArray.length, initialArray.length);
		System.arraycopy(stringArray,0, rawArgs, initialArray.length + initialArray.length + stringArray.length, stringArray.length);

		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT " );
		sql.append(" cc.ID_CONTROLE_CARGA," );
		sql.append(" cc.TP_CONTROLE_CARGA," );
		sql.append(" cc.NR_CONTROLE_CARGA, " );
		sql.append(" riv.ID_ROTA_IDA_VOLTA, " );
		sql.append(" rt.ID_ROTA, " );
		sql.append(" rt.DS_ROTA AS DS_ROTA, " );
		sql.append(" f.SG_FILIAL, " );
		sql.append(" rce.ID_ROTA_COLETA_ENTREGA, " );
		sql.append(" rce.DS_ROTA AS DS_ROTA_COLETA_ENTREGA, " );
		sql.append(" rce.NR_ROTA " );
		sql.append("FROM CONTROLE_CARGA cc " );
		sql.append("LEFT JOIN ROTA_IDA_VOLTA riv ON riv.ID_ROTA_IDA_VOLTA = cc.ID_ROTA_IDA_VOLTA " );
		sql.append("LEFT JOIN ROTA rt ON rt.ID_ROTA = cc.ID_ROTA " );
		sql.append("LEFT JOIN FILIAL f ON f.ID_FILIAL = cc.ID_FILIAL_ORIGEM " );
		sql.append("LEFT JOIN ROTA_COLETA_ENTREGA rce ON cc.ID_ROTA_COLETA_ENTREGA = rce.ID_ROTA_COLETA_ENTREGA ");
		sql.append("WHERE cc.DH_GERACAO = (SELECT max(cc.DH_GERACAO) FROM CONTROLE_CARGA cc " );
		sql.append("WHERE");
		if(idFilialUsuarioLogado != null){
			sql.append(" cc.ID_FILIAL_ATUALIZA_STATUS = ?" );
			sql.append(" AND");
		}
		if(idSemiRebocado != null) {
			sql.append(" cc.ID_SEMI_REBOCADO = ?");
			sql.append(" AND ");
		}
		if(idTransportado != null) {
			sql.append(" cc.ID_TRANSPORTADO = ?");
			sql.append(" AND ");
		}
		sql.append("cc.TP_STATUS_CONTROLE_CARGA IN (");
		sql.append(tpStatusPlaceHolder);
		sql.append(")" );
		sql.append(")" );
		if(idFilialUsuarioLogado != null){
			sql.append(" AND cc.ID_FILIAL_ATUALIZA_STATUS = ?" );
		}
		if(idSemiRebocado != null) {
			sql.append(" AND cc.ID_SEMI_REBOCADO = ?");
		}
		if(idTransportado != null) {
			sql.append(" AND cc.ID_TRANSPORTADO = ?" );
		}
		sql.append(" AND cc.TP_STATUS_CONTROLE_CARGA IN (");
		sql.append(tpStatusPlaceHolder);
		sql.append(")");
		sql.append(" AND ROWNUM < ?");

		List paramList = new ArrayList();
		for(Object obj : rawArgs) {
			if(obj != null) {
				paramList.add(obj);
			}
		}
		paramList.add(2);
		Object[] args = paramList.toArray();

		List<Map<String,Object>> controleCargas =  null;
		controleCargas=jdbcTemplate.queryForList(sql.toString(), args);
		List<ControleCarga > controleCargaList = new ArrayList<>();
		controleCargas.forEach(map->{
			BigDecimal idControleCarga = (BigDecimal) map.get("ID_CONTROLE_CARGA");
			String tpControleCarga = (String) map.get("TP_CONTROLE_CARGA");
			BigDecimal nrControleCarga = (BigDecimal) map.get("NR_CONTROLE_CARGA");
			BigDecimal idRotaIdaVolta = (BigDecimal) map.get("ID_ROTA_IDA_VOLTA");
			BigDecimal idRota = (BigDecimal) map.get("ID_ROTA");
			String dsRota = (String) map.get("DS_ROTA");
			String sgFilial = (String) map.get("SG_FILIAL");
			BigDecimal idRotaColetaEntrega =(BigDecimal) map.get("ID_ROTA_COLETA_ENTREGA");
			String dsRotaColetaEntrega = (String) map.get("DS_ROTA_COLETA_ENTREGA");
			BigDecimal nrRotaColetaEntrega  = (BigDecimal) map.get("NR_ROTA");

			RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
			if(idRotaColetaEntrega!=null) {
				rotaColetaEntrega.setIdRotaColetaEntrega(idRotaColetaEntrega.longValue());
			}
			if(dsRotaColetaEntrega!=null) {
				rotaColetaEntrega.setDsRota(dsRotaColetaEntrega);
			}
			if(nrRotaColetaEntrega!=null) {
				rotaColetaEntrega.setNrRota(nrRotaColetaEntrega.shortValue());
			}
			Filial filial = new Filial();
			filial.setSgFilial(sgFilial);
			Rota rota = new Rota();
			if(idRota != null) {
				rota.setIdRota( idRota.longValue());
			}
			rota.setDsRota(dsRota);
			RotaIdaVolta rotaIdaVolta = new RotaIdaVolta();
			if(idRotaIdaVolta != null) {
				rotaIdaVolta.setIdRotaIdaVolta(idRotaIdaVolta.longValue());
			}
			ControleCarga mapCC = new ControleCarga();
			if(idControleCarga != null) {
				mapCC.setIdControleCarga(idControleCarga.longValue());
			}
			mapCC.setTpControleCarga(new DomainValue(tpControleCarga));
			mapCC.setNrControleCarga(nrControleCarga.longValue());
			mapCC.setRotaIdaVolta(rotaIdaVolta);
			mapCC.setRota(rota);
			mapCC.setFilialByIdFilialOrigem(filial);
			mapCC.setRotaColetaEntrega(rotaColetaEntrega);
			controleCargaList.add(mapCC);
		});

		return controleCargaList;
	}

	public List<ControleCarga> findByMeioTransporteAndStatus(Long idTransportado, Long idSemiRebocado, List<String> tpStatus){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");		
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);		
		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);		
		dc.setFetchMode("solicitacaoContratacao", FetchMode.JOIN);
		
		if (idSemiRebocado != null) {
			dc.add(Restrictions.eq("meioTransporteByIdSemiRebocado.id", idSemiRebocado));
		}
		if (idTransportado != null) {
			dc.add(Restrictions.eq("meioTransporteByIdTransportado.id", idTransportado));
		}
						
		dc.add(Restrictions.in("tpStatusControleCarga", tpStatus));
		dc.addOrder(Order.desc("dhGeracao"));
				
		return super.findByDetachedCriteria(dc); 						
	}
	
	public ControleCarga findLastByMeioTransporte(Long idMeioTransporte, Long idControleCargaAtual) {
		
		final String tpControleCargaViagem = "V";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpControleCargaViagem", tpControleCargaViagem);
		params.put("idMeioTransporte", idMeioTransporte);
		params.put("idControleCargaAtual", idControleCargaAtual);
		
		final StringBuilder hql = new StringBuilder()
		.append(" from ").append(getPersistentClass().getName())
		.append(" where tpControleCarga = :tpControleCargaViagem")
		.append(" and tpStatusControleCarga <> 'CA'")
		.append(" and (meioTransporteByIdTransportado.idMeioTransporte = :idMeioTransporte")
		.append(" or meioTransporteByIdSemiRebocado.idMeioTransporte = :idMeioTransporte)");
		
		if (idControleCargaAtual != null) {
			hql.append(" and idControleCarga NOT IN (:idControleCargaAtual)");
		}
		
		hql.append(" order by idControleCarga desc limit 1");
		
		List<ControleCarga> retorno = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		
		if (retorno.isEmpty()) {
			return new ControleCarga();
		}
		
		return (ControleCarga) retorno.get(0);
	}
	
	public ControleCarga findByMeioTransporteTpControleCargaAndFilialAtualizaStatus(Long idMeioTransporte, Long idSemiRebocado, String tpControleCarga, Long idFilial){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");
		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		dc.add(Restrictions.eq("tpControleCarga", tpControleCarga));
		
		if (idSemiRebocado != null) {
			dc.add(Restrictions.eq("meioTransporteByIdSemiRebocado.id", idSemiRebocado));
		}
		if (idMeioTransporte != null) {
			dc.add(Restrictions.eq("meioTransporteByIdTransportado.id", idMeioTransporte));
		}
		
		//filial onde est� localizado o controle de cargas
		dc.add(Restrictions.eq("filialByIdFilialAtualizaStatus.id", idFilial));
		
		List lstDomain = new ArrayList();
		lstDomain.add("PO");
		lstDomain.add("AD");
		lstDomain.add("ED");
		lstDomain.add("EP");
		
		dc.add(Restrictions.in("tpStatusControleCarga", lstDomain));
		List<ControleCarga> lstControleCarga = super.findByDetachedCriteria(dc);
		if(lstControleCarga.size()>1){
			throw new BusinessException("LMS-45068");
		}
		return (ControleCarga)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ControleCarga findByMeioTransporteAndTpStatusControleCargaEmDescargaViagem(Long idMeioTransporte) {

		List lstDomain = new ArrayList();
		lstDomain.add("ED");
		lstDomain.add("EP");

		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");
		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		dc.add(Restrictions.or(Restrictions.eq("meioTransporteByIdTransportado.id", idMeioTransporte),
				Restrictions.eq("meioTransporteByIdSemiRebocado.id", idMeioTransporte)));
		dc.add(Restrictions.in("tpStatusControleCarga", lstDomain));

		List<ControleCarga> lstControleCarga = super.findByDetachedCriteria(dc);
		if(lstControleCarga.size()>1){
			throw new BusinessException("LMS-45068");
		}

		return lstControleCarga.isEmpty() ? null : lstControleCarga.get(0);

	}
	
	public ControleCarga findByIdAndTpOperacaoAndDispositivoUnitizacao(Long idControleCarga, Long idDispositivo, String tpOperacao ){
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc")
			.setFetchMode("carregamentoDescargas", FetchMode.JOIN)
			.createAlias("carregamentoDescargas", "carregamentoDescarga")
			.setFetchMode("carregamentoDescarga.carregamentoPreManifestos", FetchMode.JOIN)
			.createAlias("carregamentoDescarga.carregamentoPreManifestos", "preManifesto")
			.setFetchMode("preManifesto.dispCarregIdentificados", FetchMode.JOIN)
			.createAlias("preManifesto.dispCarregIdentificados", "dispCarregIdentificados")
			.setFetchMode("dispCarregIdentificados.dispositivoUnitizacao", FetchMode.JOIN)
			
			.add(Restrictions.eq("cc.id", idControleCarga))
			.add(Restrictions.eq("carregamentoDescarga.tpOperacao", new DomainValue(tpOperacao) ))
			.add(Restrictions.eq("dispCarregIdentificados.dispositivoUnitizacao.id", idDispositivo));
			
		return (ControleCarga)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public Long findNrManifByIdControleCarga(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT count(*) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("where man.controleCarga.idControleCarga = ? ");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga});
	
		return result;
	}
	/**
	 * Verifica e se existe somente Remetentes com permiss�o de emitir etiqueta.(Natura)
	 * @param id do Controle de Carga.
	 * @return retorna 'true' se existirem somente volumes de Remetentes que emitem etiqueta.
	 */
	public boolean findExistsOnlyClienteRemetenteComEtiquetaEDI(Long idControleCarga) {
		StringBuffer sqlFilter = new StringBuffer();
		sqlFilter.append("   and (cli.blLiberaEtiquetaEdi is null or cli.blLiberaEtiquetaEdi <> 'S') ");
		sqlFilter.append("   and m.controleCarga.id = ? ");
		
		Object[] filterValues = {idControleCarga};
		
		StringBuffer sql = new StringBuffer();		
		sql.append("select cli ");
		sql.append("from Cliente cli , DoctoServico doc, ManifestoEntregaDocumento med, ManifestoEntrega me, Manifesto m ");
		sql.append("where  cli.id = doc.clienteByIdClienteRemetente.id ");
		sql.append("   and doc.id = med.doctoServico.id ");
		sql.append("   and me.id = med.manifestoEntrega.id ");
		sql.append("   and m.id = me.manifesto.id ");
		sql.append(sqlFilter);
		
		List resultList = getAdsmHibernateTemplate().find(sql.toString(), filterValues);
		
		sql = new StringBuffer();		
		sql.append("select cli ");
		sql.append("from Cliente cli , DoctoServico doc, ManifestoNacionalCto mnc, ManifestoViagemNacional mvn, Manifesto m ");
		sql.append("where  cli.id = doc.clienteByIdClienteRemetente.id ");
		sql.append("   and doc.id = mnc.conhecimento.id ");
		sql.append("   and mvn.id = mnc.manifestoViagemNacional.id ");
		sql.append("   and m.id = mvn.manifesto.id ");
		sql.append(sqlFilter);

		resultList.addAll(getAdsmHibernateTemplate().find(sql.toString(), filterValues));
		
		sql = new StringBuffer();		
		sql.append("select cli ");
		sql.append("from Cliente cli , DoctoServico doc, PreManifestoDocumento pmd, Manifesto m ");
		sql.append("where  cli.id = doc.clienteByIdClienteRemetente.id ");
		sql.append("   and doc.id = pmd.doctoServico.id ");
		sql.append("   and m.id = pmd.manifesto.id ");
		sql.append(sqlFilter);
		
		resultList.addAll(getAdsmHibernateTemplate().find(sql.toString(), filterValues));		
				
		return resultList.isEmpty(); 	
	}
	
	public boolean findSolicitacaoPuxada(Long idControleCarga, Long idFilial) {
		
		
		StringBuilder sql = new StringBuilder()
		.append("select count(*) ")
		.append("from "+getPersistentClass().getName()+" cc, SolicitacaoContratacao sc ")
		.append("where cc.idControleCarga = sc.controleCarga.idControleCarga and sc.tpSolicitacaoContratacao = 'P' ")
		.append("and  cc.filialByIdFilialOrigem.idFilial = ? ")
		.append("and cc.idControleCarga = ?");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idFilial,idControleCarga});
		
	
		if(result > 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	public boolean findSaiuFilialOrigem(ControleCarga controleCarga) {
		
		
		StringBuilder sql = new StringBuilder()
		.append("select count(*) ")
		.append("from "+getPersistentClass().getName()+" cc, ")
		.append(FilialRotaCc.class.getName())
		.append(" fr, ")
		.append(ControleTrecho.class.getName())
		.append(" ct, ")
		.append(Manifesto.class.getName())
		.append(" m ")
		.append("where cc.idControleCarga = m.controleCarga.idControleCarga ")
		.append(" and m.tpModal = 'A' ")
		.append(" and fr.controleCarga.idControleCarga = cc.idControleCarga ")
		.append(" and fr.nrOrdem = 1 ")
		.append(" and cc.idControleCarga = ct.controleCarga.idControleCarga  ")
		.append(" and ct.filialByIdFilialOrigem.idFilial = fr.filial.idFilial ")
		.append(" and ct.dhSaida is not null ")
		.append(" and cc.idControleCarga = ?");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{controleCarga.getIdControleCarga()});
		
	
		if(result > 0){
			return true;
		}else{
			return false;
		}

	}

	public boolean findColeta(ControleCarga controleCarga) {

		StringBuilder sql = new StringBuilder()
		.append("select count(*) ")
		.append("from "+getPersistentClass().getName()+" cc, ")
		.append(ManifestoColeta.class.getName())
		.append(" mc, ")
		.append(PedidoColeta.class.getName())
		.append(" pc, ")
		.append(ValorCampoComplementar.class.getName())
		.append(" vcc, ")
		.append(CampoComplementar.class.getName())
		.append(" cco ")
		.append(" where cc.idControleCarga = mc.controleCarga.idControleCarga ")
		.append(" and mc.idManifestoColeta = pc.manifestoColeta.idManifestoColeta ")
		.append(" and vcc.cliente.idCliente = pc.cliente.idCliente ")
		.append(" and vcc.campoComplementar.idCampoComplementar = cco.idCampoComplementar ")
		.append(" and vcc.vlValor = 'S' ")
		.append(" and cco.tpSituacao = 'A' ")
		.append(" and cco.nmCampoComplementar = 'Indicador de Empr�stimo de Carretas' ")
		.append(" and cc.tpControleCarga = 'C' ")
		.append(" and cc.idControleCarga = ?");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{controleCarga.getIdControleCarga()});
		
	
		if(result > 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean findEmPuxada(ControleCarga controleCarga) {

		StringBuilder sql = new StringBuilder()
		.append("select count(*) ")
		.append("from "+getPersistentClass().getName()+" cc, ")
		.append(SolicitacaoContratacao.class.getName())
		.append(" sc ")
		.append(" where cc.idControleCarga = sc.controleCarga.idControleCarga ")
		.append(" and  sc.tpSolicitacaoContratacao= 'P' ")
		.append(" and  exists (select 1 from  "+EventoPuxada.class.getName()+"  ep where ep.solicitacaoContratacao.idSolicitacaoContratacao = sc.idSolicitacaoContratacao and ep.tpStatusEvento = 'EP') ")
		.append(" and  not exists (select 1 from "+EventoPuxada.class.getName()+" ep where ep.solicitacaoContratacao.idSolicitacaoContratacao = sc.idSolicitacaoContratacao and ep.tpStatusEvento = 'PF') ")
		.append(" and cc.idControleCarga = "+controleCarga.getIdControleCarga());
		
		List resultList = getAdsmHibernateTemplate().find(sql.toString());
		Long result = Long.valueOf(0);
		if(!resultList.isEmpty()){
			result = (Long)resultList.get(0);
			
		}
		
	
		if(result > 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean findEntrega(ControleCarga controleCarga) {

		StringBuilder sql = new StringBuilder()
		.append("select count(*) ")
		.append("from "+getPersistentClass().getName()+" cc, ")
		.append(Manifesto.class.getName())
		.append(" m, ")
		.append(ManifestoEntrega.class.getName())
		.append(" me, ")
		.append(ManifestoEntregaDocumento.class.getName())
		.append(" med, ")
		.append(DoctoServico.class.getName())
		.append(" dc, ")
		.append(ValorCampoComplementar.class.getName())
		.append(" vcc, ")
		.append(CampoComplementar.class.getName())
		.append(" cco ")
		
		.append(" where cc.idControleCarga = m.controleCarga.idControleCarga ")
		.append(" and m.idManifesto = me.manifesto.idManifesto ")
		.append(" and  me.idManifestoEntrega = med.manifestoEntrega.idManifestoEntrega ")
		.append(" and  med.doctoServico.idDoctoServico = dc.idDoctoServico ")
		
		.append(" and vcc.cliente.idCliente = dc.clienteByIdClienteDestinatario.idCliente ")
		.append(" and vcc.campoComplementar.idCampoComplementar = cco.idCampoComplementar ")
		.append(" and vcc.vlValor = 'S' ")
		.append(" and cco.tpSituacao = 'A' ")
		.append(" and cco.nmCampoComplementar = 'Indicador de Empr�stimo de Carretas' ")
		.append(" and cc.idControleCarga = ?");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{controleCarga.getIdControleCarga()});
		
		if(result > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean findValidacaoTipoManifestoOperacao(ControleCarga controleCarga) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append(" SELECT count(*) 											");
		hql.append(" FROM ControleCarga cc 										");
		hql.append(" WHERE (													");
		hql.append(" 	EXISTS (SELECT 1 FROM Manifesto mani WHERE 				");
		hql.append("		mani.controleCarga.id 		 = cc.id				");
		hql.append("		AND (mani.tpManifestoEntrega is null or mani.tpManifestoEntrega	!= ?))");
		hql.append(" OR 														");
		hql.append(" 	NOT EXISTS (SELECT 1 FROM Manifesto m					");
		hql.append(" 		WHERE m.controleCarga.id = cc.id)					");
		hql.append(" )	 														");
		hql.append(" AND cc.id = ? 												");

		params.add("EP");
		params.add(controleCarga.getIdControleCarga());
		Long value = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());

		return value > 0;
		}

	public boolean findVinculoColetaEntregaTabelaColetaQuilometragem(Long idControleCarga) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		hql.append(" select count(*) ");
		hql.append(" from ControleCarga as coca ");
		hql.append(" join coca.tabelaColetaEntrega as tace ");
		hql.append(" join tace.parcelaTabelaCes as ptce ");
		hql.append(" where coca.idControleCarga = ? ");
		hql.append(" and ptce.tpParcela = 'QU' ");
		hql.append(" and ptce.vlDefinido > 0 ");
		params.add(idControleCarga);

		Long result1 = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
		if(result1 != null && result1 > 0){
			return true;
		} else {
			hql = new StringBuffer();
			hql.append(" select count(*) ");
			hql.append(" from TabelaColetaEntregaCC tecc ");
			hql.append(" join tecc.tabelaColetaEntrega as tcen ");
			hql.append(" join tecc.controleCarga coca ");
			hql.append(" join tcen.parcelaTabelaCes as ptce ");
			hql.append(" where coca.idControleCarga = ? ");
			hql.append(" and ptce.tpParcela = 'QU' ");
			hql.append(" and ptce.vlDefinido > 0 ");
			Long result2 = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
			return (result2 != null && result2 > 0);
		}
	}

	public Integer getRowCountParceiros(TypedFlatMap criteria){
		List param = new ArrayList();
		String sql = this.getQueryByParceiro(criteria , true , param);
		Integer retorno = getAdsmHibernateTemplate().getRowCountBySql(sql, param.toArray());
		return retorno;
	}

	public ResultSetPage findPaginatedByParceiro(TypedFlatMap criteria, FindDefinition findDefinition) {
		List param = new ArrayList();
		
		String sql = getQueryByParceiro(criteria , false, param);
		ResultSetPage retorno = getAdsmHibernateTemplate().findPaginatedBySql(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray(), null);
		return retorno;
	}
	
	/********************** novas querys **************************/
	
	/**
	 * Quantidade de Notas de Cr�dito Geradas
	 */
	private String getQueryQtdNotasCreditosGeradas(){
		return 	" (SELECT COUNT(NC.ID_NOTA_CREDITO) FROM NOTA_CREDITO NC "+
				" WHERE NC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA) ";
	}

	/**
	 * Quantidade de Docto Servi�o n�o entregues
	 */
	private String getQueryQtdDoctoServicoNaoEntregue(){
		return	" (SELECT COUNT(MED.ID_MANIFESTO_ENTREGA_DOCUMENTO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED "+
				" JOIN MANIFESTO_ENTREGA ME ON MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA "+
				" JOIN MANIFESTO M ON ME.ID_MANIFESTO_ENTREGA = M.ID_MANIFESTO "+
				" WHERE MED.ID_OCORRENCIA_ENTREGA IS NULL "+
				" AND M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA) ";
	}
	
	/**
	 * Quantidade de Pedidos de Coleta n�o executados
	 */
	private String getQueryPedidosColetaNaoExecutados() {
		return 	" (SELECT COUNT(PC.ID_PEDIDO_COLETA) FROM PEDIDO_COLETA PC " +
				" INNER JOIN MANIFESTO_COLETA MC ON MC.ID_MANIFESTO_COLETA = PC.ID_MANIFESTO_COLETA " +
				" WHERE MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA " +
				" AND PC.TP_STATUS_COLETA NOT IN ('CA', 'FI', 'NT', 'EX')) ";
	}
	
	/**
	 * Soma Quantidade de Docto Servi�o n�o entregues + Quantidade de Pedidos de Coleta n�o executados
	 */
	private String doctoServicoNaoEntreguePlusPedidosColetaNaoExecutados(){
		return " ("+ getQueryQtdDoctoServicoNaoEntregue() + " + " +getQueryPedidosColetaNaoExecutados()+ ") ";
	}
	
	/**
	 * Verifica a exist�ncia de Notas de Cr�dito Pendentes
	 */
	private String getQueryHasNotaCreditoPendente(){
		StringBuilder sql =  new StringBuilder();
		sql.append(" (CASE WHEN EXISTS ( SELECT 1 ");		
		sql.append("	FROM MANIFESTO_ENTREGA_DOCUMENTO MED ");
		sql.append("	JOIN MANIFESTO_ENTREGA ME ");
		sql.append("	ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ");
		sql.append("	JOIN MANIFESTO M ");
		sql.append("	ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ");
		sql.append("	JOIN OCORRENCIA_ENTREGA OE ");
		sql.append("	ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ");
		sql.append("	WHERE M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ");
		sql.append("	AND M.TP_MANIFESTO_ENTREGA IN ('EN', 'EP', 'ED')");
		sql.append("	AND M.TP_STATUS_MANIFESTO    != 'CA'");
		sql.append("	AND OE.TP_OCORRENCIA         IN ('A','E')");
		sql.append("	AND NOT EXISTS");
		sql.append("		(SELECT 1");
		sql.append("		FROM NOTA_CREDITO_DOCTO NCD");
		sql.append("		WHERE NCD.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO))");
		sql.append(" OR ");
		sql.append("	EXISTS ");
		sql.append("	(SELECT 1 ");
		sql.append("	FROM PEDIDO_COLETA PC ");
		sql.append("	JOIN MANIFESTO_COLETA MC ");
		sql.append("	ON MC.ID_MANIFESTO_COLETA  = PC.ID_MANIFESTO_COLETA ");
		sql.append("	WHERE MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ");
		sql.append("	AND PC.TP_STATUS_COLETA   IN ('FI', 'NT', 'EX') ");
		sql.append("	AND NOT EXISTS ");
		sql.append("	(SELECT 1 ");
		sql.append("	FROM NOTA_CREDITO_COLETA NCC ");
		sql.append("	WHERE NCC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA");
		sql.append(" 	))  ");
		sql.append("	THEN 'S'");
		sql.append("	ELSE 'N' ");
		sql.append(" END)");
	                           
	   return sql.toString();
		
	}
	
	public Boolean validateGeracaoNotaCreditoParceiraByIdControleCarga(Long idControleCarga) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(getQueryHasNotaCreditoPendente()).append(" AS has_nota_credito_pendente ");
		sql.append("FROM CONTROLE_CARGA CC ");
		sql.append("WHERE ");
		sql.append("CC.ID_CONTROLE_CARGA = :idControleCarga");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idControleCarga", idControleCarga);
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("has_nota_credito_pendente", Hibernate.STRING);
			}
		};
		
		
		List<Object[]> l  = getAdsmHibernateTemplate().findBySql(sql.toString(), param, csq);
		
		return "S".equals(l.get(0));
	}
	
	/*********************** fim novas querys ********************/
	
	private String getQueryByParceiro(TypedFlatMap criteria , boolean isRowCount , List param) {

		StringBuilder sql = new StringBuilder();

		if (!isRowCount) {
			/** SELECT */
			sql.append(" SELECT F.SG_FILIAL  || ' '  || CC.NR_CONTROLE_CARGA AS nr_controle_carga, ")
			.append(" CC.ID_CONTROLE_CARGA AS id_controle_carga, ")
			.append(getQueryHasNotaCreditoPendente()).append(" AS has_nota_credito_pendente, ")
			.append(doctoServicoNaoEntreguePlusPedidosColetaNaoExecutados()).append(" AS docto_mais_coleta_pendentes, ")
			.append(getQueryQtdNotasCreditosGeradas()).append(" AS qtd_notas_geradas ");
		}

		/** FROM */
		sql.append(" FROM ")
		.append(" CONTROLE_CARGA CC ")
		.append(" INNER JOIN FILIAL F ON F.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
		.append(" INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
		.append(" AND MT.TP_VINCULO IN ('A', 'E') ");

		/** WHERE */
		sql.append(" WHERE CC.ID_FILIAL_ORIGEM = ? ");
		param.add(SessionUtils.getFilialSessao().getIdFilial());
		sql.append(" AND CC.TP_CONTROLE_CARGA = 'C' ")
		.append(" AND CC.ID_NOTA_CREDITO IS NULL ")
		.append(" AND CC.TP_STATUS_CONTROLE_CARGA NOT IN ('CA') ")
		.append(" AND CC.ID_PROPRIETARIO = ?");
		param.add(criteria.get("idProprietario"));
		sql.append(" AND EXISTS (SELECT 1 FROM MANIFESTO M ")
		.append(" WHERE M.TP_MANIFESTO_ENTREGA = 'EP' ")
		.append(" AND M.TP_STATUS_MANIFESTO <> 'CA' ")
		.append(" AND M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND ROWNUM = 1) ");
		if(criteria.get("idMeioTransporte") != null){
			sql.append(" AND MT.ID_MEIO_TRANSPORTE = ? ");
			param.add(criteria.get("idMeioTransporte"));
		}
		if(criteria.get("idControleCarga") != null){
			sql.append(" AND CC.ID_CONTROLE_CARGA = ? ");
			param.add(criteria.get("idControleCarga"));
		}
		sql.append(" ORDER BY CC.ID_CONTROLE_CARGA ASC");

		return sql.toString();
	}

	public ControleCarga findByNrControleCargaJoinNotasCredito(Long nrControleCarga, Long filial) {
		
		Criteria crit = getSession().createCriteria(ControleCarga.class)
		.createAlias("notasCredito", "notasCredito")
		.add(Restrictions.eq("nrControleCarga", nrControleCarga))
		.add(Restrictions.eq("filialByIdFilialOrigem.id", filial));

		return (ControleCarga) crit.uniqueResult();
	}

	// FIXME: Verificar coment�rio na service, onde este m�todo � chamado.
	public List findControleCargaByIdNotaCredito(Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append(" select cc "); 
		hql.append(" from ControleCarga as cc ");
		hql.append(" join cc.notasCredito as nocr ");
		hql.append(" where nocr.idNotaCredito = ? ");
		params.add(idNotaCredito);
		return getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
	}
	
	/**
	 * M�todo que retorna �ltimo controle de carga 
	 * que utilizou o meio de transporte passado por par�metro.
	 */
	public ControleCarga findUltimoControleCargaComMeioTransporte(Long idMeioTransporte, boolean semiReboque) {
	    DetachedCriteria criteria = DetachedCriteria.forClass(ControleCarga.class, "cc")
	    .setFetchMode("filialByIdFilialAtualizaStatus", FetchMode.JOIN);
	    if(semiReboque){
	    	criteria.add(Restrictions.eq("meioTransporteByIdSemiRebocado.id",idMeioTransporte));
	    } else {
	    	criteria.add(Restrictions.eq("meioTransporteByIdTransportado.id",idMeioTransporte));
	    }
	    criteria.addOrder(Order.desc("cc.dhGeracao"));
		
	    List<ControleCarga> listCC = findByDetachedCriteria(criteria);
	    if(listCC.isEmpty()){
			return null;
		} else {
			return listCC.get(0);	
		}
	}	
	
    public Integer countCteControleCarga(Long idControleCarga) {
        
        return queryCountConhecimentosControleCarga(idControleCarga, new String[] {"CTE"});
        
    }
	
    public Integer countConhecimentoControleCarga(Long idControleCarga) {
        
        return queryCountConhecimentosControleCarga(idControleCarga, new String[] {"CTE","CTR"});
        
    }
    
    private Integer queryCountConhecimentosControleCarga(Long idControleCarga, String[] tiposDoctoServico) {

    	StringBuilder sql = getSqlConhecimentosControleCarga(tiposDoctoServico);
        
        return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {idControleCarga});
        
    }
    
	private StringBuilder getSqlConhecimentosControleCarga(String[] tiposDoctoServico) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" from docto_servico ds ");
        sql.append("      inner join pre_manifesto_documento pmd on pmd.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO ");
        sql.append("      inner join manifesto m on m.id_manifesto = pmd.id_manifesto ");
        sql.append(" where m.id_Controle_Carga = ? ");
        sql.append("   and m.tp_status_manifesto not in ('CA', 'FE') ");
        sql.append("   and ds.tp_documento_servico in ('").append(StringUtils.join(tiposDoctoServico, "','")).append("')");
		return sql;
        }
        
    public List<Long> findConhecimentosControleCarga(final Long idControleCarga) {
        
    	final String[] tiposDoctoServico = new String[] {"CTE","CTR"};
    	
		final StringBuilder sql = new StringBuilder();
		sql.append("select ds.id_docto_servico ");
		sql.append(getSqlConhecimentosControleCarga(tiposDoctoServico));
        
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
    }
		};
    
		final HibernateCallback hcb = new HibernateCallback() {
        
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
        
            	query.setParameter(0, idControleCarga);
        
				return query.list();
			}
        
		};

		return getHibernateTemplate().executeFind(hcb);
        
    }

    public Integer countManifestoAereoControleCarga(Long idControleCarga) {
        
        StringBuilder sql = new StringBuilder();
        
        sql.append(" from Controle_Carga cc "); 
        sql.append("      inner join manifesto man on man.id_controle_carga  = cc.id_controle_carga ");
        sql.append(" where cc.id_Controle_Carga = ? ");
        sql.append("   and man.tp_Status_Manifesto <> 'CA' ");
        sql.append("   and man.tp_Status_Manifesto <> 'FE' ");
        sql.append("   and man.tp_Modal = 'A' ");
        
        return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {idControleCarga});

    }
    
    public Integer countByVolumeEmDescargaFilial(Long idVolumeNotaFiscal, Long idFilial, Long idControleCarga) {
        
        StringBuilder sql = new StringBuilder();
        
        sql.append(" from Controle_Carga cc "); 
        
        sql.append(" inner join carregamento_descarga cd on cc.id_controle_carga = cd.id_controle_carga");
        sql.append(" inner join carreg_desc_volume cdv on cd.id_carregamento_descarga = cdv.id_carregamento_descarga");
        sql.append(" where  cd.id_filial = ?");
        sql.append(" and    cd.tp_operacao = 'D'");
        sql.append(" and    cd.TP_STATUS_OPERACAO IN ('I', 'E') ");
        sql.append(" and 	cdv.ID_VOLUME_NOTA_FISCAL = ?");
        sql.append(" and 	cd.ID_CONTROLE_CARGA = ?");
        
        return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {idFilial, idVolumeNotaFiscal, idControleCarga});
    }
    
	public List findValorTotalFrota(Long idControleCarga) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM ( ");
		sql.append("select ");
		sql.append("manifesto.ID_MANIFESTO as ID_MANIFESTO, ");
		sql.append("moeda.ID_MOEDA as ID_MOEDA, ");
		sql.append("moeda.SG_MOEDA as SG_MOEDA, ");
		sql.append("moeda.DS_SIMBOLO as DS_SIMBOLO, ");
		sql.append(" (CASE WHEN manifesto.TP_MANIFESTO = 'V' THEN (SELECT SUM(D.vl_mercadoria) ");
		sql.append("  FROM DOCTO_SERVICO D,MANIFESTO_NACIONAL_CTO MNC WHERE ");
		sql.append(" MNC.ID_CONHECIMENTO = D.ID_DOCTO_SERVICO  ");
		sql.append(" AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL) ");
		sql.append(" WHEN manifesto.TP_MANIFESTO = 'E' THEN (SELECT SUM(D.vl_mercadoria) ");
		sql.append(" FROM DOCTO_SERVICO D,PRE_MANIFESTO_DOCUMENTO PMD WHERE ");
		sql.append(" PMD.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO  ");
		sql.append(" AND PMD.ID_MANIFESTO = manifesto.ID_MANIFESTO)END) AS VL_MERCADORIA ");
		sql.append("from ");
		sql.append("controle_carga cc ");
		sql.append("inner join manifesto on (manifesto.ID_CONTROLE_CARGA = cc.ID_CONTROLE_CARGA) ");
		sql.append("inner join filial filialOrigem on (filialOrigem.ID_FILIAL = manifesto.ID_FILIAL_ORIGEM) ");
		sql.append("INNER JOIN filial filialDestino ON (filialDestino.ID_FILIAL = manifesto.ID_FILIAL_DESTINO) ");
		sql.append("left join moeda on (moeda.ID_MOEDA = manifesto.ID_MOEDA) ");
		sql.append("left join manifesto_entrega me on (me.ID_MANIFESTO_ENTREGA = manifesto.ID_MANIFESTO) ");
		sql.append("left join manifesto_viagem_nacional mvn on (mvn.ID_MANIFESTO_VIAGEM_NACIONAL = manifesto.ID_MANIFESTO) ");
		sql.append("left join manifesto_internacional mi on (mi.ID_MANIFESTO_INTERNACIONAL = manifesto.ID_MANIFESTO) ");
		sql.append("where ");
		sql.append("manifesto.TP_STATUS_MANIFESTO <> 'CA' ");
		sql.append("AND manifesto.TP_MANIFESTO_ENTREGA <> 'ED' ");
		sql.append("AND cc.ID_CONTROLE_CARGA = ? ");
		sql.append("UNION ");
		sql.append("select ");
		sql.append("mc.ID_MANIFESTO_COLETA as ID_MANIFESTO, ");
		sql.append("NULL as ID_MOEDA, ");
		sql.append("NULL as SG_MOEDA, ");
		sql.append("NULL as DS_SIMBOLO, ");
		sql.append("(select sum(pc.vl_Total_Verificado) as valor from ");
		sql.append("manifesto_coleta mc1 ");
		sql.append("inner join pedido_coleta pc on (pc.ID_MANIFESTO_COLETA = mc1.ID_MANIFESTO_COLETA) ");
		sql.append("where mc1.ID_MANIFESTO_COLETA = mc.ID_MANIFESTO_COLETA) ");
		sql.append("as VL_MERCADORIA ");
		sql.append("from ");
		sql.append("controle_carga cc ");
		sql.append("inner join manifesto_coleta mc on (mc.ID_CONTROLE_CARGA = cc.ID_CONTROLE_CARGA) ");
		sql.append("inner join filial filialOrigem on (filialOrigem.ID_FILIAL = mc.ID_FILIAL_ORIGEM) ");
		sql.append("where ");
		sql.append("mc.TP_STATUS_MANIFESTO_COLETA <> 'CA' ");
		sql.append("AND cc.ID_CONTROLE_CARGA = ? ");
		sql.append(") ");
	
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
				sqlQuery.addScalar("ID_MOEDA", Hibernate.LONG);
				sqlQuery.addScalar("SG_MOEDA", Hibernate.STRING);
				sqlQuery.addScalar("DS_SIMBOLO", Hibernate.STRING);
				sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);			
			}
		};
		
		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idControleCarga);
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), 
				Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), confSql).getList();
	}
    
    /**
     * Consulta se existe registro de lan�amento de quilomatragem atrav�s do controle de carga e filial.
     */
	public List<ControleCarga> findLancamentoQuilometragem(Long idControleCarga, Long idFilial, Boolean possuiSaida) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT cc");
		hql.append("  FROM " + ControleCarga.class.getName() + " cc");
		hql.append("  JOIN cc.controleQuilometragems cq");
		hql.append("  JOIN cq.filial f");
		hql.append(" WHERE cc.idControleCarga = ?");
		hql.append("   AND f.idFilial = ? "); 
		hql.append("   AND cq.blSaida = ?"); 
		
		List<ControleCarga> controleCargaList = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idControleCarga, idFilial, possuiSaida});
				
		return controleCargaList;
	}	
    
    /**
     * M�todo respons�vel por retornar a soma do valor do frete dos documentos de servi�o carregados no controle de carga em quest�o do tipo
     * C (Coleta/Entrega)
     * LMS-2212
     * @param idControleCarga
     * @return
     */
    public BigDecimal findSomaVlTotalFreteByIdControleCargaColetaEntrega(Long idControleCarga) {
    	StringBuilder sql = new StringBuilder();
        
    	sql.append(" select SUM(ds.vlLiquido) ")
        	.append(" from ControleCarga c ") 
        	.append(" join c.manifestos m ")
        	.append(" join m.manifestoEntrega me ")
        	.append(" join me.manifestoEntregaDocumentos med ")
        	.append(" join med.doctoServico ds ")
        	.append(" where c.idControleCarga = ? ");
        
        Object result = getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idControleCarga});
        return result == null ? BigDecimal.ZERO : (BigDecimal)result;
    }
    
    /**
     * M�todo respons�vel por retornar a soma do valor do frete dos documentos de servi�o carregados no controle de carga em quest�o do tipo
     * V (Viagem)
     * LMS-2212
     * @param idControleCarga
     * @return
     */
    public BigDecimal findSomaVlTotalFreteByIdControleCargaViagem(Long idControleCarga) {
    	StringBuilder sql = new StringBuilder();
        
    	sql.append(" select SUM(c.vlLiquido) ")
	    	.append(" from ControleCarga cc ") 
	    	.append(" join cc.manifestos m ")
    		.append(" join m.manifestoViagemNacional mvn ")
    		.append(" join mvn.manifestoNacionalCtos mnc ")
    		.append(" join mnc.conhecimento c  ")
    		.append(" where cc.idControleCarga = ? ");
        
        Object result = getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idControleCarga});
        return result == null ? BigDecimal.ZERO : (BigDecimal)result;
    }

    //LMS-4902
    public boolean isControleCargaNullTest(Long idControleCarga) {
    	
    	StringBuilder sql = new StringBuilder();
    	List<Object> params = new ArrayList<Object>();
    	sql.append(" select count(*) ") 
    	.append(" FROM Controle_Carga cc, Tabela_Coleta_Entrega tce ")
    	.append(" where cc.id_tabela_Coleta_Entrega = tce.id_tabela_Coleta_Entrega ")
    	.append(" and tce.id_tabela_Coleta_Entrega is not null ")
    	.append(" and tce.tp_Calculo = 'C1' ")
    	.append(" and cc.id_Controle_Carga = ?")
    	.append(" and NOT EXISTS (SELECT 1 FROM Tabela_Coleta_Entrega_CC tcecc ")
    		.append(" where tcecc.id_Controle_Carga = ? ")
    		.append(" ) ");
    	params.add(idControleCarga);
    	params.add(idControleCarga);
     return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params.toArray()) > 0;
	}

    //LMS-4902
	public boolean isControleCargaNotNullTest(Long idControleCarga) {
		
		StringBuilder sql = new StringBuilder();
    	List<Object> params = new ArrayList<Object>();
    	sql.append("  ")
    	.append(" SELECT count(*) ")
    	.append(" FROM Controle_Carga cc, tabela_Coleta_Entrega tce  ")
    	.append(" where cc.id_tabela_Coleta_Entrega = tce.id_Tabela_Coleta_Entrega (+) ")
    	.append(" and cc.id_Tabela_Coleta_Entrega is null ")
    	.append(" and cc.id_Controle_Carga = ?")
    	.append(" and EXISTS (SELECT 1  ")
    		.append(" FROM Tabela_Coleta_Entrega_CC tcecc, tabela_Coleta_Entrega tce ")
    		.append(" where tcecc.id_Controle_Carga = ? ")
    		.append(" and tcecc.id_Tabela_Coleta_Entrega = tce.id_Tabela_Coleta_Entrega ")
    		.append(" and tce.tp_Calculo = 'C2' ")
    		.append(" ) ")
    	.append(" and NOT EXISTS (SELECT 1  ")
    		.append(" FROM Tabela_Coleta_Entrega_CC tcecc, tabela_Coleta_Entrega tce ")
    		.append(" where tcecc. id_controle_carga = ?")
    		.append("  and tcecc.id_Tabela_Coleta_Entrega = tce.id_Tabela_Coleta_Entrega ")
    		.append(" and tce.tp_Calculo = 'C1' ")
    		.append(" )");
    	
    	params.add(idControleCarga);
    	params.add(idControleCarga);
    	params.add(idControleCarga);
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params.toArray()) > 0;	
	}
   
	public List findMeioTransporte(Long idMeioTransporte){
		List param = new ArrayList();
		param.add(idMeioTransporte);
		param.add(SessionUtils.getFilialSessao().getIdFilial());
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new map (mt.idMeioTransporte as idMeioTransporte)"); 
		hql.append("  FROM MeioTransporte mt");
		hql.append("  LEFT JOIN mt.filialAgregadoCe fac");
				
		hql.append(" WHERE mt.idMeioTransporte = ?");
		hql.append("   AND ( (mt.tpVinculo = 'A' AND fac.idFilial = ?) OR (mt.tpVinculo = 'P') )");
		
		List result = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
		
		return result;		
	}
	
	public Object[] findMoedaPaisByControleCarga(Long idControleCarga) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cc.moeda.idMoeda, ");
		hql.append("cc.filialByIdFilialOrigem.pessoa.enderecoPessoa.municipio.unidadeFederativa.pais.idPais");
		hql.append("  FROM ControleCarga cc");
		hql.append(" WHERE cc.idControleCarga = ?");
		List result = getAdsmHibernateTemplate().find(hql.toString(),
				idControleCarga);
		Object[] retorno = (Object[]) (result.isEmpty() ? null : result.get(0));

		if (retorno == null) {
			retorno[0] = findMoedaByControleCargaDefault();
			retorno[1] = findPaisByControleCargaDefault();
		} else {
			Long idMoeda = (Long) retorno[0];
			Long idPais = (Long) retorno[1];
			if (idMoeda == null) {
				retorno[0] = findMoedaByControleCargaDefault();
			}
			if (idPais == null) {
				retorno[1] = findPaisByControleCargaDefault();
			}
		}
		return retorno;
	}
	
	public Long findMoedaByControleCargaDefault() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT m.idMoeda");
		hql.append(" FROM Moeda m ");
		hql.append(" WHERE m.sgMoeda = 'BRL' ");
		List result = getAdsmHibernateTemplate().find(hql.toString());
		return (Long) (result.isEmpty() ? null : result.get(0));
	}
	
	public Long findPaisByControleCargaDefault() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT p.idPais");
		hql.append(" FROM Pais p ");
		hql.append(" WHERE p.sgPais = 'BRA' ");
		List result = getAdsmHibernateTemplate().find(hql.toString());
		return (Long) (result.isEmpty() ? null : result.get(0));
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT cc.id_controle_carga as idcontrolercarga, ");
		sql.append("       fo.sg_filial as sgfilial, "); 
		sql.append("       cc.nr_controle_carga as nrcontrolecarga, ");
		sql.append("       cc.dh_geracao as dhgeracao ");
		
		sql.append("  FROM controle_carga cc ");
		sql.append("       inner join filial fo on fo.id_filial = cc.id_filial_origem ");
		
		sql.append(" WHERE upper(fo.sg_filial) = upper(:sgFilial) ");
		if (filter.get("nrControleCarga") != null) {
		sql.append("   and cc.nr_controle_carga = :nrControleCarga ");
		}
		if (filter.get("idEmpresa") != null) {
			sql.append("   and fo.id_empresa = :idEmpresa ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idcontrolercarga", Hibernate.LONG);
				sqlQuery.addScalar("sgfilial", Hibernate.STRING);
				sqlQuery.addScalar("nrcontrolecarga", Hibernate.LONG);
				sqlQuery.addScalar("dhgeracao", Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		return new ResponseSuggest(sql.toString(), filter, csq);
				}
		
		
	/**
	 * Retorna, caso exista, as filiais que contenham manifesto em aberto de a
	 * partir de um meio de transporte.
	 * 
	 * @param idMeioTransporte
	 * @param idSemiRebocado
	 * @param idControleCarga
	 * @param semiReboque
	 * 
	 * @return List<Object[]>
	 */
	public List<Object[]> findFilialControleCargaManifestoAberto(Long idMeioTransporte, Long idSemiRebocado, Long idControleCarga, Boolean semiReboque){
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT f.sg_filial");
		sql.append(" FROM meio_transporte mt");
		sql.append("  LEFT JOIN modelo_meio_transporte mmt");
		sql.append("   ON mmt.id_modelo_meio_transporte = mt.id_modelo_meio_transporte");
		sql.append("  LEFT JOIN tipo_meio_transporte tmt");
		sql.append("   ON tmt.id_tipo_meio_transporte = mmt.id_tipo_meio_transporte");

		if(idSemiRebocado != null){
			sql.append(" LEFT JOIN controle_carga cc");
			sql.append("  ON cc.id_semi_rebocado = mt.id_meio_transporte");
		} else {
			sql.append(" LEFT JOIN controle_carga cc");
			sql.append("  ON cc.id_transportado = mt.id_meio_transporte");
		}

		sql.append("  LEFT JOIN manifesto m");
		sql.append("   ON m.id_controle_carga = cc.id_controle_carga");
		sql.append("  LEFT JOIN filial f");
		sql.append("   ON f.id_filial = m.id_filial_origem");
		sql.append(" WHERE tmt.tp_meio_transporte = 'R'");
		sql.append("  AND cc.tp_status_controle_carga = 'FE'");
		sql.append("  AND (m.tp_status_manifesto != 'CA' AND m.tp_status_manifesto != 'FE')");
		sql.append("  AND (m.tp_manifesto = 'E'");
		sql.append("  AND m.tp_modal = 'R')");
		sql.append("  AND (m.tp_manifesto_entrega = 'EN' OR m.tp_manifesto_entrega = 'ED')");

		if(idControleCarga != null){
			sql.append(" AND cc.id_controle_carga != :idControleCarga");
			params.put("idControleCarga", idControleCarga);
		}

		if(idSemiRebocado != null){
			sql.append(" AND cc.id_semi_rebocado = :idSemiRebocado");
			params.put("idSemiRebocado", idSemiRebocado);
		} else if(idMeioTransporte != null){
			sql.append(" AND cc.id_transportado = :idMeioTransporte");
			params.put("idMeioTransporte", idMeioTransporte);
		}

		if(semiReboque != null && semiReboque){
			sql.append(" AND NOT EXISTS");
			sql.append("  (SELECT mc.id_manifesto_coleta");
			sql.append("   FROM controle_carga ccs");
			sql.append("    LEFT JOIN manifesto_coleta mc");
			sql.append("     ON mc.id_controle_carga = ccs.id_controle_carga");
			sql.append("    LEFT JOIN pedido_coleta pc");
			sql.append("     ON pc.id_manifesto_coleta = mc.id_manifesto_coleta");
			sql.append("    LEFT JOIN valor_campo_complementar vcc");
			sql.append("     ON vcc.id_cliente = pc.id_cliente");
			sql.append("    LEFT JOIN campo_complementar cac");
			sql.append("     ON cac.id_campo_complementar = vcc.id_campo_complementar");
			sql.append("  WHERE vcc.vl_valor = 'S'");
			sql.append("    AND cac.tp_situacao = 'A'");
			sql.append("    AND cac.nm_campo_complementar = lower('Indicador de Emprstimo de Carretas')");
			sql.append("    AND ccs.tp_controle_carga = 'C'");
			sql.append("    AND ccs.id_controle_carga = cc.id_controle_carga)");
		}

		sql.append(" AND ROWNUM = 1");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, csq);
	}

	public boolean findBlMonitoramentoByMeioTranspIdTransportadoRodoviario(Long idControleCarga) {
		return findBlMonitoramentoByMeioTranspRodoviario(idControleCarga, "meioTransporteByIdTransportado");
	}
	
	public boolean findBlMonitoramentoByMeioTranspIdSemiRebocadoRodoviario(Long idControleCarga) {
		return findBlMonitoramentoByMeioTranspRodoviario(idControleCarga, "meioTransporteByIdSemiRebocado");
																		   
	}
	
	public Boolean validateControleCargaSemNotaCredito(Long idFilial, Long idProprietario, Long idMeioTransporte, YearMonthDay dataRetroativaMinima, YearMonthDay dataRetroativaMaxima) {
		Map<String,Object> params = new HashMap<String, Object>(); 
		params.put("idFilial", idFilial);
		params.put("dataRetroativaMinima", dataRetroativaMinima);
		params.put("dataRetroativaMaxima", dataRetroativaMaxima);
		
		StringBuilder sql = new StringBuilder();
		sql.append("FROM controle_carga cc, \n");
		sql.append("  evento_controle_carga ecc, \n");
		sql.append("  proprietario p, \n");
		sql.append("  empresa e \n");
		sql.append("WHERE \n");
		sql.append("cc.id_nota_credito IS NULL \n");
		sql.append("AND cc.id_controle_carga = ecc.id_controle_carga \n");
		sql.append("AND ecc.tp_evento_controle_carga = 'EM' \n");
		sql.append("AND cc.id_filial_origem = :idFilial \n");
		sql.append("AND cc.tp_controle_carga = 'C' \n");
		sql.append("AND cc.tp_status_controle_carga <> 'CA' \n");
		sql.append("AND cc.id_proprietario = p.id_proprietario \n");
		sql.append("AND p.tp_proprietario <> 'P' \n");
		sql.append("AND p.id_proprietario = e.id_empresa(+) \n");
		sql.append("AND (e.tp_empresa <> 'P' OR e.tp_empresa IS NULL) \n");
		sql.append("AND TRUNC(ecc.dh_evento) <= :dataRetroativaMinima \n");
		sql.append("AND TRUNC(ecc.dh_evento) > :dataRetroativaMaxima \n");

		if(idProprietario != null){
			sql.append("AND cc.id_proprietario = :idProprietario \n");
			params.put("idProprietario", idProprietario);
		}
		
		if(idMeioTransporte != null){
			sql.append("AND cc.id_transportado = :idMeioTransporte \n");
			params.put("idMeioTransporte", idMeioTransporte);
		}
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString() , params ).compareTo(0) > 0;

	}
	
	public boolean findBlMonitoramentoByMeioTranspRodoviario(Long idControleCarga, String tpMeioTransporte) {
		StringBuilder hql = new StringBuilder()
				.append(" SELECT mtr.blMonitorado ")
				.append("FROM ControleCarga cc ")
				.append("JOIN cc." + tpMeioTransporte + " mt ")
				.append("JOIN mt.meioTransporteRodoviario mtr ")
				.append("WHERE cc.idControleCarga = :idControleCarga");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		Boolean result = (Boolean) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
		return result != null ? result : false;
	}

	public BigDecimal generateCalculaValorTotalMercadoriaControleCarga(Long idControleCarga) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idControleCarga", idControleCarga);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("vlTotal", Hibernate.BIG_DECIMAL);
    		}
    	};
    	
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(vl_total_manifesto) as vlTotal ")
		   .append("FROM ( ")
		   		.append("SELECT m.vl_total_manifesto ")
		   		.append("FROM manifesto m ")
		   		.append("WHERE m.id_controle_carga = :idControleCarga ")
		   		.append("AND m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
		   		.append("UNION ALL ")
		   		.append("SELECT nvl(pc.vl_total_informado,pc.vl_total_verificado) ")
		   		.append("FROM manifesto_coleta mc, pedido_coleta pc ")
		   		.append("WHERE mc.id_controle_carga = :idControleCarga ")
		   		.append("AND mc.id_manifesto_coleta = pc.id_manifesto_coleta (+) ")
		   		.append("AND mc.tp_status_manifesto_coleta NOT IN ('CA', 'FE', 'ED')) ");

		List<Object[]> listRetorno = getAdsmHibernateTemplate().findBySql(sql.toString(), param, csq);
		
		if (null != listRetorno && null != listRetorno.get(0) && !CollectionUtils.isEmpty(listRetorno)) {
			BigDecimal vlRetornado = new BigDecimal(String.valueOf(listRetorno.get(0)));
			return vlRetornado == null ? BigDecimal.ZERO : vlRetornado;
		}
		return BigDecimal.ZERO;
	}
	
	public ControleCarga findControleCargaByPedidoColeta(Long idPedidoColeta) {
		StringBuffer sql = new StringBuffer()
			.append("select cc from ").append(PedidoColeta.class.getName()).append(" as pc ").append(
			"join pc.manifestoColeta mc " +
			"join mc.controleCarga cc " + 
			"where pc.idPedidoColeta = ?");
		sql.append("and pc.tpStatusColeta <> 'CA' ");

		List<Object> param = new ArrayList<Object>(1);
		param.add(idPedidoColeta);

		List lista =  getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		if(lista.isEmpty()){
			return null;
		}
		return (ControleCarga) lista.get(0);
	}

	/**
	 * FIXME corrigir n�mero do JIRA
	 * 
	 * LMS-???? (Tela para simula��o do Plano de Gerenciamento de Risco) - Busca
	 * quantidade de {@link ControleCarga} conforme par�metros de filtro da tela
	 * "Simular Plano de Gerenciamento de Risco".
	 * 
	 * @param criteria
	 * @return
	 * 
	 * @see #makeQueryByControleCargaByDhGeracao(TypedFlatMap)
	 */
	public Integer getRowCountByControleCargaByDhGeracao(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeQueryByControleCargaByDhGeracao(criteria), criteria);
	}

	/**
	 * FIXME corrigir n�mero do JIRA
	 * 
	 * LMS-???? (Tela para simula��o do Plano de Gerenciamento de Risco) - Busca
	 * {@link ControleCarga}s conforme par�metros de filtro da tela
	 * "Simular Plano de Gerenciamento de Risco".
	 * 
	 * @param criteria
	 * @return
	 * 
	 * @see #makeQueryByControleCargaByDhGeracao(TypedFlatMap)
	 */
	public List<ControleCarga> findByControleCargaByDhGeracao(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeQueryByControleCargaByDhGeracao(criteria), criteria);
	}

	/**
	 * FIXME corrigir n�mero do JIRA
	 * 
	 * LMS-???? (Tela para simula��o do Plano de Gerenciamento de Risco) -
	 * Prepara query HQL para {@link ControleCarga} espec�fico ou relacionados a
	 * uma {@link Filial} e/ou com determinada {@code dhGeracao}, conforme os
	 * seguintes par�metros opcionais:
	 * <ul>
	 * <li>{@code idControleCarga} para {@link ControleCarga} espec�fico;
	 * <li>{@code idFilial} para relacionar a determinada {@link Filial};
	 * <li>{@code dhGeracaoInicial} para limite inferior de {@code dhGeracao};
	 * <li>{@code dhGeracaoFinal} para limite superior de {@code dhGeracao}.
	 * </ul>
	 * Os relacionamentos com {@link Filial} (para
	 * {@code filialByIdFilialOrigem}), {@link Rota}, {@link RotaColetaEntrega}
	 * e {@link MeioTransporte} (para {@code meioTransporteByIdTransportado} e
	 * {@code meioTransporteByIdSemiRebocado} s�o recuperado imediatamente.
	 * 
	 * @param criteria
	 *            Crit�rio para busca incluindo opcionalmente
	 *            {@code idControleCarga}, {@code idFilial},
	 *            {@code dhGeracaoInicial} e {@code dhGeracaoFinal}.
	 * @return Query HQL para busca de quantidade ou inst�ncias de
	 *         {@link ControleCarga}.
	 */
	private String makeQueryByControleCargaByDhGeracao(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM ControleCarga cc ")
				.append("JOIN FETCH cc.filialByIdFilialOrigem ")
				.append("LEFT JOIN FETCH cc.rota ")
				.append("LEFT JOIN FETCH cc.rotaColetaEntrega ")
				.append("LEFT JOIN FETCH cc.meioTransporteByIdTransportado ")
				.append("LEFT JOIN FETCH cc.meioTransporteByIdSemiRebocado ")
				.append("WHERE 1 = 1 ");
		if (criteria.containsKey("idControleCarga")) {
			hql.append("AND cc.idControleCarga = :idControleCarga ");
		}
		if (criteria.containsKey("idFilial")) {
			hql.append("AND cc.filialByIdFilialOrigem.idFilial = :idFilial ");
		}
		if (criteria.containsKey("dhGeracaoInicial")) {
			hql.append("AND cc.dhGeracao.value >= :dhGeracaoInicial ");
		}
		if (criteria.containsKey("dhGeracaoFinal")) {
			hql.append("AND cc.dhGeracao.value <= :dhGeracaoFinal ");
		}
		return hql.toString();
	}

	public  List<Object[]> findTrechosOrigemDestino(Long idControleCarga, Long idFilialLogada) {
		StringBuilder sql = new StringBuilder()
		.append("select p.nm_pessoa as nm_pessoa, ")
		.append("ep.ds_endereco ||', '|| ep.nr_endereco||', '|| ep.ds_complemento||', '||ep.ds_bairro as endereco, ")
		.append("m.nm_municipio as nm_municipio, ")
		.append("ep.nr_cep as cep, ")
		.append("lpad(uf.nr_ibge,2,0)||lpad(m.cd_ibge,5,0) as codigo_ibge, ")
		.append("ep.id_endereco_pessoa as id_endereco_pessoa, ")
		.append("pais.sg_resumida as pais, ")
		.append("uf.sg_unidade_federativa as uf, ")
		.append("p.nr_identificacao as nr_identificacao, ")
		.append("frc.id_filial as id_filial, ")
		.append("f.sg_filial as sg_filial ")
		.append("from filial_rota_cc frc, ")
		.append("pessoa                    p, ")
		.append("endereco_pessoa           ep, ")
		.append("municipio                 m, ")
		.append("unidade_federativa        uf, ")
		.append("pais                      pais, ")
		.append("filial                    f ")
		.append("where frc.id_controle_carga = :idControleCarga ")
		.append("and frc.id_filial = p.id_pessoa ")
		.append("and p.id_endereco_pessoa = ep.id_endereco_pessoa ")
		.append("and ep.id_municipio = m.id_municipio ")
		.append("and m.id_unidade_federativa = uf.id_unidade_federativa ")
		.append("and uf.id_pais = pais.id_pais ")
		.append("and frc.id_filial = f.id_filial ")
		.append("and frc.id_filial_rota_cc >=  ")
		.append("(select frc2.id_filial_rota_cc ")
		.append("from filial_rota_cc frc2 ")
		.append("where frc2.id_controle_carga = frc.id_controle_carga ")
		.append("and frc2.id_filial = :idFilialLogada ) ")
		.append("order by frc.nr_ordem asc "); 
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idControleCarga", idControleCarga);
		parametersValues.put("idFilialLogada", idFilialLogada);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
      			sqlQuery.addScalar("endereco", Hibernate.STRING);
      			sqlQuery.addScalar("nm_municipio", Hibernate.STRING);
      			sqlQuery.addScalar("cep", Hibernate.STRING);
      			sqlQuery.addScalar("codigo_ibge", Hibernate.INTEGER);
      			sqlQuery.addScalar("id_endereco_pessoa", Hibernate.LONG);
      			sqlQuery.addScalar("pais", Hibernate.STRING);
      			sqlQuery.addScalar("uf", Hibernate.STRING);
      			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
      			sqlQuery.addScalar("id_filial", Hibernate.LONG);
      			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
    		}
    	};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);	
	}

	public  List<Object[]> findCargaTrechosDestino(Long idControleCarga, Long idFilial) {
		StringBuilder sql = new StringBuilder()
		.append("select pRemetente.Nr_Identificacao as identificacao , ")
		.append("sum(ds.vl_mercadoria) as vlMercadoria ")
		.append("from controle_carga            cc, ")
		.append("manifesto                 m, ")
		.append("manifesto_viagem_nacional mvn, ")
		.append("manifesto_nacional_cto    mnc, ")
		.append("conhecimento              c, ")
		.append("docto_servico             ds, ")
		.append("pessoa                    pRemetente ")
		.append("WHERE cc.id_controle_carga = :idControleCarga ")
		.append("and m.id_filial_destino = :idFilial ")
		.append("and cc.id_controle_carga = m.id_controle_carga ")
		.append("and m.id_manifesto = mvn.id_manifesto_viagem_nacional ")
		.append("and mvn.id_manifesto_viagem_nacional =   mnc.id_manifesto_viagem_nacional ")
		.append("and mnc.id_conhecimento = c.id_conhecimento ")
		.append("and c.id_conhecimento = ds.id_docto_servico ")
		.append("and ds.id_cliente_remetente = pRemetente.Id_Pessoa ")
		.append("and m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
		.append("and c.tp_documento_servico IN ('CTR', 'CTE', 'NFT', 'NTE') ")
		.append("group by pRemetente.Nr_Identificacao ");
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idControleCarga", idControleCarga);
		parametersValues.put("idFilial", idFilial);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("identificacao", Hibernate.STRING);
      			sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
    		}
    	};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
	}	
	
	public ControleCarga findControleCargaByDoctoServico(Long idDoctoServico) {
		StringBuffer sql = new StringBuffer()
			.append("select cc from ").append(DoctoServico.class.getName()).append(" as ds ").append(
			"join ds.pedidoColeta pc " +
			"join pc.manifestoColeta mc " +
			"join mc.controleCarga cc " + 
			"where ds.idDoctoServico = ? ");
		sql.append("and pc.tpStatusColeta <> 'CA' ");

		List<Object> param = new ArrayList<Object>(1);
		param.add(idDoctoServico);

		List lista =  getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		if(lista.isEmpty()){
			return null;
		}
		return (ControleCarga) lista.get(0);
	}

	public  List<Object[]> findTrechosDestinoColetaEntrega(Long idFilial) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idFilial", idFilial);
		
		StringBuilder sql = new StringBuilder()
		.append("select p.nm_pessoa as nm_pessoa, ")
		.append("ep.ds_endereco ||', '|| ep.nr_endereco||', '|| ep.ds_complemento||', '||ep.ds_bairro as endereco, ")
		.append("m.nm_municipio as nm_municipio, ")
		.append("ep.nr_cep as cep, ")
		.append("lpad(uf.nr_ibge,2,0)||lpad(m.cd_ibge,5,0) as codigo_ibge, ")
		.append("ep.id_endereco_pessoa as id_endereco_pessoa, ")
		.append("pais.sg_resumida as pais, ")
		.append("uf.sg_unidade_federativa  uf_sg, ")
		.append("p.nr_identificacao as nr_identificacao, ")
		.append("f.sg_filial as sg_filial ")
		.append("from filial                    f, ")
		.append("pessoa                    p, ")
		.append("endereco_pessoa           ep, ")
		.append("municipio                 m, ")
		.append("unidade_federativa        uf, ")
		.append("pais                      pais ")
		.append("where p.id_pessoa = :idFilial ")
		.append("and f.id_filial = p.id_pessoa ")
		.append("and p.id_endereco_pessoa = ep.id_endereco_pessoa ")
		.append("and ep.id_municipio = m.id_municipio ")
		.append("and m.id_unidade_federativa = uf.id_unidade_federativa ")
		.append("and uf.id_pais = pais.id_pais ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
      			sqlQuery.addScalar("endereco", Hibernate.STRING);
      			sqlQuery.addScalar("nm_municipio", Hibernate.STRING);
      			sqlQuery.addScalar("cep", Hibernate.STRING);
      			sqlQuery.addScalar("codigo_ibge", Hibernate.INTEGER);
      			sqlQuery.addScalar("id_endereco_pessoa", Hibernate.LONG);
      			sqlQuery.addScalar("pais", Hibernate.STRING);
      			sqlQuery.addScalar("uf_sg", Hibernate.STRING);
      			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
      			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
      		}
    	};
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
	}

	public  List<Object[]> findTrechosDestinoColetaEntregaCargas(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		.append("select p.nm_pessoa as nm_pessoa, ")
		.append("ep.ds_endereco ||', '|| ep.nr_endereco||', '|| ep.ds_complemento||', '||ep.ds_bairro as endereco, ")
		.append("m.nm_municipio as nm_municipio, ")
		.append("ep.nr_cep as cep, ")
		.append("lpad(uf.nr_ibge,2,0)||lpad(m.cd_ibge,5,0) as codigo_ibge, ")
		.append("ep.id_endereco_pessoa as id_endereco, ")
		.append("pais.sg_resumida as pais, ")
        .append("uf.sg_unidade_federativa as uf_sg, ")
        .append("p.nr_identificacao as identificador, ")
        .append("pontos.tp_ponto as ponto, ")
        .append("pontos.nr_identificacao as nr_identificacao, ")
        .append("sum(pontos.vl_Mercadoria) as vl_mercadoria ")
		.append("from (SELECT 'C' as tp_ponto, ")
		.append("cc.id_rota_coleta_entrega, ")
		.append("p.id_pessoa as id_pessoa, ")
		.append("nvl(pc.id_endereco_pessoa,p.id_endereco_pessoa) as id_endereco_pessoa, ")
		.append("p.nr_identificacao as nr_identificacao, ")
		.append("dc.vl_mercadoria as vl_mercadoria ")
		.append("FROM controle_carga     cc, ")
		.append("manifesto_coleta   mc, ")
		.append("pedido_coleta      pc, ")
		.append("detalhe_coleta     dc, ")
		.append("pessoa             p ")
		.append("WHERE cc.id_controle_carga = :idControleCarga ")
		.append("and cc.id_controle_carga = mc.id_controle_carga ")
		.append("and mc.id_manifesto_coleta = pc.id_manifesto_coleta ")
		.append("and pc.id_pedido_coleta = dc.id_pedido_coleta ")
		.append("and pc.id_cliente = p.id_pessoa ")
		.append("and mc.tp_status_manifesto_coleta NOT IN ('CA', 'FE', 'ED') ")
		.append("and EXISTS ")
		.append("(SELECT 1 ")
		.append("FROM evento_coleta ec ")
		.append("WHERE ec.tp_evento_coleta NOT IN ('EX', 'CA', 'FD', 'ID') ")
		.append("AND ec.id_pedido_coleta = pc.id_pedido_coleta) ")
		.append("union all ")
		.append("SELECT 'E' as tp_ponto, ")
		.append("cc.id_rota_coleta_entrega, ")
		.append("pd.id_pessoa as id_pessoa, ")
		.append("nvl(ds.id_endereco_entrega,pd.id_endereco_pessoa) as id_endereco_pessoa, ")
		.append("pr.nr_identificacao, ")
		.append("ds.vl_mercadoria as vl_Mercadoria ")
		.append("FROM controle_carga              cc, ")
		.append("manifesto                   m, ")
		.append("manifesto_entrega           me, ")
		.append("manifesto_entrega_documento med, ")
		.append("docto_servico               ds, ")
		.append("conhecimento                c, ")
		.append("pessoa                      pr, ")
		.append("pessoa                      pd ")
		.append("WHERE cc.id_controle_carga = :idControleCarga ")
		.append("and cc.id_controle_carga = m.id_controle_carga ")
		.append("AND m.id_manifesto = me.id_manifesto_entrega ")
		.append("AND me.id_manifesto_entrega = med.id_manifesto_entrega ")
		.append("AND med.id_docto_servico = ds.id_docto_servico ")
		.append("AND ds.id_docto_servico = c.id_conhecimento(+) ")
		.append("and ds.id_cliente_remetente = pr.id_pessoa ")
		.append("and ds.id_cliente_destinatario = pd.id_pessoa ")
		.append("AND m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') ")
		.append("AND ds.tp_documento_servico IN  ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
		.append("AND NOT EXISTS ")
		.append("(SELECT 1 ")
		.append("FROM evento_documento_servico eds, evento e ")
		.append("WHERE eds.id_evento = e.id_evento ")
		.append("AND e.cd_evento = 21 ")
		.append("AND eds.bl_evento_cancelado = 'N' ")
		.append("AND eds.id_docto_servico = ds.id_docto_servico)) pontos, ")
		.append("pessoa                    p, ")
		.append("endereco_pessoa           ep, ")
		.append("municipio                 m, ")
		.append("unidade_federativa        uf, ")
		.append("pais                      pais ")
		.append("where pontos.id_pessoa = p.id_pessoa ")
		.append("and pontos.id_endereco_pessoa = ep.id_endereco_pessoa ")
		.append("and ep.id_municipio = m.id_municipio ")
		.append("and m.id_unidade_federativa = uf.id_unidade_federativa ")
		.append("and uf.id_pais = pais.id_pais ")
		.append("group by pontos.tp_ponto, ")
		.append("p.nm_pessoa, ")
		.append("p.nm_fantasia, ")
		.append("ep.ds_endereco ||', '|| ep.nr_endereco||', '|| ep.ds_complemento||', '||ep.ds_bairro, ")
		.append("m.nm_municipio, ")
		.append("ep.nr_cep, ")
		.append("lpad(uf.nr_ibge,2,0)||lpad(m.cd_ibge,5,0), ")
		.append("ep.id_endereco_pessoa, ")
		.append("pais.sg_resumida, ")
		.append("uf.sg_unidade_federativa, ")
		.append("p.nr_identificacao, ")
		.append("pontos.nr_identificacao, ")
		.append("pontos.id_rota_coleta_entrega ")
		
		.append("order by ")
		.append("(select ric.nr_ordem_operacao ")
		.append("from rota_intervalo_cep ric ")
		.append("where ric.id_rota_coleta_entrega = pontos.id_rota_coleta_entrega ")
		.append("and ep.nr_cep  between ric.nr_cep_inicial and ric.nr_cep_final ")
		.append("and trunc(sysdate) between ric.dt_vigencia_inicial and ric.dt_vigencia_final ")
		.append("and rownum = 1) asc ");
		
		
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idControleCarga", idControleCarga);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
      			sqlQuery.addScalar("endereco", Hibernate.STRING);
      			sqlQuery.addScalar("nm_municipio", Hibernate.STRING);
      			sqlQuery.addScalar("cep", Hibernate.STRING);
      			sqlQuery.addScalar("codigo_ibge", Hibernate.INTEGER);
      			sqlQuery.addScalar("id_endereco", Hibernate.LONG);
      			sqlQuery.addScalar("pais", Hibernate.STRING);
      			sqlQuery.addScalar("uf_sg", Hibernate.STRING);
      			sqlQuery.addScalar("identificador", Hibernate.STRING);
      			sqlQuery.addScalar("ponto", Hibernate.STRING);
      			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
      			sqlQuery.addScalar("vl_mercadoria", Hibernate.BIG_DECIMAL);
      		}
    	};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
	}
	
	public ControleCarga findDadosParaCiot(Long idControleCarga) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		
		final StringBuilder hql = new StringBuilder()
		.append(" SELECT cc ")
		.append(" FROM ControleCarga cc ")
		
		.append(" left join fetch cc.meioTransporteByIdTransportado mt ")
		.append(" left join fetch mt.modeloMeioTransporte mmt ")
		
		.append(" left join fetch cc.proprietario pr ")
		.append(" left join fetch pr.telefoneEndereco tepr ")
		.append(" left join fetch pr.pessoa ppr ")
		.append(" left join fetch ppr.enderecoPessoa eppr ")
		.append(" left join fetch eppr.municipio meppr ")
		.append(" left join fetch meppr.unidadeFederativa ufmeppr ")
		.append(" left join fetch eppr.tipoLogradouro tleppr ")
		
		.append(" left join fetch cc.filialByIdFilialOrigem fo ")
		.append(" left join fetch fo.empresa foemp ")
		.append(" left join fetch foemp.pessoa foempp ")
		.append(" left join fetch fo.pessoa pfo ")
		.append(" left join fetch pfo.enderecoPessoa epfo ")
		.append(" left join fetch epfo.municipio mepfo ")
		.append(" left join fetch mepfo.unidadeFederativa ufmepfo ")
		.append(" left join fetch epfo.tipoLogradouro tlepfo ")
		
		.append(" left join fetch cc.filialByIdFilialDestino fd ")
		.append(" left join fetch fd.pessoa pfd ")
		.append(" left join fetch pfd.enderecoPessoa epfd ")
		.append(" left join fetch epfd.municipio mepfd ")
		.append(" left join fetch mepfd.unidadeFederativa ufmepfd ")
		.append(" left join fetch epfd.tipoLogradouro tlepfd ")
		
		.append(" WHERE cc.idControleCarga = :idControleCarga ");
		
		return (ControleCarga) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
	}
	
public ControleCarga findDadosParaAlteracaoCiot(Long idControleCarga) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		
		final StringBuilder hql = new StringBuilder()
		.append(" SELECT cc ")
		.append(" FROM ControleCarga cc ")
		
		.append(" left join fetch cc.meioTransporteByIdTransportado mt ")
		.append(" left join fetch cc.proprietario pr ")
		
		.append(" left join fetch cc.filialByIdFilialOrigem fo ")
		.append(" left join fetch fo.empresa foemp ")
		.append(" left join fetch foemp.pessoa foempp ")
		
		.append(" left join fetch cc.filialByIdFilialDestino fd ")
		
		.append(" WHERE cc.idControleCarga = :idControleCarga ");
		
		return (ControleCarga) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
	}

	public List findControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		StringBuffer sql = new StringBuffer()
		.append("from ").append(getPersistentClass().getName()).append(" as cc ")
		.append("inner join fetch cc.filialByIdFilialOrigem filialOrigem ")
		.append("inner join fetch cc.filialByIdFilialDestino filialDestino ")
		.append("where ")
		.append("cc.idControleCarga = ? ");
	
		List param = new ArrayList();
		param.add(idControleCarga);
		
		if (idFilialOrigem != null) {
			sql.append("and filialOrigem.id = ? ");
			param.add(idFilialOrigem);
		}
		if (idFilialDestino != null) {
			sql.append("and filialDestino.id = ? ");
			param.add(idFilialDestino);
		}
		return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	public Boolean verificaSaidaPortariaDiaPosterior(DateTime dia, Long idDoctoServico) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		params.put("dia", dia);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT 1 ");
		sql.append(" FROM controle_carga              cc ");
		sql.append(" JOIN manifesto                   m   ON cc.id_controle_carga = m.id_controle_carga ");
		sql.append(" JOIN manifesto_entrega_documento med ON med.id_manifesto_entrega = m.id_manifesto ");
		sql.append(" WHERE med.id_docto_servico = :idDoctoServico ");
		sql.append(" AND TRUNC( cc.dh_saida_coleta_entrega ) > :dia ");
		sql.append(" AND TRUNC( cc.dh_geracao ) = :dia ");
		sql.append(" AND EXISTS ( SELECT 1 ");
		sql.append(" 				FROM   ocorrencia_docto_servico ODS ");
		sql.append(" 				WHERE  ods.id_docto_servico = :idDoctoServico ");
		sql.append(" 				AND TRUNC( ods.dh_liberacao ) = :dia ");
		sql.append(" 				AND ROWNUM = 1 )");
		sql.append(" AND ROWNUM = 1");
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params) > 0;
	}

	public boolean validateExistePreManifestoDocumentoPreManifestoVolume(Long idControleCarga, Long idDoctoServico, Long idFilial) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("idDoctoServico", idDoctoServico);
		params.put("idFilial", idFilial);
	
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT 1 ");
		sql.append(" FROM controle_carga cc ");
		sql.append(" JOIN manifesto m on m.id_controle_carga = cc.id_controle_carga ");
		sql.append(" WHERE cc.id_controle_carga = :idControleCarga ");
		sql.append(" AND m.id_filial_origem = :idFilial ");
		sql.append(" AND EXISTS(SELECT 1 FROM pre_manifesto_documento pmd, manifesto md ");
		sql.append(" 		   WHERE pmd.id_manifesto = md.id_manifesto ");
		sql.append(" 		   AND md.id_controle_carga = :idControleCarga ");
		sql.append(" 		   AND pmd.id_docto_servico = :idDoctoServico ");
		sql.append(" 		   AND md.id_filial_origem = :idFilial )  ");
		sql.append(" AND EXISTS(SELECT 1 FROM pre_manifesto_volume pmv, manifesto mv  ");
		sql.append(" 		   WHERE pmv.id_manifesto = mv.id_manifesto ");
		sql.append(" 		   AND mv.id_controle_carga = :idControleCarga ");
		sql.append(" 		   AND pmv.id_docto_servico = :idDoctoServico ");
		sql.append(" 		   AND mv.id_filial_origem = :idFilial) ");
		sql.append(" AND NOT EXISTS(SELECT 1 FROM ocorrencia_entrega oe, evento_documento_servico eds ");
		sql.append("           WHERE eds.id_ocorrencia_entrega = oe.id_ocorrencia_entrega ");
		sql.append("           AND eds.id_docto_servico = :idDoctoServico ");
		sql.append("           AND eds.bl_evento_cancelado = 'N' ");
		sql.append("           AND oe.cd_ocorrencia_entrega = 102) ");
		sql.append(" AND ROWNUM = 1 ");
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params) > 0;
}
	
	public List<Long> findIdControleCargaFechaManifestoAuto(Long idFilial, String dhInicioFechamentoAutomatico) {

		StringBuffer sql = new StringBuffer("SELECT cc.ID_CONTROLE_CARGA FROM CONTROLE_CARGA cc, MANIFESTO m, MANIFESTO_ENTREGA me ");
		sql.append("WHERE cc.ID_CONTROLE_CARGA = m.ID_CONTROLE_CARGA " );
		sql.append(" AND m.ID_MANIFESTO = me.ID_MANIFESTO_ENTREGA " );
		sql.append(" AND cc.ID_FILIAL_ORIGEM = " ).append(idFilial);
		sql.append(" AND cc.TP_STATUS_CONTROLE_CARGA = 'TC' " );
		sql.append(" AND m.TP_MANIFESTO_ENTREGA = 'EP' " );
		sql.append(" AND cc.DH_GERACAO > to_date('").append(dhInicioFechamentoAutomatico).append(" 00:00:00','dd/MM/yyyy hh24:mi:ss') " );
		sql.append(" AND NOT (EXISTS (SELECT	1 FROM MANIFESTO_ENTREGA_DOCUMENTO md " );
				sql.append("WHERE me.ID_MANIFESTO_ENTREGA = md.ID_MANIFESTO_ENTREGA " );
				sql.append("AND (md.ID_OCORRENCIA_ENTREGA IS NULL))) " );

		return getAdsmHibernateTemplate()
				.getSessionFactory().getCurrentSession()
				.createSQLQuery(sql.toString())
				.addScalar("ID_CONTROLE_CARGA", Hibernate.LONG).list();
    }

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
