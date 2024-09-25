<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGerarPreManifesto" type="main" onPageLoad="setAbas">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterGerarPreManifesto" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterGerarPreManifesto" cmd="cad"/>
		<adsm:tab title="documentos" id="documentos" src="/carregamento/manterGerarPreManifesto" cmd="documentos" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>

<script type="text/javascript">
	function setAbas(){		
		var url = new URL(document.location.href);		
		var origem = url.parameters.origem;
			
		if ( origem != undefined && origem != "" ) {
			tabGroup.selectTab("cad", null, true);
		} else {
			tabGroup.selectTab("pesq");		
		}
	}
</script>  