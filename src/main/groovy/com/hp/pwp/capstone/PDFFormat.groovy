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
import groovy.json.JsonSlurper
import groovy.transform.Synchronized


public class PDFFormat {
	
	PDFFormat(){
	
	}
	@Synchronized
	public void start(String s){
		//Get the file path for the PDF we are using and the path to the new PDF that will be created.
		def parser = new JsonSlurper();
		def data = parser.parseText(s)
		print data;
		

		int pdflength = data.pageLength;
		String path = data.path;
		String outputpath = data.outPath;
		println path;
		println outputpath;
		File pdfFile = new File(path);
		println "made file1"
		File outPdfFile = new File(outputpath);
		println "made file2"
		PDDocument originalPdf = null
			PDDocument outPdf = null

			//Here I am loading in the pdf we are using as well as creating the new output pdf.
			originalPdf = PDDocument.load(pdfFile);
		println "load old"
		outPdf = new PDDocument();
		println "made new"
		//This is the layer utility we use to set the position of each pdf on the larger page.
		LayerUtility layerUtility = new LayerUtility(outPdf)
			//Grab the first page from the pdf.
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

			//These are the four positions we will use to place pages on the larger pages so that there are four pdfs on a single page.
			AffineTransform bottomLeft = new AffineTransform()
			AffineTransform topLeft = AffineTransform.getTranslateInstance(0.0, pdfFrame.getHeight())
			AffineTransform bottomRight = AffineTransform.getTranslateInstance(pdfFrame.getWidth(), 0.0)
			AffineTransform topRight = AffineTransform.getTranslateInstance(pdfFrame.getWidth(), pdfFrame.getHeight())
			int i= 0
			int count = 1
			for(i;i<pdflength;i++){
				// Add the pages to their correct position on the single page pdf being created.
				//If it is the first of four pages it is placed in the top left.
				if(count == 1){
					pdfFrame = originalPdf.getPage(i).getCropBox()
						formPdf = layerUtility.importPageAsForm(originalPdf, i)
						layerUtility.appendFormAsLayer(outPdfPage, formPdf, topLeft, "topleft"+Integer.toString(i))
				}
				//If it is the second of four pages it will be placed in the top right.
				if(count == 2){
					pdfFrame = originalPdf.getPage(i).getCropBox()
						formPdf = layerUtility.importPageAsForm(originalPdf, i)
						layerUtility.appendFormAsLayer(outPdfPage, formPdf, topRight, "topright"+Integer.toString(i))
				}
				//If it is the third of four pages it will be placed in the bottom left.
				if(count == 3){
					pdfFrame = originalPdf.getPage(i).getCropBox()
						formPdf = layerUtility.importPageAsForm(originalPdf, i)
						layerUtility.appendFormAsLayer(outPdfPage, formPdf, bottomLeft, "bottomleft"+Integer.toString(i))
				}
				//If it is the fourth of four page it will be placed in the bottom right.
				if(count == 4){
					pdfFrame = originalPdf.getPage(i).getCropBox()
						formPdf = layerUtility.importPageAsForm(originalPdf, i)
						layerUtility.appendFormAsLayer(outPdfPage, formPdf, bottomRight, "bottomright"+Integer.toString(i))
						//Here we are creating a new blank page to add more pages if necessary.
						if(i != pdflength-1){
							dict = new COSDictionary()
								dict.setItem(COSName.TYPE, COSName.PAGE)
								dict.setItem(COSName.MEDIA_BOX, outPdfFrame)
								dict.setItem(COSName.CROP_BOX, outPdfFrame)
								dict.setItem(COSName.ART_BOX, outPdfFrame)
								outPdfPage = new PDPage(dict)
								outPdf.addPage(outPdfPage)
						}
					count=0
				}
				count++
					//Output the finished PDF if we are done.
					if(i == pdflength-1){
						outPdf.save(outPdfFile)
							if (originalPdf != null) originalPdf.close()
								if (outPdf != null) outPdf.close()
					}
			} 	
	}
}



//Convert our JSON to JAVA

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
