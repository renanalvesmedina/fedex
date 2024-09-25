<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.configuracoes.manterDadosInternacionalizadosAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form
		action="/configuracoes/manterPesquisaTabelasInternacionalizaveis">
		<adsm:textbox dataType="text" minValue="0" property="nmSistema"
			labelWidth="18%" width="82%" label="sistema" maxLength="35" size="35"
			required="false" />
		<adsm:hidden property="idSistema" />
		<adsm:textbox dataType="text" property="nmTabela" labelWidth="18%"
			width="82%" label="tabela" maxLength="35" size="35" required="false" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tabelas" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idTabela" property="tabelas" rows="13"
		width="100%"
		service="lms.configuracoes.manterDadosInternacionalizadosAction.findTabelasInternacionalizadasLookup"
		rowCountService="lms.configuracoes.manterDadosInternacionalizadosAction.getRowCountTabelasInternacionalizadas"
		gridHeight="150" selectionMode="none">
		<adsm:gridColumn width="100%" title="nomeDaTabela" property="nmTabela" dataType="text" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>			
	</adsm:grid>
</adsm:window>
<script>
function myOnPageLoad_cb(data,error){	
	onPageLoad_cb(data,error);
	document.getElementById("nmTabela").focus();
}
</script>
