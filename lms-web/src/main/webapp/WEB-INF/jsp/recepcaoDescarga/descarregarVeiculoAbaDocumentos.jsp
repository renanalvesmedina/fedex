<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.recepcaodescarga.descarregarVeiculoAction">
	<adsm:form action="/recepcaoDescarga/descarregarVeiculo">
		
		<adsm:hidden property="origem" value="descarregarVeiculo" serializable="false"/>
		<adsm:hidden property="idDoctoServico" serializable="false"/>
		<adsm:hidden property="tpDocumentoServico" serializable="false"/>
		<adsm:hidden property="sgFilialOrigem" serializable="false"/>
		<adsm:hidden property="nrDoctoServico" serializable="false"/>
		
		<adsm:hidden property="idControleCarga" />
		<adsm:textbox label="controleCarga" property="sgControleCarga" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="18%" width="82%">
			<adsm:textbox property="nrControleCarga" dataType="text" disabled="true" 
						  size="8" maxLength="8" />
		</adsm:textbox>
						
		<adsm:combobox label="servico" property="servico.idServico"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.recepcaodescarga.descarregarVeiculoAction.findServico" 
					   labelWidth="18%" width="82%" boxWidth="230" onlyActiveValues="true"/>
		
		<adsm:checkbox property="emAtraso" label="emAtraso" labelWidth="18%" width="82%" />
		<adsm:checkbox property="naData" label="naData" labelWidth="18%" width="82%" />
		<adsm:checkbox property="emDia" label="emDia" labelWidth="18%" width="82%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="doctoServico"/>
			<adsm:resetButton caption="limpar"/>	
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idDoctoServico" property="doctoServico" selectionMode="none" onRowClick="onRowClick"	   
			   gridHeight="175" unique="true" autoSearch="false" scrollBars="both" 		   
   			   service="lms.recepcaodescarga.descarregarVeiculoAction.findPaginatedDoctoServico" 
			   rowCountService="lms.recepcaodescarga.descarregarVeiculoAction.getRowCountDoctoServico">	
		<adsm:gridColumn title="documentoServico" property="tpDocumento" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="sgFilialOrigem" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="60" />
		<adsm:gridColumn title="dpe" property="dtPrevEntrega" dataType="JTDate" width="100" align="center" />
		<adsm:gridColumn title="servico" property="sgServico" width="90" />
		<adsm:gridColumn title="remetente" property="clienteRemetente" width="215" />
		<adsm:gridColumn title="destinatario" property="clienteDestinatario" width="215" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="psReal" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorMercadoria" property="sgMoeda" dataType="text" width="30"/>
            <adsm:gridColumn title="" property="dsSimboloMoeda" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlMercadoria" dataType="currency" width="70"/>		
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorFrete" property="sgMoeda2" dataType="text" width="30"/>
         	<adsm:gridColumn title="" property="dsSimboloMoeda2" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlTotalDocServico" dataType="currency" width="70"/>
		<adsm:gridColumn title="localizacao" property="tpStatusManifesto" isDomain="true" width="120" />		
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn title="manifesto" property="sgFilialManifesto " dataType="text" width="30"/>
			<adsm:gridColumn title="" property="nrPreManifesto" dataType="integer" width="70" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:buttonBar>
			<adsm:button id="abrirRNC" caption="abrirRNC" action="/rnc/abrirRNC" cmd="main"/>
<% /*
			<adsm:button id="abrirRNC" caption="abrirRNC" action="/rnc/abrirRNC" cmd="main" disabled="true">
				<adsm:linkProperty src="origem" target="origem"/>
				<adsm:linkProperty src="idDoctoServico" 
								   target="ocorrenciaDoctoServico.doctoServico.idDoctoServico"/>
				<adsm:linkProperty src="tpDocumentoServico" 
								   target="ocorrenciaDoctoServico.doctoServico.tpDocumentoServico"/>
				<adsm:linkProperty src="sgFilialOrigem" 
								   target="ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="nrDoctoServico" 
								   target="ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial"/>
			</adsm:button>
*/ %>			
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script type="text/javascript">

	function initWindow(eventObj) {		
		if(eventObj.name == "tab_click") {
			resetValue(this.document);
			doctoServicoGridDef.resetGrid();
		}
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("recepcaoDescarga");
		var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");
		var sgControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
		var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");
		
		setElementValue("idControleCarga" , idControleCarga);
		setElementValue("sgControleCarga" , sgControleCarga);
		setElementValue("nrControleCarga" , nrControleCarga);
	}
	
	function onRowClick() {
		return false;
	}
	
	function onSelectRow(rowRef) {
		setDisabled("abrirRNC", false);
		var gridData = doctoServicoGridDef.gridState.data[rowRef.rowIndex];		
		setElementValue("idDoctoServico", gridData.idDoctoServico);
		setElementValue("tpDocumentoServico", gridData.tpDocumento.description);
		setElementValue("sgFilialOrigem", gridData.sgFilialOrigem);
		setElementValue("nrDoctoServico", gridData.nrDoctoServico);
	}
	
</script>