<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
		<adsm:buttonBar>
			<adsm:button caption="municipioBloqueado" onClick="if(confirm('Município de destino com embarque bloqueado. Deseja liberar o embarque?')){showModalDialog('expedicao/liberacaoEmbarque.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:615px;dialogHeight:230px;');}" />
			<adsm:button caption="clienteBloqueado" onClick="if(confirm('Cliente com embarque proibido. Deseja liberar o embarque?')){showModalDialog('expedicao/liberacaoEmbarque.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:615px;dialogHeight:230px;');}" />
		</adsm:buttonBar>
</adsm:window>
