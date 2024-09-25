<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/contasReceber/manterNotasDebitoInternacionais">

		<adsm:textarea label="descricao" property="descricao" width="85%" maxLength="80" 
			columns="70" rows="5"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onClick="javascript:window.close();"/>
			<adsm:button caption="cancelar" onClick="javascript:window.close();"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>