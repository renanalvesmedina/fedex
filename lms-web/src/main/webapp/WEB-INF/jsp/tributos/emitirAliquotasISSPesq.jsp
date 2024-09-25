<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	function aliquotaIssPageLoad(){
		onPageLoad();
		setDisplay("dia1", false);
	}
</script>
<adsm:window service="lms.tributos.emitirAliquotasISSAction" onPageLoad="aliquotaIssPageLoad" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/tributos/emitirAliquotasISS">

		<adsm:i18nLabels>
			<adsm:include key="LMS-27041" />
			<adsm:include key="LMS-27042" />
			<adsm:include key="LMS-27013" />
			<adsm:include key="LMS-27043" />
			<adsm:include key="diaEntrega" />
		</adsm:i18nLabels>

		<adsm:hidden property="nomeMunicipio" serializable="true"/>

		<adsm:lookup 
			label="municipio" 
			labelWidth="18%" 
			property="municipio" 
			idProperty="idMunicipio" 
			dataType="text" 
			criteriaProperty="nmMunicipio"
			service="lms.tributos.emitirAliquotasISSAction.findLookupMunicipio" 
			size="35" 
			maxLength="60" 
			action="/municipios/manterMunicipios" 
			exactMatch="false" 
			minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping modelProperty="nmMunicipio" formProperty="nomeMunicipio"/>
		</adsm:lookup>
		
		<adsm:range label="vigencia" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="JTDate" property="dtVigencia"/>
		</adsm:range>
		
		<adsm:hidden property="dsPeriodicidade" serializable="true"/>

        <adsm:combobox 
        	labelWidth="18%"
        	width="32%"
	        property="tpPeriodicidade" 
	        label="periodicidade" 
	        domain="DM_PERIODICIDADE_OBRIGACOES_MUNICIPIO" 
	        onchange="return tpPeriodicidade_change()"/>
	    
	    <adsm:hidden property="dsDiaEntrega1" serializable="true" />
		<adsm:hidden property="dsDiaEntrega2" serializable="true" />
		<adsm:hidden property="dsDiaEntrega3" serializable="true" />		
		
		<adsm:textbox label="diaEntrega" labelWidth="15%" width="35%" dataType="integer" property="dia1" size="2" maxLength="2" style="display : none;">
			<adsm:textbox property="dia2" dataType="integer" serializable="false" size="2" maxLength="2" style="display : none;" />
			<adsm:textbox property="dia3" dataType="integer" serializable="false" size="2" maxLength="2" style="display : none;"/>

	        <adsm:hidden property="dsDiaSemana" serializable="true"/>
	        <adsm:combobox 
		        property="tpDiasSemana"
		        domain="DM_DIAS_SEMANA"
		        style="display: none"
		        onchange="diaSemanaOnChange(this);"
	        />
  			
  			<adsm:textbox property="data1" dataType="JTDate" serializable="false" mask="dd/MM" picker="false" style="display : none;"/>
  			<adsm:textbox property="data2" dataType="JTDate" serializable="false" mask="dd/MM" picker="false" style="display : none;"/>
		</adsm:textbox>

        
	    <adsm:textbox label="diaRecolhimento"  dataType="integer" property="dtDiaRecolhimento" size="10" maxLength="2" labelWidth="18%" maxValue="31" width="82%"/>

		<adsm:hidden property="dsFormaPagamento" serializable="true"/>
		<adsm:combobox property="tpFormaPagamento" label="formaPagamento" domain="DM_FORMA_PGTO_ISS" labelWidth="18%" width="32%" onchange="formaPagamentoOnChange(this)"/>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" labelWidth="18%" width="85%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" onclick="testaPeriodicidade(this)" buttonType="storeButton" disabled="false"/>
			<adsm:button caption="limpar" buttonType="resetButton" onclick="limparClick()"/>
		</adsm:buttonBar>

		<script>
			function alert_LMS_27041(){	alert(i18NLabel.getLabel("LMS-27041")); }
			function alert_LMS_27042(){	alert(i18NLabel.getLabel("LMS-27042")); }
			function alert_LMS_27013(){	alert(i18NLabel.getLabel("LMS-27013")); }
			function alert_LMS_27043(){	alert(i18NLabel.getLabel("LMS-27043")); }
			var labelEntrega = i18NLabel.getLabel("diaEntrega");
		</script>

	</adsm:form>
</adsm:window>

<script>

	function myOnPageLoadCallBack_cb(data,erro){
	
		onPageLoad_cb(data,erro);
		setElementValue("tpFormatoRelatorio","pdf");
	
	}

	function formaPagamentoOnChange(elemento){
		
		if( getElementValue("tpFormaPagamento") != "" ){
		
			var formaPagto = document.getElementById("tpFormaPagamento");		
			setElementValue("dsFormaPagamento", formaPagto[formaPagto.selectedIndex].text );
			
		} 		
		
	}

	function diaSemanaOnChange(elemento){
		
		if( getElementValue("tpDiasSemana") != "" ){
		
			var diaSemana = document.getElementById("tpDiasSemana");		
			setElementValue("dsDiaSemana", diaSemana[diaSemana.selectedIndex].text );
			
		} 
	}

	function testaPeriodicidade(elemento) {	
		document.getElementById("dia1").label  = labelEntrega;
		document.getElementById("dia2").label  = labelEntrega;
		document.getElementById("dia3").label  = labelEntrega;
		document.getElementById("data1").label = labelEntrega;
		document.getElementById("data2").label = labelEntrega;
		document.getElementById("tpDiasSemana").label = labelEntrega;
		
		if(getElementValue("dia1") == ""){
			setElementValue("dsDiaEntrega1", "");
		}
		if(getElementValue("dia2") == ""){
			setElementValue("dsDiaEntrega2", "");
		}
		if(getElementValue("dia3") == ""){
			setElementValue("dsDiaEntrega3", "");
		}
		if (getElementValue("tpPeriodicidade") != "") {
			var periodicidade = document.getElementById("tpPeriodicidade");		
			setElementValue("dsPeriodicidade", periodicidade[periodicidade.selectedIndex].text);
		} else {
			setElementValue("dsPeriodicidade","");
		}
		
		if (getElementValue("tpDiasSemana") == ""){
			setElementValue("dsDiaSemana", "");
		}
		
		if( getElementValue("dsFormaPagamento") == "" ){
			setElementValue("dsFormaPagamento","");
		}

		reportButtonScript('lms.tributos.emitirAliquotasISSAction', 'openPdf', elemento.form);
	
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


	function tpPeriodicidade_change(){
		var tpPeriodicidade = getElementValue("tpPeriodicidade");
		
		/** Limpa os conteúdo dos campos */
		setElementValue("dia1", "");
		setElementValue("dia2", "");
		setElementValue("dia3", "");
		setElementValue("dsDiaEntrega1", "");
		setElementValue("dsDiaEntrega2", "");
		setElementValue("dsDiaEntrega3", "");
		
		switch (tpPeriodicidade){
	    	case "E" : // Decendial
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("dia1").style.display = 'inline'; 
				//document.getElementById("dia1").required = "true";
				document.getElementById("dia2").style.display = 'inline';
				//document.getElementById("dia2").required = "true";
				document.getElementById("dia3").style.display = 'inline';
				//document.getElementById("dia3").required = "true";
				break;
	    	case "A" : // Anual
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("data1").style.display = 'inline';
				//document.getElementById("data1").required = "true";
				break;
	    	case "D" : // Diaria
				setDisplay("dia1", false);
				desabilitaTodosPeriodicidade();
				break;
			case "Q" : // Quinzenal
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("dia1").style.display = 'inline'; 
				//document.getElementById("dia1").required = "true";
				document.getElementById("dia2").style.display = 'inline';
				//document.getElementById("dia2").required = "true";
				break;	
	    	case "M" : // Mensal
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("dia1").style.display = 'inline';
				//document.getElementById("dia1").required = "true";
				break;
	    	case "S" : // Semanal
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("tpDiasSemana").style.display = 'inline';
				//document.getElementById("tpDiasSemana").required = "true";
				break;
	    	case "C" : // Semestral
				setDisplay("dia1", true);
				desabilitaTodosPeriodicidade();
				document.getElementById("data1").style.display = 'inline';
				//document.getElementById("data1").required = "true";
				document.getElementById("data2").style.display = 'inline';
				//document.getElementById("data2").required = "true";
				break;
			default :  // Selecione...
				setDisplay("dia1", false);
				desabilitaTodosPeriodicidade();
				break;
		}
	}
	
	function validateTab() {
		return validateTabScript(document.forms) && validaPeridiodicidade();
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
	
	}
	
	function validaPeridiodicidade() {
		var tpPeriodicidade = getElementValue("tpPeriodicidade");
		
		var dia1 = getElementValue("dia1");
		var dia2 = getElementValue("dia2");
		var dia3 = getElementValue("dia3");
	
		var data1 = getElementValue("data1");
		var data2 = getElementValue("data2");
		
		switch (tpPeriodicidade){
	    	case "E" : // Decendial
	    		if( dia1 != "" || dia2 != "" || dia3 != "" ){
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
				}
				break;
	    	case "A" : // Anual
	    		if( data1 != "" ){
		    		setElementValue("dsDiaEntrega1", getElement("data1").value);
		    	}
				break;
	    	case "D" : // Diaria
				break;    		
			case "S" : // Semanal
				var selObj = document.getElementById("tpDiasSemana");
				//selObj.required = "true";
				
				if( selObj != undefined && selObj != null && selObj.selectedIndex != 0 ){				
					setElementValue("dsDiaEntrega1", selObj.options[selObj.selectedIndex].value );
				}
					
				break;
			case "Q" : // Quinzenal
				if( dia1 != "" || dia2 != "" ){
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
				}
				break;	
			case "M" : // Mensal
				if( dia1 != "" ){
					if (!(dia1 >= 1 && dia1 <= 31)){
						alert_LMS_27013();
						if( !(dia1 >= 1 && dia1 <= 31) ){
							setFocus(document.getElementById("dia1"));
						}
						return false;
					}else{
						setElementValue("dsDiaEntrega1", dia1);
					}
				}					
				break;
	    	case "C" : // Semestral
				data1 = stringToDate(getElement("data1").value, getElement("data1").mask);
				data2 = stringToDate(getElement("data2").value, getElement("data2").mask);
				if (data1 != 0 && data2 != 0) {
					var mes1 = data1.getMonth() + 1;
					var mes2 = data2.getMonth() + 1;
		
					if (!((mes1 >= 1 && mes1 <= 6) && (mes2 >= 7 && mes2 <= 12))) {
						alert_LMS_27043();
						if (!(mes1 >= 1 && mes1 <= 6)) {
							setFocus("data1");
						} else if (!(mes2 >= 7 && mes2 <= 12)) {
							setFocus("data2");
						}
						return false;
					} else {
						setElementValue("dsDiaEntrega1", getElement("data1").value);
						setElementValue("dsDiaEntrega2", getElement("data2").value);
					}
				}
				break;
	    	default :  // Selecione...
				break;
		}
		return true;
	}
	
	function limparClick(){
		newButtonScript();
		tpPeriodicidade_change();
		setElementValue("tpFormatoRelatorio","pdf");
	}
</script>
