<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script> 

	/**
	 * Carrega os dados da tela pai
	 */
	function loadWindowData() {
		onPageLoad();
		loadData();
		disableButtons(false);
	}
	
	function disableButtons(disable) {
		setDisabled("fechar", disable);
		setDisabled("adicionarDocumentoManual", disable);
		setDisabled("adicionarDocumento", disable);
		setDisabled("limpar", disable);
	}
	
	function loadData(){
		var parentWindow = dialogArguments.window.document;

		//CONTROLE CARGA
		//carrega os objetos		
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
		setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
		//Seta os elementos da tela como masterLink...
		document.getElementById("controleCarga.idControleCarga").masterLink=true;
		document.getElementById("controleCarga.nrControleCarga").masterLink=true;
		document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").masterLink=true;
		
		//MANIFESTO
		//Busca o idManifesto da URL para que o restante dos dados sejam populados a partir da pesquisa de manifesto.
		var url = new URL(parent.location.href);
		var idManifesto = url.parameters["idManifesto"];

		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoDocumentosAction.findManifestoById", "findManifesto", {idManifesto:idManifesto});
    	xmit({serviceDataObjects:[sdo]});
	}
	
	
	function findManifesto_cb(data, error){
		if (error){
			alert (error);
			return false;
		}

		setElementValue("manifesto.idManifesto", data.manifesto.idManifesto);
		setElementValue("manifesto.nrPreManifesto", getFormattedValue("integer", data.manifesto.nrPreManifesto, "00000000", true));
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.manifesto.filialByIdFilialOrigem.sgFilial);
		setElementValue("manifesto.filialByIdFilialDestino.idFilial", data.manifesto.filialByIdFilialDestino.idFilial);
		setElementValue("manifesto.tpModal", data.manifesto.tpModal);
		
		document.getElementById("manifesto.idManifesto").masterLink=true;
		document.getElementById("manifesto.nrPreManifesto").masterLink=true;
		document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").masterLink=true;
		document.getElementById("manifesto.filialByIdFilialdestino.idFilial").masterLink=true;
		document.getElementById("manifesto.tpModal").masterLink=true;
		
		loadGridDocuments();
	}
</script>

<adsm:window title="carregarVeiculo" service="lms.carregamento.carregarVeiculoDocumentosAction" onPageLoad="loadWindowData">
	<adsm:form action="/carregamento/carregarVeiculo" id="formData">
		<adsm:section caption="documentos" />

		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					 label="controleCargas" labelWidth="17%" width="33%" size="3" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" maxLength="12" size="14" mask="0000000000" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="manifesto.tpModal"/> <!-- Utilizada para validação ao selecionar um documento na tela de adicionar documentos -->		
		<adsm:hidden property="manifesto.filialByIdFilialDestino.idFilial"/> <!-- Utilizada para pesquisa da tela de adicionar documentos -->
		<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial" 
					 label="preManifesto" size="3" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox dataType="integer" property="manifesto.nrPreManifesto" maxLength="12" size="14" mask="0000000000" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="17%" width="83%"
					   service="lms.carregamento.carregarVeiculoDocumentosAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.carregamento.carregarVeiculoDocumentosAction'
					   });"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableDoctoServico"
						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
							 });"
						 />
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico" popupLabel="pesquisarDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="preManifestoDocumento" caption="consultar"/>
			<adsm:button id="limpar" caption="limpar" onclick="return resetView();"/> 
		</adsm:buttonBar>
	</adsm:form>		        
		
	<adsm:grid property="preManifestoDocumento" idProperty="idPreManifestoDocumento" scrollBars="horizontal" 
			   gridHeight="250" onRowClick="onRowClick" rows="12" showPagging="true" showTotalPageCount="true"
			   onDataLoadCallBack="validaFinalCarregamento">
			   
		<adsm:gridColumn title="documentoServico" property="tpDocumento" isDomain="true" width="35"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="sgFilialOrigem" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="70" />
		<adsm:gridColumn title="dpe" property="dtPrevEntrega" dataType="JTDate" width="100" align="center" />
		<adsm:gridColumn title="servico" property="sgServico" width="50" />
		<adsm:gridColumn title="remetente" property="clienteRemetente" width="200" />
		<adsm:gridColumn title="destinatario" property="clienteDestinatario" width="200" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="psReal" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorMercadoria" property="sgMoeda" dataType="text" width="30"/>
            <adsm:gridColumn title="" property="dsSimbolo" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlMercadoria" dataType="currency" width="70"/>		
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorFrete" property="sgMoedaFrete" dataType="text" width="30"/>
         	<adsm:gridColumn title="" property="dsSimboloFrete" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlTotalDocServico" dataType="currency" width="70"/>

		<adsm:buttonBar>
			<adsm:button id="adicionarDocumentoManual" caption="adicionarDocumentoManual"  onclick="javascript:adicionarDocumentosManualmente()"/>
			<adsm:button id="adicionarDocumento" caption="adicionarDocumento" onclick="javascript:adicionarDocumentos()"/>
			<adsm:removeButton/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent()"/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			loadData();
			disableButtons(false);
			setFocus(document.getElementById("doctoServico.tpDocumentoServico"));
		}
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	/**
	 * Disabilita a posibilidade de se alterar dados desta tela caso esteja setado
	 * uma data de termino de carregamento na tela pai.
	 */
	function validaFinalCarregamento_cb(data, error){
		var parentWindow = dialogArguments.window.document;
		
		if (parentWindow.getElementById("dhFimOperacao").value!="") {
			preManifestoDocumentoGridDef.disabled=true;
			document.getElementById("adicionarDocumentoManual").disabled = true;
			document.getElementById("adicionarDocumento").disabled = true;
		}
	}
	
	/**
	 * javaScripts para a 'tag documents'
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
	}
	
	/**
	 * Funcao chamada pelo botao de adicionar documentos
	 */
	function adicionarDocumentos(){
		showModalDialog('carregamento/manterGerarPreManifestoAdicionarDocumentos.do?cmd=list&from=carregamento',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:540px;');
		loadGridDocuments();
	}
	
	/**
	 * Funcao chamada pelo botao de adicionar documentos manualmente
	 */
	function adicionarDocumentosManualmente(){
		showModalDialog('carregamento/manterGerarPreManifesto.do?cmd=man&from=carregamento',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:147px;');
		loadGridDocuments();
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Carrega os dados da grid de documentos
	 */
	function loadGridDocuments(){
		var formData = buildFormBeanFromForm(document.getElementById("formData"));
		preManifestoDocumentoGridDef.executeSearch(formData, true);
		
		var parentWindow = dialogArguments.window.document;
		if (parentWindow.getElementById("dhFimOperacao").value!="") {
			preManifestoDocumentoGridDef.disabled=true;
		}
	}
	
	/**
	 * Limpa os objetos da tela.
	 */
	function resetView() {
		resetValue(this.document);
		setDisabled("doctoServico.idDoctoServico", true);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		preManifestoDocumentoGridDef.resetGrid();
		setFocus(document.getElementById("doctoServico.tpDocumentoServico"));
	}
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
	/**
	 * Desabilita o click na row
	 */
	function onRowClick() {
		return false;
	}
</script>