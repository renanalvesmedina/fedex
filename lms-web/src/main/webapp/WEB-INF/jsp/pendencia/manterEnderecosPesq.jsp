<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterEnderecos">
		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" required="true" >
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial" />
			<adsm:textbox dataType="text" property="nomeFilial" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="terminal" label="terminal" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="" width="35%" />
		<adsm:textbox property="modulo" label="modulo" dataType="integer" size="4" maxLength="4" width="35%" />

		<adsm:textbox property="rua" label="rua" dataType="integer" size="4" maxLength="4" width="35%" />
		<adsm:textbox property="predio" label="predio" dataType="integer" size="4" maxLength="4" width="35%" />

		<adsm:textbox property="andar" label="andar" dataType="integer" size="4" maxLength="4" width="35%" />
		<adsm:textbox property="apartamento" label="apartamento" dataType="integer" size="4" maxLength="4" width="35%" />

		<adsm:combobox property="situacao" label="situacao" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Livre|Ocupado|Bloqueado|Inacessível" width="85%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" gridHeight="200" unique="true">
		<adsm:gridColumn property="terminal" title="terminal" align="right" />
		<adsm:gridColumn property="modulo" title="modulo" align="right" />
		<adsm:gridColumn property="rua" title="rua" align="right" />
		<adsm:gridColumn property="andar" title="andar" align="right" />
		<adsm:gridColumn property="apartamento" title="apartamento" align="right" />
		<adsm:gridColumn property="situacao" title="situacao" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
			<adsm:button caption="bloquear"/>
			<adsm:button caption="desbloquear"/>
			<adsm:button caption="marcarComoInacessivel"/>
			<adsm:button caption="gerarNovosEnderecos" onclick="showModalDialog('pendencia/gerarNovosEnderecos.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:230px;');"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>