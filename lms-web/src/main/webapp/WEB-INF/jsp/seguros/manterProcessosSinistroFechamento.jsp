<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.seguros.manterProcessosSinistroAction" onPageLoad="pageLoad">

	<adsm:form action="/seguros/manterProcessosSinistro" idProperty="idProcessoSinistro">
	
		<adsm:textbox property="nmUsuario" dataType="text" label="usuario" labelWidth="18%" width="82%" disabled="true"/>		
		<adsm:textbox property="dhFechamento" dataType="JTDateTimeZone" label="dataHoraFechamento" labelWidth="18%" width="82%" disabled="true"/>
	
		<adsm:textarea maxLength="200" property="dsJustificativaEncerramento" required="true" label="justificativa" labelWidth="18%" width="82%" rows="6" columns="50"/>
		<adsm:buttonBar freeLayout="false" >
			<adsm:storeButton id="storeButton" caption="efetuarFechamento" service="lms.seguros.manterProcessosSinistroAction.executeEfetuarFechamento" callbackProperty="storeCallback"/>
			<adsm:button      id="closeButton" caption="fechar" onclick="self.close();" buttonType="storeButton" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script>
	function storeCallback_cb(data, error) {
		store_cb(data, error);		
		if (error==undefined) {
			dialogArguments.window.findAtualizaValoresDetalhamento();
			setDisabled('dsJustificativaEncerramento', true);
			setDisabled('storeButton', true);
			// window.close();
		} 
	}

	function pageLoad() {
		setElementValue('idProcessoSinistro', dialogArguments.document.forms[0].elements["idProcessoSinistro"].value);
		onPageLoad();		
		findDadosFechamento();
	}
	
	function findDadosFechamento() {
		var data = new Array();
		data.idProcessoSinistro = getElementValue('idProcessoSinistro');
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findDadosFechamento", "findDadosFechamento", data);
    	xmit({serviceDataObjects:[sdo]});			
	}
	
	function findDadosFechamento_cb(data, error) {
		if (error==undefined) {
			setElementValue('nmUsuario', data.nmUsuario);
			
			if (data.dhFechamento!=undefined) {
				setElementValue('dhFechamento', setFormat(document.getElementById('dhFechamento'), data.dhFechamento));
				setElementValue('dsJustificativaEncerramento', data.dsJustificativaEncerramento);
				setDisabled('dsJustificativaEncerramento', true);
				setDisabled('storeButton', true);
			
			} else {
				setDisabled('dsJustificativaEncerramento', false);
				setDisabled('storeButton', false);
			}
		}
	}	
	
</script>  