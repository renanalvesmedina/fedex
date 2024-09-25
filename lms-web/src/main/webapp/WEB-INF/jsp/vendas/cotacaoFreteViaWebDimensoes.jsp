<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript">
	function myOnPageLoad(){
		onPageLoad();
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebDimensoesAction.findInSession", "findSession" );
		xmit({serviceDataObjects:[sdo]});
	}

	function findSession_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		var gridDms = document.getElementById("dimensao.dataTable").gridDefinition;
		gridDms.resetEditGrid();
		gridDms.insertRow();
		if (data != undefined){
			for(var i = 0; i < data.length; i++) {
				setElementValue("dimensao" + ":" + i + "." + "nrAltura", data[i].nrAltura);
				setElementValue("dimensao" + ":" + i + "." + "nrLargura", data[i].nrLargura);
				setElementValue("dimensao" + ":" + i + "." + "nrComprimento", data[i].nrComprimento);
				setElementValue("dimensao" + ":" + i + "." + "nrQuantidade", data[i].nrQuantidade);
				setElementValue("dimensao" + ":" + i + "." + "id", data[i].idDimensao);
				gridDms.insertRow();
			}
		}
		setFocus(document.getElementById("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrAltura"),false);
		setDisabled("fechar",false);
		setDisabled("salvar",false);
	}
</script>
<adsm:window
	service="lms.vendas.cotacaoFreteViaWebDimensoesAction" onPageLoad="myOnPageLoad"
>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01101"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/cotacaoFreteViaWebDimensoes" idProperty="idDimensao" >
		<adsm:section caption="dimensoes" width="65"/>
	</adsm:form>
	<adsm:grid property="dimensao" onRowClick="ignore" idProperty="idDimensao" gridHeight="200"
		selectionMode="none" showRowIndex="false" autoAddNew="false" scrollBars="vertical" onValidate="validaDimensao"
		showGotoBox="false" showPagging="false" showTotalPageCount="false" autoSearch="false">
		<adsm:editColumn dataType="integer" unit="cm" property="nrAltura" title="altura" 
			field="textbox" width="80" maxLength="8" minValue="1"/>
		<adsm:editColumn dataType="integer" unit="cm" property="nrLargura" title="largura" 
			field="textbox" width="90" maxLength="8" minValue="1"/>
		<adsm:editColumn dataType="integer" unit="cm" property="nrComprimento" title="comprimento" 
			field="textbox" width="130" maxLength="8" minValue="1"/>
		<adsm:editColumn dataType="integer" property="nrQuantidade" title="quantidade" 
			field="textbox" width="100" maxLength="5" minValue="1"/>
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="salvar" onclick="storeDimensoes();" 
				buttonType="salvarButton" disabled="false"/>
			<adsm:button caption="fechar" id="fechar" onclick="self.close();" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function ignore() {
		return false;
	}

	function validaDimensao(rowIndex, columnName, objCell) {
		var gridDms = document.getElementById("dimensao.dataTable").gridDefinition;
		var al = getElementValue("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrAltura");
		var la = getElementValue("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrLargura");
		var cp = getElementValue("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrComprimento");
		var qt = getElementValue("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrQuantidade");
		if((al && al != "") && (la && la != "") && (cp && cp != "") && (qt && qt != "")) {
			gridDms.insertRow();
			setFocus(document.getElementById("dimensao" + ":" + (gridDms.currentRowCount - 1) + "." + "nrAltura"),false);	
		}
		return true;	
	}

	function storeDimensoes() {
		if(validaDimensoes() == true)
			storeEditGridScript('lms.vendas.cotacaoFreteViaWebDimensoesAction.storeDimensoes', 'storeDimensoes', document.forms[0], document.forms[1]);
	}

	function validaDimensoes() {
		var gridDms = document.getElementById("dimensao.dataTable").gridDefinition;
		var existeDim = false;
		for(var i = 0; i < gridDms.currentRowCount; i++) {
			var al = getElementValue("dimensao" + ":" + i + "." + "nrAltura");
			var la = getElementValue("dimensao" + ":" + i + "." + "nrLargura");
			var cp = getElementValue("dimensao" + ":" + i + "." + "nrComprimento");
			var qt = getElementValue("dimensao" + ":" + i + "." + "nrQuantidade");
			if((al == "" || la == "" || cp == "" || qt == "") && !(al == "" && la == "" && cp == "" && qt == "")) { 
				alertI18nMessage("LMS-01101");
				return false;
			} else if(al != "" && la != "" && cp != "" && qt != "")
				existeDim = true;
		}
		if(existeDim == false){
			alertI18nMessage("LMS-01101");
			return false;
		}
		return true;
	}

	function storeDimensoes_cb(data, erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		window.returnValue = data;
		self.close();
	}
//-->
</script>
