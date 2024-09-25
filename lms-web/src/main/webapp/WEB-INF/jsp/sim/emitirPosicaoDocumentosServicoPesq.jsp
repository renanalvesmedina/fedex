<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/emitirPosicaoDocumentosServico" >
		
		<adsm:combobox property="regional" label="regional" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" required="false" />		
		<adsm:combobox property="filial" label="filial" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" required="false" />				
		
		<adsm:lookup service="" dataType="text" property="funcionario.id" criteriaProperty="funcionario.codigo" label="remetente" size="6" maxLength="10" labelWidth="20%" width="11%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nomeFuncionario" formProperty="nomeFuncionario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="nomeFuncionario" size="20" maxLength="50" disabled="true" width="19%"  />		

		<adsm:lookup service="" dataType="text" property="funcionario.id" criteriaProperty="funcionario.codigo" label="destinatario" size="6" maxLength="10" labelWidth="20%" width="11%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nomeFuncionario" formProperty="nomeFuncionario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="nomeFuncionario" size="20" maxLength="50" disabled="true" width="19%"  />
		
		<adsm:combobox property="servico" label="servico" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" required="false" />

		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" required="true" >
             <adsm:textbox dataType="date" property="periodoEmissaoInicial"  />
             <adsm:textbox dataType="date" property="periodoEmissaoFinal" />
        </adsm:range>

		
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/sim/emitirPosicaoDocumentosServico.jasper" />
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>

		
	</adsm:form>	
</adsm:window>

