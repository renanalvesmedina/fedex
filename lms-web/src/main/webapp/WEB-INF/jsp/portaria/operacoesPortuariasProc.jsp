<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="operacoesPortuarias" onPageLoadCallBack="saidaPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-05387"/>
	</adsm:i18nLabels>

	<adsm:form action="/portaria/operacoesPortuarias" idProperty="idMeioTransporte">

		
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="idMeioTransporte"/>
		
		<adsm:section caption="informarVeiculo" width="70" />
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.portaria.operacoesPortuariasAction.findLookupMeioTransporte" 
					 action="/portaria/operacoesPortuarias" 
					 picker="false" label="meioTransporte" size="6" maxLength="6" labelWidth="18%" width="7%" serializable="false" >
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdSemiRebocado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="idMeioTransporte" />			
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.portaria.operacoesPortuariasAction.findLookupMeioTransporte" 
					 action="/portaria/operacoesPortuarias" 
					 picker="false" size="24" maxLength="25" width="26%" required="true" >
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="idMeioTransporte" />	
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button caption="coletarGerarMdfe" id="salvar" onclick="coletarGerarMdfe()" disabled="false" />
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/mdfe.js"></script>

<script type="text/javascript">

var mdfe = inicializaMdfe();

function inicializaMdfe() {
	var mdfe = new Mdfe();
	
	var action = "lms.portaria.operacoesPortuariasAction";
	
	mdfe.urlGerarMdfe = action+ ".generateColeta";
	
	mdfe.gerarMdfeCallId = "coletarGerarMdfe";
	
	mdfe.urVerificaAutorizacaoMdfe = action+ ".verificaAutorizacaoMdfe";
	mdfe.verificaAutorizacaoMdfeCallId = "verificaAutorizacaoMdfe";
	
	mdfe.urlEncerrarMdfesAutorizados = action+ ".encerrarMdfesAutorizados";
	mdfe.encerrarMdfesAutorizadosCallId = "encerrarMdfesAutorizados";
	
	mdfe.urlVerificaEncerramentoMdfe = action+ ".verificaEncerramentoMdfe";
	mdfe.verificaEncerramentoMdfeCallId = "verificaEncerramentoMdfe";

	mdfe.urlImprimirMdfe = action+ ".imprimirMDFe";
	
	mdfe.chaveMensagemFinal = "LMS-05387";
	
	return mdfe;
	
}

function saidaPageLoad_cb(){
	onPageLoad_cb();

	if (dialogArguments) {
		setElementValue("idControleCarga", dialogArguments.document.getElementById("idControleCarga").value);
		setElementValue("idFilial", dialogArguments.document.getElementById("filial.idFilial").value);
	}		

}

function coletarGerarMdfe() {
	
	var data = buildFormBeanFromForm(document.forms[0]);
	
	mdfe.gerarMdfe(data, false);
	
}

function coletarGerarMdfe_cb(data, error) {
	if (error) {
		alert(error);
		setElementValue("meioTransporteByIdTransportado2.nrFrota");
		setElementValue("meioTransporteByIdSemiRebocado.nrIdentificador");
		setFocusOnFirstFocusableField();
		return false;
	}
	
	if (mdfe.gerarMdfeCallback(data))  {
		finalizaColetaAtualizaStatus();
	}

}

function verificaAutorizacaoMdfe_cb(data, error) {
	
	if (error) {
		alert(error);
		setFocusOnFirstFocusableField();
		mdfe.hideMessageMDFe();
		return false;
	}
	
	if (mdfe.verificaAutorizacaoMdfeCallback(data)) {
		finalizaColetaAtualizaStatus();
	}
	
}
		
function finalizaColetaAtualizaStatus(data) {
	var sdo = createServiceDataObject("lms.portaria.operacoesPortuariasAction.generateColetaAtualizaStatus", "generateColetaAtualizaStatus", buildFormBeanFromForm(document.forms[0]));
	xmit({serviceDataObjects:[sdo]});
}

function generateColetaAtualizaStatus_cb(data, error) {
	
	if (error) {
		alert(error);
		setFocusOnFirstFocusableField();
		mdfe.hideMessageMDFe();
		return false;
	}
	
	mdfe.imprimirMDFe();

	self.close();

}

function encerrarMdfesAutorizados_cb(data, error) {
	if (error) {
		alert(error);
		setFocusOnFirstFocusableField();
		return false;
	}
	
	mdfe.verificaEncerramentoMdfe(data);
	
}

function verificaEncerramentoMdfe_cb(data, error) {
	if (error) {
		mdfe.hideMessageMDFe();
		alert(error);
		setFocusOnFirstFocusableField();
		return false;
	}
	
	if (mdfe.verificaEncerramentoMdfeCallback(data)) {
		coletarGerarMdfe();
	}
	
}


</script>