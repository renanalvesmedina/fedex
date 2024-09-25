<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.expedicao.manterCRTAction"
	onPageLoad="myOnPageLoad">

	<adsm:form
		action="/expedicao/manterCRT"
		idProperty="idCtoInternacional"
		onDataLoadCallBack="myOnDataLoad"
		height="370">

		<%-- Campo que contem um flag pra determinar em que modo esta a tela--%>
		<adsm:hidden
			property="MODO_TELA"
			value="INCLUSAO"
			serializable="false"/>

		<adsm:hidden
			property="tpSituacao"
			value="A"/>

		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.manterCRTAction.findFilialOrigem"
			dataType="text"
			required="true"
			disabled="true"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialOrigem"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="38%">

			<adsm:propertyMapping
				modelProperty="pessoa.idPessoa"
				relatedProperty="filialByIdFilialOrigem.pessoa.idPessoa" />

			<adsm:propertyMapping
				modelProperty="pessoa.nmFantasia"
				relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" />

			<adsm:hidden property="filialByIdFilialOrigem.pessoa.idPessoa" />

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialOrigem.pessoa.nmFantasia"
				size="30"
				maxLength="60"
				disabled="true" />

		</adsm:lookup>

		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.manterCRTAction.findFilialDestino"
			dataType="text"
			property="filialByIdFilialDestino"
			criteriaProperty="sgFilial"
			idProperty="idFilial"
			required="true"
			label="filialDestino"
			size="3"
			maxLength="3"
			labelWidth="12%"
			width="38%">

			<adsm:propertyMapping modelProperty="pessoa.idPessoa"
				relatedProperty="filialByIdFilialDestino.pessoa.idPessoa" />

			<adsm:propertyMapping
				modelProperty="pessoa.nmFantasia"
				relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" />

			<adsm:hidden property="filialByIdFilialDestino.pessoa.idPessoa" />

			<adsm:textbox dataType="text"
				property="filialByIdFilialDestino.pessoa.nmFantasia" size="30"
				serializable="false" maxLength="45" disabled="true" />
		</adsm:lookup>

		<adsm:textbox property="nrCrt" dataType="text" label="numeroCRT"
			size="6" maxLength="6" required="false" disabled="true"
			serializable="true" labelWidth="12%" width="38%" />

		<adsm:textbox
			dataType="JTDate"
			label="dataEmissao"
			labelWidth="12%"
			width="38%"
			size="9"
			picker="false"
			property="dhEmissao"
			required="false" />

		<adsm:textbox
			property="tpSituacaoCrt.description"
			dataType="text"
			label="situacao"
			labelWidth="12%"
			required="true"
			width="38%"
			disabled="true" />

		<adsm:combobox property="servico.idServico" optionProperty="idServico"
			optionLabelProperty="dsServico" onDataLoadCallBack="servico"
			label="servico"
			onlyActiveValues="true"
			service="lms.expedicao.manterCRTAction.findServicoCombo"
			required="true" labelWidth="12%" width="38%" boxWidth="220"/>
		
		<adsm:textbox
			property="dsMoeda"
			dataType="text"
			required="false"
			size="7"
			serializable="false"
			labelWidth="12%"
			label="moeda"
			width="38%"
			disabled="true"/>

		<adsm:hidden
			property="moeda.sgMoeda"
			serializable="true"/>

		<adsm:hidden
			property="moeda.dsSimbolo"
			serializable="true"/>
		
		<adsm:section caption="dadosRemetente01" />

		<adsm:lookup
			label="remetente"
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findRemetente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			onchange="return clienteByIdClienteRemetenteOnChange(this);"
			onDataLoadCallBack="clienteByIdClienteRemetenteOnDataLoad"
			onPopupSetValue="listenerInteractionPopup"
			size="20"
			maxLength="20"
			labelWidth="22%"
			width="47%"
			required="true">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" />

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:propertyMapping
				modelProperty="pessoa.idInscricaoEstadual"
				relatedProperty="inscricaoEstadualDestinatario.idInscricaoEstadual" />

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="dsNomeRemetente" />

			<adsm:propertyMapping
				modelProperty="pessoa.endereco.idMunicipio"
				relatedProperty="clienteByIdClienteRemetente.pessoa.enderecoPessoa.municipio.idMunicipio" />

			<adsm:propertyMapping
				modelProperty="pessoa.endereco.nrCep"
				relatedProperty="clienteByIdClienteRemetente.pessoa.enderecoPessoa.nrCep" />

			<adsm:hidden
				property="clienteByIdClienteRemetente.pessoa.enderecoPessoa.municipio.idMunicipio" />

			<adsm:hidden
				property="clienteByIdClienteRemetente.pessoa.enderecoPessoa.nrCep" />

			<adsm:hidden
				property="inscricaoEstadualDestinatario.idInscricaoEstadual" />

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="34"
				maxLength="40"
				disabled="true" />
		</adsm:lookup>

		<td colspan="17">
			<adsm:button style="FmLbSectionj"
				id="btnIncluirRemetente"
				caption="incluir"
				onclick="WindowUtils.showCadastroCliente();" />
		</td>

		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="22%" />

		<adsm:textarea
			maxLength="500"
			property="dsDadosRemetente"
			width="68%"
			columns="80"
			onchange="controlModifiedFields(this)"
			rows="4"
			required="true"/>

		<adsm:textbox dataType="text" label="nome21" labelWidth="22%"
			width="68%" property="dsNomeRemetente" size="80" maxLength="60"
			required="true" />

		<adsm:section caption="dadosDestinatario04" />

		<adsm:lookup
			label="destinatario"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findDestinatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			onDataLoadCallBack="clienteByIdClienteDestinatarioOnDataLoad"
			onchange="return clienteByIdClienteDestinatarioOnChange(this);"
			onPopupSetValue="listenerInteractionPopup"
			size="20"
			maxLength="20"
			width="47%"
			labelWidth="22%"
			required="true"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" />

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				relatedProperty="tpSituacao"/>

			<adsm:propertyMapping
				modelProperty="pessoa.endereco.idMunicipio"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.enderecoPessoa.municipio.idMunicipio" />

			<adsm:propertyMapping
				modelProperty="pessoa.endereco.nrCep"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.enderecoPessoa.nrCep" />

			<adsm:hidden
				property="clienteByIdClienteDestinatario.pessoa.enderecoPessoa.municipio.idMunicipio" />

			<adsm:hidden
				property="clienteByIdClienteDestinatario.pessoa.enderecoPessoa.nrCep" />

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="34"
				maxLength="40"
				disabled="true" />
		</adsm:lookup>

		<td colspan="17">
			<adsm:button
				style="FmLbSection"
				caption="incluir"
				id="btnIncluirDestinatario"
				onclick="WindowUtils.showCadastroCliente();" />
		</td>

		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="22%" />
		<adsm:textarea
			maxLength="600"
			property="dsDadosDestinatario"
			width="70%"
			columns="80"
			rows="4"
			onchange="replicateData();controlModifiedFields(this)"
			required="true" />

		<adsm:section caption="dadosConsignatario06" />

		<adsm:lookup
			label="consignatario"
			property="clienteByIdClienteConsignatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findConsignatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			onDataLoadCallBack="clienteByIdClienteConsignatarioOnDataLoad"
			onchange="return clienteByIdClienteConsignatarioOnChange(this);"
			onPopupSetValue="listenerInteractionPopup"
			size="20"
			maxLength="20"
			width="47%"
			labelWidth="22%"
			required="true">

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteConsignatario.pessoa.nmPessoa" />

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteConsignatario.pessoa.nmPessoa"
				size="34"
				maxLength="40"
				disabled="true" />

		</adsm:lookup>

		<td colspan="17">
			<adsm:button
				style="FmLbSection"
				caption="incluir"
				id="btnIncluirConsignatario"
				onclick="WindowUtils.showCadastroCliente();" />
		</td>
		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="22%" />
		<adsm:textarea
			property="dsDadosConsignatario"
			width="70%"
			columns="80"
			onchange="controlModifiedFields(this)"
			rows="3"
			maxLength="500"
			required="true"/>

		<adsm:section caption="dadosNotificado09" />

		<adsm:lookup
			label="notificado"
			property="clienteByIdClienteRedespacho"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.manterCRTAction.findNotificado"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			onDataLoadCallBack="clienteByIdClienteRedespachoOnDataLoad"
			onchange="return clienteByIdClienteRedespachoOnChange(this);"
			onPopupSetValue="listenerInteractionPopup"
			size="20"
			maxLength="20"
			width="47%"
			labelWidth="22%"
			required="true"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRedespacho.pessoa.nmPessoa" />

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRedespacho.pessoa.nmPessoa"
				size="34"
				maxLength="40"
				disabled="true" />
		</adsm:lookup>

		<td colspan="17">
			<adsm:button
				style="FmLbSection"
				caption="incluir"
				id="btnIncluirRedespacho"
				onclick="WindowUtils.showCadastroCliente();" />
		</td>

		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="22%" />
		<adsm:textarea
			property="dsNotificar"
			width="70%"
			columns="80"
			rows="3"
			onchange="controlModifiedFields(this)"
			maxLength="500"
			required="true" />

		<adsm:section caption="dadosGeraisSecao" />

		<adsm:combobox
			required="false"
			property="pedidoColeta.idPedidoColeta"
			optionLabelProperty="nrColeta"
			optionProperty="idPedidoColeta"
			service="lms.expedicao.manterCRTAction.findPedidoColetaCombo"
			label="numeroColeta"
			width="78%"
			boxWidth="180"
			labelWidth="22%">

			<adsm:propertyMapping
				criteriaProperty="clienteByIdClienteRemetente.idCliente"
				modelProperty="clienteByIdClienteRemetente.idCliente"/>

		</adsm:combobox>

		<adsm:lookup
			action="/municipios/manterMunicipios"
			service="lms.expedicao.manterCRTAction.findMunicipioEmissao"
			dataType="text"
			property="municipioEmissao"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			label="municipioEmissao05"
			onDataLoadCallBack="municipioEmissaoOnDataLoad"
			onPopupSetValue="municipioEmissaoOnPopup"
			onchange="return municipioEmissaoOnChange(this)"
			size="20"
			maxLength="20"
			width="80%"
			labelWidth="22%"
			required="true">

			<adsm:textbox
				dataType="text"
				property="dsLocalEmissao"
				onchange="controlModifiedFields(this)"
				size="71"
				maxLength="500" />

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

		</adsm:lookup>

		<adsm:lookup
			action="/municipios/manterMunicipios"
			service="lms.expedicao.manterCRTAction.findMunicipioCarregamento"
			dataType="text"
			property="municipioCarregamento"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			onDataLoadCallBack="municipioCarregamentoOnDataLoad"
			onPopupSetValue="municipioCarregamentoOnPopup"
			onchange="return municipioCarregamentoOnChange(this)"
			label="municipioCarregamento07"
			size="20"
			maxLength="20"
			width="80%"
			labelWidth="22%"
			required="true">

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:textbox
				dataType="text"
				property="dsLocalCarregamento"
				onchange="controlModifiedFields(this)"
				size="71"
				maxLength="500" />
		</adsm:lookup>

		<adsm:textbox
			dataType="JTDate"
			label="dataCarregamento"
			labelWidth="22%"
			width="80%"
			property="dtCarregamento"
			required="true"
			picker="false"/>

		<adsm:lookup
			action="/municipios/manterMunicipios"
			service="lms.expedicao.manterCRTAction.findMunicipioEntrega"
			dataType="text"
			property="municipioEntrega"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			onDataLoadCallBack="municipioEntregaOnDataLoad"
			onPopupSetValue="municipioEntregaOnPopup"
			onchange="return municipioEntregaOnChange(this)"
			label="municipioEntrega08"
			size="20"
			maxLength="20"
			width="80%"
			labelWidth="22%"
			required="true">

			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>

			<adsm:textbox
				dataType="text"
				property="dsLocalEntrega"
				onchange="controlModifiedFields(this)"
				size="71"
				maxLength="500" />
		</adsm:lookup>

		<adsm:textbox
			dataType="integer"
			label="prazoEntrega"
			labelWidth="22%"
			width="80%"
			property="nrDiasPrevEntrega"
			unit="dias"
			size="3"
			required="false" />

		<adsm:textbox
			dataType="text"
			label="transportadoresSucessivos10"
			labelWidth="22%"
			width="80%"
			property="dsTransportesSucessivos"
			disabled="true"
			size="100"
			maxLength="500"
			required="true"/>

		<adsm:textarea
			maxLength="500"
			property="dsAnexos"
			label="complementoCampo17"
			labelStyle="vertical-align: text-top; margin-top: 4px;padding-top:8px; border:none;"
			labelWidth="22%"
			width="80%"
			columns="90"
			rows="3"/>

		<adsm:textarea
			maxLength="500"
			property="dsAduanas"
			label="complementoCampo18"
			labelStyle="vertical-align: text-top; margin-top: 4px;padding-top:8px; border:none;"
			labelWidth="22%"
			width="80%"
			columns="90"
			rows="3"/>

		<adsm:section caption="dadosMercadoria" />

		<adsm:textarea
			property="dsDadosMercadoria"
			label="qtdeTipoVolumes11"
			labelStyle="vertical-align: text-top; margin-top: 4px;padding-top:8px; border:none;"
			labelWidth="22%"
			width="80%"
			columns="90"
			required="true"
			onchange="controlModifiedFields(this)"
			rows="11"
			maxLength="1000" />

		<adsm:textbox
			property="psReal"
			dataType="weight"
			minValue="0.001"
			label="pesoBruto12"
			onchange="controlModifiedFields(this)"
			size="10"
			maxLength="10"
			unit="kg"
			labelWidth="22%"
			width="28%"
			required="true" />

		<adsm:textbox
			property="psLiquido"
			dataType="weight"
			minValue="0.001"
			label="pesoLiquido12"
			size="10"
			onchange="controlModifiedFields(this)"
			maxLength="10"
			unit="kg"
			labelWidth="22%"
			width="28%"
			required="true" />

		<adsm:textbox
			property="vlVolume"
			dataType="decimal"
			minValue="0.01"
			label="cubagem13"
			onchange="controlModifiedFields(this)"
			size="10"
			maxLength="10"
			unit="m3"
			labelWidth="22%"
			width="28%"
			required="true" />

		<adsm:complement
			label="valorMercadoria14"
			labelWidth="22%"
			width="28%"
			separator="branco"
			required="true">
			<adsm:combobox
				property="moedaValorMercadoria.idMoeda"
				optionLabelProperty="dsSimbolo"
				optionProperty="idMoeda"
				onchange="controlModifiedFields(this)"
				service="lms.expedicao.manterCRTAction.findMoedaCombo"
				boxWidth="85">
			</adsm:combobox>
			<adsm:textbox
				dataType="currency"
				minValue="0.01"
				property="vlMercadoria"
				onchange="controlModifiedFields(this)"
				size="10"
				maxLength="18" />
		</adsm:complement>

		<adsm:textbox
			property="dsValorMercadoria"
			dataType="text"
			size="28"
			label="incoterme16"
			labelWidth="22%"
			maxLength="60"
			required="true"
			width="28%"/>

		<adsm:combobox
			property="moedaValorTotalMercadoria.idMoeda"
			service="lms.expedicao.manterCRTAction.findMoedaCombo"
			optionLabelProperty="dsSimbolo"
			optionProperty="idMoeda"
			labelWidth="22%"
			width="28%"
			required="true"
			label="valorTotalMercadoria16"
			boxWidth="85">

			<adsm:textbox
				dataType="currency"
				minValue="0.01"
				property="vlTotalMercadoria"
				size="10"
				maxLength="18" />
		</adsm:combobox>

		<adsm:section caption="dadosParaMIC" />

		<adsm:combobox
			label="tipoMercadoria"
			property="produto.idProduto"
			optionLabelProperty="dsProduto"
			optionProperty="idProduto"
			onlyActiveValues="true"
			service="lms.expedicao.manterCRTAction.findProdutoCombo"
			labelWidth="22%"
			boxWidth="180"
			required="true"
			width="28%" />

		<adsm:combobox
			label="tipoVolumes"
			property="embalagem.idEmbalagem"
			optionLabelProperty="dsEmbalagem"
			optionProperty="idEmbalagem"
			onlyActiveValues="true"
			service="lms.expedicao.manterCRTAction.findEmbalagemCombo"
			labelWidth="22%"
			boxWidth="180"
			required="true"
			width="28%" />

		<adsm:textbox
			property="qtVolumes"
			dataType="integer"
			label="qtdeVolumes"
			size="10"
			maxLength="10"
			labelWidth="22%"
			width="78%"
			minValue="1"
			required="true" />

		<adsm:section caption="manutencoes" />

		<adsm:textbox
			property="dhInclusao"
			label="dataHoraDeInclusao"
			dataType="JTDateTimeZone"
			picker="false"
			disabled="true"
			labelWidth="22%"
			width="28%" />

		<adsm:textbox
			property="usuarioByIdUsuarioInclusao.nmUsuario"
			label="usuarioDeInclusao"
			dataType="text"
			size="30" disabled="true"
			labelWidth="22%"
			width="28%"/>

		<adsm:textbox
			property="dhAlteracao"
			label="dataHoraDeAlteracao"
			dataType="JTDateTimeZone"
			picker="false"
			disabled="true"
			labelWidth="22%"
			width="28%" />

		<adsm:textbox
			property="usuarioByIdUsuarioAlteracao.nmUsuario"
			label="usuarioDeAlteracao"
			dataType="text"
			size="30"
			disabled="true"
			labelWidth="22%"
			width="28%" />

		<adsm:buttonBar lines="2">

			<adsm:button
				caption="documentosAnexos17"
				id="btnDocumentosAnexos"
				boxWidth="160"
				onclick="WindowUtils.showDocumentosAnexos();" />

			<adsm:button
				caption="aduanas18"
				id="btnAduanas"
				boxWidth="110"
				onclick="WindowUtils.showAduanas();" />

			<adsm:button
				caption="observacoes22"
				id="btnObservacoes"
				boxWidth="120"
				onclick="WindowUtils.showObservacoes();" />

			<adsm:button
				caption="servicosAdicionais"
				id="btnServicosAdicionais"
				boxWidth="130"
				onclick="WindowUtils.showServicosAdicionais();" />

			<adsm:button
				caption="despachantes"
				id="btnDespachantes"
				boxWidth="110"
				onclick="WindowUtils.showDespachantes();" />

			<adsm:button
				caption="dimensoes"
				id="btnDimensoes"
				boxWidth="110"
				onclick="WindowUtils.showDimensoes();" />

			<adsm:button
				caption="calculoCRT"
				id="btnCalculoCRT"
				boxWidth="120"
				onclick="storeInSession();" />

			<adsm:button
				caption="gastos15"
				id="btnGastos"
				boxWidth="130"
				onclick="WindowUtils.showGastos();" />

			<adsm:button
				caption="emitir"
				id="btnEmitir"
				boxWidth="110"
				onclick="WindowUtils.showEmitir();" />

			<adsm:button
				caption="duplicar"
				onclick="duplicar()"
				id="btnDuplicar"
				boxWidth="110" />

		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="valorTotalMercadoria16"/>
			<adsm:include key="LMS-04024"/>
		</adsm:i18nLabels>
	</adsm:form>
</adsm:window>

<script type="text/javascript" src="../lib/expedicao.js">
</script>

<script type="text/javascript" src="../lib/digitarDadosCTRC.js">
</script>

<script type="text/javascript">
	var tpDocumentoServico = "CRT";
	var tpDocumento = "CRT";
	var CtoInternacional = {
		servico : {
			idServicoPadrao : 0
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var ObjectUtils = {
		extend : function(from, to){
			if(!from || !to) return null;
			for(var k in from) to[k] = from[k];
			return to;
		}
		/************************************************************\
		*
		\************************************************************/
		,objToString : function (o){
			if(!o) return '';
	
			var saida = '';
			var reg = /function (.*?)\(/m;
			var c = reg.test("" + o.constructor) ? RegExp.$1 : '';
	
			for(var a in o) {
				saida += c + '.' + a + '='+ o[a] + '\n';
			}

			return saida;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var isModifiedByPopups = false;//Variavel que guarda um boolean para indicar se o crt foi modificado pela popup de documentos anexos ou aduanas
	var WindowUtils = {
		DEFAULT_CONTEXT : '/expedicao/'
		,params : {}
		,Options : {
			unardorned : 'no'
			,scroll : 'no'
			,resizable : 'no'
			,status : 'no'
			,center : 'yes'
			,help : 'no'
			,dialogWidth : '700px'
			,dialogHeight : '350px'
			/************************************************************\
			*
			\************************************************************/
			,toStr : function(){
				var str = '';
				for(var k in this){
					if(typeof(this[k]) == 'string') str += k + ':' + this[k] + ';';
				}
				return str;
			}
		}
		/************************************************************\
		*
		\************************************************************/
		,showModalParams : function(url, params, options){
			return showModalDialog(url, params, options);
		}
		/************************************************************\
		*
		\************************************************************/
		,showModal : function(url){
			return this.showModalParams(url, window, this.Options.toStr());
		}
		/************************************************************\
		*
		\************************************************************/
		,showModalBySize : function(url, window, width, height){
			var opts = ObjectUtils.extend(this.Options, {});
			opts.dialogWidth = width + 'px';
			opts.dialogHeight = height + 'px';
			return this.showModalParams(url, window, opts.toStr());
		}
		/************************************************************\
		*
		\************************************************************/
		,showModalDefaultImpl: function(url, w, h){
			var modo = getElementValue('MODO_TELA');
			var isModified = false;;
			if(modo == ModosTela.ALTERACAO) isModified = Snapshot.isModified() || isModifiedByPopups;

			this.params.w = window;
			this.params.MODO_TELA = modo;
			this.params.isModified = isModified;

			return this.showModalBySize(this.DEFAULT_CONTEXT + url, this.params, w, h);
		}
		/************************************************************\
		*
		\************************************************************/
		,showDocumentosAnexos : function(){
			var retorno = this.showModalDefaultImpl('manterCRTDocumentosAnexos.do?cmd=main', 590, 340);
			isModifiedByPopups = (retorno == "MODIFICOU_DADOS");
			if(isModifiedByPopups) controlDisableStoreButtons(true);
			return retorno;
		}
		/************************************************************\
		*
		\************************************************************/
		,showAduanas : function(){
			var retorno = this.showModalDefaultImpl('manterCRTAduanas.do?cmd=main', 400, 190);
			isModifiedByPopups = (retorno == "MODIFICOU_DADOS");
			if(isModifiedByPopups) controlDisableStoreButtons(true);
			return retorno;
		}
		/************************************************************\
		*
		\************************************************************/
		,showObservacoes : function(){
			return this.showModalDefaultImpl('manterCRTObservacoes.do?cmd=main', 445, 275);
		}
		/************************************************************\
		*
		\************************************************************/
		,showServicosAdicionais : function(){
			var idDocumento = getElementValue('idCtoInternacional');
			return openServicosAdicionais(idDocumento);
		}
		/************************************************************\
		*
		\************************************************************/
		,showDespachantes : function(){
			var idCliente = getElementValue('clienteByIdClienteDestinatario.idCliente');
			var obj = getElement('clienteByIdClienteDestinatario.idCliente');
			if(getElementValue(obj) != ''){
				var url = 'manterCRTDespachantes.do?cmd=main&idCliente=' + idCliente;
				return this.showModalDefaultImpl(url, 550, 195);
			}

			alert(getMessage(erRequired, [obj.label]));
			setFocus('clienteByIdClienteDestinatario.pessoa.nrIdentificacao', false);

			return null;
		}
		/************************************************************\
		*
		\************************************************************/
		,showDimensoes : function(){
			var idDocumento = getElementValue('idCtoInternacional');
			return openDimensoes(idDocumento);
		}
		/************************************************************\
		*
		\************************************************************/
		,showCalculoCRT : function(){
			return this.showModalDefaultImpl('manterCRTCalculoCRT.do?cmd=main', 535, 555);
		}
		/************************************************************\
		*
		\************************************************************/
		,showGastos : function(){
			var retorno = this.showModalDefaultImpl('manterCRTGastos.do?cmd=main', 416, 345);
			return returnPopupsListener(retorno);
		}
		/************************************************************\
		*
		\************************************************************/
		,showEmitir : function(){
			return this.showModalDefaultImpl('manterCRTEmitir.do?cmd=main', 400, 205);
		}
		/************************************************************\
		*
		\************************************************************/
		,showCadastroCliente : function(){
			var data = this.showModalDefaultImpl('cadastrarClientes.do?cmd=main&origem=exp', 750, 300);
			listenerInteractionPopup(data);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var idServicoPadrao;
	function servico_cb(data) {
		if(data) {
			servico_idServico_cb(data);
			for(var i = 0; i < data.length; i++) {
				if(data[i].servicoPadrao) {
					CtoInternacional.servico.idServicoPadrao = data[i].idServico;
					setServicoPadrao();
					break;
				}
			}
			
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function setServicoPadrao() {
		if(CtoInternacional.servico.idServicoPadrao) {
			setElementValue("servico.idServico", CtoInternacional.servico.idServicoPadrao);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var FormatadorMunicipio = {
		nmMunicipio : ''
		,sgUnidadeFederativa : ''
		,nmPais : ''
		,isOk : function(key){
			return this[key] && this[key] != '';
		}
		,toStr : function(){
			var saida = '';
			if(this.isOk('nmMunicipio')) saida += this.nmMunicipio;
			if(this.isOk('sgUnidadeFederativa')) saida += ' - ' + this.sgUnidadeFederativa;
			if(this.isOk('nmPais')) saida += ' - ' + this.nmPais;
			return saida;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var FormatadorEndereco = {
		nmPessoa : ''
		,tpIdentificacao : ''
		,nrIdentificacao : ''
		,tpLogradouro : ''
		,dsEndereco : ''
		,nrEndereco : ''
		,dsComplemento : ''
		,dsBairro : ''
		,nrCep : ''
		,nmMunicipio : ''
		,sgUnidadeFederativa : ''
		,nmPais : ''
		,isOk : function(key){
			return this[key] && this[key] != '';
		}
		,toStr : function(){
			var saida = '';
			var linha1 = '';
			var linha2 = '';
			var linha3 = '';
			var linha4 = '';

			if(this.isOk('nmPessoa')) linha1 += this.nmPessoa;

			if(this.isOk('tpLogradouro')) linha2 += this.tpLogradouro + ' ';
			if(this.isOk('dsEndereco')) linha2 += this.dsEndereco + ', ';
			if(this.isOk('nrEndereco')) linha2 += this.nrEndereco;
			if(this.isOk('dsComplemento')) linha2 += ' - ' + this.dsComplemento;
			if(this.isOk('dsBairro')) linha2 += ' - ' + this.dsBairro;

			if(this.isOk('nrCep')) linha3 += this.nrCep;
			if(this.isOk('nmMunicipio')) linha3 += ' - ' + this.nmMunicipio;
			if(this.isOk('sgUnidadeFederativa')) linha3 += ' - ' + this.sgUnidadeFederativa;
			if(this.isOk('nmPais')) linha3 += ' - ' + this.nmPais;

			if(this.isOk('tpIdentificacao') && this.isOk('nrIdentificacao')) {
				linha4 += this.tpIdentificacao.toUpperCase();
				linha4 += ": ";
				linha4 += this.nrIdentificacao;
			}

			if(linha1 != '') saida += linha1;
			if(linha2 != '') saida += (saida != '' ? '\n' : '') + linha2;
			if(linha3 != '') saida += (saida != '' ? '\n' : '') + linha3;
			if(linha4 != '') saida += (saida != '' ? '\n' : '') + linha4;

			return saida;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteRemetenteOnChange(obj){
		if(obj.value == ''){
			setElementValue('dsDadosRemetente', '');
			setDisabled('btnIncluirRemetente', true);
		}
		return clienteByIdClienteRemetente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteRemetenteOnDataLoad_cb(data){
		var isFound = false;
	
		if(data && data.length > 0)
			isFound = clienteByIdClienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);

		setElementValue('dsDadosRemetente', getEndereco(data));

		setDisabled('btnIncluirRemetente', isFound);
		setFocus("btnIncluirRemetente", isFound);

		if(!isFound){
			setElementValue('clienteByIdClienteRemetente.idCliente', '');
			setElementValue('clienteByIdClienteRemetente.pessoa.nmPessoa', '');
			setElementValue('dsNomeRemetente', '');
		} else if(data && data != undefined){
			setPropertiesInMunicipio(data[0].pessoa.endereco, 'Usuario')
		}

		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEmissaoOnDataLoad_cb(data){
		var isFound = municipioEmissao_nmMunicipio_exactMatch_cb(data);
		setElementValue('dsLocalEmissao', getMunicipio(data));
		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEmissaoOnPopup(data){
		if(data){
			setElementValue('municipioEmissao.idMunicipio', data.idMunicipio);
			setElementValue('municipioEmissao.nmMunicipio', data.nmMunicipio);
		}
		var dataToArray = new Array(data);
		setElementValue('dsLocalEmissao', getMunicipio(dataToArray));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEmissaoOnChange(obj){
		if(obj.value == ''){
			setElementValue('municipioEmissao.idMunicipio', '');
			setElementValue('municipioEmissao.nmMunicipio', '');
			setElementValue('dsLocalEmissao', '');
		}
		return municipioEmissao_nmMunicipioOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function setPropertiesInMunicipio(objEndereco, callerId){
		var dataMunicipios = [];
		dataMunicipios[0] = {//Cria um objeto municipio para popular o municipio de emissao
			idMunicipio : objEndereco.idMunicipio
			,nmMunicipio : objEndereco.nmMunicipio
			,unidadeFederativa : {
				sgUnidadeFederativa : objEndereco.sgUnidadeFederativa
				,pais : {
					nmPais : objEndereco.nmPais
				}
			}
		};

		var target = callerId;

		if(callerId == 'Usuario') target = 'Emissao';
		else if(callerId == 'Remetente') target = 'Carregamento';
		else if(callerId == 'Destinatario') target = 'Entrega';

		setElementValue('municipio' + target + '.idMunicipio', objEndereco.idMunicipio);
		setElementValue('municipio' + target + '.nmMunicipio', objEndereco.nmMunicipio);
		setElementValue('dsLocal' + target, getMunicipio(dataMunicipios));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioCarregamentoOnDataLoad_cb(data){
		var isFound = municipioCarregamento_nmMunicipio_exactMatch_cb(data);
		setElementValue('dsLocalCarregamento', getMunicipio(data));
		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioCarregamentoOnPopup(data){
		if(data){
			setElementValue('municipioCarregamento.idMunicipio', data.idMunicipio);
			setElementValue('municipioCarregamento.nmMunicipio', data.nmMunicipio);
		}
		var dataToArray = new Array(data);
		setElementValue('dsLocalCarregamento', getMunicipio(dataToArray));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioCarregamentoOnChange(obj){
		if(obj.value == ''){
			setElementValue('municipioCarregamento.idMunicipio', '');
			setElementValue('municipioCarregamento.nmMunicipio', '');
			setElementValue('dsLocalCarregamento', '');
		}
		return municipioCarregamento_nmMunicipioOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEntregaOnDataLoad_cb(data){
		var isFound = municipioEntrega_nmMunicipio_exactMatch_cb(data);
		setElementValue('dsLocalEntrega', getMunicipio(data));
		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEntregaOnPopup(data){
		if(data){
			setElementValue('municipioEntrega.idMunicipio', data.idMunicipio);
			setElementValue('municipioEntrega.nmMunicipio', data.nmMunicipio);
		}
		var dataToArray = new Array(data);
		setElementValue('dsLocalEntrega', getMunicipio(dataToArray));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioEntregaOnChange(obj){
		controlModifiedFields(obj);
		if(obj.value == ''){
			setElementValue('municipioEntrega.idMunicipio', '');
			setElementValue('municipioEntrega.nmMunicipio', '');
			setElementValue('dsLocalEntrega', '');
		}

		return municipioEntrega_nmMunicipioOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function listenerInteractionPopup(data){
		if(data){
			var objCaller = event.srcElement;
			var idCaller = objCaller.id.toUpperCase();
			var nmCallBackFunction = 'clienteByIdCliente';
			var service = "lms.expedicao.manterCRTAction";

			if(idCaller.indexOf("REMETENTE") != -1){
				nmCallBackFunction += 'Remetente';
				service += '.findRemetente';
			} else if(idCaller.indexOf("DESTINATARIO") != -1){
				nmCallBackFunction += 'Destinatario';
				service += '.findDestinatario';
			} else if(idCaller.indexOf("CONSIGNATARIO") != -1){
				nmCallBackFunction += 'Consignatario';
				service += '.findConsignatario';
			} else if(idCaller.indexOf("REDESPACHO") != -1){
				nmCallBackFunction += 'Redespacho';
				service += '.findNotificado';
			} else {
				return false;
			}

			nmCallBackFunction += 'OnDataLoad';

			var sdo = createServiceDataObject(service, nmCallBackFunction, {pessoa:{nrIdentificacao:data.pessoa.nrIdentificacao}});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteDestinatarioOnDataLoad_cb(data){
		var isFound = false;

		if(data && data.length > 0)
			isFound = clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);

		setElementValue('dsDadosDestinatario', getEndereco(data));
		setDisabled('btnIncluirDestinatario', isFound);
		setFocus("btnIncluirDestinatario", isFound);

		if(!isFound){
			setElementValue('clienteByIdClienteDestinatario.idCliente', '');
			setElementValue('clienteByIdClienteDestinatario.pessoa.nmPessoa', '');
		} else if(data && data != undefined){
			setPropertiesInMunicipio(data[0].pessoa.endereco, 'Destinatario');
		}
		
		if(data && data.length > 0){
			//Preenche o valor no campo do notificado
			replicateData();
		}

		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteDestinatarioOnChange(obj){
		if(obj.value == ''){
			setElementValue('dsDadosDestinatario', '');
			setDisabled('btnIncluirDestinatario', true);

			replicateData();
		}

		return clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteConsignatarioOnDataLoad_cb(data){
		var isFound = false;

		if(data && data.length > 0)
			isFound = clienteByIdClienteConsignatario_pessoa_nrIdentificacao_exactMatch_cb(data);

		setElementValue('dsDadosConsignatario', getEndereco(data));
		setDisabled('btnIncluirConsignatario', isFound);
		setFocus("btnIncluirConsignatario", isFound);

		if(!isFound){
			setElementValue('clienteByIdClienteConsignatario.idCliente', '');
			setElementValue('clienteByIdClienteConsignatario.pessoa.nmPessoa', '');
		}

		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteConsignatarioOnChange(obj){
		if(obj.value == ''){
			setElementValue('dsDadosConsignatario', '');
			setDisabled('btnIncluirConsignatario', true);
		}
		return clienteByIdClienteConsignatario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteRedespachoOnDataLoad_cb(data){
		var isFound = false;

		if(data && data.length > 0)
			isFound = clienteByIdClienteRedespacho_pessoa_nrIdentificacao_exactMatch_cb(data);

		setElementValue('dsNotificar', getEndereco(data));
		setDisabled('btnIncluirRedespacho', isFound);
		setFocus("btnIncluirRedespacho", isFound);

		if(!isFound){
			setElementValue('clienteByIdClienteRedespacho.idCliente', '');
			setElementValue('clienteByIdClienteRedespacho.pessoa.nmPessoa', '');
		}

		return isFound;
	}
	/************************************************************\
	*
	\************************************************************/
	function clienteByIdClienteRedespachoOnChange(obj){
		if(obj.value == ''){
			setElementValue('dsNotificar', '');
			setDisabled('btnIncluirRedespacho', true);
		}
		return clienteByIdClienteRedespacho_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function getEndereco(data){
		var objFormEnd = ObjectUtils.extend(FormatadorEndereco, {});
		var obj = data[0];
		var dsDadosRementente = '';

		if(obj && obj.pessoa){

			objFormEnd.nmPessoa = obj.pessoa.nmPessoa;
			objFormEnd.tpIdentificacao = obj.pessoa.tpIdentificacao.value;
			objFormEnd.nrIdentificacao = obj.pessoa.nrIdentificacaoFormatado;
			objFormEnd.tpLogradouro = '';

			if(obj.pessoa.endereco){

				objFormEnd.dsEndereco = obj.pessoa.endereco.dsEndereco;
				objFormEnd.nrEndereco = obj.pessoa.endereco.nrEndereco;
				objFormEnd.dsComplemento = obj.pessoa.endereco.dsComplemento;
				objFormEnd.dsBairro = obj.pessoa.endereco.dsBairro;
				objFormEnd.nrCep = obj.pessoa.endereco.nrCep;
				objFormEnd.tpLogradouro = obj.pessoa.endereco.dsTipoLogradouro;

				if(obj.pessoa.endereco.nmMunicipio){
					objFormEnd.nmMunicipio = obj.pessoa.endereco.nmMunicipio;
				}

				if(obj.pessoa.endereco.nmPais){
					objFormEnd.nmPais = obj.pessoa.endereco.nmPais;
				}

				if(obj.pessoa.endereco.sgUnidadeFederativa){
					objFormEnd.sgUnidadeFederativa = obj.pessoa.endereco.sgUnidadeFederativa;
				}
			}

			dsDadosRementente = objFormEnd.toStr();
		}
		return dsDadosRementente;
	}
	/************************************************************\
	*
	\************************************************************/
	function getMunicipio(data){
		var objFormMun = ObjectUtils.extend(FormatadorMunicipio, {});
		var retorno = '';
		if(data && data.length > 0){
			var obj = data[0];
			
			objFormMun.nmMunicipio = obj.nmMunicipio;
			objFormMun.sgUnidadeFederativa = obj.unidadeFederativa.sgUnidadeFederativa;
			objFormMun.nmPais = obj.unidadeFederativa.pais.nmPais;
			
			retorno = objFormMun.toStr();
		}

		return retorno;
	}
	/************************************************************\
	*
	\************************************************************/
	var ModosTela = {
		INCLUSAO : 'INCLUSAO'
		,ALTERACAO : 'ALTERACAO'
		,VISUALIZACAO : 'VISUALIZACAO'
		,modo : 'INCLUSAO'
		/************************************************************\
		*
		\************************************************************/
		,setModo : function(modo, arg){
			isModifiedByPopups = false;
			this.modo = modo;
			this.setIsDisabledButtons(false);
			this.setDefaultOptions();

			switch(modo){
				case this.INCLUSAO:
					this.setModoInclusao(arg);
					if(!arg){
						var service = "lms.expedicao.manterCRTAction.findDataToInitialStateWindow";
						var sdo = createServiceDataObject(service, "setDataToInitialStateWindow", {});
						xmit({serviceDataObjects:[sdo]});
					}
					break;

				case this.ALTERACAO:
					this.setModoAlteracao();
					break;

				case this.VISUALIZACAO:
					this.setModoVisualizacao();
					break;

				default:
					this.modo = this.INCLUSAO;
					alert("Modo tela não reconhecido.");
					break;
			}
			setElementValue('MODO_TELA', this.modo);
		}
		/************************************************************\
		*
		\************************************************************/
		,setModoInclusao : function(args){
			if(!args) cleanButtonScript();
			setDisabled(document, false);
			setServicoPadrao();
			var objsToDisable = ['filialByIdFilialOrigem.idFilial'
								,'filialByIdFilialOrigem.sgFilial'
								,'filialByIdFilialOrigem.pessoa.nmFantasia'
								,'filialByIdFilialDestino.pessoa.nmFantasia'
								,'nrCrt'
								,'dsMoeda'
								,'clienteByIdClienteRemetente.pessoa.nmPessoa'
								,'btnIncluirRemetente'
								,'clienteByIdClienteDestinatario.pessoa.nmPessoa'
								,'btnIncluirDestinatario'
								,'clienteByIdClienteConsignatario.pessoa.nmPessoa'
								,'btnIncluirConsignatario'
								,'clienteByIdClienteRedespacho.pessoa.nmPessoa'
								,'btnIncluirRedespacho'
								,'dhEmissao'
								,'tpSituacaoCrt.description'
								,'nrDiasPrevEntrega'
								,'dsTransportesSucessivos'
								,'dtCarregamento'
								,'dhInclusao'
								,'usuarioByIdUsuarioInclusao.nmUsuario'
								,'dhAlteracao'
								,'usuarioByIdUsuarioAlteracao.nmUsuario'
								,'btnGastos'
								,'btnEmitir'
								,'btnDuplicar'];

			this.setIsDisableComponentRange(objsToDisable, true);
		}
		/************************************************************\
		*
		\************************************************************/
		,setModoAlteracao : function(){
			setDisabled(document, true);

			var fields = ['municipioEntrega.nmMunicipio'
						,'psReal'
						,'vlVolume'
						,'moedaValorMercadoria.idMoeda'
						,'vlMercadoria'
						,'psLiquido'
						,'municipioEntrega.idMunicipio'
						,'dsLocalEntrega'
						,'dsDadosMercadoria'
						,'dsDadosRemetente'
						,'dsDadosDestinatario'
						,'dsDadosConsignatario'
						,'dsNotificar'
						,'dsLocalEmissao'
						,'dsLocalCarregamento'];

			var objsToDisable = ['municipioEntrega.idMunicipio'
								,'dsLocalEntrega'
								,'dsDadosMercadoria'
								,'dsDadosRemetente'
								,'dsDadosDestinatario'
								,'dsDadosConsignatario'
								,'dsNotificar'
								,'dsLocalEmissao'
								,'dsLocalCarregamento'
								,'btnDocumentosAnexos'
								,'btnAduanas'
								,'btnObservacoes'
								,'btnServicosAdicionais'
								,'btnDespachantes'
								,'btnDimensoes'
								,'btnCalculoCRT'
								,'btnGastos'
								,'btnEmitir'
								,'btnDuplicar'
								];

			Snapshot.capture(fields);//Capitura o estado atual dos campos modificaveis que pode alterar o valor do calculo

			this.setIsDisableComponentRange(objsToDisable.concat(fields), false);
		}
		/************************************************************\
		*
		\************************************************************/
		,setModoVisualizacao : function(){
			setDisabled(document, true);
			this.setIsDisabledButtons(false);
			setDisabled('btnEmitir', true);
		}
		/************************************************************\
		*
		\************************************************************/
		,setIsDisableComponentRange : function(arr, isDisable){
			for(var k in arr) setDisabled(arr[k], isDisable);
		}
		/************************************************************\
		*
		\************************************************************/
		,setIsDisabledButtons : function(isDisabled){
			var objsToDisable = ['btnDocumentosAnexos'
								,'btnAduanas'
								,'btnObservacoes'
								,'btnServicosAdicionais'
								,'btnDespachantes'
								,'btnDimensoes'
								,'btnCalculoCRT'
								,'btnGastos'
								,'btnEmitir'
								,'btnDuplicar'];
			this.setIsDisableComponentRange(objsToDisable, isDisabled);
		}
		/************************************************************\
		*
		\************************************************************/
		,setDefaultOptions : function(){

			var objEmissao = getElement('municipioEmissao.nmMunicipio');
			var objCarregamento = getElement('municipioCarregamento.nmMunicipio');
			var objEntrega = getElement('municipioEntrega.nmMunicipio');
			var objDsEmissao = getElement('dsLocalEmissao');
			var objDsCarregamento = getElement('dsLocalCarregamento');
			var objDsEntrega = getElement('dsLocalEntrega');

			objEmissao.required = false;
			objCarregamento.required = false;
			objEntrega.required = false;

			objDsEmissao.required = 'true';
			objDsCarregamento.required = 'true';
			objDsEntrega.required = 'true';

			objDsEmissao.label = objEmissao.label;
			objDsCarregamento.label = objCarregamento.label;
			objDsEntrega.label = objEntrega.label;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj){
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		var strModo = '';
		

		if(data) {
			strModo = (data.isEditavel == 'S' ? ModosTela.ALTERACAO : ModosTela.VISUALIZACAO)
		} else {
			strModo = ModosTela.INCLUSAO;
		}
		ModosTela.setModo(strModo);
	}
	/************************************************************\
	*
	\************************************************************/
	function initWindow(eventObj){
		if(eventObj && eventObj.name == 'tab_click')
			ModosTela.setModo(ModosTela.INCLUSAO);
	}
	/************************************************************\
	*
	\************************************************************/
	function setDataToInitialStateWindow_cb(data, erros) {
		if (erros != undefined){
			alert(erros);
			return false;
		}

		if(data){
			setElementValue('filialByIdFilialOrigem.idFilial', data.filialUsuario.idFilial);
			setElementValue('filialByIdFilialOrigem.sgFilial', data.filialUsuario.sgFilial);
			setElementValue('filialByIdFilialOrigem.pessoa.idPessoa', data.filialUsuario.pessoa.idPessoa);
			setElementValue('filialByIdFilialOrigem.pessoa.nmFantasia', data.filialUsuario.pessoa.nmFantasia);
			setElementValue('nrCrt', data.nrCrt);
			setElementValue('tpSituacaoCrt.description', data.tpSituacaoCrt.description);
			setElementValue('dsTransportesSucessivos', setFormat('dsTransportesSucessivos', data.dsTransportesSucessivos));
			setElementValue('dhEmissao', setFormat('dhEmissao', data.dataAtual));
			setElementValue('dtCarregamento', setFormat('dtCarregamento', data.dataAtual));
			setElementValue('moedaValorMercadoria.idMoeda', data.moedaValorMercadoria.idMoeda);
			setElementValue('moedaValorTotalMercadoria.idMoeda', data.moedaValorTotalMercadoria.idMoeda);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad(){
		onPageLoad();
		document.getElementById("tpSituacao").masterLink = "true";
		ModosTela.setModo(ModosTela.INCLUSAO);
		var objMoeda = getElement('moedaValorTotalMercadoria.idMoeda');
		var objValTotMercadoria = getElement('vlTotalMercadoria');

		objMoeda.label = i18NLabel.getLabel('valorTotalMercadoria16');
		objValTotMercadoria.label = i18NLabel.getLabel('valorTotalMercadoria16');
		objMoeda.required = 'true';
		objValTotMercadoria.required = 'true';
	}
	/************************************************************\
	*
	\************************************************************/
	function storeInSession(objCaller){
		var arrFields = ['psReal'
						,'psLiquido'
						,'vlVolume'
						,'vlMercadoria'
						,'vlTotalMercadoria'
						,'qtVolumes'];
		if(validateTabScript(document.forms[0])
		   && NumberUtils.validateRangeMaiorZero(arrFields)
		   && validatePesos()){
			var args = buildFormBeanFromForm(document.forms[0]);
			var service = 'lms.expedicao.manterCRTAction.storeInSession'
			var sdo = createServiceDataObject(service, 'storeInSession', args);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function storeInSession_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}

		if(data && data != undefined) setElementValue('nrDiasPrevEntrega', data.nrDiasPrevEntrega);

		returnPopupsListener(WindowUtils.showCalculoCRT());

		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function duplicar(){
		var service = 'lms.expedicao.manterCRTAction.duplicar'
		var sdo = createServiceDataObject(service, 'duplicar', {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function duplicar_cb(data, error){
		if(error && error != undefined){
			alert(error);
			return false;
		}

		ModosTela.setModo(ModosTela.INCLUSAO, true);

		if(data && data != undefined){
			setElementValue('dtCarregamento', setFormat('dtCarregamento', data.dataAtual));
			resetValue('dsMoeda');
			resetValue('nrCrt');
			resetValue('dhEmissao');
			resetValue('nrDiasPrevEntrega');
			resetValue('dsAnexos');
			resetValue('dhInclusao');
			resetValue('usuarioByIdUsuarioInclusao.nmUsuario');
			resetValue('dhAlteracao');
			resetValue('usuarioByIdUsuarioAlteracao.nmUsuario');
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function returnPopupsListener(obj){//controla o retorno das telas de calculo e gastos

		var status = (obj.state ? obj.state : '');

		switch(status){
			case 'VISUALIZACAO':
			case 'CALCULO_SALVO':
				setDisabled('btnGastos', false);

				var dsMoeda = obj.sgMoeda ? obj.sgMoeda : '';
				dsMoeda += obj.dsSimbolo ? ' ' + obj.dsSimbolo : '';
				setElementValue('dsMoeda', dsMoeda);

				if(obj.dsSimbolo) setElementValue('moeda.dsSimbolo', obj.dsSimbolo);
				if(obj.sgMoeda) setElementValue('moeda.sgMoeda', obj.sgMoeda);

				if(ModosTela.modo == ModosTela.ALTERACAO && (obj && obj.vlCubagem && obj.vlCubagem != '')){
					setElementValue('vlVolume', setFormat('vlVolume', obj.vlCubagem));
				}
				setFocus('btnGastos', false);
				break;
			case 'CANCELAR_TUDO':
				ModosTela.setModo(ModosTela.INCLUSAO);
				break;
			case 'CRT_SALVO':
				setElementValue('nrCrt', obj.nrCrt);
				setElementValue('dhInclusao', setFormat('dhInclusao', obj.dhInclusao));
				setElementValue('usuarioByIdUsuarioInclusao.nmUsuario', obj.nmUsuarioInclusao);
				setElementValue('dhAlteracao', setFormat('dhAlteracao', obj.dhAlteracao));
				setElementValue('usuarioByIdUsuarioAlteracao.nmUsuario', obj.nmUsuarioAlteracao);
				setElementValue('idCtoInternacional', obj.idDoctoServico);

				ModosTela.setModo(ModosTela.ALTERACAO);
				break;
			case 'VOLTAR_DETALHAMENTO':
				setDisabled('btnGastos', true);
				break;
			default:
				alert("Retorno["+status+"] nao tratado.");
				setDisabled('btnGastos', true);
				setDisabled('btnEmitir', true);
				setDisabled('btnDuplicar', true);
				break;
		}
		return obj;
	}
	/************************************************************\
	*
	\************************************************************/
	var Snapshot = {
		objs : null
		,capture : function(arrNmObjs){
			this.objs = [];
			for(var k in arrNmObjs){
				var o = {};
				o.name = arrNmObjs[k];
				o.value = getElementValue(o.name);
				this.objs[this.objs.length] = o;
			}
		}
		,isModified : function(){
			for(var k in this.objs){
				if(this.objs[k].value != getElementValue(this.objs[k].name)){
					return true;
				}
			}
			return false;
		}
	};
	/************************************************************\
	*
	\************************************************************/
	function controlModifiedFields(objCaller){
		var modo = getElementValue("MODO_TELA");
		if(modo == ModosTela.ALTERACAO){
			var isDisable = Snapshot.isModified();
			controlDisableStoreButtons(isDisable);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function controlDisableStoreButtons(isDisable){
		var arrObjs = ['btnGastos'
					  ,'btnEmitir'
					  ,'btnDuplicar'];
		ModosTela.setIsDisableComponentRange(arrObjs, isDisable);
	}
	/************************************************************\
	*
	\************************************************************/
	var NumberUtils = {
		isMaiorZero : function(val){
			return val > 0;
		}
		,validateRangeMaiorZero : function(arr){
			for(var k in arr){
				var o  = getElement(arr[k]);
				var val = getElementValue(o);
				if(val != '' && !this.isMaiorZero(val)){
					alert('O campo \'' + o.label + '\' deve ser maior que zero.');
					setFocus(o, false);
					return false;
				}
			}
			return true;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function replicateData(){
		var idCliente = getElementValue('clienteByIdClienteDestinatario.idCliente');
		var nrIdentificacao = getElementValue('clienteByIdClienteDestinatario.pessoa.nrIdentificacao');
		var nmPessoa = getElementValue('clienteByIdClienteDestinatario.pessoa.nmPessoa');
		var text = getElementValue('dsDadosDestinatario');

		setElementValue('clienteByIdClienteConsignatario.idCliente', idCliente);
		setElementValue('clienteByIdClienteConsignatario.pessoa.nrIdentificacao', nrIdentificacao);
		setElementValue('clienteByIdClienteConsignatario.pessoa.nmPessoa', nmPessoa);
		setElementValue('dsDadosConsignatario', text);

		setElementValue('clienteByIdClienteRedespacho.idCliente', idCliente);
		setElementValue('clienteByIdClienteRedespacho.pessoa.nrIdentificacao', nrIdentificacao);
		setElementValue('clienteByIdClienteRedespacho.pessoa.nmPessoa', nmPessoa);
		setElementValue('dsNotificar', text);
	}
	/************************************************************\
	*
	\************************************************************/
	function validatePesos(){
		var pesoLiquido = getElementValue('psLiquido');
		var pesoBruto = getElementValue('psReal');

		if(stringToNumber(pesoLiquido) > stringToNumber(pesoBruto)) {
			alert(i18NLabel.getLabel('LMS-04024'));
			setFocus('psLiquido', false);
			return false;
		}

		return true;
	}
</script>