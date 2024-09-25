<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.gerarFluxoCargaFilialNovaAction" >
	<adsm:form action="/municipios/gerarFluxoCargaFilialNova" idProperty="idFluxoFilial">
	 
		<adsm:combobox property="tpGeracao" label="tipoGeracao" domain="DM_TIPO_GERACAO_FLUXO" required="true" width="85%" />
		
		<adsm:hidden property="gerar" value="false"/>
		
		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3" 
				service="lms.municipios.gerarFluxoCargaFilialNovaAction.findLookupFilial" dataType="text" label="filialNova" size="3"
				action="/municipios/manterFiliais" labelWidth="15%" width="85%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" disabled="false">

			<adsm:propertyMapping relatedProperty="lastHistoricoFilial.tpFilial.value" modelProperty="lastHistoricoFilial.tpFilial.value" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.siglaNomeFilial" modelProperty="siglaNomeFilial" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />			
			<adsm:hidden property="filial.siglaNomeFilial"/>
			<adsm:hidden property="lastHistoricoFilial.tpFilial.value" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico"
				service="lms.municipios.gerarFluxoCargaFilialNovaAction.findServico" onlyActiveValues="true"
				label="servico" labelWidth="15%" width="85%" boxWidth="231"/>
				
		<adsm:range label="vigencia">		
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        <adsm:i18nLabels>
        	<adsm:include key="LMS-29131"/>
        </adsm:i18nLabels>
	</adsm:form>
	<adsm:buttonBar>
		<adsm:button id="gerarButton" caption="gerar" onclick="gerar();" disabled="false"/>
		<adsm:resetButton/>
	</adsm:buttonBar>
</adsm:window>

<script>
	function gerar() {
		var tabGroup = getTabGroup(this.document);
		var tabFiliais = tabGroup.getTab("filiais");		
		
		var tab = getTab(document);
		var valid = tab.validate({name:"storeButton_click"});
		if (valid == false) {
			return false;
		}
		if (getElementValue("lastHistoricoFilial.tpFilial.value") == "MA") {
			alert(i18NLabel.getLabel("LMS-29131"));
			return;
		}
		tabGroup.setDisabledTab('filiais',false);
		setElementValue("gerar", "true");
 		tabGroup.selectTab('filiais',{name:'tab_click'});		
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			var tabGroup = getTabGroup(this.document);
			var tabFiliais = tabGroup.getTab("filiais");		
			newButtonScript(tabFiliais.getDocument(), true, {name:'newButton_click'});
			tabFiliais.getElementById("filial.dataTable").gridDefinition.resetGrid();
			
			tabFiliais.getElementById("servico.description").value = "";
			tabFiliais.getElementById("filialByIdFilialOrigem.siglaNomeFilial").value = "";
			tabFiliais.getElementById("filialByIdFilialDestino.siglaNomeFilial").value = "";
			tabFiliais.getDocument().parentWindow.desabilitaCampos(true);
		}
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("filiais",true);
		}
		
	}
	
			
	
      
	

</script>