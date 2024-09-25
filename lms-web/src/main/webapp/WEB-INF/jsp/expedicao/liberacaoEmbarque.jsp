<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myPageLoad"
	service="lms.expedicao.liberacaoEmbarqueAction">

	<adsm:form
		action="/expedicao/liberacaoEmbarque">

		<adsm:section
			caption="liberacaoEmbarque"
			width="80"/>

		<adsm:label
			key="branco" width="1%"/>

		<adsm:hidden
			property="tpBloqueioLiberacao"/>

		<adsm:combobox
			property="tipoLiberacaoEmbarque.idTipoLiberacaoEmbarque"
			optionLabelProperty="dsTipoLiberacaoEmbarque"
			optionProperty="idTipoLiberacaoEmbarque"
			service="lms.expedicao.liberacaoEmbarqueAction.findTipoLiberacao"
			label="tipoLiberacao"
			onlyActiveValues="true"
			required="true"
			boxWidth="200"
			width="84%"/>

		<adsm:label
			key="branco"
			width="1%"/>

		<adsm:textarea
			width="84%"
			columns="95"
			rows="3"
			maxLength="500"
			property="obLiberacao"
			label="observacao"/>

		<adsm:label
			key="branco"
			width="1%"/>

		<adsm:lookup
			property="funcionario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			serializable="false"
			service="lms.expedicao.liberacaoEmbarqueAction.findLookupUsuarioFuncionario"
			dataType="text"
			label="funcionario"
			size="15"
			maxLength="15"
			width="45%"
			action="/configuracoes/consultarFuncionariosView"
			exactMatch="true"
			required="true">

			<adsm:propertyMapping
				relatedProperty="funcionario.codPessoa.nome"
				modelProperty="nmUsuario"/>

			<adsm:propertyMapping
				relatedProperty="usuario.login"
				modelProperty="login"/>

			<adsm:propertyMapping
				relatedProperty="usuario.idUsuario"
				modelProperty="idUsuario"/>

			<adsm:textbox
				dataType="text"
				property="funcionario.codPessoa.nome"
				size="30"
				disabled="true"
				serializable="false"/>

		</adsm:lookup>

		<adsm:hidden
			property="usuario.idUsuario"/>

		<adsm:hidden
			property="usuario.login"/>

		<adsm:textbox
			dataType="password"
			property="password"
			size="10"
			label="senha"
			required="true"
			labelWidth="10%"
			width="15%"
			maxLength="40"/>

		<adsm:buttonBar>
			<adsm:storeButton
				caption="liberar"
				id="storeButton"
				callbackProperty="liberacao"
				service="lms.expedicao.liberacaoEmbarqueAction.storeInSession"/>

			<adsm:button
				caption="cancelar"
				id="cancelButton"
				buttonType="cancelarButton"
				disabled="false"
				onclick="return cancela();"
				boxWidth="63"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	function myPageLoad(){
		onPageLoad();

		var url = new URL(document.location.href);
		var tpBloqueioLiberacao = url.parameters['tpBloqueioLiberacao'];
		setElementValue('tpBloqueioLiberacao', tpBloqueioLiberacao);

		/** Se tipo for diferente de Cliente e Municupio **/
		if(tpBloqueioLiberacao != "CP" && tpBloqueioLiberacao != "MP") {
			//Oculta combo 
			setRowVisibility("tipoLiberacaoEmbarque.idTipoLiberacaoEmbarque", false);

			var tipoLiberacaoEmbarque = getElement("tipoLiberacaoEmbarque.idTipoLiberacaoEmbarque");
			tipoLiberacaoEmbarque.required = "false";

			//Torna Observação obrigatória
			var obLiberacao = getElement("obLiberacao");
			obLiberacao.required = "true";
			setFocus("obLiberacao");
		}
	}

	function cancela(){
		handleSave(false);
	}

	function handleSave(data) {// Handle click of Save button
		window.returnValue = data;
		window.close();
	}

	function liberacao_cb(data, errorMsg, errorKey) {
		if (errorMsg) {
			alert(errorMsg);
			return false;
		} 
		handleSave(true);
	}
</script>