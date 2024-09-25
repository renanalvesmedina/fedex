<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/inserirParametros" height="370">
		<adsm:textbox dataType="text" property="rotaOrigemId" label="origem" size="80" labelWidth="23%" width="77%"  disabled="true" />
		<adsm:textbox dataType="text" property="rotaDestinoId" label="destino" size="80" labelWidth="23%" width="77%" disabled="true" />
		<adsm:combobox property="moedaId" label="moeda" optionLabelProperty="" optionProperty="" service="" required="true" disabled="true" labelWidth="23%" width="77%" />

		<adsm:section caption="fretePeso" />

		<adsm:combobox property="fretePesoIndicador" label="minimoFretePeso" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Peso|Valor" required="true" labelWidth="23%" width="29%">		
		<adsm:textbox dataType="decimal" property="fretePesoValor" maxLength="15" size="15" />
		</adsm:combobox>

		<adsm:combobox property="percentualMinimoProgressivoIndicador" label="percentualMinimoProgressivo" service="" optionLabelProperty="" optionProperty="" prototypeValue="Acréscimo|Desconto" required="true" labelWidth="19%" width="29%">		
		<adsm:textbox dataType="decimal" property="percentualMinimoProgressivoValor" maxLength="15" size="15" />
		</adsm:combobox>

		<adsm:combobox property="fretePesoIndicador" label="fretePeso" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo" required="true" labelWidth="23%" width="77%">		
		<adsm:textbox dataType="decimal" property="fretePesoValor" maxLength="15" size="15" />
		</adsm:combobox>


		<adsm:textbox dataType="decimal" property="minimoFreteQuilo" label="valorMinimoFreteQuilo" maxLength="15" size="15" labelWidth="23%" width="29%" required="true" />
		<adsm:textbox dataType="decimal" property="freteVolume" label="valorFreteVolume" maxLength="15" size="15" labelWidth="19%" width="29%" required="true" />
		<adsm:combobox property="tarifaMinima" label="tarifaMinima" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo" required="true" labelWidth="23%" width="29%">		
		<adsm:textbox dataType="decimal" property="valorTarifaMinima" maxLength="15" size="15" />
		</adsm:combobox>		
		<adsm:checkbox property="pagaPesoExcedente" label="pagaPesoExcedente" labelWidth="19%" width="29%"/>  		

		<adsm:checkbox property="pagaCubagemIndicador" labelWidth="23%" width="29%" label="pagaCubagem" />  
		<adsm:textbox dataType="decimal" property="pagaCubagemPercentual" label="percentualCubagem" labelWidth="19%" width="29%" maxLength="3" size="5" required="true" />

		<adsm:combobox property="tarifaMinima" label="valorTarifaEspecifica" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo" required="true" labelWidth="23%" width="29%">		
		<adsm:textbox dataType="decimal" property="valorTarifaMinima" maxLength="15" size="15" />
		</adsm:combobox>		

		<adsm:combobox property="produtoEspecificoId" label="tarifaEspecifica" optionLabelProperty="" optionProperty="" service="" labelWidth="19%" width="29%" />		

<adsm:section caption="freteValor" />
		<adsm:combobox property="advaloremIndicador" label="advalorem1" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo|Pontos" required="true" labelWidth="23%" width="77%">		
			<adsm:textbox dataType="decimal" property="advaloremValor" maxLength="15" size="15" />
		</adsm:combobox>
		<adsm:combobox property="advalorem2Indicador" label="advalorem2" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo|Pontos" required="true" labelWidth="23%" width="77%">		
			<adsm:textbox dataType="decimal" property="advalorem2Valor" maxLength="15" size="15" />
		</adsm:combobox>
		<adsm:combobox property="valorReferenciaIndicador" label="valorReferencia" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo" required="true" labelWidth="23%" width="77%">		
			<adsm:textbox dataType="decimal" property="valorReferenciaValor" maxLength="15" size="15" />
		</adsm:combobox>

		<adsm:section caption="fretePercentual" />
		<adsm:textbox dataType="decimal" property="percentualFretePercentual" label="percentualFretePercentual" maxLength="15" size="15" labelWidth="28%" width="62%" required="true" />
		<adsm:textbox dataType="decimal" property="valorMinimoFretePercentual" label="valorMinimoFretePercentual" maxLength="15" size="15" labelWidth="28%" width="62%" required="true" />		
		<adsm:textbox dataType="decimal" property="valorToneladaFretePercentual" label="valorToneladaFretePercentual" maxLength="15" size="15" labelWidth="28%" width="62%" required="true" />		
		<adsm:textbox dataType="decimal" property="pesoFretePercentual" label="pesoReferenciaFretePercentual" maxLength="15" size="15" labelWidth="28%" width="62%" required="true" unit="kg" />		

		<adsm:section caption="gris" />
		<adsm:combobox property="percentualGrisIndicador" label="percentualGris" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo|Pontos" required="true" labelWidth="28%" width="62%">		
			<adsm:textbox dataType="decimal" property="percentualGrisValor" maxLength="15" size="15" />
		</adsm:combobox>
		<adsm:combobox property="minimoGrisIndicador" label="minimoGris" service="" optionLabelProperty="" optionProperty="" prototypeValue="Tabela|Valor|Desconto|Acréscimo" required="true" labelWidth="28%" width="62%">		
			<adsm:textbox dataType="decimal" property="minimoGrisValor" maxLength="15" size="15" />
		</adsm:combobox>

		<adsm:section caption="totalFrete" />
		<adsm:textbox dataType="decimal" property="percentualFreteTotal" label="percentualDescontoFreteTotal" maxLength="15" size="15" labelWidth="28%" width="62%" required="true" />

    	<adsm:buttonBar>				
			<adsm:button caption="salvarParametros"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   