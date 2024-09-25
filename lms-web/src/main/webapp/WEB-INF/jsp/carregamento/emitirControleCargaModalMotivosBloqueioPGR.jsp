<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.emitirControleCargasAction">
	<adsm:form action="/carregamento/emitirControleCargas">
		<adsm:textarea 
			property="dsMotivos"
			maxLength="1000"
			required="false"
			columns="90"
			rows="16"
			label="motivos" 
			labelStyle="padding-left: 10px;vertical-align:top"
			cellStyle="padding-left: 10px;vertical-align:top"
			labelWidth="10%" 
			width="82%"
			disabled="true"
			style="margin-left:10px"/>
			
		<adsm:buttonBar>
			<adsm:button 
				caption="fechar" 
				onclick="self.close()"
				disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	var u = new URL(parent.location.href);
	var msgBloqueio = u.parameters["msgBloqueio"];
	setElementValue("dsMotivos", msgBloqueio);
</script>