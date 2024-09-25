<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.vendas.consultarHistoricoAprovacaoAction"
	onPageLoadCallBack="myOnPageLoad">
	
	<adsm:section caption="consultarHistoricoAprovacaoTitulo" />
	
	<adsm:grid 
		idProperty="idAcao" 
		property="acoesList" 
		selectionMode="none" 
		gridHeight="200" 
		unique="true"
		onRowClick="acoesRowClick">
		
		<adsm:gridColumn 
			title="aprovador" 
			property="usuario.dsDescricao" 
			width="20%" />
			
		<adsm:gridColumn 
			title="dataAcao" 
			property="dhAcao" 
			width="10%" />
			
		<adsm:gridColumn 
			title="situacao" 
			property="tpSituacaoAcao" 
			isDomain="true" 
			width="15%" />
			
		<adsm:gridColumn 
			title="observacao" 
			property="obAcao" 
			width="55%" />
			
	</adsm:grid>
	
	<adsm:buttonBar freeLayout="false">
		<adsm:button 
			id="btnFechar"
			onclick="self.close();"
			caption="fechar"
			disabled="false" />
	</adsm:buttonBar>
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
function acoesRowClick(valor) {
	return false;
}

function populaGrid() {
	acoesListGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]), true);
}
-->
</script>