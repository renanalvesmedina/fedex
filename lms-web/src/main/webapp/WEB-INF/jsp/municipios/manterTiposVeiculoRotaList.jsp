<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTiposVeiculoRota" service="lms.municipios.manterTiposVeiculoRotaAction" onPageLoadCallBack="pageLoadCustom">
	<adsm:form action="/municipios/manterTiposVeiculoRota" idProperty="idRotaTipoMeioTransporte">
   
	    <adsm:lookup service="lms.municipios.manterTiposVeiculoRotaAction.findLookupFilial" dataType="text" property="rotaColetaEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial"
				label="filial" size="3" maxLength="3" action="/municipios/manterFiliais" width="85%" exactMatch="true" required="true" disabled="true">
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
   			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="false" />
   			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
   			<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
	    </adsm:lookup>
	   
	   <adsm:lookup service="lms.municipios.manterTiposVeiculoRotaAction.findLookupRotaColetaEntrega" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota" dataType="integer" labelWidth="15%" label="numeroRota" size="3" maxLength="3" width="37%" action="/municipios/manterRotaColetaEntrega" exactMatch="true" >
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial"  blankFill="false"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" />
        </adsm:lookup>
        
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" label="tipoMeioTransporte" 
			service="lms.municipios.manterTiposVeiculoRotaAction.findTipoMeioTransporte" 
			optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" 
			boxWidth="200" labelWidth="20%" width="25%" />
		
		<adsm:range label="vigencia" labelWidth="15%" width="85%" required="false" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaTipoMeioTransporte" />
			<adsm:resetButton />
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idRotaTipoMeioTransporte" rows="12" property="rotaTipoMeioTransporte" defaultOrder="rotaColetaEntrega_filial_.sgFilial,rotaColetaEntrega_.nrRota, tipoMeioTransporte_.dsTipoMeioTransporte, dtVigenciaInicial" gridHeight="200" unique="true">
		<adsm:gridColumnGroup customSeparator=" - ">
				<adsm:gridColumn title="rota" property="rotaColetaEntrega.nrRota" width="150"/>
				<adsm:gridColumn title="" property="rotaColetaEntrega.dsRota" width="50"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" width="150"/>		
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="90" />
		<adsm:buttonBar> 
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado; 
	
	function findFilialCallBack_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setValues();
		}
	}

	function setValues() {
		if (idFilialLogado != undefined &&
			sgFilialLogado != undefined &&
			nmFilialLogado != undefined &&
			document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink != "true") {
			setElementValue("rotaColetaEntrega.filial.idFilial",idFilialLogado);
			setElementValue("rotaColetaEntrega.filial.sgFilial",sgFilialLogado);
			setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",nmFilialLogado);
		}
	}
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			setValues();
	}
	
	
</script>
