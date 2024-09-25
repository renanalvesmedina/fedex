<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="" service="" onPageLoadCallBack="carregaPagina" >

	<adsm:form action="/coleta/transmitirColetasLiberacaoGerRisco"  >
	
		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />
	
		<adsm:section caption="LMS-05148" width="90%" style="align:center" />

		<adsm:lookup property="usuario" label="funcionario" 
					 idProperty="idUsuario" criteriaProperty="nrMatricula" 
                     service="lms.coleta.programacaoColetasVeiculosRetornoAction.findLookupUsuarioFuncionario" 
            		 action="/configuracoes/consultarFuncionariosView"
            		 dataType="text" size="16" maxLength="16" width="85%" required="true" >
        	<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
        	<adsm:propertyMapping modelProperty="nmUsuario" criteriaProperty="usuario.nmUsuario" disable="false" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" disable="true" />
        	<adsm:textbox dataType="text" property="usuario.nmUsuario" size="40" disabled="true" />
		</adsm:lookup>

		<adsm:textarea label="motivo" property="dsMotivo" maxLength="150" rows="4" columns="100" 
					   width="85%" required="true" />
		   
		<adsm:buttonBar freeLayout="false" >
			<adsm:button caption="confirmar" id="botaoConfirmar" onclick="botaoConfirmar_OnClick(this.form);" disabled="false" />
			<adsm:button caption="cancelar" id="botaoCancelar" onclick="botaoCancelar_OnClick();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script>
function carregaPagina_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	carregaDadosUsuario();
}

function carregaDadosUsuario() {
	var sdo = createServiceDataObject("lms.coleta.programacaoColetasVeiculosRetornoAction.getDataUsuario", "retornoCarregaDadosUsuario"); 
	xmit({serviceDataObjects:[sdo]});
}


function retornoCarregaDadosUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data,"filialUsuario.idFilial"));
	setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data,"filialUsuario.sgFilial"));
	setElementValue("filialUsuario.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filialUsuario.pessoa.nmFantasia"));
}


function botaoConfirmar_OnClick(form) {
	if (!validateForm(form)) {
		return false;
	}
	dialogArguments.window.document.getElementById('nmUsuarioLiberacao').value = getElementValue("usuario.nmUsuario");
	dialogArguments.window.document.getElementById('dsMotivoLiberacao').value = getElementValue("dsMotivo");
	window.close();
}

function botaoCancelar_OnClick() {
	window.close();
}
</script>