<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';

		return colunas;	
	}

	function pageLoadMotorista() {
		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
				,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
				,numberElement:document.getElementById("pessoa.idPessoa")});
	}
</script>
<adsm:window service="lms.contratacaoveiculos.manterMotoristasAction" onPageLoadCallBack="motorista_pageLoad" onPageLoad="pageLoadMotorista">
	<adsm:i18nLabels>
		<adsm:include key="LMS-26008"/>
		<adsm:include key="LMS-00051"/>
		<adsm:include key="motorista"/>
	</adsm:i18nLabels>
	<adsm:form action="/contratacaoVeiculos/manterMotoristas" service="lms.contratacaoveiculos.manterMotoristasAction.findByIdDetalhamento" idProperty="idMotorista" height="370" onDataLoadCallBack="motoristaLoad">

	<td colspan="100" >

	<div id="principal" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>	

	<%-- DADOS BÁSICOS ....................................................................................................--%>		
		<adsm:hidden property="pessoa.tpPessoa" value="F"/>
		<adsm:hidden property="labelPessoa"/>
		<adsm:hidden property="tpSituacaoLookup" value="A" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="desabilitaCad" serializable="false"/>

		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupFilial" dataType="text" 
					idProperty="idFilial" property="filial" criteriaProperty="sgFilial" required="true"
					label="filial" size="3" maxLength="3" labelWidth="17%" width="8%" action="/municipios/manterFiliais">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="27" maxLength="50" width="25%" disabled="true" />
		</adsm:lookup>

		<adsm:combobox property="tpVinculo" label="tipoVinculo" onchange="tipoVinculoChange(this.value);" 
				domain="DM_TIPO_VINCULO_MOTORISTA" labelWidth="17%" width="33%" required="true"/>
				
		<adsm:combobox property="tpOperacao" domain="DM_TIPO_OPERACAO_PROPRIETARIO" label="tipoOperacao"  labelWidth="17%" width="63%"  required="true" />					

		<adsm:hidden property="codFuncao.cargo.codigo" value="" serializable="false" />

		<adsm:lookup label="matricula" size="10" maxLength="16" labelWidth="17%" width="12%" 
					service="lms.contratacaoveiculos.manterMotoristasAction.findLookupMotoristaInstrutor" 
					dataType="text" 
					exactMatch="true"
					property="funcionario"
					idProperty="usuario.idUsuario" 
					criteriaProperty="nrMatricula"
					action="/contratacaoVeiculos/consultarMotoristasInstrutoresView" 
					onDataLoadCallBack="funcionarioDataLoad" 
					onPopupSetValue="funcionarioPopup" 
					onchange="return funcionarioOnChange(this)">
			<adsm:propertyMapping criteriaProperty="codFuncao.cargo.codigo" modelProperty="codFuncao.cargo.codigo"/>			
 			<adsm:propertyMapping criteriaProperty="pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao"/>		
 			<adsm:propertyMapping criteriaProperty="pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao"/>	

			<adsm:propertyMapping relatedProperty="funcionario.nmFuncionario" modelProperty="nmUsuario" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="usuarioMotorista.idUsuario" modelProperty="idUsuario"/>	
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="nmUsuario" />
			<adsm:propertyMapping relatedProperty="pessoa.nrIdentificacao" modelProperty="nrCpf"/>
			<adsm:propertyMapping relatedProperty="dtNascimento" modelProperty="dtNascimento"/>			
			<adsm:propertyMapping relatedProperty="tpSexo" modelProperty="tpSexo"/>
			<adsm:propertyMapping relatedProperty="pessoa.nrRg" modelProperty="nrRg"/>
			<adsm:propertyMapping relatedProperty="pessoa.dsOrgaoEmissorRg" modelProperty="dsOrgaoEmissor"/>
			<adsm:propertyMapping relatedProperty="pessoa.dtEmissaoRg" modelProperty="dtEmissaoRg"/>
			<adsm:propertyMapping relatedProperty="nrCarteiraHabilitacao" modelProperty="nrCnh"/>
			<adsm:propertyMapping relatedProperty="dsClasse" modelProperty="tpCategoriaCnh"/>
			<adsm:propertyMapping relatedProperty="dtVencimentoHabilitacao" modelProperty="dtVencimentoHabilitacao"/>
			<adsm:propertyMapping relatedProperty="pessoa.dsEmail" modelProperty="dsEmail"/>			
		</adsm:lookup>

		<adsm:hidden property="usuarioMotorista.idUsuario"/>

		<adsm:textbox dataType="text" property="funcionario.nmFuncionario" size="30" disabled="true" width="37%" serializable="false"/>

		<adsm:complement label="identificacao" labelWidth="17%" width="33%">		
					<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad"/>		
					<adsm:lookup definition="IDENTIFICACAO_PESSOA" service="lms.contratacaoveiculos.manterMotoristasAction.validateIdentificacao" onDataLoadCallBack="validaIdPessoa"/>		
					<adsm:hidden property="pessoa.dsEmail" serializable="false"/>
		</adsm:complement>		

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" size="30" maxLength="50" labelWidth="17%" width="33%" required="true" depends="pessoa.nrIdentificacao"/>

		<adsm:textbox dataType="JTDate" property="dtNascimento" label="dataNascimento" labelWidth="17%" width="33%" required="true"/>

		<adsm:combobox property="tpSexo" domain="DM_TIPO_SEXO" label="sexo" labelWidth="17%" width="33%"/>
		
		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupMunicipio" idProperty="idMunicipio" dataType="text" property="municipioNaturalidade" 
					criteriaProperty="nmMunicipio" label="naturalidade" maxLength="60" size="30" exactMatch="false" minLengthForAutoPopUpSearch="3"
					action="/municipios/manterMunicipios" width="33%" labelWidth="17%">
			<adsm:propertyMapping criteriaProperty="tpSituacaoLookup" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="nmPais" modelProperty="unidadeFederativa.pais.nmPais"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="sgUnidadeFederativa" disabled="true" serializable="false" size="3" maxLength="60" label="uf" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="nmPais" disabled="true" serializable="false" size="30" maxLength="60" label="pais" labelWidth="17%" width="34%"/>

		<adsm:textbox dataType="text" property="nmPai" size="30" maxLength="60" label="nomePai" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="nmMae" size="30" maxLength="60" label="nomeMae" labelWidth="17%" width="33%"/>

		<adsm:combobox property="tpCorPele" label="corPele" domain="DM_COR_PELE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="tpCorCabelo" label="corCabelo" domain="DM_COR_CABELO" labelWidth="17%" width="33%"/>

		<adsm:combobox property="tpCorOlhos" label="corOlhos" domain="DM_COR_OLHOS" labelWidth="17%" width="33%"/>

		<adsm:checkbox property="blPossuiBarba" label="possuiBarba" labelWidth="17%" width="33%"/>

		<adsm:hidden property="idFotoMotorista"/>

		<adsm:textbox property="imFotoMotorista"
					  dataType="picture"
					  blobColumnName="IM_FOTO_MOTORISTA"
					  tableName="FOTO_MOTORISTA"
					  primaryKeyColumnName="ID_FOTO_MOTORISTA"
					  primaryKeyValueProperty="idFotoMotorista"
					  label="foto"
					  labelWidth="17%"
					  width="33%"/>

		<adsm:combobox domain="DM_STATUS" property="tpSituacao" label="situacao" labelWidth="17%" width="33%" required="true"/>

		<adsm:textbox property="localizacao" dataType="text" label="localizacao" labelWidth="17%" width="80%" size="100" disabled="true"/>

		<adsm:hidden property="usuario.idUsuario" />

		<adsm:complement width="90%" labelWidth="17%" label="cadastradoPor">
			<adsm:textbox dataType="text" property="usuario.nrMatricula" width="20%" size="10" disabled="true"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="50" width="80%" disabled="true"/>
		</adsm:complement>
		
		<adsm:complement width="90%" labelWidth="17%" label="alteradoPor">
			<adsm:textbox dataType="text" property="usuarioAlteracao.nrMatricula" width="20%" size="10" disabled="true"/>
			<adsm:textbox dataType="text" property="usuarioAlteracao.nmUsuario" size="50" width="80%" disabled="true"/>
		</adsm:complement>
		
		<adsm:textbox dataType="text" property="dsPendencia" size="30" maxLength="60" label="situacaoPendencia" labelWidth="17%" width="33%" disabled="true"/>

		<adsm:textbox dataType="JTDate" property="dtAtualizacao" disabled="true" picker="false" label="dataAtualizacao" size="20" maxLength="20" labelWidth="17%" width="33%"/>
	<%-- FIM DADOS BÁSICOS ....................................................................................................--%>

	<%-- ENDERECO DO MOTORISTA ....................................................................................................--%>
		<adsm:section caption="endereco"/>

		<adsm:hidden property="endereco.municipio.unidadeFederativa.pais.blCepAlfanumerico"/>
		<adsm:hidden property="endereco.idEndereco"/>		
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>

		<adsm:lookup label="pais" labelWidth="17%" 
					 property="endereco.municipio.unidadeFederativa.pais" 
				 	 idProperty="idPais"
					 criteriaProperty="nmPais" 
					 dataType="text" 			
					 service="lms.contratacaoveiculos.manterMotoristasAction.findLookupPais" 
					 width="33%" size="38" maxLength="60" 					 
					 serializable="false" 
					 minLengthForAutoPopUpSearch="3"
					 action="/municipios/manterPaises" 
					 required="true" 
					 exactMatch="false"
					 onchange="return paisOnChange(this);"
					 onDataLoadCallBack="paisOnDataLoadCallBack"
					 onPopupSetValue="paisOnPopupSetValue">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:lookup>

		<!-- Manter o campo como text e com a mascara setada para habilitar o listener da mascara -->
	 	<adsm:lookup label="cep" labelWidth="17%"	 				
	 	             property="endereco.nrCepLookup" 	 	             
	 	             idProperty="nrCep"
	 	             criteriaProperty="cepCriteria"
	 	             dataType="text"
	 	             service="lms.contratacaoveiculos.manterMotoristasAction.findCepLookup" 
	 	             width="33%" maxLength="8" 
	 	             serializable="false" 
	 	             required="false" 
	 	             exactMatch="true"
	 	             allowInvalidCriteriaValue="true"
					 action="/configuracoes/pesquisarCEP" 					 
					 mask="00000000"		 			
					 onDataLoadCallBack="nrCepLookup" 
					 onPopupSetValue="popupNrCep"
					 onchange="return cepOnChange(this);">
			<!-- PAÍS -->		 			 			
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.pais.nmPais" addChangeListener="false" modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.pais.idPais" addChangeListener="false" modelProperty="municipio.unidadeFederativa.pais.idPais" inlineQuery="true"/>				
			<!-- UF -->
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
			<!-- MUNICÍPIO -->
			<adsm:propertyMapping criteriaProperty="endereco.municipio.idMunicipio" modelProperty="municipio.idMunicipio" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="endereco.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" inlineQuery="false"/>			
			<!-- BAIRRO -->
			<adsm:propertyMapping criteriaProperty="endereco.dsBairro" modelProperty="nmBairro" inlineQuery="false"/>
			<!-- CEP -->
			<adsm:propertyMapping relatedProperty="endereco.nrCep" modelProperty="nrCep"/>
			
			<adsm:propertyMapping relatedProperty="endereco.dsBairro" modelProperty="nmBairro"/>
			<adsm:propertyMapping relatedProperty="endereco.dsEndereco" modelProperty="nmLogradouro"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.pais.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="endereco._dsTipoLogradouro" modelProperty="dsTipoLogradouro"/>

			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
  		</adsm:lookup>
  		
		<adsm:hidden property="endereco.nrCep" serializable="true"/>
		<adsm:hidden property="endereco._dsTipoLogradouro" serializable="false"/>		
		
		<adsm:complement label="endereco" labelWidth="17%" width="85%" required="true">
			<adsm:combobox 	property="endereco.tipoLogradouro.idTipoLogradouro" 
							width="15%" 
							service="lms.contratacaoveiculos.manterMotoristasAction.findLookupTipoLogradouro"
							optionLabelProperty="dsTipoLogradouro" 
							optionProperty="idTipoLogradouro" 
							onlyActiveValues="true" 
							required="true" 
							boxWidth="75">
				<adsm:propertyMapping relatedProperty="endereco._dsTipoLogradouro" modelProperty="endereco.dsTipoLogradouro" addChangeListener="false"/>
			</adsm:combobox>
			<adsm:textbox property="endereco.dsEndereco" dataType="text" size="91" maxLength="100" width="65%"/>
		</adsm:complement>

		<adsm:textbox label="numero" labelWidth="17%" width="33%" property="endereco.nrEndereco" dataType="text" size="10" maxLength="5" required="true"/>
		<adsm:textbox label="complemento" labelWidth="17%" width="33%"  property="endereco.dsComplemento" dataType="text" size="35" maxLength="60"/>
	
		<adsm:textbox label="bairro" labelWidth="17%" width="33%" property="endereco.dsBairro" dataType="text" size="40" maxLength="60"/>
		<adsm:lookup	label="municipio" labelWidth="17%" 
						property="endereco.municipio" 
						idProperty="idMunicipio" 
						dataType="text" 
						criteriaProperty="nmMunicipio"
						service="lms.contratacaoveiculos.manterMotoristasAction.findLookupMunicipio" 
						width="33%" size="35" maxLength="60" 
						onDataLoadCallBack="municipio"
						required="true"
						action="/municipios/manterMunicipios" 
						exactMatch="false" 
						minLengthForAutoPopUpSearch="3">

			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="endereco.municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais"blankFill="false"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"blankFill="false" />
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>			
		</adsm:lookup>
	
		<adsm:hidden property="endereco.municipio.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
		<adsm:textbox property="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf"
			serializable="false" disabled="true" size="5" width="33%" labelWidth="17%">
				<adsm:textbox property="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" dataType="text" 
					serializable="false" size="32"/>
		</adsm:textbox>

		<adsm:range label="vigencia" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="endereco.dtVigenciaInicial" size="10" required="true"/>
			<adsm:textbox dataType="JTDate" property="endereco.dtVigenciaFinal"   size="10"/>
		</adsm:range>
	
	<%-- FIM ENDERECO DO MOTORISTA ....................................................................................................--%>

	<%-- RG ....................................................................................................--%>
		<adsm:section caption="rg"/>

		<adsm:textbox dataType="text" property="pessoa.nrRg" required="true" label="rg" size="20" maxLength="10" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="pessoa.dsOrgaoEmissorRg" label="orgaoEmissor" size="20" maxLength="10" labelWidth="17%" width="33%"/>

		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupMunicipio" idProperty="idMunicipio" dataType="text" property="localEmissaoIdentidade" 
					criteriaProperty="nmMunicipio" label="localEmissao" maxLength="60" size="30" exactMatch="false" minLengthForAutoPopUpSearch="3"
					action="/municipios/manterMunicipios" width="33%" labelWidth="17%" required="true">
 			<adsm:propertyMapping criteriaProperty="tpSituacaoLookup" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="localEmissaoIdentidade.sgUFEmissao" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="localEmissaoIdentidade.sgUFEmissao" disabled="true" serializable="false" size="3" maxLength="60" label="uf" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="JTDate" property="pessoa.dtEmissaoRg" label="dataEmissao" size="20" maxLength="20" labelWidth="17%" width="33%"/>
	<%-- FIM RG ....................................................................................................--%>

	<%-- CNH ....................................................................................................--%>
		<adsm:section caption="CNH"/>

		<adsm:textbox dataType="integer" property="nrCarteiraHabilitacao" label="CNH" size="20" maxLength="11" labelWidth="17%" width="33%" required="true"/>

		<adsm:textbox dataType="text" property="dsClasse" label="categoria" size="10" maxLength="3" labelWidth="17%" width="33%" required="true"/>

		<adsm:textbox dataType="integer" property="nrProntuario" label="prontuario" size="20" maxLength="9" labelWidth="17%" width="33%" required="true"/>

		<adsm:textbox dataType="JTDate" property="dtVencimentoHabilitacao" label="vencimentoCNH" labelWidth="17%" width="33%" required="true"/>

		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					service="lms.contratacaoveiculos.manterMotoristasAction.findLookupUF" dataType="text"
					labelWidth="17%" width="33%" label="ufEmissao" size="3" maxLength="3" 
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="true">
			<adsm:hidden property="tpSituacaoUnidadeFederativa" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoUnidadeFederativa" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="19" serializable="false" disabled="true" />
		</adsm:lookup>	
		
		<adsm:textbox dataType="text" property="unidadeFederativa.pais.nmPais" disabled="true" serializable="false" size="30" maxLength="60" label="pais" labelWidth="17%" width="33%"/>
		
		<adsm:textbox dataType="JTDate" property="dtEmissao" label="emissaoCNH" labelWidth="17%" width="33%" required="true"/>
	<%-- FIM CNH ....................................................................................................--%>

	<%-- CTPS ....................................................................................................--%>
		<adsm:section caption="ctps"/>

		<adsm:complement label="ctps" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" property="nrSerieCarteiraProfissional" size="5" maxLength="10" width="10%"/>
			<adsm:textbox dataType="text" property="nrCarteiraProfissional" size="15" maxLength="10" width="20%"/>
		</adsm:complement>

		<adsm:textbox dataType="JTDate" property="dtEmissaoCarteiraProfission" label="emissaoCTPS" labelWidth="17%" width="33%"/>
	<%-- FIM CTPS ....................................................................................................--%>

	<%-- Infomações do bloqueio/liberação ................................................................................................................--%>
		<adsm:section caption="informacoesBloqueioLiberacao"/>
		<adsm:textbox label="situacao" property="situacaoBloqueioLiberacao" serializable="false" dataType="text" disabled="true" labelWidth="17%" width="33%"/>

	<%-- REFERENCIAS PROFISSIONAIS ....................................................................................................--%>

		</table>	
	</div>
	<div id="referencias" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="referenciasProfissionais"/>

		<%-- REFERENCIA PROFISSIONAIS 1 ................................................................................................................--%> 

		<adsm:hidden property="ref_idContato_1"/>
		<adsm:hidden property="ref_idTelefone_1"/>
		<adsm:hidden property="ref_idTelefoneContato_1"/>

		<adsm:textbox dataType="text" required="true" property="ref_nome_1" label="referencia1" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_1" label="email" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_1" required="true" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_1" required="true" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_1" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="true" property="ref_nrDdd_1" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="true" property="ref_nrTelefone_1" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

		<%-- REFERENCIA PROFISSIONAIS 2 ................................................................................................................--%> 

		<adsm:hidden property="ref_idContato_2"/>
		<adsm:hidden property="ref_idTelefone_2"/>
		<adsm:hidden property="ref_idTelefoneContato_2"/>

		<adsm:textbox dataType="text" required="false" property="ref_nome_2" label="referencia2" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_2" label="email" size="40" maxLength="60"labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_2" required="false" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_2" required="false" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_2" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="false" property="ref_nrDdd_2" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="false" property="ref_nrTelefone_2" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

		<%-- REFERENCIA PROFISSIONAIS 3 ................................................................................................................--%>
		<adsm:hidden property="ref_idContato_3"/>
		<adsm:hidden property="ref_idTelefone_3"/>
		<adsm:hidden property="ref_idTelefoneContato_3"/>

		<adsm:textbox dataType="text" required="false" property="ref_nome_3" label="referencia3" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_3" label="email" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_3" required="false" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_3" required="false" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_3" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="false" property="ref_nrDdd_3" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="false" property="ref_nrTelefone_3" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

	<%-- FIM REFERENCIAS PROFISSIONAIS ....................................................................................................--%>

	<%-- REFERENCIAS PESSOAIS .............................................................................................................--%>

		<adsm:section caption="referenciasPessoais"/>

		<%-- REFERENCIA PESSOAL 1 ................................................................................................................--%>
		<adsm:hidden property="ref_idContato_4"/>
		<adsm:hidden property="ref_idTelefone_4"/>
		<adsm:hidden property="ref_idTelefoneContato_4"/>

		<adsm:textbox dataType="text" required="true" property="ref_nome_4" label="referencia1" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_4" label="email" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_4" required="true" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_4" required="true" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_4" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="true" property="ref_nrDdd_4" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="true" property="ref_nrTelefone_4" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

		<%-- REFERENCIA PESSOAL 2 ................................................................................................................--%>
		<adsm:hidden property="ref_idContato_5"/>
		<adsm:hidden property="ref_idTelefone_5"/>
		<adsm:hidden property="ref_idTelefoneContato_5"/>

		<adsm:textbox dataType="text" required="false" property="ref_nome_5" label="referencia2" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_5" label="email" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_5" required="false" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_5" required="false" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_5" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="false" property="ref_nrDdd_5" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="false" property="ref_nrTelefone_5" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

		<%-- REFERENCIA PESSOAL 3 ................................................................................................................--%>
		<adsm:hidden property="ref_idContato_6"/>
		<adsm:hidden property="ref_idTelefone_6"/>
		<adsm:hidden property="ref_idTelefoneContato_6"/> 

		<adsm:textbox dataType="text" required="false" property="ref_nome_6" label="referencia3" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="email" property="ref_email_6" label="email" size="40" maxLength="60" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_tpTelefone_6" required="false" label="tipo" domain="DM_TIPO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:combobox property="ref_uso_6" required="false" label="uso" domain="DM_USO_TELEFONE" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="text" property="ref_nrDdi_6" label="ddi" size="5" maxLength="5" labelWidth="17%" width="33%"/>

		<adsm:complement label="numero" labelWidth="17%" width="33%">
			<adsm:textbox dataType="text" required="false" property="ref_nrDdd_6" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" required="false" property="ref_nrTelefone_6" size="10" maxLength="10" width="20%"/>
		</adsm:complement>

		<adsm:hidden property="linkPropertyLiberacoesReguladora" value="motorista" serializable="false"/>
		</table>
	</div>
	</td></tr></table>

	<%-- FIM REFERENCIAS PESSOAIS ....................................................................................................--%>
	<adsm:buttonBar lines="2">

		<adsm:button id="btLiberacoesReguladora" caption="liberacoesReguladora" action="/contratacaoVeiculos/manterLiberacoesReguladora" cmd="main" boxWidth="150">
			<adsm:linkProperty src="idMotorista" target="motorista.idMotorista"/>
			<adsm:linkProperty src="pessoa.tpIdentificacao" target="motorista.pessoa.tpIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="motorista.pessoa.nrIdentificacao" disabled="true"/>
			<adsm:linkProperty src="tpVinculo" target="motorista.tpVinculo.value" disabled="true"/>
			<adsm:linkProperty src="pessoa.nmPessoa" target="motorista.pessoa.nmPessoa" disabled="true"/>
			<adsm:linkProperty src="pessoa.tpIdentificacao" target="proprietarioMotorista.motorista.pessoa.tpIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="proprietarioMotorista.motorista.pessoa.nrIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nmPessoa" target="proprietarioMotorista.motorista.pessoa.nmPessoa" disabled="true"/>
			<adsm:linkProperty src="linkPropertyLiberacoesReguladora" target="src"/>
		</adsm:button>

		<adsm:button id="btMeiosTransporte" caption="meiosTransporte" action="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" cmd="main" boxWidth="135" breakBefore="true">
			<adsm:linkProperty src="idMotorista" target="motorista.idMotorista"/>
			<adsm:linkProperty src="pessoa.tpIdentificacao" target="motorista.pessoa.tpIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="motorista.pessoa.nrIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nmPessoa" target="motorista.pessoa.nmPessoa" disabled="true"/>		
		</adsm:button>

		<adsm:button id="btConsultarBloqueioLiberacao" caption="consultarBloqueioLiberacao" action="/contratacaoVeiculos/manterBloqueiosMotoristaProprietario" cmd="main" boxWidth="195">
			<adsm:linkProperty src="idMotorista" target="motorista.idMotorista"/>
			<adsm:linkProperty src="pessoa.tpIdentificacao" target="motorista.pessoa.tpIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="motorista.pessoa.nrIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nmPessoa" target="motorista.pessoa.nmPessoa" disabled="true"/>
		</adsm:button>

		<adsm:button id="btEndereco" caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" boxWidth="70" >
			<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>
			<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true"/>
			<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
			<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
		</adsm:button>

		<adsm:button caption="bloquear" id="bloquear" onclick="showModalDialog('contratacaoVeiculos/popupBloqueios.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:250px;');"/>

		<adsm:storeButton id="btStore" service="lms.contratacaoveiculos.manterMotoristasAction.storeMap" callbackProperty="motoristaStore" />
		<adsm:newButton id="btNew" />
		<adsm:removeButton id="btRemove"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script language="javascript" type="text/javascript">
	document.getElementById("labelPessoa").masterLink = "true";
	document.getElementById("codFuncao.cargo.codigo").masterLink = "true";
	
	
	function motorista_pageLoad_cb(data){
		onPageLoad_cb(data);
		
		
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1);
			getTabGroup(this.document).setDisabledTab("pesq",true);
		}
		
		
		setElementValue(document.getElementById("labelPessoa"), getI18nMessage("motorista"));
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'})
		document.getElementById("pessoa.nrIdentificacao").serializable = "true"; 
		loadUsuarioLogado();
		
	}

	function motoristaStore_cb(data, error, errorKey, showError){
		store_cb(data, error, errorKey, showError);	
		var id = getNestedBeanPropertyValue(data, "idMotorista");
		if (id != undefined) {
			setElementValue("pessoa.idPessoa", id);
			setElementValue("endereco.idEndereco",getNestedBeanPropertyValue(data, "idEndereco"));
			setElementValue("tpSituacao",getNestedBeanPropertyValue(data, "tpSituacao"));
			setElementValue("usuarioAlteracao.nrMatricula",getNestedBeanPropertyValue(data, "usuarioAlteracao.nrMatricula"));
			setElementValue("usuarioAlteracao.nmUsuario",getNestedBeanPropertyValue(data, "usuarioAlteracao.nmUsuario"));			
			setElementValue('dtAtualizacao', getFormattedValue("JTDate", getNestedBeanPropertyValue(data, "dtAtualizacao"), "dd/MM/yyyy", true));			
			setElementValue("desabilitaCad",getNestedBeanPropertyValue(data, "desabilitaCad"));			
			
			setaLabelBotaoBloqueio(getNestedBeanPropertyValue(data, "blBloqueio"));		
			
			setaIdsReferencias(data);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("funcionario.usuario.idUsuario", true);
			
			defineDisabledButtons(
					null, 
					getElementValue("idProcessoWorkflow"), 
					getNestedBeanPropertyValue(data, "desabilitaCad"));
		}
	}

	function setaIdsReferencias(data){
		for (var i = 1; i < 7; i++) {
			var idContato = getNestedBeanPropertyValue(data, "ref_idContato_" + i);
			var idTelefone = getNestedBeanPropertyValue(data, "ref_idTelefone_" + i);
			var idTelefoneContato = getNestedBeanPropertyValue(data, "ref_idTelefoneContato_" + i);
			if(idContato != undefined && idTelefone != undefined && idTelefoneContato != undefined) {
				setElementValue("ref_idContato_" + i, idContato);
				setElementValue("ref_idTelefone_" + i, idTelefone);
				setElementValue("ref_idTelefoneContato_" + i, idTelefoneContato);
			}
		}
	}

	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	
	var oldTpVinculo;
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function motoristaLoad_cb(data, exception){
		onDataLoad_cb(data, exception);

		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
				numberElement:document.getElementById("pessoa.nrIdentificacao")});

		var idPessoa = getNestedBeanPropertyValue(data, "idMotorista");
		
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
			disableTabAnexos(false);
		} else {
			disableTabAnexos(true);
		}

		oldTpVinculo = getNestedBeanPropertyValue(data, "tpVinculo");
		setaPropriedadesPorVinculo(oldTpVinculo);

		var nrMatricula = getNestedBeanPropertyValue(data, "funcionario.nrMatricula");
		if (nrMatricula != undefined){
			setDisabled("funcionario.usuario.idUsuario", true);
			setDisabled("funcionario.nrMatricula", true);
		}

		var blVencida = getNestedBeanPropertyValue(data, "blVencida");

		if(blVencida == 'S') {
			alertI18nMessage("LMS-26008");
		}
		var tpIdentificacao = getNestedBeanPropertyValue(data, "pessoa.tpIdentificacao");
		setaLabelBotaoBloqueio(getNestedBeanPropertyValue(data, "blBloqueio"));		
 		
		defineDisabledButtons(
				getNestedBeanPropertyValue(data, "tpSituacao"), 
				getElementValue("idProcessoWorkflow"), 
				getNestedBeanPropertyValue(data, "desabilitaCad"));
	}
	
	function defineDisabledButtons(situacaoMotorista, idProcessoWorkflow, desabilitaCad){		
		if (idProcessoWorkflow != "" || desabilitaCad === 'true') {	
			setDisabled("btLiberacoesReguladora", true);
			setDisabled("btMeiosTransporte", true);
			setDisabled("btConsultarBloqueioLiberacao", true);
			setDisabled("btEndereco", true);
			setDisabled("bloquear", true);
			setDisabled("btStore", true);
			setDisabled("btNew", true);
			setDisabled("btRemove", true);
		} else if (situacaoMotorista && situacaoMotorista === "I"){
			alertI18nMessage("LMS-00051", 'motorista');
		}		
	}

	function setaLabelBotaoBloqueio(blBloqueio){
		if (blBloqueio == 'S'){
			document.getElementById("bloquear").value = parent.i18NLabel.getLabel("liberar");
			setElementValue(document.getElementById("situacaoBloqueioLiberacao"),parent.i18NLabel.getLabel("bloqueado"));
		} else {
			document.getElementById("bloquear").value = parent.i18NLabel.getLabel("bloquear");
			setElementValue(document.getElementById("situacaoBloqueioLiberacao"),parent.i18NLabel.getLabel("liberado"));
		}
	}

	var idUsuario;
	var nrMatricula;
	var nmUsuario;
	var idFilial;
	var sgFilial;
	var nmFantasia;
	var cargo;

	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {			
			var idMotorista = getElementValue("idMotorista");
			
			if(!idMotorista || idMotorista == ""){				
				setaPropriedadesPorVinculo("");
				setaValoresSessao();
				
				setDisabled("pessoa.tpIdentificacao", false );
				setDisabled("pessoa.nrIdentificacao", true );
				document.getElementById("bloquear").value = parent.i18NLabel.getLabel("bloquear");
				oldTpVinculo = undefined;
				disableTabAnexos(true);
			}
			
			defineDisabledButtons(null, getElementValue("idProcessoWorkflow"), getElementValue("desabilitaCad"));
		} else {
			disableTabAnexos(false);
		} 		
	}

	//Chama o servico que retorna os dados do usuario logado 
	function loadUsuarioLogado(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.contratacaoveiculos.motoristaService.findDadosUsuarioLogado",
				"preencheDadosUsuarioLogado",data);
		xmit({serviceDataObjects:[sdo]});
	}

	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosUsuarioLogado_cb(data, exception){
		if (exception == null){
			idUsuario = getNestedBeanPropertyValue(data,"idUsuario");
			nrMatricula = getNestedBeanPropertyValue(data,"nrMatricula");
			nmUsuario = getNestedBeanPropertyValue(data, "nmUsuario");
			idFilial = getNestedBeanPropertyValue(data, "idFilial");
			sgFilial = getNestedBeanPropertyValue(data, "sgFilial");
			nmFantasia = getNestedBeanPropertyValue(data, "nmFantasia");
			cargo = getNestedBeanPropertyValue(data, "cargo");
			setaValoresSessao();
		}
	}

	function setaValoresSessao(){
		setElementValue("usuario.idUsuario", idUsuario);
		setElementValue("usuario.nrMatricula", nrMatricula);
		setElementValue("usuario.nmUsuario", nmUsuario);
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFantasia);
		setElementValue("codFuncao.cargo.codigo", cargo);
	}

	function testEmptyField(fieldName) {
		if(getElementValue(fieldName) == '') {
			return true;
		} else {
			return false;
		}				
	}

	function setDisabledIfFieldIsEmpty(fieldName, blDisabled) {
		if(testEmptyField(fieldName) && blDisabled) {
			setDisabled(fieldName, false);
		} else {
			setDisabled(fieldName, blDisabled);
		}
	}
	
	function setDisableFuncionarioFields(blDisabled) {		
		setDisabledIfFieldIsEmpty("pessoa.nmPessoa", blDisabled);
		setDisabledIfFieldIsEmpty("pessoa.tpIdentificacao", blDisabled);
		setDisabledIfFieldIsEmpty("pessoa.nrIdentificacao", blDisabled);
		setDisabledIfFieldIsEmpty("dtNascimento", blDisabled);
		setDisabledIfFieldIsEmpty("tpSexo", blDisabled);
		//setDisabledIfFieldIsEmpty("pessoa.nrRg", blDisabled);
		//setDisabledIfFieldIsEmpty("pessoa.dsOrgaoEmissorRg", blDisabled);
		//setDisabledIfFieldIsEmpty("pessoa.dtEmissaoRg", blDisabled);
		setDisabledIfFieldIsEmpty("nrCarteiraHabilitacao", blDisabled);
		//setDisabledIfFieldIsEmpty("dsClasse", blDisabled);
		//setDisabledIfFieldIsEmpty("dtVencimentoHabilitacao", blDisabled);
		//UF CNH e UF RG		
	}
	
	function tipoVinculoChange(tpVinculo){
		findReferencias();
		setaPropriedadesPorVinculo(tpVinculo);
		setaFuncionarioRelateds(tpVinculo);
	}

	function validateVinculo_cb(data,error) {
		var mensagem = getNestedBeanPropertyValue(data,"mensagem");
		var tpVinculo = getNestedBeanPropertyValue(data,"tpVinculo");
		if (mensagem != undefined && mensagem != "") {
			alert(mensagem);
			setElementValue("tpVinculo","");
			setFocus("tpVinculo");
		} else {
			setaPropriedadesPorVinculo(tpVinculo);
			setaFuncionarioRelateds(tpVinculo);
		}
	}

	function setaFuncionarioRelateds(tpVinculo){
		var relateds;
		if(getElementValue("idMotorista") != ''
			&& oldTpVinculo != 'F'
			&& tpVinculo == 'F'
		) {
			relateds =
 			[{ modelProperty:"nrMatricula", criteriaProperty:"funcionario.nrMatricula", inlineQuery:true, disable:true }, 
			{ modelProperty:"nrMatricula", relatedProperty:"funcionario.nrMatricula", blankFill:true }, 
			{ modelProperty:"nmUsuario", relatedProperty:"funcionario.nmFuncionario", blankFill:true },
			{ modelProperty:"codFuncao.cargo.codigo", criteriaProperty:"codFuncao.cargo.codigo", blankFill:false, inlineQuery:true, disable:false }, 
			{ modelProperty:"pessoa.nrIdentificacao", criteriaProperty:"pessoa.nrIdentificacao", inlineQuery:true, disable:true }, 
 			{ modelProperty:"pessoa.tpIdentificacao", criteriaProperty:"pessoa.tpIdentificacao", inlineQuery:true, disable:true }, 
			{ modelProperty:"idUsuario", relatedProperty:"usuarioMotorista.idUsuario", blankFill:true } ];
		} else {
			relateds = 		
			[{ modelProperty:"nrMatricula", criteriaProperty:"funcionario.nrMatricula", inlineQuery:true, disable:true }, 
			{ modelProperty:"nrMatricula", relatedProperty:"funcionario.nrMatricula", blankFill:true }, 
			{ modelProperty:"codFuncao.cargo.codigo", criteriaProperty:"codFuncao.cargo.codigo", blankFill:false, inlineQuery:true, disable:false }, 
			{ modelProperty:"pessoa.nrIdentificacao", criteriaProperty:"pessoa.nrIdentificacao", inlineQuery:true, disable:true }, 
 			{ modelProperty:"pessoa.tpIdentificacao", criteriaProperty:"pessoa.tpIdentificacao", inlineQuery:true, disable:true }, 
			{ modelProperty:"nmUsuario", relatedProperty:"funcionario.nmFuncionario", blankFill:true }, 
			{ modelProperty:"idUsuario", relatedProperty:"usuarioMotorista.idUsuario", blankFill:true }, 
			{ modelProperty:"nmUsuario", relatedProperty:"pessoa.nmPessoa", blankFill:true }, 
			{ modelProperty:"nrCpf", relatedProperty:"pessoa.nrIdentificacao", blankFill:true }, 
			{ modelProperty:"dtNascimento", relatedProperty:"dtNascimento", blankFill:true }, 
			{ modelProperty:"tpSexo", relatedProperty:"tpSexo", blankFill:true }, 
			{ modelProperty:"nrRg", relatedProperty:"pessoa.nrRg", blankFill:true }, 
			{ modelProperty:"dsOrgaoEmissor", relatedProperty:"pessoa.dsOrgaoEmissorRg", blankFill:true }, 
			{ modelProperty:"dtEmissaoRg", relatedProperty:"pessoa.dtEmissaoRg", blankFill:true }, 
			{ modelProperty:"nrCnh", relatedProperty:"nrCarteiraHabilitacao", blankFill:true }, 
			{ modelProperty:"tpCategoriaCnh", relatedProperty:"dsClasse", blankFill:true }, 
			{ modelProperty:"dtVencimentoHabilitacao", relatedProperty:"dtVencimentoHabilitacao", blankFill:true }, 
			{ modelProperty:"dsEmail", relatedProperty:"pessoa.dsEmail", blankFill:true }];
		}
		document.getElementById("funcionario.usuario.idUsuario").propertyMappings = relateds;
	}

	function funcionarioAfterDataLoad(data) {
		if (getElementValue("tpVinculo") == 'F'){
			setDisableFuncionarioFields(true);
		} else {
			setDisableFuncionarioFields(false);			
		}
	}

	function funcionarioBeforeDataLoad(data) {
		if (data != undefined){
			if (getElementValue("pessoa.tpIdentificacao") == '' && oldTpVinculo != 'F'){
				var isEnabled = document.getElementById("pessoa.nrIdentificacao").isDisabled;
				setElementValue("pessoa.tpIdentificacao", "CPF");			
				setDisabled("pessoa.nrIdentificacao",isEnabled);
		}
	}
	}

	function funcionarioDataLoad_cb(data){
		funcionarioBeforeDataLoad(data);		
		funcionario_nrMatricula_exactMatch_cb(data);		
		funcionarioAfterDataLoad(data);
	}

	function funcionarioPopup(data){
		funcionarioBeforeDataLoad(data);

		var backupOnPopupSetValue = document.getElementById("funcionario.usuario.idUsuario").onPopupSetValue;
		document.getElementById("funcionario.usuario.idUsuario").onPopupSetValue = undefined;
		__lookupSetValue({e:document.getElementById("funcionario.usuario.idUsuario"), data:data});
		document.getElementById("funcionario.usuario.idUsuario").onPopupSetValue = backupOnPopupSetValue;

		funcionarioAfterDataLoad(data);
		
		return false;
	}

	function funcionarioOnChange(obj){
		var r = funcionario_nrMatriculaOnChangeHandler();
		if (obj.value == ''&& !document.getElementById("pessoa.tpIdentificacao").disabled)
			resetValue("pessoa.tpIdentificacao");
		return r;		
	}

	function limpaCamposRh(){
		resetValue("funcionario.nrMatricula");
		resetValue("funcionario.usuario.idUsuario");
		resetValue("funcionario.nmFuncionario");
		resetValue("usuarioMotorista.idUsuario");
		resetValue("pessoa.nmPessoa");
		resetValue("pessoa.tpIdentificacao");
		resetValue("pessoa.nrIdentificacao");
		resetValue("dtNascimento");
		resetValue("tpSexo");
		resetValue("pessoa.nrRg");
		resetValue("pessoa.dsOrgaoEmissorRg");
		resetValue("pessoa.dtEmissaoRg");
		resetValue("nrCarteiraHabilitacao");
		resetValue("dsClasse");
		resetValue("dtVencimentoHabilitacao"); 
		resetValue("pessoa.dsEmail");
	}	

	//Seta a tela de acordo com o tipo de vinculo do motorista. 
	// Esconde as referencias e habilita o nr. de matricula se o motorista for funcionario.
	function setaPropriedadesPorVinculo(tpVinculo){	
		if(tpVinculo == 'F') {
			document.getElementById("funcionario.nrMatricula").required = "true";
			setDisabled("funcionario.usuario.idUsuario", false);
			document.getElementById("referencias").style.display = 'none';
			for (var i=1; i <= 6; i++){
				document.getElementById("ref_nome_" + i).required = "false";
				document.getElementById("ref_email_" + i).required = "false";
				document.getElementById("ref_tpTelefone_" + i).required = "false";
				document.getElementById("ref_uso_" + i).required = "false";
				document.getElementById("ref_nrDdd_" + i).required = "false";
				document.getElementById("ref_nrTelefone_" + i).required = "false";
				resetValue("ref_nome_" + i);
				resetValue("ref_email_" + i);
				resetValue("ref_tpTelefone_" + i);
				resetValue("ref_uso_" + i);
				resetValue("ref_nrDdd_" + i);
				resetValue("ref_nrTelefone_" + i);
			}
			setDisableFuncionarioFields(true);
		} else {		
			document.getElementById("funcionario.nrMatricula").required = "false";	
			setDisabled("funcionario.usuario.idUsuario", true);	
			resetValue("usuarioMotorista.idUsuario");			
			resetValue("funcionario.nrMatricula");
			resetValue("funcionario.nmFuncionario");
			document.getElementById("referencias").style.display = '';
			
			document.getElementById("ref_nome_1").required = "true";
			document.getElementById("ref_email_1").required = "false";
			document.getElementById("ref_tpTelefone_1").required = "true";
			document.getElementById("ref_uso_1").required = "true";		
			document.getElementById("ref_nrDdd_1").required = "true";
			document.getElementById("ref_nrTelefone_1").required = "true";
			
			document.getElementById("ref_nome_4").required = "true";
			document.getElementById("ref_email_4").required = "false";
			document.getElementById("ref_tpTelefone_4").required = "true";
			document.getElementById("ref_uso_4").required = "true";		
			document.getElementById("ref_nrDdd_4").required = "true";
			document.getElementById("ref_nrTelefone_4").required = "true";	
			
			setDisableFuncionarioFields(false);			
		}
	}

	function findReferencias(){
		if(document.getElementById("idMotorista").value != ''){						
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.motoristaService.findReferencias", "setaReferenciasEventualAgregado", {idMotorista:getElementValue("idMotorista")}));
			xmit();
		}
	}
	
	function setaReferenciasEventualAgregado_cb(data,exception){
		document.forms[0].elements["usuarioMotorista.idUsuario"].value = '';		
		setDisabled("funcionario.nrMatricula", true);	
		document.getElementById("funcionario.nrMatricula").required = "false";	
		document.getElementById("referencias").style.display = '';
		for (var i=1; i <= 6; i++) {
			document.getElementById("ref_nome_" + i).required = "true";
			document.getElementById("ref_email_" + i).required = "false";
			document.getElementById("ref_tpTelefone_" + i).required = "true";
			document.getElementById("ref_uso_" + i).required = "true";		
			document.getElementById("ref_nrDdd_" + i).required = "true";
			document.getElementById("ref_nrTelefone_" + i).required = "true";
			setElementValue("ref_nome_" + i,getNestedBeanPropertyValue(data,"ref_nome_" + i));
			setElementValue("ref_email_" + i,getNestedBeanPropertyValue(data,"ref_email_" + i));
			setElementValue("ref_tpTelefone_" + i,getNestedBeanPropertyValue(data,"ref_tpTelefone_" + i));
			setElementValue("ref_uso_" + i,getNestedBeanPropertyValue(data,"ref_uso_" + i));
			setElementValue("ref_nrDdd_" + i,getNestedBeanPropertyValue(data,"ref_nrDdd_" + i));
			setElementValue("ref_nrTelefone_" + i,getNestedBeanPropertyValue(data,"ref_nrTelefone_" + i));
			setElementValue("ref_idTelefone_" + i, getNestedBeanPropertyValue(data,"ref_idTelefone_" + i));
			setElementValue("ref_idTelefoneContato_" + i, getNestedBeanPropertyValue(data,"ref_idTelefoneContato_" + i));
			setElementValue("ref_idContato_" + i, getNestedBeanPropertyValue(data,"ref_idContato_" + i));	
		}
	}

	/**
	 * Reseta campos com valores de pessoa.
	 */
	function reiniciaValores(){
		if (getElementValue("tpVinculo") == "F"){
			limpaCamposRh();
		}
		resetValue(document.getElementById("pessoa.idPessoa"));
		resetValue(document.getElementById("pessoa.nmPessoa"));
		resetValue(document.getElementById("pessoa.tpPessoa"));
	}

	/**
	 * Valida a pessoa de acordo com o retorno do método na service.
	 */
	function validaIdPessoa_cb(data,exception) {
		pessoa_nrIdentificacao_exactMatch_cb(data);
		if (exception != undefined) {
			alert(exception);
			reiniciaValores();
			if (getElementValue("tpVinculo") == "F"){
				setFocus(document.getElementById("funcionario.nrMatricula"));
			} else {
				setFocus(document.getElementById("pessoa.nrIdentificacao"));
			}
		} else {
			//Pessoa não cadastrada na especialização
			if (data.idPessoa != undefined) {
				setElementValue("pessoa.idPessoa",data.idPessoa);
				if (getElementValue("tpVinculo") != "F"){
					setElementValue("pessoa.nmPessoa",data.nmPessoa);
					//setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
					setElementValue("pessoa.tpPessoa",data.tpPessoa);		
				}
			}
		}
	}

	function atualizaLocalizacao() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMotorista", getElementValue("idMotorista"));
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMotoristasAction.findLocalizacaoMotorista", "setaLocalizacao", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function setaLocalizacao_cb(data){
		setElementValue("localizacao", getNestedBeanPropertyValue(data, "localizacao"));
		setaLabelBotaoBloqueio(getNestedBeanPropertyValue(data, "blBloqueio"));
	}

	/**********************************************************************************************************/ 
	function clearEndereco() {
		resetValue('endereco._dsTipoLogradouro');
		resetValue('endereco.tipoLogradouro.idTipoLogradouro');
		resetValue('endereco.dsEndereco');
		resetValue('endereco.nrEndereco');
		resetValue('endereco.dsComplemento');
		resetValue('endereco.dsBairro');
		resetValue('endereco.municipio.idMunicipio');
		resetValue('endereco.municipio.unidadeFederativa.idUnidadeFederativa');
		resetValue('endereco.municipio.unidadeFederativa.sgUnidadeFederativa');
		resetValue('endereco.municipio.unidadeFederativa.nmUnidadeFederativa');
	}
	
	function clearCepEndereco() {
		resetValue('endereco.nrCepLookup.nrCep');
		resetValue('endereco.nrCep');
		clearEndereco();
	}

	function doClearCepMask() {
		setElementValue("endereco.municipio.unidadeFederativa.pais.blCepAlfanumerico", "true");
		doSetCepMask();
	}

	function doSetCepMask() {
		if (getElementValue("endereco.municipio.unidadeFederativa.pais.blCepAlfanumerico") == "true") {
			document.getElementById("endereco.nrCepLookup.cepCriteria").dataType = "text";
			document.getElementById("endereco.nrCepLookup.cepCriteria").mask = "";
		} else {
			document.getElementById("endereco.nrCepLookup.cepCriteria").dataType = "integer";
			document.getElementById("endereco.nrCepLookup.cepCriteria").mask = "00000000";
		}
	}
	
	function doSetBlCepAlfanumerico() {
		var idPais = getElementValue("endereco.municipio.unidadeFederativa.pais.idPais");
		if (idPais != undefined && idPais != "") {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idPais", idPais);
			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMotoristasAction.findPaisByIdPais","doSetBlCepAlfanumerico",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function doSetBlCepAlfanumerico_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		setElementValue("endereco.municipio.unidadeFederativa.pais.blCepAlfanumerico", getNestedBeanPropertyValue(data, "blCepAlfanumerico"));
		doSetCepMask();
	}

	/**
	 * Verifica o ID da combobox de Tipo de Logradouro pelo seu texto.
	 *@param dsTipoLogradouro Descrição contida na combobox
	 *@return ID da combobox
	 */
	function getIdTipoLogradouroByDs(dsTipoLogradouro){
		var opt = document.getElementById("endereco.tipoLogradouro.idTipoLogradouro").options;
		for (i = 0; i < opt.length; i++){
			if (opt[i].text == dsTipoLogradouro){
				return opt[i].value;
			}
		}
	}
	
	/* LOOKUP PAÍS */
	function paisOnChange() {
		var r = endereco_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		clearCepEndereco();

		var idPais = getElementValue("endereco.municipio.unidadeFederativa.pais.idPais");
		if (idPais != undefined && idPais != "") {
			doClearCepMask();
		} else {
			//setElementValue("blCepOpcional", "true");
		}
		
		return r;
	}
	function paisOnDataLoadCallBack_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		var toReturn = endereco_municipio_unidadeFederativa_pais_nmPais_exactMatch_cb(data);
		if (data != undefined && data.length > 0) {
			//setElementValue("blCepOpcional", data[0].blCepOpcional);
			doSetBlCepAlfanumerico();
		} else {
			doClearCepMask();
			setFocus("endereco.municipio.unidadeFederativa.pais.nmPais");
		}
		return toReturn;
	}
	function paisOnPopupSetValue(data, exception) {
		__lookupSetValue({e:document.getElementById("endereco.municipio.unidadeFederativa.pais.idPais"), data:data});
		//setElementValue("blCepOpcional", data.blCepOpcional);
		doSetBlCepAlfanumerico();
		return false;
	}
	/* LOOKUP CEP */
	function cepOnChange() {
		doSetCepMask();
		var r = endereco_nrCepLookup_cepCriteriaOnChangeHandler();
		if (getElementValue("endereco.nrCepLookup.cepCriteria") == "") {
			clearEndereco();
		}
		return r;
	}
	
	function popupNrCep(data) {
		// Caso o Municipio, a uf ou o pais seja inativo, lançar a exceção.
		if ( data.municipio.tpSituacao.value == 'I'
				|| data.municipio.unidadeFederativa.tpSituacao.value == 'I'
				|| data.municipio.unidadeFederativa.pais.tpSituacao.value == 'I' ) {
			
			setElementValue("endereco.nrCep", "");
			alert(i18NLabel.getLabel("LMS-29172")+'');	
			return false;
		}

		data.cepCriteria = data.nrCep;
		
		var backupOnPopupSetValue = document.getElementById("endereco.nrCepLookup.nrCep").onPopupSetValue;
		document.getElementById("endereco.nrCepLookup.nrCep").onPopupSetValue = undefined;
		__lookupSetValue({e:document.getElementById("endereco.nrCepLookup.nrCep"), data:data});
		document.getElementById("endereco.nrCepLookup.nrCep").onPopupSetValue = backupOnPopupSetValue;
		
		var idTpLog = getIdTipoLogradouroByDs(data.dsTipoLogradouro);
		if (idTpLog != undefined){
			setElementValue("endereco.tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "endereco.unidadeFederativa.pais.nmPais");
		if (nmPais != document.getElementById("endereco.municipio.unidadeFederativa.pais.nmPais").value){
			endereco_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
		resetValue('endereco.nrEndereco');
		resetValue('endereco.dsComplemento');
		return false;
	}

	function nrCepLookup_cb(data, erro) {
		if (data == "" || !!erro) {
			resetValue("endereco.nrCepLookup.nrCep");
			setFocus("endereco.nrCepLookup.cepCriteria");
			if(erro) alert(erro);
			return false;
		}
		
		if (data.length <= 0) {
			var cep = document.getElementById("endereco.nrCepLookup.cepCriteria").value;
			setElementValue("nrCep", cep);
			clearEndereco();
			return;
		} 

		var retorno = endereco_nrCepLookup_cepCriteria_exactMatch_cb(data);
		
		var idTpLog = getIdTipoLogradouroByDs(getElementValue("endereco._dsTipoLogradouro"));
		if (idTpLog != undefined){
			setElementValue("endereco.tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['endereco.nrCepLookup.nrCep']});
		} else {
			resetValue('endereco.nrEndereco');
			resetValue('endereco.dsComplemento');
		}
		
		var nmPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.nmPais");
		
		if (nmPais != document.getElementById("endereco.municipio.unidadeFederativa.pais")){
			endereco_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
		
		return retorno;
	}

	function municipio_cb(data) {
		lookupExactMatch({e:document.getElementById("endereco.municipio.idMunicipio"), data:data, callBack:'municipioLikeAndMatch'});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != undefined && nmPais != document.getElementById("endereco.municipio.unidadeFederativa.pais.nmPais").value){
			endereco_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}

	function municipioLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("endereco.municipio.idMunicipio"), data:data});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (data == undefined) return;
		if (nmPais != undefined && nmPais != document.getElementById("endereco.municipio.unidadeFederativa.pais.nmPais").value){
			endereco_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}
</script>