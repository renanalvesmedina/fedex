<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/digitarDadosNotaNormalObservacoes">

		<adsm:section caption="observacoes" width="70"/>
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		
		<adsm:textarea 
			width="99%"
			maxLength="500" 
			property="obAwb"
			columns="98"
			rows="6"
			disabled="true"/>

		<adsm:buttonBar>
			<adsm:button 
				id="btnFechar"
				onclick="self.close();"
				caption="fechar"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnFechar", false);
	setElementValue("obAwb", dialogArguments.getElementValue("obAwb"));
	setFocus("btnFechar", false);
}
-->
</script>