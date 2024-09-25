<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	
	function loadData(){
		onPageLoad();
		loadBasicData();
		loadObjects(false);
	}
	
	/**
	 * Faz a chamada para a funcao de captura de dados basicos da tela. 
	 */
	function loadBasicData(){
		var url = new URL(parent.location.href);
		var idDoctoServicoValue = url.parameters["idDoctoServico"];

		setElementValue("doctoServico.idDoctoServico", idDoctoServicoValue);
	
		var data = new Object();
		data.idDoctoServico = idDoctoServicoValue;
		var sdo = createServiceDataObject("lms.pendencia.emitirCartaMercadoriasDisposicaoAction.findBasicData", "loadBasicData", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da funcao loadBasicData.
	 * Carrega os dodos basico da tela a partir do objeto de retorno.
	 *
	 * @param data 
	 * @param error
	 */
	function loadBasicData_cb(data, error) {
		//caso não haja email cadastrado exibe uma mensagem não bloqueante.
		if (data.errorKey != undefined){
			alert(data.errorKey);	
		}
		setElementValue("clienteRemetente.idCliente", data.clienteRemetente.idCliente);
		setElementValue("clienteRemetente.pessoa.nmPessoa", data.clienteRemetente.pessoa.nmPessoa);
		setElementValue("clienteRemetente.pessoa.nrIdentificacao", data.clienteRemetente.pessoa.nrIdentificacao);
		setElementValue("clienteRemetente.pessoa.dsMail", data.clienteRemetente.pessoa.dsMail);
		setElementValue("contatoCliente", data.contatoCliente);
		
		setElementValue("filial.idFilial", data.filial.idFilial);
		setElementValue("filial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
		setElementValue("filial.sgFilial", data.filial.sgFilial);
		
		setElementValue("usuario.nmUsuario", data.usuario.nmUsuario);
		setElementValue("usuario.nrMatricula", data.usuario.nrMatricula);
		setElementValue("usuario.dsEmail", data.usuario.dsEmail);
		setElementValue("telefone.nrDdd", data.nrDdd);
		setElementValue("telefone.nrTelefone", data.nrTelefone);
	}
	
	/**
	 * Habilita os botao da tela.
	 *
	 * @param disable
	 */
	function loadObjects(disable) {
		setDisabled("fechar", disable);
		setDisabled("email", disable);
		setDisabled("enviarCarta", disable);
	}
	
</script>
<adsm:include key="LMS-17056"></adsm:include>

<adsm:window title="dadosCarta" service="lms.pendencia.emitirCartaMercadoriasDisposicaoAction" onPageLoad="loadData">
	<adsm:form action="/pendencia/emitirCartaMercadoriasDisposicao">

		<adsm:section caption="dadosCarta" width="90%"/>
		
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		
		<adsm:hidden property="clienteRemetente.idCliente"/>
		<adsm:textbox dataType="integer" property="clienteRemetente.pessoa.nrIdentificacao" 
			  label="responsavel" 
			  size="18" maxLength="18" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa" 
						  size="57" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox label="email" property="clienteRemetente.pessoa.dsMail" maxLength="255" dataType="text" width="85%" size="80"/>

		<adsm:textbox label="contatoCliente" property="contatoCliente" maxLength="50" dataType="text" width="85%" size="80"/>

		<adsm:textarea label="observacao" property="observacao" maxLength="200" columns="80" rows="3" width="85%"/>

		<adsm:section caption="responsavel" width="90%"/>

		<adsm:hidden property="filial.idFilial"/>
		<adsm:textbox dataType="text" property="filial.sgFilial" 
					  label="filial" size="3" maxLength="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" 
						  size="72" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:textbox dataType="integer" property="usuario.nrMatricula"
			  		  label="responsavel" size="18" maxLength="18" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="usuario.nmUsuario" 
						  size="57" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox label="email" property="usuario.dsEmail" dataType="text" width="85%" size="80" maxLength="255"/>

		<adsm:complement label="telefone" width="85%">
    		<adsm:textbox dataType="integer" property="telefone.nrDdd" maxLength="5" size="5" minValue="0"/>
			<adsm:textbox dataType="text" property="telefone.nrTelefone" maxLength="10" size="10" minValue="0"/>
		</adsm:complement>

		<adsm:complement label="fax" width="85%">
    		<adsm:textbox dataType="integer" property="fax.nrDdd" maxLength="5" size="5" minValue="0"/>
			<adsm:textbox dataType="text" property="fax.nrTelefone" maxLength="10" size="10" minValue="0"/>
		</adsm:complement>

		<adsm:textbox label="ramal" property="ramal" dataType="integer" size="5" maxLength="6" width="85%"/>


		<script>
			function emailEnviadoSucesso() {
				alert("<adsm:label key='emailEnviadoSucesso'/>");
			}
		</script>
		<adsm:buttonBar>
			<adsm:button caption="enviarEmail" id="email" onclick="enviarEmail()"/>
			<adsm:button caption="emitirCarta" id="enviarCarta" onclick="emitirCarta()"/>
			<adsm:button caption="fechar" onclick="returnToParent()" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/**
	 * Faz a chamada para a geracao da carta de mercadoria disposicao.
	 */
	function emitirCarta() {
		
		var dataObject = new Object();
		loadDataFromParent(dataObject);
		dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
		//Determina que este xmit sera para um relatorio...
		var sdo = createServiceDataObject('lms.pendencia.emitirCartaMercadoriasDisposicaoAction.execute',  'emitirCarta', dataObject); 
		xmit({serviceDataObjects:[sdo]});
	}
	
	function emitirCarta_cb(data, error) {
		openReportWithLocator(data, error);
	}
	
	/**
	 * Faz a chamada para a funcao de geracao do email.
	 */
	function enviarEmail() {
		var dataObject = new Object();
		loadDataFromParent(dataObject);
		dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
		
		var parentWindow = dialogArguments.window.document;
		dataObject.idRecusa = parentWindow.getElementById("idRecusa").value;
		
		var sdo = createServiceDataObject("lms.pendencia.emitirCartaMercadoriasDisposicaoAction.sendEmailCartaOcorrencia", "enviarEmail", dataObject);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback do retorno do email.
	 *
	 * @param data
	 * @param error
	 */
	function enviarEmail_cb(data, error) {

		if (data._exception==undefined) {

			var dataObject = new Object();
			loadDataFromParent(dataObject);
			dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
			var parentWindow = dialogArguments.window.document;
				
			var dados = new Array();
			dados.obEnvio = dataObject.formBean.observacao;
			dados.idRecusa = parentWindow.getElementById("idRecusa").value;
			dados.dsEmail = dataObject.formBean.clienteRemetente.pessoa.dsMail;
			if (dados.idRecusa!=""){
				var sdo = createServiceDataObject("lms.vol.recusaAction.store", null, dados);
				xmit({serviceDataObjects:[sdo]});
			}
			
			emailEnviadoSucesso()
			
			returnToParent();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Carrega um objeto contendo dados da 'grid' da tela pai.
	 *
	 * @param dataObject
	 */
	function loadDataFromParent(dataObject) {
		var parentWindow = dialogArguments.window.document;
		var notaFiscalConhecimentoGridDef = parentWindow.getElementById('notaFiscalConhecimento.dataTable').gridDefinition;

		var idObject = null;
		var list = new Array();
		var listSize = 0;
		
		if (notaFiscalConhecimentoGridDef.getSelectedIds().ids.length>0) {
			for (i=0; i<notaFiscalConhecimentoGridDef.getSelectedIds().ids.length; i++) {
				idObject = notaFiscalConhecimentoGridDef.getSelectedIds().ids[i];
	
				for (j=0; j<notaFiscalConhecimentoGridDef.gridState.data.length; j++) {
					var rowData = notaFiscalConhecimentoGridDef.gridState.data[j];
					
					if (rowData.idNotaFiscalConhecimento==idObject){    				
	   					var rowData = notaFiscalConhecimentoGridDef.gridState.data[j];
						
						list[listSize] = new Object();
						list[listSize].idNotaFiscalConhecimento = rowData.idNotaFiscalConhecimento;
						list[listSize].dtSaida  = parentWindow.getElementById(notaFiscalConhecimentoGridDef.property + ":" + i + "." + "dtSaida").value;
	   					listSize++;
					}
				}
			}	
		} else {
			for (var i=0; i<notaFiscalConhecimentoGridDef.gridState.data.length; i++) {
				var rowData = notaFiscalConhecimentoGridDef.gridState.data[i];
				
				list[listSize] = new Object();
				list[listSize].idNotaFiscalConhecimento = rowData.idNotaFiscalConhecimento;
				list[listSize].dtSaida  = parentWindow.getElementById(notaFiscalConhecimentoGridDef.property + ":" + i + "." + "dtSaida").value;
				listSize++;
			}
		}

		//Seta o objeto com os ids de destinatario...
		setNestedBeanPropertyValue(dataObject, "idsNotaFiscalConhecimento", list);
		setNestedBeanPropertyValue(dataObject, "dsOcorrencia", parentWindow.getElementById("ocorrenciaEntrega.dsOcorrenciaEntrega").value);
		setNestedBeanPropertyValue(dataObject, "dhBloqueio", parentWindow.getElementById("dhBloqueio").value);
		return dataObject;
	}

	/**
	 * Fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
</script>
