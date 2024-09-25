<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRegioesColetaEntregaFiliais"
		service="lms.municipios.regiaoColetaEntregaFilService" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/municipios/manterRegioesColetaEntregaFiliais" idProperty="idRegiaoColetaEntregaFil">
		
		<adsm:hidden property="idFilialSessao" serializable="false" />
		<adsm:hidden property="sgFilialSessao" serializable="false" />
		<adsm:hidden property="nmFilialSessao" serializable="false" />
		
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"></adsm:hidden>
		<adsm:lookup property="filial" criteriaProperty="sgFilial" idProperty="idFilial"
				service="lms.municipios.filialService.findLookup" dataType="text" label="filial" size="3" maxLength="3"
				labelWidth="20%" width="80%" action="/municipios/manterFiliais" exactMatch="false"
				minLengthForAutoPopUpSearch="3" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" /> 
        </adsm:lookup>
      	
 	  	<adsm:textbox dataType="text" property="dsRegiaoColetaEntregaFil" label="descricaoRegiao" size="60" labelWidth="20%" width="80%" maxLength="60" /> 
 	  	
       	<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="regiaoColetaEntregaFil"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRegiaoColetaEntregaFil" property="regiaoColetaEntregaFil" selectionMode="check" gridHeight="200" unique="true" rows="12">
	    <adsm:gridColumnGroup separatorType="FILIAL">
	    	<adsm:gridColumn title="filial" property="filial.sgFilial" width="150"/>	
	    	<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="100"/>	 
	    </adsm:gridColumnGroup>
		
		<adsm:gridColumn title="descricaoRegiao" property="dsRegiaoColetaEntregaFil" width="250"/>	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--

	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;

	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			findInfoUsuarioLogado();
		}
	}
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.municipios.manterRegioesColetaEntregaFiliaisAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
		populateInfoUsuarioLogado();
	}
	
	function populateInfoUsuarioLogado() {
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",getElementValue("idFilialSessao"));
			setElementValue("filial.sgFilial",getElementValue("sgFilialSessao"));
			setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateInfoUsuarioLogado();
		}
	}

//-->
</script>