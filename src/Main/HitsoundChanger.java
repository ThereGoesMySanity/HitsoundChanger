package Main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class HitsoundChanger implements ActionListener {

	private JFrame frame;
	private JFrame fileDialog;
	private JFileChooser fileChooser;
	private JMenuItem mntmAddHitsound;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HitsoundChanger window = new HitsoundChanger();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HitsoundChanger() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Hitsound Changer");
		
		fileChooser = new JFileChooser();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmAddHitsound = new JMenuItem("Add hitsound...");
		mntmAddHitsound.addActionListener(this);
		mnFile.add(mntmAddHitsound);
		
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmAddHitsound){
			
		}
	}
}
