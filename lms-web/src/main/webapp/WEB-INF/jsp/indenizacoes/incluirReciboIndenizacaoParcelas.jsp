<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">

	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" idProperty="idParcelaReciboIndenizacao" service="lms.indenizacoes.incluirReciboIndenizacaoAction.findParcelasById">

	    <adsm:masterLink idProperty="idReciboIndenizacao" >
		    <adsm:masterLinkItem property="reciboIndenizacao" label="RIM" itemWidth="50" />
	    </adsm:masterLink>

		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" size="13" maxLength="13" width="80%" labelWidth="20%" required="true" />


		<adsm:textbox label="dataVencimento" property="dtVencimento" dataType="JTDate" 
					  onchange="return validaDtVencimento()"
					  width="80%" labelWidth="20%" disabled="false" required="true"/>

		<adsm:combobox label="valor" width="80%" labelWidth="20%"
					   property="moeda.idMoeda" 
					   boxWidth="85"
					   service="lms.indenizacoes.incluirReciboIndenizacaoAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   required="true"
					   onlyActiveValues="true" disabled="true" defaultValue="1"
					   >
			<adsm:textbox property="vlPagamento" dataType="currency" required="true" disabled="false"/>
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarParcela" service="lms.indenizacoes.incluirReciboIndenizacaoAction.saveParcelas" callbackProperty="storeCallback"/>
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="parcelaReciboIndenizacao" 
				idProperty="idParcelaReciboIndenizacao" 
				service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedParcelas"
				rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountParcelas"
				detailFrameName="parcelas"				
				selectionMode="check" 
				unique="true" 
				rows="9"
				defaultOrder="nrBoleto"
				>
		<adsm:gridColumn property="nrBoleto"              title="numeroBoleto" width="300" align="left" />
		<adsm:gridColumn property="dtVencimento"          title="vencimento"   width="100" dataType="JTDate"/>
		<adsm:gridColumn property="sgSimboloParcela"      title="valor" width="50"  align="left"                     />
		<adsm:gridColumn property="vlPagamento"           title=""      width="250" align="right" dataType="currency"/>
		
		<adsm:buttonBar>
			<adsm:button caption="excluirParcela" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:grid>


</adsm:window>
<script>

   	var tabGroup = getTabGroup(this.document);
	var abaRim = tabGroup.getTab("rim");
	
	function pageLoad_cb() {
		onPageLoad_cb();		
	}
		
	function initWindow(e) {
		if (e.name=='newButton_click') {
			setMoedaSessao();
		}
	}
		
	function setMoedaSessao() {
		setElementValue('moeda.idMoeda', abaRim.getFormProperty("idMoedaHidden"));		
	}	
	
	function onRemoveButtonClick() {
		// parcelaReciboIndenizacaoGridDef.removeByIds('', 'remove');
		
		var idsMap = parcelaReciboIndenizacaoGridDef.getSelectedIds();
		if (idsMap.ids.length>0) { 
			if (window.confirm(erExcluir)) {

				var data = new Array();
				data.ids = idsMap.ids;
				data.masterId = getElementValue('masterId');

				var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.removeParcelasByIds", "remove", data);
		    	xmit({serviceDataObjects:[sdo]});		

			}
		} else {
			alert(erSemRegistro);
		}
	}	
	
	function remove_cb(data, error) {
		if (error==undefined && data!=undefined && data.rowCount!=undefined) {
		}
		resetValue(document);
		setMoedaSessao();
		parcelaReciboIndenizacaoGridDef.removeByIds_cb(data, error);
	}	
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		data.masterId = getElementValue('masterId');
		parcelaReciboIndenizacaoGridDef.executeSearch(data);
	}	
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) {
			setMoedaSessao();		
			setElementValue('idParcelaReciboIndenizacao', data.idParcelaReciboIndenizacao);			
			resetValue(document);
			setMoedaSessao();
			executeSearch();
		}
	}
	
	function onTabShow(fromTab) {
		resetValue(document);
		setMoedaSessao();
		executeSearch();
	}
	
	
	function validaDtVencimento() {
		if (getElementValue("dtVencimento") != "") {
			var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.validateDtVencimento", 
				"retornoValidaDtVencimento", {dtVencimento:getElementValue("dtVencimento")});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function retornoValidaDtVencimento_cb(data, error) {
		if (error != undefined) {
			alert(error);
			resetValue("dtVencimento");
			setFocus(document.getElementById("dtVencimento"));
		}
	}
</script>