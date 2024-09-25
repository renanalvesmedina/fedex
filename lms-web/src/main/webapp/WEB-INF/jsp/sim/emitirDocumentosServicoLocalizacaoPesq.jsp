<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/emitirDocumentosServicoLocalizacao" >
		<adsm:combobox property="regionalOrigem" label="regionalOrigem" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" required="false" />		
		<adsm:combobox property="regionalDestino" label="regionalDestino" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" required="false" />		
		<adsm:combobox property="filialOrigem" label="filialOrigem" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" required="false" />				
		<adsm:combobox property="filialDestino" label="filialDestino" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" required="false" />
		<adsm:lookup service="" dataType="text" property="funcionario.id" criteriaProperty="funcionario.codigo" label="remetente" size="6" maxLength="10" labelWidth="20%" width="11%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nomeFuncionario" formProperty="nomeFuncionario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="nomeFuncionario" size="20" maxLength="50" disabled="true" width="19%"  />		


		<adsm:lookup service="" dataType="text" property="funcionario.id" criteriaProperty="funcionario.codigo" label="destinatario" size="6" maxLength="10" labelWidth="20%" width="11%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nomeFuncionario" formProperty="nomeFuncionario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="nomeFuncionario" size="20" maxLength="50" disabled="true" width="19%"  />
		
		<adsm:combobox property="localizacao" label="localizacao" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" required="true" />
		<adsm:range label="periodoLocalizacao" labelWidth="20%" width="80%" required="true">
			<adsm:textbox dataType="dateTime" property="localizacaoInicial"/>
			<adsm:textbox dataType="dateTime" property="localizacaoFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/sim/emitirDocumentosServicoLocalizacao.jasper" />
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>
