<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterStatusPendencia">
  		<adsm:range label="periodo" labelWidth="18%" width="85%" required="true">
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" required="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true" required="true" />
		</adsm:range>
		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" cmd="main" service="" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="false">
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:lookup property="cliente.id" criteriaProperty="cliente" dataType="text" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" size="18" maxLength="18" labelWidth="18%" width="85%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox property="nomeCliente" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox property="tipoCliente" label="tipoCliente" optionLabelProperty="" optionProperty="" prototypeValue="Remetente|Destinatário" service="" labelWidth="18%" width="79%" disabled="false"/>
		<adsm:combobox property="localizacao" label="localizacao" optionLabelProperty="" optionProperty="" prototypeValue="Agendamento|A disposição" service="" labelWidth="18%" width="79%" disabled="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" selectionMode="none" showPagging="true" scrollBars="horizontal" unique="true" rows="9">
		<adsm:gridColumn property="documentoServico" title="documentoServico" width="130" />
		<adsm:gridColumn property="remetente" title="remetente" width="240" />
		<adsm:gridColumn property="destinatario" title="destinatario" width="240" />
		<adsm:gridColumn property="dataEmissao" title="dataEmissao" width="120" align="center"/>
		<adsm:gridColumn property="volumes" title="volumes" width="90" align="right"/>
		<adsm:gridColumn property="peso" title="pesoKG" width="70" align="right"/>
		<adsm:gridColumn property="valorMercadoria" title="valorMercadoriaReais" width="145" />
		<adsm:gridColumn property="valorFrete" title="valorFreteReais" width="120" />
		<adsm:gridColumn property="dataBloqueio" title="dataBloqueio" width="140" align="center"/>
		<adsm:gridColumn property="dataLiberacao" title="dataLiberacao" width="110" align="center"/>
		<adsm:gridColumn property="qtdDiasBloqueio" title="qtdDiasBloqueio" width="115" align="right"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="/pendencia/emitirTempoMedioArmazenagem.jasper" caption="emitir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>