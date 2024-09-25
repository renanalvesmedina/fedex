<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	/**
	 * Carrega página.
	 */	
	function carregaPagina_cb() {
		setDisabled("btnLimpar", false);
		
		onPageLoad_cb();
		setMasterLink(document, true);
		if(getElementValue("origem") == "regOcorrDoctoServico") {		
			document.getElementById("doctoServico.tpDocumentoServico").onchange();
			lookupChange({e:document.getElementById("doctoServico.idDoctoServico"), forceChange:true});
		}
		
		//Verifica se ao carregar a pagina existe um doctoServico setado...
		if (getElementValue("doctoServico.idDoctoServico")!="") {
			findOcorrenciaDoctoServico(getElementValue("doctoServico.idDoctoServico"));
		}
		
		//Caso o parametro de ocorrenciaEntrega tenha sido preenchido...
		var url = new URL(parent.location.href);
		var doctoServico = url.parameters["doctoServico.idDoctoServico"];
		if (doctoServico!=undefined) {
			document.getElementById("ocorrenciaEntrega.idOcorrenciaEntrega").masterLink=true;
			document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").masterLink=true;
			document.getElementById("ocorrenciaEntrega.dsOcorrenciaEntrega").masterLink=true;
			document.getElementById("dhBloqueio").masterLink=true;
		}
	}
</script>
<adsm:window title="emitirCartaMercadoriasDisposicao" 
			 service="lms.pendencia.emitirCartaMercadoriasDisposicaoAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/pendencia/emitirCartaMercadoriasDisposicao">
		
		<adsm:hidden property="origem"/>
		<adsm:hidden property="idRecusa"/>
		
		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:combobox 
			label="documentoServico"
			width="85%"
			property="doctoServico.tpDocumentoServico"
			service="lms.pendencia.emitirCartaMercadoriasDisposicaoAction.findTipoDocumentoServico"
			optionProperty="value" 
			optionLabelProperty="description"
			onchange="tpDocumentoServico_OnChange(this.value)"
			>

			<adsm:lookup 
				dataType="text"
				property="doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="" popupLabel="pesquisarFilial"
				disabled="true"
				action=""
				size="3" 
				maxLength="3" 
				picker="false" 
				onDataLoadCallBack="enableDoctoServico"
				onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
							 });"/>
							 
							 
			<adsm:lookup 
				dataType="integer"
				property="doctoServico" 
				idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
				service=""
				action=""
				onDataLoadCallBack="retornoDocumentoServico"
				onPopupSetValue="retornoDocumentoServicoPopUp"
				onchange="return nrDoctoServicoOnChange();"
				popupLabel="pesquisarDocumentoServico"
				size="10" 
				maxLength="9" 
				serializable="true" 
				disabled="true" 
				mask="000000000"
				required="true"/>
		</adsm:combobox>

		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="tpOcorrencia" value="R" serializable="false"/>
		<adsm:lookup action="/entrega/manterOcorrenciasEntrega" 
					 service="lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findOcorrenciaEntrega"
					 dataType="integer" exactMatch="true" property="ocorrenciaEntrega" required="true" disabled="true"
					 idProperty="idOcorrenciaEntrega" criteriaProperty="cdOcorrenciaEntrega" criteriaSerializable="true"
					 size="3" width="85%" label="ocorrencia" labelWidth="15%" picker="true">
				<adsm:propertyMapping relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega" modelProperty="dsOcorrenciaEntrega"/>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
				<adsm:propertyMapping criteriaProperty="tpOcorrencia" modelProperty="tpOcorrencia"/>
				<adsm:textbox dataType="text" property="ocorrenciaEntrega.dsOcorrenciaEntrega" size="45" disabled="true" serializable="false"/>						
		</adsm:lookup>
		

		<adsm:textbox property="dhBloqueio" label="dataOcorrencia" dataType="JTDateTimeZone" disabled="true" picker="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="notaFiscalConhecimento"/>
			<adsm:button id="btnLimpar" caption="limpar" onclick="resetView();"/>
		</adsm:buttonBar>
		<script>
			function lms_17009() {
				alert("<adsm:label key='LMS-17009'/>");
			}
			function lms_17010() {
				alert("<adsm:label key='LMS-17010'/>");
			}
			function lms_17038() {
				alert("<adsm:label key='LMS-17038'/>");
			}
		</script>
	</adsm:form>
 
	<adsm:grid property="notaFiscalConhecimento" idProperty="idNotaFiscalConhecimento" onRowClick="disableGridClick" 
			   showPagging="false" showTotalPageCount="false" scrollBars="vertical" gridHeight="255" >
		<adsm:gridColumn property="nrNotaFiscal" title="notaFiscal" dataType="integer" align="right" width="70"/> 					
		<adsm:gridColumn property="dtEmissao" title="dataEmissao" dataType="JTDate" width="140"/> 									
		<adsm:editColumn property="dtSaida" title="dataSaida"  field="TextBox"  dataType="JTDate" width="140"/> 	
		<adsm:gridColumn property="qtVolumes" title="volumes" dataType="integer" align="right" width="105" />															
		<adsm:gridColumn property="psMercadoria" title="peso" dataType="decimal" unit="kg" mask="###,###,###,##0.000" width="105" align="right"/> 												
		<adsm:gridColumn property="siglaSimbolo" dataType="text" title="valor" width="50"/> 											
		<adsm:gridColumn property="vlTotal" title="" dataType="currency" width="90"/> 	
											
		<adsm:buttonBar>
			<adsm:button caption="gerarCarta" onclick="validateGridSelect();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	//##################################
    // Comportamentos apartir de objetos
	//##################################
		
	// INICIO JS da tag de doctoServico ********************************************

	/**
	 * Controla as tags aninhadas para habilitar/desabilitar
	 */
	 function enableDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	
		if (data != undefined && data.length > 0) {
			var idDoctoServico = getElementValue("doctoServico.idDoctoServico");
			setElementValue('doctoServico.idDoctoServico', idDoctoServico);
		
			if (idDoctoServico != undefined && idDoctoServico != '') {
				findOcorrenciaDoctoServico(idDoctoServico);
			}
		}
	}
	
	function retornoDocumentoServicoPopUp(data) {
		findOcorrenciaDoctoServico(data.idDoctoServico);
	}
	// FIM JS da tag de doctoServico ***********************************************
	
	/**
	 * Limpa os elementos da tela apos a mudanca de dados da combo de doctoServico.
	 */
	function limparTela() {
		resetValue("ocorrenciaEntrega.idOcorrenciaEntrega");
		setDisabled('ocorrenciaEntrega.idOcorrenciaEntrega', true);		
		setElementValue("dhBloqueio", "");
		notaFiscalConhecimentoGridDef.resetGrid();
		return true;
	}
	
	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	function validateGridSelect() {
		if(notaFiscalConhecimentoGridDef.getSelectedIds().ids.length>0) {
			// valida se o documento está bloqueado ou não
			validateDocBloqueado();			
		} else {
			lms_17038();
			return false;
		}
	}
	
	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validateDocBloqueado() {	
		var data = new Object();
		data.idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		var sdo = createServiceDataObject("lms.pendencia.emitirCartaMercadoriasDisposicaoAction.validateDocBloqueado", "validateDocBloqueado", data);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	/**
	* Callback da chamada de validacao de documento bloqueado, chama a popUp de alert caso o documento não esteja bloqueado
	* caso contrário chama validação do PCE
	* 
	* @param data
	* @param error
	*/
	function validateDocBloqueado_cb(data, error) {
		if (data.isDocBloqueado === "false") {
			lms_17010();
			return false;
		}
		validatePCE();
	}
	
	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE() {	
		var data = new Object();
		data.idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		var sdo = createServiceDataObject("lms.pendencia.emitirCartaMercadoriasDisposicaoAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception==undefined) {
			if (data.destinatario.codigo!=undefined) {
				showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.destinatario.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
			}
			if (data.remetente.codigo!=undefined) {
				showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.remetente.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
			}
		} else {
			alert(error);
		}
		
		gerarCarta();
	}
	
	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Empty...
	}	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################
		
	function gerarCarta() {
		var url = new URL(parent.location.href);
		var idDoctoServicoValue = getElementValue("doctoServico.idDoctoServico");
		
		if ( url.parameters.idRecusa != undefined ) {
			var idOcorrenciaEntrega = getElementValue("ocorrenciaEntrega.idOcorrenciaEntrega");
			var cdOcorrenciaEntrega = getElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega");
			var dsOcorrenciaEntrega = getElementValue("ocorrenciaEntrega.dsOcorrenciaEntrega");
			var dadosURL = null;
			dadosURL = "&idOcorrenciaEntrega="+idOcorrenciaEntrega+"&cdOcorrenciaEntrega="+cdOcorrenciaEntrega+"&dsOcorrenciaEntrega="+dsOcorrenciaEntrega+"&idDoctoServico="+idDoctoServicoValue;
			showModalDialog('vol/recusaTratativas.do?cmd=carta&idDoctoServico=' + dadosURL,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:400px;');
		} else {
				if (idDoctoServicoValue!="") {
						showModalDialog('pendencia/emitirCartaMercadoriasDisposicao.do?cmd=dados&idDoctoServico=' + idDoctoServicoValue,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:365px;');
				} else {
					lms_17009();
				}
		}
	}
	
	
	
	function findOcorrenciaDoctoServico(idDoctoServicoValue) {
		
		var data = new Object();
		data.idDoctoServico = idDoctoServicoValue;
		
		var sdo = createServiceDataObject("lms.pendencia.emitirCartaMercadoriasDisposicaoAction.findOcorrenciaByIdDoctoServico", "findOcorrenciaDoctoServico", data);
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	/**
	 * Limpa a tela caso o campo de nrDoctoServico seja vazio.
	 */
	function nrDoctoServicoOnChange(){
		if (getElementValue("doctoServico.nrDoctoServico")==""){
			setElementValue("doctoServico.idDoctoServico", "");
			limparTela();
		}
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	
	/**
	 * Funcao de retorno com os dados de ocorrenciaDoctoServico.
	 *
	 * @data
	 * @error
	 */
	function findOcorrenciaDoctoServico_cb(data, error) {
		if ((data._exception==undefined) && (data!=undefined)) {
			setElementValue("ocorrenciaEntrega.idOcorrenciaEntrega", data.idOcorrencia);
			setElementValue("ocorrenciaEntrega.dsOcorrenciaEntrega", data.dsOcorrencia);
			setElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega", data.cdOcorrencia);
			
			// Se tiver tpOcorrencia e se esta for de recusa, entao habilita a lookup de ocorrencia.
			if (data.tpOcorrencia){
				if (data.tpOcorrencia == "R"){
					setDisabled("ocorrenciaEntrega.idOcorrenciaEntrega", false);				
				}
			}
			setElementValue("dhBloqueio", setFormat(document.getElementById("dhBloqueio"), data.dhBloqueio));
		} else if (data._exception!=undefined) {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView(){
		cleanButtonScript(this.document);
		setDisabled("btnLimpar", false);
		tpDocumentoServico_OnChange("");
		setDisabled('ocorrenciaEntrega.idOcorrenciaEntrega', true);
	}
	
	function disableGridClick() {
		return false;
	}
	
	function tpDocumentoServico_OnChange(valor){
		changeDocumentWidgetType({
			documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
			filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
			documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
			parentQualifier:'',
			documentGroup:'SERVICE',
			actionService:'lms.pendencia.emitirCartaMercadoriasDisposicaoAction'});
		return limparTela();
	}
	
</script>