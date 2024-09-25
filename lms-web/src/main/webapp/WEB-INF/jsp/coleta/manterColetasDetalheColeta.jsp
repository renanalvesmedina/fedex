<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.manterColetasAction">

	<adsm:form action="/coleta/manterColetas" idProperty="idDetalheColeta" height="240"
			   service="lms.coleta.manterColetasAction.findByIdDetalheColeta" onDataLoadCallBack="habilitaCamposGridClick">

		<adsm:masterLink showSaveAll="true" idProperty="idPedidoColeta" >
			<adsm:masterLinkItem label="numeroColeta" property="siglaNumeroColeta" itemWidth="25"/>
			<adsm:masterLinkItem label="qtdCtes" property="qtdeTotalDocumentos" itemWidth="30"/>
			<adsm:masterLinkItem label="peso" property="vlPesoTotal" itemWidth="20"/>
			<adsm:masterLinkItem label="valorTotal" property="valorTotalDocumentos" itemWidth="25"/>
		</adsm:masterLink>
		
		<adsm:hidden property="cotacao.idCotacao"/>
		
		<!-- Dados para Pop-up de Liberar Coleta para Destino Bloqueado -->
		<adsm:hidden property="eventoColeta.usuario.idUsuario"/>
		<adsm:hidden property="eventoColeta.ocorrenciaColeta.idOcorrenciaColeta"/>
		<adsm:hidden property="eventoColeta.dsDescricao"/>
		<adsm:hidden property="buttonDestinoBloqueado" value="false"/>
		
		<adsm:hidden property="tipoColeta" />
		<adsm:hidden property="tipoCliente" />
		<adsm:hidden property="tipoStatus" />
		
		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="servico" property="servico.idServico"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.manterColetasAction.findServico" renderOptions="true"
					   required="true" labelWidth="18%" width="32%" boxWidth="230" 
					   onchange="return habilitaCampos()" onlyActiveValues="true"/>
		
		<adsm:hidden property="naturezaProduto.dsNaturezaProduto" />
		<adsm:combobox label="naturezaProduto" property="naturezaProduto.idNaturezaProduto" 
					    optionProperty="idNaturezaProduto" optionLabelProperty="dsNaturezaProduto" 
					    service="lms.coleta.manterColetasAction.findNaturezaProduto" renderOptions="true"
					    required="true" width="35%" onlyActiveValues="true">
					    
			<adsm:propertyMapping relatedProperty="naturezaProduto.dsNaturezaProduto" modelProperty="dsNaturezaProduto" />					    			
		</adsm:combobox>			

		<adsm:combobox label="tipoFrete" property="tpFrete" 
					   domain="DM_TIPO_FRETE" renderOptions="true"
					   required="true" labelWidth="18%" width="32%"
					   onchange="return verificaObrigatoriedadeDestinatario()"
					   defaultValue="C"/>
					   
		<adsm:combobox label="destino" property="destino"
					   domain="DM_MUNICIPIO_LOCALIDADE_ESPECIAL" renderOptions="true"
					   labelWidth="15%" width="32%" required="true"
					   onchange="return habilitaMunicipioOuLocalidade()" 
					   defaultValue="M"/>
		
		<adsm:hidden property="municipio.idMunicipio"/>
		<adsm:lookup label="municipioDestino" 
					 idProperty="idMunicipioFilial" 
					 property="municipioFilial" 
					 criteriaProperty="municipio.nmMunicipio"
					 action="/municipios/manterMunicipiosAtendidos" 
					 exactMatch="false" 					 
					 minLengthForAutoPopUpSearch="3"
					 maxLength="60"					 
					 service="lms.coleta.manterColetasAction.findLookupMunicipio"
					 dataType="text" size="35" labelWidth="18%" width="32%"					 
					 onDataLoadCallBack="municipioFilialOnDataLoadCallBack"
					 onPopupSetValue="municipioFilialOnPopupSetValue"
					 onchange="return limpaCampoMunicipio(this.value)">	 
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
								  modelProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:lookup>
		
		<adsm:hidden property="localidadeEspecial.tpSituacao" value="A" />
		<adsm:lookup label="localidadeEspecial" 
					 idProperty="idLocalidadeEspecial" 
					 property="localidadeEspecial"
					 criteriaProperty="dsLocalidade"
					 maxLength="60"
					 action="/coleta/manterLocalidadesEspeciais" 
					 service="lms.coleta.manterColetasAction.findLookupLocalidadeEspecial" 					
					 exactMatch="false" minLengthForAutoPopUpSearch="3"
					 dataType="text" size="35" width="35%">
			<adsm:propertyMapping criteriaProperty="localidadeEspecial.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="filial.idFilial"/>
  			<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="filial.sgFilial"/>
  			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>	
		</adsm:lookup>

		<adsm:textbox label="filialDestino" property="filial.sgFilial" dataType="text" 
					  size="3" maxLength="3" labelWidth="18%" width="32%" 
					  disabled="true">
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" 
						  disabled="true" />
			<adsm:hidden property="filial.idFilial" />						  
		</adsm:textbox>
		
		<adsm:textbox label="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" 
					  size="3" disabled="true" />

		<adsm:lookup label="destinatario" 
					 idProperty="idCliente" 
					 property="cliente" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.coleta.manterColetasAction.findLookupCliente"
					 action="/coleta/cadastrarPedidoColeta" 
					 cmd="consultarClientes" 
					 dataType="text" size="20" maxLength="20" 
					 labelWidth="18%" width="82%">
			
			<adsm:propertyMapping relatedProperty="nmDestinatario" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="nmDestinatario" size="50" maxLength="50" serializable="true"/>
		</adsm:lookup>
		
		<adsm:combobox label="valor" property="moeda.idMoeda" renderOptions="true"
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   service="lms.coleta.manterColetasAction.findMoeda" 
					   width="12%" labelWidth="18%" boxWidth="85">
					   
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="siglaSimbolo" />			
		</adsm:combobox>
		<adsm:hidden property="moeda.siglaSimbolo" />
		<adsm:textbox property="vlMercadoria" dataType="currency" width="20%" 
					  mask="#,###,###,###,###,##0.00" required="true" 
					  size="16" maxLength="18"/>
		
		<adsm:textbox label="volumes" property="qtVolumes" dataType="integer" 
					  size="6" maxLength="6" required="true" 
					  onchange="return desmarcaAforado()" />

		<adsm:textbox label="peso" property="psMercadoria" dataType="weight" 
					  unit="kg" required="true" 
					  labelWidth="18%" width="22%" size="16" maxLength="15" 
					  onchange="return desmarcaAforado()" />
		
		<adsm:checkbox label="aforado" property="aforado" 
					   labelWidth="10%" width="8%" 
					   onclick="rotinaCalculoPeso()" />
		<adsm:textbox label="pesoAforado" property="psAforado" dataType="weight" 
					  unit="kg" size="16" maxLength="15" labelWidth="15%" width="18%"/>
					  
		
		<adsm:checkbox label="entregaDireta" property="blEntregaDireta" 
					   labelWidth="18%" width="32%" />
		
		
		<adsm:hidden property="ctoInternacional.idDoctoServico" />
		<adsm:hidden property="doctoServico.idDoctoServico" />
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.coleta.cadastrarPedidoColetaAction.findTpDoctoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   label="documentoServico" 
					   labelWidth="18%" 
					   width="32%"
					   disabled="true">
					<adsm:textbox property="doctoServico.filialByIdFilialOrigem.sgFilial" dataType="text"
						 size="3" maxLength="3" disabled="true"/>
					<adsm:textbox property="doctoServico.nrDoctoServico" dataType="integer"
						 size="10" maxLength="10" mask="00000000" disabled="true"/>
		</adsm:combobox>
		
		<adsm:hidden property="awb.idAwb" />
		
		<adsm:hidden property="ctoInternacional.idDoctoServico" />
		<adsm:textbox label="numeroCRT" property="ctoInternacional.sgPais" 
					  dataType="text" size="1" maxLength="2" labelWidth="18%" width="32%" 
					  serializable="false" disabled="true">
			<adsm:textbox property="ctoInternacional.nrCrt" dataType="integer"
						 size="6" maxLength="6" mask="000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:combobox label="ciaAerea" property="ciaFilialMercurio.empresa.idEmpresa" 
					   optionLabelProperty="pessoa.nmPessoa" 
			 		   optionProperty="idEmpresa" renderOptions="true"
					   service="lms.coleta.cadastrarPedidoColetaAction.findCiaAerea" 
					   boxWidth="120" serializable="false" />

		<adsm:listbox label="notaFiscal" property="notaFiscalColetas" 
					  optionProperty="idNotaFiscalColeta" 
					  optionLabelProperty="nrNotaFiscal" 
					  size="9" boxWidth="120" labelWidth="18%" width="32%" labelStyle="vertical-align:top" >
			<adsm:textbox property="nrNotaFiscal" dataType="integer" maxLength="9"
						  serializable="false" />
		</adsm:listbox>

		<adsm:listbox label="chaveNfe" property="nrChaveNfe" onContentChange="contentChange"
					  optionProperty="idNotaFiscalColeta" 
					  optionLabelProperty="nrChave"
					  size="9" boxWidth="280" labelWidth="18%" width="50%" labelStyle="vertical-align:top" >
			<adsm:textbox property="nrChave" dataType="integer" maxLength="44"
						  serializable="false" size="50" onchange="return validateChaveNfe()"/>
		</adsm:listbox>

		<adsm:textarea label="observacao" property="obDetalheColeta" 
					   maxLength="100" columns="92" rows="2" 
					   labelWidth="18%" width="82%" labelStyle="vertical-align:top" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDetalhe" id="salvarDetalheButton" callbackProperty="salvarDetalhe" 
							  service="lms.coleta.manterColetasAction.saveDetalheColeta" />
			<adsm:button caption="limpar" id="newButton" onclick="novoRegistro();"/>	
		</adsm:buttonBar>
		
		<script>
			var lms_02025 = '<adsm:label key="LMS-02025"/>';
			var lms_02027 = '<adsm:label key="LMS-02027"/>';
			var lms_02077 = '<adsm:label key="LMS-02077"/>';
			var lms_04400 = '<adsm:label key="LMS-04400"/>';
			var lms_04508 = '<adsm:label key="LMS-04508"/>';
		</script>		

	</adsm:form>

	<adsm:grid property="detalheColeta" idProperty="idDetalheColeta" detailFrameName="detalheColeta" onRowClick="onRowClick"
			   autoSearch="false" rows="2" selectionMode="check" defaultOrder="servico_.idServico:asc"
			   showPagging="true" showGotoBox="true" gridHeight="40" unique="true" scrollBars="horizontal"
			   service="lms.coleta.manterColetasAction.findPaginatedDetalheColeta" 
			   rowCountService="lms.coleta.manterColetasAction.getRowCountDetalheColeta"
			   onDataLoadCallBack="gridOnDataLoadCallBack">
			   
		<adsm:gridColumn title="servico" property="servico.dsServico" width="200"/>
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="170"/>
		<adsm:gridColumn title="frete" property="tpFrete" isDomain="true" width="50"/>
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right"/>
		<adsm:gridColumn title="peso" property="psMercadoria" dataType="decimal" mask="###,###,###,###,##0.000" align="right" unit="kg" width="105"/>
		<adsm:gridColumn title="pesoAforado" property="psAforado" dataType="decimal" mask="###,###,###,###,##0.000" align="right" unit="kg" width="110"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="moeda.sgMoeda" width="30" />		
			<adsm:gridColumn title="" property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right"/>
		<adsm:gridColumn title="notaFiscal" property="notaFiscal" image="/images/popup.gif" align="center" link="/coleta/cadastrarPedidoColeta.do?cmd=listaNota" linkIdProperty="idDetalheColeta" popupDimension="380,240" width="80" />
		<adsm:gridColumn title="preAwbAwb" property="awb" width="140" align="left"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="documentoServico" property="doctoServico.tpDoctoSgFilial" width="60" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="entregaDireta" property="blEntregaDireta" width="110" renderMode="image-check"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="crt" property="ctoInternacional.sgPais" width="30" />
            <adsm:gridColumn title="" property="ctoInternacional.nrCrt" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>		
		<adsm:gridColumn title="destinatario" property="nmDestinatario" width="300"/>
		<adsm:gridColumn title="municipioDestino" property="municipio.nmMunicipio" width="130"/>
		<adsm:gridColumn title="localidadeEspecial" property="localidadeEspecial.dsLocalidade" width="200"/>
		<adsm:gridColumn title="filialDestino" property="filial.sgFilial" width="105"/>
		
		<adsm:buttonBar>
			<adsm:button id="restricoesColeta" caption="produtosProibidos" onclick="exibeRestricoes();" />				
			<adsm:removeButton caption="excluirDetalhe" service="lms.coleta.manterColetasAction.removeByIdsDetalheColeta" />
		</adsm:buttonBar>			
		
	</adsm:grid>		

</adsm:window>

<script type="text/javascript">
	document.getElementById("servico.dsServico").serializable = true;
	document.getElementById("naturezaProduto.dsNaturezaProduto").serializable = true;
	document.getElementById("moeda.siglaSimbolo").serializable = true;
	document.getElementById("nmDestinatario").serializable = true;
	document.getElementById("municipioFilial.municipio.nmMunicipio").serializable = true;
	document.getElementById("localidadeEspecial.dsLocalidade").serializable = true;
	
	// Pega a aba 'cad' para pegar os valores dos properties
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");	
	
	function initWindow(eventObj) {
		setDisabled("newButton", false);		
		setDisabled("ciaFilialMercurio.empresa.idEmpresa", true);
		setDisabled("restricoesColeta", false);
		setDisabled("nrChaveNfe", true);
		setDisabled("nrChaveNfe_nrChave", true);
		
		var idPedidoColeta = tabDet.getFormProperty("idPedidoColeta");
		var idDetalheColeta = getElementValue("idDetalheColeta");
		
		if(idPedidoColeta != "" && idDetalheColeta == "") {
			setDisabled("salvarDetalheButton", true);
		} else {
			setDisabled("salvarDetalheButton", false);		
		}
		
		desabilitaCamposByStatusColeta();
		
		if (idDetalheColeta == "") {
			setDisabled("servico.idServico", true);
		}
		carregaDados();
	}
	
	function gridOnDataLoadCallBack_cb(data, error) {
		populaSomatorios();
	}
	
	function populaSomatorios(){
      	var remoteCall = {serviceDataObjects:new Array()};
      	var dataCall = createServiceDataObject("lms.coleta.manterColetasAction.findSomatorios", "populaSomatorios", 
            {
                  idColeta:getElementValue("masterId")
            }
      	);
      	remoteCall.serviceDataObjects.push(dataCall);
      	xmit(remoteCall);  
	
	}
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			return;
		}
		
		var telaCad = tabDet.tabOwnerFrame;	
		
		setElementValue(telaCad.document.getElementById("qtdeTotalDocumentos"), data.qtdeTotalDocumentos);
		setElementValue(telaCad.document.getElementById("vlPesoTotal"), setFormat(telaCad.document.getElementById("psTotalInformado"), data.vlPesoTotal));
		setElementValue(telaCad.document.getElementById("valorTotalDocumentos"), setFormat(telaCad.document.getElementById("vlTotalInformado"),  data.valorTotalDocumentos));
		
		tabGroup.selectTab("cad");
		tabGroup.selectTab("detalheColeta");
	}
	
	
	/**
	 * Carrega dados na tela
	 */
	function carregaDados() {		
		if(getElementValue("servico.idServico").length == 0) {			
			setDisabled("naturezaProduto.idNaturezaProduto", true);
			setDisabled("tpFrete", true);
			setDisabled("destino", true);
			setDisabled("municipioFilial.idMunicipioFilial", true);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			setDisabled("cliente.idCliente", true);
			setDisabled("nmDestinatario", true);
			setDisabled("moeda.idMoeda", true);
			setDisabled("vlMercadoria", true);
			setDisabled("qtVolumes", true);
			setDisabled("psMercadoria", true);
			setDisabled("aforado", true);
			setDisabled("psAforado", true);
			setDisabled("notaFiscalColetas", true);
			setDisabled("notaFiscalColetas_nrNotaFiscal", true);			
			setDisabled("obDetalheColeta", true);
			setDisabled("blEntregaDireta", true);
		}
	
		buscarDadosUsuarioSessao();	

		document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";

		var tipoColeta = tabDet.getFormProperty("tpPedidoColeta");
		if(tipoColeta == "DE") {
			document.getElementById("nmDestinatario").required = "true";
		}	
		
		// Seta o label para o campo valor de mercadoria
		document.getElementById("vlMercadoria").label = document.getElementById("moeda.idMoeda").label;
		document.getElementById("moeda.idMoeda").label = "Moeda de valor";
		document.getElementById("moeda.idMoeda").required = "true";	
		
		// Seta o label para o campo nmDestinatario
		document.getElementById("nmDestinatario").label = document.getElementById("cliente.idCliente").label;		
	}
	
	/**
	 * Retorno do Salvamento do registro de Detalhe Coleta
	 */
	function salvarDetalhe_cb(data, erros, errorMsg, eventObj) {
		storeItem_cb(data, erros, errorMsg, eventObj);
		newButtonScript();
	}		
	
	/**
	 * Limpa a tela para um novo registro.
	 */	
	function novoRegistro() {
		newButtonScript();
		setFocusOnFirstFocusableField(this.document);
	}		
		
	/**
	 * Habilita campos caso o serviço seja informado
	 */
	function habilitaCampos() {
		var idServico = getElementValue("servico.idServico");
		
		if(idServico.length > 0) {
			var comboBoxServico = document.getElementById("servico.idServico");
			setElementValue("servico.dsServico", comboBoxServico.options[comboBoxServico.selectedIndex].text);
			
			setDisabled("naturezaProduto.idNaturezaProduto", false);
			setDisabled("tpFrete", false);
			setDisabled("destino", false);
			setDisabled("municipioFilial.idMunicipioFilial", false);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			setDisabled("cliente.idCliente", false);
			setDisabled("nmDestinatario", false);
			setDisabled("moeda.idMoeda", false);
			setDisabled("vlMercadoria", false);
			setDisabled("qtVolumes", false);
			setDisabled("psMercadoria", false);
			setDisabled("aforado", false);		
			setDisabled("psAforado", false);		
			setDisabled("notaFiscalColetas", false);
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);
			setDisabled("obDetalheColeta", false);
		} else {
			setElementValue("servico.dsServico", "");
		
			setDisabled("naturezaProduto.idNaturezaProduto", true);
			setDisabled("tpFrete", true);
			setDisabled("destino", true);
			setDisabled("municipioFilial.idMunicipioFilial", true);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			setDisabled("cliente.idCliente", true);
			setDisabled("nmDestinatario", true);
			setDisabled("moeda.idMoeda", true);
			setDisabled("vlMercadoria", true);
			setDisabled("qtVolumes", true);
			setDisabled("psMercadoria", true);
			setDisabled("aforado", true);		
			setDisabled("psAforado", true);		
			setDisabled("notaFiscalColetas", true);
			setDisabled("notaFiscalColetas_nrNotaFiscal", true);
			setDisabled("obDetalheColeta", true);		
		}
			
		return true;
	}
	
	/**
	 * Habilita lookup de municipio ou localidade especial de acordo com o escolhido na combobox.
	 */
	function habilitaMunicipioOuLocalidade() {
		var comboBox = document.getElementById("destino");
		if(comboBox.value == "M") {
			resetValue("localidadeEspecial.idLocalidadeEspecial");
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");
			setDisabled("municipioFilial.idMunicipioFilial", false);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "false";
		} else if(comboBox.value == "L") {
			resetValue("municipioFilial.idMunicipioFilial");
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");			
			setDisabled("municipioFilial.idMunicipioFilial", true);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "false";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", false);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "true";
		} 
		
		return true;
	}
	
	/**
	 * Habilita os campos apos popular o form ao clicar no registro
	 */
	function habilitaCamposGridClick_cb(data) {
		if(data.municipioFilial != undefined) {
			setNestedBeanPropertyValue(data, "destino", "M");
			setDisabled("municipioFilial.idMunicipioFilial", false);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "false";			
		}
		if(data.localidadeEspecial != undefined) {
			setNestedBeanPropertyValue(data, "destino", "L");
			setDisabled("municipioFilial.idMunicipioFilial", true);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "false";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", false);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "true";			
		}
		
		setDisabled("nrChaveNfe", false);
		setDisabled("nrChaveNfe_nrChave", false);

		onDataLoad_cb(data);
		
		desabilitaCamposByStatusColeta();
		
		if(data.psAforado != undefined && data.psAforado != "") {
			document.getElementById("aforado").checked = true;
		} else {
			document.getElementById("aforado").checked = false;
		}			
	}

	function desabilitaCamposByStatusColeta(){
		var tipoStatus = tabDet.getFormProperty("tpStatusColeta");
		var tipoColeta = tabDet.getFormProperty("tpPedidoColeta");
		if(tipoColeta == "AE"){
			setDisabled("blEntregaDireta", false);
			setDisabled("salvarDetalheButton", false);
			setDisabled("municipioFilial.idMunicipioFilial", true);
			if(tipoStatus == "CA"){
				setDisabled("salvarDetalheButton", true);
			}
		}else{
			setDisabled("blEntregaDireta", true);
			if(tipoStatus == "EX" || tipoStatus == "AD" || tipoStatus == "NT" || tipoStatus == "ED") {
				setDisabled("servico.idServico", true);
				setDisabled("municipioFilial.idMunicipioFilial", true);
				setDisabled("localidadeEspecial.idLocalidadeEspecial", true);			
				setDisabled("vlMercadoria", false);
				setDisabled("qtVolumes", false);
				setDisabled("psMercadoria", false);
				setDisabled("aforado", false);	
				setDisabled("psAforado", false);
				setDisabled("obDetalheColeta", false);	
			} 
			
			if(tipoStatus == "CA" || tipoStatus == "FI") { 
				setDisabled("servico.idServico", true);
				setDisabled("municipioFilial.idMunicipioFilial", true);
				setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
				setDisabled("salvarDetalheButton", true);
				setDisabled("aforado", true);
				setDisabled("psAforado", true);		
			}
			
			if(tipoStatus != "EX" && tipoStatus != "AD" && tipoStatus != "NT" && 
			   tipoStatus != "ED" && tipoStatus != "CA" && tipoStatus != "FI" ) {
			    setDisabled("servico.idServico", false);
				setDisabled("naturezaProduto.idNaturezaProduto", false);
				setDisabled("tpFrete", false);
				setDisabled("destino", false);
				setDisabled("cliente.idCliente", false);
				setDisabled("nmDestinatario", false);
				setDisabled("moeda.idMoeda", false);
				setDisabled("vlMercadoria", false);
				setDisabled("qtVolumes", false);
				setDisabled("psMercadoria", false);
				setDisabled("aforado", false);		
				setDisabled("psAforado", false);		
				setDisabled("notaFiscalColetas", false);
				setDisabled("notaFiscalColetas_nrNotaFiscal", false);
				setDisabled("obDetalheColeta", false);			
			}	
		}
	}
	
	/**
	 * Chama PopUp de calculo de Peso Aforado.
	 */
	function rotinaCalculoPeso() {
		if(getElementValue("qtVolumes") != "" && getElementValue("psMercadoria") != "") {
			var checkBox = document.getElementById("aforado");	
			if(checkBox.checked) {
				window.showModalDialog('coleta/cadastrarPedidoColeta.do?cmd=pesoAforado',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:300px;dialogHeight:215px;');
				//Se fechou a janela pelo X, entao deve desmarcar o checkbox.
				if (getElementValue("psAforado") == ""){
					document.getElementById("aforado").checked = false;
				}
			} else {
				setElementValue("psAforado", "");
			}
		} else {
			desmarcaAforado();
			alert(lms_02025);
		}		
	}
	
	/**
	 * Pega o resultado da PopUp de calculo de Peso Aforado.
	 */
	function resultadoRotinaCalculoPeso(resultado) {
		setElementValue("psAforado", setFormat(document.getElementById("psAforado"), resultado));
	}
	
	
	/**
	 * Busca os Dados referente ao usuário da sessão.
	 */
	function buscarDadosUsuarioSessao() {
		var comboBox = document.getElementById("moeda.idMoeda");
		for(var i=0; i < comboBox.length; i++) {
			if(comboBox.options[i].text == tabDet.getFormProperty("moeda.siglaSimbolo")) {
				setElementValue("moeda.siglaSimbolo", tabDet.getFormProperty("moeda.siglaSimbolo"));
				comboBox.options[i].selected = "true";
			}
		}
	}
	
	/**
	 * Verifica se o tipo de Frete é FOB e se o tipo cliente não for um cliente especial.
	 */
	function verificaObrigatoriedadeDestinatario() {
		var tipoCliente = tabDet.getFormProperty("cliente.tpCliente");
		var tipoFrete = getElementValue("tpFrete");
		
		if(tipoFrete == "F" && (tipoCliente != "" && tipoCliente != "S")) {
			document.getElementById("cliente.pessoa.nrIdentificacao").required = "true";
		} else {
			document.getElementById("cliente.pessoa.nrIdentificacao").required = "false";
		}
		
		return true;
	}
	
	/**
	 * Função chamada no onDataLoadCallBack da lookup de Municipio Filial.
	 */
	function municipioFilialOnDataLoadCallBack_cb(data) {
		municipioFilial_municipio_nmMunicipio_exactMatch_cb(data);
		if (data.length == 1) {
			validaVigenciaMunicipioFilial(data[0]);
		}
	}

	/**
	 * Função chamada no onPopupSetValue da lookup de Municipio Filial.
	 */	
	function municipioFilialOnPopupSetValue(data) {
		validaVigenciaMunicipioFilial(data);
	}
	
	/**
	 * Função que verifica a vigencia do municipio selecionado.
	 */
	function validaVigenciaMunicipioFilial(data) {
		var mapCriteria = new Array();    
		setNestedBeanPropertyValue(mapCriteria, "idMunicipioFilial", data.idMunicipioFilial);
	   	setNestedBeanPropertyValue(mapCriteria, "idMunicipio", data.municipio.idMunicipio);
	
	   	var sdo = createServiceDataObject("lms.coleta.manterColetasAction.validaVigenciaAtendimento", "resultado_validaVigenciaAtendimento", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da verificação de vigencia do municipio.
	 */		
	function resultado_validaVigenciaAtendimento_cb(data, error) {
		if (!error) {
			verificaMunicipioDestinoBloqueado(data);
		} else {
			alert(error);
			resetValue("municipioFilial.idMunicipioFilial");
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");	
			setElementValue("filial.pessoa.nmFantasia", "");				
			setFocus("municipioFilial.municipio.nmMunicipio");
		}
	}		
	
	/**
	 * Verifica se o Municipio recebe coletas eventuais, caso NÃO, chamar 
	 * especificação 02.06.01.03 Liberar Coleta para Destino Bloqueado 
	 */
	function verificaMunicipioDestinoBloqueado(data) {
		var mapCriteria = new Array();    
	   	setNestedBeanPropertyValue(mapCriteria, "idMunicipio", data.idMunicipio);
	    setNestedBeanPropertyValue(mapCriteria, "idServico", getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(mapCriteria, "idCliente", tabDet.getFormProperty("cliente.idCliente"));
	    setNestedBeanPropertyValue(mapCriteria, "tpFrete", getElementValue("tpFrete"));
	    
	   	var sdo = createServiceDataObject("lms.coleta.manterColetasAction.getMunicipioDestinoBloqueado", "resultado_verificaMunicipioDestinoBloqueado", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da verificação de Destino Bloqueado
	 */
	function resultado_verificaMunicipioDestinoBloqueado_cb(data, error) {
		if (error){
			alert(error);
			clearMunicipio();
			setFocus("municipioFilial.municipio.nmMunicipio");		
			return false;
		}
		setElementValue("eventoColeta.usuario.idUsuario", "");
		setElementValue("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta", "");
		setElementValue("eventoColeta.dsDescricao", "");
		
		setElementValue("filial.idFilial", "");
		setElementValue("filial.sgFilial", "");	
		setElementValue("filial.pessoa.nmFantasia", "");		
		if (data.filialId != undefined) {
			setElementValue("filial.idFilial",data.filialId);
			setElementValue("filial.sgFilial",data.filialSigla);
			setElementValue("filial.pessoa.nmFantasia",data.filialNome);
		} else {
			alert(lms_02077);
			resetValue("municipioFilial.idMunicipioFilial");
			setElementValue("municipio.idMunicipio", "");
		}
		
		var tipoCliente = tabDet.getFormProperty("cliente.tpCliente");
		if (data.recebeColetaEventual != undefined && 
			data.recebeColetaEventual == "false" &&
			data.hasToValidateEmbarqueProibido == "true" ) {
				if(data.bloqCredEmbProib != undefined && data.bloqCredEmbProib == "true"){
					alert(lms_04508);
					return;
				}
			
				window.showModalDialog('coleta/liberarColetasDestinoBloqueado.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:760px;dialogHeight:490px;');
				if(getElementValue("buttonDestinoBloqueado") == "false") {
					resetValue("municipioFilial.idMunicipioFilial");
					setElementValue("filial.idFilial", "");
					setElementValue("filial.sgFilial", "");	
					setElementValue("filial.pessoa.nmFantasia", "");
				} else {
					setElementValue("buttonDestinoBloqueado", "false");
				}
		}
	}
	
	/**
	 * Função que lima o campo da filial caso o municipio esteja em branco.
	 */
	function limpaCampoMunicipio(value) {
		var boolean = municipioFilial_municipio_nmMunicipioOnChangeHandler();
		if (value == "") {
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");	
			setElementValue("filial.pessoa.nmFantasia", "");
		}
		return boolean;
	}
	
	/**
	 * Desmarca o checkbox de Aforado
	 */
	function desmarcaAforado() {
		setElementValue("psAforado", "");
		document.getElementById("aforado").checked = false;
	}

	/**
	 * Seta os dados da Pop-up de Liberar Bloqueio de Crédito para Coleta.
	 */
	function setaDadosPopupLiberarColetaDestinoBloqueado(mapDestinoBloqueado) {
		setElementValue("eventoColeta.usuario.idUsuario", mapDestinoBloqueado.idUsuario);	
		setElementValue("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta", mapDestinoBloqueado.idOcorrenciaColeta);
		setElementValue("eventoColeta.dsDescricao", mapDestinoBloqueado.dsDescricao);
		setElementValue("buttonDestinoBloqueado", "true");
	}
	
	/**
	 * Função que manipula o rowClick na grid.
	 */
	function onRowClick() {
	} 	
	
	/**
	 * Função que ajusta o campo sigla pais da lookup de CRT.
	 */
	function sgPaisChange(campo) {
	 	var isValid = getElementValue(campo).length > 0;
	 	if(isValid) {
	 		setElementValue(campo, getElementValue(campo).toUpperCase());
	 	}
	 	resetValue("ctoInternacional.idDoctoServico");
	 	return true;
	}	
	
	/**
	 * Função chamado no onPopupValue da Lookup de CRT
	 */
	function findLookupCRT(data) {
		setElementValue("ctoInternacional.sgPais", data.sgPais);
		setElementValue("ctoInternacional.nrCrt", data.nrCrt);
		return lookupChange({e:document.getElementById("ctoInternacional.idDoctoServico"), forceChange:true});
	}
	
	function exibeRestricoes() {
		showModalDialog('/coleta/cadastrarPedidoColeta.do?cmd=restricoesColeta',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}	
	
  	function validateChaveNfe() {
		var chaveNfe = getElementValue("nrChaveNfe_nrChave");
		
		if(chaveNfe.length >= 44){
			if(!validateDigitoVerificadorNfe(chaveNfe)){
				setElementValue("nrChaveNfe_nrChave","");
				return false;
			}
		}else{
			alert(lms_04400);
			setElementValue("nrChaveNfe_nrChave","");
			return false;
		}
		
		var sizeNFe = nrChaveNfeListboxDef.getData().length;
		
		nrChaveNfeListboxDef.insertOrUpdateOption();

		if(sizeNFe != nrChaveNfeListboxDef.getData().length){
			setaValorNotaFiscal(chaveNfe);
		}
		else{
			setElementValue("nrChaveNfe_nrChave","");
		}
		
		return true;
	}
  	
  	/**
  	* Eventos de modificação de conteúdo do listbox
  	*/
  	function contentChange(event){
		if(event.name == "deleteButton_click"){
			removeNF();
		}
	}
  	
  	/**
  	* Remove a Nota Fiscal de acordo com a chave NFe removida
  	*/
  	function removeNF(){
  		var notasFiscais = notaFiscalColetasListboxDef.getData();
		var nfToCompare = parseInt(getElementValue("nrChaveNfe_nrChave").substring(25, 34) , 10);
		var element = document.getElementById("notaFiscalColetas");
		var elementDisabled = element.elementDisabled;
		
		if (elementDisabled == true) {
			setDisabled("notaFiscalColetas", false);	
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);
		}
		
		for( var i = 0 ; i < notaFiscalColetasListboxDef.getData().length ; i++ ){
			if(parseInt(notasFiscais[i].nrNotaFiscal,10) == nfToCompare){
				element.selectedIndex = i;
				notaFiscalColetasListboxDef.deleteOption();
			}
		}
		
		setDisabled("notaFiscalColetas", elementDisabled);	
		setDisabled("notaFiscalColetas_nrNotaFiscal", elementDisabled);
  	}

  	
	/**
	 * Valida o digito verificador da Chave Nfe
	 */
	function validateDigitoVerificadorNfe(chaveNfe){
		var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1, chaveNfe.length);
		var chave = chaveNfe.substring(0, chaveNfe.length - 1);
		var calculoChaveNfe = modulo11(chave);
		
		if(dvChaveNfe == (calculoChaveNfe)){
			return true;
		}else{
			alert(lms_04400);
			return false;
		}
	}
	
	function modulo11(chave){
		var n = new Array();
		var peso = 2;
		var soma = 0;

		n = chave.split('');
		
		for (var i = n.length-1; i >= 0; i--) {
			var value = n[i];
			soma = soma + value * peso++;
			if(peso == 10){
				peso = 2;
			}
		}
		
		var mod = soma % 11;
		var dv;
		
		if(mod == 0 || mod == 1){
			dv = 0;
		} else {
			dv = 11 - mod;
		}
		
		return dv
	}
	
	function setaValorNotaFiscal(chaveNfe){
		var elementDisabled = document.getElementById("notaFiscalColetas").elementDisabled;
		
		if (elementDisabled == true) {
			setDisabled("notaFiscalColetas", false);	
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);
		}
		
		var nrNf = chaveNfe.substring(25, 34);
		
		setElementValue("notaFiscalColetas_nrNotaFiscal", parseInt(nrNf,10));
		notaFiscalColetasListboxDef.insertOrUpdateOption();

		setDisabled("notaFiscalColetas", elementDisabled);	
		setDisabled("notaFiscalColetas_nrNotaFiscal", elementDisabled);
	}
	
</script>