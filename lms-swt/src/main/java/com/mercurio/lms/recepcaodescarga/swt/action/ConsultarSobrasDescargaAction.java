package com.mercurio.lms.recepcaodescarga.swt.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.VolumeSobraFilialService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ConsultarSobrasDescargaAction extends CrudAction {

	private FilialService filialService;
	private VolumeSobraFilialService volumeSobraFilialService;

	@SuppressWarnings("unchecked")
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedConsultarSobrasDescarga(TypedFlatMap criteria) {

		ResultSetPage rsp = volumeSobraFilialService.findPaginatedConsultarSobrasDescarga(criteria);

		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				Map mapItem = (Map) item;
				TypedFlatMap filterItem = new TypedFlatMap();

				filterItem.put("idDoctoServico", mapItem.get("idDoctoServico"));
				filterItem.put("idFilialOrigem", mapItem.get("idFilialOrigem"));
				filterItem.put("dhCriacao", mapItem.get("dhCriacao"));
				filterItem.put("sgFilialOrigemDocumento", mapItem.get("sgFilialOrigemDocumento"));
				filterItem.put("tpDocumentoServico", ((DomainValue) mapItem.get("tpDocumentoServico")).getValue());
				filterItem.put("nrDoctoServico", mapItem.get("nrDoctoServico"));
				
				filterItem.put("filialOrigem", mapItem.get("sgFilialOrigem"));
				filterItem.put("documentoDeServico", mapItem.get("sgFilialOrigemDocumento") + " " + FormatUtils.fillNumberWithZero(String.valueOf(mapItem.get("nrDoctoServico")), 10));
				filterItem.put("totalVolume", mapItem.get("qtVolumes"));
				filterItem.put("totalVolumeManipulado", mapItem.get("totalVolumeManipulado"));
				filterItem.put("totalPesoManipulado", mapItem.get("totalPesoManipulado"));
				
				Integer totalVolume = (Integer) mapItem.get("qtVolumes");
				Long totalVolumeManipulado = (Long) mapItem.get("totalVolumeManipulado");
				BigDecimal totalValorManipulado = (BigDecimal) mapItem.get("vlMercadoria");
				if(totalValorManipulado != null && totalVolume != null && totalVolumeManipulado != null){
					totalValorManipulado = totalValorManipulado.divide(new BigDecimal(totalVolume), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(totalVolumeManipulado));
				}
				filterItem.put("totalValorManipulado", totalValorManipulado);
				
				return filterItem;
			}
		};

		return (ResultSetPage) frsp.doFilter();
	}

	public Integer getRowCountConsultarSobrasDescarga(TypedFlatMap criteria) {
		return volumeSobraFilialService.getRowCountConsultarSobrasDescarga(criteria);
	}

	/**
	 * Recebe todos os dados da linha selecionada na grid da primeira aba, e retorna os mesmos, para que sejam utilizados na busca da grid da segunda
	 * aba.
	 * 
	 * @param criteria
	 * @return
	 */
	public Map popularCriterios(TypedFlatMap criteria) {
		return criteria;
	}
	
	public ResultSetPage findPaginatedConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria) {
		ResultSetPage rsp = volumeSobraFilialService.findPaginatedConsultarSobrasDescargaDetalhamento(criteria);

		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				Map mapItem = (Map) item;
				TypedFlatMap filterItem = new TypedFlatMap();

				filterItem.put("dataManipulacao", JTDateTimeUtils.formatDateTimeToString((DateTime) mapItem.get("dataManipulacao")));
				filterItem.put("sequenciaVolume", mapItem.get("nrSequencia") + "/" + mapItem.get("qtVolumes"));
				filterItem.put("pesoVolume", mapItem.get("psAferido"));
				
				Integer qtVolumes = (Integer) mapItem.get("qtVolumes");
				BigDecimal valorVolume = (BigDecimal) mapItem.get("vlMercadoria");
				if(qtVolumes != null && valorVolume != null){
					valorVolume = valorVolume.divide(new BigDecimal(qtVolumes), 6, BigDecimal.ROUND_HALF_UP);
				}
				filterItem.put("valorVolume",valorVolume);
				return filterItem;
			}
		};

		return (ResultSetPage) frsp.doFilter();
	}

	public Integer getRowCountConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria) {
		return volumeSobraFilialService.getRowCountConsultarSobrasDescargaDetalhamento(criteria);
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setVolumeSobraFilialService(VolumeSobraFilialService volumeSobraFilialService) {
		this.volumeSobraFilialService = volumeSobraFilialService;
	}

}
