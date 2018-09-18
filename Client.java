import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class Client {
	public static void main(String[] args) throws Exception {
		ClientJFrame f = new ClientJFrame("Client");
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();
		Socket s =new Socket(ip,8000);
		f.getSocket(s);
		new ClientInput(s,f).start();
	}
}
class ClientJFrame extends JFrame {
	Socket s;
	ClientJFrame f = this;
	JTextArea jt = null;
	JTextArea jt1 = null;
	String msg = "";
	
	ClientJFrame(String name) {
		super(name);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		jt = new JTextArea();
		jt.setLineWrap(true);
		jt.setEditable(false);
		
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setBounds(0,0,500,300);
		c.add(jsp,BorderLayout.CENTER);
		
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBounds(0,300,500,80);
		jt1 = new JTextArea();
		jt1.setLineWrap(true);
		JButton jb = new JButton("send");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				msg = f.jt1.getText();
				dos.writeUTF(msg);
				jt.append("Me:"+msg+"\n");
				f.jt1.setText("");
				}catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		JPanel btjp = new JPanel();
		btjp.add(jb);
		btjp.setBounds(380,5,50,50);
		JScrollPane jsp1 = new JScrollPane(jt1);
		jsp1.setBounds(30,10,300,30);
		jp.add(jsp1);
		jp.add(btjp);
		c.add(jp);
		
		setResizable(false);
		setLayout(null);
		setBounds(300,300,500,380);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void getSocket(Socket s) {
		this.s = s;
	}
}
class ClientInput extends Thread {
	Socket s = null;
	ClientJFrame f = null;
	
	ClientInput(Socket s,ClientJFrame f) {
		this.s = s;
		this.f = f;
	}
	
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(s.getInputStream());
			while(true) {
				f.jt.append("Client:"+dis.readUTF()+"\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}