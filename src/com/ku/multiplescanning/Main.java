/**The main class which starts the execution of multiple scanning.
 * This class is used to read inputs from user such as filename and 
 * number of scans and call corresponding multiple scanning class to
 * perform multiple scanning on the given input.
 */
package com.ku.multiplescanning;

import java.io.BufferedReader;

import java.io.FileReader;
import java.util.Scanner;

/**
 * @author chandra
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Parser parser=new Parser();
		int option = 0;
		//Prompting user to enter the input filename.
		System.out.println("Please enter the input file name:");
		Scanner scanner=new Scanner(System.in);
		String fileName=scanner.next();
		boolean validate=validateName(fileName);
		while(!validate){
			System.out.println("Please check if the fileName provided exists or not");
			System.out.println("1.Try once more\n2.Exit");
			option=scanner.nextInt();
			while(option !=1 && option !=2){
				System.out.println("Please select from any one of the following options");
				System.out.println("1.Try once more\n2.Exit");
				option=scanner.nextInt();
			}
				if(option == 1){
					System.out.println("Please enter the input file name:");
					fileName=scanner.next();
					validate=validateName(fileName);
				}
				else if(option == 2){
					System.out.println("Thank you!!!!");
					System.exit(0);
				}
		}
		System.out.println("Please enter the number of scans:");
		int noOfScans=scanner.nextInt();
		Table table=parser.parseInput(fileName);	
		MultipleScanning.scan(table,noOfScans);
	}

	/* This method is used to validate the input fileName
	*by trying to open the file and read the contents.
	*If file do not exist, catch expection and throw error.
	*/
	public static boolean validateName(String fileName) {
		boolean flag=true;
		try{
			BufferedReader br=new BufferedReader(new FileReader(fileName));
			br.close();
		}
		catch (Exception e) {
			flag=false;
		}
		return flag;
	}
}
