import java.io.*;
import java.net.*;
import java.util.*;

public class UDPserver
{
	public static void main(String args[]) throws IOException
	{
		DatagramSocket soc = new DatagramSocket(5001);
		byte[] rec = new byte[1000];
		byte[] ch = new byte[16];
		String st=""+'1';
		
		while(true){
		DatagramPacket DPrec=null;
		DatagramPacket DPch=null;
		DPch=new DatagramPacket(ch,ch.length);
		System.out.println("\n\nWaiting for client...");
		soc.receive(DPch);
		DPrec=new DatagramPacket(rec,rec.length);
		soc.receive(DPrec);                                            //Receiving data from a client
		InetAddress ip = DPrec.getAddress();
		System.out.println("\tReceived:"+data(rec)+"\n\tchoice is "+data(ch)+"\n");   //data method returns data in the byte[] arrays
		String str = data(rec).toString();
		String choice =data(ch).toString();
		String send = new String();
		
		try{
			int chint = Integer.parseInt(choice)+1;
			String type = null;                                             //Getting the type of input sent by client ; chint is 'Choice int'
			switch(Math.round(chint/2)){
				case 1: type="Infix"; break;
				case 2: type="Prefix"; break;
				case 3: type="Postfix"; break;
				default: type="__";
				}

			if(chint/2==type(str)){		                                     //Checking if the client choice matches with the expression received 
			switch(choice){ 
				case "" + '1' : send = infix_to_prefix(str).toString(); break;
				case "" + '2' : send = infix_to_postfix(str).toString();break;
				case "" + '3' : send =  prefix_to_infix(str);break;
				case "" + '4' : send =  prefix_to_postfix(str);break;
				case "" + '5' : send =  postfix_to_infix(str);break;
				case "" + '6' : send =  postfix_to_prefix(str);break;
				default : System.out.println("\tInvalid choice...");send = "Invalid choice..";st=""+'0';      //Accessing the methods based on the client choice
				}
			}

			else{
			send = "Entered expression is not "+type;
			st = ""+'0';
			System.out.println("\tEntered expression is not "+type);         //if the choice doesn't match the expression, sending an error message and setting the conversion status to '0'
			}
		}
		catch(Exception e){                                                  //Try and catch to catch syntax errors in choice by client
			send = "Invalid choice.";
			st=""+'0';
		}

		byte[] Bsend = send.getBytes();                                 
		byte[] Bst = st.getBytes();
		DatagramPacket DPsend = new DatagramPacket(Bsend,Bsend.length,ip,5000);
		DatagramPacket DPst = new DatagramPacket(Bst,Bst.length,ip,5000);
		soc.send(DPsend);
		soc.send(DPst);                                                    //Sending data back to the client
		System.out.println("\tSent to client");
		}
	}
	
	static double type(String exp){                                        //Method to check whether the received expression is Infix or Prefix or Postfix 
		int i = 0;
		double v=1.0;
		while(i<exp.length()-1){
			if(isOperator(exp.charAt(i))&&isOperator(exp.charAt(i+1))) { v=0.0;break;}
			i++;
		}
		if(isOperator(exp. charAt(0))) v = 2.0;
		if(isOperator(exp. charAt(exp.length()-1))) v = 3.0;
		return v;
		}


	public static StringBuilder data(byte[] a)         //data method returns data in the byte[] arraysl
   	 {
       		 if (a == null)
           			 return null;
      	 	 StringBuilder ret = new StringBuilder();
       		 int i = 0;
       		 while (a[i] != 0)
        		{
           			 ret.append((char) a[i]);
           			 i++;
       		 }
      		 return ret;
   	 }


	static boolean isOperator(char x) {            //Method to check if the character is an operator or not
        	switch (x) {
          		  case '+':
           		  case '-':
            		  case '/':
            		  case '*':
               		  return true;
       		}
       		return false;
  		}


	 static int precedence(char c){                  //Method to check the precedence of the operator
       		 switch (c){
           			 case '+':
            			 case '-':
             			   return 1;
           			case '*':
            			case '/':
               			   return 2;
            			case '^':
               			   return 3;
       			}
        			return -1;
    	}


    static StringBuilder infix_to_prefix(String expression){      //Method to convert infix to prefix

        StringBuilder result = new StringBuilder();
        StringBuilder input = new StringBuilder(expression);
        input.reverse();
        Stack<Character> stack = new Stack<Character>();

        char [] charsExp = new String(input).toCharArray();
        for (int i = 0; i < charsExp.length; i++) {

            if (charsExp[i] == '(') {
                charsExp[i] = ')';
                i++;
            }
            else if (charsExp[i] == ')') {
                charsExp[i] = '(';
                i++;
            }
        }
        for (int i = 0; i <charsExp.length ; i++) {
            char c = charsExp[i];

            //check if char is operator or operand
            if(precedence(c)>0){
                while(stack.isEmpty()==false && precedence(stack.peek())>=precedence(c)){
                    result.append(stack.pop());
                }
                stack.push(c);
            }else if(c==')'){
                char x = stack.pop();
                while(x!='('){
                    result.append(x);
                    x = stack.pop();
                }
            }else if(c=='('){
                stack.push(c);
            }else{
                //character is neither operator nor "("
                result.append(c);
            }
        }

        for (int i = 0; i <=stack.size() ; i++) {
            result.append(stack.pop());
        }
        return result.reverse();
    }

    static String infix_to_postfix(String expression){              //Method to convert infix to postfix

        String result = "";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i <expression.length() ; i++) {
            char c = expression.charAt(i);

            //check if char is operator
            if(precedence(c)>0){
                while(stack.isEmpty()==false && precedence(stack.peek())>=precedence(c)){
                    result += stack.pop();
                }
                stack.push(c);
            }else if(c==')'){
                char x = stack.pop();
                while(x!='('){
                    result += x;
                    x = stack.pop();
                }
            }else if(c=='('){
                stack.push(c);
            }else{
                //character is neither operator nor ( 
                result += c;
            }
        }
        for (int i = 0; i <=stack.size() ; i++) {
            result += stack.pop();
        }
        return result;
    }

static String  prefix_to_infix(String expression){          //Method to convert postfix to infix

        Stack<String> stack = new Stack<>();
        for (int i = expression.length()-1; i >=0 ; i--) {
            char c = expression.charAt(i);

            if(isOperator(c)){
                String s1 = stack.pop();
                String s2 = stack.pop();
                String temp = s1+c+s2;
                stack.push(temp);
            }else{
                stack.push(c+"");
            }
        }

        String result=stack.pop();

        return result;
    }

static String prefix_to_postfix(String expression){        //Method to convert prefix to postfix

        Stack<String> stack = new Stack<String>();
        for (int i = expression.length()-1; i >=0 ; i--) {

            char c = expression.charAt(i);

            if(isOperator(c)){
                String s1 = stack.pop();
                String s2 = stack.pop();
                String temp = s1 + s2 + c;
                stack.push(temp);
            }else{
                stack.push(c+"");
            }
        }

        String result = stack.pop();
        return result;
    }

 static String postfix_to_infix(String expression){           //Method to convert postfix to infix

        Stack<String> stack = new Stack<>();
        for (int i = 0; i <expression.length() ; i++) {
            char c = expression.charAt(i);

            if(c=='*'||c=='/'||c=='^'||c=='+'||c=='-' ){
                String s1 = stack.pop();
                String s2 = stack.pop();
                String temp = s2+c+s1;
                stack.push(temp);
            }else{
                stack.push(c+"");
            }
        }

        String result=stack.pop();
        return result;
    }

 static String postfix_to_prefix(String expression){           //Method to convert postfix to prefix

        Stack<String> stack = new Stack<>();
        for (int i = 0; i <expression.length() ; i++) {

            char c = expression.charAt(i);

            if(isOperator(c)){
                String s1 = stack.pop();
                String s2 = stack.pop();
                String temp = c + s2 + s1;
                stack.push(temp);
            }else{
                stack.push(c+"");
            }
        }
        String result = stack.pop();
        return result;
    }

}
