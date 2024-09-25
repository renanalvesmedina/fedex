<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoad_cb() {
		onPageLoad_cb();
		setMasterLink(this.document, true);
	
		if (document.getElementById("idProcessoWorkflow").value != "") {
			findDadosHistorico(getElementValue("idProcessoWorkflow"));
		}
	}
//-->
</script>
<adsm:window service="lms.municipios.atualizarTrocaFiliaisAction" onPageLoadCallBack="pageLoad">

	<adsm:form action="/municipios/atualizarTrocaFiliais">	
		<adsm:hidden property="idProcessoWorkflow"/>
		
		<adsm:hidden property="sgFilialAtual" serializable="true"/>
	
		<adsm:lookup property="municipioFilial.municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 
			service="lms.municipios.atualizarTrocaFiliaisAction.findMunicipio"   dataType="text"  
			labelWidth="20%"  label="municipio" size="30"  required="true" onchange="return municipioChange(this)" onDataLoadCallBack="municipioDataLoad" onPopupSetValue="municipioPopup"
			action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="60" >
			<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="nmMunicipio" modelProperty="nmMunicipio" />
			<adsm:hidden property="nmMunicipio" serializable="true"/>
		</adsm:lookup>
	
		<adsm:complement label="uf" labelWidth="20%" width="39%" >
			<adsm:textbox dataType="text" property="unidadeFederativa.sgUnidadeFederativa" size="3" disabled="true"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true"/>
		</adsm:complement>
	
		<adsm:textbox dataType="text" property="unidadeFederativa.pais.nmPais" label="pais" size="20" labelWidth="15%" width="26%" disabled="true"/>

		<adsm:combobox property="filialAtual.idMunicipioFilial" autoLoad="false" label="filialAtual" service="" optionLabelProperty="siglaNomeFilial" optionProperty="idMunicipioFilial" 
					   disabled="true" onchange="filialAtualChange(this)" onDataLoadCallBack="filialAtualDataLoad" required="true" width="80%" labelWidth="20%"/>		
	
		<adsm:lookup property="municipioFilial.filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
					service="lms.municipios.atualizarTrocaFiliaisAction.findFilial" dataType="text" label="filialNova" size="3"
					action="/municipios/manterFiliais" labelWidth="20%" width="9%" minLengthForAutoPopUpSearch="3" onchange="filialNovaChange(this)"
					exactMatch="true" style="width:45px" onDataLoadCallBack="filialNovaDataLoad" onPopupSetValue="filialPopup">
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="sgFilialNova" modelProperty="sgFilial" />
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" width="30%" disabled="true"/>			
			<adsm:hidden property="sgFilialNova" serializable="true"/>
		</adsm:lookup>

		<adsm:checkbox property="municipioFilial.blPadraoMcd" label="padraoMCD" labelWidth="15%" width="26%" disabled="true"/>

		<adsm:textbox dataType="integer" property="municipioFilial.nrDistanciaAsfalto" label="distanciaAsfalto" cellStyle="vertical-align:bottom" size="6" unit="km2" maxLength="6" required="true" labelWidth="20%" width="39%"/>
		
		<adsm:checkbox property="municipioFilial.blRestricaoAtendimento" label="restricaoAtendimento"  cellStyle="vertical-align:bottom" labelWidth="15%" width="26%" disabled="true"/>
			
		<adsm:textbox dataType="integer" property="municipioFilial.nrDistanciaChao" label="distanciaChao" size="6" unit="km2" maxLength="6" required="true" labelWidth="20%" />
	
		<adsm:textbox dataType="integer" property="municipioFilial.nrGrauDificuldade" label="grauDificuldade"  unit="km2" maxLength="6" size="6" labelWidth="20%" width="80%"/>
	
		<adsm:checkbox property="municipioFilial.blRecebeColetaEventual" label="recebeColetaEventual" labelWidth="20%"  width="80%"/>
		
		<adsm:range label="vigencia" labelWidth="20%">
             <adsm:textbox dataType="JTDate" property="municipioFilial.dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="municipioFilial.dtVigenciaFinal"/>
        </adsm:range>
		<adsm:combobox labelWidth="20%" label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
	
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="60%" style="border:none;"/>
		<adsm:label key="infoAtualizarTrocaFilial" width="40%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		
		
		<adsm:buttonBar>
			<adsm:button id="botao_atualizar" caption="atualizar" onclick="atualizar()"/>
			<adsm:button caption="visualizar" id="botao_visualizar" onclick="visualizar()" disabled="true"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

		
	function atualizar(){
		
		document.getElementById("filialAtual.idMunicipioFilial").required = "true";
		document.getElementById("municipioFilial.filial.sgFilial").required = "true";
		document.getElementById("municipioFilial.nrDistanciaAsfalto").required = "true";
		document.getElementById("municipioFilial.nrDistanciaChao").required = "true";
		document.getElementById("municipioFilial.dtVigenciaInicial").required = "true";

		if (validateForm(document.forms[0])){		
			if (confirm(parent.i18NLabel.getLabel("LMS-29135"))){
				var data = new Array();
					
				setNestedBeanPropertyValue(data, "municipioFilial.municipio.idMunicipio", getElementValue("municipioFilial.municipio.idMunicipio"));
				setNestedBeanPropertyValue(data, "filialAtual.idMunicipioFilial", getElementValue("filialAtual.idMunicipioFilial"));
				setNestedBeanPropertyValue(data, "municipioFilial.filial.idFilial", getElementValue("municipioFilial.filial.idFilial"));
				setNestedBeanPropertyValue(data, "municipioFilial.blPadraoMcd", getElementValue("municipioFilial.blPadraoMcd"));
				setNestedBeanPropertyValue(data, "municipioFilial.blRestricaoAtendimento", getElementValue("municipioFilial.blRestricaoAtendimento"));
				setNestedBeanPropertyValue(data, "municipioFilial.nrDistanciaAsfalto", getElementValue("municipioFilial.nrDistanciaAsfalto"));
				setNestedBeanPropertyValue(data, "municipioFilial.nrDistanciaChao", getElementValue("municipioFilial.nrDistanciaChao"));
				setNestedBeanPropertyValue(data, "municipioFilial.nrGrauDificuldade", getElementValue("municipioFilial.nrGrauDificuldade"));
				setNestedBeanPropertyValue(data, "municipioFilial.blRecebeColetaEventual", getElementValue("municipioFilial.blRecebeColetaEventual"));
				setNestedBeanPropertyValue(data, "municipioFilial.dtVigenciaInicial", getElementValue("municipioFilial.dtVigenciaInicial"));
				setNestedBeanPropertyValue(data, "municipioFilial.dtVigenciaFinal", getElementValue("municipioFilial.dtVigenciaFinal"));
				
				var sdo = createServiceDataObject("lms.municipios.atualizarTrocaFiliaisAction.executeAtualizar",
													"procAtualizaTrocaFilialCallBack",data);	
				xmit({serviceDataObjects:[sdo]});
			}
		}
	}
	
	function visualizar(){
	
		document.getElementById("filialAtual.idMunicipioFilial").required = "false";
		document.getElementById("municipioFilial.filial.sgFilial").required = "false";
		document.getElementById("municipioFilial.nrDistanciaAsfalto").required = "false";
		document.getElementById("municipioFilial.nrDistanciaChao").required = "false";
		document.getElementById("municipioFilial.dtVigenciaInicial").required = "false";
		
		reportButtonScript('lms.municipios.atualizarTrocaFiliaisAction', 'openPdf', document.forms[0]);

	}
	
	function procAtualizaTrocaFilialCallBack_cb(data, error){
		
		if (error != undefined){
			alert(error);
		} else {			
			showSuccessMessage();
			if (data._value != undefined && data._value != ''){
				alert(data._value);
			}
		}
	}
	
	function initWindow(event){
		if (event.name == "cleanButton_click"){
				
			setDisabled("filialAtual.idMunicipioFilial", true);
			setDisabled("botao_atualizar", true);
			setDisabled("botao_visualizar", true);
		}
	}


	function municipioChange(obj){
		var flag = municipioFilial_municipio_nmMunicipioOnChangeHandler();
		if (obj.value == ''){
			resetValue("filialAtual.idMunicipioFilial");
			
			setDisabled("filialAtual.idMunicipioFilial", true);
			setDisabled("botao_visualizar", true);
			
		}
		
		return flag;
	}
	
	function municipioDataLoad_cb(data){
		
		if (data != undefined && data.length == 1) {
			var idMunicipio = getNestedBeanPropertyValue(data, ":0.idMunicipio");
			loadComboFilialAtual(idMunicipio);
			setDisabled("filialAtual.idMunicipioFilial", false);
			setDisabled("botao_visualizar", false);
			resetValue("filialAtual.idMunicipioFilial");			
		}
		
		return lookupExactMatch({e:document.getElementById("municipioFilial.municipio.idMunicipio"), data:data, callBack:'municipioLikeEndMatch'});
		
	}
	
	function municipioLikeEndMatch_cb(data){
		if (data != undefined && data.length == 1) {
			var idMunicipio = getNestedBeanPropertyValue(data, ":0.idMunicipio");
			loadComboFilialAtual(idMunicipio);
			setDisabled("filialAtual.idMunicipioFilial", false);
			setDisabled("botao_visualizar", false);
		}
		
		return municipioFilial_municipio_nmMunicipio_likeEndMatch_cb(data);
	}
	
	function municipioPopup(data){
		var idMunicipio = getNestedBeanPropertyValue(data, "idMunicipio");
		loadComboFilialAtual(idMunicipio);
		setDisabled("filialAtual.idMunicipioFilial", false);
		setDisabled("botao_visualizar", false);
		return true;
	}
	
	function filialAtualChange(obj){
		if (obj.value != ''){
			loadCamposBloqueados(obj.value);
			var valor = obj.options[obj.selectedIndex].text;
			if (valor.indexOf('-') > 0){
				var sub = valor.substring(0, valor.indexOf('-') - 1);
				setElementValue("sgFilialAtual", sub);
			}
		} else {
			setElementValue("sgFilialAtual");
			resetValue("municipioFilial.blPadraoMcd");
			resetValue("municipioFilial.blRestricaoAtendimento");
		}
	}
	
	function loadComboFilialAtual(idMunicipio){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipio", idMunicipio);
		
		var sdo = createServiceDataObject("lms.municipios.atualizarTrocaFiliaisAction.findMunicipioFilialVigenteByMunicipio",
				"filialAtual.idMunicipioFilial",data);
		
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	function loadCamposBloqueados(idMunicipioFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipioFilial", idMunicipioFilial);	
		
		var sdo = createServiceDataObject("lms.municipios.atualizarTrocaFiliaisAction.findMunicipioFilial",
					"setaCamposBloqueados",data);
		
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaCamposBloqueados_cb(data){
		if (data != undefined && data.length == 1) {
			var blPadraoMCD = getNestedBeanPropertyValue(data, ":0.blPadraoMcd");
			setElementValue("municipioFilial.blPadraoMcd", blPadraoMCD);
			
			var blRestricaoAtendimento= getNestedBeanPropertyValue(data, ":0.blRestricaoAtendimento");
			setElementValue("municipioFilial.blRestricaoAtendimento", blRestricaoAtendimento);
					
		}
	}
	
	function filialNovaDataLoad_cb(data){
		municipioFilial_filial_sgFilial_exactMatch_cb(data);
		
		if (data != undefined && data.length == 1) {
			habilitaBotoes();
		} else {
			setFocus("municipioFilial.filial.sgFilial");
		}
	}
	
	function filialNovaChange(obj){
		if (obj.value == '') {
			setDisabled("botao_atualizar", true);
		}

		return municipioFilial_filial_sgFilialOnChangeHandler();
	}
	
	function filialPopup(data){
		habilitaBotoes();
		return true;
	}
	
	function habilitaBotoes(){
		setDisabled("botao_atualizar", false);
		setDisabled("botao_visualizar", false);
	}
	
	function filialAtualDataLoad_cb(data){
		filialAtual_idMunicipioFilial_cb(data);
	
		if (getElementValue("idProcessoWorkflow") != ''){
	
			document.getElementById("filialAtual.idMunicipioFilial").selectedIndex = 1;
		}
	}
			
	function findDadosHistorico(idHistoricoTrocaFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idHistoricoTrocaFilial", idHistoricoTrocaFilial);	
		
		var sdo = createServiceDataObject("lms.municipios.atualizarTrocaFiliaisAction.findDadosHistoricoTrocaFilial",
					"preencheCamposHistorico",data);
		
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	function loadComboFilialAtualHistorico(idMunicipioFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMunicipioFilial", idMunicipioFilial);	
		
		var sdo = createServiceDataObject("lms.municipios.atualizarTrocaFiliaisAction.findFilialAtualHistorico",
												"filialAtualDataLoad",data);
		
		xmit({serviceDataObjects:[sdo]});
		
	}
		
	
	function preencheCamposHistorico_cb(data){
         setElementValue("municipioFilial.municipio.nmMunicipio", data.nmMunicipio);
    	 setElementValue("unidadeFederativa.sgUnidadeFederativa", data.sgUnidadeFederativa);
    	 setElementValue("unidadeFederativa.nmUnidadeFederativa", data.nmUnidadeFederativa);
		 setElementValue("unidadeFederativa.pais.nmPais", data.nmPais);
		 var idMunicipioFilialAtual = data.idMunicipioFilial;
		 setElementValue("municipioFilial.filial.sgFilial", data.sgFilialTroca);
		 setElementValue("municipioFilial.filial.idFilial", data.idFilialTroca);
		 setElementValue("municipioFilial.filial.pessoa.nmFantasia", data.nmFantasiaTroca);
		 setElementValue("municipioFilial.blPadraoMcd", data.blPadraoMcd);
		 setElementValue("municipioFilial.nrDistanciaAsfalto", data.nrDistanciaAsfalto);
		 setElementValue("municipioFilial.nrDistanciaChao", data.nrDistanciaChao);
		 setElementValue("municipioFilial.nrGrauDificuldade", data.nrGrauDificuldade);
		 setElementValue("municipioFilial.blRestricaoAtendimento", data.blRestricaoAtendimento);
		 setElementValue("municipioFilial.blRecebeColetaEventual", data.blRecebeColetaEventual);
		 setElementValue("municipioFilial.dtVigenciaInicial", setFormat(document.getElementById("municipioFilial.dtVigenciaInicial"),data.dtVigenciaInicial));
	     setElementValue("municipioFilial.dtVigenciaFinal",setFormat(document.getElementById("municipioFilial.dtVigenciaFinal"),data.dtVigenciaFinal));
		
		 loadComboFilialAtualHistorico(idMunicipioFilialAtual);
		 
		 setDisabled(document, true);
 		 setDisabled("botao_visualizar", false);
		 
	}
	
</script>
