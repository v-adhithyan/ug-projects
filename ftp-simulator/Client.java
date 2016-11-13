import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Client  
{
	static JFrame frame;
	JLabel label;
	static String c;

	static JTextField tf ;
	static String sendcommand = "";	
	static JTextField Command ;
	static JPanel panel2,panel3;
	static String[] commands = {"RETR","STOR","STOU","ABOR","DELE","RMD","MKD","PWD","LIST","NOOP","SYST"};
	static JTextArea display;
	static JTextArea response;
	static JButton sendbut;
	Client()
	{
		frame = new JFrame("FTP Client");
		
		tf = new JTextField();
		tf.setEditable(false);
		tf.setBackground(Color.GREEN
		);
		tf.setText("Connecting to client....");
		
		label = new JLabel("Response from Server");
		
		JPanel panel1 = new JPanel();
		panel1.add(tf);
		
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(panel1,BorderLayout.NORTH);
		panel2.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		Command = new JTextField();
		Command.setEditable(false);
		Command.setForeground(Color.CYAN);
		Command.setBackground(Color.GRAY);

		panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
	
		display = new JTextArea();
		display.setEditable(false);
		display.setForeground(Color.RED);
		display.setText("Service Commands:\n"+"1.RETR\n"+"2.STOR\n"+"3.STOU\n"+"4.ABOR\n"+"5.DELE\n"+"6.RMD\n"+"7.MKD\n"+"8.PWD\n"+"9.LIST\n"+"10.NOOP\n"+"11.SYST");
		
		response = new JTextArea();
		response.setBackground(Color.ORANGE);
					
		panel3.add(display,BorderLayout.NORTH);
		panel3.add(label,BorderLayout.CENTER);
		
		panel3.add(response,BorderLayout.SOUTH);
		
		panel2.add(Command,BorderLayout.CENTER);
		panel2.add(panel3,BorderLayout.SOUTH);

		
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	static void setCommand(String st)
	{
		c = st;
	}
	static String getCommand()
	{
		return c;
	}

	public static void main(String[] args)throws IOException
	{
		SwingUtilities.invokeLater(new Runnable() {
		public void run(){
		try{
		Client c = new Client();
		//final GuiWorker gui;
		JButton sendbut = new JButton("Enter Command");
		//JButton closestream = new JButton("Stop Streaming");
		JMenuBar me = new JMenuBar();
		me.add(sendbut);
		//me.add(closestream);
		frame.setLayout(new BorderLayout());
		frame.add(me,BorderLayout.NORTH);
		frame.add(panel2,BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);

		new Thread().start();
		final Socket sock = new Socket("localhost",3592);
		tf.setText("Connected:    "+sock);

		final DataInputStream dis = new DataInputStream(sock.getInputStream());
		final DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
	
	
		sendbut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{	String cmd = JOptionPane.showInputDialog("Enter a command:");
					setCommand(cmd);
					int n = Integer.parseInt(getCommand());
					dos.writeUTF(n+"");
					String command=commands[n-1];
					new GuiWorker(dis,dos,sock,Command,response,n,command,frame).execute();
				}
				catch(IOException exce){}
			}
						
		});
		
			
}
	catch(IOException exec){}
	}	
	});
	}

}

class GuiWorker extends SwingWorker<Integer, Integer> {

  /*
  * This should just create a frame that will hold a progress bar until the
  * work is done. Once done, it should remove the progress bar from the dialog
  * and add a label saying the task complete.
  */
	DataInputStream dis;DataOutputStream dos;Socket sk;JTextField Command;JTextArea response;int n;String commands;
	JFrame fr;

  public GuiWorker(DataInputStream dis,DataOutputStream dos,Socket sk,JTextField Command,JTextArea response,int n,String command,JFrame fr) {
  this.dis=dis;
  this.dos=dos;
  this.sk=sk;
  this.Command=Command;
  this.response=response;
  this.n=n;
  this.commands=command;
  this.fr=fr;

    
  }

  @Override
  protected Integer doInBackground() throws Exception {
    System.out.println( "GuiWorker.doInBackground" );
	
			Command.setText("");
			response.setText("");
			Command.setText("Requested command is:"+commands);

			if(n==1)
			{
			
				String f = JOptionPane.showInputDialog("Enter file name to be transferred from server:");
				dos.writeUTF(commands);
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
			if(n==2)
			{
				dos.writeUTF(commands);
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
			if(n==3)
			{
				dos.writeUTF(commands);
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
			if(n==4 || n==10 )
			{
				dos.writeUTF(commands);
				JOptionPane.showMessageDialog(fr,"Disconnecting from server and exiting...");
				//dis.flush();
				dos.flush();
				System.exit(0);
			}
			if(n==5)
			{
				dos.writeUTF(commands);
				String fname = JOptionPane.showInputDialog("Enter a filename to be deleted:");
				dos.writeUTF(fname);
				String res=dis.readUTF();
				response.setText(res);
				//dis.flush();
				dos.flush();
			}
			if(n==6)					
			{
				//dos.writeUTF(commands);
				String name = JOptionPane.showInputDialog("Enter the name of the directory:");
				//String dir = new java.io.File( "." ).getCanonicalPath()+"/"+name;
				File f = new File(name);
				boolean bool = f.delete();
				if(bool)
					response.setText("Directory "+name+" deleted Successfully");
				else
					response.setText("Directory "+name+"  not deleted");
			}

			if(n==7)					
			{
				//dos.writeUTF(commands);
				String name = JOptionPane.showInputDialog("Enter the name of the directory:");
				String dir = new java.io.File( "." ).getCanonicalPath()+"/"+name;
				File f = new File(dir);
				boolean bool = f.mkdir();
				if(bool)
					response.setText("Directory "+dir+" Created Successfully");
				else
					response.setText("Directory "+dir+"  not created");
			}

			if(n==8)
			{
				dos.writeUTF(commands);
				String str=dis.readUTF();
				response.setText(str);
				//dis.flush();
				dos.flush();

			}
			if(n==9)
			{
				dos.writeUTF(commands);
				String str = dis.readUTF();
				//response.setText(str);
				JOptionPane.showMessageDialog(fr,str);
				//dis.flush();
				dos.flush();	
			}
			if(n==11)
			{
				String os = System.getProperty("os.name");
				response.setText("System type:"+os);
			}

			
			
    //Thread.sleep(1000);
    return 0;
  }

  @Override
  protected void done() {
    System.out.println("done");
    }

}