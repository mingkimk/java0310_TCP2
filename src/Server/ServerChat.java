package Server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServerChat extends Thread{
	
	private Socket withClient=null; 
	private InputStream reMsg= null;
	private OutputStream sendMsg= null;
	private String id= null;
	
	
	ServerChat(Socket c){
		this.withClient=c;
		//streamSet();
	}
	
	@Override
	public void run() {
		//ServerChat(Socket c){ 에 있는  streamSet();을  멀티스래드 만들려고  override 만들어서 돌리기
		streamSet();
		receive();
		send();
		
	}
	
	private void receive() {
		// recevie() 만 별도로 쓰레드 처리
		new Thread(new Runnable() {
			
			@Override
			public void run() { // 독자적인 스레드 받는기능만
				// TODO Auto-generated method stub
				try {
					System.out.println("receive start!");
					while(true) {
					reMsg= withClient.getInputStream();
					byte[] reBuffer= new byte[100];
					reMsg.read(reBuffer);
					String msg= new String(reBuffer);
					msg= msg.trim();
					System.out.println("["+id+"]"+msg);
					}
				}catch(Exception e) {
				
				}
				
			}
		}).start();
	
		
	}
	
	private void send() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					System.out.println("send start!");
					Scanner in= new Scanner(System.in);
					while(true){
					String reMsg=in.nextLine();
					sendMsg= withClient.getOutputStream();
					sendMsg.write(reMsg.getBytes());
					}
				}catch(Exception e) {
					
				}
			}
		}).start();
		
		
	}

	private void streamSet() {
		try {
			reMsg= withClient.getInputStream();
			byte[] reBuffer= new byte[100];
			reMsg.read(reBuffer);
			id= new String(reBuffer);
			id= id.trim(); //  트림 공백 제거
			
			InetAddress c_ip= withClient.getInetAddress();
			//withClient 라는 소캣으로 getInetAddress 을 받아서 c_ip에 저장
			String ip=c_ip.getHostAddress();
			//c_ip를 string으로 변경
			System.out.println(id+"님 로그인 하셨습니다");
	
			String reMsg="정상접속 되었습니다";
			sendMsg= withClient.getOutputStream();
			sendMsg.write(reMsg.getBytes());
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
