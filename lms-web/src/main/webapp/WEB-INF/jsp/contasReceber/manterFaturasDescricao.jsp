<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas">

		<adsm:textarea label="descricao" property="descricao" width="92%" labelWidth="8%" maxLength="500" 
			columns="79" rows="5"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="confirmar" onclick="setValue();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		document.getElementById("descricao").value = dialogArguments.window.document.getElementById("obFatura").value;
	}

	function setValue(){
		dialogArguments.window.document.getElementById("obFatura").value = document.getElementById("descricao").value;
		window.close();
	}
</script>