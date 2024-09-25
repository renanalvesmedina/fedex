<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.coleta.enviarEmailAgendamentoAction" >
	<adsm:form action="/coleta/enviarEmailAgendamento" >
	<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="mensagem" id="mensagem" src="/coleta/enviarEmailAgendamento.do" cmd="mensagem"/>
			<adsm:tab title="anexo" id="anexo" src="/coleta/enviarEmailAgendamento.do" cmd="anexo"/>
		</adsm:tabGroup>
	</adsm:form>
</adsm:window>
