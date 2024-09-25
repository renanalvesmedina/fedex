<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tabelaprecos.simularNovosPrecosCiasAereasReajustesEspecificosAction">
	
	<adsm:form 
		action="/tabelaPrecos/simularNovosPrecosCiasAereas"
		idProperty="idReajuste">
		
		<adsm:hidden property="geral.tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="aeroportoOrigem.sgAeroporto" />
		<adsm:hidden property="aeroportoDestino.sgAeroporto" />
		<adsm:hidden property="reajuste.idReajuste" />
		
	
		<%-----------------------------%>
		<%-- AEROPORTO ORIGEM LOOKUP --%>
		<%-----------------------------%>
		<adsm:lookup 
			idProperty="idAeroporto" 
			dataType="text" 
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasReajustesEspecificosAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			criteriaProperty="sgAeroporto"
			property="aeroportoByIdAeroportoOrigem" 
			size="5" 
			maxLength="3"
			label="aeroportoOrigem" 
			width="80%">
			
			<adsm:propertyMapping 
				criteriaProperty="geral.tpSituacao" 
				modelProperty="tpSituacao" />
				
		   	<adsm:propertyMapping 
		   		relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" />
			
			<adsm:propertyMapping 
		   		relatedProperty="aeroportoOrigem.sgAeroporto" 
				modelProperty="sgAeroporto" />
				
            <adsm:textbox 
            	dataType="text" 
            	size="30"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				maxLength="30" 
				disabled="true" />
				
        </adsm:lookup>
        
        <%------------------------------%>
		<%-- AEROPORTO DESTINO LOOKUP --%>
		<%------------------------------%>
		<adsm:lookup 
			idProperty="idAeroporto" 
			dataType="text" 
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasReajustesEspecificosAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			criteriaProperty="sgAeroporto"
			property="aeroportoByIdAeroportoDestino" 
			size="5" 
			maxLength="3"
			label="aeroportoDestino" 
			width="80%">
			
			<adsm:propertyMapping 
				criteriaProperty="geral.tpSituacao" 
				modelProperty="tpSituacao" />
				
		   	<adsm:propertyMapping 
		   		relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" />
			
			<adsm:propertyMapping 
		   		relatedProperty="aeroportoDestino.sgAeroporto" 
				modelProperty="sgAeroporto" />
				
            <adsm:textbox 
            	dataType="text" 
            	size="30"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				maxLength="30" 
				disabled="true" />
				
        </adsm:lookup>
        
        <adsm:textbox 
        	dataType="percent" 
        	property="pcDesconto" 
        	label="percentualDesconto" 
        	maxLength="10" 
        	minValue="0.01"
        	size="10"
        	required="true"/>		
        	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton 
				service="lms.tabelaprecos.simularNovosPrecosCiasAereasReajustesEspecificosAction.storeInSession"
				callbackProperty="afterStore"/>
			<adsm:newButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		idProperty="idReajuste"
		property="reajustesList"
		unique="true"
		detailFrameName="reajEsp"
		rows="11">
		
		<adsm:gridColumn 
			title="aeroportoOrigem" 
			property="aeroportoByIdAeroportoOrigem.siglaDescricao" 
			width="40%"/>
			
		<adsm:gridColumn 
			title="aeroportoDestino" 
			property="aeroportoByIdAeroportoDestino.siglaDescricao" 
			width="40%"/>
			
		<adsm:gridColumn 
			title="percentualDesconto" 
			property="pcDesconto" 
			dataType="percent"
			width="20%" 
			align="right"/>

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
function initWindow(eventObj) {
	if (eventObj.name == "tab_load" || eventObj.name == "tab_click") {
		populaGrid();
	}
	if (eventObj.name == "removeButton_grid"){
		newButtonScript(document, true, {name:'newButton_click'});
	}
}

function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (errorMsg != undefined) {
		alert(errorMsg);
		return false;
	}
	setElementValue("reajuste.idReajuste", data._value);
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
	populaGrid();
}

function populaGrid() {
	reajustesListGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]), true);
	newButtonScript(document, true, {name:'newButton_click'});
}
//-->
</script>
