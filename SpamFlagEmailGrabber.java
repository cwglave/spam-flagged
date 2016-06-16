/*****************************************************************************************
*  Program Name	:	AOLSpamFlagGrabber.java
*  Author		:	Caleb Glave
*  Last Modified:	10-30-2012
*  Description	:	Simple program to parse the email addresses out of the attachments
*					sent via the AOL and Hotmail feedback loops, so that those flagging
*					our email as SPAM may be removed from the subscriber list easier.
*****************************************************************************************/
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.Writer;
//import java.io.OutputStreamWriter;
//import java.io.FileWriter;
import java.io.*;

public class SpamFlagEmailGrabber
{

	public static void main(String[] args)
	{
		String AOLMSG = "Email";//Begining of filename string for AOL email files
		String HOTMAILMSG = "complaint";//Begining of filename string for Hotmail email files
		String ROADRUNNER = "RoadRunner";//Begining of filename string for Roadrunner email files
		String SUBSCRIBERID = "X-MailPersonSubscriberID:";//String that comes before the subscriber ID in email header
		File directory = new File(System.getProperty("user.dir"));//points to the directory the user is currently in
		File files[] = directory.listFiles();//creates array of files from current directory
		String[] fileNames = new String[files.length];//array of the file names of the files
		String[] emails = new String[files.length];//array to store email subscriber IDs
		int i =0;
		for (File f : files)
		{
			if (f.getName().startsWith(AOLMSG)|| (f.getName().startsWith(HOTMAILMSG))||(f.getName().startsWith(ROADRUNNER)))//prevents program from reading other files that may be in the directory
			{
				StringBuilder sb = new StringBuilder();
				try
				{
					BufferedReader reader = new BufferedReader(new FileReader(f));
					while (reader.ready())
					{
						sb.append(reader.readLine());
					}
				}
				catch(IOException ex)
				{
					System.out.println("There was an error with Spam email: "+ f.getName());//exception code that gives file that caused exception
					ex.printStackTrace();
				}
				String fileText = sb.toString().replaceAll("[^\\x21-\\x7e]", "");//removes null chatacters, white space, and returns
				int XMailPerson = fileText.indexOf(SUBSCRIBERID) + 25;//finds index to start string parsing from
				Boolean insert = true;
				String hEmail = fileText.substring(XMailPerson, fileText.indexOf("X", XMailPerson));//parses the subscriber ID using the begining index and index of the next part of the header after the subscriber id
				for(int p=0; p<emails.length;p++)
				{
					if (emails[p] != null)//null check
					{
						if (emails[p].equals(hEmail))//checks if the subscriber ID is already in the array
						{
							insert = false;
						}
					}
				}
				if (insert)//if the subscriber ID doesn't already exist in the array this will write it to the array
				{
					fileNames[i] = f.getName();//writes file name to corresponding spot in array for troubleshooting
					emails[i] = hEmail;//write email to array
				}
			}

			i++;
	}
	try
	{
		BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
		for(int j=0; j<emails.length; j++)
		{
			if (emails[j] != null)//null check before it writes each string to the output file
			{
				out.write("" + emails[j]);//writes to output file
				out.newLine();//starts new line in output file

			}
		}

		out.close();//closes bufferwriter
	}
	catch(IOException iox)//catches exception thrown from the writer
	{

		System.out.println("Exception:  FileWriter");
	}
	System.out.println("DONE\nThe Results will be in the output.txt file.");//finished message
	}
}