package Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;



public class HitsoundChanger extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JFileChooser fileChooser;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmAddHitsound;

	private ArrayList<File> hitsounds = new ArrayList<File>();
	private ArrayList<Clip> loadedHitsounds = new ArrayList<Clip>();

	private JScrollPane buttonPanel;
	Path hitsoundDir = Paths.get(System.getProperty("user.home")).resolve("Hitsound");
	private JButton z;
	private JPanel panel;
	/**
	 * Create the application.
	 */
	public HitsoundChanger() {
		super(new BorderLayout());
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		if(!Files.isDirectory(hitsoundDir)){
			hitsoundDir.toFile().mkdirs();
		}

		for(File hitsound : hitsoundDir.toFile().listFiles()){
			String[] find = hitsound.getName().split("\\.");
			if(find[find.length-1].equals("wav") ){
				hitsounds.add(hitsound);
				try {
					Clip y = AudioSystem.getClip();
					y.open(AudioSystem.getAudioInputStream(hitsound));
					loadedHitsounds.add(y);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}


		buttonPanel = new JScrollPane();
		buttonPanel.setBounds(0, 0, 444, 250);
		frame.getContentPane().add(buttonPanel);
		buttonPanel.setLayout(new ScrollPaneLayout());
		buttonPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel = new JPanel();
		buttonPanel.setViewportView(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		for(File hitsound : hitsounds){
			z = new JButton(hitsound.getName(), new ImageIcon("/HitsoundChanger/Resources/148890.gif"));
			z.addActionListener(this);
			z.setBounds(0, 48*(buttonPanel.getComponentCount()-3), buttonPanel.getWidth(), 48);
			panel.add(z);
		}

		frame.setTitle("Hitsound Changer");

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmAddHitsound = new JMenuItem("Add hitsound...");
		mntmAddHitsound.addActionListener(this);
		mnFile.add(mntmAddHitsound);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void infoBox(String infoMessage, String titleBar){
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmAddHitsound){
			int returnVal = fileChooser.showOpenDialog(HitsoundChanger.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File[] newHitsounds = fileChooser.getSelectedFiles();
				for(File newHitsound : newHitsounds){
					if(!newHitsound.getName().split("\\.")[newHitsound.getName().split("\\.").length-1].equals("wav")){
						infoBox(newHitsound.getName() + " is not a valid .wav file.", "Error");
						break;
					}
					try {
						Files.copy(newHitsound.toPath(), hitsoundDir.resolve(newHitsound.getName()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					hitsounds.add(newHitsound);
					try {
						Clip y = AudioSystem.getClip();
						y.open(AudioSystem.getAudioInputStream(newHitsound));
						loadedHitsounds.add(y);
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					z = new JButton(newHitsound.getName(), new ImageIcon("/HitsoundChanger/Resources/148890.gif"));
					z.addActionListener(this);
					z.setBounds(0, 16*buttonPanel.getComponentCount(), buttonPanel.getWidth(), 16);
					panel.add(z);
				}
				panel.validate();
			}
		}else if(e.getSource() instanceof JButton){
			try {
				String chosen = ((JButton) e.getSource()).getText();
				for(File item : hitsounds){
					if(item.getName().equals(chosen)){
						Clip currentClip = loadedHitsounds.get(hitsounds.indexOf(item));
						if(currentClip.isRunning()){
							currentClip.stop();
						}
						currentClip.start();
						while(currentClip.isRunning()){
							continue;
						}
						loadedHitsounds.get(hitsounds.indexOf(item)).setFramePosition(0);
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
