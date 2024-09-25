<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.visualizarHistoricoNegociacoesClientesAction">
	<adsm:form
		action="/vendas/visualizarHistoricoNegociacoesClientes" 
		height="370"
		idProperty="idParametroCliente"
		onDataLoadCallBack="formLoad">

		<adsm:hidden property="zonaOrigem.dsZona" />
    	<adsm:hidden property="paisByIdPaisOrigem.nmPais" />
    	<adsm:hidden property="unidadeFederativaByIdUfOrigem.siglaDescricao" />
    	<adsm:hidden property="filialByIdFilialOrigem.sgFilial" />
    	<adsm:hidden property="municipioByIdMunicipioOrigem.municipio.nmMunicipio" />
    	<adsm:hidden property="aeroportoByIdAeroportoOrigem.sgAeroporto" />
    	<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" />

    	<adsm:hidden property="zonaDestino.dsZona" />
    	<adsm:hidden property="paisByIdPaisDestino.nmPais" />
    	<adsm:hidden property="unidadeFederativaByIdUfDestino.siglaDescricao" />
    	<adsm:hidden property="filialByIdFilialDestino.sgFilial" />
    	<adsm:hidden property="municipioByIdMunicipioDestino.municipio.nmMunicipio" />
    	<adsm:hidden property="aeroportoByIdAeroportoDestino.sgAeroporto" />
    	<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" />

		<%-------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%-------------------%>
		<adsm:complement
			label="cliente"
			labelWidth="18%"
			width="45%"
			separator="branco">

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacaoFormatado"
				serializable="false"
				size="18"
				maxLength="18"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false"
				size="36"/>
		</adsm:complement>
		
		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="divisao"
			labelWidth="130"
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			serializable="false"
			width="18%"/>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:textbox
			dataType="text" 
			disabled="true"
			label="tabela"
			labelWidth="18%"
			property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"
			serializable="false"
			size="60"
			width="78%"/>

		<%----------------%>
		<%-- ROTAS TEXT --%>
		<%----------------%>
		<adsm:textbox
			dataType="text"
			disabled="true" 
			label="origem"
			labelWidth="18%"
			property="rotaPreco.origemString"
			serializable="false"
			size="85"
			width="78%"/>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="destino"
			labelWidth="18%"
			property="rotaPreco.destinoString"
			serializable="false"
			size="85"
			width="78%"/>
		
		<%----------------------------------%>
		<%-- ESPECIFICACAO ROTA TEXT AREA --%>
		<%----------------------------------%>
		<adsm:textarea 
			columns="85" 
			label="especificacaoRota" 
			labelWidth="18%" 
			maxLength="500" 
			property="dsEspecificacaoRota" 
			rows="2" 
			width="78%" 
			disabled="true"
		/>
		
		<%--------------------%>
		<%-- SITUACAO COMBO --%>
		<%--------------------%>
		<adsm:combobox 
			disabled="true"
			label="situacao" 
			labelWidth="18%"
			property="tpSituacaoParametro" 
			width="45%"
			domain="DM_STATUS"
		/>

		<%-----------%>
		<%-- MOEDA --%>
		<%-----------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo" 
			serializable="false"
			label="moeda" 
			labelWidth="130" 
			width="18%" 
		/>
		
		<%--------------%>
		<%-- VIGENCIA --%>
		<%--------------%>
		<adsm:range 
			label="vigencia" 
			labelWidth="18%" 
			width="78%"
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal"
	        	disabled="true"
	        	picker="false"
	        />
	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"
	    		disabled="true"
	    		picker="false"
	    	/>
        </adsm:range>
        
        <%------------------------%>
		<%-- FRETE PESO SECTION --%>
		<%------------------------%>
		<adsm:section caption="fretePeso" />
		
		<%-----------------------------%>
		<%-- MINIMO FRETE PESO COMBO --%>
		<%-----------------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_FRETE_MINIMO" 
			label="minimoFretePeso" 
			labelWidth="23%"
			property="tpIndicadorMinFretePeso" 
			width="29%"
			disabled="true"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlMinFretePeso" 
				mask="###,###,###,###,##0.00"
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%-----------------------------------------%>
		<%-- PERCENTUAL MINIMO PROGRESSIVO COMBO --%>
		<%-----------------------------------------%>
		<adsm:combobox 
			domain="DM_ACRESCIMO_DESCONTO" 
			label="percentualMinimoProgressivo" 
			labelWidth="19%"
			property="tpIndicadorPercMinimoProgr" 
			width="29%"
			disabled="true"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlPercMinimoProgr" 
				mask="###,##0.00"
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%----------------------%>
		<%-- FRETE PESO COMBO --%>
		<%----------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="fretePeso" 
			labelWidth="23%"
			property="tpIndicadorFretePeso" 
			width="29%"
			disabled="true"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlFretePeso" 
				mask="###,###,###,###,##0.00000"
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%-----------------------------------------%>
		<%-- PERCENTUAL REAJUSTE FRETE PESO TEXT --%>
		<%-----------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajFretePeso" 
			label="percentualReajuste" 
			labelWidth="19%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true"	/>
		
		<%-----------------------------------%>
		<%-- VALOR MINIMO FRETE QUILO TEXT --%>
		<%-----------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlMinimoFreteQuilo" 
			label="valorMinimoFreteQuilo" 
			mask="###,###,###,###,##0.00"
			size="18" 
			labelWidth="23%" 
			width="29%" 
			disabled="true" 
		/>
		
		<%-------------------------------------------------%>
		<%-- PERCENTUAL REAJUSTE MINIMO FRETE QUILO TEXT --%>
		<%-------------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajVlMinimoFreteQuilo" 
			label="percentualReajuste" 
			labelWidth="19%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true"
		/>
		
		<%-----------------------%>
		<%-- FRETE VOLUME TEXT --%>
		<%-----------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlFreteVolume" 
			label="valorFreteVolume" 
			mask="###,###,###,###,##0.00"
			labelWidth="23%"
			size="18" 
			width="29%" 
			disabled="true" 
		/>
		
		<%-------------------------------------------%>
		<%-- PERCENTUAL REAJUSTE FRETE VOLUME TEXT --%>
		<%-------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajVlFreteVolume" 
			label="percentualReajuste" 
			labelWidth="19%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true"
		/>
		
		<%-------------------------%>
		<%-- TARIFA MINIMA COMBO --%>
		<%-------------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="tarifaMinima" 
			labelWidth="23%"
			property="tpTarifaMinima" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlTarifaMinima" 
				mask="###,###,###,###,##0.00"
				disabled="true"
				size="18" 
			/>
		</adsm:combobox>
		
		<%--------------------------------------------%>
		<%-- PERCENTUAL REAJUSTE TARIFA MINIMA TEXT --%>
		<%--------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajTarifaMinima" 
			label="percentualReajuste" 
			labelWidth="19%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true"
		/>
		
		<%------------------------%>
		<%-- PAGA CUBAGEM CHECK --%>
		<%------------------------%>
		<adsm:checkbox 
			property="blPagaCubagem" 
			labelWidth="23%" 
			width="29%" 
			label="pagaCubagem" 
			disabled="true"
		/>
		
		<%----------------------------------%>
		<%-- PAGA PERCENTUAL CUBAGEM TEXT --%>
		<%----------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcPagaCubagem" 
			label="percentualCubagem" 
			labelWidth="19%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%-------------------------------%>
		<%-- PAGA PESO EXCEDENTE CHECK --%>
		<%-------------------------------%>
		<adsm:checkbox 
			property="blPagaPesoExcedente" 
			label="pagaPesoExcedente" 
			labelWidth="23%" 
			width="29%"
			disabled="true"
		/>
		
		<%-------------------------%>
		<%-- FRETE VALOR SECTION --%>
		<%-------------------------%>
		<adsm:section caption="freteValor" />
		
		<%----------------------%>
		<%-- ADVALOREM1 COMBO --%>
		<%----------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_ADVALOREM" 
			label="advalorem1" 
			labelWidth="28%"
			property="tpIndicadorAdvalorem" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlAdvalorem" 
				maxLength="15" 
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%------------------------------%>
		<%-- REAJUSTE ADVALOREM1 TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajAdvalorem" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%----------------------%>
		<%-- ADVALOREM2 COMBO --%>
		<%----------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_ADVALOREM" 
			label="advalorem2" 
			labelWidth="28%"
			property="tpIndicadorAdvalorem2" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlAdvalorem2" 
				maxLength="15" 
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%------------------------------%>
		<%-- REAJUSTE ADVALOREM2 TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajAdvalorem2" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%----------------------------%>
		<%-- VALOR REFERENCIA COMBO --%>
		<%----------------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="valorReferencia" 
			labelWidth="28%"
			property="tpIndicadorValorReferencia" 
			disabled="true"
			width="72%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlValorReferencia" 
				mask="###,###,###,###,##0.00"
				size="15" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%------------------------------%>
		<%-- FRETE PERCENTUAL SECTION --%>
		<%------------------------------%>
		<adsm:section caption="fretePercentual" />
		
		<%--------------------------------------%>
		<%-- PERCENTUAL FRETE PERCENTUAL TEXT --%>
		<%--------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			label="percentualFretePercentual"
			labelWidth="28%"
			property="pcFretePercentual" 
			disabled="true"
			mask="##0.00"
			size="5" 
			width="72%"
		/>
		
		<%----------------------------------------%>
		<%-- VALOR MINIMO FRETE PERCENTUAL TEXT --%>
		<%----------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			label="valorMinimoFretePercentual"
			labelWidth="28%"
			property="vlMinimoFretePercentual" 
			disabled="true"
			mask="###,###,###,###,##0.00"
			size="18" 
			width="29%"
		/>
		
		<%-------------------------------------------%>
		<%-- REAJUSTE MINIMO FRETE PERCENTUAL TEXT --%>
		<%-------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajVlMinimoFretePercen" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%------------------------------------------%>
		<%-- VALOR TONELADA FRETE PERCENTUAL TEXT --%>
		<%------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			label="valorToneladaFretePercentual"
			labelWidth="28%"
			property="vlToneladaFretePercentual" 
			disabled="true"
			mask="###,###,###,###,##0.00"
			size="18" 
			width="29%"
		/>
		
		<%---------------------------------------------------%>
		<%-- REAJUSTE VALOR TONELADA FRETE PERCENTUAL TEXT --%>
		<%---------------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajVlToneladaFretePerc" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%-------------------------------------------%>
		<%-- PESO REFERENCIA FRETE PERCENTUAL TEXT --%>
		<%-------------------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			label="pesoReferenciaFretePercentual"
			labelWidth="28%"
			mask="#,###,###,###,##0.000"
			property="psFretePercentual" 
			maxLength="15" 
			disabled="true"
			size="18" 
			unit="kg" 
			width="72%"
		/>
		
		<%------------------%>
		<%-- GRIS SECTION --%>
		<%------------------%>
		<adsm:section caption="gris" />
		
		<%---------------------------%>
		<%-- PERCENTUAL GRIS COMBO --%>
		<%---------------------------%>
		<adsm:combobox 
			property="tpIndicadorPercentualGris" 
			domain="DM_INDICADOR_ADVALOREM" 
			label="percentualGris" 
			labelWidth="28%"
			disabled="true"
			width="72%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlPercentualGris" 
				mask="###,###,###,###,##0.00"
				disabled="true"
				size="18" 
			/>
		</adsm:combobox>
	
		<%-----------------------%>
		<%-- MINIMO GRIS COMBO --%>
		<%-----------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="minimoGris" 
			labelWidth="28%"
			property="tpIndicadorMinimoGris" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlMinimoGris" 
				mask="###,###,###,###,##0.00"
				size="18" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%-------------------------------%>
		<%-- REAJUSTE MINIMO GRIS TEXT --%>
		<%-------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajMinimoGris" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%---------------------%>
		<%-- PEDAGIO SECTION --%>
		<%---------------------%>
		<adsm:section caption="pedagio" />
		
		<%-------------------------%>
		<%-- VALOR PEDAGIO COMBO --%>
		<%-------------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PEDAGIO" 
			label="valorPedagio" 
			labelWidth="28%"
			property="tpIndicadorPedagio" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlPedagio" 
				mask="###,###,###,###,##0.00000"
				disabled="true"
				size="18" 
			/>
		</adsm:combobox>
		
		<%---------------------------------%>
		<%-- REAJUSTE VALOR PEDAGIO TEXT --%>
		<%---------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajPedagio" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%------------------%>
		<%-- TRT SECTION --%>
		<%------------------%>
		<adsm:section caption="trt" />
		
		<%---------------------------%>
		<%-- PERCENTUAL TRT COMBO --%>
		<%---------------------------%>
		<adsm:combobox 
			property="tpIndicadorPercentualTrt" 
			domain="DM_INDICADOR_ADVALOREM" 
			label="percentualTrt" 
			labelWidth="28%"
			disabled="true"
			width="72%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlPercentualTrt" 
				mask="###,###,###,###,##0.00"
				disabled="true"
				size="18" 
			/>
		</adsm:combobox>
	
		<%-----------------------%>
		<%-- MINIMO TRT COMBO --%>
		<%-----------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="minimoTrt" 
			labelWidth="28%"
			property="tpIndicadorMinimoTrt" 
			disabled="true"
			width="29%"
		>
			<adsm:textbox 
				dataType="decimal" 
				property="vlMinimoTrt" 
				mask="###,###,###,###,##0.00"
				size="18" 
				disabled="true"
			/>
		</adsm:combobox>
		
		<%-------------------------------%>
		<%-- REAJUSTE MINIMO TRT TEXT --%>
		<%-------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajMinimoTrt" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="29%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>

		<%-----------------%>
		<%-- TDE SECTION --%>
		<%-----------------%>
		<adsm:section caption="tde" />
		
		<%--------------------------%>
		<%-- PERCENTUAL TDE COMBO --%>
		<%--------------------------%>
		<adsm:combobox
			property="tpIndicadorPercentualTde"
			domain="DM_INDICADOR_ADVALOREM"
			label="percentualTde"
			labelWidth="28%"
			disabled="true"
			width="72%">

			<adsm:textbox
				dataType="decimal"
				property="vlPercentualTde"
				mask="###,###,###,###,##0.00"
				disabled="true"
				size="18"/>
		</adsm:combobox>
	
		<%----------------------%>
		<%-- MINIMO TDE COMBO --%>
		<%----------------------%>
		<adsm:combobox
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			label="minimoTde"
			labelWidth="28%"
			property="tpIndicadorMinimoTde"
			disabled="true"
			width="29%">

			<adsm:textbox
				dataType="decimal"
				property="vlMinimoTde"
				mask="###,###,###,###,##0.00"
				size="18"
				disabled="true"/>
		</adsm:combobox>

		<%------------------------------%>
		<%-- REAJUSTE MINIMO TDE TEXT --%>
		<%------------------------------%>
		<adsm:textbox
			dataType="decimal" 
			property="pcReajMinimoTde" 
			label="percentualReajuste" 
			labelWidth="14%"
			width="29%"
			mask="##0.00"
			size="5"
			disabled="true"/>

		<%-------------------------%>
		<%-- TOTAL FRETE SECTION --%>
		<%-------------------------%>
		<adsm:section caption="totalFrete" />

		<%------------------------------------------%>
		<%-- PERCENTUAL DESCONTO FRETE TOTAL TEXT --%>
		<%------------------------------------------%>
		<adsm:textbox
			dataType="decimal"
			label="percentualDescontoFreteTotal"
			labelWidth="28%"
			property="pcDescontoFreteTotal"
			mask="##0.00"
			size="5"
			disabled="true"
			width="72%"/>
		<%-------------------------------------------%>
		<%-- PERCENTUAL COBRANCA DE REENTREGA TEXT --%>
		<%-------------------------------------------%>
		<adsm:textbox
			dataType="decimal"
			label="percentualCobrancaReentrega"
			labelWidth="28%"
			property="pcCobrancaReentrega"
			mask="##0.00"
			size="5"
			disabled="true"
			width="72%"/>
		<%--------------------------------------------%>
		<%-- PERCENTUAL COBRANCA DE DEVOLUCOES TEXT --%>
		<%--------------------------------------------%>
		<adsm:textbox
			dataType="decimal"
			label="percentualCobrancaDevolucoes"
			labelWidth="28%"
			property="pcCobrancaDevolucoes"
			mask="##0.00"
			size="5"
			disabled="true"
			width="72%"/>
		
		<adsm:buttonBar />
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	function montaRotas() {
		setElementValue("rotaPreco.origemString", "");
		var rota = criaRotaString("Origem");
		
		if(rota != "") {
			var i = rota.lastIndexOf(" - ");
			rota = rota.substring(0, i);
			setElementValue("rotaPreco.origemString", rota);
		}
	
		setElementValue("rotaPreco.destinoString", "");
		rota = criaRotaString("Destino");
	
		if(rota!="") {
			var i = rota.lastIndexOf(" - ");
			rota = rota.substring(0, i);
			setElementValue("rotaPreco.destinoString", rota);
		}
	}

	function criaRotaString(tipo) {
		var rota = "";
		var value = "";
		
		value = getElementValue("zona"+tipo+".dsZona");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("paisByIdPais"+tipo+".nmPais");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("unidadeFederativaByIdUf"+tipo+".siglaDescricao");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("filialByIdFilial"+tipo+".sgFilial");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("municipioByIdMunicipio"+tipo+".municipio.nmMunicipio");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("aeroportoByIdAeroporto"+tipo+".sgAeroporto");
		if(value!="") {
			rota += value+" - ";
		}
		value = getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao"+tipo+".dsTipoLocalizacaoMunicipio");
		if(value!="") {
			rota += value+" - ";
		}
		
		return rota;
	}

	function formLoad_cb(dados, erros){
		try{
			onDataLoad_cb(dados, erros);
			montaRotas();
		} catch(e) {
			alert(e);
		}
	}
	
	function getValuesFromParam(){
	    var dados = new Array();

		setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao", getElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacaoFormatado"));
		setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa", getElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"));
		setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente", getElementValue("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"));
		setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao", getElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"));
		setNestedBeanPropertyValue(dados, "rotaPreco.origemString", getElementValue("rotaPreco.origemString"));
		setNestedBeanPropertyValue(dados, "rotaPreco.destinoString", getElementValue("rotaPreco.destinoString"));
		setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo", getElementValue("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"));
		setNestedBeanPropertyValue(dados, "parametroCliente.idParametroCliente", getElementValue("idParametroCliente"));
		
		return dados;
	}

	function hide() {
		tab_onHide();
		var frame = parent.document.frames["pesq_iframe"];
		frame.setOrigem("detail");
		return true;
	}
</script>