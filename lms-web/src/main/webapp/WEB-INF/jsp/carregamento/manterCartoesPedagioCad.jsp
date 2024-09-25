<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterCartoesPedagioAction">
	<adsm:form action="/carregamento/manterCartoesPedagio" idProperty="idCartaoPedagio" onDataLoadCallBack="validaData" >
	
		<adsm:hidden property="dtValidadeMenorDtAtual" serializable="false" />
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="tpIdentificacao" serializable="false" />
		
		
		<adsm:lookup property="operadoraCartaoPedagio"
					 label="operadoraCartaoPedagio" dataType="text" 
					 idProperty="idOperadoraCartaoPedagio" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 service="lms.carregamento.manterCartoesPedagioAction.findLookupOperadoraCartaoPedagio" 
					 action="/carregamento/manterOperadorasCartaoPedagio" 
					 size="18" maxLength="18" labelWidth="24%" width="76%" required="true" >
        	<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
        	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="operadoraCartaoPedagio.pessoa.nmPessoa" />
        	<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao.value" relatedProperty="tpIdentificacao" />
        	<adsm:propertyMapping modelProperty="nrIdentificacao" criteriaProperty="operadoraCartaoPedagio.pessoa.nrIdentificacao" inlineQuery="false"/>
        	<adsm:propertyMapping modelProperty="tpIdentificacao" criteriaProperty="tpIdentificacao" inlineQuery="false"/>
        	
            <adsm:textbox dataType="text" property="operadoraCartaoPedagio.pessoa.nmPessoa" size="40" maxLength="50" disabled="true" />
        </adsm:lookup>

		<adsm:textbox dataType="integer" property="nrCartao" label="numeroCartaoPedagio" maxLength="16" size="18%" labelWidth="24%" width="76%" required="true" />
		<adsm:textbox dataType="JTDate" property="dtValidade" label="validade" labelWidth="24%" width="76%" required="true" />
		
		<adsm:checkbox property="blCartaoTerceiro" label="cartaoTerceiro" labelWidth="24%" width="76%"/> 

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<script>
			var labelOperadoraCartaoPedagio = '<adsm:label key="operadoraCartaoPedagio"/>';
		</script>
		
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name != "gridRow_click") {
		setDisabled( document.getElementById("operadoraCartaoPedagio.idOperadoraCartaoPedagio"), false);
		setDisabled("nrCartao", false);
		if (eventObj.name != "storeButton") {
			setFocusOnFirstFocusableField();
		}
	}
}

function validaData_cb(data, error) {
	onDataLoad_cb(data, error);
	if ( getElementValue('dtValidadeMenorDtAtual') == "true" ) {
		setDisabled( document.getElementById("operadoraCartaoPedagio.idOperadoraCartaoPedagio"), true);
		setDisabled("nrCartao", true);
	}
	setFocusOnFirstFocusableField();
}

document.getElementById("operadoraCartaoPedagio.pessoa.nrIdentificacao").label = labelOperadoraCartaoPedagio;
</script>