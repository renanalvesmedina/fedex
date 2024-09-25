<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" idProperty="idParcelaReciboIndenizacao" service="lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdParcela">

		<adsm:hidden property="reciboIndenizacao.idReciboIndenizacao"/>
		<adsm:complement label="numeroRIM" labelWidth="20%" width="80%" separator="branco">
		<adsm:textbox property="reciboIndenizacao.filial.sgFilial" dataType="text"        disabled="true" size="3"/>
		<adsm:textbox property="reciboIndenizacao.nrReciboIndenizacao" dataType="integer" disabled="true" size="8" mask="00000000"/>
		</adsm:complement>

		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" size="13" maxLength="13" width="80%" labelWidth="20%" required="false" disabled="true"/>
		<adsm:textbox label="dataVencimento" property="dtVencimento" dataType="JTDate" width="80%" labelWidth="20%" disabled="true" required="false" picker="false"/>

		<adsm:combobox label="valor" width="80%" labelWidth="20%"
					   property="moeda.idMoeda" 
					   boxWidth="85"
					   disabled="true"
					   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   required="false"
					   onlyActiveValues="true"
					   >
			<adsm:textbox property="vlPagamento" dataType="currency" required="false" disabled="true"/>
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="parcelaReciboIndenizacao" 
				idProperty="idParcelaReciboIndenizacao" 
				service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedParcela"
				rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountParcela"
				detailFrameName="parcelas"				
				selectionMode="none" 
				unique="true" 
				rows="10"
				defaultOrder="nrBoleto"
				>
		<adsm:gridColumn property="nrBoleto"              title="numeroBoleto" width="300" align="left" />
		<adsm:gridColumn property="dtVencimento"          title="vencimento"   width="100" dataType="JTDate"/>
		<adsm:gridColumn property="sgSimboloParcela"      title="valor" width="50"  align="left"                     />
		<adsm:gridColumn property="vlPagamento"           title=""      width="250" align="right" dataType="currency"/>
		
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>


</adsm:window>
<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");

	document.getElementById('reciboIndenizacao.idReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.nrReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.filial.sgFilial').masterLink='true';	
	
	function pageLoad_cb() {
		onPageLoad_cb();
	}
				
	function remove_cb(data, error) {
		if (error==undefined && data!=undefined && data.rowCount!=undefined) {
			alteraQtParcelasAbaDetalhamento(data.rowCount);
		}
		parcelaReciboIndenizacaoGridDef.removeByIds_cb(data, error);
	}
	
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'reciboIndenizacao.idReciboIndenizacao', getElementValue('reciboIndenizacao.idReciboIndenizacao'));
		parcelaReciboIndenizacaoGridDef.executeSearch(data);
	}	
		
	// altera o valor da quantidade de parcelas na aba rim
	function alteraQtParcelasAbaDetalhamento(qtParcelas) {	
		abaDetalhamento.getDocument().getElementById('qtParcelasBoletoBancario').value = qtParcelas;
	}

	// tab show
	function onTabShow(fromTab) {
		resetValue(document);
		setMasterLinkProperties();		
		executeSearch();	
	}
	
	// seta os valores masterLink
	function setMasterLinkProperties() {
		setElementValue('reciboIndenizacao.idReciboIndenizacao', abaDetalhamento.getFormProperty("idReciboIndenizacao"));
		setElementValue('reciboIndenizacao.filial.sgFilial',     abaDetalhamento.getFormProperty("sgFilialRecibo"));
		setElementValue('reciboIndenizacao.nrReciboIndenizacao', abaDetalhamento.getFormProperty("nrReciboIndenizacao"));
	}
	
	
	
</script>