package com.ku.multiplescanning;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {

public Table parseInput(String fileName){
	BufferedReader br=null;
	Table returnTable=null;
	try{
		br=new BufferedReader(new FileReader(fileName));
		returnTable=readFile(br,fileName.replace(".d",""));
	}
	catch (IOException e) {
		System.out.println("An exception occured while reading from file,Kindly retry");
	}
	finally{
		try {
			if(br!=null)
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return returnTable;
	}

private Table readFile(BufferedReader br, String fileName)
throws NullPointerException,InputMismatchException,NoSuchElementException{
	Scanner scan=new Scanner(br);
	//Defining the pattern which is to be used as delimiter in order to read the input
	scan.useDelimiter(Pattern.compile("(\\s*(!.*((\\r\\n)|(\\n)|(\\r)))\\s*)|(\\s+)"));
	ArrayList<String> attrNames=new ArrayList<String>();
	ArrayList<Double> attrValues;
	String decisionValue;
	ArrayList<Row> rows=new ArrayList<Row>();
	int columns=0;
	int attributes=0;
	getNextMatch(scan, "<");
	while(scan.hasNext()){
		String next=scan.next();
		if(">".equals(next)){
			break;	//As the end of line is reached
		}
		else if("a".equals(next) || "x".equals(next) || "d".equals(next)){
			columns+=1;
			if("a".equals(next))
				attributes++;
		}
		else{
			throw new InputMismatchException("Token"+next+"is invalid");
		}
	}
	
	getNextMatch(scan,"[");
	for(int i=0;i<columns;i++){
		String attrName=scan.next();
		if("]".equals(attrName)){
			throw new InputMismatchException("The input data is not in valid format ,failed to read attribute names");
		}
		else{
		attrNames.add(attrName);
		}
	}
	
	getNextMatch(scan,"]");
	String decisionName=attrNames.remove(attrNames.size()-1);
	for(int i=1;scan.hasNext();i++){
		Double[] longValues=new Double[attributes];
		for(int j=0;j<attributes;j++){
			longValues[j]=scan.nextDouble();
		}
		attrValues = new ArrayList<Double>(Arrays.asList(longValues));
		decisionValue=scan.next();
		Row row=new Row(decisionValue, i, attrValues, attrNames);
		rows.add(row);
	}
	scan.close();
	return new Table(rows,attrNames,fileName,decisionName);
	}
public static void getNextMatch(Scanner scanner, String token)
throws InputMismatchException, NoSuchElementException {
	String t = scanner.next();
	if (t.equals(token) == false) {
		throw new InputMismatchException("Expected token '" + token
		+ "', but found: " + t);
	}
	}

public static void writeToOutputFile(Table table) {

	try {
		//writing output to int file
		File file = new File(table.getTableName() + ".int");
		FileWriter fstreamWrite = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(fstreamWrite);
		int attributesize=table.getAttributeNames().size();
		for (String attribute : table.getAttributeNames()) {
			out.write(attribute);
			out.newLine();
			ArrayList<Double> listOfCutPoints = table.getCutpointsMap()
					.get(attribute);
			if (listOfCutPoints.isEmpty()) {
				out.write(table.getAttrMinMaxMap().get(attribute).getMin()
						+ ".."
						+ table.getAttrMinMaxMap().get(attribute).getMax());
				out.newLine();
			} else {
				for (int index = 0; index < listOfCutPoints.size(); index++) {
					if (index == 0) {
						out.write(table.getAttrMinMaxMap().get(attribute)
								.getMin()
								+ ".." + listOfCutPoints.get(index));
						out.newLine();
					}
					if (index == listOfCutPoints.size() - 1) {
						out.write(listOfCutPoints.get(index)
								+ ".."
								+ table.getAttrMinMaxMap().get(attribute)
										.getMax());
						out.newLine();
					} else {
						out.write(listOfCutPoints.get(index) + ".."
								+ listOfCutPoints.get(index + 1));
						out.newLine();
					}
				}
			}

		}
		out.close();

		//writing output to data file
		File dataFile = new File(table.getTableName() + ".data");
		FileWriter dataFstreamWrite = new FileWriter(dataFile);
		BufferedWriter dataOut = new BufferedWriter(dataFstreamWrite);
		dataOut.write("< ");
		for(int i=0;i<attributesize;i++){
			dataOut.write("a ");
		}
		dataOut.write("d ");
		dataOut.write(">");
		dataOut.newLine();
		dataOut.write("[ ");
		for (String attributeName : table.getAttributeNames()) {
			dataOut.write(attributeName + " ");
		}
		dataOut.write(table.getDecisionName());
		dataOut.write(" ]");
		dataOut.newLine();
		for (Row row : table.getRows()) {

			for (AttributeParams data : row.getAttrParams()) {
				dataOut.write(data.getInterval() + " ");
			}
			dataOut.write(row.getDecision());
			
			dataOut.newLine();
		}
		dataOut.close();

	} catch (Exception e) {
		e.printStackTrace();
	}
}

}
