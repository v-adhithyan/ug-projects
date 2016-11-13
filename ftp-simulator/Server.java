import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;


public class Server
{
	JFrame frame;
	JLabel label;
	static String commandrecv = "";
	public String[] commands = {"RETR","STOR","STOU","APPE","REST","RNFR","RNTO","ABOR","DELE","RMD","MKD","PWD","LIST","NLST","SITE","SYST","STAT","HELP","NOOP"};
	static JTextField request;
	
	static JTextField tf ;
	Server()
	{
		frame = new JFrame("FTP Server");
		
		tf = new JTextField();
		tf.setEditable(false);
		tf.setBackground(Color.RED);
		tf.setText("Waiting for connection from client........");
		
		label = new JLabel("RFC 959-FTP Server");
		
		JPanel panel1 = new JPanel();
		panel1.add(tf);
		
		request = new JTextField(50);
		request.setEditable(false);	
		request.setBackground(Color.GRAY);	
		request.setForeground(Color.CYAN);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(panel1,BorderLayout.NORTH);
		panel2.add(label,BorderLayout.SOUTH);
		panel2.add(request,BorderLayout.CENTER);
		panel2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		frame.setContentPane(panel2);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(300,150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) throws Exception
	{
		
		Server s = new Server();
		SwingUtilities.invokeLater(new Runnable() {
		public void run(){
		try
		{
		ServerSocket sock = new ServerSocket(3592);
		Socket client;
		
		//new Thread().start();
			client = sock.accept();
			tf.setBackground(Color.GREEN);
			tf.setText("Connected:   "+client);
			DataInputStream dis = new DataInputStream(client.getInputStream());
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			String cmd = dis.readUTF();
			int no = Integer.parseInt(cmd);
			if(no==1)
				new GuiWork(dis,dos,request,no).execute();	
			if(no==2)
				new GuiWork(dis,dos,request,no).execute();	
			if(no==3)
				new GuiWork(dis,dos,request,no).execute();	
			if(no==5)
				new GuiWork(dis,dos,request,no).execute();		
			
			if(no==8)
					new GuiWork(dis,dos,request,no).execute();					
			if(no==9)
					new GuiWork(dis,dos,request,no).execute();	
			if(no==4 || no==10)
					new GuiWork(dis,dos,request,no).execute();	


		}
		catch(IOException exce){}
		}
		});
		}

}

class GuiWork extends SwingWorker<Integer, Integer> {
	DataInputStream dis;DataOutputStream dos;Socket sk;JTextField request;int no;String commands;

  public GuiWork(DataInputStream dis,DataOutputStream dos,JTextField request,int n) {
  this.dis=dis;
  this.dos=dos;
  //this.sk=sk;
  this.request=request;
  this.no=n;
  }

  @Override
  protected Integer doInBackground() throws Exception {
    System.out.println( "GuiWorker.doInBackground" );
	
		if(no==1)
			{
				
				String str = dis.readUTF();
				request.setText("Command received is:"+str);
				String myPath=dis.readUTF();
				ServerSocket sersoc = new ServerSocket(33592);
				Socket ftp = sersoc.accept();
				File myFile = new File(myPath);
				int count;
				byte[] buffer = new byte[(int)myFile.length()];
				dos.writeLong(myFile.length());
				OutputStream out = ftp.getOutputStream();
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(myFile));
				in.read(buffer,0,buffer.length);
					out.write(buffer, 0, buffer.length);
     				out.flush();
				//dis.flush();
				dos.flush();
				sersoc.close();

			}
			if(no==2)
			{
				String str = dis.readUTF();
				request.setText("Command received is:"+str);
				String f = JOptionPane.showInputDialog("Enter file name:");
				dos.writeUTF(f);
				//String name=f.substring(0,f.lastIndexOf("."))+"Temp"+f.substring(f.lastIndexOf("."),f.length());
				Socket ftp = new Socket("localhost",33592);
				long L=dis.readLong();
				int b = (int)L;
				byte[] buffer = new byte[b];
				int count;
				InputStream in = ftp.getInputStream();
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream out = new BufferedOutputStream(fos);
				int bytesRead = in.read(buffer, 0, buffer.length);
    			out.write(buffer, 0, buffer.length);
				out.close();
				System.out.println("Transferred successfully");
				//dis.flush();
				dos.flush();
				fos.close();
				ftp.close();	

			}
			if(no==3)
			{
				String str = dis.readUTF();
				request.setText("Command received is:"+str);
				String f = JOptionPane.showInputDialog("Enter file name to be transferred from client:");
				dos.writeUTF(f);
				String name=JOptionPane.showInputDialog("Enter new file name:");
				//String name=f.substring(0,f.lastIndexOf("."))+"Temp"+f.substring(f.lastIndexOf("."),f.length());
					Socket ftp = new Socket("localhost",33592);
				long L=dis.readLong();
				int b = (int)L;
				byte[] buffer = new byte[b];
				int count;
				InputStream in = ftp.getInputStream();
				FileOutputStream fos = new FileOutputStream(name);
				BufferedOutputStream out = new BufferedOutputStream(fos);
				int bytesRead = in.read(buffer, 0, buffer.length);
    			out.write(buffer, 0, buffer.length);
				out.close();
				System.out.println("Transferred successfully");
				//dis.flush();
				dos.flush();
				fos.close();
				ftp.close();
			}
			if(no==5)
			{
				String str = dis.readUTF();
				request.setText("Command received is:"+str);
				String fname = dis.readUTF();
				File file = new File(fname);
				String stri ="";
				if(file.exists())
				{
					if(file.delete())
						stri = fname+" deleted successfully";
					else
						stri = fname+" deleted successfully";
				}
				else
						stri = fname+" not found in server";
//dis.flush();
				dos.flush();
				dos.writeUTF(stri);

			}
				
			if(no==8)
			{
				String str = dis.readUTF();
				request.setText("Command received is:"+str);
				String dir = new java.io.File( "." ).getCanonicalPath();
        			dos.writeUTF("Current working directory: "+dir);
				//dis.flush();
				dos.flush();
			}
			if(no==9)
			{
				int count=0;
				String str = dis.readUTF();
				String send ="";
				request.setText("Command received is:"+str);
				File dir = new File(".");
				File[] filesList = dir.listFiles();
				send+=("Contents of current directory: "+"\n");
				for (File file : filesList) {
    			if (file.isFile()) 
				{
					System.out.println(file.getName());
						send+=file.getName()+"\n";
	//dis.flush();
				dos.flush();
   			 	}}
				dos.writeUTF(send);

			}
			if(no==10 || no==4)
			{
				request.setText("Command received is:"+dis.readUTF());
				//JOptionPane.showMessageDialog(frame,"Disconnecting from client and exiting..."); 
				System.exit(0);
			}		
    return 0;
  }
  @Override
  protected void done() {
    System.out.println("done");
  }

}


