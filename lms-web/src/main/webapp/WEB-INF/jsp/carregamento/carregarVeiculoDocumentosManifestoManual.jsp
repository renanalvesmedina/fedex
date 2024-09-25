<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

	/**
	 * Carrega informacoes da tela pai.
	 */
	function loadData() {
	 
		onPageLoad();
		
		var parentWindow = dialogArguments.window.document;
		setElementValue("idManifesto", parentWindow.getElementById("manifesto.idManifesto").value);
		setElementValue("manifesto.nrPreManifesto", parentWindow.getElementById("manifesto.nrPreManifesto").value);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").value);
	}
</script>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.carregarVeiculoDocumentosManifestoAction" onPageLoad="loadData">
	<adsm:form action="/carregamento/manterGerarPreManifestoAdicionarDocumentos" height="65">

		<adsm:section caption="adicionarDocumentoManualTitulo"/>
		<adsm:hidden property="idManifesto"/>

		<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial" 
					  label="preManifesto" labelWidth="20%" width="80%" size="3" maxLength="3" disabled="true"  >
			<adsm:textbox dataType="integer" property="manifesto.nrPreManifesto" size="10" 
						  maxLength="8" mask="0000000000" disabled="true" />
		</adsm:textbox>

		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="20%" width="80%"
					   service="lms.carregamento.carregarVeiculoDocumentosAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.carregamento.carregarVeiculoDocumentosManifestoAction'
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
						 popupLabel="pesquisarDocumentoServico"
						 property="doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico" required="true"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="salvar" service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.savePreManifestoDocumento" callbackProperty="savePreManifestoDocumento"/>
  		    <adsm:button caption="fechar" id="fechar" onclick="javascript:window.close();"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script type="text/javascript">

	setDisabled("fechar", false);
	setDisabled("salvar", false);
	
	function savePreManifestoDocumento_cb(data, error) {
		if (data._exception==undefined) {
			returnToParent();
		} else {
			alert(data._exception._message);
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
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
</script>