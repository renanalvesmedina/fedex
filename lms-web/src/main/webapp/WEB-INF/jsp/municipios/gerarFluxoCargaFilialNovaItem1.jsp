<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.municipios.gerarFluxoCargaFilialNovaAction"
	onPageLoadCallBack="pageLoad">
	<adsm:form action="municipios/gerarFluxoCargaFilialNova"
		idProperty="idFilial"
		service="lms.municipios.gerarFluxoCargaFilialNovaAction.findByIdFilial"
		onDataLoadCallBack="dataLoad">

		<adsm:textbox property="servico.description" label="servico"
			dataType="text" size="42" width="85%" disabled="true"
			serializable="false" />
		<adsm:hidden property="servico.idServico" serializable="true" />

		<adsm:textbox dataType="text"
			property="filialByIdFilialOrigem.siglaNomeFilial"
			label="filialOrigem" size="42" labelWidth="15%" width="35%"
			disabled="true" serializable="false" required="true" />
		<adsm:textbox dataType="text"
			property="filialByIdFilialDestino.siglaNomeFilial"
			label="filialDestino" size="42" labelWidth="17%" width="33%"
			disabled="true" serializable="false" required="true" />

		<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="filialByIdFilialDestino.idFilial" />

		<adsm:hidden property="filialByIdFilialOrigem.sgFilial" />
		<adsm:hidden property="filialByIdFilialDestino.sgFilial" />
		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:hidden property="filialByIdFilialDestino.pessoa.nmFantasia" />
		<adsm:hidden property="dtVigenciaInicial" serializable="true" />
		<adsm:hidden property="dtVigenciaFinal" serializable="true" />

		<adsm:lookup property="filialByIdFilialReembarcadora"
			idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
			onchange="return filialReemChange(this);"
			service="lms.municipios.gerarFluxoCargaFilialNovaAction.findLookupFilial"
			dataType="text" label="filialReembarcadora" size="3"
			action="/municipios/manterFiliais" labelWidth="15%" width="35%"
			minLengthForAutoPopUpSearch="3" onDataLoadCallBack="filialReembLoad"
			onPopupSetValue="filialReembPopup" exactMatch="false"
			disabled="false" labelStyle="vertical-align: center;"
			cellStyle="vertical-align: center;">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialReembarcadora.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilialReembarcadora.pessoa.nmFantasia" size="30"
				disabled="true" />
		</adsm:lookup>

		<adsm:listbox property="fluxoReembarque" size="3" serializable="false"
			labelStyle="vertical-align: center;" optionProperty=""
			optionLabelProperty="fluxo" label="fluxoReembarque" labelWidth="17%"
			width="33%" boxWidth="231" />
		<adsm:textbox dataType="integer" property="nrPrazoView" label="tempo"
			maxLength="3" size="6" labelWidth="15%" width="35%" required="true"
			unit="h" />
		<adsm:textbox dataType="integer" property="nrDistancia"
			label="distancia" maxLength="6" size="6" labelWidth="17%" width="33%"
			required="true" unit="km2" />
		<adsm:multicheckbox texts="dom|seg|ter|qua|qui|sex|sab|"
			cellStyle="vertical-align: bottom;"
			property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|"
			align="top" label="frequencia" labelStyle="vertical-align: bottom;" />
		<adsm:textbox dataType="integer" property="nrGrauDificuldade"
			label="grauDificuldade" maxLength="6" size="6" labelWidth="17%"
			width="33%" unit="km2" cellStyle="vertical-align: bottom;"
			labelStyle="vertical-align: bottom;" />
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="btnSalvar" callbackProperty="storeButton"
				caption="salvarFilial"
				service="lms.municipios.gerarFluxoCargaFilialNovaAction.storeMap" />
			<adsm:button id="resetButton" caption="limpar"
				onclick="onResetButton();" />
		</adsm:buttonBar>
		<script>
			var lms_29024 = '<adsm:label key="LMS-29024"/>';
		</script>
	</adsm:form>
	<adsm:grid property="filial" idProperty="idFilial" selectionMode="none"
		rows="7" onRowClick="onRowClick" emptyLabel="LMS-29082"
		service="lms.municipios.gerarFluxoCargaFilialNovaAction.findPagindatedFluxoInicialOrigem"
		rowCountService="lms.municipios.gerarFluxoCargaFilialNovaAction.getRowCountFluxoInicialOrigem"
		detailFrameName="filiais">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialByIdFilialOrigem.sgFilial"
				width="185" title="filialOrigem" />
			<adsm:gridColumn property="filialByIdFilialOrigem.pessoa.nmFantasia"
				width="185" title="" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialByIdFilialDestino.sgFilial"
				width="185" title="filialDestino" />
			<adsm:gridColumn property="filialByIdFilialDestino.pessoa.nmFantasia"
				width="185" title="" />
		</adsm:gridColumnGroup>
		<adsm:buttonBar />
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
			limpaFluxoReembarque();
			var tabGroup = getTabGroup(this.document);
			var tabProcesso = tabGroup.getTab("proc");
			tpGeracao = tabProcesso.getFormProperty("tpGeracao");
			
			var idFilialProc = tabProcesso.getFormProperty("filial.idFilial");
			var sgFilialProc = tabProcesso.getFormProperty("filial.sgFilial")
			var siglaNomeFilialProc = tabProcesso.getFormProperty("filial.sgFilial") + " - " + tabProcesso.getFormProperty("filial.pessoa.nmFantasia");
			var nmFantasiaProc = tabProcesso.getFormProperty("filial.pessoa.nmFantasia")
			
			if (tpGeracao == 'O') {
					setElementValue("filialByIdFilialDestino.siglaNomeFilial","");
					setElementValue("filialByIdFilialDestino.idFilial","");
					setElementValue("filialByIdFilialDestino.sgFilial","");
					setElementValue("filialByIdFilialDestino.pessoa.nmFantasia","");
					
					setElementValue("filialByIdFilialOrigem.siglaNomeFilial",siglaNomeFilialProc);
					setElementValue("filialByIdFilialOrigem.idFilial",idFilialProc);
					setElementValue("filialByIdFilialOrigem.sgFilial",sgFilialProc);
					setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia",nmFantasiaProc);
			} else if (tpGeracao == 'D') {
					setElementValue("filialByIdFilialOrigem.siglaNomeFilial","");
					setElementValue("filialByIdFilialOrigem.idFilial","");
					setElementValue("filialByIdFilialOrigem.sgFilial","");
					setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia","");
					
					setElementValue("filialByIdFilialDestino.siglaNomeFilial",siglaNomeFilialProc);
					setElementValue("filialByIdFilialDestino.idFilial",idFilialProc);
					setElementValue("filialByIdFilialDestino.sgFilial",sgFilialProc);
					setElementValue("filialByIdFilialDestino.pessoa.nmFantasia",nmFantasiaProc);
			}
			if(eventObj.name == "newButton_click")
				desabilitaCampos(true);
				
			setDisabled("btnSalvar",true);
		}
	}
	

	var servico = null;
	var tpGeracao = null;
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var siglaNomeFilial = null;
	var idServico = null;
	var dtVigenciaInicial = null;
	var dtVigenciaFinal = null;
	
	document.getElementById("filialByIdFilialOrigem.pessoa.nmFantasia").masterLink = true;
	document.getElementById("filialByIdFilialDestino.pessoa.nmFantasia").masterLink = true;		
	document.getElementById("filialByIdFilialOrigem.siglaNomeFilial").masterLink = true;
	document.getElementById("filialByIdFilialDestino.siglaNomeFilial").masterLink = true;	
	document.getElementById("filialByIdFilialOrigem.idFilial").masterLink = true;
	document.getElementById("filialByIdFilialDestino.idFilial").masterLink = true;	
	document.getElementById("filialByIdFilialOrigem.sgFilial").masterLink = true;
	document.getElementById("filialByIdFilialDestino.sgFilial").masterLink = true;	
	document.getElementById("dtVigenciaInicial").masterLink = true;
	document.getElementById("dtVigenciaFinal").masterLink = true;
	document.getElementById("fluxoReembarque").masterLink = "true";
	
	
	document.getElementById("servico.description").masterLink = true;	
	document.getElementById("servico.idServico").masterLink = true;	
		
	function carregaGridPrincipal(isStoreCb) {			

		var tabGroup = getTabGroup(this.document);
		var tabProcesso = tabGroup.getTab("proc");

		// só executa a consulta quando o usuário clicou em gerar		
		if (tabProcesso.tabOwnerFrame.document.getElementById("gerar").value == "true" || isStoreCb == "storeButtom") {
			tabProcesso.tabOwnerFrame.document.getElementById("gerar").value = "false";
		
			servico = tabProcesso.tabOwnerFrame.document.getElementById("servico.idServico");			
			tpGeracao = tabProcesso.getFormProperty("tpGeracao");
			idFilial = tabProcesso.getFormProperty("filial.idFilial");
			sgFilial = tabProcesso.getFormProperty("filial.sgFilial");
			nmFilial = tabProcesso.getFormProperty("filial.pessoa.nmFantasia");			
			siglaNomeFilial = tabProcesso.getFormProperty("filial.siglaNomeFilial");
			dtVigenciaInicial = dropFormat(tabProcesso.tabOwnerFrame.document.getElementById("dtVigenciaInicial"));
			dtVigenciaFinal = dropFormat(tabProcesso.tabOwnerFrame.document.getElementById("dtVigenciaFinal"));
			
			if (tpGeracao == 'O') {
				if (isStoreCb != "storeButtom") {
					setElementValue("filialByIdFilialOrigem.siglaNomeFilial", siglaNomeFilial);
					setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
					setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
					setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);

					setElementValue("filialByIdFilialDestino.siglaNomeFilial","");
					setElementValue("filialByIdFilialDestino.idFilial","");
					setElementValue("filialByIdFilialDestino.sgFilial","");
					setElementValue("filialByIdFilialDestino.pessoa.nmFantasia","");
					limpaFluxoReembarque();
				}
			} else if (tpGeracao == 'D') {
				if (isStoreCb != "storeButtom") {
					setElementValue("filialByIdFilialDestino.siglaNomeFilial", siglaNomeFilial);
					setElementValue("filialByIdFilialDestino.idFilial", idFilial);				
					setElementValue("filialByIdFilialDestino.sgFilial", sgFilial);
					setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", nmFilial);
					setElementValue("filialByIdFilialOrigem.siglaNomeFilial","");
					setElementValue("filialByIdFilialOrigem.idFilial","");
					setElementValue("filialByIdFilialOrigem.sgFilial","");
					setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia","");
					limpaFluxoReembarque();
				}
			}
			

			if (servico[servico.selectedIndex].value != '') {
				setElementValue("servico.description", servico[servico.selectedIndex].text );
				setElementValue("servico.idServico", servico[servico.selectedIndex].value );
			} else {
				setElementValue("servico.description", '');
				setElementValue("servico.idServico", '' );				
			}
			setElementValue("dtVigenciaInicial", dtVigenciaInicial);
			setElementValue("dtVigenciaFinal", dtVigenciaFinal);
			
			var data = new Array();
			setNestedBeanPropertyValue(data, "tpGeracao", tpGeracao);
			setNestedBeanPropertyValue(data, "filial.idFilial", idFilial);
			setNestedBeanPropertyValue(data, "servico.idServico", document.getElementById("servico.idServico").value);
			setNestedBeanPropertyValue(data, "dtVigenciaInicial", dtVigenciaInicial);
			setNestedBeanPropertyValue(data, "dtVigenciaFinal", dtVigenciaFinal);
		
			filialGridDef.executeSearch(data);
			gerar = false;
		}
	}
	
	function pageLoad_cb() {
		desabilitaCampos(true);
	}
	
	function desabilitaCampos(valor) {
		setDisabled("filialByIdFilialReembarcadora.idFilial", valor);
		setDisabled("nrPrazoView", valor);
		setDisabled("nrDistancia", valor);
		setDisabled("nrGrauDificuldade", valor);
		setDisabled("blDomingo", valor);
		setDisabled("blSegunda", valor);
		setDisabled("blTerca", valor);
		setDisabled("blQuarta", valor);
		setDisabled("blQuinta", valor);
		setDisabled("blSexta", valor);
		setDisabled("blSabado", valor);
	}
	
	
	function onResetButton() {
		resetForm();
		
	}
	
	function onRowClick() {
		document.getElementById("fluxoReembarque").masterLink = "false";
		resetValue("fluxoReembarque");
		document.getElementById("fluxoReembarque").masterLink = "true";
		desabilitaCampos(false);
		return true;	
	}
	
    function storeButton_cb(data,exception,key){
    	store_cb(data,exception,key);
    	if (exception == undefined) {
    		setDisabled("btnSalvar",true);
    		setFocus(document.getElementById("resetButton"),false);
    		carregaGridPrincipal("storeButtom");
    	}
    }
	
	
	function resetForm() {

		if (tpGeracao == "O")
			resetValue("filialByIdFilialDestino.siglaNomeFilial");

		else if (tpGeracao == "D")
			resetValue("filialByIdFilialOrigem.siglaNomeFilial");		

		newButtonScript(this.document, true, {name:'newButton_click'});
	}
	
	function dataLoad_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		onDataLoad_cb(data, error); 
		
		if (tpGeracao == 'O') {
			setElementValue("filialByIdFilialDestino.siglaNomeFilial", getNestedBeanPropertyValue(data, "siglaNomeFilial") );
			setElementValue("filialByIdFilialDestino.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("filialByIdFilialDestino.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
			setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "pessoa.Fantasia"));
		} else if (tpGeracao == 'D') {
			setElementValue("filialByIdFilialOrigem.siglaNomeFilial", getNestedBeanPropertyValue(data, "siglaNomeFilial"));
			setElementValue("filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));				
			setElementValue("filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));				
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		}
		
		var dados = new Array();
		if(getElementValue("filialByIdFilialOrigem.idFilial")!= ""){
			setNestedBeanPropertyValue(dados,"idFilialOrigem",getElementValue("filialByIdFilialOrigem.idFilial"));
			setNestedBeanPropertyValue(dados,"idFilialDestino",getNestedBeanPropertyValue(data, "idFilial"));
		}	
		if(getElementValue("filialByIdFilialDestino.idFilial")!= ""){
			setNestedBeanPropertyValue(dados,"idFilialDestino",getElementValue("filialByIdFilialDestino.idFilial"));
			setNestedBeanPropertyValue(dados,"idFilialOrigem",getNestedBeanPropertyValue(data, "idFilial"));
		}
		
		setNestedBeanPropertyValue(dados,"idServico",getElementValue("servico.idServico"));
		
		var sdo = createServiceDataObject("lms.municipios.gerarFluxoCargaFilialNovaAction.findDistancia","setaDistancia",dados);
        xmit({serviceDataObjects:[sdo]});
        
		
	}
	
	function setaDistancia_cb(data){
		setElementValue("nrDistancia",getNestedBeanPropertyValue(data,"nrDistancia"));
		setDisabled("btnSalvar",false);
	}
			
	// ################################################################
	// funcoes relacionadas com o fluxo de reembarque
	// ################################################################
	
	function filialReemChange(eThis) {
		if (eThis.previousValue == eThis.value)
			return true;
		limpaFluxoReembarque();
    	var flag = filialByIdFilialReembarcadora_sgFilialOnChangeHandler();
    	if (eThis.value == "") {
    	    limpaFluxoReembarque();
    	}
    	return flag;
	}
	
	function limpaFluxoReembarque(){
		while(document.getElementById("fluxoReembarque").length != 0)
		      document.getElementById("fluxoReembarque")[0] = null;
	}
		
	function filialReembLoad_cb(data,exception) {
		if (data != undefined ) {
		    if(data.length == 1)
				var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
			if (validaFilialReemb(idFilial)) {
				resetValue("filialByIdFilialReembarcadora.sgFilial");
				setFocus("filialByIdFilialReembarcadora.sgFilial");
				return false;
			} else	
				return filialByIdFilialReembarcadora_sgFilial_exactMatch_cb(data);
		} else {
		    fluxoReembarqueListboxDef.cleanRelateds();
			return true;		
		}
	}

	function filialReembPopup(data,dialogWindow) {
		if (data != undefined) {
		    limpaFluxoReembarque();
			var idFilial = getNestedBeanPropertyValue(data,":idFilial");
			var flag = validaFilialReemb(idFilial,dialogWindow);
			if(flag == true)
				setFocus(document.getElementById("filialByIdFilialReembarcadora.sgFilial"));
			return (!flag);
		} else {
			fluxoReembarqueListboxDef.cleanRelateds();
			return true;
		}
	}
	
	function validaFilialReemb(idFilial,dialogWindow) {
		var filiais = new Array(2);
		filiais[0] = 'filialByIdFilialOrigem.idFilial';
		filiais[1] = 'filialByIdFilialDestino.idFilial';
		if (validaFilial(idFilial,filiais)) {
			if(dialogWindow != undefined)
				dialogWindow.close();
		    alert(lms_29024);
			return true;
		} else if (idFilial != undefined)
			fluxoByFilialReemb(idFilial);
		return false;
	}
	
	/**
	 * retorna true quando a filial for igual à alguma no array.
	 */
	function validaFilial(idFilial,filiais) {
		if (filiais.length > 0 ) {
			for (i = 0 ; i < filiais.length ; i++)
				if (idFilial == getElementValue(filiais[i]))
					return true;
		}
		return false;		
	}
	
	function fluxoByFilialReemb(id) {
		if (getElementValue("filialByIdFilialDestino.idFilial") != "")
			callFindFluxoReembarqueReemb(id);
		else
			return true;
	}
	
	/**********************************************************************************
	 * xmit para consultar fluxo de reembarque.
	 *********************************************************************************/
	function callFindFluxoReembarqueReemb(id) {
		var data = new Array();

		// setNestedBeanPropertyValue(data,"idFluxoFilial",getElementValue("idFluxoFilial"));
		setNestedBeanPropertyValue(data,"idFluxoFilial",'');
		setNestedBeanPropertyValue(data,"idFilialOrigem",getElementValue("filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(data,"idFilialDestino",getElementValue("filialByIdFilialDestino.idFilial"));
		setNestedBeanPropertyValue(data,"idFilialReembarcadora",id);
		setNestedBeanPropertyValue(data,"idServico",getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(data,"which","reemb");
		
		var sdo = createServiceDataObject("lms.municipios.gerarFluxoCargaFilialNovaAction.findFluxoReembarqueToMap","findFluxoReembarqueReemb",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFluxoReembarqueReemb_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			resetValue("filialByIdFilialReembarcadora.idFilial");
			resetValue("filialByIdFilialReembarcadora.sgFilial");
			resetValue("filialByIdFilialReembarcadora.pessoa.nmFantasia");
			fluxoReembarqueListboxDef.cleanRelateds();
			setFocus(document.getElementById("filialByIdFilialReembarcadora.sgFilial"));
		} else {
			fluxoReembarque_cb(data);
			setFocus(document.getElementById("fluxoReembarque"));
		}
	}
	
</script>
