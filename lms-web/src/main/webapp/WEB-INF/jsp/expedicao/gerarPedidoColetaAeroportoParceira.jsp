<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/expedicao/gerarPedidoColetaAeroportoParceira">
	
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		
		<adsm:hidden property="idFilialDestinoAwb"/>
		<adsm:hidden property="sgFilialDestinoAwb"/>
		<adsm:hidden property="nmFilialDestinoAwb"/>
		<adsm:hidden property="nrIdentificacaoPessoa"/>
		<adsm:hidden property="idPessoa"/>
		<adsm:hidden property="nmPessoa"/>
		
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 service="lms.expedicao.manterPrealertasAction.findLookupRotaColetaEntrega" 
					 action="/municipios/manterRotaColetaEntrega"
					 label="rotaColetaEntrega" labelWidth="18%" width="82%" size="5" maxLength="5" exactMatch="true" required="true">
			<adsm:propertyMapping criteriaProperty="idFilialDestinoAwb" modelProperty="filial.idFilial" disable="true"/> 
			<adsm:propertyMapping criteriaProperty="sgFilialDestinoAwb" modelProperty="filial.sgFilial" disable="true"/> 
			<adsm:propertyMapping criteriaProperty="nmFilialDestinoAwb" modelProperty="filial.pessoa.nmFantasia" disable="true"/> 
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
		
		<adsm:combobox property="meioTransporte" 
					   optionProperty="idMeioTransporte" 
					   optionLabelProperty="dsMeioTransporteFormatado"
					   labelWidth="18%" 
					   width="82%"
					   onlyActiveValues="true" 
					   label="meioTransporte" 
					   boxWidth="135"
					   style="vertical-align:top"
					   required="true"/>
		
		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 idProperty="idMotorista"
					 property="motorista" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.carregamento.consultarControleCargasAction.findLookupMotorista" 
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motorista.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motorista.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" disable="true" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button 
				id="btnSalvar"
				caption="salvar"
				onclick="salvar();"/>
						
			<adsm:button 
				id="btnFechar"
				onclick="self.close();"
				caption="fechar"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-04515"/>
		</adsm:i18nLabels>
		
	</adsm:form>
</adsm:window>
<script type="text/javascript">

function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnSalvar", false);
	setDisabled("btnFechar", false);
	
	var url = new URL(parent.location.href);
	 setElementValue("idFilialDestinoAwb", url.parameters["idFilialDestinoAwb"]);
	 setElementValue("sgFilialDestinoAwb", url.parameters["sgFilialDestinoAwb"]);
	 setElementValue("nmFilialDestinoAwb", url.parameters["nmFilialDestinoAwb"]);
	 setElementValue("nrIdentificacaoPessoa", url.parameters["nrIdentificacaoPessoa"]);
	 setElementValue("idPessoa", url.parameters["idPessoa"]);
	 setElementValue("nmPessoa", url.parameters["nmPessoa"]);
	 
	 findMeioTransporteByProprietarioParceira(getElementValue("idPessoa"), getElementValue("idFilialDestinoAwb"));
}

function findMeioTransporteByProprietarioParceira(idPessoa, idFilial){
	var idMeioTransporte
	var sdo = createServiceDataObject("lms.expedicao.manterPrealertasAction.findMeioTransporteByProprietarioAndFilial", "findMeioTransporteByProprietarioAndFilial", 
			{proprietario:{idProprietario:idPessoa, idFilial:idFilial}});
	xmit({serviceDataObjects:[sdo]});
}

function findMeioTransporteByProprietarioAndFilial_cb(data, error){
	if(error != undefined){
		alert(error);
		return;
	}
	meioTransporte_cb(data);
}

/***************************/
/* ONCLICK CANCELAR BUTTON */
/***************************/
function salvar() {
	var f = document.forms[0];
	if (!validateForm(f)) {
		return false;
	}
	
 	var meioTransporteByIdTransportado = dialogArguments.window.document.getElementById('meioTransporteByIdTransportado.idMeioTransporte');
 	var motorista = dialogArguments.window.document.getElementById('motorista.idMotorista');
 	var rota = dialogArguments.window.document.getElementById('rotaColetaEntrega.idRotaColetaEntrega');

 	meioTransporteByIdTransportado.value = getElementValue('meioTransporte');
 	motorista.value = getElementValue('motorista.idMotorista');
 	rota.value = getElementValue('rotaColetaEntrega.idRotaColetaEntrega');
	self.close();
	
}

function storeSession_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	self.close();	
}


</script>