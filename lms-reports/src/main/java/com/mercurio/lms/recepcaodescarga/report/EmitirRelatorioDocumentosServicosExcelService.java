package com.mercurio.lms.recepcaodescarga.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.model.util.AliasToNestedMapResultTransformer;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.dao.ControleCargaConhScanDAO;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * @spring.bean id="lms.recepcaodescarga.emitirRelatorioDocumentosServicosExcelService"
 * @spring.property name="reportName" value="com/mercurio/lms/recepcaodescarga/report/emitirRelatorioDocumentosServicosExcel.jasper"
 */

public class EmitirRelatorioDocumentosServicosExcelService extends ReportServiceSupport {
	
	private CarregamentoDescargaService carregamentoDescargaService;
	private ControleCargaConhScanDAO controleCargaConhScanDAO;

    public JRReportDataObject execute(Map parameters) throws Exception {
    	final Long idControleCarga = Long.parseLong(parameters.get("idControleCarga").toString());
		Long idCarregamentoDescarga = getIdControleCarga(idControleCarga);

		Map<String, Object> params = buildParameters(idControleCarga, idCarregamentoDescarga);

		List<Map> loadControleCargaConhScan = loadControleCargaConhScan(idCarregamentoDescarga, params);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(loadControleCargaConhScan);
    	
		Map parametersReport = buildReportParameters();
		JRReportDataObject jr = createReportDataObject(jrMap, parametersReport);

		return jr;
    }

	private Map buildReportParameters() {
		Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("siglaSimbolo", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		return parametersReport;
	}

	private Map<String, Object> buildParameters(final Long idControleCarga, Long idCarregamentoDescarga) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("idCarregamentoDescarga", idCarregamentoDescarga);
		params.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		return params;
	}
	private Long getIdControleCarga(final Long idControleCarga) {
		final CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), "D");
		
		Long idCarregamentoDescarga = null;
		
		if (carregamentoDescarga != null) {
			idCarregamentoDescarga = carregamentoDescarga.getIdCarregamentoDescarga();
		}
		return idCarregamentoDescarga;
	}
    
    private List<Map> loadControleCargaConhScan(Long idCarregamentoDescarga, Map<String, Object> params){
    	List retorno = new ArrayList();

    	String hql = getControleCargaConhScanDAO().buildFindByControleCargaAndCarregamentoHQL(idCarregamentoDescarga).toString();
		List<Map> data = getControleCargaConhScanDAO().getAdsmHibernateTemplate().findByNamedParam(hql, params);
		List<Map> nestedMap = AliasToNestedMapResultTransformer.getInstance().transformListResult(data);

		for (Map map : nestedMap) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			String nrDoctoServico = tfm.getLong("nrDoctoServico") == null ? "": String.valueOf(tfm.getLong("nrDoctoServico").toString());
			String tipoDocumento = formatTipoDocumento(tfm);
			
			Map dados = new HashMap();
			dados.put( "TP_DOCUMENTO", tipoDocumento);
			dados.put( "SG_FILIAL_ORIGEM", ifNullEmpty(tfm.getString("sgFilialOrigem")));
			dados.put( "NR_DOCTO_SERVICO", nrDoctoServico);
			dados.put( "CLIENTE_REMETENTE", tfm.getString("clienteRemetente"));
			dados.put( "CLIENTE_DESTINATARIO", tfm.getString("clienteDestinatario"));
			dados.put( "QT_VOLUMES", tfm.getInteger("qtVolumes"));
			dados.put( "PS_REAL", FormatUtils.formatPeso(tfm.getBigDecimal("psReal"), true));
			dados.put( "SG_MOEDA", ifNullEmpty(tfm.getString("sgMoeda")));
			dados.put( "VL_MERCADORIA", tfm.getBigDecimal("vlMercadoria"));
			dados.put( "SG_MOEDA2", tfm.getString("sgMoeda2"));
			dados.put( "VL_TOTAL_DOC_SERVICO", tfm.getBigDecimal("vlTotalDocServico"));
			dados.put( "SITUACAO_DOCUMENTO", tfm.getString("situacaoDocumento"));
			dados.put( "SG_FILIAL_DESTINO", tfm.getString("sgFilialDestino"));
			dados.put( "DT_PREV_ENTREGA", JTFormatUtils.format(tfm.getYearMonthDay("dtPrevEntrega"), JTFormatUtils.MEDIUM));

			dados.put( "SG_SERVICO", tfm.getString("sgServico"));
			retorno.add(dados);
		}

		return retorno;
    }

    private String ifNullEmpty(String value) {
    	return value == null? "" : value;
    }
    
	private String formatTipoDocumento(TypedFlatMap tfm) {
		if ( tfm.get("tpDocumento") != null) {
			Map map = (Map) tfm.get("tpDocumento");
			return String.valueOf(map.get("description"));
		}
		return "";
	}

    public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public ControleCargaConhScanDAO getControleCargaConhScanDAO() {
		return controleCargaConhScanDAO;
	}

	public void setControleCargaConhScanDAO(ControleCargaConhScanDAO controleCargaConhScanDAO) {
		this.controleCargaConhScanDAO = controleCargaConhScanDAO;
	}
}