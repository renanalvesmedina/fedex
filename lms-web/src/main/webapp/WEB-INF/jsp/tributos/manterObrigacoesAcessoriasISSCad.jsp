<script type="text/javascript">
<!--

  	function myOnDataLoad_cb(data, error){
    	onDataLoad_cb(data, error);
		tpPeriodicidade_change();
		carregaPeridiodicidade();
	}

//-->
</script>


<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tributos.manterObrigacoesAcessoriasISSAction"
>
	<adsm:form 
		action="/tributos/manterObrigacoesAcessoriasISS"
		idProperty="idObrigacaoAcessoriaIssMun"
		onDataLoadCallBack="myOnDataLoad"
	>

        <adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:lookup 
			service="lms.tributos.manterObrigacoesAcessoriasISSAction.findLookupMunicipio" 
			action="/municipios/manterMunicipios" 
			property="municipio" 
			idProperty="idMunicipio" 
			criteriaProperty="nmMunicipio" 

			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			dataType="text" 

			width="85%" 
			maxLength="60" 
			size="30"
			required="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:textbox 
			label="descricao" 
			dataType="text" 
			property="dsObrigacaoAcessoriaIssMun" 
			size="80" 
			maxLength="60" 
			width="85%" 
			required="true"
		/>

        <adsm:combobox 
        	labelWidth="15%"
        	width="35%"
	        property="tpPeriodicidade" 
	        label="periodicidade" 
	        domain="DM_PERIODICIDADE_OBRIGACOES_MUNICIPIO" 
	        onchange="return tpPeriodicidade_change()"
	        required="true"
        />

		<adsm:hidden property="dsDiaEntrega1" serializable="true" />
		<adsm:hidden property="dsDiaEntrega2" serializable="true" />
		<adsm:hidden property="dsDiaEntrega3" serializable="true" />

		<adsm:textbox label="diaEntrega" labelWidth="15%" width="35%" dataType="integer" property="dia1" size="4" maxLength="2" maxValue="31" style="display : none;">
			<adsm:textbox property="dia2" dataType="integer" serializable="false" size="4" maxLength="2" maxValue="31" style="display : none;" />
			<adsm:textbox property="dia3" dataType="integer" serializable="false" size="4" maxLength="2" maxValue="31" style="display : none;"/>

	        <adsm:combobox 
		        property="tpDiasSemana"
		        domain="DM_DIAS_SEMANA"
		        style="display: none"
	        />
  			<adsm:textbox property="data1" dataType="JTDate" serializable="false" mask="dd/MM" picker="false" style="display : none;"/>
  			<adsm:textbox property="data2" dataType="JTDate" serializable="false" mask="dd/MM" picker="false" style="display : none;"/>

		</adsm:textbox>


		<adsm:textarea 
			label="observacao" 
			width="85%" 
			columns="60" 
			rows="3" 
			maxLength="500" 
			property="obObrigacaoAcessoriaIssMun" 
		/>

		<adsm:buttonBar>
			<adsm:button caption="salvar" disabled="false" onclick="myStore(this)" id="storeButton" buttonType="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

<script>
	function alert_LMS_27041(){	alert('<adsm:label key="LMS-27041"/>');	}
	function alert_LMS_27042(){	alert('<adsm:label key="LMS-27042"/>');	}
	function alert_LMS_27013(){	alert('<adsm:label key="LMS-27013"/>');	}
	function alert_LMS_27043(){	alert('<adsm:label key="LMS-27043"/>');	}
	function alert_LMS_27042(){	alert('<adsm:label key="LMS-27042"/>');	}
	
	var labelEntrega = '<adsm:label key="diaEntrega"/>';

</script>
		
	</adsm:form>
</adsm:window>

<script>

function myStore(elemento){

	document.getElementById("dia2").label = labelEntrega;
	document.getElementById("dia3").label = labelEntrega;
	document.getElementById("tpDiasSemana").label = labelEntrega;
	document.getElementById("data1").label = labelEntrega;
	document.getElementById("data2").label = labelEntrega;


	storeButtonScript('lms.tributos.manterObrigacoesAcessoriasISSAction.store', 'store', elemento.form);
	
}

function validateTab() {
	return validateTabScript(document.forms) && validaPeridiodicidade();
}

function initWindow(eventObj) {
	if (eventObj.name == "tab_load" || eventObj.name == "tab_click"){
		setDisplay("dia1", false);
	}else
	if (eventObj.name == "gridRow_click")
		tpPeriodicidade_change();
	
	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton" ) {
	  setDisplay("dia1", false);
	  desabilitaTodosPeriodicidade();
	}	
}

function setDisplay(obj, value, documento) {
	if (obj != '[object]'){
		if (documento == undefined) {
			documento = this.document;
		}
		obj = documento.getElementById(obj);
	}
	
	if (obj == undefined){
		alert('Objeto não encontrado!');
		return;
	}	

	mylabel = documento.getElementById("spanlbl_" + obj.id);
	if (value) {
		obj.parentNode.style.display = "inline";
		if (mylabel != undefined) mylabel.parentNode.style.display = "inline";
	} else {
		obj.parentNode.style.display = "none";
		if (mylabel != undefined) mylabel.parentNode.style.display = "none";
	}
}


function carregaPeridiodicidade(){
	var tpPeriodicidade = getElementValue("tpPeriodicidade");
	var dsDiaEntrega1 = getElementValue("dsDiaEntrega1");
	var dsDiaEntrega2 = getElementValue("dsDiaEntrega2");
	var dsDiaEntrega3 = getElementValue("dsDiaEntrega3");
	
	switch (tpPeriodicidade){
    	case "E" : // Decendial
			setElementValue("dia1", dsDiaEntrega1);
			setElementValue("dia2", dsDiaEntrega2);
			setElementValue("dia3", dsDiaEntrega3);
			break;
    	case "A" : // Anual
    		setElementValue("data1", dsDiaEntrega1);
			break;
    	case "D" : // Diaria
			break;    		
		case "S" : // Semanal
			setElementValue("tpDiasSemana", dsDiaEntrega1);
			break;
		case "Q" : // Quinzenal
			setElementValue("dia1", dsDiaEntrega1);
			setElementValue("dia2", dsDiaEntrega2);
			break;
		case "M" : // Mensal
			setElementValue("dia1", dsDiaEntrega1);
			break;
    	case "C" : // Semestral
    		setElementValue("data1", dsDiaEntrega1);
    		setElementValue("data2", dsDiaEntrega2);
			break;
    	default :  // Selecione...
			break;
	}
}


function validaPeridiodicidade(){
	var tpPeriodicidade = getElementValue("tpPeriodicidade");
	var dia1 = document.getElementById("dia1").value;
	var dia2 = document.getElementById("dia2").value;
	var dia3 = document.getElementById("dia3").value;

	var data1 = document.getElementById("data1").value;
	var data2 = document.getElementById("data2").value;
	
	switch (tpPeriodicidade){
    	case "E" : // Decendial
			if (!( (dia1 >= 1 && dia1 <= 10) && (dia2 >= 11 && dia2 <= 20)  && (dia3 >= 21 && dia3 <= 31) )){
				alert_LMS_27041();
				if( !(dia1 >= 1 && dia1 <= 10) ){
					setFocus(document.getElementById("dia1"));
				} else {
					if( !(dia2 >= 11 && dia2 <= 20) ){
						setFocus(document.getElementById("dia2"));
					} else {
						if( !(dia3 >= 21 && dia3 <= 31) ){
							setFocus(document.getElementById("dia3"));								
						}
					}
				}
				return false;
			}else{
				setElementValue("dsDiaEntrega1", dia1);
				setElementValue("dsDiaEntrega2", dia2);
				setElementValue("dsDiaEntrega3", dia3);
			}
			break;
    	case "A" : // Anual
    	//	alert(data1);
    		setElementValue("dsDiaEntrega1", data1);
			break;
    	case "D" : // Diaria
			break;    		
		case "S" : // Semanal
			var selObj = document.getElementById("tpDiasSemana");
		//	alert(getElementValue("tpDiasSemana"));
			setElementValue("dsDiaEntrega1", selObj.options[selObj.selectedIndex].value );
			break;
		case "Q" : // Quinzenal
			if (!( (dia1 >= 1 && dia1 <= 15) && (dia2 >= 16 && dia2 <= 31) )){				
				alert_LMS_27042();
				if( !(dia1 >= 1 && dia1 <= 15) ){
					setFocus(document.getElementById("dia1"));
				} else {
					if( !(dia2 >= 16 && dia2 <= 31) ){
						setFocus(document.getElementById("dia2"));
					}
				}
				return false;
				
			}else{
				setElementValue("dsDiaEntrega1", dia1);
				setElementValue("dsDiaEntrega2", dia2);
			}
			break;	
		case "M" : // Mensal
			if (!(dia1 >= 1 && dia1 <= 31)){
				alert_LMS_27013();
				if( !(dia1 >= 1 && dia1 <= 31) ){
					setFocus(document.getElementById("dia1"));
				}
				return false;
			}else{
				setElementValue("dsDiaEntrega1", dia1);
			}
			break;
    	case "C" : // Semestral
			var mask = document.getElementById("data1").mask;
			data1 = stringToDate(data1, mask);
			var mes1 = data1.getMonth()+1;

			mask = document.getElementById("data2").mask;
			data2 = stringToDate(data2, mask);
			var mes2 = data2.getMonth()+1;

			if ( !((mes1 >= 1 && mes1 <= 6 ) && (mes2 >= 7 && mes2 <= 12)) ){
				alert_LMS_27043();
				if( !(mes1 >= 1 && mes1 <= 6) ){
					setFocus(document.getElementById("data1"));
				} else {
					if( !(mes2 >= 7 && mes2 <= 12) ){
						setFocus(document.getElementById("data2"));
					}
				}
				return false;
			}else{
				setElementValue("dsDiaEntrega1", document.getElementById("data1").value);
				setElementValue("dsDiaEntrega2", document.getElementById("data2").value);
			}
			break;
    	default :  // Selecione...
			break;
	}
	return true;
}


function desabilitaTodosPeriodicidade(){
	document.getElementById("dia1").style.display = 'none';
	document.getElementById("dia1").required = "false";
	setElementValue("dia1", "");
	
	document.getElementById("dia2").style.display = 'none';
	document.getElementById("dia2").required = "false";
	setElementValue("dia2", "");
	
	document.getElementById("dia3").style.display = 'none';
	document.getElementById("dia3").required = "false";
	setElementValue("dia3", "");
	
	document.getElementById("tpDiasSemana").style.display = 'none';
	document.getElementById("tpDiasSemana").required = "false";
	setElementValue("tpDiasSemana", "");
	
	document.getElementById("data1").style.display = 'none';
	document.getElementById("data1").required = "false";
	setElementValue("data1", "");
	
	document.getElementById("data2").style.display = 'none';
	document.getElementById("data2").required = "false";
	setElementValue("data2", "");

//	document.getElementById("data1_picker").style.display = 'none';
//	document.getElementById("data2_picker").style.display = 'none';
}

function tpPeriodicidade_change(){
	var tpPeriodicidade = getElementValue("tpPeriodicidade");

	switch (tpPeriodicidade){
    	case "E" : // Decendial
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();

			document.getElementById("dia1").style.display = 'inline'; 
			document.getElementById("dia1").required = "true";

			document.getElementById("dia2").style.display = 'inline';
			document.getElementById("dia2").required = "true";

			document.getElementById("dia3").style.display = 'inline';
			document.getElementById("dia3").required = "true";
			break;
    	case "A" : // Anual
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();
			document.getElementById("data1").style.display = 'inline';
			document.getElementById("data1").required = "true";
//			document.getElementById("data1_picker").style.display = 'inline';
			break;
    	case "D" : // Diaria
			setDisplay("dia1", false);
			desabilitaTodosPeriodicidade();
			break;
		case "Q" : // Quinzenal
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();

			document.getElementById("dia1").style.display = 'inline'; 
			document.getElementById("dia1").required = "true";

			document.getElementById("dia2").style.display = 'inline';
			document.getElementById("dia2").required = "true";

			break;	
    	case "M" : // Mensal
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();
			document.getElementById("dia1").style.display = 'inline';
			document.getElementById("dia1").required = "true";
			break;
    	case "S" : // Semanal
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();
			document.getElementById("tpDiasSemana").style.display = 'inline';
			document.getElementById("tpDiasSemana").required = "true";
			break;
    	case "C" : // Semestral
			setDisplay("dia1", true);
			desabilitaTodosPeriodicidade();
			document.getElementById("data1").style.display = 'inline';
			document.getElementById("data1").required = "true";
//			document.getElementById("data1_picker").style.display = 'inline';
			
			document.getElementById("data2").style.display = 'inline';
			document.getElementById("data2").required = "true";
//			document.getElementById("data2_picker").style.display = 'inline';
			break;
		default :  // Selecione...
			setDisplay("dia1", false);
			desabilitaTodosPeriodicidade();
			break;
	}
}
</script>
