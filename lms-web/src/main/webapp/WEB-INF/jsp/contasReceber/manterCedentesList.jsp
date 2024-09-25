<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterCedentesAction">
	<adsm:form action="/contasReceber/manterCedentes">
		
		<adsm:textbox 
			dataType="text" 
			property="dsCedente" 
			labelWidth="20%" 
			width="80%" 
			label="descricao" 
			size="60" 
			maxLength="60"/>

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
			onchange="return bancoChange(this);"
			action="/configuracoes/manterBancos">			
 				
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				formProperty="agenciaBancaria.banco.nmBanco"/> 
				
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
			action="/configuracoes/manterAgencias">			
			
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.idBanco" 
				modelProperty="banco.idBanco" addChangeListener="false"/>
				
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.nmBanco" 
				modelProperty="banco.nmBanco" 
				inlineQuery="false" 
				addChangeListener="false"/>
				
			<adsm:propertyMapping 
				criteriaProperty="agenciaBancaria.banco.nrBanco" 
				modelProperty="banco.nrBanco" 
				inlineQuery="false" 
				addChangeListener="false"/>
				
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
				modelProperty="nmAgenciaBancaria" 
				formProperty="agenciaBancaria.nmAgenciaBancaria"/> 
				
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
			maxLength="12"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrContaCorrente" 
			labelWidth="17%" 
			width="33%" 
			label="conta" 
			size="12" 
			maxLength="12"/>

		<adsm:textbox 
			label="vigencia"
			dataType="JTDate" 
			labelWidth="20%" 
			width="30%" 
			property="dataVigencia" 
			picker="true"/>
	    	
	    <adsm:buttonBar freeLayout="true">
			<adsm:button 
				disabled="false"
				buttonType="findButton" 
				caption="consultar" 
				onclick="validateCedente(this.form)"/>
				
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		selectionMode="check" 
		idProperty="idCedente" 
		property="cedente" 
		service="lms.contasreceber.manterCedentesAction.findPaginatedByCedente"
		rowCountService="lms.contasreceber.manterCedentesAction.getRowCountByCedente"
		gridHeight="200" 
		unique="true" 
		rows="11">
	
		<adsm:gridColumn 
			width="60%" 
			title="descricao" 
			property="dsCedente"/>
			
		<adsm:gridColumn 
			width="20%" 
			title="contaCorrente" 
			property="nrContaCorrente" 
			align="right"/>
			
		<adsm:gridColumn 
			width="10%" 
			title="carteira" 
			property="nrCarteira" 
			align="right"/>
			
		<adsm:gridColumn 
			width="78" 
			dataType="JTDate"
			title="vigencia" 
			property="dtVigenciaInicial" 
			/>
			
		<adsm:gridColumn 
			width="78"
			dataType="JTDate"
			title=""
			property="dtVigenciaFinal" 
			/>
			
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	
	</adsm:grid>
	
</adsm:window>

<script>
	
	function bancoChange(e){

		if (e.value == e.previousValue)
		   return true;

		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));
		
		return agenciaBancaria_banco_nrBancoOnChangeHandler();
		
	}
	
	function validateCedente(form){
		findButtonScript('cedente', form);
	}
	
</script>