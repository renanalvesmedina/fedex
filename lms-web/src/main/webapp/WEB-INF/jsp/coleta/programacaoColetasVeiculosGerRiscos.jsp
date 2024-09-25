<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="programacaoColetasVeiculos" service="lms.coleta.programacaoColetasVeiculosAction" >
	<adsm:form action="/coleta/programacaoColetasVeiculos">

		<adsm:textbox label="numeroColeta" property="manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" dataType="text" 
			  		  size="3" width="35%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.pedidoColeta.nrColeta" dataType="text" size="10" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="meioTransporte" property="meioTransporte.nrFrota" dataType="text" width="35%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador" size="19" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="valorColetado" property="dsSimboloMoeda1" dataType="text" size="6" width="35%" disabled="true" 
					  serializable="false" >
			<adsm:textbox property="valorColeta" dataType="currency" size="19" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="valorNoVeiculo" property="dsSimboloMoeda2" dataType="text" size="6" width="35%" disabled="true" 
					  serializable="false" >
			<adsm:textbox property="valorNoVeiculo" dataType="currency" size="19" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="valorColetar" property="dsSimboloMoeda3" dataType="text" size="6" width="85%" disabled="true" 
					  serializable="false" >
			<adsm:textbox property="valorColetar" dataType="currency" size="19" serializable="false" disabled="true" />
		</adsm:textbox>
		
		<adsm:hidden property="idControleCarga" serializable="false" />
	</adsm:form>


	<adsm:grid property="exigenciasConteudoAtual" idProperty="idExigenciaGerRisco" 
			   selectionMode="none" gridHeight="90" unique="false" title="exigenciasConteudoAtual" 
			   scrollBars="vertical" showPagging="false" autoSearch="false"
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedExigenciasConteudoAtual"
			   onRowClick="exigenciasConteudoAtual_OnClick"
			   >
		<adsm:gridColumn title="exigencia" 	property="dsTipoExigenciaGerRisco" width="90%" align="left" />
		<adsm:gridColumn title="detalhe" 	property="detalhe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosRiscoDetalhe.do?cmd=main" popupDimension="550,220" width="10%" align="center" linkIdProperty="idExigenciaGerRisco"/>
	</adsm:grid>

	<adsm:grid property="exigenciasPrevistas" idProperty="idExigenciaGerRisco" 
			   selectionMode="none" gridHeight="90" unique="false" title="exigenciasPrevistas"
			   scrollBars="vertical" showPagging="false" autoSearch="false"
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedExigenciasPrevistas"
			   onRowClick="exigenciasPrevistas_OnClick"
			   >
		<adsm:gridColumn title="exigencia" 	property="dsTipoExigenciaGerRisco" width="90%" align="left" />
		<adsm:gridColumn title="detalhe" 	property="detalhe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosRiscoDetalhe.do?cmd=main" popupDimension="550,220" width="10%" align="center" linkIdProperty="idExigenciaGerRisco"/>

	</adsm:grid>
	<adsm:buttonBar>
	</adsm:buttonBar>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		povoaDadosMaster();
	}
}

function povoaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDetPesq = tabGroup.getTab("veiculo");
    var filialColeta = tabDetPesq.getFormProperty("filialByIdFilialResponsavel.sgFilial");
    var numeroColeta = tabDetPesq.getFormProperty("nrColeta");

    var tabDetCad = tabGroup.getTab("cad");
    var nrFrota = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrFrota");
    var nrIdentificador = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrIdentificador");
    var idControleCarga = tabDetCad.getFormProperty("idControleCarga");

    setElementValue("manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" , filialColeta);
    setElementValue("manifestoColeta.pedidoColeta.nrColeta" , numeroColeta);
    setElementValue("meioTransporte.nrFrota" , nrFrota);
    setElementValue("meioTransporte.nrIdentificador" , nrIdentificador);
    setElementValue("idControleCarga" , idControleCarga);

    buscarControleCarga();
    povoaGrids();
}


function buscarControleCarga() {
	var sdo = createServiceDataObject("lms.coleta.programacaoColetasVeiculosAction.findByIdControleCarga", "resultado_buscarControleCarga", 
		{idControleCarga:getElementValue('idControleCarga')});
   	xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa a combo "Descrição não conformidade".
 */
function resultado_buscarControleCarga_cb(data, error){
	if (error != undefined) {
		alert(error);
		return false;
	}

	var valorColetado = getNestedBeanPropertyValue(data,"vlColetado");
	if (valorColetado != undefined && valorColetado != "") {
		setElementValue(document.getElementById("valorColeta"), setFormat(document.getElementById("valorColeta"), valorColetado));
    	setElementValue("dsSimboloMoeda1", getNestedBeanPropertyValue(data,"siglaSimbolo"));
    }
    else {
    	resetValue("valorColeta");
    	resetValue("dsSimboloMoeda1");
    }

	var valorNoVeiculo = getNestedBeanPropertyValue(data,"vlTotalFrota");
	if (valorNoVeiculo != undefined && valorNoVeiculo != "") {
		setElementValue(document.getElementById("valorNoVeiculo"), setFormat(document.getElementById("valorNoVeiculo"), valorNoVeiculo));
    	setElementValue("dsSimboloMoeda2", getNestedBeanPropertyValue(data,"siglaSimbolo"));
    }
    else {
    	resetValue("valorNoVeiculo");
    	resetValue("dsSimboloMoeda2");
    }

	var valorColetar = getNestedBeanPropertyValue(data,"vlAColetar");
	if (valorColetar != undefined && valorColetar != "") {
		setElementValue(document.getElementById("valorColetar"), setFormat(document.getElementById("valorColetar"), valorColetar));
    	setElementValue("dsSimboloMoeda3", getNestedBeanPropertyValue(data,"siglaSimbolo"));
    }
    else {
    	resetValue("valorColetar");
    	resetValue("dsSimboloMoeda3");
    }
}


function povoaGrids() {
	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue('idControleCarga'));
	exigenciasConteudoAtualGridDef.executeSearch(filtro);
	exigenciasPrevistasGridDef.executeSearch(filtro);
	return false;
}

function exigenciasConteudoAtual_OnClick(id) {
	return false;
}

function exigenciasPrevistas_OnClick(id) {
	return false;
}
</script>