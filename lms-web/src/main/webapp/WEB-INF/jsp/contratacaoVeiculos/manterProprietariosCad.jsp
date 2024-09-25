<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script src="../lib/pis.js" type="text/javascript"></script>
<script language="javascript">
<!--
var sessionData;

function pageLoad() {
   onPageLoad();

 	initPessoaWidget({
 		tpTipoElement:document.getElementById("pessoa.tpPessoa"),
 		tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
 		numberElement:document.getElementById("pessoa.idPessoa")
 	});
}
//-->
</script>
<adsm:window title="manterProprietarios" service="lms.contratacaoveiculos.manterProprietariosAction" onPageLoadCallBack="myOnPageLoad" onPageLoad="pageLoad">

	<adsm:i18nLabels>
		<adsm:include key="proprietario"/>
		<adsm:include key="bloquear"/>
		<adsm:include key="bloqueado"/>
		<adsm:include key="desbloquear"/>
		<adsm:include key="liberado"/>
		<adsm:include key="LMS-00049"/>
	</adsm:i18nLabels>

	<adsm:form action="/contratacaoVeiculos/manterProprietarios" idProperty="idProprietario" service="lms.contratacaoveiculos.manterProprietariosAction.findByIdDetalhamento" onDataLoadCallBack="dataLoad">

		<adsm:hidden property="tpEmpresaMercurio" value="M" serializable="false"/>
		
		<adsm:hidden property="blMei" />
		<adsm:hidden property="blNaoAtualizaDbi" />
		<adsm:hidden property="blRotaFixa" />
		<adsm:hidden property="blCooperado" />
		<adsm:hidden property="tpOperacao" />
		<adsm:hidden property="nrIdentificacaoMei" />
		<adsm:hidden property="dtNascimento" />
		<adsm:hidden property="dtVigenciaInicial" />
		<adsm:hidden property="dtVigenciaFinal" />
		<adsm:hidden property="nmMei"/>

		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3" 
					 service="lms.contratacaoveiculos.manterProprietariosAction.findLookupFilial" dataType="text" label="filial" size="3" 
					 action="/municipios/manterFiliais" labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="tpEmpresaMercurio" modelProperty="empresa.tpEmpresa" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" disabled="true" />
			<adsm:hidden property="filial.siglaNomeFilial"/>
		</adsm:lookup>

		<%-- embora este campo não seja obrigatório no banco, ele deve ser obrigatório na tela --%>
		<adsm:combobox definition="TIPO_PESSOA.cad" label="tipoPessoa" required="true" labelWidth="17%" width="33%"
				onchange="return tpPessoaChange(this);" />

	    <adsm:complement label="identificacao" labelWidth="17%" width="33%">		
		            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" onchange="return tpIdentificacaoChange(this);" />		
		            <adsm:lookup definition="IDENTIFICACAO_PESSOA" service="lms.contratacaoveiculos.manterProprietariosAction.validateIdentificacao" onDataLoadCallBack="validaIdPessoa"/>
		</adsm:complement>	
  
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" maxLength="50" required="true" size="59" labelWidth="17%" width="83%" depends="pessoa.nrIdentificacao"/>
		<adsm:combobox property="tpProprietario" label="tipoProprietario" labelWidth="17%" domain="DM_TIPO_PROPRIETARIO" width="33%" required="true" />

		<adsm:textbox dataType="integer" property="nrAntt"
				label="antt" maxLength="14" size="20" labelWidth="17%" width="33%" >
			<span id="nrAntt_req"><font size="2" color="red">*</font></span>
		</adsm:textbox>
		
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" maxLength="50" size="32" labelWidth="17%" width="33%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS_PESSOA" labelWidth="17%" width="33%" required="true"/>

		<adsm:textbox dataType="JTDate" property="dtAtualizacao" disabled="true" picker="false" label="dataAtualizacao" size="20" maxLength="20"  labelWidth="17%" width="33%"/>

		<adsm:section caption="telefonePrincipal"/>

		<adsm:combobox property="telefoneEndereco.tpTelefone" label="tipoTelefone" domain="DM_TIPO_TELEFONE" required="true" labelWidth="17%" width="33%"/>
		<adsm:combobox property="telefoneEndereco.tpUso" label="usoTelefone" domain="DM_USO_TELEFONE" required="true" labelWidth="17%" width="33%"/>
        <adsm:textbox property="telefoneEndereco.nrDdi" label="ddi" dataType="integer" maxLength="5" size="5" labelWidth="17%" width="33%"/>
		<adsm:complement label="numero" width="35%" required="true" labelWidth="17%">
            <adsm:textbox dataType="integer" property="telefoneEndereco.nrDdd" maxLength="5" size="5" />
        	<adsm:textbox dataType="integer" property="telefoneEndereco.nrTelefone" maxLength="10" size="10"/>
        </adsm:complement>
        <adsm:hidden property="telefoneEndereco.idTelefoneEndereco" serializable="true"/>

		<adsm:section caption="informacoesDocumentos"/>

		<adsm:textbox dataType="integer" property="nrPis"
				maxLength="11" label="numeroPis" size="20"
				labelWidth="17%" width="33%"
				onchange="return isPis(this);" >
			<span id="nrPis_req"><font size="2" color="red">*</font></span>
		</adsm:textbox>
		<adsm:textbox dataType="integer" property="nrDependentes" label="numeroDependentes" maxLength="2" size="10" labelWidth="17%" width="33%"/>	

		<adsm:section caption="informacoesDoPagamento"/>

		<adsm:combobox property="tpPeriodoPagto" label="periodoPagamento" domain="DM_PERIODO_PAGAMENTO_PROPRIETARIO" labelWidth="17%" width="33%" cellStyle="vertical-align:bottom;"/>
		<adsm:combobox property="diaSemana" optionProperty="value" optionLabelProperty="description" label="diaPagamentoSemanal" service="lms.contratacaoveiculos.manterProprietariosAction.findDiasUteisPagamentoSemanal"  labelWidth="17%" width="33%" cellStyle="vertical-align:bottom;"/>

		<adsm:section caption="informacoesBloqueioLiberacao"/>
		<adsm:textbox label="situacao" property="situacaoBloqueioLiberacao" dataType="text" disabled="true" labelWidth="17%" width="33%"/>

		<adsm:hidden property="labelPessoa"/>

		<adsm:buttonBar lines="2" >
			<adsm:button caption="enderecos" id="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" boxWidth="70">
				<adsm:linkProperty src="idProprietario" target="pessoa.idPessoa" disabled="true" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />				
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>

			<adsm:button caption="telefones" id="telefones" action="configuracoes/manterTelefonesPessoa" cmd="main" boxWidth="59">
				<adsm:linkProperty src="idProprietario" target="pessoa.idPessoa" disabled="true" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />				
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>

			<adsm:button caption="contatos" id="telefones" action="configuracoes/manterContatos" cmd="main" boxWidth="60">
				<adsm:linkProperty src="idProprietario" target="pessoa.idPessoa" disabled="true" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />				
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>

			<adsm:button caption="dadosBancarios" id="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa" cmd="main" boxWidth="105">
				<adsm:linkProperty src="idProprietario" target="pessoa.idPessoa" disabled="true" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />				
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>				
			</adsm:button>

			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main" boxWidth="122">
				<adsm:linkProperty src="idProprietario" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true" />				
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>			

			<adsm:button caption="consultarBloqueioLiberacao" id="consultarBloqueioLiberacao" action="/contratacaoVeiculos/manterBloqueiosMotoristaProprietario" cmd="main" boxWidth="188"> 
				<adsm:linkProperty src="idProprietario" target="proprietario.idProprietario" disabled="true" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="proprietario.pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="proprietario.pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="proprietario.pessoa.nmPessoa" disabled="true" />				
			</adsm:button>

			<adsm:button caption="meiosTransporte" id="meiosTransporte" action="/contratacaoVeiculos/manterMeiosTransporteProprietarios" cmd="main" boxWidth="120" >
				<adsm:linkProperty src="idProprietario" target="proprietario.idProprietario" disabled="true" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="proprietario.pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="proprietario.pessoa.nmPessoa" disabled="true" />				
			</adsm:button>

			<adsm:button caption="bloquear" id="bloquear" onclick="onBloquearButtonClick();" disabled="true" boxWidth="75"/>

			<adsm:storeButton callbackProperty="storeCallback" />
			<adsm:newButton />
			<adsm:removeButton service="lms.contratacaoveiculos.manterProprietariosAction.removeProprietarioById" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	// ao clicar em limpar, não limpa o elemento
    document.getElementById("labelPessoa").masterLink = "true";
    
    function storeCallback_cb(data, exception, key) {
    	var msg;
    	if (key != undefined && (key == 'LMS-26030' || key == 'LMS-26017')){	
    			msg = exception;
	    		exception = undefined;
    	} else {
    		store_cb(data, exception);
    	}
    	
		verificaTipoPessoa();
		verificaSituacao(data);

		if (key != undefined && (key == 'LMS-26030' || key == 'LMS-26017')){
			alert(msg);
			if (key == 'LMS-26017'){
				setFocus("nrAntt");
			}
		} else if (key == undefined) {
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("pessoa.tpPessoa", true);
		}
    }
    

	// ##############################################################
	// verifica se o proprietario ou nenhum de seus beneficiarios,
	// possuem conta bancaria. caso nenhum deles possua, alerta com 
	// mensagem de aviso ao usuario.
	// ##############################################################
	function verificaSituacao(data){
		var flag = getNestedBeanPropertyValue(data, "flag");
		if (flag != undefined && flag != "")
			alert(flag);
	}
	
	//se a pessoa for juridica habilita o botao inscricao estadual, senao desabilita
	function verificaTipoPessoa(){	
		if(document.getElementById("pessoa.tpPessoa").value == "J")
			setDisabled("buttonInscricaoEstadual",false);
		else
			setDisabled("buttonInscricaoEstadual", true);
	}

	// ############################################################
	// pageLoadCallBack - setando o nome "proprietario" a ser 
	// passado para a o sub-evento manterDadosBancarios
	// ############################################################
    function myOnPageLoad_cb(data, erro, k) {
		onPageLoad_cb(data,erro);
	    setElementValue("labelPessoa", getI18nMessage("proprietario"));
	    document.getElementById("pessoa.nrIdentificacao").serializable = true;

	    getFilialUsuario();
    }

	// ############################################################
	// callback do form
	// ############################################################
	function dataLoad_cb(data, exception) {

		onDataLoad_cb(data, exception); 

		// muda dinamicamente o valor do botao bloquear, conforme 
		// a existencia ou nao de bloqueios para o proprietario
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
					numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		if (data.valorBotaoBloqueio == "desbloquear") {
			document.getElementById("bloquear").value = getI18nMessage("desbloquear");
			setElementValue('situacaoBloqueioLiberacao', getI18nMessage("bloqueado"));
		}
		else {
			document.getElementById("bloquear").value = getI18nMessage("bloquear");
			setElementValue('situacaoBloqueioLiberacao', getI18nMessage("liberado"));
		}
		
		verificaTipoPessoa();
		setDisabled("pessoa.tpIdentificacao", true);
		setDisabled("pessoa.nrIdentificacao", true);
		setDisabled("pessoa.tpPessoa", true);
		setDisabled("tpProprietario", true);
		setFocusOnFirstFocusableField();
		setDisabled("tpProprietario", false);
	}
	
	
	// ############################################################
	// trata o clique no botão bloquear
	// ############################################################
	function onBloquearButtonClick() {
		showModalDialog('contratacaoVeiculos/popupBloqueios.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:250px;');
		verificaBloqueiosVigentes();
	}
	
	// ############################################################
	// verifica se há bloqueios vigentes para o proprietario
	// ############################################################
	function verificaBloqueiosVigentes() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idProprietario", getElementValue("idProprietario"));
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterProprietariosAction.validateBloqueiosVigentes","verificaBloqueiosVigentes",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function verificaBloqueiosVigentes_cb (data, exception) {
		if (exception != undefined) {
			alert(exception);
			return false;

		} else if (data.valorBotaoBloqueio != undefined) {
			if (data.valorBotaoBloqueio == "desbloquear") {
				document.getElementById("bloquear").value = getI18nMessage("desbloquear");
				setElementValue('situacaoBloqueioLiberacao', getI18nMessage("bloqueado"));
			}
			else {
				document.getElementById("bloquear").value = getI18nMessage("bloquear");
				setElementValue('situacaoBloqueioLiberacao', getI18nMessage("liberado"));
			}
			return true;
		}
	}
   
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {	
		if (eventObj.name != "gridRow_click" && eventObj.name != 'storeButton'){
		   setaValoresFilial();
		   setDisabled("pessoa.tpPessoa", false);
           setDisabled("pessoa.tpIdentificacao", true);
		   setDisabled("pessoa.nrIdentificacao", true);
		   setDisabled("tpProprietario", true);
		   setFocusOnFirstFocusableField();
		   setDisabled("tpProprietario", false);

		   manipulaNrPis(undefined);
		   manipulaNrAntt(undefined)
		}
    }

	// ############################################################ ok
	// seta valores da filial
	// ############################################################
	function setaValoresFilial() {
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(sessionData, "idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(sessionData, "sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(sessionData, "pessoa.nmFantasia"));
	}

	// ############################################################ ok
	// busca valores da filial do usuario logado
	// ############################################################
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterProprietariosAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}

	// ############################################################
	// callback de getFilialUsuario()
	// ############################################################
	function getFilialCallBack_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return false;
		}
		if(data != undefined) {
			sessionData = data;
			setaValoresFilial();
		}
	}

	function validaIdPessoa_cb(data,exception) {
		pessoa_nrIdentificacao_exactMatch_cb(data);

		if (exception != undefined) {
			alert(exception);			
			resetValue(document.getElementById("pessoa.nrIdentificacao"));
			setFocus(document.getElementById("pessoa.nrIdentificacao"));
		} else {
			setElementValue("pessoa.nmPessoa",getNestedBeanPropertyValue(data,"0.nmPessoa"));	
			//Pessoa não cadastrada na especialização
			if (data != undefined &&
					data[0] != undefined &&
					data[0].idPessoa != undefined){
				setElementValue("telefoneEndereco.idTelefoneEndereco",data[0].idTelefoneEndereco);
				setElementValue("telefoneEndereco.tpTelefone",data[0].tpTelefone);
				setElementValue("telefoneEndereco.tpUso",data[0].tpUso);
				setElementValue("telefoneEndereco.nrDdi",data[0].nrDdi);
				setElementValue("telefoneEndereco.nrDdd",data[0].nrDdd);
				setElementValue("telefoneEndereco.nrTelefone",data[0].nrTelefone);
			} 
		}
	}

	function tpProprietarioDataLoad_cb(data){
		var newData = new Array();
		var count = 0;
		for(var i=0; i < data.length; i++){
			if (getNestedBeanPropertyValue(data[i], "value") != "P"){
				newData[count++] = data[i];
			}
		}
		tpProprietario_cb(newData); 
		
	}
	
	function manipulaNrPis(tpPessoaValue) {
		document.getElementById("nrPis_req").style.visibility = tpPessoaValue == 'F' ? "visible" : "hidden";
		document.getElementById("nrPis").required = tpPessoaValue == 'F' ? "true" : "false";
	}
	
	function tpPessoaChange(elem) {
		manipulaNrPis(elem.value);	
		return changeTypePessoaWidget(
				{tpTipoElement:elem, tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'cad'});
	}
	
	function manipulaNrAntt(tpIdentValue) {
		var blValido = (tpIdentValue == "CPF" || tpIdentValue == "CNPJ");
		document.getElementById("nrAntt_req").style.visibility = blValido ? "visible" : "hidden";
		document.getElementById("nrAntt").required = blValido ? "true" : "false";
	}
	
	function tpIdentificacaoChange(elem) {
		manipulaNrAntt(elem.value);
		return changeIdentificationTypePessoaWidget(
				{tpIdentificacaoElement:elem, numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'cad'});
	}
	
</script>