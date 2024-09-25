package com.mercurio.lms.indenizacoes.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioIndenizacoesFranqueadoQuery;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoFranqueado;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboIndenizacaoFranqueadoDAO extends BaseCrudDao<ReciboIndenizacaoFranqueado, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ReciboIndenizacaoFranqueado.class;
	}
	
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		HashMap lazyFindPaginated = new HashMap();
		lazyFindPaginated.put("franquia", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	public Long findPendenciaByReciboIndenizacao(ReciboIndenizacao reciboIndenizacao) {
		StringBuffer s = new StringBuffer()
		.append("select rim.pendencia.idPendencia ")
		.append("from "+ReciboIndenizacao.class.getName()+" rim ")
		.append("where rim.idReciboIndenizacao = ?");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(s.toString(), new Object[]{reciboIndenizacao.getIdReciboIndenizacao()});
	}
	
	public List<Object[]> findIndenizacoesFranqueadas(TypedFlatMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("aprovada", ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO);
		params.put("emAprovacao", ConstantesFranqueado.TP_SITUACAO_PENDENCIA_EM_APROVACAO);
		params.put("tpStatusIndenizacao", ConstantesFranqueado.TP_STATUS_INDENIZACAO_LIQUIDADO);
		params.put("idFranquia", map.getLong("idFilial"));
		params.put("dtPagamentoInicial", map.get("dtPagamentoInicial"));
		params.put("dtPagamentoFinal", map.get("dtPagamentoFinal"));
		
		String sql = 	"SELECT 	"
						+"			  RI.ID_RECIBO_INDENIZACAO, "
						+"            F.SG_FILIAL, "
						+"            RI.NR_RECIBO_INDENIZACAO, "
						+"            FFRQ.SG_FILIAL AS SG_FRANQUIA, "
						+"            MIN(CONCAT(CONCAT(FDS.SG_FILIAL,' '),DS.NR_DOCTO_SERVICO)) AS NR_DOCUMENTO, "
						+"            DECODE(MANC.TP_MOTIVO, 'AV','AV','FV','FA','OU') AS TP_MOTIVO_INDENIZACAO, "
						+"            COUNT(DS.ID_DOCTO_SERVICO) AS NR_DOCUMENTOS, "
						+"            MIN(ROUND(RI.VL_INDENIZACAO * (FD.PC_DEBITADO / 100),2)) AS VL_TOTAL_INDENIZACAO, "
						+"			  MAX(FRQ.ID_FRANQUIA), "
						+ " 		  MIN(( "
						+ "				SELECT RIF.TP_SITUACAO_PENDENCIA "
						+ " 			FROM RECIBO_INDENIZACAO_FRQ RIF "
						+ "				WHERE RIF.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO "
						+ "					AND RIF.ID_FRANQUIA = FRQ.ID_FRANQUIA "
						+ "					AND RIF.ID_RECIBO_INDENIZACAO_FRQ = ("
						+ "						SELECT MAX(R.ID_RECIBO_INDENIZACAO_FRQ) "
						+ "						FROM   RECIBO_INDENIZACAO_FRQ R "
						+ "						WHERE  R.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO"
						+ " 				) "
						+ " 		  )) AS TP_SITUACAO_PENDENCIA "
						+"FROM 		RECIBO_INDENIZACAO RI,  "
						+"            DOCTO_SERVICO_INDENIZACAO DSI, "
						+"            DOCTO_SERVICO DS, "
						+"            FILIAL FDS, "
						+"            FILIAL F, "
						+"            FRANQUIA FRQ, "
						+"            FILIAL FFRQ, "
						+"            FILIAL_DEBITADA FD, "
						+"            OCORRENCIA_NAO_CONFORMIDADE ONC,  "
						+"            MOTIVO_ABERTURA_NC MANC "
						+"WHERE       RI.ID_FILIAL = F.ID_FILIAL "
						+"AND         RI.ID_RECIBO_INDENIZACAO = DSI.ID_RECIBO_INDENIZACAO "
						+"AND         DSI.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
						+"AND 		  DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL "
						+"AND         FRQ.ID_FRANQUIA = FFRQ.ID_FILIAL "
						+"AND         RI.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO "
						+"AND         FD.ID_FILIAL = FRQ.ID_FRANQUIA "
						+"AND         ONC.ID_OCORRENCIA_NAO_CONFORMIDADE = DSI.ID_OCORRENCIA_NAO_CONFORMIDADE "
						+"AND         ONC.ID_MOTIVO_ABERTURA_NC = MANC.ID_MOTIVO_ABERTURA_NC ";
				
				if (params.containsKey("dtPagamentoInicial") && params.get("dtPagamentoInicial") != null) {
					sql +="AND         RI.DT_PAGAMENTO_EFETUADO >= :dtPagamentoInicial ";
				} 
				if (params.containsKey("dtPagamentoFinal") && params.get("dtPagamentoFinal") != null) {
					sql +="AND         RI.DT_PAGAMENTO_EFETUADO <= :dtPagamentoFinal ";
				}
				
				if(params.get("idFranquia") != null){
					sql +="AND         FRQ.ID_FRANQUIA = :idFranquia ";
				}

				sql +=	"AND         RI.TP_STATUS_INDENIZACAO = :tpStatusIndenizacao "
						+"AND         NOT EXISTS(SELECT 1 "
						+"                       FROM   RECIBO_INDENIZACAO_FRQ RIF  "
						+"                       WHERE  RIF.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO "
						+"                       AND    RIF.ID_FRANQUIA = FRQ.ID_FRANQUIA "
						+"                       AND    RIF.TP_SITUACAO_PENDENCIA IN (:aprovada,:emAprovacao)) "
						+"GROUP BY RI.ID_RECIBO_INDENIZACAO, F.SG_FILIAL, RI.NR_RECIBO_INDENIZACAO, FFRQ.SG_FILIAL, DECODE(MANC.TP_MOTIVO, 'AV','AV','FV','FA','OU') "
						+"ORDER BY F.SG_FILIAL, RI.NR_RECIBO_INDENIZACAO, FFRQ.SG_FILIAL ";


		return getAdsmHibernateTemplate().findBySql(sql,params,null);
	}
	
	public List<Map<String, Object>> findRelatorioIndezacoesFranqueado(Map<String, Object> parameters) {	
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioIndenizacoesFranqueadoQuery.getQuery(parameters), 
																	parameters, 
																	RelatorioIndenizacoesFranqueadoQuery.configureCSV());
	}
	
}