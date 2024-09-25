<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirLoteSalvados">
	<adsm:form action="/pendencia/emitirLoteSalvados">

		<adsm:lookup label="lote" property="lote" action="/pendencia/manterLotesSalvados" dataType="text" service="" size="12" maxLength="12" width="85%"/>

		<adsm:combobox label="disposicao" property="disposicao" optionLabelProperty="" optionProperty="" service="" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar" disabled="true" width="85%"/>

		<adsm:textbox label="dataGeracao" property="dataGeracao" dataType="JTDateTimeZone" disabled="true"/>

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="gerarDocumento" onclick="showModalDialog('pendencia/emitirLoteSalvados.do?cmd=dest',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:455px;');"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>