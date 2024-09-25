<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contasReceber/manterReciboOficial">
		<adsm:masterLink>
			<adsm:masterLinkItem property="filialCobranca" label="filialCobranca" />
			<adsm:masterLinkItem property="numeroRecibo" label="numeroRecibo" />
		</adsm:masterLink>

       <adsm:combobox property="tipoRecebimento" label="tipoRecebimento" labelWidth="20%" service="" prototypeValue="Peso|Dólar|Cheque peso|Cheque dólar" width="80%" optionLabelProperty="" optionProperty="" />

		<adsm:lookup action="/configuracoes/manterBancos" service="" dataType="integer" property="banco.id" criteriaProperty="banco.codigo" label="banco" size="3" maxLength="3" width="30%" labelWidth="20%" >
			<adsm:propertyMapping modelProperty="numBanco" formProperty="numBanco"/>
			<adsm:textbox dataType="text" property="nomeBanco" size="20" maxLength="30" disabled="true"/>		
		</adsm:lookup>

		<adsm:lookup action="/configuracoes/manterAgencias" service="" dataType="integer" property="agencia.id" criteriaProperty="agencia.codigo" label="agencia" size="4" maxLength="4" width="30%" labelWidth="20%" >
			<adsm:propertyMapping modelProperty="numAgencia" formProperty="numAgencia"/>
			<adsm:textbox dataType="text" property="nomeAgencia" size="20" maxLength="50" disabled="true"/>		
		</adsm:lookup>

		<adsm:textbox dataType="text" property="conta" labelWidth="20%" width="30%" label="conta" size="15" maxLength="15" />

       <adsm:textbox dataType="text" property="cheque"  label="cheque" maxLength="18" size="18" width="30%" labelWidth="20%" /> 
 	   <adsm:textbox dataType="text" property="valor"  label="valorRecebido" maxLength="18" size="18" width="30%" labelWidth="20%" required="true"/>
       <adsm:textbox dataType="JTDate" property="dataVencimento" labelWidth="20%" width="30%" label="dataVencimento" size="15" maxLength="15" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novoRecebimento"/>
			<adsm:button caption="salvarRecebimento"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true">
		<adsm:gridColumn title="tipoRecebimento" property="tipoRecebimento"/>
		<adsm:gridColumn title="banco" property="banco"/>
		<adsm:gridColumn title="agencia" property="agencia"/>
		<adsm:gridColumn title="conta" property="conta"/>
        <adsm:gridColumn title="cheque" property="cheque"/>     
        <adsm:gridColumn title="valorRecebido" property="valor"/>
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="excluirRecebimento"/>
	</adsm:buttonBar>

</adsm:window>