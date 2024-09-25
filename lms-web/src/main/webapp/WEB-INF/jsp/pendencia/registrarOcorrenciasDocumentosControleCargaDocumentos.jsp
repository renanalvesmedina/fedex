<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript"> 
	/**
	 * Carrega os dados da tela pai
	 */
	function loadWindowData_cb(data, error) {	
		onPageLoad_cb(data, error);
		var parentWindow = dialogArguments.window.document;
		
		//carrega os objetos
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
		setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
				
		//busca os dados da URL
		var url = new URL(parent.location.href);
		var idManifesto = url.parameters["idManifesto"];
		var sgFilial = url.parameters["sgFilial"];
		var nrManifesto = url.parameters["nrManifesto"];		
		
		setElementValue("manifesto.idManifesto", idManifesto);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("manifesto.nrManifesto", setFormat(document.getElementById("manifesto.nrManifesto"), nrManifesto));	
	}
</script>

<adsm:window service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaDocumentosAction" onPageLoadCallBack="loadWindowData">
	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosControleCarga">
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					  label="controleCargas" labelWidth="17%" width="33%" size="3" maxLength="3" 
					  disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" 
						  size="8" mask="00000000" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="manifesto.idManifesto"/>		
		<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial" 
					 label="manifesto" size="3" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox dataType="integer" property="manifesto.nrManifesto" maxLength="8" 
						  size="8" mask="00000000" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:combobox label="documentoServico"
					   labelWidth="17%" width="33%"
					   property="doctoServico.tpDocumentoServico"
					   service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaDocumentosAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
								 documentTypeElement:this,
								 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
								 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
								 documentGroup:'SERVICE',
								 actionService:'lms.pendencia.registrarOcorrenciasDocumentosControleCargaDocumentosAction' });">
								 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
						 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 popupLabel="pesquisarFilial"
						 onDataLoadCallBack="enableDoctoServico"
						 onchange="return doctoServicoFilialByIdFilialOrigem_OnChange(this.value);"/>
								   
			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 popupLabel="pesquisarDocumentoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"
						 onchange="return doctoServicoNrDoctoServico_OnChange(this.value)"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 mask="00000000" />
		</adsm:combobox>		

		<adsm:textbox dataType="text" property="notaFiscal" label="notaFiscal" maxLength="20"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="documentos" caption="consultar"/>
			<adsm:button id="reset" caption="limpar" onclick="resetView()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="documentos" idProperty="idDoctoServico" selectionMode="none" onRowClick="onRowClick"
			   unique="true" scrollBars="both" showPagging="true" showTotalPageCount="true" gridHeight="285" rows="50">			   
		<adsm:gridColumn title="servico" property="sgServico" width="70" />	   
		<adsm:gridColumn title="documentoServico" property="tpDocumento" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="sgFilialOrigem" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="70" />		
		<adsm:gridColumn title="remetente" property="clienteRemetente" width="160" />
		<adsm:gridColumn title="destinatario" property="clienteDestinatario" width="160" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="psReal" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorMercadoria" property="sgMoeda" dataType="text" width="30"/>
            <adsm:gridColumn title="" property="dsSimboloMoeda" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlMercadoria" dataType="currency" width="70"/>		
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorFrete" property="sgMoeda2" dataType="text" width="30"/>
         	<adsm:gridColumn title="" property="dsSimboloMoeda2" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlTotalDocServico" dataType="currency" width="70"/>        
        <adsm:gridColumn title="dpe" property="dtPrevEntrega" dataType="JTDate" width="100" align="center" />
        <adsm:gridColumn title="localizacao" property="dsLocalizacaoMercadoria" width="200" dataType="text"/>
        
		<adsm:buttonBar>
			<adsm:button caption="fechar" buttonType="storeButton" onclick="self.close()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>	

</adsm:window>

<script type="text/javascript">
	
	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {	
		setDisabled("reset", false);
	}

	/**
	 * #############################################################
	 * # Inicio das funções para a tag customizada de DoctoServico #
	 * #############################################################
	 */

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
		var boolean = doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if (boolean == true) {
			if (data != undefined && data.length > 0) {
				setElementValue("doctoServico.idDoctoServico", data[0].idDoctoServico);
			}
		}
	}
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
											filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
								      		documentNumberElement:document.getElementById("doctoServico.idDoctoServico") } );
		
		if (valor == "") {
			var tpDocumentoServico = getElementValue("doctoServico.tpDocumentoServico");
       	    setDisabled(document.getElementById("doctoServico.nrDoctoServico"), true, undefined, true);
       	    setElementValue("doctoServico.tpDocumentoServico", tpDocumentoServico);
		}
		
		return boolean;		
	}	
	
	function doctoServicoNrDoctoServico_OnChange(valor) {
		var boolean = doctoServico_nrDoctoServicoOnChangeHandler();
		if (valor == "") {
			var tpDocumentoServico = getElementValue("doctoServico.tpDocumentoServico");
			var idFilial = getElementValue("doctoServico.filialByIdFilialOrigem.idFilial");
   	        var sgFilial = getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial");
    		resetView();    		
          	setElementValue("doctoServico.tpDocumentoServico", tpDocumentoServico);
           	setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
            setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
            setDisabled("doctoServico.idDoctoServico", false);
		}		
		return boolean;		
	}	
		
	/**
	 * ##########################################################
	 * # Fim das funções para a tag customizada de DoctoServico #
	 * ##########################################################
	 */

	
	/**
	 * Reseta os campos da tela e a grid
	 */
	function resetView() {
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		var nrControleCarga = getElementValue("controleCarga.nrControleCarga");
		var sgFilialControleCarga = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");		
		var idManifesto = getElementValue("manifesto.idManifesto");
		var nrManifesto = getElementValue("manifesto.nrManifesto");
		var sgFilialManifesto = getElementValue("manifesto.filialByIdFilialOrigem.sgFilial");
           
		resetValue(this.document);
		documentosGridDef.resetGrid();
		setFocus(document.getElementById("doctoServico.tpDocumentoServico"));
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
		
		setElementValue("controleCarga.idControleCarga", idControleCarga);
		setElementValue("controleCarga.nrControleCarga", nrControleCarga);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilialControleCarga);
		setElementValue("manifesto.idManifesto", idManifesto);
		setElementValue("manifesto.nrManifesto", nrManifesto);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", sgFilialManifesto);
	}
	
	function onRowClick() {
		return false;
	}
</script>