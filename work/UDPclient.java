import java.io.*;
import java.net.*;
import java.util.*;


public class UDPclient
{
	public static void main(String args[]) throws IOException
	{
		Scanner scan = new Scanner(System.in);
		DatagramSocket soc = new DatagramSocket();                             //Creating sockets
		DatagramSocket soc2 = new DatagramSocket(5000);
		InetAddress ip = InetAddress.getLocalHost();
		
		while(true){
		System.out.println("\nChoices : \n\n1 - Infix to Prefix\n2 - Infix to Postfix\n3 - Prefix to Infix\n4 - Prefix to Postfix\n5 - Postfix to Infix\n6 - Postfix to Prefix\n7 - Exit"); 
		System.out.print("\nEnter choice: ");
		String choice=scan.nextLine(); 
		if(choice.equals(""+"7")) java.lang.System.exit(0);
		else{
			System.out.print("Enter the expression: ");
			String str = scan.nextLine(); 
			byte[] data=str.getBytes();                                         //Creating byte arrays of input strings
			byte[] ch = choice.getBytes();
			DatagramPacket DPch = new DatagramPacket(ch,ch.length,ip,5001);
			soc.send(DPch);
			DatagramPacket DPsend = new DatagramPacket(data,data.length,ip,5001);
			soc.send(DPsend);                                                      //Sending the datagram packets
			System.out.println("\nSent to server");

			
			
			DatagramPacket DPrec = null;
			DatagramPacket DPst = null;
			byte[] receive=new byte[1000];                                     //Packets to receive data from server
			byte[] st = new byte[16];
			System.out.println("\nWaiting for server....");
			DPrec = new DatagramPacket(receive,receive.length);
			DPst = new DatagramPacket(st,st.length);
			soc2.receive(DPrec);
			soc2.receive(DPst);                                                //Receiving packets from the server
			System.out.println("Received");
			String out = new String(DPrec.getData(), 0, DPrec.getLength()); 
			String sta = new String(DPst.getData(),0,DPst.getLength());
			if(sta.equals(""+'1')){
				switch(choice){
				case ""+'3' :
				case ""+'5' : System.out.print("Infix of "+str+" is "+out);break; 
				case "" +'1':
				case ""+'6' : System.out.print("Prefix of "+str+" is "+out);break;
				case "" +'2':
				case ""+'4' : System.out.print("Postfix of "+str+" is "+out);      //Printing the outputs
				} 
			}
			else System.out.println(out);
			System.out.println("\nEnter '0' if you want to exit!");
			String a = scan.nextLine();
			if(a.equals("0"))java.lang.System.exit(0);                             //Enter anything other than '0' to continue
			else continue;
			}
		}
	}
}