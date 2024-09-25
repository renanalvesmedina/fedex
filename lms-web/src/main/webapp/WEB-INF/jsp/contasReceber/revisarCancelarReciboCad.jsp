<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contasReceber/revisarCancelarRecibo">

		<adsm:textbox dataType="text" property="nomeRecibo" size="3" maxLength="3" label="recibo" labelWidth="20%" width="6%"/>
		<adsm:lookup action="/contasReceber/manterDocumento" size="20" maxLength="20" labelWidth="0%" dataType="text" service="" property="numDocumento" width="24%"/>

		<adsm:buttonBar>
			<adsm:button caption="cancelarRecibo"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>