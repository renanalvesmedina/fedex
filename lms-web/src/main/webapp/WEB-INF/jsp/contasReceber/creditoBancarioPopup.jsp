<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.creditoBancarioAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasreceber/creditoBancarioLookup">
		<adsm:hidden property="tpSituacao" value="L,E"/>
		<adsm:hidden property="nrBanco"/>
		<adsm:hidden property="idBanco"/>

		<adsm:lookup idProperty="idFilial" property="filial" criteriaProperty="sgFilial"
					service="lms.contasreceber.manterBoletosAction.findLookupFilial" dataType="text"
					label="filial" size="5" maxLength="3" 
					action="/municipios/manterFiliais" minLengthForAutoPopUpSearch="3" exactMatch="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria.banco" 
			idProperty="idBanco"
			service="lms.contasreceber.manterBoletosAction.findLookupBanco" 
			label="banco" 
			size="3" 
			maxLength="3"
			allowInvalidCriteriaValue="true"
		    serializable="false"
			criteriaProperty="nrBanco" 
			action="/configuracoes/manterBancos">
 				
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				formProperty="agenciaBancaria.banco.nmBanco"/> 
				
			<adsm:propertyMapping 
				modelProperty="nrBanco" 
				formProperty="nrBanco"/> 

			<adsm:propertyMapping 
				modelProperty="idBanco" 
				formProperty="idBanco"/> 

				
			<adsm:textbox 
				property="agenciaBancaria.banco.nmBanco" 
				dataType="text" 
				size="20" 
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>



		<adsm:range label="dtCredito">
			<adsm:textbox property="dataCreditoInicial" dataType="JTDate"/>
			<adsm:textbox property="dataCreditoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:range label="dtAlteracao">
			<adsm:textbox property="dataAlteracaoInicial" dataType="JTDate"/>
			<adsm:textbox property="dataAlteracaoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:combobox label="modalidade" property="modalidade" domain="DM_TP_MODALIDADE_CRED_BANC" boxWidth="200" renderOptions="true"/>

		<adsm:combobox label="origemRegistro" property="origemRegistro" domain="DM_TP_ORIGEM_CRED_BANC" boxWidth="200" renderOptions="true" />

		<adsm:range label="valorCredito" >
			<adsm:textbox property="vlCreditoInicial" dataType="currency" size="10"/>
			<adsm:textbox property="vlCreditoFinal" dataType="currency" size="10"/>
		</adsm:range>

		<adsm:range label="valorSaldo" >
			<adsm:textbox property="vlSaldoInicial" dataType="currency" size="10"/>
			<adsm:textbox property="vlSaldoFinal" dataType="currency" size="10"/>
		</adsm:range>

		<adsm:textbox maxLength="40" size="43" dataType="text" property="cpfCnpj" label="cpfCnpj" />
		
		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" size = "43" />

		<adsm:textbox maxLength="40" size="43" dataType="text" property="nomeRazaoSocial" label="nomeRazaoSocial" />
		
		<adsm:textbox maxLength="40" size="43" dataType="text" property="observacoes" label="observacoes" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="remetenteOtd"/>
			<adsm:button caption="limpar" onclick="limpaCampos()" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>


	
	</adsm:form>

	<adsm:grid	idProperty="idRemetenteOtd" 
		property="remetenteOtd" 
		unique="true"
		rowCountService="lms.contasreceber.creditoBancarioAction.getRowCountCreditoBancario" 
		service="lms.contasreceber.creditoBancarioAction.findPaginatedCreditoBancario"
		gridHeight="180"
		rows="8"
		scrollBars="horizontal">
		
		<adsm:gridColumn title="filial" property="filial.sgFilial" dataType="text" width="100" />
		<adsm:gridColumn title="banco" property="banco.nmBanco" dataType="text" width="100" />
		<adsm:gridColumn title="dtCredito" property="dtCredito" dataType="JTDate" width="100" />
		<adsm:gridColumn title="vlCredito" property="vlCredito" dataType="currency" width="100" />
		<adsm:gridColumn title="saldo" property="saldo" dataType="currency" width="100" />
		<adsm:gridColumn title="modalidade" property="tpModalidade" isDomain="true" width="100" />
		<adsm:gridColumn title="origem" property="tpOrigem" isDomain="true" width="100" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="100" />
		<adsm:gridColumn title="cpfCnpj" property="dsCpfCnpj" dataType="text" width="100" />
		<adsm:gridColumn title="nomeRazaoSocial" property="dsNomeRazaoSocial" dataType="text" width="100" />
		<adsm:gridColumn title="numeroBoleto" property="dsBoleto" dataType="text" width="100" />
		<adsm:gridColumn title="observacoes" property="obCreditoBancario" dataType="text" width="100"/>
		<adsm:gridColumn title="dataHoraAlteracao" dataType="JTDateTimeZone" property="dhAlteracao" width="100" />
		<adsm:gridColumn title="usuarioAlteracao" property="usuario.usuarioADSM.nmUsuario" dataType="text" width="100" />
	</adsm:grid>

	<adsm:buttonBar>
	</adsm:buttonBar>

</adsm:window>

<script>

	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);		
		findFilialUsuario();
	}

	function findFilialUsuario(){
		if (getElement("filial.idFilial").masterLink == undefined){
			var dados = new Array();
	        
	        var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao", "findFilialUsuario", dados);
	        xmit({serviceDataObjects:[sdo]});
        }	
	}

	function findFilialUsuario_cb(data,erro){
		if (erro == undefined){
			setElementValue("filial.idFilial", data.filialByIdFilial.idFilial);
			setElementValue("filial.sgFilial", data.filialByIdFilial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);
		}			

		if(data.filialByIdFilial.sgFilial != 'MTZ'){
			setDisabled('filial.idFilial',true);
		}else{
			setDisabled('filial.idFilial',false);
		}
		
	}

	function limpaCampos() {
		cleanButtonScript(this.document);
		findFilialUsuario();
	}

</script>