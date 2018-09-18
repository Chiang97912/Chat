import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class Server {
	public static void main(String[] args) throws Exception {
		ServerJFrame f = new ServerJFrame("Server");
		ServerSocket ss = new ServerSocket(8000);
		Socket s = ss.accept();
		f.getSocket(s);
		new ServerInput(s,f).start();
	}
}
class ServerJFrame extends JFrame {
	Socket s;
	JTextArea jt = null;
	JTextArea jt1 = null;
	ServerJFrame f = this;
	String msg = "";
	
	ServerJFrame(String name) {
		super(name);
		Container c = getContentPane();
		jt = new JTextArea();
		jt.setLineWrap(true);
		jt.setEditable(false);
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setBounds(0,0,500,300);
		c.add(jsp);
		
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
				msg = jt1.getText();
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
class ServerInput extends Thread {
	Socket s = null;
	ServerJFrame f = null;
	
	ServerInput(Socket s,ServerJFrame f) {
		this.s = s;
		this.f = f;
	}
	
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(s.getInputStream());
			while(true) {
				f.jt.append("Server:"+dis.readUTF()+"\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}