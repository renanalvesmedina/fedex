<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterArmazenagemMercadorias">
		
  		<adsm:range label="dataHoraEntrada" labelWidth="20%" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true" />
		</adsm:range>

		<adsm:textbox property="numeroVolume" label="numeroVolume" dataType="text" size="10" labelWidth="20%" width="80%" maxLength="10" />

		<adsm:lookup property="mda" label="mda" dataType="text" size="20" action="/pendencia/consultarMDA" cmd="mda" service="" labelWidth="20%" width="80%" />
	
		<adsm:textbox property="descricaoProduto" label="descricaoProduto" dataType="text" size="50" labelWidth="20%" width="40%" maxLength="50" />
		<adsm:textbox property="codProdCliente" label="codProdCliente" dataType="text" size="6" labelWidth="20%" width="20%" maxLength="3" />

		<adsm:lookup property="cliente.id" criteriaProperty="cliente" dataType="text" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" size="18" maxLength="18" labelWidth="20%" width="85%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox property="nomeCliente" dataType="text" size="40" maxLength="40" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox optionLabelProperty="disposicao" label="disposicao" optionProperty="disposicao" property="disposicao" service="disposicao" labelWidth="20%" width="40%" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar"/>
		<adsm:combobox property="natureza" label="natureza" labelWidth="20%" width="20%" optionLabelProperty="" optionProperty="" service=""/>

		<adsm:checkbox label="somenteIndenizadas" property="somenteIndenizadas" labelWidth="20%" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" unique="true" showPagging="true" rows="7">
		<adsm:gridColumn property="dataHoraEntrada" title="dataHoraEntrada" width="120" />
		<adsm:gridColumn property="volume1" title="numeroVolume" width="75" />
		<adsm:gridColumn property="volume2" title="" width="75" align="right" />
		<adsm:gridColumn property="mda" title="mda" width="100"/>
		<adsm:gridColumn property="terminal" title="terminal" width="100" />
		<adsm:gridColumn property="endereco" title="endereco" width="200" />
		<adsm:gridColumn property="dispositivo1" title="dispositivo" width="75" />
		<adsm:gridColumn property="dispositivo2" title="" width="75" align="right" />		
		<adsm:buttonBar/>
	</adsm:grid>
	<adsm:buttonBar> 
		<adsm:button caption="excluir"/>
	</adsm:buttonBar>	
</adsm:window>