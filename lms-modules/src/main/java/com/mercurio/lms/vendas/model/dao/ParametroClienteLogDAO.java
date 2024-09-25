package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.ParametroClienteLog;

/**
 * @spring.bean 
 */
public class ParametroClienteLogDAO extends BaseCrudDao<ParametroClienteLog, Long> {
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("clienteRedespacho.pessoa", FetchMode.SELECT);
		lazyFindPaginated.put("filialDestino", FetchMode.SELECT);
		lazyFindPaginated.put("filialOrigem", FetchMode.SELECT);
	}
	
	protected final Class getPersistentClass() {

		return ParametroClienteLog.class;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			
			public Map filterItem(Object item){
				ParametroClienteLog log = (ParametroClienteLog)item;
				Map map = new HashMap();
				//informações do log
				map.put("dhLog", log.getDhLog());
				map.put("loginLog", log.getLoginLog());
				map.put("tpOrigemLog", log.getTpOrigemLog());
				map.put("opLog", log.getOpLog());
				
				map.put("idParametroClienteLog",log.getIdParametroClienteLog());
				map.put("idParametroCliente", log.getParametroCliente()==null?"":log.getParametroCliente().getIdParametroCliente());
				map.put("aeroportoDestino.siglaDescricao",log.getAeroportoDestino()==null?"":log.getAeroportoDestino().getSiglaDescricao());
				map.put("aeroportoOrigem.siglaDescricao", log.getAeroportoOrigem()==null?"":log.getAeroportoOrigem().getSiglaDescricao());
				map.put("clienteRedespacho.pessoa.nrIdentificacao", log.getClienteRedespacho()==null?"":FormatUtils.formatIdentificacao(log.getClienteRedespacho().getPessoa()));
				map.put("clienteRedespacho.pessoa.nmPessoa",log.getClienteRedespacho()==null?"": log.getClienteRedespacho().getPessoa().getNmPessoa());
				map.put("dsEspecificacaoRota", log.getDsEspecificacaoRota());
				map.put("dtVigenciaFinal", log.getDtVigenciaFinal());
				map.put("dtVigenciaInicial", log.getDtVigenciaInicial());
				map.put("filialDestino.sgFilial", log.getFilialDestino()==null?"":log.getFilialDestino().getSgFilial());
				map.put("filialOrigem.sgFilial", log.getFilialOrigem()==null?"":log.getFilialOrigem().getSgFilial());
				map.put("filialMercurioRedespacho.sgFilial", log.getFilialMercurioRedespacho()==null?"":log.getFilialMercurioRedespacho().getSgFilial());
				map.put("municipioDestino.nmMunicipio",log.getMunicipioDestino()==null?"": log.getMunicipioDestino().getNmMunicipio());
				map.put("municipioOrigem.nmMunicipio", log.getMunicipioOrigem()==null?"":log.getMunicipioOrigem().getNmMunicipio());
				map.put("paisDestino.nmPais", log.getPaisDestino()==null?"":log.getPaisDestino().getNmPais());
				map.put("paisOrigem.nmPais", log.getPaisOrigem()==null?"":log.getPaisOrigem().getNmPais());
				map.put("pcCobrancaDevolucoes", log.getPcCobrancaDevolucoes());
				map.put("pcCobrancaReentrega", log.getPcCobrancaReentrega());
				map.put("pcDescontoFreteTotal", log.getPcDescontoFreteTotal());
				map.put("pcFretePercentual", log.getPcFretePercentual());
				map.put("pcPagaCubagem", log.getPcPagaCubagem());
				map.put("pcReajAdvalorem", log.getPcReajAdvalorem());
				map.put("pcReajAdvalorem2", log.getPcReajAdvalorem2());
				map.put("pcReajFretePeso", log.getPcReajFretePeso());
				map.put("pcReajMinimoGris", log.getPcReajMinimoGris());
				map.put("pcReajMinimoTde", log.getPcReajMinimoTde());
				map.put("pcReajPedagio", log.getPcReajPedagio());
				map.put("pcReajTarifaMinima", log.getPcReajTarifaMinima());
				map.put("pcReajVlFreteVolume", log.getPcReajVlFreteVolume());
				map.put("pcReajVlMinimoFretePercen", log.getPcReajVlMinimoFretePercen());
				map.put("pcReajVlMinimoFreteQuilo", log.getPcReajVlMinimoFreteQuilo());
				map.put("pcReajVlTarifaEspecifica", log.getPcReajVlTarifaEspecifica());
				map.put("pcReajVlToneladaFretePerc", log.getPcReajVlToneladaFretePerc());
				map.put("psFretePercentual", log.getPsFretePercentual());
				map.put("pcReajVlMinimoFreteQuilo", log.getPcReajVlMinimoFreteQuilo());
				map.put("tabelaPreco.tabelaPrecoString", log.getTabelaPreco()==null?"":log.getTabelaPreco().getTabelaPrecoString());
				map.put("tipoLocalizacaoMunicipioDestino.dsTipoLocalizacaoMunicipio",log.getTipoLocMunicipioDestino()==null?"": log.getTipoLocMunicipioDestino().getDsTipoLocalizacaoMunicipio());
				map.put("tipoLocalizacaoMunicipioOrigem.dsTipoLocalizacaoMunicipio",log.getTipoLocMunicipioOrigem()==null?"":log.getTipoLocMunicipioOrigem().getDsTipoLocalizacaoMunicipio());
				map.put("tpIndicadorAdvalorem", log.getTpIndicadorAdvalorem());
				map.put("tpIndicadorAdvalorem2", log.getTpIndicadorAdvalorem2());
				map.put("tpIndicadorFretePeso", log.getTpIndicadorFretePeso());
				map.put("tpIndicadorMinFretePeso", log.getTpIndicadorMinFretePeso());
				map.put("tpIndicadorMinimoGris", log.getTpIndicadorMinimoGris());
				map.put("tpIndicadorMinimoTde", log.getTpIndicadorMinimoTde());
				map.put("tpIndicadorPedagio", log.getTpIndicadorPedagio());
				map.put("tpIndicadorPercentualGris", log.getTpIndicadorPercentualGris());
				map.put("tpIndicadorPercentualTde", log.getTpIndicadorPercentualTde());
				map.put("tpIndicVlrTblEspecifica", log.getTpIndicVlrTblEspecifica());
				map.put("tpSituacaoParametro", log.getTpSituacaoParametro());
				map.put("tpTarifaMinima", log.getTpTarifaMinima());
				map.put("ufDestino.sgUnidadeFederativa", log.getUfDestino()==null?"":log.getUfDestino().getSgUnidadeFederativa());
				map.put("ufOrigem.sgUnidadeFederativa", log.getUfOrigem()==null?"":log.getUfOrigem().getSgUnidadeFederativa());
				map.put("vlAdvalorem", log.getVlAdvalorem());
				map.put("vlAdvalorem2", log.getVlAdvalorem2());
				map.put("vlFretePeso", log.getVlFretePeso());
				map.put("vlFreteVolume", log.getVlFreteVolume());
				map.put("vlMinFretePeso", log.getVlMinFretePeso());
				map.put("vlMinimoFretePercentual", log.getVlMinimoFretePercentual());
				map.put("vlMinimoFreteQuilo", log.getVlMinimoFreteQuilo());
				map.put("vlMinimoGris", log.getVlMinimoGris());
				map.put("vlMinimoTde", log.getVlMinimoTde());
				map.put("vlPedagio", log.getVlPedagio());
				map.put("vlPercentualGris", log.getVlPercentualGris());
				map.put("vlPercentualTde", log.getVlPercentualTde());
				map.put("vlPercMinimoProgr", log.getVlPercMinimoProgr());
				map.put("vlTarifaMinima", log.getVlTarifaMinima());
				map.put("vlTblEspecifica", log.getVlTblEspecifica());
				map.put("vlToneladaFretePercentual", log.getVlToneladaFretePercentual());
				map.put("vlValorReferencia", log.getVlValorReferencia());
				map.put("zonaDestino.dsZona", log.getZonaDestino()==null?"":log.getZonaDestino().getDsZona());
				map.put("zonaOrigem.dsZona", log.getZonaOrigem()==null?"":log.getZonaOrigem().getDsZona());
				return map;
			}
		};
		
		return (ResultSetPage) filter.doFilter();
	}
}
