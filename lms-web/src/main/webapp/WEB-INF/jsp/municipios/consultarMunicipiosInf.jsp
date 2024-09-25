<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMunicipiosAction">
	<adsm:form action="/municipios/consultarMunicipios" idProperty="idMunicipio" onDataLoadCallBack="onDataLoadConsultarMunicipios">
				
		<adsm:textbox dataType="text" property="nmMunicipio" label="municipio" disabled="true" maxLength="30" size="37" labelWidth="16%" width="34%"/>
		
		<adsm:textbox property="sgUnidadeFederativa" dataType="text" label="uf" labelWidth="16%" width="30%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="pais" label="pais" disabled="true" maxLength="30" size="37" labelWidth="16%" width="34%"/>

		<adsm:textbox dataType="text" property="nrCep" disabled="true" label="cep" maxLength="9" labelWidth="16%" width="34%"/>

		<adsm:textbox dataType="integer" property="cdIbge" label="codIBGE" disabled="true" maxLength="8" size="8" labelWidth="16%" width="34%"/>
		<adsm:textbox dataType="integer" property="nrPopulacao" label="populacao" disabled="true" maxLength="8" size="9" labelWidth="16%" width="34%"/>

		<adsm:textbox dataType="integer" property="cdEstadual" disabled="true" label="codEstadual" maxLength="5" size="9" labelWidth="16%" width="70%"/>

		<adsm:checkbox disabled="true" property="blDistrito" label="indDistrito" labelWidth="16%" width="34%" cellStyle="vertical-align:bottom"/>
		<adsm:textbox dataType="text" property="municipioDistrito" disabled="true" label="municDistrito" maxLength="30" size="37" labelWidth="16%" width="34%"/>
		
		<adsm:listbox 
		optionLabelProperty="cepInicialFinal" 
		optionProperty="cepInicialFinal" 
		property="intervaloCeps" size="6" boxWidth="150" label="cep" labelWidth="16%" width="84%" />
		
				
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="16%" width="84%" disabled="true"/>
		
		<adsm:buttonBar/>
	</adsm:form>
</adsm:window>
<script>

getTab(document).properties.ignoreChangedState=true;

function onDataLoadConsultarMunicipios_cb(data,exception){
   	onDataLoad_cb(data,exception);
   	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.municipios.consultarMunicipiosAction.findMunicipioFilialVigenteByMunicipio", "setaAbaAtendimentoFeriados", {idMunicipio:getElementValue("idMunicipio")}));
  	xmit();
}

function setaAbaAtendimentoFeriados_cb(data,exception){
    var existeMunicipioFilialVigente = getNestedBeanPropertyValue(data,"existeMunicipioFilialVigente");
    var existeFeriadoVigente = getNestedBeanPropertyValue(data,"existeFeriadoVigente");
       
    var tabGroup = getTabGroup(this.document);
    
    if(existeMunicipioFilialVigente == "true")
       	tabGroup.setDisabledTab("aten", false);
    else 
		tabGroup.setDisabledTab("aten", true);
		
	if(existeFeriadoVigente == "true")
		tabGroup.setDisabledTab("feriados", false);
		
	else if(existeFeriadoVigente == "false")
		tabGroup.setDisabledTab("feriados", true);	
		
		
}




</script>