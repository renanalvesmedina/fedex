<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	
	function gridFaturaLogRowClick(id){
	
		gridItemFaturaLogGridDef.executeSearch({idFaturaLog:id});
		

		return false;
	}
	
</script>
<adsm:window service="lms.contasreceber.consultarFaturaLogAction">
	<adsm:form action="/contasReceber/consultarFaturasLog" >
									
		<adsm:lookup property="filialByIdFilial" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterFaturasAction.findLookupFilial"
			dataType="text" label="filialFaturamento" size="3"
			action="/municipios/manterFiliais" width="9%" required="true"
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px"
			maxLength="3">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilial.pessoa.nmFantasia" width="26%" size="30"
				serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:textbox dataType="integer" label="numero" property="nrFatura"
			size="10" maxLength="10" width="35%" disabled="false" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridFaturaLog" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
			property="gridFaturaLog" 
			idProperty="idFaturaLog" 				
			rows="6" 
			service="lms.contasreceber.consultarFaturaLogAction.findPaginated"
			rowCountService="lms.contasreceber.consultarFaturaLogAction.getRowCount"
			selectionMode="none"
			onRowClick="gridFaturaLogRowClick"
			width="10000"
			gridHeight="100"
			scrollBars="horizontal"
			unique="false"
  	>

		<adsm:gridColumn property="idFatura" title="fatura" dataType="integer"/> 
		<adsm:gridColumn property="idFilial" title="filial" dataType="integer"/> 
		<adsm:gridColumn property="idCliente" title="cliente" dataType="integer"/>
		<adsm:gridColumn property="idFilialCobradora" title="filialCobradora" dataType="integer"/>  
		<adsm:gridColumn property="idDivisaoCliente" title="divisaoCliente" dataType="integer"/> 
		<adsm:gridColumn property="nrFatura" title="fatura" dataType="integer"/> 
		<adsm:gridColumn property="qtDocumentos" title="qtdeTotalDocumentos" dataType="integer"/> 
		<adsm:gridColumn property="vlBaseCalcPisCofinsCsll" title="baseCalcPisCofinsCsll" dataType="currency"/> 
		<adsm:gridColumn property="vlBaseCalcIr" title="baseCalcIr" dataType="currency"/> 
		<adsm:gridColumn property="vlPis" title="pis" dataType="currency"/> 
		<adsm:gridColumn property="vlCofins" title="cofins" dataType="currency"/> 
		<adsm:gridColumn property="vlCsll" title="csll" dataType="currency"/> 
		<adsm:gridColumn property="vlIr" title="ir" dataType="currency"/> 
		<adsm:gridColumn property="vlIva" title="valorIVA" dataType="currency"/> 
		<adsm:gridColumn property="vlTotal" title="valorTotalFrete" dataType="currency"/> 
		<adsm:gridColumn property="vlDesconto" title="valorTotalDesconto" dataType="currency"/> 
		<adsm:gridColumn property="vlTotalRecebido" title="valorTotalRecebido" dataType="currency"/> 
		<adsm:gridColumn property="vlJuroCalculado" title="valorJurosCalc" dataType="currency"/> 
		<adsm:gridColumn property="vlJuroRecebido" title="valorJurosRecebidos" dataType="currency"/> 
		<adsm:gridColumn property="vlCotacaoMoeda" title="vlCotacaoMoeda" dataType="currency"/> 
		<adsm:gridColumn property="dtEmissao" title="dataEmissao" dataType="JTDate"/> 
		<adsm:gridColumn property="dtVencimento" title="dataVencimento" dataType="JTDate"/> 
		<adsm:gridColumn property="blGerarEdi" title="gerarEDI" renderMode="image-check"/> 
		<adsm:gridColumn property="blGerarBoleto" title="gerarBoleto" renderMode="image-check"/> 
		<adsm:gridColumn property="blFaturaReemitida" title="faturaReemitida" renderMode="image-check"/> 
		<adsm:gridColumn property="blIndicadorImpressao" title="indicadorImpressao" renderMode="image-check"/> 
		<adsm:gridColumn property="tpFatura" title="fatura" isDomain="true"/> 
		<adsm:gridColumn property="tpSituacaoAprovacao" title="situacaoAprovacao" isDomain="true"/> 
		<adsm:gridColumn property="tpSituacaoFatura" title="situacaoFatura" isDomain="true"/> 
		<adsm:gridColumn property="tpOrigem" title="origem" isDomain="true"/> 
		<adsm:gridColumn property="tpAbrangencia" title="abrangencia" isDomain="true"/> 
		<adsm:gridColumn property="tpModal" title="modal" isDomain="true"/> 
		<adsm:gridColumn property="idRelacaoCobranca" title="relacaoCobranca" dataType="integer"/> 
		<adsm:gridColumn property="idUsuario" title="usuario" dataType="integer"/> 
		<adsm:gridColumn property="idFaturaOriginal" title="faturaOriginal" dataType="integer"/> 
		<adsm:gridColumn property="idCedente" title="cedente" dataType="integer"/> 
		<adsm:gridColumn property="idManifestoEntrega" title="manifestoEntrega" dataType="integer"/> 
		<adsm:gridColumn property="idCotacaoMoeda" title="cotacaoMoeda" dataType="integer"/> 
		<adsm:gridColumn property="idManifesto" title="manifesto" dataType="integer"/> 
		<adsm:gridColumn property="idTipoAgrupamento" title="tipoAgrupamento" dataType="integer"/> 
		<adsm:gridColumn property="idAgrupamentoCliente" title="agrupamentoCliente" dataType="integer"/> 
		<adsm:gridColumn property="idPendencia" title="pendencia" dataType="integer"/> 
		<adsm:gridColumn property="dtLiquidacao" title="dataLiquidacao" dataType="JTDate"/> 
		<adsm:gridColumn property="dtTransmissaoEdi" title="dataTransmissaoEDI" dataType="JTDate"/> 
		<adsm:gridColumn property="dhReemissao" title="dataHoraReemissao" dataType="JTDateTimeZone"/> 
		<adsm:gridColumn property="dhTransmissao" title="dataHoraTransmissao" dataType="JTDateTimeZone"/> 
		<adsm:gridColumn property="nrPreFatura" title="numeroPreFatura" dataType="integer"/> 
		<adsm:gridColumn property="obFatura" title="observacao"/> 
		<adsm:gridColumn property="idMoeda" title="moeda" dataType="integer"/> 
		<adsm:gridColumn property="tpFrete" title="tipoFrete" isDomain="true"/> 
		<adsm:gridColumn property="idServico" title="servico" dataType="integer"/> 
		<adsm:gridColumn property="idManifestoOrigem" title="manifestoOrigem" dataType="integer"/> 
		<adsm:gridColumn property="idManifestoEntregaOrigem" title="manifestoEntregaOrigem" dataType="integer"/> 
		<adsm:gridColumn property="idPendenciaDesconto" title="pendenciaDesconto" dataType="integer"/> 
		<adsm:gridColumn property="idBoleto" title="boleto" dataType="integer"/> 
		<adsm:gridColumn property="idRecibo" title="recibo" dataType="integer"/> 
		<adsm:gridColumn property="idRedeco" title="redeco" dataType="integer"/> 
		<adsm:gridColumn property="idNotaDebitoNacional" title="notaDebitoNacional" dataType="integer"/> 
		<adsm:gridColumn property="loginLog" title="loginLog"/> 
		<adsm:gridColumn property="dhLog" title="dataHoraLog" dataType="JTDateTimeZone"/> 
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/> 
	</adsm:grid>
	<adsm:grid 
			property="gridItemFaturaLog" 
			idProperty="idItemFaturaLog"
			service="lms.contasreceber.consultarFaturaLogAction.findPaginatedItems"
			rowCountService="lms.contasreceber.consultarFaturaLogAction.getRowCountItems"	
			rows="3" 
			selectionMode="none"
			scrollBars="horizontal"
			autoSearch="false"
			gridHeight="60"
			width="3000"
			unique="false"
  	>

  		<adsm:gridColumn property="itemIdItemFatura" title="item" dataType="integer"/>
  		<adsm:gridColumn property="itemIdDevedorDocServFat" title="devedor" dataType="integer"/>
  		<adsm:gridColumn property="itemNrVersao" title="versao" dataType="integer"/>
		<adsm:gridColumn property="itemLoginLog" title="loginLog"/> 
		<adsm:gridColumn property="itemDhLog" title="dataHoraLog" dataType="JTDateTimeZone"/> 
		<adsm:gridColumn property="itemOpLog" title="opLog" isDomain="true"/>

		<adsm:gridColumn property="descontoIdDesconto" title="desconto" dataType="integer"/>
		<adsm:gridColumn property="descontoIdReciboDesconto" title="recibo" dataType="integer"/>
		<adsm:gridColumn property="descontoIdDemonstrativoDesconto" title="demonstrativo" dataType="integer"/>
		<adsm:gridColumn property="descontoIdMotivoDesconto" title="motivoDesconto" dataType="integer"/>
		<adsm:gridColumn property="descontoTpSituacaoAprovacao" title="situacaoAprovacaoDesconto" isDomain="true"/>
		<adsm:gridColumn property="descontoVlDesconto" title="valorDesconto" dataType="currency"/>                                                                                                                                                                                   
		<adsm:gridColumn property="descontoObDesconto" title="observacao"/>                                                                                                                                                                                  
		<adsm:gridColumn property="descontoIdPendencia" title="pendencia" dataType="integer"/>
		<adsm:gridColumn property="descontoLoginLog" title="loginLog"/>
		<adsm:gridColumn property="descontoDhLog" title="dataHoraLog" dataType="JTDateTimeZone"/> 
		<adsm:gridColumn property="descontoOpLog" title="opLog" isDomain="true"/>

	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>