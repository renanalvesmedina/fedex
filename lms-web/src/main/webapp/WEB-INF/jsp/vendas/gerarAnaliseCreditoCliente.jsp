<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterAnaliseCreditoClienteAction" onPageLoad="myPageLoad">
	<%
	String labelTitle = request.getParameter("labelTitle") == null ? "observacaoAnaliseCredito" : request.getParameter("labelTitle");
	String idCliente = request.getParameter("idCliente");
	Boolean isRequired = request.getParameter("isRequired") == null ? true : Boolean.parseBoolean(request.getParameter("isRequired"));
	%>

	<adsm:form action="/vendas/gerarAnaliseCreditoCliente">

		<adsm:section caption="<%=labelTitle%>" width="62"/>
		<adsm:label key="branco" width="1%"/>
		
		<adsm:hidden property="idCliente" value="<%=idCliente%>"/>

		<adsm:textarea
			width="84%"
			columns="74"
			rows="6"
			maxLength="500"
			property="obEvento"
			label="observacao"
			labelWidth="10%"
			required="<%=isRequired%>"/>
		
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="serasaPdf" 
					dataType="file"
					label="arquivo"
					labelWidth="11%"
					width="55%"
					size="52"
					serializable="true" />
					
		<adsm:buttonBar>
			<adsm:storeButton 
				caption="ok" 
				id="btnOk" 
				callbackProperty="storeItem" 
				service="lms.vendas.manterAnaliseCreditoClienteAction.gerarAnaliseCreditoClienteStore"/>

			<adsm:button
				caption="cancelar"
				id="cancelButton"
				buttonType="cancelarButton"
				onclick="return cancela();"
				boxWidth="63"
				disabled="false"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function myPageLoad(){
		onPageLoad();
		setFocus("obEvento");
		setDisplay("serasaPdf_browse",false);
	}
	
	function handleSave(data) {// Handle click of Save button
		window.returnValue = data;
		window.close();
	}

	function cancela(){
		handleSave();
	}

	function storeItem_cb(data, error, eventObj){
		handleSave(data);
	}
</script>