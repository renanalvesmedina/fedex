<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="motivoAbertura">
	<adsm:form action="/rnc/abrirRNC" >
		
		<adsm:textarea 
			property="dsMotivoAbertura" 
			label="motivoAbertura" 
			width="50%" 
			labelWidth="23%" 
			maxLength="400" 
			columns="50"
			rows="6"
			required="true" 
		/>
	
		<adsm:buttonBar>
			<adsm:button caption="confirmar" disabled="false" onclick="confirmar();"/>
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function confirmar(){
		if (validateForm(document.forms[0])){
		window.returnValue = getElementValue("dsMotivoAbertura");
		window.close();
	}
	}
</script>
	