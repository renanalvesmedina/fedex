<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.expedicao.digitarPreAWBAction">
	<adsm:form 
		action="/expedicao/digitarPreAWB"
		service="lms.expedicao.digitarPreAWBAction">
		
		<adsm:hidden property="empresa.pessoa.nmPessoa" serializable="true"/>
		<adsm:hidden property="empresa.idEmpresa" serializable="true"/>
		<adsm:hidden property="sgFilial" serializable="true"/>
		<adsm:hidden property="tela" serializable="false"/>

		<%----------------%>
        <%-- NRAWB TEXT --%>
        <%----------------%>
		<adsm:textbox 
			dataType="integer" 
			label="numeroPreAWB" 
			property="idAwb" 
			size="13" 
			maxLength="10" 
			labelWidth="17%" 
			width="33%" />
		
		<%---------------------%>
        <%-- CIA AEREA COMBO --%>
        <%---------------------%>
		<adsm:combobox 
			property="ciaFilialMercurio.idCiaFilialMercurio" 
			optionLabelProperty="empresa.pessoa.nmPessoa" 
 			optionProperty="idCiaFilialMercurio" 
 			service="lms.expedicao.digitarAWBCiasAereasAction.findCiaAerea" 
 			label="ciaAerea"
 			width="33%"
 			labelWidth="17%"
 			serializable="true"
 			boxWidth="260">
 			
		    <adsm:propertyMapping 
		    	relatedProperty="sgFilial" 
		    	modelProperty="filial.sgFilial"/>
		    	
			<adsm:propertyMapping 
				relatedProperty="empresa.idEmpresa" 
				modelProperty="empresa.idEmpresa"/>
				
			<adsm:propertyMapping 
				relatedProperty="empresa.pessoa.nmPessoa" 
				modelProperty="empresa.pessoa.nmPessoa"/>
			
		</adsm:combobox>
        
        <%-----------------------------%>
        <%-- AEROPORTO ORIGEM LOOKUP --%>
        <%-----------------------------%>
        <adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarPreAWBAction.findAeroporto"
			dataType="text"
			property="aeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeOrigem"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%">
			
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoOrigem.pessoa.nmPessoa"/>
			
			<adsm:textbox
				dataType="text"
				property="aeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="26"
				maxLength="50"
				disabled="true"/>
				
		</adsm:lookup>

		<%------------------------------%>
        <%-- AEROPORTO DESTINO LOOKUP --%>
        <%------------------------------%>
        <adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarPreAWBAction.findAeroporto"
			dataType="text"
			property="aeroportoDestino"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%">
			
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoDestino.pessoa.nmPessoa"/>
			
			<adsm:textbox
				dataType="text"
				property="aeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="26"
				maxLength="50"
				disabled="true"/>
				
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="awbList"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		property="awbList"
		idProperty="idAwb" 
		gridHeight="250" 
		unique="true"
		onRowClick="awbListRowClick();"
		detailFrameName="geracaoManutencao"
		service="lms.expedicao.digitarPreAWBAction.findPaginated"
		rowCountService="lms.expedicao.digitarPreAWBAction.getRowCount">
		
		<adsm:gridColumn 
			title="numeroPreAWB" 
			property="awb.idAwb" 
			width="15%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="ciaAerea" 
			property="ciaAerea.nmPessoa" 
			width="28%" />
			
		<adsm:gridColumn 
			title="aeroportoOrigem" 
			property="aeroportoOrigem.sgAeroporto" 
			width="10%" />
			
		<adsm:gridColumn 
			title="aeroportoDestino" 
			property="aeroportoDestino.sgAeroporto" 
			width="10%"/>
			
		<adsm:gridColumn 
			title="dataHoraDigitacao" 
			property="awb.dhDigitacao" 
			width="20%" 
			align="center" 
			dataType="JTDateTimeZone"/>
			
		<adsm:gridColumn 
			title="valorFreteReais" 
			property="awb.vlFrete" 
			width="17%" 
			align="right" 
			dataType="currency"/>
		
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
function awbListRowClick() {
	// desabilita a aba de consolidacao de cargas
	changeConsolidacaoCargasTabStatus(true);
	
	var frame = parent.document.frames["geracaoManutencao_iframe"];
	frame.setOrigem("list");
}

function initWindow(event) {
	if(event.name == "tab_load" || event.name == "tab_click") {
		// habilita a aba de consolidacao de cargas
		changeConsolidacaoCargasTabStatus(false);
	}
	
	if (event.name == "tab_click") {
		limpaAbas();
	}
	
	var service = "lms.expedicao.digitarPreAWBAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
}

function changeConsolidacaoCargasTabStatus(status) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("consolidacaoCargas", status);
}

function limpaAbas() {
	var tabGroup = getTabGroup(document);
	var tabConsolidacao = tabGroup.getTab("consolidacaoCargas");
	var tabGeracao = tabGroup.getTab("geracaoManutencao");
	cleanButtonScript(tabConsolidacao.getDocument());
	cleanButtonScript(tabGeracao.getDocument());
}
</script>
