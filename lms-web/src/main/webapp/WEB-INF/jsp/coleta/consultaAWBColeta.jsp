<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.consultaAWBColetaAction" onPageLoad="loadWindowData">
	<adsm:form action="/coleta/consultaAWBColeta" >
		
		<adsm:grid property="awbs" idProperty="idPedidoColeta" rows="10" gridHeight="235" width="230"
			   service="lms.coleta.consultaAWBColetaAction.findPaginatedAwbs" selectionMode="none" onRowClick="click"
			   rowCountService="lms.coleta.consultaAWBColetaAction.getRowCountAwbs" >
			
			<adsm:gridColumn title="preAwbAwb" property="nrAwb" width="230" align="right"/>
			<adsm:buttonBar>
				<adsm:button caption="fechar" id="btnFechar" onclick="javascript:window.close();" disabled="false" />			
			</adsm:buttonBar>
		</adsm:grid>
		
	</adsm:form>
</adsm:window>
<script>

function click(){
	return false;
}

function loadWindowData() {	
	var query = getQueryParams(document.location.search);
	var idPedidoColetaVal = query.idPedidoColeta;			
	awbsGridDef.executeSearch({idPedidoColeta:idPedidoColetaVal}, true);
}

function getQueryParams(qs) {
    qs = qs.split("+").join(" ");

    var params = {}, tokens,
        re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
        params[decodeURIComponent(tokens[1])]
            = decodeURIComponent(tokens[2]);
    }

    return params;
}

</script>