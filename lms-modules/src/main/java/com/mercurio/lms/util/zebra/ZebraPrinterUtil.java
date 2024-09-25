package com.mercurio.lms.util.zebra;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.mercurio.lms.util.zebra.enums.TipoImpressoraEnum;
import com.mercurio.lms.util.zebra.enums.ZebraColorEnum;
import com.mercurio.lms.util.zebra.enums.ZebraDeviceEnum;
import com.mercurio.lms.util.zebra.enums.ZebraFieldOrientationEnum;
import com.mercurio.lms.util.zebra.enums.ZebraFontEnum;
import com.mercurio.lms.util.zebra.enums.ZebraJustificationEnum;
import com.mercurio.lms.util.zebra.enums.ZebraTextJustifEnum;

/**
 * @author marcelosc
 *
 *  Classe utilitária para impressão com Zebra
 *  
 * Para impressão de imagens deve-se converte-las para GRF utilizando o ZTools,
 * contudo, para que este funcione perfeitamente, deve-se <strong>observar os seguintes aspectos</strong>:<br />
 *  <p><ul><li>A imagem criada deve ser preta e branco.</li>
 * <li>Não pode ser RGB, e sim, Tons de Cinza, ou mesmo, Preto e Branco</li>
 * <li>No programa, o nome da imagem  (Image Name) será o nome utilizado pelo método</li>
 * <li>Após mover-la para o projeto, deve-se remover os caracteres adicionais presentes no início do arquivo 
 *     Ex. (~DGUN3373L,74981,097)</li></ul></p>
 * 
 */
public class ZebraPrinterUtil {
	
	/**
	 * Representa o simbolo da quebra de linha do campo texto.
	 */
	public static final String CARRIAGE_RETURN = "\\&";

	/**
	 * Representa o simbolo da quebra de linha.
	 */
	private static final String ZPL_NEW_LINE = "\n";
	
	private String tpImpressora;
	
	private static final Pattern patternAUpperCase = Pattern.compile("(Ã|Â|À|Á|Ä)");
	private static final Pattern patternEUpperCase = Pattern.compile("(Ê|È|É|Ë)");
	private static final Pattern patternIUpperCase = Pattern.compile("(Î|Ì|Í|Ï)");
	private static final Pattern patternOUpperCase = Pattern.compile("(Õ|Ô|Ò|Ó|Ö)");
	private static final Pattern patternUUpperCase = Pattern.compile("(Û|Ù|Ú|Ü)");
	private static final Pattern patternCUpperCase = Pattern.compile("(Ç)");
	private static final Pattern patternNUpperCase = Pattern.compile("(Ñ)");
	private static final Pattern patternALowerCase = Pattern.compile("(ã|â|à|á|ä)");
	private static final Pattern patternELowerCase = Pattern.compile("(ê|è|é|ë)");
	private static final Pattern patternILowerCase = Pattern.compile("(î|ì|í|ï)");
	private static final Pattern patternOLowerCase = Pattern.compile("(õ|ô|ò|ó|ö)");
	private static final Pattern patternULowerCase = Pattern.compile("(û|ú|ù|ü)");
	private static final Pattern patternCLowerCase = Pattern.compile("(ç)");
	private static final Pattern patternNLowerCase = Pattern.compile("(ñ)");
	
	/**
	 * Representa o codigo ZPL que sera construido.
	 */
	private StringBuffer zpl;
	
	/**
	 * Representa o device padrao para as imagens.
	 */
	private ZebraDeviceEnum defaultDevice;
	
	/**
	 * Representa a fonte padrao do texto.
	 */
	private ZebraFontEnum defaultFont;
	
	/**
	 * Construtor padrao da classe.
	 */
	public ZebraPrinterUtil(String tipoImpressora) {
		this.tpImpressora = tipoImpressora;
		defaultDevice = ZebraDeviceEnum.FLASH;
		defaultFont = ZebraFontEnum.FONT_0;
		zpl = new StringBuffer();
		startZPL(tipoImpressora);
	}
	
	/**
	 * @see  #drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation)
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y) throws Exception {
		return drawText(text, font, x, y, 1, null, false, 0, 0, null);
	}
	
	/**
	 * @see  #drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation)
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y, boolean reversePrint) throws Exception {
		return drawText(text, font, x, y, 1, null, reversePrint, 0, 0, null);
	}

	/**
	 * @see  #drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation)
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y, int scale, boolean reversePrint) throws Exception {
		return drawText(text, font, x, y, scale, null, reversePrint, 0, 0, null);
	}
	
	/**
	 * @see  #drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation)
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y, int width, int maxNumberLines, ZebraTextJustifEnum justification) throws Exception {
		return drawText(text, font, x, y, 1, null, false, width, maxNumberLines, justification);
	}
	
	/**
	 * @see  #drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation)
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y, boolean reversePrint, int width, int maxNumberLines, ZebraTextJustifEnum justification) throws Exception {
		return drawText(text, font, x, y, 1, null, reversePrint, width, maxNumberLines, justification);
	}
	
	/**
	 * Desenha um texto.
	 * 
	 * @param text Texto que deve ser escrito.
	 * @param font Fonte que deve ser utilizada.
	 * @param x Posicao do texto no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do texto no eixo Y, em relacao a origem da etiqueta.
	 * @param scale Scala do texto (ex.: 2 para o dobro do tamanho.
	 * @param orientation Orientacao do texto.
	 * @param reversePrint Permite a um texto aparecer branco se o fundo for preto ou vice-versa.
	 * @return Altura da linha ocupada pelo texto.
	 * @throws Exception
	 */
	public int drawText(String text, ZebraFontEnum font, int x, int y, int scale, ZebraFieldOrientationEnum orientation, boolean reversePrint, int width, int maxNumberLines, ZebraTextJustifEnum justification) throws Exception {
		
		text = removeSpecialCharacters(text.toUpperCase());
		
		setFieldOrigin(x, y);
		
		int realScaleHeight = changeFonte(font, scale, orientation, reversePrint);
		
		if(width > 0) {
			zpl.append("^FB" + width + "," + maxNumberLines + ",," + (justification != null ? justification.getValue() : "L") + ",0");
		}
		zpl.append("^FD" + text + "^FS");
		zpl.append(ZPL_NEW_LINE);
		
		return realScaleHeight;
	}

	/**
	 * Altera a fonte do texto.
	 * 
	 * @param font Fonte que deve ser utilizada.
	 * @param scale Scala do texto (ex.: 2 para o dobro do tamanho.
	 * @param orientation Orientacao do texto.
	 * @param reversePrint Permite a um texto aparecer branco se o fundo for preto ou vice-versa.
	 * @throws Exception
	 */
	private int changeFonte(ZebraFontEnum font, int scale, ZebraFieldOrientationEnum orientation, boolean reversePrint)
			throws Exception {
		zpl.append("^A");
		if(font != null) {
			zpl.append(font.getName());
		} else {
			zpl.append(defaultFont.getName());
		}
		if(orientation != null) {
			zpl.append(orientation.getValue());
		}
		int realScaleHeight = (font.getHeight() * scale);
		int realScaleWidth = (font.getWidth() * scale);
		if(realScaleHeight < 10 || realScaleHeight > 32000 || realScaleWidth < 10 || realScaleWidth > 32000) {
			throw new Exception("Valor da escala ultrapassa o limite maximo (10 a 32000).");
		}
		if(scale >= 1) {
			zpl.append("," + realScaleHeight + "," + realScaleWidth);
		}
		if(reversePrint) {
			zpl.append("^FR");
		}
		return realScaleHeight;
	}

	
	
	
	
	
	
	
	
	/**
	 * Desenha um texto.
	 * 
	 * @param startIteratorValue Valor inicial que será incrementado
	 * @param endIteratorValue Valor final até onde será incrementado o valor
	 * @param incrementValue Valor a ser incrementado 
	 * @param textSeparatorIteration texto separdora entre o startIteratorValue e o endIteratorValue
	 * @param font Fonte que deve ser utilizada.
	 * @param x Posicao do texto no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do texto no eixo Y, em relacao a origem da etiqueta.
	 * @param reversePrint Permite a um texto aparecer branco se o fundo for preto ou vice-versa.
	 * @return Altura da linha ocupada pelo texto.
	 * @throws Exception
	 */
	public int drawTextIterator(int startIteratorValue, int endIteratorValue, int incrementValue, String textSeparatorIteration, ZebraFontEnum font, int x, int y, boolean reversePrint) throws Exception {
		int scale = 1;
		ZebraFieldOrientationEnum orientation = null;
		
		setFieldOrigin(x, y);

		int realScaleHeight = changeFonte(font, scale, orientation, reversePrint);
		zpl.append("^FD" + generateTextIterator(startIteratorValue, endIteratorValue, incrementValue, textSeparatorIteration) + "^FS");
		zpl.append(ZPL_NEW_LINE);
		
		return realScaleHeight;
	}

	/**
	 * Desenha um texto.
	 * 
	 * @param startIteratorValue Valor inicial que será incrementado
	 * @param endIteratorValue Valor final até onde será incrementado o valor
	 * @param incrementValue Valor a ser incrementado 
	 * @param textSeparatorIteration texto separdora entre o startIteratorValue e o endIteratorValue
	 **/
	public String generateTextIterator(int startIteratorValue, int endIteratorValue, int incrementValue, String textSeparatorIteration) {
		int sizeEndValue = new String("" + endIteratorValue).length();
		if (textSeparatorIteration == null) {
			textSeparatorIteration = "";
		}
		String strTextSeparator = fillStringWithChar(null, ("" + endIteratorValue + textSeparatorIteration).length(), '%');

		StringBuilder sb = new StringBuilder();
		sb.append(fillNumberWithZero("" + startIteratorValue, sizeEndValue));
		sb.append(textSeparatorIteration + endIteratorValue);
		sb.append("^SF");
		sb.append(fillStringWithChar(null, sizeEndValue, 'd'));
		sb.append(strTextSeparator);
		sb.append("," + incrementValue);
		sb.append(strTextSeparator);
		return sb.toString();
	}

	/**
	 * Desenha a imagem na posicao especificada.
	 * 
	 * @param name Nome do arquivo da imagem na impressora.
	 * @param x Posicao da imagem no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao da imagem no eixo Y, em relacao a origem da etiqueta.
	 * @throws Exception 
	 */
	public void drawImage(String name, int x, int y) throws Exception {
		drawImage(defaultDevice, name, x, y, 1, 1);
	}
	
	/**
	 * Desenha a imagem na posicao especificada.
	 * 
	 * @param device Device da impressora onde as imagens se encontram.
	 * @param name Nome do arquivo da imagem na impressora.
	 * @param x Posicao da imagem no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao da imagem no eixo Y, em relacao a origem da etiqueta.
	 * @param magX Fator de multiplicacao do tamanho horizontal da imagem.
	 * @param magY Fator de multiplicacao do tamanho vertical da imagem.
	 * @throws Exception 
	 */
	public void drawImage(ZebraDeviceEnum device, String name, int x, int y, int magX, int magY) throws Exception {
		
		setFieldOrigin(x, y);
		zpl.append("^XG" + device.getDevice() + ":" + name + ".GRF," + magX + "," + magY + "^FS");
		zpl.append(ZPL_NEW_LINE);
	}

	/**
	 * Desenha uma linha horizontal.
	 * 
	 * @param x Posicao no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao no eixo Y, em relacao a origem da etiqueta.
	 * @param width Comprimento da linha.
	 * @param border Espessura da linha.
	 * @throws Exception
	 */
	public void drawHorizontalLine(int x, int y, int width, int border) throws Exception {
		drawBox(x, y, width, 0, border);
	}

	/**
	 * Desenha uma linha vertical.
	 * 
	 * @param x Posicao no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura da linha.
	 * @param border Espessura da linha.
	 * @throws Exception
	 */
	public void drawVerticalLine(int x, int y, int height, int border) throws Exception {
		drawBox(x, y, 0, height, border);
	}
	
	/**
	 * @see  #drawBox(int x, int y, int width, int height, int border, ZebraColorEnum lineColor, int rounding)
	 */
	public void drawBox(int x, int y, int width, int height) throws Exception {
		drawBox(x, y, width, height, 1, null, 0);
	}
	
	/**
	 * @see  #drawBox(int x, int y, int width, int height, int border, ZebraColorEnum lineColor, int rounding)
	 */
	public void drawBox(int x, int y, int width, int height, int border) throws Exception {
		drawBox(x, y, width, height, border, null, 0);
	}
	
	/**
	 * Desenha um retangulo.
	 * 
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param width Largura do retangulo.
	 * @param height Altura do retangulo.
	 * @param border Borda do retangulo
	 * @param lineColor Cor da linha do retangulo.
	 * @param rounding Arredondamento dos cantos do retangulo (valor de 0 a 8).
	 * @throws Exception
	 */
	public void drawBox(int x, int y, int width, int height, int border, ZebraColorEnum lineColor, int rounding) throws Exception {
		
		if(border < 1 || border > 32000) {
			throw new Exception("Valor da borda ultrapassa o limite maximo (0 a 32000).");
		}
		if(rounding < 0 || rounding > 8) {
			throw new Exception("Valor do rounding ultrapassa o limite maximo (0 a 8).");
		}
		setFieldOrigin(x, y);
		zpl.append("^GB" + width + "," + height + "," + border);
		if(lineColor != null) {
			zpl.append("," + lineColor.getValue());
		}
		if(rounding > 0) {
			zpl.append("," + rounding);
		}
		zpl.append("^FS" );
		zpl.append(ZPL_NEW_LINE);
	}

	/**
	 * @see  #drawBarCode39(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean useMod43, boolean printInterpLine, boolean printInterpLineAbove)
	 */
	public void drawBarCode39(String code, int x, int y, int height) throws Exception {
		drawBarCode39(code, x, y, height, ZebraFieldOrientationEnum.NORMAL, false, false, false);
	}
	
	/**
	 * Desenha um codigo de barras do padrao 3 de 9.
	 * 
	 * @param code Codigo alfa numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param useMod43 Se true, devera gerar o digito verificador em MOD43.
	 * @param printInterpLine Se true, devera imprimir o codigo gerador abaixo da etiqueta.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @throws Exception
	 */
	public void drawBarCode39(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean useMod43, boolean printInterpLine, boolean printInterpLineAbove) throws Exception {
		setFieldOrigin(x, y);
		zpl.append("^B3");
		zpl.append(orientation != null ? orientation.getValue() : "N");
		zpl.append(useMod43 ? ",Y" : ",N");
		zpl.append("," + height);
		zpl.append(printInterpLine ? ",Y" : ",N");
		zpl.append(printInterpLineAbove ? ",Y" : ",N");
		zpl.append("^FD" + code + "^FS" );
		zpl.append(ZPL_NEW_LINE);
	}
	
	/**
	 * Desenha um codigo de barras do padrao 128.
	 * 
	 * @param code Codigo alfa numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param useCheckDigit Se true, devera gerar o digito verificador.
	 * @param printInterpLine Se true, devera imprimir o codigo gerador abaixo da etiqueta.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @throws Exception
	 */
	public void drawBarCode128(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean useCheckDigit, boolean printInterpLine, boolean printInterpLineAbove) throws Exception {
		drawBarCode128(code, x, y, height, orientation, useCheckDigit, printInterpLine, printInterpLineAbove, null, null, null, 2);
	}

	/**
	 * Desenha um codigo de barras do padrao 128.
	 * 
	 * @param code Codigo alfa numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param useCheckDigit Se true, devera gerar o digito verificador.
	 * @param printInterpLine Se true, devera imprimir o codigo gerador abaixo da etiqueta.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @param moduleWidth Permite alterar a largura do codigo de barras (in dots) - Accepted Values: 1 to 10. Initial Value at power-up: 2
	 * @throws Exception
	 */
	public void drawBarCode128(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean useCheckDigit, boolean printInterpLine, boolean printInterpLineAbove, int moduleWidth) throws Exception {
		drawBarCode128(code, x, y, height, orientation, useCheckDigit, printInterpLine, printInterpLineAbove, null, null, null, moduleWidth);
	}
	
	/**
	 * Desenha um codigo de barras do padrao 128.
	 * 
	 * @param code Codigo alfa numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param useCheckDigit Se true, devera gerar o digito verificador.
	 * @param printInterpLine Se true, devera imprimir o codigo gerador abaixo da etiqueta.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @param startIteratorIndex Inicio do indice que será incrementado no código de barras
	 * @param endIteratorIndex Se true, ira gerar varias etiquetas para o codigodevera imprimir o codigo gerador acima da etiqueta.
	 * @param incrementValue Valor a ser incrementado na geração do código de barras
	 * @param moduleWidth Permite alterar a largura do codigo de barras (in dots) - Accepted Values: 1 to 10. Initial Value at power-up: 2
	 * @throws Exception
	 */
	public void drawBarCode128(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean useCheckDigit, boolean printInterpLine, boolean printInterpLineAbove, Integer startIteratorIndex, Integer endIteratorIndex, Integer incrementValue, int moduleWidth) throws Exception {
		setFieldOrigin(x, y);
		if(moduleWidth <= 2) {
			zpl.append("^BY2,3.0,60");
		} else {
			zpl.append("^BY" + moduleWidth + ",3.0,50");
		}
		zpl.append("^BC");
		zpl.append(orientation != null ? orientation.getValue() : "N");
		zpl.append("," + height);
		zpl.append(printInterpLine ? ",Y" : ",N");
		zpl.append(printInterpLineAbove ? ",Y" : ",N");
		zpl.append(useCheckDigit ? ",Y" : ",N");
		zpl.append(",A");
		zpl.append("^FD" + code);
		if (startIteratorIndex != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(fillStringWithChar(null, startIteratorIndex.intValue()-1, '%'));
			sb.append(fillStringWithChar(null, (endIteratorIndex.intValue() - startIteratorIndex.intValue()), 'd'));
			String endString = fillStringWithChar(null, code.length() - sb.toString().length(), '%');
			sb.append(endString);
			sb.append("," + incrementValue.intValue() + endString);
			zpl.append("^SF" + sb.toString());
		}
		zpl.append("^FS" );
		zpl.append(ZPL_NEW_LINE);
	}
	
	/**
	 * Desenha um codigo de barras do padrao 2 de 5.
	 * 
	 * @param code Codigo numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param printInterpLine Se true, devera imprimir o codigo gerador abaixo da etiqueta.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @param checkMod10 Se true, devera gerar o digito verificador em MOD10.
	 * @throws Exception
	 */
	public void drawBarCode2Of5(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean printInterpLine, boolean printInterpLineAbove, boolean checkMod10) throws Exception {
		setFieldOrigin(x, y);
		zpl.append("^B2");
		zpl.append(orientation != null ? orientation.getValue() : "N");
		zpl.append("," + height);
		zpl.append(printInterpLine ? ",Y" : ",N");
		zpl.append(printInterpLineAbove ? ",Y" : ",N");
		zpl.append(checkMod10 ? ",Y" : ",N");
		zpl.append("^FD" + code + "^FS" );
		zpl.append(ZPL_NEW_LINE);
	}
	
	/**
	 * Desenha um codigo de barras do padrao LOGMARS.<br>
	 * Is a special application of Code 39 used by the Department of Defense.<br>
	 * LOGMARS is an acronym for Logistics Applications of Automated Marking and Reading Symbols.
	 * 
	 * @param code Codigo alfa numerico que ira gerar o codigo de barras.
	 * @param x Posicao do canto superior esquerdo, no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao do canto superior esquerdo, no eixo Y, em relacao a origem da etiqueta.
	 * @param height Altura do codigo de barras.
	 * @param orientation Orientacao do codigo de barras.
	 * @param printInterpLineAbove Se true, devera imprimir o codigo gerador acima da etiqueta.
	 * @throws Exception
	 */
	public void drawBarCodeLogMars(String code, int x, int y, int height, ZebraFieldOrientationEnum orientation, boolean printInterpLineAbove) throws Exception {
		setFieldOrigin(x, y);
		zpl.append("^BL");
		zpl.append(orientation != null ? orientation.getValue() : "N");
		zpl.append("," + height);
		zpl.append(printInterpLineAbove ? ",Y" : ",N");
		zpl.append("^FD" + code + "^FS" );
		zpl.append(ZPL_NEW_LINE);
	}
	
	/**
	 * This command sets the label home position.
	 * 
	 * The default home position of a label is the upper-left corner (position 0,0 along the x and y axis). 
	 * This is the axis reference point for labels. Any area below and to the right of this point is 
	 * available for printing. This command changes this reference point. For instance, when 
	 * working with preprinted labels, use this command to move the reference point below the 
	 * preprinted area. This command affects only fields that come after it. 
	 * It is recommended to use it as one of the first commands in the label format.
	 * 
	 * @param x Posicao em relacao ao eixo x.
	 * @param y Posicao em relacao ao eixo y.
	 */
	public void setLabelHome(int x, int y) {
		zpl.append("^LH" + x + "," + y);
		zpl.append(ZPL_NEW_LINE);
	}
	
	/**
	 * @see  #setFieldOrigin(int x, int y, ZebraJustificationEnum justification)
	 */
	public void setFieldOrigin(int x, int y) throws Exception {
		
		if(x < 0 || x > 32000 || y < 0 || y > 32000) {
			throw new Exception("Valor do posicionamento do campo ultrapassado (0 a 32000).");
		}
		setFieldOrigin(x, y, null);
	}

	/**
	 * Posiciona o 'cursor' de desenho na posicao especificada. <br>
	 * Posicao onde o proximo campo sera desenhado.
	 * 
	 * @param x Posicao no eixo X, em relacao a origem da etiqueta.
	 * @param y Posicao no eixo Y, em relacao a origem da etiqueta.
	 * @param justification Alinhamento do campo em relacao ao ponto especificado.
	 * @throws Exception
	 */
	public void setFieldOrigin(int x, int y, ZebraJustificationEnum justification) {
		
		zpl.append("^FO" + x + "," + y);
		if(justification != null) {
			zpl.append("," + justification.getValue());
		}
	}
	
	/**
	 * Imprime o script ZPL criado, na saida especificada.
	 * @throws FileNotFoundException 
	 */
	public void print(Integer lptNumber) throws FileNotFoundException {
		
		finalizeZPL();
		LptPrinter lptPrinter = new LptPrinter();
		lptPrinter.print(zpl.toString(), lptNumber);
	}
	
	/**
	 * Imprime o script ZPL criado, na saida especificada.
	 * @throws FileNotFoundException 
	 */
	public void print(String ip, Integer port) throws FileNotFoundException {
		
		finalizeZPL();
		
		if (TipoImpressoraEnum.WINDOWS.getType().equals(tpImpressora )) {
			DefaultPrinter defaultPrinter = new DefaultPrinter();
			try {
				defaultPrinter.print(zpl.toString());
			} catch (Exception e) {
				System.out.println(e);
			}
		} else if(port > 2000) {
			IpPrinter ipPrinter = new IpPrinter();
			ipPrinter.print(zpl.toString(), ip, port);
		} else {
			LptPrinter lptPrinter = new LptPrinter();
			lptPrinter.print(zpl.toString(), false, port);
		}
	}
	
	/**
	 * Imprime o script ZPL criado, na saida especificada.
	 * @throws FileNotFoundException 
	 */
	public void printList(final List<String> zpls, final String ip, final int port) throws FileNotFoundException {
		if(port > 80) {
			new IpPrinter().print(zpls, ip, port);
		} else {
			new LptPrinter().print(zpls, false, port);
		}
	}
	
	public String getZpl(){
		finalizeZPL();
		return zpl.toString();
	}
	
	/**
	 * Downloads an ASCII Hex representation of a graphic image.
	 * 
	 * @param name Nome da imagem.
	 * @param device Device da impressora onde a imagem devera ser armazenada.
	 * 
	 * @param totalOfBytes - número total de bytes da imagem, presente no arquivo logo
	 * 						 após sua conversão após a primeira vírgula (~DGUN3373L,74981,097) totalOfBytes == 74981
	 *  
	 * @param bytesPerRow  - número de bytes por linha, presente no arquivo logo
	 * 						 após sua conversão após a segunda vírgula (~DGUN3373L,74981,097) bytesPerRow == 97
	 * 
	 * @param data ASCII hexadecimal string defining image.
	 * @see ZebraPrinterUtil - <strong>Detalhes sobre conversão de imagens utilizando ZTools.exe</strong>
	 */
	public void storeImagem(String name, ZebraDeviceEnum device, int totalOfBytes, int bytesPerRow, StringBuffer data) {
		zpl.append("~DG" + device.getDevice() + ":" + name + ".GRF," + fillNumberWithZero("" + totalOfBytes, 5) + "," + fillNumberWithZero("" + bytesPerRow, 3) + "," + data);
	}
	
	public void setLabelsAmount(int labelsAmount) {
		zpl.append("^PQ" + labelsAmount);
	}
	
	/**
	 * Inicia o script do ZPL.
	 */
	private void startZPL(String tipoImpressora) {
		zpl.append("^XA" + ZPL_NEW_LINE);
		
		if (tipoImpressora.equals(TipoImpressoraEnum.PORTATIL.getType())) {
			zpl.append("^POI" + ZPL_NEW_LINE); //Linha adicionada para suportar as impressoras m—veis.
		}
	}
	
	/**
	 * Finaliza o script do ZPL.
	 */
	private void finalizeZPL() {
		zpl.append("^XZ");
	}
	
	private String removeSpecialCharacters(String text) {
		text = patternALowerCase.matcher(text).replaceAll("a");
		text = patternELowerCase.matcher(text).replaceAll("e");
		text = patternILowerCase.matcher(text).replaceAll("i");
		text = patternOLowerCase.matcher(text).replaceAll("o");
		text = patternULowerCase.matcher(text).replaceAll("u");
		text = patternAUpperCase.matcher(text).replaceAll("A");
		text = patternEUpperCase.matcher(text).replaceAll("E");
		text = patternIUpperCase.matcher(text).replaceAll("I");
		text = patternOUpperCase.matcher(text).replaceAll("O");
		text = patternUUpperCase.matcher(text).replaceAll("U");
		text = patternCLowerCase.matcher(text).replaceAll("c");
		text = patternCUpperCase.matcher(text).replaceAll("C");
		text = patternNLowerCase.matcher(text).replaceAll("n");
		return patternNUpperCase.matcher(text).replaceAll("N");
	}
	
    public static String fillStringWithChar(String strLine, int size, char character) {
        if (strLine == null) {
              strLine = "";
        }
        if (strLine.length() <= size) {
              return StringUtils.leftPad(strLine, size, character);
        } else {
              return strLine.substring(0, size);
        }
    }
    
    public static String fillNumberWithZero(String strLine, int size) {
        if (strLine == null) {
            strLine = "";
        }
        if (strLine.length() <= size) {
            return StringUtils.leftPad(strLine, size, '0');
        } else {
            return strLine.substring(0, size);
        }
    }
}
