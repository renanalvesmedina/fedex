<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.motivoAberturaNcService" >
	<adsm:form action="/rnc/manterMotivosAberturaNaoConformidade" idProperty="idMotivoAberturaNc" >
		<adsm:textbox dataType="text" property="dsMotivoAbertura" label="descricao" size="40" maxLength="30" labelWidth="23%" width="77%" required="true" />
		<adsm:checkbox property="blExigeDocServico" label="exigeDocumentoServico" labelWidth="23%" width="77%" />
		<adsm:checkbox property="blPermiteIndenizacao" label="permiteIndenizacao" labelWidth="23%" width="77%" />
		<adsm:checkbox property="blExigeValor" label="exigeValor" labelWidth="23%" width="77%" />
		<adsm:checkbox property="blExigeQtdVolumes" label="exigeQuantidadeVolumes" labelWidth="23%" width="77%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="23%" width="77%" required="true" renderOptions="true" />
		<adsm:combobox property="tpMotivo" label="tipoMotivo" domain="DM_TIPO_MOTIVO_NC" labelWidth="23%" width="77%" required="true" renderOptions="true" />
		<adsm:buttonBar>
			<adsm:button caption="setores" action="/rnc/manterSetoresMotivoAberturaNaoConformidade" cmd="main">
				<adsm:linkProperty src="idMotivoAberturaNc, dsMotivoAbertura" target="motivoAberturaNc.idMotivoAberturaNc" />
			</adsm:button>
			<adsm:button caption="motivosDisposicao" action="/rnc/manterMotivosDisposicaoMotivoAbertura" cmd="main">
				<adsm:linkProperty src="idMotivoAberturaNc, dsMotivoAbertura" target="motivoAberturaNc.idMotivoAberturaNc" />
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

function initWindow(eventObj) {
	var event = eventObj.name;
	if (event == "removeButton" || event == "newButton_click") {
		desabilitaTabDescricaoPadrao(true);
	}
	else
	if (event == "gridRow_click" || event == "storeButton") { 
		desabilitaTabDescricaoPadrao(false);
	}
}

function desabilitaTabDescricaoPadrao(disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("descricoesPadrao", disabled);
}

function verificaDescricaoPadrao(){
	var idMotivoAberturaNc = getElementValue("idMotivoAberturaNc");
	if (idMotivoAberturaNc != "" && idMotivoAberturaNc != undefined){
		desabilitaTabDescricaoPadrao(false);
	} else {
		desabilitaTabDescricaoPadrao(true);
	}
}
</script>