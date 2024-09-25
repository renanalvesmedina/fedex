<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacaoDetalhadaWeb" height="420">
		<adsm:combobox property="servico" label="servico" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="78%"/>
		<adsm:textbox dataType="text" property="nf" size="28" width="78%" label="notaFiscal" labelWidth="19%" required="false"/>
		<adsm:combobox property="tpDocumentoServico" label="tipoDocumentoServico" prototypeValue="CTRC|CRT|NFS|MDA|RRE" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="80%" cellStyle="vertical-align:bottom;" />	

		<adsm:textbox label="documentoServico" labelWidth="19%" width="8%" size="6" maxLength="3" dataType="text" property="filialOrigem" />
        <adsm:textbox width="10%" size="10" dataType="text" property="numeroDocumento"/>
        <adsm:label key="hifen" width="2%" style="border:none;text-align:center"/>
        <adsm:lookup action="" dataType="text" property="numeroDocumentoCompl" service="" width="59%" size="5" style="width: 15px;"/>

		<adsm:lookup service="" dataType="text" property="responsavel.id" criteriaProperty="responsavel.codigo" label="remetente" size="11" maxLength="20" labelWidth="19%" width="14%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="25" maxLength="50" disabled="true" width="50%"  />

		<adsm:lookup service="" dataType="text" property="responsavel.id" criteriaProperty="responsavel.codigo" label="destinatario" size="11" maxLength="20" labelWidth="19%" width="14%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="25" maxLength="50" disabled="true" width="50%"  />

		<adsm:lookup service="" dataType="text" property="responsavel.id" criteriaProperty="responsavel.codigo" label="responsavelFrete" size="11" maxLength="20" labelWidth="19%" width="14%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="25" maxLength="50" disabled="true" width="50%"  />

		<adsm:range label="periodoEmbarque" labelWidth="19%" width="80%">
			<adsm:textbox dataType="date" property="periodoGeracaoInicial"/>
			<adsm:textbox dataType="date" property="periodoGeracaoFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   