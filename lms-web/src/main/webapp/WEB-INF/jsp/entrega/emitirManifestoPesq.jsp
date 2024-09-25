<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.emitirManifestoAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/entrega/emitirManifesto" >
 
		<adsm:hidden property="empresa.tpEmpresa" value="M"/>
		<adsm:hidden property="manifesto.tpStatusManifesto" value="ME"/>		
	
		<adsm:lookup dataType="text" 
        		property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.entrega.emitirManifestoAction.findLookupFilial"
				action="/municipios/manterFiliais" onchange="return filialChange(this);"
				label="filial" labelWidth="17%" width="83%" size="3" maxLength="3" exactMatch="true" required="true" disabled="true">
			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="manifesto.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		
		<adsm:combobox property="tpManifestoEntrega" domain="DM_TIPO_MANIFESTO_ENTREGA"
				label="tipoManifesto" labelWidth="17%" width="33%" renderOptions="true" onlyActiveValues="true"/>
		
		<adsm:lookup dataType="integer" property="controleCarga.rotaColetaEntrega" 
				idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
				service="lms.entrega.emitirManifestoAction.findLookupRotaColetaEntrega"
				action="/municipios/manterRotaColetaEntrega"
				label="rotaColetaEntrega" size="3" maxLength="3" labelWidth="17%" width="33%"
				cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
            <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
            <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia"
            		modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
            
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false" />            
        </adsm:lookup>
		
		<adsm:textbox dataType="text" property="manifestoEntrega.filial.sgFilial"
   				label="manifestoEntrega" labelWidth="17%" width="33%" size="3" disabled="true" >
   			<adsm:lookup dataType="integer" 
	   				property="manifestoEntrega" idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" 
	   				service="lms.entrega.emitirManifestoAction.findLookupManifestoEntrega"
	   				action="/entrega/consultarManifestosEntrega" cmd="lookup" popupLabel="pesquisarManifestoEntrega"
	   				size="7" maxLength="8" disabled="true" mask="00000000" exactMatch="true" >
   				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="filial.sgFilial"
   						modelProperty="filial.sgFilial" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia"
   						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   						
			<adsm:propertyMapping criteriaProperty="manifesto.tpStatusManifesto" modelProperty="manifesto.tpStatusManifesto" />   						
   			</adsm:lookup>
   		</adsm:textbox>
		
		<adsm:hidden property="tpManifestoLookup" value="E" serializable="false" />
		<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial"
   				label="preManifesto" labelWidth="17%" width="33%" size="3" disabled="true" >
			<adsm:lookup dataType="integer"
		    		 property="manifesto" idProperty="idManifesto" criteriaProperty="nrPreManifesto"
			         service="lms.entrega.emitirManifestoAction.findLookupPreManifesto" 
			         action="carregamento/manterGerarPreManifesto"
			         disabled="false" mask="00000000" popupLabel="pesquisarPreManifesto" >
			    <adsm:propertyMapping criteriaProperty="tpManifestoLookup" modelProperty="tpManifesto" />
			        
			    <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" />
   				<adsm:propertyMapping criteriaProperty="filial.sgFilial"
   						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
			</adsm:lookup>
		</adsm:textbox >
		
		<adsm:buttonBar freeLayout="true">     
			<adsm:findButton callbackProperty="manifesto" />
			<adsm:resetButton />
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid property="manifesto" idProperty="idManifesto" 
			service="lms.entrega.emitirManifestoAction.findPaginatedPreManifesto"
			rowCountService="lms.entrega.emitirManifestoAction.getRowCountPreManifesto"
			selectionMode="none" unique="true" rows="11" onRowClick="onRowClickDef"
			scrollBars="horizontal" gridHeight="221" >
			
		<adsm:gridColumn title="tipoManifesto" property="tpManifesto" isDomain="true" width="100" />
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numeroPreManifesto" property="sgFilialManifesto" width="75" />
			<adsm:gridColumn title="" property="nrPreManifesto" dataType="integer" mask="00000000" width="75" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="sgFilialManifestoEntrega" width="75" />
			<adsm:gridColumn title="" property="nrManifestoEntrega" dataType="integer" mask="00000000" width="75" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="dataHoraEmissao" property="dhEmissao" width="140" dataType="JTDateTimeZone" />
		 
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="rota" property="nrRota" dataType="integer" width="120" align="left"/>
			<adsm:gridColumn title="" property="dsRota" width="120" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="quantidadeEntregas" property="qtEntregas" width="150" dataType="integer" />
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="solicitacaoRetirada" property="sgFilialSolicitacao" width="100" />
			<adsm:gridColumn title="" property="nrSolicitacao" dataType="integer" mask="00000000" width="80" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="situacao" property="tpStatusManifesto" isDomain="true" width="130" />
		
		<adsm:buttonBar />
	</adsm:grid>

</adsm:window>

<script type="text/javascript">
<!--
	document.getElementById("tpManifestoLookup").masterLink = "true";
	
	function onRowClickDef() {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab('cad', false );
	}
	
	function filialChange(elem) {
		setDisabled("manifestoEntrega.idManifestoEntrega",elem.value == "");
		return filial_sgFilialOnChangeHandler();
	}
	
	
	function initWindow(event) {
		if (event.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("cad", true);
		} else if (event.name == "cleanButton_click") {
			populateFilial();
		}
	}
	
	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		findFilialUsuarioLogado();
	}	
	
	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.emitirManifestoAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;
	
	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		
		populateFilial();
	}

	function populateFilial() {
		setElementValue("filial.idFilial",idFilial);
		setElementValue("filial.sgFilial",sgFilial);
		setElementValue("filial.pessoa.nmFantasia",nmFilial);
		
		setElementValue("manifestoEntrega.filial.sgFilial",sgFilial);
		setDisabled("manifestoEntrega.idManifestoEntrega",false);
		
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial",sgFilial);
	}
	
//-->
</script>