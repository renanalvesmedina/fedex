<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterParametrosClienteGeneralidadesAction">
	<adsm:grid 
		autoSearch="false"
		selectionMode="none"
		detailFrameName="gen"
		idProperty="idGeneralidadeCliente" 
		gridHeight="400" 
		property="generalidadeCliente" 
		unique="true" 
		rows="9"
		onRowClick="rowClick"
	>
		<adsm:gridColumn title="generalidade" property="parcelaPreco.nmParcelaPreco" width="40%"/>
		<adsm:gridColumn title="indicador" property="tpIndicador" width="30%" isDomain="true"/>
		<adsm:gridColumn title="valorIndicador" property="valorIndicador" width="30%" align="right" />
		<adsm:buttonBar/>
	</adsm:grid>
	
</adsm:window>
<script>
	function initWindow(eventObj){
		var doc = getTabGroup(document).getTab("list").tabOwnerFrame.document;
		var id = doc.getElementById("idParametroClienteRef").value;
		carregaGrid(id);
	}

	function carregaGrid(id){
		if (id!=undefined){
			var filter = new Object();
			setNestedBeanPropertyValue(filter,"parametroCliente.idParametroCliente",id);
			generalidadeClienteGridDef.executeSearch(filter);	
		}
	}

	function rowClick(id){
		if (id!=undefined && id!=""){
			var popupURL ="vendas/consultarParametroClienteLog.do?cmd=genlog&idGeneralidadeCliente="+id;
			showModalDialog(popupURL,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
		}
		return false;
	}		
</script>