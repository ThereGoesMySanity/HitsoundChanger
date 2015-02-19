package Main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Toolkit;



public class HitsoundChanger extends JPanel implements ActionListener {
	private static final Path[] DEFAULTDIRECTORIES = {
		Paths.get("~/.local/share/Steam/SteamApps/common/Team Fortress 2/tf/custom/sounds/sound/ui/"),
		Paths.get("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Team Fortress 2\\tf\\custom\\sounds\\sound\\ui\\"),
		Paths.get("~/Library/Application Support/Steam/SteamApps/common/Team Fortress 2/tf/custom/sounds/sound/ui/")};
	private static Path[] DIRECTORIES = {
		Paths.get("~/.local/share/Steam/SteamApps/common/Team Fortress 2/tf/custom/sounds/sound/ui/"),
		Paths.get("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Team Fortress 2\\tf\\custom\\sounds\\sound\\ui\\"),
		Paths.get("~/Library/Application Support/Steam/SteamApps/common/Team Fortress 2/tf/custom/sounds/sound/ui/")};
	private static final int LINUX = 0;
	private static final int WINDOWS = 1;
	private static final int MAC = 2;
	private int os;
	
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	
	private JFileChooser fileChooser;
	
	private JMenuBar menuBar;
	
	private JMenu mnFile;
	
	private JMenuItem mntmAddHitsound;
	private JMenuItem mntmChangeTfInstallation;
	
	private ArrayList<File> hitsounds = new ArrayList<File>();
	private ArrayList<Clip> loadedHitsounds = new ArrayList<Clip>();

	private JScrollPane buttonPanel;
	
	private Path hitsoundDir = Paths.get(System.getProperty("user.home")).resolve("Hitsound");
	
	private JButton z;
	private JButton btnNewHitsound;
	
	private JPanel panel;
	private String chosen;
	private JLabel lblCurrentlySelected;
	
	
	/**
	 * Create the application.
	 */
	public HitsoundChanger() {
		super(new BorderLayout());
		if(System.getProperty("os.name").startsWith("Windows")){
			os = WINDOWS;
		}else if(System.getProperty("os.name").startsWith("Mac")){
			os = MAC;
		}else if(System.getProperty("os.name").startsWith("Linux")){
			os = LINUX;
		}else{
			infoBox("What OS are you trying to run this on and why?", "????????");
			System.exit(1);
		}
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(HitsoundChanger.class.getResource("/Resources/Killicon_sniper_rifle.png")));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		if(!Files.isDirectory(hitsoundDir)){
			hitsoundDir.toFile().mkdirs();
		}
		if(Files.exists(hitsoundDir.resolve("loc.txt"))){
			try (BufferedReader br = new BufferedReader(new FileReader(hitsoundDir.resolve("loc.txt").toString()))){
				DIRECTORIES[os] = Paths.get(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} 
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
		buttonPanel.setBounds(0, 0, 444, 198);
		frame.getContentPane().add(buttonPanel);
		buttonPanel.setLayout(new ScrollPaneLayout());
		buttonPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel = new JPanel();
		buttonPanel.setViewportView(panel);
		panel.setLayout(new GridLayout(panel.getComponentCount(), 1, 0, 0));
		btnNewHitsound = new JButton("Set as new hitsound");
		btnNewHitsound.setBounds(0, 214, 444, 36);
		btnNewHitsound.addActionListener(this);
		frame.getContentPane().add(btnNewHitsound);
		
		lblCurrentlySelected = new JLabel("Currently selected:");
		lblCurrentlySelected.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentlySelected.setBounds(0, 200, 444, 14);
		frame.getContentPane().add(lblCurrentlySelected);

		for(File hitsound : hitsounds){
			z = new JButton(hitsound.getName(), new ImageIcon("/HitsoundChanger/Resources/148890.gif"));
			z.addActionListener(this);
			z.setBounds(0, 48*(buttonPanel.getComponentCount()-3), buttonPanel.getWidth(), 48);
			z.setIcon(new ImageIcon(HitsoundChanger.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
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
		
		mntmChangeTfInstallation = new JMenuItem("Change TF2 installation location...");
		mntmChangeTfInstallation.addActionListener(this);
		mnFile.add(mntmChangeTfInstallation);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void infoBox(String infoMessage, String titleBar){
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmAddHitsound){									//Add new hitsound
			int returnVal = fileChooser.showOpenDialog(HitsoundChanger.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File[] newHitsounds = fileChooser.getSelectedFiles();
				for(File newHitsound : newHitsounds){
					if(!newHitsound.getName().split("\\.")[newHitsound.getName().split("\\.").length-1].equals("wav")){
						infoBox(newHitsound.getName() + " is not a valid .wav file.", "Error");
						break;
					}
					try {
						Files.copy(newHitsound.toPath(), hitsoundDir.resolve(newHitsound.getName()), StandardCopyOption.REPLACE_EXISTING);
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
					z.setIcon(new ImageIcon(HitsoundChanger.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
					panel.add(z);
					panel.setLayout(new GridLayout(panel.getComponentCount(), 1, 0, 0));
				}
				panel.validate();
			}
		}else if(e.getSource() == mntmChangeTfInstallation){						//Change install dir
			String newPath = JOptionPane.showInputDialog("Enter new path. Current path: \n" 
														+ DIRECTORIES[os].toString() 
														+ "\nTo reset, type default.");
			if (newPath != null&&(newPath.contains(":")||newPath.contains("/")||newPath.equals("default"))) {
				if (newPath.equals("default")) {
					DIRECTORIES[os] = DEFAULTDIRECTORIES[os];
					try {
						Files.deleteIfExists(hitsoundDir.resolve("loc.txt"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					DIRECTORIES[os] = Paths.get(newPath);
					if (!Files.isDirectory(DIRECTORIES[os])) {
						DIRECTORIES[os].toFile().mkdirs();
					}
					try (PrintWriter writer = new PrintWriter(hitsoundDir.resolve("loc.txt").toString(), "UTF-8");) {
						writer.println(newPath);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}else if(e.getSource() == btnNewHitsound && chosen != null){			//Set new hitsound
			if(!Files.isDirectory(DIRECTORIES[os])){
				DIRECTORIES[os].toFile().mkdirs();
			}
			try {
				Files.copy(hitsoundDir.resolve(chosen), DIRECTORIES[os].resolve("hitsound.wav"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() instanceof JButton){							//Use one of the hitsounds
			try {
				chosen = ((JButton) e.getSource()).getText();
				lblCurrentlySelected.setText("Currently selected: " + chosen);
				for(File item : hitsounds){
					if(item.getName().equals(chosen)){
						Clip currentClip = loadedHitsounds.get(hitsounds.indexOf(item));
						if(currentClip.isActive()){
							break;
						}
						currentClip.start();
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
