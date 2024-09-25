<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="eventos" service="lms.freteCarreteiroColetaEntrega.consultarEventosNotaCreditoAction" 
				onPageLoadCallBack="retornoCarregaPagina">
			 
	<adsm:form action="/freteCarreteiroColetaEntrega/consultarEventosNotaCredito" idProperty="idNotaCredito" 
			   service="lms.freteCarreteiroColetaEntrega.manterNotasCreditoAction.findById" >
			   
	   <adsm:textbox dataType="text" label="numeroNota" property="sgFilial"
			  size="3" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="nrNotaCredito" size="9" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>

	</adsm:form>
	
	<adsm:grid idProperty="idEventosNotaCredito" property="eventosNotaCredito" 
			   selectionMode="none" gridHeight="340" title="eventos" unique="true" rows="17" autoSearch="false"
			   defaultOrder="dhEvento:asc"
			   onRowClick="grid_OnClick" 
			   onDataLoadCallBack="retornoGrid" 
			   service="lms.fretecarreteirocoletaentrega.consultarEventosNotaCreditoAction.findPaginated"
			   rowCountService="lms.fretecarreteirocoletaentrega.consultarEventosNotaCreditoAction.getRowCount"
			   >
		
		<adsm:gridColumn title="evento"	property="tpOrigemEvento" isDomain="true" width="20%" />
		<adsm:gridColumn title="complemento" property="tpComplementoEvento" isDomain="true" width="15%" />
		<adsm:gridColumn title="dataHora" 	property="dhEvento" dataType="JTDateTimeZone" align="center" width="25%" />
		<adsm:gridColumn title="usuario" 	property="nmUsuario" width="40%" />
		
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>

<script>

function grid_OnClick(id) {
	return false;
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		inicializaTela();
		povoaGrid(getElementValue("idNotaCredito"));
	}
}

function retornoGrid_cb(data, error) {
	if (error == undefined)
		setFocus(document.getElementById("botaoFechar"), true, true);
}

function povoaDadosMaster() {
	if(dialogArguments.window.document.getElementById('idNotaCredito') != null && dialogArguments.window.document.getElementById('idNotaCredito') != undefined 
			&& dialogArguments.window.document.getElementById('idNotaCredito') != "" ){
		setElementValue("idNotaCredito", dialogArguments.window.document.getElementById('idNotaCredito').value);
		setElementValue("sgFilial", dialogArguments.window.document.getElementById('sgFilial').value);
		setElementValue("nrNotaCredito", dialogArguments.window.document.getElementById('nrNotaCredito').value);	
	}else {
		setMasterLink(document, true);
		
		if( getElementValue('idNotaCredito') != null && getElementValue('idNotaCredito') != "" && getElementValue('idNotaCredito') != undefined ){
			findNotaCredito();
		}
	}
	
}

function findNotaCredito() {
    var data = new Array();
    setNestedBeanPropertyValue(data, "idNotaCredito", getElementValue('idNotaCredito'));
    var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.consultarEventosNotaCreditoAction.findNotaCreditoById", "findNotaCredito", data);    
    xmit({serviceDataObjects:[sdo]});
}

function findNotaCredito_cb(data, errorMsg) {
	
	if(errorMsg == null || errorMsg == "" || errorMsg == undefined ){
		var sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
	    var nrNotaCredito = getNestedBeanPropertyValue(data,"nrNotaCredito");
		if (sgFilial != null && sgFilial != undefined && nrNotaCredito != null && nrNotaCredito != undefined){
		    setElementValue("sgFilial",sgFilial);
			setElementValue("nrNotaCredito", nrNotaCredito);
			format(document.getElementById("nrNotaCredito"))
		}
	}
}

function povoaGrid(idNotaCredito) {
	
	if(idNotaCredito != null && idNotaCredito != undefined){
		var filtro = new Array();
	    setNestedBeanPropertyValue(filtro, "notaCredito.idNotaCredito", idNotaCredito);
	    eventosNotaCreditoGridDef.executeSearch(filtro, true);
	}
	
    return false;
}

function inicializaTela() {
	resetValue(this.document);
	povoaDadosMaster();
}

</script>