<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("filial.idFilial") == "" &&
					getElementValue("motorista.idMotorista") == "" &&
					getElementValue("meioTransporte2.idMeioTransporte") == "" &&
					getElementValue("proprietario.idProprietario") == "") {
					alert(i18NLabel.getLabel("LMS-00013") 
								+ i18NLabel.getLabel("filial") + ", "
								+ i18NLabel.getLabel("motorista")+ ", "
								+ i18NLabel.getLabel("meioTransporte") + " " + i18NLabel.getLabel("ou") + " "
								+ i18NLabel.getLabel("proprietario")+ ". "
								);
					setFocusOnFirstFocusableField();
					return false;
			}
			return true;
		}
		return false;
	}	

//-->
</script>
<adsm:window title="consultarLiberacoesReguladora" service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00013"/>
		<adsm:include key="motorista"/>
		<adsm:include key="meioTransporte"/>
		<adsm:include key="proprietario"/>
		<adsm:include key="filial"/>
		<adsm:include key="ou"/>
	</adsm:i18nLabels>

	<adsm:form action="/contratacaoVeiculos/manterLiberacoesReguladora">
		<adsm:hidden property="idLiberacaoReguladoraTemp"/>
		<adsm:hidden property="src"/>
	<adsm:combobox property="reguladoraSeguro.idReguladora" label="reguladora" boxWidth="250" service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findComboReguladoraSeguro" optionLabelProperty="pessoa.nmPessoa" optionProperty="idReguladora" labelWidth="20%" width="80%"/>
	<adsm:textbox dataType="text" property="nrLiberacao" label="numeroLiberacao" size="18" maxLength="17" labelWidth="20%" width="30%"/>
 
	<adsm:lookup service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupFilial" dataType="text"
				property="filial" labelWidth="20%" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="80%" action="/municipios/manterFiliais" idProperty="idFilial">
		<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
		<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true" serializable="false"/>
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M"/>
	</adsm:lookup>

	<adsm:lookup dataType="text" property="motorista" idProperty="idMotorista"
			service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMototoristaReal"
			action="/contratacaoVeiculos/manterMotoristas" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao"
			label="motorista" labelWidth="20%" width="80%" size="20" maxLength="20" >
			
		<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" addChangeListener="false"/>
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" />
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" />
			
		<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		<adsm:hidden property="motorista.showFilialUsuarioLogado" value="true"/>
		<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" serializable="false" size="50" disabled="true"/>
	</adsm:lookup>

	<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
			service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMeioTransporte" picker="false" maxLength="6"
			action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
			label="meioTransporte" labelWidth="20%" width="8%" size="7" serializable="false" >
			
		<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />
		<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />		
		<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />
	</adsm:lookup>
		
	<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
			service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMeioTransporte" picker="true" maxLength="25"
			action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
			width="60%" size="20" cellStyle="vertical-align:bottom;">
			
		<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" addChangeListener="false"/>
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false" />
			
		<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" disable="false"/>
		<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
		<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />	
	</adsm:lookup>

	<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
			service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupProprietario"
			action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao"
			label="proprietario" labelWidth="20%" width="80%" size="20" maxLength="20" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
		<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="50" disabled="true"/>
	</adsm:lookup>
	
	<adsm:range label="dataLiberacao" labelWidth="20%" width="30%">
		<adsm:textbox dataType="JTDate" property="dtLiberacaoInicial"/>
		<adsm:textbox dataType="JTDate" property="dtLiberacaoFinal"/>
	</adsm:range>

	<adsm:range label="dataVencimento" labelWidth="20%" width="30%">
		<adsm:textbox dataType="JTDate" property="dtVencimentoInicial"/>
		<adsm:textbox dataType="JTDate" property="dtVencimentoFinal"/>
	</adsm:range>

	<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="Reguladora"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idLiberacaoReguladora" property="Reguladora" selectionMode="check" 
			gridHeight="140" unique="true" rows="7" scrollBars="horizontal">
		<adsm:gridColumn title="reguladora" property="reguladoraSeguro_pessoa_nmPessoa" width="230"/>	
		<adsm:gridColumn title="numeroLiberacao" property="nrLiberacao" dataType="text" width="140"/>	

		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="filial_sgFilial" width="80"/> 
			<adsm:gridColumn title="" property="filial_pessoa_nmFantasia" width="70"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="tipoOperacao" property="tpOperacao" isDomain="true" width="120"/>

		<adsm:gridColumn title="meioTransporte" property="meioTransporte_nrFrota" width="40"/>
		<adsm:gridColumn title="" property="meioTransporte_nrIdentificador" width="60"/>

		<adsm:gridColumn title="identificacao" property="proprietario_pessoa_tpIdentificacao" isDomain="true" width="40"/>	
		<adsm:gridColumn title="" property="proprietario_pessoa_nrIdentificacao" align="right" width="100"/>

		<adsm:gridColumn title="proprietario" property="proprietario_pessoa_nmPessoa" width="150"/>

		<adsm:gridColumn title="identificacao" property="motorista_pessoa_tpIdentificacao" isDomain="true" width="40"/>	
		<adsm:gridColumn title="" property="motorista_pessoa_nrIdentificacao" align="right" width="100"/>

		<adsm:gridColumn title="motorista" property="motorista_pessoa_nmPessoa" width="150"/>
		<adsm:gridColumn title="tipoVinculo" property="motorista_tpVinculo" isDomain="true" width="110"/>
		<adsm:gridColumn title="dataLiberacao" property="dtLiberacao" width="130" dataType="JTDate"/>
		<adsm:gridColumn title="dataVencimento" property="dtVencimento" width="130" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
