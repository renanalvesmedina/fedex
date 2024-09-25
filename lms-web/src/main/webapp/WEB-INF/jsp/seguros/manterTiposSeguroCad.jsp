<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.tipoSeguroService">
	
	<adsm:form action="/seguros/manterTiposSeguro" idProperty="idTipoSeguro">
		<adsm:textbox dataType="text" property="sgTipo" label="sigla" maxLength="10" size="10" width="85%" required="true"/>
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" maxLength="100" size="80" width="85%" required="true"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" renderOptions="true"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" renderOptions="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="15%" width="35%" required="true" renderOptions="true"/>
		<adsm:checkbox property="blEnvolveCarga" label="tipoSeguroEnvolveCarga" labelWidth="20%" width="30%" />
		<adsm:pairedListbox property="seguroTipoSinistros" 
							service="lms.seguros.tipoSinistroService.find"
							size="6" boxWidth="210" label="tipoSinistro" width="85%" 
							showOrderControls="true"
							optionLabelProperty="dsTipo" optionProperty="idTipoSinistro" />
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">

	function initWindow(eventObj) {
		if ((eventObj.name == "cleanButton_click") || (eventObj.name == "tab_click") || (eventObj.name == "newButton_click")) {
			document.getElementById("blEnvolveCarga").checked = true;
		}
	} 	

</script>