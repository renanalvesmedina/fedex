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
		var idOcorrenciaEntrega = url.parameters["idOcorrenciaEntrega"];
		var cdOcorrenciaEntrega = url.parameters["cdOcorrenciaEntrega"];
		var dsOcorrenciaEntrega = url.parameters["dsOcorrenciaEntrega"];

		setElementValue("doctoServico.idDoctoServico", idDoctoServicoValue);
		setElementValue("ocorrenciaEntrega.idOcorrenciaEntrega", idOcorrenciaEntrega);
		setElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega", cdOcorrenciaEntrega);
		setElementValue("ocorrenciaEntrega.dsOcorrenciaEntrega", dsOcorrenciaEntrega);
		
		var data = new Object();
		data.idDoctoServico = idDoctoServicoValue;
		var sdo = createServiceDataObject("lms.vol.recusaTratativasCartaAction.findBasicData", "loadBasicData", data);
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
		setElementValue("clienteRemetente.idCliente", data.clienteRemetente.idCliente);
		setElementValue("clienteRemetente.pessoa.nmPessoa", data.clienteRemetente.pessoa.nmPessoa);
		setElementValue("clienteRemetente.pessoa.nrIdentificacao", data.clienteRemetente.pessoa.nrIdentificacao);
		
		setElementValue("pessoa.idPessoa", data.clienteRemetente.idCliente);
		setElementValue("pessoa.nmPessoa", data.clienteRemetente.pessoa.nmPessoa);
		setElementValue("pessoa.pessoa.nrIdentificacao", data.clienteRemetente.pessoa.nrIdentificacao);

		setElementValue("filial.idFilial", data.filial.idFilial);
		setElementValue("filial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
		setElementValue("filial.sgFilial", data.filial.sgFilial);
			
		setElementValue("usuario.nmUsuario", data.usuario.nmUsuario);
		setElementValue("usuario.nrMatricula", data.usuario.nrMatricula);
		setElementValue("usuario.dsEmail", data.usuario.dsEmail);

	}
	
	/**
	 * Habilita os botao da tela. 
	 *
	 * @param disable
	 */
	function loadObjects(disable) {
		setDisabled("fechar", disable);
		setDisabled("email", disable);
	}
	
</script>

<adsm:window title="dadosCarta" service="lms.vol.recusaTratativasCartaAction" onPageLoad="loadData">
	<adsm:form action="/vol/recusaTratativas">

		<adsm:section caption="dadosCarta" width="90%"/> 
		
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		
		<adsm:hidden property="clienteRemetente.idCliente"/>
		<adsm:textbox dataType="integer" property="clienteRemetente.pessoa.nrIdentificacao" label="responsavel"  size="18"
				 maxLength="18" width="85%" disabled="true">
		<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa"  size="57" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="sgFilialContato" serializable="true"/>		
		<adsm:hidden property="nmFantasiaFiliaContato" serializable="true"/>
		<adsm:hidden property="pessoa.idPessoa" serializable="true"/>
		<adsm:hidden property="pessoa.pessoa.nrIdentificacao" serializable="true"/>	
		<adsm:hidden property="pessoa.nmPessoa" serializable="true"/>	
		
		<adsm:listbox label="emailContato" size="4" boxWidth="170" width="40%" labelWidth="180"
					  property="contatos" 
					  optionProperty="dsEmail" 
					  optionLabelProperty="nmContato" 
					  required="true"
					  onContentChange="retornoContato"
					  >
			<adsm:lookup property="contato"
						 idProperty="idContato"
		                 criteriaProperty="dsEmail"
		  				 action="/vol/manterContatosVol"
		  				 service="lms.vol.recusaTratativasCartaAction.findContato"
		  				 cmd="list"
		  				 dataType="text"
						 exactMatch="false"
						 minLengthForAutoPopUpSearch="3"
		  				 size="30"
		  				 serializable="true"
		  				 required="true" 
		  				 >
   		  		  <adsm:propertyMapping relatedProperty="sgFilialContato" criteriaProperty="filial.sgFilial" disable="false" />  
   		  		  <adsm:propertyMapping relatedProperty="nmFantasiaFiliaContato" criteriaProperty="filial.pessoa.nmFantasia" disable="false" />	
   		  		  <adsm:propertyMapping relatedProperty="pessoa.idPessoa" criteriaProperty="pessoa.idPessoa" /> 
  	 	  		  <adsm:propertyMapping relatedProperty="pessoa.pessoa.nrIdentificacao" criteriaProperty="pessoa.pessoa.nrIdentificacao" disable="true"/> 
  	 	  		  <adsm:propertyMapping relatedProperty="pessoa.nmPessoa" criteriaProperty="pessoa.nmPessoa"/> 
  	 	  		  	 	  		  
  		   </adsm:lookup>
		</adsm:listbox>	
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="isRecusa" value="R" serializable="true"/>
		<adsm:lookup action="/entrega/manterOcorrenciasEntrega" 
					 service="lms.vol.recusaTratativasCartaAction.findOcorrenciaEntregaByCodigoTipo" 
					 dataType="integer" 
					 exactMatch="true"
					 property="ocorrenciaEntrega"
					 required="true"
					 idProperty="idOcorrenciaEntrega"
					 criteriaProperty="cdOcorrenciaEntrega"
					 criteriaSerializable="true"
					 size="3" width="85%" label="ocorrencia" labelWidth="15%" picker="true">
				<adsm:propertyMapping relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega" modelProperty="dsOcorrenciaEntrega"/>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
				<adsm:propertyMapping criteriaProperty="isRecusa" modelProperty="isRecusa"/>
				<adsm:textbox dataType="text" property="ocorrenciaEntrega.dsOcorrenciaEntrega" size="45" disabled="true" serializable="true"/>						
		</adsm:lookup>
 
		<adsm:textarea label="observacao" property="observacao" maxLength="200" columns="80" rows="3" width="85%"/>

		<adsm:section caption="responsavel" width="90%"/>

		<adsm:hidden property="filial.idFilial"/>
		<adsm:textbox dataType="text" property="filial.sgFilial"  label="filial" size="3" maxLength="3" width="85%" disabled="true">
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

		<adsm:complement label="telefone" width="250" >
    		<adsm:textbox dataType="integer" property="telefone.nrDdd" maxLength="5" size="5" minValue="0"/>
			<adsm:textbox dataType="text" property="telefone.nrTelefone" maxLength="10" size="10" minValue="0"/>
		</adsm:complement>
		
		<adsm:textbox label="ramal" property="ramal" dataType="integer" size="5" maxLength="6" labelWidth="80" />

		<adsm:complement label="fax" width="85%">
    		<adsm:textbox dataType="integer" property="fax.nrDdd" maxLength="5" size="5" minValue="0"/>
			<adsm:textbox dataType="text" property="fax.nrTelefone" maxLength="10" size="10" minValue="0"/>
		</adsm:complement>

		

		<script>
			function emailEnviadoSucesso() {
				alert("<adsm:label key='emailEnviadoSucesso'/>");
			}
		</script>

		<adsm:buttonBar>
			<adsm:button caption="enviarEmail" id="email" onclick="enviarEmail(this.form);"/>
			<adsm:button caption="emitirCarta" id="enviarCarta" onclick="emitirCarta(this.form);" disabled="false"/>
			<adsm:button caption="fechar" onclick="returnToParent()" id="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:i18nLabels>
		<adsm:include key="emailEnviadoSucesso"/>
	</adsm:i18nLabels>
	
</adsm:window>

<script>

	
	function retornoContato( data, error ){
		var dataObject = new Object();
		dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);

		setElementValue("pessoa.idPessoa", dataObject.formBean.clienteRemetente.idCliente );
		setElementValue("pessoa.pessoa.nrIdentificacao", dataObject.formBean.clienteRemetente.pessoa.nrIdentificacao );
		setElementValue("pessoa.nmPessoa", dataObject.formBean.clienteRemetente.pessoa.nmPessoa );
	}
	
	/**
	 * Faz a chamada para a geracao da carta de mercadoria disposicao.
	 */
	function emitirCarta( form ) {
	
		if (!validateForm(form)) {
			return false;
		} 
		
		var dataObject = new Object();
		loadDataFromParent(dataObject);
		dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
		dataObject.dsOcorrencia = dataObject.formBean.ocorrenciaEntrega.dsOcorrenciaEntrega;
		dataObject.formBean.contatoCliente = "";
		var sdo = createServiceDataObject('lms.pendencia.emitirCartaMercadoriasDisposicaoAction.execute',  'emitirCarta', dataObject); 
		xmit({serviceDataObjects:[sdo]});
		setFocus('fechar',true,true); 
	}
				
	function emitirCarta_cb(data, error) {
		openReportWithLocator(data, error);
	}
	
	/**
	 * Faz a chamada para a funcao de geracao do email.
	 */
	function enviarEmail( form ) {
	
		if (!validateForm(form)) {
			return false;
		}
		
		var dataObject = new Object();
		loadDataFromParent(dataObject);
		dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
		
		var parentWindow = dialogArguments.window.document;
		dataObject.idRecusa = parentWindow.getElementById("idRecusa").value;
		dataObject.dsOcorrencia = dataObject.formBean.ocorrenciaEntrega.dsOcorrenciaEntrega;
		
		var sdo = createServiceDataObject("lms.vol.recusaTratativasCartaAction.sendEmailCartaOcorrencia", null, dataObject);
		xmit({serviceDataObjects:[sdo]});
		
		storeRecusa_cb(dataObject);
	}
	
	/**
	 * Callback do enviarEmail
	 *
	 * @param data
	 * @param error
	 */
	function storeRecusa_cb(data, error) {
		if (data._exception==undefined) {
		
			var obEnvio = data.formBean.observacao;
			var idRecusa = data.idRecusa;
			var dsEmail = data.formBean.contatos;
			var idOcorrenciaEntrega = data.formBean.ocorrenciaEntrega.idOcorrenciaEntrega;	
			var idDoctoServico = data.formBean.doctoServico.idDoctoServico;
			
			var dados = new Array();
			dados.obEnvio = obEnvio;
			dados.idRecusa = idRecusa;
			dados.dsEmail = dsEmail;
			dados.idOcorrenciaEntrega = idOcorrenciaEntrega;
			dados.idDoctoServico = idDoctoServico;
			var sdo = createServiceDataObject("lms.vol.recusaAction.store", null, dados );
			xmit({serviceDataObjects:[sdo]});

			alert(i18NLabel.getLabel("emailEnviadoSucesso"));	
			
			returnToParent();
		} else {
			alert(data._exception._message);
		}
	}
	
	function mensagemEmailEnviado_cb( data, error ){
		if( data._exception==undefined ){
			confirm(i18NLabel.getLabel("emailEnviadoSucesso"));		
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

