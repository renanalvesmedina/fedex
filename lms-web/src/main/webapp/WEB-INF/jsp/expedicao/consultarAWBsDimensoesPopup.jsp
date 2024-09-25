<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.consultarAWBsDimensoesAction"
	onPageLoadCallBack="myOnPageLoad">
	
	<adsm:form action="/expedicao/consultarAWBsDimensoes">
		<adsm:section 
			caption="dimensoes"  
			width="65%"/>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idDimensao"
		property="dimensoes"
		selectionMode="none" 
		gridHeight="115" 
		unique="true" 
		showPagging="false" 
		scrollBars="vertical" 
		onRowClick="dimensoesRowClick">
		
		<adsm:gridColumn 
			dataType="integer"
			title="altura" 
			unit="cm"
			property="nrAltura" 
			align="right"/>
			
		<adsm:gridColumn 
			dataType="integer"
			title="largura" 
			unit="cm" 
			property="nrLargura" 
			align="right"/>
			
		<adsm:gridColumn
			dataType="integer" 
			title="comprimento" 
			unit="cm" 
			property="nrComprimento" 
			align="right"/>
			
		<adsm:gridColumn 
			dataType="integer"
			title="qtdeVolumes" 
			property="nrQuantidade" 
			align="right"/>
		
		<adsm:buttonBar>
			<adsm:button 
				id="btnFechar"
				caption="fechar"
				onclick="self.close();" />	
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

/*****************************/
/* ONROWCLICK DIMENSOES GRID */
/*****************************/
function dimensoesRowClick(valor) {
	return false;
}

function populaGrid() {
	var vIdAwb = dialogArguments.getElementValue("awb.idAwb");
	dimensoesGridDef.executeSearch({idAwb:vIdAwb}, true);
}
-->
</script>