package com.mercurio.lms.coleta.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.core.model.hibernate.VarcharI18nUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaDetalhadoDTO;
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaResumidoDTO;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.dto.ConsultarColetaDTO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PedidoColetaDAO extends BaseCrudDao<PedidoColeta, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PedidoColeta.class;
	}

	public Map findMeioTransporteByIdPedidoColeta(Long idPedidoColeta) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select new map(mt.id as idMeioTransporte ")
		   .append("			  , mt.nrFrota as nrFrota ")
		   .append("			  , mt.nrIdentificador as nrIdentificador ")
		   .append("			  , pc.tpModoPedidoColeta as tpModoPedidoColeta ) ")
		   .append("   from PedidoColeta pc ")
		   .append("        join pc.eventoColetas ec ")
		   .append("        left outer join ec.meioTransporteRodoviario mtr ")
		   .append("        left outer join mtr.meioTransporte mt ")
		   .append("  where pc.id = :idPedidoColeta ")
		   .append("    and pc.tpStatusColeta in ('EX', 'NT', 'ED', 'AD') ")
		   .append("    and ec.tpEventoColeta = 'EX' ");
		Map parameterValues = new HashMap();
		parameterValues.put("idPedidoColeta", idPedidoColeta);
		return (Map) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), parameterValues);
	}
	
	public Map findColetaNaoFinalizadaByFilialNrColeta(Long idFilialResponsavel, Long nrColeta, DateTime dhEvento) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select new map(pc.id as idPedidoColeta ")
		   .append("			  , pc.nrColeta as nrColeta ")
		   .append("			  , mt.id as idMeioTransporte ")
		   .append("			  , mt.nrFrota as nrFrota ")
		   .append("			  , mt.nrIdentificador as nrIdentificador ")
		   .append("			  , pc.tpModoPedidoColeta as tpModoPedidoColeta ")
		   .append("			  , ec.dhEvento as dhEvento ) ")
		   .append("   from PedidoColeta pc ")
		   .append("        join pc.eventoColetas ec ")
		   .append("        join ec.meioTransporteRodoviario mtr ")
		   .append("        join mtr.meioTransporte mt ")
		   .append("  where pc.nrColeta = :nrColeta ")
		   .append("    and pc.tpStatusColeta in ('EX', 'NT', 'ED', 'AD') ")
		   .append("    and pc.filialByIdFilialResponsavel.id = :idFilialResponsavel ")
		   .append("    and ec.tpEventoColeta = 'EX' ")
		   .append("    and ec.dhEvento.value >= :dhEvento ");
		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), new String[]{"nrColeta", "idFilialResponsavel", "dhEvento"}, new Object[]{nrColeta, idFilialResponsavel, dhEvento});
		Map map = null;
		if(result != null && result.size() > 0) {
			map = (Map) result.get(0);
		}
		return map;
	}
	
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("eventoColetas", FetchMode.SELECT);
		map.put("cliente", FetchMode.JOIN);	
		map.put("cliente.pessoa", FetchMode.JOIN);
		map.put("filialByIdFilialResponsavel",FetchMode.JOIN);
		map.put("rotaColetaEntrega",FetchMode.JOIN);
		map.put("manifestoColeta",FetchMode.JOIN);
		map.put("moeda",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map map) {
		// Utilizado em Cancelar Coleta.
		map.put("usuario",FetchMode.JOIN);
		map.put("moeda",FetchMode.JOIN);
		map.put("detalheColetas",FetchMode.SELECT);
		map.put("filialByIdFilialResponsavel",FetchMode.JOIN);
		map.put("filialByIdFilialResponsavel.pessoa",FetchMode.JOIN);
		map.put("filialByIdFilialSolicitante",FetchMode.JOIN);
		map.put("filialByIdFilialSolicitante.pessoa",FetchMode.JOIN);
		map.put("rotaColetaEntrega",FetchMode.JOIN);
		map.put("manifestoColeta",FetchMode.JOIN);
		map.put("manifestoColeta.filial",FetchMode.JOIN);
		map.put("manifestoColeta.controleCarga",FetchMode.JOIN);
		map.put("manifestoColeta.controleCarga.filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("manifestoColeta.controleCarga.meioTransporteByIdTransportado",FetchMode.JOIN);
		map.put("cliente",FetchMode.JOIN);
		map.put("cliente.pessoa",FetchMode.JOIN);
		map.put("municipio",FetchMode.JOIN);
		map.put("municipio.unidadeFederativa",FetchMode.JOIN);
	}
	
	
	public void removeAll(ItemList items){
		if (items.getRemovedItems().size() > 0) {
			for (Iterator iter = items.getRemovedItems().iterator(); iter.hasNext();) {
				DetalheColeta detalheColeta = (DetalheColeta) iter.next();
				removeListDetalheColeta(detalheColeta.getNotaFiscalColetas());
			}
		}
		
		removeListDetalheColeta(items.getRemovedItems());
		getAdsmHibernateTemplate().flush();
	}

	/**
	 * Salva um Pedido de Coleta, Servico Adicional de Coleta, Detalhes de Coleta,
	 * Nota Fiscal de Coleta e AWB de Coleta.
	 * 
	 * @param pedidoColeta
	 * @param items
	 * @return
	 */
	public PedidoColeta storeAll(PedidoColeta pedidoColeta, ItemList items, ItemListConfig config) {
		super.store(pedidoColeta);
		super.store(pedidoColeta.getServicoAdicionalColetas());
		super.store(pedidoColeta.getPedidoColetaProdutos());
		
		if (items.getRemovedItems().size() > 0) {
			for (Iterator iter = items.getRemovedItems().iterator(); iter.hasNext();) {
				DetalheColeta detalheColeta = (DetalheColeta) iter.next();
				removeListDetalheColeta(detalheColeta.getNotaFiscalColetas());
			}
		}
		
		removeListDetalheColeta(items.getRemovedItems());		
				
		for(Iterator iter = items.iterator(pedidoColeta.getIdPedidoColeta(), config); iter.hasNext();) {
			DetalheColeta detalheColeta = (DetalheColeta) iter.next();			
			super.store(detalheColeta);
			
			// Salva um Evento de Coleta caso exista Liberação de Coleta para Destino Bloqueado.
			if(detalheColeta.getEventoColeta() != null) {
				EventoColeta eventoColeta = detalheColeta.getEventoColeta();
				eventoColeta.setPedidoColeta(detalheColeta.getPedidoColeta());
				eventoColeta.setDetalheColeta(detalheColeta);
				super.store(detalheColeta.getEventoColeta());
			}
			
			storeListDetalheColeta(detalheColeta.getNotaFiscalColetas());
		}
		getAdsmHibernateTemplate().flush();
		return pedidoColeta;
	}
		
	/**
	 * Salva um Pedido de Coleta, Servico Adicional de Coleta, Detalhes de Coleta,
	 * Nota Fiscal de Coleta e AWB de Coleta.
	 * 
	 * @param pedidoColeta
	 * @return
	 */
	public PedidoColeta storeAll(PedidoColeta pedidoColeta) {
		super.store(pedidoColeta);
		super.store(pedidoColeta.getServicoAdicionalColetas());
		super.store(pedidoColeta.getPedidoColetaProdutos());
		super.store(pedidoColeta.getDetalheColetas());
		
		List listDetalheColeta = pedidoColeta.getDetalheColetas();
		List listNotaFiscalColetas = null;
		List listAwbColetas = null;
		for(int j=0; j < listDetalheColeta.size(); j++) {
			DetalheColeta detalheColeta = (DetalheColeta)listDetalheColeta.get(j);
			
			// Salva um Evento de Coleta caso exista Liberação de Coleta para Destino Bloqueado.
			if(detalheColeta.getEventoColeta() != null) {
				EventoColeta eventoColeta = detalheColeta.getEventoColeta();
				eventoColeta.setPedidoColeta(detalheColeta.getPedidoColeta());
				eventoColeta.setDetalheColeta(detalheColeta);
				super.store(detalheColeta.getEventoColeta());
			}
			
			listNotaFiscalColetas = detalheColeta.getNotaFiscalColetas();
			listAwbColetas = detalheColeta.getAwbColetas();
		}
		if(listNotaFiscalColetas != null) {
			super.store(listNotaFiscalColetas);
		}
		if(listAwbColetas != null) {
			super.store(listAwbColetas);
		}

		getAdsmHibernateTemplate().flush();
		return pedidoColeta;
	}

	/**
	 * Salva lista de Detalhes de Coleta
	 * 
	 * @param newOrModifiedItems
	 */
	private void storeListDetalheColeta(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
    @SuppressWarnings("unchecked")
    public List<PedidoColeta> findByIdControleCarga(Long idControleCarga) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.setFetchMode("manifestoColeta", FetchMode.JOIN);
        criteria.setFetchMode("manifestoColeta.controleCarga", FetchMode.JOIN)
                .createAlias("manifestoColeta.controleCarga", "controleCarga");
        criteria.add(Restrictions.in("tpStatusColeta", new Object[]{ "FI", "NT", "EX" }));
        criteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));

        return (List<PedidoColeta>) findByDetachedCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<PedidoColeta> findByIdControleCargaAndIdCliente(Long idControleCarga, Long idCliente) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.setFetchMode("cliente", FetchMode.JOIN);
        criteria.setFetchMode("manifestoColeta", FetchMode.JOIN);
        criteria.setFetchMode("manifestoColeta.controleCarga", FetchMode.JOIN)
                .createAlias("manifestoColeta.controleCarga", "controleCarga");

        criteria.add(Restrictions.eq("cliente.idCliente", idCliente));
        criteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));
        criteria.add(Restrictions.in("tpSituacao", new Object[]{ "FI", "NT", "EX" }));

        return (List<PedidoColeta>) findByDetachedCriteria(criteria);
    }

	/**
	 * Busca o objeto PedidoColeta a partir do id sem fazer fetch.
	 * @param idPedidoColeta
	 * @return
	 */
	public PedidoColeta findByIdBasic(Long idPedidoColeta){
		return (PedidoColeta)getAdsmHibernateTemplate().get(PedidoColeta.class, idPedidoColeta);
	}

	/**
	 * Remove uma lista de Detalhes de Coleta
	 * 
	 * @param removeItems
	 */
	private void removeListDetalheColeta(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

	public List<Map<String, Object>> findLookupPedidoColeta(Long idCliente) {
		Filial filial = SessionUtils.getFilialSessao();
        return queryExecuteLookupPedidoColeta(idCliente, filial);
	}

	public List<Map<String, Object>> findLookupPedidoColeta(Long idCliente, Filial filial) {
		return queryExecuteLookupPedidoColeta(idCliente, filial);
	}
	private List<Map<String, Object>> queryExecuteLookupPedidoColeta(Long idCliente, Filial filial){

		DateTime dhAnterior = JTDateTimeUtils.getDataHoraAtual(filial);
		if(filial.getNrHrColeta() != null){
			dhAnterior = dhAnterior.minusHours(filial.getNrHrColeta());
		}else{
			dhAnterior = dhAnterior.minusDays(1);
		}
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual(filial);

		StringBuffer sql = new StringBuffer()
				.append(" select new map(pc.id as idPedidoColeta ")
				.append("              , pc.nrColeta as nrColeta ")
				.append("              , ec.dhEvento as dhEvento ")
				.append("              , pc.blProdutoDiferenciado as blProdutoDiferenciado) ")
				.append("   from PedidoColeta pc ")
				.append("        join pc.eventoColetas ec ")
				.append("  where  ")
				.append("    pc.filialByIdFilialResponsavel.id = :idFilialResponsavel ")
				.append("    and pc.tpStatusColeta in ('EX', 'NT', 'ED', 'AD') ")
				.append("    and ec.tpEventoColeta in ('EX', 'CP') ")
				.append("    and ec.dhEvento.value >= :dhAnterior ")
				.append("    and ec.dhEvento.value <= :dhAtual ");
		if (idCliente != null) {
			sql.append("    and pc.cliente.id = :idCliente ");
		}
		sql.append("order by pc.idPedidoColeta desc ");

		List<Map<String, Object>> result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), new String[]{
				"idFilialResponsavel",
				"dhAnterior",
				"dhAtual",
				"idCliente"}, new Object[]{
				filial.getIdFilial(),
				dhAnterior,
				dhAtual,
				idCliente});

		return result;
	}
	/**
	 * Retorna o PedidoColeta passando o ID do Cliente
	 * @param idCliente
	 * @return List: Pedidos de Coleta do Cliente.
	 */	
	public List<PedidoColeta> findPedidoColetaByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.add( Restrictions.eq("cliente.idCliente", idCliente));
		return super.findByDetachedCriteria(dc);		
	}

	/**
	 * Retorna o último PedidoColeta do Cliente. Caso não encontre, retorna null;
	 * @param idCliente
	 * @return List: Pedidos de Coleta do Cliente.
	 */	
	public PedidoColeta findUltimoPedidoColetaByIdCliente(Long idCliente) {
		DetachedCriteria dcMax = DetachedCriteria.forClass(PedidoColeta.class);
		dcMax.setProjection(Projections.max("dhPedidoColeta.value"));
		dcMax.add(Restrictions.eq("cliente.idCliente", idCliente));
		
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.add(Restrictions.eq("cliente.idCliente", idCliente));
		dc.add(Property.forName("dhPedidoColeta.value").eq(dcMax));
		List<PedidoColeta> result = super.findByDetachedCriteria(dc);
		if (result.size()>0){
			return result.get(0);
		}
		return null;
	}

	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param idManifestoColeta
	 * @return Integer com o numero de registos com os dados da grid.
	 */
	public Integer getRowCountByManifestoColeta(Long idManifestoColeta){
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class)
			.setProjection(Projections.rowCount()) 
			.setFetchMode("rotaColetaEntrega",FetchMode.JOIN)
			.setFetchMode("filialByIdFilialResponsavel",FetchMode.JOIN)
			.setFetchMode("cliente",FetchMode.JOIN)
			.setFetchMode("cliente.pessoa",FetchMode.JOIN)
			.setFetchMode("manifestoColeta",FetchMode.JOIN)
			.createAlias("manifestoColeta","mc")
			.add(Restrictions.eq("mc.idManifestoColeta",idManifestoColeta))
			.add(Restrictions.eq("tpStatusColeta", "NM"))
			.add(Restrictions.or(Restrictions.isNull("situacaoAprovacao"), Restrictions.eq("situacaoAprovacao", "A")) );

		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}

	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idManifestoColeta seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "NM". 
	 * 
	 * @param Long idManifestoCaleta
	 * @param String tpStatusColeta
	 * @param FindDefinition findDefinition
	 * @return ResultSetPage com os dados da grid.
	 */
	public ResultSetPage findPaginatedByManifestoColeta(Long idManifestoColeta, FindDefinition findDefinition){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class)
			.setFetchMode("rotaColetaEntrega",FetchMode.JOIN)
			.setFetchMode("moeda",FetchMode.JOIN)
			.setFetchMode("filialByIdFilialResponsavel",FetchMode.JOIN)
			.setFetchMode("cliente",FetchMode.JOIN)
			.setFetchMode("cliente.pessoa",FetchMode.JOIN)
			.setFetchMode("manifestoColeta",FetchMode.JOIN)
			.createAlias("manifestoColeta","mc")
			.add(Restrictions.eq("mc.idManifestoColeta",idManifestoColeta))
			.add(Restrictions.eq("tpStatusColeta", "NM"))
			.add(Restrictions.or(Restrictions.isNull("situacaoAprovacao"), Restrictions.eq("situacaoAprovacao", "A")) );
		
		
		return super.findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());	
	}

	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param idRotaColetaEntrega
	 * @return Integer com o numero de registos com os dados da grid.
	 */
	public Integer getRowCountByRotaColetaEntrega(Long idRotaColetaEntrega, Long idPedidoColeta, Long idFilialResponsavel){
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class, "pedidoColeta")
			.setProjection(Projections.rowCount()) 
			.add(Restrictions.eq("pedidoColeta.tpStatusColeta", "AB"))
			.add(Restrictions.le("pedidoColeta.dtPrevisaoColeta", JTDateTimeUtils.getDataAtual()));

		if (idPedidoColeta!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.idPedidoColeta", idPedidoColeta));
		}
		
		if (idRotaColetaEntrega!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega));
		}
		
		if (idFilialResponsavel!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.filialByIdFilialResponsavel.id", idFilialResponsavel));
		}
		
		List<Integer> result = super.findByDetachedCriteria(detachedCriteria);
		return result.get(0);	
	}

	/**
	 * 
	 * @param idRotaColetaEntrega
	 * @param idPedidoColeta
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedByRotaColetaEntrega(Long idRotaColetaEntrega, Long idPedidoColeta, Long idFilialResponsavel, FindDefinition findDefinition){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class, "pedidoColeta")
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("idPedidoColeta"), "idPedidoColeta")
					.add(Projections.property("filialByIdFilialResponsavel.sgFilial"), "sgFilial")
					.add(Projections.property("nrColeta"), "nrColeta")
					.add(Projections.property("pessoa.nmPessoa"), "nmPessoa")
					.add(Projections.property("rotaColetaEntrega.dsRota"), "dsRota")
					.add(Projections.property("edColeta"), "edColeta")
					.add(Projections.property("nrEndereco"), "nrEndereco")
					.add(Projections.property("dsBairro"), "dsBairro")
					.add(Projections.property("dsComplementoEndereco"), "dsComplementoEndereco")
					.add(Projections.property("qtTotalVolumesVerificado"), "qtTotalVolumesVerificado")
					.add(Projections.property("psTotalVerificado"), "psTotalVerificado")
					.add(Projections.property("vlTotalVerificado"), "vlTotalVerificado")
					.add(Projections.property("moeda.dsSimbolo"), "dsSimbolo")
					.add(Projections.property("moeda.sgMoeda"), "sgMoeda")
					.add(Projections.property("dtPrevisaoColeta"), "dtPrevisaoColeta"))
			.createAlias("pedidoColeta.filialByIdFilialResponsavel", "filialByIdFilialResponsavel")
			.createAlias("pedidoColeta.rotaColetaEntrega", "rotaColetaEntrega")
			.createAlias("pedidoColeta.cliente", "cliente")
			.createAlias("pedidoColeta.cliente.pessoa", "pessoa")
			.createAlias("pedidoColeta.moeda","moeda")
			.setFetchMode("pedidoColeta.cliente",FetchMode.JOIN)
			.setFetchMode("pedidoColeta.cliente.pessoa",FetchMode.JOIN)
			.add(Restrictions.eq("pedidoColeta.tpStatusColeta", "AB"))
			.add(Restrictions.le("pedidoColeta.dtPrevisaoColeta", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.or(Restrictions.isNull("pedidoColeta.situacaoAprovacao"), Restrictions.eq("pedidoColeta.situacaoAprovacao", "A")))
			.addOrder(Order.asc("sgFilial"))
			.addOrder(Order.asc("nrColeta"));

		detachedCriteria.setResultTransformer(new AliasToTypedFlatMapResultTransformer());

		if (idPedidoColeta!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.idPedidoColeta", idPedidoColeta));
		}

		if (idRotaColetaEntrega!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega));
		}

		if (idFilialResponsavel!=null) {
			detachedCriteria.add(Restrictions.eq("pedidoColeta.filialByIdFilialResponsavel.id", idFilialResponsavel));
		}

		return super.findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());	
	}

	/**
	 * Retorna uma list com os objetos.
	 * Exige que um idManifestoColeta seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "NM". 
	 * 
	 * @param Long idManifestoCaleta
	 * @return List com os dados.
	 */
	public List findByManifestoColeta(Long idManifestoColeta){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class)
			.setFetchMode("rotaColetaEntrega",FetchMode.JOIN)
			.setFetchMode("cliente",FetchMode.JOIN)
			.setFetchMode("cliente.pessoa",FetchMode.JOIN)
			.setFetchMode("moeda",FetchMode.JOIN)
			.setFetchMode("manifestoColeta",FetchMode.JOIN)
			.add(Restrictions.eq("manifestoColeta.idManifestoColeta",idManifestoColeta))
			.add(Restrictions.eq("tpStatusColeta","NM"))
			.add(Restrictions.or(Restrictions.isNull("situacaoAprovacao"), Restrictions.eq("situacaoAprovacao", "A")) );

		//detachedCriteria.setResultTransformer(new AliasToBeanResultTransformer(PedidoColeta.class));
		return super.findByDetachedCriteria(detachedCriteria);
	}

	private SqlTemplate getSqlTemplateByColetasProgramadas(Boolean blAlteradoPosProgramacao) {
		StringBuffer projecao = new StringBuffer() 
			.append("new map(") 
			.append("filialResponsavel.sgFilial as sgFilialResponsavel, ")
			.append("pedidoColeta.idPedidoColeta as idPedidoColeta, ")
			.append("pedidoColeta.nrColeta as nrColeta, ")
			.append("pedidoColeta.edColeta as edColeta, ")
			.append("pedidoColeta.nrEndereco as nrEndereco, ")
			.append("pedidoColeta.dsComplementoEndereco as dsComplementoEndereco, ")
			.append("meioTransporteByIdTransportado.nrIdentificador as nrIdentificadorByIdTransportado, ")
			.append("meioTransporteByIdTransportado.nrFrota as nrFrotaByIdTransportado, ")
			.append("pessoa.nrIdentificacao as nrIdentificacaoCliente, ")
			.append("pessoa.tpIdentificacao as tpIdentificacaoCliente, ")
			.append("pessoa.nmPessoa as nmPessoaCliente, ")
			.append("pedidoColeta.qtTotalVolumesVerificado as qtTotalVolumesVerificado, ")
			.append("pedidoColeta.psTotalVerificado as psTotalVerificado, ") 
			.append("moeda.dsSimbolo as dsSimboloMoeda, ")
			.append("moeda.sgMoeda as sgMoeda, ")
			.append("pedidoColeta.vlTotalVerificado as vlTotalVerificado, ") 
			.append("pedidoColeta.dhColetaDisponivel as dhColetaDisponivel, ") 
			.append("pedidoColeta.hrLimiteColeta as hrLimiteColeta) ");

		StringBuffer from = new StringBuffer()
			.append(PedidoColeta.class.getName()).append(" as pedidoColeta ")
			.append("join pedidoColeta.moeda as moeda ")
			.append("join pedidoColeta.rotaColetaEntrega as rotaColetaEntrega ")
			.append("join pedidoColeta.filialByIdFilialResponsavel as filialResponsavel ")
			.append("join pedidoColeta.cliente as cliente ")
			.append("join cliente.pessoa as pessoa ")
			.append("left join pedidoColeta.manifestoColeta as manifestoColeta ")
			.append("left join manifestoColeta.controleCarga as controleCarga ")
			.append("left join controleCarga.meioTransporteByIdTransportado as meioTransporteByIdTransportado ");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		sql.addCriteria("pedidoColeta.blAlteradoPosProgramacao", "=", blAlteradoPosProgramacao);
		sql.addCriteria("pedidoColeta.filialByIdFilialResponsavel.id", "=", SessionUtils.getFilialSessao().getIdFilial());
		sql.addCustomCriteria(" ( pedidoColeta.situacaoAprovacao is null or pedidoColeta.situacaoAprovacao = 'A' ) ");
		sql.addCustomCriteria("trunc(cast (pedidoColeta.dhPedidoColeta.value AS date)) = ? ", JTDateTimeUtils.getDataAtual());
		sql.addOrderBy("filialResponsavel.sgFilial");
		sql.addOrderBy("pedidoColeta.nrColeta");
		return sql;
	}

	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idControleCarga seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "MA" ou "TR"
	 * 
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedByColetasProgramadas(FindDefinition findDefinition, Boolean blAlteradoPosProgramacao) {
		SqlTemplate sql = getSqlTemplateByColetasProgramadas(blAlteradoPosProgramacao);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
	}

	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountByColetasProgramadas(Boolean blAlteradoPosProgramacao) {
		SqlTemplate sql = getSqlTemplateByColetasProgramadas(blAlteradoPosProgramacao);
		Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCount;
	}

	private SqlTemplate addSqlColetasNaoRealizadas(Map criteria) {
		SqlTemplate sql = new SqlTemplate();

		StringBuffer projecao = new StringBuffer();
		projecao.append("distinct new map(filialByIdFilialResponsavel.sgFilial as sgFilial, ");
		projecao.append("pedidoColeta.idPedidoColeta as idPedidoColeta, ");
		projecao.append("cliente.idCliente as idCliente, ");
		projecao.append("pedidoColeta.nrColeta as nrColeta, ");
		projecao.append("pessoa.nmPessoa as nmPessoa, ");
		projecao.append("pedidoColeta.tpPedidoColeta as tpPedidoColeta, ");
		projecao.append("pedidoColeta.dhPedidoColeta as dhPedidoColeta, ");
		projecao.append("pedidoColeta.psTotalInformado as psTotalInformado, ");
		projecao.append("pedidoColeta.qtTotalVolumesInformado as qtTotalVolumesInformado, ");
		projecao.append("moeda.dsSimbolo as dsSimbolo, ");
		projecao.append("moeda.sgMoeda as sgMoeda, ");
		projecao.append("pedidoColeta.vlTotalInformado as vlTotalInformado, ");
		projecao.append("pedidoColeta.tpStatusColeta as tpStatusColeta) ");
		sql.addProjection(projecao.toString());

		sql.addFrom(PedidoColeta.class.getName() + " as pedidoColeta " +
				"inner join pedidoColeta.cliente as cliente " +
				"inner join cliente.pessoa as pessoa " +
				"inner join pedidoColeta.filialByIdFilialResponsavel as filialByIdFilialResponsavel " +
				"inner join pedidoColeta.moeda as moeda " +
				"left join pedidoColeta.eventoColetas as eventoColeta");

		if (criteria.get("tpOpcaoColeta").equals("N")) {
			sql.addCustomCriteria("(pedidoColeta.tpStatusColeta = 'AB') AND (eventoColeta.tpEventoColeta = 'RC')");
		} else {
			sql.addCustomCriteria("((pedidoColeta.tpStatusColeta = 'AB') OR (pedidoColeta.tpStatusColeta = 'MA') OR (pedidoColeta.tpStatusColeta = 'TR'))");
			sql.addCustomCriteria("(eventoColeta.tpEventoColeta = 'RC')");
		}
		sql.addOrderBy("pedidoColeta.nrColeta"); 
		return sql;
	}

	public ResultSetPage findPaginatedColetasNaoRealizadas(Map criteria, FindDefinition findDefinition) {
		SqlTemplate sql = new SqlTemplate();
		sql = addSqlColetasNaoRealizadas(criteria);

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
	}

	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountColetasNaoRealizadas(Map criteria){
		SqlTemplate sqlTemplate = this.addSqlColetasNaoRealizadas(criteria);
		List<Long> result = this.getAdsmHibernateTemplate().find("select count(distinct pedidoColeta.idPedidoColeta) as rowCount " + sqlTemplate.getSql(false));
		Long count = result.get(0);
		return Integer.valueOf(count.intValue()); 
	}

	/**
	 * Busca os pedidos de coleta para o cliente e na data informada.
	 * Considera que as coletas Milk Runs foram feitas na hora 00:00:00
	 * juntamente com o fuso passado por parâmetro.
	 * @param dataProcessoColeta
	 * @param idCliente
	 * @return
	 */
	public List<PedidoColeta> findPedidoColetaMilkRun(Long idCliente, YearMonthDay dataProcessoColeta, DateTimeZone dateTimeZone) {
		DateTime dataHoraProcessoColeta = JTDateTimeUtils.yearMonthDayToDateTime(dataProcessoColeta, dateTimeZone);
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.createAlias("cliente","cliente")
		.add(Restrictions.eq("cliente.id",idCliente))
		.add(Restrictions.eq("tpModoPedidoColeta", "MI"))
		.add(Restrictions.eq("dhPedidoColeta", dataHoraProcessoColeta));

		List<PedidoColeta> listPedido = this.getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return listPedido;
	}

	/**
	 * Busca os pedidos de coleta automática para o cliente e na data informada.
	 * Assume-se que a data do processo de coleta automática sempre tenha hora 00:00:00
	 * juntamente com o fuso passado por parâmetro.
	 * @param dataProcessoColeta
	 * @param idCliente
	 * @return
	 */
	public List<PedidoColeta> findPedidoColetaAutomatica(Long idCliente, YearMonthDay dataProcessoColeta, DateTimeZone dateTimeZone) {
		DateTime dataHoraProcessoColeta = JTDateTimeUtils.yearMonthDayToDateTime(dataProcessoColeta, dateTimeZone);
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.createAlias("cliente","cliente")
		.add(Restrictions.eq("cliente.id",idCliente))
		.add(Restrictions.eq("tpModoPedidoColeta", "AU"))
		.add(Restrictions.eq("dhPedidoColeta", dataHoraProcessoColeta));

		List<PedidoColeta> listPedido = this.getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return listPedido;
	}

	/**
	 * Find usado na tela de Manter Coletas ao clicar em um registro da listagem para
	 * carregar a aba de Detalhamento de Pedido de Coleta usando ID do Pedido de Coleta. 
	 * @param idPedidoColeta
	 * @return
	 */
	public PedidoColeta findDetalhamentoByIdPedidoColeta(Long idPedidoColeta) {
		PedidoColeta pedidoColeta = null;

		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);

		dc.add(Restrictions.eq("id", idPedidoColeta));
		dc.setFetchMode("municipio", FetchMode.JOIN);
		dc.setFetchMode("enderecoPessoa", FetchMode.JOIN);
		dc.setFetchMode("enderecoPessoa.tipoLogradouro", FetchMode.JOIN);
		dc.setFetchMode("municipio.unidadeFederativa", FetchMode.JOIN);
		dc.setFetchMode("moeda", FetchMode.JOIN);
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialResponsavel.pessoa", FetchMode.JOIN);
		dc.setFetchMode("cotacao", FetchMode.JOIN);
		dc.setFetchMode("cotacao.filialByIdFilialOrigem", FetchMode.JOIN);

		List<PedidoColeta> result = super.findByDetachedCriteria(dc);
		if(result.size() == 1) {
			pedidoColeta = result.get(0);
		}
		return pedidoColeta;
	}	

	/**
	 * Metodo que retorna dados de Pedido Coleta para a tab de Listagem da tela Manter Coletas
	 * utilizando a função IN do SQL para concatenar os checkboxes de filtro.
	 * 
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */	
	public DetachedCriteria getDetachedCriteriaManterColetas(Map criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);

		if (criteria.get("filialByIdFilialResponsavel") != null) {
			String idFilial = (String)((Map)criteria.get("filialByIdFilialResponsavel")).get("idFilial");
			if (idFilial != null && !idFilial.equals("")) {
				dc.add(Restrictions.eq("filialByIdFilialResponsavel.id", Long.valueOf(idFilial)));
			}
		}
		
		String nrColeta = (String)criteria.get("nrColeta");
		if (nrColeta != null && !nrColeta.equals("")) {
			dc.add(Restrictions.eq("nrColeta", Long.valueOf(nrColeta)));
		}
		
		String strDtInclusaoInicial = (String)criteria.get("dtInclusaoInicial");
		if (strDtInclusaoInicial != null && !strDtInclusaoInicial.equals("")) {
			YearMonthDay dtInclusaoInicial = (YearMonthDay) criteria.get("dtInclusaoInicial");
			dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_PEDIDO_COLETA AS DATE)) >= ?", dtInclusaoInicial, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
		}
		String strDtInclusaoFinal = (String)criteria.get("dtInclusaoFinal");
		if (strDtInclusaoFinal != null && !strDtInclusaoFinal.equals("")) {
			YearMonthDay dtInclusaoFinal = (YearMonthDay) criteria.get("dtInclusaoFinal");
			dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_PEDIDO_COLETA AS DATE)) <= ?", dtInclusaoFinal, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
		}
				
		List listStatus = new ArrayList();
		Boolean chkAberta = Boolean.valueOf((String)criteria.get("chkAberta"));
		Boolean chkTransmitida = Boolean.valueOf((String)criteria.get("chkTransmitida"));
		Boolean chkManifestdada = Boolean.valueOf((String)criteria.get("chkManifestdada"));
		Boolean chkExecutada = Boolean.valueOf((String)criteria.get("chkExecutada"));
		Boolean chkCancelada = Boolean.valueOf((String)criteria.get("chkCancelada"));
		if (chkAberta.booleanValue()) {
			listStatus.add("AB");
		}
		if (chkTransmitida.booleanValue()) {
			listStatus.add("TR");
		}
		if (chkManifestdada.booleanValue()) {
			listStatus.add("MA");
		}
		if (chkExecutada.booleanValue()) {
			listStatus.add("EX");
		}
		if (chkCancelada.booleanValue()) {
			listStatus.add("CA");
		}		

		if ( chkAberta.booleanValue() || chkTransmitida.booleanValue() || chkManifestdada.booleanValue() ||
			chkExecutada.booleanValue() || chkCancelada.booleanValue() ) {
			
			dc.add(Restrictions.in("tpStatusColeta", listStatus));
		}
		
		if (criteria.get("rotaColetaEntrega") != null) {
			String idRotaColetaEntrega = (String)((Map)criteria.get("rotaColetaEntrega")).get("idRotaColetaEntrega");
			if (idRotaColetaEntrega != null && !idRotaColetaEntrega.equals("")) {
				dc.add(Restrictions.eq("rotaColetaEntrega.id", Long.valueOf(idRotaColetaEntrega)));
			}
		}
		
		if (((Map)((Map)criteria.get("rotaColetaEntrega")).get("regiaoFilialRotaColEnts")).get("regiaoColetaEntregaFil") != null) {
			String idRegiaoColetaEntregaFil = (String)((Map)((Map)((Map)criteria.get("rotaColetaEntrega")).get("regiaoFilialRotaColEnts")).get("regiaoColetaEntregaFil")).get("idRegiaoColetaEntregaFil");
			if (idRegiaoColetaEntregaFil != null && !idRegiaoColetaEntregaFil.equals("")) {
				dc.createCriteria("rotaColetaEntrega")
					.createCriteria("regiaoFilialRotaColEnts")
						.createCriteria("regiaoColetaEntregaFil")
							.add(Restrictions.eq("id", Long.valueOf(idRegiaoColetaEntregaFil)));
			}
		}		
		
		if (criteria.get("cliente") != null) {
			String idCliente = (String)((Map)criteria.get("cliente")).get("idCliente");
			if (idCliente != null && !idCliente.equals("")) {
				dc.add(Restrictions.eq("cliente.id", Long.valueOf(idCliente)));
			}
		}
		if (((Map)criteria.get("municipio")).get("unidadeFederativa") != null) {
			String idUnidadeFederativa = (String)((Map)((Map)criteria.get("municipio")).get("unidadeFederativa")).get("idUnidadeFederativa");
			if (idUnidadeFederativa != null && !idUnidadeFederativa.equals("")) {
				dc.createCriteria("municipio")
					.createCriteria("unidadeFederativa")						
							.add(Restrictions.eq("id", Long.valueOf(idUnidadeFederativa)));				
			}
		}
		
		if (criteria.get("tpPedidoColeta") != null) {
			String tpPedidoColeta = (String)criteria.get("tpPedidoColeta");
			if (tpPedidoColeta != null && !tpPedidoColeta.equals("")) {
				dc.add(Restrictions.eq("tpPedidoColeta", tpPedidoColeta));
			}
		}
		
		if (criteria.get("usuario") != null) {
			String idUsuario = (String)((Map)criteria.get("usuario")).get("idUsuario");
			if (idUsuario != null && !idUsuario.equals("")) {
				dc.add(Restrictions.eq("usuario.id", Long.valueOf(idUsuario)));
			}
		}
		
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);		
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialSolicitante", FetchMode.JOIN); 
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		return dc;
	}
	
	public ResultSetPage findPaginatedManterColetas(Map criteria, FindDefinition findDefinition) {
		DetachedCriteria dc = getDetachedCriteriaManterColetas(criteria);
		return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
	}
	
	public Integer getRowCountManterColetas(Map criteria, FindDefinition findDefinition) {
		DetachedCriteria dc = getDetachedCriteriaManterColetas(criteria);		
		dc.setProjection(Projections.rowCount()); 
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);		
	}
			
	/**
	 * @param consultaColetaDTO
	 * @param findDefinition
	 * 
	 * @return ResultSetPage
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedConsultarColeta(ConsultarColetaDTO consultaColetaDTO, FindDefinition findDefinition) {
		SqlTemplate sql = getSqlTemplateForConsultarColetas(consultaColetaDTO);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
	}
	
	/**
	 * 
	 * @param consultaColetaDTO
	 * @return Integer
	 */
	public Integer getRowCountConsultarColeta(ConsultarColetaDTO consultaColetaDTO) {
		SqlTemplate sql = getSqlTemplateForConsultarColetas(consultaColetaDTO);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
	}

	/**
	 * @param consultaColetaDTO
	 * @return SqlTemplate
	 */
	private SqlTemplate getSqlTemplateForConsultarColetas(ConsultarColetaDTO consultaColetaDTO) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("distinct new map (pc.idPedidoColeta", "idPedidoColeta");
		sql.addProjection("pc.nrColeta", "nrColeta");
		sql.addProjection("pc.tpModoPedidoColeta", "tpModoPedidoColeta");
		sql.addProjection("filialResponsavel.sgFilial", "sgFilial");
		sql.addProjection("pessoa.nmPessoa", "nmPessoa");
		sql.addProjection("cliente.idCliente", "idCliente");
		sql.addProjection("pc.tpPedidoColeta", "tpPedidoColeta");
		sql.addProjection("pc.dhPedidoColeta", "dhPedidoColeta");
		sql.addProjection("pc.psTotalVerificado", "psTotalVerificado");
		sql.addProjection("pc.qtTotalVolumesVerificado", "qtTotalVolumesVerificado");
		sql.addProjection("moeda.dsSimbolo", "dsSimbolo");
		sql.addProjection("moeda.sgMoeda", "sgMoeda");
		sql.addProjection("pc.vlTotalVerificado", "vlTotalVerificado");
		sql.addProjection("pc.tpStatusColeta", "tpStatusColeta");
		sql.addProjection("pc.usuario.idUsuario", "idUsuario");
		sql.addProjection("pc.usuario.nmUsuario", "nmUsuario");
		sql.addProjection("filialResponsavel.idFilial", "idFilial)");
	
		StringBuffer from = new StringBuffer()
		.append(PedidoColeta.class.getName() + " pc ")
		.append("inner join pc.moeda moeda ")
		.append("left join pc.usuario usuario ")
		.append("inner join pc.cliente cliente ")
		.append("inner join cliente.pessoa pessoa ")
		.append("inner join pc.filialByIdFilialResponsavel filialResponsavel ")
		.append("inner join pc.detalheColetas detalheColeta ")
		.append("left join detalheColeta.doctoServico ds ")
		.append("left join ds.ctoAwbs cto ");
			
		sql.addCriteria("pc.filialByIdFilialResponsavel.id", "=", consultaColetaDTO.getIdFilial());
		sql.addCriteria("pc.nrColeta", "=", consultaColetaDTO.getNrColeta());
		sql.addCriteria("pc.rotaColetaEntrega.id", "=", consultaColetaDTO.getIdRotaColetaEntrega());
	
		if (consultaColetaDTO.getIdRegiaoColetaEntregaFil() != null) {			
			from.append("inner join pc.rotaColetaEntrega rce ");
			from.append("inner join rce.regiaoFilialRotaColEnts rfrce ");
			
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
			
			sql.addCriteria("rfrce.regiaoColetaEntregaFil.id", "=", consultaColetaDTO.getIdRegiaoColetaEntregaFil());
			sql.addCriteria("rfrce.dtVigenciaInicial", "<=", dataAtual);
			sql.addCriteria("rfrce.dtVigenciaFinal", ">=", dataAtual);
		}
				
		sql.addCriteria("pc.cliente.id", "=", consultaColetaDTO.getIdCliente());
		sql.addCriteria("detalheColeta.servico.id", "=", consultaColetaDTO.getIdServico());
		sql.addCriteria("detalheColeta.filial.id", "=", consultaColetaDTO.getIdDestino());
		sql.addCriteria("pc.usuario.id", "=", consultaColetaDTO.getIdUsuario());		
		sql.addCriteria("pc.dhPedidoColeta.value", ">=", consultaColetaDTO.getDhPedidoColetaInicial());
		sql.addCriteria("pc.dhPedidoColeta.value", "<=", consultaColetaDTO.getDhPedidoColetaFinal());
		
		if (CollectionUtils.isNotEmpty(consultaColetaDTO.getTpsStatusColeta())) {
			sql = SQLUtils.joinExpressionsWithComma(consultaColetaDTO.getTpsStatusColeta(), sql, "pc.tpStatusColeta");
		}
		
		if(StringUtils.isNotEmpty(consultaColetaDTO.getTpPedidoColeta())){
			sql.addCriteria("pc.tpPedidoColeta", "=", consultaColetaDTO.getTpPedidoColeta());
		}
		
		if(consultaColetaDTO.getBlSemVinculoDoctoServico()){
			StringBuilder hqlVinculo = new StringBuilder();
			hqlVinculo.append("NOT EXISTS");
			hqlVinculo.append("(SELECT docto FROM DoctoServico docto");
			hqlVinculo.append(" WHERE docto.pedidoColeta.idPedidoColeta = pc.idPedidoColeta");
			hqlVinculo.append("  AND docto.filialByIdFilialOrigem.idFilial = pc.filialByIdFilialSolicitante.idFilial)");
			
			sql.addCustomCriteria(hqlVinculo.toString());
		}
		
		if (Boolean.TRUE.equals(consultaColetaDTO.getBlProdutoPerigoso())) {
			sql.addCriteria("pc.blProdutoPerigoso", "=", true);
		}

		if (Boolean.TRUE.equals(consultaColetaDTO.getBlProdutoControlado())) {
			sql.addCriteria("pc.blProdutoControlado", "=", true);
		}

		sql.addFrom(from.toString());
		sql.addOrderBy("pc.nrColeta");
		
		return sql;	
	}
	
	
	private DetachedCriteria getDetachedCriteriaByColetasVeiculosRealizadas(Long idControleCarga) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class)
		.setFetchMode("filialByIdFilialResponsavel",FetchMode.JOIN)
		.setFetchMode("cliente",FetchMode.JOIN)
		.setFetchMode("cliente.pessoa",FetchMode.JOIN)
		.setFetchMode("moeda",FetchMode.JOIN)
		.createAlias("manifestoColeta","manifestoColeta")
		.createAlias("filialByIdFilialResponsavel", "filialByIdFilialResponsavel")
		.add(Restrictions.eq("manifestoColeta.controleCarga.id", idControleCarga))
		.add(Restrictions.or( 
				Restrictions.or( 
						Restrictions.eq("tpStatusColeta", "EX"), Restrictions.eq("tpStatusColeta", "AD")),
				Restrictions.eq("tpStatusColeta", "ED") 
		));
		return detachedCriteria;
	}
	
	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idControleCarga seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "EX", "AD" ou "ED" 
	 * 
	 * @param Long idControleCarga
	 * @param FindDefinition findDefinition
	 * @return List.
	 */
	public List findPaginatedByColetasVeiculosRealizadas(Long idControleCarga){
		DetachedCriteria dc = getDetachedCriteriaByColetasVeiculosRealizadas(idControleCarga);
		dc.addOrder(Order.asc("filialByIdFilialResponsavel.sgFilial"));
		dc.addOrder(Order.asc("nrColeta"));
		return super.findByDetachedCriteria(dc);	
	}
	
	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param idControleCarga
	 * @return Integer com o numero de registos com os dados da grid.
	 */
	public Integer getRowCountByColetasVeiculosRealizadas(Long idControleCarga){
		DetachedCriteria dc = getDetachedCriteriaByColetasVeiculosRealizadas(idControleCarga);
		dc.setProjection(Projections.rowCount()); 
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}
	

	/**
	 * Adiciona os criterios de pesquisa para o método findPaginatedClientesColeta
	 * @param idFilial
	 * @param idCliente
	 * @param dataInicial
	 * @param dataFinal
	 * @param municipios
	 * @param rotaIntervaloCepList
	 * @param dc
	 * @author Rodrigo Antunes
	 */
	private void addCriteriaClientesColeta(Long idFilial, Long idCliente, YearMonthDay dataInicial, YearMonthDay dataFinal, Long idRotaColetaEntrega, List rotas, DetachedCriteria dc) {
		
		DetachedCriteria subQuery = DetachedCriteria.forClass(PedidoColeta.class,"pc2");
		subQuery.setProjection(Projections.max("pc2.id"));
		subQuery.setFetchMode("cliente",FetchMode.JOIN);
		subQuery.add(Restrictions.eqProperty("pc2.cliente.idCliente", "c.idCliente"));

		// adiciona a subQuery no select principal
		dc.add(Property.forName("pc.id").eq(subQuery));
		dc.createAlias("pc.filialByIdFilialResponsavel","f")
			.createAlias("pc.cliente","c")
			.createAlias("c.pessoa","p");
		
		// Adiciona restrição para data, conforme o seguinte:
		// Quando informado o periodo, deve buscar os clientes que tenham coleta naquele periodo
		// mas deve exibir a data da última coleta.
		if (dataInicial!=null || dataFinal!=null) {
			DetachedCriteria subQueryForDate = DetachedCriteria.forClass(PedidoColeta.class,"pc3");
			subQueryForDate.setProjection(Projections.distinct(Projections.property("pc3.cliente")));
			
			if(dataInicial!=null) {
				subQueryForDate.add(Restrictions.sqlRestriction("TRUNC (CAST(DH_PEDIDO_COLETA AS DATE)) >= ?", dataInicial, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
			}
			
			if(dataFinal!=null) {
				subQueryForDate.add(Restrictions.sqlRestriction("TRUNC (CAST(DH_PEDIDO_COLETA AS DATE)) <= ?", dataFinal, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
			}
			dc.add(Property.forName("pc.cliente").in(subQueryForDate));
		}
		
		if (idCliente!=null) {
			dc.add(Restrictions.eq("c.id", idCliente));
		}

		if (idFilial!=null) {
			dc.add(Restrictions.eq("f.idFilial", idFilial));
		}

		if (idRotaColetaEntrega!=null) {
			dc.createAlias("pc.rotaColetaEntrega","rce");
			dc.add(Restrictions.eq("rce.idRotaColetaEntrega", idRotaColetaEntrega));
		}
		
		if (rotas!=null && !rotas.isEmpty()) {
			dc.createAlias("pc.rotaColetaEntrega","rce");
			dc.add(Restrictions.in("rce.id", rotas));
		}
	}
	
	/**
	 * findpaginated personalizado para buscar os clientes que possuem coletas.
	 * busca por cliente ou por uma relação de municipios
	 * @param idCliente
	 * @param dataInicial
	 * @param dataFinal
	 * @return ResultSetPage
	 * @author Rodrigo Antunes
	 */
	public ResultSetPage findPaginatedClientesColeta(Long idFilial, Long idCliente, YearMonthDay dataInicial, YearMonthDay dataFinal, Long idRotaColetaEntrega, List rotas, FindDefinition fd) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class, "pc");

		dc.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("c.idCliente")),"idCliente")
				.add(Projections.property("p.tpIdentificacao"), "tpIdentificacaoCliente")
				.add(Projections.property("p.nrIdentificacao"), "nrIdentificacaoCliente")
				.add(Projections.property("p.nmPessoa"), "nmPessoaCliente")
				.add(Projections.property("f.sgFilial"),"sgFilial")
				.add(Projections.property("pc.nrColeta"),"nrColeta")
				.add(Projections.property("pc.dhPedidoColeta.value"),"dhPedidoColeta")
			);

		// adiciona os criterios relacionados a esta consulta
		addCriteriaClientesColeta(idFilial, idCliente, dataInicial, dataFinal, idRotaColetaEntrega, rotas,dc);
		dc.addOrder(Order.asc("p.tpIdentificacao"));
		dc.addOrder(Order.asc("p.nrIdentificacao"));
		
		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		ResultSetPage rsp = super.findPaginatedByDetachedCriteria(dc,fd.getCurrentPage(), fd.getPageSize()); 

		return rsp;
	}

	/**
	 * Row count para findPaginatedClientesColeta.
	 * @param idCliente
	 * @param dataInicial
	 * @param dataFinal
	 * @param municipios
	 * @param fd
	 * @return Integer
	 * @author Rodrigo Antunes
	 */
	public Integer getRowCountClientesColeta(Long idFilial, Long idCliente, YearMonthDay dataInicial, YearMonthDay dataFinal, Long idRotaColetaEntrega, List rotas) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class, "pc");

		dc.setProjection(Projections.countDistinct("c.idCliente"));
		// adiciona os criterios relacionados a esta consulta
		addCriteriaClientesColeta(idFilial, idCliente, dataInicial, dataFinal,idRotaColetaEntrega, rotas, dc);

		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}
	
	/**
	 * Busca pedidos de coleta do cliente informado. Filtra a pesquisa com 
	 * a data inicial e final do dh_pedido_coleta e pelo status da coleta 
	 * @param idCliente
	 * @param dataInicial
	 * @param dataFinial
	 * @param statusColeta
	 * @param tpColeta
	 * @return
	 * @author Rodrigo Antunes
	 */
	public ResultSetPage findPaginatedPedidosColetaByCliente(Long idCliente, YearMonthDay dtInicial, YearMonthDay dtFinal, List statusColeta, String tpPedidoColeta, FindDefinition fd) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class, "pc");
		dc.setProjection(Projections.projectionList()
				.add(Projections.property("pc.idPedidoColeta"),"idPedidoColeta")
				.add(Projections.property("pc.nrColeta"),"nrColeta")
				.add(Projections.property("f.sgFilial"),"sgFilialColeta")
				.add(Projections.property("m.dsSimbolo"),"dsSimbolo")
				.add(Projections.property("m.sgMoeda"),"sgMoeda")
				.add(Projections.property("pc.tpPedidoColeta"),"tpPedidoColeta")
				.add(Projections.property("pc.dhPedidoColeta.value"),"dhPedidoColeta")
				.add(Projections.property("pc.psTotalVerificado"),"psTotalVerificado")
				.add(Projections.property("pc.qtTotalVolumesVerificado"),"qtTotalVolumesVerificado")
				.add(Projections.property("pc.tpStatusColeta"),"tpStatusColeta")
				.add(Projections.property("pc.vlTotalVerificado"),"vlTotalVerificado")
		);
		
		dc.createAlias("pc.filialByIdFilialResponsavel", "f");
		dc.createAlias("pc.moeda", "m");
		
		dc.addOrder(Order.asc("pc.nrColeta"));
		addCriteriaPedidosColetaByCliente(idCliente, dtInicial, dtFinal, statusColeta, tpPedidoColeta, dc);		
		dc.setResultTransformer(new AliasToEntityMapResultTransformer());
		ResultSetPage rsp = super.findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize()); 

		return rsp;
	}

	/**
	 * Row count para findPaginatedPedidosColetaByCliente()
	 * @return
	 * @author Rodrigo Antunes
	 */
	public Integer getRowCountPedidosColetaByCliente(Long idCliente, YearMonthDay dtInicial, YearMonthDay dtFinal, List statusColeta, String tpPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class,"pc");
		dc.setProjection(Projections.rowCount());
		
		addCriteriaPedidosColetaByCliente(idCliente, dtInicial, dtFinal, statusColeta, tpPedidoColeta, dc);
		
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}
	
	/**
	 * Adiciona os criterios e restrições que serão utilizados nos métodos 
	 * findPaginatedPedidosColetaByCliente() e getRowCountPedidosColetaByCliente
	 * @param idCliente
	 * @param dtInicial
	 * @param dtFinal
	 * @param statusColeta
	 * @param tpPedidoColeta
	 * @param dc
	 * @author Rodrigo Antunes
	 */
	private void addCriteriaPedidosColetaByCliente(Long idCliente, YearMonthDay dtInicial, YearMonthDay dtFinal, List statusColetaList, String tpPedidoColeta, DetachedCriteria dc) {
		dc.add(Restrictions.eq("cliente.id", idCliente));
		
		if(statusColetaList!=null && !statusColetaList.isEmpty()) {
			dc.add(Restrictions.in("pc.tpStatusColeta", statusColetaList));
		}
		
		if(dtInicial!=null) {
			dc.add(Restrictions.sqlRestriction("TRUNC (CAST({alias}.DH_PEDIDO_COLETA AS DATE)) >= ?", dtInicial, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
		}
		
		if(dtFinal!=null) {
			dc.add(Restrictions.sqlRestriction("TRUNC (CAST({alias}.DH_PEDIDO_COLETA AS DATE)) <= ?", dtFinal, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
		}
		
		if(tpPedidoColeta!=null && !tpPedidoColeta.equals("")) {
			dc.add(Restrictions.eq("pc.tpPedidoColeta", tpPedidoColeta));
		}
		
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
	}

	public Integer getRowCountByIdBlClienteLiberadoManual(Long idPedidoColeta, Boolean blClienteLiberadoManual) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"pc")
			.setProjection(Projections.count("pc.idPedidoColeta"))
			.add(Restrictions.idEq(idPedidoColeta))
			.add(Restrictions.eq("pc.blClienteLiberadoManual", blClienteLiberadoManual));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}	
	
	
	/**
	 * 
	 * @param criteria
	 * @param isRowCount
	 * @return
	 */
	private Object[] createSqlPaginatedByColetasNaoProgramadas(Map criteria) {
		String subsql = new StringBuffer()
		.append("(SELECT count(1)")
		.append(" FROM produto")
		.append(" INNER JOIN pedido_coleta_produto")
		.append(" ON produto.id_produto = pedido_coleta_produto.id_produto")
		.append(" INNER JOIN pedido_coleta")
		.append(" ON pedido_coleta_produto.id_pedido_coleta = pedido_coleta.id_pedido_coleta")
		.append(" WHERE produto.tp_categoria               in ('PE','PR')")
		.append(" AND pedido_coleta.id_pedido_coleta        = PC.id_pedido_coleta)").toString();
		
		StringBuffer sql = new StringBuffer()
		.append("SELECT ")
		.append("PC.id_pedido_coleta as idPedidoColeta, ")	
		.append(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "tpModoPedidoColeta") +",")		
		.append("FILIAL.sg_filial as sgFilial, ")
		.append("PC.nr_coleta as nrColeta, ")
		.append("RCE.ds_rota as dsRota, ")
		.append("RCE.nr_rota as nrRota, ")
		.append("PESSOA.nm_pessoa as nmPessoa, ")
		.append("PESSOA.tp_identificacao as tpIdentificacao, ")
		.append("PESSOA.nr_identificacao as nrIdentificacao, ")
		.append("decode(").append(subsql).append(",0, 'N','S') as blPerigoso,")
		.append("NVL((select 'S' ")
		.append("from ")
		.append("DUAL ")
		.append("WHERE ")
		.append("PC.id_pedido_coleta IN ")
		.append("(SELECT DC.id_pedido_coleta FROM detalhe_coleta DC ")
		.append("inner join servico S on (S.id_servico = DC.id_servico) ")
		.append("inner join tipo_servico TS on (ts.id_tipo_servico = S.id_tipo_servico) ")
		.append("where DC.id_pedido_coleta = PC.id_pedido_coleta ")
		.append("and TS.bl_priorizar = 'S' OR S.tp_modal = 'A') ")
		.append("), 'N') as blPriorizar, ")
		.append("PC.bl_produto_perigoso as blProdutoPerigoso ")
		.append("FROM ")
		.append("pedido_coleta PC ")
		.append("INNER JOIN rota_coleta_entrega RCE ON PC.id_rota_coleta_entrega = RCE.id_rota_coleta_entrega ")
		.append("LEFT JOIN regiao_filial_rota_col_ent RFRCE ON RCE.id_rota_coleta_entrega = RFRCE.id_rota_coleta_entrega ")
		.append("LEFT JOIN regiao_coleta_entrega_fil RCEF ON RFRCE.id_regiao_coleta_entrega_fil = RCEF.id_regiao_coleta_entrega_fil ")
		.append("INNER JOIN filial ON PC.id_filial_responsavel = FILIAL.id_filial ")
		.append("INNER JOIN cliente ON PC.id_cliente = CLIENTE.id_cliente ")
		.append("INNER JOIN pessoa ON CLIENTE.id_cliente = PESSOA.id_pessoa ")
		.append("JOIN VALOR_DOMINIO VD ON VD.VL_VALOR_DOMINIO = PC.TP_MODO_PEDIDO_COLETA ")
		.append("JOIN DOMINIO DM ON DM.ID_DOMINIO = VD.ID_DOMINIO AND DM.NM_DOMINIO = 'DM_MODO_PEDIDO_COLETA' ")
		.append("WHERE ")
		.append("PC.tp_status_coleta = 'AB' ")
		.append("AND PC.tp_modo_pedido_coleta <> 'MI' ");

		Map<String, Object> parameters = new HashMap<String, Object>();

		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		
		sql.append("AND FILIAL.id_filial = :idFilial ");
		parameters.put("idFilial", (Long)criteria.get("filialByIdFilialResponsavel.idFilial"));

		sql.append("AND ((rcef.id_regiao_coleta_entrega_fil in ");
		sql.append("(select fr.id_regiao_coleta_entrega_fil from funcionario_regiao fr, usuario ");
		sql.append("where fr.id_usuario = usuario.id_usuario and usuario.id_usuario = :idUsuario)) ");
		sql.append("OR (not exists ");
		sql.append("(select fr.id_regiao_coleta_entrega_fil from funcionario_regiao fr, usuario ");
		sql.append("where fr.id_usuario = usuario.id_usuario and usuario.id_usuario = :idUsuario))) ");
		parameters.put("idUsuario", (Long)criteria.get("idUsuario"));
		parameters.put("idUsuario", (Long)criteria.get("idUsuario"));

		sql.append("AND PC.dt_previsao_coleta <= :dtAtual ");
		parameters.put("dtAtual", dataAtual);
		
		sql.append("AND PC.dh_pedido_coleta <= :dhVigencia ");
		parameters.put("dhVigencia", dataHoraAtual);

		sql.append("AND :dtAtual between RCE.dt_vigencia_inicial and RCE.dt_vigencia_final ");
		parameters.put("dtAtual", dataAtual);

		sql.append("AND (RFRCE.id_regiao_filial_rota_col_ent IS NULL OR (");
		sql.append(":dtAtual between RFRCE.dt_vigencia_inicial and RFRCE.dt_vigencia_final)) ");
		parameters.put("dtAtual", dataAtual);

		sql.append("AND (RCEF.id_regiao_coleta_entrega_fil IS NULL OR (");
		sql.append(":dtAtual between RCEF.dt_vigencia_inicial and RCEF.dt_vigencia_final)) ");
		parameters.put("dtAtual", dataAtual);

		if (criteria.get("idRotaColetaEntrega") != null){
			Long idRotaColetaEntrega = (Long)criteria.get("idRotaColetaEntrega");
			sql.append("AND RCE.id_rota_coleta_entrega = :idRotaColetaEntrega ");
			parameters.put("idRotaColetaEntrega", idRotaColetaEntrega);
		}
			
		sql.append(" ORDER BY RCE.nr_rota ASC , PC.TP_MODO_PEDIDO_COLETA ASC,  PC.nr_coleta ASC");
		return new Object[]{sql.toString(),parameters};
	}

	
	/**
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public List findPaginatedByColetasNaoProgramadas(Map criteria) {
		Object[] obj = createSqlPaginatedByColetasNaoProgramadas(criteria);
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idPedidoColeta",Hibernate.LONG);
				sqlQuery.addScalar("sgFilial",Hibernate.STRING);
				sqlQuery.addScalar("nrColeta",Hibernate.LONG);
				sqlQuery.addScalar("dsRota",Hibernate.STRING);
				sqlQuery.addScalar("nmPessoa",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("blPriorizar",Hibernate.STRING);
				sqlQuery.addScalar("blPerigoso",Hibernate.STRING);
				sqlQuery.addScalar("tpModoPedidoColeta",Hibernate.STRING);
				sqlQuery.addScalar("nrRota",Hibernate.LONG);
				sqlQuery.addScalar("blProdutoPerigoso",Hibernate.STRING);
			}
		};
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql((String)obj[0], Integer.valueOf(1), Integer.valueOf(10000), (Map)obj[1],csq);
		return rsp.getList();
	}

	
	/**
	 * 
	 * @param typedFlatMap
	 * @return
	 */
	public List findSumValoresByMoedaByManifestoColeta(Long idManifestoColeta) {
		SqlTemplate sql = new SqlTemplate();

		StringBuffer projecao = new StringBuffer()
			.append("new map(sum(pc.vlTotalVerificado) as vlTotalVerificado, ")
			.append("sum(pc.psTotalVerificado) as psTotalVerificado, ")
			.append("moeda.idMoeda as idMoeda) ");

		sql.addProjection(projecao.toString());
		
		sql.addFrom(PedidoColeta.class.getName() + " as pc " +
				"join pc.manifestoColeta as manifestoColeta " +
				"join pc.moeda as moeda ");

		sql.addCriteria("manifestoColeta.id", "=", idManifestoColeta);
		sql.addGroupBy("moeda.id"); 
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * Retorna uma list de registros de Pedido Coleta com o ID do Manifesto Coleta
	 * 
	 * @param idManifestoColeta
	 * @return
	 */
	public List findPedidoColetaByIdManifestoColeta(Long idManifestoColeta) {
		DetachedCriteria dc = getDetachedCriteriaFindPedidoColetaByIdManifestoColeta(idManifestoColeta);
		return super.findByDetachedCriteria(dc);
	}

	/**
	 * Retorna uma list de registros de Pedido Coleta com os detalhes da mesma com o ID do Manifesto Coleta
	 *
	 * @param idManifestoColeta
	 * @return
	 */
	public List findPedidoColetaByIdManifestoColetaComDetalhes(Long idManifestoColeta) {
		DetachedCriteria dc = getDetachedCriteriaFindPedidoColetaByIdManifestoColeta(idManifestoColeta);
		dc.setFetchMode("detalheColetas", FetchMode.JOIN);
		return super.findByDetachedCriteria(dc);
	}

	private DetachedCriteria getDetachedCriteriaFindPedidoColetaByIdManifestoColeta(Long idManifestoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);

		dc.createAlias("filialByIdFilialResponsavel", "fr");
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		dc.setFetchMode("moeda", FetchMode.JOIN);

		dc.add(Restrictions.eq("manifestoColeta.id", idManifestoColeta));

		dc.addOrder(Order.asc("fr.sgFilial"));
		dc.addOrder(Order.asc("nrColeta"));
		return dc;
	}

	/**
	 * Método utilizado na pesquisa da lookup de filial.
	 * @param idFilial
	 * @param nrColeta
	 * @return
	 */
	public List findPedidoColetaForLookup(Long nrColeta, Long idPedidoColeta, Long idFilial) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class)
			.setFetchMode("filialByIdFilialSolicitante",FetchMode.JOIN)
			.setFetchMode("filialByIdFilialSolicitante.pessoa",FetchMode.JOIN)
			.setFetchMode("filialByIdFilialResponsavel",FetchMode.JOIN)
			.setFetchMode("filialByIdFilialResponsavel.pessoa",FetchMode.JOIN)
			.setFetchMode("usuario",FetchMode.JOIN)
			.setFetchMode("cliente",FetchMode.JOIN)
			.setFetchMode("cliente.pessoa",FetchMode.JOIN)
			.setFetchMode("municipio",FetchMode.JOIN)
			.setFetchMode("municipio.unidadeFederativa",FetchMode.JOIN)
			.setFetchMode("moeda",FetchMode.JOIN);

		if (idFilial!=null) {
			detachedCriteria.add(Restrictions.eq("filialByIdFilialResponsavel.id", idFilial));
		}		
		
		if (nrColeta!=null) {
			detachedCriteria.add(Restrictions.eq("nrColeta",nrColeta));
		}
		
		if (idPedidoColeta!=null) {
			detachedCriteria.add(Restrictions.eq("idPedidoColeta",idPedidoColeta));
		}
		
		return super.findByDetachedCriteria(detachedCriteria);
	}
	
	/**
	 * Obtém a soma do valorTotalVerificado do pedido de coleta, agrupando por moeda.
	 * @param idManifesto
	 * @return
	 */
	public List findValorVerificadoPedidoColetaByIdManifestoColeta(Long idManifestoColeta) {
		StringBuffer s = new StringBuffer()
		.append("select new Map( sum(pc.vlTotalVerificado) as vlTotalVerificado, mo.id as idMoeda )")
		.append("  from "+PedidoColeta.class.getName()+" pc")
		.append("  join pc.moeda as mo")
		.append(" where pc.manifestoColeta.id = ?")
		.append("   and pc.tpStatusColeta in (?, ?, ?) ")
		.append(" group by mo.id ");
		
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s.toString())
		.setParameter(0, idManifestoColeta)
		.setParameter(1, "EX")
		.setParameter(2, "ED")
		.setParameter(3, "AD")
		.list();
	}
	
	/**
	 * Método que busca uma lista de PedidoColeta usando como filtro uma coleção de Status de Coleta e 
	 * pelo ID da filial responsável. 
	 * @param List tiposStatusColeta
	 * @param Long IdFilialResponsavel
	 * @return List
	 */
	public List findPedidoColetaByTpStatusColetaByIdFilialResponsavel(List listTpStatusColeta, Long idFilialResponsavel) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);		
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		dc.setFetchMode("filialByIdFilialResponsavel.pessoa", FetchMode.JOIN);
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		
		dc.add(Restrictions.eq("filialByIdFilialResponsavel.id", idFilialResponsavel));
		dc.add(Restrictions.in("tpStatusColeta", listTpStatusColeta));
		
		Type[] types = new Type[]{Hibernate.custom(JodaTimeDateTimeUserType.class), Hibernate.custom(JodaTimeDateTimeUserType.class)};
		Object[] dates = new Object[]{JTDateTimeUtils.yearMonthDayToDateTime(JTDateTimeUtils.getDataAtual()), JTDateTimeUtils.getDataAtual().toDateTime(new TimeOfDay(23, 59, 59))};
		dc.add(Restrictions.sqlRestriction("(SELECT MAX(EC.DH_EVENTO) FROM EVENTO_COLETA EC WHERE EC.ID_PEDIDO_COLETA = {alias}.ID_PEDIDO_COLETA) BETWEEN ? AND ?", dates, types));
		
		return super.findByDetachedCriteria(dc);
	}		
	
 
	
	/**
	 * Retorna um DetachedCriteria para a pesquisa "ColetasPendentes".
	 * 
	 * @param idControleCarga
	 * @return
	 */
	private DetachedCriteria getDetachedCriteriaByColetasPendentes(Long idControleCarga) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PedidoColeta.class)
		.setFetchMode("manifestoColeta.controleCarga",FetchMode.JOIN)
		.setFetchMode("filialByIdFilialResponsavel",FetchMode.JOIN)
		.setFetchMode("cliente",FetchMode.JOIN)
		.setFetchMode("cliente.pessoa",FetchMode.JOIN)
		.setFetchMode("moeda",FetchMode.JOIN)
		.createAlias("manifestoColeta","manifestoColeta")
		.createAlias("manifestoColeta.controleCarga","controleCarga")
		.createAlias("filialByIdFilialResponsavel", "filialByIdFilialResponsavel")
		.add(Restrictions.eq("controleCarga.id", idControleCarga))
		.add(Restrictions.isNotNull("controleCarga.dhSaidaColetaEntrega"))
		.add(Restrictions.or(
				Restrictions.eq("tpStatusColeta", "MA"),
				Restrictions.eq("tpStatusColeta", "TR")
				)
		
		);
		return detachedCriteria;
	}
	
	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idControleCarga seja informado.
	 * Tem como restricao buscar apenas "Tipo de Status de Coleta" diferente de AB, NM e CA 
	 * 
	 * @param Long idControleCarga
	 * @param FindDefinition findDefinition
	 * @return ResultSetPage com os dados da grid.
	 */
	public ResultSetPage findPaginatedColetasPendentes(Long idControleCarga, FindDefinition findDefinition){
		DetachedCriteria dc = getDetachedCriteriaByColetasPendentes(idControleCarga);
		dc.addOrder(Order.asc("filialByIdFilialResponsavel.sgFilial"));
		dc.addOrder(Order.asc("nrColeta"));
		if (findDefinition == null) {
			return super.findPaginatedByDetachedCriteria(dc, Integer.valueOf(1), 10000);
		}

		return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());	
	}
	
	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
	 * 
	 * @param idControleCarga
	 * @return Integer com o numero de registos com os dados da grid.
	 */
	public Integer getRowCountColetasPendentes(Long idControleCarga){
		DetachedCriteria dc = getDetachedCriteriaByColetasPendentes(idControleCarga);
		dc.setProjection(Projections.rowCount()); 
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}

	/**
	 * Método que retorna uma list de Pedidos de Coleta para o ID da Cotação informada.
	 * @param idCotacao
	 * @return
	 */
	public List findPedidoColetaByIdCotacao(Long idCotacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class, "pc");
		dc.createAlias("filialByIdFilialResponsavel", "fr");
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		dc.add(Restrictions.eq("cotacao.id", idCotacao));
		return super.findByDetachedCriteria(dc);
	}

	public List findSomatorioPedidoColetaAgrupandoPorMoeda(Long idManifestoColeta) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("sum(pc.vlTotalVerificado) as somaVlTotalVerificado, ")
		.append("pc.filialByIdFilialResponsavel.id as idFilialResponsavel, ")
		.append("pc.moeda.id as idMoeda) ")
		.append("from ").append(PedidoColeta.class.getName()).append(" as pc ")
		.append("where 1=1 ");

		List<Long> param = new ArrayList<Long>(1);
		if (idManifestoColeta != null) {
			sql.append("and pc.manifestoColeta.id = ? ");
			param.add(idManifestoColeta);
		}

		sql.append("group by pc.filialByIdFilialResponsavel.id, pc.moeda.id ");

		return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}


	/**
	 * Retorna o Maior Número de Coleta existente na Tabela PEDIDO_COLETA para a Filial
	 * @param filial
	 * @return Long: maior número de coleta para a filial informada. Caso nao encontre, retorna 0;
	 */	
	public Long findMaiorNroColetaByFilial(Filial filial) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class)
				.setProjection(Projections.max("nrColeta")).add(
						Restrictions.eq("filialByIdFilialResponsavel", filial));

		List result = super.findByDetachedCriteria(dc);
		if (result != null) {
			return (Long) result.get(0);
		}
		return Long.valueOf(0);
	}

	public boolean findPedidoColetaJaExecutadoByMeiodeTransporte(Long idPedidoColeta, Long idMeioTransporte) {

		SqlTemplate sql = new SqlTemplate();

		StringBuffer sb = new StringBuffer();
		sb.append(EventoColeta.class.getName()).append(" as ev ");
		sb.append(" inner join ev.pedidoColeta as pc " );
		sb.append(" inner join ev.meioTransporteRodoviario as mt " );

		sql.addFrom(sb.toString());
		sql.addProjection("ev.tpEventoColeta");
		sql.addCriteria("pc.idPedidoColeta", "=", idPedidoColeta);
		sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);
		sql.addCustomCriteria("ev.tpEventoColeta in (?, ?)");

		sql.addOrderBy("ev.dhEvento.value desc");

		sql.addCriteriaValue("EX");
		sql.addCriteriaValue("TR");

		ResultSetPage retorno = getAdsmHibernateTemplate().findPaginated(sql.getSql(), Integer.valueOf(1), Integer.valueOf(1), sql.getCriteria());

		if (retorno.getList().size() == 0 || ((DomainValue)retorno.getList().get(0)).getValue().equals("TR")) {
			return false;
		}
		return true;
	}

	/**
	 * Retorna os Pedidos de Coleta que estejam no status, na rota informada e com filial responsável igual à filial informada,
	 * cuja data de previsao coleta seja menor ou igual a data passada.
	 * @param tpStatusColeta: é o status da coleta.
	 * @param idRotaColetaEntrega: rota de coleta entrega.
	 * @param idFilialResponsavel: filial responsavel pela coleta.
	 * @param date: é o data de previsão de coleta.
	 * @return List: Pedidos.
	 */
	public List findPedidosColetaByStatusByIdRotaColetaEntregaUntilPrevisaoColeta(String tpStatusColeta, Long idFilialResponsavel, Long idRotaColetaEntrega, YearMonthDay dataPrevisaoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class, "pc");
		dc.add(Restrictions.eq("pc.tpStatusColeta", tpStatusColeta));
		dc.add(Restrictions.eq("pc.filialByIdFilialResponsavel.id", idFilialResponsavel));
		dc.add(Restrictions.eq("pc.rotaColetaEntrega.id", idRotaColetaEntrega));
		dc.add(Restrictions.le("pc.dtPrevisaoColeta", dataPrevisaoColeta));		
		return super.findByDetachedCriteria(dc);
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public List findSomatorioColetasRealizadasByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("sum(pc.vlTotalVerificado) as somaVlTotalVerificado, ")
		.append("sum(pc.psTotalVerificado) as somaPsTotalVerificado, ")
		.append("sum(pc.psTotalAforadoVerificado) as somaPsTotalAforadoVerificado, ")
		.append("pc.filialByIdFilialResponsavel.id as idFilial, ")
		.append("pc.moeda.id as idMoeda) ")
		.append("from ").append(PedidoColeta.class.getName()).append(" as pc ")
		.append("inner join pc.manifestoColeta mc ")
		.append("where ")
		.append("mc.controleCarga.id = ? ")
		.append("and exists ( ")
			.append("SELECT 1 ")
			.append("FROM ")
			.append(EventoColeta.class.getName()).append(" as ec ")
			.append("WHERE ")
			.append("ec.tpEventoColeta = 'EX' ") 
			.append("AND ec.pedidoColeta.id = pc.id) ")
		.append("group by pc.filialByIdFilialResponsavel.id, pc.moeda.id "); 

		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
	}
	
	
	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public List findSomatorioColetasASeremRealizadasByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("sum(pc.vlTotalVerificado) as somaVlTotalVerificado, ")
		.append("sum(pc.psTotalVerificado) as somaPsTotalVerificado, ")
		.append("pc.filialByIdFilialResponsavel.id as idFilial, ")
		.append("pc.moeda.id as idMoeda) ")
		.append("from ").append(PedidoColeta.class.getName()).append(" as pc ")
		.append("inner join pc.manifestoColeta mc ")
		.append("where ")
		.append("mc.controleCarga.id = ? ")
		.append("and not exists ( ")
			.append("SELECT 1 ")
			.append("FROM ")
			.append(EventoColeta.class.getName()).append(" as ec ")
			.append("WHERE ")
			.append("ec.tpEventoColeta = 'EX' ") 
			.append("AND ec.pedidoColeta.id = pc.id) ")
		.append("group by pc.filialByIdFilialResponsavel.id, pc.moeda.id "); 

		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public List findPedidoColetaByIdControleCargaByTpStatusColeta(Long idControleCarga, String tpStatusColeta) {
		StringBuffer sql = new StringBuffer()
			.append("from ").append(PedidoColeta.class.getName()).append(" as pc ")
			.append("where ")
			.append("pc.manifestoColeta.controleCarga.id = ? ");

		List<Object> param = new ArrayList<Object>(2);
		param.add(idControleCarga);

		if (tpStatusColeta != null) {
			sql.append("and pc.tpStatusColeta = ? ");
			param.add(tpStatusColeta);
		}
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

	/**
	 * Alterar o campo tpStatusColeta para "AB" na tabela PedidoColeta para o Controle de Carga 
	 * em questão.
	 * 
	 * @param idControleCarga
	 */
	public void updateStatusColetaByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("update ")
		.append(PedidoColeta.class.getName()).append(" as pc ")
		.append("set pc.manifestoColeta.id = null, pc.tpStatusColeta = 'AB' ")
		.append("where pc.manifestoColeta.id in ")
			.append("(select mc.id as idManifestoColeta from ")
			.append(ManifestoColeta.class.getName()).append(" mc ")
			.append("where mc.controleCarga.id = ?) ");

		List param = new ArrayList();
		param.add(idControleCarga);

		executeHql(sql.toString(), param);
	}
	
	public Boolean validatePedidoColetaByIdControleCarga(Long idControleCarga) {
	
		DetachedCriteria subquery = DetachedCriteria.forClass(NotaCredito.class, "nc")
			.createCriteria("notaCreditoColetas", "ncc")
				.createCriteria("pedidoColeta", "pedidoColeta")
					.setProjection(Projections.projectionList().add(Projections.property("pedidoColeta.idPedidoColeta")));
	
		Criteria c = getSession().createCriteria(PedidoColeta.class , "pc")
    		.createCriteria("manifestoColeta" , "mc")
    			.createCriteria("controleCarga" , "cc")
    				.add(Restrictions.eq("cc.id", idControleCarga))
    				.add(Restrictions.in("pc.tpStatusColeta", new String[]{"FI","NT","EX"}))
    					.add(Subqueries.propertyNotIn("pc.idPedidoColeta", subquery));
		return !c.list().isEmpty();
	}
	
	public boolean validateColetaExecutadaOuNao(Long idControleCarga) {
		
		Criteria c = getSession().createCriteria(PedidoColeta.class , "pc")
			.createCriteria("manifestoColeta" , "mc")
				.createCriteria("controleCarga" , "cc")
					.add(Restrictions.eq("cc.id", idControleCarga))
						.add(Restrictions.not(Restrictions.in("pc.tpStatusColeta", new String[]{"FI","NT","EX","CA"})));
			
		return c.list().isEmpty();
	}
	
	public void flush() {
		getAdsmHibernateTemplate().flush();
	}

	public List<PedidoColeta> findPedidoColetaByIdManifestoColetaIdNotaCredito(Long idManifestoColeta, Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		hql.append("select pc ");
		hql.append("from PedidoColeta as pc ");
		hql.append("join pc.manifestoColeta as mc ");
		hql.append("join pc.notaCreditoColetas as ncc ");
		hql.append("join ncc.notaCredito as nc ");
		hql.append("where mc.idManifestoColeta = ? ");
		hql.append("and nc.idNotaCredito = ? ");

		return (List<PedidoColeta>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idManifestoColeta, idNotaCredito).toArray());
	}



	/**
	 * 
	 * @param nrChave
	 * @return Pedido de Coleta com maior ID. Portando o registro mais recente.
	 */
	public PedidoColeta findByNrChave(String nrChave) {
		Criteria c = getSession().createCriteria(PedidoColeta.class , "pedidoColeta");
		c.createAlias("pedidoColeta.detalheColetas", "detalhesColeta");
		c.createAlias("detalhesColeta.notaFiscalColetas", "notaFiscalColetas");
		c.add(Restrictions.eq("notaFiscalColetas.nrChave", nrChave));
		c.addOrder(Order.desc("pedidoColeta.idPedidoColeta"));
		c.setMaxResults(1);
		return (PedidoColeta) c.uniqueResult();
	}
	
	public Integer getCountByManifestoColeta(Long idManifestoColeta){
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.setProjection(Projections.rowCount());
		dc.createAlias("filialByIdFilialResponsavel", "fr");
		dc.setFetchMode("filialByIdFilialResponsavel", FetchMode.JOIN);
		dc.setFetchMode("usuario", FetchMode.JOIN);
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		dc.setFetchMode("moeda", FetchMode.JOIN);

		dc.add(Restrictions.eq("manifestoColeta.id", idManifestoColeta));	
		
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);
	}
	
	public List<Map<String, Object>> findPedidoColetaSuggest(final String sgFilial, final Long nrPedidoColeta, final Long idEmpresa) {
		final StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pc.id_pedido_coleta, ");
		sql.append("       fo.sg_filial as sg_filial, "); 
		sql.append("       pc.nr_coleta, ");
		sql.append("       pc.dh_pedido_coleta ");
		
		sql.append("  FROM pedido_coleta pc ");
		sql.append("       inner join filial fo on fo.id_filial = pc.id_filial_responsavel ");
		
		sql.append(" WHERE fo.sg_filial = :sgFilial ");
		sql.append("   and pc.nr_coleta = :nrPedidoColeta ");
		if (idEmpresa != null) {
			sql.append("   and fo.id_empresa = :idEmpresa ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_pedido_coleta", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_coleta", Hibernate.LONG);
				sqlQuery.addScalar("dh_pedido_coleta", Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setString("sgFilial", sgFilial);
				query.setLong("nrPedidoColeta", nrPedidoColeta);
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
			map.put("idPedidoColeta", o[0]);
			map.put("sgFilial", o[1]);
			map.put("nrColeta", o[2]);
			map.put("dhPedidoColeta", o[3]);
			toReturn.add(map);
			
		}
		
		return toReturn;
	}
	
	public List<RelatorioEficienciaColetaDetalhadoDTO> listRelatorioEficienciaColetaDetalhado(
			YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
			String modoPedidoColeta, Long idRegional, Long idCliente,
			Long idRotaColetaEntrega, String efc) {
		
		String query = mountSqlRelatorioEficienciaColetaDetalhado(dataInicial, dataFinal, idFilial, 
				modoPedidoColeta, idRegional, idCliente, idRotaColetaEntrega, efc);
		
		Map<String, Object> namedParams = params(dataInicial, dataFinal, idFilial, modoPedidoColeta, 
				idRegional, idCliente, idRotaColetaEntrega, efc);

		List<Object[]> sqlResult = getAdsmHibernateTemplate().findBySql(query, namedParams, 
				configureSqlQueryRelatorioEficienciaColetaDetalhado());
		
		return populateRelatorioEficienciaColetaDetalhadoDTO(sqlResult);
	}
	
	public List<RelatorioEficienciaColetaResumidoDTO> listRelatorioEficienciaColetaResumido(
			YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
			String modoPedidoColeta, Long idRegional, Long idCliente,
			Long idRotaColetaEntrega, String efc) {
		
		String query = mountSqlRelatorioEficienciaColetaResumido(dataInicial, dataFinal, idFilial, 
				modoPedidoColeta, idRegional, idCliente, idRotaColetaEntrega, efc);
		
		Map<String, Object> namedParams = params(dataInicial, dataFinal, idFilial, modoPedidoColeta, 
				idRegional, idCliente, idRotaColetaEntrega, efc);

		List<Object[]> sqlResult = getAdsmHibernateTemplate().findBySql(query, namedParams, 
				configureSqlQueryRelatorioEficienciaColetaResumido());
		
		return populateRelatorioEficienciaColetaResumidoDTO(sqlResult);
	}

	private List<RelatorioEficienciaColetaDetalhadoDTO> populateRelatorioEficienciaColetaDetalhadoDTO(
			List<Object[]> sqlResult) {
		List<RelatorioEficienciaColetaDetalhadoDTO> result = new ArrayList<RelatorioEficienciaColetaDetalhadoDTO>();
		for (Object[] row : sqlResult) {
			RelatorioEficienciaColetaDetalhadoDTO dto = new RelatorioEficienciaColetaDetalhadoDTO();
			dto.setIdCliente((String) row[0]);
			dto.setTipoIdentificacao((String) row[1]);
			dto.setNomeCliente((String) row[2]);
			dto.setIdFilial((String) row[3]);
			dto.setNomeFilial((String) row[4]);
			dto.setNumeroColeta((BigDecimal) row[5]);
			dto.setNumeroRota((BigDecimal) row[6]);
			dto.setModoPedidoColeta((VarcharI18n) row[7]);
			dto.setNomeUsuario((String) row[8]);
			dto.setQuantidadeDocumentos((BigDecimal) row[9]);
			dto.setDataSolicitacao((Date) row[10]);
			dto.setDataPrevista((Date) row[11]);
			dto.setDataBaixa((Date) row[12]);
			dto.setUsuarioBaixa((String) row[13]);
			dto.setEfc((String) row[14]);
			dto.setCodigoUltimaOcorrencia((BigDecimal) row[15]);
			dto.setDescricaoUltimaOcorrencia((VarcharI18n) row[16]);
			dto.setUsuarioUltimaOcorrencia((String) row[17]);
			dto.setDataUltimaOcorrencia((Date) row[18]);
			dto.setCodigoOcorrenciaTNT((BigDecimal) row[19]);
			dto.setDescricaoOcorrenciaTNT((VarcharI18n) row[20]);
			dto.setUsuarioOcorrenciaTNT((String) row[21]);
			dto.setDataOcorrenciaTNT((Date) row[22]);
			result.add(dto);
		}
		return result;
	}

	private List<RelatorioEficienciaColetaResumidoDTO> populateRelatorioEficienciaColetaResumidoDTO(
			List<Object[]> sqlResult) {
		List<RelatorioEficienciaColetaResumidoDTO> result = new ArrayList<RelatorioEficienciaColetaResumidoDTO>();
		for (Object[] row : sqlResult) {
			RelatorioEficienciaColetaResumidoDTO dto = new RelatorioEficienciaColetaResumidoDTO();
			dto.setIdFilial((String) row[0]);
			dto.setNomeFilial((String) row[1]);
			dto.setQtdTotal((BigDecimal) row[2]);
			dto.setQtdEficiente((BigDecimal) row[3]);
			dto.setQtdIneficiente((BigDecimal) row[4]);
			dto.setQtdNeutra((BigDecimal) row[5]);
			result.add(dto);
		}
		return result;
	}

	private ConfigureSqlQuery configureSqlQueryRelatorioEficienciaColetaDetalhado() {
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idCliente", Hibernate.STRING);
				sqlQuery.addScalar("tipoIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("nomeCliente", Hibernate.STRING);
				sqlQuery.addScalar("idFilial", Hibernate.STRING);
				sqlQuery.addScalar("nomeFilial", Hibernate.STRING);
				sqlQuery.addScalar("numeroColeta", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("numeroRota", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("modoPedidoColeta", Hibernate.custom(VarcharI18nUserType.class));
				sqlQuery.addScalar("nomeUsuario", Hibernate.STRING);
				sqlQuery.addScalar("quantidadeDocumentos", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dataSolicitacao", Hibernate.DATE);
				sqlQuery.addScalar("dataPrevista", Hibernate.DATE);
				sqlQuery.addScalar("dataBaixa", Hibernate.DATE);
				sqlQuery.addScalar("usuarioBaixa", Hibernate.STRING);
				sqlQuery.addScalar("efc", Hibernate.STRING);
				sqlQuery.addScalar("codigoUltimaOcorrencia", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("descricaoUltimaOcorrencia", Hibernate.custom(VarcharI18nUserType.class));
				sqlQuery.addScalar("usuarioUltimaOcorrencia", Hibernate.STRING);
				sqlQuery.addScalar("dataUltimaOcorrencia", Hibernate.DATE);
				sqlQuery.addScalar("codigoOcorrenciaTNT", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("descricaoOcorrenciaTNT", Hibernate.custom(VarcharI18nUserType.class));
				sqlQuery.addScalar("usuarioOcorrenciaTNT", Hibernate.STRING);
				sqlQuery.addScalar("dataOcorrenciaTNT", Hibernate.DATE);
			}			
		};
		return csq;
	}

	private ConfigureSqlQuery configureSqlQueryRelatorioEficienciaColetaResumido() {
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idFilial", Hibernate.STRING);
				sqlQuery.addScalar("nomeFilial", Hibernate.STRING);
				sqlQuery.addScalar("qtdTotal", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("qtdEficiente", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("qtdIneficiente", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("qtdNeutra", Hibernate.BIG_DECIMAL);
			}			
		};
		return csq;
	}

	private Map<String, Object> params(YearMonthDay dataInicial, YearMonthDay dataFinal,
			Long idFilial, String modoPedidoColeta, Long idRegional, Long idCliente, 
			Long idRotaColetaEntrega, String efc) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
    	namedParams.put("dataInicial", dataInicial);
    	namedParams.put("dataFinal", dataFinal);
    	namedParams.put("idFilial", idFilial);
    	namedParams.put("modoPedidoColeta", modoPedidoColeta);
    	namedParams.put("idRegional", idRegional);
    	namedParams.put("idCliente", idCliente);
    	namedParams.put("idRotaColetaEntrega", idRotaColetaEntrega);
    	namedParams.put("efc", efc);
		return namedParams;
	}
	
	private String mountSqlRelatorioEficienciaColetaDetalhado(
			YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
			String modoPedidoColeta, Long idRegional, Long idCliente,
			Long idRotaColetaEntrega, String efc) {

		StringBuilder sql = new StringBuilder()
				.append(" SELECT * FROM ( ")
				.append("   SELECT ")
				.append("     TAB.NR_IDENTIFICACAO idCliente, ")
				.append("     TAB.TP_IDENTIFICACAO as tipoIdentificacao, ")
				.append("     TAB.NM_PESSOA nomeCliente, ")
				.append("     TAB.SG_FILIAL idFilial, ")
				.append("     TAB.NM_FANTASIA nomeFilial, ")
				.append("     TAB.NR_COLETA numeroColeta, ")
				.append("     TAB.NR_ROTA numeroRota, ")
				.append("     TAB.DS_VALOR_DOMINIO_I modoPedidoColeta, ")
				.append("     TAB.NM_USUARIO nomeUsuario, ")
				.append("     TAB.QT_DOC quantidadeDocumentos, ")
				.append("     CAST(TAB.DH_PEDIDO_COLETA AS DATE) dataSolicitacao, ")
				.append("     TAB.DT_PREVISAO_COLETA dataPrevista, ")
				.append("     CAST(EC.DH_EVENTO AS DATE) dataBaixa, ")
				.append("     US.NM_USUARIO usuarioBaixa, ")
				.append("     CASE ")
				.append("       WHEN EC.DH_EVENTO IS NOT NULL THEN ")
				.append("         CASE ")
				.append("           WHEN TRUNC(EC.DH_EVENTO) <= TAB.DT_PREVISAO_COLETA THEN 'Eficiente' ")
				.append("           ELSE 'Ineficiente' ")
				.append("         END ")
				.append("       ELSE ")
				.append("         CASE ")
				.append("           WHEN TRUNC(SYSDATE) <= TAB.DT_PREVISAO_COLETA THEN 'Neutra' ")
				.append("           WHEN ")
				.append("             TRUNC(SYSDATE) > TAB.DT_PREVISAO_COLETA ")
				.append("             AND UEC.DH_EVENTO IS NOT NULL AND TRUNC(UEC.DH_EVENTO) <= TAB.DT_PREVISAO_COLETA ")
				.append("             AND UOC.CODIGO IS NOT NULL AND UOC.CODIGO NOT IN (10,14,20,36,39,53,01) ")
				.append("             AND TEC.DH_EVENTO IS NULL ")
				.append("           THEN 'Neutra' ")
				.append("           ELSE 'Ineficiente' ")
				.append("         END ")
				.append("       END efc, ")
				.append("     UOC.CODIGO codigoUltimaOcorrencia, ")
				.append("     UOC.DS_DESCRICAO_COMPLETA_I descricaoUltimaOcorrencia, ")
				.append("     USU.NM_USUARIO usuarioUltimaOcorrencia, ")
				.append("     CAST(UEC.DH_EVENTO AS DATE) dataUltimaOcorrencia, ")
				.append("     TOC.CODIGO codigoOcorrenciaTNT, ")
				.append("     TOC.DS_DESCRICAO_COMPLETA_I descricaoOcorrenciaTNT, ")
				.append("     UST.NM_USUARIO usuarioOcorrenciaTNT, ")
				.append("     CAST(TEC.DH_EVENTO AS DATE) dataOcorrenciaTNT ")
				.append("   FROM ( ")
				.append("     SELECT ")
				.append("       CL.NR_IDENTIFICACAO, ")
				.append("       CL.TP_IDENTIFICACAO, ")
				.append("       CL.NM_PESSOA, ")
				.append("       F.SG_FILIAL, ")
				.append("       PE.NM_FANTASIA, ")
				.append("       PC.NR_COLETA, ")
				.append("       RC.NR_ROTA, ")
				.append("       VD.DS_VALOR_DOMINIO_I, ")
				.append("       US.NM_USUARIO, ")
				.append("       (SELECT COUNT(*) FROM DOCTO_SERVICO DC WHERE DC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA) AS QT_DOC, ")
				.append("       PC.DH_PEDIDO_COLETA, ")
				.append("       PC.DT_PREVISAO_COLETA, ")
				.append("       (SELECT MAX(EC.ID_EVENTO_COLETA) FROM EVENTO_COLETA EC WHERE EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA AND EC.TP_EVENTO_COLETA = 'EX') AS ID_EVENTO_EXECUCAO, ")
				.append("       (SELECT ")
				.append("           MAX(EC.ID_EVENTO_COLETA) ")
				.append("         FROM ")
				.append("           EVENTO_COLETA EC ")
				.append("           JOIN OCORRENCIA_COLETA OC ON OC.ID_OCORRENCIA_COLETA = EC.ID_OCORRENCIA_COLETA ")
				.append("         WHERE ")
				.append("           EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
				.append("           AND EC.TP_EVENTO_COLETA = 'RC' ")
				.append("           AND OC.CODIGO IN (10,14,20,36,39,53)) AS ID_EVENTO_TNT, ")
				.append("       (SELECT ")
				.append("           MAX(EC.ID_EVENTO_COLETA) ")
				.append("         FROM ")
				.append("           EVENTO_COLETA EC ")
				.append("           JOIN OCORRENCIA_COLETA OC ON OC.ID_OCORRENCIA_COLETA = EC.ID_OCORRENCIA_COLETA ")
				.append("         WHERE ")
				.append("           EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
				.append("           AND EC.TP_EVENTO_COLETA = 'RC' ")
				.append("           AND OC.CODIGO NOT IN (01)) AS ID_ULTIMO_EVENTO ")
				.append("     FROM ")
				.append("       PEDIDO_COLETA PC ")
				.append("       JOIN FILIAL F ON F.ID_FILIAL = PC.ID_FILIAL_RESPONSAVEL ")
				.append("       JOIN PESSOA PE ON PE.ID_PESSOA = F.ID_FILIAL ")
				.append("       JOIN PESSOA CL ON CL.ID_PESSOA = PC.ID_CLIENTE ")
				.append("       JOIN ROTA_COLETA_ENTREGA RC ON RC.ID_ROTA_COLETA_ENTREGA = PC.ID_ROTA_COLETA_ENTREGA ")
				.append("       JOIN USUARIO US ON US.ID_USUARIO = PC.ID_USUARIO ")
				.append("       JOIN REGIONAL_FILIAL RF ON RF.ID_FILIAL = F.ID_FILIAL ")
				.append("       JOIN VALOR_DOMINIO VD ON VD.VL_VALOR_DOMINIO = PC.TP_MODO_PEDIDO_COLETA ")
				.append("       JOIN DOMINIO DM ON DM.ID_DOMINIO = VD.ID_DOMINIO AND DM.NM_DOMINIO = 'DM_MODO_PEDIDO_COLETA' ")
				.append("     WHERE ")
				.append("       PC.TP_STATUS_COLETA <> 'CA' ")
				.append("       AND PC.TP_MODO_PEDIDO_COLETA <> 'BA' ")
				.append("       AND SYSDATE BETWEEN RF.DT_VIGENCIA_INICIAL AND RF.DT_VIGENCIA_FINAL ")
				.append(filterModoPedidoColeta(modoPedidoColeta))
				.append(filterRegional(idRegional))
				.append(filterFilial(idFilial))
				.append(filterCliente(idCliente))
				.append(filterRotaColetaEntrega(idRotaColetaEntrega))
				.append("       AND TRUNC(PC.DT_PREVISAO_COLETA) >= :dataInicial ")
				.append("       AND TRUNC(PC.DT_PREVISAO_COLETA) < :dataFinal ")
				.append("     ) TAB ")
				.append("     LEFT JOIN EVENTO_COLETA EC ON EC.ID_EVENTO_COLETA = TAB.ID_EVENTO_EXECUCAO ")
				.append("     LEFT JOIN USUARIO US ON US.ID_USUARIO = EC.ID_USUARIO ")
				.append("     LEFT JOIN EVENTO_COLETA UEC ON UEC.ID_EVENTO_COLETA = TAB.ID_ULTIMO_EVENTO ")
				.append("     LEFT JOIN USUARIO USU ON USU.ID_USUARIO = UEC.ID_USUARIO ")
				.append("     LEFT JOIN OCORRENCIA_COLETA UOC ON UOC.ID_OCORRENCIA_COLETA = UEC.ID_OCORRENCIA_COLETA ")
				.append("     LEFT JOIN EVENTO_COLETA TEC ON TEC.ID_EVENTO_COLETA = TAB.ID_EVENTO_TNT ")
				.append("     LEFT JOIN OCORRENCIA_COLETA TOC ON TOC.ID_OCORRENCIA_COLETA = TEC.ID_OCORRENCIA_COLETA ")
				.append("     LEFT JOIN USUARIO UST ON UST.ID_USUARIO = TEC.ID_USUARIO) ")
				.append(filterEFC(efc))
				.append(" ORDER BY idFilial, numeroColeta ");

		return sql.toString();
	}
	
	private String mountSqlRelatorioEficienciaColetaResumido(
			YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
			String modoPedidoColeta, Long idRegional, Long idCliente,
			Long idRotaColetaEntrega, String efc) {

		StringBuilder sql = new StringBuilder()
				.append(" SELECT ")
				.append("   IDFILIAL idFilial, ")
				.append("   NOMEFILIAL nomeFilial, ")
				.append("   sum(1) as qtdTotal, ")
				.append("   sum(case when EFC = 'Eficiente' then 1 else 0 end) qtdEficiente, ")
				.append("   sum(case when EFC = 'Ineficiente' then 1 else 0 end) qtdIneficiente, ")
				.append("   sum(case when EFC = 'Neutra' then 1 else 0 end) qtdNeutra ")
				.append(" FROM ( ")
				.append("   SELECT ")
				.append("     TAB.SG_FILIAL idFilial, ")
				.append("     TAB.NM_FANTASIA nomeFilial, ")
				.append("     CASE ")
				.append("       WHEN EC.DH_EVENTO IS NOT NULL THEN ")
				.append("         CASE ")
				.append("           WHEN TRUNC(EC.DH_EVENTO) <= TAB.DT_PREVISAO_COLETA THEN 'Eficiente' ")
				.append("           ELSE 'Ineficiente' ")
				.append("         END ")
				.append("       ELSE ")
				.append("         CASE ")
				.append("           WHEN TRUNC(SYSDATE) <= TAB.DT_PREVISAO_COLETA THEN 'Neutra' ")
				.append("           WHEN ")
				.append("             TRUNC(SYSDATE) > TAB.DT_PREVISAO_COLETA ")
				.append("             AND UEC.DH_EVENTO IS NOT NULL AND TRUNC(UEC.DH_EVENTO) <= TAB.DT_PREVISAO_COLETA ")
				.append("             AND UOC.CODIGO IS NOT NULL AND UOC.CODIGO NOT IN (10,14,20,36,39,53,01) ")
				.append("             AND TEC.DH_EVENTO IS NULL ")
				.append("           THEN 'Neutra' ")
				.append("           ELSE 'Ineficiente' ")
				.append("         END ")
				.append("       END efc ")
				.append("   FROM ( ")
				.append("     SELECT ")
				.append("       F.SG_FILIAL, ")
				.append("       PE.NM_FANTASIA, ")
				.append("       PC.DH_PEDIDO_COLETA, ")
				.append("       PC.DT_PREVISAO_COLETA, ")
				.append("       (SELECT MAX(EC.ID_EVENTO_COLETA) FROM EVENTO_COLETA EC WHERE EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA AND EC.TP_EVENTO_COLETA = 'EX') AS ID_EVENTO_EXECUCAO, ")
				.append("       (SELECT ")
				.append("           MAX(EC.ID_EVENTO_COLETA) ")
				.append("         FROM ")
				.append("           EVENTO_COLETA EC ")
				.append("           JOIN OCORRENCIA_COLETA OC ON OC.ID_OCORRENCIA_COLETA = EC.ID_OCORRENCIA_COLETA ")
				.append("         WHERE ")
				.append("           EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
				.append("           AND EC.TP_EVENTO_COLETA = 'RC' ")
				.append("           AND OC.CODIGO IN (10,14,20,36,39,53)) AS ID_EVENTO_TNT, ")
				.append("       (SELECT ")
				.append("           MAX(EC.ID_EVENTO_COLETA) ")
				.append("         FROM ")
				.append("           EVENTO_COLETA EC ")
				.append("           JOIN OCORRENCIA_COLETA OC ON OC.ID_OCORRENCIA_COLETA = EC.ID_OCORRENCIA_COLETA ")
				.append("         WHERE ")
				.append("           EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
				.append("           AND EC.TP_EVENTO_COLETA = 'RC' ")
				.append("           AND OC.CODIGO NOT IN (01)) AS ID_ULTIMO_EVENTO ")
				.append("     FROM ")
				.append("       PEDIDO_COLETA PC ")
				.append("       JOIN FILIAL F ON F.ID_FILIAL = PC.ID_FILIAL_RESPONSAVEL ")
				.append("       JOIN PESSOA PE ON PE.ID_PESSOA = F.ID_FILIAL ")
				.append("       JOIN ROTA_COLETA_ENTREGA RC ON RC.ID_ROTA_COLETA_ENTREGA = PC.ID_ROTA_COLETA_ENTREGA ")
				.append("       JOIN REGIONAL_FILIAL RF ON RF.ID_FILIAL = F.ID_FILIAL ")
				.append("     WHERE ")
				.append("       PC.TP_STATUS_COLETA <> 'CA' ")
				.append("       AND PC.TP_MODO_PEDIDO_COLETA <> 'BA' ")
				.append("       AND SYSDATE BETWEEN RF.DT_VIGENCIA_INICIAL AND RF.DT_VIGENCIA_FINAL ")
				.append(filterModoPedidoColeta(modoPedidoColeta))
				.append(filterRegional(idRegional))
				.append(filterFilial(idFilial))
				.append(filterCliente(idCliente))
				.append(filterRotaColetaEntrega(idRotaColetaEntrega))
				.append("       AND TRUNC(PC.DT_PREVISAO_COLETA) >= :dataInicial ")
				.append("       AND TRUNC(PC.DT_PREVISAO_COLETA) < :dataFinal ")
				.append("     ) TAB ")
				.append("     LEFT JOIN EVENTO_COLETA EC ON EC.ID_EVENTO_COLETA = TAB.ID_EVENTO_EXECUCAO ")
				.append("     LEFT JOIN EVENTO_COLETA UEC ON UEC.ID_EVENTO_COLETA = TAB.ID_ULTIMO_EVENTO ")
				.append("     LEFT JOIN OCORRENCIA_COLETA UOC ON UOC.ID_OCORRENCIA_COLETA = UEC.ID_OCORRENCIA_COLETA ")
				.append("     LEFT JOIN EVENTO_COLETA TEC ON TEC.ID_EVENTO_COLETA = TAB.ID_EVENTO_TNT ")
				.append("     LEFT JOIN OCORRENCIA_COLETA TOC ON TOC.ID_OCORRENCIA_COLETA = TEC.ID_OCORRENCIA_COLETA ")
				.append(" ) " + filterEFC(efc))
				.append(" GROUP BY idFilial, nomeFilial ")
				.append(" ORDER BY idFilial, nomeFilial ");
		return sql.toString();
	}
	
	private String filterModoPedidoColeta(String modoPedidoColeta) {
		return modoPedidoColeta != null ? "       AND PC.TP_MODO_PEDIDO_COLETA = :modoPedidoColeta " : "";
	}
	
	private String filterRegional(Long idRegional) {
		return idRegional != null ? "       AND RF.ID_REGIONAL = :idRegional " : "";
	}
	
	private String filterFilial(Long idFilial) {
		return idFilial != null ? "       AND F.ID_FILIAL = :idFilial " : "";
	}
	
	private String filterCliente(Long idCliente) {
		return idCliente != null ? "       AND PC.ID_CLIENTE = :idCliente " : "";
	}
	
	private String filterRotaColetaEntrega(Long idRotaColetaEntrega) {
		return idRotaColetaEntrega != null ? "       AND RC.ID_ROTA_COLETA_ENTREGA = :idRotaColetaEntrega " : "";
	}
	
	private String filterEFC(String efc) {
		return efc != null ? " WHERE efc = :efc " : "";
	}

	public Integer findRowCountByIdDoctoServicoNotCanceled(Long idDoctoServico, Long idPedidoColeta) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(DetalheColeta.class);
		dc.setProjection(Projections.rowCount());
		dc.createAlias("pedidoColeta", "pd");
		dc.setFetchMode("pedidoColeta", FetchMode.JOIN);

		dc.add(Restrictions.eq("doctoServico.id", idDoctoServico));
		dc.add(Restrictions.ne("pd.tpStatusColeta", "CA"));
		if(idPedidoColeta != null){
			dc.add(Restrictions.ne("pd.idPedidoColeta", idPedidoColeta));
		}
		
		List<Integer> result = super.findByDetachedCriteria(dc);
		return result.get(0);

	}

	public List<VolumeNotaFiscal> findVolumesPedidoColetaByControleCarga(Long idControleCarga) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("select vnf ");
		hql.append("from VolumeNotaFiscal as vnf ");
		hql.append("join fetch vnf.localizacaoMercadoria as lm ");
		hql.append("join fetch vnf.localizacaoFilial as lf ");

		hql.append("join fetch vnf.notaFiscalConhecimento as nfc ");
		hql.append("join fetch nfc.conhecimento as con ");
		hql.append("join con.ctoAwbs as ca ");
		hql.append("join ca.awb as awb ");
		hql.append("join awb.awbColetas as ac ");
		hql.append("join ac.detalheColeta as dc ");
		hql.append("join dc.pedidoColeta as pc ");
		hql.append("join pc.manifestoColeta as mc ");
		hql.append("join mc.controleCarga as cc ");
		hql.append("where cc.id = ? ");

		List<VolumeNotaFiscal> volumes = (List<VolumeNotaFiscal>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idControleCarga).toArray());

		hql = new StringBuilder();
		hql.append("select vnf ");
		hql.append("from VolumeNotaFiscal as vnf ");
		hql.append("join fetch vnf.localizacaoMercadoria as lm ");
		hql.append("join fetch vnf.localizacaoFilial as lf ");
		
		hql.append("join fetch vnf.notaFiscalConhecimento as nfc ");
		hql.append("join fetch nfc.conhecimento as con ");
		hql.append("join con.detalheColetas as dc ");
		hql.append("join dc.pedidoColeta as pc ");
		hql.append("join pc.manifestoColeta as mc ");
		hql.append("join mc.controleCarga as cc ");
		hql.append("where cc.id = ? ");
		
		volumes.addAll((List<VolumeNotaFiscal>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idControleCarga).toArray()));		

		return volumes;
	}

	public List<Map<String, Object>> findPedidoColetaLoja(Long idCliente) {
		
		DateTime hoje = JTDateTimeUtils.getDataHoraAtual();
		DateTime ontem = JTDateTimeUtils.getLastHourOfDay(hoje).minusDays(1);
		
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select new map(");
		sql.append("pc.idPedidoColeta as idPedidoColeta, ");
		sql.append("pc.nrColeta as nrColeta, ");
		sql.append("ec.dhEvento as dhEvento, ");		
		sql.append("pc.filialByIdFilialResponsavel.id as idFilial, ");
		sql.append("pc.blProdutoDiferenciado as blProdutoDiferenciado) ");
		sql.append("from ").append(PedidoColeta.class.getName()).append(" as pc ");
		sql.append("inner join pc.manifestoColeta mc ");
		sql.append("inner join pc.filialByIdFilialResponsavel fi ");
		sql.append("inner join pc.eventoColetas ec ");
		sql.append("where pc.filialByIdFilialResponsavel.id = fi.idFilial ");
		sql.append("	and fi.filialByIdFilialResponsavel.idFilial = ?");
		sql.append("	and ec.dhEvento.value <= ?");
		sql.append("	and ec.dhEvento.value >= ?");
		sql.append("	and ec.tpEventoColeta = 'EX' ");
		sql.append("	and pc.tpStatusColeta in('EX', 'AD', 'ED', 'NT') ");
		sql.append("	and pc.cliente.id = ?");
		sql.append("	order by pc.idPedidoColeta desc");
		

		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idFilial, hoje.toDate(),ontem.toDate() ,idCliente});
		
	}
	
	public List<Map<String, Object>> findPedidosColetasPendentes(TypedFlatMap parameter) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append(mountProjectioColetasPendentes())
		.append(mountFromColetasPendentes())
		.append(mountWhereColetasPendentes(parameter))
		.append(mountGroupByColetasPendentes());

		ConfigureSqlQuery confSql = configureSqlQueryColetasPendentes();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parameter, confSql);
	}
	
	public List<Map<String, Object>> findPedidosColetasRealizadas(TypedFlatMap parameter) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append(mountProjectioColetasRealizadas())
		.append(mountFromColetasRealizadas())
		.append(mountWhereColetasRealizadas(parameter))
		.append(mountGroupByColetasRealizadas());

		ConfigureSqlQuery confSql = configureSqlQueryColetasRealizadas();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parameter, confSql);
	}
	
	private String mountProjectioColetasPendentes() {
		StringBuilder projection = new StringBuilder();
		projection.append("SELECT ")
			.append("  fr.SG_FILIAL as \"coleta.sgFilial\",")
			.append("  pc.NR_COLETA as \"coleta.nrColeta\", ")
			.append("  CASE ")
			.append("    WHEN aw.NR_AWB < 0 ")
			.append("    THEN TO_CHAR(aw.ID_AWB) ")
			.append("    ELSE aw.DS_SERIE ")
			.append("      ||aw.NR_AWB ")
			.append("      ||aw.DV_AWB ")
			.append("  END as \"preAwbAwb\", ")
			.append("  CASE ")
			.append("    WHEN e.SG_EMPRESA IS NOT NULL ")
			.append("    THEN e.SG_EMPRESA ")
			.append("    ELSE ' ' ")
			.append("  END as \"sgEmpresa\", ")
			.append("  aw.TP_STATUS_AWB as \"tpStatusAwb\", ")
			.append("  MAX(dc.BL_ENTREGA_DIRETA) as \"blEntregaDireta\", ")
			.append("  TO_CHAR(Min(ds.DT_PREV_ENTREGA), 'DD/MM/YYYY') as \"dtPrevEntrega\", ")
			.append("  pes.NM_PESSOA as \"cliente\", ")
			.append("  ep.DS_ENDERECO as \"endereco\", ")
			.append("  pc.DH_COLETA_DISPONIVEL as \"dhColetaDisponivel\", ")
			.append("  mt.NR_IDENTIFICADOR as \"veiculo\", ")
			.append("  aw.ID_AWB as \"idAwb\", ")
			.append("  pc.ID_PEDIDO_COLETA as \"idPedidoColeta\", ")
			.append("  SUM(ds.qt_volumes) as \"volume\", ")
			.append("  SUM(ds.ps_real) as \"peso\", ")
			.append("  SUM(ds.vl_mercadoria) as \"valor\", ")
			.append("  count(ds.id_docto_servico) as \"nrDocumentos\" ");

		return projection.toString();
	}
	
	private String mountProjectioColetasRealizadas() {
		StringBuilder projection = new StringBuilder();
		projection.append("SELECT ")
			.append("  fr.SG_FILIAL as \"coleta.sgFilial\",")
			.append("  pc.NR_COLETA as \"coleta.nrColeta\", ")
			.append("  CASE ")
			.append("    WHEN aw.NR_AWB < 0 ")
			.append("    THEN TO_CHAR(aw.ID_AWB) ")
			.append("    ELSE aw.DS_SERIE ")
			.append("      ||aw.NR_AWB ")
			.append("      ||aw.DV_AWB ")
			.append("  END as preAwbAwb, ")
			.append("  CASE ")
			.append("    WHEN e.SG_EMPRESA IS NOT NULL ")
			.append("    THEN e.SG_EMPRESA ")
			.append("    ELSE ' ' ")
			.append("  END 						   as sgEmpresa, ")
			.append("  aw.TP_STATUS_AWB 		   as tpStatusAwb, ")
			.append("  dc.BL_ENTREGA_DIRETA 	   as blEntregaDireta, ")
			.append("  TO_CHAR(ds.DT_PREV_ENTREGA, 'DD/MM/YYYY') as dtPrevEntrega, ")
			.append("  pessCliente.nm_pessoa       AS nmCliente, ")
			.append("  epCliente.ds_endereco       AS endCliente, ")
			.append("  pc.DH_COLETA_DISPONIVEL 	   as dhColetaDisponivel, ")
			.append("  mt.NR_IDENTIFICADOR 		   as veiculo, ")
			.append("  pc.ID_PEDIDO_COLETA 		   as idPedidoColeta, ")
			.append("  ds.nr_docto_servico         AS nrDoctoServico, ")
			.append("  fmc.sg_filial || ' ' || mc.nr_manifesto         AS nrManifesto, ")
			.append("  fod.sg_filial               AS sgFilialOrigemDocto, ")
			.append("  ds.qt_volumes 			   as volumes, ")			
			.append("  ").append(PropertyVarcharI18nProjection.createProjection("de.ds_descricao_evento_i")).append("    AS dsEvento, ")
			.append("  TO_CHAR(eds.dh_evento, 'DD/MM/YYYY hh24:mi TZR') AS dhEvento, ")
			.append("  ds.ps_real	 			   as peso, ")
			.append("  ds.vl_mercadoria		 	   as valor ");

		return projection.toString();
	}
	
	private String mountFromColetasPendentes() {
		StringBuilder from = new StringBuilder();
		
		from.append("FROM ")
		.append("  awb aw, ")
		.append("  cia_filial_mercurio cia, ")
		.append("  empresa e, ")
		.append("  cto_awb cto, ")
		.append("  conhecimento co, ")
		.append("  docto_servico ds, ")
		.append("  detalhe_coleta dc, ")
		.append("  pedido_coleta pc, ")
		.append("  manifesto_coleta mc, ")
		.append("  controle_carga cc, ")
		.append("  meio_transporte mt, ")
		.append("  filial fr, ")
		.append("  cliente cli, ")
		.append("  pessoa pes, ")
		.append("  endereco_pessoa ep, ")
		.append("  moeda m ");
		
		return from.toString();
	}
	
	private String mountFromColetasRealizadas() {
		StringBuilder from = new StringBuilder();
		
		from.append("FROM ")
		.append("  awb aw, ")
		.append("  cia_filial_mercurio cia, ")
		.append("  empresa e, ")
		.append("  cto_awb cto, ")
		.append("  conhecimento co, ")
		.append("  docto_servico ds, ")		
		.append("  PESSOA pessCliente, ")
		.append("  ENDERECO_PESSOA epCliente, ")
		.append("  FILIAL fod, ")
		.append("  detalhe_coleta dc, ")
		.append("  pedido_coleta pc, ")
		.append("  manifesto_coleta mc, ")
		.append("  filial fmc, ")
		.append("  controle_carga cc, ")
		.append("  meio_transporte mt, ")
		.append("  filial fr, ")
		.append("  moeda m, ")
		.append("  EVENTO_DOCUMENTO_SERVICO eds, ")
		.append("  EVENTO ev, ")
		.append("  DESCRICAO_EVENTO de ");
		
		return from.toString();
	}
	
	private String mountWhereColetasPendentes(TypedFlatMap parameter) {
		parameter.put("blEventoCancelado", Boolean.FALSE);
		parameter.put("cdEvento", ConstantesEventosDocumentoServico.CD_EVENTO_COLETA_REALIZADA_AEROPORTO);
		
		StringBuilder where = new StringBuilder();
		
		where.append("WHERE ")
		.append("    aw.id_cia_filial_mercurio = cia.id_cia_filial_mercurio ")
		.append("AND cia.id_empresa = e.id_empresa ")
		.append("AND aw.id_awb = cto.id_awb ")
		.append("AND cto.id_conhecimento = co.id_conhecimento ")
		.append("AND co.id_conhecimento = ds.id_docto_servico ")
		.append("AND ds.id_docto_servico = dc.id_docto_servico ")
		.append("AND pc.id_pedido_coleta  = dc.id_pedido_coleta ")
		.append("AND pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
		.append("AND mc.id_controle_carga   = cc.id_controle_carga ")
		.append("AND mt.id_meio_transporte = cc.id_transportado ")
		.append("AND pc.id_filial_responsavel = fr.id_filial ")
		.append("AND pc.id_cliente = cli.id_cliente ")
		.append("AND cli.id_cliente = pes.id_pessoa ")
		.append("AND pes.id_endereco_pessoa = ep.id_endereco_pessoa ")
		.append("AND pc.id_moeda = m.id_moeda ")
		.append("AND    pc.tp_status_coleta in ('MA', 'TR') ")
		.append("AND cc.dh_saida_coleta_entrega is not null ")
		.append("AND cc.id_filial_origem = :idFilialSessao ")
		.append("AND aw.id_awb = ( SELECT MAX(aw2.id_awb) ")
		.append("                  FROM cto_awb cto2, ")
		.append("                    	awb aw2, ")
		.append("                    	conhecimento co2 ")
		.append("                  WHERE ")
		.append("					 	cto2.id_awb = aw2.id_awb")
		.append("				   AND 	cto2.id_conhecimento = co2.id_conhecimento")
		.append("                  AND 	co2.id_conhecimento = co.id_conhecimento ")
		.append("                ) ")
		.append("AND NOT EXISTS ( ")
		.append("                SELECT 1 ")
		.append("                FROM evento_documento_servico eds, ")
		.append("                  	  evento ev, ")
		.append("                  	  docto_servico ds2 ")
		.append("                WHERE ")
		.append("				 	 eds.id_evento = ev.id_evento")
		.append("				 AND eds.id_docto_servico = ds2.id_docto_servico")
		.append("                AND eds.id_docto_servico = ds.id_docto_servico ")
		.append("                AND ev.cd_evento = :cdEvento ")
		.append("                AND eds.bl_evento_cancelado = :blEventoCancelado ")
		.append("				) ")
		.append("AND NOT EXISTS ( ")
		.append("  				SELECT 1 ")
		.append("				FROM evento_coleta ec ")
		.append("				WHERE  ")
		.append("					ec.id_pedido_coleta = pc.id_pedido_coleta")
		.append("				AND ec.tp_evento_coleta ='RC'")
		.append("				) ")
		;
		
		Long idControleCarga = parameter.getLong("idControleCarga");
		if (idControleCarga != null) {
			where.append(" AND cc.id_controle_carga = :idControleCarga ");
		}
		
		Long idMeioTransporte = parameter.getLong("idMeioTransporte");
		if (idMeioTransporte != null) {
			where.append(" AND mt.id_meio_transporte = :idMeioTransporte ");
		}
		
		Long idAwb = parameter.getLong("idAwb");
		if (idAwb != null) {
			where.append(" AND aw.id_awb = :idAwb ");
		}
		
		Long idConhecimento = parameter.getLong("idConhecimento");
		if (idConhecimento != null) {
			where.append(" AND co.id_conhecimento = :idConhecimento ");
		}
		
		Long idManifestoColeta = parameter.getLong("idManifestoColeta");
		if (idManifestoColeta != null) {
			where.append(" AND mc.id_manifesto_coleta = :idManifestoColeta ");
		}		
		
		return where.toString();
	}
	
	private String mountWhereColetasRealizadas(TypedFlatMap parameter) {
		parameter.put("blEventoCancelado", Boolean.FALSE);
		parameter.put("cdEventoColetaAereo", ConstantesEventosDocumentoServico.CD_EVENTO_COLETA_REALIZADA_AEROPORTO);
		
		StringBuilder where = new StringBuilder();
		
		where.append("WHERE ")
		.append("    aw.id_cia_filial_mercurio = cia.id_cia_filial_mercurio ")
		.append("AND cia.id_empresa = e.id_empresa ")
		.append("AND aw.id_awb = cto.id_awb ")
		.append("AND cto.id_conhecimento = co.id_conhecimento ")
		.append("AND co.id_conhecimento = ds.id_docto_servico ")		
		.append("AND ds.id_filial_origem = fod.id_filial ")
		.append("AND ds.id_docto_servico = dc.id_docto_servico ")
		.append("AND ds.id_cliente_destinatario      = pessCliente.id_pessoa ")
		.append("AND pessCliente.id_endereco_pessoa  = epCliente.id_endereco_pessoa	")
		.append("AND pc.id_pedido_coleta  = dc.id_pedido_coleta ")
		.append("AND pc.id_manifesto_coleta = mc.id_manifesto_coleta ")
		.append("AND mc.id_filial_origem = fmc.id_filial ")
		.append("AND mc.id_controle_carga   = cc.id_controle_carga ")
		.append("AND mt.id_meio_transporte = cc.id_transportado ")
		.append("AND eds.id_docto_servico 			 = ds.id_docto_servico ")
		.append("AND eds.id_evento 					 = ev.id_evento ")
		.append("AND ev.id_descricao_evento 		 = de.id_descricao_evento ")
		.append("AND eds.bl_evento_cancelado         = :blEventoCancelado ")
		.append("AND ev.cd_evento                    = :cdEventoColetaAereo ")
		.append("AND pc.id_filial_responsavel = fr.id_filial ")
		.append("AND cc.tp_status_controle_carga <> 'FE'  ")
		.append("AND cc.tp_status_controle_carga <> 'CA'  ")
		.append("AND pc.id_moeda = m.id_moeda ")
		.append("AND    pc.tp_status_coleta not in ('CA') ")
		.append("AND cc.dh_saida_coleta_entrega is not null ")
		.append("AND cc.id_filial_origem = :idFilialSessao ")
		.append("AND aw.id_awb = ( SELECT MAX(aw2.id_awb) ")
		.append("                  FROM cto_awb cto2, ")
		.append("                    	awb aw2, ")
		.append("                    	conhecimento co2 ")
		.append("                  WHERE ")
		.append("					 	cto2.id_awb = aw2.id_awb")
		.append("				   AND 	cto2.id_conhecimento = co2.id_conhecimento")
		.append("                  AND 	co2.id_conhecimento = co.id_conhecimento ")
		.append("                ) ");
		
		
		Long idControleCarga = parameter.getLong("idControleCarga");
		if (idControleCarga != null) {
			where.append(" AND cc.id_controle_carga = :idControleCarga ");
		}
		
		Long idMeioTransporte = parameter.getLong("idMeioTransporte");
		if (idMeioTransporte != null) {
			where.append(" AND mt.id_meio_transporte = :idMeioTransporte ");
		}
		
		Long idAwb = parameter.getLong("idAwb");
		if (idAwb != null) {
			where.append(" AND aw.id_awb = :idAwb ");
		}
		
		Long idConhecimento = parameter.getLong("idConhecimento");
		if (idConhecimento != null) {
			where.append(" AND co.id_conhecimento = :idConhecimento ");
		}
		
		Long idManifestoColeta = parameter.getLong("idManifestoColeta");
		if (idManifestoColeta != null) {
			where.append(" AND mc.id_manifesto_coleta = :idManifestoColeta ");
		}		
		
		return where.toString();
	}
	
	private String mountGroupByColetasPendentes(){
		StringBuilder groupBy = new StringBuilder();
		
		groupBy.append("GROUP BY ")
		.append(" fr.SG_FILIAL, ")
		.append(" pc.NR_COLETA,")
		.append(" CASE ")
		.append("    WHEN aw.NR_AWB<0 ")
		.append("    THEN TO_CHAR(aw.ID_AWB) ")
		.append("    ELSE aw.DS_SERIE ")
		.append("      ||aw.NR_AWB ")
		.append("      ||aw.DV_AWB ")
		.append("  END , ")
		.append("  CASE ")
		.append("    WHEN e.SG_EMPRESA IS NOT NULL ")
		.append("    THEN e.SG_EMPRESA ")
		.append("    ELSE ' ' ")
		.append("  END , ")
		.append("  aw.TP_STATUS_AWB , ")
		.append("  pes.NM_PESSOA , ")
		.append("  ep.DS_ENDERECO , ")
		.append("  pc.DH_COLETA_DISPONIVEL, ")
		.append("  pc.DH_COLETA_DISPONIVEL_TZR , ")
		.append("  mt.NR_IDENTIFICADOR , ")
		.append("  aw.ID_AWB , ")
		.append("  pc.ID_PEDIDO_COLETA ")
		.append("ORDER BY 1,2,4,3 ");
		
		return groupBy.toString();
	}
	
	private String mountGroupByColetasRealizadas(){
		StringBuilder groupBy = new StringBuilder();
		
		groupBy.append("GROUP BY ")
		.append("  fr.SG_FILIAL,")
			.append("  pc.NR_COLETA, ")
			.append("  CASE ")
			.append("    WHEN aw.NR_AWB < 0 ")
			.append("    THEN TO_CHAR(aw.ID_AWB) ")
			.append("    ELSE aw.DS_SERIE ")
			.append("      ||aw.NR_AWB ")
			.append("      ||aw.DV_AWB ")
			.append("  END, ")
			.append("  CASE ")
			.append("    WHEN e.SG_EMPRESA IS NOT NULL ")
			.append("    THEN e.SG_EMPRESA ")
			.append("    ELSE ' ' ")
			.append("  END, ")
			.append("  aw.TP_STATUS_AWB, ")
			.append("  dc.BL_ENTREGA_DIRETA, ")
			.append("  TO_CHAR(ds.DT_PREV_ENTREGA, 'DD/MM/YYYY'), ")
			.append("  pessCliente.nm_pessoa, ")
			.append("  epCliente.ds_endereco, ")
			.append("  pc.DH_COLETA_DISPONIVEL, ")
			.append("  mt.NR_IDENTIFICADOR, ")
			.append("  pc.ID_PEDIDO_COLETA, ")
			.append("  ds.nr_docto_servico, ")
			.append("  fmc.sg_filial || ' ' || mc.nr_manifesto, ")
			.append("  fod.sg_filial, ")
			.append("  ds.qt_volumes, ")
			.append("  ").append(PropertyVarcharI18nProjection.createProjection("de.ds_descricao_evento_i")).append(", ")
			.append("  TO_CHAR(eds.dh_evento, 'DD/MM/YYYY hh24:mi TZR'), ")
			.append("  ds.ps_real, ")
			.append("  ds.vl_mercadoria ")
			.append("ORDER BY 1,2,4,3 ");
		
		return groupBy.toString();
	}
	
	private String subQueryPedidoColetasPendentes() {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT 1 FROM ")
		.append(EventoDocumentoServico.class.getSimpleName()).append(" as eds ")
		.append(" JOIN eds.evento e ")
		.append(" JOIN eds.doctoServico ds ")
		.append(" WHERE ds.idDoctoServico = c.id")
		.append(" AND e.cdEvento = :cdEvento")
		.append(" AND eds.blEventoCancelado = :blEventoCancelado");
		
		return hql.toString();
	}

	public Long getRowCountDocumentosNaoExecutadosByPedidoColeta(Long idPedidoColeta) {
		StringBuilder queryString = new StringBuilder();
		TypedFlatMap parametersValues = new TypedFlatMap();
		parametersValues.put("idPedidoColeta", idPedidoColeta);
		parametersValues.put("blEventoCancelado", Boolean.FALSE);
		parametersValues.put("cdEvento", ConstantesEventosDocumentoServico.CD_EVENTO_COLETA_REALIZADA_AEROPORTO);
		
		queryString
			.append("SELECT count(pc) ")
			.append("FROM ").append(PedidoColeta.class.getSimpleName()).append(" as pc ")
			.append(" JOIN pc.detalheColetas dc ")
			.append(" JOIN dc.doctoServico c ")
			.append(" WHERE ")
			.append(" pc.idPedidoColeta = :idPedidoColeta")
			.append(" AND NOT EXISTS (")
			.append(subQueryPedidoColetasPendentes())
			.append(")")
			;
		
		return (Long)getAdsmHibernateTemplate().findUniqueResult(queryString.toString(), parametersValues);
	}
	
	public Long getRowCountDocumentosRetornados(Long idPedidoColeta) {
		StringBuilder queryString = new StringBuilder();
		TypedFlatMap parametersValues = new TypedFlatMap();
		parametersValues.put("idPedidoColeta", idPedidoColeta);
		parametersValues.put("blEventoCancelado", Boolean.FALSE);
		parametersValues.put("cdEvento", ConstantesEventosDocumentoServico.CD_EVENTO_COLETA_AEROPORTO_NAO_EXECUTADA);
		
		queryString
			.append("SELECT count(pc) ")
			.append("FROM ").append(PedidoColeta.class.getSimpleName()).append(" as pc ")
			.append(" JOIN pc.detalheColetas dc ")
			.append(" JOIN dc.doctoServico c ")
			.append(" WHERE ")
			.append(" pc.idPedidoColeta = :idPedidoColeta")
			.append(" AND EXISTS (")
			.append(" SELECT 1 FROM ")
			.append(	EventoDocumentoServico.class.getSimpleName()).append(" as eds ")
			.append(" 	JOIN eds.evento e ")
			.append(" 	JOIN eds.doctoServico ds ")
			.append(" 	WHERE ds.idDoctoServico = c.id")
			.append(" 	AND e.cdEvento = :cdEvento")
			.append(" 	AND eds.blEventoCancelado = :blEventoCancelado")
			.append(")");
		
		return (Long)getAdsmHibernateTemplate().findUniqueResult(queryString.toString(), parametersValues);
	}
	
	private ConfigureSqlQuery configureSqlQueryColetasPendentes() {
		return new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("coleta.sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("coleta.nrColeta", Hibernate.STRING);
				sqlQuery.addScalar("preAwbAwb", Hibernate.STRING);
				sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
				sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
				sqlQuery.addScalar("blEntregaDireta");
				sqlQuery.addScalar("dtPrevEntrega",Hibernate.STRING);
				sqlQuery.addScalar("endereco", Hibernate.STRING);
				sqlQuery.addScalar("dhColetaDisponivel", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("veiculo", Hibernate.STRING);
				sqlQuery.addScalar("idAwb", Hibernate.LONG);
				sqlQuery.addScalar("idPedidoColeta", Hibernate.LONG);
				sqlQuery.addScalar("volume", Hibernate.LONG);
				sqlQuery.addScalar("peso", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("valor", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrDocumentos", Hibernate.INTEGER);
				sqlQuery.addScalar("cliente", Hibernate.STRING);
			}
		};		
	}
	
	private ConfigureSqlQuery configureSqlQueryColetasRealizadas() {
		return new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("coleta.sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("coleta.nrColeta", Hibernate.STRING);
				sqlQuery.addScalar("preAwbAwb", Hibernate.STRING);
				sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
				sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
				sqlQuery.addScalar("blEntregaDireta");
				sqlQuery.addScalar("dtPrevEntrega",Hibernate.STRING);
				sqlQuery.addScalar("nmCliente", Hibernate.STRING);
				sqlQuery.addScalar("endCliente", Hibernate.STRING);
				sqlQuery.addScalar("dhColetaDisponivel", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("veiculo", Hibernate.STRING);
				sqlQuery.addScalar("idPedidoColeta", Hibernate.LONG);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				sqlQuery.addScalar("nrManifesto", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrigemDocto", Hibernate.STRING);
				sqlQuery.addScalar("volumes", Hibernate.INTEGER);
				sqlQuery.addScalar("dsEvento", Hibernate.STRING);
				sqlQuery.addScalar("dhEvento", Hibernate.STRING);
				sqlQuery.addScalar("peso", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("valor", Hibernate.BIG_DECIMAL);
			}
		};
	}
	
	public List<Map<String, Object>> findPedidoColetaJaInseridoByAwb(Long idAwb) {
		
		StringBuilder s = new StringBuilder();
		
		s.append(" SELECT dc.id_pedido_coleta from detalhe_coleta dc, pedido_coleta pc, ");
		s.append("  cto_awb cto, awb a ");
		s.append(" where dc.id_pedido_coleta =  pc.id_pedido_coleta	");
		s.append("  and dc.id_docto_servico = cto.id_conhecimento ");
		s.append("  and cto.id_awb = a.id_awb ");
		s.append("  and pc.id_filial_solicitante = a.id_filial_destino ");
		s.append("	and pc.tp_status_coleta <> 'CA' ");
		s.append("  and a.id_awb = :idAwb ");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = new TypedFlatMap();		
       	params.put("idAwb", idAwb); 
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(s.toString(), params, getConfigureSqlQueryPedidoColetaJaInseridoByAwb());
	}

	private ConfigureSqlQuery getConfigureSqlQueryPedidoColetaJaInseridoByAwb() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_pedido_coleta", Hibernate.LONG);	
			}
		};
		
		return csq;
	}
	
	/**
     * Retorna uma lista de Documentos de Servico a partir de um Controle de Carga de Coleta gerado automaticamente para parceira.
     * @param idControleCarga
     * @return
     */
    public PedidoColeta findPedidoColetaByIdDoctoServicoAndTpColeta(Long idDoctoServico, String tpPedidoColeta){
    	StringBuilder sql = new StringBuilder()
    	.append("select pc ")
    	.append(" from ")
    	.append(DoctoServico.class.getName()).append(" as ds ")
    	.append(" inner join ds.pedidoColeta pc ")
    	.append(" where ")
    	.append(" ds.idDoctoServico = ? ")
    	.append(" and pc.tpPedidoColeta = ? ");
    	
    	return (PedidoColeta) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idDoctoServico, tpPedidoColeta});
    }
	
    /**
     * Busca um pedido pelo código de coleta do cliente
     * 
     * @param cdColetaCliente
     * @return Pedido Coleta
     */
	public PedidoColeta findByCdColetaCliente(String cdColetaCliente) {
		
		PedidoColeta pedidoColeta = null;
		
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);		
		dc.add(Restrictions.eq("cdColetaCliente", cdColetaCliente));

		List<PedidoColeta> result = super.findByDetachedCriteria(dc);
		
		if(result.size() == 1) {
			pedidoColeta = result.get(0);
		}
		
		return pedidoColeta;
	}
	
	public List<Map<String, Object>> findInfoColetasAbertas(Map<String, Object> parameters) {
        return getAdsmHibernateTemplate().findBySqlToMappedResult(getSqlRelatorioExcelColetasAbertas(parameters), parameters, configureSqlExcelColetasAbertas());
    }
	
	private ConfigureSqlQuery configureSqlExcelColetasAbertas() {
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("NR_ROTA", Hibernate.STRING);
                sqlQuery.addScalar("DS_ROTA", Hibernate.STRING);
                sqlQuery.addScalar("SG_FILIAL_COLETA", Hibernate.STRING);
                sqlQuery.addScalar("NR_COLETA", Hibernate.STRING);
                sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
                sqlQuery.addScalar("TP_PESSOA", Hibernate.STRING);
                sqlQuery.addScalar("CLIENTE", Hibernate.STRING);
                sqlQuery.addScalar("NR_CEP", Hibernate.STRING);
                sqlQuery.addScalar("ENDERECO", Hibernate.STRING);
                sqlQuery.addScalar("NR_ENDERECO", Hibernate.STRING);
                sqlQuery.addScalar("COMPLEMENTO", Hibernate.STRING);
                sqlQuery.addScalar("MUNICIPIO", Hibernate.STRING);
                sqlQuery.addScalar("UF", Hibernate.STRING);
                sqlQuery.addScalar("VOLUMES", Hibernate.STRING);
                sqlQuery.addScalar("PESO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PESO_AFORADO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("SG_MOEDA", Hibernate.STRING);
                sqlQuery.addScalar("MOEDA", Hibernate.STRING);
                sqlQuery.addScalar("VALOR", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("HR_LIMITE_COLETA", Hibernate.TIMESTAMP);
                sqlQuery.addScalar("HR_CORTE_SOLICITACAO", Hibernate.TIMESTAMP);
            }
        };
    }

    private String getSqlRelatorioExcelColetasAbertas(Map<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT ");
		sb.append("SUBSTR(REGEXP_SUBSTR(SERVICO.DS_SERVICO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(SERVICO.DS_SERVICO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»'))  AS SERVICO, ");
		sb.append("RCE.NR_ROTA as NR_ROTA, ");
		sb.append("TRIM(RCE.DS_ROTA) as DS_ROTA, ");
		sb.append("F_RESP.SG_FILIAL as SG_FILIAL_COLETA, ");
		sb.append("PC.NR_COLETA as NR_COLETA, ");
		sb.append("P_CLIENTE.NR_IDENTIFICACAO as NR_IDENTIFICACAO, ");
		sb.append("P_CLIENTE.TP_PESSOA as TP_PESSOA, ");
		sb.append("TRIM(P_CLIENTE.NM_PESSOA) as CLIENTE, ");
		sb.append("SUBSTR(PC.NR_CEP, 1, 5) || '-' || SUBSTR(PC.NR_CEP, 6, 3)  as NR_CEP, ");
		sb.append("TRIM(PC.ED_COLETA) as ENDERECO, ");
		sb.append("case when NOT (PC.NR_ENDERECO IS NULL) then ', ' || PC.NR_ENDERECO ELSE ' ' end as NR_ENDERECO, ");
		sb.append("TRIM(PC.DS_COMPLEMENTO_ENDERECO) as COMPLEMENTO, ");
		sb.append("TRIM(MUNICIPIO.NM_MUNICIPIO) as MUNICIPIO, ");
		sb.append("UF.SG_UNIDADE_FEDERATIVA as UF, ");
		sb.append("PC.QT_TOTAL_VOLUMES_VERIFICADO as VOLUMES, ");
		sb.append("PC.PS_TOTAL_VERIFICADO as PESO, ");
		sb.append("PC.PS_TOTAL_AFORADO_VERIFICADO as PESO_AFORADO, ");
		sb.append("MOEDA.SG_MOEDA as SG_MOEDA, ");
		sb.append("MOEDA.DS_SIMBOLO as MOEDA, ");
		sb.append("PC.VL_TOTAL_VERIFICADO as VALOR, ");
		sb.append("PC.HR_LIMITE_COLETA as HR_LIMITE_COLETA, ");
		sb.append("RIC.HR_CORTE_SOLICITACAO as HR_CORTE_SOLICITACAO  ");
		
		sb.append("FROM ");
		sb.append("PEDIDO_COLETA PC, ");
		sb.append("DETALHE_COLETA DC, ");
		sb.append("FILIAL F_RESP, ");
		sb.append("CLIENTE, ");
		sb.append("PESSOA P_CLIENTE, ");
		sb.append("ROTA_COLETA_ENTREGA RCE, ");
		sb.append("SERVICO, ");
		sb.append("MUNICIPIO, ");
		sb.append("UNIDADE_FEDERATIVA UF, "); 
		sb.append("MOEDA, ");
		sb.append("ROTA_INTERVALO_CEP RIC ");
		
		sb.append("WHERE DC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ");
		sb.append("AND PC.ID_FILIAL_RESPONSAVEL = F_RESP.ID_FILIAL ");
		sb.append("AND DC.ID_SERVICO = SERVICO.ID_SERVICO ");
		sb.append("AND PC.ID_ROTA_COLETA_ENTREGA = RCE.ID_ROTA_COLETA_ENTREGA ");
		sb.append("AND PC.ID_CLIENTE = CLIENTE.ID_CLIENTE ");
		sb.append("AND CLIENTE.ID_CLIENTE = P_CLIENTE.ID_PESSOA ");
		sb.append("AND PC.ID_MUNICIPIO = MUNICIPIO.ID_MUNICIPIO ");
		sb.append("AND MUNICIPIO.ID_UNIDADE_FEDERATIVA = UF.ID_UNIDADE_FEDERATIVA ");
		sb.append("AND PC.ID_MOEDA = MOEDA.ID_MOEDA ");
		sb.append("AND PC.ID_ROTA_INTERVALO_CEP = RIC.ID_ROTA_INTERVALO_CEP ");
		sb.append("AND PC.TP_STATUS_COLETA = 'AB' ");
		
        if (parameters.get("idFilial") != null) {
        	sb.append("AND PC.ID_FILIAL_RESPONSAVEL = :idFilial ");
        }
        if (parameters.get("idRotaColetaEntrega") != null) {
        	sb.append("AND PC.ID_ROTA_COLETA_ENTREGA = :idRotaColetaEntrega ");
        }
        if (parameters.get("idServico") != null) {
        	sb.append("AND DC.ID_SERVICO = :idServico ");
        }
        if (parameters.get("dataInicial") != null) {
        	sb.append("AND PC.DT_PREVISAO_COLETA >= :dataInicial ");
        }
        if (parameters.get("dataFinal") != null) {
        	sb.append("AND PC.DT_PREVISAO_COLETA <= :dataFinal ");
        }
        
		sb.append("ORDER BY ");
		sb.append("SUBSTR(REGEXP_SUBSTR(SERVICO.DS_SERVICO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(SERVICO.DS_SERVICO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) , ");
		sb.append("RCE.NR_ROTA, ");
		sb.append("F_RESP.SG_FILIAL, ");
		sb.append("PC.NR_COLETA ");
		
        return sb.toString();
    }

	public List<PedidoColeta> findPedidoColetaByIds(List<Long> idsPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(PedidoColeta.class);
		dc.add(Restrictions.in("idPedidoColeta", idsPedidoColeta));
		return super.findByDetachedCriteria(dc);
	}
}