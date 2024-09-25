<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotaColetaEntregaAction">

	<adsm:form action="/municipios/manterRotaColetaEntrega" idProperty="idRotaColetaEntrega" onDataLoadCallBack="rotaColetaEntregaDataLoad" service="lms.municipios.manterRotaColetaEntregaAction.findByIdDetalhamento">
	
			
		<adsm:lookup service="lms.municipios.manterRotaColetaEntregaAction.findFilialLookup" dataType="text" property="filial" 
					idProperty="idFilial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" 
					exactMatch="true" labelWidth="15%"  action="/municipios/manterFiliais" required="true" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true"/>
		</adsm:lookup>	
       
       <adsm:hidden property="filial.siglaNomeFilial"/>
       
		<adsm:textbox dataType="integer" property="nrRota"  size="2" maxLength="3" label="numeroRota"  labelWidth="15%" disabled="true" />

		<adsm:hidden property="numeroDescricaoRota"/>
		
		<adsm:textbox dataType="integer" property="nrKm" label="distanciaRota2" size="6" maxLength="6" disabled="false"
				required="true" labelWidth="15%" width="14%" unit="km2" />

		<adsm:textarea property="dsRota" label="descricao" required="true" rows="2" columns="60" maxLength="120"  labelWidth="15%" width="85%" />
	
		<adsm:listbox 
                   label="saidasPrevistas" 
                   size="4" 
                   property="horarioPrevistoSaidaRotas"
				   optionProperty="idHorarioPrevistoSaidaRota"
				   optionLabelProperty="hrPrevista"
				   labelWidth="15%"
                   width="77%"
                   showOrderControls="false" required="true" boxWidth="198" showIndex="false" serializable="true">
                 <adsm:textbox property="hrPrevista" dataType="JTTime" size="35" maxLength="60"/>
       </adsm:listbox>	

		<adsm:range label="vigencia" labelWidth="15%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"  />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
	<adsm:buttonBar lines="2">
			
			<adsm:button caption="regioesRota" id="regioesRota" action="municipios/manterRegioesFilialRota" cmd="main" boxWidth="125" breakBefore="true" >
				<adsm:linkProperty src="idRotaColetaEntrega" target="rotaColetaEntrega.idRotaColetaEntrega"/>
				<adsm:linkProperty src="nrRota" target="rotaColetaEntrega.nrRota"/>
				<adsm:linkProperty src="dsRota" target="rotaColetaEntrega.dsRota"/>
				<adsm:linkProperty src="filial.idFilial" target="rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="rotaColetaEntrega.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="rotaColetaEntrega.filial.pessoa.nmFantasia"/>
			</adsm:button>
			
			<adsm:button caption="tiposVeiculoRotaButton" id="tiposVeiculoRota" action="municipios/manterTiposVeiculoRota" cmd="main" boxWidth="170">
				<adsm:linkProperty src="idRotaColetaEntrega" target="rotaColetaEntrega.idRotaColetaEntrega"/>
				<adsm:linkProperty src="nrRota" target="rotaColetaEntrega.nrRota"  disabled="true"/>
				<adsm:linkProperty src="dsRota" target="rotaColetaEntrega.dsRota" disabled="true"/>
				<adsm:linkProperty src="filial.idFilial" target="rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="rotaColetaEntrega.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="rotaColetaEntrega.filial.pessoa.nmFantasia"/>
			</adsm:button>
			
			<adsm:button caption="postosPassagem" id="postosPassagem" action="municipios/manterPostosPassagemRota" cmd="main" boxWidth="170">
				<adsm:linkProperty src="idRotaColetaEntrega" target="rotaColetaEntrega.idRotaColetaEntrega"/>
				<adsm:linkProperty src="nrRota" target="rotaColetaEntrega.nrRota" disabled="true"/>
				<adsm:linkProperty src="dsRota" target="rotaColetaEntrega.dsRota" disabled="true"/>
				<adsm:linkProperty src="filial.idFilial" target="rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="rotaColetaEntrega.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="rotaColetaEntrega.filial.pessoa.nmFantasia"/>
				
			</adsm:button>
			
			<adsm:button caption="intervalosCEPRotaButton" id="intervalosCEPRota" action="municipios/manterRotaIntervaloCEP" cmd="main" boxWidth="160">
				<adsm:linkProperty src="idRotaColetaEntrega" target="rotaColetaEntrega.idRotaColetaEntrega"/>
				<adsm:linkProperty src="nrRota" target="rotaColetaEntrega.nrRota"/>
				<adsm:linkProperty src="dsRota" target="rotaColetaEntrega.dsRota"/>
				<adsm:linkProperty src="filial.idFilial" target="rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="rotaColetaEntrega.filial.sgFilial"/>
				<%--
				<adsm:linkProperty src="filial.siglaNomeFilial" target="filial.siglaNomeFilial"/>
				--%>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="rotaColetaEntrega.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="dtVigenciaInicial" target="municipioFilial.dtVigenciaInicial"/>
				<adsm:linkProperty src="dtVigenciaFinal" target="municipioFilial.dtVigenciaFinal"/>
			</adsm:button>
			
			<adsm:storeButton service="lms.municipios.manterRotaColetaEntregaAction.storeMap" callbackProperty="storeRotaColetaEntrega"/>
			<adsm:newButton id="limpar" />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	var isFilial = true;

	function storeRotaColetaEntrega_cb(data, error, key){
		store_cb(data, error, key);
		if (error == undefined && data != undefined){
			var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";			
			validaAcaoVigencia(acaoVigencia, store);
			setElementValue("filial.siglaNomeFilial", getElementValue("filial.sgFilial") + ' - ' + getElementValue("filial.pessoa.nmFantasia"));
			setElementValue("numeroDescricaoRota", getNestedBeanPropertyValue(data, "nrRota") + ' - ' + getElementValue("dsRota"));
		}
	}

	function rotaColetaEntregaDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		validateFilialMatriz();
		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");		
		validaAcaoVigencia(acaoVigencia, null);
		
	}
	
	function validateFilialMatriz(){
	    var sdo = createServiceDataObject("lms.municipios.manterRotaColetaEntregaAction.isFilialMatriz", "isFilialMatriz", {idFilial:getElementValue("filial.idFilial")});
	    xmit({serviceDataObjects:[sdo]});
	}

	// Habilita o campo caso a Filial do usuário seja Matriz	
	function isFilialMatriz_cb(data, error){
		if (error){
			alert(error);
			return false;
		}
		if (data && data.isFilialMatriz && data.isFilialMatriz == "true"){		
			isFilial = false;			
		}else{
			isFilial = true;			
		}
		setDisabled("nrRota", isFilial);
	}
	
	
	
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.municipios.manterRotaColetaEntregaAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}

	function setaValoresFilial() {
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
	}
	
	function validaAcaoVigencia(acaoVigencia, tipoEvento){
		if (acaoVigencia == 0){
			novo();
			setDisabled("__buttonBar:0.removeButton", false);
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
     		else
     			setFocus(document.getElementById("limpar"), false);
		} else if (acaoVigencia == 1){
			setDisabled(document, true);			
			setDisabled("nrKm", false);
			setDisabled("dsRota", false);
			setDisabled("horarioPrevistoSaidaRotas", false);
			setDisabled("horarioPrevistoSaidaRotas_hrPrevista", false);
			setDisabled("dtVigenciaFinal", false);
			setDisabled("limpar", false);
			setDisabled("__buttonBar:0.storeButton", false);
			setDisabled("regioesRota", false);
			setDisabled("tiposVeiculoRota", false);
			setDisabled("postosPassagem", false);
			setDisabled("intervalosCEPRota", false);
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
     		else
     			setFocus(document.getElementById("limpar"), false);
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);			
			setDisabled("limpar", false);
			setDisabled("regioesRota", false);
			setDisabled("tiposVeiculoRota", false);
			setDisabled("postosPassagem", false);
			setDisabled("intervalosCEPRota", false);
			setFocus(document.getElementById("limpar"), false);
		}
	}
	
	function novo(){
		//setDisabled("filial.sgFilial", false);
		//setDisabled("filial.idFilial", false);
		setDisabled("nrRota", true);
		setDisabled("nrKm", false);
		setDisabled("dsRota", false);
		setDisabled("horarioPrevistoSaidaRotas", false);
		setDisabled("horarioPrevistoSaidaRotas_hrPrevista", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);		
		setDisabled("limpar", false);
		setDisabled("__buttonBar:0.storeButton", false);
		setDisabled("__buttonBar:0.removeButton", true);			
	}
	
	function initWindow(event){
		if (event.name != "gridRow_click" && event.name != "storeButton"){
			novo();
			setFocusOnFirstFocusableField();
		}
		
		if (event.name == "tab_click" || event.name == "removeButton") {
			getFilialUsuario();
		} else if (event.name == "newButton_click") {
			setaValoresFilial();
		}
	}	
	
</script>

