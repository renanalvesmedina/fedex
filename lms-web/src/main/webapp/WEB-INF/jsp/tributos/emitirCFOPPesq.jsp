<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/tributos/emitirCFOP">
		<adsm:combobox property="status" label="situacao" domain="DM_STATUS" labelWidth="20%" width="80%"/>
		<adsm:combobox property="tpFormatoRelatorio" 
					   required="true"
					   label="formatoRelatorio" 
					   defaultValue="pdf"
			           domain="DM_FORMATO_RELATORIO"
					   labelWidth="20%" width="80%"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.tributos.emitirCFOPAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
}
</script>