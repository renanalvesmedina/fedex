<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.consultarAWBsNFsAction"
	onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/expedicao/consultarAWBsNFs">
		<adsm:section caption="notasFiscais" width="60"/>
 	</adsm:form>

 	<adsm:grid 
 		idProperty="idNotaFiscalConhecimento"
 		property="nfs"
 		selectionMode="none" 
 		gridHeight="140" 
 		unique="true" 
 		showPagging="false" 
 		scrollBars="vertical"
 		defaultOrder="nrNotaFiscal"
 		onRowClick="nfsRowClick">

		<adsm:gridColumn 
			dataType="text"
			property="nrNotaFiscal" 
			title="numeroNF" 
			width="80" />

		<adsm:gridColumn 
			dataType="decimal"
			mask="#,###,###,###,##0.000"
			property="psMercadoria" 
			title="peso" 
			width="80" 
			unit="kg" 
			align="right"/>

		<adsm:gridColumn 
			dataType="integer"
			property="qtVolumes" 
			title="qtdeVolumes" 
			width="110" />

		<adsm:gridColumn 
			dataType="currency"
			property="vlTotal" 
			title="valorMerc"  
			width="110" 
			unit="reais"/>

		<adsm:buttonBar>
			<adsm:button 
				id="btnFechar"
				caption="fechar"
				onclick="self.close();" />

		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		setDisabled("btnFechar", false);
		populaGrid();
		setFocus("btnFechar", false);
	}

	function populaGrid() {
		var vIdAwb = dialogArguments.getElementValue("awb.idAwb");
		nfsGridDef.executeSearch({idAwb:vIdAwb}, true);
	}

	/**************/
	/* ONROWCLICK */
	/**************/
	function nfsRowClick() {
		return false;
	}
</script>