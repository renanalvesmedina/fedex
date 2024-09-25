<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/portaria/definirAreaDescargaEstacionamento">
		<adsm:textbox dataType="text" property="empresaId" size="15" disabled="true" label="empresa" labelWidth="30%" width="15%" />
		<adsm:textbox dataType="text" property="empresa" size="30" disabled="true" width="55%" required="true" />
		
		<adsm:textbox dataType="text" property="filialId" size="15" disabled="true" label="filial" labelWidth="30%" width="15%" />	
		<adsm:textbox dataType="text" property="filial" size="30" disabled="true" width="55%" required="true" />	

		<adsm:textbox dataType="text" property="terminal" size="20" disabled="true" label="terminal" labelWidth="30%" width="70%" required="true"/>
		<adsm:textbox dataType="text" property="portariaId" size="20" disabled="true" label="portaria" labelWidth="30%" width="70%" required="true"/>
		
		<adsm:section caption="encaminhamento"/>
		
		<adsm:textbox dataType="text" property="idMeioTransporte" size="15" disabled="true" label="identificacaoMeioTransporte" labelWidth="30%" width="15%" />
		<adsm:textbox dataType="text" property="frotaMeioTransporte" size="15" disabled="true" width="55%" required="true" />
		
		<adsm:textbox dataType="text" property="idSemiReboque" size="15" disabled="true" label="identificacaoDoSemiReboque" labelWidth="30%" width="15%" />
		<adsm:textbox dataType="text" property="idSemiReboque" size="15" disabled="true" width="55%" />
	
		<adsm:combobox property="docaId" optionLabelProperty="" optionProperty="" service="" label="doca"  labelWidth="30%" width="70%" required="true" />
		<adsm:combobox property="boxId" optionLabelProperty="" optionProperty="" service="" label="box" labelWidth="30%" width="70%" required="true" />
        <adsm:buttonBar>
			<adsm:button caption="estacionamento" action="portaria/informarChegada" cmd="main"/>
			<adsm:button caption="encaminhar" action="portaria/informarChegada" cmd="main"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>   