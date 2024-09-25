<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterParametrosClienteTaxasAction">
	<adsm:grid autoSearch="false" detailFrameName="tax" property="taxaCliente"	
			gridHeight="170" unique="true" idProperty="idTaxaCliente" 
			rows="8" onRowClick="rowClick" selectionMode="none">

		<adsm:gridColumn title="taxa" property="parcelaPreco.nmParcelaPreco"width="27%" />
		<adsm:gridColumn title="indicador" property="tpTaxaIndicador" width="20%" isDomain="true" />
		<adsm:gridColumn title="valorIndicador" property="valor" align="right" width="18%" />
		<adsm:gridColumn title="pesoMinimoKg" dataType="decimal" property="psMinimo" align="right" width="15%" 
			mask="#,###,###,###,##0.000" />
		<adsm:gridColumn title="valorExcedenteReal" property="vlExcedente" mask="##,###,###,###,##0.00"
			dataType="decimal" align="right" width="20%"/>
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirTaxa"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

function initWindow(eventObj){
	var doc = getTabGroup(document).getTab("list").tabOwnerFrame.document;
	var id = doc.getElementById("idParametroClienteRef").value;
	carregaGrid(id);	
}

function carregaGrid(id){
	if (id!=undefined){
		var filter = new Object();
		setNestedBeanPropertyValue(filter,"parametroCliente.idParametroCliente",id);
		taxaClienteGridDef.executeSearch(filter); 		
	}
}

function rowClick(id){
	if (id!=undefined && id!=""){
		var popupURL ="vendas/consultarParametroClienteLog.do?cmd=taxaslog&idTaxaCliente="+id;
		showModalDialog(popupURL,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
	}
	return false;
}
</script>