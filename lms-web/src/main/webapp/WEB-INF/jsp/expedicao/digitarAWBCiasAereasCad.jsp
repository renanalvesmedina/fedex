<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/digitarAWBCiasAereas" height="380" >
	
		<adsm:hidden property="statusAwbCancelado" value="true"/>
		<adsm:hidden property="dsJustificativaPrejuizo" />

		<adsm:lookup
			property="filial"
			service="lms.expedicao.digitarAWBCiasAereasAction.findFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			label="filial"
			size="3"
			required="false"
			minLengthForAutoPopUpSearch="3"
			exactMatch="true"
			maxLength="3"
			labelWidth="5%"
			picker="false"
			width="35%"
			disabled="true">
			<adsm:textbox
				dataType="text"
				property="filial.nmFantasia"
				size="32"
				maxLength="45"
				disabled="true"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="awb.dhEmissao"
			label="dataHoraEmissao"
			labelWidth="13%"
			width="20%"
			serializable="true"
			required="true"/>
			
		<adsm:combobox property="tpAwb" 
			label="finalidade" 
		    service="lms.expedicao.digitarAWBCiasAereasAction.findFinalidade"
		    optionProperty="value"
		    optionLabelProperty="description"
			autoLoad="true"
			onlyActiveValues="true"
			width="15%"
			labelWidth="8%"
			required="true"
			defaultValue="NO" 
			onchange="return habilitarCamposPorTipoAwb();"/>
			
		<!-- Dados do Expedidor INICIO -->
		<adsm:section
			caption="dadosExpedidor" />
		<adsm:hidden
			property="servico.tpModal"/>
		<adsm:lookup
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			service="lms.expedicao.digitarAWBCiasAereasAction.findCliente" 
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			size="18"
			maxLength="20"
			onchange="return remetenteChange();"
			width="43%"
			labelWidth="11%"
			allowInvalidCriteriaValue="true"
			required="true"
			onDataLoadCallBack="remetente"
			label="expedidor"
			onPopupSetValue="remetentePopup" >
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteRemetente.tpCliente"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteRemetente.nrIdentificacao"/>
			<adsm:hidden
				property="clienteByIdClienteRemetente.nrIdentificacao"
				serializable="false"/>
			<adsm:hidden
				property="clienteByIdClienteRemetente.tpCliente"/>
			<adsm:hidden
				serializable="true"
				property="awb.idClienteExpedidor"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="28"
				maxLength="30" 
				serializable="false"
				disabled="true"/>
		</adsm:lookup>
		<td colspan="12">
		<adsm:button style="FmLbSection" id="clienteByIdClienteRemetenteButton" caption="incluir" onclick="WindowUtils.showCadastroCliente('clienteByIdClienteRemetente');" disabled="true"/>
		</td>
		<adsm:hidden
			property="clienteByIdClienteRemetente.ie.id"
			serializable="false"/>
		<adsm:combobox
			label="ie"
			property="clienteByIdClienteRemetente.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="20%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>
		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteRemetente.endereco.dsEndereco"
			label="endereco"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteRemetente.endereco.nrEndereco"
			label="numero"
			size="5" 
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteRemetente.endereco.dsComplemento"
			label="complemento"
			size="18"
			maxLength="40"
			labelWidth="10%"
			width="20%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteRemetente.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			property="clienteByIdClienteRemetente.endereco.siglaDescricaoUf"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="12"
			property="clienteByIdClienteRemetente.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="20%"
			size="12"
			disabled="true"/>
		<adsm:hidden
			property="clienteByIdClienteRemetente.endereco.idMunicipio"/>
		<adsm:hidden
			property="clienteByIdClienteRemetente.endereco.idUnidadeFederativa"/>
		<!-- Dados do Expedidor FIM -->
		
		<!-- Dados do Destinatário INICIO -->
		<adsm:section
			caption="dadosDestinatario"/>
		<adsm:lookup
			label="destinatario"
			property="clienteByIdClienteDestinatario" 
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarAWBCiasAereasAction.findCliente" 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			allowInvalidCriteriaValue="true"
			onchange="return destinatarioChange();"
			onDataLoadCallBack="destinatario"
			size="18"
			maxLength="20"
			onPopupSetValue="destinatarioPopup"
			required="true"
			width="43%"
			labelWidth="11%">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteDestinatario.nrIdentificacao"/>
			<adsm:propertyMapping modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteDestinatario.tpCliente"/>
			<adsm:hidden
				property="clienteByIdClienteDestinatario.nrIdentificacao"
				serializable="false"/>
			<adsm:hidden
				property="clienteByIdClienteDestinatario.tpCliente"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
				size="28"
				maxLength="30"
				serializable="false"
				disabled="true"/>
		</adsm:lookup>
		<td colspan="12">
		<adsm:button
			style="FmLbSection"
			id="clienteByIdClienteDestinatarioButton"
			caption="incluir"
			onclick="WindowUtils.showCadastroCliente('clienteByIdClienteDestinatario');"
			disabled="true"/>
		</td>
		<adsm:hidden
			property="clienteByIdClienteDestinatario.ie.id"
			serializable="false"/>
		<adsm:combobox
			label="ie"
			property="clienteByIdClienteDestinatario.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="20%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>
		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteDestinatario.endereco.dsEndereco"
			label="endereco" 
			maxLength="100"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteDestinatario.endereco.nrEndereco"
			label="numero"
			size="5" 
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteDestinatario.endereco.dsComplemento"
			label="complemento"
			size="18" 
			maxLength="40"
			labelWidth="10%"
			width="20%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteDestinatario.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false" />
		<adsm:textbox
			property="clienteByIdClienteDestinatario.endereco.siglaDescricaoUf"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="12"
			property="clienteByIdClienteDestinatario.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="20%"
			size="12"
			disabled="true"/>
		<adsm:hidden
			property="clienteByIdClienteDestinatario.endereco.idMunicipio"/>
		<adsm:hidden
			property="clienteByIdClienteDestinatario.endereco.idUnidadeFederativa"/>
		<!-- Dados do Destinatário FIM -->

		<!-- Dados do AWB INICIO -->
		<adsm:section
			caption="dadosAWB"/>

		<adsm:hidden
			property="flag"
			serializable="false"
			value="01"/>
			
		<adsm:hidden property="empresa.idEmpresa"/>

		<adsm:combobox
			property="ciaFilialMercurio.idCiaFilialMercurio"
			optionLabelProperty="empresa.pessoa.nmPessoa"
		 	optionProperty="idCiaFilialMercurio"
		 	service="lms.expedicao.digitarAWBCiasAereasAction.findCiaAerea"
		 	label="ciaAerea"
		 	required="true"
		 	boxWidth="204"
		 	width="33%"
		 	onchange="return preencheFilialTomador_OnChange(this);"
		 	labelWidth="17%"
		 	serializable="true">

		 	<adsm:propertyMapping
		 		relatedProperty="empresa.idEmpresa"
		 		modelProperty="empresa.idEmpresa"/>
		 </adsm:combobox>
		
		<adsm:textbox labelWidth="17%" width="28%"
					 label="numeroAWB"	
					 dataType="integer"
					 maxLength="14"
				     property="numero"
					 size="13"
					 required="true" />
		
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarAWBCiasAereasAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeOrigem"
			size="3"
			required="true"
			maxLength="3"
			labelWidth="17%"
			width="33%">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="23"
				maxLength="45"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup action="/municipios/manterAeroportos" 
			service="lms.expedicao.digitarAWBCiasAereasAction.findAeroporto" 
			dataType="text"
			required="true"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto" 
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			size="3"
			maxLength="3" 
			labelWidth="17%"
			width="28%">
			<adsm:hidden
				property="aeroportoByIdAeroportoDestino.filial.idFilial"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="idFilial"
				relatedProperty="aeroportoByIdAeroportoDestino.filial.idFilial"/>
			<adsm:textbox 
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="23"
				maxLength="45"
				disabled="true"/>
		</adsm:lookup>

		<adsm:textbox label="pesoReal"   property="psTotal"  dataType="weight" labelWidth="17%" width="33%" size="9"  maxLength="18" mask="##,##0.000"                   unit="kg" required="true"/>
		<adsm:textbox label="pesoCubado" property="psCubado" dataType="weight" labelWidth="17%" width="28%" size="10" maxLength="11" mask="###,##0.000" minValue="0.001" unit="kg" required="true"/>

		<adsm:complement
			label="valorFrete"
			required="true"
			labelWidth="17%"
			separator="branco"
			width="33%" >
			<adsm:combobox
				property="awb.idMoeda"
				service="lms.expedicao.digitarAWBCiasAereasAction.findMoedaCombo"
				optionLabelProperty="dsSimbolo"
				optionProperty="idMoeda"
				boxWidth="85"
				onDataLoadCallBack="verificaMoedaPais">
				<adsm:textbox
					dataType="currency"
					maxLength="18"
					property="awb.vlFrete"
					serializable="true"
					size="10" 
					onchange="return setValorIcms();"/>
			</adsm:combobox>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			property="awb.dsVooPrevisto"
			maxLength="60"
			size="15"
			label="vooPrevisto"
			labelWidth="17%"
			width="28%"
			required="true"/>

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhPrevistaSaida"
			label="dataPrevistaSaida"
			labelWidth="17%"
			width="33%"
			serializable="true"
			required="true"/>

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhPrevistaChegada"
			label="dataPrevistaChegada"
			labelWidth="17%"
			biggerThan="dhPrevistaSaida"
			width="28%"
			serializable="true"
			required="true"/>

		<adsm:combobox label="aliquotaIcmsPerc" property="pcAliquotaIcms"
					   domain="DM_ALIQUOTA_ICMS_AWB" 
					   labelWidth="17%" width="33%"
					   required="true" renderOptions="true"
					   onchange="return setValorIcms();"/>
		
		<adsm:textbox label="vlIcms" property="vlIcms" dataType="currency" labelWidth="17%" width="28%" required="true" disabled="true"/>

		<adsm:textbox label="qtdeVolumes"  property="qtVolumes"  dataType="integer" labelWidth="17%" width="33%" size="5"  maxLength="5" required="true"/>

		<adsm:combobox property="tpLocalEmissao" 
			label="localEmissao" 
		    service="lms.expedicao.digitarAWBCiasAereasAction.findLocalEmissao"
		    optionProperty="value" 
		    optionLabelProperty="description"
			autoLoad="true" onlyActiveValues="true" 
			width="28%" 
			labelWidth="17%" 
			defaultValue="C" />	

		<adsm:textbox
			dataType="integer"
			property="awb.nrChave"
			onchange="return nrChaveChange();"
			maxLength="44"
			size="50"
			label="chaveAWBe"
			required="true"
			labelWidth="17%"
			width="50%"/>
			
		 <adsm:combobox label="awbSubstituido" property="tpStatusAwb"
					   domain="DM_LOOKUP_AWB" 
					   labelWidth="17%" width="50%"
					   defaultValue="E" renderOptions="true" disabled="true">
        
			<adsm:lookup property="ciaFilialMercurio.empresa" 
						 idProperty="idEmpresa"
						 dataType="text"
						 criteriaProperty="sgEmpresa"
				 		 criteriaSerializable="true"
						 service="lms.expedicao.digitarAWBCiasAereasAction.findLookupSgCiaAereaAwbSustituido"
						 action="" 	
						 size="3" maxLength="3"						 
					 	picker="false">
			</adsm:lookup>

	        <adsm:lookup dataType="integer" size="13" maxLength="11" 
	        	property="awb"
	        	idProperty="idAwb"
	        	criteriaProperty="nrAwb"
	        	criteriaSerializable="true"
	 			service="lms.expedicao.digitarAWBCiasAereasAction.findLookupAwbSustituido"
				action="expedicao/consultarAWBs"
				onDataLoadCallBack="awbOnDataLoadCallBack"
				onPopupSetValue="findAwb_cb">
				
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
				<adsm:propertyMapping modelProperty="statusAwbCancelado" criteriaProperty="statusAwbCancelado" disable="true" />
	        </adsm:lookup>
	    </adsm:combobox>	
			
		<adsm:lookup action="" 
			service="lms.expedicao.digitarAWBCiasAereasAction.validateLiberacaoAwbComplementar" 
			dataType="text"
			required="true"
			property="dsSenha"
			idProperty="idLiberacaoAwb" 
			criteriaProperty="dsSenha"
			label="codigoLiberacaoAWBComplementar"
			maxLength="8"
			size="12"
			labelWidth="28%"
			width="22%"
			picker="false">
			<adsm:propertyMapping modelProperty="idEmpresa" criteriaProperty="empresa.idEmpresa" />
			<adsm:propertyMapping modelProperty="nrAwb" relatedProperty="awbOriginal.nrAwb" />
		</adsm:lookup>
			
		<adsm:textbox
			label="nrAwbaComplementar"
			dataType="text"
			property="awbOriginal.nrAwb"
			labelWidth="17%"
			width="33%"
			size="20"
			disabled="true"
			/>

		<adsm:textarea
			maxLength="500"
			property="awb.obAwb"
			label="observacao"
			columns="100"
			rows="2"
			labelWidth="17%"
			width="75%"/>

		<adsm:buttonBar lines="1">
			<adsm:button
 				caption="incluirExcluirCTRC" 
				id="btnIncluirExcluirCTRC"
				onclick="WindowUtils.showIncluirExcluirCTRC();"
				disabled="false" />
			<adsm:button 
				caption="limpar"
				disabled="false"
				id="btnLimpar"
				onclick="limpar()"/>
			<adsm:button
				caption="salvar"
				id="storeButton"
				disabled="false"
				onclick="store()"/>
		</adsm:buttonBar>
		<!-- Dados do AWB FIM -->
		
		<!-- Dados do Tomador do servico INICIO -->
		<adsm:section
			caption="dadosTomadorServico"/>
		<adsm:lookup
			label="tomador"
			property="clienteByIdClienteTomador" 
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarAWBCiasAereasAction.findCliente" 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			allowInvalidCriteriaValue="true"
			onchange="return tomadorChange();"
			onDataLoadCallBack="tomador"
			size="18"
			maxLength="20"
			onPopupSetValue="tomadorPopup"
			required="true"
			width="43%"
			labelWidth="11%">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteTomador.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteTomador.nrIdentificacao"/>
			<adsm:propertyMapping modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteTomador.tpCliente"/>
			<adsm:hidden
				property="clienteByIdClienteTomador.nrIdentificacao"
				serializable="false"/>
			<adsm:hidden
				property="clienteByIdClienteTomador.tpCliente"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteTomador.pessoa.nmPessoa" 
				size="28"
				maxLength="30"
				serializable="false"
				disabled="true"/>
		</adsm:lookup>
		<td colspan="12">
		<adsm:button
			style="FmLbSection"
			id="clienteByIdClienteTomadorButton"
			caption="incluir"
			onclick="WindowUtils.showCadastroCliente('clienteByIdClienteTomador');"
			disabled="true"/>
		</td>
		<adsm:hidden
			property="clienteByIdClienteTomador.ie.id"
			serializable="false"/>
		<adsm:combobox
			label="ie"
			property="clienteByIdClienteTomador.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="20%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>
		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteTomador.endereco.dsEndereco"
			label="endereco" 
			maxLength="100"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteTomador.endereco.nrEndereco"
			label="numero"
			size="5" 
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteTomador.endereco.dsComplemento"
			label="complemento"
			size="18" 
			maxLength="40"
			labelWidth="10%"
			width="20%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteTomador.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false" />
		<adsm:textbox
			property="clienteByIdClienteTomador.endereco.siglaDescricaoUf"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>
		<adsm:textbox
			dataType="text"
			maxLength="12"
			property="clienteByIdClienteTomador.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="20%"
			size="12"
			disabled="true"/>
		<adsm:hidden
			property="clienteByIdClienteTomador.endereco.idMunicipio"/>
		<adsm:hidden
			property="clienteByIdClienteTomador.endereco.idUnidadeFederativa"/>
			
		<adsm:label key="espacoBranco" width="56%" />
		<adsm:textbox dataType="integer" property="clienteByIdClienteTomador.nrContaCorrente" label="contaCiaAerea" 
			maxLength="11" size="12" labelWidth="20%" width="15%" required="true" />	
			
		<!-- Dados do Tomador do servico FIM -->
		
		<adsm:i18nLabels>
			<adsm:include key="dvAwb"/>
			<adsm:include key="valorFrete"/>
			<adsm:include key="LMS-00054"/>
		</adsm:i18nLabels>
	</adsm:form>
</adsm:window>
<script src="../lib/expedicao.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
	
	function habilitarCamposPorTipoAwb() {
		if ("CO" == getElementValue("tpAwb")) {
			setDisabled("btnIncluirExcluirCTRC", true);
			setDisabled("dsSenha.dsSenha", false);
			getElement("dsSenha.dsSenha").required = true;
			desablitaCampoAwbSubstituto(true);
		} else {
			setDisabled("btnIncluirExcluirCTRC", false);
			desablitaCampoAwbSubstituto(false);
			getElement("dsSenha.dsSenha").required = false;
			setDisabled("dsSenha.dsSenha", true);
			resetValue("dsSenha.dsSenha");
			resetValue("dsSenha.idLiberacaoAwb");
			resetValue("awbOriginal.nrAwb");
			
		}
	}
	
	function desablitaCampoAwbSubstituto(disabled) {
		setDisabled("ciaFilialMercurio.empresa.sgEmpresa", disabled);
		setDisabled("awb.nrAwb", disabled);
		setDisabled("awb.idAwb", disabled)
	}
	
	function habilitarCamposViaConsolidacao() {
		setElementValue("tpAwb", "NO");
		setDisabled("tpAwb", true);
		habilitarCamposPorTipoAwb();
	}

	function initWindow(obj){
		habilitaBotoes();
	}

	function limpar() {
		remove();
		clearIes();
		newButtonScript(document);
		initFields();
	}
	/************************************************************\
	*
	\************************************************************/
	function store_cb(data, erros){
		if (erros != undefined){
			alert(erros);
			var erro = erros.substring(0,9);
			if(erro == "LMS-04513"){
				showJustificarAwbPrejuizo();
				if(getElementValue("dsJustificativaPrejuizo") != null && getElementValue("dsJustificativaPrejuizo") != ""){
					store();
				}
			}
			return false;
		} else {
			showSuccessMessage();

			/* Verifica se houve Exceção no envio do e-mail */
			if (data.exception != undefined) {
				alert(data.exception);
			}
			limpar();
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function store(){
		var service = 'lms.expedicao.digitarAWBCiasAereasAction.store';
		storeButtonScript(service, 'store', document.forms[0]);
	}
	/************************************************************\
	*
	\************************************************************/
	function remove() {
		var service = "lms.expedicao.digitarAWBCiasAereasAction.remove";
		var sdo = createServiceDataObject(service, "remove", {});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function remove_cb(data, erros) {
		if (erros != undefined){
			alert(erros);
			return false;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad_cb() {
		var url = new URL(document.location.href);
		var nrChave = url.parameters.nrChave;

		if ( nrChave != undefined && nrChave != "" ){
			setElementValue("awb.nrChave", nrChave);
		}
		
		initFields();
	}
	/************************************************************\
	*
	\************************************************************/
	function initFields() {
		populateWindow();
		setDisabled("btnIncluirExcluirCTRC", false);
		setDisabled("btnLimpar", false);
		setDisabled("storeButton", false);
		setDisabled("tpAwb", false);
		habilitarCamposPorTipoAwb();
		setMoedaPadrao();
	}
	/************************************************************\
	*
	\************************************************************/
	function populateWindow_cb(data, error){
		if (error != undefined){
			alert(error);
			return false;
		}
		setElementValue('filial.idFilial', data.filial.idFilial);
		setElementValue('filial.sgFilial', data.filial.sgFilial);
		setElementValue('filial.nmFantasia', data.filial.pessoa.nmFantasia);
		habilitaBotoes();
	}
	/************************************************************\
	*
	\************************************************************/
	function populateWindow(){
		var service = 'lms.expedicao.digitarAWBCiasAereasAction.getData';
		var sdo = createServiceDataObject(service, 'populateWindow', {});
		xmit({serviceDataObjects:[sdo]});
	}
	//FUNCOES UTILITARIAS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	var idMoedaPadrao;
	function verificaMoedaPais_cb(dados) {
		awb_idMoeda_cb(dados);
		if(dados) {
			for(var i = 0; i < dados.length; i++) {
				var indUtil = getNestedBeanPropertyValue(dados[i], "blIndicadorMaisUtilizada");
				if(indUtil == true || indUtil == 'true') {
					idMoedaPadrao = getNestedBeanPropertyValue(dados[i], "idMoeda");
					setMoedaPadrao();
					break;
				}
			}
		}
	}

	function setMoedaPadrao() {
		if(idMoedaPadrao) {
			setElementValue("awb.idMoeda", idMoedaPadrao);
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
	
			for(var a in o) saida += c + '.' + a + '='+ o[a] + '\n';
	
			return saida;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	var WindowUtils = {
		Options : {
			unardorned : 'no'
			,scroll : 'auto'
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
		,showIncluirExcluirCTRC : function() {
			return this.showModal('expedicao/digitarAWBCiasAereasCTRCs.do?cmd=main&millis=' + new Date().getTime());
		}
		/************************************************************\
		*
		\************************************************************/
		,showCadastroCliente : function(tipo){
			var data = this.showModalBySize('expedicao/cadastrarClientes.do?cmd=main&origem=exp', window, 750, 300);
			if(data) {
				cliente_cb(data, tipo);
				if (tipo == 'clienteByIdClienteRemetente') {
					findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoRemetente");
				} else if (tipo == 'clienteByIdClienteDestinatario') {
					findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoDestinatario");
				} else if (tipo == 'clienteByIdClienteTomador') {
					findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoTomador");
				}
			}
		}
	}
	/************************************************************\
	*
	\************************************************************/
	
	function showJustificarAwbPrejuizo() {
		showModalDialog('expedicao/justificarInclusaoAWBPrejuizo.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:490px;dialogHeight:160px;');
	}
	
	function cadastrarCliente(tipo){
		var data = WindowUtils.showModalBySize(urlBase + '/expedicao/cadastrarClientes.do?cmd=main&origem=exp', window, 750, 300);
		if(data) {
			cliente_cb(data, tipo);
			if (tipo == 'clienteByIdClienteRemetente') {
				findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoRemetente");
			} else if (tipo == 'clienteByIdClienteDestinatario') {
				findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoDestinatario");
			} else if (tipo == 'clienteByIdClienteTomador') {
				findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoTomador");
			}
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function firePopupChangeModel(data, caller){
		var criteria = {
			pessoa: {
				nrIdentificacao : data
			}
		}
		var service = "lms.expedicao.digitarAWBCiasAereasAction.findCliente";
		var sdo = createServiceDataObject(service, "firePopupChange" + caller + "Model", criteria);
		xmit({serviceDataObjects:[sdo]});
	}
	//FUNCOES UTILITARIAS<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//FUNCOES REMETENTE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	function remetentePopup(data) {

		if (data == undefined) {
			resetValue("clienteByIdClienteRemetente.idCliente");
			setDisabled("clienteByIdClienteRemetenteButton", false);
			clearIeRemetente();
			setFocus(getElement("clienteByIdClienteRemetenteButton"), false);
		} else {
			// setTimeout("firePopupChangeModel('" + data.pessoa.nrIdentificacao + "', 'Remetente')", 500);
			findDadosCliente(data.pessoa.nrIdentificacao, "Remetente");
			setDisabled("clienteByIdClienteRemetenteButton", true);
			// findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoRemetente");
			var objNotas = document.getElementById("quantidadeNotasFiscais");
		}
		resetValue("clienteByIdClienteRemetente.ie.id");
		setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);

		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function firePopupChangeRemetenteModel_cb(data){
		if(data != undefined && data.length > 0){
			setElementValue("clienteByIdClienteRemetente.nrInscricaoEstadual", data[0].ie.nrInscricaoEstadual);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function findEndereco(idPessoa, tpPessoa, callback) {
		var sdo = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasAction.findEndereco", callback, {idPessoa:idPessoa, tpPessoa:tpPessoa});
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function enderecoRemetente_cb(data, erros) {
		if (erros) {
			alert(erros);
			return false;
		}
		setEndereco(data, "clienteByIdClienteRemetente");
	}
	/************************************************************\
	*
	\************************************************************/
	function setEndereco(dados, tipo) {
		var nmTarget = tipo + ".endereco";
		var sEV = setElementValue;
		if(dados && dados.pessoa && dados.pessoa.endereco) {
			var objEnd = dados.pessoa.endereco;
			sEV(nmTarget + ".nrCep", objEnd.nrCep);
			sEV(nmTarget + ".nmMunicipio", objEnd.nmMunicipio);
			sEV(nmTarget + ".idMunicipio", objEnd.idMunicipio);
			sEV(nmTarget + ".idUnidadeFederativa", objEnd.idUnidadeFederativa);
			sEV(nmTarget + ".siglaDescricaoUf", objEnd.sgUnidadeFederativa);
			sEV(nmTarget + ".dsComplemento", objEnd.dsComplemento);
			sEV(nmTarget + ".nrEndereco", objEnd.nrEndereco);
			sEV(nmTarget + ".dsEndereco", objEnd.dsEndereco);

			if(tipo == "clienteByIdClienteRemetente") findAeroportoOrigem();
			else if (tipo != "clienteByIdClienteTomador") findAeroportoDestino(objEnd.idMunicipio, objEnd.nrCep, dados.idCliente);
		} else if(dados && dados.endereco && dados.endereco.dsEndereco) {
			var objEnd = dados.endereco;
			sEV(nmTarget + ".nrCep", objEnd.nrCep);
			sEV(nmTarget + ".nmMunicipio", objEnd.nmMunicipio);
			sEV(nmTarget + ".idMunicipio", objEnd.idMunicipio);
			sEV(nmTarget + ".idUnidadeFederativa", objEnd.idUnidadeFederativa);
			sEV(nmTarget + ".siglaDescricaoUf", objEnd.sgUnidadeFederativa);
			sEV(nmTarget + ".dsComplemento", objEnd.dsComplemento);
			sEV(nmTarget + ".nrEndereco", objEnd.nrEndereco);
			sEV(nmTarget + ".dsEndereco", objEnd.dsEndereco);
		} else {
			sEV(nmTarget + ".nrCep", "");
			sEV(nmTarget + ".nmMunicipio", "");
			sEV(nmTarget + ".idMunicipio", "");
			sEV(nmTarget + ".idUnidadeFederativa", "");
			sEV(nmTarget + ".siglaDescricaoUf", "");
			sEV(nmTarget + ".dsComplemento", "");
			sEV(nmTarget + ".nrEndereco", "");
			sEV(nmTarget + ".dsEndereco", "");
			resetAeroporto(tipo);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function resetAeroporto(tipo) {
		if(tipo == "clienteByIdClienteRemetente")
			resetValue("aeroportoByIdAeroportoOrigem.idAeroporto");
		else resetValue("aeroportoByIdAeroportoDestino.idAeroporto");
	}
	/************************************************************\
	*
	\************************************************************/
	function findAeroportoOrigem(){

		if(getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto") == "") {
			var service = "lms.expedicao.digitarAWBCiasAereasAction.findAeroportoOrigem";
			var sdo = createServiceDataObject(service, "findAeroportoOrigem");
			xmit({serviceDataObjects:[sdo]});	
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function findAeroportoOrigem_cb(data, error) {
		setAeroporto(data, "aeroportoByIdAeroportoOrigem");
	}
	/************************************************************\
	*
	\************************************************************/
	function setAeroporto(data, tipo) {
		setElementValue(tipo + ".idAeroporto", data.idAeroporto);
		setElementValue(tipo + ".sgAeroporto", data.sgAeroporto);
		setElementValue(tipo + ".pessoa.nmPessoa", data.nmAeroporto);
	}
	/************************************************************\
	*
	\************************************************************/
	function remetenteChange(){
		var nrId = getElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
		if(nrId == getElementValue("clienteByIdClienteRemetente.nrIdentificacao")){
			setDisabled("clienteByIdClienteRemetenteButton", true);
			return true;
		}
		if(nrId == "") {
			setEndereco(undefined, "clienteByIdClienteRemetente");
			resetValue("clienteByIdClienteRemetente.idCliente"); 
			setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true); 
			setIe("Remetente");
		}
		return clienteByIdClienteRemetente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function remetente_cb(data) {
		if(data == undefined || data.length == 0){
			var nrId = getElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteRemetente.idCliente");
			setElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteRemetenteButton", false);
			clearIeRemetente();
			setFocus(document.getElementById("clienteByIdClienteRemetenteButton"), false);
		} else {
			setDisabled("clienteByIdClienteRemetenteButton", true);
			setIe("Remetente", data);
		}
		setEndereco(data[0], "clienteByIdClienteRemetente");
		//setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);
		resetValue("clienteByIdClienteRemetente.ie.id");
		return clienteByIdClienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);
	}
	//FUNCOES REMETENTE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//FUNCOES DESTINATARIO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	function destinatarioChange() {
		var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		if(nrId == getElementValue("clienteByIdClienteDestinatario.nrIdentificacao")){
			setDisabled("clienteByIdClienteDestinatarioButton", true);
			return true;
		}
		if(nrId == "") {
			setEndereco(undefined, "clienteByIdClienteDestinatario");
			resetValue("clienteByIdClienteDestinatario.idCliente"); 
			setDisabled("clienteByIdClienteDestinatario.idInscricaoEstadual", true); 
			setIe("Destinatario");
		}
		return clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function firePopupChangeDestinatarioModel_cb(data){
		if(data != undefined && data.length > 0){
			setElementValue("clienteByIdClienteDestinatario.nrInscricaoEstadual", data[0].ie.nrInscricaoEstadual);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function destinatarioPopup(data) {
		if(data == undefined){
			var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteDestinatarioButton", false);
			clearIeDestinatario();
			setFocus(document.getElementById("clienteByIdClienteDestinatarioButton"), false);
		} else {
			findDadosCliente(data.pessoa.nrIdentificacao, "Destinatario");
			setDisabled("clienteByIdClienteDestinatarioButton", true);
			findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoDestinatario");
		}
		resetValue("clienteByIdClienteDestinatario.ie.id");
		setDisabled("clienteByIdClienteDestinatario.idInscricaoEstadual", true);
		

		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function enderecoDestinatario_cb(data, erros) {
		if (erros) {
			alert(erros);
			return false;
		}
		setEndereco(data, "clienteByIdClienteDestinatario");
	}

	/************************************************************\
	*
	\************************************************************/
	function findAeroportoDestino(idMunicipio, nrCep, idCliente) {
		var tpModal = getElementValue("servico.tpModal");
		if(tpModal == "A" && getElementValue("aeroportoByIdAeroportoDestino.idAeroporto") == "") {
				var idServico = getElementValue("servico.idServico");
				var parameters = {idMunicipio:idMunicipio, idCliente:idCliente, idServico:idServico, nrCep:nrCep};
				var service = "lms.expedicao.digitarAWBCiasAereasAction.findAeroportoDestino";
				var sdo = createServiceDataObject(service, "findAeroportoDestino", parameters);
				xmit({serviceDataObjects:[sdo]}); 
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function destinatario_cb(data) {
		if(data == undefined || data.length == 0){
			var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteDestinatarioButton", false);
			clearIeDestinatario();
			setFocus(document.getElementById("clienteByIdClienteDestinatarioButton"), false);
		} else {
			setDisabled("clienteByIdClienteDestinatarioButton", true);
			setIe("Destinatario", data);
		}
		var r = clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
		setEndereco(data[0], "clienteByIdClienteDestinatario");
		//setDisabled("clienteByIdClienteDestinatario.idInscricaoEstadual", true);
		resetValue("clienteByIdClienteDestinatario.ie.id");
		return r;
	}
	//FUNCOES DESTINATARIO<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//FUNCOES TOMADOR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	function tomadorChange() {
		var nrId = getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao");
		if(nrId == getElementValue("clienteByIdClienteTomador.nrIdentificacao")){
			setDisabled("clienteByIdClienteTomadorButton", true);
			return true;
		}
		if(nrId == "") {
			setEndereco(undefined, "clienteByIdClienteTomador");
			resetValue("clienteByIdClienteTomador.idCliente"); 
			setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true); 
			setIe("Tomador");
		}
		return clienteByIdClienteTomador_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function firePopupChangeTomadorModel_cb(data){
		if(data != undefined && data.length > 0){
			setElementValue("clienteByIdClienteTomador.nrInscricaoEstadual", data[0].ie.nrInscricaoEstadual);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function tomadorPopup(data) {
		if(data == undefined) {
			var nrId = getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteTomador.idCliente");
			setElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteTomadorButton", false);
			clearIeTomador();
			setFocus(document.getElementById("clienteByIdClienteTomadorButton"), false);
		} else {
			findDadosCliente(data.pessoa.nrIdentificacao, "Tomador");
			setDisabled("clienteByIdClienteTomadorButton", true);
			// findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoTomador");
		}
		resetValue("clienteByIdClienteTomador.ie.id");
		setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true);
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function enderecoTomador_cb(data, erros) {
		if (erros) {
			alert(erros);
			return false;
		}
		setEndereco(data, "clienteByIdClienteTomador");
	}

	/************************************************************\
	*
	\************************************************************/
	function tomador_cb(data) {
		if(data == undefined || data.length == 0){
			var nrId = getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteTomador.idCliente");
			setElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteTomadorButton", false);
			clearIeTomador();
			setFocus(document.getElementById("clienteByIdClienteTomadorButton"), false);
		} else {
			setDisabled("clienteByIdClienteTomadorButton", true);
			setIe("Tomador", data);
		}
		var r = clienteByIdClienteTomador_pessoa_nrIdentificacao_exactMatch_cb(data);
		setEndereco(data[0], "clienteByIdClienteTomador");
		//setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true);
		resetValue("clienteByIdClienteTomador.ie.id");
		return r;
	}
	//FUNCOES TOMADOR<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	function cliente_cb(dados, tipo) {
		if(dados != undefined){
			setElementValue(tipo + ".idCliente", dados.idCliente);
			setElementValue(tipo + ".pessoa.nrIdentificacao", dados.pessoa.nrIdentificacao);
			setElementValue(tipo + ".nrIdentificacao", dados.pessoa.nrIdentificacao);
			setElementValue(tipo + ".tpCliente", "E");
			setElementValue(tipo + ".pessoa.nmPessoa", dados.pessoa.nmPessoa);
			setElementValue(tipo + ".ie.id", dados.inscricaoEstadual.idInscricaoEstadual);
			setEndereco(dados, tipo);
			setDisabled(tipo + "Button", true);
			notifyElementListeners({e:document.getElementById(tipo + ".idCliente")});
		}
	}
	
	// FUNCOES AUXILIARES<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	function setIe(tipo, data) {
		if (data) {
			if (data[0].ie != undefined) {
				eval("clienteByIdCliente"+tipo+"_idInscricaoEstadual_cb(data[0].ie)");
				var idInscricaoEstadualPadrao = getIdInscricaoEstadualPadrao(data[0].ie);
				setElementValue("clienteByIdCliente"+tipo+".idInscricaoEstadual", idInscricaoEstadualPadrao);
				setDisabled("clienteByIdCliente"+tipo+".idInscricaoEstadual", !(data[0].ie.length > 1));
			} else {
				eval("clearIe"+tipo+"()");
			}
		} else {
			eval("clearIe"+tipo+"()");
		}
	}
	
	function getIdInscricaoEstadualPadrao(ies) {
		for (var i = 0; i < ies.length; i++) {
			var inscricaoEstadual = ies[i].inscricaoEstadual;
			if (inscricaoEstadual.blIndicadorPadrao == "true") {
				return inscricaoEstadual.idInscricaoEstadual;
			}
		}
		return 0;
	}
	
	function clearIes() {
		clearIeRemetente();
		clearIeDestinatario();
		clearIeTomador();
	}
	
	function clearIeRemetente() {
		clearElement("clienteByIdClienteRemetente.idInscricaoEstadual");
		setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);
	}
	
	function clearIeDestinatario() {	
		clearElement("clienteByIdClienteDestinatario.idInscricaoEstadual");
		setDisabled("clienteByIdClienteDestinatario.idInscricaoEstadual", true);
	}

	function clearIeTomador() {	
		clearElement("clienteByIdClienteTomador.idInscricaoEstadual");
		setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true);
	}

	function findDadosCliente(nrIdentificacao, tipo) {
		var sdo = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasAction.findCliente", tipo.toLowerCase(), {pessoa:{nrIdentificacao:nrIdentificacao}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function nrChaveChange(){
		var sdo = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasAction.validateChave","nrChaveChange", {nrChave:getElementValue("awb.nrChave")});
		xmit({serviceDataObjects:[sdo]});
		return true;
	}
	function nrChaveChange_cb(data, error){
		if (error) {
			alert(error);
			setFocus(document.getElementById("awb.nrChave"), true);
			document.getElementById("awb.nrChave").changed = true;
			return false;
		}
	}
	
	//Sera utilizada pela tela de consolidacao
	//de cargas para passagem de dados via javascript
	function saveDataFromConsolidacaoCargas(data) {
		setAcumuladores(data);
		getDadosFilial();
		desabilitaDadosExpedidor();
		desabilitaDadosDestinatario();
		habilitaBotoes();
		if(data.aeroporto.idAeroporto != "") {
			setElementValue("aeroportoByIdAeroportoDestino.idAeroporto", data.aeroporto.idAeroporto);
			setElementValue("aeroportoByIdAeroportoDestino.sgAeroporto", data.aeroporto.sgAeroporto);
			setElementValue("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", data.aeroporto.pessoa.nmPessoa);
		}
		habilitarCamposViaConsolidacao();
	}
	
	function getDadosFilial(){
		var service = "lms.expedicao.digitarAWBCiasAereasAction.findDadosFilial";
		var sdo = createServiceDataObject(service, "getDadosFilial");
		xmit({serviceDataObjects:[sdo]});	
	}
	
	function getDadosFilial_cb(data, error){
		if (error) {
			alert(error);
			return false;
		}
		cliente_cb(data, "clienteByIdClienteRemetente");
		findEndereco(data.idCliente, data.pessoa.tpPessoa, "enderecoRemetenteConsolidacaoCarga");
		setElementValue("aeroportoByIdAeroportoOrigem.idAeroporto", data.aeroportoByIdAeroportoOrigem.idAeroporto);
		setElementValue("aeroportoByIdAeroportoOrigem.sgAeroporto", data.aeroportoByIdAeroportoOrigem.sgAeroporto);
		setElementValue("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", data.aeroportoByIdAeroportoOrigem.pessoa.nmPessoa);
	}
	
	function enderecoRemetenteConsolidacaoCarga_cb(data, error){
		if (error) {
			alert(error);
			return false;
		}
		setEnderecoConsolidacaoCarga(data, "clienteByIdClienteRemetente");
	}
	
	function setEnderecoConsolidacaoCarga(dados, tipo) {
		var nmTarget = tipo + ".endereco";
		var sEV = setElementValue;
		if(dados && dados.endereco) {
			var objEnd = dados.endereco;
			sEV(nmTarget + ".nrCep", objEnd.nrCep);
			sEV(nmTarget + ".nmMunicipio", objEnd.nmMunicipio);
			sEV(nmTarget + ".idMunicipio", objEnd.idMunicipio);
			sEV(nmTarget + ".idUnidadeFederativa", objEnd.idUnidadeFederativa);
			sEV(nmTarget + ".siglaDescricaoUf", objEnd.sgUnidadeFederativa);
			sEV(nmTarget + ".dsComplemento", objEnd.dsComplemento);
			sEV(nmTarget + ".nrEndereco", objEnd.nrEndereco);
			sEV(nmTarget + ".dsEndereco", objEnd.dsEndereco);
		}
	}
	
	function setAcumuladores(data) {
		var qtVolumesTotal = data.qtVolumesTotal;
		if(parseInt(qtVolumesTotal) <= 0) {
			qtVolumesTotal = "";
		}
		var psRealTotal = data.psRealTotal;
		if(parseFloat(psRealTotal) <= 0.00000) {
			psRealTotal = "";
		}
		var psCubadoTotal = data.psCubadoTotal;
		if(parseFloat(psCubadoTotal) <= 0.00000) {
			psCubadoTotal = "";
		}
		setElementValue("qtVolumes", setFormat("qtVolumes", qtVolumesTotal));
		setElementValue("psTotal", setFormat("psTotal", psRealTotal));
		setElementValue("psCubado", setFormat("psCubado", psCubadoTotal));
	}
	
	function habilitaBotoes(){
		setDisabled("btnIncluirExcluirCTRC", false);
		setDisabled("btnLimpar", false);
		setDisabled("storeButton", false);
	}
	
	function desabilitaDadosExpedidor(){
		clearIeRemetente();
		resetValue("clienteByIdClienteRemetente.idCliente");
		setDisabled("clienteByIdClienteRemetenteButton", true);		
		setDisabled("clienteByIdClienteRemetente.endereco.dsEndereco", true);
		setDisabled("clienteByIdClienteRemetente.endereco.nrEndereco", true);
		setDisabled("clienteByIdClienteRemetente.endereco.dsComplemento", true);
		setDisabled("clienteByIdClienteRemetente.endereco.nmMunicipio", true);
		setDisabled("clienteByIdClienteRemetente.endereco.siglaDescricaoUf", true);
		setDisabled("clienteByIdClienteRemetente.endereco.nrCep", true);
	}
	
	function desabilitaDadosDestinatario(){
		clearIeDestinatario();		
		resetValue("clienteByIdClienteDestinatario.idCliente");
		setDisabled("clienteByIdClienteDestinatarioButton", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.dsEndereco", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.nrEndereco", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.dsComplemento", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.nmMunicipio", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.siglaDescricaoUf", true);
		setDisabled("clienteByIdClienteDestinatario.endereco.nrCep", true);
	}
	
	function awbOnDataLoadCallBack_cb(data) {
		awb_nrAwb_exactMatch_cb(data);
		if(data != null && data.length > 0){
			data = data[0] != null ? data[0] : data;			
		}
	}
	
	function findAwb_cb(data, error){
		if(data != null){
			data = data[0] != null ? data[0] : data;
			setElementValue("awb.idAwb", data.idAwb);
			setElementValue("ciaFilialMercurio.empresa.idEmpresa", data.ciaFilialMercurio.empresa.idEmpresa);
			setElementValue("ciaFilialMercurio.empresa.sgEmpresa", data.ciaFilialMercurio.empresa.sgEmpresa);
		}
	}
	
	function setValorIcms(){
		if(getElementValue("awb.vlFrete") != null && getElementValue("awb.vlFrete") != "" && getElementValue("pcAliquotaIcms") != "" && getElementValue("pcAliquotaIcms") != "0"){
			var sdo = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasAction.setValorIcms", "setValorIcms", {valorFrete:getElementValue("awb.vlFrete"), aliquotaIcms:getElementValue("pcAliquotaIcms")});
			xmit({serviceDataObjects:[sdo]});
		}else if (getElementValue("pcAliquotaIcms") == "0"){
			setElementValue("vlIcms", setFormat("vlIcms", "0.00"));
		}
	}
	
	function setValorIcms_cb(data, error){
		if (error) {
			alert(error);
		} else {
			setElementValue("vlIcms", setFormat("vlIcms", data.vlIcms));
		}
	}
	
	function preencheFilialTomador_OnChange(combo) {
		var r = comboboxChange({e:combo});
		var criteria = new Array();
		var idCiaFilial = document.getElementById("ciaFilialMercurio.idCiaFilialMercurio").value;
		
		if (idCiaFilial == "") {
			document.getElementById("clienteByIdClienteTomador.pessoa.nrIdentificacao").value = "";
			document.getElementById("clienteByIdClienteTomador.nrContaCorrente").value = "";
			tomadorChange();
			return r;
		}
		
	    setNestedBeanPropertyValue(criteria, "idCiaFilial", idCiaFilial);
		var sdoFilialTomador = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasAction.findTomadorDocument",
			"preencheFilial", criteria);
		xmit({serviceDataObjects:[sdoFilialTomador]});
		return r;
	}	
	
	function preencheFilial_cb(data, exception){
		if (exception == null) {
			document.getElementById("clienteByIdClienteTomador.pessoa.nrIdentificacao").value = getNestedBeanPropertyValue(data,'tomador.nrIdentificacao');
			document.getElementById("clienteByIdClienteTomador.nrContaCorrente").value = getNestedBeanPropertyValue(data,'tomador.nrContaCorrente');
		} else {
			document.getElementById("clienteByIdClienteTomador.pessoa.nrIdentificacao").value = "";
			document.getElementById("clienteByIdClienteTomador.nrContaCorrente").value = "";
		}
		tomadorChange();	
	}
	
</script>