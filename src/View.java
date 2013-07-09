import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: lizhi
 * Date: 13-7-9
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class View extends JFrame{
	private JTree tree;
	private int totalLineCount = 0;
	private int codeCount = 0;
	private int commentCount = 0;
	private int blankCount = 0;
	public View(){
		super("jLineCounter");
		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("menu");
		JMenuItem item=new JMenuItem("item");
		setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(item);
		UIManager.LookAndFeelInfo[] lookAndFeels= UIManager.getInstalledLookAndFeels();
		menu=new JMenu("lookAndFeels");
		menuBar.add(menu);
		for (int i=0;i<lookAndFeels.length;i++){
			item = new JMenuItem(lookAndFeels[i].getClassName());
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JMenuItem item1 = (JMenuItem) e.getSource();
					try {
						UIManager.setLookAndFeel(item1.getText());
						SwingUtilities.updateComponentTreeUI(getContentPane());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			menu.add(item);
		}

		JToolBar toolBar = new JToolBar();
		toolBar.setEnabled(false);
		JButton btn=new JButton("btn");

		toolBar.add(btn);
		add(toolBar,BorderLayout.PAGE_START);

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		tree = new JTree();
		JScrollPane jp=new JScrollPane(tree);
		splitPane.add(jp,JSplitPane.LEFT);

		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				try{
					if(dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						java.util.List<File> list = (java.util.List<File>)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
						dofile(list);
						dtde.dropComplete(true);
					}else{
						dtde.rejectDrop();
					}
				}catch (Exception e){

				}
			}
		});
	}

	private void dofile(java.util.List<File> list){
		totalLineCount=codeCount=commentCount=blankCount=0;
		for (File file:list){
			countFiles(file);
		}
		JOptionPane.showMessageDialog(null, "total " + totalLineCount + " code " + codeCount + " commit " + commentCount + " blank " + blankCount);
	}

	private void countFiles(File file){
		if(file.isDirectory()){
			for (File f:file.listFiles()){
				countFiles(f);
			}
		}else{
			countFile(file);
		}

	}

	private void countFile(File file)
	{
		int fileTotalLineCount = 0;
		int fileCodeCount = 0;
		int fileCommentCount = 0;
		int fileBlankCount = 0;
		Boolean isInComment = false;
		InputStream fis;
		BufferedReader br;
		String line;
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				fileTotalLineCount++;
				if (line.trim().length() == 0){
					fileBlankCount++;
				}else{
					String trimmed = line.trim();
					if (isInComment)
						fileCommentCount++;
					if (trimmed.startsWith("/*")){
						isInComment = true;
						fileCommentCount++;
					}
					if (trimmed.endsWith("*/")){
						isInComment = false;
						continue;
					}
					if (isInComment) continue;
					if (trimmed.startsWith("//"))
						fileCommentCount++;
					else
						fileCodeCount++;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		br = null;
		fis = null;
		totalLineCount+=fileTotalLineCount;
		codeCount+=fileCodeCount;
		commentCount+=fileCommentCount;
		blankCount+=fileBlankCount;

	}
}
