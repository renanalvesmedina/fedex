<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.manterValoresCruzeAction">
	<adsm:form action="/tabelaPrecos/manterValoresCruze" idProperty="idValorCruze" onDataLoadCallBack="dataLoad">


		<adsm:textbox dataType="weight" property="nrFaixaInicialPeso" unit="kg" label="faixaInicialPeso" mask="##,##0.000" width="25%" required="true"/>
		<adsm:textbox dataType="weight" property="nrFaixaFinalPeso" unit="kg" label="faixaFinalPeso" mask="##,##0.000" required="true"/> 

		<adsm:complement label="valorCruze" required="true" width="80%" separator="branco">
			<adsm:combobox
				property="moeda.idMoeda"
				service="lms.tabelaprecos.manterValoresCruzeAction.findMoedaCombo"
				optionLabelProperty="dsSimbolo"
				optionProperty="idMoeda"
				boxWidth="85">
				<adsm:textbox
					dataType="currency"
					property="vlCruze"
					serializable="true"
					mask="###,##0.00"
					size="10"/>
			</adsm:combobox>
		</adsm:complement>

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/> 
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" id="salvar"/>
			<adsm:newButton id="limpar"/>
			<adsm:removeButton id="excluir" service="lms.tabelaprecos.manterValoresCruzeAction.removeById"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	document.getElementById("nrFaixaInicialPeso").smallerThan = "nrFaixaFinalPeso";
	document.getElementById("nrFaixaFinalPeso").biggerThan = "nrFaixaInicialPeso";
	
	document.getElementById("dtVigenciaInicial").smallerThan = "dtVigenciaFinal";
	document.getElementById("dtVigenciaFinal").biggerThan = "dtVigenciaInicial";

	var save = false;

function initWindow(eventObj) {
	if (eventObj.name != "gridRow_click"){
		findDadosSessao_cb(null, null);
	}
	
    if (eventObj.name == "newButton_click" || eventObj.name == "tab_click"){
		save = false;
		setFocus("nrFaixaInicialPeso");
		findDadosSessao_cb(null, null);
		var data = {habilita:"0"};
    	validateVigencia_cb(data);
		setDisabled("excluir",true);
	}
}

function afterStore_cb(data,erro) {
	store_cb(data,erro);
	if(!erro) {
		save = true;
		validateVigencia();
	}
}

function dataLoad_cb(data,erro) {
	onDataLoad_cb(data,erro);
	save = false;
	validateVigencia();
}

function validateVigencia() {
		var dtVigenciaInicial = getElementValue('dtVigenciaInicial');
		var dtVigenciaFinal = getElementValue('dtVigenciaFinal');
		
		var sdo = createServiceDataObject("lms.tabelaprecos.manterValoresCruzeAction.validateVigencia", "validateVigencia", {dtVigenciaInicial:dtVigenciaInicial, dtVigenciaFinal:dtVigenciaFinal});
		xmit({serviceDataObjects:[sdo]});
}

function validateVigencia_cb(data,erro) {
	if( erro != undefined ){
		alert(erro);
	}

	if(data.habilita == "0") {
		setDisabled("nrFaixaInicialPeso",false);
		setDisabled("nrFaixaFinalPeso",false);
		setDisabled("moeda.idMoeda",false);
		setDisabled("vlCruze",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);

		setDisabled("salvar",false);
		setDisabled("limpar",false);
		setDisabled("excluir",false);
		setFocus("nrFaixaInicialPeso");
		
	}else if(data.habilita == "1"){
		setDisabled("nrFaixaInicialPeso",true);
		setDisabled("nrFaixaFinalPeso",true);
		setDisabled("moeda.idMoeda",true);
		setDisabled("vlCruze",true);
		setDisabled("dtVigenciaInicial",true);
		setDisabled("dtVigenciaFinal",false);

		setDisabled("salvar",false);
		setDisabled("limpar",false);
		setDisabled("excluir",true);
		setFocus("dtVigenciaFinal");
	}else{
		setDisabled("nrFaixaInicialPeso",true);
		setDisabled("nrFaixaFinalPeso",true);
		setDisabled("moeda.idMoeda",true);
		setDisabled("vlCruze",true);
		setDisabled("dtVigenciaInicial",true);
		setDisabled("dtVigenciaFinal",true);
		
		setDisabled("salvar",true);
		setDisabled("limpar",false);
		setDisabled("excluir",true);
		setFocus("limpar",false);
	}
	if(save == true) {
		setFocus("limpar",false);
	}
}

function findDadosSessao_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return false;
	}
	var sdo = createServiceDataObject("lms.tabelaprecos.manterValoresCruzeAction.findDadosSessao", "ajustaDadosSessao");
	xmit({serviceDataObjects:[sdo]});
}

function ajustaDadosSessao_cb(data, errorMsg, errorKey) {
	if(errorMsg) {
		alert(errorMsg);
		return;
	}
	var idMoeda;
	if(data) {
		idMoeda = data.idMoeda;
	}
	setElementValue("moeda.idMoeda", idMoeda);
}


</script>

