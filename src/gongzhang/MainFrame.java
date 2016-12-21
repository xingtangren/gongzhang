package gongzhang;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainFrame extends JFrame {
	private JTextField sealPath;
	private JTextField excelPath;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rbtnExcel;
	private static MainFrame frame;
	JFileChooser fc = new JFileChooser(); 
	private JTextArea sealNameArea;
	private JButton selectExcelBtn;
	private JButton savePathBtn;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	public MainFrame() {
		setTitle("\u7535\u5B50\u5370\u7AE0\u81EA\u52A8\u751F\u6210\u5668");
		getContentPane().setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 700, 400);
		
		JLabel lblNewLabel = new JLabel("\u751F\u6210\u8DEF\u5F84\uFF1A");
		lblNewLabel.setBounds(36, 44, 66, 15);
		getContentPane().add(lblNewLabel);
		
		sealPath = new JTextField();
		sealPath.setBounds(118, 41, 456, 21);
		getContentPane().add(sealPath);
		sealPath.setColumns(10);
		
		savePathBtn = new JButton("\u6D4F\u89C8...");
		savePathBtn.setBounds(584, 40, 93, 23);
		savePathBtn.addActionListener(new SavePathButtonActionListener());
		getContentPane().add(savePathBtn);
		
		excelPath = new JTextField();
		excelPath.setColumns(10);
		excelPath.setBounds(118, 74, 456, 21);
		getContentPane().add(excelPath);
		
		selectExcelBtn = new JButton("\u6D4F\u89C8...");
		selectExcelBtn.setBounds(584, 73, 93, 23);
		selectExcelBtn.addActionListener(new SelectExcelButtonActionListener());
		getContentPane().add(selectExcelBtn);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(118, 338, 456, 14);
		progressBar.setVisible(false);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		getContentPane().add(progressBar);
		
		progressLabel = new JLabel("");
		progressLabel.setBounds(584, 337, 100, 15);
		progressLabel.setVisible(false);
		getContentPane().add(progressLabel);
		
		rbtnExcel = new JRadioButton("Excel\u8F93\u5165\uFF1A");
		rbtnExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sealNameArea.setEnabled(false);
				excelPath.setEnabled(true);
				selectExcelBtn.setEnabled(true);
			}
		});
		buttonGroup.add(rbtnExcel);
		rbtnExcel.setSelected(true);
		rbtnExcel.setBounds(17, 73, 98, 23);
		getContentPane().add(rbtnExcel);
		
		JRadioButton rbtnInput = new JRadioButton("\u624B\u52A8\u8F93\u5165\uFF1A");
		buttonGroup.add(rbtnInput);
		rbtnInput.setBounds(17, 105, 98, 23);
		getContentPane().add(rbtnInput);
		rbtnInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sealNameArea.setEnabled(true);
				excelPath.setEnabled(false);
				selectExcelBtn.setEnabled(false);
			}
		});
		
		sealNameArea = new JTextArea();
		sealNameArea.setEnabled(false);
		sealNameArea.setBounds(118, 105, 456, 172);
		getContentPane().add(sealNameArea);
		
		JButton genSealBtn = new JButton("\u751F\u6210");
		genSealBtn.addActionListener(new GenSealButtonActionListener());
		genSealBtn.setBounds(217, 294, 93, 23);
		getContentPane().add(genSealBtn);
		
		JButton closeWinBtn = new JButton("\u5173\u95ED");
		closeWinBtn.setBounds(359, 294, 93, 23);
		closeWinBtn.addActionListener(new CloseWinButtonActionListener());
		getContentPane().add(closeWinBtn);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 选择保存路径
	 * @author zuobin
	 *
	 */
	private class SavePathButtonActionListener implements ActionListener{  
        public void actionPerformed(ActionEvent e) {  
        	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
            int intRetVal = fc.showOpenDialog(frame); 
            if(intRetVal == JFileChooser.APPROVE_OPTION){ 
              sealPath.setText(fc.getSelectedFile().getPath()); 
            } 
        }     
    }  
	
	/**
	 * 选择Excel路径
	 * @author zuobin
	 *
	 */
	private class SelectExcelButtonActionListener implements ActionListener{  
		public void actionPerformed(ActionEvent e) {  
			fc.setFileFilter(new ExcelFilter());
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY); 
            int intRetVal = fc.showOpenDialog(frame); 
            if(intRetVal == JFileChooser.APPROVE_OPTION){ 
              excelPath.setText(fc.getSelectedFile().getPath()); 
            } 
		}     
	}  
	
	/**
	 * 关闭窗体
	 * @author zuobin
	 *
	 */
	private class CloseWinButtonActionListener implements ActionListener{  
		public void actionPerformed(ActionEvent e) {  
			progressBar.setVisible(false);
        	progressLabel.setVisible(false);
			frame.dispose();
		}     
	}  
		
	/**
	 * 生成按钮点击事件
	 * @author zuobin
	 *
	 */
	private class GenSealButtonActionListener implements ActionListener{  
		public void actionPerformed(ActionEvent e) {  
        	if(rbtnExcel.isSelected()){
        		progressBar.setVisible(true);
            	progressLabel.setVisible(true);
        		if("".equals(sealPath.getText())){
        			JOptionPane.showMessageDialog(frame, "保存路径不能为空！");
        			progressBar.setVisible(false);
                	progressLabel.setVisible(false);
        		}else if("".equals(excelPath.getText())){
        			progressBar.setVisible(false);
                	progressLabel.setVisible(false);
        			JOptionPane.showMessageDialog(frame, "Excel路径不能为空！");
        		}else{
        			YinZhang.generateSealByExcel(sealPath.getText(),excelPath.getText(),progressBar,progressLabel);
        		}
        	}else{
        		if("".equals(sealPath.getText())){
        			progressBar.setVisible(false);
                	progressLabel.setVisible(false);
        			JOptionPane.showMessageDialog(frame, "保存路径不能为空！");
        		}else if("".equals(sealNameArea.getText())){
        			progressBar.setVisible(false);
                	progressLabel.setVisible(false);
        			JOptionPane.showMessageDialog(frame, "印章名称输入框不能为空！");
        		}else{
        			progressBar.setVisible(true);
                	progressLabel.setVisible(true);
        			YinZhang.generateSealByInput(sealPath.getText(),sealNameArea.getText().split("\\n"),progressBar,progressLabel);
        		}
        	}
		}     
	}  
	
	class ExcelFilter extends javax.swing.filechooser.FileFilter{
        public boolean accept(File file)
        {
            return file.getName().toLowerCase().endsWith(".xlsx")||file.getName().toLowerCase().endsWith(".xls")||file.isDirectory();
        }
        public String getDescription()
        {
            return "Excel File";
        }
    }
}
