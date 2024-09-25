<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.seguros.manterProcessosSinistroAction">
	<adsm:form action="/seguros/manterProcessosSinistro" idProperty="idProcessoSinistroManifesto">
	
		<adsm:masterLink idProperty="idProcessoSinistro" showSaveAll="false" >
			<adsm:masterLinkItem property="nrProcesso" label="numeroProcesso" />
			<adsm:masterLinkItem property="nrControleCarga" label="controleCarga" />
			<adsm:hidden property="idControleCarga" serializable="true"/>
			<adsm:hidden property="dhSinistro"      serializable="true"/>
		</adsm:masterLink>
		
		<adsm:hidden property="nrProcessoSinistro"  serializable="false"/> 
		
		<script>
		var LMS_22005 = '<adsm:label key="LMS-22005"/>';
		</script>		
		
	</adsm:form>
	<adsm:grid property="processoSinistroManifesto" 
				idProperty="idProcessoSinistroManifesto" 
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedProcessoSinistroManifesto" 
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountProcessoSinistroManifesto" 
				selectionMode="none" 
				onRowClick="onRowClick"
				scrollBars="vertical"
				autoSearch="false"
				gridHeight="290"
				
				>

		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="sgFilialManifesto" dataType="text" title="manifesto" width="30"/>
			<adsm:gridColumn property="nrManifesto" dataType="integer" title="" width="70" mask="00000000"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="destino" property="sgFilialDestino" width="120" />
		<adsm:gridColumn title="tipoManifesto" property="tpManifesto" width="120" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="abrangencia" width="120" isDomain="true"/>
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valor" width="30"/>
			<adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="30"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="vlManifesto" dataType="currency" title="" width="100"/>
		<adsm:gridColumn title="documentos"  property="documentos" image="/images/popup.gif" openPopup="true" link="/seguros/manterProcessosSinistroManifestosDocumentos.do?cmd=list" popupDimension="790,520" align="center" linkIdProperty="idProcessoSinistroManifesto" />
		<adsm:buttonBar >
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");


	// desabilita o rowClick
	function onRowClick() {
		return false;
	}

	// seta os valores da masterLink (na mão:B)
	function setMasterLinkProperties() {
		var sgFilialControleCarga = abaDetalhamento.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
		var nrControleCarga       = abaDetalhamento.getFormProperty("controleCarga.nrControleCarga");
		var nrProcessoSinistro  = abaDetalhamento.getFormProperty("nrProcessoSinistro");
		
		setElementValue("idControleCarga", abaDetalhamento.getFormProperty("controleCarga.idControleCarga"));
		setElementValue("dhSinistro", getElementValue(abaDetalhamento.tabOwnerFrame.document.getElementById('dhSinistro'), true));
		
		// estes elementos são utilizados pela popup de documentos
		setElementValue('nrProcessoSinistro', nrProcessoSinistro);
		
		if ((sgFilialControleCarga != "") && (nrControleCarga != ""))
			setElementValue("_nrControleCarga", sgFilialControleCarga + " " + nrControleCarga); 
		if (nrProcessoSinistro != "")
			setElementValue("_nrProcesso", nrProcessoSinistro);		

	}
    
    function validateDhSinistro() {
    	if (abaDetalhamento.getFormProperty("dhSinistro")!="")
    		return true;
    	return false;
    }
    
    // onShow
	function onManifestosShow(fromTab) {
		setMasterLinkProperties();
		if (validateDhSinistro() == true) {

			//FIXME: nao é possível nao consultar ao mudar de aba, numa tela masterLink
			//var data = buildFormBeanFromForm(document.forms[0]);
			//processoSinistroManifestoGridDef.executeSearch(data);
		} else {
			alert(LMS_22005);
			tabGroup.selectTab(fromTab.properties.id, "tab_click");
		}
	}
	
</script>