<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/incluirExcluirCRT" height="370">

		<adsm:crt property="numeroCRT" label="numeroCRT23" required="true" labelWidth="20%" width="30%" />
	    <adsm:lookup action="/" service="" dataType="integer" property="remetente.codigo" criteriaProperty="remetente.nome" label="aduanaDestino24" size="20" maxLength="20" required="true" labelWidth="23%" width="27%"/>

		<adsm:combobox property="moeda" label="moeda25" service="" optionLabelProperty="" optionProperty="" prototypeValue="" required="true" disabled="true" labelWidth="20%" width="30%"/>
		<adsm:textbox property="origemMercadoria26" dataType="text" label="origemMercadoria26" size="10" maxLength="10" required="true" disabled="true" labelWidth="23%" width="27%" />

		<adsm:textbox property="valorFOT27" dataType="text" label="valorFOT27" size="10" maxLength="10" labelWidth="20%" width="30%" required="true" disabled="true"/>
		<adsm:textbox property="valorFrete28" dataType="text" label="valorFrete28" size="10" maxLength="10" labelWidth="23%" width="27%" disabled="true" required="true"/>

		<adsm:textbox property="valorSeguro29" dataType="text" label="valorSeguro29" size="10" maxLength="10" labelWidth="20%" width="30%" disabled="true" required="true"/>
		<adsm:combobox property="tipoVolumes30" label="tipoVolumes30" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="23%" width="27%" disabled="true" required="true"/>

		<adsm:textbox property="qtdeVolumes31" dataType="text" label="qtdeVolumes31" size="7" maxLength="10" labelWidth="20%" width="15%" required="true"/>
		<adsm:textbox property="pesoBruto32" dataType="text" label="pesoBruto32" unit="kg" size="7" maxLength="10" labelWidth="13%" width="20%" required="true"/>
		<adsm:textbox property="pesoLiquido32" dataType="text" label="pesoLiquido32" size="7" unit="kg" maxLength="10" labelWidth="14%" width="15%" required="true"/>

		<adsm:section caption="dadosRemetente33" />
	    <adsm:lookup action="/vendas/manterDadosIdentificacao" service="" dataType="integer" property="remetente.codigo" criteriaProperty="remetente.nome" label="remetente" size="14" maxLength="14" width="49%" labelWidth="20%" required="true" disabled="true">
		   	<adsm:propertyMapping modelProperty="remetente.codigo" formProperty="remetente.nome"/>
            <adsm:textbox dataType="text" property="remetente.nome" size="40" maxLength="40" disabled="true"/>
        </adsm:lookup>
		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="20%" />
        <adsm:textarea maxLength="600" property="dadosRemetente" width="70%" columns="82" rows="4" disabled="true" required="true"/>

		<adsm:section caption="dadosDestinatario34" />
	    <adsm:lookup action="/vendas/manterDadosIdentificacao" service="" dataType="integer" property="destinatario.codigo" criteriaProperty="destinatario.nome" label="destinatario" size="14" maxLength="14" width="49%" labelWidth="20%" required="true" disabled="true">
		   	<adsm:propertyMapping modelProperty="destinatario.codigo" formProperty="destinatario.nome"/>
            <adsm:textbox dataType="text" property="destinatario.nome" size="40" maxLength="40" disabled="true"/>
        </adsm:lookup>
		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="20%" />
        <adsm:textarea maxLength="600" property="dadosDestinatario" width="70%" columns="82" rows="4" disabled="true" required="true"/>

 		<adsm:section caption="dadosConsignatario35" />
	    <adsm:lookup action="/vendas/manterDadosIdentificacao" service="" dataType="integer" property="consignatario.codigo" criteriaProperty="consignatario.nome" label="consignatario" size="14" maxLength="14" width="49%" labelWidth="20%" required="true" disabled="true">
		   	<adsm:propertyMapping modelProperty="consignatario.codigo" formProperty="consignatario.nome"/>
            <adsm:textbox dataType="text" property="consignatario.nome" size="40" maxLength="40" disabled="true"/>
        </adsm:lookup>
		<adsm:label key="branco" style="border:none;" width="31%" />
		<adsm:label key="branco" style="border:none;" width="20%" />
        <adsm:textarea maxLength="600" property="dadosConsignatario" width="70%" columns="82" rows="3" disabled="true" required="true"/>

 		<adsm:section caption="dadosGerais" />

        <adsm:textarea maxLength="600" label="documentosAnexos36" property="dadosNotificado" labelWidth="20%" width="70%" columns="82" rows="3" disabled="true"/>

        <adsm:textarea maxLength="600" label="mercadorias38" property="dadosNotificado" labelWidth="20%" width="70%" columns="82" rows="3" required="true" disabled="true"/>
 
		<adsm:buttonBar lines="1"> 
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar" />
			<adsm:button caption="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 