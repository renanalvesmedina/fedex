<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.pedidoColetaService">

	<adsm:form action="/coleta/cadastrarPedidoColetaCliente" height="245">

		<adsm:masterLink>
			<adsm:masterLinkItem property="numeroColeta" label="numeroColeta" itemWidth="50"/>
		</adsm:masterLink>

		<adsm:combobox label="servico" property="servico" optionLabelProperty="" optionProperty="" service="" required="true" labelWidth="15%" width="35%"/>
		<adsm:combobox label="natureza" property="natureza" optionLabelProperty="" optionProperty="" service="" required="true" labelWidth="15%" width="35%"/>

		<adsm:combobox label="destino" property="destino" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="35%" prototypeValue="Município|Localidade especial" required="true"/>
		<adsm:lookup label="localidadeEspecial" idProperty="" action="/municipios/manterMunicipios" dataType="text" service="" property="municipioDestino" size="35" labelWidth="15%" width="35%"/>

		<adsm:combobox label="uf" property="uf" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="35%"/>
		<adsm:lookup label="municipio" idProperty="" action="/municipios/manterMunicipios" dataType="text" service="" property="municipioDestino" size="35" labelWidth="15%" width="35%"/>

		<adsm:lookup property="destinatario.id" idProperty="" label="destinatario" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" labelWidth="15%" width="85%" required="false">
			<adsm:propertyMapping modelProperty="destinatario.id" formProperty="nomeDestinatario" />
			<adsm:textbox dataType="text" property="nomeDestinatario" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox label="tipoFrete" property="tipoFrete" optionLabelProperty="" optionProperty="" service="" prototypeValue="CIF|FOB" required="true" labelWidth="15%" width="35%"/>
		<adsm:textbox label="peso" property="peso" dataType="weight" unit="kg" required="true" labelWidth="15%" width="35%" size="6" maxLength="10"/>

		<adsm:combobox label="valor" property="moeda" optionLabelProperty="" optionProperty="" service="" labelWidth="15%" width="12%"/>
		<adsm:textbox property="valor" dataType="currency" width="23%" required="true"/>
		<adsm:textbox label="volumes" property="volumes" dataType="decimal" labelWidth="15%" width="35%" required="true"/>

		<adsm:listbox property="notaFiscalList" optionLabelProperty="" optionProperty="" service="" size="5" boxWidth="120" labelWidth="15%" width="35%" label="notaFiscal">
			<adsm:textbox property="notaFiscal" dataType="text" />
		</adsm:listbox>
 
		<adsm:listbox label="awb" property="awbList" optionLabelProperty="" optionProperty="" service="" size="5" boxWidth="120">		
			<adsm:lookup property="awb" idProperty="" action="expedicao/consultarAWBs"  dataType="text" service=""/>
		</adsm:listbox>

		<adsm:textarea label="observacao" property="observacao" maxLength="500" columns="65" rows="3" labelWidth="15%" width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="popUpPesosAforadosTemp" onclick="showModalDialog('coleta/cadastrarPedidoColeta.do?cmd=pesoAforado',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:300px;dialogHeight:215px;');" />
			<adsm:button caption="novoItem"/>
			<adsm:button caption="salvarItem"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid property="" idProperty="" selectionMode="check" scrollBars="horizontal" gridHeight="42" rows="2" unique="true" >
		<adsm:gridColumn title="servico" property="servico" width="200"/>
		<adsm:gridColumn title="naturezaMercadoria" property="naturezaMercadoria" width="170"/>
		<adsm:gridColumn title="frete" property="frete" width="50"/>
		<adsm:gridColumn title="volumes" property="volumes" width="80" align="right"/>
		<adsm:gridColumn title="peso" property="peso" unit="kg" width="105"/>
		<adsm:gridColumn title="pesoAforado" property="pesoAforado" unit="kg" width="110"/>
		<adsm:gridColumn title="valor" property="valor" width="105"/>
		<adsm:gridColumn title="notaFiscal" property="notaFiscal" image="popup.gif" link="/coleta/cadastrarPedidoColeta.do?cmd=listaNota" popupDimension="380,240" width="80" />
		<adsm:gridColumn title="awb"        property="awb"        image="popup.gif" link="/coleta/cadastrarPedidoColeta.do?cmd=listaAWB" popupDimension="380,240" width="80" />
		<adsm:gridColumn title="destinatario" property="destinatario" width="300"/>
		<adsm:gridColumn title="municipioDestino" property="municipioDestino" width="130"/>
		<adsm:gridColumn title="localidadeEspecial" property="localidadeEspecial" width="200"/>
		<adsm:gridColumn title="filialDestino" property="filialDestino" width="105"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="excluirItem"/>
	</adsm:buttonBar>
</adsm:window>
