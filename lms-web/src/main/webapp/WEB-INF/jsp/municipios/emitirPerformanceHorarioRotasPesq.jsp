<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirPerformanceHorarioRotas">
	<adsm:form action="/municipios/emitirPerformanceHorarioRotas">
		<adsm:range label="periodoEmissao" labelWidth="17%" required="true" width="83%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
        <adsm:lookup label="filialOrigem" labelWidth="17%" dataType="text" size="2" maxLength="3" width="83%"
				     service="lms.municipios.emitirPerformanceHorarioRotasAction.findLookupFilial" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" serializable="true">
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			      	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" disabled="true" serializable="false"/> 					
		</adsm:lookup>

    	<adsm:lookup dataType="text" property="rota" idProperty="idRota" criteriaProperty="dsRota"  exactMatch="false"
			service="lms.municipios.emitirPostosPassagemRotaViagemAction.findLookupRota" 
			action="municipios/manterPostosPassagemRotasViagem" cmd="list" minLengthForAutoPopUpSearch="3"
		  	label="rota" labelWidth="17%" width="83%" size="34" maxLength="60"/>

        <adsm:combobox property="filialOrigem.idFilial" service="lms.municipios.emitirPerformanceHorarioRotasAction.findLookupRotaFilial" optionLabelProperty="siglaNomeFilial" optionProperty="idFilial" label="filialOrigemTrecho" labelWidth="17%" width="83%" boxWidth="190">
        	<adsm:propertyMapping criteriaProperty="rota.idRota" modelProperty="rota.idRota"/>
        </adsm:combobox>
                
        <adsm:combobox property="filialDestino.idFilial" service="lms.municipios.emitirPerformanceHorarioRotasAction.findLookupRotaFilial" optionLabelProperty="siglaNomeFilial" optionProperty="idFilial" label="filialDestinoTrecho" labelWidth="17%" width="83%" boxWidth="190">
        	<adsm:propertyMapping criteriaProperty="rota.idRota" modelProperty="rota.idRota"/>
        </adsm:combobox>

		<adsm:combobox property="analisePor" domain="DM_TIPO_ANALISE_PHR_VIAGEM" label="analisePor" labelWidth="17%" width="83%" boxWidth="110">
			<adsm:propertyMapping relatedProperty="analisePorDescription" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="analisePorDescription"/>
		<adsm:combobox property="somenteAtraso" domain="DM_SIM_NAO" label="somenteAtraso" labelWidth="17%" width="83%" boxWidth="110">
			<adsm:propertyMapping relatedProperty="somenteAtrasoDescription" modelProperty="description"/>
		</adsm:combobox>

		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio"
				labelWidth="17%" width="83%" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:hidden property="somenteAtrasoDescription"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirPerformanceHorarioRotasViagemService"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
	document.getElementById("filial.sgFilial").serializable = "true";
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

	var sdo = createServiceDataObject("lms.contratacaoveiculos.meioTransporteService.findFilialUsuarioLogado","findFilialCallBack",null);
	xmit({serviceDataObjects:[sdo]});
	
	function setValues() {
		if (idFilialLogado != undefined &&
			sgFilialLogado != undefined &&
			nmFilialLogado != undefined) {
			setElementValue("filial.idFilial",idFilialLogado);
			setElementValue("filial.sgFilial",sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia",nmFilialLogado);
		}
		
	}
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			setValues();
		
	}
	
</script>