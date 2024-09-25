<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirLoteSalvados">
	<adsm:form action="/pendencia/emitirProtocoloTransferencia">

		<adsm:lookup label="lote" property="lote" action="/pendencia/manterLotesSalvados" dataType="text" service="" size="12" maxLength="12" labelWidth="10%" width="57%" required="true"/>

		<adsm:combobox label="disposicao" property="disposicao" optionLabelProperty="" optionProperty="" service="" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar" disabled="true" labelWidth="10%" width="18%"/>

		<adsm:lookup label="comprador" property="comprador.id" action="/vendas/manterDadosIdentificacao" service="" dataType="text" size="18" maxLength="18" labelWidth="10%" width="90%" disabled="true">
			<adsm:propertyMapping modelProperty="comprador.id" formProperty="nomeComprador" />
			<adsm:textbox dataType="text" property="nomeComprador" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox label="destinatario" property="destinatario" optionLabelProperty="" optionProperty="" service="" prototypeValue="Cliente|Funcionário" labelWidth="10%" width="90%" required="true"/>

		<adsm:lookup label="cliente" dataType="text" property="cliente"	service="" action="coleta/cadastrarPedidoColeta" cmd="consultarClientes" size="18" maxLength="18" labelWidth="10%" width="57%" >
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true" />
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente" />
		</adsm:lookup>
		<adsm:textbox property="recebedor" label="recebedor" dataType="integer" size="15%" labelWidth="10%" width="23%" maxLength="5" />

		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" cmd="list" service="" dataType="text" size="3" maxLength="3" labelWidth="10%" width="90%" >
			<adsm:propertyMapping modelProperty="filialm.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup label="funcionario" dataType="text" property="funcionario"	service="" action="coleta/cadastrarPedidoColeta" cmd="consultarClientes" size="18" maxLength="18" labelWidth="10%" width="57%" >
			<adsm:textbox dataType="text" property="nomeFuncionario" size="50" maxLength="50" disabled="true" />
			<adsm:propertyMapping modelProperty="funcionario.id" formProperty="nomeFuncionario" />
		</adsm:lookup>
		<adsm:combobox label="setor" property="setor" optionLabelProperty="" optionProperty="" service="" prototypeValue="" labelWidth="10%" width="23%" />

		<adsm:buttonBar freeLayout="false">
			<adsm:reportViewerButton caption="emitir" service="pendencia/emitirProtocoloTransferenciaMercadoria.jasper"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>