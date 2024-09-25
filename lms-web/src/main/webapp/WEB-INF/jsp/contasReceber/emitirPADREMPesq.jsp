<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/contasReceber/emitirPADREM">

		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="ultimaPadrem"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialEmissora" 
					 size="3" 
					 maxLength="3" 
					 width="40%"
					 labelWidth="20%"
					 onDataLoadCallBack="lookupFilialDataLoad"
					 required="true"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="45" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="monthYear" property="dtCompetencia" label="competencia" onchange="onChangeCompetencia()" labelWidth="20%" width="20%" required="true"/>

 		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="20%"
    				   required="true"
    				   defaultValue="pdf"
    				   width="40%"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:checkbox 	property="blGerarIntegracao"
						label="gerarArquivoTxtParaMatriz"
						labelWidth="20%"
						width="20%"/>		

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirPADREMAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>

    function initWindow(ev){ 
		if (ev.name == "tab_load" || ev.name == "cleanButton_click") {
			setaDefault();		
		}
	}

	function setaDefault() {
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirPADREMAction.findDefaults",
			"setDefaults", 
			new Array()));

        xmit(false);	
	}
	
	function onChangeCompetencia(){
		var dataAtual = new Date();
		var date = stringToDate(getElementValue("dtCompetencia"),"yyyy-MM-dd");
		if (date != 0){
			var year = date.getYear();
			var month = date.getMonth();
			if ((year == dataAtual.getYear() && month > dataAtual.getMonth()) || year > dataAtual.getYear() ){
				setElementValue("dtCompetencia","");
				alert(erCompetencia);
				return;
			}
		}
		
		disableCheckIntegracao();
		
	}
	
	
	function lookupFilialDataLoad_cb(data,error){
		if (error != undefined){
			alert(error);
		}else{
			filial_sgFilial_exactMatch_cb(data);
			var sdo = createServiceDataObject("lms.contasreceber.emitirPADREMAction.findUltimaPadrem", "findUltimaPadrem", 
					data[0]);
		    xmit({serviceDataObjects:[sdo]});
		}
	}
	
	
	function findUltimaPadrem_cb(data,error){
		if (error != undefined){
			alert(error);
		}else{
			setElementValue('ultimaPadrem',data.ultimaPadrem);
			disableCheckIntegracao();
		}
	}
	
	function setDefaults_cb(data, error) {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
		setElementValue('dtCompetencia',data.dtCompetencia);
		setElementValue('ultimaPadrem',data.ultimaPadrem);
		
		if(data.sgFilial != 'MTZ'){
			setDisabled('filial.idFilial',true);
	}
		disableCheckIntegracao();
	}

	function disableCheckIntegracao(){
		var blDisableCheckIntegracao = false;
		var date = stringToDate(getElementValue("dtCompetencia"),"yyyy-MM-dd");
		if (date != 0){
			var year = date.getYear();
			var month = date.getMonth();
			if (getElementValue("ultimaPadrem") != undefined){
				var ultimaPadrem = stringToDate(getElementValue("ultimaPadrem"),"yyyy-MM-dd");
				if (ultimaPadrem != 0){
					blDisableCheckIntegracao = (year == ultimaPadrem.getYear() && month < ultimaPadrem.getMonth()) || year < ultimaPadrem.getYear();
				}
			}		
		}		
		setDisabled("blGerarIntegracao",blDisableCheckIntegracao);
		if (blDisableCheckIntegracao == true){
			setElementValue("blGerarIntegracao",false);
		}
	}

</script>