<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreNotaFiscalServicoAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04036"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/digitarPreNotaFiscalServico">

		<adsm:hidden property="servico.tpModal"/>
		<adsm:hidden property="filialByIdFilialOrigem.empresa.tpEmpresa"/>
		<adsm:hidden property="blBloqueado"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tipoServico.idTipoServico"/>
		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>

		<adsm:lookup
			label="filialEmissao"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findFilialEmissaoLookup"
			action="/municipios/manterFiliais"
			dataType="text"
			exactMatch="true"
			labelWidth="14%"
			maxLength="3"
			minLengthForAutoPopUpSearch="3"
			size="5"
			width="43%"
			required="true"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="30"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="integer"
			property="nrNotaFiscalServico"
			label="numNF"
			maxLength="9"
			size="8"
			labelWidth="15%"
			width="28%"/>

		<adsm:range label="periodoEmissao" labelWidth="14%" width="43%">
			<adsm:textbox
				dataType="JTDate"
				property="dtInicial"
				smallerThan="dtFinal"/>
			<adsm:textbox
				biggerThan="dtInicial"
				dataType="JTDate"
				property="dtFinal"/>
		</adsm:range>

		<adsm:lookup
			label="municipioDoServico"
			property="municipio"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findMunicipioLookup"
			action="/municipios/manterMunicipios"
			labelWidth="15%"
			width="28%"
			dataType="text"
			size="30"
			maxLength="60"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3">
		</adsm:lookup>

		<adsm:lookup
			label="tomadorServico"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findDestinatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="14%"
			maxLength="20"
			size="20"
			width="43%">
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:lookup>

		<adsm:combobox
			labelWidth="15%"
			width="28%"
			label="servicoAdicional"
			property="servicoAdicional.idServicoAdicional"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findServicosAdicionais"
			optionProperty="idServicoAdicional"
			optionLabelProperty="dsServicoAdicional"
			autoLoad="true"
			boxWidth="220">
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="notaFiscalServico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idNotaFiscalServico"
		property="notaFiscalServico"
		service="lms.expedicao.digitarPreNotaFiscalServicoAction.findPaginatedNFS"
		rowCountService="lms.expedicao.digitarPreNotaFiscalServicoAction.getRowCountNFS"
		unique="true"
		gridHeight="200"
		selectionMode="none"
		rows="12">
		<adsm:gridColumn title="filialEmissao" property="sgFilialOrigem" width="14%" dataType="text"/>
		<adsm:gridColumn title="numNF" property="nrNotaFiscalServico" width="10%" dataType="integer" mask="000000" />
		<adsm:gridColumn title="dataEmissao" property="dhEmissao" width="16%" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="destinatario" property="nmPessoaCliente" width="33%" dataType="text"/>
		<adsm:gridColumn title="servicoAdicional" property="dsServicoAdicional" width="15%" dataType="text"/>
		<adsm:gridColumn title="valor" property="valor" align="right" unit="reais" dataType="currency"/>
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />		
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script type="text/javascript">
	function validateTab() {
		/** Validacao Padrao */
		if (!validateTabScript(this.document.forms)) {
			return false;
		}

		var nrNotaFiscalServico = getElementValue("nrNotaFiscalServico");
		var dtInicial = getElementValue("dtInicial");
		var dtFinal = getElementValue("dtFinal");

		/** nrNota ou Periodo devem ser preenchidos. */
		var isValid = (nrNotaFiscalServico != "" || (dtInicial != "" && dtFinal != ""));
		if (!isValid) {
			alert(i18NLabel.getLabel("LMS-04036"));
			/** Seta Foco no campo */
			if (dtInicial != "") {
				setFocus("dtFinal", false);
			} else if (dtFinal != "") {
				setFocus("dtInicial", false);
			} else {
				setFocus("nrNotaFiscalServico", false);
			}
			return false;
		}
		return true;
	}
	
	function myOnPageLoad_cb(d,e){
		onPageLoad_cb(d,e);
		validaBotaoFechar();
	}	
	
	/**
	  *	Valida se deve ou não tornar o botão de fechar visível
	  */
	function validaBotaoFechar(){
		
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
			document.getElementById('btnFechar').property = ".closeButton";
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}	
</script>