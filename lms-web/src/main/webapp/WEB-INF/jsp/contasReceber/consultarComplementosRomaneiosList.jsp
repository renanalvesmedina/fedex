<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.consultarComplementosRomaneiosAction" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/contasReceber/consultarComplementosRomaneios" idProperty="idComplementoRomaneio" >
		
		<adsm:hidden property="idFatura" serializable="true" />
		
		<adsm:textbox property="numeroPreFatura"  	label="numeroPreFatura" dataType="integer" size="20" 
		labelWidth="22%" width="25%" disabled="true" maxLength="7"/>		

		<adsm:textbox property="sgFilialFatura" label="fatura" dataType="text" size="5" 
		labelWidth="22%" width="7%" disabled="true"/>		
		<adsm:textbox property="nrFatura"  dataType="integer" size="10" width="10%" disabled="true" />


		<adsm:textbox property="codigoDeposito"  	label="codigoDeposito" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true" maxLength="5"/>		

		<adsm:textbox property="cliente" label="cliente" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true" maxLength="14"/>		


		<adsm:textbox property="codigoTransportadora" label="codigoTransportadora" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true" maxLength="3"/>		

		<adsm:textbox property="cnpjTransportadora" label="cnpjTransportadora" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true" maxLength="14"/>		


		<adsm:textbox property="tipoFrete"  label="tipoFrete" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		


		<adsm:textbox property="modalidadeFrete"  	label="modalidadeFrete" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		
		
		
		<adsm:textbox property="tipoFreteUrbano"  	label="tipoFreteUrbano" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>	
			
		<adsm:textbox property="dataVencimento"  	label="dataVencimento" dataType="JTDate" picker="false" size="20" 	
		labelWidth="22%" width="28%" disabled="true"/>		


		<adsm:textbox property="valorFrete"  		label="valorFrete" dataType="currency" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		

		
		
		<adsm:range label="fechamento" labelWidth="22%" width="28%" >
			<adsm:textbox disabled="true"	label="fechamento"	dataType="JTDate" 	property="fechamentoIni" 	
			picker="false" />
			
			<adsm:textbox 	dataType="JTDate" 	property="fechamentoFim" disabled="true"	picker="false"/>
		</adsm:range>
		
		
		<adsm:textbox property="cnpjFornecedor"  	label="cnpjFornecedor" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		
		
		<adsm:textbox property="valorBloqueio"  		label="valorBloqueio" dataType="currency" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		


			
		<adsm:textbox property="valorDesbloqueio"  		label="valorDesbloqueio" dataType="currency" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		
		
		<adsm:textbox property="moeda"  label="moedaFatura" dataType="text" size="20" 	
		labelWidth="22%" 	width="25%" disabled="true"/>		
		
				
		<adsm:buttonBar />
	</adsm:form>
	
	
	
		<adsm:grid idProperty="idComplementoRomaneio" property="gridComplementoRomaneio" 
			   scrollBars="horizontal"
			   service="lms.contasreceber.consultarComplementosRomaneiosAction.findPaginatedComplemento" 
			   rowCountService="lms.contasreceber.consultarComplementosRomaneiosAction.getRowCountComplemento"
			   onRowClick="doNothing"
			   onRowDblClick="doNothing"
			   selectionMode="none"
			   rows="7" gridHeight="140">
	
		
		
		<adsm:gridColumn width="100" title="notaFiscal" property="nrNotaFiscal" dataType="integer"/>
		<adsm:gridColumn property="cdSerieNotaFiscal" 		width="100" title="serie" 	 dataType="text"/>
		<adsm:gridColumn property="emissao" 				width="100" title="emissao" 		 dataType="JTDate"/>
		<adsm:gridColumn property="peso" 					width="100" title="pesoKG" 		 dataType="integer"/>
		<adsm:gridColumn title="valor" dataType="text" property="moedaValor" width="40"/>
		<adsm:gridColumn title="" dataType="currency" property="valor" width="90"/>
		<adsm:gridColumn property="tipoFrete" 				width="100" title="tipoFrete" 		dataType="text" />
		<adsm:gridColumn property="modalidadeFrete" 		width="150" title="modalidadeFrete" dataType="text"/>
		<adsm:gridColumn property="tipoFreteUrbano" 		width="150" title="tipoFreteUrbano" dataType="text"/>
		<adsm:gridColumn property="roteiro" 				width="100" title="roteiro" 				dataType="integer"/>
		<adsm:gridColumn property="protocolo" 				width="100" title="protocolo" 			dataType="integer"/>
		<adsm:gridColumn property="clienteDestino" 			width="200" title="clienteDestinatario"  dataType="text"/>
		<adsm:gridColumn property="volume" 					width="100" title="volumeM3" 			dataType="integer"/>
		<adsm:gridColumn property="cidadeCliente" 			width="200" title="cidadeCliente" 		dataType="text"/>
		
		<adsm:buttonBar />
		
	</adsm:grid>
	
	
</adsm:window>

<script>
	function doNothing(){
		return false;
	}

	function myPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);
	       var dados = new Array();
	       setNestedBeanPropertyValue(dados, "idFatura", getElementValue("idFatura"));
	       var sdo = createServiceDataObject("lms.contasreceber.consultarComplementosRomaneiosAction.findByIdComplemento",
	                                            "idFaturaSet",
	                                            dados);
	        xmit({serviceDataObjects:[sdo]});
	        
	   findButtonScript('gridComplementoRomaneio', this.document.forms[0]);
		
	}

	/** Função chamada após o xmit */
	function idFaturaSet_cb(data, errorMessage, errorCode, eventObj){
	
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	
		var idFatura = getElementValue("idFatura");
		if (idFatura != ""){
			setElementValue("idFatura", idFatura);
		}
	}

</script>