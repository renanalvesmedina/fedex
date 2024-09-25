<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="volumes">
	<adsm:form action="/pendencia/manterArmazenagemMercadorias">
				
		<adsm:combobox optionLabelProperty="unidade" label="unidade" optionProperty="" property="" service="" labelWidth="20%" width="30%" />		
		<adsm:textbox label="quantidade" property="quantidade" dataType="integer" size="2" maxLength="2" labelWidth="20%" width="30%"/>
		
		<adsm:textbox dataType="text" property="numeroVolume" label="numeroVolume" labelWidth="20%" width="80%"/>
				
		<adsm:combobox label="endereco" property="endereco" optionLabelProperty="" optionProperty="" service="" labelWidth="20%" width="80%">				
			<adsm:textbox property="rua" dataType="integer" size="2" maxLength="2"/>
			<adsm:textbox property="bairro" dataType="integer" size="2" maxLength="2"/>
			<adsm:textbox property="predio" dataType="integer" size="2" maxLength="2"/>
			<adsm:textbox property="andar" dataType="integer" size="2" maxLength="2"/>
			<adsm:lookup property="apto" action="/" dataType="integer" service="" size="2" maxLength="2">
				<adsm:listbox property="enderecosList" optionLabelProperty="" optionProperty="" service="" size="6" prototypeValue="M1 - R5 - B2 - P1 - A4 - AP2|M1 - R5 - B3 - P1 - A5 - AP23|M1 - R5 - B1 - P2 - A7 - AP54|M1 - R5 - B4 - P6 - A4 - AP6" boxWidth="205"/>
			</adsm:lookup>
		</adsm:combobox>
				
		<adsm:combobox optionLabelProperty="dispositivo" label="dispositivo" optionProperty="" property="" service="" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="currency" property="dispositivo" />
		</adsm:combobox>		
	
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="limpar"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
	
</adsm:window>