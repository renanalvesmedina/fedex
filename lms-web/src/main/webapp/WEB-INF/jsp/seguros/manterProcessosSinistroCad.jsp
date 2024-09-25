<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterProcessosSinistroAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/seguros/manterProcessosSinistro" height="390" idProperty="idProcessoSinistro" 
			onDataLoadCallBack="onDataLoadCallback" id="processoSinistroForm">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-22031"/>
			<adsm:include key="LMS-22032"/>
			<adsm:include key="LMS-22033"/>
		</adsm:i18nLabels>
		
		<adsm:textbox property="nrProcessoSinistro" label="numeroProcesso" dataType="text" labelWidth="17%" width="83%" disabled="false" required="true"/>
		
		<adsm:textbox property="situacao" label="situacao" dataType="text" labelWidth="17%" width="83%" disabled="true"/>
		<adsm:hidden property="situacaoHidden"/>
		
		<adsm:textbox property="nrBoletimOcorrencia" label="numeroBoletimOcorrencia" dataType="text" size="20" labelWidth="17%" width="83%" maxLength="10"/>
		
		<adsm:textbox property="dhSinistro" label="dataHoraSinistro" dataType="JTDateTimeZone" onchange="changeValidateDH();" size="30%" labelWidth="17%" width="83%" maxLength="16" required="true"/>

		<adsm:textbox property="dhAbertura" label="dataHoraAbertura" dataType="JTDateTimeZone" size="16%" labelWidth="17%" width="20%" maxLength="16" disabled="true" picker="false"/>
		
		<adsm:textbox property="usuarioAbertura.idUsuario" label="usuarioResponsavel" dataType="text" size="15" disabled="true" labelWidth="15%" width="48%">
		<adsm:textbox property="usuarioAbertura.nmUsuario" dataType="text" size="45" disabled="true"/>
		</adsm:textbox>
				
		<adsm:textbox property="dhFechamento" label="dataHoraFechamento" dataType="JTDateTimeZone" size="16%" labelWidth="17%" width="83%" maxLength="16" disabled="true" picker="false"/>
		
		<adsm:combobox 
			property="tipoSeguro.idTipoSeguro" 
			label="tipoSeguro" 
			optionLabelProperty="sgTipo" 
			optionProperty="idTipoSeguro" 
			service="lms.seguros.manterProcessosSinistroAction.findComboTipoSeguro" 
			labelWidth="17%" 
			width="83%" 
			required="true" 
			onlyActiveValues="true"
			onchange="findTipoSinistro();"
		/>

		<adsm:combobox 
			property="tipoSinistro.idTipoSinistro" 
			label="tipoSinistro" 
			labelWidth="17%" 
			width="83%" 
			boxWidth="200" 
			optionLabelProperty="dsTipo" 
			optionProperty="idTipoSinistro" 
			required="true" 
			onchange="findCorretoraAndSeguradora();"
		/>
				
		<adsm:textbox property="corretora" label="reguladora" dataType="text" size="42%" labelWidth="17%" width="83%" disabled="true"/>
		
		<adsm:textbox property="seguradora" label="seguradora" dataType="text" size="42%" labelWidth="17%" width="83%" disabled="true"/>
						
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="17%" width="22%" size="8" serializable="false" maxLength="6"
				onchange="return meioTransporteRodoviario_Onchange(this);"
				onDataLoadCallBack="onDataLoadLookupVeiculo">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador"/>
								  
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
								  modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador"/>		
				
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
					picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="8" required="false" onPopupSetValue="onPopupSetVeiculo" onDataLoadCallBack="onDataLoadLookupVeiculo"
					onchange="return meioTransporteRodoviario_Onchange(this);">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota"/>
									  
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
									  modelProperty="idMeioTransporte"/>	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota"/>	
			</adsm:lookup>
        </adsm:lookup>
				
		<adsm:textbox property="tpVeiculo" label="tipoVeiculo" dataType="text" size="20%" labelWidth="11%" width="18%" disabled="true"/>
		<adsm:textbox property="nrCertificado" label="certificado" dataType="text" size="20%" labelWidth="9%" width="23%" disabled="true"/>

		<adsm:lookup property="motorista" label="motorista" dataType="text" size="15" maxLength="20" labelWidth="17%" width="40%"
					 idProperty="idMotorista"					 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.seguros.manterProcessosSinistroAction.findLookupMotorista"  					  
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5">			
			<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="motorista.tpVinculo" modelProperty="tpVinculo.description"/>      	
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="25" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox property="motorista.tpVinculo" label="tipoVinculo" dataType="text" size="20%" labelWidth="11%" width="32%" disabled="true"/>
		
		<adsm:textarea property="dsSinistro" label="descricaoSinistro" maxLength="1500" columns="119" labelWidth="17%" width="83%" required="true"/>
				
		<adsm:combobox property="localSinistro" label="localSinistro" labelWidth="17%" width="83%" domain="DM_LOCAL_SINISTRO" required="true" onchange="onLocalSinistroChange(this.options[this.selectedIndex].value );" renderOptions="true"/>
		
		<adsm:lookup property="rodovia" label="rodovia" dataType="text" size="10" maxLength="10" labelWidth="17%" width="40%"
					 idProperty="idRodovia" criteriaProperty="sgRodovia"  
					 service="lms.seguros.manterProcessosSinistroAction.findLookupRodovia"   
					 action="/municipios/manterRodovias" 
					 disabled="true">
			<adsm:propertyMapping relatedProperty="rodovia.dsRodovia" modelProperty="dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="26" disabled="true" serializable="false" />
			<adsm:hidden property="stituacaoRodovia" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="stituacaoRodovia" modelProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:textbox property="nrKmSinistro" label="km" dataType="integer" size="7%" labelWidth="3%" width="40%" maxLength="6" disabled="true"/>
				
		<adsm:lookup property="filialSinistro" label="filialSinistro" dataType="text" size="3" maxLength="3" labelWidth="17%" width="83%"
					 idProperty="idFilial" 
					 required="false" 
					 criteriaProperty="sgFilial"  
					 service="lms.seguros.manterProcessosSinistroAction.findLookupFilial"    
					 action="/municipios/manterFiliais"  
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="true" 
					 disabled="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
			<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
			<adsm:hidden property="flagBuscaEmpresaUsuarioLogado"      value="true" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
		</adsm:lookup>
		
		<adsm:lookup property="aeroporto" label="aeroporto" dataType="text" size="3" maxLength="3" labelWidth="17%" width="83%"
					 idProperty="idAeroporto"
					 service="lms.seguros.manterProcessosSinistroAction.findLookupAeroporto" 
					 action="municipios/manterAeroportos"			 	 	      
			 	 	 criteriaProperty="sgAeroporto" 
			 	 	 required="false"
			 	 	 disabled="true">
 	 	    <adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text" size="30" maxLength="30" disabled="true"/>	 	    	      
			<adsm:hidden property="stituacaoAeroporto" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="stituacaoAeroporto" modelProperty="tpSituacao"/>
			
	 	</adsm:lookup>
		
		<adsm:textarea property="dsLocalSinistro" label="localSinistroOutro" maxLength="1500" columns="119" labelWidth="17%" width="83%" required="false" disabled="true"/>
		
		<adsm:lookup property="municipio" label="municipio" dataType="text" maxLength="60" size="30" labelWidth="17%" width="35%"
					 idProperty="idMunicipio" 
					 criteriaProperty="nmMunicipio"
	             	 action="/municipios/manterMunicipios" 
	             	 service="lms.seguros.manterProcessosSinistroAction.findLookupMunicipio"
                     minLengthForAutoPopUpSearch="3" exactMatch="false" 
                     required="true">
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
                <adsm:hidden property="municipio.tpSituacao" value="A" serializable="false"/>
				<adsm:propertyMapping criteriaProperty="municipio.tpSituacao" modelProperty="tpSituacao"/>
        </adsm:lookup>

		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" label="uf" dataType="text" size="2" labelWidth="3%" width="45%" disabled="true"/>
				
		<adsm:hidden property="idMoedaHidden"        serializable="false"/>
		<adsm:hidden property="sgSimboloMoedaHidden" serializable="false"/>
		<adsm:combobox label="moeda"
					   property="moeda.idMoeda" 
					   labelWidth="17%"
					   boxWidth="83"
					   service="lms.seguros.manterProcessosSinistroAction.findComboMoeda" 
					   required="true" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   onlyActiveValues="true"					   
					   onchange="return onMoedaChange(this);"/>

		<adsm:textbox property="sgSimboloVlMercadoria" label="valorMercadoria" dataType="text" size="8" disabled="true" labelWidth="17%" width="35%">
		<adsm:textbox property="vlMercadoria" dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>		

		<adsm:textbox property="sgSimboloRecuperado" label="valorRecuperado" dataType="text" size="8" disabled="true" labelWidth="15%" width="33%">
		<adsm:textbox property="vlRecuperado" dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="sgSimboloPrejuizo" label="valorPrejuizo" dataType="text" size="8" labelWidth="17%" width="35%" disabled="true">		
		<adsm:textbox property="vlPrejuizo" dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="sgSimboloIndenizado" label="valorIndenizado" dataType="text" size="8" labelWidth="15%" width="33%" disabled="true">
		<adsm:textbox property="vlIndenizado" dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="sgSimboloPrejuizoProprio" label="valorPrejuizoProprio" dataType="text" size="8" labelWidth="17%" width="35%" disabled="true">		
		<adsm:textbox property="vlPrejuizoProprio" dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="sgSimboloDifeIndenizadoReembolsado" label="diferencaIndenizadoReembolso" dataType="text" size="8" disabled="true" labelWidth="15%" width="33%">
		<adsm:textbox property="vlDifeIndenizadoReembolsado" dataType="currency" size="18"  maxLength="16" disabled="true"/>
		</adsm:textbox>		
		
		<adsm:textbox property="sgSimboloReembolso" label="valorReembolso" dataType="text" size="8" disabled="true" labelWidth="17%" width="35%">
		<adsm:textbox property="vlReembolso"  dataType="currency" size="18" maxLength="16" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="sgSimboloDiferencaPagamentoCliente" label="diferencaPagamentoCliente" dataType="text" size="8" disabled="true" labelWidth="15%" width="33%">
		<adsm:textbox property="diferencaPagamentoCliente" dataType="currency" size="18" maxLength="18" disabled="true"/>
		</adsm:textbox>		
		
		<adsm:textbox property="vlFranquia" label="vlFranquia" dataType="currency" mask="###,###,###,###,##0.00" maxLength="18" size="31%" labelWidth="17%" width="35%" required="true"/>
			
		<adsm:textbox property="tempoPagamento" label="tempoPgto" dataType="text" size="5" maxLength="5" labelWidth="15%" width="30%" disabled="true"/>		
		
		<adsm:textbox property="sgSimboloVlReceber" label="valorReceber" dataType="text" size="8" disabled="true" labelWidth="17%" width="35%">
		<adsm:textbox property="vlReceber" dataType="currency" size="18" maxLength="18" disabled="true" minValue="0.00"/>		</adsm:textbox>		

		<adsm:combobox property="situacaoReembolso.idSituacaoReembolso" label="situacaoReembolso" optionLabelProperty="dsSituacaoReembolso" 
			optionProperty="idSituacaoReembolso" 
			service="lms.seguros.manterProcessosSinistroAction.findComboSituacaoReembolso" boxWidth="233" labelWidth="15%" width="33%"/>		
		
		<adsm:textarea property="obSinistro" label="observacoes" maxLength="1500" columns="119" labelWidth="17%" width="83%"/>

		<adsm:textbox property="dsComunicadoCorretora" label="comunicadoCorretora" dataType="text" maxLength="1500" size="50%" labelWidth="17%" width="83%"/>		
		<adsm:buttonBar>
			<adsm:button id="reportButton" caption="cartaOcorrencia" disabled="true" onclick="cartaOcorrenciaClick();" buttonType="findButton">
				<adsm:linkProperty src="idProcessoSinistro" target="processoSinistro.idProcessoSinistro"/>			
				<adsm:linkProperty src="nrProcessoSinistro" target="processoSinistro.nrProcessoSinistro"/>
			</adsm:button>
			 
			<adsm:button id="recibosButton" caption="recibosReembolso" disabled="true" buttonType="storeButton" onclick="onRecibosButtonClick();"/>
			
			<adsm:button id="fechamentoButton" caption="fechamento" disabled="true" buttonType="storeButton" onclick="onFechamentoButtonClick();"/>			
			
			<adsm:button id="reabrirButton" caption="reabrir" disabled="true" buttonType="storeButton" onclick="onReabrirButtonClick();"/>
			
			<adsm:button id="storeButton" caption="salvar" buttonType="store" onclick="validatePCE();"/>
			<adsm:newButton id="newButton"/>
			<adsm:button id="__buttonBar:1.removeButton" caption="excluir" onclick="removeById()"/> 
		</adsm:buttonBar>
			<script>
				function lms_00019() {
					alert("<adsm:label key='LMS-00019'/>");
				}
			</script>
	</adsm:form>
</adsm:window>

<script>

	// setando required nos campos do numero do processo de sinistro
	document.getElementById("nrProcessoSinistro").required = "true";	

	// setando masterLink true na moeda
	document.getElementById("idMoedaHidden").masterLink = "true";
	document.getElementById("sgSimboloMoedaHidden").masterLink = "true";
	
	function onRecibosButtonClick() {
		showModalDialog('seguros/manterRecibosReembolso.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	}
	
	function onFechamentoButtonClick() {
		showModalDialog('seguros/manterProcessosSinistro.do?cmd=fechamento',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:210px;');
	}

	function onReabrirButtonClick() {
		if (window.confirm(i18NLabel.getLabel("LMS-22031"))) {
			var data = new Array();
			data.idProcessoSinistro = getElementValue('idProcessoSinistro');
			var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.reabrirProcessoSinistroFechado", "onReabrirButtonClick", data);
	    	xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function onReabrirButtonClick_cb(data, error) {
		if (error == undefined) {
			findAtualizaValoresDetalhamento();
		}
	}
	
	function findAtualizaValoresDetalhamento() {
		var data = new Array();
		data.idProcessoSinistro = getElementValue('idProcessoSinistro');
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findAtualizaValoresDetalhamento", "findAtualizaValoresDetalhamento", data);
    	xmit({serviceDataObjects:[sdo]});	
	}
	
	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	var codigos;
	
	/**
	 * Este get existe decorrente de uma necessidade da popUp de alert.
	 */
	function getCodigos() {
		return codigos;
	}
	
	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE() {	
				
		if ((validateTabScript(document.getElementById("processoSinistroForm")))==false) return false;
	
		var data = new Object();
		data.idProcessoSinistro = getElementValue("idProcessoSinistro");
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Callback da validacao. 
	 *
	 * @param data
	 * @param error
	 */ 
	function validatePCE_cb(data, error) {
	
		var list = data.list;
		if (list!=undefined) {
		
			codigos = new Array();
		
			//Carregando list que sera carregada pela de popUp...
			for (var i=0; i<list.length; i++) {
				if (list[i].codigo!=undefined) {
					codigos[i] = list[i].codigo;
				}
			} 
			
			// Janela de chamada para a tela de pce
			// Apos sua chamada cai na funcao de callBack - alertPCE
			if (codigos.length>0) {
				showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
			}
		}
		
		storeButtonScript('lms.seguros.manterProcessosSinistroAction.storeProcessoSinistro', 'storeCallback', document.getElementById("processoSinistroForm"));
		
	}
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################s
	
	function findAtualizaValoresDetalhamento_cb(data, error) {
		if (error!=undefined)	
			alert(error);
			
		else {
			setElementValue("dhFechamento", data.dhFechamento);
			setElementValue("situacao", data.situacao);
			setElementValue("situacaoHidden", data.situacaoHidden);
			setElementValue("tempoPagamento", data.tempoPagamento);
			setElementValue("sgSimboloVlMercadoria", data.sgSimboloVlMercadoria);
			setElementValue("sgSimboloRecuperado", data.sgSimboloRecuperado);
			setElementValue("sgSimboloPrejuizo", data.sgSimboloPrejuizo);
			setElementValue("sgSimboloPrejuizoProprio", data.sgSimboloPrejuizoProprio);
			setElementValue("sgSimboloIndenizado", data.sgSimboloIndenizado);
			setElementValue("sgSimboloReembolso", data.sgSimboloReembolso);
			setElementValue("sgSimboloDifeIndenizadoReembolsado", data.sgSimboloDifeIndenizadoReembolsado);
			setElementValue("sgSimboloDiferencaPagamentoCliente", data.sgSimboloDiferencaPagamentoCliente);
			setElementValue("sgSimboloVlReceber", data.sgSimboloVlReceber);

			setElementValue("vlReembolso",                 setFormat(document.getElementById("vlReembolso"),                 data.vlReembolso));
			setElementValue("vlPrejuizo",                  setFormat(document.getElementById("vlPrejuizo"),                  data.vlPrejuizo));
			setElementValue("vlPrejuizoProprio",           setFormat(document.getElementById("vlPrejuizoProprio"),           data.vlPrejuizoProprio));
			setElementValue("vlRecuperado",                setFormat(document.getElementById("vlRecuperado"),                data.vlRecuperado));
			setElementValue("vlIndenizado",                setFormat(document.getElementById("vlIndenizado"),                data.vlIndenizado));
			setElementValue("vlDifeIndenizadoReembolsado", setFormat(document.getElementById("vlDifeIndenizadoReembolsado"), data.vlDifeIndenizadoReembolsado));
			setElementValue("vlReceber",                   setFormat(document.getElementById("vlReceber"),                   data.vlReceber));
			
			habilitaReabrirSalvar();
		}	
	}
	

	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error == undefined) {
			setDisabled('moeda.idMoeda', true);	
			tabSetDisabled("custosAdicionais", false);
			tabSetDisabled("fotos", false);						
		}
	}

	var idTipoSinistro = null;
	function onDataLoadCallback_cb(data, error) {
		onDataLoad_cb(data, error);
		
		if (error == undefined) {
			idTipoSinistro = data.tipoSinistro.idTipoSinistro;
			
			// desabilita moeda
			setDisabled('moeda.idMoeda', true);
			
			habilitaReabrirSalvar()
			
			//popula combo de tipo de sinistro e preencher as informações de corretora e seguradora
			findTipoSinistro();
			
			// onchange da combo de local sinistro
			onLocalSinistroChange(data.localSinistro.value);
		}
	}
	
	// Função para verificar qual o botão deve ser habilitado, Reabrir ou Salvar 
	function habilitaReabrirSalvar() {
		if (document.getElementById('dhFechamento').value != undefined && document.getElementById('dhFechamento').value != "") {
			setDisabled('reabrirButton', false);
			setDisabled('storeButton', true);
		}
		else {
			setDisabled('reabrirButton', true);
			setDisabled('storeButton', false);
		}
	}
	
	function onReportButtonClick() {
		reportButtonScript('lms.seguros.emitirCartaOcorrenciaAction', 'openPdf', document.forms[0]);
	}

	function onLocalSinistroChange(option) {

		if (option == "") {
			setDisabledAeroporto(true);
			setDisabledRodovia(true);
			setDisabledFilial(true);			
			setDisabledLocalSinistro(true); 
			
		} else if (option == "A") {
			setDisabledAeroporto(false);
			setDisabledRodovia(true);
			setDisabledFilial(true);			
			setDisabledLocalSinistro(true); 			
		
		} else if (option == "F") {
			setDisabledAeroporto(true);
			setDisabledRodovia(true);
			setDisabledFilial(false);			
			setDisabledLocalSinistro(true); 			
		
		} else if (option == "R") {
			setDisabledAeroporto(true);
			setDisabledRodovia(false);
			setDisabledFilial(true);			
			setDisabledLocalSinistro(true); 

		} else if (option == "O") {
			setDisabledAeroporto(true);
			setDisabledRodovia(true);
			setDisabledFilial(true);		
			setDisabledLocalSinistro(false);

		}
		
	}
	
	// funcoes para habilitar / desabilitar campos 
	// de acordo com o tipo de local de sinistro
	function setDisabledAeroporto(disable) {
		setDisabled("aeroporto.idAeroporto", disable);
		if (disable)
			resetValue("aeroporto.idAeroporto");
	}
	
	// funcoes para habilitar / desabilitar campos 
	// de acordo com o tipo de local de sinistro
	function setDisabledRodovia(disable) {
		setDisabled("rodovia.idRodovia", disable);
		setDisabled("nrKmSinistro", disable);
		if (disable) {
			resetValue("rodovia.idRodovia");
			resetValue("nrKmSinistro");			
		}
	}
	
	// funcoes para habilitar / desabilitar campos 
	// de acordo com o tipo de local de sinistro
	function setDisabledFilial(disable) {
		setDisabled("filialSinistro.idFilial", disable);
		if (disable)		
			resetValue("filialSinistro.idFilial");
	}
	
	// funcoes para habilitar / desabilitar campos 
	// de acordo com o tipo de local de sinistro
	function setDisabledLocalSinistro(disable) {
		setDisabled("dsLocalSinistro", disable);
		if (disable)		
			resetValue("dsLocalSinistro");
	}

	function pageLoad_cb() {
		onPageLoad_cb();
		getMoedaSessao();
		setDisabled("reportButton", true);
	}
	
	function initWindow(evento) {
		var localSinistro = document.getElementById('localSinistro');
		
		if (evento.name == "newButton_click" || evento.name=="tab_click" ) {
			
			if (evento.name == "newButton_click" || (evento.name=="tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id=="pesq")) { 
				newMaster();
				tabSetDisabled("fotos", true);			
				tabSetDisabled("custosAdicionais", true);
				tabSetDisabled("documentos", false);
				setDisabled('moeda.idMoeda', false);
				setDisabled('reportButton', true);
				setDisabled('recibosButton', true);	
				setDisabled('fechamentoButton', true);	
				setDisabledAeroporto(true);
				setDisabledRodovia(true);
				setDisabledFilial(true);			
				setDisabledLocalSinistro(true);
				document.getElementById("tipoSinistro.idTipoSinistro").length = 1;
				habilitaReabrirSalvar();
			}

			if (evento.name=="tab_click") {

				if (getElementValue('idProcessoSinistro')!='') {
					findAtualizaValoresDetalhamento();
	
				} else if (getElementValue('idProcessoSinistro') == '' ) {
					
					setDisabled('reportButton', true);
					setDisabled('recibosButton', true);			
					setDisabled('fechamentoButton', true);
					setDisabled('reabrirButton', true);
				}
			}
			
		} else if(evento.name == "gridRow_click") {
			
			setDisabled('reportButton', false);
			setDisabled('recibosButton', false);			
			setDisabled('fechamentoButton', false);				
			tabSetDisabled("fotos", false);	
			tabSetDisabled("documentos", false);
			tabSetDisabled("custosAdicionais", false);			
		}
	}

	// função para habilitar/ desablitar tab	
	function tabSetDisabled(tab, disable) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("documentos", false);
 		tabGroup.setDisabledTab(tab, disable);	
	}
	
	// obtém a moeda da sessão e seta nos campos de valor
	function newMaster() {
		var service = "lms.seguros.manterProcessosSinistroAction.newMaster";
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, 'newMaster', data)); 
		xmit(remoteCall); 		
	}
	
	function newMaster_cb(data, error) {
		setaMoedaPadrao();
	}	
	
	// obtém a moeda da sessão e seta nos campos hidden
	function getMoedaSessao() {
		var service = "lms.seguros.manterProcessosSinistroAction.getMoedaSessao";
		var callback = "getMoedaSessao";
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, callback, data)); 
		xmit(remoteCall); 		
	}
	
	// retorno da funcao acima
	function getMoedaSessao_cb(data, error) {
		if (error==undefined) {
			setElementValue("idMoedaHidden", data.idMoeda);
			setElementValue("sgSimboloMoedaHidden", data.sgSimboloMoeda);
		}
	}
	
	// seta a moeda padrao nos campos de moeda
	function setaMoedaPadrao() {		

		// seta campos com sgSimbolo moeda padrao
		var sgSimboloMoeda = getElementValue('sgSimboloMoedaHidden');
		setaSgSimboloMoeda(sgSimboloMoeda)
		
		// seta a combo de moeda com a moeda padrao
		var idMoeda = getElementValue('idMoedaHidden');
		setElementValue("moeda.idMoeda", idMoeda);
	}
	
	// seta valor nos campos de sigla de moeda
	function setaSgSimboloMoeda(sgSimboloMoeda) {
		setElementValue("sgSimboloVlMercadoria",   sgSimboloMoeda);		
		setElementValue("sgSimboloRecuperado",   sgSimboloMoeda);
		setElementValue("sgSimboloPrejuizo", sgSimboloMoeda);
		setElementValue("sgSimboloPrejuizoProprio", sgSimboloMoeda);
		setElementValue("sgSimboloIndenizado", sgSimboloMoeda);
		setElementValue("sgSimboloReembolso",  sgSimboloMoeda);			
		setElementValue("sgSimboloDifeIndenizadoReembolsado", sgSimboloMoeda);	
		setElementValue("sgSimboloDiferencaPagamentoCliente", sgSimboloMoeda);
		setElementValue("sgSimboloVlReceber",   sgSimboloMoeda);					
	}
	
	
	// onchange da moeda
	function onMoedaChange(e) {
		var sgSimbolo;

		if (e.options[e.selectedIndex].value=='') {
			sgSimbolo = '';
			
		} else {
			sgSimbolo = e.options[e.selectedIndex].text;
			
		}
		
		// seta moeda nos campos de sigla de moeda
		setaSgSimboloMoeda(sgSimbolo);		
	}
	
	function changeValidateDH() {
		var service = "lms.seguros.manterProcessosSinistroAction.isValidatedDHSinistro";
		var callback = "changeValidateDH";
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, callback, buildFormBeanFromForm(this.document.forms[0]))); 
		xmit(remoteCall); 			
	}

	function changeValidateDH_cb(data, error){		
		if(data.isValidate == 'true'){
			lms_00019();
			setElementValue("dhSinistro", "");
		}		
	}

	function cartaOcorrenciaClick() {
		if (getIdProcessoWorkflow() != undefined || getIsReciboIndenizacao() != undefined) {
			showModalDialog('seguros/emitirCartaOcorrencia.do?cmd=main'+buildLinkPropertiesQueryString_disposicao(),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		} else {
			parent.parent.redirectPage('seguros/emitirCartaOcorrencia.do?cmd=main'+buildLinkPropertiesQueryString_disposicao() );
		}
	}
	
	function buildLinkPropertiesQueryString_disposicao() {
		var qs = "";
		qs += "&processoSinistro.idProcessoSinistro=" + document.getElementById("idProcessoSinistro").value;
		qs += "&processoSinistro.nrProcessoSinistro=" + document.getElementById("nrProcessoSinistro").value;
		
		return qs;
	}
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}
	
	function getIsReciboIndenizacao() {
		var url = new URL(parent.location.href);
		return url.parameters["isReciboIndenizacao"];
	}	
	
	function onDataLoadLookupVeiculo_cb(data) {
		var dados = new Object();
		if(data[0] != undefined && data[0] != null) {
			dados.idMeioTransporte = getNestedBeanPropertyValue(data[0], "idMeioTransporte");
			onPopupSetVeiculo(dados);
		}
	}
	
	function onPopupSetVeiculo(data) {
		var dados = new Object();
		dados.idMeioTransporte = getNestedBeanPropertyValue(data, "idMeioTransporte");
		
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findDataLookupByIdMeioTransporte", "onPopupSetVeiculo", dados);
    	xmit({serviceDataObjects:[sdo]});		
	}
	
	function onPopupSetVeiculo_cb(data, error) {
		if(error == null) {
			setElementValue("nrCertificado", data.nrCertificado);
			setElementValue("tpVeiculo", data.tipoVeiculo);
			setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota", data.nrFrota);
			setElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador", data.nrIdentificacao);
			setElementValue("meioTransporteRodoviario.idMeioTransporte", data.idMeioTransporte);
		}	
	}
	
	function meioTransporteRodoviario_Onchange(element) {
		var data = new Object();
		
		if(element.id == "meioTransporteRodoviario2.meioTransporte.nrFrota") {
			resetValue("meioTransporteRodoviario.meioTransporte.nrIdentificador");
			data.meioTransporte = { nrFrota: element.value, nrIdentificador: "" };
		} else { // meioTransporteRodoviario.meioTransporte.nrIdentificador
			resetValue("meioTransporteRodoviario2.meioTransporte.nrFrota");
			data.meioTransporte = { nrFrota: "", nrIdentificador: element.value };
		}
		
		if(getElementValue(element.id) == "") {
			setElementValue("nrCertificado", "");
			setElementValue("tpVeiculo", "");
		} else {
			var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findLookupMeioTransporte", "onDataLoadLookupVeiculo", data);
	    	xmit({serviceDataObjects:[sdo]});
    	}
    	
    	return;
	}
		
	function removeById(){
		var data = new Object();
		var idProcessoSinistro = getElementValue('idProcessoSinistro');
		var vlPrejuizo = getElementValue('vlPrejuizo');
		
		// LMS-6172 - Se o registro não for novo E valor do prejuizo = 0, então exibir a msg LMS-22032
		if(idProcessoSinistro != null && idProcessoSinistro != undefined  && vlPrejuizo != null && vlPrejuizo == 0){
		
			data.idProcessoSinistro = idProcessoSinistro;
			data.vlPrejuizo = vlPrejuizo;
		
			if (window.confirm(i18NLabel.getLabel("LMS-22032"))) {
			  	var remoteCall = {serviceDataObjects:new Array()}; 
				remoteCall.serviceDataObjects.push(createServiceDataObject("lms.seguros.manterProcessosSinistroAction.removeById", "retornoRemoveProcessoSinistro", data)); 
			  	xmit(remoteCall); 		
			}
		
		} else{
			alert(i18NLabel.getLabel("LMS-22033"));
		}
		
	}
	
	function retornoRemoveProcessoSinistro_cb(data, error, eventObj) {
		if (error != undefined) {
			alert(error);
		}
		
		resetValue(this.document);
		atualizaTela(eventObj);
		showSuccessMessage();
		
	}
	
	function atualizaTela(eventObj){
		if (eventObj == undefined) {
			eventObj = {name:"newButton_click"};
		}

		var tab = getTab(document, false);
		if (tab) {
			eventObj.src = tab.tabGroup.selectedTab;
		}
   		initWindowScript(document.parentWindow, eventObj);
	}
	
	// LMS-6178
	/* Função que preenche a combo de tipo de sinistro conforme é alterado a combos tipo de seguros */
	function findTipoSinistro() {
		var data = new Array();
		
		if (getElementValue("tipoSeguro.idTipoSeguro") != "") {
			/** Insere a informação de tipo de seguro */
			setNestedBeanPropertyValue(data, "idTipoSeguro", getElementValue("tipoSeguro.idTipoSeguro"));
			
			if(getElementValue("idProcessoSinistro") == "") {
				setNestedBeanPropertyValue(data, "tpSituacao", "A");
			}
			
			/** Invoca o método da service, especifica a função JS que será executada no callBack e passa os dados para serem usados como filtro */
			var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findComboTipoProcessoSinistroByTipoSeguro", "tiposSinistro", data);
			xmit({serviceDataObjects:[sdo]});
		}
		else {
			/** Retira todos options da Select deixando apenas o primeiro */
			document.getElementById("tipoSinistro.idTipoSinistro").length = 1;
		}
	}
	
	// LMS-6178
	/** Função que popula a combo tipo de sinistro no callBack */
	function tiposSinistro_cb(data, error, erromsg){
		if(error == null){
			/** Função gerada pelo framework para popular o combo */
			tipoSinistro_idTipoSinistro_cb(data);
		}
		if (idTipoSinistro != null) {
			setElementValue("tipoSinistro.idTipoSinistro", idTipoSinistro);
			idTipoSinistro = null;
			findCorretoraAndSeguradora();
		}
	}
	
	// LMS-6178
	/** Função que popula os campos Corretora e Seguradora */
	function findCorretoraAndSeguradora() {
		if(getElementValue("dhSinistro") != "" && getElementValue("tipoSeguro.idTipoSeguro") != "") {
			var data = new Array();
			
			setNestedBeanPropertyValue(data, "idTipoSeguro", getElementValue("tipoSeguro.idTipoSeguro"));
			setNestedBeanPropertyValue(data, "dhSinistro", getElementValue("dhSinistro"));
			
			var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findCorretoraAndSeguradora", "corretoraAndSeguradora", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	// LMS-6178
	/** Função que popula os campos Corretora e Seguradora no callBack */
	function corretoraAndSeguradora_cb(data, error, erromsg){
		if(error == null){	
			var corretora = getNestedBeanPropertyValue(data, "corretora");
 			var seguradora = getNestedBeanPropertyValue(data, "seguradora");	
 			
			setElementValue("corretora", corretora);
			setElementValue("seguradora", seguradora);
		}
	}
	
	function removeById(){
		var data = new Object();
		var idProcessoSinistro = getElementValue('idProcessoSinistro');
		var vlPrejuizo = getElementValue('vlPrejuizo');
		
		// LMS-6172 - Se o registro não for novo E valor do prejuizo = 0, então exibir a msg LMS-22032
		if(idProcessoSinistro != null && idProcessoSinistro != undefined  && vlPrejuizo != null && vlPrejuizo == 0){
		
			data.idProcessoSinistro = idProcessoSinistro;
			data.vlPrejuizo = vlPrejuizo;
		
			if (window.confirm(i18NLabel.getLabel("LMS-22032"))) {
			  	var remoteCall = {serviceDataObjects:new Array()}; 
				remoteCall.serviceDataObjects.push(createServiceDataObject("lms.seguros.manterProcessosSinistroAction.removeById", "retornoRemoveProcessoSinistro", data)); 
			  	xmit(remoteCall); 		
			}
		
		} else{
			alert(i18NLabel.getLabel("LMS-22033"));
		}
		
	}
	
	function retornoRemoveProcessoSinistro_cb(data, error, eventObj) {
		if (error != undefined) {
			alert(error);
		}
		
		resetValue(this.document);
		atualizaTela(eventObj);
		showSuccessMessage();
		
	}
	
	function atualizaTela(eventObj){
		if (eventObj == undefined) {
			eventObj = {name:"newButton_click"};
		}

		var tab = getTab(document, false);
		if (tab) {
			eventObj.src = tab.tabGroup.selectedTab;
		}
   		initWindowScript(document.parentWindow, eventObj);
	}
	
</script>