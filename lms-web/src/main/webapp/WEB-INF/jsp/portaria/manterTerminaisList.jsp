<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTerminais" service="lms.portaria.manterTerminaisAction">
	<adsm:form action="portaria/manterTerminais">
		<adsm:hidden property="tpEmpresa" serializable="false" value="M"/>
		<adsm:lookup service="lms.portaria.manterTerminaisAction.findLookupFilial" dataType="text"
					 property="filial"  criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
					 width="85%" action="/municipios/manterFiliais" idProperty="idFilial">
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true" serializable="false"/>
        	<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        </adsm:lookup>
        
        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="52" maxLength="50" label="descricao" width="65%"/>
	   
		<adsm:range label="vigencia" width="65%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="Terminal"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTerminal" property="Terminal" selectionMode="check" unique="true" rows="10" gridHeight="200">		
		<adsm:gridColumn title="filial" property="filial.sgFilial" width="4%" />	
		<adsm:gridColumn title="terminal" property="pessoa.nmPessoa" />				
		<adsm:gridColumn title="areaTotal" dataType="decimal" mask="#,###,###,###,###,##0.00" property="nrAreaTotal" width="16%" unit="m2"/>
		<adsm:gridColumn title="areaArmazenagem" dataType="decimal" mask="#,###,###,###,###,##0.00" property="nrAreaArmazenagem" width="16%" unit="m2" />
		<adsm:gridColumn title="numeroDocas" dataType="integer" property="nrDocas" width="7%" />
		<adsm:gridColumn title="numeroBoxes" dataType="integer" property="nrBoxes" width="7%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="9%" dataType="JTDate" />			
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="9%" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
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

	var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
	xmit({serviceDataObjects:[sdo]});
	
	function setValues() {
		if (idFilialLogado != undefined &&
			sgFilialLogado != undefined &&
			nmFilialLogado != undefined &&
			document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",idFilialLogado);
			setElementValue("filial.sgFilial",sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia",nmFilialLogado);
		}
	}
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") 
			setValues(); 
	}		

	
//-->
</script>