<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.consultarAWBsCTRCsAction"
	onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/expedicao/consultarAWBsCTRCs">
		<adsm:section caption="ctrcs" width="99%" />
 	</adsm:form>
 	
 	<adsm:grid 
 		property="ctoAwb"
 		idProperty="idCtoAwb"
 		selectionMode="none" 
 		gridHeight="167" 
 		unique="true"
 		onRowClick="ctoAwbRowClick">
 		
		<adsm:gridColumn 
			property="conhecimento.nrConhecimento" 
			title="numCTRC" 
			width="95" />
			
		<adsm:gridColumn 
			property="aeroportoByIdAeroportoOrigem.sgAeroporto" 
			title="aeroportoOrigem" 
			width="118" />
		
		<adsm:gridColumn 
			property="conhecimento.psReal" 
			title="peso" 
			width="69" 
			dataType="decimal"
			mask="#,###,###,###,##0.000"
			unit="kg" 
			align="right"/>
			
		<adsm:gridColumn 
			property="conhecimento.psAforado" 
			title="pesoCubado" 
			width="118"
			dataType="decimal"
			mask="#,###,###,###,##0.000" 
			unit="kg" 
			align="right"/>
			
		<adsm:gridColumn 
			dataType="integer"
			property="conhecimento.qtVolumes" 
			title="qtdeVolumes" 
			width="110" />
			
		<adsm:gridColumn 
			dataType="currency"
			property="conhecimento.vlMercadoria" 
			title="valorMerc"  
			width="100" 
			unit="reais"/>
			
		<adsm:gridColumn 
			dataType="currency"
			property="conhecimento.vlTotalDocServico" 
			title="valorDocumento"  
			width="141" 
			unit="reais"/>
		
		<adsm:buttonBar>
		
			<adsm:button 
				id="btnFechar"
				onclick="self.close();"
				caption="fechar" />
				
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnFechar", false);
	populaGrid();
	setFocus("btnFechar", false);
}

/***************************/
/* ONROWCLICK CTO AWB GRID */
/***************************/
function ctoAwbRowClick(valor) {
	return false;
}

function populaGrid() {
	var vIdAwb = dialogArguments.getElementValue("awb.idAwb");
	ctoAwbGridDef.executeSearch({idAwb:vIdAwb}, true);
}
-->
</script>
