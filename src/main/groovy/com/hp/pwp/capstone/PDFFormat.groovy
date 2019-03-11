package com.hp.pwp.capstone

import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer  
import java.util.Random
import java.awt.geom.AffineTransform

import org.apache.pdfbox.pdmodel.PDDocument 
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.FileSystemFontProvider
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.multipdf.LayerUtility
import org.apache.pdfbox.pdmodel.graphics.PDXObject
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject


public class PDFFormat {
	public static void main(String [] args) {
		//JSON string
		String json = "{" + "'path': '/mnt/TestPDF.pdf'," + "'outputPath': '/mnt/FinalPDF.pdf'," + "'WID': 1019," + "'JID': 1109," + "'pdfLength': 4" + "}"
		
		//Convert our JSON to JAVA
		JsontoJava data = new Gson().fromJson(json, JsontoJava.class)
		
		InputPDF input = new InputPDF()



		input.getPages(data.pdfLength, data.path, data.outputPath)
		
	}
}

//Functions that handle the JSON conversions
class JsontoJava {
	private String path
	private String outputPath
	private int pdfLength
	private int WID
	private int JID

	public String getPath() {return path}
	public String getOutput() {return outputPath}
	public int getpdfLength() {return pdfLength}
	public int getWID() {return WID}
	public int getJID() {return JID}

	public void setPath(String path) {this.path = path}
	public void setOutput(String outputPath) {this.outputPath = outputPath}
	public void setpdfLength(int pdfLength) {this.pdfLength = pdfLength}
	public void setWID(int WID) {this.WID = WID}
	public void setJID(int JID) {this.JID = JID}
}

//Function to handle the input to pdfs.
class InputPDF {
	public void getPages(int pdflength, String path, String outputpath){
	//This gets rid of all the warnings caused by not having fonts installed
	//System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog")
    File pdfFile = new File(path);
    File outPdfFile = new File(outputpath);
	PDDocument originalPdf = null
	PDDocument outPdf = null

	originalPdf = PDDocument.load(pdfFile);
	outPdf = new PDDocument();
	LayerUtility layerUtility = new LayerUtility(outPdf)
	PDRectangle pdfFrame = originalPdf.getPage(0).getCropBox()
	PDFormXObject formPdf = layerUtility.importPageAsForm(originalPdf, 0)

	// Create output PDF frame
	float width = pdfFrame.getWidth()*2
	float height =	pdfFrame.getHeight()*2
	PDRectangle outPdfFrame = new PDRectangle(width, height)

	// Create an output page.
	COSDictionary dict = new COSDictionary()
	dict.setItem(COSName.TYPE, COSName.PAGE)
	dict.setItem(COSName.MEDIA_BOX, outPdfFrame)
	dict.setItem(COSName.CROP_BOX, outPdfFrame)
	dict.setItem(COSName.ART_BOX, outPdfFrame)
	PDPage outPdfPage = new PDPage(dict)
	outPdf.addPage(outPdfPage)

	int i= 0
	for(i;i<pdflength;i++){

			// Turn each PDF into a form object so that it can be inserted in a certain part of the page.

			// Add the pages to their correct position on the single page pdf being created.
			if(i == 0){
				pdfFrame = originalPdf.getPage(i).getCropBox()
				formPdf = layerUtility.importPageAsForm(originalPdf, i)
				AffineTransform bottomLeft = new AffineTransform()
				layerUtility.appendFormAsLayer(outPdfPage, formPdf, bottomLeft, "bottomleft")
			}

			if(i == 1){
				pdfFrame = originalPdf.getPage(i).getCropBox()
				formPdf = layerUtility.importPageAsForm(originalPdf, i)
				AffineTransform bottomRight = AffineTransform.getTranslateInstance(pdfFrame.getWidth(), 0.0)
				layerUtility.appendFormAsLayer(outPdfPage, formPdf, bottomRight, "bottomright")
			}

			if(i == 2){
				pdfFrame = originalPdf.getPage(i).getCropBox()
				formPdf = layerUtility.importPageAsForm(originalPdf, i)
				AffineTransform topLeft = AffineTransform.getTranslateInstance(0.0, pdfFrame.getHeight())
				layerUtility.appendFormAsLayer(outPdfPage, formPdf, topLeft, "topleft")
			}

			if(i == 3){
				pdfFrame = originalPdf.getPage(i).getCropBox()
				formPdf = layerUtility.importPageAsForm(originalPdf, i)
				AffineTransform topRight = AffineTransform.getTranslateInstance(pdfFrame.getWidth(), pdfFrame.getHeight())
				layerUtility.appendFormAsLayer(outPdfPage, formPdf, topRight, "topright")
						//Output the finished PDF.
				outPdf.save(outPdfFile)
				if (originalPdf != null) originalPdf.close()
				if (outPdf != null) outPdf.close()
			}

	
		
	} 
}
}
