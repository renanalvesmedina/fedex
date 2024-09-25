<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="CarregamentoDescarga" service="lms.carregamento.consultarControleCargasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >
	
	<adsm:form action="/carregamento/consultarControleCargasVeiculos" idProperty="idPagtoProprietarioCc" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdPagtoProprietario" >

		<adsm:hidden property="idControleCarga" serializable="false" />
		<adsm:hidden property="idFilial" serializable="false" />
		<adsm:hidden property="tpControleCarga" serializable="false" />
	</adsm:form>


	<adsm:grid idProperty="idCarregamentoDescarga" property="carregamentoDescarga" 
			   selectionMode="none" gridHeight="220" rows="11" unique="true" 
			   autoSearch="false"
			   onRowClick="myOnRowClick"
			   onDataLoadCallBack="myOndataLoadCB"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedCarregamentoDescarga"
			   rowCountService="lms.carregamento.consultarControleCargasAction.getRowCountCarregamentoDescarga"
	>

    	<adsm:gridColumn property="sgFilial" title="filial" width="30"/>
		<adsm:gridColumn property="tpOperacao" isDomain="true" title="tipo" width="140"/>
		<adsm:gridColumn title="relatorio" property="relatorio" image="/images/popup.gif" openPopup="true" link="javascript:relatorioDiscrepancia" linkIdProperty="idCarregamentoDesc" align="center" width="85" />
		<adsm:gridColumn property="tpStatusOperacao" isDomain="true" title="tipo" width="140"/>
		<adsm:gridColumn property="dhInicioOperacao" dataType="JTDateTimeZone" title="dataInicio" width="140" align="center"/>
		<adsm:gridColumn property="dhFimOperacao" dataType="JTDateTimeZone" title="dataFim" width="140" align="center"/>

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

function myOndataLoadCB_cb(data,error){
	return false;
}

function myOnRowClick(){
	return false;	
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaDadosMaster();

		var data = {idControleCarga:getElementValue("idControleCarga")};
		carregamentoDescargaGridDef.executeSearch(data);
	}
}

function povoaDadosMaster() {
	var url = new URL(parent.location.href);	
	
	setElementValue("idControleCarga", url.parameters['idControleCarga']);
	setElementValue("idFilial", url.parameters['idFilial']);
	setElementValue("tpControleCarga", url.parameters['tpControleCarga']);
}

function relatorioDiscrepancia(idCarregamentoDescarga){
	var dadosUrl = '&idControleCarga='+getElementValue("idControleCarga");
	dadosUrl += '&idFilialControleCarga='+getElementValue("idFilial");
	dadosUrl += '&tpControleCarga='+getElementValue("tpControleCarga");
	dadosUrl += '&idCarregamentoDescarga='+idCarregamentoDescarga;
	dadosUrl += '&blContinuaDescarga=false';
	window.showModalDialog('carregamento/consultarControleCargas.do?cmd=relatorioDivergencias'+dadosUrl,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:375px;dialogHeight:200px;');
}

</script>