
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;


public class MenuBar extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private String copyCut;
	
	private final String icon = "img/fireball16.png",exitQeustion = "Would you like to save work before exit?";
	
	private JMenu file,edit,help;
	
	private JFileChooser fileChooser;
	
	private File selectedFile;
	
	private JTextArea textArea;
	
	private JMenuItem open,save,exit,copy,paste,cut,about;
	
	
	public MenuBar(JTextArea parenttextArea){
		
		textArea = parenttextArea;
		ImageIcon fire_icon=null;
		try{
			 fire_icon = new ImageIcon(getClass().getResource(icon));
		}catch(Exception e){
			
		}
		fileChooser = new JFileChooser();
		
		open = new JMenuItem("Open",fire_icon);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		
		save = new JMenuItem("Save",fire_icon);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		
		exit = new JMenuItem("Exit",fire_icon);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitPerformed();
			}
		});
		
		copy = new JMenuItem("Copy",fire_icon);
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
				/*
				String selected = textArea.getSelectedText();
				if(selected!=null && selected.length()!=0){
					copyCut = selected; 
				}
				*/
			}
		});
		
		paste = new JMenuItem("Paste",fire_icon);
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
				/*
				if(copyCut != null && copyCut.length()!=0)
					textArea.insert(copyCut,textArea.getCaretPosition());
				*/
			}
		});
		
		
		cut = new JMenuItem("Cut",fire_icon);
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
				// TODO Auto-generated method stub
				/*
				String selected =textArea.getSelectedText();
				if(selected!=null && selected.length()!=0){
					copyCut = selected;
					textArea.replaceSelection("");
				}
				*/
			}
		});
		
		about = new JMenuItem("About",fire_icon);
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = new JOptionPane();
				pane.setMessage("Notepad-- text redactor version -0.1");
				JDialog dialog = pane.createDialog("About");
				dialog.setVisible(true);
			}
		});
		
		
		file = new JMenu("File");
		edit = new JMenu("Edit");
		help = new JMenu("Help");
		
		file.add(open);
		file.add(save);
		file.add(exit);
		
		edit.add(copy);
		edit.add(paste);
		edit.add(cut);
		
		help.add(about);
		
		add(file);
		add(edit);
		add(help);
		
		
		
	}
	
	private void clearTextArea(){textArea.setText("");}
	private int saveFile(){
		int result = fileChooser.showSaveDialog(null);
		if(result==JFileChooser.APPROVE_OPTION){
			selectedFile=fileChooser.getSelectedFile();
			try {
				if(!selectedFile.exists()){
					if(!selectedFile.createNewFile()){
						JOptionPane.showMessageDialog(null,"Could Not Creat A New File, Abort!","File Creation Error",JOptionPane.ERROR_MESSAGE);
						return JFileChooser.CANCEL_OPTION;
					}
				}
				if(selectedFile.canWrite()){
						FileWriter fr = new FileWriter(selectedFile);
						BufferedWriter bw = new BufferedWriter(fr);
						String line = textArea.getText();
						bw.write(line);
						bw.close();
						fr.close();
				}else{
					JOptionPane.showMessageDialog(null,"Could Not Save The File","File Save Error",JOptionPane.ERROR_MESSAGE);
					return JFileChooser.CANCEL_OPTION;
				}
				return JFileChooser.APPROVE_OPTION;
			} catch (IOException e1) {
				e1.printStackTrace();
				return JFileChooser.CANCEL_OPTION;
			}
		}
		return JFileChooser.CANCEL_OPTION;
	}
	private void openFile(){
		if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			selectedFile = fileChooser.getSelectedFile();
			if(selectedFile.canRead()){
				try {
					clearTextArea();
					FileReader fr = new FileReader(selectedFile);
					BufferedReader br = new BufferedReader(fr);
					String line = br.readLine();
					while(line!=null){
						textArea.append(line+"\n");
						line = br.readLine();
					}
					br.close();
					fr.close();
				} catch (Exception e1) {
					clearTextArea();
				}
			}else{
				JOptionPane.showMessageDialog(null,"Could Not Open A File","File Open Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public void exitPerformed(){
		int result =  JOptionPane.showConfirmDialog(null, exitQeustion);
		if(result==JOptionPane.OK_OPTION){
			if(saveFile()==JFileChooser.APPROVE_OPTION)
				closeApplication();
		}else if(result==JOptionPane.NO_OPTION){
			closeApplication();
		}
	}
	private void closeApplication(){
		System.exit(ABORT);
		
	}

}
