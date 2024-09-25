<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.contasreceber.manterCedentesAction">

	<adsm:form action="/contasReceber/manterCedentes" idProperty="idCedente">
		
		<adsm:hidden property="tpSituacaoInativo" value="A" />
		
		<adsm:textbox 
			dataType="text" 
			property="dsCedente" 
			labelWidth="20%" 
			width="80%" 
			label="descricao" 
			size="60" 
			maxLength="60"
			required="true"/>

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria.banco" 
			idProperty="idBanco" 
			service="lms.configuracoes.bancoService.findLookup" 
			label="banco" 
			size="5" 
			maxLength="3"
			labelWidth="20%" 
			width="9%"
			criteriaProperty="nrBanco" 
			required="true"
			serializable="true"
			action="/configuracoes/manterBancos">			

 			<adsm:propertyMapping 
 				criteriaProperty="tpSituacaoInativo" 
 				modelProperty="tpSituacao"/>
 				
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				relatedProperty="agenciaBancaria.banco.nmBanco"/> 
				
			<adsm:textbox 
				property="agenciaBancaria.banco.nmBanco" 
				dataType="text" 
				size="25" 
				width="21%"
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>		

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria" 
			idProperty="idAgenciaBancaria"
			service="lms.configuracoes.agenciaBancariaService.findLookup" 
			label="agencia" 
			maxLength="4" 
			size="7" 
			labelWidth="17%"
			width="11%"
			criteriaProperty="nrAgenciaBancaria"
			action="/configuracoes/manterAgencias"
			required="true">
			
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.idBanco" 
				modelProperty="banco.idBanco"
				/>
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.nrBanco" 
				modelProperty="banco.nrBanco" 
				inlineQuery="false" 
				/>
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.nmBanco" 
				modelProperty="banco.nmBanco" 
				inlineQuery="false" 
				/>
				
			<adsm:propertyMapping 
				relatedProperty="agenciaBancaria.banco.idBanco" 
				modelProperty="banco.idBanco"
				blankFill="false"
				/>
			<adsm:propertyMapping 
				relatedProperty="agenciaBancaria.banco.nrBanco" 
				modelProperty="banco.nrBanco" 
				inlineQuery="false" 
				blankFill="false"
				/>
			<adsm:propertyMapping 
				relatedProperty="agenciaBancaria.banco.nmBanco" 
				modelProperty="banco.nmBanco" 
				inlineQuery="false" 
				blankFill="false"
				/>
				
 			<adsm:propertyMapping 
 				criteriaProperty="tpSituacaoInativo" 
 				modelProperty="tpSituacao"/>

			<adsm:propertyMapping 
				modelProperty="nmAgenciaBancaria" 
				relatedProperty="agenciaBancaria.nmAgenciaBancaria"/>			
				
			<adsm:textbox 
				property="agenciaBancaria.nmAgenciaBancaria" 
				dataType="text" 
				size="25" 
				width="22%"
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>


		<adsm:textbox 
			dataType="integer" 
			property="cdCedente" 
			labelWidth="20%" 
			width="30%" 
			label="cedente" 
			size="12" 
			maxLength="12" 
			required="true"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrContaCorrente" 
			labelWidth="17%" 
			width="33%" 
			label="conta" 
			size="12" 
			maxLength="12" 
			required="true"/>

		<adsm:range 
			label="vigencia" 
			labelWidth="20%" 
			width="30%">
			
			<adsm:textbox 
				label="vigencia"
				dataType="JTDate" 
				property="dtVigenciaInicial" 
				picker="true"
				required="true"/>
	    	
	    	<adsm:textbox 
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal" 
	    		picker="true"/>
	    		
		</adsm:range>

		<adsm:textbox 
			dataType="integer" 
			property="sqCobranca" 
			labelWidth="17%" 
			width="33%" 
			label="sequenciaCobranca" 
			size="10" 
			maxLength="10" 
			required="true"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrCarteira" 
			labelWidth="20%" 
			width="80%" 
			label="carteira" 
			size="3" 
			maxLength="3" />

		<adsm:textbox 
			dataType="JTDate" 
			property="dtUltimaRemessaCobranca" 
			picker="false" 
			label="dataUltimaRemessa" 
			labelWidth="20%" 
			width="30%" 
			disabled="true"/>

		<adsm:textbox 
			dataType="JTDate" 
			property="dtUltimoRetornoCobranca" 
			picker="false" 
			label="dataUltimaRetorno" 
			labelWidth="17%" 
			width="33%" 
			disabled="true"/>

		<adsm:textbox 
			dataType="text" 
			property="dsNomeArquivoCobranca" 
			labelWidth="20%" 
			width="80%" 
			label="arquivo" 
			size="60" 
			maxLength="60"/>

		<adsm:buttonBar>
		
            <adsm:button caption="associarCedentesEmpresasButton" action="/contasReceber/associarCedentesEmpresas" boxWidth="180" cmd="main">
            	<adsm:linkProperty src="idCedente" target="idCedente"/>
            </adsm:button>
            
			<adsm:button caption="vincularModalAbrangencia" action="/contasReceber/vincularCedenteModalAbrangencia" boxWidth="180" cmd="main">
				<adsm:linkProperty src="idCedente" target="idCedente"/>
            </adsm:button>
            
			<adsm:storeButton/>
			
			<adsm:newButton/>
			
			<adsm:removeButton/>
			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function bancoChange(e){

		if (e.value == e.previousValue)
		   return true;

		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));

		return agenciaBancaria_banco_nrBancoOnChangeHandler();

	}
	
	document.getElementById("agenciaBancaria.banco.nrBanco").serializable = true;
</script>