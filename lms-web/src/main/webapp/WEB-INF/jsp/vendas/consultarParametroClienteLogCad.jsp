<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarParametroClienteLogAction" >
	<adsm:grid
			autoSearch="false"
			property="gridLog"
			idProperty="parametroCliente.idParametroClienteLog"
			width="10000"
			scrollBars="horizontal"
			selectionMode="none"
			onRowClick="rowClick"
			title="consultarParametroClienteLog"
			rows="15">

		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		
		<adsm:gridColumn property="dtVigenciaInicial" title="vigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="vigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="tpIndicadorPercentualGris" title="indicadorPercentualGris" isDomain="true"/>
		<adsm:gridColumn property="vlPercentualGris" title="percentualGris" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorMinimoGris" title="indicadorMinimoGris" isDomain="true"/>
		<adsm:gridColumn property="vlMinimoGris" title="minimoGris" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorPedagio" title="indicadorPedagio" isDomain="true"/>
		<adsm:gridColumn property="vlPedagio" title="pedagio" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorMinFretePeso" title="indicadorMinFretePeso" isDomain="true"/>
		<adsm:gridColumn property="vlMinFretePeso" title="minimoFretePeso" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorPercMinimoProgr" title="indicadorMinimoProgressivo" isDomain="true"/>
		<adsm:gridColumn property="vlPercMinimoProgr" title="percentualMinimoProgressivo" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorFretePeso" title="indicadorFretePeso" isDomain="true"/>
		<adsm:gridColumn property="vlFretePeso" title="fretePeso" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorAdvalorem" title="indicadorAdvalorem" isDomain="true"/>
		<adsm:gridColumn property="vlAdvalorem" title="advalorem" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorAdvalorem2" title="indicadorAdvalorem2" isDomain="true"/>
		<adsm:gridColumn property="vlAdvalorem2" title="vlAdvalorem2" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorValorReferencia" title="indicadorValorReferencia" isDomain="true"/>
		<adsm:gridColumn property="vlValorReferencia" title="valorReferencia" dataType="currency"/>
		<adsm:gridColumn property="vlMinimoFreteQuilo" title="valorMinimoFreteQuilo" dataType="currency"/>
		<adsm:gridColumn property="pcFretePercentual" title="percentualFretePercentual" dataType="currency"/>
		<adsm:gridColumn property="vlMinimoFretePercentual" title="valorMinimoFretePercentual" dataType="currency"/>
		<adsm:gridColumn property="vlToneladaFretePercentual" title="valorToneladaFretePercentual" dataType="currency"/>
		<adsm:gridColumn property="psFretePercentual" title="pesoReferenciaFretePercentual"/>
		<adsm:gridColumn property="pcDescontoFreteTotal" title="percentualDescontoFreteTotal" dataType="currency"/>
		<adsm:gridColumn property="tpIndicVlrTblEspecifica" title="indicVlrTblEspecifica" isDomain="true"/>
		<adsm:gridColumn property="vlTblEspecifica" title="valorTabelaEspecifica" dataType="currency"/>
		<adsm:gridColumn property="vlFreteVolume" title="valorFreteVolume" dataType="currency"/>
		<adsm:gridColumn property="blPagaCubagem" title="pagaCubagem" renderMode="image-check"/>
		<adsm:gridColumn property="pcPagaCubagem" title="percentualCubagem" dataType="currency"/>
		<adsm:gridColumn property="blPagaPesoExcedente" title="pagaPesoExcedente" renderMode="image-check"/>
		<adsm:gridColumn property="tpTarifaMinima" title="tarifaMinima" isDomain="true"/>
		<adsm:gridColumn property="vlTarifaMinima" title="tarifaMinima" dataType="currency"/>
		<adsm:gridColumn property="pcCobrancaReentrega" title="percentualCobrancaReentrega" dataType="currency"/>
		<adsm:gridColumn property="pcCobrancaDevolucoes" title="percentualCobrancaDevolucoes" dataType="currency"/>
		<adsm:gridColumn property="tpSituacaoParametro" title="situacao" isDomain="true"/>
		<adsm:gridColumn property="clienteRedespacho.pessoa.nmPessoa" title="parceiraRedespacho" dataType="text"/>
		<adsm:gridColumn property="filialMercurioRedespacho.sgFilial" title="filialRedespacho" dataType="text"/>
		<adsm:gridColumn property="filialOrigem.sgFilial" title="filialOrigem" dataType="text"/>
		<adsm:gridColumn property="filialDestino.sgFilial" title="filialDestino" dataType="text"/>
		<adsm:gridColumn property="zonaOrigem.dsZona" title="zonaOrigem" dataType="text"/>
		<adsm:gridColumn property="zonaDestino.dsZona" title="zonaDestino" dataType="text"/>
		<adsm:gridColumn property="paisOrigem.nmPais" title="paisOrigem" dataType="text"/>
		<adsm:gridColumn property="paisDestino.dsPais" title="paisDestino" dataType="text"/>
		<adsm:gridColumn property="tipoLocalizacaoMunicipioOrigem.dsTipoLocalizacaoMunicipio" title="tipoLocalizacaoOrigem" dataType="text"/>
		<adsm:gridColumn property="tipoLocalizacaoMunicipioDestino.dsTipoLocalizacaoMunicipio" title="tipoLocalizacaoDestino" dataType="text"/>
		<adsm:gridColumn property="ufOrigem.sgUnidadeFederativaOrigem" title="ufOrigem" dataType="text"/>
		<adsm:gridColumn property="ufDestino.idUfDestino" title="ufDestino" dataType="text"/>
		<adsm:gridColumn property="aeroportoOrigem.siglaDescricao" title="aeroportoOrigem" dataType="text"/>
		<adsm:gridColumn property="aeroportoDestino.siglaDescricao" title="aeroportoDestino" dataType="text"/>
		<adsm:gridColumn property="municipioOrigem.nmMunicipio" title="municipioOrigem" dataType="text"/>
		<adsm:gridColumn property="municipioDestino.nmMunicipio" title="municipioDestino" dataType="text"/>
		<adsm:gridColumn property="pcReajFretePeso" title="percentualReajFretePeso" dataType="currency"/>
		<adsm:gridColumn property="pcReajVlMinimoFreteQuilo" title="percentualReajVlMinimoFreteQuilo" dataType="currency"/>
		<adsm:gridColumn property="pcReajVlFreteVolume" title="percentualReajVlFreteVolume" dataType="currency"/>
		<adsm:gridColumn property="pcReajTarifaMinima" title="percentualReajTarifaMinima" dataType="currency"/>
		<adsm:gridColumn property="pcReajVlTarifaEspecifica" title="percentualReajVlTarifaEspecifica" dataType="currency"/>
		<adsm:gridColumn property="pcReajAdvalorem" title="percentualReajAdvalorem" dataType="currency"/>
		<adsm:gridColumn property="pcReajAdvalorem2" title="percentualReajAdvalorem2" dataType="currency"/>
		<adsm:gridColumn property="pcReajVlMinimoFretePercen" title="percentualReajVlMinimoFretePercen" dataType="currency"/>
		<adsm:gridColumn property="pcReajVlToneladaFretePerc" title="percentualReajVlToneladaFretePerc" dataType="currency"/>
		<adsm:gridColumn property="pcReajMinimoGris" title="percentualReajMinimoGris" dataType="currency"/>
		<adsm:gridColumn property="dsEspecificacaoRota" title="especificacaoRota"/>
		<adsm:gridColumn property="pcReajPedagio" title="percentualReajPedagio" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorPercentualTde" title="indicadorPercentualTde" isDomain="true"/>
		<adsm:gridColumn property="vlPercentualTde" title="percentualTde" dataType="currency"/>
		<adsm:gridColumn property="tpIndicadorMinimoTde" title="indicadorMinimoTde" isDomain="true"/>
		<adsm:gridColumn property="vlMinimoTde" title="minimoTde" dataType="currency"/>
		<adsm:gridColumn property="pcReajMinimoTde" title="percentualReajMinimoTde" dataType="currency"/>
		<adsm:gridColumn property="tabelaPreco.tabelaPrecoString" title="tabelaPrecos" dataType="text"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script language="javascript" type="text/javascript">

function rowClick(){
	return false;
}

function initWindow(eventObj){
	/** @author Vagner Huzalo 
	 *	se clicou em um registro da grid, vai buscar o valor do id em um hidden que o guarda, 
	 *	isto porque esta tela não possui um form de detalhamento e o preenchimento da grid será 
	 *  feito manualmente devido a problemas de chamadas assíncronas.
	 */
	if (eventObj.name == "gridRow_click2"){
		var doc = getTabGroup(document).getTab("list").tabOwnerFrame.document;
		var id = doc.getElementById("idParametroClienteRef").value;
		carregaGrid(id);
	}
}

function carregaGrid(id){
	if (id!=undefined){
		var filter = new Object();
		setNestedBeanPropertyValue(filter,"parametroCliente.idParametroCliente",id);
		gridLogGridDef.executeSearch(filter); 		
	}
}
</script>