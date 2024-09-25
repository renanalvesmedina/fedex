<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.motivoAberturaNcService" >
	<adsm:form action="/rnc/manterMotivosAberturaNaoConformidade" idProperty="idMotivoAberturaNc">
		<adsm:textbox dataType="text" property="dsMotivoAbertura" label="descricao" size="40" maxLength="30" labelWidth="23%" width="77%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="23%" width="77%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivosAberturaNC"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="motivosAberturaNC" idProperty="idMotivoAberturaNc" defaultOrder="dsMotivoAbertura:asc" rows="12">
		<adsm:gridColumn property="dsMotivoAbertura" title="descricao" width="30%" />
		<adsm:gridColumn property="tpMotivo" title="tipoMotivo" isDomain="true" width="15%" />
		<adsm:gridColumn property="blExigeDocServico" title="exigeDocumentoServico" renderMode="image-check" width="13%" />
		<adsm:gridColumn property="blPermiteIndenizacao" title="permiteIndenizacao" renderMode="image-check" width="10%" />
		<adsm:gridColumn property="blExigeValor" title="exigeValor" renderMode="image-check" width="8%" />
		<adsm:gridColumn property="blExigeQtdVolumes" title="exigeQuantidadeVolumes" renderMode="image-check" width="14%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		desabilitaTabDescricaoPadrao(true);
	}
}

function desabilitaTabDescricaoPadrao(disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("descricoesPadrao", disabled);
}
</script>