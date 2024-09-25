package com.mercurio.lms.portaria.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.StringBuilderSQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.PassThroughResultTransformer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

/**
 * LMSA-768 - DAO para carga de dados, validação e processos "Informar
 * Chegada/Saída na Portaria".
 *
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 */
public class NewSaidaChegadaDAO extends AdsmDao {

	public static final String COLS_EVENTO_DOCUMENTO_SERVICO = "DH_EVENTO, DH_EVENTO_TZR, DH_INCLUSAO, DH_INCLUSAO_TZR, BL_EVENTO_CANCELADO, NR_DOCUMENTO, " +
			"OB_COMPLEMENTO, DH_GERACAO_PARCEIRO, DH_GERACAO_PARCEIRO_TZR, DH_COMUNICACAO, DH_COMUNICACAO_TZR, TP_DOCUMENTO, ID_USUARIO, " +
			"ID_DOCTO_SERVICO, ID_EVENTO, ID_FILIAL, ID_PEDIDO_COMPRA, ID_OCORRENCIA_ENTREGA, ID_OCORRENCIA_PENDENCIA";

	/**
	 * Segmento de HQL para relacionamentos com {@link ControleCarga}.
	 *
	 * @see /lms-entities/src/main/resources/com/mercurio/lms/carregamento/model/Manifesto.hbm.xml
	 */
	private static final String SQL_JOIN_FETCH_CONTROLE_CARGA =
			"JOIN FETCH cc.filialByIdFilialOrigem f "
					+ "JOIN FETCH f.pessoa "
					+ "LEFT JOIN FETCH cc.meioTransporteByIdTransportado mt1 "
					+ "LEFT JOIN FETCH mt1.meioTransporteRodoviario "
					+ "LEFT JOIN FETCH cc.meioTransporteByIdSemiRebocado mt2 "
					+ "LEFT JOIN FETCH mt2.meioTransporteRodoviario "
					+ "LEFT JOIN FETCH cc.manifestos m "
					+ "LEFT JOIN FETCH m.manifestoEntrega " // outer-join="auto", Manifesto.hbm.xml line 244
					+ "LEFT JOIN FETCH m.manifestoViagemNacional "
					+ "LEFT JOIN FETCH m.manifestoInternacional "; // outer-join="auto", Manifesto.hbm.xml line 287

	/**
	 * Segmento de HQL para relacionamentos com {@link DoctoServico}.
	 *
	 * @see /lms-entities/src/main/resources/com/mercurio/lms/expedicao/model/DoctoServico.hbm.xml
	 * @see /lms-entities/src/main/resources/com/mercurio/lms/sim/model/LocalizacaoMercadoria.hbm.xml
	 */
	private static final String SQL_JOIN_FETCH_DOCTO_SERVICO =
			"JOIN FETCH ds.filialByIdFilialDestino " //lazy="false", DoctoServico.hbm.xml line 613
					+ "LEFT JOIN FETCH ds.localizacaoMercadoria lm "
					+ "LEFT JOIN FETCH lm.faseProcesso " //lazy="false", LocalizacaoMercadoria.hbm.xml line 50
					+ "LEFT JOIN FETCH ds.servico ";

	/**
	 * Select SQL para busca de Id's dos {@link DoctoServico}s relacionados a um
	 * {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s e {@link ManifestoColeta}s com
	 * respectivos atributos {@code tpStatusManifesto} e
	 * {@code tpStatusManifestoColeta} de valor {@code 'CA'} (Cancelado).
	 */
	private static final String SQL_SUBSELECT_ID_DOCTO_SERVICO =
			"SELECT pmd.id_docto_servico "
					+ "FROM pre_manifesto_documento pmd "
					+ "JOIN manifesto m ON m.id_manifesto = pmd.id_manifesto "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT mnc.id_conhecimento "
					+ "FROM manifesto_nacional_cto mnc "
					+ "JOIN manifesto m ON m.id_manifesto = mnc.id_manifesto_viagem_nacional "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT dc.id_docto_servico "
					+ "FROM detalhe_coleta dc "
					+ "JOIN pedido_coleta pc ON pc.id_pedido_coleta = dc.id_pedido_coleta "
					+ "JOIN manifesto_coleta mc ON mc.id_manifesto_coleta = pc.id_manifesto_coleta "
					+ "WHERE dc.id_docto_servico IS NOT NULL "
					+ "AND mc.id_controle_carga = :idControleCarga "
					+ "AND mc.tp_status_manifesto_coleta <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT med.id_docto_servico "
					+ "FROM manifesto_entrega_documento med "
					+ "JOIN manifesto m ON m.id_manifesto = med.id_manifesto_entrega "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA ";

	/**
	 * Select SQL para busca de Id's dos {@link VolumeNotaFiscal}s relacionados
	 * a um {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s e {@link ManifestoColeta}s com
	 * respectivos atributos {@code tpStatusManifesto} e
	 * {@code tpStatusManifestoColeta} de valor {@code 'CA'} (Cancelado).
	 */
	private static final String SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL =
			"SELECT pmv.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN pre_manifesto_volume pmv ON pmv.id_manifesto = m.id_manifesto "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT mnv.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN manifesto_nacional_volume mnv ON mnv.id_manifesto_viagem_nacional = m.id_manifesto "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT vnf.id_volume_nota_fiscal "
					+ "FROM volume_nota_fiscal vnf "
					+ "JOIN nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento "
					+ "JOIN detalhe_coleta dc ON dc.id_docto_servico = nfc.id_conhecimento "
					+ "JOIN pedido_coleta pc ON pc.id_pedido_coleta = dc.id_pedido_coleta "
					+ "JOIN manifesto_coleta mc ON mc.id_manifesto_coleta = pc.id_manifesto_coleta "
					+ "WHERE mc.id_controle_carga = :idControleCarga "
					+ "AND mc.tp_status_manifesto_coleta <> :tpStatusCA "
					+ "  UNION "
					+ "SELECT mev.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN manifesto_entrega_volume mev ON mev.id_manifesto_entrega = m.id_manifesto "
					+ "WHERE m.id_controle_carga = :idControleCarga "
					+ "AND m.tp_status_manifesto <> :tpStatusCA ";

	private static final String SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL_BY_MANIFESTO =
			"SELECT pmv.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN pre_manifesto_volume pmv ON pmv.id_manifesto = m.id_manifesto "
					+ "WHERE m.id_manifesto = :idManifesto "
					+ "AND m.tp_status_manifesto in (:tpStatusAD, :tpStatusEF) "
					+ "  UNION "
					+ "SELECT mnv.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN manifesto_nacional_volume mnv ON mnv.id_manifesto_viagem_nacional = m.id_manifesto "
					+ "WHERE m.id_manifesto = :idManifesto "
					+ "AND m.tp_status_manifesto in (:tpStatusAD, :tpStatusEF) "
					+ "  UNION "
					+ "SELECT vnf.id_volume_nota_fiscal "
					+ "FROM volume_nota_fiscal vnf "
					+ "JOIN nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento "
					+ "JOIN detalhe_coleta dc ON dc.id_docto_servico = nfc.id_conhecimento "
					+ "JOIN pedido_coleta pc ON pc.id_pedido_coleta = dc.id_pedido_coleta "
					+ "JOIN manifesto_coleta mc ON mc.id_manifesto_coleta = pc.id_manifesto_coleta "
					+ "WHERE mc.id_manifesto_coleta = :idManifesto "
					+ "AND mc.tp_status_manifesto_coleta in (:tpStatusAD, :tpStatusEF) "
					+ "  UNION "
					+ "SELECT mev.id_volume_nota_fiscal "
					+ "FROM manifesto m "
					+ "JOIN manifesto_entrega_volume mev ON mev.id_manifesto_entrega = m.id_manifesto "
					+ "WHERE m.id_manifesto = :idManifesto "
					+ "AND m.tp_status_manifesto in (:tpStatusAD, :tpStatusEF) ";
	private static final int REGISTER_LIMIT = 500;
	private static final String ID_EVENTO = "idEvento";
	private static final String OB_COMPLEMENTO = "obComplemento";
	private static final String ID_FILIAL = "idFilial";
	private static final String ID_LOCALIZACAO_MERCADORIA = "idLocalizacaoMercadoria";
	private static final String ID_CONTROLE_CARGA = "idControleCarga";
	private static final String TP_STATUS_CA = "tpStatusCA";
	private static final String DH_EVENTO = "dhEvento";
	private static final String TP_STATUS_AD = "tpStatusAD";
	private static final String DH_CHEGADA = "dhChegada";
	private static final String DH_CHEGADA_TZR = "dhChegadaTzr";
	private static final String TP_STATUS_TC = "tpStatusTC";
	private static final String CONTROLE_CARGA = "controleCarga";

	/*
     * Carga de dados para operações de Viagem, Coleta/Entrega e Ordem de Saída
	 */

	/**
	 * Busca de {@link ControleTrecho} e respectivos relacionamentos para
	 * processo de "Informar Chegada na Portaria".
	 *
	 * @param idControleTrecho
	 * @return
	 */
	public ControleTrecho findControleTrecho(Long idControleTrecho) {
		ControleTrecho controleTrecho = (ControleTrecho) getSession().createQuery(
				"FROM ControleTrecho ct "
						+ "JOIN FETCH ct.controleCarga cc "
						+ SQL_JOIN_FETCH_CONTROLE_CARGA
						+ "WHERE ct.idControleTrecho = :idControleTrecho")
				.setLong("idControleTrecho", idControleTrecho)
				.uniqueResult();
		ControleCarga controleCarga = controleTrecho.getControleCarga();
		fetchPreManifestoDocumentos(controleCarga);
		fetchManifestoNacionalCtos(controleCarga);
		fetchManifestoNacionalVolumes(controleCarga);
		return controleTrecho;
	}

	/**
	 * Busca de {@link ControleTrecho} e respectivos relacionamentos para
	 * processo de "Informar Saída na Portaria", a partir do
	 * {@link ControleCarga} e {@link Filial}.
	 *
	 * @param controleCarga
	 * @param filial
	 * @return
	 */
	public ControleTrecho findControleTrecho(Long idControleCarga, Long idFilial) {
		ControleTrecho controleTrecho = (ControleTrecho) getSession().createQuery(
				"FROM ControleTrecho ct "
						+ "JOIN FETCH ct.filialByIdFilialDestino fd "
						+ "JOIN FETCH fd.pessoa "
						+ "JOIN FETCH ct.controleCarga cc "
						+ "JOIN FETCH cc.filialByIdFilialOrigem f "
						+ "JOIN FETCH f.pessoa "
						+ "LEFT JOIN FETCH cc.meioTransporteByIdTransportado mt1 "
						+ "LEFT JOIN FETCH mt1.meioTransporteRodoviario "
						+ "LEFT JOIN FETCH cc.meioTransporteByIdSemiRebocado mt2 "
						+ "LEFT JOIN FETCH mt2.meioTransporteRodoviario "
						+ "LEFT JOIN FETCH cc.manifestos m "
						+ "LEFT JOIN FETCH m.manifestoEntrega " // outer-join="auto", Manifesto.hbm.xml line 244
						+ "LEFT JOIN FETCH m.manifestoViagemNacional "
						+ "LEFT JOIN FETCH m.manifestoInternacional " // outer-join="auto", Manifesto.hbm.xml line 287
						+ "WHERE cc.idControleCarga = :idControleCarga "
						+ "AND ct.filialByIdFilialOrigem.idFilial = :idFilial "
						+ "AND ct.blTrechoDireto = 'S'")
				.setLong(ID_CONTROLE_CARGA, idControleCarga)
				.setLong(ID_FILIAL, idFilial)
				.uniqueResult();
		ControleCarga controleCarga = controleTrecho.getControleCarga();
		Hibernate.initialize(controleCarga.getControleTrechos());
		fetchPreManifestoDocumentos(controleCarga);
		fetchPreManifestoVolumes(controleCarga);
		fetchManifestoNacionalCtos(controleCarga);
		fetchManifestoNacionalVolumes(controleCarga);
		return controleTrecho;
	}

	/**
	 * Busca de {@link ControleCarga} e respectivos relacionamentos.
	 *
	 * @param idControleCarga
	 * @return
	 */
	public ControleCarga findControleCarga(Long idControleCarga) {
		ControleCarga controleCarga = (ControleCarga) getSession().createQuery(
				"FROM ControleCarga cc "
						+ SQL_JOIN_FETCH_CONTROLE_CARGA
						+ "WHERE cc.idControleCarga = :idControleCarga")
				.setLong(ID_CONTROLE_CARGA, idControleCarga)
				.uniqueResult();
		fetchPreManifestoDocumentos(controleCarga);
		fetchPreManifestoVolumes(controleCarga);
		Hibernate.initialize(controleCarga.getManifestoColetas());
		fetchPedidoColetas(controleCarga);
		fetchDetalheColetas(controleCarga);
		fetchManifestoEntregaDocumentos(controleCarga);
		fetchManifestoEntregaVolumes(controleCarga);
		return controleCarga;
	}

	/**
	 * Executa carga de {@link PreManifestoDocumento}s e {@link DoctoServico}s
	 * relacionados a um {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchPreManifestoDocumentos(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM Manifesto m "
						+ "LEFT JOIN FETCH m.preManifestoDocumentos pmd "
						+ "JOIN FETCH pmd.doctoServico ds "
						+ SQL_JOIN_FETCH_DOCTO_SERVICO
						+ "WHERE m.controleCarga = :controleCarga "
						+ "AND m.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link PreManifestoVolume}s e {@link VolumeNotaFiscal}s
	 * relacionados a um {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchPreManifestoVolumes(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM Manifesto m "
						+ "LEFT JOIN FETCH m.preManifestoVolumes pmv "
						+ "JOIN FETCH pmv.volumeNotaFiscal "
						+ "WHERE m.controleCarga = :controleCarga "
						+ "AND m.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link ManifestoNacionalCto}s e {@link Conhecimento}s
	 * relacionados aos {@link ManifestoViagemNacional}s de um
	 * {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchManifestoNacionalCtos(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM Manifesto m "
						+ "JOIN FETCH m.manifestoViagemNacional.manifestoNacionalCtos mnc "
						+ "JOIN FETCH mnc.conhecimento ds "
						+ SQL_JOIN_FETCH_DOCTO_SERVICO
						+ "WHERE m.controleCarga = :controleCarga "
						+ "AND m.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link ManifestoNacionalVolume}s e
	 * {@link VolumeNotaFiscal}s relacionados aos
	 * {@link ManifestoViagemNacional}s de um {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchManifestoNacionalVolumes(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM Manifesto m "
						+ "JOIN FETCH m.manifestoViagemNacional.manifestoNacionalVolumes mnv "
						+ "JOIN FETCH mnv.volumeNotaFiscal "
						+ "WHERE m.controleCarga = :controleCarga "
						+ "AND m.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link PedidoColeta}s relacionados aos
	 * {@link ManifestoColeta}s de um {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link ManifestoColeta}s com atributo
	 * {@code tpStatusManifestoColeta} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchPedidoColetas(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM ManifestoColeta mc "
						+ "LEFT JOIN FETCH mc.pedidoColetas "
						+ "WHERE mc.controleCarga = :controleCarga "
						+ "AND mc.tpStatusManifestoColeta <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link DetalheColeta}s e {@link DoctoServico}s
	 * relacionados aos {@link ManifestoColeta}s e {@link PedidoColeta}s de um
	 * {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link ManifestoColeta}s com atributo
	 * {@code tpStatusManifestoColeta} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchDetalheColetas(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM PedidoColeta pc "
						+ "JOIN FETCH pc.detalheColetas dc "
						+ "LEFT JOIN FETCH dc.doctoServico ds "
						+ SQL_JOIN_FETCH_DOCTO_SERVICO
						+ "WHERE pc.manifestoColeta.controleCarga = :controleCarga "
						+ "AND pc.manifestoColeta.tpStatusManifestoColeta <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link ManifestoEntregaDocumento}s e
	 * {@link DoctoServico}s relacionados aos {@link ManifestoEntrega}s de um
	 * {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchManifestoEntregaDocumentos(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM ManifestoEntrega me "
						+ "JOIN FETCH me.manifestoEntregaDocumentos med "
						+ "JOIN FETCH med.doctoServico ds "
						+ SQL_JOIN_FETCH_DOCTO_SERVICO
						+ "WHERE me.manifesto.controleCarga = :controleCarga "
						+ "AND me.manifesto.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Executa carga de {@link ManifestoEntregaVolume}s e
	 * {@link VolumeNotaFiscal}s relacionados aos {@link ManifestoEntrega}s e
	 * aos {@link ManifestoColeta}s e {@link PedidoColeta}s de um
	 * {@link ControleCarga}.
	 * <p>
	 * São desconsiderados os {@link Manifesto}s e {@link ManifestoColeta}s com
	 * respectivos atributos {@code tpStatusManifesto} e
	 * {@code tpStatusManifestoColeta} de valor {@code 'CA'} (Cancelado).
	 *
	 * @param controleCarga
	 */
	private void fetchManifestoEntregaVolumes(ControleCarga controleCarga) {
		getSession().createQuery(
				"FROM ManifestoEntrega me "
						+ "LEFT JOIN FETCH me.manifestoEntregaVolumes mev "
						+ "LEFT JOIN FETCH mev.volumeNotaFiscal "
						+ "WHERE me.manifesto.controleCarga = :controleCarga "
						+ "AND me.manifesto.tpStatusManifesto <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
		getSession().createQuery(
				"FROM DetalheColeta dc "
						+ "JOIN FETCH dc.doctoServico.manifestoEntregaVolumes mev "
						+ "JOIN FETCH mev.volumeNotaFiscal "
						+ "WHERE dc.pedidoColeta.manifestoColeta.controleCarga = :controleCarga "
						+ "AND dc.pedidoColeta.manifestoColeta.tpStatusManifestoColeta <> :tpStatusCA")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.list();
	}

	/**
	 * Busca de {@link OrdemSaida} e respectivos relacionamentos.
	 *
	 * @param idOrdemSaida
	 * @return
	 */
	public OrdemSaida findOrdemSaida(Long idOrdemSaida) {
		return (OrdemSaida) getSession().createQuery(
				"FROM OrdemSaida os "
						+ "LEFT JOIN FETCH os.meioTransporteRodoviarioByIdMeioTransporte "
						+ "LEFT JOIN FETCH os.meioTransporteRodoviarioByIdSemiReboque "
						+ "WHERE os.idOrdemSaida = :idOrdemSaida")
				.setLong("idOrdemSaida", idOrdemSaida)
				.uniqueResult();
	}

	/**
	 * Busca de {@link Box}.
	 *
	 * @param idBox
	 * @return
	 */
	public Box findBox(Long idBox) {
		return (Box) getAdsmHibernateTemplate().get(Box.class, idBox);
	}

	/*
	 * Validações para processos "Informar Chegada/Saída na Portaria"
	 */

	/**
	 * Busca {@link Filial} anterior a determinada {@link Filial} conforme
	 * {@link FilialRotaCc}s de um {@link ControleCarga} sem nenhum
	 * {@link EventoControleCarga} de {@code tpEventoControleCarga} específico.
	 * Caso não exista retorna {@code null}.
	 *
	 * @param controleCarga {@link ControleCarga} relacionado aos {@link FilialRotaCc}s.
	 * @param filial        {@link Filial} referente a busca da {@link Filial} anterior.
	 * @param tpEvento      {@code tpEventoControleCarga} do {@link EventoControleCarga}.
	 * @return {@link Filial} anterior ou {@code null} caso não exista.
	 */
	public Filial findFilialAnteriorSemEvento(ControleCarga controleCarga, Filial filial, String tpEvento) {
		return (Filial) getSession().createQuery(
				"SELECT frc.filial "
						+ "FROM FilialRotaCc frc "
						+ "WHERE frc.controleCarga = :controleCarga "
						+ "AND frc.nrOrdem < ( "
						+ "  SELECT frc2.nrOrdem "
						+ "  FROM FilialRotaCc frc2 "
						+ "  WHERE frc2.controleCarga = :controleCarga "
						+ "  AND frc2.filial = :filial "
						+ ") "
						+ "AND frc.filial NOT IN ( "
						+ "  SELECT ecc.filial "
						+ "  FROM EventoControleCarga ecc "
						+ "  WHERE ecc.controleCarga = :controleCarga "
						+ "  AND ecc.tpEventoControleCarga = :tpEvento "
						+ ")")
				.setEntity(CONTROLE_CARGA, controleCarga)
				.setEntity("filial", filial)
				.setString("tpEvento", tpEvento)
				.uniqueResult();
	}

	/**
	 * Busca contagem de {@link OcorrenciaDoctoServico}s de bloqueio
	 * relacionadas aos {@link Manifesto}s, {@link ManifestoEntrega} e
	 * {@link ManifestoEntregaDocumento} de um {@link ControleCarga}.
	 * <p>
	 * As {@link OcorrenciaDoctoServico} de bloqueio são aquelas cujos atributos
	 * {@code ocorrenciaPendenciaByIdOcorLiberacao} e
	 * {@code ocorrenciaPendenciaByIdOcorBloqueio} tem valor {@code null}.
	 * <p>
	 * Somente são considerados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'TC'} (Em Trânsito de Coleta).
	 *
	 * @param controleCarga {@link ControleCarga} para busca de ocorrências.
	 * @return Contagem de {@link OcorrenciaDoctoServico} de bloqueio.
	 */
	public int countOcorrenciaBloqueio(ControleCarga controleCarga) {
		BigDecimal count = (BigDecimal) getSession().createSQLQuery(
				"SELECT COUNT(*) "
						+ "FROM ocorrencia_docto_servico ods "
						+ "JOIN manifesto_entrega_documento med ON med.id_docto_servico = ods.id_docto_servico "
						+ "JOIN manifesto m ON m.id_manifesto = med.id_manifesto_entrega "
						+ "WHERE ods.id_ocor_liberacao IS NULL "
						+ "AND med.id_ocorrencia_entrega IS NULL "
						+ "AND m.id_controle_carga = :idControleCarga "
						+ "AND m.tp_status_manifesto = :tpStatusTC ")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setString(TP_STATUS_TC, ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA)
				.uniqueResult();
		return count.intValue();
	}

	/**
	 * Busca contagem de {@link OcorrenciaDoctoServico}s de liberação
	 * relacionadas aos {@link Manifesto}s, {@link ManifestoEntrega} e
	 * {@link ManifestoEntregaDocumento} de um {@link ControleCarga} posteriores
	 * a determinada data/hora.
	 * <p>
	 * As {@link OcorrenciaDoctoServico} de liberação são aquelas cujos
	 * atributos {@code ocorrenciaPendenciaByIdOcorLiberacao} tem valor
	 * diferente de {@code null} e destas são consideradas aquelas cujo atributo
	 * {@code dhLiberacao} seja posterior a determinada data/hora.
	 * <p>
	 * Somente são considerados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'TC'} (Em Trânsito de Coleta).
	 *
	 * @param controleCarga {@link ControleCarga} para busca de ocorrências.
	 * @param dhLiberacao   Data/hora limite para busca de ocorrências.
	 * @return Contagem de {@link OcorrenciaDoctoServico} de liberação.
	 */
	public int countOcorrenciaLiberacao(ControleCarga controleCarga, DateTime dhLiberacao) {
		BigDecimal count = (BigDecimal) getSession().createSQLQuery(
				"SELECT COUNT(*) "
						+ "FROM ocorrencia_docto_servico ods "
						+ "JOIN manifesto_entrega_documento med ON med.id_docto_servico = ods.id_docto_servico "
						+ "JOIN manifesto m ON m.id_manifesto = med.id_manifesto_entrega "
						+ "WHERE ods.id_ocor_liberacao IS NOT NULL "
						+ "AND ods.dh_liberacao > :dhLiberacao "
						+ "AND m.id_controle_carga = :idControleCarga "
						+ "AND m.tp_status_manifesto = :tpStatusTC ")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setTimestamp("dhLiberacao", dhLiberacao.toDate())
				.setString(TP_STATUS_TC, ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA)
				.uniqueResult();
		return count.intValue();
	}

	/*
	 * Preparação para processos "Informar Chegada/Saída na Portaria"
	 */

	/**
	 * Busca Id's de {@link LocalizacaoMercadoria} cujo atributo
	 * {@code cdLocalizacaoMercadoria} esteja listado no {@link ParametroGeral}
	 * {@code 'CD_PEND_BLOQ_LOCALIZACAO'}.
	 *
	 * @return Id's de {@link LocalizacaoMercadoria}.
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findPendBloqLocalizacaoList() {
		return getSession().createSQLQuery(
				"SELECT lm.id_localizacao_mercadoria "
						+ "FROM localizacao_mercadoria lm "
						+ "WHERE lm.cd_localizacao_mercadoria IN ( "
						+ "  SELECT REGEXP_SUBSTR(x.ds_conteudo, '\\d+', 1, LEVEL) "
						+ "  FROM ( "
						+ "    SELECT pg.ds_conteudo "
						+ "    FROM parametro_geral pg "
						+ "    WHERE pg.nm_parametro_geral = :nmParametroGeral "
						+ "  ) x "
						+ "  CONNECT BY REGEXP_SUBSTR(x.ds_conteudo, '\\d+', 1, LEVEL) > 0 "
						+ ")")
				.setString("nmParametroGeral", ConstantesPortaria.PARAMETRO_CD_PEND_BLOQ_LOCALIZACAO)
				.setResultTransformer(new BigDecimalToLongResultTransformer())
				.list();
	}

	/**
	 * Busca e prepara {@link Map} de {@link Evento}s com atributo
	 * {@code cdEvento} de valores:
	 * <ul>
	 * <li>{@code 55} (Em Viagem),
	 * <li>{@code 57} (Mercadoria Retornada),
	 * <li>{@code 58} (Em Escala na Filial),
	 * <li>{@code 66} (Previsão de Chegada),
	 * <li>{@code 130} (Aguardando Descarga), ou
	 * <li>{@code 152} (Sem Localização Mercadoria).
	 * </ul>
	 * São incluídos na busca os respectivos {@link DescricaoEvento},
	 * {@link LocalizacaoMercadoria} e {@link FaseProcesso}.
	 *
	 * @return {@link Map} de {@link Evento} por seus respectivos
	 * {@code cdEvento}.
	 * @see /lms-entities/src/main/resources/com/mercurio/lms/sim/model/LocalizacaoMercadoria.hbm.xml
	 */
	public Map<Short, Evento> findCdEventoMap() {
		Map<Short, Evento> map = new HashMap<Short, Evento>();
		@SuppressWarnings("unchecked")
		List<Evento> eventos = getSession().createQuery(
				"FROM Evento e "
						+ "LEFT JOIN FETCH e.descricaoEvento "
						+ "LEFT JOIN FETCH e.localizacaoMercadoria lm "
						+ "LEFT JOIN FETCH lm.faseProcesso " // lazy="false", LocalizacaoMercadoria.hbm.xml line 50
						+ "WHERE e.cdEvento IN (:cdEvento)")
				.setParameterList("cdEvento", new Short[]{
						ConstantesPortaria.CD_EVENTO_EM_VIAGEM,
						ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA,
						ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL,
						ConstantesPortaria.CD_EVENTO_PREVISAO_DE_CHEGADA,
						ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA,
						ConstantesPortaria.CD_EVENTO_SEM_LOCALIZACAO_MERCADORIA
				})
				.list();
		for (Evento evento : eventos) {
			map.put(evento.getCdEvento(), evento);
		}
		return map;
	}

	/**
	 * Busca e prepara {@link Map} de {@link FaseProcesso} com atributo
	 * {@code cdFase} de valor {@code 1} (No Terminal).
	 *
	 * @return {@link Map} de {@link FaseProcesso} por seus respectivos
	 * {@code cdFase}.
	 */
	public Map<Short, FaseProcesso> findCdFaseProcessoMap() {
		Map<Short, FaseProcesso> map = new HashMap<Short, FaseProcesso>();
		@SuppressWarnings("unchecked")
		List<FaseProcesso> fases = getSession().createQuery(
				"FROM FaseProcesso fp "
						+ "WHERE fp.cdFase = :cdFase")
				.setShort("cdFase", ConstantesPortaria.CD_FASE_PROCESSO_NO_TERMINAL)
				.list();
		for (FaseProcesso fase : fases) {
			map.put(fase.getCdFase(), fase);
		}
		return map;
	}

	/**
	 * Busca e prepara {@link Map} de {@link OcorrenciaColeta} com atributo
	 * {@code tpEventoColeta} de valor {@code 'CP'} (Chegada na Portaria).
	 * <p>
	 * São considerados na busca somente as {@link OcorrenciaColeta}s de
	 * atributo {@code tpSituacao} com valor {@code 'A'} (Ativo).
	 *
	 * @return
	 */
	public Map<String, OcorrenciaColeta> findOcorrenciaColetaMap() {
		Map<String, OcorrenciaColeta> map = new HashMap<String, OcorrenciaColeta>();
		@SuppressWarnings("unchecked")
		List<OcorrenciaColeta> list = getSession().createQuery(
				"FROM OcorrenciaColeta oc "
						+ "WHERE oc.tpEventoColeta = :tpEvento "
						+ "AND oc.tpSituacao = :tpSituacao")
				.setString("tpEvento", ConstantesPortaria.TP_EVENTO_CHEGADA_NA_PORTARIA)
				.setString("tpSituacao", ConstantesPortaria.TP_SITUACAO_ATIVO)
				.list();
		for (OcorrenciaColeta ocorrenciaColeta : list) {
			map.put(ocorrenciaColeta.getTpEventoColeta().getValue(), ocorrenciaColeta);
		}
		return map;
	}

	/**
	 * Busca Id's de {@link DoctoServico}s entregues relacionados aos
	 * {@link Manifesto}s, {@link ManifestoEntrega}s e
	 * {@link ManifestoEntregaDocumento}s de um {@link ControleCarga}.
	 * <p>
	 * São considerados entregues os {@link DoctoServico}s que tenham uma
	 * {@link OcorrenciaEntrega} relacionada com atributo {@code tpOcorrencia}
	 * de valor específico, conforme parâmetros do método.
	 * <p>
	 * Somente são considerados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} diferente de {@code 'CA'} (Cancelado) e
	 * atributo {@code tpManifestoEntrega} com os valores:
	 * <ul>
	 * <li>{@code 'ED'} (Entrega Direta),
	 * <li>{@code 'EN'} (Entrega), ou
	 * <li>{@code 'EP'} (Entrega Parceira).
	 * </ul>
	 * <p>
	 * Somente são consideradas as {@link OcorrenciaEntrega}s de atributo
	 * {@code tpOcorrencia} com os valores:
	 * <ul>
	 * <li>{@code 'E'} (Entregue), ou
	 * <li>{@code 'A'} (Entregue Aeroporto).
	 * </ul>
	 *
	 * @param controleCarga {@link ControleCarga} para busca das
	 *                      {@link OcorrenciaEntrega}s.
	 * @return Id's dos {@link DoctoServico} entregues.
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findDocumentosEntregues(ControleCarga controleCarga) {
		return getSession().createSQLQuery(
				"SELECT med.id_docto_servico "
						+ "FROM manifesto_entrega_documento med "
						+ "JOIN manifesto m ON m.id_manifesto = med.id_manifesto_entrega "
						+ "JOIN ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega "
						+ "WHERE med.id_docto_servico IN ( " + SQL_SUBSELECT_ID_DOCTO_SERVICO + " ) "
						+ "AND m.tp_status_manifesto <> :tpStatusCA "
						+ "AND m.tp_manifesto_entrega IN (:tpManifesto) "
						+ "AND oe.tp_ocorrencia IN (:tpOcorrencia)"
						+ "AND oe.cd_ocorrencia_entrega NOT IN (:cdOcorrencia)")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.setParameterList("tpManifesto", new String[]{
						ConstantesPortaria.TP_MANIFESTO_ENTREGA_DIRETA,
						ConstantesPortaria.TP_MANIFESTO_ENTREGA,
						ConstantesPortaria.TP_MANIFESTO_ENTREGA_PARCEIRA
				})
				.setParameterList("tpOcorrencia", new String[]{
						ConstantesPortaria.TP_OCORRENCIA_ENTREGUE,
						ConstantesPortaria.TP_OCORRENCIA_ENTREGUE_AEROPORTO
				})
				.setParameterList("cdOcorrencia", new Short[]{
                        ConstantesPortaria.CD_EVENTO_ENTREGA_PARCIAL
                })
				.setResultTransformer(new BigDecimalToLongResultTransformer())
				.list();
	}

	/**
	 * Busca Id's de {@link VolumeNotaFiscal}s entregues relacionados aos
	 * {@link Manifesto}s, {@link ManifestoEntrega}s e
	 * {@link ManifestoEntregaVolume}s de um {@link ControleCarga}.
	 * <p>
	 * São considerados entregues os {@link VolumeNotaFiscal}s relacionados ao
	 * {@link ManifestoEntregaVolume} e ao {@link ManifestoEntregaDocumento} que
	 * tenham uma {@link OcorrenciaEntrega} relacionada com atributo
	 * {@code tpOcorrencia} de valor específico, conforme parâmetro do método.
	 * <p>
	 * Somente são considerados os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} diferente de {@code 'CA'} (Cancelado) e
	 * atributo {@code tpManifestoEntrega} com os valores:
	 * <ul>
	 * <li>{@code 'ED'} (Entrega Direta),
	 * <li>{@code 'EN'} (Entrega), ou
	 * <li>{@code 'EP'} (Entrega Parceira).
	 * </ul>
	 * <p>
	 * Somente são consideradas as {@link OcorrenciaEntrega}s de atributo
	 * {@code tpOcorrencia} com os valores:
	 * <ul>
	 * <li>{@code 'E'} (Entregue), ou
	 * <li>{@code 'A'} (Entregue Aeroporto).
	 * </ul>
	 *
	 * @param controleCarga {@link ControleCarga} para busca das
	 *                      {@link OcorrenciaEntrega}s.
	 * @return Id's dos {@link VolumeNotaFiscal} entregues.
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findVolumesEntregues(ControleCarga controleCarga) {
		return getSession().createSQLQuery(
				"SELECT mev.id_volume_nota_fiscal "
						+ "FROM manifesto_entrega_volume mev "
						+ "JOIN manifesto m ON m.id_manifesto = mev.id_manifesto_entrega "
						+ "JOIN manifesto_entrega_documento med ON med.id_manifesto_entrega_documento = mev.id_manifesto_entrega_documento "
						+ "JOIN ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega "
						+ "WHERE mev.id_volume_nota_fiscal IN ( " + SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL + " ) "
						+ "AND m.tp_status_manifesto <> :tpStatusCA "
						+ "AND m.tp_manifesto_entrega IN (:tpManifesto) "
						+ "AND oe.tp_ocorrencia IN (:tpOcorrencia)")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.setParameterList("tpManifesto", new String[]{
						ConstantesPortaria.TP_MANIFESTO_ENTREGA_DIRETA,
						ConstantesPortaria.TP_MANIFESTO_ENTREGA,
						ConstantesPortaria.TP_MANIFESTO_ENTREGA_PARCEIRA
				})
				.setParameterList("tpOcorrencia", new String[]{
						ConstantesPortaria.TP_OCORRENCIA_ENTREGUE,
						ConstantesPortaria.TP_OCORRENCIA_ENTREGUE_AEROPORTO
				})
				.setResultTransformer(new BigDecimalToLongResultTransformer())
				.list();
	}

	/*
	 * Processamento de "Informar Chegada/Saída na Portaria"
	 */

	/**
	 * Verifica se {@link Filial} precede ou sucede porto, conforme
	 * {@link FilialRotaCc}s relacionados ao {@link ControleCarga} e
	 * configuração definida em {@link FluxoFilial}.
	 * <p>
	 * São considerados das instâncias de {@link FluxoFilial}:
	 * <ul>
	 * <li>parâmetro {@code dtVigencia} no intervalo entre
	 * {@code dtVigenciaInicial} e {@code dtVigenciaFinal},
	 * <li>atributo {@link Servico} com valor {@code null}, e
	 * <li>atributo {@code blPorto} com valor {@code true}.
	 * </ul>
	 *
	 * @param controleCarga {@link ControleCarga} para relacionar {@link FilialRotaCc}s.
	 * @param filial        {@link Filial} para verificação de {@code filialDestino}.
	 * @param dhVigencia    Data/hora para vigência.
	 * @return {@code true} se {@link Filial} preceder ou suceder porto.
	 */
	public boolean findFilialPorto(ControleCarga controleCarga, Filial filial, DateTime dhVigencia) {
		return !getSession().createSQLQuery(
				"SELECT 1 "
						+ "FROM filial_rota_cc frc "
						+ "JOIN fluxo_filial ff1 ON ff1.id_filial_origem = frc.id_filial "
						+ "JOIN fluxo_filial ff2 ON ff2.id_filial_destino = frc.id_filial "
						+ "WHERE frc.id_controle_carga = :idControleCarga "
						+ "AND ff1.id_filial_destino = :idFilial "
						+ "AND :dhVigencia BETWEEN ff1.dt_vigencia_inicial AND ff1.dt_vigencia_final "
						+ "AND ff1.id_servico IS NULL "
						+ "AND ff2.id_filial_origem = :idFilial "
						+ "AND :dhVigencia BETWEEN ff2.dt_vigencia_inicial AND ff2.dt_vigencia_final "
						+ "AND ff2.id_servico IS NULL "
						+ "AND (ff1.bl_porto = 'S' OR ff2.bl_porto = 'S')")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setLong(ID_FILIAL, filial.getIdFilial())
				.setTimestamp("dhVigencia", dhVigencia.toDate()).list().isEmpty();
	}

	public FilialRotaCc findFilialRota(ControleCarga controleCarga, Filial filial, Integer nrOrdem) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select frc ");
		hql.append(" from FilialRotaCc frc ");
		hql.append(" where frc.controleCarga.id = :idControleCarga ");
		if (filial != null) {
			hql.append(" and frc.filial.id = :idFilial ");
		}
		if (nrOrdem != null) {
			hql.append(" and frc.nrOrdem = :nrOrdem ");
		}

		Map paramMap = new HashMap();
		paramMap.put(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga());
		if (filial != null) {
			paramMap.put(ID_FILIAL, filial.getIdFilial());
		}
		if (nrOrdem != null) {
			paramMap.put("nrOrdem", Byte.valueOf(nrOrdem.toString()));
		}

		return (FilialRotaCc) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramMap);
	}

	public FluxoFilial findFluxoFilial(Filial filialOrigem, Filial filialDestino, DateTime dhVigencia) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select ff ");
		hql.append(" from FluxoFilial ff ");
		hql.append(" where ff.filialByIdFilialOrigem.id = :idFilialOrigem ");
		hql.append(" and ff.filialByIdFilialDestino.id = :idFilialDestino ");
		hql.append(" and (ff.servico is null OR ff.servico.idServico = (select cast(dsConteudo as long) FROM ParametroGeral where nmParametroGeral = 'ID_SERVICO_RODOVIARIO_NACIONAL'))");
		hql.append(" and :dhVigencia between ff.dtVigenciaInicial and ff.dtVigenciaFinal ");
		hql.append(" and ff.blPorto = 'S' ");

		Map paramMap = new HashMap();
		paramMap.put("idFilialOrigem", filialOrigem.getIdFilial());
		paramMap.put("idFilialDestino", filialDestino.getIdFilial());
		paramMap.put("dhVigencia", dhVigencia);

		return (FluxoFilial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramMap);
	}

	/*
	 * Queries para atualizações do processo "Informar Chegada na Portaria"
	 */

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link ControleTrecho}s
	 * relacionados a um {@link ControleCarga} e determinada {@link Filial} no
	 * atributo {@code filialDestino}.
	 * <p>
	 * Somente são atualizados os {@link ControleTrecho}s com atributo
	 * {@code dhSaida} de valor diferente de {@code null}.
	 * <p>
	 * O atributo {@code nrVersao} é incrementado para versionamento da entidade
	 * conforme mapeamento Objeto-Relacional.
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link ControleTrecho}.
	 * @param filial        {@link Filial} para verificação de {@code filialDestino}.
	 * @param dhChegada     Data/hora para atributo {@code dhChegada}.
	 * @return {@link Query} SQL para atualização dos {@link ControleTrecho}s.
	 */
	public Query queryUpdateControleTrecho(ControleCarga controleCarga, Filial filial, DateTime dhChegada) {
		return getSession().createSQLQuery(
				"UPDATE controle_trecho "
						+ "SET "
						+ "  dh_chegada = :dhChegada, "
						+ "  dh_chegada_tzr = :dhChegadaTzr, "
						+ "  nr_versao = nr_versao + 1 "
						+ "WHERE id_controle_carga = :idControleCarga "
						+ "AND id_filial_destino = :idFilial "
						+ "AND dh_saida IS NOT NULL")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setLong(ID_FILIAL, filial.getIdFilial())
				.setTimestamp(DH_CHEGADA, dhChegada.toDate())
				.setString(DH_CHEGADA_TZR, dhChegada.getZone().toString());
	}

	/**
	 * Retorna {@link Query} SQL para atualização do {@link ControleCarga}.
	 *
	 * @param controleCarga {@link ControleCarga} para atualização.
	 * @param filial        {@link Filial} para atributo {@code filialAtualizaStatus}.
	 * @param tpStatus      Valor para atributo {@code tpStatusControleCarga}.
	 * @param dhChegada     Data/hora para atributo {@code dhChegadaColetaEntrega}.
	 * @return {@link Query} SQL para atualização do {@link ControleCarga}.
	 */
	public Query queryUpdateControleCarga(
			ControleCarga controleCarga, Filial filial, String tpStatus, DateTime dhChegada) {
	    StringBuilder query = new StringBuilder();
	    query.append("UPDATE controle_carga ");
	    query.append("SET ");
	    query.append("  id_filial_atualiza_status = ");
	    query.append("    CASE ");
	    query.append("      WHEN tp_status_controle_carga = :tpStatus ");
	    query.append("         THEN id_filial_atualiza_status ");
	    query.append("      ELSE :idFilial ");
	    query.append("    END, ");
	    
	    if(dhChegada!= null){
	        query.append("  dh_chegada_coleta_entrega = ");
	        query.append("'" + JTFormatUtils.format(dhChegada, JTFormatUtils.MONTHYEAR, JTFormatUtils.LONG) + "', ");
	    }else{
	        query.append("  dh_chegada_coleta_entrega = null, ");
	    }
	    
        query.append("  dh_chegada_coleta_entrega_tzr = :dhChegadaTzr, ");
        query.append("  tp_status_controle_carga = :tpStatus ");
        query.append("WHERE id_controle_carga = :idControleCarga");
	    
		return getSession().createSQLQuery(query.toString())
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setLong(ID_FILIAL, filial.getIdFilial())
				.setString("tpStatus", tpStatus)
				.setString(DH_CHEGADA_TZR, dhChegada != null ? dhChegada.getZone().toString() : null);
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link Manifesto}s de um
	 * {@link ControleCarga} de uma operação de Viagem, setanto o atributo
	 * {@code tpStatusManifesto} conforme a regra:
	 * <ul>
	 * <li>{@code 'AD'} (Aguardando Descarga) se o parâmetros {@link Filial}
	 * corresponder ao atributo {@code filialDestino},
	 * <li>{@code 'EF'} (Em Escala na Filial) caso contrário.
	 * </ul>
	 * <p>
	 * São atualizados somente os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'EV'} (Em Viagem).
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link Manifesto}s.
	 * @param filial        {@link Filial} para resolução do atributo
	 *                      {@code tpStatusManifesto}.
	 * @return {@link Query} SQL para atualização dos {@link Manifesto}s de uma
	 * operação Viagem.
	 */
	public Query queryUpdateManifestoViagem(ControleCarga controleCarga, Filial filial) {
		return getSession().createSQLQuery(
				"UPDATE manifesto "
						+ "SET tp_status_manifesto = "
						+ "  CASE "
						+ "    WHEN id_filial_destino = :idFilial "
						+ "    THEN :tpStatusAD "
						+ "    ELSE :tpStatusEF "
						+ "  END "
						+ "WHERE id_controle_carga = :idControleCarga "
						+ "AND tp_status_manifesto = :tpStatusEV")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setLong(ID_FILIAL, filial.getIdFilial())
				.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA)
				.setString("tpStatusEF", ConstantesPortaria.TP_STATUS_EM_ESCALA_NA_FILIAL)
				.setString("tpStatusEV", ConstantesPortaria.TP_STATUS_EM_VIAGEM);
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link Manifesto}s de um
	 * {@link ControleCarga} de uma operação de Coleta/Entrega, setanto o
	 * atributo {@code tpStatusManifesto} com o valor {@code 'AD'} (Aguardando
	 * Descarga).
	 * <p>
	 * São atualizados somente os {@link Manifesto}s com atributo
	 * {@code tpStatusManifesto} de valor {@code 'TC'} (Em Trânsito de Coleta).
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link Manifesto}s.
	 * @return {@link Query} SQL para atualização dos {@link Manifesto}s de uma
	 * operação Coleta/Entrega.
	 */
	public Query queryUpdateManifestoColetaEntrega(ControleCarga controleCarga) {
		return getSession().createSQLQuery(
				"UPDATE manifesto "
						+ "SET tp_status_manifesto = :tpStatusAD "
						+ "WHERE id_controle_carga = :idControleCarga "
						+ "AND tp_status_manifesto = :tpStatusTC")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA)
				.setString(TP_STATUS_TC, ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA);
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link ManifestoColeta}s
	 * de um {@link ControleCarga}, setando o atributo
	 * {@code tpStatusManifestoColeta} com o valor {@code 'AD'} (Aguardando
	 * Descarga).
	 * <p>
	 * São atualizados os {@link ManifestoColeta}s do {@link ControleCarga} que
	 * tiverem {@link EventoManifestoColeta} registrado em determinada
	 * data/hora.
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link ManifestoColeta}s.
	 * @return {@link Query} SQL para atualização dos {@link ManifestoColeta}s.
	 */
	public Query queryUpdateManifestoColeta(boolean parametroFilialLMSA1675, List<Long> idsManifestoColeta, ControleCarga controleCarga, DateTime dhEvento) {

		if (parametroFilialLMSA1675) {
			final StringBuilder sbIdsManifestoColetas = new StringBuilder();
			for (Long idManifestoColeta : idsManifestoColeta) {
				sbIdsManifestoColetas.append(idManifestoColeta);
				sbIdsManifestoColetas.append(",");
			}
			if (sbIdsManifestoColetas.length() > 0) {
				sbIdsManifestoColetas.setLength(sbIdsManifestoColetas.length() - 1);
			}

			return getSession().createSQLQuery(
					"UPDATE manifesto_coleta "
							+ "SET tp_status_manifesto_coleta = :tpStatusAD "
							+ "WHERE id_manifesto_coleta IN ( " + sbIdsManifestoColetas + " ) ")
					.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA);
		} else {
			return getSession().createSQLQuery(
					"UPDATE manifesto_coleta "
							+ "SET tp_status_manifesto_coleta = :tpStatusAD "
							+ "WHERE id_manifesto_coleta IN ( "
							+ "  SELECT mc.id_manifesto_coleta "
							+ "  FROM manifesto_coleta mc "
							+ "  JOIN evento_manifesto_coleta emc ON emc.id_manifesto_coleta = mc.id_manifesto_coleta "
							+ "  WHERE mc.id_controle_carga = :idControleCarga "
							+ "  AND emc.dh_evento = :dhEvento "
							+ ")")
					.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
					.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA)
					.setTimestamp(DH_EVENTO, dhEvento.toDate());
		}
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link PedidoColeta}s de
	 * um {@link ControleCarga}, setando o atributo {@code tpStatusColeta} com o
	 * valor {@code 'AD'} (Aguardando Descarga).
	 * <p>
	 * São atualizados os {@link PedidoColeta}s do {@link ControleCarga} que
	 * tiverem {@link EventoColeta} registrado em determinada data/hora.
	 * <p>
	 * O atributo {@code nrVersao} é incrementado para versionamento da entidade
	 * conforme mapeamento Objeto-Relacional.
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link ManifestoColeta}s.
	 * @return {@link Query} SQL para atualização dos {@link PedidoColeta}s.
	 */
	public Query queryUpdatePedidoColeta(boolean parametroFilialLMSA1675, List<Long> idsPedidoColeta, ControleCarga controleCarga, DateTime dhEvento) {

		if (parametroFilialLMSA1675) {
			final StringBuilder sbIdsPedidoColetas = new StringBuilder();
			for (Long idPedidoColeta : idsPedidoColeta) {
				sbIdsPedidoColetas.append(idPedidoColeta);
				sbIdsPedidoColetas.append(",");
			}
			if (sbIdsPedidoColetas.length() > 0) {
				sbIdsPedidoColetas.setLength(sbIdsPedidoColetas.length() - 1);
			}

			return getSession().createSQLQuery(
					"UPDATE pedido_coleta "
							+ "SET "
							+ "  tp_status_coleta = :tpStatusAD, "
							+ "  nr_versao = nr_versao + 1 "
							+ "WHERE id_pedido_coleta IN ( " + sbIdsPedidoColetas + " ) ")
					.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA);
		} else {
			return getSession().createSQLQuery(
					"UPDATE pedido_coleta "
							+ "SET "
							+ "  tp_status_coleta = :tpStatusAD, "
							+ "  nr_versao = nr_versao + 1 "
							+ "WHERE id_pedido_coleta IN ( "
							+ "  SELECT pc.id_pedido_coleta "
							+ "  FROM manifesto_coleta mc "
							+ "  JOIN pedido_coleta pc ON pc.id_manifesto_coleta = mc.id_manifesto_coleta "
							+ "  JOIN evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta "
							+ "  WHERE mc.id_controle_carga = :idControleCarga "
							+ "  AND mc.tp_status_manifesto_coleta <> :tpStatusCA "
							+ "  AND ec.dh_evento = :dhEvento "
							+ ")")
					.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
					.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA)
					.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
					.setTimestamp(DH_EVENTO, dhEvento.toDate());
		}
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link DoctoServico}s de
	 * um {@link ControleCarga}, setando os atributos
	 * {@code localizacaoMercadoria}, {@code filialLocalizacao} e
	 * {@code obComplementoLocalizacao}.
	 * <p>
	 * São atualizados os {@link DoctoServico}s do {@link ControleCarga} que
	 * tiverem um {@link EventoDocumentoServico} relacionado a determinado
	 * {@link Evento} e registrado em determinada data/hora.
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos {@link DoctoServico}s.
	 * @param evento        Para verificação dos {@link EventoDocumentoServico}s e
	 *                      atribuição da {@code localizacaoMercadoria}.
	 * @param filial        Para atribuição da {@code filialLocalizacao}.
	 * @param obComplemento Para atribuição do {@code obComplementoLocalizacao}.
	 * @return {@link Query} SQL para atualização dos {@link DoctoServico}s.
	 */
	public Query queryUpdateDoctoServico(boolean parametroFilialLMSA1675,
										 ControleCarga controleCarga, Evento evento, Filial filial, String obComplemento, List<Long> idsDoctoServico, DateTime dhEvento) {

		if (parametroFilialLMSA1675) {
			final StringBuilder sbIdsDoctoServicos = new StringBuilder();
			for (Long idDoctoServico : idsDoctoServico) {
				sbIdsDoctoServicos.append(idDoctoServico);
				sbIdsDoctoServicos.append(",");
			}
			if (sbIdsDoctoServicos.length() > 0) {
				sbIdsDoctoServicos.setLength(sbIdsDoctoServicos.length() - 1);
			}

			return getSession().createSQLQuery(
					"UPDATE docto_servico "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_filial_localizacao = :idFilial, "
							+ "  ob_complemento_localizacao = :obComplemento "
							+ "WHERE id_docto_servico IN ( "
							+ "  SELECT eds.id_docto_servico "
							+ "  FROM evento_documento_servico eds "
							+ "  WHERE eds.id_docto_servico IN ( " + sbIdsDoctoServicos + " ) "
							+ "  AND eds.id_evento = :idEvento "
							+ ")")
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial())
					.setString(OB_COMPLEMENTO, obComplemento)
					.setLong(ID_EVENTO, evento.getIdEvento());
		} else {
			return getSession().createSQLQuery(
					"UPDATE docto_servico "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_filial_localizacao = :idFilial, "
							+ "  ob_complemento_localizacao = :obComplemento "
							+ "WHERE id_docto_servico IN ( "
							+ "  SELECT eds.id_docto_servico "
							+ "  FROM evento_documento_servico eds "
							+ "  WHERE eds.id_docto_servico IN ( " + SQL_SUBSELECT_ID_DOCTO_SERVICO + " ) "
							+ "  AND eds.dh_evento = :dhEvento "
							+ "  AND eds.id_evento = :idEvento "
							+ ")")
					.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
					.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial())
					.setString(OB_COMPLEMENTO, obComplemento)
					.setTimestamp(DH_EVENTO, dhEvento.toDate())
					.setLong(ID_EVENTO, evento.getIdEvento());
		}
	}

	/**
	 * Retorna {@link Query} SQL para atualização das
	 * {@link OcorrenciaDoctoServico}s relacionadas aos {@link DoctoServico}s de
	 * um {@link ControleCarga}, setando a {@link FaseProcesso} conforme
	 * parâmetro.
	 * <p>
	 * São atualizadas as {@link OcorrenciaDoctoServico}s sem
	 * {@link OcorrenciaPendencia} de liberação (atributos
	 * {@code ocorrenciaPendenciaByIdOcorLiberacao} e {@code dhLiberacao} com
	 * valor {@code null}).
	 *
	 * @param controleCarga Para busca dos {@link DoctoServico}s e
	 *                      {@link OcorrenciaDoctoServico}s relacionadas.
	 * @param faseProcesso  Para atribuição da {@link FaseProcesso}.
	 * @return {@link Query} SQL para atualização das
	 * {@link OcorrenciaDoctoServico}s.
	 */
	public Query queryUpdateOcorrenciaDoctoServico(ControleCarga controleCarga, FaseProcesso faseProcesso) {
		return getSession().createSQLQuery(
				"UPDATE ocorrencia_docto_servico "
						+ "SET id_fase_processo = :idFaseProcesso "
						+ "WHERE id_docto_servico IN ( " + SQL_SUBSELECT_ID_DOCTO_SERVICO + " ) "
						+ "AND id_ocor_liberacao IS NULL "
						+ "AND dh_liberacao IS NULL")
				.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
				.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
				.setLong("idFaseProcesso", faseProcesso.getIdFaseProcesso());
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos {@link VolumeNotaFiscal}s
	 * relacionados de um {@link ControleCarga}, setando os atributos
	 * {@code localizacaoMercadoria} e {@code localizacaoFilial}.
	 * <p>
	 * São atualizados os {@link VolumeNotaFiscal}s do {@link ControleCarga} que
	 * tiverem um {@link EventoVolume} relacionado a determinado {@link Evento}
	 * e registrado em determinada data/hora.
	 *
	 * @param controleCarga {@link ControleCarga} para busca dos
	 *                      {@link VolumeNotaFiscal}s.
	 * @param evento        Para verificação dos {@link EventoVolume}s e atribuição da
	 *                      {@code localizacaoMercadoria}.
	 * @param filial        Para atribuição da {@code localizacaoFilial}.
	 * @return {@link Query} SQL para atualização dos {@link VolumeNotaFiscal}s.
	 */
	public Query queryUpdateVolumeNotaFiscal(boolean parametroFilialLMSA1675,
											 ControleCarga controleCarga, Evento evento, Filial filial, List<Long> idsVolumeNotaFiscal, DateTime dhEvento) {

		if (parametroFilialLMSA1675) {
			final StringBuilder sbIdsVolumeNotaFiscal = new StringBuilder();
			for (Long idVolumeNotaFiscal : idsVolumeNotaFiscal) {
				sbIdsVolumeNotaFiscal.append(idVolumeNotaFiscal);
				sbIdsVolumeNotaFiscal.append(",");
			}
			if (sbIdsVolumeNotaFiscal.length() > 0) {
				sbIdsVolumeNotaFiscal.setLength(sbIdsVolumeNotaFiscal.length() - 1);
			}

			return getSession().createSQLQuery(
					"UPDATE volume_nota_fiscal "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_localizacao_filial = :idFilial "
							+ "WHERE id_volume_nota_fiscal IN ( "
							+ "  SELECT ev.id_volume_nota_fiscal "
							+ "  FROM evento_volume ev "
							+ "  WHERE ev.id_volume_nota_fiscal IN ( " + sbIdsVolumeNotaFiscal + " ) "
							+ "  AND ev.id_evento = :idEvento "
							+ ")")
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial())
					.setLong(ID_EVENTO, evento.getIdEvento());
		} else {
			return getSession().createSQLQuery(
					"UPDATE volume_nota_fiscal "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_localizacao_filial = :idFilial "
							+ "WHERE id_volume_nota_fiscal IN ( "
							+ "  SELECT ev.id_volume_nota_fiscal "
							+ "  FROM evento_volume ev "
							+ "  WHERE ev.id_volume_nota_fiscal IN ( " + SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL + " ) "
							+ "  AND ev.dh_evento = :dhEvento "
							+ "  AND ev.id_evento = :idEvento "
							+ ")")
					.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
					.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial())
					.setTimestamp(DH_EVENTO, dhEvento.toDate())
					.setLong(ID_EVENTO, evento.getIdEvento());
		}
	}

	public Query queryUpdateVolumeNotaFiscal(Manifesto manifesto, Evento evento, Filial filial) {
		return getSession().createSQLQuery(
				"UPDATE volume_nota_fiscal "
						+ "SET "
						+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
						+ "  id_localizacao_filial = :idFilial "
						+ "WHERE id_volume_nota_fiscal IN ( " + SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL_BY_MANIFESTO + " ) ")
				.setLong("idManifesto", manifesto.getIdManifesto())
				.setString(TP_STATUS_AD, ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA)
				.setString("tpStatusEF", ConstantesPortaria.TP_STATUS_EM_ESCALA_NA_FILIAL)
				.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
				.setLong(ID_FILIAL, filial.getIdFilial());
	}

	/**
	 * Retorna {@link Query} SQL para atualização dos
	 * {@link DispositivoUnitizacao}s relacionados aos {@link VolumeNotaFiscal}s
	 * de um {@link ControleCarga}, setando os atributos
	 * {@code localizacaoMercadoria} e {@code localizacaoFilial}.
	 * <p>
	 * São atualizados os {@link DispositivoUnitizacao}s que tiverem um
	 * {@link EventoDispositivoUnitizacao} relacionado a determinado
	 * {@link Evento} e registrado em determinada data/hora.
	 *
	 * @param evento Para verificação dos {@link EventoDispositivoUnitizacao}s e
	 *               atribuição da {@code localizacaoMercadoria}.
	 * @param filial Para atribuição da {@code localizacaoFilial}.
	 * @return {@link Query} SQL para atualização dos
	 * {@link DispositivoUnitizacao}s.
	 */
	public Query queryUpdateDispositivoUnitizacao(boolean parametroFilialLMSA1675,
												  ControleCarga controleCarga, Evento evento, Filial filial, List<Long> idsDispositivoUnitizacao, DateTime dhEvento) {

		if (parametroFilialLMSA1675) {
			final StringBuilder sbIdsDispositivoUnitizacao = new StringBuilder();
			for (Long idDispositivoUnitizacao : idsDispositivoUnitizacao) {
				sbIdsDispositivoUnitizacao.append(idDispositivoUnitizacao);
				sbIdsDispositivoUnitizacao.append(",");
			}
			if (sbIdsDispositivoUnitizacao.length() > 0) {
				sbIdsDispositivoUnitizacao.setLength(sbIdsDispositivoUnitizacao.length() - 1);
			}

			return getSession().createSQLQuery(
					"UPDATE dispositivo_unitizacao "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_localizacao_filial = :idFilial "
							+ "WHERE id_dispositivo_unitizacao IN ( " + sbIdsDispositivoUnitizacao + " ) ")
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial());
		} else {
			return getSession().createSQLQuery(
					"UPDATE dispositivo_unitizacao "
							+ "SET "
							+ "  id_localizacao_mercadoria = :idLocalizacaoMercadoria, "
							+ "  id_localizacao_filial = :idFilial "
							+ "WHERE id_dispositivo_unitizacao IN ( "
							+ "  SELECT edu.id_dispositivo_unitizacao "
							+ "  FROM evento_dispositivo_unitizacao edu "
							+ "  JOIN volume_nota_fiscal vnf ON vnf.id_dispositivo_unitizacao = edu.id_dispositivo_unitizacao "
							+ "  WHERE vnf.id_volume_nota_fiscal IN ( " + SQL_SUBSELECT_ID_VOLUME_NOTA_FISCAL + " ) "
							+ "  AND edu.dh_evento = :dhEvento "
							+ "  AND edu.id_evento = :idEvento "
							+ ")")
					.setLong(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga())
					.setString(TP_STATUS_CA, ConstantesPortaria.TP_STATUS_CANCELADO)
					.setLong(ID_LOCALIZACAO_MERCADORIA, evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria())
					.setLong(ID_FILIAL, filial.getIdFilial())
					.setTimestamp(DH_EVENTO, dhEvento.toDate())
					.setLong(ID_EVENTO, evento.getIdEvento());
		}

	}

	/**
	 * Retorna {@link Query} SQL para atualização do
	 * {@link EventoMeioTransporte} imediatamente anterior a determinado
	 * {@link EventoMeioTransporte}, setando o atributo {@code dhFimEvento}.
	 * <p>
	 * A atualização só ocorre se o atributo {@code dhFimEvento} estiver com
	 * valor {@code null}.
	 * <p>
	 * Eventualmente mais de um {@link EventoMeioTransporte} poderão ser
	 * atualizados caso existam com mesmo valor no atributo
	 * {@code dhInicioEvento}.
	 *
	 * @param evento Para busca do {@link EventoMeioTransporte} imediatamente
	 *               anterior.
	 * @return {@link Query} SQL para atualização do
	 * {@link EventoMeioTransporte}.
	 */
	public Query queryUpdateUltimoEventoMeioTransporte(boolean parametroFilialLMSA1675, EventoMeioTransporte evento) {
		if (parametroFilialLMSA1675) {
			return getSession().createSQLQuery(
					"UPDATE evento_meio_transporte "
							+ "SET "
							+ "  dh_evento_final = :dhEvento, "
							+ "  dh_evento_final_tzr = :dhEventoTzr "
							+ "WHERE dh_evento_inicial = ( "
							+ "  SELECT MAX(emt.dh_evento_inicial) "
							+ "  FROM evento_meio_transporte emt "
							+ "  WHERE emt.id_meio_transporte = :idMeioTransporte "
							+ "  AND emt.id_evento_meio_transporte < :idEventoMeioTransporte "
							+ ") "
							+ "AND dh_evento_final IS NULL")
					.setLong("idMeioTransporte", evento.getMeioTransporte().getIdMeioTransporte())
					.setTimestamp(DH_EVENTO, evento.getDhInicioEvento().toDate())
					.setLong("idEventoMeioTransporte", evento.getIdEventoMeioTransporte())
					.setString("dhEventoTzr", evento.getDhInicioEvento().getZone().toString());
		} else {
			return getSession().createSQLQuery(
					"UPDATE evento_meio_transporte "
							+ "SET "
							+ "  dh_evento_final = :dhEvento, "
							+ "  dh_evento_final_tzr = :dhEventoTzr "
							+ "WHERE dh_evento_inicial = ( "
							+ "  SELECT MAX(emt.dh_evento_inicial) "
							+ "  FROM evento_meio_transporte emt "
							+ "  WHERE emt.id_meio_transporte = :idMeioTransporte "
							+ "  AND emt.dh_evento_inicial < :dhEvento "
							+ ") "
							+ "AND dh_evento_final IS NULL")
					.setLong("idMeioTransporte", evento.getMeioTransporte().getIdMeioTransporte())
					.setTimestamp(DH_EVENTO, evento.getDhInicioEvento().toDate())
					.setString("dhEventoTzr", evento.getDhInicioEvento().getZone().toString());
		}
	}

	/**
	 * Retorna {@link Query} SQL para atualização de determinada
	 * {@link OrdemSaida}, setando o atributo {@code dhChegada} conforme
	 * parâmetro.
	 *
	 * @param ordemSaida {@link OrdemSaida} para atualização.
	 * @param dhChegada  Data/hora para atribuição.
	 * @return {@link Query} SQL para atualização da {@link OrdemSaida}.
	 */
	public Query queryUpdateOrdemSaida(OrdemSaida ordemSaida, DateTime dhChegada) {
		return getSession().createSQLQuery(
				"UPDATE ordem_saida "
						+ "SET "
						+ "  dh_chegada = :dhChegada, "
						+ "  dh_chegada_tzr = :dhChegadaTzr "
						+ "WHERE id_ordem_saida = :idOrdemSaida")
				.setLong("idOrdemSaida", ordemSaida.getIdOrdemSaida())
				.setTimestamp(DH_CHEGADA, dhChegada.toDate())
				.setString(DH_CHEGADA_TZR, dhChegada.getZone().toString());
	}

	/**
	 * Classe auxiliar para conversão de {@link BigDecimal} em {@link Long}.
	 *
	 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
	 */
	private static class BigDecimalToLongResultTransformer extends PassThroughResultTransformer {

		private static final long serialVersionUID = 1L;

		@Override
		public Object transformTuple(Object[] tuple, String[] aliases) {
			Object value = super.transformTuple(tuple, aliases);
			return value instanceof BigDecimal ? ((BigDecimal) value).longValue() : value;
		}

	}

	public void processarEntidadesASeremInseridasSaidaPortaria(List<Serializable> entidadesASeremInseridas, List<Serializable> entidades) {
		StringBuilder eventoDocumentoServicoSql = new StringBuilder();

		Map<Long, StringBuilder> doctoServicoSqls = new HashMap<Long, StringBuilder>();
		Map<Long, List<String>> doctoServicoIds = new HashMap<Long, List<String>>();

		int eventoDocumentoServicoCount = 0;

		for (int i = 0; i < entidades.size(); i++) {
			if (entidades.get(i) instanceof EventoDocumentoServico) {
				entidadesASeremInseridas.remove(entidades.get(i));
				EventoDocumentoServico eventoDocumentoServico = (EventoDocumentoServico) entidades.get(i);
				if (eventoDocumentoServicoCount == 0) {
					eventoDocumentoServicoSql.append(
							"INSERT " +
									"INTO EVENTO_DOCUMENTO_SERVICO " +
									"(" + COLS_EVENTO_DOCUMENTO_SERVICO + ", ID_EVENTO_DOCUMENTO_SERVICO) " +
									"SELECT " +
									COLS_EVENTO_DOCUMENTO_SERVICO +
									",EVENTO_DOCUMENTO_SERVICO_SQ.nextval AS ID_EVENTO_DOCUMENTO_SERVICO FROM (");
				}

				adicionarSubqueryInsertEventoDocumentoServico(eventoDocumentoServicoSql, eventoDocumentoServico);

				eventoDocumentoServicoCount++;
				if (eventoDocumentoServicoCount > 30) {
					executarSubqueryParaAdicaoNoBanco(eventoDocumentoServicoSql, false);
					eventoDocumentoServicoSql = new StringBuilder();
					eventoDocumentoServicoCount = 0;
				} else {
					eventoDocumentoServicoSql.append("UNION ALL ");
				}
			}
			if (entidades.get(i) instanceof DoctoServico) {
				manterUpdateDeDoctoServico(doctoServicoSqls, doctoServicoIds, entidadesASeremInseridas, entidades, i);
			}
		}

		if (eventoDocumentoServicoSql.length() > 0) {
			executarSubqueryParaAdicaoNoBanco(eventoDocumentoServicoSql, true);
		}

		addDoctosServicos(doctoServicoSqls, doctoServicoIds);

		//Dois flushs são dados. Um para inserir as informações nativas e outro para inserir as gerenciadas pelo hibernate.
		this.getAdsmHibernateTemplate().flush();
		this.getAdsmHibernateTemplate().clear();
	}

	private void addDoctosServicos(Map<Long, StringBuilder> doctoServicoSqls, Map<Long, List<String>> doctoServicoIds) {
		for (Map.Entry<Long, StringBuilder> longStringBuilderEntry : doctoServicoSqls.entrySet()) {
			StringBuilder currentStringBuild = new StringBuilder(longStringBuilderEntry.getValue().toString());
			ArrayList<String> listToAdd = new ArrayList<String>();
			for (int i = 0; i < doctoServicoIds.get(longStringBuilderEntry.getKey()).toArray().length; i++) {
				listToAdd.add(doctoServicoIds.get(longStringBuilderEntry.getKey()).get(i));
				if (listToAdd.size() % REGISTER_LIMIT == 0) {
					currentStringBuild.append("(" + StringUtils.join(listToAdd.toArray(), ",") + ")");
					executarSubqueryParaUpdateNoBanco(currentStringBuild);
					listToAdd = new ArrayList<String>();
					currentStringBuild = new StringBuilder(longStringBuilderEntry.getValue().toString());
				}
			}
			if (!listToAdd.isEmpty()) {
				currentStringBuild.append("(" + StringUtils.join(listToAdd.toArray(), ",") + ")");
				executarSubqueryParaUpdateNoBanco(currentStringBuild);
			}
		}
	}

	/**
	 * A função adicionará um insert preparado para ser feito em lote no string builder para, posteriormente, ser inserido no sistema.
	 *
	 * @param eventoDocumentoServicoSql
	 * @param eventoDocumentoServico
	 */
	private void adicionarSubqueryInsertEventoDocumentoServico(StringBuilder eventoDocumentoServicoSql, EventoDocumentoServico eventoDocumentoServico) {
		eventoDocumentoServicoSql.append("SELECT ");

		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getDhEvento().toDateTime(), "DH_EVENTO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, JTDateTimeUtils.getUserDtz().getID(), "DH_EVENTO_TZR");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getDhInclusao(), "DH_INCLUSAO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, JTDateTimeUtils.getUserDtz().getID(), "DH_INCLUSAO_TZR");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getBlEventoCancelado(), "BL_EVENTO_CANCELADO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getNrDocumento(), "NR_DOCUMENTO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getObComplemento(), "OB_COMPLEMENTO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getDhGeracaoParceiro(), "DH_GERACAO_PARCEIRO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, JTDateTimeUtils.getUserDtz().getID(), "DH_GERACAO_PARCEIRO_TZR");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getDhComunicacao(), "DH_COMUNICACAO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, JTDateTimeUtils.getUserDtz().getID(), "DH_COMUNICACAO_TZR");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getTpDocumento().getValue(), "TP_DOCUMENTO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getUsuario().getIdUsuario(), "ID_USUARIO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getDoctoServico().getIdDoctoServico(), "ID_DOCTO_SERVICO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getEvento().getIdEvento(), "ID_EVENTO");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, eventoDocumentoServico.getFilial().getIdFilial(), "ID_FILIAL");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, (eventoDocumentoServico.getPedidoCompra() != null) ? eventoDocumentoServico.getPedidoCompra().getIdPedidoCompra() : null, "ID_PEDIDO_COMPRA");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, (eventoDocumentoServico.getOcorrenciaEntrega() != null) ? eventoDocumentoServico.getOcorrenciaEntrega().getIdOcorrenciaEntrega() : null, "ID_OCORRENCIA_ENTREGA");
		StringBuilderSQLUtils.adicionarValorSelect(eventoDocumentoServicoSql, (eventoDocumentoServico.getOcorrenciaPendencia() != null) ? eventoDocumentoServico.getOcorrenciaPendencia().getIdOcorrenciaPendencia() : null, "ID_OCORRENCIA_PENDENCIA", true);

		eventoDocumentoServicoSql.append("FROM dual ");
	}

	private void executarSubqueryParaUpdateNoBanco(StringBuilder stringBuilderSql) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql = stringBuilderSql.toString();
		session.createSQLQuery(sql).executeUpdate();
	}

	private void executarSubqueryParaAdicaoNoBanco(StringBuilder stringBuilderSql, Boolean removerUnionAll) {

		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();

		String sql = stringBuilderSql.toString();
		if (removerUnionAll) {
			sql = sql.substring(0, sql.length() - 10);
		}
		sql += ")";
		session.createSQLQuery(sql).executeUpdate();

	}

	private void manterUpdateDeDoctoServico(Map<Long, StringBuilder> doctoServicoSqls, Map<Long, List<String>> doctoServicoIds, List<Serializable> entidadesASeremInseridas, List<Serializable> entidades, int i) {
		entidadesASeremInseridas.remove(entidades.get(i));

		DoctoServico doctoServico = (DoctoServico) entidades.get(i);

		if (doctoServicoSqls.get(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()) == null) {
			StringBuilder docServicoLocalizacaoSql = new StringBuilder();
			docServicoLocalizacaoSql.append("UPDATE DOCTO_SERVICO SET ");
			docServicoLocalizacaoSql.append("ID_LOCALIZACAO_MERCADORIA = " + doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() + ",");
			docServicoLocalizacaoSql.append("OB_COMPLEMENTO_LOCALIZACAO = " + ((doctoServico.getObComplementoLocalizacao() != null) ? "'" + doctoServico.getObComplementoLocalizacao() + "'" : "NULL") + ",");
			docServicoLocalizacaoSql.append("NR_DIAS_REAL_ENTREGA = " + ((doctoServico.getNrDiasRealEntrega() != null) ? doctoServico.getNrDiasRealEntrega() : "NULL") + ",");
			docServicoLocalizacaoSql.append("ID_FILIAL_LOCALIZACAO = " + ((doctoServico.getFilialLocalizacao().getIdFilial() != null) ? doctoServico.getFilialLocalizacao().getIdFilial() : "NULL") + " ");
			docServicoLocalizacaoSql.append("WHERE ID_DOCTO_SERVICO IN ");
			doctoServicoSqls.put(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria(), docServicoLocalizacaoSql);
			doctoServicoIds.put(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria(), new ArrayList<String>());
		}
		doctoServicoIds.get(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()).add(doctoServico.getIdDoctoServico().toString());
	}

}
