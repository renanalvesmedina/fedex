<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.consultarMercadoriasPendenciaAction">
	<adsm:form action="/pendencia/consultarMercadoriasPendencia" height="169" >

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>

		<adsm:range label="periodo" labelWidth="28%" width="72%" required="true">
			<adsm:textbox dataType="JTDateTimeZone" property="dhInclusaoInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhInclusaoFinal"/>
		</adsm:range>
		
		<adsm:lookup property="filialAbertura" dataType="text" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.pendencia.consultarMercadoriasPendenciaAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="filialEmitente" maxLength="3" size="3" labelWidth="28%" width="72%">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
            <adsm:textbox property="filialAbertura.pessoa.nmFantasia" dataType="text" size="50" disabled="true"/>
            <adsm:propertyMapping relatedProperty="filialAbertura.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        </adsm:lookup>
		
		<adsm:lookup property="filialResponsavel" dataType="text" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.pendencia.consultarMercadoriasPendenciaAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="filialResponsavel" maxLength="3" size="3" labelWidth="28%" width="72%">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
            <adsm:textbox property="filialResponsavel.pessoa.nmFantasia" dataType="text" size="50" disabled="true"/>
            <adsm:propertyMapping relatedProperty="filialResponsavel.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        </adsm:lookup>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="28%" width="72%"
					   service="lms.pendencia.consultarMercadoriasPendenciaAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.pendencia.consultarMercadoriasPendenciaAction'
					   });"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 popupLabel="pesquisarFilial"
						 serializable="true"
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableNaoConformidadeDoctoServico"
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
						 onDataLoadCallBack="retornoDocumentoServico"
						 popupLabel="pesquisarDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
		</adsm:combobox>
		
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:hidden property="tpSituacao" value="A"/>
		
		<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNC" optionLabelProperty="dsMotivoAbertura" optionProperty="idMotivoAberturaNc" 
					   service="lms.pendencia.consultarMercadoriasPendenciaAction.findMotivoAberturaNc"  
					   label="motivoAberturaNaoConformidade" labelWidth="28%" width="72%"/>
		
		<adsm:lookup dataType="text" property="clienteRemetente" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.pendencia.consultarMercadoriasPendenciaAction.findClienteLookup" 
					 label="remetente" labelWidth="28%" width="72%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="clienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="clienteDestinatario" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.pendencia.consultarMercadoriasPendenciaAction.findClienteLookup" 
					 label="destinatario" labelWidth="28%" width="72%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="clienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="clienteDestinatario.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textarea label="descricaoNaoConformidade" property="dsOcorrenciaNC" maxLength="1000" columns="79" rows="4" labelWidth="28%" width="72%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="btnConsultar" onclick="validaCampos();" caption="consultar" disabled="false"/>
			<adsm:button caption="limpar" onclick="cleanButton();" id="btnLimpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="naoConformidade" idProperty="idNaoConformidade" 
			   gridHeight="110" unique="true" scrollBars="horizontal" selectionMode="none"
			   onRowClick="disableGridClick" rows="5">
		<adsm:gridColumn title="documentoServico" property="tpDoctoServico" isDomain="true" width="30"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="30" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="rnc" property="sgFilialNC" width="30" />
			<adsm:gridColumn title="" property="nrNaoConformidade" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="ocorrencia" property="nrOcorrenciaNc" width="70" align="right" dataType="integer" mask="00000000"/>
		<adsm:gridColumn title="motivoAbertura" property="dsMotivoAbertura" width="140"/>
		<adsm:gridColumn title="remetente" property="nmPessoaRemetente" width="140"/>
		<adsm:gridColumn title="destinatario" property="nmPessoaDestinatario" width="140"/>
		<adsm:gridColumn title="descricao" property="dsOcorrenciaNc" width="140"/>
		<adsm:gridColumn property="dadosRNC" title="dadosRNC" align="center" image="/images/popup.gif" link="javascript:abrirRNCGrid" linkIdProperty="idNaoConformidade" width="70"/>
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>

<script>

	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	function initWindow(eventObj) {
		setDisabled("btnLimpar" ,false);
		setDisabled("btnConsultar" ,false);
	}
	
	function cleanButton() {
		cleanButtonScript(this.document);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
	}
	
	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableNaoConformidadeDoctoServico_cb(data) {
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
	 * Chama a tela de ocorrencia nao conformidade.
	 *
	 * @param idNaoConformidade o id da nao conformidade selecionada
	 */
	function abrirRNCGrid(idNaoConformidade) {
		var dataObject = getDataNaoConformidadeGrid(idNaoConformidade);
		parent.parent.redirectPage('rnc/manterOcorrenciasNaoConformidade.do?cmd=main' + getParametersURL(dataObject));
	}
	
	/**
	 * Busca os parametros
	 */
	function getParametersURL(dataObject){
		var parameters = "";
		if (dataObject.sgFilialNC){
			parameters += "&naoConformidade.filial.sgFilial=" + dataObject.sgFilialNC;
		}
		if (dataObject.idNaoConformidade){
			parameters += "&naoConformidade.idNaoConformidade=" + dataObject.idNaoConformidade;
		}
		if (dataObject.nrNaoConformidade){	
			parameters += "&naoConformidade.nrNaoConformidade=" + getFormattedValue("integer", dataObject.nrNaoConformidade, "00000000", true);
		}
		if (dataObject.idDoctoServico){
			parameters += "&naoConformidade.doctoServico.idDoctoServico=" + dataObject.idDoctoServico;
			parameters += "&naoConformidade.doctoServico.tpDocumentoServico.description=" + dataObject.tpDoctoServico.description;
		}
		if (dataObject.sgFilialOrigem){
			parameters += "&naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial=" + dataObject.sgFilialOrigem;
		}
		if (dataObject.nrDoctoServico){
			parameters += "&naoConformidade.doctoServico.nrDoctoServico=" + getFormattedValue("integer", dataObject.nrDoctoServico, "00000000", true);
		}
		return parameters;
	}
	
	/**
	 * Retorna um objeto naoConformidade da grid selecionado.
	 */
	function getDataNaoConformidadeGrid(idNaoConformidade){
		for (i=0; i<naoConformidadeGridDef.gridState.data.length; i++) {
			if (naoConformidadeGridDef.gridState.data[i].idNaoConformidade==idNaoConformidade){
				return naoConformidadeGridDef.gridState.data[i];
			}
		}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	function disableGridClick(){
		return false;
	}
	
	/**
	* Função de validação de campos obrigatórios (filiais)
	*/
	function validaCampos(){
		if(validateForm(this.document.forms[0])){
		    var sdo = createServiceDataObject("lms.pendencia.consultarMercadoriasPendenciaAction.validaCamposObrigatorios", "retornoValidaCampos", buildFormBeanFromForm(this.document.forms[0]));
		    xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/**
	* Retorno da função de validação de campos obrigatórios (filiais)
	*/
	function retornoValidaCampos_cb(data, error){
		if (error){
			alert(error);
			setFocus(document.getElementById("filialAbertura.sgFilial"));
		} else {
			var x =	buildFormBeanFromForm(this.document.forms[0]); 
		    naoConformidadeGridDef.executeSearch(x, true);
		}
	}
	
</script>