<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.informacaoDoctoClienteService">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01004"/>
	</adsm:i18nLabels>

<adsm:form action="/vendas/manterInformacoesDocumentoCliente" idProperty="idInformacaoDoctoCliente">
	<script language="javascript">
		function validateFormat(){
		var formatacao = getElementValue("dsFormatacao");
		var tipo = getElementValue("tpCampo");
		var validacao = false;
		var caracteres = "";

		if(tipo == "A") {
			caracteres = "A9?-/.";
		} else if (tipo == "N") {
			caracteres = "#0,.";
		} else if (tipo == "D") {
			caracteres = "dMy/ Hm:";
		}
		for(j=0; j < formatacao.length; j++) {
			validacao = false;
			for(i=0; i < caracteres.length;i++) {
				if (caracteres.charAt(i)==formatacao.charAt(j)) {
					validacao = true;
				}
			}
			if(validacao == false) {
				setFocus(document.getElementById("dsFormatacao"));
				document.forms[0].dsFormatacao.select();
				alertI18nMessage("LMS-01004");
				return false;
			}
		}
		return true;
	}

	// Função que desabilita o campo tamanho quando o tipo selecionado é Data/Hora
	function onSelectType() {
		var selectedType = getElementValue(document.forms[0].tpCampo);
		var tamanho = document.forms[0].nrTamanho;
		var formatacao = document.getElementById("dsFormatacao");
		if (selectedType == '') {
			setElementValue(tamanho,"");
			tamanho.disabled = true;
			tamanho.datatype = "integer"
			setElementValue(formatacao,"");
			setDisabled(formatacao,true);
			return;
		}
		if (selectedType == 'D') {
			formatacao.disabled = false;
			setElementValue(tamanho, "");
			tamanho.disabled = true;
			tamanho.datatype = "integer"
		} else {
			tamanho.disabled = false;
			formatacao.disabled = false;
		}
		validateFormat()
	}
	
	function afterPopupCliente(data){
		setElementValue("cliente.pessoa.nrIdentificacao",data.pessoa.nrIdentificacaoFormatado);
	}
</script>

	<adsm:lookup
		label="cliente"
		property="cliente"
		idProperty="idCliente"
		criteriaProperty="pessoa.nrIdentificacao"
		relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		action="/vendas/manterDadosIdentificacao"
		service="lms.vendas.manterInformacoesDocumentoClienteAction.findLookupCliente"
		dataType="text"
		size="18"
		maxLength="20"
		labelWidth="15%"
		width="18%"
		exactMatch="false"
		minLengthForAutoPopUpSearch="1"
		afterPopupSetValue="afterPopupCliente"
		required="true"
	>
		<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
		<adsm:textbox
			dataType="text"
			property="cliente.pessoa.nmPessoa"
			size="40"
			disabled="true"
			width="50%"/>
	</adsm:lookup>

	<adsm:combobox
		property="tpModal"
		label="modal"
		domain="DM_MODAL"/>

	<adsm:combobox
		property="tpAbrangencia"
		label="abrangencia"
		domain="DM_ABRANGENCIA"/>

	<adsm:textbox
		maxLength="50"
		dataType="text"
		property="dsCampo"
		label="campo"
		size="43"/>

	<adsm:combobox
		property="tpCampo"
		label="tipo"
		onchange="onSelectType();"
		domain="DM_TIPO_CAMPO"/>

	<adsm:textbox
		maxLength="50"
		dataType="text"
		property="dsFormatacao"
		disabled="true"
		onchange="return validateFormat()"
		label="formatacao"
		size="43"/>

	<adsm:textbox
		label="tamanho"
		property="nrTamanho"
		maxLength="2"
		disabled="true"
		dataType="integer"
		size="5"/>

	<adsm:combobox property="blOpcional" label="opcional" domain="DM_SIM_NAO"/>
	<adsm:combobox property="blImprimeConhecimento" label="imprimeCTO" domain="DM_SIM_NAO"/>
	<adsm:combobox property="blIndicadorNotaFiscal" label="indicadorNF" domain="DM_SIM_NAO"/>
	<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>

	<adsm:buttonBar freeLayout="true">
		<adsm:findButton callbackProperty="informacaoDoctoCliente"/>
		<adsm:resetButton />
	</adsm:buttonBar>
</adsm:form>

	<adsm:grid
		property="informacaoDoctoCliente"
		idProperty="idInformacaoDoctoCliente"
		defaultOrder="cliente_pessoa_.nmPessoa, tpModal, tpAbrangencia, dsCampo"
		gridHeight="200"
		unique="true"
		rows="9">
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa" width="30%" />
		<adsm:gridColumn title="modal" property="tpModal" width="15%" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" width="15%" isDomain="true"/>
		<adsm:gridColumn title="campo" property="dsCampo" width="25%" />
		<adsm:gridColumn title="tipo" property="tpCampo" width="15%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
