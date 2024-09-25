<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
/**
 * Carrrega em um campo HIDDEN o valor do label, para ser passado por linkProperty (para outra p�gina)
 */	 
function setLabelPrestadorServicoHidden(){
	if (i18NLabel.getLabel("prestadorServico") != "")
		document.getElementById("labelPrestadorServico").masterLink = "true";
		setElementValue(document.getElementById("labelPrestadorServico"), i18NLabel.getLabel("prestadorServico"));
}

function carregaPagina(){
	setLabelPrestadorServicoHidden();
	onPageLoad();
}
</script>

<adsm:window service="lms.carregamento.manterPrestadoresServicoAction" onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregaPagina">

	<adsm:i18nLabels>
		<adsm:include key="prestadorServico"/>
	</adsm:i18nLabels>
		
	<adsm:form action="/carregamento/manterPrestadoresServico" idProperty="idPrestadorServico" onDataLoadCallBack="myDataLoad">

		<adsm:complement label="identificacao" required="true" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
						 service="lms.carregamento.manterPrestadoresServicoAction.validateExistenciaPrestadorServico"
						 onDataLoadCallBack="pessoaCallback"/>
		</adsm:complement>
		
		<!-- Atributos para salvar/atualizar em Pessoa -->
		<adsm:hidden property="pessoa.tpPessoa" value="F"/>
		<adsm:hidden property="blTermoComp" value="N"/>
		
		<adsm:textbox property="pessoa.nmPessoa" label="nome" dataType="text" size="60" maxLength="50" labelWidth="18%" width="82%"
					depends="pessoa.nrIdentificacao" required="true"/>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%" required="true" renderOptions="true"/>

		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" size="70" maxLength="60" labelWidth="18%" width="82%" 
					depends="pessoa.nrIdentificacao" />

		<adsm:hidden property="labelPrestadorServico" serializable="false"/>

		<adsm:buttonBar>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idPrestadorServico" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPrestadorServico" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idPrestadorServico" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPrestadorServico" target="labelPessoaTemp" />
			</adsm:button>

			<adsm:storeButton callbackProperty="retorno_salvar" id="storeID"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>

/**
 * Quando detalhar item, desabilita campos de Identificacao da Pessoa
 */
function initWindow(eventObj) {
	setDisabled("pessoa.tpIdentificacao", (eventObj.name == "gridRow_click") );
	setDisabled("pessoa.nrIdentificacao", true );
	setLabelPrestadorServicoHidden();
	setFocusOnFirstFocusableField();
}

/**
 * Fun��o para filtrar os tipos de identifica��o para mostrar apenas as pessoas F�sicas.
 */	
function retornoCarregaPagina_cb(data, error){
	onPageLoad_cb(data,error);
	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
}


/**
 * Ap�s inclus�o atribui o idPessoa inserido.
 * Atribui o valor do label para o campo hidden do form.
 */
function myDataLoad_cb(data, error){
	if (error != undefined) {
		alert(error);
		return false;
	}
	var idPessoa = getNestedBeanPropertyValue(data, "_value");
	
	if (idPessoa == undefined && data != undefined){
		idPessoa = getNestedBeanPropertyValue(data, "pessoa.idPessoa");
	}
	
	if (idPessoa != undefined){
		setElementValue("pessoa.idPessoa", idPessoa);
		setDisabled("pessoa.tpIdentificacao", true );
		setDisabled("pessoa.nrIdentificacao", true );
	}
	onDataLoad_cb(data,error);
	setLabelPrestadorServicoHidden();
	setFocusOnFirstFocusableField();
	
	setTypeNrIdentificacao(true);
	format(document.getElementById("pessoa.nrIdentificacao"));
}

function pessoaCallback_cb(data, error) {
	if (error != undefined){
		alert(error);
		cleanButtonScript(this.document);

		setElementValue("pessoa.tpIdentificacao","");
		setElementValue("pessoa.nrIdentificacao","");
		setElementValue("pessoa.idPessoa","");
		setElementValue("pessoa.nmPessoa","");
		setElementValue("pessoa.dsEmail","");
	}

   	// Se Pessoa cadastrada
   	if (data.idPessoa != undefined) {
	   	setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
	   	setElementValue("pessoa.nrIdentificacao",data.nrIdentificacao);
		setElementValue("pessoa.idPessoa",data.idPessoa);
		setElementValue("pessoa.nmPessoa",data.nmPessoa);
		setElementValue("pessoa.dsEmail",data.dsEmail);
	}
}

function retorno_salvar_cb(data, error){
	if (error != undefined) {
		alert(error);
		return false;
	}
	store_cb(data, error);
	if (data.idPrestadorServico != undefined){
		onDataLoad_cb(data);
		setElementValue("pessoa.idPessoa", data.idPrestadorServico);
	}
	setFocusOnNewButton();
}

/**
 * Se o Tipo de Identifica��o for J�ridico, ent�o o dataType do N�mero de Identifica��o � CNPJ.
 * Ao atribuir o tipo CNPJ � feita a formata��o do campo, setando o focus
 */
function setTypeNrIdentificacao(disableNrIdentificacao){
	var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
	
	if (disableNrIdentificacao == undefined || disableNrIdentificacao == false){
		resetValue(document.getElementById("pessoa.nrIdentificacao"));
		setDisabled("pessoa.nrIdentificacao", (tpIdentificacao == "") );
	}

	if (tpIdentificacao == "CNPJ")
		document.getElementById("pessoa.nrIdentificacao").dataType = "CNPJ";
	else 
	if (tpIdentificacao == "CUIT")
		document.getElementById("pessoa.nrIdentificacao").dataType = "CUIT";
	else 
	if (tpIdentificacao == "RUT" )
		document.getElementById("pessoa.nrIdentificacao").dataType = "RUT";
	else 
	if (tpIdentificacao == "RUC" )
		document.getElementById("pessoa.nrIdentificacao").dataType = "RUC";
}

/**
 * onChange de tpIdentifica��o
 */
function pessoa_tpIdentificacao_onChange(eThis){
	comboboxChange({e:eThis});
	setTypeNrIdentificacao();
	return findPessoa();
}

/**
 * onChange de nrIdentifica��o
 */
function pessoa_nrIdentificacao_onChange(eThis){
	if (validate(eThis) == true)
		findPessoa();
	else
		return false;
}

/**
 * Busca Pessoa pelo Nr Identifica��o e Tipo de Identifica��o.
 * A busca da Pessoa (e valida��o de sua Identifica��o) N�O � realizada no caso de altera��o 
 * de Prestador Servi�o, i.e., a Pessoa em Prestador Servi�o n�o ser� alterada, o que ser�
 * alterado s�o os dados em Pessoa.
 */
function findPessoa(){
	var idPrestadorServico = getElementValue("idPrestadorServico");
	var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
	var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");

	//Permitido apenas se N�mero e Tipo de Identifica��o forem informados!		
	if (tpIdentificacao == "" || nrIdentificacao == ""){
		return false;
	}

	var data = new Array();
	setNestedBeanPropertyValue(data, "pessoa.tpIdentificacao", tpIdentificacao);
	setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdentificacao);
	//Envia idPrestadorServico quando ele existir
	if (idPrestadorServico != ""){
		setNestedBeanPropertyValue(data, "idPrestadorServico", idPrestadorServico);
	}
	var sdo = createServiceDataObject("lms.carregamento.manterPrestadoresServicoAction.validateIdentificacao", "pessoa_identificacao",data);
	xmit({serviceDataObjects:[sdo]});
    return true;
}

/**
 * CallBack que popula dados de Pessoa no formul�rio.
 * Em caso de erro (Exception, da Service), n�o realiza nada.
 * Se a pessoa existe seus dados s�o populados nos campos.<b> 
 * Se a pessoa n�o existe, os campos s�o reiniciados e � permitido inserir uma nova Prestadora/Pessoa.
 */
function pessoa_identificacao_cb(data, error){
	if (error != undefined){
		alert(error);
		reiniciaValores();
		setFocus(document.getElementById("pessoa.tpIdentificacao"), false);
		return false;
	}

	//Pessoa n�o cadastrada em Prestador Servi�o
	if (data.idPessoa != undefined){
		setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
		setElementValue("pessoa.idPessoa",data.idPessoa);
		setElementValue("pessoa.nmPessoa",data.nmPessoa);
		setElementValue("pessoa.tpPessoa",data.tpPessoa);
		setElementValue("pessoa.dsEmail",data.dsEmail);
		return true;
	}

	//Pessoa n�o existe
	//Reiniciar valores
	resetValue(document.getElementById("pessoa.idPessoa"));
	resetValue(document.getElementById("pessoa.nmPessoa"));
	resetValue(document.getElementById("pessoa.tpPessoa"));
	resetValue(document.getElementById("pessoa.dsEmail"));
}

/**
 *
 */
function reiniciaValores(){
	resetValue(document.getElementById("pessoa.nrIdentificacao"));
	resetValue(document.getElementById("pessoa.tpIdentificacao"));
}
</script>