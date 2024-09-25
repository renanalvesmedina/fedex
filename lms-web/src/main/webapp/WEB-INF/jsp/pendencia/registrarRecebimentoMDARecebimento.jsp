<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.registrarRecebimentoMDAAction">
	<adsm:form action="/pendencia/registrarRecebimentoMDA">
		<adsm:hidden property="origem"/>
		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.pendencia.registrarRecebimentoMDAAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="20%" width="30%" 
					 size="5" maxLength="3" picker="false" serializable="false">			
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="lms.pendencia.registrarRecebimentoMDAAction.findMdaByNrDoctoServicoByIdFilialOrigem" 
						 action="/pendencia/consultarMDA"
						 afterPopupSetValue="carregaDadosMda"
						 onDataLoadCallBack="carregaDadosMda" 
						 onchange="return checkValueMda(this.value)"						 
						 dataType="integer" maxLength="10" size="10" mask="00000000" >
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" disable="true"/>
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" blankFill="false"/>
				<adsm:propertyMapping criteriaProperty="origem"	modelProperty="origem" />											
			</adsm:lookup>					 
		</adsm:lookup>		

		<adsm:textbox property="dhRecebimento" label="recebimento" dataType="JTDateTimeZone" 
					  labelWidth="20%" width="30%" picker="false" disabled="true"/>

		<adsm:textbox property="nmRecebedorCliente" label="recebedorCliente" dataType="text" 
					  labelWidth="20%" width="80%" size="50" maxLength="60" disabled="true"/>

		<adsm:lookup property="usuarioByIdUsuarioRecebidoPor"
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView"
					 service="lms.pendencia.registrarRecebimentoMDAAction.findLookupUsuarioFuncionario" 
					 dataType="text" label="recebedorFuncionario" size="16" 
					 maxLength="16" labelWidth="20%" width="80%" exactMatch="true" 
					 onchange="return limpaCamposFuncionario();" disabled="true">
 				<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioRecebidoPor.nmUsuario"
									  modelProperty="nmUsuario" />
									  
			<adsm:textbox property="usuarioByIdUsuarioRecebidoPor.nmUsuario" dataType="text" 
						  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox property="filialOrigem.sgFilial"
					  label="filialOrigem" dataType="text" labelWidth="20%" width="80%"
					  size="5" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox property="filialOrigem.pessoa.nmFantasia" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="filialByIdFilialDestino.sgFilial"
					  label="filialDestino" dataType="text" labelWidth="20%" width="80%"
					  size="5" maxLength="3" disabled="true" serializable="false">
			<adsm:textbox property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"
					  label="remetente" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"
					  label="destinatario" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado"
					  label="consignatario" dataType="text" labelWidth="20%" width="80%"
					  size="20" maxLength="20" disabled="true" serializable="false">
			<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="moeda.siglaSimbolo" label="valorFrete" labelWidth="20%"
					  width="80%" dataType="text" size="8" maxLength="8" disabled="true" serializable="false">
			<adsm:textbox property="vlTotalDocServico" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="registrarRecebimento" id="registrarButton" onclick="registrarRecebimento(this.form)"/>
			<adsm:button caption="limpar" disabled="false" id="btnLimpar" buttonType="resetButton" onclick="resetButtonFunction()"/>
		</adsm:buttonBar>
		
		<script>
			var lms_17004 = '<adsm:label key="LMS-17004"/>';
		</script>
	</adsm:form>

	<adsm:grid property="itemMda" idProperty="idItemMda" gridHeight="120" selectionMode="none"
			   scrollBars="both" unique="true" onRowClick="myOnRowClick"
			   service="lms.pendencia.registrarRecebimentoMDAAction.findPaginatedItemMda"
			   rowCountService="lms.pendencia.registrarRecebimentoMDAAction.getRowCountItemMda">
		
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>		
		<adsm:gridColumn title="descricaoMercadoria" property="dsMercadoria" width="205"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="naoConformidade" property="naoConformidade.filial.sgFilial" width="40" />
            <adsm:gridColumn title="" property="naoConformidade.nrNaoConformidade" width="90" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" property="psItem" dataType="decimal" mask="#,###,##0.000" align="right" unit="kg" width="70"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorMercadoria" property="moeda.sgMoeda" width="30" />		
			<adsm:gridColumn title="" property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right"/>				
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="150" />
		<adsm:gridColumn title="observacoes" property="obItemMda" width="200" />
		
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	document.getElementById("usuarioByIdUsuarioRecebidoPor.nrMatricula").style.textAlign = "right";
	var u = new URL(parent.location.href);
   	var objOrigem = document.getElementById("origem");
   	setElementValue(objOrigem, u.parameters["origem"]);
   	objOrigem.masterLink = "true";

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {		
		if(eventObj.name == "tab_load" || eventObj.name == "cleanButton_click") {
			setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);			
			setDisabled("registrarButton", true);			
		}
		if(getElementValue("origem") == "consultarMda") {
			populaTela();
		} else {
			setElementValue("origem", "registrarRecebimentoMda");
		}		
	}
	
	/**
	 * Função que popula a tela.
	 */
	function populaTela() {		
		//setDisabled("mda.idDoctoServico", true);
		//setDisabled("filialByIdFilialOrigem.idFilial", true);
		setDisabled("mda.idDoctoServico", false);
		setElementValue("filialByIdFilialOrigem.idFilial", u.parameters["filialByIdFilialOrigem.idFilial"]);
		setElementValue("filialByIdFilialOrigem.sgFilial", u.parameters["filialByIdFilialOrigem.sgFilial"]);
		setElementValue("mda.idDoctoServico", u.parameters["idDoctoServico"]);
		setElementValue("mda.nrDoctoServico", u.parameters["nrDoctoServico"]);
   		lookupChange({e:document.getElementById("mda.idDoctoServico"), forceChange:true});
	 }

	/**
	 * Controla o objeto de DoctoServico
	 */	
	function sgFilialOnChangeHandler() {	
		var x = lookupChange({e:document.forms[0].elements["filialByIdFilialOrigem.idFilial"]});
		if (getElementValue("filialByIdFilialOrigem.sgFilial") == "") {
			setDisabled(document.getElementById("mda.nrDoctoServico"), true, undefined, true);
		} else {
			setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);
		}
		checkValueMda();
		return x;
	}
	
	function disableNrDoctoServico_cb(data, error) {
		if (data.length == 0) {
			setDisabled(document.getElementById("mda.nrDoctoServico"), false, undefined, true);
		}
		return lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueMda(valor) {		
		var x = mda_nrDoctoServicoOnChangeHandler();
		if (valor == "" || !valor) {			
       	    resetCamposDependentesMDA();
       	    itemMdaGridDef.resetGrid();
		}		
		return x;
	}	

	/**
	 * Função chamada no retorno dos dados da pop-up na lookup de MDA.
	 */
	function carregaDadosMda(){
		setDisabled("mda.idDoctoServico", false);
		lookupChange({e:document.forms[0].elements["mda.idDoctoServico"], forceChange:true});
	}

	/**
	 * Função chamada no retorno dos dados de callback na lookup de MDA.
	 */	
	function carregaDadosMda_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		mda_nrDoctoServico_exactMatch_cb(data);

		if(data[0]) {
			resetCamposDependentesMDA();

			if(data[0].tpStatusMda.value == "E") {
				setDisabled("registrarButton", false);
				if(data[0].tpDestinatarioMda.value == "F") {
					setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", false);
					document.getElementById("usuarioByIdUsuarioRecebidoPor.nrMatricula").required = "true";
					setDisabled("nmRecebedorCliente", true);
					document.getElementById("nmRecebedorCliente").required = "false";			
				} else if(data[0].tpDestinatarioMda.value == "C") {
					setDisabled("nmRecebedorCliente", false);
					document.getElementById("nmRecebedorCliente").required = "true";
					setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", true);
					document.getElementById("usuarioByIdUsuarioRecebidoPor.nrMatricula").required = "false";
				}				
			} else {
				setDisabled("nmRecebedorCliente", true);
				setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", true);
				if(data[0].tpDestinatarioMda.value == "F" && data[0].usuarioByIdUsuarioRecebidoPor != undefined) {
					setElementValue("usuarioByIdUsuarioRecebidoPor.idUsuario", data[0].usuarioByIdUsuarioRecebidoPor.idUsuario);
					setElementValue("usuarioByIdUsuarioRecebidoPor.nrMatricula", data[0].usuarioByIdUsuarioRecebidoPor.nrMatricula);
					setElementValue("usuarioByIdUsuarioRecebidoPor.nmUsuario", data[0].usuarioByIdUsuarioRecebidoPor.nmUsuario);
				} else if(data[0].tpDestinatarioMda.value == "C" && data[0].nmRecebedorCliente != undefined) {
					setElementValue("nmRecebedorCliente", data[0].nmRecebedorCliente);
				}					
			}	
			
			if(data[0].dhRecebimento) {
				setElementValue("dhRecebimento", setFormat(document.getElementById("dhRecebimento"), data[0].dhRecebimento));			
			}
			
			setElementValue("filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("filialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("filialOrigem.pessoa.nmFantasia", data[0].filialByIdFilialOrigem.pessoa.nmFantasia);		
			if(data[0].filialByIdFilialDestino) {
				setElementValue("filialByIdFilialDestino.sgFilial", data[0].filialByIdFilialDestino.sgFilial);
				setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", data[0].filialByIdFilialDestino.pessoa.nmFantasia);
			}
			if(data[0].clienteByIdClienteRemetente) {
				setElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteRemetente.pessoa.nmPessoa", data[0].clienteByIdClienteRemetente.pessoa.nmPessoa);
			}
			if(data[0].clienteByIdClienteDestinatario) {
				setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteDestinatario.pessoa.nmPessoa", data[0].clienteByIdClienteDestinatario.pessoa.nmPessoa);
			}
			if(data[0].clienteByIdClienteConsignatario) {
				setElementValue("clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado", data[0].clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado);
				setElementValue("clienteByIdClienteConsignatario.pessoa.nmPessoa", data[0].clienteByIdClienteConsignatario.pessoa.nmPessoa);
			}
			
	   		setElementValue("moeda.siglaSimbolo", data[0].moeda.siglaSimbolo);
	    	setElementValue("vlTotalDocServico", setFormat(document.getElementById("vlTotalDocServico"), data[0].vlTotalDocServico));			
	    	
	    	carregaGrid();	    	
		}			
	}
	
	/**
	 * Limpa os campos de Recebedor Funcionario.
	 */
	function limpaCamposFuncionario() {				
		if(getElementValue("usuarioByIdUsuarioRecebidoPor.nrMatricula") == "" && 
		   getElementValue("usuarioByIdUsuarioRecebidoPor.nmUsuario") != "") {
			resetValue("usuarioByIdUsuarioRecebidoPor.idUsuario");
		}
		var x = usuarioByIdUsuarioRecebidoPor_nrMatriculaOnChangeHandler();
		
		return x;
	}	
	
	/**
	 * Função que salva o registro em questão.
	 */
	function registrarRecebimento(form) {
		var mapCriteria = new Array();	   
   		setNestedBeanPropertyValue(mapCriteria, "idDoctoServico", getElementValue("mda.idDoctoServico"));	
		var sdo = createServiceDataObject("lms.pendencia.registrarRecebimentoMDAAction.validaDoctoServico", "validaDoctoServico", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da validação do documento de serviço.
	 */
	function validaDoctoServico_cb(data, error) {
		if (error){
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}

     	if(validateForm(this.document.forms[0])) {
			if(window.confirm(lms_17004)) {
				storeButtonScript("lms.pendencia.registrarRecebimentoMDAAction.store", "registrarRecebimento", this.document.forms[0]);
			}
		}
	}	
	
	/**
	 * Função de CallBack de salvar.
	 */
	function registrarRecebimento_cb(data, erros, errorMsg, eventObj) {
		store_cb(data, erros, errorMsg, eventObj);
		if (erros){
			alert(erros);
			setFocusOnFirstFocusableField();
			return false;
		}
		setElementValue("dhRecebimento", setFormat(document.getElementById("dhRecebimento"), data.dhRecebimento));
		setDisabled("registrarButton", true);
		setDisabled("nmRecebedorCliente", true);
		setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", true);
		setFocus("btnLimpar", false);
	}	

	/**
	 * Função que retorna 'false' caso uma linha da grid tenha sido clicada.
	 */
	function myOnRowClick(id) {
		return false;
	}
	
	/**
	 * Função que pesquisa os dados da grid.
	 */
	function carregaGrid() {	
		var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
		itemMda_cb(fb);	
	}	
	
	/**
	 * Função executada pelo botão Limpar.
	 */
	function resetButtonFunction() {
		resetValue(this.document);
		itemMdaGridDef.resetGrid();
		setDisabled("mda.nrDoctoServico", true);
		setDisabled("nmRecebedorCliente", true);
		setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", true);
		setDisabled("registrarButton", true);
		setFocusOnFirstFocusableField();
	}
	
	/**
	 *	Função que limpa todos os campos da tela, com exceção dos campos do MDA.
	 */
	function resetCamposDependentesMDA(){
		resetValue("dhRecebimento");
		resetValue("nmRecebedorCliente");
		resetValue("usuarioByIdUsuarioRecebidoPor.idUsuario");
		resetValue("filialOrigem.sgFilial");
		resetValue("filialOrigem.pessoa.nmFantasia");	
		resetValue("filialByIdFilialDestino.sgFilial");
		resetValue("filialByIdFilialDestino.pessoa.nmFantasia");		
		resetValue("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado");
		resetValue("clienteByIdClienteRemetente.pessoa.nmPessoa");		
		resetValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado");
		resetValue("clienteByIdClienteDestinatario.pessoa.nmPessoa");
		resetValue("clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado");
		resetValue("clienteByIdClienteConsignatario.pessoa.nmPessoa");
		resetValue("moeda.siglaSimbolo");
		resetValue("vlTotalDocServico");
		setDisabled("nmRecebedorCliente", true);
		setDisabled("usuarioByIdUsuarioRecebidoPor.idUsuario", true);       	    
       	setDisabled("registrarButton", true);
	}
	
</script>