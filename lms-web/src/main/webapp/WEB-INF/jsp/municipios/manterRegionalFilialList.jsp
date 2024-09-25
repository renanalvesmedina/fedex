<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
//vem do linkProperty de regional
function setaVigenciaInativaComboRegional_cb(data,error){
	onPageLoad_cb(data,error);
	if(document.getElementById("idRegionalF").value != ''){
	  document.getElementById("regional.idRegional").value = document.getElementById("idRegionalF").value;
	  document.getElementById("regional.idRegional").masterLink = "true";
	  setDisabled("regional.idRegional", true);
	}else{
	 	setDisabled("regional.idRegional", false);
	}

	//Anderson Machado 06/04/2006
	if (getElementValue("filial.sgFilial")!="") {
		lookupChange({e:document.forms[0].elements["filial.idFilial"],forceChange:true});
		document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";
		setDisabled("filial.idFilial",true);
	}
	setFocusOnFirstFocusableField();
}
 
function setaMascaraVigenciaRegional_cb(data, error) {
	var i;
	var vi = document.getElementById("regional.dtVigenciaInicial");
	var vf = document.getElementById("regional.dtVigenciaFinal");	
	for (i = 0; i < data.length; i++) {
		setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
		setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
	}
	if (error != undefined) {
		alert(error);
		return false;
	}
	regional_idRegional_cb(data);
}

</script>
<adsm:window title="consultarRegionalFilial" service="lms.municipios.regionalFilialService" onPageLoadCallBack="setaVigenciaInativaComboRegional">
<adsm:form action="/municipios/manterRegionalFilial" idProperty="idRegionalFilial" >
	   
	   <adsm:hidden property="dsRegionalF" serializable="false"/>
	   <adsm:hidden property="idRegionalF" serializable="false"/>
	   <adsm:hidden  property="tpEmpresa" value="M" serializable="false"/>
	   
	    <adsm:combobox 
	    label="regional" 
	    property="regional.idRegional" 
	    optionLabelProperty="siglaDescricao" 
	    optionProperty="idRegional" 
	    service="lms.municipios.regionalService.findRegional" 
	    labelWidth="15%" width="85%" boxWidth="170" onDataLoadCallBack="setaMascaraVigenciaRegional">
	         <adsm:propertyMapping relatedProperty="regional.usuario.nmUsuario" modelProperty="usuario.nmUsuario"/>
   	         <adsm:propertyMapping relatedProperty="regional.dtVigenciaInicial" modelProperty="dtVigenciaInicial"/>
	         <adsm:propertyMapping relatedProperty="regional.dtVigenciaFinal"   modelProperty="dtVigenciaFinal"/>
       </adsm:combobox>
       
		<adsm:range label="vigencia" width="35%">
             <adsm:textbox dataType="JTDate" cellStyle="vertical-align:bottom;" property="regional.dtVigenciaInicial" picker="false" disabled="true" serializable="false"/>
             <adsm:textbox dataType="JTDate" cellStyle="vertical-align:bottom;" property="regional.dtVigenciaFinal" picker="false" disabled="true" serializable="false"/>
        </adsm:range>
       <adsm:textbox dataType="text" label="responsavelRegional" property="regional.usuario.nmUsuario" size="30" disabled="true" labelWidth="15%" width="35%" serializable="false" cellStyle="vertical-align:bottom;"/>
       
	   <adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" width="10%" style="width:45px" action="/municipios/manterFiliais" service="lms.municipios.filialService.findLookup">
                  <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
       </adsm:lookup>
       <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" width="25%" />
        
        
		<adsm:combobox property="blSedeRegional" label="sedeRegional" domain="DM_SIM_NAO" width="10%"/>
		
		<adsm:range label="vigencia" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="regionalFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRegionalFilial" property="regionalFilial" selectionMode="check" gridHeight="190" unique="true" rows="9" defaultOrder="regional_.sgRegional,filial_.sgFilial,dtVigenciaInicial" scrollBars="horizontal" >
		<adsm:gridColumn title="siglaRegional" property="siglaDescricaoRegional" width="185"/>	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicialRegional" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinalRegional" width="90" dataType="JTDate"/>
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn property="sgFilial" width="50" title="siglaFilial"/>	
			<adsm:gridColumn property="nmFantasia" width="135" title=""/>	
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="sede" property="blSedeRegional" width="80" renderMode="image-check"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
